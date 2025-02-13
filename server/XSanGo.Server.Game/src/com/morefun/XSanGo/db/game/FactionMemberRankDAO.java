/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 公会个人荣誉排行榜DAO
 * 
 * @author lixiongming
 *
 */
public class FactionMemberRankDAO extends HibernateDaoSupport {

	/**
	 * 清空排行榜
	 * 
	 * @param persistentInstance
	 */
	public void deleteAll() {
		getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sqlStr = "delete from faction_member_rank";
				return session.createSQLQuery(sqlStr).executeUpdate();
			}
		});
	}

	public static FactionMemberRankDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (FactionMemberRankDAO) ctx.getBean("FactionMemberRankDAO");
	}
}
