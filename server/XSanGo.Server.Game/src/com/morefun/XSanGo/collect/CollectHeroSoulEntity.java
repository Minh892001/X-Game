package com.morefun.XSanGo.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.CollectConsume;
import com.XSanGo.Protocol.CollectHeroSoulView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleCollectHeroSoul;
import com.morefun.XSanGo.util.TextUtil;

/**
 *
 * @author qinguofeng
 */
public class CollectHeroSoulEntity {
	@SuppressWarnings("unused")
	private final static Log logger = LogFactory.getLog(CollectHeroSoulEntity.class);

	private String dbId; // 对应的数据库实体id
	private int type;
	private int doCollectCount; // 转盘次数
	private int dayGoldCollectCount; // 今日元宝转盘次数
	private Date lastRefreshTime; // 上次刷新时间
	private Date lastDayRefreshTime; // 上次每日刷新时间
	private HeroSoulData specialHeroData; // 特殊将魂数据
	private List<HeroSoulData> normalHeroData = new ArrayList<HeroSoulData>(3); // 普通将魂数据
	private Map<Integer, CollectConsumeData> consumeData = new HashMap<Integer, CollectConsumeData>(3);
	private List<Integer> sortList;

	public CollectHeroSoulEntity() {

	}

	public CollectHeroSoulEntity(int type, int count, int dayCount, HeroSoulData specialHeroData, List<HeroSoulData> normalHeroData,
			List<CollectConsumeData> collectConsumeData, List<Integer> sortList, Date lastRefreshTime, Date lastDayRefreshTime) {
		this.type = type;
		this.doCollectCount = count;
		this.dayGoldCollectCount = dayCount;
		this.specialHeroData = specialHeroData;
		this.normalHeroData = normalHeroData;
		this.lastRefreshTime = lastRefreshTime;
		this.lastDayRefreshTime = lastDayRefreshTime;
		this.sortList = sortList;
		this.setConsumeData(collectConsumeData);
	}

