/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.db.HibernateExtension;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author Yangzz
 * 
 */
public class ChatVoteForbiddenDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(ChatVoteForbiddenDAO.class);
	
	/**
	 * 查找自己一段时间内发起的被禁言记录
	 * @return
	 */
	public List<ChatVoteForbidden> findByRoleId(String roleId, Date startTime, Date endTime) {
		log.debug("finding all ChatVoteForbidden instances");
		try {
			String queryString = "from ChatVoteForbidden " +
					"where roleId = ? " +
					"and addTime >= ? and addTime < ? " +
					"order by addTime desc ";
			return getHibernateTemplate().find(queryString, roleId, startTime, endTime);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/**
	 * 查找目标一段时间内被禁言的记录
	 * @return
	 */
	public List<ChatVoteForbidden> findByTarget(String targetId, Date startTime, Date endTime) {
		log.debug("finding all ChatVoteForbidden instances");
		try {
			String queryString = "from ChatVoteForbidden " +
					"where targetId = ? " +
					"and addTime >= ? and addTime < ? " +
					"order by addTime desc ";
			return getHibernateTemplate().find(queryString, targetId, startTime, endTime);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * 查找有效的被禁言记录
	 * @return
	 */
	public List<ChatVoteForbidden> findValidVotes(Date startTime, Date endTime) {
		log.debug("finding all ChatVoteForbidden instances");
		try {
			String queryString = "from ChatVoteForbidden " +
					"where addTime >= ? and addTime < ? " +
					"order by addTime desc ";
			return getHibernateTemplate().find(queryString, startTime, endTime);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * 删除指定日期之前的记录
	 * @param date
	 * @return
	 */
	public int removeRecord(final Date date) {
		log.debug("remove drity Record");
		try {
			return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

				@Override
				public Integer doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sql = "delete from role_chat_vote_forbidden where add_time < ?";
					int res = session.createSQLQuery(sql).setDate(0, date)
							.executeUpdate();
					return res;
				}
			});
		} catch (RuntimeException re) {
			log.error("removeRecord failed", re);
			throw re;
		}
	}
	
	public List findAll() {
		log.debug("finding all ChatVoteForbidden instances");
		try {
			String queryString = "from ChatVoteForbidden";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public void save(ChatVoteForbidden transientInstance) {
		log.debug("saving ChatVoteForbidden instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ChatVoteForbidden persistentInstance) {
		log.debug("deleting ChatVoteForbidden instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ChatVoteForbidden findById(String id) {
		log.debug("getting ChatVoteForbidden instance with id: " + id);
		try {
			ChatVoteForbidden instance = (ChatVoteForbidden) getHibernateTemplate().get(
					ChatVoteForbidden.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public ChatVoteForbidden merge(ChatVoteForbidden detachedInstance) {
		log.debug("merging ChatVoteForbidden instance");
		try {
			ChatVoteForbidden result = (ChatVoteForbidden) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ChatVoteForbidden instance) {
		log.debug("attaching dirty ChatVoteForbidden instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ChatVoteForbidden instance) {
		log.debug("attaching clean ChatVoteForbidden instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/**
	 * 自定义合并实现，将传入对象与数据库对象做匹配，来实现相应的增删改操作
	 * 
	 * @param instance
	 * @throws Exception
	 */
	public void customMerge(ChatVoteForbidden instance) throws Exception {
		Transaction transation = null;
		Session session = null;
		try {
//			long t1 = System.currentTimeMillis();
			session = this.getSession();
			// throw new Exception();
			transation = session.beginTransaction();
			ChatVoteForbidden old = this.findById(instance.getId());
//			long t2 = System.currentTimeMillis();
			HibernateExtension.update(session, old, instance);
			transation.commit();
		} catch (Exception re) {
			if (transation != null) {
				transation.rollback();
			}
			log.error(TextUtil.format("{0} merge failed.", instance.getId()),
					re);
			throw re;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public static ChatVoteForbiddenDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ChatVoteForbiddenDAO) ctx.getBean("ChatVoteForbiddenDAO");
	}

}
