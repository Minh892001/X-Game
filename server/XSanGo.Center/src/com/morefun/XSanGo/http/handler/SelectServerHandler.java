/**
 * 
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.XSanGo.Protocol.AMI_Center_sendTocken;
import com.XSanGo.Protocol.AlarmType;
import com.XSanGo.Protocol.DeviceInfo;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.WhiteList;
import com.XSanGo.Protocol.WhiteListType;
import com.morefun.XSanGo.CenterServer;
import com.morefun.XSanGo.GameServerInfo;
import com.morefun.XSanGo.SMSManager;
import com.morefun.XSanGo.SessionData;
import com.morefun.XSanGo.XsgAccountManager;
import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

import Ice.LocalException;
import Ice.UserException;

/**
 * 选择服务器处理逻辑
 * 
 * @author sulingyun
 * 
 */
public class SelectServerHandler extends AbsHandler {
	public static void main(String[] args) {
		int a = 100;
		if (a > 50) {
			System.out.println("a>50");
		} else if (a > 60) {
			System.out.println("a>60");
		}
	}

	private static Logger log = LogManager.getLogger(SelectServerHandler.class);

	@Override
	protected void innerHandle(final HttpExchange httpExchange) throws IOException {
		final SelectServerResponse res = new SelectServerResponse();
		res.success = false;

		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		String idStr = paramsMap.get("serverId");
		String sessionId = paramsMap.get("sessionId");

		final SessionData sessionData = XsgAccountManager.getInstance().findSessionData(sessionId);
		final Account account = XsgAccountManager.getInstance().findAccountBySessionId(sessionId);
		if (account == null) {
			res.token = "timeout";
			sendResponse(httpExchange, TextUtil.GSON.toJson(res));
			return;
		}

		// ///////////////// 白名单控制逻辑/////////////////
		WhiteList wList = CenterServer.instance().findWhiteList(WhiteListType.Normal);
		String remoteIp = this.getRemoteIp(httpExchange);
		if (!CenterServer.instance().isInWhiteList(wList, remoteIp, account.getAccount())) {
			res.token = wList.tip;
			String responseMsg = TextUtil.GSON.toJson(res);
			this.sendResponse(httpExchange, responseMsg);
			return;
		}
		// ///////////////// 结束白名单逻辑/////////////////

		boolean isCp = CenterServer.instance().isCpUser(account == null ? "" : account.getAccount(), remoteIp);
		int temp = -1;
		GameServerInfo gameServer = null;
		try {
			temp = Integer.parseInt(idStr);
			if (temp != -1) {
				gameServer = CenterServer.instance().findServerInfoById(temp);
			}
		} catch (Exception e) {
			log.error(idStr, e);
		}

		// 目标服务器ID默认为0，不为0则表示服务器已经被合并
		final int requestServerId = temp;
		final int targetServerId = (gameServer != null && gameServer.getDB().getTargetId() != 0) ? gameServer.getDB()
				.getTargetId() : temp;
		boolean isCpShowOnly = gameServer.isCpShowOnly();
		boolean isCpEnterOnly = gameServer.isCpEnterOnly();
		if ((isCpShowOnly || isCpEnterOnly) && !isCp) {// CP白名单控制逻辑，只针对有访问限制的服务器
			if (isCpShowOnly) {
				res.token = "非法请求。";
			} else if (isCpEnterOnly) {
				res.token = "服务器维护中。";
			}

			String responseMsg = TextUtil.GSON.toJson(res);
			this.sendResponse(httpExchange, responseMsg);
			return;
		}
		if (targetServerId == -1) {
			res.token = "非法请求。";
		} else if (account.getActive() == 0) {
			res.token = "帐号未激活";
		} else if (account.isFrozen()) {
			res.token = "您的帐号已经被冻结。";
		} else if (!CenterServer.instance().isServerReady(targetServerId)) {
			res.token = "服务器维护中。";
		} else if (CenterServer.instance().findServerInfoById(targetServerId).isOverload()) {
			res.token = "服务器人数已满。";
			SMSManager.getInstance().sendAlarmSMS(AlarmType.ServerFull,
					TextUtil.format("X三国发生系统故障，故障类型[{0}]，关键字[{1}]", AlarmType.ServerFull, targetServerId));
		} else {
			res.success = true;
		}

		if (!res.success) {
			sendResponse(httpExchange, TextUtil.GSON.toJson(res));
			return;
		}

		// 生成随机的口令
		res.token = UUID.randomUUID().toString();
		DeviceInfo device = new DeviceInfo(account.getChannelId(), sessionData.getMac(), remoteIp,
				sessionData.getVersion(), account.getCreateTime().getTime(), temp, sessionData.getCurrentChannel());

		// 通知到game服务器
		try {
			IceEntry.instance().sendTocken_async(new AMI_Center_sendTocken() {

				@Override
				public void ice_response() {
					try {
						sendResponse(httpExchange, TextUtil.GSON.toJson(res));
						CenterServer.instance().updateAccountRecentServerData(account, requestServerId);
					} catch (IOException e) {
						log.error(e, e);
					}
				}

				@Override
				public void ice_exception(LocalException ex) {
					res.token = "无法连接该服务器。";
					res.success = false;
					try {
						sendResponse(httpExchange, TextUtil.GSON.toJson(res));
					} catch (IOException e) {
						log.error(e, e);
					}
				}

				@Override
				public void ice_exception(UserException ex) {
					res.token = "暂时无法进入。";
					res.success = false;
					try {
						sendResponse(httpExchange, TextUtil.GSON.toJson(res));
					} catch (IOException e) {
						log.error(e, e);
					}
				}
			}, targetServerId, account.getId(), account.getAccount(), res.token, device);
		} catch (NoteException e) {
			log.error(e);
		}

	}
}

class SelectServerResponse {
	boolean success;
	String token;
}
