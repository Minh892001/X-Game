/**
 * 
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.XsgAccountManager;
import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.db.ActiveCode;
import com.morefun.XSanGo.db.ActiveCodeDao;
import com.morefun.XSanGo.gm.GmI;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * 激活帐号
 * 
 * @author sulingyun
 *
 */
public class ActiveAccountHandler extends AbsHandler {

	private static ActiveCodeDao DAO = ActiveCodeDao
			.getFromApplicationContext(LoginDatabase.instance().getAc());

	@Override
	protected void innerHandle(HttpExchange httpExchange) throws IOException {

		ActiveAccountRes result = new ActiveAccountRes();
		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		String sessionId = paramsMap.get("sessionId");
		String activeCode = paramsMap.get("activeCode").toUpperCase();

		ActiveCode code = DAO.findByCode(activeCode);
		Account account = XsgAccountManager.getInstance()
				.findAccountBySessionId(sessionId);
		if (account == null) {
			result.errorMsg = "timeout";
		} else if (account.getActive() == 1) {
			result.errorMsg = "帐号已激活";
		} else if (code == null) {
			result.errorMsg = "无效激活码";
		} else if (!TextUtil.isNullOrEmpty(code.getAccount())
				&& !GmI.instance().canReuseActiveCode()) {
			result.errorMsg = "激活码已被使用";
		} else {
			code.setAccount(account.getAccount());
			code.setUseTime(Calendar.getInstance().getTime());
			DAO.attachDirty(code);
			// 更新帐号
			account.setActive(1);

			result.success = true;
		}

		this.sendResponse(httpExchange, TextUtil.GSON.toJson(result));

	}
}

class ActiveAccountRes {
	boolean success;
	String errorMsg;

}
