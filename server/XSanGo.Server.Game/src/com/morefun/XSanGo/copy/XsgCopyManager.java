/**
 * 
 */
package com.morefun.XSanGo.copy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.XSanGo.Protocol.CopyDifficulty;
import com.XSanGo.Protocol.DuelTemplateType;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.battle.DuelUnit;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.ServerCopy;
import com.morefun.XSanGo.db.stat.StatDao;
import com.morefun.XSanGo.db.stat.StatValidateInfo;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.PropertyT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * 副本管理类
 * 
 * @author sulingyun
 * 
 */
public class XsgCopyManager {
	private static XsgCopyManager instance = new XsgCopyManager();

	public static XsgCopyManager getInstance() {
		return instance;
	}

	private Map<Integer, SmallCopyT> smallCopyTMap;

	private Map<Integer, BigCopyT> bigCopyTMap;

	private CopyConfigT copyConfig;

	private Map<Integer, CopyAdditionT> copyAdditionTMap;

	private Map<Integer, CopyResetT> copyResetTMap;

	private Map<Integer, Map<Integer, CaptureT>> captureTMap;

	private Map<Integer, MonsterT> monsterTMap;

	private Map<Integer, StoryEventT> eventMap;

	private HudongConfigT hudongConfigT;

	private List<CaptureEmploySpandT> captureEmploySpandTList;

	private Map<Integer, ServerCopyRewardT> serverCopyRewardTMap;

	private Map<Integer, BuyJunLingT> buyJunLingTMap;

	private Map<Integer, CheckCopyT> checkCopyMap;

	private List<WarmupT> warmupTList;

	private List<RandomWarmDialogT> warmupDialogTList;

	private int escapePopCount;

	private Map<Integer, String> escapePopMessage = new HashMap<Integer, String>();

	private List<CopyBuffT> copyBuffT = new ArrayList<CopyBuffT>();

	/**
	 * 首次占领奖励
	 */
	private List<ServerCopyFirstT> copyFirstTs = new ArrayList<ServerCopyFirstT>();

	private XsgCopyManager() {
		this.captureEmploySpandTList = ExcelParser.parse(CaptureEmploySpandT.class);
		this.copyConfig = ExcelParser.parse(CopyConfigT.class).get(0);

		// 章节
		this.bigCopyTMap = new HashMap<Integer, BigCopyT>();
		List<BigCopyT> bigList = ExcelParser.parse(BigCopyT.class);
		CollectionUtil.removeWhere(bigList, new IPredicate<BigCopyT>() {

			@Override
			public boolean check(BigCopyT item) {
				return item.open == 0;
			}
		});

		// 关卡
		List<SmallCopyT> smallList = ExcelParser.parse(SmallCopyT.class);
		for (final BigCopyT bigCopyT : bigList) {
			bigCopyT.initChildren(CollectionUtil.where(smallList, new IPredicate<SmallCopyT>() {
				@Override
				public boolean check(SmallCopyT item) {
					// // 忽视高手和大神难度里面的小关卡
					// boolean ignore = bigCopyT.getDifficulty() !=
					// CopyDifficulty.Junior
					// && item.type == 1;
					return item.parentId == bigCopyT.id;// && !ignore;
				}
			}));

			this.bigCopyTMap.put(bigCopyT.id, bigCopyT);
		}
		this.smallCopyTMap = CollectionUtil.toMap(smallList, "id");

		// 副本扫荡额外奖励
		this.copyAdditionTMap = CollectionUtil.toMap(ExcelParser.parse(CopyAdditionT.class), "level");

		this.copyResetTMap = CollectionUtil.toMap(ExcelParser.parse(CopyResetT.class), "time");

		this.captureTMap = new HashMap<Integer, Map<Integer, CaptureT>>();
		for (CaptureT ct : ExcelParser.parse(CaptureT.class)) {
			if (!this.captureTMap.containsKey(ct.copyId)) {
				this.captureTMap.put(ct.copyId, new HashMap<Integer, CaptureT>());
			}

			this.captureTMap.get(ct.copyId).put(ct.heroId, ct);
		}

		this.eventMap = CollectionUtil.toMap(ExcelParser.parse(StoryEventT.class), "id");
		// 关卡场景
		for (CopySceneT cst : ExcelParser.parse(CopySceneT.class)) {
			if (this.smallCopyTMap.containsKey(cst.copyId)) {
				String[] steps = cst.vsTips.split(",");
				for (String step : steps) {
					if (step.startsWith("X-")) {
						StoryEventT eventT = eventMap.get(Integer.parseInt(step.substring(2)));
						if (eventT.eventType == 2) {
							cst.addDuelEventT(eventT);
						}
					}
				}
				this.smallCopyTMap.get(cst.copyId).addSceneT(cst);
			}
		}

		// 怪物
		this.monsterTMap = CollectionUtil.toMap(ExcelParser.parse(MonsterT.class), "id");
		this.hudongConfigT = ExcelParser.parse(HudongConfigT.class).get(0);

		// 关卡占领奖励
		this.serverCopyRewardTMap = CollectionUtil.toMap(ExcelParser.parse(ServerCopyRewardT.class), "count");
		Date serverCopyRewardTime = XsgGameParamManager.getInstance().getServerCopyRewardTime();
		long wait = DateUtil.betweenTaskHourMillis(serverCopyRewardTime.getHours(), serverCopyRewardTime.getMinutes());
		LogicThread.scheduleTask(new DelayedTask(wait, TimeUnit.DAYS.toMillis(1)) {

			@Override
			public void run() {
				sendServerCopyReward();
			}
		});

		loadBuyJunLingScript();

		// 副本过关战力匹配
		this.checkCopyMap = CollectionUtil.toMap(ExcelParser.parse(CheckCopyT.class), "copyId");

		// 热身赛相关配置
		this.warmupTList = ExcelParser.parse(WarmupT.class);
		this.warmupDialogTList = ExcelParser.parse(RandomWarmDialogT.class);
		List<BulletinTriggerT> bulletinTriggerTList = ExcelParser.parse(BulletinTriggerT.class);
		escapePopCount = bulletinTriggerTList.get(0).dailyTriggerCount;
		for (BulletinTriggerT t : bulletinTriggerTList) {
			escapePopMessage.put(t.noticeCount, t.noticeTitle);
		}

		// 加载副本Buff脚本
		loadCopyBuffScript();

		copyFirstTs = ExcelParser.parse(ServerCopyFirstT.class);
	}

