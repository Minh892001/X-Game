package com.morefun.XSanGo.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CrossLogDAO extends HibernateDaoSupport {

	public static CrossLogDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CrossLogDAO) ctx.getBean("CrossLogDAO");
	}

	@SuppressWarnings("unchecked")
	public List<CrossLog> findAll() {
		return getHibernateTemplate().find("from CrossLog order by createDate desc");
	}

	public void save(CrossLog crossLog) {
		getHibernateTemplate().save(crossLog);
	}

}
