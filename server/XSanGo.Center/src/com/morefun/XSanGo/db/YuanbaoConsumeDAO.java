/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * YunbaoTransferLog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.morefun.XSanGo.db.YuanbaoConsume
 * @author MyEclipse Persistence Tools
 */

public class YuanbaoConsumeDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(YuanbaoConsumeDAO.class);
	// property constants
	public static final String YUANBAO = "yuanbao";
	public static final String ROLE_ID = "roleId";
	public static final String ROLE_NAME = "roleName";
	public static final String STATE = "state";

	protected void initDao() {
		// do nothing
	}

	public void save(YuanbaoConsume transientInstance) {
		log.debug("saving YuanbaoConsume instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(YuanbaoConsume persistentInstance) {
		log.debug("deleting YuanbaoConsume instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public YuanbaoConsume findById(Integer id) {
		log.debug("getting YuanbaoConsume instance with id: " + id);
		try {
			YuanbaoConsume instance = (YuanbaoConsume) getHibernateTemplate()
					.get("com.dreamstar.dragon.stat.YuanbaoTransferLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<YuanbaoConsume> findByExample(YuanbaoConsume instance) {
		log.debug("finding YuanbaoConsume instance by example");
		try {
			List<YuanbaoConsume> results = (List<YuanbaoConsume>) getHibernateTemplate()
					.findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding YuanbaoConsume instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from YuanbaoConsume as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<YuanbaoConsume> findByYuanbao(Object yuanbao) {
		return findByProperty(YUANBAO, yuanbao);
	}

	public List<YuanbaoConsume> findByRoleId(Object roleId) {
		return findByProperty(ROLE_ID, roleId);
	}

	public List<YuanbaoConsume> findByRoleName(Object roleName) {
		return findByProperty(ROLE_NAME, roleName);
	}

	public List<YuanbaoConsume> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List findAll() {
		log.debug("finding all YuanbaoConsume instances");
		try {
			String queryString = "from YuanbaoConsume";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public YuanbaoConsume merge(YuanbaoConsume detachedInstance) {
		log.debug("merging YuanbaoConsume instance");
		try {
			YuanbaoConsume result = (YuanbaoConsume) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(YuanbaoConsume instance) {
		log.debug("attaching dirty YuanbaoConsume instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(YuanbaoConsume instance) {
		log.debug("attaching clean YuanbaoConsume instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static YuanbaoConsumeDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (YuanbaoConsumeDAO) ctx.getBean("YuanbaoConsumeDAO");
	}
}