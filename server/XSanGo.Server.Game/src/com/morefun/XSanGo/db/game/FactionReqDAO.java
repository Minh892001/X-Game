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
 * @author BruceSu
 * 
 */
public class FactionReqDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FactionReqDAO.class);

	public List findAll() {
		log.debug("finding all FactionReq instances");
		try {
			String queryString = "from FactionReq";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public void save(FactionReq transientInstance) {
		log.debug("saving FactionReq instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(FactionReq persistentInstance) {
		log.debug("deleting FactionReq instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public static FactionReqDAO getFromApplicationContext(ApplicationContext ctx) {
		return (FactionReqDAO) ctx.getBean("FactionReqDAO");
	}
}
