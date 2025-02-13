package com.morefun.XSanGo.db.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RedPacketDao extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory.getLogger(RedPacketDao.class);
	
	/**
	 * 删除过期红包
	 * @param passDate
	 */
	public void deletePass() {
		try {
			String queryString = "from RedPacket";
			getHibernateTemplate().deleteAll(getHibernateTemplate().find(queryString));
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public static RedPacketDao getFromApplicationContext(ApplicationContext ctx) {
		return (RedPacketDao) ctx.getBean("RedPacketDao");
	}

}
