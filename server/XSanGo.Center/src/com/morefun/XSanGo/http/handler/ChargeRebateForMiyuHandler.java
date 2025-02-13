/**
 * 
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.XSanGo.Protocol.Callback_Center_charge;
import com.XSanGo.Protocol.CenterPrx;
import com.XSanGo.Protocol.CustomChargeParams;
import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.db.Charge;
import com.morefun.XSanGo.db.ChargeDAO;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.EncryptUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

import Ice.LocalException;


/**
 * @author WFY
 *专门为米娱提供的充值返利接口
 *接口文档和返利规则参照'工程/doc/MiYuSdk/'
 */
public class ChargeRebateForMiyuHandler extends AbsHandler {
	private static String Key = "8956bb50bb2b77072e61d7ca8fc5e999";//secret米娱给定的密钥
	private static Logger log = LogManager.getLogger(ChargeRebateForMiyuHandler.class);
	private static String SuccessRes = "1";
	private static String ErrorRes = "0";
	private static ChargeDAO dao = ChargeDAO.getFromApplicationContext(LoginDatabase.instance().getAc());
	
	
	private class Result{
		public int result;
		public String msg = "";
	}
	
	/* (non-Javadoc)
	 * @see com.morefun.XSanGo.http.handler.AbsHandler#innerHandle(com.sun.net.httpserver.HttpExchange)
	 	参数名			 		数据类型		 是否必填	是否参与签名		 描述
	  	applyid 			String(32) 		必填 	是 			申请记录 ID
		gameid 				Int(11) 		必填 	是 			游戏 id
		userid 				String(32) 		必填 	是 			米娱账号唯一标识
		serverid 			String(32) 		必填 	是			 玩家所在区服 ID
		roleid 				String(32)	 	必填 	是			 玩家角色 ID
		total_amount 		Int(11) 		必填 	是 			订单总金额(返利日期一天的汇总)
		rebate_gamecoin 	Int(11) 		必填 	是 			要返的游戏币
		order_lists 		String(512) 	必填 	是 			返利换算的游戏币 urlencode
		applytime 			Int(11) 		必填 	是 			申请操作的时间
		rebatedate 			Int(11) 		必填 	是 			返利的日期(只针对哪天返利)
		sign 				String(32) 		必填 	否 			MD5签名结果(将上面的参数按照签名实例中的顺序排列后进行 md5)
		ext 				String(512) 	否 	否 			扩展参数(备用)
		以 json 形式返还通知结果
			成功示例： {"result":1,”msg”:”返利成功”}
			失败示例： {"result":0,"msg":"返利失败，额度不足"}
			result:表示接口调用结果,1 为成功,msg:拒绝的理由，当 result 为 0 时必须填入。
	 */
	@Override
	protected void innerHandle(HttpExchange httpExchange) throws IOException {
		String res = ErrorRes;
		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		String applyid = paramsMap.get("applyid");
		int gameid = Integer.parseInt(paramsMap.get("gameid"));
		String userid = paramsMap.get("userid");
		String serverid = paramsMap.get("serverid");
		String roleid = paramsMap.get("roleid");
		int total_amount = Integer.parseInt(paramsMap.get("total_amount"));
		int rebate_gamecoin = Integer.parseInt(paramsMap.get("rebate_gamecoin"));
		String order_lists = paramsMap.get("order_lists");
		int applytime = Integer.parseInt(paramsMap.get("applytime"));
		int rebatedate = Integer.parseInt(paramsMap.get("rebatedate"));
		String sign = paramsMap.get("sign");
		String ext = paramsMap.get("ext");

		String checkInput = TextUtil
				.format("applyid={0}&gameid={1}&userid={2}&serverid={3}&roleid={4}&total_amount={5}&rebate_gamecoin={6}&order_lists={7}&applytime={8}&rebatedate={9}:{10}",
						applyid, gameid, userid, serverid, roleid, total_amount, rebate_gamecoin, order_lists,applytime,rebatedate, Key);
		log.info(checkInput);
		
		String orderLists = URLDecoder.decode(order_lists);
//		try {
//			String checkOutput =new String(EncryptUtil.getMD5(checkInput.getBytes("UTF-8")));
//			if (sign.equals(checkOutput)) {//验证签名
//				// 重复订单检测，没有则直接入库，最后返回响应
//				Charge order = dao.findById(orderid);
//				if (order != null) {
//					log.warn(TextUtil.format("Repeat charge order {0}.", orderid));
//				} else {
//					final String account = this.generateUserName(userid);
//					final Charge newOrder = new Charge(orderid, channel, account, areaid, roleid, rebate_gamecoin, pm_id,
//							cardType, Calendar.getInstance().getTime(), Charge.State_Init, expand, currency);
//
//					CenterPrx prx = IceEntry.instance().getCenterPrx(serverid, true);
//					if (prx == null) {
//						log.error(TextUtil.format("ChargeRebateForMiyuHandler {0} has no prx.", order.getOrderId()));
//						return;
//					}
//
//					if (!TextUtil.isNullOrEmpty(roleId)) {
//						prx.begin_sendMailByRoleId(roleId.trim(), title, content, attach,senderName, new Callback_Center_sendMailByRoleId() {
//							@Override
//							public void exception(LocalException __ex) {
//								log.error("ChargeRebateForMiyuHandler notify error by LocalException.", __ex);
//							}
//
//							@Override
//							public void response() {
//								log.info("ChargeRebateForMiyuHandler suc.");
//							}
//
//							@Override
//							public void exception(UserException __ex) {
//								log.error("Order notify error by UserException.", __ex);
//							}
//						});
//					} 
//					else if (!TextUtil.isNullOrEmpty(roleName)) {
//						prx.begin_sendMail(roleName.trim(), title, content, attach,senderName, new Callback_Center_sendMail() {
//
//							@Override
//							public void exception(LocalException __ex) {
//								__cb.ice_exception(__ex);
//							}
//
//							@Override
//							public void response() {
//								__cb.ice_response();
//							}
//
//							@Override
//							public void exception(UserException __ex) {
//								__cb.ice_exception(__ex);
//							}
//						});
//					}
//					
//					// 通知游戏服加元宝
//					addChargeYuanbao(newOrder);

//					final Account accountDB = XsgAccountManager.getInstance().findAccount(account);
//					if (accountDB == null) {
//						log.error(TextUtil.format("订单[{0}]引用了不存在的帐号[{1}]。", orderid, account));
//						return;
//					}
//
//					final int chargeCount = accountDB.getChargeCount() + 1;
//					accountDB.setChargeCount(chargeCount);
//					XsgAccountManager.getInstance().updateAccount(accountDB);
//
//					// 充值日志
//					LoginDatabase.execute(new Runnable() {
//
//						@Override
//						public void run() {
//							StatSimpleDAO.getForStat(LoginDatabase.instance().getAc()).save(
//									new ChargeLog(orderid, account, money, areaid, DateUtil.isSameDay(accountDB
//											.getCreateTime().getTime(), System.currentTimeMillis()) ? 1 : 0,
//											chargeCount, channel, pm_id, cardType));
//						}
//					});
//				}

//				res = SuccessRes;
//			}
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		} finally {
//			this.sendResponse(httpExchange, res);
//		}

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
