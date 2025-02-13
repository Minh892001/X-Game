package com.morefun.XSanGo.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CrossArenaMovieDAO extends HibernateDaoSupport {

	/**
	 * 删除所有
	 * 
	 * @return
	 */
	public int deleteAll() {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "delete from CrossArenaMovie";
				int res = session.createQuery(sql).executeUpdate();
				return res;
			}
		});
	}

	public static CrossArenaMovieDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CrossArenaMovieDAO) ctx.getBean("CrossArenaMovieDAO");
	}

	@SuppressWarnings("unchecked")
	public List<CrossArenaRank> findAll() {
		return getHibernateTemplate().find("from CrossArenaMovie");
	}

	public void save(CrossArenaMovie movie) {
		getHibernateTemplate().save(movie);
	}

	public CrossArenaMovie get(String id) {
		return getHibernateTemplate().get(CrossArenaMovie.class, id);
	}

	public int deletePast(final Date date) {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "delete from CrossArenaMovie where datetime < ?";
				Query query = session.createQuery(sql);
				query.setTimestamp(0, date);
				return query.executeUpdate();
			}
		});
	}

}
