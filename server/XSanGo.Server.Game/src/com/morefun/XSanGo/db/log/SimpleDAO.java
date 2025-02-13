/**
 * 
 */
package com.morefun.XSanGo.db.log;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 单表操作类，支持简单增删改和无条件查询
 * 
 * @author BruceSu
 * 
 */
public class SimpleDAO extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory.getLogger(SimpleDAO.class);

	public <T> List<T> findAll(Class<T> type) {
		try {
			String queryString = "from " + type.getSimpleName();
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public void save(Serializable transientInstance) {
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Serializable persistentInstance) {
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public <T> T findById(Class<T> type, Serializable id) {
		try {
			T instance = getHibernateTemplate().get(type, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void attachDirty(Serializable instance) {
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SimpleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SimpleDAO) ctx.getBean("LogSimpleDAO");
	}

}
