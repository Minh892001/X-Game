/**
 * 
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.morefun.XSanGo.announce.XsgAnnounceManager;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * 登录公告处理逻辑
 * 
 * @author sulingyun
 *
 */
public class AnnounceHandler extends AbsHandler {
	private String response;
	private long lastRefreshTime;
	private long refreshInterval = TimeUnit.SECONDS.toMillis(90);

	private synchronized void refreshResponse() {
		long now = System.currentTimeMillis();
		if (TextUtil.isNullOrEmpty(this.response)
				|| now - this.lastRefreshTime > this.refreshInterval) {
			this.response = TextUtil.GSON.toJson(XsgAnnounceManager
					.getInstance().getAnnounces());
			this.lastRefreshTime = now;
		}
	}

	@Override
	protected void innerHandle(HttpExchange httpExchange) throws IOException {
		this.refreshResponse();
		this.sendResponse(httpExchange, response);
	}

}
