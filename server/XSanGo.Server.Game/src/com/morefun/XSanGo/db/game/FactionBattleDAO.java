/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleDAO
 * 功能描述：
 * 文件名：FactionBattleDAO.java
 **************************************************
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 公会战数据操作
 * 
 * @author zwy
 * @since 2015-12-31
 * @version 1.0
 */
@Repository("FactionBattleDAO")
public class FactionBattleDAO extends HibernateDaoSupport {
	/** 日志 */
	private static final Logger log = LoggerFactory.getLogger(FactionBattleDAO.class);

	/**
	 * 查询所有公会战数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FactionBattle> findAllFactionBattle() {
		try {
			return this.getHibernateTemplate().find("from FactionBattle");
		} catch (RuntimeException e) {
			log.error("find all faction_battle failed", e);
			throw e;
		}
	}

	// /**
	// * 查询公会战参战成员数据
	// *
	// * @return
	// */
	// @SuppressWarnings("unchecked")
	// public List<FactionBattleMember> findAllFactionBattleMember() {
	// try {
	// return this.getHibernateTemplate().find("from FactionBattleMember");
	// } catch (RuntimeException e) {
	// log.error("find all faction_battle_member failed", e);
	// throw e;
	// }
	// }

	// /**
	// * 查询公会战据点数据
	// *
	// * @return
	// */
	// @SuppressWarnings("unchecked")
	// public List<FactionBattleStronghold> findAllFactionBattleStronghold() {
	// try {
	// return this.getHibernateTemplate().find("from FactionBattleStronghold");
	// } catch (RuntimeException e) {
	// log.error("find all faction_battle_stronghold failed", e);
	// throw e;
	// }
	// }

	/**
	 * 查询公会战随机事件数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FactionBattleEvent> findAllFactionBattleEvent() {
		try {
			return this.getHibernateTemplate().find("from FactionBattleEvent");
		} catch (RuntimeException e) {
			log.error("find all faction_battle_event failed", e);
			throw e;
		}
	}

	/**
	 * 查询公会战随机事件产出概率数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FactionBattleEventRatio> findAllFactionBattleEventRatio() {
		try {
			return this.getHibernateTemplate().find("from FactionBattleEventRatio");
		} catch (RuntimeException e) {
			log.error("find all faction_battle_event_ratio failed", e);
			throw e;
		}
	}

	/**
	 * 查询公会战日志记录
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FactionBattleLog> findAllFactionBattleLog() {
		try {
			return this.getHibernateTemplate().find("from FactionBattleLog");
		} catch (RuntimeException e) {
			log.error("find all faction_battle_log failed", e);
			throw e;
		}
	}

	// /**
	// * 保存公会战公会参与数据
	// *
	// * @param fb
	// */
	// public void saveFactionBattle(FactionBattle fb) {
	// try {
	// this.getHibernateTemplate().saveOrUpdate(fb);
	// } catch (RuntimeException e) {
	// log.error("save faction_battle failed", e);
	// throw e;
	// }
	// }

	// /**
	// * 保存公会战参战成员数据
	// *
	// * @param fbm
	// */
	// public void saveFactionBattleMember(FactionBattleMember fbm) {
	// try {
	// this.getHibernateTemplate().saveOrUpdate(fbm);
	// } catch (RuntimeException e) {
	// log.error("save faction_battle_member failed", e);
	// throw e;
	// }
	// }

	// /**
	// * 保存公会战据点数据
	// *
	// * @param fbs
	// */
	// public void saveFactionBattleStronghold(FactionBattleStronghold fbs) {
	// try {
	// this.getHibernateTemplate().saveOrUpdate(fbs);
	// } catch (RuntimeException e) {
	// log.error("save faction_battle_stronghold failed", e);
	// throw e;
	// }
	// }

