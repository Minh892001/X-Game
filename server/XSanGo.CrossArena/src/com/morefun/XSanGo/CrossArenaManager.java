package com.morefun.XSanGo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.XSanGo.Protocol.ArenaReportView;
import com.XSanGo.Protocol.CrossArenaPrx;
import com.XSanGo.Protocol.CrossArenaPvpView;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.RivalRank;
import com.morefun.XSanGo.db.CrossArenaMovie;
import com.morefun.XSanGo.db.CrossArenaMovieDAO;
import com.morefun.XSanGo.db.CrossArenaRank;
import com.morefun.XSanGo.db.CrossArenaRankDAO;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.template.CrossArenaMatchT;
import com.morefun.XSanGo.template.CrossArenaRangeT;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class CrossArenaManager {

	private static final class Inner {
		private static final CrossArenaManager instance = new CrossArenaManager();
	}

	public static int MAX_CACHE_NUM = 40000;

	private Object writeLock = new Object();

	/**
	 * 跨服竞技场缓存 key-角色ID
	 */
	private Map<String, CrossArenaRank> roleArenaMap = new ConcurrentHashMap<String, CrossArenaRank>();

	/**
	 * 跨服排行榜，key-跨服区间类型，value:key-排名，value-角色ID
	 */
	public Map<Integer, ConcurrentHashMap<Integer, String>> rankRoleMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, String>>();

	/**
	 * 缓存角色使用时间
	 */
	private Map<String, Long> roleUseTimeMap = new ConcurrentHashMap<String, Long>();

	public List<CrossArenaMatchT> arenaMatchTs = new ArrayList<CrossArenaMatchT>();

	public List<CrossArenaRangeT> rangeTs = new ArrayList<CrossArenaRangeT>();

	/**
	 * 合过服的目标服ID,key-原来服务器ID，value-目标服务器ID
	 */
	public Map<Integer, Integer> targetServerIdMap = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * 未合服服务器列表
	 */
	public List<Integer> serverList = new ArrayList<Integer>();

	private CrossArenaRankDAO crossRankDao = CrossArenaRankDAO.getFromApplicationContext(CrossArenaMain.getAC());

	private CrossArenaManager() {
		arenaMatchTs = ExcelParser.parse(CrossArenaMatchT.class);
		rangeTs = ExcelParser.parse(CrossArenaRangeT.class);

		String result = HttpUtil.sendPost((String) CrossArenaMain.getAC().getBean("mergerServerHttpUrl"), "");
		if (TextUtil.isNotBlank(result)) {
			IntIntPair[] is = TextUtil.GSON.fromJson(result, IntIntPair[].class);
			this.targetServerIdMap.clear();
			for (IntIntPair i : is) {
				this.targetServerIdMap.put(i.first, i.second);
			}
		}

		// 获取未合服服务器列表
		result = HttpUtil.sendPost((String) CrossArenaMain.getAC().getBean("serverHttpUrl"), "");
		if (TextUtil.isNotBlank(result)) {
			List<String> ids = TextUtil.stringToList(result);
			for (String id : ids) {
				serverList.add(NumberUtil.parseInt(id));
			}
		}

		// 删除过期战报
		long delayed = DateUtil.betweenTaskHourMillis(0);
		LogicThread.scheduleTask(new DelayedTask(delayed, TimeUnit.DAYS.toMillis(1)) {

			@Override
			public void run() {
				CrossArenaMovieDAO dao = CrossArenaMovieDAO.getFromApplicationContext(CrossArenaMain.getAC());
				Calendar now = Calendar.getInstance();
				DateUtil.addDays(now, -3);
				dao.deletePast(now.getTime());
			}
		});
	}

	public synchronized void initRank() {
		// 缓存玩家信息
		for (CrossArenaRangeT r : this.rangeTs) {
			List<CrossArenaRank> ranks = crossRankDao.findToCache(r.id);
			if (ranks.size() <= 0) {
				rankRoleMap.put(r.id, new ConcurrentHashMap<Integer, String>());
				CrossArenaPrx prx = IceEntry.getRandomServerPrx(r);
				while (prx == null) {// 直到有游戏服可用
					LogManager.warn("无法初始跨服竞技场排行榜");
					try {
						TimeUnit.SECONDS.sleep(5);
					} catch (InterruptedException e) {
					}
					prx = IceEntry.getRandomServerPrx(r);
				}
				prx.initRank();
			} else {
				for (CrossArenaRank rk : ranks) {
					putCrossArena(rk);
				}
			}
		}
		// 初始化所有排名
		List<Object[]> idRank = crossRankDao.findRank();
		for (Object[] obj : idRank) {
			int rangeId = NumberUtil.parseInt(obj[0].toString());
			String roleId = obj[1].toString();
			int rank = NumberUtil.parseInt(obj[2].toString());
			ConcurrentHashMap<Integer, String> m = rankRoleMap.get(rangeId);
			if (m == null) {
				m = new ConcurrentHashMap<Integer, String>();
				rankRoleMap.put(rangeId, m);
			}
			m.put(rank, roleId);
		}
	}

	public static CrossArenaManager getInstance() {
		return Inner.instance;
	}

	/** 初始化 */
	public void init() {
		// 初始化ICE框架相关逻辑
		IceEntry.activeIceAdapter();
	}

	/**
	 * 更新玩家信息和部队信息
	 * 
	 * @param rank
	 * @param pvpView
	 * @return
	 */
	public synchronized RivalRank updateArena(RivalRank rank, PvpOpponentFormationView pvpView) {
		CrossArenaRank arena = getCrossArenaRank(rank.id);
		if (arena == null) {
			int rangeId = getRangeIdByServerId(rank.serverId);
			// 初始排名
			arena = new CrossArenaRank(rangeId, rank.id, rankRoleMap.get(rangeId).size() + 1, rank.sneerStr, 0, 0, 0,
					0, TextUtil.GSON.toJson(rank), TextUtil.GSON.toJson(pvpView));
			putCrossArena(arena);
			rankRoleMap.get(rangeId).put(arena.getRank(), arena.getRoleId());

			addCrossRank(arena);
		} else {
			arena.setRivalRank(TextUtil.GSON.toJson(rank));
			arena.setFormationView(TextUtil.GSON.toJson(pvpView));

			updateCrossRank(arena);
		}
		rank.rank = arena.getRank();
		return rank;
	}

	/**
	 * 异步增加排行榜
	 * 
	 * @param rank
	 */
	private void addCrossRank(final CrossArenaRank rank) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				crossRankDao.save(rank);
			}
		});
	}

	/**
	 * 异步更新排行榜对象
	 * 
	 * @param rank
	 */
	private void updateCrossRank(final CrossArenaRank rank) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				crossRankDao.update(rank);
			}
		});
	}

	/**
	 * 获取玩家RivalRank对象
	 * 
	 * @param roleId
	 * @return
	 */
	public RivalRank getRoleRivalRank(String roleId) {
		CrossArenaRank arena = getCrossArenaRank(roleId);
		if (arena != null) {
			RivalRank r = TextUtil.GSON.fromJson(arena.getRivalRank(), RivalRank.class);
			r.attack = calcScale(arena.getAttackWinSum(), arena.getAttackFightSum());
			r.guard = calcScale(arena.getGuardWinSum(), arena.getGuardFightSum());
			r.sneerStr = arena.getSignature();
			r.rank = arena.getRank();
			return r;
		}
		return new RivalRank();
	}

	/**
	 * 计算概率
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	private float calcScale(int num1, int num2) {
		if (num2 <= 0) {
			return 100;
		}
		return NumberUtil.randUp((float) num1 * 100 / num2, 2);
	}

	/**
	 * 获取玩家PvpOpponentFormationView
	 * 
	 * @param roleId
	 * @return
	 */
	public PvpOpponentFormationView getRolePvpView(String roleId) {
		CrossArenaRank arena = getCrossArenaRank(roleId);
		return TextUtil.GSON.fromJson(arena.getFormationView(), PvpOpponentFormationView.class);
	}

	/**
	 * 获取排行榜
	 * 
	 * @param size
	 * @return
	 */
	public RivalRank[] getArenaRank(int size, int rangeId) {
		size = Math.min(size, rankRoleMap.get(rangeId).size());
		RivalRank[] rivals = new RivalRank[size];
		for (int i = 1; i <= size; i++) {
			CrossArenaRank arena = getCrossArenaRank(rankRoleMap.get(rangeId).get(i));
			RivalRank r = TextUtil.GSON.fromJson(arena.getRivalRank(), RivalRank.class);

			r.attack = calcScale(arena.getAttackWinSum(), arena.getAttackFightSum());
			r.guard = calcScale(arena.getGuardWinSum(), arena.getGuardFightSum());
			r.sneerStr = arena.getSignature();
			r.rank = i;
			rivals[i - 1] = r;
		}
		return rivals;
	}

	/**
	 * 刷新对手
	 * 
	 * @param roleId
	 * @return
	 */
	public RivalRank[] refreshRival(String roleId) {
		RivalRank[] rs = new RivalRank[3];
		CrossArenaRank my = getCrossArenaRank(roleId);
		int myRank = my.getRank();

		CrossArenaMatchT matchT = null;
		for (CrossArenaMatchT m : this.arenaMatchTs) {
			int endRank = m.endRank == 0 ? Integer.MAX_VALUE : m.endRank;
			if (myRank >= m.beginRank && myRank <= endRank) {
				matchT = m;
				break;
			}
		}

		// 第一个位置
		rs[0] = match(matchT, myRank, 0, my.getRangeId());
		// 第二个位置
		rs[1] = match(matchT, myRank, 1, my.getRangeId());
		// 第三个位置
		rs[2] = match(matchT, myRank, 2, my.getRangeId());
		return rs;
	}

	private RivalRank match(CrossArenaMatchT matchT, int myRank, int index, int rangeId) {
		int beginMatch = matchT.beginMatch1 < 0 ? myRank + matchT.beginMatch1 : matchT.beginMatch1;
		int endMatch = matchT.endMatch1 < 0 ? myRank + matchT.endMatch1 : matchT.endMatch1;
		if (index == 1) {
			beginMatch = matchT.beginMatch2 < 0 ? myRank + matchT.beginMatch2 : matchT.beginMatch2;
			endMatch = matchT.endMatch2 < 0 ? myRank + matchT.endMatch2 : matchT.endMatch2;
		} else if (index == 2) {
			beginMatch = matchT.beginMatch3 < 0 ? myRank + matchT.beginMatch3 : matchT.beginMatch3;
			endMatch = matchT.endMatch3 < 0 ? myRank + matchT.endMatch3 : matchT.endMatch3;
		}
		int rank = NumberUtil.random(beginMatch, endMatch + 1);
		CrossArenaRank arena = getCrossArenaRank(rankRoleMap.get(rangeId).get(rank));
		RivalRank r = TextUtil.GSON.fromJson(arena.getRivalRank(), RivalRank.class);
		r.attack = calcScale(arena.getAttackWinSum(), arena.getAttackFightSum());
		r.guard = calcScale(arena.getGuardWinSum(), arena.getGuardFightSum());
		r.sneerStr = arena.getSignature();
		r.rank = rank;
		return r;
	}

	/**
	 * 结束战斗的逻辑处理
	 * 
	 * @param sourceRoleId
	 * @param isWin
	 * @param rivalRoleId
	 * @param movieId
	 * @return
	 */
	public IntIntPair endFight(String sourceRoleId, boolean isWin, String rivalRoleId, String movieId,
			FightMovieView movieView) {
		CrossArenaRank source = getCrossArenaRank(sourceRoleId);
		CrossArenaRank rival = getCrossArenaRank(rivalRoleId);
		RivalRank sourceRival = TextUtil.GSON.fromJson(source.getRivalRank(), RivalRank.class);
		RivalRank rivalRival = TextUtil.GSON.fromJson(rival.getRivalRank(), RivalRank.class);

		int rankChange = 0;
		int myRank = 0;

		synchronized (writeLock) {
			if (isWin) {
				source.setAttackWinSum(source.getAttackWinSum() + 1);
				source.setAttackFightSum(source.getAttackFightSum() + 1);
				rival.setGuardFightSum(rival.getGuardFightSum() + 1);
				if (source.getRank() > rival.getRank()) {
					rankChange = source.getRank() - rival.getRank();
					// 排位调换
					int sourceRank = source.getRank();
					source.setRank(rival.getRank());
					rival.setRank(sourceRank);

					int rangeId = source.getRangeId();
					rankRoleMap.get(rangeId).put(source.getRank(), sourceRoleId);
					rankRoleMap.get(rangeId).put(rival.getRank(), rivalRoleId);
				}
			} else {
				source.setAttackFightSum(source.getAttackFightSum() + 1);
				rival.setGuardFightSum(rival.getGuardFightSum() + 1);
				rival.setGuardWinSum(rival.getGuardWinSum() + 1);
			}
			myRank = source.getRank();
		}

		updateCrossRank(rival);
		updateCrossRank(source);

		// 保存战报
		try {
			addMovie(new CrossArenaMovie(movieId, TextUtil.gzip(TextUtil.GSON.toJson(movieView)), new Date()));
		} catch (IOException e) {
			LogManager.error(e);
		}

		// 处理合服
		int serverId = sourceRival.serverId;
		if (targetServerIdMap.containsKey(serverId)) {
			serverId = targetServerIdMap.get(serverId);
		}
		CrossArenaPrx prx = IceEntry.getCrossArenaPrx(serverId);
		if (prx != null) {// 增加战斗发起者战报
			prx.begin_addCrossArenaLog(sourceRoleId, new ArenaReportView(rivalRival.id, rivalRival.name,
					(short) rivalRival.level, rivalRival.icon, rivalRival.vip, rivalRival.sex, myRank, isWin ? 1 : 0,
					rivalRival.compositeCombat, rankChange, 0, new Property[0], System.currentTimeMillis(), "", "",
					movieId, 0, rivalRival.serverId, rival.getSignature()));
		}

		// 处理合服
		serverId = rivalRival.serverId;
		if (targetServerIdMap.containsKey(serverId)) {
			serverId = targetServerIdMap.get(serverId);
		}
		CrossArenaPrx prx2 = IceEntry.getCrossArenaPrx(serverId);
		if (prx2 != null) {// 增加对手战报
			prx2.begin_addCrossArenaLog(rivalRoleId,
					new ArenaReportView(sourceRival.id, sourceRival.name, (short) sourceRival.level, sourceRival.icon,
							sourceRival.vip, sourceRival.sex, source.getRank(), isWin ? 0 : 1,
							sourceRival.compositeCombat, -rankChange, 0, new Property[0], System.currentTimeMillis(),
							"", "", movieId, 0, sourceRival.serverId, source.getSignature()));
		}
		return new IntIntPair(myRank, rankChange);
	}

	private void addMovie(final CrossArenaMovie movie) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				CrossArenaMovieDAO dao = CrossArenaMovieDAO.getFromApplicationContext(CrossArenaMain.getAC());
				dao.save(movie);
			}
		});
	}

	/**
	 * 获取PVP需要的对象信息
	 * 
	 * @param leftRoleId
	 * @param rightRoleId
	 * @return
	 */
	public CrossArenaPvpView[] getCrossArenaPvpView(String leftRoleId, String rightRoleId) {
		CrossArenaRank left = getCrossArenaRank(leftRoleId);
		CrossArenaRank right = getCrossArenaRank(rightRoleId);

		RivalRank leftRank = TextUtil.GSON.fromJson(left.getRivalRank(), RivalRank.class);
		RivalRank rightRank = TextUtil.GSON.fromJson(right.getRivalRank(), RivalRank.class);

		CrossRoleView leftRole = new CrossRoleView(leftRank.id, leftRank.name, leftRank.icon, leftRank.level,
				leftRank.vip, leftRank.serverId, leftRank.sex, leftRank.groupName);
		CrossRoleView rightRole = new CrossRoleView(rightRank.id, rightRank.name, rightRank.icon, rightRank.level,
				rightRank.vip, rightRank.serverId, rightRank.sex, rightRank.groupName);

		CrossArenaPvpView[] views = new CrossArenaPvpView[] {
				new CrossArenaPvpView(leftRole, TextUtil.GSON.fromJson(left.getFormationView(),
						PvpOpponentFormationView.class), left.getRank()),
				new CrossArenaPvpView(rightRole, TextUtil.GSON.fromJson(right.getFormationView(),
						PvpOpponentFormationView.class), right.getRank()) };
		return views;
	}

	/**
	 * 设置个性签名
	 * 
	 * @param roleId
	 * @param signature
	 */
	public void setSignature(String roleId, String signature) {
		CrossArenaRank arena = getCrossArenaRank(roleId);
		arena.setSignature(signature);

		updateCrossRank(arena);
	}

	public synchronized void putCrossArena(CrossArenaRank rank) {
		if (!roleArenaMap.containsKey(rank.getRoleId())) {
			roleArenaMap.put(rank.getRoleId(), rank);

			roleUseTimeMap.put(rank.getRoleId(), System.currentTimeMillis());
			// 超限后移除最老的
			if (roleArenaMap.size() > MAX_CACHE_NUM * rangeTs.size()) {
				long min = 0;
				String roleId = null;
				for (Entry<String, Long> entry : roleUseTimeMap.entrySet()) {
					if (min == 0) {
						min = entry.getValue();
						roleId = entry.getKey();
					} else if (entry.getValue() < min) {
						min = entry.getValue();
						roleId = entry.getKey();
					}
				}
				roleArenaMap.remove(roleId);
				roleUseTimeMap.remove(roleId);
			}
		}
	}

	public CrossArenaRank getCrossArenaRank(String roleId) {
		CrossArenaRank rank = roleArenaMap.get(roleId);
		if (rank == null) {
			rank = crossRankDao.getByRoleId(roleId);
			if (rank != null) {
				putCrossArena(rank);
			}
		}
		if (roleUseTimeMap.containsKey(roleId)) {
			roleUseTimeMap.put(roleId, System.currentTimeMillis());
		}
		return rank;
	}

	/**
	 * 保存所有缓存，停服时调用
	 */
	public void saveAllCache() {
		for (final CrossArenaRank r : this.roleArenaMap.values()) {
			DBThreads.execute(new Runnable() {

				@Override
				public void run() {
					crossRankDao.saveOrUpdate(r);
				}
			});
		}
	}

	/**
	 * 获取跨服区间ID
	 * 
	 * @param serverId
	 * @return
	 */
	public int getRangeIdByServerId(int serverId) {
		for (CrossArenaRangeT r : this.rangeTs) {
			if (serverId >= r.beginServerId && serverId <= r.endServerId) {
				return r.id;
			}
		}
		return 1;
	}
}
