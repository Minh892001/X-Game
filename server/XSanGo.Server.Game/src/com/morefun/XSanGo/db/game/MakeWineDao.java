/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.morefun.XSanGo.makewine.XsgMakeWineManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 煮酒论英雄
 * 
 * @author zhuzhi.yang
 * 
 */
@Repository("MakeWineDao")
public class MakeWineDao extends HibernateDaoSupport {
	
	private static final Logger log = LoggerFactory.getLogger(MakeWineDao.class);
	
	@SuppressWarnings("unchecked")
	public List<RoleMakeWine> findAll() {
		log.debug("finding all MakeWine instances");
		try {
			final String queryString2 = "from MakeWine";
			return getHibernateTemplate().find(queryString2);
		} catch (RuntimeException re) {
			log.error("find MakeWine all failed", re);
			throw re;
		}
	}
	
	/**
	 * 查询排行榜中的数据
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoleMakeWine> findRankData() {
		log.debug("finding all MakeWine instances");
		try {
			final String queryString2 = "select * from role_makewine order by (compose_score + share_score) desc ";
			List<RoleMakeWine> list = getHibernateTemplate().executeFind(new HibernateCallback<List<RoleMakeWine>>() {

				@Override
				public List<RoleMakeWine> doInHibernate(Session session)
						throws HibernateException, SQLException {
					List<RoleMakeWine> list_make_wine = new ArrayList<RoleMakeWine>();
					Query query = session.createSQLQuery(queryString2);
					List<Object[]> list_rs = query.list();
					for(Object[] obj : list_rs) {
						RoleMakeWine mw = new RoleMakeWine();
						mw.setRoleId(String.valueOf(obj[0]));
						mw.setName(String.valueOf(obj[1]));
						mw.setVip(Integer.valueOf(String.valueOf(obj[2])));
						mw.setLevel(Integer.valueOf(String.valueOf(obj[3])));
						mw.setHeadImg(String.valueOf(obj[4]));
						mw.setComposeScore(Integer.valueOf(String.valueOf(obj[5])));
						mw.setShareScore(Integer.valueOf(String.valueOf(obj[6])));
						mw.setExchangeUsedScore(Integer.valueOf(String.valueOf(obj[7])));
						mw.setReceiveMaterialDate((Date)(obj[8]));
						mw.setReceiveSocre(Integer.valueOf(String.valueOf(obj[9])));
						mw.setItemComposedCount(String.valueOf(obj[10]));
						mw.setReceiveShareDate((Date)(obj[11]));
						mw.setTopedTimes(Integer.valueOf(String.valueOf(obj[12])));
						mw.setResetDate((Date)(obj[13]));
						list_make_wine.add(mw);
					}
					return list_make_wine;
				}
				
			});
			return list;
		} catch (RuntimeException re) {
			log.error("find MakeWine all failed", re);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public RoleMakeWine findByRole(String roleId) {
		log.debug("finding all MakeWine instances");
		try {
			final String queryString2 = "from MakeWine where roleId = ?";
			List<RoleMakeWine> list = getHibernateTemplate().find(queryString2, roleId);
			if(list != null && list.size() > 0) {
				return list.get(0);
			}
			return null;
		} catch (RuntimeException re) {
			log.error("find MakeWine all failed", re);
			throw re;
		}
	}
	
	public void save(RoleMakeWine makeWine) {
		log.debug("saving MakeWine instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(makeWine);
			log.debug("save MakeWine successful");
		} catch (RuntimeException re) {
			log.error("save MakeWine failed", re);
			throw re;
		}
	}
	
	/**
	 * 活动结束(活动列表的时间)
	 * @return
	 */
	public int clearData() {
		log.debug("clear MakeWine data");
		try {
			int result = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sqlStr = "update role_makewine set compose_score = 0, share_score = 0, " +
							"receive_material_date = null, receive_socre = 0, " +
							"item_composed_count = ' " + TextUtil.GSON.toJson(XsgMakeWineManager.getInstance().DEFAULT_COMPOSED_COUNT) + " ', " +
									"receive_share_date = null, reset_date = null";
					return session.createSQLQuery(sqlStr).executeUpdate();
				}
			});
			log.debug("clear MakeWine successful");
			return result;
		} catch (RuntimeException re) {
			log.error("clear MakeWine failed", re);
			throw re;
		}
	}
	
	public static MakeWineDao getFromApplicationContext(ApplicationContext ctx) {
		return (MakeWineDao) ctx.getBean("MakeWineDao");
	}
	
	@Resource(name = "sessionFactory")
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
