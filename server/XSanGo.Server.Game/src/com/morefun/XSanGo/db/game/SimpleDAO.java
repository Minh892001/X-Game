/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 单表操作类，支持简单增删改和无条件查询
 * 
 * @author BruceSu
 * 
 */
public class SimpleDAO extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory.getLogger(SimpleDAO.class);

	/**
	 * 查询指定类型的数据
	 * 
	 * @param type
	 * @return 
	 */
	public <T> List<T> findAll(Class<T> type) {
		try {
			String queryString = "from " + type.getSimpleName();
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/**
	 * 执行指定SQL和对象的数据
	 * 
	 * @param sql
	 * @param type
	 * @return 
	 */
	public <T> List<T> executeSql(final String sql, final Class<T> type) {
		try {
			return getHibernateTemplate().execute(new HibernateCallback<List<T>>() {

				@Override
				public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery(sql);
					query.addEntity(type);
					return (List<T>) query.list();
				}
			});
		} catch (RuntimeException re) {
			log.error("executeSql failed:" + sql, re);
			throw re;
		}
	}

	/**
	 * 数据保存
	 * 
	 * @param transientInstance 
	 */
	public void save(Serializable transientInstance) {
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	/**
	 * 批量保存
	 * 
	 * @param objs 
	 */
	public void batchSave(List<? extends Serializable> objs) {
		if (objs == null || objs.size() <= 0) {
			return;
		}
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction tr = session.beginTransaction();
		try {
			int index = 0;
			for (Serializable obj : objs) {
				session.save(obj);
				if ((++index) % 50 == 0) {
					session.flush();
					session.clear();
				}
			}

			tr.commit();
		} catch (Exception e) {
			if (tr != null) {
				tr.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 数据更新
	 * 
	 * @param transientInstance 
	 */
	public void update(Serializable transientInstance) {
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	/**
	 * 删除指定数据
	 * 
	 * @param persistentInstance 
	 */
	public void delete(Serializable persistentInstance) {
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	/**
	 * 批量删除
	 * 
	 * @param list 
	 */
	public void deleteAll(Collection<? extends Serializable> list) {
		if (list.isEmpty()) {
			return;
		}
		try {
			getHibernateTemplate().deleteAll(list);
			log.debug("delete all successful");
		} catch (Exception e) {
			log.error("delete all failed", e);
			throw e;
		}
	}
	

	/**
	 * 清除表数据接口
	 * 
	 * @param table_name
	 */
	public void deleteAll(final String table_name) {
		try {
			getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session) throws HibernateException, SQLException {
					String sqlStr = "delete from " + table_name;
					return session.createSQLQuery(sqlStr).executeUpdate();
				}
			});
		} catch (RuntimeException e) {
			log.error("clear all " + table_name + " failed", e);
			throw e;
		}
	}
	

	/**
	 * 功能描述
	 * @param type
	 * @param id
	 * @return 
	 */
	public <T> T findById(Class<T> type, Serializable id) {
		try {
			T instance = getHibernateTemplate().get(type, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/**
	 * 数据插入或者更新
	 * 
	 * @param instance 
	 */
	public void attachDirty(Serializable instance) {
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/**
	 * 按照时间，取最近的指定数量记录
	 * 
	 * @param type
	 * @return
	 */
	public <T> List<T> findByParam(Class<T> type, String whereKey, String whereValue, String orderParam, int limitNum) {
		try {
			String queryString = "from " + type.getSimpleName() + " where " + whereKey + "= " + whereValue
					+ " order by " + orderParam + " desc limit " + limitNum;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public static SimpleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SimpleDAO) ctx.getBean("SimpleDAO");
	}

}
