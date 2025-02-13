/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandDAO
 * 功能描述：
 * 文件名：DreamlandDAO.java
 **************************************************
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.morefun.XSanGo.dreamland.DreamlandRank;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;

/**
 * 南华幻境数据库操作
 * 
 * @author weiyi.zhao
 * @since 2016-4-23
 * @version 1.0
 */
@Repository("DreamlandDAO")
public class DreamlandDAO extends HibernateDaoSupport {

	/**
	 * 查找南华幻境排行数据
	 * 
	 * @param limit
	 * @return
	 */
	public List<DreamlandRank> findDreamlandRank(final int limit) {
		List<DreamlandRank> rankList = null;
		try {
			rankList = getHibernateTemplate().execute(new HibernateCallback<List<DreamlandRank>>() {
				@SuppressWarnings("unchecked")
				public java.util.List<DreamlandRank> doInHibernate(Session session) throws HibernateException, SQLException {
					String sql = "select a.role_id, b.name, CONCAT(b.head_image, '~', b.head_border), b.level, c.vip_level, a.star_num, a.layer_num, a.update_time " + 
								"from role_dreamland a, role b, role_vip c " + 
								"where a.role_id = b.id and b.id = c.role_id and (a.star_num > 0 or a.layer_num > 0) order by a.star_num desc, a.layer_num DESC, a.update_time DESC limit " + limit;
					List<Object[]> query = session.createSQLQuery(sql).list();
					List<DreamlandRank> list = new ArrayList<DreamlandRank>();
					for (Object[] obj : query) {
						DreamlandRank rank = new DreamlandRank();
						rank.setRankUnit(String.valueOf(obj[0]), String.valueOf(obj[1]), String.valueOf(obj[2]), Integer.parseInt(String.valueOf(obj[3])), Integer.parseInt(String.valueOf(obj[4])), Integer.parseInt(String.valueOf(obj[5])), Integer.parseInt(String.valueOf(obj[6])));
						rank.setRankUpdateTime(DateUtil.parseDate(String.valueOf(obj[7])));
						list.add(rank);
					}
					return list;
				};
			});
		} catch (Exception e) {
			LogManager.error(e);
		}
		return rankList;
	}

	/**
	 * 获得数据库实例对象
	 * 
	 * @param ctx
	 * @return
	 */
	public static DreamlandDAO getFromApplicationContext(ApplicationContext ctx) {
		return (DreamlandDAO) ctx.getBean("DreamlandDAO");
	}

	@Resource(name = "sessionFactory")
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
