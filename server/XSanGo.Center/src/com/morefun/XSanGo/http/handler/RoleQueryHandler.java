package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import Ice.LocalException;

import com.XSanGo.Protocol.Callback_Center_findRoleViewListByRole;
import com.XSanGo.Protocol.CenterPrx;
import com.XSanGo.Protocol.RoleViewForGM;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.EncryptUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * 角色查询接口
 * 
 * @author sulingyun
 * 
 */
public class RoleQueryHandler extends AbsHandler {

	private static String Key = "3508edc886b1538dfd2e009af38dc1ee";
	private static Logger log = LogManager.getLogger(RoleQueryHandler.class);

	/**
	 * 通过角色名称查询roleId、account
	 */
	@Override
	protected void innerHandle(final HttpExchange httpExchange) throws IOException {

		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		// 获取请求中的参数
		final String roleName = paramsMap.get("roleName");
		String idStr = paramsMap.get("serverId");
		String sign = paramsMap.get("sign");
		// 装载查询结果
		final List<RoleInfoResponse> results = new ArrayList<RoleInfoResponse>();

		String checkInput = TextUtil.format("roleName={0}&serverId={1}{2}", roleName, idStr, Key);

		String checkOutput = null;

		try {
			checkOutput = EncryptUtil.bytesToHexString(EncryptUtil.getMD5(checkInput.getBytes()));

			if (checkOutput.equals(sign)) {// 签名验证
				// 获取serverId
				int serverId = Integer.parseInt(idStr);

				log.info(TextUtil.format("find roleId and account by rolename,[{0},{1}]", roleName, serverId));

				CenterPrx prx = IceEntry.instance().getCenterPrx(serverId, true);

				if (prx == null) {
					log.error(TextUtil.format("{0} has no prx.", serverId));
					RoleInfoResponse res = new RoleInfoResponse();
					res.message = "can not connect center server prx, serverId:" + serverId;
					results.add(res);
					// 将结果返回
					this.sendResponse(httpExchange, TextUtil.GSON.toJson(results.toArray(new RoleInfoResponse[0])));
					return;
				}
				// 通过代理对象查询调用服务器端查询角色信息
				prx.begin_findRoleViewListByRole(roleName, new Callback_Center_findRoleViewListByRole() {

					@Override
					public void response(String __ret) {
						// 根据服务器返回的结过筛选出roleId和account
						RoleViewForGM[] views = TextUtil.GSON.fromJson(__ret, RoleViewForGM[].class);
						if (views != null && views.length > 0) {
							for (int i = 0; i < views.length; i++) {
								RoleInfoResponse res = new RoleInfoResponse();
								res.roleId = views[i].baseView.id;
								res.account = views[i].account.username;
								res.message = "SUCC";
								results.add(res);
							}
						} else {
							log.error(TextUtil.format("find roleId and account by rolename,[{0}]", roleName));
							RoleInfoResponse res = new RoleInfoResponse();
							res.message = "no results found, roleName:" + roleName;
							results.add(res);
						}

						this.queryOver(httpExchange);
					}

					private void queryOver(final HttpExchange httpExchange) {
						String jsonRes = TextUtil.GSON.toJson(results.toArray(new RoleInfoResponse[0]));
						log.info(TextUtil.format("the query result,[{0}]", jsonRes));
						// 将结果返回给充值中心
						try {
							sendResponse(httpExchange, jsonRes);
						} catch (IOException e) {
							log.error(e, e);
						}
					}

					@Override
					public void exception(LocalException __ex) {
						log.error("find RoleView error by LocalException.", __ex);
						this.queryOver(httpExchange);
					}
				});

			} else {
				log.error(TextUtil.format("signature check failed,[{0}]", sign));
				RoleInfoResponse res = new RoleInfoResponse();
				res.message = "invalid signnature";
				results.add(res);
				// 将结果返回
				this.sendResponse(httpExchange, TextUtil.GSON.toJson(results.toArray(new RoleInfoResponse[0])));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}

class RoleInfoResponse {
	String roleId;
	String account;
	String item; // 充值选项
	String message;// 消息说明
}
