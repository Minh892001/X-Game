/**
 * 
 */
package com.morefun.XSanGo.db.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Use SimpleDAO instead.
 * 
 * @author BruceSu
 * 
 */
public class MailDAO extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory.getLogger(MailDAO.class);
	
	/**
	 * 删除过期邮件
	 * @param passDate
	 */
	public void deletePass(final String passDate) {
		try {
			String queryString = "from Mail where create_time < '" + passDate + "'";
			getHibernateTemplate().deleteAll(getHibernateTemplate().find(queryString));
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public static MailDAO getFromApplicationContext(ApplicationContext ctx) {
		return (MailDAO) ctx.getBean("MailDAO");
	}

}
