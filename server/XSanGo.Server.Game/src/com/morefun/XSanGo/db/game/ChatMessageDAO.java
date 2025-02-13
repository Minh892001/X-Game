/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author BruceSu
 * 
 */
public class ChatMessageDAO extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory
			.getLogger(ChatMessageDAO.class);

	public List findAll() {
		log.debug("finding all ChatMessage instances");
		try {
			String queryString = "from ChatMessage";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public void save(ChatMessage transientInstance) {
		log.debug("saving ChatMessage instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	//保存离线消息
//	public void saveOffline(ChatMessageOffline messageOffline) {
//		log.debug("saving ChatMessageOffline instance");
//		try {
//			getHibernateTemplate().save(messageOffline);
//			log.debug("save successful");
//		} catch (RuntimeException re) {
//			log.error("save ChatMessageOffline failed", re);
//			throw re;
//		}
//	}

	public static ChatMessageDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ChatMessageDAO) ctx.getBean("ChatMessageDAO");
	}
}