	/**
	 * 加载购买军令配置脚本
	 */
	public void loadBuyJunLingScript() {
		this.buyJunLingTMap = CollectionUtil.toMap(ExcelParser.parse(BuyJunLingT.class), "count");
	}

	/**
	 * 结算关卡占领奖励
	 */
	private void sendServerCopyReward() {
		SortedSet<Property> set = GlobalDataManager.getInstance().getHallOfFameRoleIdListOrderByCount();
		for (Property role : set) {
			ServerCopyRewardT scrt = this.serverCopyRewardTMap.get(role.value);
			if (scrt != null) {
				Map<String, Integer> rewardMap = new HashMap<String, Integer>();
				for (Property item : scrt.parseItems()) {
					rewardMap.put(item.code, item.value);
				}
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("$a", DateUtil.toString(XsgGameParamManager.getInstance().getServerCopyRewardTime()
						.getTime(), "HH:mm"));
				replaceMap.put("$n", String.valueOf(role.value));

				XsgMailManager.getInstance().sendTemplate(role.code, MailTemplate.ServerCopyReward,
						rewardMap, replaceMap);
			} else {
				LogManager.warn(TextUtil.format("占领数量{0}无匹配奖励", role.value));
			}
		}

	}

	/**
	 * 加载副本Buff
	 * */
	private void loadCopyBuffScript() {
		List<CopyBuffT> buffT = ExcelParser.parse(CopyBuffT.class);
		if (buffT != null) {
			// 按优先级排序
			Collections.sort(buffT, new Comparator<CopyBuffT>() {
				@Override
				public int compare(CopyBuffT o1, CopyBuffT o2) {
					return o2.priority - o1.priority;
				}
			});

			copyBuffT.clear();
			copyBuffT = buffT;
		}
	}

	/**
	 * 获取副本Buff
	 * */
	public List<CopyBuffT> getCopyBuff() {
		return copyBuffT;
	}

	public ICopyControler createCopyControler(IRole rt, Role db) {
		return new CopyControler(rt, db);
	}

	public SmallCopyT findSmallCopyT(int templateId) {
		return this.smallCopyTMap.get(templateId);
	}

	/**
	 * 是否可以重置副本挑战次数
	 * 
	 * @param lastTime
	 *            最后一次挑战的时间
	 * @return
	 */
	public boolean canResetChallenge(Date lastTime) {
		String patten = "HH:mm:ss";
		return DateUtil.isPass(DateUtil.toString(this.copyConfig.resetTime1.getTime(), patten), patten, lastTime)
				|| DateUtil.isPass(DateUtil.toString(this.copyConfig.resetTime2.getTime(), patten), patten, lastTime);
	}

