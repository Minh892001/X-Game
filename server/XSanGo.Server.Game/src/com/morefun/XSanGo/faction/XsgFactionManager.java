package com.morefun.XSanGo.faction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.expression.Criteria;

import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.FactionListView;
import com.XSanGo.Protocol.FactionShop;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.Faction;
import com.morefun.XSanGo.db.game.FactionDAO;
import com.morefun.XSanGo.db.game.FactionMember;
import com.morefun.XSanGo.db.game.FactionMemberRank;
import com.morefun.XSanGo.db.game.FactionMemberRankDAO;
import com.morefun.XSanGo.db.game.FactionReq;
import com.morefun.XSanGo.db.game.FactionReqDAO;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class XsgFactionManager {

	private static XsgFactionManager instance = new XsgFactionManager();

	private FactionConfigT factionConfig;
	// 公会等级配置
	private Map<Integer, FactionLevelT> factionLevel = new HashMap<Integer, FactionLevelT>();

	private Map<Integer, FactionCopyT> factionCopys = new HashMap<Integer, FactionCopyT>();

	/**
	 * 副本场景数据
	 */
	private Map<Integer, Map<Integer, FactionCopyStageT>> copyStageTs = new HashMap<Integer, Map<Integer, FactionCopyStageT>>();

	/**
	 * 公会战排名奖励
	 */
	private Map<Integer, GvgRankAwardT> rankAwardMap = new HashMap<Integer, GvgRankAwardT>();

	/**
	 * 公会副本配置
	 */
	public FactionCopyConfT copyConfT;
	private int createLevel = 15;
	private Ehcache factionCache;
	private Ehcache applyCache;

	/**
	 * 公会商城物品
	 */
	public Map<Integer, FactionShopT> factionShopT = new HashMap<Integer, FactionShopT>();

	/**
	 * 荣誉加成
	 */
	private Map<Integer, HonorAdditionT> honorAddtions = new HashMap<Integer, HonorAdditionT>();

	/**
	 * 公会个人排行榜 key=roleId
	 */
	private Map<String, FactionMemberRank> memberRanks = new HashMap<String, FactionMemberRank>();

	/**
	 * 公会战胜利记录
	 */
	private List<GvgLog> gvgLogs = new ArrayList<GvgLog>();

	/**
	 * 公会战界面7个图标对应的报名角色ID
	 */
	private Map<Integer, List<String>> applyMembers = new HashMap<Integer, List<String>>();

	/**
	 * 副本排名奖励金币比例
	 */
	private Map<Integer, Integer> copyRankScales = new HashMap<Integer, Integer>();

	/**
	 * 公会战连胜次数
	 */
	private Map<String, Integer> roleWinNum = new HashMap<String, Integer>();

	/**
	 * 公会战累计胜利次数
	 */
	private Map<String, Integer> sumWinNum = new HashMap<String, Integer>();

	/**
	 * 热血公会战胜利次数奖励
	 */
	private WarmGvgAwardT warmGvgAwardT;

	/**
	 * 公会自动转让检测时间 key-公会ID
	 */
	private Map<String, Date> factionCheckDate = new HashMap<String, Date>();

	/**
	 * 公会战会员获得荣誉排行榜
	 */
	private Map<String, List<Property>> gvgHonorRank = new HashMap<String, List<Property>>();

	/**
	 * 公会仓库等级容量配置
	 */
	private Map<Integer, FactionWarehouseT> warehouseTs = new HashMap<Integer, FactionWarehouseT>();

	/**
	 * 公会栈房物品
	 */
	private List<FactionStorehouseT> storehouseTs = new ArrayList<FactionStorehouseT>();

	/**
	 * 微章ID
	 */
	public static String WEI_ZHANG_CODE = "g_badge";

	private List<TechnologyT> technologyTs = new ArrayList<TechnologyT>();

	private TechnologyConfT technologyConfT;

	private Map<Integer, TechnologyIncomeT> incomeMapT = new HashMap<Integer, TechnologyIncomeT>();

	private List<TechnologyHotLevelT> hotLevelTs = new ArrayList<TechnologyHotLevelT>();

	private List<TechnologyLevelT> levelTs = new ArrayList<TechnologyLevelT>();

	// /**
	// * 公会仓库物品
	// */
	// private List<FactionWarehouseItemT> warehouseItemTs = new
	// ArrayList<FactionWarehouseItemT>();

	public static XsgFactionManager getInstance() {
		return instance;
	}

	public enum FactionOrderBy {
		PeopleDesc, LevelDesc, RandomSort
	}

	/**
	 * 公会科技类型
	 * 
	 * @author xiongming.li
	 *
	 */
	public enum TechnologyType {
		HOT(1), WEI_GUO(2), SHU_GUO(3), WU_GUO(4), QUN_XIONG(5), ALL(6);
		public int value;

		private TechnologyType(int value) {
			this.value = value;
		}
	}

	private XsgFactionManager() {
		this.factionCache = XsgCacheManager.getInstance().getCache(Const.Faction.CACHE_NAME_FACTION);
		this.applyCache = XsgCacheManager.getInstance().getCache(Const.FactionReq.CACHE_NAME_REQ);
		this.initCacheData();

		List<FactionLevelT> levels = ExcelParser.parse(FactionLevelT.class);
		for (FactionLevelT f : levels) {
			factionLevel.put(f.level, f);
		}
		copyConfT = ExcelParser.parse(FactionCopyConfT.class).get(0);
		// 处理副本场景
		List<FactionCopyStageT> stageTs = ExcelParser.parse(FactionCopyStageT.class);
		for (FactionCopyStageT st : stageTs) {
			Map<Integer, FactionCopyStageT> stageMap = this.copyStageTs.get(st.copyId);
			if (stageMap == null) {
				stageMap = new HashMap<Integer, FactionCopyStageT>();
				this.copyStageTs.put(st.copyId, stageMap);
			}
			st.parserMonster();
			stageMap.put(st.stageNum, st);
		}

		List<FactionCopyT> copys = ExcelParser.parse(FactionCopyT.class);
		for (FactionCopyT c : copys) {
			factionCopys.put(c.id, c);
		}

		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
		if (hour == 24) {
			hour = 0;
		}
		// 删除过期申请记录、自动分配仓库物品,每个整点执行一次
		LogicThread.scheduleTask(new DelayedTask(DateUtil.betweenTaskHourMillis(hour), 60 * 60 * 1000) {
			@Override
			public void run() {
				try {
					Attribute<Long> index = applyCache.getSearchAttribute(Const.FactionReq.CACHE_NAME_REQ_DATE);
					Calendar now = Calendar.getInstance();
					now.add(Calendar.DATE, -1);// 超过一天就删除
					List<FactionReq> reqs = searchCandidate(index.lt(now.getTimeInMillis()));
					for (FactionReq r : reqs) {
						removeFactionReq(r.getId());
					}
					// autoAllotItem();
				} catch (Exception e) {
					LogManager.error(e);
				}
			}
		});

		loadFactionScript();

		// 荣誉加成
		List<HonorAdditionT> additionTs = ExcelParser.parse(HonorAdditionT.class);
		for (HonorAdditionT h : additionTs) {
			honorAddtions.put(h.num, h);
		}

		SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		List<FactionMemberRank> ranks = simpleDao.findAll(FactionMemberRank.class);
		for (FactionMemberRank fr : ranks) {
			this.memberRanks.put(fr.getRoleId(), fr);
		}
		// initApplyMember();
		// startGvgTimer();

		// 副本排名奖励比例
		List<CopyRankAwardT> copyRankAwardTs = ExcelParser.parse(CopyRankAwardT.class);
		for (CopyRankAwardT c : copyRankAwardTs) {
			copyRankScales.put(c.rank, c.scale);
		}

		List<FactionWarehouseT> list = ExcelParser.parse(FactionWarehouseT.class);
		for (FactionWarehouseT fw : list) {
			warehouseTs.put(fw.level, fw);
		}
		storehouseTs = ExcelParser.parse(FactionStorehouseT.class);

		loadTechnologyScript();

		// 自动删除长时间不登录的玩家
		LogicThread.scheduleTask(new DelayedTask(DateUtil.betweenTaskHourMillis(1), TimeUnit.DAYS.toMillis(1)) {

			@Override
			public void run() {
				autoDeleteMember();
			}

		});

		// 自动清除死公会
		LogicThread.scheduleTask(new DelayedTask(DateUtil.betweenTaskHourMillis(3), TimeUnit.DAYS.toMillis(1)) {

			@Override
			public void run() {
				// 每个月的第一周星期日3点执行
				Calendar now = Calendar.getInstance();
				if (now.get(Calendar.WEEK_OF_MONTH) == 1 && now.get(Calendar.DAY_OF_WEEK) == 1) {
					autoDeleteFaction();
				}
			}

		});
	}

	/**
	 * 加载公会商城脚本
	 */
	public void loadFactionScript() {
		this.factionConfig = ExcelParser.parse(FactionConfigT.class).get(0);
		this.warmGvgAwardT = ExcelParser.parse(WarmGvgAwardT.class).get(0);

		factionShopT.clear();
		rankAwardMap.clear();

		List<FactionShopT> shops = ExcelParser.parse(FactionShopT.class);
		for (FactionShopT s : shops) {
			factionShopT.put(s.id, s);
		}
		// 排名奖励
		List<GvgRankAwardT> rankAwardTs = ExcelParser.parse(GvgRankAwardT.class);
		for (GvgRankAwardT g : rankAwardTs) {
			rankAwardMap.put(g.rank, g);
		}
	}

	/**
	 * 加载公会科技脚本
	 */
	public void loadTechnologyScript() {
		List<TechnologyT> listT = ExcelParser.parse(TechnologyT.class);
		for (TechnologyT t : listT) {
			if (t.isDisplay == 1) {
				technologyTs.add(t);
			}
		}

		technologyConfT = ExcelParser.parse(TechnologyConfT.class).get(0);
		List<TechnologyIncomeT> list = ExcelParser.parse(TechnologyIncomeT.class);
		for (TechnologyIncomeT i : list) {
			incomeMapT.put(i.id, i);
		}
		List<TechnologyHotLevelT> listH = ExcelParser.parse(TechnologyHotLevelT.class);
		for (TechnologyHotLevelT th : listH) {
			if (th.level > 0) {
				hotLevelTs.add(th);
			}
		}

		List<TechnologyLevelT> listLevelT = ExcelParser.parse(TechnologyLevelT.class);
		for (TechnologyLevelT th : listLevelT) {
			if (th.level > 0) {
				levelTs.add(th);
			}
		}
		// warehouseItemTs = ExcelParser.parse(FactionWarehouseItemT.class);
	}

	/**
	 * 启动后初始化图标对应报名人数
	 */
	public void initApplyMember() {
		for (int i = 0; i < 7; i++) {
			applyMembers.put(i, new ArrayList<String>());
		}
		List<IFaction> factions = getFactionRank();
		for (IFaction f : factions) {
			for (FactionMember fm : f.getAllMember()) {
				if (fm.getApplyDate() != null && DateUtil.isSameDay(new Date(), fm.getApplyDate())) {
					applyMembers.get(NumberUtil.random(applyMembers.size())).add(fm.getRoleId());
				}
			}
		}
	}

	/**
	 * 启动公会战定时器
	 */
	private void startGvgTimer() {
		Date date = DateUtil.parseDate("HH:mm:ss", this.factionConfig.beginTime);
		// 公会战开战时间
		Calendar beginDate = Calendar.getInstance();
		beginDate.setTime(date);

		beginDate.add(Calendar.MINUTE, -15);
		long delayed = DateUtil.betweenTaskHourMillis(beginDate.get(Calendar.HOUR_OF_DAY),
				beginDate.get(Calendar.MINUTE));

		final List<String> openWeekDay = TextUtil.stringToList(factionConfig.openWeekDay);
		// 检测公会战是否可报名
		LogicThread.scheduleTask(new DelayedTask(delayed, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				try {
					Calendar now = Calendar.getInstance();
					int weekDay = now.get(Calendar.DAY_OF_WEEK) - 1;
					if (weekDay == 0) {
						weekDay = 7;
					}
					if (!openWeekDay.contains(String.valueOf(weekDay))) {
						return;
					}
					// 公告
					List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
							XsgChatManager.AdContentType.GvgAD);
					if (adTList != null && adTList.size() > 0) {
						final ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
						if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
							XsgChatManager.getInstance().sendAnnouncement(
									chatAdT.content.replaceAll("~param_1~", String.valueOf(15)));
							// 再启动两个一次性的定时器
							LogicThread.scheduleTask(new DelayedTask(300000) {// 5分钟后执行

										@Override
										public void run() {
											String content = chatAdT.content.replaceAll("~param_1~", String.valueOf(10));
											XsgChatManager.getInstance().sendAnnouncement(content);
										}
									});

							LogicThread.scheduleTask(new DelayedTask(600000) {// 10分钟后执行

										@Override
										public void run() {
											String content = chatAdT.content.replaceAll("~param_1~", String.valueOf(5));
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

		beginDate.setTime(date);
		delayed = DateUtil.betweenTaskHourMillis(beginDate.get(Calendar.HOUR_OF_DAY), beginDate.get(Calendar.MINUTE));
		// 检测公会战是否开战
		LogicThread.scheduleTask(new DelayedTask(delayed, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				try {
					Calendar now = Calendar.getInstance();
					int weekDay = now.get(Calendar.DAY_OF_WEEK) - 1;
					if (weekDay == 0) {
						weekDay = 7;
					}
					if (!openWeekDay.contains(String.valueOf(weekDay))) {
						return;
					}
					// 清空个人排行榜
					memberRanks.clear();
					clearFactionHonor();
					initApplyMember();
					gvgLogs.clear();
					roleWinNum.clear();
					sumWinNum.clear();

					DBThreads.execute(new Runnable() {

						@Override
						public void run() {
							FactionMemberRankDAO rankDao = FactionMemberRankDAO.getFromApplicationContext(ServerLancher
									.getAc());
							rankDao.deleteAll();
						}
					});
					// 公告
					List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
							XsgChatManager.AdContentType.gvgOpen);
					if (adTList != null && adTList.size() > 0) {
						ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
						if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
							XsgChatManager.getInstance().sendAnnouncement(chatAdT.content);
						}
					}
				} catch (Exception e) {
					LogManager.error(e);
				}
			}
		});

		// 结束后等待2分钟再刷新礼包
		beginDate.add(Calendar.MINUTE, this.factionConfig.gvgMinute + 2);
		delayed = DateUtil.betweenTaskHourMillis(beginDate.get(Calendar.HOUR_OF_DAY), beginDate.get(Calendar.MINUTE));
		// 检测公会战是否结束
		LogicThread.scheduleTask(new DelayedTask(delayed, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				try {
					Calendar now = Calendar.getInstance();
					int weekDay = now.get(Calendar.DAY_OF_WEEK) - 1;
					if (weekDay == 0) {
						weekDay = 7;
					}
					if (!openWeekDay.contains(String.valueOf(weekDay))) {
						return;
					}
					sendGvgRankItems();
					clearFactionHonor();
					// 清空个人排行榜
					memberRanks.clear();
					// 清空图标人数
					applyMembers.clear();
					gvgLogs.clear();
					roleWinNum.clear();
					sumWinNum.clear();

					for (int i = 0; i < 7; i++) {
						applyMembers.put(i, new ArrayList<String>());
					}
					DBThreads.execute(new Runnable() {

						@Override
						public void run() {
							FactionMemberRankDAO rankDao = FactionMemberRankDAO.getFromApplicationContext(ServerLancher
									.getAc());
							rankDao.deleteAll();
						}
					});

					// 公告
					List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
							XsgChatManager.AdContentType.gvgOver);
					if (adTList != null && adTList.size() > 0) {
						ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
						if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
							XsgChatManager.getInstance().sendAnnouncement(chatAdT.content);
						}
					}
				} catch (Exception e) {
					LogManager.error(e);
				}
			}
		});
	}

	/**
	 * 初始化缓存数据，包括填充帮派和申请入帮数据
	 */
	private void initCacheData() {
		FactionDAO dao = FactionDAO.getFromApplicationContext(ServerLancher.getAc());
		for (Object obj : dao.findAll()) {
			IFaction faction = new XsgFaction((Faction) obj);
			this.factionCache.put(new Element(faction.getId(), faction));
		}
		FactionReqDAO reqDao = FactionReqDAO.getFromApplicationContext(ServerLancher.getAc());
		for (Object obj : reqDao.findAll()) {
			FactionReq req = (FactionReq) obj;
			this.applyCache.put(new Element(req.getId(), req));
		}
	}

	/**
	 * 获取适合当会长的roleId
	 * 
	 * @param faction
	 * @return
	 */
	private String getTargetBossId(IFaction faction) {
		List<FactionMember> listFms = new ArrayList<FactionMember>();
		Set<FactionMember> fms = faction.getAllMember();
		for (FactionMember fm : fms) {
			// 必须2天内登录过
			if (fm.getOfflineTime() != null && DateUtil.compareDate(new Date(), fm.getOfflineTime()) < 2) {
				listFms.add(fm);
			}
		}
		if (listFms.isEmpty()) {
			return null;
		}
		Collections.sort(listFms, new Comparator<FactionMember>() {

			@Override
			public int compare(FactionMember o1, FactionMember o2) {
				int i = o2.getDutyId() - o1.getDutyId();
				if (i == 0) {
					i = o2.getLevel() - o1.getLevel();
				}
				if (i == 0) {
					i = o2.getContribution() - o1.getContribution();
				}
				if (i == 0) {
					i = (int) (o1.getParticipateTime().getTime() - o2.getParticipateTime().getTime());
				}
				return i;
			}
		});
		return listFms.get(0).getRoleId();
	}

	public IFactionControler createFactionControler(IRole rt, Role db) {
		return new FactionControler(rt, db);
	}

	public int getCreateLevel() {
		return createLevel;
	}

	public int getCreateYuanbao() {
		return this.factionConfig.createNum;
	}

	public IFaction findFactionByName(String name) {
		Query query = this.factionCache.createQuery().includeValues();
		Criteria criteria = this.factionCache.getSearchAttribute(Const.Faction.CACHE_INDEX_NAME).eq(name);
		query.addCriteria(criteria);
		List<IFaction> list = XsgCacheManager.parseCacheValue(query.execute().all(), IFaction.class);
		return list.size() > 0 ? list.get(0) : null;
	}

	public Faction createFaction(IRole role, String name, String icon) {
		Date time = Calendar.getInstance().getTime();
		final Faction result = new Faction(GlobalDataManager.getInstance().generateFactionId(), name, icon,
				role.getRoleId(), time);
		result.getMembers().add(
				new FactionMember(GlobalDataManager.getInstance().generatePrimaryKey(), result,
						Const.Faction.DUTY_BOSS, role.getRoleId(), role.getName(), role.getLevel(),
						(Date) time.clone(), role.getLogoutTime()));
		// String remark = TextUtil.format(
		// "【{0}】正式成立！帮主，要将帮会发扬光大还需要帮众们的努力，快去招收帮众吧!", name);
		// result.getHistories().add(new FactionHistory(result, remark, time));
		return result;

	}

	public void loadFaction(Faction db) {
		this.factionCache.put(new Element(db.getId(), new XsgFaction(db)));
	}

	/**
	 * 获取指定玩家的入帮申请
	 * 
	 * @param roleId
	 * @return
	 */
	public List<FactionReq> findCandidate(String roleId) {
		Attribute<String> index = this.applyCache.getSearchAttribute(Const.FactionReq.CACHE_INDEX_ROLEID);
		return this.searchCandidate(index.eq(roleId));

	}

	/**
	 * 获取公会的入会申请
	 * 
	 * @param factionId
	 * @return
	 */
	public List<FactionReq> findFactionReq(String factionId) {
		Attribute<String> index = this.applyCache.getSearchAttribute(Const.FactionReq.CACHE_INDEX_FACTIONID);
		return this.searchCandidate(index.eq(factionId));
	}

	private List<FactionReq> searchCandidate(Criteria criteria) {
		Query query = this.applyCache.createQuery().includeValues().addCriteria(criteria);

		Results results = query.execute();
		List<FactionReq> list = XsgCacheManager.parseCacheValue(results.all(), FactionReq.class);

		return list;
	}

	/**
	 * 移除入帮申请
	 * 
	 * @param id
	 */
	public void removeFactionReq(String id) {
		if (!this.applyCache.getKeysWithExpiryCheck().contains(id)) {
			return;
		}
		final FactionReq req = (FactionReq) this.applyCache.get(id).getObjectValue();
		this.applyCache.remove(id);
		this.deleteReq(req);
	}

	protected void deleteReq(final FactionReq req) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FactionReqDAO dao = FactionReqDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.delete(req);
			}
		});
	}

	/**
	 * 随机取20个公会 自己申请过的在最前面
	 * 
	 * @param roleId
	 * @return
	 */
	public List<FactionListView> random20FacitonForList(String roleId, final int orderBy) {
		List<FactionReq> reqList = findCandidate(roleId);
		List<FactionListView> list = new ArrayList<FactionListView>();
		int max = this.factionCache.getSize();
		int count = Math.min(max, this.factionConfig.factionNum);
		List<Object> hitList = new ArrayList<Object>();
		List<String> reqIds = new ArrayList<String>();
		// 自己已申请公会在最前面
		for (FactionReq req : reqList) {
			hitList.add(req.getFactionId());
			reqIds.add(req.getFactionId());
		}
		for (Object key : this.factionCache.getKeysWithExpiryCheck()) {
			if (!hitList.contains(key)) {
				hitList.add(key);
			}
		}
		for (Object key : hitList) {
			IFaction faction = (IFaction) this.factionCache.get(key).getObjectValue();
			int maxPeople = getFactionLevelT(faction.getLevel()).people;
			list.add(new FactionListView(faction.getId(), faction.getIcon(), faction.getName(), faction.getLevel(),
					faction.getQQ(), faction.getMemberSize(), reqIds.contains(faction.getId()), maxPeople, faction
							.getJoinLevel(), "", 0, faction.getJoinVip(), faction.getManifesto()));
		}

		Collections.shuffle(list);
		Collections.sort(list, new Comparator<FactionListView>() {

			@Override
			public int compare(FactionListView o1, FactionListView o2) {
				int i = 0;
				if (o1.apply && o2.apply) {
					i = 0;
				} else if (o1.apply) {
					i = -1;
				} else if (o2.apply) {
					i = 1;
				}
				if (orderBy == FactionOrderBy.PeopleDesc.ordinal()) {
					if (i == 0) {
						i = o2.memberSize - o1.memberSize;
					}
					if (i == 0) {
						i = o1.id.compareTo(o2.id);
					}
				}

				if (orderBy == FactionOrderBy.LevelDesc.ordinal()) {
					if (i == 0) {
						i = o2.level - o1.level;
					}
					if (i == 0) {
						i = o1.id.compareTo(o2.id);
					}
				}

				return i;
			}
		});
		return list.subList(0, count);
	}

	/**
	 * 根据数据库里面的公会ID查找
	 * 
	 * @param factionId
	 * @return
	 */
	public IFaction findFaction(String factionId) {
		Element item = this.factionCache.get(factionId);
		if (item != null) {
			return (IFaction) item.getObjectValue();
		}
		return null;
	}

	public void addFactionReq(FactionReq factionReq) {
		this.applyCache.put(new Element(factionReq.getId(), factionReq));
		saveApplication(factionReq);
	}

	/**
	 * 向数据库异步插入一条申请数据
	 * 
	 * @param factionReq
	 */
	protected void saveApplication(final FactionReq factionReq) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FactionReqDAO dao = FactionReqDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.save(factionReq);
			}
		});
	}

	public int getLevelUpExp(int level) {
		FactionLevelT levelT = factionLevel.get(level);
		if (levelT == null) {
			return 0;
		}
		return levelT.exp;
	}

	/**
	 * 获取特定公会和玩家的入会申请记录
	 * 
	 * @param factionId
	 * @param applyId
	 * @return
	 */
	public FactionReq findCandidateById(String factionId, String applyId) {
		Element cache = this.applyCache.get(applyId);
		if (cache != null) {
			FactionReq req = (FactionReq) cache.getObjectValue();
			if (req.getFactionId().equals(factionId)) {
				return req;
			}
		}

		return null;
	}

	/**
	 * 保存公会数据
	 * 
	 * @param faction
	 */
	public void saveFaction(Faction faction) {
		FactionDAO dao = FactionDAO.getFromApplicationContext(ServerLancher.getAc());
		Cache errorCache = XsgCacheManager.getInstance().getCache(Const.FACTION_ERROR_DATA_NAME);
		try {
			dao.customMerge(faction);
			errorCache.remove(faction.getId());
		} catch (Exception e) {
			String name = faction.getName();
			LogFactory.getLog(getClass()).error(TextUtil.format("[{0}]保存失败!!!!", name));
			errorCache.put(new Element(faction.getId(), faction));
			LogFactory.getLog(getClass()).info(TextUtil.format("[{0}]备份数据成功。", name));
		}
	}

	/**
	 * 根据显示的数字ID查找公会
	 * 
	 * @param id
	 * @return
	 */
	public IFaction findByShowId(String id) {
		for (Object key : this.factionCache.getKeysWithExpiryCheck()) {
			Element element = this.factionCache.get(key);
			if (element != null) {
				IFaction faction = (IFaction) element.getObjectValue();
				if (faction.getSubId().equals(id)) {
					return faction;
				}
			}
		}
		return null;
	}

	/**
	 * 根据显示ID或者名字查找公会
	 * 
	 * @param showIdOrName
	 * @return
	 */
	public List<IFaction> findByShowIdOrName(String showIdOrName) {
		List<IFaction> list = new ArrayList<IFaction>();

		for (Object key : this.factionCache.getKeysWithExpiryCheck()) {
			Element element = this.factionCache.get(key);
			if (element != null) {
				IFaction faction = (IFaction) element.getObjectValue();
				if (faction.getSubId().equals(showIdOrName)
						|| Pattern.matches(".*" + showIdOrName + ".*", faction.getName())) {
					list.add(faction);
					if (list.size() == 10) {
						return list;
					}
				}
			}
		}
		return list;
	}

	/**
	 * 获取公会配置
	 * 
	 * @return
	 */
	public FactionConfigT getFactionConfigT() {
		return this.factionConfig;
	}

	public FactionLevelT getFactionLevelT(int level) {
		return this.factionLevel.get(level);
	}

	/**
	 * 获取公会最大等级配置
	 * 
	 * @return
	 */
	public int getFactionMaxLevel() {
		return factionLevel.size();
	}

	/**
	 * 统计副本场景数量
	 * 
	 * @param copyId
	 * @return
	 */
	public int getFactionCopyStageCount(int copyId) {
		return factionCopys.get(copyId).stageCount;
	}

	/**
	 * 统计副本场景数量
	 * 
	 * @param copyId
	 * @return
	 */
	public FactionCopyStageT getFactionCopyStageT(int copyId, int stageNum) {
		return this.copyStageTs.get(copyId).get(stageNum);
	}

	/**
	 * 获取副本所有场景集合
	 * 
	 * @param copyId
	 * @return
	 */
	public Map<Integer, FactionCopyStageT> getFactionCopyStageTMap(int copyId) {
		return this.copyStageTs.get(copyId);
	}

	/**
	 * 获取副本TC
	 * 
	 * @param copyId
	 * @return
	 */
	public FactionCopyT getFactionCopyT(int copyId) {
		return factionCopys.get(copyId);
	}

	/**
	 * 根据公会名模糊查找所有公会
	 * 
	 * @param name
	 * @return
	 */
	public List<IFaction> likeFactionByName(String name) {
		Query query = this.factionCache.createQuery().includeValues();
		Criteria criteria = this.factionCache.getSearchAttribute(Const.Faction.CACHE_INDEX_NAME)
				.ilike("*" + name + "*");
		query.addCriteria(criteria);
		List<IFaction> list = XsgCacheManager.parseCacheValue(query.execute().all(), IFaction.class);
		return list;
	}

	/**
	 * 获取商品模版
	 * 
	 * @param id
	 * @return
	 */
	public FactionShopT getFactionShopT(int id) {
		return factionShopT.get(id);
	}

	/**
	 * 获取荣誉加成模版(已处理超过最大次数问题)
	 * 
	 * @param id
	 * @return
	 */
	public HonorAdditionT getHonorAdditionT(int winNum) {
		HonorAdditionT additionT = honorAddtions.get(winNum);
		if (additionT == null) {
			// 查找最大连胜配置
			int num = 2;
			for (HonorAdditionT h : this.honorAddtions.values()) {
				if (h.num > num) {
					num = h.num;
				}
			}
			additionT = honorAddtions.get(num);
		}
		return additionT;
	}

	/**
	 * 获取公会荣誉点排行
	 * 
	 * @return
	 */
	public List<IFaction> getFactionRank() {
		List<IFaction> factions = new ArrayList<IFaction>();
		for (Object key : this.factionCache.getKeysWithExpiryCheck()) {
			IFaction f = (IFaction) this.factionCache.get(key).getObjectValue();
			if (f.getApplyPeople() != 0) {
				factions.add(f);
			}
		}
		// 荣誉点倒序
		Collections.sort(factions, new Comparator<IFaction>() {

			@Override
			public int compare(IFaction o1, IFaction o2) {
				int t = o2.getFactionHonor() - o1.getFactionHonor();
				if (t == 0) {
					t = o1.getId().compareTo(o2.getId());
				}
				return t;
			}
		});
		return factions;
	}

	/**
	 * 获取公会战个人排行榜
	 * 
	 * @return
	 */
	public List<FactionMemberRank> getFactionMemberRankList() {
		List<FactionMemberRank> rankList = new ArrayList<FactionMemberRank>();
		for (FactionMemberRank f : this.memberRanks.values()) {
			rankList.add(f);
		}
		Collections.sort(rankList, new Comparator<FactionMemberRank>() {

			@Override
			public int compare(FactionMemberRank o1, FactionMemberRank o2) {
				int t = o2.getHonor() - o1.getHonor();
				if (t == 0) {
					t = o1.getId().compareTo(o2.getId());
				}
				return t;
			}
		});
		return rankList;
	}

	/**
	 * 获取roleId的个人荣誉排行榜
	 * 
	 * @param roleId
	 * @return
	 */
	public FactionMemberRank getFactionMemberRank(String roleId) {
		return this.memberRanks.get(roleId);
	}

	/**
	 * 添加排行榜
	 * 
	 * @param rank
	 */
	public void putFactionMemberRank(FactionMemberRank rank) {
		this.memberRanks.put(rank.getRoleId(), rank);
	}

	/**
	 * 根据公会排名获取奖励物品
	 * 
	 * @param index
	 * @return
	 */
	public GvgRankAwardT getAwardByRank(int rank) {
		return this.rankAwardMap.get(rank);
	}

	public void addGvgLog(GvgLog gvgLog) {
		this.gvgLogs.add(gvgLog);
	}

	/**
	 * 验证是否存在战斗记录
	 * 
	 * @param winRoleId
	 * @param failRoleId
	 * @return
	 */
	public boolean isExistGvgLog(String winRoleId, String failRoleId) {
		for (GvgLog g : this.gvgLogs) {
			if (g.winRoleId.equals(winRoleId) && g.failRoleId.equals(failRoleId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取所有商品
	 * 
	 * @return
	 */
	public FactionShop[] getShopViews() {
		List<FactionShop> views = new ArrayList<FactionShop>();
		for (FactionShopT s : this.factionShopT.values()) {
			views.add(new FactionShop(s.id, s.itemId, s.name, s.num, s.price, false, s.coinType, s.freeValue));
		}
		Collections.sort(views, new Comparator<FactionShop>() {

			@Override
			public int compare(FactionShop o1, FactionShop o2) {
				return o1.id - o2.id;
			}
		});
		return views.toArray(new FactionShop[0]);
	}

	/**
	 * 发送公会战排名奖励
	 */
	public void sendGvgRankItems() {
		List<IFaction> factionRank = getFactionRank();
		int size = Math.min(factionRank.size(), rankAwardMap.size());
		// 是否在运营活动时间内
		boolean isActivity = DateUtil.isBetween(this.factionConfig.activityStartDate,
				this.factionConfig.activityEndDate);

		MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
				.get(MailTemplate.GVG_AWARD.value());

		MailRewardT activityRewardT = XsgMailManager.getInstance().getMailRewardTList()
				.get(MailTemplate.GVG_ACTIVITY_AWARD.value());
		for (int i = 1; i <= size; i++) {
			GvgRankAwardT awardT = getAwardByRank(i);
			if (awardT == null) {
				break;
			}
			String[] its = awardT.items.split(",");
			Property[] pro = new Property[its.length];
			for (int j = 0; j < pro.length; j++) {
				String[] id_num = its[j].split(":");
				pro[j] = new Property(id_num[0], Integer.parseInt(id_num[1]));
			}
			// 活动奖励
			its = awardT.activityItems.split(",");
			Property[] activityPro = new Property[its.length];
			for (int j = 0; j < activityPro.length; j++) {
				String[] id_num = its[j].split(":");
				activityPro[j] = new Property(id_num[0], Integer.parseInt(id_num[1]));
			}

			IFaction faction = factionRank.get(i - 1);
			Set<FactionMember> allMembers = faction.getAllMember();
			// 邮件发放奖励
			for (FactionMember f : allMembers) {
				// 至少打赢过一场才发奖
				if (!memberRanks.containsKey(f.getRoleId())) {
					continue;
				}
				XsgMailManager.getInstance()
						.sendMail(
								new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "",
										mailRewardT.sendName, f.getRoleId(), mailRewardT.title, mailRewardT.body
												.replace("$x", String.valueOf(i)), XsgMailManager.getInstance()
												.serializeMailAttach(pro), Calendar.getInstance().getTime()));
				if (isActivity) {
					XsgMailManager.getInstance().sendMail(
							new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "",
									activityRewardT.sendName, f.getRoleId(), activityRewardT.title,
									activityRewardT.body.replace("$x", String.valueOf(i)), XsgMailManager.getInstance()
											.serializeMailAttach(activityPro), Calendar.getInstance().getTime()));
				}
			}
		}
	}

	/**
	 * 清空公会战荣誉
	 */
	public void clearFactionHonor() {
		List<IFaction> factionRank = getFactionRank();
		for (IFaction f : factionRank) {
			f.setFactionHonor(0);
		}
	}

	/**
	 * 移除公会个人排行榜
	 * 
	 * @param roleId
	 */
	public void removeMemberRank(String roleId) {
		if (memberRanks.containsKey(roleId)) {
			memberRanks.remove(roleId);
		}
	}

	/**
	 * 随机分配报名者的位置
	 * 
	 * @param roleId
	 */
	public void addApplyMember(String roleId) {
		int index = NumberUtil.random(applyMembers.size());
		applyMembers.get(index).add(roleId);
	}

	/**
	 * 获取图标的报名列表
	 * 
	 * @param index
	 * @return
	 */
	public List<String> getApplyMemberList(int index) {
		return applyMembers.get(index);
	}

	/**
	 * 移除公会战报名者
	 * 
	 * @param roleId
	 */
	public void removeApplyMember(String roleId) {
		for (List<String> list : applyMembers.values()) {
			for (String rid : list) {
				if (rid.equals(roleId)) {
					list.remove(rid);
					break;
				}
			}
		}
	}

	/**
	 * 获取副本排名奖励金币比例
	 * 
	 * @param rank
	 * @return
	 */
	public Integer getCopyRankScale(int rank) {
		return copyRankScales.get(rank);
	}

	/**
	 * 获取角色连胜次数
	 * 
	 * @param roleId
	 * @return
	 */
	public int getWinNum(String roleId) {
		Integer num = this.roleWinNum.get(roleId);
		return num == null ? 0 : num;
	}

	/**
	 * 设置角色连胜次数
	 * 
	 * @param roleId
	 * @param num
	 */
	public void setWinNum(String roleId, int num) {
		this.roleWinNum.put(roleId, num);
	}

	/**
	 * 获取热血公会战累计胜利奖励
	 * 
	 * @return
	 */
	public WarmGvgAwardT getWarmGvgAwardT() {
		return this.warmGvgAwardT;
	}

	/**
	 * 获取角色累计胜利次数
	 * 
	 * @param roleId
	 * @return
	 */
	public int getSumWinNum(String roleId) {
		Integer num = this.sumWinNum.get(roleId);
		return num == null ? 0 : num;
	}

	/**
	 * 设置角色累计胜利次数
	 * 
	 * @param roleId
	 * @param num
	 */
	public void setSumWinNum(String roleId, int num) {
		this.sumWinNum.put(roleId, num);
	}

	/**
	 * 自动转让公会
	 * 
	 * @param faction
	 */
	public void autoTransfer(final IFaction faction) {
		Date checkDate = factionCheckDate.get(faction.getId());
		// 3天执行一次
		if (checkDate != null && DateUtil.compareDate(new Date(), checkDate) < 3) {
			return;
		}
		factionCheckDate.put(faction.getId(), new Date());
		XsgRoleManager.getInstance().loadRoleByIdAsync(faction.getBossId(), new Runnable() {

			@Override
			public void run() {
				final IRole bossRole = XsgRoleManager.getInstance().findRoleById(faction.getBossId());
				if (bossRole != null) {
					int passDay = DateUtil.compareDate(new Date(), bossRole.getLoginTime());
					if (passDay >= 5) {// 会长超过5天没登录了
						List<String> roleIds = new ArrayList<String>();
						for (FactionMember fm : faction.getAllMember()) {
							roleIds.add(fm.getRoleId());
						}
						XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

							@Override
							public void run() {
								for (FactionMember fm : faction.getAllMember()) {
									IRole rt = XsgRoleManager.getInstance().findRoleById(fm.getRoleId());
									if (rt != null) {
										fm.setLevel(rt.getLevel());
										fm.setOfflineTime(rt.getLoginTime());
									}
								}
								String targetId = getTargetBossId(faction);
								if (targetId == null) {
									return;
								}
								try {
									bossRole.getFactionControler().transferFaction(targetId);
								} catch (NoteException e) {
									LogManager.error(e);
								}
							}
						});
					}
				}
			}
		}, new Runnable() {

			@Override
			public void run() {
				LogManager.warn("faction boss load error");
			}
		});
	}

	/**
	 * 获取公会战最大人数荣誉总和
	 * 
	 * @param faction
	 * @return
	 */
	public int getGvgRankHonor(IFaction faction) {
		int factionLevel = faction.getLevel();
		int maxPeople = this.factionLevel.get(factionLevel).people;
		List<Property> list = this.gvgHonorRank.get(faction.getId());
		if (list == null) {
			return 0;
		}
		int length = Math.min(list.size(), maxPeople);
		int sumHonor = 0;
		for (int i = 0; i < length; i++) {
			sumHonor += list.get(i).value;
		}
		return sumHonor;
	}

	/**
	 * 获取公会仓库配置
	 * 
	 * @param level
	 * @return
	 */
	public FactionWarehouseT getFactionWarehouseTByLevel(int level) {
		return warehouseTs.get(level);
	}

	/**
	 * 获取单个公会栈房物品
	 * 
	 * @param itemId
	 * @return
	 */
	public FactionStorehouseT getFactionStorehouseT(String itemId) {
		for (FactionStorehouseT fs : this.storehouseTs) {
			if (fs.itemId.equals(itemId)) {
				return fs;
			}
		}
		return null;
	}

	/**
	 * 获取公会栈房物品
	 * 
	 * @param itemCode
	 * @return
	 */
	public List<FactionStorehouseT> getFactionStorehouseT() {
		return this.storehouseTs;
	}

	/**
	 * 增加公会战荣誉
	 * 
	 * @param factionId
	 * @param roleId
	 * @param honor
	 */
	public void addGvgHonor(String factionId, String roleId, int honor) {
		List<Property> list = this.gvgHonorRank.get(factionId);
		if (list == null) {
			list = new ArrayList<Property>();
			gvgHonorRank.put(factionId, list);
		}
		for (Property p : list) {
			if (p.code.equals(roleId)) {
				p.value += honor;
				return;
			}
		}
		Property property = new Property(roleId, honor);
		list.add(property);

		sortGvgHonor(list);
	}

	public List<TechnologyT> getAllTechnologyT() {
		return this.technologyTs;
	}

	public TechnologyT getTechnologyTById(int id) {
		for (TechnologyT t : technologyTs) {
			if (t.id == id) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 根据ID最后一位获取模板
	 * 
	 * @param sameId
	 * @return
	 */
	public TechnologyT getTechnologyTBySameId(String sameId) {
		for (TechnologyT t : technologyTs) {
			if (t.type != TechnologyType.ALL.value) {
				continue;
			}
			String sid = String.valueOf(t.id).substring(2, 3);
			if (sid.equals(sameId)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 获取热门类等级配置
	 * 
	 * @param level
	 * @return
	 */
	public TechnologyHotLevelT getTechnologyHotLevelT(int level) {
		for (TechnologyHotLevelT l : hotLevelTs) {
			if (l.level == level) {
				return l;
			}
		}
		return null;
	}

	/**
	 * 获取阵营类等级配置
	 * 
	 * @param level
	 * @return
	 */
	public TechnologyLevelT getTechnologyLevelT(int level) {
		for (TechnologyLevelT l : levelTs) {
			if (l.level == level) {
				return l;
			}
		}
		return null;
	}

	/**
	 * 获取公会科技最大等级
	 * 
	 * @return
	 */
	public int getTechnologyMaxLevel(int type) {
		if (type == TechnologyType.HOT.value) {// 热门
			return hotLevelTs.size();
		} else {
			return levelTs.size();
		}
	}

	public TechnologyConfT getTechnologyConfT() {
		return this.technologyConfT;
	}

	// /**
	// * 获取公会仓库物品
	// *
	// * @return
	// */
	// public List<FactionWarehouseItemT> getWarehouseItem() {
	// return this.warehouseItemTs;
	// }

	// /**
	// * 检测仓库物品指定等级是否开放
	// *
	// * @param itemId
	// * @param level
	// * @return
	// */
	// public boolean checkIsOpen(String itemId, int level) {
	// for (FactionWarehouseItemT i : this.warehouseItemTs) {
	// if (i.itemId.equals(itemId) && level >= i.level) {
	// return true;
	// }
	// }
	// return false;
	// }

	/**
	 * 获取科技等级收益
	 * 
	 * @param id
	 * @param level
	 * @return
	 */
	public int getTechnologyValue(int id, int level) {
		if (level <= 0) {
			return 0;
		}
		TechnologyIncomeT incomeT = incomeMapT.get(id);
		if (level <= incomeT.addValues.length) {
			return incomeT.addValues[level - 1].addValue;
		}
		return 0;
	}

	/**
	 * 根据类型获取科技模板
	 * 
	 * @param contry
	 * @return
	 */
	public List<TechnologyT> getTechnologyListByType(TechnologyType type) {
		List<TechnologyT> list = new ArrayList<TechnologyT>();
		for (TechnologyT t : technologyTs) {
			if (t.type == type.value) {
				list.add(t);
			}
		}
		return list;
	}

	private void sortGvgHonor(List<Property> list) {
		Collections.sort(list, new Comparator<Property>() {

			@Override
			public int compare(Property o1, Property o2) {
				return o2.value - o1.value;
			}
		});
	}

	/**
	 * 自动分配物品
	 */
	private void autoAllotItem() {
		// List<String> autoTime =
		// TextUtil.stringToList(factionConfig.autoAllotTime);
		// Calendar now = Calendar.getInstance();
		// int hour = now.get(Calendar.HOUR_OF_DAY);
		// if (autoTime.contains(String.valueOf(hour))) {
		// for (Object key : this.factionCache.getKeysWithExpiryCheck()) {
		// Element element = this.factionCache.get(key);
		// if (element != null) {
		// final IFaction faction = (IFaction) element.getObjectValue();
		// WarehouseItemBean[] items = null;
		// if (TextUtil.isNotBlank(faction.getWarehouseData())) {
		// items = TextUtil.GSON.fromJson(faction.getWarehouseData(),
		// WarehouseItemBean[].class);
		// } else {
		// items = new WarehouseItemBean[0];
		// }
		// for (final WarehouseItemBean i : items) {
		// List<String> queue = TextUtil.stringToList(i.queue);
		// if (queue.isEmpty() || i.itemNum <= 0) {
		// continue;
		// }
		// i.itemNum -= 1;
		// final String sendId = queue.remove(0);
		// i.queue = TextUtil.join(queue, ",");
		// faction.setWarehouseData(TextUtil.GSON.toJson(items));
		//
		// // 发送自动分配邮件
		// Map<String, Integer> rewardMap = new HashMap<String, Integer>();
		// rewardMap.put(i.itemId, 1);
		// XsgMailManager.getInstance().sendTemplate(sendId,
		// MailTempleId.FactionAutoAllot, rewardMap);
		//
		// // 添加自动分配日志
		// XsgRoleManager.getInstance().loadRoleByIdAsync(sendId, new Runnable()
		// {
		//
		// @Override
		// public void run() {
		// IRole r = XsgRoleManager.getInstance().findRoleById(sendId);
		// String itemName =
		// XsgItemManager.getInstance().findAbsItemT(i.itemId).getName();
		// FactionAllotLog log = new FactionAllotLog(DateUtil.format(new Date(),
		// Messages.getString("FactionControler.52")), Messages
		// .getString("ChatControler.7"), 0, r.getName(), r.getVipLevel(),
		// itemName);
		// faction.addAllotLog(log);
		// }
		// }, null);
		// }
		// }
		// }
		// }
	}

	/**
	 * 自动删除成员
	 */
	private void autoDeleteMember() {
		for (Object key : this.factionCache.getKeysWithExpiryCheck()) {
			Element element = this.factionCache.get(key);
			if (element != null) {
				final IFaction faction = (IFaction) element.getObjectValue();
				if (faction.getDeleteDay() <= 0) {
					continue;
				}
				DBThreads.execute(new Runnable() {

					@Override
					public void run() {
						RoleDAO roleDao = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
						final List<String> roleIds = roleDao.findOfflineFactionMember(faction.getId(),
								DateUtil.addDays(new Date(), -faction.getDeleteDay()));
						if (roleIds.isEmpty()) {
							return;
						}
						XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

							@Override
							public void run() {
								for (String id : roleIds) {
									IRole r = XsgRoleManager.getInstance().findRoleById(id);
									if (r != null && !faction.getBossId().equals(id)
											&& faction.getMemberByRoleId(id) != null) {
										try {
											faction.removeMember(faction.getMemberByRoleId(id), r);
											r.getFactionControler().setNoFaction();
											r.saveAsyn();
											faction.addHistory(r,
													Messages.getString("FactionControler.deleteNotLogin"), null);
										} catch (Exception e) {
											LogManager.error(e);
										}
									}
								}
								faction.saveAsyn();
							}
						});
					}
				});
			}
		}
	}

	/**
	 * 自动清理死公会
	 */
	private void autoDeleteFaction() {
		for (Object key : this.factionCache.getKeysWithExpiryCheck()) {
			Element element = this.factionCache.get(key);
			if (element != null) {
				final IFaction faction = (IFaction) element.getObjectValue();
				// 超限人数
				final int returnNum = faction.getAllMember().size() - getFactionLevelT(faction.getLevel()).people;
				if (returnNum < 0) {// 没有超限
					continue;
				}
				DBThreads.execute(new Runnable() {

					@Override
					public void run() {
						RoleDAO roleDao = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
						int count = roleDao.findFactionLoginMember(faction.getId(), DateUtil.addDays(new Date(), -15));
						if (count != 0) {
							return;
						}
						LogicThread.execute(new Runnable() {

							@Override
							public void run() {
								faction.setJoinType(0);// 允许直接加入

								List<String> ids = new ArrayList<String>();
								for (FactionMember m : faction.getAllMember()) {
									ids.add(m.getRoleId());
								}
								ids.remove(faction.getBossId());
								for (int i = 0; i <= returnNum; i++) {
									faction.removeMember(faction.getMemberByRoleId(ids.get(i)), null);
								}
							}
						});
					}
				});
			}
		}
	}
}
