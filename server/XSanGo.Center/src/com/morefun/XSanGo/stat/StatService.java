/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.stat;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.db.YuanbaoConsume;
import com.morefun.XSanGo.db.YuanbaoConsumeDAO;
import com.morefun.XSanGo.db.stat.Online;
import com.morefun.XSanGo.db.stat.StatSimpleDAO;

/**
 * 统计服务
 * 
 * @author jkp
 * 
 */
public class StatService implements IStat {

	private static final Logger logger = LoggerFactory
			.getLogger(StatService.class);

	@Override
	public void online(int serverId, int count) {
		try {
			Online online = new Online();
			online.setServerId(serverId);
			online.setOnlineCount(count);
			online.setStatTime(new Timestamp(System.currentTimeMillis()));
			StatSimpleDAO.getForStat(LoginDatabase.instance().getAc()).save(online);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void levelup(int serverId, String roleId, String roleName,
			int newLevel, int onlineDuration) {
	}

	@Override
	public void logout(int serverId, String account, String roleId,
			String roleName, int onlineDuration) {
	}

	@Override
	public int beginYuanbaoTransfer(int serverId, String account,
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

	@Override
	public void endYuanbaoTransfer(int id, boolean isSuc) {
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