	public CollectHeroSoulEntity(String dbId, int type, int count, int dayCount, Date lastRefreshTime, Date lastDayRefreshTime, HeroSoulData specialHeroData,
			List<HeroSoulData> normalHeroData, List<CollectConsumeData> collectConsumeData, List<Integer> sortList) {
		this.dbId = dbId;
		this.type = type;
		this.doCollectCount = count;
		this.dayGoldCollectCount = dayCount;
		this.lastRefreshTime = lastRefreshTime;
		this.lastDayRefreshTime = lastDayRefreshTime;
		this.specialHeroData = specialHeroData;
		this.normalHeroData = normalHeroData;
		this.sortList = sortList;
		setConsumeData(collectConsumeData);
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDoCollectCount() {
		return doCollectCount;
	}

	public void setDoCollectCount(int count) {
		this.doCollectCount = count;
	}

	/**
	 * @return the dayGoldCollectCount
	 */
	public int getDayGoldCollectCount() {
		return dayGoldCollectCount;
	}

	/**
	 * @param dayGoldCollectCount
	 *            the dayGoldCollectCount to set
	 */
	public void setDayGoldCollectCount(int dayGoldCollectCount) {
		this.dayGoldCollectCount = dayGoldCollectCount;
	}

	public Date getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(Date time) {
		lastRefreshTime = time;
	}

	/**
	 * @return the lastDayRefreshTime
	 */
	public Date getLastDayRefreshTime() {
		return lastDayRefreshTime;
	}

	/**
	 * @param lastDayRefreshTime the lastDayRefreshTime to set
	 */
	public void setLastDayRefreshTime(Date lastDayRefreshTime) {
		this.lastDayRefreshTime = lastDayRefreshTime;
	}

	public Map<Integer, CollectConsumeData> getConsumeDataMap() {
		return consumeData;
	}

	public CollectConsumeData getConsumeData(int type) {
		return consumeData.get(type);
	}

	public HeroSoulData getSpecialHeroData() {
		return specialHeroData;
	}

	public List<HeroSoulData> getNormalHeroData() {
		return normalHeroData;
	}

	/** 返回要显示的将魂的模版id数组,供客户端显示 */
	private String[] wrapHeroSoulTIdList() {
		if (sortList == null) {
			sortList = getRandomSortList(4);
		}
		String[] tids = new String[4];
		tids[0] = specialHeroData.heroSoulTemplateId;
		for (int i = 1; i < 4; i++) {
			tids[i] = normalHeroData.get(i - 1).heroSoulTemplateId;
		}
		List<String> resList = new ArrayList<String>();
		for (Integer i : sortList) {
			resList.add(tids[i]);
		}
		return resList.toArray(new String[0]);
	}

	/**
	 * 产生一个随机序列
	 */
	private List<Integer> getRandomSortList(int count) {
		List<Integer> list = new ArrayList<Integer>(count);
		for (int i = 0; i < count; i++) {
			list.add(i);
		}
		Collections.shuffle(list);
		return list;
	}

	/** 返回消耗的数组，供客户端显示 */
	private CollectConsume[] wrapCollectConsumeList() {
		List<CollectConsume> consumeList = new ArrayList<CollectConsume>();
		XsgCollectHeroSoulManager manager = XsgCollectHeroSoulManager.getInstance();
		for (CollectConsumeData data : consumeData.values()) {
			CollectConsume c = new CollectConsume();
			CollectHeroSoulT soutT = manager.getCollectHeroSoulT(getType(), data.type);
			c.count = data.count;
			if (soutT.type.equals(CollectHeroSoulT.CONSUME_TYPE_STR[CollectHeroSoulT.RMBY])) {
				c.num = manager.getGoldCollectPrice(dayGoldCollectCount + 1);
			} else {
				c.num = soutT.price;
			}
			c.type = data.type;
			consumeList.add(c);
		}
		// 按照客户端要求的顺序排序
		Collections.sort(consumeList, new Comparator<CollectConsume>() {
			@Override
			public int compare(CollectConsume o1, CollectConsume o2) {
				return o1.type - o2.type;
			}
		});
		return consumeList.toArray(new CollectConsume[0]);
	}

	/** 返回给客户端显示用的View对象 */
	public CollectHeroSoulView getView() {
		CollectHeroSoulView view = new CollectHeroSoulView();
		view.type = this.type;
		view.heroSoulList = wrapHeroSoulTIdList();
		view.consumeList = wrapCollectConsumeList();
		view.refreshConsume = getRefreshMoney();
		XsgCollectHeroSoulManager.getInstance().setCollectHeroSoulViewStatus(view);
		return view;
	}

	/** 从数据库存储结构获得对象 */
	public static CollectHeroSoulEntity fromCollectHeroSoul(RoleCollectHeroSoul collectSoul) {
		CollectDBData data = TextUtil.GSON.fromJson(collectSoul.getViewJsonStr(), CollectDBData.class);
		CollectHeroSoulEntity entity = new CollectHeroSoulEntity(collectSoul.getId(), collectSoul.getType(),
				collectSoul.getCollectCount(), collectSoul.getDayGoldCollectCount(), collectSoul.getLastRefreshTime(), 
				collectSoul.getLastDayRefreshTime(), data.specialHeroData,
				data.normalHeroData, data.collectConsumeData, data.sortList);
		return entity;
	}

	/** 转换为数据库存储结构 */
	public RoleCollectHeroSoul toCollectHeroSoul(Role r) {
		CollectDBData data = new CollectDBData();
		data.normalHeroData = this.normalHeroData;
		data.specialHeroData = this.specialHeroData;
		data.sortList = this.sortList;
		data.collectConsumeData = new ArrayList<CollectConsumeData>(this.consumeData.values());
		String viewStr = TextUtil.GSON.toJson(data);
		if (TextUtil.isBlank(dbId)) {
			dbId = GlobalDataManager.getInstance().generatePrimaryKey();
		}
		RoleCollectHeroSoul chs = new RoleCollectHeroSoul(r, dbId, type, doCollectCount, dayGoldCollectCount,
				lastRefreshTime, lastDayRefreshTime, viewStr);
		return chs;
	}

	public void resetSortList() {
		// hard code, 每次随机产生4个将魂
		sortList = getRandomSortList(4);
	}

	public void setSpecialHeroData(HeroSoulData data) {
		specialHeroData = data;
	}

	public void setNormalHeroData(List<HeroSoulData> dataList) {
		normalHeroData = dataList;
	}

	public void setConsumeData(List<CollectConsumeData> dataList) {
		for (CollectConsumeData data : dataList) {
			consumeData.put(data.type, data);
		}
	}

	/**
	 * 返回需要消耗的对应类型的数量
	 * 
	 * @param consumeType
	 *            ,消耗的类型
	 */
	public int getMoney(int consumeType) {
		XsgCollectHeroSoulManager manager = XsgCollectHeroSoulManager.getInstance();
		CollectHeroSoulT soutT = manager.getCollectHeroSoulT(getType(), consumeType);
		int price = soutT.price;
		if (consumeType == CollectHeroSoulT.RMBY) {
			price = manager.getGoldCollectPrice(dayGoldCollectCount + 1);
		}
		return price;
	}

	/** 返回刷新需要的元宝数 */
	public int getRefreshMoney() {
		CollectTimeSettingT settingT = XsgCollectHeroSoulManager.getInstance().getTimeSetting(getType());
		return settingT.goldConsume;
	}

	static class CollectDBData {
		public HeroSoulData specialHeroData;
		public List<HeroSoulData> normalHeroData;
		public List<CollectConsumeData> collectConsumeData;
		public List<Integer> sortList; // 返回给客户端的排序数组
	}

	/** 表示消耗，金币，元宝，美酒 */
	static class CollectConsumeData {
		public int type;
		public int count; // 可用次数
		public int specialRank;
	}

	/** 表示出现在召唤转盘中的将魂 */
	static class HeroSoulData {
		public String tc;
		public String heroSoulTemplateId;
		public boolean notice;
	}
}
