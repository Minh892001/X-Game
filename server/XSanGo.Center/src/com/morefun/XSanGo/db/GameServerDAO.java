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
 * GameServer entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.morefun.XSanGo.db.GameServer
 * @author MyEclipse Persistence Tools
 */

public class GameServerDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(GameServerDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String HOST_IP = "hostIp";

	protected void initDao() {
		// do nothing
	}

	public void save(GameServer transientInstance) {
		log.debug("saving GameServer instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GameServer persistentInstance) {
		log.debug("deleting GameServer instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GameServer findById(Integer id) {
		log.debug("getting GameServer instance with id: " + id);
		try {
			GameServer instance = (GameServer) getHibernateTemplate().get(
					"com.dreamstar.dragon.db.GameServer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<GameServer> findByExample(GameServer instance) {
		log.debug("finding GameServer instance by example");
		try {
			List<GameServer> results = (List<GameServer>) getHibernateTemplate()
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
		log.debug("finding GameServer instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from GameServer as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<GameServer> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<GameServer> findByHostIp(Object hostIp) {
		return findByProperty(HOST_IP, hostIp);
	}

	public List findAll() {
		log.debug("finding all GameServer instances");
		try {
			String queryString = "from GameServer";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GameServer merge(GameServer detachedInstance) {
		log.debug("merging GameServer instance");
		try {
			GameServer result = (GameServer) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GameServer instance) {
		log.debug("attaching dirty GameServer instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GameServer instance) {
		log.debug("attaching clean GameServer instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static GameServerDAO getFromApplicationContext(ApplicationContext ctx) {
		return (GameServerDAO) ctx.getBean("GameServerDAO");
	}
}