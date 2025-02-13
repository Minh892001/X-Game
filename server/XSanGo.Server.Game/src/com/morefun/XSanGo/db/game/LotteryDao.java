/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
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

import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 大富温积分排行 数据库结构
 * 
 * @author sunjie
 * 
 */
@Repository("LotteryDao")
public class LotteryDao extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory
			.getLogger(LotteryDao.class);

	@SuppressWarnings("unchecked")
	public List<LotteryScoreInfo> findAll() {
		List<LotteryScoreInfo> scoreRankList;
		try {
			scoreRankList = getHibernateTemplate().execute(
					new HibernateCallback<List<LotteryScoreInfo>>() {
						@Override
						public List<LotteryScoreInfo> doInHibernate(
								Session session) throws HibernateException,
								SQLException {
							String sqlStr = "select a.role_id, a.grid_id, a.throw_num, a.auto_num, a.cycle_time, a.shop_info, a.grids_info, a.score, a.special_score, a.update_time, a.daily_throw_num"
									+ ", b.name, CONCAT(b.head_image, '~', b.head_border), b.level, b.faction_id, c.vip_level, b.sex,a.shop_open_time,a.send_mail"
									+ " from lottery_record a, role b, role_vip c where a.role_id = b.id and b.id = c.role_id "
									+ " order by a.score desc";
							List<Object[]> query = session.createSQLQuery(
									sqlStr).list();
							LinkedList<LotteryScoreInfo> rankList = new LinkedList<LotteryScoreInfo>();
							for (Object[] obj : query) {
								LotteryScoreInfo lotteryScoreInfo = new LotteryScoreInfo();
								lotteryScoreInfo.setRoleId(String.valueOf(obj[0]));
								lotteryScoreInfo.setGridId(Integer.parseInt(String
										.valueOf(obj[1])));
								lotteryScoreInfo.setThrowNum(Integer.parseInt(String
										.valueOf(obj[2])));
								lotteryScoreInfo.setAutoNum(Integer.parseInt(String
										.valueOf(obj[3])));
								lotteryScoreInfo.setCycleTime(Integer.parseInt(String
										.valueOf(obj[4])));
								if(String.valueOf(obj[5])!=null)
								{
									lotteryScoreInfo.setShopInfo(String.valueOf(obj[5]));
								}
								if(String.valueOf(obj[6])!=null)
								{
									lotteryScoreInfo.setGridsInfo(String.valueOf(obj[6]));
								}
								lotteryScoreInfo.setScore(Integer.parseInt(String
										.valueOf(obj[7])));
								lotteryScoreInfo.setSpecialScore(Integer.parseInt(String
										.valueOf(obj[8])));
								lotteryScoreInfo.setUpdateTime(DateUtil.parseDate(String.valueOf(obj[9])));
								lotteryScoreInfo.setDailyThrowNum(Integer.parseInt(String
										.valueOf(obj[10])));
								
								lotteryScoreInfo.setRoleName(String
										.valueOf(obj[11]));
								lotteryScoreInfo.setIcon(this.setIcon(String
										.valueOf(obj[12]), Integer
										.valueOf(String.valueOf(obj[16]))));
								if (obj[14] != null
										&& !TextUtil.isBlank(String
												.valueOf(obj[14]))) {
									lotteryScoreInfo.setFactionId(String
											.valueOf(obj[14]));
								}
								lotteryScoreInfo.setLevel(Integer.valueOf(String
										.valueOf(obj[13])));
								lotteryScoreInfo.setVip(Integer.valueOf(String
										.valueOf(obj[15])));
								if(String.valueOf(obj[17])!= null && !String.valueOf(obj[17]).equals("null"))
								{
									lotteryScoreInfo.setShopOpenTime(DateUtil.parseDate(String.valueOf(obj[17])));
								}
								lotteryScoreInfo.setSendMail(Integer.valueOf(String
										.valueOf(obj[18])));
								rankList.add(lotteryScoreInfo);
							}
							return rankList;
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
			log.error("find LotteryScoreInfo score list failed", re);
			throw re;
		}
		return scoreRankList;
	}

	public void save(LotteryScoreInfo lotteryScoreInfo) {
		log.debug("saving LotteryScoreInfo instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(lotteryScoreInfo);
			log.debug("save LotteryScoreInfo successful");
		} catch (RuntimeException re) {
			log.error("save LotteryScoreInfo failed", re);
			throw re;
		}
	}

	public void clear(Collection<LotteryScoreInfo> lotteryScoreInfoList) {
		log.debug("clear ShootScoreRank list");
		try {
			this.getHibernateTemplate().deleteAll(lotteryScoreInfoList);
			log.debug("clear LotteryScoreInfo list successful");
		} catch (RuntimeException re) {
			log.error("clear LotteryScoreInfo list failed", re);
			throw re;
		}
	}

	public static LotteryDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (LotteryDao) ctx.getBean("LotteryDao");
	}
	
	@Resource(name = "sessionFactory")
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
