package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import com.XSanGo.Protocol.ActivityInfoView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.ShootAwardInfo;
import com.XSanGo.Protocol.SummationActivityView;
import com.XSanGo.Protocol.SummationReward;
import com.XSanGo.Protocol.UpActivityInfoView;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.api.XsgApiManager;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.ArenaAwardLog;
import com.morefun.XSanGo.db.game.ArenaRank;
import com.morefun.XSanGo.db.game.FootballBet;
import com.morefun.XSanGo.db.game.InviteCode;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.SeckillItem;
import com.morefun.XSanGo.db.game.ShootScoreRank;
import com.morefun.XSanGo.db.game.ShootScoreRankDao;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.goodsExchange.ExchangeActivityControler;
import com.morefun.XSanGo.goodsExchange.ExchangeItemT;
import com.morefun.XSanGo.goodsExchange.IExchangeActivityControler;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * 活动
 */
public class XsgActivityManage {
	/*
	 * 1 升级奖励 2 我要做VIP 3 名将召唤 4 名将仰慕 5 充值好礼 6 消费有礼 7 邀请好友 8 天天秒杀 9 充值厚礼 10 消费有奖
	 * 11 冲级有奖 12 最强战力 13 成长基金 14 VIP特权礼包
	 */
	public enum ActivityTemplate {
		None(0), RoleLevelUpReward(1), MakeVip(2), CollectHeroSoul(3), HeroAdmire(4), SumCharge(5), SumConsume(6), InviteFriend(
				7), Seckill(8), DayCharge(9), DayConsume(10), ChongjiyouJiang(11), ZuiqingZhanli(12), Fund(13), VipGift(
				14), SendJunLing(15), LeijiLogin(16), BigSumCharge(17), BigSumConsume(18), BigDayCharge(19), BigDayConsume(
				20), FirstJia(21), FortuneWheel(22), ItemExchange(24), LevelWeal(25), ForverLeijiLogin(26), Share(28), ResourceBack(
				29);
		private int _value = 0;

		private ActivityTemplate(int v) {
			_value = v;
		};

		public int getValue() {
			return _value;
		}
	}

	// private final Log log = LogFactory.getLog(getClass());

	private static final String letter = "ABCDEFGHJKMNPQRSTUVWXYZ";

	private static final String number = "0123456789";

	private Random random = new Random();
	/**
	 * 活动配置
	 */
	private List<ActivityT> activityTs = new ArrayList<ActivityT>();
	/**
	 * 升级奖励配置
	 */
	private List<UpGiftT> upGiftTList = new ArrayList<UpGiftT>();

	/** 累计充值配置 */
	private Map<Integer, SummationActivityComponentT> sumChargeTMap;
	/** 累计消费配置 */
	private Map<Integer, SummationActivityComponentT> sumConsumeTMap;

	/** Big累计充值配置 */
	private Map<Integer, SummationActivityComponentT> bigSumChargeTMap;
	/** Big累计消费配置 */
	private Map<Integer, SummationActivityComponentT> bigSumConsumeTMap;

	private static XsgActivityManage instance = new XsgActivityManage();

	public static final int TIME_LIMIT = 2;// 限时

	public static final int CLOSEED = 0;// 关闭

	/** 根据开服时间自动设置 */
	public static final int OPEN_AFTER = 3;

	/** 百步穿杨活动ID */
	public static final int SHOOT_ACTIVEID = 25;

	/** 大富温ID */
	public static final int LOTTERY_ACTIVEID = 27;

	/**
	 * 平局
	 */
	public static final int DRAW = -1;

	/**
	 * 奖杯道具ID
	 */
	public static final String JIANG_BEI_ID = "jiangbei";

	/** 百步穿杨中奖记录保存条数 */
	private static final int award_record_size = 20;
	/** 百步穿杨 中奖记录 */
	private LinkedList<ShootAwardInfo> list_award_record = new LinkedList<ShootAwardInfo>();

	/** 百步穿杨积分排行榜 */
	private LinkedList<ShootScoreRank> shootScoreList = new LinkedList<ShootScoreRank>();
	private Map<String, ShootScoreRank> shootScoreMap = new HashMap<String, ShootScoreRank>();

	/**
	 * 我要做VIP配置
	 */
	public MakeVipT makeVipT;

	public final static int OPEN = 1;

	/**
	 * 我要做VIP题库
	 */
	private List<AnswerT> answerTs = new ArrayList<AnswerT>();

	/**
	 * 邀请好友活动数据
	 */
	public List<InviteActivityT> inviteActivitys;

	/**
	 * 邀请好友活动配置
	 */
	public InviteConfT inviteConf;

	/**
	 * 全局邀请码
	 */
	private Map<String, InviteCode> inviteCodes = new HashMap<String, InviteCode>();

	/**
	 * 成长基金奖励配置
	 */
	private Map<Integer, FundT> fundRewards = new HashMap<Integer, FundT>();

	/**
	 * 成长基金基本配置
	 */
	private FundConfigT fundConfig;

	/**
	 * 等级奖励配置
	 */
	private Map<Integer, LevelRewardT> levelRewardMap = new HashMap<Integer, LevelRewardT>();

	/**
	 * 第一佳活动配置
	 */
	private Map<Integer, FirstJiaT> firstJiaRewardMap = new HashMap<Integer, FirstJiaT>();

	/**
	 * 累计登录
	 */
	private Map<Integer, DayLoginRewardT> dayLoginRewardMap = new HashMap<Integer, DayLoginRewardT>();

	/**
	 * 累计登录永久
	 */
	private Map<Integer, DayforverLoginRewardT> dayforverLoginRewardMap = new HashMap<Integer, DayforverLoginRewardT>();

	private int openServerActiveEndTime;

	/**
	 * 全服活动基础信息<type,MAP<id,vo>>
	 */
	private Map<Integer, Map<Integer, AllServerActiveT>> allServerActionMap = new HashMap<Integer, Map<Integer, AllServerActiveT>>();

	/**
	 * 全服活动具体配置信息MAP<id,<nodeId,vo>>
	 */
	private Map<Integer, TreeMap<Integer, AllServerActiveDetailT>> allServerActionDetailMap = new HashMap<Integer, TreeMap<Integer, AllServerActiveDetailT>>();

	/**
	 * 开服活动时间限制
	 */
	private AllServerActiveTimeLimitT OpenServerTimeLimit;

	/**
	 * 开服半价道具活动
	 */
	private TreeMap<Integer, AllServerActiveSaleT> OpenServerSaleMap = new TreeMap<Integer, AllServerActiveSaleT>();

	/**
	 * 全服活动ID 所属类型
	 */
	private Map<Integer, Integer> AllServerActionId4Type = new HashMap<Integer, Integer>();

	/**
	 * 送军令
	 */
	private Map<Integer, SendJunLingT> sendJunLingMap = new HashMap<Integer, SendJunLingT>();
	/** 送军令节日活动 */
	private List<SendJunLingActivityT> sendJunLingActivityList = new ArrayList<SendJunLingActivityT>();

	/**
	 * 战力嘉奖
	 */
	private Map<Integer, PowerRewardT> powerRewardMap = new HashMap<Integer, PowerRewardT>();

	/**
	 * 秒杀活动设置
	 */
	private List<SecKillConfigT> secKillConfigTs = new ArrayList<SecKillConfigT>();

	/**
	 * 全局秒杀物品
	 */
	private List<SeckillItem> seckillItems = new ArrayList<SeckillItem>();

	/**
	 * 日充值模版
	 */
	public Map<Integer, DayChargeConsumeT> dayCharges = new HashMap<Integer, DayChargeConsumeT>();

	/**
	 * 日消费模版
	 */
	public Map<Integer, DayChargeConsumeT> dayConsumes = new HashMap<Integer, DayChargeConsumeT>();

	/**
	 * Big日充值模版
	 */
	public Map<Integer, DayChargeConsumeT> bigDayCharges = new HashMap<Integer, DayChargeConsumeT>();

	/**
	 * Big日消费模版
	 */
	public Map<Integer, DayChargeConsumeT> bigDayConsumes = new HashMap<Integer, DayChargeConsumeT>();

	/**
	 * 幸运大转盘系统配置
	 */
	private FortuneWheelConfigT fortuneWheelConfig;

	/**
	 * 幸运大转盘Vip抽奖上限配置
	 */
	private Map<Integer, FortuneWheelVipT> fortuneWheelVip = new HashMap<Integer, FortuneWheelVipT>();

	/**
	 * 幸运大转盘系统奖品配置
	 */
	private Map<Integer, List<FortuneWheelRewardT>> fortuneWheelRewards = new HashMap<Integer, List<FortuneWheelRewardT>>();

	/**
	 * 幸运大转盘系统奖品类型配置
	 */
	private Map<Integer, FortuneWheelRewardTypeT> fortuneWheelRewardType = new HashMap<Integer, FortuneWheelRewardTypeT>();

	/**
	 * 兑换活动配置
	 */
	private List<ExchangeItemT> exchangeItems = new ArrayList<ExchangeItemT>();

	/**
	 * 聚宝盆物品
	 */
	private List<CornucopiaItemT> cornucopiaItemTs = new ArrayList<CornucopiaItemT>();

	/**
	 * 聚宝盆配置
	 */
	private CornucopiaConfT cornucopiaConfT = null;

	/**
	 * 全服最高等级
	 */
	private int maxLevel;

	/**
	 * 等级福利配置
	 */
	private List<LevelWealConfigT> levelWealConfigList = null;
	private LevelWealLimitConfigT levelWealLimitConfig = null;

	/** 百步穿杨参数配置 */
	private List<MarksManParamT> marksManParamTs = new ArrayList<MarksManParamT>();

	/** 百步穿杨射击抽奖类型配置 */
	private Map<Integer, ShootParamT> shootParamMap = new HashMap<Integer, ShootParamT>();

	/** 百步穿杨射击抽奖奖励配置 */
	private List<ShootRewardT> shootRewardTs = new ArrayList<ShootRewardT>();
	private Map<Integer, ShootRewardT> shootRewardMap = new HashMap<Integer, ShootRewardT>();
	/** 百步穿杨随机奖励配置 */
	private List<RandomShootReward> shootRewardRandomList = new ArrayList<RandomShootReward>();

	/** 【超级】 百步穿杨射击抽奖奖励配置 */
	private List<ShootRewardT> shootRewardTsSuper = new ArrayList<ShootRewardT>();
	private Map<Integer, ShootRewardT> shootRewardMapSuper = new HashMap<Integer, ShootRewardT>();
	/** 【超级】 百步穿杨随机奖励配置 */
	private List<RandomShootReward> shootRewardRandomListSuper = new ArrayList<RandomShootReward>();

