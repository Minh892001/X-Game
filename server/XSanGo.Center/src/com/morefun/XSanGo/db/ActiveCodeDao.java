package com.morefun.XSanGo.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ActiveCodeDao extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(ActiveCodeDao.class);

	public static ActiveCodeDao getFromApplicationContext(ApplicationContext ctx) {
		return (ActiveCodeDao) ctx.getBean("ActiveCodeDao");
	}

	public ActiveCode findByCode(String code) {
		try {
			ActiveCode instance = (ActiveCode) getHibernateTemplate().get(
					ActiveCode.class, code);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void attachDirty(ActiveCode code) {
		try {
			getHibernateTemplate().saveOrUpdate(code);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void save(ActiveCode activeCode) {
		try {
			getHibernateTemplate().save(activeCode);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

}
