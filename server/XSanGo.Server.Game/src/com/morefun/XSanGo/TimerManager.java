/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StopWatch;

import com.morefun.XSanGo.auction.XsgAuctionHosueLog;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.center.CenterI;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.GroovyManager;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.log.SimpleDAO;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.haoqingbao.XsgHaoqingbaoManager;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.monitor.XsgMonitorManager;
import com.morefun.XSanGo.net.PermissionsVerifierI;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * 一些全局定时器的管理
 * 
 * @author BruceSu
 * 
 */
public final class TimerManager {

	private static TimerManager instance = new TimerManager();
	private static Logger logger = LoggerFactory.getLogger(TimerManager.class);

	public static TimerManager getInstance() {
		return instance;
	}

	private Map<Integer, TimerTaskT> taskTMap = new HashMap<Integer, TimerTaskT>();
	private int logicThreshold = 2000;
	private long databaseThreshold = 5000;

	private TimerManager() {
		int interval = 2 * 60 * 1000;
		LogicThread.scheduleTask(new DelayedTask(interval, interval) {

			@Override
			public void run() {
				handlePress();
			}

		});

		interval = 60 * 1000;
		LogicThread.scheduleTask(new DelayedTask(interval, interval) {

			@Override
			public void run() {
				refreshRedPoint();
			}
		});

		// 军令检查刷新间隔为脚本配置的一半
		interval = XsgGameParamManager.getInstance().getJunLingRecorvInterval() * 500;
		LogicThread.scheduleTask(new DelayedTask(interval, interval) {

			@Override
			public void run() {
				refreshJunLing();
			}
		});

		this.refreshTask();

		// 检查日志表的创建
		this.checkLogTable();
		interval = 60 * 60 * 1000;
		LogicThread.scheduleTask(new DelayedTask(interval, interval) {

			@Override
			public void run() {
				DBThreads.execute(new Runnable() {
					@Override
					public void run() {
						checkLogTable();
					}
				});
			}
		});

		// 检查拍卖行日志表的清理, 两天执行一次
		checkAuctionHouseLog();
		interval = 48 * 60 * 60 * 1000;
		LogicThread.scheduleTask(new DelayedTask(interval, interval) {
			@Override
			public void run() {
				checkAuctionHouseLog();
			}
		});

		// 检查战报录像表清理, 一天执行一次
		startCheckFightMovie();

		// 清理红包
		startCheckRedPacket();
	}

	private void startCheckRedPacket() {
		long interval = 48 * 60 * 60 * 1000L;
		LogicThread.scheduleTask(new DelayedTask(interval, interval) {
			@Override
			public void run() {
				XsgHaoqingbaoManager.getInstance().removeOldItems();
			}
		});
	}

