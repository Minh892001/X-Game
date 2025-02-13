/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.morefun.XSanGo.role.XsgRoleManager;

/**
 * 被合并服务器的角色访问类
 * 
 * @author linyun.su
 * 
 */
public class CombineRoleDAO extends RoleDAO {
	public static CombineRoleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (CombineRoleDAO) ctx.getBean("CombineRoleDAO");
	}

	/**
	 * 查找所有非机器人角色ID
	 * 
	 * @return
	 */
	public List<String> findAllNotRobot() {
		List<String> roleIdList = getHibernateTemplate().execute(
				new HibernateCallback<List<String>>() {
					@SuppressWarnings("unchecked")
					@Override
					public List<String> doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session
								.createSQLQuery("SELECT id FROM role WHERE account <> ?");
						query.setString(0, XsgRoleManager.Robot_Account);
						query.addScalar("id", Hibernate.STRING); // 返回值类型
						return (List<String>) query.list();
					}
				});

		return roleIdList;

	}
}
