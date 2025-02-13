/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
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

/**
 * 老友召回被召
 * @author yangzz
 * 
 */
@Repository("RoleFriendsRecalledDao")
public class RoleFriendsRecalledDao extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(RoleFriendsRecalledDao.class);


	public static RoleFriendsRecalledDao getFromApplicationContext(ApplicationContext ctx) {
		return (RoleFriendsRecalledDao) ctx.getBean("RoleFriendsRecalledDao");
	}
	
	/**
	 * 查找邀请和被邀请人ID
	 * 
	 * */
	public List<Object[]> findRecalledFriends() {
		List<Object[]> roleIdList = getHibernateTemplate().execute(new HibernateCallback<List<Object[]>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery("SELECT role_id, invite_role_id FROM `role_friends_recalled` ");
				query.addScalar("role_id", Hibernate.STRING); // 返回值类型
				query.addScalar("invite_role_id", Hibernate.STRING); // 返回值类型
				return (List<Object[]>) query.list();
			}
		});

		return roleIdList;
	}
	
	@Resource(name = "sessionFactory")
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
