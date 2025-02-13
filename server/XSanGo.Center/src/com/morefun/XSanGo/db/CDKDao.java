/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author BruceSu
 * 
 */
public class CDKDao extends HibernateDaoSupport {
	public static CDKDao getFromApplicationContext(ApplicationContext ctx) {
		return (CDKDao) ctx.getBean("CDKDao");
	}

	public CDKDetail findDetailByCDK(String cdk) {
		try {
			CDKDetail instance = (CDKDetail) getHibernateTemplate().get(CDKDetail.class, cdk);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public CDKGroup findCDKGroupById(int id) {
		try {
			CDKGroup instance = (CDKGroup) getHibernateTemplate().get(CDKGroup.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachDirty(CDKDetail instance) {
		try {
			getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public CDKGroup loadCdk(String prefix) {
		Transaction transation = null;
		try {
			Session session = this.getSession();
			transation = session.beginTransaction();
			String queryString = "from CDKGroup as model where model.category = ?";
			CDKGroup g = (CDKGroup) session.createQuery(queryString).setString(0, prefix).uniqueResult();
			g.getCdks().size();
			transation.commit();
			session.close();
			return g;
		} catch (RuntimeException re) {
			if (transation != null) {
				transation.rollback();
			}
			throw re;
		}
	}

	public void update(CDKGroup group) {
		this.getHibernateTemplate().saveOrUpdate(group);
	}

	public void save(CDKGroup transientInstance) {
		Transaction transation = null;
		try {
			Session session = this.getSession();
			transation = session.beginTransaction();
			session.save(transientInstance);
			transation.commit();
			session.close();
		} catch (RuntimeException re) {
			if (transation != null) {
				transation.rollback();
			}

			throw re;
		}
	}

	public void delete(CDKGroup group) {
		Transaction transation = null;
		try {
			Session session = this.getSession();
			transation = session.beginTransaction();
			session.delete(group);
			;
			transation.commit();
			session.close();
		} catch (RuntimeException re) {
			if (transation != null) {
				transation.rollback();
			}
			throw re;
		}
	}
	
	
	/**
	 * 根据CDK查找group_id和use_time
	 * 
	 * */
	public List<Object[]> findCdkUseTimeAndGroupId(final String cdk) {
		List<Object[]> results = getHibernateTemplate().execute(new HibernateCallback<List<Object[]>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery("SELECT group_id, use_time FROM `cdk_detail` where cdk = ? ");
				query.setString(0, cdk);
				query.addScalar("group_id", Hibernate.INTEGER); // 返回值类型
				query.addScalar("use_time", Hibernate.INTEGER); // 返回值类型
				return (List<Object[]>) query.list();
			}
		});

		return results;
	}
	
	public CDKGroup loadCdkById(int id) {
		Transaction transation = null;
		try {
			Session session = this.getSession();
			transation = session.beginTransaction();
			String queryString = "from CDKGroup as model where model.id = ?";
			CDKGroup g = (CDKGroup) session.createQuery(queryString).setInteger(0, id).uniqueResult();
			g.getCdks().size();
			transation.commit();
			session.close();
			return g;
		} catch (RuntimeException re) {
			if (transation != null) {
				transation.rollback();
			}
			throw re;
		}
	}
}
