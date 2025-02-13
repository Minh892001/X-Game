package com.morefun.XSanGo.db.game;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class WorshipRankDAO extends HibernateDaoSupport {

	@SuppressWarnings("unchecked")
	public WorshipRank findByRoleId(String roleId) {
		List<WorshipRank> worshipRanks = this.getHibernateTemplate().find(
				"from WorshipRank where roleId=?", roleId);
		if (worshipRanks.isEmpty()) {
			return null;
		}
		return worshipRanks.get(0);
	}

	public void save(WorshipRank worshipRank) {
		this.getHibernateTemplate().save(worshipRank);
	}

	public void update(WorshipRank worshipRank) {
		this.getHibernateTemplate().saveOrUpdate(worshipRank);
	}

	public static WorshipRankDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (WorshipRankDAO) ctx.getBean("WorshipRankDAO");
	}
}
