/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.ServerConfig;
import com.morefun.XSanGo.db.game.ServerConfigDao;
import com.morefun.XSanGo.db.game.ServerCopy;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.VipT;
import com.morefun.XSanGo.vip.XsgVipManager;

/**
 * 服务器全局数据管理，包含主键生成序列，副本最高记录，公告阅读记录等
 * 
 * @author sulingyun
 * 
 */
public final class GlobalDataManager implements IAsynSavable {
	private static Logger log = LogManager.getLogger(GlobalDataManager.class);
	private static GlobalDataManager instance = new GlobalDataManager();

	private ServerConfig serverData;
	private String platform;
	private boolean shutdown;
	private ServerConfigDao dao;
	private AsynSaver saver;
	private AtomicLong key;

	/** 是否合过服 */
	private boolean isMergeServer;

	private GlobalDataManager() {
		this.saver = new AsynSaver(this);
		this.platform = ServerLancher.getAc().getBean("DeployPlatform", String.class);

		dao = ServerConfigDao.getFromApplicationContext(ServerLancher.getAc());
		this.serverData = dao.find();
		if (this.serverData == null) {
			this.serverData = new ServerConfig(1, Calendar.getInstance().getTime(), Const.Faction.FACTION_ID);

			// 这里必须同步保存，确保主键被返回以及数据库中有数据
			try {
				dao.flush2Db(this.serverData);
			} catch (Exception e) {
				log.error("全局数据初始化失败", e);
				System.exit(1);
			}
		}
		this.key = new AtomicLong(this.serverData.getSequence());

		if (!ServerLancher.isDebug()) {// 非开发环境定时保存
			long interval = TimeUnit.MINUTES.toMillis(2);
			LogicThread.scheduleTask(new DelayedTask(interval, interval) {

				@Override
				public void run() {
					saver.saveAsyn();
				}
			});
		}

		// 查询是否合过服
		isMergeServer = dao.getServerIdCount() > 1;
	}

	public static GlobalDataManager getInstance() {
		return instance;
	}

	/**
	 * 生成一个主键，该方法是线程安全的
	 * 
	 * @return
	 */
	public String generatePrimaryKey() {
		if (this.shutdown) {
			throw new IllegalStateException();
		}

		String result = TextUtil.format("{0}-{1}-{2}", this.platform, ServerLancher.getServerId(),
				this.key.incrementAndGet());
		if (ServerLancher.isDebug()) {
			this.saver.saveAsyn();
		}

		return result;
	}

	/**
	 * 生成公会ID，该方法是线程安全的
	 * 
	 * @return
	 */
	public synchronized String generateFactionId() {
		if (this.shutdown) {
			throw new IllegalStateException();
		}

		long factionId = this.serverData.getFactionSeq() + 1;
		this.serverData.setFactionSeq(factionId);
		if (ServerLancher.isDebug()) {
			this.saver.saveAsyn();
		}
		return TextUtil.format("{0}-{1}-{2}", this.platform, ServerLancher.getServerId(), factionId);
	}

	public void shutdown() throws Exception {
		GlobalDataManager.log.warn(TextUtil.format("The last key is {0}.", this.generatePrimaryKey()));
		if (this.shutdown) {
			return;
		}
		this.shutdown = true;
		this.save(this.cloneData());
	}

	/**
	 * 获取服务器的副本数据
	 * 
	 * @param templateId
	 * @return
	 */
	public ServerCopy getServerCopy(int templateId) {
		return this.serverData.getServerCopys().get(templateId);
	}

	/**
	 * 移除关卡占领者
	 * 
	 * @param templateId
	 * @return
	 */
	public ServerCopy removeServerCopy(int templateId) {
		return this.serverData.getServerCopys().remove(templateId);
	}

	public List<ServerCopy> getAllServerCopy() {
		return new ArrayList<ServerCopy>(serverData.getServerCopys().values());
	}

	/**
	 * 创建一条新的服务器副本记录
	 * 
	 * @param copyId
	 * @param roleId
	 */
	public void newServerCopy(int copyId, String roleId) {
		this.serverData.getServerCopys().put(copyId, new ServerCopy(this.serverData, copyId, roleId));
	}

	@Override
	public void save(byte[] data) {
		try {
			log.debug("正在保存全局数据...");
			dao.flush2Db((ServerConfig) TextUtil.bytesToObject(data));
			log.debug("全局数据保存成功。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开服到现在几天了
	 * 
	 * @return
	 */
	public int getServerOpenDay() {
		long now = System.currentTimeMillis();
		int result = (int) ((now - this.serverData.getOpenTime().getTime()) / TimeUnit.DAYS.toMillis(1));
		return Math.max(0, result);
	}

	public Date getServerOpenTime() {
		return this.serverData.getOpenTime();
	}

	/**
	 * 获取名人堂排行，Property的code和value分别表示角色ID和关卡数量
	 * 
	 * @return
	 */
	public SortedSet<Property> getHallOfFameRoleIdListOrderByCount() {
		SortedSet<Property> set = new TreeSet<Property>(new Comparator<Property>() {
			@Override
			public int compare(Property o1, Property o2) {
				int com = o2.value - o1.value;
				if (com == 0) {
					com = o2.code.compareToIgnoreCase(o1.code);
				}
				return com;
			}
		});
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (ServerCopy sc : this.serverData.getServerCopys().values()) {
			int temp = 0;
			if (map.containsKey(sc.getChampionId())) {
				temp = map.get(sc.getChampionId());
			}

			map.put(sc.getChampionId(), temp + 1);
		}
		for (String roleId : map.keySet()) {
			set.add(new Property(roleId, map.get(roleId)));
		}
		return set;
	}

	@Override
	public byte[] cloneData() {
		this.serverData.setSequence(this.key.get());
		return TextUtil.objectToBytes(this.serverData);
	}

	public String getPlaform() {
		return this.platform;
	}

	/**
	 * 处理关卡占领上限等逻辑
	 * 
	 * @param rt
	 */
	public void afterHoldServerCopy(final IRole rt) {
		Collection<ServerCopy> holdCopys = CollectionUtil.where(this.serverData.getServerCopys().values(),
				new IPredicate<ServerCopy>() {
					@Override
					public boolean check(ServerCopy item) {
						return item.getChampionId().equals(rt.getRoleId());
					}
				});

		VipT vt = XsgVipManager.getInstance().findVipT(rt.getVipLevel());
		int overflow = holdCopys.size() - vt.maxServerCopy;
		if (overflow > 0) {
			// 这里认为ID小的副本等级低
			SortedSet<Integer> copyIdSet = new TreeSet<Integer>();
			for (ServerCopy sc : holdCopys) {
				copyIdSet.add(sc.getTemplateId());
			}
			Integer[] array = copyIdSet.toArray(new Integer[0]);
			for (int i = 0; i < overflow; i++) {
				this.serverData.getServerCopys().remove(array[i]);
			}
		}
	}

	public boolean isMergeServer() {
		return isMergeServer;
	}
}