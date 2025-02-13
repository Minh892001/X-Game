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

public class ScheduleDAO extends HibernateDaoSupport {

	/**
	 * 删除所有
	 * 
	 * @return
	 */
	public int deleteAll() {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "delete from Schedule";
				int res = session.createQuery(sql).executeUpdate();
				return res;
			}
		});
	}

	public static ScheduleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ScheduleDAO) ctx.getBean("ScheduleDAO");
	}

	public void save(Schedule schedule) {
		getHibernateTemplate().save(schedule);
	}

	public void update(Schedule schedule) {
		getHibernateTemplate().update(schedule);
	}
	
	@SuppressWarnings("unchecked")
	public List<Schedule> findAll(){
		return getHibernateTemplate().find("from Schedule order by crossId,stage desc,orderNum");
	}

}
