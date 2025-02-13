/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * Account entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.morefun.XSanGo.db.Account
 * @author MyEclipse Persistence Tools
 */

public class AccountDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(AccountDAO.class);
	// property constants
	public static final String PASSWORD = "password";
	public static final String YUANBAO = "yuanbao";
	public static final String VALID = "valid";
	public static final String MOBILE = "mobile";
	public static final String MAX_LEVEL = "maxLevel";
	public static final String ROLE_COUNT = "roleCount";
	public static final String ONLINE_DURATION = "onlineDuration";
	public static final String FROZEN = "frozen";
	public static final String GMPOPEDOM = "gmpopedom";
	public static final String LAST_ROLE_ID = "lastRoleId";
	private static AtomicInteger id;

	protected void initDao() {
		// do nothing
	}

	public void save(Account transientInstance) {
		log.debug("saving Account instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Account persistentInstance) {
		log.debug("deleting Account instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Account findByName(String name) {
		List<Account> list = findByProperty("account", name);
		if (list.size() > 1) {
			throw new IllegalStateException("存在多个同名帐号。");
		}

		return list.size() > 0 ? list.get(0) : null;
	}

	public Account findById(int id) {
		log.debug("getting Account instance with id: " + id);
		try {
			Account instance = (Account) getHibernateTemplate().get(
					"com.dreamstar.dragon.db.Account", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Account> findByExample(Account instance) {
		log.debug("finding Account instance by example");
		try {
			List<Account> results = (List<Account>) getHibernateTemplate()
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
		log.debug("finding Account instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Account as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Account> findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	public List<Account> findByYuanbao(Object yuanbao) {
		return findByProperty(YUANBAO, yuanbao);
	}

	public List<Account> findByValid(Object valid) {
		return findByProperty(VALID, valid);
	}

	public List<Account> findByMobile(Object mobile) {
		return findByProperty(MOBILE, mobile);
	}

	public List<Account> findByMaxLevel(Object maxLevel) {
		return findByProperty(MAX_LEVEL, maxLevel);
	}

	public List<Account> findByRoleCount(Object roleCount) {
		return findByProperty(ROLE_COUNT, roleCount);
	}

	public List<Account> findByOnlineDuration(Object onlineDuration) {
		return findByProperty(ONLINE_DURATION, onlineDuration);
	}

	public List<Account> findByFrozen(Object frozen) {
		return findByProperty(FROZEN, frozen);
	}

	public List<Account> findByGmpopedom(Object gmpopedom) {
		return findByProperty(GMPOPEDOM, gmpopedom);
	}

	public List<Account> findByLastRoleId(Object lastRoleId) {
		return findByProperty(LAST_ROLE_ID, lastRoleId);
	}

	public List findAll() {
		log.debug("finding all Account instances");
		try {
			String queryString = "from Account";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Account merge(Account detachedInstance) {
		log.debug("merging Account instance");
		try {
			Account result = (Account) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Account instance) {
		log.debug("attaching dirty Account instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static AccountDAO getFromApplicationContext(ApplicationContext ctx) {
		return (AccountDAO) ctx.getBean("AccountDAO");
	}

	/**
	 * 获取当前计数
	 * 
	 * @return
	 */
	public int getNextId() {
		if (id == null) {
			id = new AtomicInteger(this.getCurrentMaxId());
		}
		return id.incrementAndGet();
	}

	/**
	 * 获取当前数据库的最大ID
	 * 
	 * @return
	 */
	private int getCurrentMaxId() {
		try {
			final String queryString = "SELECT IFNULL(MAX(id),0) FROM account";

			return getHibernateTemplate().execute(
					new HibernateCallback<Integer>() {

						@Override
						public Integer doInHibernate(Session session)
								throws HibernateException, SQLException {
							return ((Number) session
									.createSQLQuery(queryString).uniqueResult())
									.intValue();

						}
					});

		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}