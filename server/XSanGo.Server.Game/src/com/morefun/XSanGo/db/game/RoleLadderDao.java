/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
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

import com.XSanGo.Protocol.LadderRankListSub;

/**
 * 群雄争霸
 * 
 * @author 吕明涛
 * 
 */
public class RoleLadderDao extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory.getLogger(RoleLadderDao.class);

	/**
	 * 群雄争霸 排名
	 * 
	 * @return
	 */
	public List<LadderRankListSub> findLadder(final int limit, final String seasonDate) {
		List<LadderRankListSub> ladderList;
		try {
			ladderList = getHibernateTemplate().execute(new HibernateCallback<List<LadderRankListSub>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<LadderRankListSub> doInHibernate(Session session) throws HibernateException, SQLException {

					String sqlStr = "select b.id, b.`name`,CONCAT(b.head_image, '~', b.head_border), "
							+ " b.`level`, a.ladder_level, a.ladder_star, c.vip_level,a.change_level_date,a.ladder_score "
							+ " from role_ladder a, role b, role_vip c "
							+ " where a.role_id = b.id and a.role_id = c.role_id and a.season_date >= '" + seasonDate
							+ "' " + " order by a.ladder_score desc, a.change_level_date " + " limit " + limit;
					List<Object[]> query = session.createSQLQuery(sqlStr).list();
					List<LadderRankListSub> listTempl = new ArrayList<LadderRankListSub>(query.size());

					int i = 1;
					for (Object[] obj : query) {
						LadderRankListSub ladder = new LadderRankListSub();
						ladder.rank = i;
						ladder.roleId = String.valueOf(obj[0]);
						ladder.roleName = String.valueOf(obj[1]);
						ladder.icon = String.valueOf(obj[2]);
						ladder.level = Integer.valueOf(String.valueOf(obj[3]));
						ladder.ladderLevel = Integer.valueOf(String.valueOf(obj[4]));
						ladder.ladderStar = Integer.valueOf(String.valueOf(obj[5]));
						ladder.vipLevel = Integer.valueOf(String.valueOf(obj[6]));
						ladder.groupName = "";
						ladder.rankTime = ((Date) obj[7]).getTime();
						ladder.ladderScore = Integer.valueOf(String.valueOf(obj[8]));

						i++;

						listTempl.add(ladder);
					}

					return listTempl;
				}
			});
		} catch (RuntimeException re) {
			log.error("findLadder failed", re);
			throw re;
		}

		return ladderList;
	}

	public static RoleLadderDao getFromApplicationContext(ApplicationContext ctx) {
		return (RoleLadderDao) ctx.getBean("RoleLadderDao");
	}
}
