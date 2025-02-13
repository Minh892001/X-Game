/**
 * 
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.morefun.XSanGo.CenterServer;
import com.morefun.XSanGo.GameServerInfo;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * 获取所有服务器列表(未合服)
 * 
 * @author lixiongming
 * 
 */
public class ServerMapHandler extends AbsHandler {

	@Override
	protected void innerHandle(HttpExchange httpExchange) throws IOException {
		Collection<GameServerInfo> collection = CenterServer.instance().getAllServers().values();
		List<String> list = new ArrayList<String>();
		for (GameServerInfo gs : collection) {
			int orignalId = gs.getId();
			int targetId = CenterServer.instance().getRealServerId(gs.getId());
			if (orignalId != targetId) {
				continue;
			}
			list.add(String.valueOf(targetId));
		}

		this.sendResponse(httpExchange, TextUtil.join(list, ","));
	}
}
