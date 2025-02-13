/**
 * 
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.Map;

import com.XSanGo.Protocol.AMD_Gm_findRoleViewList;
import com.XSanGo.Protocol.AMD_Gm_sendSystemMail;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.RoleViewForGM;
import com.google.gson.JsonSyntaxException;
import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.db.CDKDao;
import com.morefun.XSanGo.db.CDKDetail;
import com.morefun.XSanGo.gm.GmI;
import com.morefun.XSanGo.logicserver.CenterCallbackI;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * CDK使用的HTTP外部接口
 * 
 * @author sulingyun
 *
 */
public class CDKHandler extends AbsHandler {

	@Override
	protected void innerHandle(final HttpExchange httpExchange)
			throws IOException {
		final CDKResponse res = new CDKResponse();
		res.success = false;
		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		final String account = paramsMap.get("account");
		final int serverId = NumberUtil.parseInt(paramsMap.get("serverId"));
		final String roleName = paramsMap.get("roleName");
		final String cdk = paramsMap.get("cdk");

		final CDKDao dao = CDKDao.getFromApplicationContext(LoginDatabase
				.instance().getAc());
		// final CDKDetail detail = dao.findDetailByCDK(cdk);
		// // 验证CDK合法性，获取CDK奖励物品清单
		// try {
		// CenterCallbackI.checkCDK(account, serverId, 60, "", detail);
		// } catch (NoteException e) {
		// res.error = e.reason;
		// this.sendResponse(httpExchange, TextUtil.GSON.toJson(res));
		// return;
		// }

		// 调用GM接口查询指定服务器的帐号角色信息
		AMD_Gm_findRoleViewList __cb = new AMD_Gm_findRoleViewList() {

			@Override
			public void ice_exception(Exception ex) {
				res.error = "获取玩家信息失败。";
				try {
					sendResponse(httpExchange, TextUtil.GSON.toJson(res));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void ice_response(String __ret) {// 返回角色信息时调用
				RoleViewForGM[] array = TextUtil.GSON.fromJson(__ret,
						RoleViewForGM[].class);
				for (RoleViewForGM view : array) {
					if (view.baseView.name.equals(roleName)) {
						final CDKDetail detail = dao.findDetailByCDK(cdk);
						// 用GM邮件接口发送邮件，最后用角色名字来发邮件，避免同一帐号多角色问题
						try {
							CenterCallbackI.checkCDK(account, serverId,
									view.baseView.level, "", detail);
							GmI.instance().sendSystemMail_async(
									new AMD_Gm_sendSystemMail() {

										@Override
										public void ice_exception(Exception ex) {
											res.error = "礼品发放失败。";
											try {
												sendResponse(httpExchange,
														TextUtil.GSON
																.toJson(res));
											} catch (IOException e) {
												e.printStackTrace();
											}
										}

										@Override
										public void ice_response() {// 邮件发送成功时调用
											// 更新CDK数据
											detail.setUseTime(detail
													.getUseTime() + 1);
											// 保存数据库
											dao.attachDirty(detail);

											// 回传消息
											res.success = true;
											try {
												sendResponse(httpExchange,
														TextUtil.GSON
																.toJson(res));
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									},
									serverId,
									"",
									roleName,
									"礼包兑换奖励",
									"主公，请见附件。",
									TextUtil.GSON.fromJson(detail.getGroup()
											.getContent(), Property[].class),null);
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
							res.error = "CDK配置错误。";
						} catch (NoteException e) {
							res.error = e.reason;
						}

						if (!TextUtil.isNullOrEmpty(res.error)) {
							try {
								sendResponse(httpExchange,
										TextUtil.GSON.toJson(res));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						return;
					}
				}

				res.error = "帐号角色数据不匹配。";
				try {
					sendResponse(httpExchange, TextUtil.GSON.toJson(res));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		try {
			GmI.instance().findRoleViewList_async(__cb, serverId, account,
					roleName,null);
		} catch (NoteException e) {
			res.error = e.reason;
			this.sendResponse(httpExchange, TextUtil.GSON.toJson(res));
			return;
		}
	}

}

class CDKResponse {
	boolean success;
	String error;
}