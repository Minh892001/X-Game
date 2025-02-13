package com.morefun.XSanGo.collect;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.CollectHeroSoulView;
import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.collect.CollectHeroSoulEntity.CollectConsumeData;
import com.morefun.XSanGo.collect.CollectHeroSoulEntity.HeroSoulData;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

public class XsgCollectHeroSoulManager {
	private final static Log logger = LogFactory.getLog(XsgCollectHeroSoulManager.class);

	private static XsgCollectHeroSoulManager instance = new XsgCollectHeroSoulManager();
	private Map<String, Map<String, CollectHeroSoulT>> collectHeroSoulMap = new HashMap<String, Map<String, CollectHeroSoulT>>();
	private Map<String, CollectTimeSettingT> collectHeroSoulTimeSettingMap = new HashMap<String, CollectTimeSettingT>();

	// 相关常量设置
	private final String NORMAL_HERO_STR = CollectHeroSoulT.HERO_STR[CollectHeroSoulT.NORMAL_HERO];
	private final String TIMELIMIT_HERO_STR = CollectHeroSoulT.HERO_STR[CollectHeroSoulT.TIMELIMIT_HERO];
	private final String GOLD_STR = CollectHeroSoulT.CONSUME_TYPE_STR[CollectHeroSoulT.GOLD];
	private final String RMBY_STR = CollectHeroSoulT.CONSUME_TYPE_STR[CollectHeroSoulT.RMBY];
	private final String WINE_STR = CollectHeroSoulT.CONSUME_TYPE_STR[CollectHeroSoulT.WINE];

	private List<RandomNum> randomNumList = new ArrayList<XsgCollectHeroSoulManager.RandomNum>();
	private List<CollectHeroSoulGoldPriceT> collectHeroSoulGoldPrice = new ArrayList<CollectHeroSoulGoldPriceT>();

	private XsgCollectHeroSoulManager() {
		loadCollectHeroSoulScript();
	}
	
	public void loadCollectHeroSoulScript() {
		collectHeroSoulMap.clear();
		collectHeroSoulTimeSettingMap.clear();

		List<NormalCollectHeroSoulT> normalList = ExcelParser
				.parse(NormalCollectHeroSoulT.class);
		List<TimeLimitCollectHeroSoulT> timelimitList = ExcelParser
				.parse(TimeLimitCollectHeroSoulT.class);
		List<CollectTimeSettingT> timeSettingList = ExcelParser
				.parse(CollectTimeSettingT.class);
		addToMap(normalList, NORMAL_HERO_STR);
		addToMap(timelimitList, TIMELIMIT_HERO_STR);
		if (timeSettingList != null) {
			for (CollectTimeSettingT setting : timeSettingList) {
				collectHeroSoulTimeSettingMap.put(setting.name, setting);
			}
		}
		List<CollectHeroSoulNumT> numList = ExcelParser.parse(CollectHeroSoulNumT.class);
		if (numList == null || numList.size() <= 0) {
			logger.error("名将召唤,脚本配置错误");
			return;
		}
		for (CollectHeroSoulNumT numT:numList) {
			randomNumList.add(new RandomNum(numT.num1, numT.prob1));
			randomNumList.add(new RandomNum(numT.num2, numT.prob2));
			randomNumList.add(new RandomNum(numT.num3, numT.prob3));
		}
		
		collectHeroSoulGoldPrice = ExcelParser.parse(CollectHeroSoulGoldPriceT.class);
		Collections.sort(collectHeroSoulGoldPrice, new Comparator<CollectHeroSoulGoldPriceT>() {
			@Override
			public int compare(CollectHeroSoulGoldPriceT o1, CollectHeroSoulGoldPriceT o2) {
				return Integer.valueOf(o1.num).compareTo(o2.num);
			}
		});
	}

	private void addToMap(List<? extends CollectHeroSoulT> list, String key) {
		if (list != null) {
			Map<String, CollectHeroSoulT> map = new HashMap<String, CollectHeroSoulT>();
			for (CollectHeroSoulT collect : list) {
				if (collect.num <= 0) {
					collect.num = -1;
				}
				map.put(collect.type, collect);
			}
			collectHeroSoulMap.put(key, map);
		}
	}

	public static XsgCollectHeroSoulManager getInstance() {
		return instance;
	}

	public CollectTimeSettingT getTimeSetting(int type) {
		return collectHeroSoulTimeSettingMap
				.get(CollectHeroSoulT.HERO_STR[type]);
	}

	public CollectHeroSoulT getCollectHeroSoulT(int type, int consume) {
		return collectHeroSoulMap.get(CollectHeroSoulT.HERO_STR[type]).get(
				CollectHeroSoulT.CONSUME_TYPE_STR[consume]);
	}
	
