/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.logicserver;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.XsgAccountManager;
import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.db.YuanbaoConsume;
import com.morefun.XSanGo.db.YuanbaoConsumeDAO;

/**
 * 接受游戏逻辑服跟登录中心服相关状态改变的处理类
 * @author WFY
 *
 */
public class LogicServerControler implements ILogicServerControler {
	private static final Logger logger = LoggerFactory
			.getLogger(LogicServerControler.class);
	private int serverId;

	public LogicServerControler(int serverId) {
		this.serverId = serverId;
	}

	@Override
	public void createRole(final String account, String roleId, String roleName) {

		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				Account accountDB = XsgAccountManager.getInstance()
						.findAccount(account);
//				accountDB.setRoleCount(accountDB.getRoleCount() + 1);//每创建一个角色就把该帐号拥有的角色数+1
				XsgAccountManager.getInstance().updateAccount(accountDB);
			}
		});
	}

	@Override
	public void newMaxLevel(final String account, final String roleId,
			final String roleName, final int newLevel) {
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				Account accountDB = XsgAccountManager.getInstance()
						.findAccount(account);

				if (newLevel > accountDB.getMaxLevel()) {
					accountDB.setMaxLevel(newLevel);
				}
				if (accountDB.getMaxLevel() > 5) {//大于5级的角色才设为有效角色
					accountDB.setValid(true);
				}
				XsgAccountManager.getInstance().updateAccount(accountDB);
			}
		});
	}

	@Override
	public void bindToRoleResult(final int id, final boolean result) {

		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				// 记录日志
				endYuanbaoTransfer(id, result);
			}
		});
	}

	private int beginYuanbaoTransfer(int serverId, String account,
			String roleId, String roleName, int yuanbao) {
		try {
			YuanbaoConsumeDAO dao = YuanbaoConsumeDAO
					.getFromApplicationContext(LoginDatabase.instance().getAc());
			YuanbaoConsume log = new YuanbaoConsume(serverId, account, yuanbao,
					roleId, roleName, 0);
			dao.save(log);
			return log.getId();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}

	private void endYuanbaoTransfer(int id, boolean isSuc) {
		try {
			YuanbaoConsumeDAO dao = YuanbaoConsumeDAO
					.getFromApplicationContext(LoginDatabase.instance().getAc());
			YuanbaoConsume log = dao.findById(id);
			if (log != null) {
				log.setState(isSuc ? 1 : 2);
				log.setCompleteTime(new Timestamp(new Date().getTime()));
				dao.merge(log);
			} else {
				// 记录错误日志
				logger.error("未找到对应的元宝转移记录：id={0}", id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
