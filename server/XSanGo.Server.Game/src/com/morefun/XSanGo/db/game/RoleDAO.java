/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db.game;

import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.HibernateExtension;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author lvmingtao
 * 
 */
public class RoleDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(RoleDAO.class);

	// property constants
	public static final String NAME = "name";

	protected void initDao() {
		// ProxoolFacade
		// do nothing
	}

	/**
	 * 复制一个对象到缓存中
	 * 
	 * @param role
	 */
	private void cloneAndPut2Cache(Role role) {
		if (ServerLancher.isRoleL2cacheOpen()) {
			this.getCache().put(new Element(role.getId(), TextUtil.clone(role)));
		}
	}

	private Cache getCache() {
		return XsgCacheManager.getInstance().getCache(Const.L2_Cache_name);
	}

	public Role findById(String id) {
		log.debug("getting Role instance with id: " + id);
		try {
			Element element = this.getCache().get(id);
			Role instance = (Role) (element != null ? element.getObjectValue() : getHibernateTemplate().get(Role.class, id));
			if (element == null && instance != null) {
				this.cloneAndPut2Cache(instance);
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Role instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Role as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/**
	 * 自定义合并实现，将传入对象与数据库对象做匹配，来实现相应的增删改操作
	 * 
	 * @param instance
	 * @throws Exception
	 */
	public void customMerge(Role instance) throws Exception {
		Transaction transation = null;
		Session session = null;
		try {
			session = this.getSession();
			// throw new Exception();
			transation = session.beginTransaction();
			Role old = this.findById(instance.getId());
			HibernateExtension.update(session, old, instance);
			transation.commit();
			this.cloneAndPut2Cache(instance);
		} catch (Exception re) {
			if (transation != null) {
				transation.rollback();
			}
			log.error(TextUtil.format("{0} merge failed.", instance.getId()), re);
			throw re;
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	/**
	 * 带重复尝试计数器的自定义合并实现，内部捕获并发异常，并递增计数器递归调用自身进行保存，当计数器达到一定阈值，则不再尝试直接抛出异常
	 * 
	 * @param instance
	 * @param repeatTime 重复尝试的上下文计数器，用于递归调用
	 * @throws Exception
	 */
	private void customMerge(Role instance, int repeatTime) throws Exception {
		int limitTime = 5;
		if (repeatTime >= limitTime) {
			throw new TimeoutException(TextUtil.format("Role {0} try merge too many times.", instance.getId()));
		}
		Transaction transation = null;
		Session session = null;
		try {
			session = this.getSession();
			// throw new Exception();
			transation = session.beginTransaction();
			Role old = this.findById(instance.getId());
			HibernateExtension.update(session, old, instance);
			transation.commit();
			this.cloneAndPut2Cache(instance);
		} catch (ConcurrentModificationException e) {
			log.warn(TextUtil.format("Role {0} catch a ConcurrentModificationException.", instance.getId()));
			Thread.sleep(200);
			this.customMerge(instance, repeatTime + 1);
		} catch (Exception re) {
			if (transation != null) {
				transation.rollback();
			}
			log.error(TextUtil.format("{0} merge failed.", instance.getId()), re);
			throw re;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public static RoleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (RoleDAO) ctx.getBean("RoleDAO");
	}

	public List<Role> findByAccount(String account) {
		try {
			String queryString = "from Role as model where model.account=?";

			return (List<Role>) getHibernateTemplate().find(queryString, account);
		} catch (RuntimeException re) {
			log.error("find by property account failed", re);
			throw re;
		}
	}

	public List<Role> findByAccountWithLimit(String account, int limit) {
		try {
			String queryString = "from Role as model where model.account=?";

			HibernateTemplate template = getHibernateTemplate();
			template.setMaxResults(limit);
			return (List<Role>) template.find(queryString, account);
		} catch (RuntimeException re) {
			log.error("find by property account with limit failed", re);
			throw re;
		}
	}

	/**
	 * 查找所有群雄机器人ID
	 * 
	 * @param account
	 * @return
	 */
	public List<String> findAllLadderRobot(String account) {
		try {
			String queryString = "select id from Role as model where model.account=? and model.robotType=1";
			HibernateTemplate template = getHibernateTemplate();
			return (List<String>) template.find(queryString, account);
		} catch (RuntimeException re) {
			log.error("find by property account with limit failed", re);
			throw re;
		}
	}

	public List<Role> findByAccountAndServer(String account, int serverId) {
		try {
			String queryString = "from Role as model where model.account=? and model.serverId = ?";

			return (List<Role>) getHibernateTemplate().find(queryString, account, serverId);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public String findAccountById(String id) {
		try {
			String queryString = "select account from Role where id = ?";
			List<String> accounts = getHibernateTemplate().find(queryString, id);
			String resAccount = null;
			if (accounts != null && accounts.size() > 0) {
				resAccount = accounts.get(0);
			}
			return resAccount;
		} catch (RuntimeException e) {
			log.error("findAccountById faield", e);
			throw e;
		}
	}

	public Role findByName(String name) {
		List<Role> list = findByProperty(NAME, name);
		if (list.size() > 1) {
			throw new IllegalStateException("存在多个同名角色。");
		}

		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 查询全部的角色名称
	 * 
	 * @return
	 */
	public List<String> findByNameAll() {
		List<String> roleNameList;
		try {
			roleNameList = getHibernateTemplate().execute(new HibernateCallback<List<String>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery("select name from role");
					query.addScalar("name", Hibernate.STRING); // 返回值类型
					return (List<String>) query.list();
				}
			});
		} catch (RuntimeException re) {
			log.error("find Role all failed", re);
			throw re;
		}

		return roleNameList;
	}

	/**
	 * 查询基于日前的全部的角色ID
	 * 
	 * @return
	 */
	public List<Object[]> findByIdDate(final String date) {
		List<Object[]> roleList;
		try {
			roleList = getHibernateTemplate().execute(new HibernateCallback<List<Object[]>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery(TextUtil.format("select id, name from role where account <> '{0}' " + " and date(create_date) >= '{1}'", XsgRoleManager.Robot_Account, date));
					query.addScalar("id", Hibernate.STRING); // 返回值类型
					query.addScalar("name", Hibernate.STRING); // 返回值类型
					return (List<Object[]>) query.list();
				}
			});
		} catch (RuntimeException re) {
			log.error("find Role all failed", re);
			throw re;
		}

		return roleList;
	}

	/**
	 * 查询 竞技场的 机器人数据
	 * 
	 * @param accountRobot
	 * @return
	 */
	public List<String> findArenaRobot(final int limitNum) {
		List<String> robotRoleList;
		try {
			robotRoleList = getHibernateTemplate().execute(new HibernateCallback<List<String>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery(TextUtil.format("select a.role_id roleId from role_arena_rank a, role b " + " where b.account = '{0}' " + " and  a.role_id = b.id limit {1}", XsgRoleManager.Robot_Account, limitNum));

					query.addScalar("roleId", Hibernate.STRING); // 返回值类型
					return (List<String>) query.list();
				}
			});
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
		return robotRoleList;
	}

	/**
	 * 查询全服最高等级
	 * */
	public int getMaxLevelInServer() {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				List<Integer> res = session.createSQLQuery("select max(`level`) from `role`").list();
				if (res != null && res.size() > 0) {
					return res.get(0);
				}
				return 0;
			}
		});
	}

	/**
	 * 查找所有机器人角色ID
	 * 
	 * @return
	 */
	public List<String> findAllRobot() {
		List<String> roleIdList = getHibernateTemplate().execute(new HibernateCallback<List<String>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery("SELECT id FROM role WHERE account = ?");
				query.setString(0, XsgRoleManager.Robot_Account);
				query.addScalar("id", Hibernate.STRING); // 返回值类型
				return (List<String>) query.list();
			}
		});

		return roleIdList;

	}

	/**
	 * 查找某个服的所有玩家
	 * 
	 * @param serverId 服务器ID
	 * */
	public List<String> findAllIdByServerId(final int serverId) {
		List<String> roleIdList = getHibernateTemplate().execute(new HibernateCallback<List<String>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery("SELECT id FROM role WHERE server_id = ?");
				query.setInteger(0, serverId);
				query.addScalar("id", Hibernate.STRING); // 返回值类型
				return (List<String>) query.list();
			}
		});

		return roleIdList;
	}

	/**
	 * 查找所有符合等级和离线天数的玩家id
	 * 
	 * @param minLevel
	 * @param offlineDays
	 * @param excludeRoleId
	 * @return
	 */
	public List<String> findOfflineRoleIdList(final int minLevel, final int offlineDays) {
		List<String> roleIdList = getHibernateTemplate().execute(new HibernateCallback<List<String>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "SELECT id FROM role r left join role_friends_recalled f on r.id=f.role_id WHERE (f.role_id is null or f.state=1) and account != ? and level >= ? and logout_time > login_time and adddate(logout_time,?) < now()";
				SQLQuery query = session.createSQLQuery(sql);
				query.setString(0, XsgRoleManager.Robot_Account);
				query.setInteger(1, minLevel);
				query.setInteger(2, offlineDays);
				query.addScalar("id", Hibernate.STRING); // 返回值类型
				return (List<String>) query.list();
			}
		});
		return roleIdList;
	}
	
	/**
	 * 根据roleName进行模糊查询
	 * @param roleName
	 * @return
	 */
	public List<Role> findBySimpleRoleName(final String roleName) {
		
		try {
			String queryString = "from Role as model where model.name like ?";

			return (List<Role>) getHibernateTemplate().find(queryString, "%"+roleName+"%");
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/**
	 * 根据账号进行模糊查询
	 * @param account
	 * @return
	 */
	public List<Role> findBySimpleAccount(String account) {

		try {
			String queryString = "from Role as model where model.account like ?";

			return (List<Role>) getHibernateTemplate().find(queryString, "%"+account+"%");
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public void deleteRole(Role role){
		   Session session =null;
           
           try{
              session= this.getSession();
              //开启事务.
              session.beginTransaction();
//              Role role = getHibernateTemplate().get(Role.class,id);
              
              //删除表中的记录.
              if(role != null && role.getAccount() != null ){
            	  session.delete(role);
            	  //提交事务.把内存的改变提交到数据库上.
            	  session.getTransaction().commit();
              }
              
           }catch(Exception e){
        	  log.error("delete role by id failed", e);
              session.getTransaction().rollback();
           }finally{
        	   session.close();
           }
		
	}
	
	/**
	 * 查询小于指定登录日期的公会成员
	 * 
	 * @param factionId
	 * @param loginDate
	 * @return
	 */
	public List<String> findOfflineFactionMember(final String factionId, final Date loginDate) {
		List<String> roleIdList = getHibernateTemplate().execute(new HibernateCallback<List<String>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "SELECT r.id FROM role r,faction_member fm where fm.role_id = r.id and fm.faction_id = ? and r.login_time <= ?";
				SQLQuery query = session.createSQLQuery(sql);
				query.setString(0, factionId);
				query.setTimestamp(1, loginDate);
				query.addScalar("id", Hibernate.STRING); // 返回值类型
				return (List<String>) query.list();
			}
		});
		return roleIdList;
	}

	/**
	 * 获取指定日期内有登录过的公会成员数量
	 * 
	 * @param factionId
	 * @param loginDate
	 * @return
	 */
	public int findFactionLoginMember(final String factionId, final Date loginDate) {
		Integer count = getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "SELECT count(*) FROM role r,faction_member f where r.id = f.role_id and r.faction_id = ? and r.login_time >= ?";
				SQLQuery query = session.createSQLQuery(sql);
				query.setString(0, factionId);
				query.setTimestamp(1, loginDate);
				Object obj = query.uniqueResult();
				return obj == null ? 0 : NumberUtil.parseInt(obj.toString());
			}
		});
		return count;
	}
	/**
	 * 查询指定条件的玩家编号列表
	 * 
	 * @param minLevel 最小等级
	 * @param offlineDays 离线天数
	 * @return
	 */
	public List<String> findRoleIdList(final int minLevel, final int offlineDays) {
		List<String> roleIdList = getHibernateTemplate().execute(new HibernateCallback<List<String>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "SELECT id FROM role  WHERE level >= ? and adddate(login_time, ?) >= now()";
				SQLQuery query = session.createSQLQuery(sql);
				query.setInteger(0, minLevel);
				query.setInteger(1, offlineDays);
				query.addScalar("id", Hibernate.STRING); // 返回值类型
				return (List<String>) query.list();
			}
		});
		return roleIdList;
	}}
