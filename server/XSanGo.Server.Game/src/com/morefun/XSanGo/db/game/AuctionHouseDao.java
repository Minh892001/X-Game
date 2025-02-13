package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author qinguofeng
 * @date Mar 30, 2015
 */
public class AuctionHouseDao extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(AuctionHouseDao.class);

	public List<AuctionHouseItem> getAllAuctionItems() {
		log.debug("find all auction house items.");
		try {
			final String query = "from AuctionHouseItem";
			return getHibernateTemplate().find(query);
		} catch (RuntimeException e) {
			log.error("AuctionHouseDao getAllAuctionItems error.", e);
			throw e;
		}
	}

	public void save(AuctionHouseItem item) {
		log.debug("save auction house item.");
		try {
			getHibernateTemplate().saveOrUpdate(item);
		} catch (RuntimeException e) {
			log.error("save auction house item error.", e);
			throw e;
		}
	}

	public void delete(AuctionHouseItem item) {
		log.debug("delete auction house item.");
		try {
			getHibernateTemplate().delete(item);
		} catch (RuntimeException e) {
			log.error("delete auction house item error.", e);
			throw e;
		}
	}
	
	/**
	 * 删除某个时间之前的日志
	 * 
	 * @param date 日期
	 * */
	public int removeLogOlderThen(final Date date) {
		log.debug("delete auction log old then " + date.toLocaleString());
		try {
			return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

				@Override
				public Integer doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sql = "delete from auction_house_log where update_time < ?";
					int res = session.createSQLQuery(sql).setTimestamp(0, date)
							.executeUpdate();
					return res;
				}
			});
		} catch (RuntimeException e) {
			log.error("delete auction log.");
			throw e;
		}
	}

	/**
	 * 删除某个日期之前的ItemLog日志
	 * 
	 * @param date 日期
	 * */
	public int removeItemLogOlderThen(final Date date) {
		log.debug("delete auction item log old then " + date.toLocaleString());
		try{
			return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sql = "delete from auction_house_item_log where update_time < ?";
					int res = session.createSQLQuery(sql).setTimestamp(0, date).executeUpdate();
					return res;
				}
			});
		}catch(RuntimeException e) {
			log.error("delete auction item log error.", e);
			throw e;
		}
	}

	public static AuctionHouseDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (AuctionHouseDao) ctx.getBean("AuctionHouseDao");
	}
}
