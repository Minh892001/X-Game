/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author BruceSu
 * 
 */
public class YuanBaoChargeDAO extends HibernateDaoSupport {//TODO 暂未使用
	private static final Logger log = LoggerFactory
			.getLogger(YuanBaoChargeDAO.class);

	public static YuanBaoChargeDAO getFromApplicationContext(ApplicationContext ctx) {
		return (YuanBaoChargeDAO) ctx.getBean("YuanBaoChargeDAO");
	}

	public void save(YuanBaoCharge transientInstance) {
		try {
			getHibernateTemplate().save(transientInstance);
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
}
