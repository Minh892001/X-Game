/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.stat;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.XSanGo.Protocol.CurrencyType;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.stat.LevelSpread;
import com.morefun.XSanGo.db.stat.StatCopyProgress;
import com.morefun.XSanGo.db.stat.StatEcnomy;
import com.morefun.XSanGo.db.stat.StatGuideProgress;
import com.morefun.XSanGo.role.XsgRoleManager;

/**
 * 定时统计服务
 * 
 * @author sulingyun
 *
 */
public class StatService {

	private static final Logger logger = LoggerFactory
			.getLogger(StatService.class);

	/** 游戏库 */
	private HibernateTemplate template;
	/** 统计库 */
	private HibernateTemplate statTemplate;

	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}

	public void setStatTemplate(HibernateTemplate statTemplate) {
		this.statTemplate = statTemplate;
	}

	public void dailyStat() {
		// logger.info("正在统计等级分布数据...");
		// this.statLevelRange();

		logger.info("正在统计货币流通数据...");
		this.statEcnomyData();

		// logger.info("正在统计关卡进度数据...");
		// this.statCopyPress();

		// logger.info("正在统计新手引导数据...");
		// this.statGuidePress();

		logger.info("每日报表统计工作已完成。");
	}

	/**
	 * 统计引导步骤完成情况
	 */
	private void statGuidePress() {
		final Map<Integer, Integer> map = template
				.execute(new HibernateCallback<Map<Integer, Integer>>() {

					@Override
					public Map<Integer, Integer> doInHibernate(Session session)
							throws HibernateException, SQLException {
						List<Object[]> list = session
								.createSQLQuery(
										"SELECT guide_id,counter FROM server_guide_counter")
								.list();
						Map<Integer, Integer> map = new HashMap<Integer, Integer>();
						for (Object[] o : list) {
							map.put(Integer.parseInt(o[0].toString()),
									Integer.parseInt(o[1].toString()));
						}
						return map;
					}
				});

		Date now = new Date();
		int serverId = ServerLancher.getServerId();
		for (int guideId : map.keySet()) {
			this.statTemplate.save(new StatGuideProgress(now, serverId,
					guideId, map.get(guideId)));
		}
	}

	/**
	 * 统计关卡进度
	 */
	private void statCopyPress() {
		final Map<Integer, Integer> map = template
				.execute(new HibernateCallback<Map<Integer, Integer>>() {

					@Override
					public Map<Integer, Integer> doInHibernate(Session session)
							throws HibernateException, SQLException {
						List<Object[]> list = session
								.createSQLQuery(
										"SELECT template_id,COUNT(1) num FROM role_copy WHERE star > 0 GROUP BY template_id")
								.list();
						Map<Integer, Integer> map = new HashMap<Integer, Integer>();
						for (Object[] o : list) {
							map.put(Integer.parseInt(o[0].toString()),
									Integer.parseInt(o[1].toString()));
						}
						return map;
					}
				});

		Date now = new Date();
		int serverId = ServerLancher.getServerId();
		for (int copyId : map.keySet()) {
			this.statTemplate.save(new StatCopyProgress(now, serverId, copyId,
					map.get(copyId)));
		}
	}

	/**
	 * 统计货币流通数据
	 */
	private void statEcnomyData() {
		this.template.execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session)
					throws HibernateException, SQLException {
				List<Object[]> list = session
						.createSQLQuery(
								"SELECT IFNULL(SUM(bind_yuanbao),0) totalBindYuanbao, IFNULL(SUM(unbind_yuanbao),0) totalUnbindYuanbao, IFNULL(SUM(jinbi),0) totalJinbi,(SELECT IFNULL(SUM(money),0) FROM role_auction_house) totalAcutionMoney,(SELECT IFNULL(SUM(num),0) FROM role_item WHERE template_id = 'med1') totalJunling,(SELECT IFNULL(SUM(num),0) FROM role_item WHERE template_id = 'g_exp') totalToken FROM role_vip")
						.list();
				Object[] queryArray = list.get(0);
				int i = 0;
				setTotalData(CurrencyType.BindYuanbao, queryArray[i++]);
				setTotalData(CurrencyType.UnbindYuanbao, queryArray[i++]);
				setTotalData(CurrencyType.Jinbi, queryArray[i++]);
				setTotalData(CurrencyType.AuctionMoney, queryArray[i++]);
				setTotalData(CurrencyType.MilitaryOrder, queryArray[i++]);
				setTotalData(CurrencyType.FactionToken, queryArray[i++]);
				return null;
			}
		});

		this.saveEcnomyData(CurrencyType.BindYuanbao);
		this.saveEcnomyData(CurrencyType.UnbindYuanbao);
		this.saveEcnomyData(CurrencyType.Jinbi);
		this.saveEcnomyData(CurrencyType.FactionToken);
		this.saveEcnomyData(CurrencyType.AuctionMoney);
		this.saveEcnomyData(CurrencyType.MilitaryOrder);
		XsgStatManager.getInstance().clearData();
	}

	/**
	 * 保存经济统计数据到数据库
	 * 
	 * @param type
	 */
	private void saveEcnomyData(CurrencyType type) {
		StatEcnomy se = XsgStatManager.getInstance().getStatEcnomy(type);
		se.setStatDate(new Date());
		this.statTemplate.save(se);
	}

	private void setTotalData(CurrencyType type, Object object) {
		XsgStatManager.getInstance().getStatEcnomy(type)
				.setTotal(Long.parseLong(object.toString()));
	}

	/**
	 * 统计角色分布
	 */
	private void statLevelRange() {
		final String sql = "select level, count(1) num from role where account <> ? group by level order by level";
		final Map<Integer, Integer> map = template
				.execute(new HibernateCallback<Map<Integer, Integer>>() {

					@Override
					public Map<Integer, Integer> doInHibernate(Session session)
							throws HibernateException, SQLException {
						List<Object[]> list = session.createSQLQuery(sql)
								.setParameter(0, XsgRoleManager.Robot_Account)
								.list();
						Map<Integer, Integer> map = new HashMap<Integer, Integer>();
						for (Object[] o : list) {
							map.put(Integer.parseInt(o[0].toString()),
									Integer.parseInt(o[1].toString()));
						}
						return map;
					}
				});
		Date now = new Date();
		int serverId = ServerLancher.getServerId();
		for (int level : map.keySet()) {
			this.statTemplate.save(new LevelSpread(now, serverId, level, map
					.get(level)));
		}
	}
}
