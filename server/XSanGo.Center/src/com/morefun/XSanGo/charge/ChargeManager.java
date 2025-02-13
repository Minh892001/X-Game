/**
 * 
 */
package com.morefun.XSanGo.charge;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.LocalException;

import com.XSanGo.Protocol.Callback_Center_charge;
import com.XSanGo.Protocol.CenterPrx;
import com.XSanGo.Protocol.CustomChargeParams;
import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.db.Charge;
import com.morefun.XSanGo.db.ChargeDAO;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author BruceSu
 * 
 */
public class ChargeManager {
	private static Log log = LogFactory.getLog(ChargeManager.class);

	/**
	 * 处理超时未处理的订单
	 * 
	 * @throws InterruptedException
	 */
	public static void handleOrder() throws InterruptedException {
		// 查询状态为0的所有订单
		final ChargeDAO dao = ChargeDAO.getFromApplicationContext(LoginDatabase.instance().getAc());
		List<Charge> list = dao.findByState(Charge.State_Init);
		final CountDownLatch cdl = new CountDownLatch(list.size());

		for (final Charge order : list) {
			// 依次向支付提供方查询各个订单
			CenterPrx prx = IceEntry.instance().getCenterPrx(order.getServerId(), true);//找到对应的游戏服务器
			if (prx == null) {
				log.error(TextUtil.format("Order {0} has no prx.", order.getOrderId()));
				cdl.countDown();
				continue;
			}

			CustomChargeParams param = null;
			try {
				param = TextUtil.GSON.fromJson(order.getParams(), CustomChargeParams.class);
			} catch (Exception e) {
				log.error("Parameter error at order " + order.getOrderId());
				cdl.countDown();
				continue;
			}
			//发送加值请求并处理回应
			prx.begin_charge(order.getRoleId(), order.getCent() / 100, param, order.getOrderId(), order.getCurrency(),
					new Callback_Center_charge() {
						@Override
						public void response() {
							order.setState(Charge.State_Sucess);//把订单状态设为处理成功
							order.setCompleteTime(Calendar.getInstance().getTime());
							dao.attachDirty(order);//把成功状态更新到数据库
							cdl.countDown();
						}

						@Override
						public void exception(LocalException ex) {
							cdl.countDown();
							log.error("Order notify error by LocalException.", ex);
						}
					});
		}

		cdl.await(20, TimeUnit.SECONDS);
		log.debug("Order handle complete.");
	}
}
