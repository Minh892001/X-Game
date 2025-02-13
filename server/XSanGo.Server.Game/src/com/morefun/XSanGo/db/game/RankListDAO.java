/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.XSanGo.Protocol.RankListSub;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.TextUtil;

public class RankListDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(RankListDAO.class);

	/**
	 * 查询 部队战力排名 不查询机器人
	 * 
	 * @param limit
	 *            查询行数
	 * @return
	 */
	public List<RankListSub> findCombat(final int limit) {
		List<RankListSub> combatList;
		try {
			combatList = getHibernateTemplate().execute(
					new HibernateCallback<List<RankListSub>>() {
						@SuppressWarnings("unchecked")
						@Override
						public List<RankListSub> doInHibernate(Session session)
								throws HibernateException, SQLException {

							String sqlStr = "select a.id, a.name, CONCAT(a.head_image, '~', a.head_border), a.level, "
									+ " b.vip_level, a.combat_power, a.sex "
									+ " from role a, role_vip b where a.account <>'"
									+ XsgRoleManager.Robot_Account
									+ "'"
									+ " and a.id = b.role_id "
									+ " ORDER BY a.combat_power desc, a.level desc limit "
									+ limit;

							List<Object[]> query = session.createSQLQuery(
									sqlStr).list();
							List<RankListSub> combatListTemp = new ArrayList<RankListSub>(
									limit);
							int i = 1;
							for (Object[] obj : query) {
								RankListSub combat = new RankListSub();
								combat.rank = i;
								combat.roleId = String.valueOf(obj[0]);
								combat.roleName = String.valueOf(obj[1]);
								combat.icon = this.setIcon(
										String.valueOf(obj[2]),
										Integer.valueOf(String.valueOf(obj[6])));
								combat.level = Integer.valueOf(String
										.valueOf(obj[3]));
								combat.vipLevel = Integer.valueOf(String
										.valueOf(obj[4]));
								combat.count = Integer.valueOf(String
										.valueOf(obj[5]));

								i++;

								combatListTemp.add(combat);
							}

							return combatListTemp;
						}

						// 设置头像
						private String setIcon(String icon, int sex) {
							if (icon.equals("")) {
								return XsgRoleManager.getInstance()
										.randomHeadImage(sex);
							} else {
								return icon;
							}
						}
					});
		} catch (RuntimeException re) {
			log.error("findCombat failed", re);
			throw re;
		}

		return combatList;
	}

	/**
	 * 查询 成就排行榜  不查询机器人
	 * 
	 * @param limit
	 *            查询行数
	 * @return
	 */
	public List<RankListSub> findAchieve(final int limit) {
		List<RankListSub> combatList;
		try {
			combatList = getHibernateTemplate().execute(
					new HibernateCallback<List<RankListSub>>() {
						@SuppressWarnings("unchecked")
						@Override
						public List<RankListSub> doInHibernate(Session session)
								throws HibernateException, SQLException {

							String sqlStr = "select a.id, a.name, CONCAT(a.head_image, '~', a.head_border), a.level, "
									+ " b.vip_level, a.achieve_completed_num, a.sex "
									+ " from role a, role_vip b where a.account <>'"
									+ XsgRoleManager.Robot_Account
									+ "'"
									+ " and a.id = b.role_id "
									+ " ORDER BY a.achieve_completed_num desc, a.level desc limit "
									+ limit;

							List<Object[]> query = session.createSQLQuery(
									sqlStr).list();
							List<RankListSub> combatListTemp = new ArrayList<RankListSub>(
									limit);
							int i = 1;
							for (Object[] obj : query) {
								RankListSub combat = new RankListSub();
								combat.rank = i;
								combat.roleId = String.valueOf(obj[0]);
								combat.roleName = String.valueOf(obj[1]);
								combat.icon = this.setIcon(
										String.valueOf(obj[2]),
										Integer.valueOf(String.valueOf(obj[6])));
								combat.level = Integer.valueOf(String
										.valueOf(obj[3]));
								combat.vipLevel = Integer.valueOf(String
										.valueOf(obj[4]));
								combat.count = Integer.valueOf(String
										.valueOf(obj[5]));

								i++;

								combatListTemp.add(combat);
							}

							return combatListTemp;
						}

						// 设置头像
						private String setIcon(String icon, int sex) {
							if (icon.equals("")) {
								return XsgRoleManager.getInstance()
										.randomHeadImage(sex);
							} else {
								return icon;
							}
						}
					});
		} catch (RuntimeException re) {
			log.error("findAchieve failed", re);
			throw re;
		}

		return combatList;
	}
	
	/**
	 * 大神 膜拜次数 排名
	 * 
	 * @param limit
	 *            查询行数
	 * @return
	 */
	public List<RankListSub> findWorship() {
		List<RankListSub> combatList;
		try {
			combatList = getHibernateTemplate().execute(
					new HibernateCallback<List<RankListSub>>() {
						@SuppressWarnings("unchecked")
						@Override
						public List<RankListSub> doInHibernate(Session session)
								throws HibernateException, SQLException {

							String sqlStr = "select a.role_id, b.name, CONCAT(b.head_image, '~', b.head_border), b.level, c.vip_level,"
									+ " a.worship_count, b.sex "
									+ " from worship_rank a, role b, role_vip c where a.role_id = b.id and b.id = c.role_id "
									+ " order by a.worship_count desc, b.level desc ";

							List<Object[]> query = session.createSQLQuery(
									sqlStr).list();
							List<RankListSub> combatMapTemp = new ArrayList<RankListSub>(
									query.size());

							int i = 1;
							for (Object[] obj : query) {
								RankListSub worship = new RankListSub();
								worship.rank = i;
								worship.roleId = String.valueOf(obj[0]);
								worship.roleName = String.valueOf(obj[1]);
								worship.icon = this.setIcon(
										String.valueOf(obj[2]),
										Integer.valueOf(String.valueOf(obj[6])));
								worship.level = Integer.valueOf(String
										.valueOf(obj[3]));
								worship.vipLevel = Integer.valueOf(String
										.valueOf(obj[4]));
								worship.count = Integer.valueOf(String
										.valueOf(obj[5]));

								i++;

								combatMapTemp.add(worship);
							}

							return combatMapTemp;
						}

						// 设置头像
						private String setIcon(String icon, int sex) {
							if (icon.equals("")) {
								return XsgRoleManager.getInstance()
										.randomHeadImage(sex);
							} else {
								return icon;
							}
						}
					});
		} catch (RuntimeException re) {
			log.error("findCombat failed", re);
			throw re;
		}

		return combatList;
	}

	/**
	 * 公会 排名
	 * 
	 * @return
	 */
	public List<RankListSub> findFaction() {
		List<RankListSub> factionList;
		try {
			factionList = getHibernateTemplate().execute(
					new HibernateCallback<List<RankListSub>>() {
						@SuppressWarnings("unchecked")
						@Override
						public List<RankListSub> doInHibernate(Session session)
								throws HibernateException, SQLException {

							String sqlStr = " select a.id, a.name, a.icon, a.level, b.num from faction a, " +
							" (select faction_id, count(*) num from faction_member group by faction_id) b " +
							" where a.id = b.faction_id " +
							" order by a.level DESC, b.num desc ";
							List<Object[]> query = session.createSQLQuery(
									sqlStr).list();
							List<RankListSub> factionListTempl = new ArrayList<RankListSub>(
									query.size());

							int i = 1;
							for (Object[] obj : query) {
								RankListSub worship = new RankListSub();
								worship.rank = i;
								worship.roleId = String.valueOf(obj[0]);
								worship.roleName = String.valueOf(obj[1]);
								worship.icon = String.valueOf(obj[2]);
								worship.level = Integer.valueOf(String
										.valueOf(obj[3]));
								worship.count = Integer.valueOf(String
										.valueOf(obj[4]));

								i++;

								factionListTempl.add(worship);
							}

							return factionListTempl;
						}
					});
		} catch (RuntimeException re) {
			log.error("findCombat failed", re);
			throw re;
		}

		return factionList;
	}

	public static RankListDAO getFromApplicationContext(ApplicationContext ac) {
		return (RankListDAO) ac.getBean("RankListDAO");
	}
}
