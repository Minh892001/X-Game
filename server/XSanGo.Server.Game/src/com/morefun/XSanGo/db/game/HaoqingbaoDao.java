package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.util.TextUtil;

/**
 * @author guofeng.qin
 */
public class HaoqingbaoDao extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(HaoqingbaoDao.class);

	public List<RoleHaoqingbaoItem> getAllHaoqingbaoItems() {
		log.debug("find all RoleHaoqingbaoItem.");
		try {
			String sql = "from RoleHaoqingbaoItem";
			return getHibernateTemplate().find(sql);
		} catch (RuntimeException e) {
			log.error("HaoqingbaoDao getAllHaoqingbaoItems error.", e);
			throw e;
		}
	}

	public List<HaoqingbaoRedPacket> getAllHaoqingbaoRedPackets() {
		log.debug("find all HaoqingbaoRedPacket.");
		try {
			String sql = "from HaoqingbaoRedPacket";
			return getHibernateTemplate().find(sql);
		} catch (RuntimeException e) {
			log.error("HaoqingbaoDao getAllHaoqingbaoRedPackets error.", e);
			throw e;
		}
	}

	public void save(RoleHaoqingbaoItem item) {
		log.debug("save RoleHaoqingbaoItem.");
		try {
			getHibernateTemplate().saveOrUpdate(item);
		} catch (RuntimeException e) {
			log.error("save RoleHaoqingbaoItem error.", e);
			throw e;
		}
	}

	public void save(HaoqingbaoRedPacket packet) {
		log.debug("save HaoqingbaoRedPacket.");
		try {
			getHibernateTemplate().saveOrUpdate(packet);
		} catch (RuntimeException e) {
			log.error("save HaoqingbaoRedPacket error.", e);
			throw e;
		}
	}
	
	public void delete(RoleHaoqingbaoItem item) {
		log.debug("delete HaoqingbaoItem.");
		try{
			getHibernateTemplate().delete(item);
		}catch(RuntimeException e) {
			log.error("delete HaoqingbaoItem error.", e);
			throw e;
		}
	}
	
	public void delete(HaoqingbaoRedPacket packet) {
		log.debug("delete HaoqingbaoRedPacket.");
		try {
			getHibernateTemplate().delete(packet);
		} catch (RuntimeException e) {
			log.error("delete HaoqingbaoRedPacket.");
			throw e;
		}
	}

	/**
	 * 根据类型获取前limit位用户
	 * 
	 * @param type
	 *            0,送出的红包排名;1,抢到的元宝排名
	 * @param limit
	 * */
	public List<RoleHaoqingbao> getSendRedPacketRank(final int type,
			final int limit) {
		List<RoleHaoqingbao> haoqingbaoList = null;
		haoqingbaoList = getHibernateTemplate().execute(
				new HibernateCallback<List<RoleHaoqingbao>>() {
					@Override
					public List<RoleHaoqingbao> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String sql = TextUtil
								.format("SELECT role_id, yuanbao_num, recv_num, total_count, total_num, send_num, last_recv_time, update_time, lucky_star_count, total_send_count, total_send_sum FROM role_haoqingbao ORDER BY {0} DESC LIMIT {1}",
										(type == 0 ? "total_send_sum"
												: "total_num"), limit);
						List<Object[]> query = session.createSQLQuery(sql)
								.list();
						List<RoleHaoqingbao> resultList = new ArrayList<RoleHaoqingbao>(
								query.size());
						for (Object obj[] : query) {
							RoleHaoqingbao rh = new RoleHaoqingbao();
							rh.setRoleId(obj[0].toString());
							rh.setYuanbaoNum(Integer.parseInt(obj[1].toString()));
							rh.setReceiveRedPacketNum(Integer.parseInt(obj[2]
									.toString()));
							rh.setTotalRecvCount(Integer.parseInt(obj[3]
									.toString()));
							rh.setTotalRecvNum(Integer.parseInt(obj[4]
									.toString()));
							rh.setSendRedPacketNum(Integer.parseInt(obj[5]
									.toString()));
							if (obj[6] != null) {
								rh.setLastReceiveTime(new Date(
										((Timestamp) obj[6]).getTime()));
							}
							rh.setUpdateTime(new Date(((Timestamp) obj[7])
									.getTime()));
							rh.setLuckyStarCount(Integer.parseInt(obj[8]
									.toString()));
							rh.setTotalSendCount(Long.parseLong(obj[9]
									.toString()));
							rh.setTotalSendSum(Long.parseLong(obj[10]
									.toString()));
							resultList.add(rh);
						}
						return resultList;
					}
				});
		return haoqingbaoList;
	}

	public static HaoqingbaoDao getFromApplicationContext(ApplicationContext ctx) {
		return (HaoqingbaoDao) ctx.getBean("HaoqingbaoDao");
	}
}
