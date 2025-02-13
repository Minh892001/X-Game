/**
 * 
 */
package com.morefun.XSanGo.db;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CrossRankDAO extends HibernateDaoSupport {

	/**
	 * 删除所有
	 * 
	 * @return
	 */
	public int deleteAll() {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "delete from CrossRank";
				int res = session.createQuery(sql).executeUpdate();
				return res;
			}
		});
	}

	public static CrossRankDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CrossRankDAO) ctx.getBean("CrossRankDAO");
	}

	@SuppressWarnings("unchecked")
	public List<CrossRank> findAll() {
		return getHibernateTemplate().find("from CrossRank order by score desc,updateDate");
	}

	public void save(CrossRank rank) {
		getHibernateTemplate().save(rank);
	}

	public void update(CrossRank rank) {
		getHibernateTemplate().update(rank);
	}

}
