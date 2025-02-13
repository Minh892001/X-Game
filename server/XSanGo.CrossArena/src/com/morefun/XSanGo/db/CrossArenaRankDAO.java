/**
 * 
 */
package com.morefun.XSanGo.db;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.CrossArenaManager;

public class CrossArenaRankDAO extends HibernateDaoSupport {

	public static CrossArenaRankDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CrossArenaRankDAO) ctx.getBean("CrossArenaRankDAO");
	}

	@SuppressWarnings("unchecked")
	public List<CrossArenaRank> findToCache(final int rangeId) {
		List<CrossArenaRank> list = getHibernateTemplate().execute(new HibernateCallback<List<CrossArenaRank>>() {

			@Override
			public List<CrossArenaRank> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("from CrossArenaRank where rangeId =? order by rank");
				query.setInteger(0, rangeId);
				query.setMaxResults(CrossArenaManager.MAX_CACHE_NUM);
				return query.list();
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findRank() {
		return getHibernateTemplate().find("select rangeId, roleId, rank from CrossArenaRank");
	}

	public void save(CrossArenaRank rank) {
		getHibernateTemplate().save(rank);
	}

	public void update(CrossArenaRank rank) {
		getHibernateTemplate().update(rank);
	}
	
	public void saveOrUpdate(CrossArenaRank rank) {
		getHibernateTemplate().saveOrUpdate(rank);
	}

	@SuppressWarnings("unchecked")
	public CrossArenaRank getByRoleId(String roleId) {
		List<CrossArenaRank> list = getHibernateTemplate().find("from CrossArenaRank where roleId = ?", roleId);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