	public int getGoldCollectPrice(int num) {
		int price = Integer.MAX_VALUE;
		for (CollectHeroSoulGoldPriceT t:collectHeroSoulGoldPrice) {
			price = t.price;
			if (t.num >= num){
				break;
			}
		}
		return price;
	}

	/**
	 * 获取两个日期相隔的秒数
	 * */
	private long getTimeInterval(String startTime, Date endTime) {
		Date tStart = DateUtil.parseDate(startTime);
		return DateUtil.compareTime(tStart, endTime) / 1000;
	}

	/**
	 * 获取某个时刻下次（今天或者明天的这个时刻，如果今天的这个时刻已经过去，就返回明天的）的Date表示形式
	 * */
	private Date getNextDateOfTimeStr(String timeStr) {
		Calendar time = DateUtil.convertCal(DateUtil.parseDate("HH:mm",
				timeStr));
		long currentInterval = DateUtil.betweenTaskHourMillis(
				time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
		long current = System.currentTimeMillis();
		Date nextDate = new Date(current + currentInterval);
		return nextDate;
	}

	/** 设置开始状态及结束时间 */
	public void setCollectHeroSoulViewStatus(CollectHeroSoulView view) {
		final int OFF = 0, ON = 1, END = 2;
		Date current = Calendar.getInstance().getTime();
		CollectTimeSettingT setting = getTimeSetting(view.type);
		switch (view.type) {
		case CollectHeroSoulT.TIMELIMIT_HERO: // 限时活动时间设置
			view.status = END; // 默认活动为结束状态
			while (!TextUtil.isBlank(setting.timelimitStart)
					&& !TextUtil.isBlank(setting.timelimitEnd)) {
				long intervalStart = getTimeInterval(setting.timelimitStart,
						current);
				if (intervalStart > 0) { // 还没开始
					view.status = OFF;
					view.time = intervalStart;
					break;
				}
				long intervalEnd = getTimeInterval(setting.timelimitEnd,
						current);
				if (intervalEnd > 0) { // 已经开始还没结束
					view.status = ON;
					view.time = intervalEnd;
					break;
				}

				break;
			}
			break;
		case CollectHeroSoulT.NORMAL_HERO: // 在野名将时间设置
			view.status = ON;
			long interval = DateUtil.compareTime(
					getNextDateOfTimeStr(setting.resetTime), current) / 1000;
			view.time = interval; // 这个时间表示下次刷新时间
			break;
		default:
			break;
		}
	}

	/**
	 * 包装一个将魂对象
	 * 
	 * @param tc
	 *            , 将魂的TC
	 * @param notice
	 *            , 抽中后是否需要发送广播
	 * @param data
	 *            , 要包装的将魂对象
	 * */
	private void wrapHeroSoulData(String tc, boolean notice, HeroSoulData data) {
		if (data != null) {
			data.tc = tc;
			data.notice = notice;
		}
	}

	/** 获取消耗类型的int标识 */
	private static int getConsumeType(String type) {
		if (CollectHeroSoulT.CONSUME_TYPE_STR[CollectHeroSoulT.GOLD]
				.equals(type)) {
			return CollectHeroSoulT.GOLD;
		}
		if (CollectHeroSoulT.CONSUME_TYPE_STR[CollectHeroSoulT.RMBY]
				.equals(type)) {
			return CollectHeroSoulT.RMBY;
		}
		if (CollectHeroSoulT.CONSUME_TYPE_STR[CollectHeroSoulT.WINE]
				.equals(type)) {
			return CollectHeroSoulT.WINE;
		}
		return -1;
	}

	/** 将模版配置中的字符串类型转换为数字类型的标识 */
	private static int getHeroSoulType(String typeStr) {
		return CollectHeroSoulT.HERO_STR[CollectHeroSoulT.NORMAL_HERO]
				.equals(typeStr) ? CollectHeroSoulT.NORMAL_HERO
				: CollectHeroSoulT.TIMELIMIT_HERO;
	}
	
	/**
	 * 获取普通将魂的随机概率
	 * */
	public int[] getNormalRank(int type, int consumeType) {
		CollectHeroSoulT soulT = getCollectHeroSoulT(type, consumeType);
		return new int[]{soulT.normalWeight1, soulT.normalWeight2, soulT.normalWeight3};
	}
	
	/**
	 * 获取特殊将魂的随机概率
	 * */
	public int getSpecialRank(int type, int consumeType) {
		CollectHeroSoulT soulT = getCollectHeroSoulT(type, consumeType);
		return soulT.specialWeight;
	}
	
	/**
	 * 获取计算特殊将魂的最大次数限制
	 * */
	public int getRandomMax(int type, int consumeType) {
		CollectHeroSoulT soulT = getCollectHeroSoulT(type, consumeType);
		return soulT.randomMax;
	}
	
	/**
	 * 获取计算特殊将魂的最低次数
	 * */
	public int getRandomMin(int type, int consumeType) {
		CollectHeroSoulT soulT = getCollectHeroSoulT(type, consumeType);
		return soulT.randomMin;
	}

	public CollectHeroSoulEntity createEntity(IRole role, String typeStr,
			List<CollectHeroSoulT> templateList) {
		CollectHeroSoulT template = templateList.get(0);

		HeroSoulData specialHeroSoulData = new HeroSoulData(); // 特殊将魂
		wrapHeroSoulData(template.specialTC, (template.specialNotice == 1),
				specialHeroSoulData);

		// 设置三种消耗的数据
		List<CollectConsumeData> collectConsumeDataList = new ArrayList<CollectConsumeData>(
				3);
		for (CollectHeroSoulT t : templateList) {
			CollectConsumeData data = new CollectConsumeData();
			data.count = t.num == 0 ? -1 : t.num;
			data.type = getConsumeType(t.type);
			data.specialRank = t.specialWeight;
			collectConsumeDataList.add(data);
		}

		List<HeroSoulData> normalHeroSoulData = new ArrayList<HeroSoulData>(3); // 普通将魂
		String[] tcArray = { template.normalTC1, template.normalTC2,
				template.normalTC3 };
		int[] noticeArray = { template.normalNotice1, template.normalNotice2,
				template.normalNotice3 };
		for (int i = 0; i < 3; i++) {
			HeroSoulData normalSoulData = new HeroSoulData();
			wrapHeroSoulData(tcArray[i], noticeArray[i] == 1, normalSoulData);
			normalHeroSoulData.add(normalSoulData);
		}

		// 构造entity
		int type = getHeroSoulType(typeStr);
		int doCollectCount = 0;
		Date currentDate = Calendar.getInstance().getTime();
		CollectHeroSoulEntity entity = new CollectHeroSoulEntity(type,
				doCollectCount, 0, specialHeroSoulData,
				normalHeroSoulData, collectConsumeDataList, null, currentDate, currentDate);
		refreshHeroSoul(entity, role, true);

		return entity;
	}

	private String doTcRandom(XsgRewardManager rewardManager, IRole role,
			String tc) {
		ItemView[] items = rewardManager.generateItemView(rewardManager.doTc(
				role, tc));
		if (items == null || items.length <= 0) {
			return null;
		}
		return items[NumberUtil.random(items.length)].templateId;
	}

	/** 重置剩余次数 */
	private void resetConsumeData(CollectHeroSoulEntity entity){
		for (CollectConsumeData data : entity.getConsumeDataMap().values()) {
			CollectHeroSoulT t = getCollectHeroSoulT(entity.getType(),
					data.type);
			data.count = t.num;
		}
	}
	/**
	 * 是否需要初始化
	 * 
	 * 当没有产生将魂的模版ID或者tc与配置文件中的tc不一致时重新初始化
	 * */
	public boolean needInit(CollectHeroSoulEntity entity, CollectHeroSoulT soutT) {
		if (entity.getSpecialHeroData() == null
				|| entity.getNormalHeroData() == null
				|| TextUtil
						.isBlank(entity.getSpecialHeroData().heroSoulTemplateId) || 
						!entity.getSpecialHeroData().tc.equals(soutT.specialTC)) {
			return true;
		}
		List<String> tcArray = new ArrayList<String>();
		tcArray.add(soutT.normalTC1);
		tcArray.add(soutT.normalTC2);
		tcArray.add(soutT.normalTC3);
		for (HeroSoulData data : entity.getNormalHeroData()) {
			if (TextUtil.isBlank(data.heroSoulTemplateId) || !tcArray.contains(data.tc)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 限时活动是否需要重置将魂
	 * */
	private boolean needResetTimelimit(CollectHeroSoulEntity entity) {
		if (entity != null && entity.getType() == CollectHeroSoulT.TIMELIMIT_HERO) {
			CollectTimeSettingT setting = XsgCollectHeroSoulManager.getInstance().getTimeSetting(entity.getType());
			long lastRefreshTime = entity.getLastRefreshTime().getTime();
			long refreshTime = DateUtil.parseDate(setting.timelimitStart).getTime();
			if (lastRefreshTime < refreshTime) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 刷新一组将魂
	 * 
	 * @param reset
	 *            , 是否刷新剩余次数
	 * */
	public boolean refreshHeroSoul(CollectHeroSoulEntity entity, IRole role, boolean reset) {
		CollectTimeSettingT setting = getTimeSetting(entity.getType());
		// 任意一种消耗类型, 将魂的配置都一样.
		CollectHeroSoulT soutT = getCollectHeroSoulT(entity.getType(), 0);
		// 刷新将魂, 需要初始化或者允许刷新将魂的情况下刷新
		if (setting.autoRefresh == 1 || needResetTimelimit(entity) || needInit(entity, soutT)) {
			HeroSoulData specialHeroData = entity.getSpecialHeroData();
			List<HeroSoulData> normalHeroData = entity.getNormalHeroData();
			if (specialHeroData == null || normalHeroData.size() <= 0) {
				return false;
			}
			// 更新TC和notice配置
			specialHeroData.tc = soutT.specialTC;
			specialHeroData.notice = soutT.specialNotice == 1;
			String tcs[] = {soutT.normalTC1, soutT.normalTC2, soutT.normalTC3};
			int ns[] = {soutT.normalNotice1, soutT.normalNotice2, soutT.normalNotice3};
			for (int i = 0; i < normalHeroData.size(); i++) {
				HeroSoulData data =  normalHeroData.get(i);
				data.tc = tcs[i];
				data.notice = (ns[i] == 1);
			}

			XsgRewardManager rewardManager = XsgRewardManager.getInstance();
			String specialHeroId = doTcRandom(rewardManager, role,
					specialHeroData.tc);
			if (TextUtil.isBlank(specialHeroId)) {
				return false;
			}
			specialHeroData.heroSoulTemplateId = specialHeroId;
			for (HeroSoulData data : normalHeroData) {
				String normalHeroId = doTcRandom(rewardManager, role, data.tc);
				if (TextUtil.isBlank(normalHeroId)) {
					return false;
				}
				data.heroSoulTemplateId = normalHeroId;
			}
			entity.resetSortList();
			entity.setLastRefreshTime(Calendar.getInstance().getTime());
		}
		if (reset) {
			resetConsumeData(entity);
		}
		return true;
	}

	/**
	 * 返回某种类型的 CollectHeroSoulEntity
	 * */
	private CollectHeroSoulEntity getHeroSoulEntity(IRole role, String typeStr) {
		Map<String, CollectHeroSoulT> map = collectHeroSoulMap.get(typeStr);
		if (map == null) {
			return null;
		}
		List<CollectHeroSoulT> collectHeroSoulTList = new ArrayList<CollectHeroSoulT>();
		collectHeroSoulTList.add(map.get(GOLD_STR));
		collectHeroSoulTList.add(map.get(RMBY_STR));
		collectHeroSoulTList.add(map.get(WINE_STR));

		CollectHeroSoulEntity entity = createEntity(role, typeStr,
				collectHeroSoulTList);
		return entity;
	}
	
	/**
	 * 判断是否正在进行
	 * @param type 类型, 在野名将还是限时任务
	 * */
	public boolean isGoingOn(int type) {
		CollectTimeSettingT setting = getTimeSetting(type);
		if (setting == null) {
			return false;
		}
		long start = DateUtil.parseDate(setting.timelimitStart).getTime();
		long current = System.currentTimeMillis();
		long end = DateUtil.parseDate(setting.timelimitEnd).getTime();
		return ((start <= current) && (current <= end));
	}

	/**
	 * 返回一组 CollectHeroSoulEntity 对象，在野名将和限时任务
	 * */
	public CollectHeroSoulEntity[] generateHeroSouls(IRole role) {
		CollectHeroSoulEntity normalEntity = getHeroSoulEntity(role,
				NORMAL_HERO_STR);
		CollectHeroSoulEntity timelimitEntity = getHeroSoulEntity(role,
				TIMELIMIT_HERO_STR);
		if (normalEntity == null || timelimitEntity == null) {
			return null;
		}
		CollectHeroSoulEntity[] entitys = { normalEntity, timelimitEntity };
		return entitys;
	}

	public CollectHeroSoulController createCollectHeroSoulControler(IRole role,
			Role roleDB) {
		return new CollectHeroSoulController(role, roleDB);
	}
	
	/** 获取一个随机的数量 */
	public int getRandomNumCount () {
		RandomRange<RandomNum> random = new RandomRange<XsgCollectHeroSoulManager.RandomNum>(randomNumList);
		RandomNum num = random.random();
		return num.getCount();
	}
	
	static class RandomNum implements IRandomHitable{ 
		int count;
		int rank;
		
		public RandomNum(int count, int rank) {
			this.count = count;
			this.rank = rank;
		}
		
		@Override
		public int getRank() {
			return rank;
		}
		
		public int getCount() {
			return count;
		}
	}
}