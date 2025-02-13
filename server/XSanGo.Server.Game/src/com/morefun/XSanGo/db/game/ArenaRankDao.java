/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 竞技场排行 数据库结构
 * 
 * @author 吕明涛
 * 
 */
public class ArenaRankDao extends HibernateDaoSupport {
	
	private static final Logger log = LoggerFactory.getLogger(ArenaRankDao.class);
	
	public List<ArenaRank> findAll() {
		log.debug("finding all ArenaRank instances");
		try {
			final String queryString2 = "from ArenaRank";
			return getHibernateTemplate().find(queryString2);
		} catch (RuntimeException re) {
			log.error("find ArenaRank all failed", re);
			throw re;
		}
	}
	
	public void save(ArenaRank arenaRank) {
		log.debug("saving ArenaRank instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(arenaRank);
			log.debug("save ArenaRank successful");
		} catch (RuntimeException re) {
			log.error("save ArenaRank failed", re);
			throw re;
		}
	}
	
	/**
	 * 保存奖励的日志
	 * @param awardLog
	 */
	public void saveAwardLog(ArenaAwardLog awardLog) {
		try {
			this.getHibernateTemplate().saveOrUpdate(awardLog);
		} catch (RuntimeException re) {
			log.error("save arenaRankLog failed", re);
			throw re;
		}
	}
	
	public static ArenaRankDao getFromApplicationContext(ApplicationContext ctx) {
		return (ArenaRankDao) ctx.getBean("ArenaRankDao");
	}
}