	/** 百步穿杨积分奖励配置 */
	private Map<Integer, ShootScoreRewardT> shootScoreRewardTs = new TreeMap<Integer, ShootScoreRewardT>(
			new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					return o2 - o1;
				}
			});;

	/** 百步穿杨积分排名奖励配置 */
	private List<ShootScoreRankT> shootScoreRankTs = new ArrayList<ShootScoreRankT>();

	/** 百步穿杨积分入榜限制配置 */
	private List<ShootScoreRankLimitT> shootScoreRankLimitTs = new ArrayList<ShootScoreRankLimitT>();

	/** 百步穿杨特殊奖励配置 */
	private Map<Integer, ShootSpecialRewardT> shootSpecialRewardTMap = new HashMap<Integer, ShootSpecialRewardT>();

	/** 资源找回 */
	private List<ResourceBackTimeT> resourceBackTimeTList = new ArrayList<ResourceBackTimeT>();
	private Map<Integer, List<ResourceBackDetailT>> resourceBackDetailTMap = new HashMap<Integer, List<ResourceBackDetailT>>();
	private Map<Integer, ResourceBackConfigT> resourceBackConfigMap = new HashMap<Integer, ResourceBackConfigT>();

	// 欧洲杯
	private FootballConfT footballConfT = null;
	private List<FootballMatchT> footballMatchTs = new ArrayList<FootballMatchT>();
	private List<FootballCountryT> footballCountryTs = new ArrayList<FootballCountryT>();
	private List<FootballBuyT> footballBuyTs = new ArrayList<FootballBuyT>();
	private List<FootballShopT> footballShopTs = new ArrayList<FootballShopT>();
	/**
	 * 押注信息 key：赛事id
	 */
	private Map<Integer, List<FootballBet>> footballBetMap = new HashMap<Integer, List<FootballBet>>();

	public static XsgActivityManage getInstance() {
		return instance;
	}

	/**
	 * 百步穿杨射击 类型
	 */
	public static enum ShootType {
		/** 射击单抽 */
		ShootOne(1),
		/** 射击十连 */
		ShootTen(2);
		private final int value;

		int getValue() {
			return value;
		}

		ShootType(int value) {
			this.value = value;
		}
	}

	/**
	 * 百步、超级百步
	 */
	public static enum ShootSystemType {
		Shoot(1), ShootSuper(2);

		private final int value;

		int getValue() {
			return value;
		}

		ShootSystemType(int value) {
			this.value = value;
		}

		public static ShootSystemType getByValue(int value) {
			for (ShootSystemType type : values()) {
				if (value == type.getValue()) {
					return type;
				}
			}
			return null;
		}
	}

	/**
	 * 百步穿杨单射刷新周期类型
	 */
	public static enum ShootCycleType {
		/** 无周期 */
		No(0),
		/** 有周期 */
		Yes(1);
		private final int value;

		int getValue() {
			return value;
		}

		ShootCycleType(int value) {
			this.value = value;
		}
	}

	/**
	 * 百步穿杨射击奖励公告类型
	 */
	public static enum ShootNoticeType {
		/** 无公告 */
		No(0),
		/** 有公告 */
		Yes(1),
		/** 记录中奖记录 */
		Record(2),
		/** 记录中奖记录并公告 */
		RecordAndNotice(3);
		private final int value;

		int getValue() {
			return value;
		}

		ShootNoticeType(int value) {
			this.value = value;
		}
	}

	/**
	 * 百步穿杨积分奖励领取状态
	 */
	public static enum ShootScoreRecvStatus {
		/** 不能领取 */
		No(0),
		/** 可领取 */
		Yes(1);
		private final int value;

		int getValue() {
			return value;
		}

		ShootScoreRecvStatus(int value) {
			this.value = value;
		}
	}

	private XsgActivityManage() {
		loadActivityScript();
		loadUpGiftScript();
		loadMakeVipScript();
		loadChargeConsumeScript();
		loadInviteFriendScript();
		loadFundScript();
		loadLevelRewardScript();
		loadSeckillScript();
		loadDayChargeConsumeScript();
		loadCombatPowerRewardScript();
		loadFirstJiaScript();
		loadDayLoginScript();
		loadDayforverLoginScript();
		loadSendJunLingScript();
		loadFortuneWheelScript();
		loadExchangeScript();
		loadCornucopiaScript();
		loadAllServerAction();
		loadAllServerActionDetail();
		loadAllServerActionTimeLimit();
		// 开服活动半价道具
		loadOpenServerSales();
		// loadLevelWealScript();
		loadMarksmanScript();
		// 初始化百步穿杨数据
		initMarksmanRank();
		// 开服活动
		initOpenServerActive();
		// 资源找回
		loadResourceBackScript();

		// 初始化押注信息
		SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		List<FootballBet> bets = simpleDao.findAll(FootballBet.class);
		for (FootballBet b : bets) {
			List<FootballBet> list = footballBetMap.get(b.getGroupId());
			if (list == null) {
				list = new ArrayList<FootballBet>();
				footballBetMap.put(b.getGroupId(), list);
			}
			list.add(b);
		}
		loadFootballScript();
	}

	/** 加载资源找回脚本 */
	private void loadResourceBackScript() {
		resourceBackTimeTList = ExcelParser.parse(ResourceBackTimeT.class);
		List<ResourceBackDetailT> resourceBackDetailTList = ExcelParser.parse(ResourceBackDetailT.class);
		if (resourceBackDetailTList != null) {
			resourceBackDetailTMap.clear();
			for (ResourceBackDetailT detailT : resourceBackDetailTList) {
				List<ResourceBackDetailT> list = resourceBackDetailTMap.get(detailT.type);
				if (list == null) {
					list = new ArrayList<ResourceBackDetailT>();
					resourceBackDetailTMap.put(detailT.type, list);
				}
				list.add(detailT);
			}
		}
		List<ResourceBackConfigT> typeConfigList = ExcelParser.parse(ResourceBackConfigT.class);
		if (typeConfigList != null) {
			resourceBackConfigMap.clear();
			for (ResourceBackConfigT configT : typeConfigList) {
				resourceBackConfigMap.put(configT.type, configT);
			}
		}
	}

	/**
	 * 加载开服活动半价道具
	 */
	private void loadOpenServerSales() {
		List<AllServerActiveSaleT> allServerSaleDetail = ExcelParser.parse(AllServerActiveSaleT.class);
		OpenServerSaleMap.clear();
		for (AllServerActiveSaleT t : allServerSaleDetail) {
			Calendar c = Calendar.getInstance();
			c.setTime(GlobalDataManager.getInstance().getServerOpenTime());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			t.openDate = DateUtil.addDays(c, t.openDay - 1).getTime();
			OpenServerSaleMap.put(t.openDay, t);
		}
	}

	/**
	 * 加载开服活动竞技场相关
	 */
	public void initOpenServerActive() {
		Map<Integer, AllServerActiveT> idMap = allServerActionMap.get(IOpenServerActiveControler.AREAN_NUM);
		if (idMap == null || idMap.size() == 0) {
			return;
		}
		for (Integer aId : idMap.keySet()) {
			AllServerActiveT t = idMap.get(aId);
			if (t == null) {
				continue;
			}
			long interval = t.startDate.getTime() - System.currentTimeMillis();
			if (interval < 0) {
				continue;
			}
			LogicThread.scheduleTask(new DelayedTask(interval, 0) {
				@Override
				public void run() {
					try {
						List<IRole> onlineRoles = XsgRoleManager.getInstance().findOnlineList();
						for (IRole rt : onlineRoles) {
							ArenaRank rank = rt.getArenaRankControler().getArenaRank();
							if (rank == null)
								continue;
							int rankNum = rank.getRank();
							rt.getOpenServerActiveControler().updateProgress(IOpenServerActiveControler.AREAN_NUM,
									rankNum + "");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 加载永全服活动
	 */
	public void loadAllServerAction() {
		List<AllServerActiveT> allServerAction = ExcelParser.parse(AllServerActiveT.class);
		allServerActionMap.clear();
		openServerActiveEndTime = 0;
		for (AllServerActiveT a : allServerAction) {
			AllServerActionId4Type.put(a.activeId, a.activeType);
			if (openServerActiveEndTime < a.overTime) {
				openServerActiveEndTime = a.overTime;
			}
			// 活动应该开启时间
			Calendar c1 = Calendar.getInstance();
			c1.setTime(GlobalDataManager.getInstance().getServerOpenTime());
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			Calendar c = Calendar.getInstance();
			c.setTime(GlobalDataManager.getInstance().getServerOpenTime());
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			a.startDate = DateUtil.addDays(c1, a.startTime - 1).getTime();
			a.endDate = DateUtil.addDays(c, a.overTime - 1).getTime();
			Map<Integer, AllServerActiveT> map = allServerActionMap.get(a.activeType);
			if (map == null) {
				map = new HashMap<Integer, AllServerActiveT>();
				map.put(a.activeId, a);
				allServerActionMap.put(a.activeType, map);
			} else {
				map.put(a.activeId, a);
			}
		}
	}

	/**
	 * 本次开服活动是否已经结束
	 * 
	 * @return
	 */
	public boolean isEndOpenServerActive() {
		if (!isInOpenServerCfgTime()) {
			return true;
		}
		if (!isInOpenServerCfgTime()) {
			return true;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(GlobalDataManager.getInstance().getServerOpenTime());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		Date end = DateUtil.addDays(c, openServerActiveEndTime - 1).getTime();
		if (DateUtil.isBetween(GlobalDataManager.getInstance().getServerOpenTime(), end)) {
			return false;
		}
		return true;
	}

	/**
	 * 加载永全服活动时间限制
	 */
	public void loadAllServerActionTimeLimit() {
		List<AllServerActiveTimeLimitT> list = ExcelParser.parse(AllServerActiveTimeLimitT.class);
		for (AllServerActiveTimeLimitT detail : list) {
			OpenServerTimeLimit = detail;
			break;
		}
	}

	/**
	 * 是否在开服活动时间内
	 * 
	 * @return
	 */
	public boolean isInOpenServerCfgTime() {
		if (OpenServerTimeLimit == null) {
			return false;
		}
		if (DateUtil.isBetween(GlobalDataManager.getInstance().getServerOpenTime(),
				DateUtil.parseDate(OpenServerTimeLimit.openTime1), DateUtil.parseDate(OpenServerTimeLimit.openTime2))) {
			return true;
		}
		return false;
	}

	/**
	 * 加载永全服活动具体信息
	 */
	public void loadAllServerActionDetail() {
		List<AllServerActiveDetailT> allServerActionDetail = ExcelParser.parse(AllServerActiveDetailT.class);
		allServerActionDetailMap.clear();
		for (AllServerActiveDetailT detail : allServerActionDetail) {
			detail.rewardMap = splitReward(detail.reward);
			TreeMap<Integer, AllServerActiveDetailT> detailMap = allServerActionDetailMap.get(detail.activeId);
			if (detailMap == null) {
				detailMap = new TreeMap<Integer, AllServerActiveDetailT>();
				detailMap.put(detail.activeNum, detail);
				allServerActionDetailMap.put(detail.activeId, detailMap);
			} else {
				detailMap.put(detail.activeNum, detail);
			}
		}
	}

	/**
	 * 加载兑换配置
	 */
	public void loadExchangeScript() {
		this.exchangeItems.clear();
		this.exchangeItems = ExcelParser.parse("物物兑换", ExchangeItemT.class);
	}

	public List<ExchangeItemT> getExchangeItems() {
		return this.exchangeItems;
	}

	/**
	 * 加载活动列表
	 */
	public void loadActivityScript() {
		this.activityTs.clear();
		List<ActivityT> activityList = ExcelParser.parse("活动列表", ActivityT.class);
		for (ActivityT a : activityList) {
			if (a.type == CLOSEED) {
				continue;
			}
			if (a.type == OPEN_AFTER) {
				Date openDate = GlobalDataManager.getInstance().getServerOpenTime();
				a.startTime = DateUtil.toString(openDate.getTime());
				Calendar openCal = Calendar.getInstance();
				openCal.setTime(openDate);
				Calendar endDate = DateUtil.addDays(openCal, a.openAfterDays);
				endDate.set(Calendar.HOUR_OF_DAY, 23);
				endDate.set(Calendar.MINUTE, 59);
				endDate.set(Calendar.SECOND, 59);
				a.endTime = DateUtil.toString(endDate.getTimeInMillis());
			}
			this.activityTs.add(a);
		}
		// 加载API活动
		XsgApiManager.getInstance().loadScript(activityTs);
	}

	/**
	 * 加载升级奖励
	 */
	public void loadUpGiftScript() {
		this.upGiftTList = ExcelParser.parse("升级奖励脚本", UpGiftT.class);
	}

	/**
	 * 加载我要做VIP
	 */
	public void loadMakeVipScript() {
		this.makeVipT = ExcelParser.parse(MakeVipT.class).get(0);
		this.answerTs = ExcelParser.parse(AnswerT.class);
	}

	/**
	 * 加载累计充值消费
	 */
	public void loadChargeConsumeScript() {
		// 累计充值活动配置
		SummationActivityT sat = ExcelParser.parse("周期充值好礼", SummationActivityT.class).get(0);
		this.sumChargeTMap = new HashMap<Integer, SummationActivityComponentT>();
		for (SummationActivityComponentT component : sat.components) {
			this.sumChargeTMap.put(component.thresholdVal, component);
		}

		// 累计消费活动配置
		sat = ExcelParser.parse("周期消费送礼", SummationActivityT.class).get(0);
		this.sumConsumeTMap = new HashMap<Integer, SummationActivityComponentT>();
		for (SummationActivityComponentT component : sat.components) {
			this.sumConsumeTMap.put(component.thresholdVal, component);
		}

		// big累计充值活动配置
		sat = ExcelParser.parse("周期充值好礼2", SummationActivityT.class).get(0);
		this.bigSumChargeTMap = new HashMap<Integer, SummationActivityComponentT>();
		for (SummationActivityComponentT component : sat.components) {
			this.bigSumChargeTMap.put(component.thresholdVal, component);
		}

		// big累计消费活动配置
		sat = ExcelParser.parse("周期消费送礼2", SummationActivityT.class).get(0);
		this.bigSumConsumeTMap = new HashMap<Integer, SummationActivityComponentT>();
		for (SummationActivityComponentT component : sat.components) {
			this.bigSumConsumeTMap.put(component.thresholdVal, component);
		}
	}

	/**
	 * 加载邀请好友
	 */
	public void loadInviteFriendScript() {
		inviteConf = ExcelParser.parse(InviteConfT.class).get(0);
		inviteActivitys = ExcelParser.parse(InviteActivityT.class);
		// 初始化邀请码
		SimpleDAO simpleDAO = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		List<InviteCode> codes = simpleDAO.findAll(InviteCode.class);
		for (InviteCode i : codes) {
			this.inviteCodes.put(i.getCode(), i);
		}
		generateInviteCode(1000);
	}

	/**
	 * 加载基金
	 */
	public void loadFundScript() {
		// 成长基金
		fundConfig = ExcelParser.parse(FundConfigT.class).get(0);
		List<FundT> funds = ExcelParser.parse(FundT.class);
		fundRewards.clear();
		for (FundT fundt : funds) {
			fundRewards.put(fundt.level, fundt);
		}
	}

	/**
	 * 加载等级奖励
	 */
	public void loadLevelRewardScript() {
		// 等级奖励
		List<LevelRewardT> levelRewards = ExcelParser.parse(LevelRewardT.class);
		levelRewardMap.clear();
		for (LevelRewardT reward : levelRewards) {
			levelRewardMap.put(reward.level, reward);
		}
	}

	/**
	 * 加载第一佳活动
	 */
	public void loadFirstJiaScript() {
		// 第一佳奖励
		List<FirstJiaT> firstJiaReward = ExcelParser.parse(FirstJiaT.class);
		firstJiaRewardMap.clear();
		for (FirstJiaT reward : firstJiaReward) {
			firstJiaRewardMap.put(reward.level, reward);
		}
	}

	/**
	 * 加载每日登录活动
	 */
	public void loadDayLoginScript() {
		// 每日登录奖励
		List<DayLoginRewardT> dayLoginReward = ExcelParser.parse(DayLoginRewardT.class);
		dayLoginRewardMap.clear();
		for (DayLoginRewardT reward : dayLoginReward) {
			dayLoginRewardMap.put(reward.day, reward);
		}
	}

	/**
	 * 加载永久每日登录活动
	 */
	public void loadDayforverLoginScript() {
		// 每日登录奖励
		List<DayforverLoginRewardT> dayforverLoginReward = ExcelParser.parse(DayforverLoginRewardT.class);
		dayforverLoginRewardMap.clear();
		for (DayforverLoginRewardT reward : dayforverLoginReward) {
			dayforverLoginRewardMap.put(reward.day, reward);
		}
	}

	/**
	 * 加载送军令脚本
	 */
	public void loadSendJunLingScript() {
		// 送军令奖励
		List<SendJunLingT> sendJunLingReward = ExcelParser.parse(SendJunLingT.class);
		sendJunLingMap.clear();
		for (SendJunLingT reward : sendJunLingReward) {
			sendJunLingMap.put(reward.id, reward);
		}
		// 军令活动
		List<SendJunLingActivityT> sendJunLingActivityTs = ExcelParser.parse(SendJunLingActivityT.class);
		sendJunLingActivityList.clear();
		if (sendJunLingActivityTs != null) {
			sendJunLingActivityList = sendJunLingActivityTs;
		}
	}

	/**
	 * 加载战力奖励脚本
	 */
	public void loadCombatPowerRewardScript() {
		// 战力嘉奖
		List<PowerRewardT> powerRewards = ExcelParser.parse(PowerRewardT.class);
		powerRewardMap.clear();
		for (PowerRewardT reward : powerRewards) {
			powerRewardMap.put(reward.power, reward);
		}
	}

	/**
	 * 加载秒杀脚本
	 */
	public void loadSeckillScript() {
		secKillConfigTs = ExcelParser.parse(SecKillConfigT.class);
		List<SecKillItemT> killItemTs = ExcelParser.parse(SecKillItemT.class);
		for (SecKillItemT i : killItemTs) {
			for (SecKillConfigT c : secKillConfigTs) {
				if (i.type == c.type) {
					c.secKillItemTs.add(i);
				}
			}
		}
		SimpleDAO simpleDAO = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		seckillItems = simpleDAO.findAll(SeckillItem.class);
	}

	/**
	 * 加载日充值消费脚本
	 */
	public void loadDayChargeConsumeScript() {
		List<DayChargeConsumeT> list = ExcelParser.parse("当日充值好礼", DayChargeConsumeT.class);
		dayCharges.clear();
		for (DayChargeConsumeT c : list) {
			if (c.isValid == 1) {// 有效
				dayCharges.put(c.id, c);
			}
		}
		list = ExcelParser.parse("当日消费送礼", DayChargeConsumeT.class);
		dayConsumes.clear();
		for (DayChargeConsumeT c : list) {
			if (c.isValid == 1) {// 有效
				dayConsumes.put(c.id, c);
			}
		}

		list = ExcelParser.parse("当日充值好礼2", DayChargeConsumeT.class);
		bigDayCharges.clear();
		for (DayChargeConsumeT c : list) {
			if (c.isValid == 1) {// 有效
				bigDayCharges.put(c.id, c);
			}
		}
		list = ExcelParser.parse("当日消费送礼2", DayChargeConsumeT.class);
		bigDayConsumes.clear();
		for (DayChargeConsumeT c : list) {
			if (c.isValid == 1) {// 有效
				bigDayConsumes.put(c.id, c);
			}
		}
	}

	/**
	 * 加载幸运大转盘系统配置
	 */
	public void loadFortuneWheelScript() {
		// 参数配置
		List<FortuneWheelConfigT> configList = ExcelParser.parse(FortuneWheelConfigT.class);
		fortuneWheelConfig = configList.get(0);

		// 奖励配置
		List<FortuneWheelRewardT> rewardList = ExcelParser.parse(FortuneWheelRewardT.class);
		fortuneWheelRewards.clear();
		for (FortuneWheelRewardT reward : rewardList) {
			List<FortuneWheelRewardT> rewards = fortuneWheelRewards.get(reward.type);
			if (rewards == null) {
				rewards = new ArrayList<FortuneWheelRewardT>();
				fortuneWheelRewards.put(reward.type, rewards);
			}
			rewards.add(reward);
		}

		// 类型配置
		List<FortuneWheelRewardTypeT> rewardTypeList = ExcelParser.parse(FortuneWheelRewardTypeT.class);
		for (FortuneWheelRewardTypeT type : rewardTypeList) {
			fortuneWheelRewardType.put(type.type, type);
		}

		// Vip配置
		List<FortuneWheelVipT> vipList = ExcelParser.parse(FortuneWheelVipT.class);
		for (FortuneWheelVipT vip : vipList) {
			fortuneWheelVip.put(vip.vipLv, vip);
		}
	}

	/**
	 * 加载聚宝盆脚本
	 */
	public void loadCornucopiaScript() {
		cornucopiaItemTs = ExcelParser.parse(CornucopiaItemT.class);
		cornucopiaConfT = ExcelParser.parse(CornucopiaConfT.class).get(0);
		cornucopiaConfT.superItems.add(new Property(cornucopiaConfT.superItem1, cornucopiaConfT.superNum1));
		cornucopiaConfT.superItems.add(new Property(cornucopiaConfT.superItem2, cornucopiaConfT.superNum2));
		cornucopiaConfT.superItems.add(new Property(cornucopiaConfT.superItem3, cornucopiaConfT.superNum3));
		cornucopiaConfT.superItems.add(new Property(cornucopiaConfT.superItem4, cornucopiaConfT.superNum4));
	}

	/**
	 * 加载等级福利脚本
	 */
	// public void loadLevelWealScript() {
	// levelWealConfigList = ExcelParser.parse(LevelWealConfigT.class);
	// List<LevelWealLimitConfigT> list =
	// ExcelParser.parse(LevelWealLimitConfigT.class);
	// if (list != null && list.size() > 0) {
	// levelWealLimitConfig = list.get(0);
	// }
	// }

	/**
	 * 加载欧洲杯活动脚本
	 */
	public void loadFootballScript() {
		footballConfT = ExcelParser.parse(FootballConfT.class).get(0);
		footballBuyTs = ExcelParser.parse(FootballBuyT.class);
		footballCountryTs = ExcelParser.parse(FootballCountryT.class);
		footballMatchTs = ExcelParser.parse(FootballMatchT.class);
		footballShopTs = ExcelParser.parse(FootballShopT.class);

		sendFootballAward();
	}

	/**
	 * 发放竞猜奖励
	 */
	private void sendFootballAward() {
		// 处理发奖
		for (FootballMatchT m : footballMatchTs) {
			if (m.isOver == 0) {
				continue;
			}
			List<FootballBet> betList = this.footballBetMap.get(m.id);
			if (betList != null) {
				for (FootballBet b : betList) {
					if (b.isAward()) {
						continue;
					}
					String[] score = m.score.split(":");
					int leftScore = NumberUtil.parseInt(score[0]);
					int rightScore = NumberUtil.parseInt(score[1]);
					boolean win = false;

					Map<String, Integer> rewardMap = new HashMap<String, Integer>();
					Map<String, String> replaceMap = new HashMap<String, String>();
					replaceMap.put("$x", String.valueOf(m.id));

					if (b.getBetCountryId() == DRAW) {
						if (leftScore == rightScore) {
							win = true;
							rewardMap.put(JIANG_BEI_ID, (int) Math.ceil(b.getBetNum() * m.drawOdds));
						}
					} else {
						if (leftScore > rightScore && m.leftCountryId == b.getBetCountryId()) {
							win = true;
							rewardMap.put(JIANG_BEI_ID, (int) Math.ceil(b.getBetNum() * m.leftOdds));
						} else if (leftScore < rightScore && m.rightCountryId == b.getBetCountryId()) {
							win = true;
							rewardMap.put(JIANG_BEI_ID, (int) Math.ceil(b.getBetNum() * m.rightOdds));
						}
					}

					if (!win && m.isRebate == 1 && b.getBetNum() >= m.rebateBetNum) {// 失败返利
						rewardMap.put(JIANG_BEI_ID, b.getBetNum() * m.rebateScale / 100);

						XsgMailManager.getInstance().sendTemplate(b.getRoleId(), MailTemplate.FootballFail, rewardMap,
								replaceMap);
					} else if (win) {
						replaceMap.put("$y", String.valueOf(rewardMap.get(JIANG_BEI_ID)));
						XsgMailManager.getInstance().sendTemplate(b.getRoleId(), MailTemplate.FootballWin, rewardMap,
								replaceMap);
					}

					b.setAward(true);
					saveFootballBet(b);
				}
			}
		}
	}

	/**
	 * 获取等级福利配置
	 */
	public LevelWealConfigT getLevelWealConfig(int levelDiff) {
		if (levelWealConfigList != null) {
			for (LevelWealConfigT config : levelWealConfigList) {
				if (config.minLv <= levelDiff && levelDiff <= config.maxLv) {
					return config;
				}
			}
		}
		return null;
	}

	/**
	 * 获取等级福利等级限制
	 */
	public int getLevelWealLimit() {
		if (levelWealLimitConfig != null) {
			return levelWealLimitConfig.startLv;
		}
		return 0;
	}

	/**
	 * 生成指定个数的邀请码
	 * 
	 * @param num
	 */
	public void generateInviteCode(int num) {
		for (int i = 0; i < num; i++) {
			List<Character> charList = new ArrayList<Character>();
			// 字母和数字各取3个
			for (int j = 0; j < 3; j++) {
				char c = letter.charAt(random.nextInt(letter.length()));
				charList.add(c);
				c = number.charAt(random.nextInt(number.length()));
				charList.add(c);
			}
			Collections.shuffle(charList);
			StringBuilder code = new StringBuilder();
			for (char c : charList) {
				code.append(c);
			}
			if (!this.inviteCodes.containsKey(code.toString())) {
				InviteCode inviteCode = new InviteCode(GlobalDataManager.getInstance().generatePrimaryKey(),
						code.toString(), null);
				this.inviteCodes.put(code.toString(), inviteCode);
			}
		}
	}

	/**
	 * 获取活动列表
	 * 
	 * @return
	 */
	public List<ActivityInfoView> getActivityInfoList() {
		List<ActivityInfoView> list = new ArrayList<ActivityInfoView>();
		for (ActivityT at : activityTs) {
			int type = at.type;
			if (at.type == TIME_LIMIT || at.type == OPEN_AFTER) {
				boolean b = DateUtil.isBetween(new Date(), DateUtil.parseDate(at.startTime),
						DateUtil.parseDate(at.endTime));
				if (!b) {
					type = CLOSEED;
					continue;// 过期的活动不再显示
				}
			}
			// 针对API活动的硬编码 --20151130 zhaoweiyi
			type = at.apiId > 0 ? 4 : type;
			list.add(new ActivityInfoView(at.id, at.name, type, at.icon));
		}
		return list;
	}

	/**
	 * 获取升级奖励内容信息
	 * 
	 * @return
	 */
	public UpActivityInfoView[] getUpActivityInfoList() {
		List<UpActivityInfoView> list = new ArrayList<UpActivityInfoView>();
		for (UpGiftT up : upGiftTList) {
			list.add(new UpActivityInfoView(up.id, up.level, up.yuanbao, up.vip, false));
		}
		return list.toArray(new UpActivityInfoView[0]);
	}

	/**
	 * 获取单个升级礼包
	 * 
	 * @param infoId
	 * @return
	 */
	public UpGiftT getUpGiftTById(int giftId) {
		for (UpGiftT up : upGiftTList) {
			if (up.id == giftId) {
				return up;
			}
		}
		return null;
	}

	/**
	 * 获取升级礼包模版
	 * 
	 * @return
	 */
	public List<UpGiftT> getUpgiftList() {
		return this.upGiftTList;
	}

	/**
	 * 随机获取指定数量题目
	 * 
	 * @param size
	 * @return
	 */
	public List<AnswerT> getSubjectList(int size) {
		List<AnswerT> as = new ArrayList<AnswerT>(size);
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			AnswerT answerT = answerTs.get(random.nextInt(answerTs.size()));
			while (as.contains(answerT)) {
				answerT = answerTs.get(random.nextInt(answerTs.size()));
			}
			as.add(answerT);
		}
		return as;
	}

	public IActivityControler createActivityControler(IRole rt, Role db) {
		return new ActivityControler(rt, db);
	}

	public IMakeVipControler createMakeVipControler(IRole rt, Role db) {
		return new MakeVipControler(rt, db);
	}

	public ISumChargeActivityControler createSumChargeControler(IRole rt, Role db) {
		return new SumChargeControler(rt, db);
	}

	/**
	 * 复制累计充值活动的视图数据
	 * 
	 * @return
	 */
	public SummationActivityView cloneSumChargeView() {
		List<SummationReward> list = new ArrayList<SummationReward>();
		for (SummationActivityComponentT config : this.sumChargeTMap.values()) {
			list.add(new SummationReward(config.thresholdVal, true, XsgRewardManager.getInstance().doTcToItem(null,
					config.tc)));
		}
		Collections.sort(list, new Comparator<SummationReward>() {
			@Override
			public int compare(SummationReward o1, SummationReward o2) {
				return o1.threshold - o2.threshold;
			}
		});

		ActivityT activityT = this.getActivityT(ActivityTemplate.SumCharge);
		long remainSeconds = this.getRemainSecond(activityT);
		SummationActivityView view = new SummationActivityView(0, (int) remainSeconds,
				list.toArray(new SummationReward[0]));
		return view;
	}

	/**
	 * 复制Big累计充值活动的视图数据
	 * 
	 * @return
	 */
	public SummationActivityView cloneBigSumChargeView() {
		List<SummationReward> list = new ArrayList<SummationReward>();
		for (SummationActivityComponentT config : this.bigSumChargeTMap.values()) {
			list.add(new SummationReward(config.thresholdVal, true, XsgRewardManager.getInstance().doTcToItem(null,
					config.tc)));
		}
		Collections.sort(list, new Comparator<SummationReward>() {
			@Override
			public int compare(SummationReward o1, SummationReward o2) {
				return o1.threshold - o2.threshold;
			}
		});

		ActivityT activityT = this.getActivityT(ActivityTemplate.BigSumCharge);
		long remainSeconds = this.getRemainSecond(activityT);
		SummationActivityView view = new SummationActivityView(0, (int) remainSeconds,
				list.toArray(new SummationReward[0]));
		return view;
	}

	/**
	 * 根据活动种类获取活动配置
	 * 
	 * @return
	 */
	public ActivityT getActivityT(ActivityTemplate type) {
		for (ActivityT at : this.activityTs) {
			if (at.id == type.getValue()) {
				return at;
			}
		}
		return null;
	}

	/**
	 * 根据活动ID获取活动配置
	 * 
	 * @return
	 */
	public ActivityT getActivityT(int id) {
		for (ActivityT at : this.activityTs) {
			if (at.id == id) {
				return at;
			}
		}
		return null;
	}

	public ISumConsumeActivityControler createSumConsumeControler(IRole rt, Role db) {
		return new SumConsumeControler(rt, db);
	}

	public IInviteActivityControler createInviteActivityControler(IRole rt, Role db) {
		return new InviteActivityControler(rt, db);
	}

	public IFundControler createFundControler(IRole rt, Role db) {
		return new FundControler(rt, db);
	}

	public ILevelRewardControler createLevelRewardControler(IRole rt, Role db) {
		return new LevelRewardControler(rt, db);
	}

	public IFirstJiaControler createFirstJiaControler(IRole rt, Role db) {
		return new FirstJiaControler(rt, db);
	}

	public IDayLoginControler createDayLoginControler(IRole rt, Role db) {
		return new DayLoginControler(rt, db);
	}

	public IDayforverLoginControler createDayforverLoginControler(IRole rt, Role db) {
		return new DayforverLoginControler(rt, db);
	}

	// public ILevelWealControler createLevelWealControler(IRole rt, Role db) {
	// return new LevelWealControler(rt, db);
	// }

	// public ILevelWealControler createLevelWealControler(IRole rt, Role db) {
	// return new LevelWealControler(rt, db);
	// }

	/**
	 * 复制累计消费活动的视图数据
	 * 
	 * @return
	 */
	public SummationActivityView cloneSumConsumeView() {
		List<SummationReward> list = new ArrayList<SummationReward>();
		for (SummationActivityComponentT config : this.sumConsumeTMap.values()) {
			list.add(new SummationReward(config.thresholdVal, true, XsgRewardManager.getInstance().doTcToItem(null,
					config.tc)));
		}
		Collections.sort(list, new Comparator<SummationReward>() {
			@Override
			public int compare(SummationReward o1, SummationReward o2) {
				return o1.threshold - o2.threshold;
			}
		});

		ActivityT activityT = this.getActivityT(ActivityTemplate.SumConsume);
		long remainSeconds = this.getRemainSecond(activityT);
		SummationActivityView view = new SummationActivityView(0, (int) remainSeconds,
				list.toArray(new SummationReward[0]));
		return view;

	}

	/**
	 * 复制big累计消费活动的视图数据
	 * 
	 * @return
	 */
	public SummationActivityView cloneBigSumConsumeView() {
		List<SummationReward> list = new ArrayList<SummationReward>();
		for (SummationActivityComponentT config : this.bigSumConsumeTMap.values()) {
			list.add(new SummationReward(config.thresholdVal, true, XsgRewardManager.getInstance().doTcToItem(null,
					config.tc)));
		}
		Collections.sort(list, new Comparator<SummationReward>() {
			@Override
			public int compare(SummationReward o1, SummationReward o2) {
				return o1.threshold - o2.threshold;
			}
		});

		ActivityT activityT = this.getActivityT(ActivityTemplate.BigSumConsume);
		long remainSeconds = this.getRemainSecond(activityT);
		SummationActivityView view = new SummationActivityView(0, (int) remainSeconds,
				list.toArray(new SummationReward[0]));
		return view;

	}

	/**
	 * 获取活动结束倒计时秒数，未开启或过期返回-1
	 * 
	 * @param activityT
	 * @return
	 */
	public long getRemainSecond(ActivityT activityT) {
		if (activityT == null) {
			return -1;
		}
		Date begin = DateUtil.parseDate(activityT.startTime);
		Date end = DateUtil.parseDate(activityT.endTime);
		return DateUtil.isBetween(begin, end) ? (end.getTime() - System.currentTimeMillis()) / 1000 : -1;
	}

	/**
	 * 指定活动是否开启
	 * 
	 * @param type
	 * @return
	 */
	public boolean isActivityOpen(ActivityTemplate type) {
		ActivityT template = this.getActivityT(type);
		// 永久有效或者在开放时间内
		return template != null && (template.type == 1 || this.getRemainSecond(template) > -1);
	}

	/**
	 * 获取累计充值配置模板
	 * 
	 * @param threshold
	 * @return
	 */
	public SummationActivityComponentT getSumChargeTemplate(int threshold) {
		return this.sumChargeTMap.get(threshold);
	}

	/**
	 * 获取big累计充值配置模板
	 * 
	 * @param threshold
	 * @return
	 */
	public SummationActivityComponentT getBigSumChargeTemplate(int threshold) {
		return this.bigSumChargeTMap.get(threshold);
	}

	/**
	 * 获取累计消费配置模板
	 * 
	 * @param threshold
	 * @return
	 */
	public SummationActivityComponentT getSumConsumeTemplate(int threshold) {
		return this.sumConsumeTMap.get(threshold);
	}

	/**
	 * 获取Big累计消费配置模板
	 * 
	 * @param threshold
	 * @return
	 */
	public SummationActivityComponentT getBigSumConsumeTemplate(int threshold) {
		return this.bigSumConsumeTMap.get(threshold);
	}

	/**
	 * 获取可用的邀请码
	 * 
	 * @return
	 */
	public InviteCode getAvailableCode() {
		int avalilableCount = getAvalilableCount();
		if (avalilableCount < 100) {
			generateInviteCode(300);
		}
		for (InviteCode i : this.inviteCodes.values()) {
			if (TextUtil.isBlank(i.getUseRoleId())) {
				return i;
			}
		}
		return null;
	}

	/**
	 * 获取邀请码对象
	 * 
	 * @return
	 */
	public InviteCode getInviteCodeByCode(String code) {
		return this.inviteCodes.get(code);
	}

	/**
	 * 获取可用邀请码的数量
	 * 
	 * @return
	 */
	private int getAvalilableCount() {
		int count = 0;
		for (InviteCode i : this.inviteCodes.values()) {
			if (TextUtil.isBlank(i.getUseRoleId())) {
				count++;
			}
		}
		return count;
	}

	public InviteActivityT getInviteActivityT(int num) {
		for (InviteActivityT i : this.inviteActivitys) {
			if (i.num == num) {
				return i;
			}
		}
		return null;
	}

	/**
	 * 获取成长基金基本配置
	 */
	public FundConfigT getFundConfig() {
		return this.fundConfig;
	}

	/**
	 * 获取成长基金奖励列表
	 */
	public List<FundT> getFundRewardList() {
		return new ArrayList<FundT>(fundRewards.values());
	}

	/**
	 * 获取成长基金奖励
	 * 
	 * @param level
	 *            等级
	 */
	public FundT getFundReward(int level) {
		return fundRewards.get(level);
	}

	/**
	 * 获取冲级有奖配置
	 */
	public LevelRewardT getLevelRewardT(int level) {
		return levelRewardMap.get(level);
	}

	/**
	 * 获取第一佳奖励配置
	 */
	public FirstJiaT getFirstJiaRewardT(int level) {
		return firstJiaRewardMap.get(level);
	}

	/**
	 * 获取每日登录奖励配置
	 */
	public DayLoginRewardT getDayLoginRewardT(int day) {
		return dayLoginRewardMap.get(day);
	}

	/**
	 * 获取永久每日登录奖励配置
	 */
	public DayforverLoginRewardT getDayforverLoginRewardT(int day) {
		return dayforverLoginRewardMap.get(day);
	}

	/**
	 * 获取冲级有奖配置列表
	 */
	public List<LevelRewardT> getLevelRewards() {
		return new ArrayList<LevelRewardT>(levelRewardMap.values());
	}

	/**
	 * 获取第一佳奖励列表
	 */
	public List<FirstJiaT> getFirstJiaRewards() {
		return new ArrayList<FirstJiaT>(firstJiaRewardMap.values());
	}

	/**
	 * 获取每日登录奖励列表
	 */
	public List<DayLoginRewardT> getDayLoginRewards() {
		return new ArrayList<DayLoginRewardT>(dayLoginRewardMap.values());
	}

	/**
	 * 获取永久每日登录奖励列表
	 */
	public List<DayforverLoginRewardT> getDayfarvorLoginRewards() {
		return new ArrayList<DayforverLoginRewardT>(dayforverLoginRewardMap.values());
	}

	private List<SendJunLingT> getSendJunLingTWithType(int type) {
		if (sendJunLingMap != null) {
			List<SendJunLingT> list = null;
			for (SendJunLingT sendT : sendJunLingMap.values()) {
				if (type == sendT.type) {
					if (list == null) {
						list = new ArrayList<SendJunLingT>();
					}
					list.add(sendT);
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 获取送军令奖励列表
	 */
	public List<SendJunLingT> getCurrentSendJunLingRewards() {
		int type = 0;
		if (sendJunLingActivityList != null && sendJunLingActivityList.size() > 0) {
			long current = System.currentTimeMillis();
			for (SendJunLingActivityT activity : sendJunLingActivityList) {
				if (activity.startDate.getTime() <= current && current <= activity.endDate.getTime()) {
					type = activity.type;
					break;
				}
			}
		}
		List<SendJunLingT> list = getSendJunLingTWithType(type);
		if (list == null) {
			list = getSendJunLingTWithType(0);
		}
		return list;
	}

	/**
	 * 获取送军令奖励
	 */
	public SendJunLingT getSendJunLingReward(int index) {
		return sendJunLingMap.get(index);
	}

	/**
	 * 获取战力嘉奖配置
	 */
	public PowerRewardT getPowerRewardT(int power) {
		return powerRewardMap.get(power);
	}

	/**
	 * 获取战力嘉奖配置列表
	 */
	public List<PowerRewardT> getPowerRewards() {
		return new ArrayList<PowerRewardT>(powerRewardMap.values());
	}

	/**
	 * 获取所有秒杀活动配置
	 * 
	 * @return
	 */
	public List<SecKillConfigT> getAllSeckillConfig() {
		return this.secKillConfigTs;
	}

	/**
	 * 获取秒杀活动配置
	 * 
	 * @return
	 */
	public SecKillConfigT getSecKillConfigT(int type) {
		for (SecKillConfigT sc : this.secKillConfigTs) {
			if (sc.type == type) {
				return sc;
			}
		}
		return null;
	}

	/**
	 * 获取秒杀物品模版
	 * 
	 * @return
	 */
	public SecKillItemT getSecKillItemT(int id) {
		for (SecKillConfigT sc : this.secKillConfigTs) {
			for (SecKillItemT st : sc.secKillItemTs) {
				if (st.id == id) {
					return st;
				}
			}
		}
		return null;
	}

	/**
	 * 获取秒杀物品购买信息
	 * 
	 * @param seckillId
	 * @return
	 */
	public SeckillItem getSeckillItem(int seckillId, SecKillConfigT secKillConfigT) {
		for (SeckillItem s : this.seckillItems) {
			if (s.getSeckillId() == seckillId
					&& DateUtil.isBetween(s.getCreateDate(), secKillConfigT.startDate, secKillConfigT.endDate)) {
				return s;
			}
		}
		return null;
	}

	public ISeckillControler createSeckillControler(IRole iRole, Role db) {
		return new SeckillControler(iRole, db);
	}

	public IOpenServerActiveControler createOpenServerActiveControler(IRole iRole, Role db) {
		return new OpenServerActiveControler(iRole, db);
	}

	public void addSeckillItem(SeckillItem seckillItem) {
		seckillItems.add(seckillItem);
	}

	public IDayChargeControler createDayChargeControler(IRole iRole, Role db) {
		return new DayChargeControler(iRole, db);
	}

	public IDayConsumeControler createDayConsumeControler(IRole iRole, Role db) {
		return new DayConsumeControler(iRole, db);
	}

	public IPowerRewardControler createPowerRewardControler(IRole role, Role db) {
		return new PowerRewardControler(role, db);
	}

	public IBigDayChargeControler createBigDayChargeControler(IRole iRole, Role db) {
		return new BigDayChargeControler(iRole, db);
	}

	public IBigDayConsumeControler createBigDayConsumeControler(IRole iRole, Role db) {
		return new BigDayConsumeControler(iRole, db);
	}

	public IBigSumChargeActivityControler createBigSumChargeControler(IRole rt, Role db) {
		return new BigSumChargeControler(rt, db);
	}

	public IBigSumConsumeActivityControler createBigSumConsumeControler(IRole rt, Role db) {
		return new BigSumConsumeControler(rt, db);
	}

	public ISendJunLingControler createSendJunLingControler(IRole role, Role db) {
		return new SendJunLingControler(role, db);
	}

	public FortuneWheelConfigT getFortuneWheelConfig() {
		return fortuneWheelConfig;
	}

	public FortuneWheelVipT getFortuneWheelVipConfig(int vipLv) {
		return fortuneWheelVip.get(vipLv);
	}

	public List<FortuneWheelVipT> getFortuneWheelVipConfigs() {
		return new ArrayList<FortuneWheelVipT>(fortuneWheelVip.values());
	}

	public List<FortuneWheelRewardTypeT> getFortuneWheelRewardTypes() {
		return new ArrayList<FortuneWheelRewardTypeT>(fortuneWheelRewardType.values());
	}

	public List<FortuneWheelRewardT> getFortuneWheelRewardsByType(int type) {
		return fortuneWheelRewards.get(type);
	}

	public IFortuneWheelControler createFortuneWheelControler(IRole role, Role db) {
		return new FortuneWheelControler(role, db);
	}

	public ICornucopiaControler createCornucopiaControler(IRole role, Role db) {
		return new CornucopiaControler(role, db);
	}

	/**
	 * 获取聚宝盆物品模版
	 * 
	 * @return
	 */
	public List<CornucopiaItemT> getCornucopiaItemTs() {
		return this.cornucopiaItemTs;
	}

	/**
	 * 获取聚宝盆配置
	 * 
	 * @return
	 */
	public CornucopiaConfT getCornucopiaConfT() {
		return this.cornucopiaConfT;
	}

	/**
	 * 获取全服最高等级
	 * 
	 * @return the maxLevel
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * 设置全服最高等级
	 * 
	 * @param maxLevel
	 *            the maxLevel to set
	 */
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	/**
	 * 获取聚宝盆物品模版
	 * 
	 * @return
	 */
	public CornucopiaItemT getCornucopiaItemT(int id) {
		for (CornucopiaItemT c : this.cornucopiaItemTs) {
			if (c.id == id) {
				return c;
			}
		}
		return null;
	}

	public IExchangeActivityControler createExchangeActivityControler(IRole role, Role db) {
		return new ExchangeActivityControler(role, db);
	}

	/**
	 * 初始化全服最高等级，查询数据库
	 */
	public void initMaxLevelInServer() {
		RoleDAO dao = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
		maxLevel = dao.getMaxLevelInServer();
	}

	public IShootControler createShootControler(IRole rt, Role db) {
		return new ShootControler(rt, db);
	}

	/**
	 * 加载百步穿杨脚本
	 */
	private void loadMarksmanScript() {
		marksManParamTs = ExcelParser.parse(MarksManParamT.class);
		List<ShootParamT> shootParamList = ExcelParser.parse(ShootParamT.class);
		for (ShootParamT shootParamT : shootParamList) {
			shootParamMap.put(shootParamT.type, shootParamT);
		}
		shootRewardTs = ExcelParser.parse("商品列表", ShootRewardT.class);
		for (ShootRewardT shootRewardT : shootRewardTs) {
			shootRewardMap.put(shootRewardT.id, shootRewardT);
		}
		ramdomShootReward(shootRewardTs, ShootSystemType.Shoot);// 生成奖励随机数组

		// 【超级】百步
		shootRewardTsSuper = ExcelParser.parse("超级商品列表", ShootRewardT.class);
		for (ShootRewardT shootRewardT : shootRewardTsSuper) {
			shootRewardMapSuper.put(shootRewardT.id, shootRewardT);
		}
		ramdomShootReward(shootRewardTsSuper, ShootSystemType.ShootSuper);// 生成奖励随机数组

		List<ShootScoreRewardT> ssList = ExcelParser.parse(ShootScoreRewardT.class);
		// 分解奖励列表
		for (ShootScoreRewardT shootScoreReward : ssList) {
			Map<String, Integer> rewardMap = splitReward(shootScoreReward.items);
			shootScoreReward.itemsMap = rewardMap;
			shootScoreRewardTs.put(shootScoreReward.score, shootScoreReward);
		}

		shootScoreRankTs = ExcelParser.parse(ShootScoreRankT.class);
		// 按编号从小到大
		Collections.sort(shootScoreRankTs, new Comparator<ShootScoreRankT>() {
			@Override
			public int compare(ShootScoreRankT t1, ShootScoreRankT t2) {
				if (t1.id < t2.id) {
					return -1;
				} else if (t1.id > t2.id) {
					return 1;
				}
				return 0;
			}
		});
		// 分解奖励列表
		for (ShootScoreRankT shootScoreRank : shootScoreRankTs) {
			Map<String, Integer> rewardMap = splitReward(shootScoreRank.items);
			shootScoreRank.rewardMap = rewardMap;
		}
		shootScoreRankLimitTs = ExcelParser.parse(ShootScoreRankLimitT.class);
		List<ShootSpecialRewardT> shootSpecialRewardTs = ExcelParser.parse(ShootSpecialRewardT.class);
		for (ShootSpecialRewardT shootSpecialRewardT : shootSpecialRewardTs) {
			if (shootSpecialRewardT.Index < 1 || shootSpecialRewardT.Index > shootRewardTs.size()) {
				throw new IllegalArgumentException("Script's index is error!");
			}
			shootSpecialRewardTMap.put(shootSpecialRewardT.Type, shootSpecialRewardT);
		}
		startShootEndTimer();
	}

	/**
	 * 百步穿杨结束公告
	 */
	private void startShootEndTimer() {
		MarksManParamT msk = getMarksManParam();
		if (null == msk || !isInShootActiveTime())
			return;
		Date endTime = DateUtil.parseDate(msk.endTime);// 活动结束时间
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);
		endDate.add(Calendar.HOUR, -6);
		endDate.add(Calendar.MINUTE, -15);
		long delayed = endDate.getTimeInMillis() - System.currentTimeMillis();
		if (delayed < 0)
			return;
		// 检测公会战是否可报名
		LogicThread.scheduleTask(new DelayedTask(delayed, 0) {
			@Override
			public void run() {
				try {
					// 公告
					List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
							XsgChatManager.AdContentType.ShootActive);
					if (adTList != null && adTList.size() > 0) {
						final ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
						if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
							XsgChatManager.getInstance().sendAnnouncement(
									chatAdT.content.replaceAll("~param_1~", String.valueOf(6)));
							// 再公告5次
							final int nextTime = 3600000;
							LogicThread.scheduleTask(new DelayedTask(nextTime) {

								@Override
								public void run() {
									String content = chatAdT.content.replaceAll("~param_1~", String.valueOf(5));
									XsgChatManager.getInstance().sendAnnouncement(content);
								}
							});
							LogicThread.scheduleTask(new DelayedTask(nextTime * 2) {

								@Override
								public void run() {
									String content = chatAdT.content.replaceAll("~param_1~", String.valueOf(4));
									XsgChatManager.getInstance().sendAnnouncement(content);
								}
							});
							LogicThread.scheduleTask(new DelayedTask(nextTime * 3) {

								@Override
								public void run() {
									String content = chatAdT.content.replaceAll("~param_1~", String.valueOf(3));
									XsgChatManager.getInstance().sendAnnouncement(content);
								}
							});
							LogicThread.scheduleTask(new DelayedTask(nextTime * 4) {

								@Override
								public void run() {
									String content = chatAdT.content.replaceAll("~param_1~", String.valueOf(2));
									XsgChatManager.getInstance().sendAnnouncement(content);
								}
							});
							LogicThread.scheduleTask(new DelayedTask(nextTime * 5) {

								@Override
								public void run() {
									String content = chatAdT.content.replaceAll("~param_1~", String.valueOf(1));
									XsgChatManager.getInstance().sendAnnouncement(content);
								}
							});
						}
					}
				} catch (Exception e) {
					LogManager.error(e);
				}
			}
		});
	}

	/**
	 * 生成奖励随机数组
	 * 
	 * @param rewardList
	 *            奖励配置数组
	 */
	private void ramdomShootReward(List<ShootRewardT> rewardList, ShootSystemType shootType) {
		// 生成随机对象列表
		for (ShootRewardT shootRewardT : rewardList) {
			RandomShootReward r = new RandomShootReward(shootRewardT.id, shootRewardT.pro);
			if (shootType == ShootSystemType.ShootSuper) {
				shootRewardRandomListSuper.add(r);
			} else {
				shootRewardRandomList.add(r);
			}
		}
	}

	/**
	 * 分解奖励配置
	 * 
	 * @param reward
	 *            奖励配置
	 * @return 奖励配置哈希表
	 */
	private Map<String, Integer> splitReward(String reward) {
		Map<String, Integer> rewardMap = new HashMap<String, Integer>();
		for (String item : reward.split(",")) {
			String[] itemSub = item.split(":");
			rewardMap.put(itemSub[0], Integer.valueOf(itemSub[1]));
		}
		return rewardMap;
	}

	/**
	 * 加载百步穿杨排行榜
	 */
	public void initMarksmanRank() {
		MarksManParamT paramT = getMarksManParam();
		ShootScoreRankDao shootScoreRankDao = ShootScoreRankDao.getFromApplicationContext(ServerLancher.getAc());
		List<ShootScoreRank> shootScoreResultList = (LinkedList<ShootScoreRank>) shootScoreRankDao.findAll();
		if (paramT == null || !isInShootActiveTime()) {
			// 处理百步穿杨活动结束时 停服维护 未清数据问题
			if (shootScoreResultList != null && shootScoreResultList.size() != 0) {
				clearShootScoreData(shootScoreResultList);
			}
			if (paramT == null)
				return;
			// 活动如果已结束 直接退出
			ActivityT t = getActivityT(SHOOT_ACTIVEID);
			if (new Date().after(DateUtil.parseDate(t.endTime))) {
				return;
			}
		}
		for (ShootScoreRank shootScoreRank : shootScoreResultList) {
			shootScoreMap.put(shootScoreRank.getRoleId(), shootScoreRank);
			if (shootScoreRank.getRank() > 0) {// 未设定排名，不入榜，
				shootScoreList.add(shootScoreRank);
			}
		}
		// 添加到期自动发送奖励任务
		grantScoreRankReward(paramT, shootScoreList);
		// 添加单次射击每日重置任务
		ShootParamT shootParam = getShootParam(ShootType.ShootOne.getValue());
		if (shootParam.freeCyc == ShootCycleType.Yes.getValue()) {
			if (shootScoreList.size() > 0) {
				// 服务器的休服时间当中，积分的每日刷新时间已过，初次启动重置数据
				cycleResetShootOne(shootScoreMap, true);
			}
			// 单射重置任务
			addCycleRefreshTask(shootScoreList);
		}
	}

	/**
	 * 获取积分排行榜
	 * 
	 * @return 积分排行榜列表
	 */
	public LinkedList<ShootScoreRank> getShootScoreList() {
		return shootScoreList;
	}

	/**
	 * 调整积分排行榜排名
	 */
	public void adjustShootScoreRank() {
		// 按照总分数和射击时间排行
		Collections.sort(shootScoreList, new Comparator<ShootScoreRank>() {
			@Override
			public int compare(ShootScoreRank t1, ShootScoreRank t2) {
				if (t1.getTotalScore() > t2.getTotalScore()) {
					return -1;
				} else if (t1.getTotalScore() < t2.getTotalScore()) {
					return 1;
				}
				if (t1.getShootTime() == null || t2.getShootTime() == null) {
					if (t1.getShootTime() == null && t2.getShootTime() != null) {
						return -1;
					}
					if (t1.getShootTime() != null && t2.getShootTime() == null) {
						return 1;
					}
				} else if (t1.getShootTime().before(t2.getShootTime())) {
					return -1;
				} else if (t1.getShootTime().after(t2.getShootTime())) {
					return 1;
				}
				return 0;
			}
		});
	}

	/**
	 * 添加到积分排行榜
	 * 
	 * @param shootScoreRank
	 *            积分对象
	 */
	public void addToShootScoreMap(ShootScoreRank shootScoreRank) {
		shootScoreMap.put(shootScoreRank.getRoleId(), shootScoreRank);
		MarksManParamT currentMarksManParamT = XsgActivityManage.getInstance().getMarksManParam();
		if (shootScoreRank.getTotalScore() >= currentMarksManParamT.Basis) {
			shootScoreList.add(shootScoreRank);
			shootScoreRank.setRank(shootScoreList.size());
		}
	}

	/**
	 * 获取射击抽奖奖励列表
	 * 
	 * @return 抽奖奖励列表
	 */
	public List<ShootRewardT> getShootRewardTsList(int systemType) {
		return systemType == 1 ? shootRewardTs : shootRewardTsSuper;
	}

	public Map<Integer, ShootScoreRewardT> getShootScoreRewardTs() {
		return shootScoreRewardTs;
	}

	public void setShootScoreRewardTs(Map<Integer, ShootScoreRewardT> shootScoreRewardTs) {
		this.shootScoreRewardTs = shootScoreRewardTs;
	}

	/**
	 * 获取积分奖励排名列表
	 * 
	 * @return 积分奖励排名列表
	 */
	public List<ShootScoreRankT> getShootScoreRankTsList() {
		return shootScoreRankTs;
	}

	/**
	 * 获取积分排行榜
	 * 
	 * @param roleId
	 *            角色编号
	 * @return 积分排行榜列表
	 */
	public ShootScoreRank getMyShootScore(String roleId) {
		return shootScoreMap.get(roleId);
	}

	/**
	 * 获取当前时段百步穿杨配置
	 * 
	 * @return 百步穿杨配置
	 */
	public MarksManParamT getMarksManParam() {
		MarksManParamT currentMarksManParamT = null;
		ActivityT t = getActivityT(SHOOT_ACTIVEID);
		if (null == t)
			return null;
		for (MarksManParamT paramT : marksManParamTs) {
			currentMarksManParamT = paramT;
			break;
		}
		return currentMarksManParamT;
	}

	/**
	 * 是否在百步穿杨活动时间内
	 * 
	 * @return
	 */
	public boolean isInShootActiveTime() {
		Date currentDate = Calendar.getInstance().getTime();
		ActivityT t = getActivityT(SHOOT_ACTIVEID);
		if (null == t)
			return false;
		boolean isBetween = DateUtil.isBetween(currentDate, DateUtil.parseDate(t.startTime),
				DateUtil.parseDate(t.endTime));
		if (isBetween) {
			return true;
		}
		return false;
	}

	/**
	 * 根据射击抽奖类型获取射击抽奖配置
	 * 
	 * @param type
	 *            射击抽奖类型
	 * @return 抽奖配置
	 */
	public ShootParamT getShootParam(int type) {
		return shootParamMap.get(type);
	}

	/**
	 * 根据积分获取积分奖励显示道具，如果未领取过，显示第一个达到该积分段而未领取的奖励，如果已经领取，显示下一个积分段未达到的积分奖励
	 * 
	 * @param score
	 *            当前积分
	 * @param scoreRecvDone
	 *            已领积分奖励
	 * @return 领取的奖励道具编号
	 */
	public ShootScoreRewardT getDispScoreReward(int score, int scoreRecvDone) {
		for (int i = 0; i < shootScoreRewardTs.size(); i++) {
			ShootScoreRewardT shootScoreReward = shootScoreRewardTs.get(0);
			if (score >= shootScoreReward.score) {
				// 如果未领取过，显示第一个可达到的奖励物品
				if (scoreRecvDone < shootScoreReward.score) {
					return shootScoreReward;
				}
			} else {
				// 积分未达到，显示该物品
				return shootScoreReward;
			}
		}
		return null;
	}

	/**
	 * 格式化活动日期
	 * 
	 * @param pattern
	 *            日期格式
	 * @param shootDate
	 *            百步穿杨日期
	 * @return 格式化后百步穿杨日期
	 */
	public String formatShootDate(String pattern, String shootDate) {
		Date date = DateUtil.parseDate(shootDate);
		return DateUtil.format(date, pattern);
	}

	/**
	 * 格式化活动结束日期
	 * 
	 * @param pattern
	 *            日期格式
	 * @param shootEndDate
	 *            百步穿杨结束日期
	 * @return 格式化后百步穿杨日期
	 */
	public String formatShootEndDate(String pattern, String shootEndDate) {
		Date date = DateUtil.parseDate(shootEndDate);
		return DateUtil.format(date, pattern);
	}

	/**
	 * 射击奖励随机对象
	 * 
	 * @author zhouming
	 */
	private static class RandomShootReward implements IRandomHitable {
		public int id;
		public int rank;

		public RandomShootReward(int id, int rank) {
			this.id = id;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}
	}

	/**
	 * 生成随机射击奖励配置
	 * 
	 * @return 随机射击奖励配置
	 */
	public ShootRewardT getRandomShootReward(int systemType) {
		RandomRange<RandomShootReward> randomRewardGen = null;
		if (systemType == ShootSystemType.Shoot.getValue()) {
			randomRewardGen = new RandomRange<RandomShootReward>(shootRewardRandomList);
			RandomShootReward randomReward = randomRewardGen.random();
			return shootRewardMap.get(randomReward.id);
		} else {
			randomRewardGen = new RandomRange<RandomShootReward>(shootRewardRandomListSuper);
			RandomShootReward randomReward = randomRewardGen.random();
			return shootRewardMapSuper.get(randomReward.id);
		}

	}

	/**
	 * 异步保存到数据库
	 * 
	 * @param shootScoreRank
	 *            积分排名对象
	 */
	public void save2DbAsync(final ShootScoreRank shootScoreRank) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				ShootScoreRankDao shootScoreRankDao = ShootScoreRankDao.getFromApplicationContext(ServerLancher.getAc());
				shootScoreRankDao.save(shootScoreRank);
			}
		});
	}

	/**
	 * 获取单射的刷新周期时点
	 */
	public long getNextCycleInterval() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cycDate = getDaysCycTime();
		if (currentDate.after(cycDate)) {
			DateUtil.addDays(cycDate, 1);// 已过每日刷新时间，延迟到下一天
			return cycDate.getTimeInMillis() - currentDate.getTimeInMillis();
		}
		return cycDate.getTimeInMillis() - currentDate.getTimeInMillis();
	}

	/**
	 * 获取百步穿杨抽奖刷新周期时间
	 * 
	 * @return 刷新周期时间
	 */
	public Calendar getDaysCycTime() {
		ShootParamT shootParam = getShootParam(ShootType.ShootOne.getValue());
		Calendar cycCal = Calendar.getInstance();
		Calendar cycCalTmp = Calendar.getInstance();
		Date cycTimeTmp = DateUtil.parseDate(DateUtil.HHMM_PATTERN, shootParam.cycTime);
		cycCalTmp.setTime(cycTimeTmp);
		cycCal.set(Calendar.HOUR_OF_DAY, cycCalTmp.get(Calendar.HOUR_OF_DAY));
		cycCal.set(Calendar.MINUTE, cycCalTmp.get(Calendar.MINUTE));
		cycCal.set(Calendar.SECOND, cycCalTmp.get(Calendar.SECOND));
		return cycCal;
	}

	/**
	 * 当前是否有可免费的单射抽取次数
	 * 
	 * @param shootScoreRank
	 *            积分榜对象
	 * @param currentDate
	 *            当前时间
	 * @return 有(true)/无(false)
	 */
	public boolean isShootOneFree(ShootScoreRank shootScoreRank, Date currentDate) {
		if (shootScoreRank == null) {
			return true;
		}
		return getFreeTime(shootScoreRank, currentDate) == 0;
	}

	/**
	 * 获取距离下一次免费抽取的时间间隔
	 * 
	 * @param shootScoreRank
	 *            积分排名对象
	 * @param currentDate
	 *            当前时间
	 * @return 免费抽取的时间间隔
	 */
	public long getFreeTime(ShootScoreRank shootScoreRank, Date currentDate) {
		long intervalTime = 0;
		if (shootScoreRank == null) {
			return intervalTime;
		}
		ShootParamT shootParam = XsgActivityManage.getInstance().getShootParam(
				XsgActivityManage.ShootType.ShootOne.getValue());
		long currentTime = currentDate.getTime();
		if (shootParam.freeCyc == ShootCycleType.No.getValue()) {
			// 无免费次数，有免费CD时间
			if (currentTime > shootScoreRank.getShootOneTime().getTime() + shootParam.freeCd * 1000) {
				intervalTime = 0;// CD时间已过
			} else {
				intervalTime = shootScoreRank.getShootOneTime().getTime() + shootParam.freeCd * 1000 - currentTime;
			}
		} else if (shootParam.freeCyc == ShootCycleType.Yes.getValue()) {
			if (shootScoreRank.getDayFreeCnt() == 0) {
				intervalTime = 0;// 每日免费次数已经重置，第一次为免费
			} else {
				if (shootScoreRank.getDayFreeCnt() < shootParam.freeNum) {
					if (currentTime > shootScoreRank.getShootFreeTime().getTime() + shootParam.freeCd * 1000) {
						intervalTime = 0;// CD时间已过
					} else {
						intervalTime = shootScoreRank.getShootFreeTime().getTime() + shootParam.freeCd * 1000
								- currentTime;
					}
				} else {
					intervalTime = Integer.MAX_VALUE;
				}
			}
		}
		return intervalTime;
	}

	/**
	 * 发放积分排名奖励
	 * 
	 * @param currentMarksManParamT
	 *            百步穿杨当前配置
	 * @param scoreRankList
	 *            积分排名列表
	 */
	public void grantScoreRankReward(MarksManParamT currentMarksManParamT, final List<ShootScoreRank> scoreRankList) {
		Date endDate = DateUtil.parseDate(currentMarksManParamT.endTime);
		long interval = endDate.getTime() - System.currentTimeMillis();
		LogicThread.scheduleTask(new DelayedTask(interval, 0) {
			@Override
			public void run() {
				try {
					final Map<String, Map<String, String>> awardMap = new HashMap<String, Map<String, String>>();
					// 用邮件发送奖励
					for (int i = 0; i < scoreRankList.size(); i++) {
						final ShootScoreRank scoreRank = scoreRankList.get(i);
						ShootScoreRankLimitT shootScoreRankLimitT = getShootScoreRankRewardT(scoreRank);
						if (shootScoreRankLimitT == null) {
							continue;
						}
						if (scoreRank.getIsSendMail() == 1)
							continue;
						Map<String, String> replaceMap = new HashMap<String, String>();
						replaceMap.put("$l", String.valueOf(scoreRank.getTotalScore()));
						final Map<String, Integer> rewardMap = getShootScoreRankRewardMap(shootScoreRankLimitT);
						if (rewardMap == null) {
							continue;
						}
						// 发送邮件
						XsgMailManager.getInstance().sendTemplate(scoreRank.getRoleId(), MailTemplate.MarksManActivity,
								rewardMap, replaceMap);
						scoreRank.setIsSendMail(1);
						save2DbAsync(scoreRank);

						// 记录Log事件
						XsgRoleManager.getInstance().loadRoleByIdAsync(scoreRank.getRoleId(), new Runnable() {
							@Override
							public void run() {
								IRole role = XsgRoleManager.getInstance().findRoleById(scoreRank.getRoleId());
								Map<String, String> awardSub = new HashMap<String, String>();
								awardSub.put("serverId", String.valueOf(role.getServerId()));
								awardSub.put("account", role.getAccount());
								awardSub.put("totalScore", String.valueOf(scoreRank.getTotalScore()));
								awardSub.put("rank", String.valueOf(scoreRank.getRank()));
								awardSub.put("award", rewardMap.toString());

								awardMap.put("roleId", awardSub);
							}
						}, null);
					}
					// 记录Log
					ArenaAwardLog awardlog = new ArenaAwardLog(new Date(), 5, TextUtil.GSON.toJson(awardMap));
					XsgArenaRankManager.getInstance().saveAwardLogAsync(awardlog);

					ActivityT t = XsgActivityManage.getInstance().getActivityT(XsgActivityManage.SHOOT_ACTIVEID);
					Date end = DateUtil.parseDate(t.endTime);// 活动结束时间
					long cleanTime = end.getTime() - System.currentTimeMillis();
					LogicThread.scheduleTask(new DelayedTask(cleanTime, 0) {
						@Override
						public void run() {
							try {
								clearShootScoreData(shootScoreMap.values());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 清空数据库记录
	 * 
	 * @param scoreRankList
	 *            积分排名列表
	 */
	public void clearShootScoreData(final Collection<ShootScoreRank> scoreRankList) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				ShootScoreRankDao shootScoreRankDao = ShootScoreRankDao.getFromApplicationContext(ServerLancher.getAc());
				shootScoreRankDao.clear(scoreRankList);
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						shootScoreList.clear();
						shootScoreMap.clear();
					}
				});
			}
		});
	}

	/**
	 * 根据角色排名获得排名奖励
	 * 
	 * @param shootScoreRankLimitT
	 *            角色排名积分限制配置
	 * @return 排名奖励
	 */
	public Map<String, Integer> getShootScoreRankRewardMap(ShootScoreRankLimitT shootScoreRankLimitT) {
		for (ShootScoreRankT shootScoreRankT : shootScoreRankTs) {
			if (shootScoreRankLimitT.Start >= shootScoreRankT.startRank
					&& shootScoreRankLimitT.Closure <= shootScoreRankT.stopRank) {
				return shootScoreRankT.rewardMap;
			}
		}
		return null;
	}

	/**
	 * 添加周期刷新任务
	 */
	public void addCycleRefreshTask(final List<ShootScoreRank> scoreRankList) {
		long interval = getNextCycleInterval();
		LogicThread.scheduleTask(new DelayedTask(interval, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				try {
					// 重置当日单射次数
					cycleResetShootOne(shootScoreMap, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 百步穿杨刷新周期数据重置
	 * 
	 * @param shootScoreMap
	 *            积分排行榜
	 * @param checkTime
	 *            是否验证时间（服务器启动时需要判断时间，每日执行时不判断时间）
	 */
	private void cycleResetShootOne(Map<String, ShootScoreRank> shootScoreMap, boolean checkTime) {
		Date cycTime = getDaysCycTime().getTime();
		List<ShootScoreRank> list_scoreRank = new ArrayList<ShootScoreRank>();
		// 充值当日单射次数
		for (ShootScoreRank scoreRank : shootScoreMap.values()) {
			if (checkTime) {
				if (scoreRank.getShootOneTime() == null) {
					// 没有单射，十连射的时间也在刷新时间以后，不重置数据
					if (scoreRank.getShootTime() != null && scoreRank.getShootTime().after(cycTime)) {
						continue;
					}
				} else {
					// 上一次单射时间与今天刷新时间间隔的天数
					int betweenDays = DateUtil.computerDaysBy2Date(cycTime.getTime(), scoreRank.getShootOneTime()
							.getTime());
					// 同一天 过了刷新时间 再射击的 不重置
					if (0 == betweenDays && scoreRank.getShootOneTime().after(cycTime)) {
						continue;
					} else {
						// 这里考虑跨天(周期为1天)
						if (1 == betweenDays) {
							if (DateUtil.compareDate(cycTime, scoreRank.getShootOneTime()) < 1)
								continue;
						}
					}
				}
			}
			scoreRank.setDayFreeCnt(0);
			scoreRank.setDayOneCnt(0);
			// scoreRank.setScore(0);
			// scoreRank.setRec("");

			list_scoreRank.add(scoreRank);
			// save2DbAsync(scoreRank);
		}
		// 批量插入
		ShootScoreRankDao shootScoreRankDao = ShootScoreRankDao.getFromApplicationContext(ServerLancher.getAc());
		shootScoreRankDao.saveBatch(list_scoreRank);
	}

	/**
	 * 根据字符类型代码获得货币数字类型
	 * 
	 * @param code
	 *            字符类型代码
	 * @return 货币数字类型
	 */
	public int getCurrencyType(String code) {
		int type = 0;
		if (Const.PropertyName.MONEY.equals(code)) {
			type = 1;// 金币
		} else if (Const.PropertyName.RMBY.equals(code)) {
			type = 2;// 元宝
		} else if (Const.PropertyName.ORDER.equals(code)) {
			type = 3;// 竞技币
		} else if (Const.PropertyName.AUCTION_COIN.equals(code)) {
			type = 4;// 拍卖币
		}
		return type;
	}

	/**
	 * 根据射击类型获得射击特殊奖励配置
	 * 
	 * @param shootType
	 *            射击类型
	 * @return 特殊奖励配置
	 */
	public ShootSpecialRewardT getShootSpecialRewardT(int shootType, int systemType) {
		ShootSpecialRewardT shootSpecialRewardT = null;
		if (systemType == ShootSystemType.Shoot.getValue()) {
			shootSpecialRewardT = shootSpecialRewardTMap.get(shootType);
		} else { // 超级百步
			shootSpecialRewardT = shootSpecialRewardTMap.get(shootType + 2);
		}
		return shootSpecialRewardT;
	}

	/**
	 * 积分能否加入排行榜单
	 * 
	 * @param shootScoreRank
	 *            角色排行对象
	 * @return 是(true)/否(false)
	 */
	public ShootScoreRankLimitT getShootScoreRankRewardT(ShootScoreRank shootScoreRank) {
		ShootScoreRankLimitT prevShootScoreRankLimitT = null;
		for (int i = shootScoreRankLimitTs.size() - 1; i >= 0; i--) {
			ShootScoreRankLimitT shootScoreRankLimitT = shootScoreRankLimitTs.get(i);
			if (shootScoreRank.getTotalScore() >= shootScoreRankLimitT.Integration) {
				if (shootScoreRank.getRank() <= shootScoreRankLimitT.Closure) {
					prevShootScoreRankLimitT = shootScoreRankLimitT;
				}
			} else {
				break;
			}
		}
		return prevShootScoreRankLimitT;
	}

	/**
	 * 根据排名的上下限获取该排名的基础积分
	 * 
	 * @param startRank
	 *            上限排名
	 * @param stopRank
	 *            下限排名
	 * @return 基础积分
	 */
	public int getRankBaseScore(int startRank, int stopRank) {
		for (int i = 0; i < shootScoreRankLimitTs.size(); i++) {
			ShootScoreRankLimitT shootScoreRankLimitT = shootScoreRankLimitTs.get(i);
			if (startRank >= shootScoreRankLimitT.Start && stopRank <= shootScoreRankLimitT.Closure) {
				return shootScoreRankLimitT.Integration;
			}
		}
		return 0;
	}

	/**
	 * 获取中奖记录
	 */
	public List<ShootAwardInfo> getAwardRecords() {
		return list_award_record;
	}

	/**
	 * 百步穿杨新增一条中奖记录
	 * 
	 * @return
	 */
	public void recordAwardInfo(ShootAwardInfo info) {
		if (list_award_record.size() >= award_record_size) {
			list_award_record.removeLast();
		}
		list_award_record.add(0, info);
	}

	public Map<Integer, Map<Integer, AllServerActiveT>> getAllServerActionMap() {
		return allServerActionMap;
	}

	public Map<Integer, TreeMap<Integer, AllServerActiveDetailT>> getAllServerActionDetailMap() {
		return allServerActionDetailMap;
	}

	public Map<Integer, Integer> getAllServerActionId4Type() {
		return AllServerActionId4Type;
	}

	public TreeMap<Integer, AllServerActiveSaleT> getOpenServerSaleMap() {
		return OpenServerSaleMap;
	}

	public ResourceBackControler createResourceBackControler(IRole r, Role db) {
		return new ResourceBackControler(r, db);
	}

	/**
	 * @return the resourceBackTimeTList
	 */
	public List<ResourceBackTimeT> getResourceBackTimeTList() {
		return resourceBackTimeTList;
	}

	/**
	 * @return the resourceBackDetailTList
	 */
	public List<ResourceBackDetailT> getResourceBackDetailTList(int type) {
		return resourceBackDetailTMap.get(type);
	}

	/**
	 * @return the resourceBackDetailTMap
	 */
	public Map<Integer, List<ResourceBackDetailT>> getResourceBackDetailTMap() {
		return resourceBackDetailTMap;
	}

	public ResourceBackConfigT getResourceBackConfigT(int type) {
		return resourceBackConfigMap.get(type);
	}

	public Map<Integer, ResourceBackConfigT> getResourceBackConfigTMap() {
		return resourceBackConfigMap;
	}

	public FootballControler createFootballControler(IRole iRole, Role role) {
		return new FootballControler(iRole, role);
	}

	public List<FootballMatchT> getFootballMatchTList() {
		return this.footballMatchTs;
	}

	public FootballConfT getFootballConfT() {
		return this.footballConfT;
	}

	/**
	 * 获取玩家押注信息
	 * 
	 * @param groupId
	 * @param roleId
	 * @return
	 */
	public FootballBet getFootballBet(int groupId, String roleId) {
		List<FootballBet> list = this.footballBetMap.get(groupId);
		if (list != null) {
			for (FootballBet b : list) {
				if (b.getRoleId().equals(roleId)) {
					return b;
				}
			}
		}
		return null;
	}

	/**
	 * 增加并保存押注信息
	 * 
	 * @param bet
	 */
	public void addFootballBet(final FootballBet bet) {
		List<FootballBet> bets = footballBetMap.get(bet.getGroupId());
		if (bets == null) {
			bets = new ArrayList<FootballBet>();
			footballBetMap.put(bet.getGroupId(), bets);
		}
		bets.add(bet);
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				simpleDao.save(bet);
			}
		});
	}

	public void saveFootballBet(final FootballBet bet) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				simpleDao.attachDirty(bet);
			}
		});
	}

	/**
	 * 根据购买次数获取购买模板
	 * 
	 * @param buyCount
	 * @return
	 */
	public FootballBuyT getFootballBuyT(int buyCount) {
		for (FootballBuyT b : this.footballBuyTs) {
			if (b.num == buyCount) {
				return b;
			}
		}
		return null;
	}
	
	public List<FootballShopT> getFootballShopT() {
		return this.footballShopTs;
	}

	public FootballShopT getFootballShopT(int id) {
		for (FootballShopT s : this.footballShopTs) {
			if (id == s.id) {
				return s;
			}
		}
		return null;
	}

	/**
	 * 获取球队押注排行榜
	 * 
	 * @return
	 */
	public IntIntPair[] getFootballRank() {
		Map<Integer, Integer> betMap = new HashMap<Integer, Integer>();
		for (List<FootballBet> list : this.footballBetMap.values()) {
			for (FootballBet b : list) {
				Integer betNum = betMap.get(b.getBetCountryId());
				if (betNum == null) {
					betNum = 0;
				}
				betMap.put(b.getBetCountryId(), betNum + b.getBetNum());
			}
		}
		List<IntIntPair> ranks = new ArrayList<IntIntPair>();
		for (Entry<Integer, Integer> entry : betMap.entrySet()) {
			if (entry.getKey() == -1) {// 平局
				continue;
			}
			ranks.add(new IntIntPair(entry.getKey(), entry.getValue()));
		}

		Collections.sort(ranks, new Comparator<IntIntPair>() {

			@Override
			public int compare(IntIntPair o1, IntIntPair o2) {
				return o2.second - o1.second;
			}
		});

		int size = Math.min(ranks.size(), footballConfT.rankSize);
		return ranks.subList(0, size).toArray(new IntIntPair[0]);
	}

	/**
	 * 获取比赛模板
	 * 
	 * @param id
	 * @return
	 */
	public FootballMatchT getFootballMatchT(int id) {
		for (FootballMatchT m : this.footballMatchTs) {
			if (id == m.id) {
				return m;
			}
		}
		return null;
	}

	/**
	 * 获取国家配置
	 * 
	 * @param countryId
	 * @return
	 */
	public FootballCountryT getFootballCountryT(int countryId) {
		for (FootballCountryT c : this.footballCountryTs) {
			if (countryId == c.id) {
				return c;
			}
		}
		return null;
	}
}
