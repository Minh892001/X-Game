package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("RoleCDKRecordDao")
public class RoleCDKRecordDao extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory.getLogger(RoleCDKRecordDao.class);


	public static RoleCDKRecordDao getFromApplicationContext(ApplicationContext ctx) {
		return (RoleCDKRecordDao) ctx.getBean("RoleCDKRecordDao");
	}
	
	/**
	 * 查找邀请和被邀请人ID
	 * 
	 * */
	public List<RoleCDKRecord> findRoleCDKRecordByCdk(final String cdk) {

		

		try {
			List<RoleCDKRecord> roleIdList = getHibernateTemplate().execute(new HibernateCallback<List<RoleCDKRecord>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<RoleCDKRecord> doInHibernate(Session session) throws HibernateException, SQLException {
					String sql = "SELECT c.id, c.category, c.cdk, c.create_time, c.role_id"
							+ " FROM role_cdk_record c WHERE c.cdk = :param";
					SQLQuery query = session.createSQLQuery(sql);
					query.addEntity(RoleCDKRecord.class);
					query.setString("param", cdk);
					return (List<RoleCDKRecord>) query.list();
				}
			});
			return roleIdList;

		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	
	
	}
	
	@Resource(name = "sessionFactory")
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
