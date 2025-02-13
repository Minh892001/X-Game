package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import Ice.LocalException;

import com.XSanGo.Protocol.Callback_Center_queryItemNum;
import com.XSanGo.Protocol.CenterPrx;
import com.XSanGo.Protocol.QueryItemResponse;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

public class QueryItemCountsHandler extends AbsHandler {

	private static Logger log = LogManager.getLogger(QueryItemCountsHandler.class);

	@Override
	protected void innerHandle(final HttpExchange httpExchange) throws IOException {
		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		String roleName = paramsMap.get("roleName");
		String itemId = paramsMap.get("ItemId");
		String idStr = paramsMap.get("serverId");

		int serverId = Integer.parseInt(idStr);

		log.info(TextUtil.format("find roleId and account by rolename,[{0},{1}]", roleName, serverId));

		CenterPrx prx = IceEntry.instance().getCenterPrx(serverId, true);

		if (prx == null) {
			log.error(TextUtil.format("{0} has no prx.", serverId));
			QueryItemResponse res = new QueryItemResponse();
			res.message = "can not connect center server prx, serverId:" + serverId;
			// 将结果返回
			this.sendResponse(httpExchange, TextUtil.GSON.toJson(res));
			return;
		}

		prx.begin_queryItemNum(roleName, itemId, new Callback_Center_queryItemNum() {

			@Override
			public void exception(LocalException __ex) {
				log.error("find item counts error by LocalException.", __ex);
				QueryItemResponse res = new QueryItemResponse();
				res.message = "FAILED";
				String jsonRes = TextUtil.GSON.toJson(res);
				try {
					sendResponse(httpExchange, jsonRes);
				} catch (IOException e) {
					log.error(e, e);
				}
			}

			@Override
			public void response(String __ret) {
				try {
					sendResponse(httpExchange, __ret);
				} catch (IOException e) {
					log.error(e, e);
				}

			}
		});

	}

}
