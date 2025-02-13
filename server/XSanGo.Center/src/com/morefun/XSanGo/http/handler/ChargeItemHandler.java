package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import Ice.LocalException;

import com.XSanGo.Protocol.Callback_Center_getChargeItem;
import com.XSanGo.Protocol.CenterPrx;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.EncryptUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

public class ChargeItemHandler extends AbsHandler {

	private static String Key = "3508edc886b1538dfd2e009af38dc1ee";
	private static Logger log = LogManager.getLogger(ChargeItemHandler.class);

	@Override
	protected void innerHandle(HttpExchange httpExchange) throws IOException {

		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		String money = paramsMap.get("money");
		String idStr = paramsMap.get("serverId");
		String sign = paramsMap.get("sign");

		String checkInput = TextUtil.format("money={0}&serverId={1}{2}", money, idStr, Key);
		log.info(TextUtil.format("the query param is money={0}&serverId={1}&sign={2}", money, idStr, sign));
		String checkOutput = null;

		final CountDownLatch cdl = new CountDownLatch(1);
		final ChargeItemResponse res = new ChargeItemResponse();
		try {
			checkOutput = EncryptUtil.bytesToHexString(EncryptUtil.getMD5(checkInput.getBytes()));

			if (checkOutput.equals(sign)) {
				CenterPrx prx = IceEntry.instance().getCenterPrx(Integer.valueOf(idStr), true);

				if (prx == null) {
					log.error(TextUtil.format("{0} has no prx.", idStr));
					res.message = "-2"; // 中心服务连接失败
				}

				prx.begin_getChargeItem(Integer.valueOf(money), new Callback_Center_getChargeItem() {

					@Override
					public void exception(LocalException __ex) {
						log.error("find chargeItem error by LocalException.", __ex);
						res.message = "-1"; // ICE查询失败
						cdl.countDown();
					}

					@Override
					public void response(String __ret) {
						res.message = "0";// 查询成功
						res.itemId = __ret;
						log.info("find chargeItem success by money.itemId =[" + __ret + "]");
						cdl.countDown();
					}
				});

				cdl.await(20, TimeUnit.SECONDS);
			} else {
				log.error(TextUtil.format("signature check failed,[{0}]", sign));
				res.message = "-3"; // 签名验证失败
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(TextUtil.format("the chargeItem query result,[{0}]", TextUtil.GSON.toJson(res)));
			// 将结果返回给充值中心
			this.sendResponse(httpExchange, TextUtil.GSON.toJson(res));
		}
	}

}

class ChargeItemResponse {
	String itemId;
	String message;// 消息说明
}