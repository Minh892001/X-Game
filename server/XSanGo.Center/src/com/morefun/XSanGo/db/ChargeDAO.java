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
 * Charge entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.morefun.XSanGo.db.Charge
 * @author MyEclipse Persistence Tools
 */

public class ChargeDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(ChargeDAO.class);
	// property constants
	public static final String STATE = "state";
	public static final String ORDER_ID = "orderId";

	protected void initDao() {
		// do nothing
	}

	public void save(Charge transientInstance) {
		log.debug("saving Charge instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Charge persistentInstance) {
		log.debug("deleting Charge instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Charge findById(String orderId) {
		log.debug("getting Charge instance with id: " + orderId);
		try {
			Charge instance = (Charge) getHibernateTemplate().get(Charge.class,
					orderId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Charge> findByExample(Charge instance) {
		log.debug("finding Charge instance by example");
		try {
			List<Charge> results = (List<Charge>) getHibernateTemplate()
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
		log.debug("finding Charge instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Charge as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Charge> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List findAll() {
		log.debug("finding all Charge instances");
		try {
			String queryString = "from Charge";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Charge merge(Charge detachedInstance) {
		log.debug("merging Charge instance");
		try {
			Charge result = (Charge) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Charge instance) {
		log.debug("attaching dirty Charge instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Charge instance) {
		log.debug("attaching clean Charge instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ChargeDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ChargeDAO) ctx.getBean("ChargeDAO");
	}
}