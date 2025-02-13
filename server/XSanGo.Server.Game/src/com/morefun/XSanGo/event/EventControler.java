/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.morefun.XSanGo.AuxDBThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.log.SimpleDAO;
import com.morefun.XSanGo.monitor.XsgMonitorManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.util.TypeUtil;

import net.sf.signalslot_apt.EventDispatcher;

/**
 * @author BruceSu
 * 
 */
@SuppressWarnings("unchecked")
class EventControler extends EventDispatcher implements IEventControler {
	private IRole roleRt;
	private Role roleDb;

	public EventControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;
	}

	@Override
	public <T> T registerEvent(Class<T> type) {
		return super.registerEvent(type);
	}

	@Override
	public <T> void registerHandler(Class<T> type, T handler) {
		if (!this.roleRt.isRobot()) {// 做任务，进排行神马的跟你就没啥关系了
			super.registerHandler(type, handler);
		}
	}

	@Override
	public <T> void emit(Class<T> type, Object[] params) {
		super.emit(type, params);
		if (!this.roleRt.isRobot()) {// 记录操作日志
			this.saveLog2Db(type.getSimpleName(), params);
		}
	}

	@Override
	protected void invokeHandler(Object handler, Method method, Object[] params) throws IllegalAccessException,
			InvocationTargetException {
		long begin = System.currentTimeMillis();
		super.invokeHandler(handler, method, params);
		XsgMonitorManager.getInstance().process(
				TextUtil.format("{0}.{1}", handler.getClass().getSimpleName(), method.getName()),
				(int) (System.currentTimeMillis() - begin));
	}

	private void saveLog2Db(final String eventName, final Object[] args) {
		// 避免出现线程同步问题，这里事先把需要数据准备好
		final int prestige = roleRt.getPrestige();
		final long jinbi = roleRt.getJinbi();
		final int level = roleRt.getLevel();
		final String roleId = roleRt.getRoleId();
		final int heroSkillPoint = roleRt.getHeroSkillPointView().heroSkillPoint;
		final int vit = roleRt.getVit();
		final int totalYuanbao = roleRt.getTotalYuanbao();
		final int vipLevel = roleRt.getVipLevel();
		final String account = roleRt.getAccount();
		String param = "";
		for (Object arg : args) {
			param += object2String(arg);
			param += ",";
		}
		if (param.length() > 1) {
			param = param.substring(0, param.length() - 1);
		}
		final String finalParam = param;
		AuxDBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).getHibernateTemplate()
						.execute(new HibernateCallback<Void>() {

							@Override
							public Void doInHibernate(Session session) throws HibernateException, SQLException {

								Date now = Calendar.getInstance().getTime();
								String table = "operation_log_" + DateUtil.toString(now.getTime(), "yyyyMMdd");
								String sql = "INSERT INTO "
										+ table
										+ " (creat_time, EXP, jinbi, LEVEL, operation, params, role_id, skill_point, vit, yuanbao, vip_level,account) "
										+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
								SQLQuery query = session.createSQLQuery(sql);
								query.setTimestamp(0, now);
								query.setInteger(1, prestige);
								query.setLong(2, jinbi);
								query.setInteger(3, level);
								query.setString(4, eventName);
								query.setString(5, finalParam.length() > 10240 ? "params too long" : finalParam);
								query.setString(6, roleId);
								query.setInteger(7, heroSkillPoint);
								query.setInteger(8, vit);
								query.setInteger(9, totalYuanbao);
								query.setInteger(10, vipLevel);
								query.setString(11, account);
								query.executeUpdate();
								return null;
							}
						});
			}
		});
	}

	private String object2String(Object arg) {
		if (arg == null) {
			return "NULL";
		}
		if (TypeUtil.isCollectionType(arg)) {
			String result = "[";
			Iterable it = this.getIterable(arg);
			Iterator reader = it.iterator();
			while (reader.hasNext()) {
				
				if(arg instanceof Map){ 
					String key = String.valueOf(reader.next());
					result += key + ":" + this.object2String(((Map) arg).get(key));
				} else {
					result += this.object2String(reader.next());
				}
				result += ",";
			}
			if (result.length() > 1) {
				result = result.substring(0, result.length() - 1);
			}
			result += "]";

			return result;
		}

		Package pkg = arg.getClass().getPackage();
		if (pkg != null && pkg.getName().equals("com.XSanGo.Protocol")) {
			return TextUtil.GSON.toJson(arg);
		}
		return arg.toString();
	}

	private Iterable getIterable(Object obj) {
		if (obj.getClass().isArray()) {
			List list = new ArrayList();
			for (int j = 0; j < Array.getLength(obj); j++) {
				list.add(Array.get(obj, j));
			}

			return list;
		} else if (obj instanceof Map) {
			return ((Map) obj).keySet();
		} else {
			return (Iterable) obj;
		}
	}
}