	private void startCheckFightMovie() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.MINUTE, 0);
		long current = System.currentTimeMillis();
		if (current > cal.getTimeInMillis()) {
			DateUtil.addDays(cal, 1);
		}
		// 检查战报录像表清理, 凌晨3点开始, 一天执行一次
		long interval = 24 * 60 * 60 * 1000;
		long delay = cal.getTimeInMillis() - current;
		LogicThread.scheduleTask(new DelayedTask(delay, interval) {
			@Override
			public void run() {
				checkFightMovie();
			}
		});

		// 检查玩家禁言状态
		interval = 60 * 1000;
		LogicThread.scheduleTask(new DelayedTask(interval, interval) {

			@Override
			public void run() {
				refreshForbiddenStatus();
				refreshSilenceMsg();
			}
		});
	}

	/**
	 * 检查日志表，日志表按日期分表，没有则创建一个
	 */
	private void checkLogTable() {
		// CREATE TABLE IF NOT EXISTS operation_log_20141222 LIKE operation_log
		SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).getHibernateTemplate()
				.execute(new HibernateCallback<Void>() {
					@Override
					public Void doInHibernate(Session session) throws HibernateException, SQLException {
						Date day = Calendar.getInstance().getTime();
						String table = "operation_log_" + DateUtil.toString(day.getTime(), "yyyyMMdd");
						String sql = TextUtil.format("CREATE TABLE IF NOT EXISTS {0} LIKE operation_log", table);
						SQLQuery query = session.createSQLQuery(sql);
						query.executeUpdate();

						day = DateUtil.addSecond(day, (int) TimeUnit.DAYS.toSeconds(1));
						table = "operation_log_" + DateUtil.toString(day.getTime(), "yyyyMMdd");
						sql = TextUtil.format("CREATE TABLE IF NOT EXISTS {0} LIKE operation_log", table);
						query = session.createSQLQuery(sql);
						query.executeUpdate();
						return null;
					}
				});

	}

	/**
	 * 刷新自定义的脚本任务
	 */
	public void refreshTask() {
		Map<Integer, TimerTaskT> map = CollectionUtil.toMap(ExcelParser.parse(TimerTaskT.class), "id");
		for (int id : map.keySet()) {
			if (!this.taskTMap.containsKey(id)) {
				TimerTaskT ttt = map.get(id);
				DelayedTask task = this.createDelayTask(ttt);
				if (task != null) {
					LogicThread.scheduleTask(task);
				}

				this.taskTMap.put(id, ttt);
			}
		}
	}

	/**
	 * 根据定时任务模板创建程序执行指令
	 * 
	 * @param TimerTaskT
	 * @return
	 */
	private DelayedTask createDelayTask(final TimerTaskT ttt) {
		long now = System.currentTimeMillis();
		long itervalSeconds = ttt.intervalHours * 3600 * 1000;
		long first = ttt.firstExeTime.getTime() - now;
		int repeat = ttt.repeat;
		if (first < 0 && ttt.repeat > 0) {// 已经过了首次执行时间，检查在重复执行期是否过期
			for (int i = 1; i <= ttt.repeat; i++) {
				// 尝试执行的时间点检测
				long point = ttt.firstExeTime.getTime() + (i * itervalSeconds);
				if (point > now) {
					first = point - now;
					repeat -= i;
					break;
				}
			}
		}

		final int finalRepeat = repeat;

		return first < 0 ? null : new DelayedTask(first, itervalSeconds) {
			int executeCount;

			@Override
			public void run() {
				try {
					GroovyManager.getInstance().executeScript(ttt.script);
					this.executeCount++;
				} catch (ScriptException e) {
					LogManager.error(e);
					this.cancel();
				}

				if (this.executeCount >= finalRepeat) {
					this.cancel();
				}
			}
		};
	}

	/**
	 * 刷新在线玩家红点提示
	 */
	private void refreshRedPoint() {
		if (LogicThread.getLogicQueueSize() > 100) {
			return;
		}

		StopWatch watch = new StopWatch();
		watch.start();
		List<IRole> list = XsgRoleManager.getInstance().findOnlineList();
		for (IRole role : list) {
			role.refreshRedPoint(false);
		}
		watch.stop();
		long interval = watch.getLastTaskTimeMillis();
		XsgMonitorManager.getInstance().process("refreshRedPoint", (int) interval);
	}

	/**
	 * 刷新在线玩家军令
	 */
	private void refreshJunLing() {
		StopWatch watch = new StopWatch();
		watch.start();
		List<IRole> list = XsgRoleManager.getInstance().findOnlineList();
		for (IRole role : list) {
			role.refreshJunLing();
		}
		watch.stop();
		long interval = watch.getLastTaskTimeMillis();
		XsgMonitorManager.getInstance().process("refreshJunLing", (int) interval);
	}

	/**
	 * 跟踪服务器负载
	 */
	private void handlePress() {
		int logicQueueSize = LogicThread.getLogicQueueSize();
		long dbQueueSize = DBThreads.getQueueSize();
		long movieQueueSize = MovieThreads.getQueueSize();
		logger.warn(TextUtil.format(
				"Logic queue size:{0};Database queue size:{1};Mail center capcity:{2};Movie queue size:{3}",
				logicQueueSize, dbQueueSize, XsgMailManager.getInstance().getMailSizeDesc(), movieQueueSize));
		XsgCacheManager.getInstance().showMonitorInfo();

		boolean overload = (logicQueueSize > this.logicThreshold || dbQueueSize > this.databaseThreshold);
		LogicThread.setDynamicOverload(overload);
		if (overload) {
			LogicThread.printQueue(); // 打印逻辑线程执行队列
			DBThreads.printQueue(); // 打印DB线程执行队列
		}
		// 启动熔断机制
		if (logicQueueSize > this.logicThreshold * 2 || dbQueueSize > this.databaseThreshold * 2) {
			this.fuse();
		}
	}

	/**
	 * 熔断服务，阻止新连接建立，同时把在线玩家全部T下线
	 */
	public void fuse() {
		if (ServerLancher.getAc().containsBean("FuseFlag") && !ServerLancher.getAc().getBean("FuseFlag", Boolean.class)) {
			return;
		}

		logger.warn("SOS,The fuse is broken!!!");
		PermissionsVerifierI.getInstance().fuse();

		List<IRole> onlineList = XsgRoleManager.getInstance().findOnlineList();
		for (IRole role : onlineList) {
			CenterI.instance().kick(role.getAccount(), role.getRoleId());
		}
	}

	/** 拍卖行日志表的清理 */
	private void checkAuctionHouseLog() {
		XsgAuctionHosueLog.removeOldLogs(2, 0);
	}

	/** 战报录像日志清理 */
	private void checkFightMovie() {
		XsgFightMovieManager.removeOldMovies();
		XsgFightMovieManager.getInstance().removeOldMovieContext();
	}

	/**
	 * 刷新玩家禁言状态
	 */
	private void refreshForbiddenStatus() {
		StopWatch watch = new StopWatch();
		watch.start();
		XsgChatManager.getInstance().refreshForbiddenStatus();
		watch.stop();
		long interval = watch.getLastTaskTimeMillis();
		XsgMonitorManager.getInstance().process("refreshForbiddenStatus", (int) interval);
	}

	/**
	 * 刷新玩家聊天静默缓存消息队列
	 */
	private void refreshSilenceMsg() {
		StopWatch watch = new StopWatch();
		watch.start();
		XsgChatManager.getInstance().refreshSilenceMsg();
		watch.stop();
		long interval = watch.getLastTaskTimeMillis();
		XsgMonitorManager.getInstance().process("refreshSilenceMsg", (int) interval);
	}

	public void setLogicThreshold(int logicThreshold) {
		this.logicThreshold = logicThreshold;
	}

	public void setDatabaseThreshold(long databaseThreshold) {
		this.databaseThreshold = databaseThreshold;
	}
}
