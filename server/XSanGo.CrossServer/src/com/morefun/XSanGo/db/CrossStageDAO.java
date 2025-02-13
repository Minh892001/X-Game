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

public class CrossStageDAO extends HibernateDaoSupport {

	/**
	 * 删除所有
	 * 
	 * @return
	 */
	public int deleteAll() {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "delete from CrossStage";
				int res = session.createQuery(sql).executeUpdate();
				return res;
			}
		});
	}

	public static CrossStageDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CrossStageDAO) ctx.getBean("CrossStageDAO");
	}

	@SuppressWarnings("unchecked")
	public List<CrossStage> findAll() {
		return getHibernateTemplate().find("from CrossStage");
	}

	public void save(CrossStage stage) {
		getHibernateTemplate().save(stage);
	}

	public void update(CrossStage stage) {
		getHibernateTemplate().update(stage);
	}

}
