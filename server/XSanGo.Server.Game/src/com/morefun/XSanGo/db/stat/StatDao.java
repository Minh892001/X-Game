/**
 * 
 */
package com.morefun.XSanGo.db.stat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 统计数据库操作对象
 * 
 * @author sulingyun
 * 
 */
public class StatDao extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(StatDao.class);

	public void save(Object transientInstance) {
		if (!this.getClass().getPackage()
				.equals(transientInstance.getClass().getPackage())) {
			throw new IllegalArgumentException(
					"Only support the class which is in the same package with the DAO.");
		}

		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {

			log.error("save failed", re);
			throw re;
		}
	}

	public static StatDao getFromApplicationContext(ApplicationContext ctx) {
		return (StatDao) ctx.getBean("StatDao");
	}
}
