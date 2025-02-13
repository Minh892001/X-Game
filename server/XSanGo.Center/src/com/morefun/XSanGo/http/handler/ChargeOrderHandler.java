/**
 * 
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import Ice.LocalException;

import com.XSanGo.Protocol.Callback_Center_charge;
import com.XSanGo.Protocol.CenterPrx;
import com.XSanGo.Protocol.CustomChargeParams;
import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.XsgAccountManager;
import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.db.Charge;
import com.morefun.XSanGo.db.ChargeDAO;
import com.morefun.XSanGo.db.stat.ChargeLog;
import com.morefun.XSanGo.db.stat.StatSimpleDAO;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.EncryptUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * @author sulingyun
 * 收到paycenter充值订单通知的处理类
 */
public class ChargeOrderHandler extends AbsHandler {
	private static String Key = "8956bb50bb2b77072e61d7ca8fc5e999";
	private static Logger log = LogManager.getLogger(ChargeOrderHandler.class);
	private static String SuccessRes = "Y";
	private static String ErrorRes = "N";
	private static ChargeDAO dao = ChargeDAO.getFromApplicationContext(LoginDatabase.instance().getAc());

	@Override
	protected void innerHandle(HttpExchange httpExchange) throws IOException {
		String res = ErrorRes;
		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		final int areaid = Integer.parseInt(paramsMap.get("areaid"));
		String tempAccount = paramsMap.get("account");
		final int money = Integer.parseInt(paramsMap.get("money"));
		final String orderid = paramsMap.get("orderid");
		final int pm_id = Integer.parseInt(paramsMap.get("pm_id"));
		final int cardType = Integer.parseInt(paramsMap.get("cardTypeCombine"));
		final int channel = Integer.parseInt(paramsMap.get("channel"));
		String role_id = paramsMap.get("role_id");
		String sign = paramsMap.get("sign");
		String expand = paramsMap.get("expand");
		String currency = this.getValue(paramsMap, "currency", "");

		String checkInput = TextUtil
				.format("account={0}&areaid={1}&cardTypeCombine={2}&channel={3}&money={4}&orderid={5}&pm_id={6}&role_id={7}{8}",
						tempAccount, areaid, cardType, channel, money, orderid, pm_id, role_id, Key);
		log.info(checkInput.substring(0, checkInput.length() - 5));
		log.info(expand);

		try {
			String checkOutput = EncryptUtil.bytesToHexString(EncryptUtil.getMD5(checkInput.getBytes()));
			if (sign.equals(checkOutput)) {
				// 重复订单检测，没有则直接入库，最后返回响应
				Charge order = dao.findById(orderid);
				if (order != null) {
					log.warn(TextUtil.format("Repeat charge order {0}.", orderid));
				} else {
					final String account = this.generateUserName(tempAccount);
					final Charge newOrder = new Charge(orderid, channel, account, areaid, role_id, money, pm_id,
							cardType, Calendar.getInstance().getTime(), Charge.State_Init, expand, currency);

					// 通知游戏服加元宝
					addChargeYuanbao(newOrder);

					final Account accountDB = XsgAccountManager.getInstance().findAccount(account);
					if (accountDB == null) {
						log.error(TextUtil.format("订单[{0}]引用了不存在的帐号[{1}]。", orderid, account));
						return;
					}

					final int chargeCount = accountDB.getChargeCount() + 1;
					accountDB.setChargeCount(chargeCount);
					XsgAccountManager.getInstance().updateAccount(accountDB);//充值次数加1再更新到数据库

					// 充值日志
					LoginDatabase.execute(new Runnable() {

						@Override
						public void run() {
							StatSimpleDAO.getForStat(LoginDatabase.instance().getAc()).save(
									new ChargeLog(orderid, account, money, areaid, DateUtil.isSameDay(accountDB
											.getCreateTime().getTime(), System.currentTimeMillis()) ? 1 : 0,
											chargeCount, channel, pm_id, cardType));
						}
					});
				}

				res = SuccessRes;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			this.sendResponse(httpExchange, res);
		}

	}

	private void saveOrder(final Charge newOrder) {
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				dao.save(newOrder);
			}
		});
	}

	private void addChargeYuanbao(final Charge newOrder) {
		log.info("save order and connect the gameserver to add yuanbao!");
		CenterPrx prx = IceEntry.instance().getCenterPrx(newOrder.getServerId(), true);
		if (prx == null) {
			log.error(TextUtil.format("Order {0} has no prx.", newOrder.getOrderId()));
			saveOrder(newOrder);
		} else {
			CustomChargeParams param = null;
			try {
				param = TextUtil.GSON.fromJson(newOrder.getParams(), CustomChargeParams.class);
			} catch (Exception e) {
				log.error("Parameter error at order " + newOrder.getOrderId());
				saveOrder(newOrder);
				return;
			}
			if (param == null) {
				log.warn("charge params:" + param);
				saveOrder(newOrder);
				return;
			}
			prx.begin_charge(newOrder.getRoleId(), newOrder.getCent() / 100, param, newOrder.getOrderId(),
					newOrder.getCurrency(), new Callback_Center_charge() {
						@Override
						public void response() {
							newOrder.setState(Charge.State_Sucess);
							newOrder.setCompleteTime(Calendar.getInstance().getTime());
							saveOrder(newOrder);
							// dao.attachDirty(newOrder);
						}

						@Override
						public void exception(LocalException ex) {
							log.error("Order notify error by LocalException.", ex);
							saveOrder(newOrder);
						}
					});
		}
	}
}
