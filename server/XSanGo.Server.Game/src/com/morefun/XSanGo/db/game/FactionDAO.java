/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.db.HibernateExtension;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author BruceSu
 * 
 */
public class FactionDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(FactionDAO.class);

	public List findAll() {
		log.debug("finding all Faction instances");
		try {
			String queryString = "from Faction";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public void save(Faction transientInstance) {
		log.debug("saving Faction instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Faction persistentInstance) {
		log.debug("deleting Faction instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Faction findById(String id) {
		log.debug("getting Faction instance with id: " + id);
		try {
			Faction instance = (Faction) getHibernateTemplate().get(
					Faction.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Faction merge(Faction detachedInstance) {
		log.debug("merging Faction instance");
		try {
			Faction result = (Faction) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Faction instance) {
		log.debug("attaching dirty Faction instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Faction instance) {
		log.debug("attaching clean Faction instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/**
	 * 自定义合并实现，将传入对象与数据库对象做匹配，来实现相应的增删改操作
	 * 
	 * @param instance
	 * @throws Exception
	 */
	public void customMerge(Faction instance) throws Exception {
		Transaction transation = null;
		Session session = null;
		try {
			// long t1 = System.currentTimeMillis();
			session = this.getSession();
			// throw new Exception();
			transation = session.beginTransaction();
			Faction old = this.findById(instance.getId());
			// long t2 = System.currentTimeMillis();
			HibernateExtension.update(session, old, instance);
			transation.commit();
		} catch (Exception re) {
			if (transation != null) {
				transation.rollback();
			}
			log.error(TextUtil.format("{0} merge failed.", instance.getId()),
					re);
			throw re;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查询全部的公会名称
	 * 
	 * @return
	 */
	public List<String> findAllName() {
		List<String> nameList;
		try {
			nameList = getHibernateTemplate().execute(
					new HibernateCallback<List<String>>() {
						@SuppressWarnings("unchecked")
						@Override
						public List<String> doInHibernate(Session session)
								throws HibernateException, SQLException {
							SQLQuery query = session
									.createSQLQuery("select name from faction");
							query.addScalar("name", Hibernate.STRING); // 返回值类型
							return (List<String>) query.list();
						}
					});
		} catch (RuntimeException re) {
			log.error("find Faction all failed", re);
			throw re;
		}

		return nameList;
	}

	public Faction findByName(String name) {
		List<Faction> list = findByProperty("name", name);
		if (list.size() > 1) {
			throw new IllegalStateException(name);
		}

		return list.size() > 0 ? list.get(0) : null;

	}

	public List findByProperty(String propertyName, Object value) {
		try {
			String queryString = "from Faction as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public static FactionDAO getFromApplicationContext(ApplicationContext ctx) {
		return (FactionDAO) ctx.getBean("FactionDAO");
	}
}
