/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 全服首次公告  数据库结构
 * 
 * @author sunjie
 * 
 */
public class FirstServerNotifyDao extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory
			.getLogger(FirstServerNotifyDao.class);

	@SuppressWarnings("unchecked")
	public Map<Integer,AchieveFirstNotify> findAll() {
		Map<Integer,AchieveFirstNotify> notifyMap;
		try {
			notifyMap = getHibernateTemplate().execute(
					new HibernateCallback<Map<Integer,AchieveFirstNotify>>() {
						@Override
						public Map<Integer,AchieveFirstNotify> doInHibernate(
								Session session) throws HibernateException,
								SQLException {
							String sqlStr = "select id, achieve_id, role_id, update_time from achieve_first_notify";
							Map<Integer,AchieveFirstNotify> map = new HashMap<Integer,AchieveFirstNotify>();
							List<Object[]> query = session.createSQLQuery(
									sqlStr).list();
							for (Object[] obj : query) {
								AchieveFirstNotify notify = new AchieveFirstNotify();
								notify.setId(String.valueOf(obj[0]));
								notify.setAchieveId(Integer.parseInt(String
										.valueOf(obj[1])));
								notify.setRoleId(String.valueOf(obj[2]));
								notify.setUpdateDate(DateUtil
											.parseDate(String.valueOf(obj[3])));
								map.put(notify.getAchieveId(), notify);
							}
							return map;
						}

					});
		} catch (RuntimeException re) {
			log.error("find achieveNotify failed", re);
			throw re;
		}
		return notifyMap;
	}

	public void save(AchieveFirstNotify achieveFirstNotify) {
		log.debug("saving achieveFirstNotify instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(achieveFirstNotify);
			log.debug("save achieveFirstNotify successful");
		} catch (RuntimeException re) {
			log.error("save achieveFirstNotify failed", re);
			throw re;
		}
	}

	public static FirstServerNotifyDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (FirstServerNotifyDao) ctx.getBean("FirstServerNotifyDao");
	}
}
