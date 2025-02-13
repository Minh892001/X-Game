package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 *
 * @author qinguofeng
 * @date Apr 9, 2015
 */
public class FightMovieDao extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(FightMovieDao.class);

	public static FightMovieDao getFromApplicationContext(ApplicationContext ctx) {
		return (FightMovieDao) ctx.getBean("FightMovieDao");
	}

	public void save(FightMovie movie) {
		log.debug("begin save movie.");
		try {
			getHibernateTemplate().save(movie);
			log.debug("save movie successful");
		} catch (RuntimeException re) {
			log.error("save movie failed", re);
			throw re;
		}
	}

	public void saveOrUpdate(FightMovie movie) {
		log.debug("begin saveorupdate movie.");
		try {
			getHibernateTemplate().saveOrUpdate(movie);
			log.debug("saveorupdate movie successful");
		} catch (RuntimeException re) {
			log.error("saveorupdate movie failed", re);
			throw re;
		}
	}

	public void save(RoleValidation validation) {
		log.debug("begin save RoleFightValidate.");
		try {
			getHibernateTemplate().save(validation);
			log.debug("save RoleFightValidate successful");
		} catch (RuntimeException re) {
			log.error("save RoleFightValidate failed", re);
			throw re;
		}
	}

	/**
	 * 根据战报ID查找战斗录像数据
	 * 
	 * @param id
	 *            战报ID
	 * */
	public FightMovie findMoviesById(String id) {
		log.debug("begin find movie by fightId.");
		try {
			String sql = "from FightMovie where id = ?";
			List<FightMovie> movieList = getHibernateTemplate().find(sql, id);
			if (movieList != null && movieList.size() > 0) {
				return movieList.get(0);
			}
			return null;
		} catch (RuntimeException e) {
			log.error("find movie by fightId falied.", e);
			throw e;
		}
	}

	/**
	 * 根据发起挑战的玩家ID查找战斗录像数据
	 * 
	 * @param id
	 *            roleId
	 * @return {id, opponent_role_id, result}
	 * */
	public List<Object[]> findMoviesByRoleId(final String roleId, final int type, final int limit) {
		log.debug("begin find movie by roleId.");
		try {
			final String sql = "select id, opponent_role_id, result, start_time from fight_movie where self_role_id = ? and `type` = ? order by start_time desc";

			List<Object[]> movieList = getHibernateTemplate().executeFind(new HibernateCallback<List<Object[]>>() {
				@Override
				public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sql);
					query.setString(0, roleId);
					query.setInteger(1, type);
					query.setFirstResult(0);
					query.setMaxResults(limit);
					return query.list();
				}
			});
			return movieList;
		} catch (RuntimeException e) {
			log.error("find movie by roleId falied.", e);
		}
		return null;
	}

	/**
	 * 获取某种类型并且是某种验证类型的战报
	 * 
	 * @param validated
	 *            验证类型
	 * @param limit
	 *            数量限制
	 * */
	public List<FightMovie> findMovieByValidation(final int validated, final int limit) {
		try {
			final String sql = "from FightMovie where validated = ?";
			List<FightMovie> movieList = getHibernateTemplate().executeFind(new HibernateCallback<List<FightMovie>>() {
				@Override
				public List<FightMovie> doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery(sql).setInteger(0, validated);
					query.setFirstResult(0);
					query.setMaxResults(limit);
					return query.list();
				}
			});
			return movieList;
		} catch (RuntimeException e) {
			log.error("find movie by type failed.", e);
		}
		return null;
	}

	/**
	 * 根据战报ID删除相关的战报录像
	 * 
	 * @param fightId
	 *            战报ID
	 * */
	public void deleteMoviesByFightId(String fightId) {
		log.debug("begin delete movie by fightId.");
		try {
			String sql = TextUtil.format("from FightMovie where fightReportId = {0}", fightId);
			getHibernateTemplate().delete(sql);
		} catch (RuntimeException e) {
			log.error("delete movie by fightId failed.", e);
			throw e;
		}
	}

	/**
	 * 删除某个日期之前, 某种验证状态的数据
	 * 
	 * @param validated
	 *            验证状态
	 * @param date
	 *            日期
	 * */
	public int removeMovieWithValidatedAndDateEq(final int validated, final Date date) {
		try {
			return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session) throws HibernateException, SQLException {
					String sql = "delete from fight_movie where validated = ? and end_time < ?";
					int res = session.createSQLQuery(sql).setInteger(0, validated).setTimestamp(1, date)
							.executeUpdate();
					return res;
				}
			});
		} catch (RuntimeException e) {
			log.error("error delete fight movie");
			throw e;
		}
	}

	/**
	 * 删除某个日期之前的数据
	 * 
	 * @param validated
	 *            验证状态
	 * @param date
	 *            日期
	 * */
	public int removeMovieWithValidatedAndDateNotEq(final Date date) {
		try {
			return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session) throws HibernateException, SQLException {
					String sql = "delete from fight_movie where end_time < ?";
					int res = session.createSQLQuery(sql).setTimestamp(0, date).executeUpdate();
					return res;
				}
			});
		} catch (RuntimeException e) {
			log.error("error delete fight movie");
			throw e;
		}
	}

	/**
	 * 删除某个日期之前的数据
	 * 
	 * @param type
	 *            战报类型
	 * @param createdDelete
	 *            创建状态的战报是否删除,即没有正常完成的战斗(中途退出等)
	 * @param needValidated
	 *            是否需要验证通过, 即未验证的战报不能删除
	 * @param date
	 *            日期
	 * @param forceDelDate
	 *            强制删除日期, 即某个日期之前的战报无论验证状态如何强制删除
	 * */
	public int removeMovieWithTypeOldThen(final int type, final boolean createdDelete, final boolean needValidated,
			final Date date, final Date forceDelDate) {
		try {
			return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session) throws HibernateException, SQLException {
					List<String> validatedList = new ArrayList<String>();
					validatedList.add(XsgFightMovieManager.Validation.Valid.getValue() + ""); // 验证为有效的战报可以删除
					if (!needValidated) { // 不需要验证的可以删除未经过验证的战报
						validatedList.add(XsgFightMovieManager.Validation.None.getValue() + "");
					}
					if (createdDelete) { // 创建状态的可以删除,即没有正常完成的战斗(中途退出等)
						validatedList.add(XsgFightMovieManager.Validation.Create.getValue() + "");
					}
					String deleteParamStr = TextUtil.join(validatedList, ",");
					String sql = TextUtil
							.format("delete from fight_movie where type = ? and ((validated in ({0}) and end_time < ?) or (validated != ? and end_time < ?))",
									deleteParamStr);
					int res = session.createSQLQuery(sql).setInteger(0, type).setTimestamp(1, date)
							.setInteger(2, XsgFightMovieManager.Validation.None.getValue())
							// 未经验证的战报即使超过了强制删除日期也不删除
							.setTimestamp(3, forceDelDate).executeUpdate();
					return res;
				}
			});
		} catch (RuntimeException e) {
			log.error("error delete fight movie");
			throw e;
		}
	}
}
