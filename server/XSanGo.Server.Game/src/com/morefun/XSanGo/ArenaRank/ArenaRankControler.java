/**
 * 
 */
package com.morefun.XSanGo.ArenaRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AMD_ArenaRank_beginChallenge;
import com.XSanGo.Protocol.AMD_ArenaRank_beginRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_challenge;
import com.XSanGo.Protocol.AMD_ArenaRank_endChallenge;
import com.XSanGo.Protocol.AMD_ArenaRank_endRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_getFightMovie;
import com.XSanGo.Protocol.AMD_ArenaRank_revenge;
import com.XSanGo.Protocol.AMD_ArenaRank_robFightReport;
import com.XSanGo.Protocol.AMD_ArenaRank_selHundredRank;
import com.XSanGo.Protocol.AMD_ArenaRank_selectRank;
import com.XSanGo.Protocol.AMD_ArenaRank_selectRivalRank;
import com.XSanGo.Protocol.ArenaMall;
import com.XSanGo.Protocol.ArenaMallSel;
import com.XSanGo.Protocol.ArenaReportView;
import com.XSanGo.Protocol.CrossMovieView;
import com.XSanGo.Protocol.CrossPvpView;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.FightMovieByteView;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.FightResult;
import com.XSanGo.Protocol.FightResultView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NoFactionException;
import com.XSanGo.Protocol.NoGroupException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OwnRank;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.RivalRank;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.MovieThreads;
import com.morefun.XSanGo.activity.IOpenServerActiveControler;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.ArenaRank;
import com.morefun.XSanGo.db.game.ArenaRankFight;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleArenaRank;
import com.morefun.XSanGo.event.protocol.IArenaBuyChallenge;
import com.morefun.XSanGo.event.protocol.IArenaClearCD;
import com.morefun.XSanGo.event.protocol.IArenaFight;
import com.morefun.XSanGo.event.protocol.IArenaFirstWin;
import com.morefun.XSanGo.event.protocol.IArenaMallExchange;
import com.morefun.XSanGo.event.protocol.IArenaMallRefresh;
import com.morefun.XSanGo.event.protocol.IArenaRankChange;
import com.morefun.XSanGo.event.protocol.IArenaRevenge;
import com.morefun.XSanGo.event.protocol.IArenaSaveGuard;
import com.morefun.XSanGo.event.protocol.IArenaSneer;
import com.morefun.XSanGo.event.protocol.IChangeChallengeMoney;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.FindMovieCallback;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.SensitiveWordManager;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author 吕明涛
 * 
 */
@RedPoint
class ArenaRankControler implements IArenaRankControler {

	private static final Log log = LogFactory.getLog(ArenaRankControler.class);

	private IRole roleRt;
	private Role roleDb;

	/** 挑战 */
	private IArenaFight eventFight;
	/** 复仇 */
	private IArenaRevenge eventRevenge;
	/** 购买挑战令 */
	private IArenaBuyChallenge eventBuyChallenge;
	/** 清除CD */
	private IArenaClearCD eventClearCD;
	/** 商城置换 */
	private IArenaMallExchange eventMallExchange;
	/** 商城刷新 */
	private IArenaMallRefresh eventMallRefresh;
	/** 保存防守队伍 */
	private IArenaSaveGuard eventSaveGuard;
	/** 设置嘲讽 */
	private IArenaSneer eventSneer;
	/** 每日首胜 */
	private IArenaFirstWin firstWin;
	/** 竞技币变更 */
	private IChangeChallengeMoney changeMoney;
	/** 竞技场被打 */
	private IArenaRankChange arenaRankChange;

	private int rivalNum = 3;
	/** 挑战对手数量 */
	private int guardNum = 1;
	/** 战报上下文ID */
	private String fightMovieIdContext = null;
	/** 刷新红点标记,防止一次请求里面刷新多次红点 */
	private boolean hasRefreshRedPoint = false;

	private long lastUpdateTime = 0L;
	private static int updateInterval = 10000; // 竞技场刷新间隔10s

	/**
	 * 对手顺序排序器
	 */
	private Comparator<Integer> comparator = new Comparator<Integer>() {
		public int compare(Integer obj1, Integer obj2) {
			// 等级 升序排序
			return obj1.compareTo(obj2);
		}
	};

	/** 嘲讽防守成功数量 */

	// 挑战对手信息
	private Map<Integer, String> rivalRoleIdMap = new TreeMap<Integer, String>(comparator);

	private byte orignalHeroCount; // 部队的武将数量，计算战斗后的星级

	/**
	 * 对手缓存
	 */
	private RivalRank[] rivalArr;

	/**
	 * 最后打开竞技场的时间
	 */
	private Date refreshRivalDate;

	public ArenaRankControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		this.eventFight = this.roleRt.getEventControler().registerEvent(IArenaFight.class);
		this.eventRevenge = this.roleRt.getEventControler().registerEvent(IArenaRevenge.class);
		this.eventBuyChallenge = this.roleRt.getEventControler().registerEvent(IArenaBuyChallenge.class);
		this.eventClearCD = this.roleRt.getEventControler().registerEvent(IArenaClearCD.class);
		this.eventMallExchange = this.roleRt.getEventControler().registerEvent(IArenaMallExchange.class);
		this.eventMallRefresh = this.roleRt.getEventControler().registerEvent(IArenaMallRefresh.class);
		this.eventSaveGuard = this.roleRt.getEventControler().registerEvent(IArenaSaveGuard.class);
		this.eventSneer = this.roleRt.getEventControler().registerEvent(IArenaSneer.class);
		this.firstWin = this.roleRt.getEventControler().registerEvent(IArenaFirstWin.class);
		this.changeMoney = this.roleRt.getEventControler().registerEvent(IChangeChallengeMoney.class);
		this.arenaRankChange = this.roleRt.getEventControler().registerEvent(IArenaRankChange.class);

