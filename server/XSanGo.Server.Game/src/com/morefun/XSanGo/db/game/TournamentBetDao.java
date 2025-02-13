package com.morefun.XSanGo.db.game;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author guofeng.qin
 */
@Repository("TournamentDao")
public class TournamentBetDao extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(TournamentBetDao.class);

	public List<RoleTournamentBet> getAllTourmentBet() {
		log.debug("find all RoleTournamentBet.");
		try {
			String sql = "from RoleTournamentBet";
			return getHibernateTemplate().find(sql);
		} catch (RuntimeException e) {
			log.error("TournamentBetDao RoleTournamentBet error.", e);
			throw e;
		}
	}

	public void save(RoleTournamentBet bet) {
		log.debug("save RoleTournamentBet.");
		try {
			getHibernateTemplate().saveOrUpdate(bet);
		} catch (RuntimeException e) {
			log.error("save RoleTournamentBet error.", e);
			throw e;
		}
	}

	public void delete(RoleTournamentBet bet) {
		log.debug("delete RoleTournamentBet.");
		try {
			getHibernateTemplate().delete(bet);
		} catch (RuntimeException e) {
			log.error("delete RoleTournamentBet error.", e);
			throw e;
		}
	}

	public static TournamentBetDao getFromApplicationContext(ApplicationContext ctx) {
		return (TournamentBetDao) ctx.getBean("TournamentDao");
	}
	
	@Resource(name = "sessionFactory")
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