	/**
	 * 保存公会战随机事件数据
	 * 
	 * @param fbe
	 */
	public void saveFactionBattleEvent(FactionBattleEvent fbe) {
		try {
			this.getHibernateTemplate().saveOrUpdate(fbe);
		} catch (RuntimeException e) {
			log.error("save faction_battle_event failed", e);
			throw e;
		}
	}

	/**
	 * 保存公会战随机事件产出概率数据
	 * 
	 * @param fbe
	 */
	public void saveFactionBattleEventRatio(FactionBattleEventRatio fber) {
		try {
			this.getHibernateTemplate().saveOrUpdate(fber);
		} catch (RuntimeException e) {
			log.error("save faction_battle_event_ratio failed", e);
			throw e;
		}
	}

	/**
	 * 保存公会战日志
	 * 
	 * @param fblog
	 */
	public void saveFactionBattleLog(FactionBattleLog fblog) {
		try {
			this.getHibernateTemplate().saveOrUpdate(fblog);
		} catch (RuntimeException e) {
			log.error("save faction_battle_log failed", e);
			throw e;
		}
	}

	/**
	 * 清除表数据接口
	 * 
	 * @param table_name
	 */
	private void deleteAll(final String table_name) {
		try {
			getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session) throws HibernateException, SQLException {
					String sqlStr = "delete from " + table_name;
					return session.createSQLQuery(sqlStr).executeUpdate();
				}
			});
		} catch (RuntimeException e) {
			log.error("clear all " + table_name + " failed", e);
			throw e;
		}
	}

	/**
	 * 清理公会战参战公会数据
	 */
	public void clearAllFactionBattle() {
		deleteAll("faction_battle");
	}

	/**
	 * 清除公会战所有参战成员数据
	 * 
	 */
	public void clearAllFactionBattleMember() {
		deleteAll("faction_battle_member");
	}

	// /**
	// * 清除公会战所有据点数据
	// *
	// */
	// public void clearAllFactionBattleStronghold() {
	// deleteAll("faction_battle_stronghold");
	// }

	// /**
	// * 清除公会战据点数据
	// *
	// * @param fbs
	// */
	// public void clearFactionBattleStronghold(FactionBattleStronghold fbs) {
	// try {
	// this.getHibernateTemplate().delete(fbs);
	// } catch (RuntimeException e) {
	// log.error("clear faction_battle_stronghold failed", e);
	// throw e;
	// }
	// }

	/**
	 * 清除公会战随机事件数据
	 * 
	 * @param fbe
	 */
	public void clearFactionBattleEvent(FactionBattleEvent fbe) {
		try {
			this.getHibernateTemplate().delete(fbe);
		} catch (RuntimeException e) {
			log.error("clear faction_battle_event failed", e);
			throw e;
		}
	}

	/**
	 * 清除公会战随机事件数据
	 * 
	 */
	public void clearFactionBattleEvent() {
		deleteAll("faction_battle_event");
	}

	/**
	 * 清除公会战随机事件产出概率数据
	 * 
	 * @param list
	 */
	public void clearFactionBattleEventRatio(Collection<FactionBattleEventRatio> list) {
		try {
			this.getHibernateTemplate().deleteAll(list);
		} catch (RuntimeException e) {
			log.error("clear all faction_battle_event_ratio failed", e);
			throw e;
		}
	}

	/**
	 * 清除公会战随机事件产出概率数据
	 * 
	 * @param fbs
	 */
	public void clearAllFactionBattleEventRatio() {
		deleteAll("faction_battle_event_ratio");
	}

	/**
	 * 清除本次公会战所有日志记录
	 */
	public void clearFactionBattleLog() {
		deleteAll("faction_battle_log");
	}

	/**
	 * 获得公会战数据库实例对象
	 * 
	 * @param ctx
	 * @return
	 */
	public static FactionBattleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (FactionBattleDAO) ctx.getBean("FactionBattleDAO");
	}

	@Resource(name = "sessionFactory")
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
