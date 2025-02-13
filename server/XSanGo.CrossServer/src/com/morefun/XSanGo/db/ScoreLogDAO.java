package com.morefun.XSanGo.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ScoreLogDAO extends HibernateDaoSupport {

	public static ScoreLogDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ScoreLogDAO) ctx.getBean("ScoreLogDAO");
	}

	@SuppressWarnings("unchecked")
	public List<ScoreLog> findAll() {
		return getHibernateTemplate().find("from ScoreLog");
	}

}
