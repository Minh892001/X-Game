/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.db.HibernateExtension;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class ServerConfigDao extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(ServerConfigDao.class);

	/**
	 * 一个服务器运行时有且最多只有一条配置数据
	 * 
	 * @return
	 */
	public ServerConfig find() {
		try {
			final String queryString = "from ServerConfig as model";
			List<ServerConfig> list = getHibernateTemplate().find(queryString);
			return list.size() > 0 ? list.get(0) : null;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void flush2Db(ServerConfig instance) throws Exception {
		Transaction transation = null;
		Session session = null;
		try {
			session = this.getSession();
			transation = session.beginTransaction();
			ServerConfig old = this.find();
			if (old != null && old.getSequence() > instance.getSequence()) {
				log.warn(TextUtil.format("全局数据保存出现脏写，old={0},new={1}", old.getSequence(), instance.getSequence()));
				return;
			}
			HibernateExtension.update(session, old, instance);
			transation.commit();
		} catch (Exception re) {
			if (transation != null) {
				transation.rollback();
			}
			log.error(TextUtil.format("{0} flush failed.", instance.getId()), re);
			throw re;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public static ServerConfigDao getFromApplicationContext(ApplicationContext ctx) {
		return (ServerConfigDao) ctx.getBean("ServerConfigDao");
	}

	/**
	 * 查询不同服务器ID数量
	 * @return
	 */
	public int getServerIdCount() {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "select count(DISTINCT(server_id)) from role";
				Object res = session.createSQLQuery(sql).uniqueResult();
				if (res == null) {
					return 0;
				}
				return NumberUtil.parseInt(res.toString());
			}
		});
	}
}