	/**
	 * 根据难度获取首关编号
	 * 
	 * @param difficulty
	 * @return
	 */
	public static int getFirstCopyId(CopyDifficulty difficulty) {
		switch (difficulty) {
		case Junior:
			return XsgGameParamManager.getInstance().getFistJuniorCopy();
		case Senior:
			return XsgGameParamManager.getInstance().getFistSeniorCopy();
		case Top:
			return XsgGameParamManager.getInstance().getFistTopCopy();
		default:
			throw new IllegalArgumentException();
		}
	}

	public short getMaxChallengeTime(int copyId) {
		BigCopyT parent = XsgCopyManager.getInstance().findSmallCopyT(copyId).getParent();
		if (parent == null) {
			return 0;
		}

		switch (parent.getDifficulty()) {
		case Junior:
			return (short) this.copyConfig.juniorMaxTime;
		case Senior:
			return (short) this.copyConfig.seniorMaxTime;
		case Top:
			return (short) this.copyConfig.topMaxTime;

		default:
			return 0;
		}
	}

	/**
	 * 获取挑战指定关卡需要消耗的军令个数
	 * 
	 * @param copyId
	 * @return
	 */
	public int getJunLingConsume(int copyId) {
		BigCopyT parent = XsgCopyManager.getInstance().findSmallCopyT(copyId).getParent();
		if (parent == null) {
			return 0;
		}

		switch (parent.getDifficulty()) {
		case Junior:
			return this.copyConfig.juniorJunLingConsume;
		case Senior:
			return this.copyConfig.seniorJunLingConsume;
		case Top:
			return this.copyConfig.topJunLingConsume;

		default:
			return 0;
		}
	}

	public BigCopyT findBigCopyT(int chapterId) {
		return this.bigCopyTMap.get(chapterId);
	}

	/**
	 * 获取副本扫荡时额外的物品奖励
	 * 
	 * @param level
	 *            玩家等级
	 * @return
	 */
	public List<ItemView> getCopyAdditionalItemList(int level) {
		List<ItemView> list = new ArrayList<ItemView>();
		CopyAdditionT template = this.copyAdditionTMap.get(level);
		for (PropertyT pt : template.items) {
			if (pt.isEffective()) {
				list.add(new ItemView("", ItemType.DefaultItemType, pt.code, pt.value, "")); // 道具extendsProperty为空
			}
		}

		return list;
	}

	public String getClearItemTemplate() {
		return "med2";
	}

	/**
	 * 获取重置副本所需元宝
	 * 
	 * @param n
	 *            第n次重置
	 * @return
	 */
	public int getResetPrice(int n) {
		return this.copyResetTMap.get(n).price;
	}

	public CaptureT findCaptureT(int copyId, int catchHeroId) {
		return this.captureTMap.get(copyId).get(catchHeroId);
	}

	public DuelUnit createDuelUnitFromMonster(MonsterT mt) {
		return new DuelUnit(DuelTemplateType.DuelTemplateTypeMonster, mt.id, mt.color, mt.star, mt.quality, (byte) 0,
				mt.name, mt.level, mt.hp, mt.brave, mt.calm, mt.power, mt.intel, mt.dodge, mt.critRate, mt.critResRate,
				mt.damageResRate, XsgHeroManager.getInstance().findDuelSkillT(
						XsgHeroManager.getInstance().findMonsterTemplate(mt.templateId).duelSkill));
	}

	public MonsterT findMonsterT(int monsterId) {
		MonsterT mt = this.monsterTMap.get(monsterId);
		if (mt == null) {
			LogManager.warn("monster " + monsterId + " is not found!");
		}
		return mt;
	}

	public StoryEventT getStoryEventT(int id) {
		return this.eventMap.get(id);
	}

	/**
	 * 获取互动次数配置
	 * 
	 * @return
	 */
	public HudongConfigT getHudongConfigT() {
		return this.hudongConfigT;
	}

	/**
	 * 群战星级计算
	 * 
	 * @param orignalHeroCount
	 *            初始武将数量
	 * @param remainHero
	 *            剩余武将数量
	 * @return
	 */
	public byte calculateStar(byte orignalHeroCount, byte remainHero) {
		if (remainHero < 0 || remainHero > orignalHeroCount) {
			throw new IllegalStateException();
		}

		if (remainHero == 0) {
			return 0;
		}

		byte star = (byte) (Const.MaxStar - (orignalHeroCount - remainHero));
		star = (byte) Math.max(1, star);

		return star;
	}

