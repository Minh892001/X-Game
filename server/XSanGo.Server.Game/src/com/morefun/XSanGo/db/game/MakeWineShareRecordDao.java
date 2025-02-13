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
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.morefun.XSanGo.makewine.XsgMakeWineManager;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 煮酒论英雄 分享记录
 * 
 * @author zhuzhi.yang
 * 
 */
@Repository("MakeWineShareRecordDao")
public class MakeWineShareRecordDao extends HibernateDaoSupport {
	
	private static final Logger log = LoggerFactory.getLogger(MakeWineShareRecordDao.class);
	
	@SuppressWarnings("unchecked")
	public List<RoleMakeWineShareRecord> findAll() {
		log.debug("finding all RoleMakeWineShareRecord instances");
		try {
			final String queryString2 = "from RoleMakeWineShareRecord";
			return getHibernateTemplate().find(queryString2);
		} catch (RuntimeException re) {
			log.error("find RoleMakeWineShareRecord all failed", re);
			throw re;
		}
	}
	
	/**
	 * 分享界面显示
	 * @param roleFilter
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoleMakeWineShareRecord> findRecords(final String roleFilter, final int startIndex) {
		log.debug("finding all RoleMakeWineShareRecord instances");
		try {
			List<RoleMakeWineShareRecord> list_record = getHibernateTemplate().executeFind(new HibernateCallback<List<RoleMakeWineShareRecord>>() {

				@Override
				public List<RoleMakeWineShareRecord> doInHibernate(Session session)
						throws HibernateException, SQLException {
					StringBuffer sql = new StringBuffer("select * from role_makewine_share_record where 1 = 1 and last_count > 0");
					if(roleFilter != null && roleFilter.trim().length() > 0) {
						sql.append(" and role_id in (").append(roleFilter).append(")");
					}
					sql.append(" order by config_id desc, top_time desc, last_count desc limit " + startIndex + "," + XsgMakeWineManager.getInstance().RANK_LIMIT);
					Query query = session.createSQLQuery(sql.toString());
					
					List<Object[]> list_rs = query.list();
					List<RoleMakeWineShareRecord> list = new ArrayList<RoleMakeWineShareRecord>();
					for(Object[] obj : list_rs) {
						RoleMakeWineShareRecord record = new RoleMakeWineShareRecord();
						record.setId(String.valueOf(obj[0]));
						record.setRoleId(String.valueOf(obj[1]));
						record.setConfigID(Integer.parseInt(String.valueOf(obj[2])));
						record.setRoleName(String.valueOf(obj[3]));
						record.setLastCount(Integer.parseInt(String.valueOf(obj[4])));
						record.setTop(Integer.parseInt(String.valueOf(obj[5])));
						record.setTopTime((Date) obj[6]);
						record.setReceivedPlayers(String.valueOf(obj[7]));
						record.setShareTime((Date) obj[8]);
						list.add(record);
					}
					return list;
				}
				
			});
			
			return list_record;
		} catch (Exception re) {
			re.printStackTrace();
			log.error("find RoleMakeWineShareRecord all failed", re);
			return null;
		}
	}
	
	/**
	 * 获取总记录数
	 * @param roleFilter
	 * @return
	 */
	public int findTotalRecords(final String roleFilter) {
		try {
			int result = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

				@Override
				public Integer doInHibernate(Session session)
						throws HibernateException, SQLException {
					StringBuffer sql = new StringBuffer("select count(1) from role_makewine_share_record where 1 = 1");
					if(roleFilter != null && roleFilter.trim().length() > 0) {
						sql.append(" and role_id in (").append(roleFilter).append(")");
					}
					Object res = session.createSQLQuery(sql.toString()).uniqueResult();
					if (res == null) {
						return 0;
					}
					return NumberUtil.parseInt(res.toString());
				}
				
			});
			
			return result;
		} catch (RuntimeException re) {
			log.error("find RoleMakeWineShareRecord all failed", re);
			return 0;
		}
	}
	
	/**
	 * 根据ID查找对象
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RoleMakeWineShareRecord findById(String id) {
		log.debug("finding all RoleMakeWineShareRecord instances");
		try {
			final String queryString2 = "from RoleMakeWineShareRecord where id = ?";
			List<RoleMakeWineShareRecord> list_record = getHibernateTemplate().find(queryString2, id);
			if (list_record != null && list_record.size() > 0) {
				return list_record.get(0);
			}
			return null;
		} catch (RuntimeException re) {
			log.error("find RoleMakeWineShareRecord all failed", re);
			throw re;
		}
	}
	
	/**
	 * 查找当日置顶的次数
	 * @return
	 */
	public int findTodayTopCount(final String roleId, final String date) {
		log.debug("findTodayTopCount RoleMakeWineShareRecord instances" + date);
		try {
			int count = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

				@Override
				public Integer doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sql = "select count(1) from role_makewine_share_record where role_id = ? and date(top_time) = ?";
					SQLQuery query = session.createSQLQuery(sql); 
					query.setString(0, roleId);
					query.setString(1, date);
					Object res = query.uniqueResult();
					if (res == null) {
						return 0;
					}
					return NumberUtil.parseInt(res.toString());
				}
				
			});
			
			return count;
		} catch (RuntimeException re) {
			log.error("find RoleMakeWineShareRecord all failed", re);
			return 0;
		}
	}
	
	public void save(RoleMakeWineShareRecord makeWine) {
		log.debug("saving RoleMakeWineShareRecord instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(makeWine);
			log.debug("save RoleMakeWineShareRecord successful");
		} catch (RuntimeException re) {
			log.error("save RoleMakeWineShareRecord failed", re);
			throw re;
		}
	}
	
	public int delete() {
		log.debug("clear RoleMakeWineShareRecord Table");
		try {
			int result = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sqlStr = "delete from role_makewine_share_record";
					return session.createSQLQuery(sqlStr).executeUpdate();
				}
			});
			log.debug("clear RoleMakeWineShareRecord successful");
			return result;
		} catch (RuntimeException re) {
			log.error("clear RoleMakeWineShareRecord failed", re);
			throw re;
		}
	}
	
	public static MakeWineShareRecordDao getFromApplicationContext(ApplicationContext ctx) {
		return (MakeWineShareRecordDao) ctx.getBean("MakeWineShareRecordDao");
	}
	
	@Resource(name = "sessionFactory")
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
