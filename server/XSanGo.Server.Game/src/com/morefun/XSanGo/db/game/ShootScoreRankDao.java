/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 百步穿杨积分排行 数据库结构
 * 
 * @author zhouming
 * 
 */
public class ShootScoreRankDao extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory
			.getLogger(ShootScoreRankDao.class);

	@SuppressWarnings("unchecked")
	public List<ShootScoreRank> findAll() {
		List<ShootScoreRank> scoreRankList;
		try {
			scoreRankList = getHibernateTemplate().execute(
					new HibernateCallback<List<ShootScoreRank>>() {
						@Override
						public List<ShootScoreRank> doInHibernate(
								Session session) throws HibernateException,
								SQLException {
							String sqlStr = "select a.role_id, a.rank, a.rec, a.total_score, a.day_free_cnt, a.day_one_cnt, a.shoot_one_cnt, a.shoot_ten_cnt, a.shoot_free_time, a.shoot_ont_time, a.shoot_time"
									+ ", b.name, CONCAT(b.head_image, '~', b.head_border), b.level, b.faction_id, c.vip_level, b.sex,a.send_mail"
									+ " from shoot_score_rank a, role b, role_vip c where a.role_id = b.id and b.id = c.role_id "
									+ " order by a.rank asc";

							List<Object[]> query = session.createSQLQuery(
									sqlStr).list();
							LinkedList<ShootScoreRank> rankList = new LinkedList<ShootScoreRank>();
							for (Object[] obj : query) {
								ShootScoreRank shootScoreRank = new ShootScoreRank();
								shootScoreRank.setRoleId(String.valueOf(obj[0]));
								shootScoreRank.setRank(Integer.parseInt(String
										.valueOf(obj[1])));
								shootScoreRank.setRec(String.valueOf(obj[2]));
								shootScoreRank.setTotalScore(Integer
										.valueOf(String.valueOf(obj[3])));
								if (obj[4] != null) {
									shootScoreRank.setDayFreeCnt(Integer
											.parseInt(String.valueOf(obj[4])));
								}
								shootScoreRank.setDayOneCnt(Integer
										.valueOf(String.valueOf(obj[5])));
								shootScoreRank.setShootOneCnt(Integer
										.valueOf(String.valueOf(obj[6])));
								shootScoreRank.setShootTenCnt(Integer
										.valueOf(String.valueOf(obj[7])));
								if (obj[8] != null) {
									shootScoreRank.setShootFreeTime(DateUtil
											.parseDate(String.valueOf(obj[8])));
								}
								if (obj[9] != null) {
									shootScoreRank.setShootOneTime(DateUtil
											.parseDate(String.valueOf(obj[9])));
								}
								if(obj[10] != null) {
									shootScoreRank.setShootTime(DateUtil
											.parseDate(String.valueOf(obj[10])));
								}
								shootScoreRank.setRoleName(String
										.valueOf(obj[11]));
								shootScoreRank.setIcon(this.setIcon(String
										.valueOf(obj[12]), Integer
										.valueOf(String.valueOf(obj[16]))));
								if (obj[14] != null
										&& !TextUtil.isBlank(String
												.valueOf(obj[14]))) {
									shootScoreRank.setFactionId(String
											.valueOf(obj[14]));
								}
								shootScoreRank.setLevel(Integer.valueOf(String
										.valueOf(obj[13])));
								shootScoreRank.setVip(Integer.valueOf(String
										.valueOf(obj[15])));
								shootScoreRank.setIsSendMail(Integer.valueOf(String
										.valueOf(obj[17])));
								rankList.add(shootScoreRank);
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
			log.error("find shoot score list failed", re);
			throw re;
		}
		return scoreRankList;
	}

	public void save(ShootScoreRank shootScoreRank) {
		log.debug("saving ShootScoreRank instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(shootScoreRank);
			log.debug("save ShootScoreRank successful");
		} catch (RuntimeException re) {
			log.error("save ShootScoreRank failed", re);
			throw re;
		}
	}
	
	/**
	 * 批量保存
	 * @param list
	 */
	public void saveBatch(List<ShootScoreRank> list) {
		Session session = this.getSessionFactory().openSession();
		session.setCacheMode(CacheMode.IGNORE);
		session.getTransaction().begin();
		
		for(ShootScoreRank shootScoreRank : list) {
			session.saveOrUpdate(shootScoreRank);
		}
		session.getTransaction().commit();  
        session.clear(); 
		session.close();
	}

	public void clear(Collection<ShootScoreRank> scoreRankList) {
		log.debug("clear ShootScoreRank list");
		try {
			this.getHibernateTemplate().deleteAll(scoreRankList);
			log.debug("clear ShootScoreRank list successful");
		} catch (RuntimeException re) {
			log.error("clear ShootScoreRank list failed", re);
			throw re;
		}
	}

	public static ShootScoreRankDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (ShootScoreRankDao) ctx.getBean("ShootScoreRankDao");
	}
}