	/**
	 * 获取武将录用话费
	 * 
	 * @param i
	 *            第几次录用
	 * @return
	 */
	public int getEmployPrice(final int i) {
		CaptureEmploySpandT template = CollectionUtil.first(this.captureEmploySpandTList,
				new IPredicate<CaptureEmploySpandT>() {

					@Override
					public boolean check(CaptureEmploySpandT item) {
						return item.num == i;
					}
				});
		int size = this.captureEmploySpandTList.size();
		if (template == null && size > 0) {
			template = this.captureEmploySpandTList.get(size - 1);
		}

		return template.cost;
	}

	/**
	 * 获取下次关卡重置时间的字符串表示形式
	 * 
	 * @return
	 */
	public String getNextResetTime() {
		String patten = "HH:mm:ss";
		Date topOfToday = DateUtil.getFirstSecondOfToday().getTime();

		String point1 = DateUtil.toString(this.copyConfig.resetTime1.getTime(), patten);
		String point2 = DateUtil.toString(this.copyConfig.resetTime2.getTime(), patten);
		if (DateUtil.isPass(point1, patten, topOfToday)) {
			if (DateUtil.isPass(point2, patten, topOfToday)) {
				return point1;
			} else {
				return point2;
			}
		} else {
			return point1;
		}
	}

	public BuyJunLingT findJunLingT(int num) {
		return this.buyJunLingTMap.get(num);
	}

	/**
	 * 预加载关卡占领者信息
	 **/
	public void preloadChampions() {
		// 查询所有占领者
		List<ServerCopy> copys = GlobalDataManager.getInstance().getAllServerCopy();
		if (copys != null && copys.size() > 0) {
			Set<String> loadIds = new HashSet<String>();
			for (ServerCopy rc : copys) {
				loadIds.add(rc.getChampionId());
			}
			// 加载到内存
			XsgRoleManager roleManager = XsgRoleManager.getInstance();
			if (loadIds.size() > 0) {
				roleManager.loadRoleAsync(new ArrayList<String>(loadIds), null);
			}
		}
	}

	/**
	 * 战力检测
	 * 
	 * @param copyId
	 * @param star
	 * @param battlePower
	 * @param seconds
	 */
	public void checkBattlePower(final String account, final String roleId, int copyId, int star, int battlePower,
			int seconds) {
		CheckCopyT cct = this.checkCopyMap.get(copyId);
		if (cct == null || star < Const.MaxStar) {
			return;
		}

		int distance = cct.minBattlePower - battlePower;
		if (distance > 0) {
			final String reason = TextUtil.format(
					"Copy {0} cost {1} seconds,need min battle-power {2},real value is {3},distance {4}|{5}%.", copyId,
					seconds, cct.minBattlePower, battlePower, distance, distance * 100 / cct.minBattlePower);
			DBThreads.execute(new Runnable() {

				@Override
				public void run() {
					// 类型2硬编码
					StatValidateInfo statInfo = new StatValidateInfo(account, roleId, ServerLancher.getServerId(), 2,
							reason, Calendar.getInstance().getTime());
					StatDao.getFromApplicationContext(ServerLancher.getAc()).save(statInfo);
				}
			});

		}
	}

	/**
	 * 查找等级匹配的热身赛配置
	 * 
	 * @param level
	 * @return
	 */
	public WarmupT findWarmupT(int level) {
		for (WarmupT wt : this.warmupTList) {
			if (level >= wt.minLevel && level <= wt.maxLevel) {
				return wt;
			}
		}
		return null;
	}

	/**
	 * 随机获取一条热身赛对白
	 * 
	 * @return
	 */
	public String randomWarmupDialog() {
		String result = "";
		int size = this.warmupDialogTList.size();
		if (size > 0) {
			result = this.warmupDialogTList.get(NumberUtil.random(size)).msg;
		}

		return result;
	}

	/**
	 * 获取触发确认框累计次数
	 * 
	 * @return
	 */
	public int getEscapePopCount() {
		return escapePopCount;
	}

	/**
	 * 获取次数称号
	 * 
	 * @param count
	 * @return
	 */
	public String getEscapePopMessage(int count) {
		return escapePopMessage.get(count);
	}

	/**
	 * 首次占领奖励
	 * 
	 * @return
	 */
	public ServerCopyFirstT getCopyFirstT() {
		return this.copyFirstTs.get(0);
	}
}