		// 角色创建，就初始化竞技场数据
		this.initRoleArenaRank();
	}

	public ArenaRankControler() {

	}

	/**
	 * 客户端 是否红点显示
	 */
	@Override
	public MajorUIRedPointNote getRedPointNote() {
		boolean note = false;

		// 只判断显示被动接受挑战的红点
		List<ArenaRankFight> rankFightList = this.roleDb.getArenaRankFightList();
		for (ArenaRankFight rankReport : rankFightList) {
			if (rankReport.getType() == 1 && this.getRoleArenaRank().getShowReportDate() != null
					&& rankReport.getFightTime() != null
					&& this.getRoleArenaRank().getShowReportDate().before(rankReport.getFightTime())) {
				note = true;
				break;
			}
		}

		return note ? new MajorUIRedPointNote(MajorMenu.ArenaRankMenu, false) : null;
	}

	/**
	 * 查询竞技场的用户数据
	 */
	@Override
	public RoleArenaRank getRoleArenaRank() {
		return this.roleDb.getArenaRank();
	}

	/**
	 * 查询竞技场的 全局 数据
	 */
	@Override
	public ArenaRank getArenaRank() {
		return XsgArenaRankManager.getInstance().ArenaRoleIdMap.get(this.roleRt.getRoleId());
	}

	/**
	 * 保存 角色 竞技场 数据
	 * 
	 * @param arenaRank
	 */
	@Override
	public void setArenaRank(ArenaRank arenaRank, IRole role) {
		// 更新 玩家 排行榜 的 缓存中数据
		XsgArenaRankManager.getInstance().setArenaRankLevelMap_Async(arenaRank);
	}

	/**
	 * 改变竞技币
	 * 
	 * @param num
	 *            负数表示减少
	 */
	@Override
	public void mondifyChallengeMoney(int num) {
		NumberUtil.checkRange(num, -10 * Const.Ten_Thousand, Const.Ten_Thousand);
		int before = getRoleArenaRank().getChallengeMoney();
		this.getRoleArenaRank().setChallengeMoney(this.getRoleArenaRank().getChallengeMoney() + num);
		changeMoney.onChange(before, getRoleArenaRank().getChallengeMoney(), num);
	}

	@Override
	public ArenaRank initArenaRank(int rank, boolean isRobot) {
		ArenaRank arenaRank = new ArenaRank();
		arenaRank.setRoleId(this.roleRt.getRoleId());
		arenaRank.setRank(rank);
		arenaRank.setRobot(isRobot);

		this.roleDb.getArenaRank().setMaxRank(rank);

		return arenaRank;
	}

	/**
	 * 创建角色的时候，初始化竞技场数据
	 */
	private void initRoleArenaRank() {

		if (this.roleDb.getArenaRank() == null) {
			RoleArenaRank roleArenaRank = new RoleArenaRank();

			roleArenaRank.setRoleId(this.roleRt.getRoleId());
			roleArenaRank.setGuardId(this.roleRt.getFormationControler().getDefaultFormation() != null ? this.roleRt
					.getFormationControler().getDefaultFormation().getId() : null);
			roleArenaRank.setSneerId(0);
			roleArenaRank.setSneerStr(""); //$NON-NLS-1$
			roleArenaRank.setAttackFightSum(1);
			roleArenaRank.setGuardFightSum(1);
			roleArenaRank.setAttackWinSum(1);
			roleArenaRank.setGuardWinNum(1);
			roleArenaRank.setGuardWinSum(0);
			roleArenaRank.setChallenge(XsgGameParamManager.getInstance().getArenaToken());
			roleArenaRank.setChallengeBuy(0);
			roleArenaRank.setChallengeBuyDate(DateUtil.addSecond(-XsgGameParamManager.getInstance().getArenaHour()));
			roleArenaRank.setFightDate(new Date(0));
			roleArenaRank.setChallengeMoney(0);
			roleArenaRank.setExchangeItemStr(""); //$NON-NLS-1$
			roleArenaRank.setExchangeRefreshDate(new Date());
			roleArenaRank.setExchangeRefreshNum(0);
			roleArenaRank.setClearCdDate(new Date(0));
			roleArenaRank.setClearCdNum(0);
			roleArenaRank.setRole(this.roleDb);
			roleArenaRank.setShowReportDate(new Date());
			this.roleDb.setArenaRank(roleArenaRank);
		}
	}

	@Override
	public ArenaRank initArenaRank(int rank) {
		ArenaRank arenaRank = this.initArenaRank(rank, false);
		this.roleDb.getArenaRank().setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem()));
		return arenaRank;
	}

	/**
	 * 检查是否能开启竞技场
	 * 
	 * @throws NoteException
	 */
	private void checkRank() throws NoteException {
		// 等级不足，无法开启竞技场
		if (this.roleRt.getLevel() < XsgGameParamManager.getInstance().getArenaLevel()) {
			throw new NoteException(Messages.getString("ArenaRankControler.2")); //$NON-NLS-1$
		}
	}

	/**
	 * 重置 竞技场的数据
	 */
	private void resetData() {
		// 重置 挑战令和购买挑战令，
		RoleArenaRank roleArenaRank = this.roleDb.getArenaRank();
		if (DateUtil.checkTime(roleArenaRank.getChallengeBuyDate(),
				DateUtil.joinTime(XsgGameParamManager.getInstance().getArenaInterval()))) {
			roleArenaRank.setChallenge(XsgGameParamManager.getInstance().getArenaToken());
			roleArenaRank.setChallengeBuy(0);
			roleArenaRank.setChallengeBuyDate(DateUtil.addSecond(-XsgGameParamManager.getInstance().getArenaHour()));
		}

		// 重置 清除CD时间
		if (DateUtil.checkTime(roleArenaRank.getClearCdDate(),
				DateUtil.joinTime(XsgGameParamManager.getInstance().getArenaInterval()))) {
			roleArenaRank.setClearCdDate(DateUtil.addSecond(new Date(), -XsgGameParamManager.getInstance()
					.getArenaHour()));
			roleArenaRank.setClearCdNum(0);
		}
	}

	/**
	 * 竞技场排名查询
	 */
	@Override
	public void selectRank(final AMD_ArenaRank_selectRank __cb) throws NoteException {
		// 检查是否能开启竞技场
		this.checkRank();

		// 5分钟后进入自动刷新对手
		if (refreshRivalDate == null || DateUtil.compareTime(new Date(), refreshRivalDate) >= 300000) {
			rivalArr = null;
			refreshRivalDate = new Date();
		}

		// 初始化竞技场排行
		this.initRoleArenaRank();
		ArenaRank arenaRank = this.getArenaRank();
		if (arenaRank == null) {
			arenaRank = this.initArenaRank(this.initRank());
		} else {
			resetData();
		}
		// 保存 用户竞技场 排行榜
		this.setArenaRank(arenaRank, this.roleRt);
		if (rivalArr == null) {
			// 筛选对手排名
			final Map<String, Integer> filterRankMap = this.filterRank(arenaRank.getRank(), this.rivalNum);
			final List<String> resFilterIdList = new ArrayList<String>(filterRankMap.keySet());

			XsgRoleManager.getInstance().loadRoleAsync(resFilterIdList, new Runnable() {
				@Override
				public void run() {
					rivalRoleIdMap.clear();
					Map<Integer, IRole> rivalRoleMap = new TreeMap<Integer, IRole>(comparator);
					for (String findId : resFilterIdList) {
						IRole findRole = XsgRoleManager.getInstance().findRoleById(findId);
						if (findRole != null) {
							int rank = filterRankMap.get(findId);
							rivalRoleIdMap.put(rank, findRole.getRoleId());
							rivalRoleMap.put(rank, findRole);
						}
					}

					__cb.ice_response(LuaSerializer.serialize(setRoleRankView(rivalRoleMap)));
				}
			});
		} else {
			__cb.ice_response(LuaSerializer.serialize(setRoleRankView(null)));
		}
	}

	/**
	 * 刷新对手排行榜
	 */
	@Override
	public void selectRivalRank(final AMD_ArenaRank_selectRivalRank __cb) throws NoteException {
		// //// 查询间隔控制, 用于防止恶意频繁请求此接口 //////
		long current = System.currentTimeMillis();
		if ((current - lastUpdateTime) < updateInterval) {
			throw new NoteException(Messages.getString("ArenaRankControler.TooMore"));
		}
		lastUpdateTime = current;
		// //// 查询间隔控制, 用于防止恶意频繁请求此接口 //////

		// 检查是否能开启竞技场
		this.checkRank();

		ArenaRank arenaRank = this.getArenaRank();
		if (arenaRank == null) {
			throw new NoteException(Messages.getString("ArenaRankControler.3")); //$NON-NLS-1$
		}

		// 筛选对手排名
		final Map<String, Integer> resFilterRankMap = this.filterRank(arenaRank.getRank(), this.rivalNum);
		final List<String> resFilterIdList = new ArrayList<String>(resFilterRankMap.keySet());

		XsgRoleManager.getInstance().loadRoleAsync(resFilterIdList, new Runnable() {
			@Override
			public void run() {
				rivalRoleIdMap.clear();
				Map<Integer, IRole> rivalRoleMap = new TreeMap<Integer, IRole>(comparator);
				for (String findId : resFilterIdList) {
					IRole findRole = XsgRoleManager.getInstance().findRoleById(findId);
					if (findRole != null) {
						int rank = resFilterRankMap.get(findId);
						rivalRoleIdMap.put(rank, findRole.getRoleId());
						rivalRoleMap.put(rank, findRole);
					}
				}

				__cb.ice_response(LuaSerializer.serialize(setRivalRankView(rivalRoleMap)));
			}
		});
	}

	/**
	 * 保存防守队伍
	 */
	@Override
	public void saveGuard(String guardId) throws NoteException {
		ArenaRank arenaRank = this.getArenaRank();
		int heroCount = this.roleRt.getFormationControler().getFormation(guardId).getHeroCountExcludeSupport();

		// 保存的防守部队中，武将数量必须大于0
		if (arenaRank != null && heroCount > 0) {
			this.roleDb.getArenaRank().setGuardId(guardId);
			this.setArenaRank(arenaRank, this.roleRt);
		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.4")); //$NON-NLS-1$
		}

		// 保存防守部队 事件
		eventSaveGuard.onSave(guardId);
	}

	/**
	 * 设置嘲讽模式
	 * 
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public void setSneer(int sneerId, String sneerStr) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		int openLevel = XsgGameParamManager.getInstance().getArenaTauntLevel();
		if (roleRt.getLevel() < openLevel) {
			throw new NoteException(
					TextUtil.format(Messages.getString("ArenaRankControler.TauntLevelLimit"), openLevel));
		}

		ArenaRank arenaRank = this.getArenaRank();
		ArenaSneerT sneerT = XsgArenaRankManager.getInstance().getSneerMap().get(sneerId);
		if (arenaRank == null || sneerT == null) {
			throw new NoteException(Messages.getString("ArenaRankControler.5")); //$NON-NLS-1$
		}

		RoleArenaRank roleArenaRank = this.roleDb.getArenaRank();
		if (roleArenaRank.getSneerId() != 0) {
			throw new NoteException(Messages.getString("ArenaRankControler.6")); //$NON-NLS-1$
		}

		// 元宝变化
		this.roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, sneerT.cost));
		// 设置嘲讽
		roleArenaRank.setSneerId(sneerId);
		roleArenaRank.setSneerStr(SensitiveWordManager.getInstance().shieldSensitiveWord(sneerStr));
		// 重置防守次数
		roleArenaRank.setGuardWinSum(0);

		this.setArenaRank(arenaRank, this.roleRt);

		// 设置嘲讽模式 事件
		eventSneer.onSet(sneerId, sneerStr, sneerT.cost);
	}

	/**
	 * 清除CD时间
	 * 
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public void clearCD() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		ArenaRank arenaRank = this.getArenaRank();
		// 倒计时已经结束, 无需重置
		if (fightRemainTime(arenaRank) <= 0) {
			return;
		}

		// 重置清除CD时间
		RoleArenaRank roleArenaRank = this.roleDb.getArenaRank();
		Integer clearCost = XsgArenaRankManager.getInstance().ArenaClearCDMap.get(roleArenaRank.getClearCdNum() + 1);
		if (clearCost == null) {
			throw new NoteException(Messages.getString("ArenaRankControler.7")); //$NON-NLS-1$
		}

		// 元宝变化
		this.roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, clearCost));
		// CD时间和战斗时间的设置
		roleArenaRank.setClearCdDate(DateUtil.addSecond(new Date(), -XsgGameParamManager.getInstance().getArenaHour()));
		roleArenaRank.setClearCdNum(roleArenaRank.getClearCdNum() + 1);

		this.setArenaRank(arenaRank, this.roleRt);

		// 添加 清除CD 事件
		eventClearCD.onClear(roleArenaRank.getClearCdNum(), clearCost);
	}

	/**
	 * 购买挑战令
	 * 
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public void buyChallenge() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {

		if (this.getArenaRank() == null) {
			throw new NoteException(Messages.getString("ArenaRankControler.8")); //$NON-NLS-1$
		}

		RoleArenaRank roleArenaRank = this.roleDb.getArenaRank();
		ArenaChallengeT challengeT = XsgArenaRankManager.getInstance().getChallengeMap(
				roleArenaRank.getChallengeBuy() + 1);
		if (challengeT != null && challengeT.VipLv <= roleRt.getVipLevel()) {
			if (roleArenaRank.getChallenge() <= 0) {
				// 元宝变化
				this.roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, challengeT.Cost));

				roleArenaRank.setChallenge(challengeT.Num);
				roleArenaRank.setChallengeBuy(roleArenaRank.getChallengeBuy() + 1);
				roleArenaRank.setChallengeBuyDate(new Date());
				roleArenaRank.setClearCdDate(DateUtil.addSecond(new Date(), -XsgGameParamManager.getInstance()
						.getArenaHour()));
				this.setArenaRank(this.getArenaRank(), this.roleRt);
			} else {
				throw new NoteException(Messages.getString("ArenaRankControler.9")); //$NON-NLS-1$
			}

		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.10")); //$NON-NLS-1$
		}

		// 购买
		eventBuyChallenge.onbuy(challengeT.Num, roleArenaRank.getChallengeBuy(), challengeT.Cost);
	}

	/**
	 * 排名榜显示,显示1到100名
	 */
	@Override
	public void selHundredRank(final AMD_ArenaRank_selHundredRank __cb) throws NoteException {
		// 过滤排行榜前100名
		final int reportNum = XsgGameParamManager.getInstance().getArenaReportNum();
		final List<String> roleIdList = new ArrayList<String>(reportNum);
		for (int i = 1; i <= reportNum; i++) {
			ArenaRank ar = XsgArenaRankManager.getInstance().ArenaRankLevelMap.get(i);
			if (ar != null) {
				roleIdList.add(ar.getRoleId());
			} else {
				log.error(TextUtil.format("AreanRank missing rank {0}", i));
			}
		}

		// 查询 详细角色信息
		XsgRoleManager.getInstance().loadRoleAsync(roleIdList, new Runnable() {
			@Override
			public void run() {
				Map<Integer, IRole> reportRoleMap = new TreeMap<Integer, IRole>();
				for (int i = 0; i < roleIdList.size(); i++) {
					IRole findRole = XsgRoleManager.getInstance().findRoleById(roleIdList.get(i));
					if (findRole != null) {
						reportRoleMap.put(i + 1, findRole);
					}
				}

				__cb.ice_response(LuaSerializer.serialize(setRivalRankView(reportRoleMap)));
			}
		});
	}

	/**
	 * 查询当前角色的挑战令
	 * 
	 * @return
	 */
	@Override
	public int getChallenge() {
		return this.roleDb.getArenaRank().getChallenge();
	}

	/**
	 * 查询 对手 阵容ID
	 * 
	 * @return
	 */
	@Override
	public String findFormationId() {
		String resFormationId = ""; //$NON-NLS-1$

		if (this.roleRt.isRobot()) {
			resFormationId = this.roleRt.getFormationControler().getDefaultFormation().getId();
		} else {
			Map<Integer, ArenaRank> ArenaRankLevelMap = XsgArenaRankManager.getInstance().ArenaRankLevelMap;
			for (ArenaRank rank : ArenaRankLevelMap.values()) {
				if (this.roleRt.getRoleId().equals(rank.getRoleId())) {
					resFormationId = this.getFormation().getId();
					break;
				}
			}
		}

		return resFormationId;
	}

	/**
	 * 查询挑战的对手是否存在
	 * 
	 * @param rivalId
	 * @return
	 */
	@Override
	public boolean isRivalExist(String rivalId) throws NoteException {
		if (!this.roleRt.getRoleId().equals(rivalId)) {
			if (rivalRoleIdMap.values().contains(rivalId)) {
				return true;
			}
		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.12")); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * 查询复仇的对手是否存在
	 * 
	 * @param rivalId
	 * @return
	 */
	@Override
	public void beginRevenge(final String rivalId, final AMD_ArenaRank_beginRevenge __cb, String formationId)
			throws NoteException {

		// 保存选择了，战斗部队武将数量
		setHeroCount(formationId);

		if (!this.roleRt.getRoleId().equals(rivalId)) {
			ArenaRank revengeRank = XsgArenaRankManager.getInstance().ArenaRoleIdMap.get(rivalId);
			int roleRank = getArenaRank().getRank();
			int rivalRank = revengeRank.getRank();
			int rankRange[] = filterRankRange(roleRank);
			// 检查复仇上限
			if (rivalRank < rankRange[0]) {
				__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.LevelTooHigh")));
				return;
			}
			if (this.getArenaRank().getRank() > revengeRank.getRank()) {

				XsgRoleManager.getInstance().loadRoleByIdAsync(rivalId, new Runnable() {
					@Override
					public void run() {
						IRole revengeRole = XsgRoleManager.getInstance().findRoleById(rivalId);
						// 开启了嘲讽模式,检查等级限制
						if (revengeRole.getArenaRankControler().getRoleArenaRank().getSneerId() != 0) {
							int limitLevel = XsgGameParamManager.getInstance().getArenaTauntLevel();
							if (roleRt.getLevel() < limitLevel) {
								__cb.ice_exception(new NoteException(TextUtil.format(
										Messages.getString("ArenaRankControler.TauntLevelLimit2"), limitLevel)));
								return;
							}
						}
						String targetFormationId = revengeRole.getArenaRankControler().findFormationId();
						if (!targetFormationId.equals("")) { //$NON-NLS-1$
							// 扣除挑战次数
							// revengeRole
							// .getArenaRankControler()
							// .getRoleArenaRank()
							// .setChallenge(
							// revengeRole
							// .getArenaRankControler()
							// .getRoleArenaRank()
							// .getChallenge()
							// - XsgGameParamManager
							// .getInstance()
							// .getArenaConsume());
							// 减少挑战令
							RoleArenaRank roleArenaRank = getRoleArenaRank();
							int arenaConsume = XsgGameParamManager.getInstance().getArenaConsume();
							if (roleArenaRank == null || roleArenaRank.getChallenge() < arenaConsume) {
								__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.14"))); //$NON-NLS-1$
								return;
							}
							getRoleArenaRank().setChallenge(
									getRoleArenaRank().getChallenge()
											- XsgGameParamManager.getInstance().getArenaConsume());

							PvpOpponentFormationView view = revengeRole.getFormationControler()
									.getPvpOpponentFormationView(targetFormationId);
							// 保存战报上下文
							fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(
									XsgFightMovieManager.Type.ArenaRank, roleRt, revengeRole);

							__cb.ice_response(view);
						} else {
							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.15"))); //$NON-NLS-1$
						}
					}
				}, new Runnable() {
					@Override
					public void run() {
						__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.16"))); //$NON-NLS-1$
						return;
					}
				});

			} else {
				throw new NoteException(Messages.getString("ArenaRankControler.17")); //$NON-NLS-1$
			}
		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.18")); //$NON-NLS-1$
		}
	}

	/**
	 * 嘲讽的 结果 参数
	 * 
	 * @return 嘲讽ID，嘲讽的花费，挑战 嘲讽奖励， 防守嘲讽奖励
	 */
	@Override
	public int[] getSneerResult() {
		int[] snerrRes = new int[3];
		ArenaRank roleRank = this.roleRt.getArenaRankControler().getArenaRank();
		if (roleRank != null) {
			ArenaSneerT sneerT = XsgArenaRankManager.getInstance().getSneerMap()
					.get(this.roleDb.getArenaRank().getSneerId());
			if (sneerT != null) {
				snerrRes[0] = sneerT.id;
				snerrRes[1] = sneerT.cost;
				snerrRes[2] = sneerT.gain;
			}
		}

		return snerrRes;
	}

	// 保存选择了，战斗部队武将数量
	private void setHeroCount(String formationId) {
		this.orignalHeroCount = this.roleRt.getFormationControler().getFormation(formationId)
				.getHeroCountIncludeSupport();
	}

	/**
	 * 挑战 开始
	 * 
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public void beginFight(final AMD_ArenaRank_beginChallenge __cb, String targetId, String formationId)
			throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {

		// 保存选择了，战斗部队武将数量
		setHeroCount(formationId);

		IArenaRankControler rankControler = this.roleRt.getArenaRankControler();
		ArenaRank roleRank = rankControler.getArenaRank();

		if (this.getRoleArenaRank().getChallenge() <= 0) {
			throw new NoteException(Messages.getString("ArenaRankControler.19")); //$NON-NLS-1$
		}
		if (rankControler.fightRemainTime(roleRank) > 0) {
			throw new NoteException(Messages.getString("ArenaRankControler.20")); //$NON-NLS-1$
		}

		final String theTargetId = targetId;
		if (isRivalExist(theTargetId)) {
			XsgRoleManager.getInstance().loadRoleByIdAsync(theTargetId, new Runnable() {
				@Override
				public void run() {
					try {
						IRole target = XsgRoleManager.getInstance().findRoleById(theTargetId);
						if (target == null) {
							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.21"))); //$NON-NLS-1$
							return;
						}
						// 开启了嘲讽模式,检查等级限制
						if (target.getArenaRankControler().getRoleArenaRank().getSneerId() != 0) {
							int limitLevel = XsgGameParamManager.getInstance().getArenaTauntLevel();
							if (roleRt.getLevel() < limitLevel) {
								__cb.ice_exception(new NoteException(TextUtil.format(
										Messages.getString("ArenaRankControler.TauntLevelLimit2"), limitLevel)));
								return;
							}
						}
						String formationId = target.getArenaRankControler().findFormationId();
						if (TextUtil.isBlank(formationId)) {
							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.22"))); //$NON-NLS-1$
							return;
						}
						// 扣除 嘲讽的花费 和 挑战次数
						roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, target.getArenaRankControler()
								.getSneerResult()[1]));
						getRoleArenaRank()
								.setChallenge(
										getRoleArenaRank().getChallenge()
												- XsgGameParamManager.getInstance().getArenaConsume());
						getRoleArenaRank().setChallengeBuyDate(new Date());

						String targetFormationId = target.getArenaRankControler().findFormationId();
						PvpOpponentFormationView view = target.getFormationControler().getPvpOpponentFormationView(
								targetFormationId);

						// 生成战报信息
						fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(
								XsgFightMovieManager.Type.ArenaRank, roleRt, target);

						__cb.ice_response(view);
					} catch (Exception e) {
						__cb.ice_exception(e);
					}
				}
			}, new Runnable() {
				@Override
				public void run() {
					__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.23"))); //$NON-NLS-1$
				}
			});
		} else {
			__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.24"))); //$NON-NLS-1$
		}
	}

	/**
	 * 挑战 resFlag : 战斗结果，0:失败，1：胜利
	 * 
	 * @throws Exception
	 */
	@Override
	public void fight(final AMD_ArenaRank_endChallenge __cb, final String rivalId, final int isWin,
			final byte remainHero) throws NoteException {

		if (isRivalExist(rivalId)) {
			XsgRoleManager.getInstance().loadRoleByIdAsync(rivalId, new Runnable() {
				@Override
				public void run() {
					// 挑战结束返回显示
					FightResult fightRes = new FightResult(0, 0, 0, 0, 0, 0, 0, 0, ""); //$NON-NLS-1$
					FightResult rivalFightRes = new FightResult(0, 0, 0, 0, 0, 0, 0, 0, ""); //$NON-NLS-1$

					// 对手 和 玩家 排行数据
					IRole rivalRole = XsgRoleManager.getInstance().findRoleById(rivalId);
					if (rivalRole == null) {
						__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.27"))); //$NON-NLS-1$
						return;
					}
					ArenaRank rivalRank = rivalRole.getArenaRankControler().getArenaRank();
					RoleArenaRank rivalRoleRank = rivalRole.getArenaRankControler().getRoleArenaRank();

					ArenaRank rank = getArenaRank();
					RoleArenaRank roleRank = getRoleArenaRank();

					// 玩家 和 对手的 排名
					int roleRnakValue = rank.getRank();
					int rivalRnakValue = rivalRank.getRank();
					int oldRivalRnak = rivalRnakValue;
					int oldRank = roleRnakValue;
					// 排名变化
					int rankChange = rivalRnakValue - roleRnakValue;
					// 对手的嘲讽数据
					int[] sneerArr = rivalRole.getArenaRankControler().getSneerResult();

					// 战斗 1：胜利
					if (isWin == 1) {

						// 判断嘲讽模式 和 嘲讽模式奖励 挑战成功1.5倍
						if (rivalRoleRank.getSneerId() != 0) {
							// 返回 客户端显示获得的竞技币
							// 胜利奖励
							int winReward = (int) (sneerArr[2] * 1.5);
							// 失败奖励
							int loseReward = (int) (sneerArr[2] * 0.5);

							fightRes.sneerNum = winReward;

							// 奖励邮件 和 对手收到失败邮件
							if (fightRes.sneerNum > 0) {
								// 胜利邮件
								Map<String, Integer> rewardMap = new HashMap<String, Integer>(1);
								rewardMap.put(Const.PropertyName.ORDER, fightRes.sneerNum);

								Map<String, String> replaceMap = new HashMap<String, String>(1);
								replaceMap.put("$n", //$NON-NLS-1$
										rivalRole.getName());

								roleRt.getMailControler().receiveRoleMail(MailTemplate.ARENARANK_SNEER_WIN, rewardMap,
										replaceMap);

								// 失败邮件
								rewardMap.clear();
								rewardMap.put(Const.PropertyName.ORDER, loseReward);
								replaceMap = new HashMap<String, String>(1);
								replaceMap.put("$n", roleRt.getName()); //$NON-NLS-1$
								rivalRole.getMailControler().receiveRoleMail(MailTemplate.ARENARANK_SNEER_DEFEND_FAIL,
										rewardMap, replaceMap);

							}

							rivalRoleRank.setSneerId(0);
							rivalRoleRank.setSneerStr(""); //$NON-NLS-1$
						}

						// 更新 玩家自身排行数据
						roleRank.setAttackWinSum(roleRank.getAttackWinSum() + 1);
						if (rankChange < 0) {
							rank.setRank(rivalRnakValue);
							rivalRank.setRank(roleRnakValue);
						}

						// 战斗 0:失败
					} else {
						rivalRoleRank.setGuardWinNum(rivalRoleRank.getGuardWinNum() + 1);
						rivalRoleRank.setGuardWinSum(rivalRoleRank.getGuardWinSum() + 1);

						// 防守成功，判断嘲讽模式
						if (rivalRoleRank.getGuardWinSum() >= guardNum) {
							rivalRoleRank.setSneerId(0);
							rivalRoleRank.setSneerStr(""); //$NON-NLS-1$
							rivalRoleRank.setGuardWinSum(0);
						}

						// 嘲讽模式 邮件奖励
						if (sneerArr[2] > 0) {
							// 胜利奖励
							int winReward = (int) (sneerArr[2] * 1.5);
							// 失败奖励
							int loseReward = (int) (sneerArr[2] * 0.5);

							Map<String, Integer> rewardMap = new HashMap<String, Integer>(1);

							// 防守方胜利邮件
							rewardMap.put(Const.PropertyName.ORDER, winReward);
							rivalRole.getMailControler()
									.receiveRoleMail(MailTemplate.ARENARANK_SNEER_DEFEND, rewardMap);
							rivalFightRes.sneerNum = winReward;

							// 进攻方失败邮件
							rewardMap.clear();
							rewardMap.put(Const.PropertyName.ORDER, loseReward);
							Map<String, String> replaceMap = new HashMap<String, String>(1);
							replaceMap.put("$n", //$NON-NLS-1$
									rivalRole.getName());
							roleRt.getMailControler().receiveRoleMail(MailTemplate.SNEER_FAIL, rewardMap, replaceMap);
						}
					}

					// 每日首胜 和 历史最大排名 邮件奖励
					ChallengeFirstReward(rank, roleRnakValue, isWin, fightRes);
					ChallengeMaxReward(rank, isWin, fightRes);

					// 玩家自身排行数据
					roleRank.setAttackFightSum(roleRank.getAttackFightSum() + 1);
					roleRank.setClearCdDate(new Date());

					// 对手数据
					rivalRoleRank.setGuardFightSum(rivalRoleRank.getGuardFightSum() + 1);

					// 保存 玩家 和 对手 数据
					setArenaRank(rank, roleRt);
					setArenaRank(rivalRank, rivalRole);

					// TODO 战报ID，以后需要添加
					String fightInfoId = ""; //$NON-NLS-1$

					// 对手战报结果 嘲讽模式 和 得到奖励
					int sneerId = 0;

					// 赢了，排名和嘲讽都不变
					if (isWin == 1) {
						sneerId = sneerArr[0];
						// 排行差值，向下挑战时，不显示上升排名
						if (rankChange < 0)
							rankChange = -rankChange;
						else
							rankChange = 0;
					} else {
						rankChange = 0;
					}

					// 战报录像ID, 从上下文获取, 验证合法性
					final String movieId = XsgFightMovieManager.getInstance().endFightMovie(roleRt.getRoleId(),
							fightMovieIdContext, isWin, remainHero);
					// 保存 战报
					if (!TextUtil.isBlank(movieId)) {
						rivalRole.getArenaRankControler().addArenaRankFightInfo(roleRt.getRoleId(), movieId, isWin ^ 1,
								rivalRank.getRank(), rankChange, 0, fightReward(rivalRank, rivalFightRes, isWin ^ 1),
								fightInfoId, 1);

						roleRt.getArenaRankControler()
								.addArenaRankFightInfo(rivalRole.getRoleId(), movieId, isWin, rank.getRank(),
										rankChange, sneerId, fightReward(rank, fightRes, isWin), fightInfoId, 0);
					} else {
						log.error(TextUtil.format(Messages.getString("ArenaRankControler.33"), roleRt.getRoleId(), //$NON-NLS-1$
								rivalId, isWin));
					}

					// 更新 缓存中数据
					refreshRantData(rank, rivalRank, rivalRole);

					// 添加竞技场战斗 事件
					eventFight.onArenaFight(isWin, oldRank, rank.getRank(), sneerId, fightReward(rank, fightRes, isWin));
					rivalRole.getArenaRankControler().onFighted(roleRt.getRoleId(), oldRivalRnak, rivalRank.getRank(),
							0, (isWin == 1 ? 0 : 1));
					// 对手玩家 竞技场开服活动
					rivalRole.getOpenServerActiveControler().updateProgress(IOpenServerActiveControler.AREAN_NUM,
							rivalRnakValue + "");
					// 首胜 和 嘲讽 的 排名变化
					fightRes.firsChangeRank = rankChange;
					fightRes.sneerhangeRank = rankChange;
					fightRes.maxRank = rank.getRank();
					fightRes.fightStar = XsgCopyManager.getInstance().calculateStar(orignalHeroCount, remainHero);

					// 显式调用 邮件的红点刷新
					try {
						roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
						if (!rivalRole.isRobot()) {
							rivalRole.getNotifyControler().onMajorUIRedPointChange(
									rivalRole.getArenaRankControler().getRedPointNote());
						}
					} catch (Exception e) {
						log.error("ArenaRank RefreshRedPoint Error.", e); //$NON-NLS-1$
					}

					// 生成一条战报录像ID, 等待客户端上传
					fightRes.reportMovieId = movieId;
					hasRefreshRedPoint = false; // 重新恢复为初始值,
												// 下次进入该方法还是要检查红点
					__cb.ice_response(LuaSerializer.serialize(fightRes));
				}
			}, new Runnable() {
				@Override
				public void run() {
					__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.35"))); //$NON-NLS-1$
				}
			});
		} else {
			__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.36"))); //$NON-NLS-1$
		}
	}

	// 战报显示的 战斗后的奖励
	private String fightReward(ArenaRank arenaRank, FightResult fightRes, int isWin) {
		if (isWin == 1) {
			List<Property> propertyList = new ArrayList<Property>(2);

			if (fightRes.sneerNum > 0) {
				Property pro = new Property();
				pro.code = Const.PropertyName.ORDER;
				pro.value = fightRes.sneerNum;
				propertyList.add(pro);
			}

			int yuanbao = fightRes.maxNum;
			int jinbi = 0;

			for (ArenaFirstWinT firstWin : XsgArenaRankManager.getInstance().firstWinList) {
				if (arenaRank.getRank() >= firstWin.start && arenaRank.getRank() <= firstWin.end) {
					for (String item : firstWin.item.split(",")) { //$NON-NLS-1$
						// 返回获得的元宝
						String codeStr = item.split(":")[0]; //$NON-NLS-1$
						if (codeStr.equals(Const.PropertyName.RMBY)) {
							yuanbao += fightRes.firstWinNum;
						}
						if (codeStr.equals(Const.PropertyName.MONEY)) {
							jinbi += fightRes.firstWinNum;
						}
					}
				}
			}

			if (yuanbao > 0) {
				Property pro = new Property();
				pro.code = Const.PropertyName.RMBY;
				pro.value = yuanbao;
				propertyList.add(pro);
			} else if (jinbi > 0) {
				Property pro = new Property();
				pro.code = Const.PropertyName.MONEY;
				pro.value = jinbi;
				propertyList.add(pro);
			}

			return TextUtil.GSON.toJson((Property[]) propertyList.toArray(new Property[propertyList.size()]));
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * 添加 竞技场排行 的战报信息
	 */
	@Override
	public ArenaRankFight addArenaRankFightInfo(String roleId, String movieId, int fightState, int rankCurrent,
			int rankChange, int sneerId, String reward, String fightId, int type) {
		// 新增一条战报信息
		ArenaRankFight ArenaRankFight = new ArenaRankFight(GlobalDataManager.getInstance().generatePrimaryKey(),
				this.roleDb, fightId, movieId, rankCurrent, rankChange, sneerId, reward, fightState, roleId,
				new Date(), type);

		this.addReportList(ArenaRankFight);
		this.rivalArr = null;// 挑战后清空对手缓存
		return ArenaRankFight;
	}

	/**
	 * 复仇 resFlag : 战斗结果，0:失败，1：胜利
	 */
	@Override
	public void endRevenge(final String rivalId, final int isWin, final byte remainHero,
			final AMD_ArenaRank_endRevenge __cb) throws NoteException {
		XsgRoleManager.getInstance().loadRoleByIdAsync(rivalId, new Runnable() {
			@Override
			public void run() {
				// 挑战结束返回显示
				FightResult fightRes = new FightResult(0, 0, 0, 0, 0, 0, 0, 0, ""); //$NON-NLS-1$

				// 对手 和 玩家 排行数据
				IRole rivalRole = XsgRoleManager.getInstance().findRoleById(rivalId);
				ArenaRank rivalRank = rivalRole.getArenaRankControler().getArenaRank();
				ArenaRank rank = getArenaRank();

				// // 历史排名
				// int roleOldRank = rank.getRank();
				// 置换的排名
				int rivalRnakValue = rivalRank.getRank();
				int roleRnakValue = rank.getRank();
				int oldRivalRank = rivalRnakValue;
				// 排名变化
				int rankChange = rivalRnakValue - roleRnakValue;

				// 战斗输赢，0:失败，1：胜利
				if (isWin == 1) {
					// 更新 玩家自身排行数据
					if (rankChange < 0) {
						rank.setRank(rivalRnakValue);
						rivalRank.setRank(roleRnakValue);
						rankChange = -rankChange;
					} else {
						rankChange = 0;
					}
				} else {
					rankChange = 0;
				}

				// 历史最大排名 邮件奖励
				ChallengeMaxReward(rank, isWin, fightRes);
				// 更新 缓存中数据
				refreshRantData(rank, rivalRank, rivalRole);

				String fightInfoId = ""; // TODO 战报ID，以后需要添加 //$NON-NLS-1$
				// 获取上下文中的战报ID, 验证战斗的合法性
				final String movieId = XsgFightMovieManager.getInstance().endFightMovie(roleRt.getRoleId(),
						fightMovieIdContext, isWin, remainHero);
				if (!TextUtil.isBlank(movieId)) {
					// 对方的战报
					rivalRole.getArenaRankControler().addArenaRankFightInfo(roleRt.getRoleId(), movieId, isWin ^ 1,
							rivalRank.getRank(), rankChange, 0, "", fightInfoId, 3); //$NON-NLS-1$
					// 自己的战报
					addArenaRankFightInfo(rivalRole.getRoleId(), movieId, isWin, getArenaRank().getRank(), rankChange,
							0, "", //$NON-NLS-1$
							fightInfoId, 2);
				} else {
					log.error(TextUtil.format(Messages.getString("ArenaRankControler.44"), roleRt.getRoleId(), rivalId, //$NON-NLS-1$
							isWin));
				}

				// 首胜 和 嘲讽 的 排名变化
				fightRes.firsChangeRank = rankChange;
				fightRes.sneerhangeRank = rankChange;
				fightRes.maxRank = rank.getRank();
				fightRes.fightStar = XsgCopyManager.getInstance().calculateStar(orignalHeroCount, remainHero);

				fightRes.reportMovieId = movieId;

				// 复仇 事件ID
				eventRevenge.onFight(isWin, rank.getRank(), rivalRank.getRank());
				rivalRole.getArenaRankControler().onFighted(roleRt.getRoleId(), oldRivalRank, rivalRank.getRank(), 1,
						(isWin == 1 ? 0 : 1));

				__cb.ice_response(LuaSerializer.serialize(fightRes));
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.45"))); //$NON-NLS-1$
			}
		});

	}

	// 排名变化后，测刷新排行榜缓存数据
	private void refreshRantData(ArenaRank roleRank, ArenaRank rivalRank, IRole rivalRole) {
		// 更新 玩家 和 对手 的 缓存中数据
		XsgArenaRankManager.getInstance().setArenaRankLevelMap_Async(roleRank);
		XsgArenaRankManager.getInstance().setArenaRankLevelMap_Async(rivalRank);
	}

	/**
	 * 排行 商城兑换列表 和 刷新列表
	 */
	@Override
	public ArenaMallSel selMallList() {
		ArenaRank arenaRank = this.getArenaRank();
		RoleArenaRank roleAarenaRank = this.getRoleArenaRank();
		// 商城兑换 刷新时间
		if (DateUtil.checkTime(roleAarenaRank.getExchangeRefreshDate(),
				DateUtil.joinTime(XsgGameParamManager.getInstance().getMall() + ":00"))) { //$NON-NLS-1$
			roleAarenaRank.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem()));
			roleAarenaRank.setExchangeRefreshNum(0);
			roleAarenaRank.setExchangeRefreshDate(new Date());
			this.setArenaRank(arenaRank, this.roleRt);
		}

		ArenaMallSel arenaMallSel = new ArenaMallSel();
		arenaMallSel.exchangeRefreshNum = roleAarenaRank.getExchangeRefreshNum();
		arenaMallSel.ArenaMallList = TextUtil.GSON.fromJson(roleAarenaRank.getExchangeItemStr(), ArenaMall[].class);

		return arenaMallSel;
	}

	/**
	 * 排行 商城 刷新列表
	 * 
	 * @throws NoteException
	 */
	@Override
	public ArenaMallSel refMallList() throws NoteException {
		ArenaRank arenaRank = this.getArenaRank();
		RoleArenaRank roleAarenaRank = this.getRoleArenaRank();
		// 商城兑换 刷新时间
		if (DateUtil.checkTime(roleAarenaRank.getExchangeRefreshDate(),
				DateUtil.joinTime(XsgGameParamManager.getInstance().getMall() + ":00"))) { //$NON-NLS-1$
			roleAarenaRank.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem()));
			roleAarenaRank.setExchangeRefreshNum(0);
			roleAarenaRank.setExchangeRefreshDate(new Date());
			this.setArenaRank(arenaRank, this.roleRt);
		} else {
			ArenaMallRefreshT RefreshT = XsgArenaRankManager.getInstance().getMallRefreshMap()
					.get(roleAarenaRank.getExchangeRefreshNum() + 1);

			if (RefreshT != null) {

				if (roleAarenaRank.getChallengeMoney() >= RefreshT.Cost) {
					roleAarenaRank.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem()));
					roleAarenaRank.setExchangeRefreshNum(roleAarenaRank.getExchangeRefreshNum() + 1);
					roleAarenaRank.setExchangeRefreshDate(new Date());
					mondifyChallengeMoney(-RefreshT.Cost);
					// roleAarenaRank.setChallengeMoney(roleAarenaRank.getChallengeMoney()
					// - RefreshT.Cost);
					this.setArenaRank(arenaRank, this.roleRt);
				} else {
					throw new NoteException(Messages.getString("ArenaRankControler.48")); //$NON-NLS-1$
				}
			} else {
				throw new NoteException(Messages.getString("ArenaRankControler.49")); //$NON-NLS-1$
			}

			// 添加刷新刷新 事件
			eventMallRefresh.onRefresh(roleAarenaRank.getExchangeRefreshNum(), RefreshT.Cost,
					TextUtil.GSON.toJson(this.selMallItem()));
		}

		ArenaMallSel arenaMallSel = new ArenaMallSel();
		arenaMallSel.exchangeRefreshNum = roleAarenaRank.getExchangeRefreshNum();
		arenaMallSel.ArenaMallList = TextUtil.GSON.fromJson(roleAarenaRank.getExchangeItemStr(), ArenaMall[].class);

		return arenaMallSel;
	}

	/**
	 * 兑换, 目前数量是全部兑换，预留参数
	 * 
	 * @throws NoteException
	 */
	@Override
	public ArenaMallSel exchangeItem(int storId) throws NoteException {
		ArenaRank arenaRank = this.getArenaRank();
		RoleArenaRank roleAarenaRank = this.getRoleArenaRank();
		ArenaMall[] arenaMallArr = TextUtil.GSON.fromJson(roleAarenaRank.getExchangeItemStr(), ArenaMall[].class);

		// 兑换商品是否存在
		boolean checkStorId = false;

		for (int i = 0; i < arenaMallArr.length; i++) {

			ArenaMall arenaMall = arenaMallArr[i];

			if (arenaMall.id == storId && arenaMall.num > 0) {
				// 兑换商品是否存在
				checkStorId = true;

				// 0:必须消耗竞技币,1:大于等于该vip等级
				if (this.roleRt.getVipLevel() >= arenaMall.coinType) {
					if (roleAarenaRank.getChallengeMoney() >= arenaMall.price) {
						// 扣除竞技币 和 得到物品
						this.mondifyChallengeMoney(-arenaMall.price);
						this.roleRt.getItemControler().changeItemByTemplateCode(arenaMall.itemId, arenaMall.num);

						arenaMall.num = 0;
						arenaMall.flag = 0;

					} else {
						throw new NoteException(Messages.getString("ArenaRankControler.50")); //$NON-NLS-1$
					}
				} else {
					throw new NoteException(Messages.getString("ArenaRankControler.51")); //$NON-NLS-1$
				}

				arenaMallArr[i] = arenaMall;

				// 添加置换 事件
				eventMallExchange.onExchange(arenaMall.itemId, arenaMall.price);

				break;
			}
		}

		// 兑换商品是否存在
		if (checkStorId) {
			roleAarenaRank.setExchangeItemStr(TextUtil.GSON.toJson(arenaMallArr));
			this.setArenaRank(arenaRank, this.roleRt);

			ArenaMallSel arenaMallSel = new ArenaMallSel();
			arenaMallSel.exchangeRefreshNum = roleAarenaRank.getExchangeRefreshNum();
			arenaMallSel.ArenaMallList = TextUtil.GSON.fromJson(roleAarenaRank.getExchangeItemStr(), ArenaMall[].class);

			return arenaMallSel;
		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.52")); //$NON-NLS-1$
		}
	}

	/**
	 * 战报列表
	 */
	@Override
	public void rankFightReport(final AMD_ArenaRank_robFightReport __cb) {

		// 保存查看战报时间
		ArenaRank arenaRank = this.getArenaRank();
		if (arenaRank == null) {
			__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.53"))); //$NON-NLS-1$
			return;
		}
		this.getRoleArenaRank().setShowReportDate(new Date());
		this.setArenaRank(arenaRank, this.roleRt);

		final List<ArenaRankFight> rankFightList = this.roleDb.getArenaRankFightList();

		final List<String> findIdList = new ArrayList<String>();
		for (ArenaRankFight rankReport : rankFightList) {
			findIdList.add(rankReport.getRivalId());
		}

		XsgRoleManager.getInstance().loadRoleAsync(findIdList, new Runnable() {
			@Override
			public void run() {
				Map<String, IRole> loadRoleMap = new HashMap<String, IRole>(findIdList.size());
				for (String findId : findIdList) {
					IRole findRole = XsgRoleManager.getInstance().findRoleById(findId);
					if (findRole != null) {
						loadRoleMap.put(findId, findRole);
					}
				}

				__cb.ice_response(LuaSerializer.serialize(resRankFightReport(loadRoleMap, rankFightList)));
			}
		});
	}

	/**
	 * 炫耀
	 * 
	 * @param reportId
	 *            战报ID
	 * @param channel
	 *            聊天频道
	 * @param targetId
	 *            私聊对象的ID
	 * @param content
	 *            炫耀的字符
	 * @throws NoFactionException
	 * @throws NoGroupException
	 */
	@Override
	public void strutReport(String reportId, int channelType, String targetId, String content) throws NoteException,
			NoGroupException, NoFactionException {

		if (content.trim().equals("")) { //$NON-NLS-1$
			throw new NoteException(Messages.getString("ArenaRankControler.55")); //$NON-NLS-1$
		}

		for (ArenaRankFight arenaRank : this.roleDb.getArenaRankFightList()) {
			if (arenaRank.getId().equals(reportId)) {
				IRole rivalRole = XsgRoleManager.getInstance().findRoleById(arenaRank.getRivalId());
				if (rivalRole != null) {

					String reportContent = "${RPL=" + arenaRank.getFightId() //$NON-NLS-1$
							+ "|" + this.roleRt.getName() + "|" //$NON-NLS-1$ //$NON-NLS-2$
							+ this.roleRt.getVipLevel() + "|" //$NON-NLS-1$
							+ rivalRole.getName() + "|" //$NON-NLS-1$
							+ rivalRole.getVipLevel() + "}"; //$NON-NLS-1$

					// 显示 聊天窗口消息
					this.roleRt.getChatControler().strutArenaRankMessage(channelType, targetId, reportContent, content);

					break;
				} else {
					throw new NoteException(Messages.getString("ArenaRankControler.62")); //$NON-NLS-1$
				}
			}
		}
	}

	@Override
	public IFormation getFormation() {
		// 防守部队信息
		IFormation formation = roleRt.getFormationControler().getFormation(getRoleArenaRank().getGuardId());
		if (formation == null)
			formation = roleRt.getFormationControler().getDefaultFormation();

		return formation;
	}

	// 显示战报列表
	private List<ArenaReportView> resRankFightReport(Map<String, IRole> loadRoleMap, List<ArenaRankFight> rankFightList) {

		// 战报排序，按照时间，最新的显示在第一位
		Collections.sort(rankFightList, new Comparator<ArenaRankFight>() {
			public int compare(ArenaRankFight arg0, ArenaRankFight arg1) {
				return arg1.getFightTime().compareTo(arg0.getFightTime());
			}
		});

		List<ArenaReportView> resReportViewList = new ArrayList<ArenaReportView>(loadRoleMap.size());

		for (ArenaRankFight arenaRank : rankFightList) {

			IRole role = loadRoleMap.get(arenaRank.getRivalId());
			if (role == null) { // 防止某些玩家数据出错引起所有的战报不能看
				continue;
			}

			ArenaReportView reportView = new ArenaReportView();
			reportView.id = role.getRoleId();
			reportView.name = role.getName();
			reportView.level = (short) role.getLevel();
			reportView.icon = this.rankIcon(role);
			reportView.vip = role.getVipLevel();
			reportView.sex = role.getSex();
			reportView.compositeCombat = role.getArenaRankControler().getFormation().calculateBattlePower(); // 综合战力
			reportView.flag = arenaRank.getFlag(); // 战报胜负
			reportView.rank = arenaRank.getRankCurrent(); // 战斗之后的当前排名
			reportView.rankChange = arenaRank.getRankChange(); // 排名变化, 负数：下降名次
			reportView.sneerId = arenaRank.getSneerId(); // 炫耀ID,是否炫耀
			reportView.reward = TextUtil.GSON.fromJson(arenaRank.getReward(), Property[].class);// 获得奖励数量
			reportView.reportId = arenaRank.getId(); // 战报详情ID
			reportView.fightId = arenaRank.getFightId(); // 战报显示ID
			reportView.fightMovieId = arenaRank.getMovieId(); // 战报录像ID
			reportView.type = arenaRank.getType(); // 战报类型
			reportView.reportTime = (System.currentTimeMillis() - arenaRank.getFightTime().getTime()) / 1000; // 战报发生时间

			resReportViewList.add(reportView);
		}

		return resReportViewList;
	}

	// 初始化排名
	// 第一次打开排行榜的，从1万开始
	private int initRank() {
		int initRank = 0;

		// 竞技场全部排名
		Map<Integer, ArenaRank> ArenaRankLevelMap = XsgArenaRankManager.getInstance().ArenaRankLevelMap;

		// 初始化，开始排名位置
		int startRank = XsgGameParamManager.getInstance().getArenaStartRank();

		if (ArenaRankLevelMap.size() > startRank) {
			initRank = ArenaRankLevelMap.size() + 1;
		} else if (ArenaRankLevelMap.get(startRank) != null) {
			initRank = startRank + 1;
		} else {
			initRank = startRank;
		}

		return initRank;
	}

	@Override
	public float calcScale(int num1, int num2) {
		return NumberUtil.randUp((float) num1 * 100 / num2, 2);
	}

	// 设置自身 和 对手 排行显示
	private OwnRank setRoleRankView(Map<Integer, IRole> rivalRoleMap) {
		OwnRank resOwnRank = new OwnRank();

		// 角色自身显示信息
		ArenaRank arenaRank = this.getArenaRank();
		RoleArenaRank roleAarenaRank = this.getRoleArenaRank();
		resOwnRank.rank = arenaRank.getRank();
		resOwnRank.sneerId = roleAarenaRank.getSneerId();
		resOwnRank.SneerStr = roleAarenaRank.getSneerStr();
		resOwnRank.guardId = roleAarenaRank.getGuardId();
		resOwnRank.challenge = roleAarenaRank.getChallenge(); // 挑战令 数量
		resOwnRank.challengeBuy = roleAarenaRank.getChallengeBuy(); // 挑战令 购买次数
		resOwnRank.challengeMoney = roleAarenaRank.getChallengeMoney(); // 获得的挑战币
		resOwnRank.clearCdNum = roleAarenaRank.getClearCdNum(); // 获得的挑战币
		resOwnRank.fightCdTime = this.fightRemainTime(arenaRank); // 挑战CD时间
		resOwnRank.attack = this.calcScale(roleAarenaRank.getAttackWinSum(), roleAarenaRank.getAttackFightSum()); // 进攻胜率
		resOwnRank.guard = this.calcScale(roleAarenaRank.getGuardWinNum(), roleAarenaRank.getGuardFightSum()); // 防守胜率
		// 对手显示信息
		resOwnRank.rivalRankList = this.setRivalRankView(rivalRoleMap);

		return resOwnRank;
	}

	// 设置 对手排行显示
	private RivalRank[] setRivalRankView(Map<Integer, IRole> rivalRoleMap) {
		if (rivalRoleMap == null) {
			return this.rivalArr;
		}
		RivalRank[] resArenaRankArr = new RivalRank[rivalRoleMap.size()];

		int i = 0;
		for (int rank : rivalRoleMap.keySet()) {
			IRole role = rivalRoleMap.get(rank);
			ArenaRank rivalRank = XsgArenaRankManager.getInstance().ArenaRoleIdMap.get(role.getRoleId());
			RoleArenaRank roleRank = role.getArenaRankControler().getRoleArenaRank();

			resArenaRankArr[i] = new RivalRank();
			resArenaRankArr[i].id = role.getRoleId();
			resArenaRankArr[i].name = role.getName();
			resArenaRankArr[i].icon = this.rankIcon(role);
			resArenaRankArr[i].level = role.getLevel();
			resArenaRankArr[i].compositeCombat = role.getArenaRankControler().getFormation().calculateBattlePower(); // 综合战力
			resArenaRankArr[i].vip = role.getVipLevel();
			resArenaRankArr[i].sex = role.getSex();
			resArenaRankArr[i].groupName = role.getFactionControler().getFactionName(); // 工会名称
			resArenaRankArr[i].rank = rivalRank.getRank();
			resArenaRankArr[i].sneerId = roleRank.getSneerId(); // 嘲讽类型
			resArenaRankArr[i].sneerStr = roleRank.getSneerStr(); // 嘲讽显示文字
			resArenaRankArr[i].guardId = roleRank.getGuardId(); // 防守队伍
			resArenaRankArr[i].attack = this.calcScale(roleRank.getAttackWinSum(), roleRank.getAttackFightSum());// 进攻胜率
			resArenaRankArr[i].guard = this.calcScale(roleRank.getGuardWinNum(), roleRank.getGuardFightSum());// 防守胜率
			resArenaRankArr[i].guardHeroArr = role.getArenaRankControler().getFormation().getSummaryView(); // 防守部队信息
			if (role.getFormationControler().getDefaultFormation() != null
					&& role.getFormationControler().getDefaultFormation().getBuff() != null) {
				resArenaRankArr[i].formationBuffID = role.getFormationControler().getDefaultFormation().getBuff()
						.getTemplate().getId();
			}
			i++;
		}
		if (rivalRoleMap.size() == this.rivalNum) {
			this.rivalArr = resArenaRankArr;
		}
		return resArenaRankArr;
	}

	/**
	 * 挑战排行榜CD剩余时间
	 * 
	 * @param arenaRank
	 * @return
	 */
	@Override
	public int fightRemainTime(ArenaRank arenaRank) {
		long remainTime = DateUtil.addSecond(this.getRoleArenaRank().getClearCdDate(),
				XsgGameParamManager.getInstance().getArenaHour()).getTime()
				- System.currentTimeMillis();
		remainTime = remainTime < 0 ? 0 : remainTime / 1000;

		return (int) remainTime;
	}

	// 头像为空，根据性别，随机取得头像
	@Override
	public String rankIcon(IRole rivalRole) {
		if (TextUtil.isBlank(rivalRole.getHeadImage())) {
			return XsgRoleManager.getInstance().randomHeadImage(rivalRole.getSex());
		} else {
			return rivalRole.getHeadImage();
		}
	}

	/**
	 * 过滤排行榜
	 * 
	 * @param roleRank
	 * @param num
	 * @return
	 */
	private Map<String, Integer> filterRank(int roleRank, int num) {

		// 根据自身排名， 筛选对手排名
		int[] rankArry = this.filterInitRank(roleRank, num);
		Map<String, Integer> resFilterRankMap = new HashMap<String, Integer>(rankArry.length);

		Map<Integer, ArenaRank> ArenaRankLevelMap = XsgArenaRankManager.getInstance().ArenaRankLevelMap;
		for (int filterRank : rankArry) {
			ArenaRank rivalRole = ArenaRankLevelMap.get(filterRank);
			if (rivalRole != null) {
				resFilterRankMap.put(rivalRole.getRoleId(), filterRank);
			} else {
				log.warn(Messages.getString("ArenaRankControler.63") + roleRt.getRoleId() //$NON-NLS-1$
						+ ",filterRank：" + filterRank); //$NON-NLS-1$
			}
		}

		return resFilterRankMap;
	}

	// 根据自身排名，随机出对手排名
	private int[] filterInitRank(int roleRank, int num) {
		int[] resRankArry = new int[num];

		for (ArenaRankMatchT matchT : XsgArenaRankManager.getInstance().rankMatchList) {
			if (roleRank >= matchT.start) {
				resRankArry[0] = NumberUtil.randomContain(this.calcRank(roleRank, matchT.oneStart),
						this.calcRank(roleRank, matchT.oneEnd));
				resRankArry[1] = NumberUtil.randomContain(this.calcRank(roleRank, matchT.twoStart),
						this.calcRank(roleRank, matchT.twoEnd));
				resRankArry[2] = NumberUtil.randomContain(this.calcRank(roleRank, matchT.threeStart),
						this.calcRank(roleRank, matchT.threeEnd));

				break;
			}
		}

		return resRankArry;
	}

	/**
	 * 获取某个等级的匹配范围
	 * 
	 * @param rank
	 *            对应的等级
	 */
	private int[] filterRankRange(int rank) {
		int[] resRank = new int[] { 0, 0 };
		List<Integer> list = new ArrayList<Integer>();

		for (ArenaRankMatchT matchT : XsgArenaRankManager.getInstance().rankMatchList) {
			if (rank >= matchT.start) {
				list.add(calcRank(rank, matchT.oneStart));
				list.add(calcRank(rank, matchT.oneEnd));
				list.add(calcRank(rank, matchT.twoStart));
				list.add(calcRank(rank, matchT.twoEnd));
				list.add(calcRank(rank, matchT.threeStart));
				list.add(calcRank(rank, matchT.threeEnd));
				break;
			}
		}

		// 从小到大排序
		Collections.sort(list, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		resRank[0] = list.get(0);
		resRank[1] = list.get(list.size() - 1);
		return resRank;
	}

	// 匹配等级小于0，需要关联自身的等级
	private int calcRank(int roleRank, int matchRank) {
		if (matchRank > 0) {
			return matchRank;
		} else {
			return roleRank + matchRank;
		}
	}

	// 每日首胜
	private void ChallengeFirstReward(ArenaRank roleRank, int oldRank, int resFlag, FightResult fightRes) {

		// 必须战斗胜利
		if (resFlag > 0) {
			// 每天首胜
			if (DateUtil.checkTime(this.getRoleArenaRank().getFightDate(),
					DateUtil.joinTime(XsgGameParamManager.getInstance().getArenaInterval()))) {

				for (ArenaFirstWinT firstWin : XsgArenaRankManager.getInstance().firstWinList) {

					if (roleRank.getRank() >= firstWin.start && roleRank.getRank() <= firstWin.end) {

						// Map<String, Integer> rewardMap = new HashMap<String,
						// Integer>();
						for (String item : firstWin.item.split(",")) { //$NON-NLS-1$
							// 返回获得的元宝
							// if (item.split(":")[0]
							// .equals(Const.PropertyName.RMBY)) {
							fightRes.firstWinNum += Integer.valueOf(item.split(":")[1]); //$NON-NLS-1$
							// }

							// rewardMap.put(item.split(":")[0], //$NON-NLS-1$
							// Integer.valueOf(item.split(":")[1]));
							// //$NON-NLS-1$
							String[] vpair = item.split(":");
							// 直接加给用户
							roleRt.getRewardControler().acceptReward(vpair[0], Integer.parseInt(vpair[1]));
						}

						// 发送 首胜邮件, 改为直接加给用户，不发邮件
						// this.roleRt.getMailControler().receiveRoleMail(
						// XsgMailManager.MailTempleId.FIRST, rewardMap);
						if (!hasRefreshRedPoint) {
							hasRefreshRedPoint = true; // 首胜刷新红点,将标记设置为true,防止最大排名重复调用
							this.roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
						}

						break;
					}
				}

				// 胜利更新战斗时间
				this.getRoleArenaRank().setFightDate(new Date());

				firstWin.onFirstWin();
			}
		}
	}

	// 历史最大排名 的 奖励
	private void ChallengeMaxReward(ArenaRank roleRank, int resFlag, FightResult fightRes) {
		// 必须战斗胜利
		if (resFlag > 0) {

			// 历史最高排名
			int oldMaxRank = this.getRoleArenaRank().getMaxRank();
			if (roleRank.getRank() < oldMaxRank) {
				int newReward = 0, oldReward = 0;

				// 最新排名得到奖励
				for (ArenaLevelAwardT levelAward : XsgArenaRankManager.getInstance().levelAwardList) {
					if (roleRank.getRank() >= levelAward.start && roleRank.getRank() <= levelAward.end) {
						newReward = (int) ((levelAward.end - roleRank.getRank() + 1) * levelAward.rmby);
					}

					if (roleRank.getRank() < levelAward.start) {
						newReward += levelAward.sum;
						break;
					}
				}

				// 历史排名 曾经得到奖励
				for (ArenaLevelAwardT levelAward : XsgArenaRankManager.getInstance().levelAwardList) {
					if (oldMaxRank >= levelAward.start && oldMaxRank <= levelAward.end) {
						oldReward = (int) ((levelAward.end - oldMaxRank + 1) * levelAward.rmby);
					}

					if (oldMaxRank < levelAward.start) {
						oldReward += levelAward.sum;
						break;
					}
				}

				// 发邮件, 改为直接加给用户，不发邮件
				Map<String, Integer> rewardMap = new HashMap<String, Integer>();
				rewardMap.put(Const.PropertyName.RMBY, newReward - oldReward);

				// Map<String, String> replaceMap = new HashMap<String,
				// String>(1);
				// replaceMap.put("$p", String.valueOf(roleRank.getRank()));
				// //$NON-NLS-1$

				// this.roleRt.getMailControler().receiveRoleMail(
				// XsgMailManager.MailTempleId.HISTORY, rewardMap,
				// replaceMap);
				// 直接加给用户
				for (Map.Entry<String, Integer> entry : rewardMap.entrySet()) {
					roleRt.getRewardControler().acceptReward(entry.getKey(), entry.getValue());
				}

				if (!hasRefreshRedPoint) {
					hasRefreshRedPoint = true; // 首胜刷新红点,将标记设置为true,防止最大排名重复调用
					this.roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
				}

				// 战斗返回数据
				fightRes.historyRank = this.getRoleArenaRank().getMaxRank();
				fightRes.maxNum = newReward - oldReward;

				// 设置历史最大等级
				this.getRoleArenaRank().setMaxRank(roleRank.getRank());
			}
		}
	}

	// 超过一定数量，删除最早的一条记录
	private void addReportList(ArenaRankFight ArenaRankFight) {
		List<ArenaRankFight> arenaRankFightList = this.roleDb.getArenaRankFightList();
		arenaRankFightList.add(ArenaRankFight);

		// 根据时间排序
		Comparator<ArenaRankFight> comparator = new Comparator<ArenaRankFight>() {
			public int compare(ArenaRankFight s1, ArenaRankFight s2) {
				return s2.getFightTime().compareTo(s1.getFightTime());
			};
		};
		Collections.sort(arenaRankFightList, comparator);

		// 超过一定数量删除
		if (arenaRankFightList.size() > XsgGameParamManager.getInstance().getReportNum()) {
			arenaRankFightList.remove(arenaRankFightList.size() - 1);
		}

		// 推送红点状态
		if (ArenaRankFight.getType() == 1 && ArenaRankFight.getFightTime() != null) {
			roleRt.getNotifyControler()
					.onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.ArenaRankMenu, false));
		}

		this.roleDb.setArenaRankFightList(arenaRankFightList);
	}

	// 计算竞技场 商品列表
	private ArenaMall[] selMallItem() {

		int mallNum1 = XsgGameParamManager.getInstance().getArenaMallNum()[0];
		int mallNum2 = XsgGameParamManager.getInstance().getArenaMallNum()[1];

		List<ArenaMallStoreT> mallStoreList = new ArrayList<ArenaMallStoreT>(mallNum1 + mallNum2);

		// 固定道具 和 概率随机 道具
		Map<Integer, ArenaMallStoreT> storeMap1 = XsgArenaRankManager.getInstance().getMallStoreMap1();
		Map<Integer, ArenaMallStoreT> storeMap2 = XsgArenaRankManager.getInstance().getMallStoreMap2();

		// 固定道具
		if (storeMap1.size() > mallNum1) {
			mallStoreList.addAll(this.calcRandPro(mallNum1, storeMap1));
		} else {
			mallStoreList.addAll(new ArrayList<ArenaMallStoreT>(storeMap1.values()));
		}

		// 概率随机 道具
		if (storeMap2.size() > mallNum2) {
			mallStoreList.addAll(this.calcRandPro(mallNum2, storeMap2));
		} else {
			mallStoreList.addAll(storeMap2.values());
		}

		ArenaMall[] arenaMallArr = new ArenaMall[mallStoreList.size()];
		for (int i = 0; i < mallStoreList.size(); i++) {
			ArenaMall arenaMall = new ArenaMall();
			ArenaMallStoreT storeT = mallStoreList.get(i);
			if (storeT != null) {
				arenaMall.id = storeT.ID;
				arenaMall.itemId = storeT.ItemID;
				arenaMall.num = storeT.NumMax;
				arenaMall.price = storeT.NumMax * storeT.Price;
				arenaMall.coinType = storeT.CoinType;
				arenaMall.flag = 1;

				arenaMallArr[i] = arenaMall;
			}
		}

		return arenaMallArr;
	}

	/**
	 * 根据概率，计算商城显示物品
	 * 
	 * @param mallNum
	 * @param storeMap
	 * @return
	 */
	private List<ArenaMallStoreT> calcRandPro(int mallNum, Map<Integer, ArenaMallStoreT> storeMap) {

		List<ArenaMallStoreT> resStoreList = new ArrayList<ArenaMallStoreT>(mallNum);

		// 合并概率
		List<Integer> proList = new ArrayList<Integer>(storeMap.size());
		Map<Integer, Integer> proMap = new HashMap<Integer, Integer>(storeMap.size());
		for (ArenaMallStoreT mallStoreT : storeMap.values()) {
			proList.add(mallStoreT.Pro);
			proMap.put(proList.size(), mallStoreT.ID);
		}

		// 概率计算
		for (int i = 0; i < mallNum; i++) {
			int index = NumberUtil.randRound(proList);
			resStoreList.add(storeMap.get(proMap.get(index)));
			proList.set(index, 0);
		}

		return resStoreList;
	}

	/**
	 * 角色下线时候，调用 清除数据
	 */
	@Override
	public void clearData() {
		// this.rivalRoleMap.clear();
	}

	@Override
	public void selectRivalArena(RivalRank rivalRank) {
		RoleArenaRank rivalArenaRank = this.getRoleArenaRank();
		rivalRank.attack = NumberUtil.randUp(
				(float) rivalArenaRank.getAttackWinSum() * 100 / rivalArenaRank.getAttackFightSum(), 2); // 进攻胜率
		rivalRank.guard = NumberUtil.randUp(
				(float) rivalArenaRank.getGuardWinNum() * 100 / rivalArenaRank.getGuardFightSum(), 2); // 防守胜率
		ArenaRank rank = getArenaRank();
		if (rank != null) {
			rivalRank.rank = rank.getRank();
		}
		// 防守部队信息
		if (!TextUtil.isBlank(rivalArenaRank.getGuardId())
				&& this.roleRt.getFormationControler().getFormation(rivalArenaRank.getGuardId()) != null) {
			rivalRank.guardHeroArr = this.roleRt.getFormationControler().getFormation(rivalArenaRank.getGuardId())
					.getSummaryView();
			rivalRank.supportHeroArr = this.roleRt.getFormationControler().getFormation(rivalArenaRank.getGuardId())
					.getSupportSummaryView();
		} else {
			rivalRank.guardHeroArr = this.roleRt.getFormationControler().getDefaultFormation().getSummaryView();
			rivalRank.supportHeroArr = this.roleRt.getFormationControler().getDefaultFormation()
					.getSupportSummaryView();
		}
	}

	@Override
	public void saveFightMovie(String fightMovieId, FightMovieView view) {
		XsgFightMovieManager.getInstance().saveFightMovie(roleRt.getRoleId(), fightMovieId, view);
	}

	@Override
	public void getFightMovie(String fightMovieId, final AMD_ArenaRank_getFightMovie _cb) {
		XsgFightMovieManager.getInstance().findFightMovie(fightMovieId, new FindMovieCallback() {
			@Override
			public void onFindMovie(FightMovieView movieView) {
				if (movieView != null) {
					_cb.ice_response(new FightMovieView[] { movieView });
				} else {
					_cb.ice_response(new FightMovieView[0]);
				}
			}
		});
	}

	private void endAutoFight(final AMD_ArenaRank_challenge __cb, final IRole rivalRole,
			PvpOpponentFormationView selfFormation, PvpOpponentFormationView opponentFormation,
			final CrossMovieView movie) {
		int isWin = movie.winRoleId.equals(roleRt.getRoleId()) ? 1 : 0;
		byte remainHero = (byte) movie.selfHeroNum;

		// 挑战结束返回显示
		FightResult fightRes = new FightResult(0, 0, 0, 0, 0, 0, 0, 0, ""); //$NON-NLS-1$
		FightResult rivalFightRes = new FightResult(0, 0, 0, 0, 0, 0, 0, 0, ""); //$NON-NLS-1$

		// 对手 和 玩家 排行数据
		if (rivalRole == null) {
			__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.27"))); //$NON-NLS-1$
			return;
		}
		ArenaRank rivalRank = rivalRole.getArenaRankControler().getArenaRank();
		RoleArenaRank rivalRoleRank = rivalRole.getArenaRankControler().getRoleArenaRank();

		ArenaRank rank = getArenaRank();
		RoleArenaRank roleRank = getRoleArenaRank();

		// 玩家 和 对手的 排名
		int roleRnakValue = rank.getRank();
		int rivalRnakValue = rivalRank.getRank();
		int oldRank = roleRnakValue;
		int oldRivalRnak = rivalRnakValue;
		// 排名变化
		int rankChange = rivalRnakValue - roleRnakValue;
		// 对手的嘲讽数据
		int[] sneerArr = rivalRole.getArenaRankControler().getSneerResult();

		// 战斗 1：胜利
		if (isWin == 1) {

			// 判断嘲讽模式 和 嘲讽模式奖励 挑战成功1.5倍
			if (rivalRoleRank.getSneerId() != 0) {
				// 返回 客户端显示获得的竞技币
				// 胜利奖励
				int winReward = (int) (sneerArr[2] * 1.5);
				// 失败奖励
				int loseReward = (int) (sneerArr[2] * 0.5);

				fightRes.sneerNum = winReward;

				// 奖励邮件 和 对手收到失败邮件
				if (fightRes.sneerNum > 0) {
					// 胜利邮件
					Map<String, Integer> rewardMap = new HashMap<String, Integer>(1);
					rewardMap.put(Const.PropertyName.ORDER, fightRes.sneerNum);

					Map<String, String> replaceMap = new HashMap<String, String>(1);
					replaceMap.put("$n", //$NON-NLS-1$
							rivalRole.getName());

					roleRt.getMailControler().receiveRoleMail(MailTemplate.ARENARANK_SNEER_WIN, rewardMap, replaceMap);

					// 失败邮件
					rewardMap.clear();
					rewardMap.put(Const.PropertyName.ORDER, loseReward);
					replaceMap = new HashMap<String, String>(1);
					replaceMap.put("$n", roleRt.getName()); //$NON-NLS-1$
					rivalRole.getMailControler().receiveRoleMail(MailTemplate.ARENARANK_SNEER_DEFEND_FAIL, rewardMap,
							replaceMap);

				}

				rivalRoleRank.setSneerId(0);
				rivalRoleRank.setSneerStr(""); //$NON-NLS-1$
			}

			// 更新 玩家自身排行数据
			roleRank.setAttackWinSum(roleRank.getAttackWinSum() + 1);
			if (rankChange < 0) {
				rank.setRank(rivalRnakValue);
				rivalRank.setRank(roleRnakValue);
			}

			// 战斗 0:失败
		} else {
			rivalRoleRank.setGuardWinNum(rivalRoleRank.getGuardWinNum() + 1);
			rivalRoleRank.setGuardWinSum(rivalRoleRank.getGuardWinSum() + 1);

			// 防守成功，判断嘲讽模式
			if (rivalRoleRank.getGuardWinSum() >= guardNum) {
				rivalRoleRank.setSneerId(0);
				rivalRoleRank.setSneerStr(""); //$NON-NLS-1$
				rivalRoleRank.setGuardWinSum(0);
			}

			// 嘲讽模式 邮件奖励
			if (sneerArr[2] > 0) {
				// 胜利奖励
				int winReward = (int) (sneerArr[2] * 1.5);
				// 失败奖励
				int loseReward = (int) (sneerArr[2] * 0.5);

				Map<String, Integer> rewardMap = new HashMap<String, Integer>(1);

				// 防守方胜利邮件
				rewardMap.put(Const.PropertyName.ORDER, winReward);
				rivalRole.getMailControler().receiveRoleMail(MailTemplate.ARENARANK_SNEER_DEFEND, rewardMap);
				rivalFightRes.sneerNum = winReward;

				// 进攻方失败邮件
				rewardMap.clear();
				rewardMap.put(Const.PropertyName.ORDER, loseReward);
				Map<String, String> replaceMap = new HashMap<String, String>(1);
				replaceMap.put("$n", //$NON-NLS-1$
						rivalRole.getName());
				roleRt.getMailControler().receiveRoleMail(MailTemplate.SNEER_FAIL, rewardMap, replaceMap);
			}
		}

		// 每日首胜 和 历史最大排名 邮件奖励
		ChallengeFirstReward(rank, roleRnakValue, isWin, fightRes);
		ChallengeMaxReward(rank, isWin, fightRes);

		// 玩家自身排行数据
		roleRank.setAttackFightSum(roleRank.getAttackFightSum() + 1);
		roleRank.setClearCdDate(new Date());

		// 对手数据
		rivalRoleRank.setGuardFightSum(rivalRoleRank.getGuardFightSum() + 1);

		// 保存 玩家 和 对手 数据
		setArenaRank(rank, roleRt);
		setArenaRank(rivalRank, rivalRole);

		// TODO 战报ID，以后需要添加
		String fightInfoId = ""; //$NON-NLS-1$

		// 对手战报结果 嘲讽模式 和 得到奖励
		int sneerId = 0;

		// 赢了，排名和嘲讽都不变
		if (isWin == 1) {
			sneerId = sneerArr[0];
			// 排行差值，向下挑战时，不显示上升排名
			if (rankChange < 0)
				rankChange = -rankChange;
			else
				rankChange = 0;
		} else {
			rankChange = 0;
		}

		// 更新 缓存中数据
		refreshRantData(rank, rivalRank, rivalRole);

		// 添加竞技场战斗 事件
		eventFight.onArenaFight(isWin, oldRank, rank.getRank(), sneerId, fightReward(rank, fightRes, isWin));
		rivalRole.getArenaRankControler().onFighted(roleRt.getRoleId(), oldRivalRnak, rivalRank.getRank(), 0,
				(isWin == 1 ? 0 : 1));
		// 对手玩家 竞技场开服活动
		rivalRole.getOpenServerActiveControler().updateProgress(IOpenServerActiveControler.AREAN_NUM,
				rivalRnakValue + "");
		// 首胜 和 嘲讽 的 排名变化
		fightRes.firsChangeRank = rankChange;
		fightRes.sneerhangeRank = rankChange;
		fightRes.maxRank = rank.getRank();
		fightRes.fightStar = XsgCopyManager.getInstance().calculateStar(orignalHeroCount, remainHero);
		if (isWin == 0) {
			fightRes.fightStar = 0;
		}

		// 显式调用 邮件的红点刷新
		try {
			roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
			if (!rivalRole.isRobot()) {
				rivalRole.getNotifyControler().onMajorUIRedPointChange(
						rivalRole.getArenaRankControler().getRedPointNote());
			}
		} catch (Exception e) {
			log.error("ArenaRank RefreshRedPoint Error.", e); //$NON-NLS-1$
		}

		// 战报录像ID
		String movieId = XsgFightMovieManager.getInstance().addFightMovie(Type.ArenaRank, roleRt.getRoleId(),
				rivalRole.getRoleId(), isWin, remainHero, selfFormation, opponentFormation,
				new FightMovieView(isWin, fightRes.fightStar, movie.soloMovie, movie.fightMovie, new byte[0]));
		// 保存 战报
		if (!TextUtil.isBlank(movieId)) {
			rivalRole.getArenaRankControler().addArenaRankFightInfo(roleRt.getRoleId(), movieId, isWin ^ 1,
					rivalRank.getRank(), rankChange, 0, fightReward(rivalRank, rivalFightRes, isWin ^ 1), fightInfoId,
					1);

			roleRt.getArenaRankControler().addArenaRankFightInfo(rivalRole.getRoleId(), movieId, isWin, rank.getRank(),
					rankChange, sneerId, fightReward(rank, fightRes, isWin), fightInfoId, 0);
		} else {
			log.error(TextUtil.format(Messages.getString("ArenaRankControler.33"), roleRt.getRoleId(), //$NON-NLS-1$
					rivalRole.getRoleId(), isWin));
		}
		fightRes.reportMovieId = movieId;
		hasRefreshRedPoint = false; // 重新恢复为初始值,
									// 下次进入该方法还是要检查红点
		__cb.ice_response(new FightResultView(fightRes, new FightMovieByteView[] { new FightMovieByteView(
				movie.soloMovie, movie.fightMovie) }, movie.winType));
	}

	@Override
	public void autoFight(final AMD_ArenaRank_challenge __cb, String targetId, String formationId)
			throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		// 保存选择了，战斗部队武将数量
		setHeroCount(formationId);

		IArenaRankControler rankControler = this.roleRt.getArenaRankControler();
		ArenaRank roleRank = rankControler.getArenaRank();

		if (this.getRoleArenaRank().getChallenge() <= 0) {
			throw new NoteException(Messages.getString("ArenaRankControler.19")); //$NON-NLS-1$
		}
		if (rankControler.fightRemainTime(roleRank) > 0) {
			throw new NoteException(Messages.getString("ArenaRankControler.20")); //$NON-NLS-1$
		}

		final String theTargetId = targetId;
		if (isRivalExist(theTargetId)) {
			XsgRoleManager.getInstance().loadRoleByIdAsync(theTargetId, new Runnable() {
				@Override
				public void run() {
					try {
						final IRole target = XsgRoleManager.getInstance().findRoleById(theTargetId);
						if (target == null) {
							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.21")));
							return;
						}
						// 开启了嘲讽模式,检查等级限制
						if (target.getArenaRankControler().getRoleArenaRank().getSneerId() != 0) {
//							if (!isSneer) {// 客户端不是嘲讽模式，服务器检测到开启了
//								__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.sneerOpen")));
//								return;
//							}
							int limitLevel = XsgGameParamManager.getInstance().getArenaTauntLevel();
							if (roleRt.getLevel() < limitLevel) {
								__cb.ice_exception(new NoteException(TextUtil.format(
										Messages.getString("ArenaRankControler.TauntLevelLimit2"), limitLevel)));
								return;
							}
							if (roleRt.getTotalYuanbao() < target.getArenaRankControler().getSneerResult()[1]) {
								__cb.ice_exception(new NotEnoughYuanBaoException());
								return;
							}
						} 
//						else if (isSneer) {
//							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.sneerClose")));
//							return;
//						}

						String formationId = target.getArenaRankControler().findFormationId();
						if (TextUtil.isBlank(formationId)) {
							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.22"))); //$NON-NLS-1$
							return;
						}
						String targetFormationId = target.getArenaRankControler().findFormationId();
						final PvpOpponentFormationView targetFormation = target.getFormationControler()
								.getPvpOpponentFormationView(targetFormationId);

						final String myFormationId = roleRt.getFormationControler().getDefaultFormation().getId();
						int type = XsgFightMovieManager.getInstance().getFightLifeT(Type.ArenaRank.ordinal()).id;
						final CrossPvpView pvpView = new CrossPvpView(type, new CrossRoleView(roleRt.getRoleId(),
								roleRt.getName(), roleRt.getHeadImage(), roleRt.getLevel(), roleRt.getVipLevel(),
								roleRt.getServerId(), roleRt.getSex(), ""), roleRt.getFormationControler()
								.getPvpOpponentFormationView(myFormationId), new CrossRoleView(target.getRoleId(),
								target.getName(), target.getHeadImage(), target.getLevel(), target.getVipLevel(),
								target.getServerId(), target.getSex(), ""), targetFormation, "", 0);

						MovieThreads.execute(new Runnable() {

							@Override
							public void run() {
								final String content = HttpUtil.sendPost(XsgLadderManager.getInstance().movieUrl,
										TextUtil.GSON.toJson(pvpView));
								LogicThread.execute(new Runnable() {

									@Override
									public void run() {
										CrossMovieView movie = TextUtil.GSON.fromJson(content, CrossMovieView.class);
										if (movie == null) {
											__cb.ice_exception(new NoteException(Messages
													.getString("FactionControler.59")));
											return;
										}
										XsgLadderManager.getInstance().replaceNull(movie);
										// 扣除 嘲讽的花费 和 挑战次数
										if (target.getArenaRankControler().getRoleArenaRank().getSneerId() != 0) {
											try {
												roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, target
														.getArenaRankControler().getSneerResult()[1]));
											} catch (Exception e) {
												__cb.ice_exception(e);
												return;
											}
										}
										getRoleArenaRank().setChallenge(
												getRoleArenaRank().getChallenge()
														- XsgGameParamManager.getInstance().getArenaConsume());
										getRoleArenaRank().setChallengeBuyDate(new Date());

										endAutoFight(__cb, target, roleRt.getFormationControler()
												.getPvpOpponentFormationView(myFormationId), targetFormation, movie);
									}
								});
							}
						});
					} catch (Exception e) {
						__cb.ice_exception(e);
					}
				}
			}, new Runnable() {
				@Override
				public void run() {
					__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.23"))); //$NON-NLS-1$
				}
			});
		} else {
			__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.24"))); //$NON-NLS-1$
		}

	}

	private void endAutoRevenge(final String rivalId, PvpOpponentFormationView selfView,
			PvpOpponentFormationView opponentView, final CrossMovieView movie, final AMD_ArenaRank_revenge __cb) {
		int isWin = movie.winRoleId.equals(roleRt.getRoleId()) ? 1 : 0;
		byte remainHero = (byte) movie.selfHeroNum;
		// 挑战结束返回显示
		FightResult fightRes = new FightResult(0, 0, 0, 0, 0, 0, 0, 0, ""); //$NON-NLS-1$

		// 对手 和 玩家 排行数据
		IRole rivalRole = XsgRoleManager.getInstance().findRoleById(rivalId);
		ArenaRank rivalRank = rivalRole.getArenaRankControler().getArenaRank();
		ArenaRank rank = getArenaRank();

		// 置换的排名
		int rivalRnakValue = rivalRank.getRank();
		int roleRnakValue = rank.getRank();
		// 排名变化
		int rankChange = rivalRnakValue - roleRnakValue;

		// 战斗输赢，0:失败，1：胜利
		if (isWin == 1) {
			// 更新 玩家自身排行数据
			if (rankChange < 0) {
				rank.setRank(rivalRnakValue);
				rivalRank.setRank(roleRnakValue);
				rankChange = -rankChange;
			} else {
				rankChange = 0;
			}
		} else {
			rankChange = 0;
		}

		// 历史最大排名 邮件奖励
		ChallengeMaxReward(rank, isWin, fightRes);
		// 更新 缓存中数据
		refreshRantData(rank, rivalRank, rivalRole);

		String fightInfoId = ""; // TODO 战报ID，以后需要添加 //$NON-NLS-1$
		// 获取战报ID
		String movieId = XsgFightMovieManager.getInstance().addFightMovie(Type.ArenaRank, roleRt.getRoleId(),
				rivalRole.getRoleId(), isWin, remainHero, selfView, opponentView,
				new FightMovieView(isWin, fightRes.fightStar, movie.soloMovie, movie.fightMovie, new byte[0]));
		if (!TextUtil.isBlank(movieId)) {
			// 对方的战报
			rivalRole.getArenaRankControler().addArenaRankFightInfo(roleRt.getRoleId(), movieId, isWin ^ 1,
					rivalRank.getRank(), rankChange, 0, "", fightInfoId, 3); //$NON-NLS-1$
			// 自己的战报
			addArenaRankFightInfo(rivalRole.getRoleId(), movieId, isWin, getArenaRank().getRank(), rankChange, 0, "", //$NON-NLS-1$
					fightInfoId, 2);
		} else {
			log.error(TextUtil.format(Messages.getString("ArenaRankControler.44"), roleRt.getRoleId(), rivalId, //$NON-NLS-1$
					isWin));
		}

		// 首胜 和 嘲讽 的 排名变化
		fightRes.firsChangeRank = rankChange;
		fightRes.sneerhangeRank = rankChange;
		fightRes.maxRank = rank.getRank();
		fightRes.fightStar = XsgCopyManager.getInstance().calculateStar(orignalHeroCount, remainHero);
		if (isWin == 0) {
			fightRes.fightStar = 0;
		}

		fightRes.reportMovieId = movieId;

		// 复仇 事件ID
		eventRevenge.onFight(isWin, rank.getRank(), rivalRank.getRank());

		__cb.ice_response(new FightResultView(fightRes, new FightMovieByteView[] { new FightMovieByteView(
				movie.soloMovie, movie.fightMovie) }, movie.winType));

	}

	@Override
	public void autoRevenge(final String rivalId, final AMD_ArenaRank_revenge __cb, String formationId)
			throws NoteException {
		// 保存选择了，战斗部队武将数量
		setHeroCount(formationId);

		if (!this.roleRt.getRoleId().equals(rivalId)) {
			ArenaRank revengeRank = XsgArenaRankManager.getInstance().ArenaRoleIdMap.get(rivalId);
			int roleRank = getArenaRank().getRank();
			int rivalRank = revengeRank.getRank();
			int rankRange[] = filterRankRange(roleRank);
			// 检查复仇上限
			if (rivalRank < rankRange[0]) {
				__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.LevelTooHigh")));
				return;
			}
			if (this.getArenaRank().getRank() > revengeRank.getRank()) {

				XsgRoleManager.getInstance().loadRoleByIdAsync(rivalId, new Runnable() {
					@Override
					public void run() {
						IRole revengeRole = XsgRoleManager.getInstance().findRoleById(rivalId);
						// 开启了嘲讽模式,检查等级限制
						if (revengeRole.getArenaRankControler().getRoleArenaRank().getSneerId() != 0) {
							int limitLevel = XsgGameParamManager.getInstance().getArenaTauntLevel();
							if (roleRt.getLevel() < limitLevel) {
								__cb.ice_exception(new NoteException(TextUtil.format(
										Messages.getString("ArenaRankControler.TauntLevelLimit2"), limitLevel)));
								return;
							}
						}
						String targetFormationId = revengeRole.getArenaRankControler().findFormationId();
						if (!targetFormationId.equals("")) { //$NON-NLS-1$
							RoleArenaRank roleArenaRank = getRoleArenaRank();
							int arenaConsume = XsgGameParamManager.getInstance().getArenaConsume();
							if (roleArenaRank == null || roleArenaRank.getChallenge() < arenaConsume) {
								__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.14"))); //$NON-NLS-1$
								return;
							}

							final PvpOpponentFormationView view = revengeRole.getFormationControler()
									.getPvpOpponentFormationView(targetFormationId);
							final String myFormationId = roleRt.getFormationControler().getDefaultFormation().getId();
							int type = XsgFightMovieManager.getInstance().getFightLifeT(Type.ArenaRank.ordinal()).id;
							final CrossPvpView pvpView = new CrossPvpView(type, new CrossRoleView(roleRt.getRoleId(),
									roleRt.getName(), roleRt.getHeadImage(), roleRt.getLevel(), roleRt.getVipLevel(),
									roleRt.getServerId(), roleRt.getSex(), ""), roleRt.getFormationControler()
									.getPvpOpponentFormationView(myFormationId), new CrossRoleView(revengeRole
									.getRoleId(), revengeRole.getName(), revengeRole.getHeadImage(), revengeRole
									.getLevel(), revengeRole.getVipLevel(), revengeRole.getServerId(), revengeRole
									.getSex(), ""), view, "", 0);

							MovieThreads.execute(new Runnable() {

								@Override
								public void run() {
									final String content = HttpUtil.sendPost(XsgLadderManager.getInstance().movieUrl,
											TextUtil.GSON.toJson(pvpView));
									LogicThread.execute(new Runnable() {

										@Override
										public void run() {
											CrossMovieView movie = TextUtil.GSON
													.fromJson(content, CrossMovieView.class);
											if (movie == null) {
												__cb.ice_exception(new NoteException(Messages
														.getString("FactionControler.59")));
												return;
											}
											XsgLadderManager.getInstance().replaceNull(movie);
											// 减少挑战令
											getRoleArenaRank().setChallenge(
													getRoleArenaRank().getChallenge()
															- XsgGameParamManager.getInstance().getArenaConsume());
											endAutoRevenge(rivalId, roleRt.getFormationControler()
													.getPvpOpponentFormationView(myFormationId), view, movie, __cb);
										}
									});
								}
							});
						} else {
							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.15"))); //$NON-NLS-1$
						}
					}
				}, new Runnable() {
					@Override
					public void run() {
						__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.16"))); //$NON-NLS-1$
						return;
					}
				});

			} else {
				throw new NoteException(Messages.getString("ArenaRankControler.17")); //$NON-NLS-1$
			}
		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.18")); //$NON-NLS-1$
		}

	}

	@Override
	public void onFighted(String opponentId, int from, int to, int type, int flag) {
		arenaRankChange.onChange(opponentId, from, to, type, flag);
	}
}
