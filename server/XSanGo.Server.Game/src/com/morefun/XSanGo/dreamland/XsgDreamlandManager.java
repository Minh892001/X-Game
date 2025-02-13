/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: XsgDreamlandManager
 * 功能描述：
 * 文件名：XsgDreamlandManager.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;

import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.DreamlandDAO;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDreamland;
import com.morefun.XSanGo.faction.factionBattle.FactionBattleUtil;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.LogManager;

/**
 * 南华幻境数据管理
 * 
 * @author weiyi.zhao
 * @since 2016-4-19
 * @version 1.0
 */
public class XsgDreamlandManager {

	/** 单例 */
	private static XsgDreamlandManager instance = new XsgDreamlandManager();

	/** 满星级数 */
	public static final int FullStar = 5;

	/** 排行上限 */
	public static final int RankLimit = 100;

	/** 关卡数据 */
	private Map<Integer, DreamlandSceneT> scenes = new HashMap<Integer, DreamlandSceneT>();

	/** 关卡集与子集关卡数据 */
	private Map<Integer, List<Integer>> sceneGroups = new TreeMap<Integer, List<Integer>>();

	/** 第一个关卡编号 */
	private int firstSceneId;

	/** 系统参数 */
	private DreamlandConfigT config = null;

	/** 星数奖励 */
	private Map<Integer, String> starAwards = new TreeMap<Integer, String>();

	/** 商店次数刷新参数 */
	private Map<Integer, DreamlandShopRefreshConfigT> shopRefreshConfigs = new HashMap<Integer, DreamlandShopRefreshConfigT>();

	/** 商店固定刷新 */
	private Map<Integer, DreamlandShopFixedT> shopFixeds = new TreeMap<Integer, DreamlandShopFixedT>();

	/** 商店固定刷新权值列表 */
	private int[] shopFixedWeights;

	/** 商店随机刷新 */
	private Map<Integer, DreamlandShopRandomT> shopRandoms = new TreeMap<Integer, DreamlandShopRandomT>();

	/** 商店随机刷新权值列表 */
	private int[] shopRandomWeights;

	/** 每日挑战次数购买配置 */
	private Map<Integer, DreamlandChallengeT> challengeTimes = new HashMap<Integer, DreamlandChallengeT>();

	/** 排行榜数据 */
	private List<DreamlandRank> rankList = new ArrayList<DreamlandRank>();

	/**
	 * 构造函数
	 */
	private XsgDreamlandManager() {
		loadScript();
		initRank();
	}

	/**
	 * 返回实例
	 * 
	 * @return
	 */
	public static XsgDreamlandManager getInstance() {
		return instance;
	}

	/**
	 * 初始化排行数据
	 */
	private void initRank() {
		rankList = DreamlandDAO.getFromApplicationContext(ServerLancher.getAc()).findDreamlandRank(RankLimit);
	}

	/**
	 * 加载脚本配置
	 */
	private void loadScript() {
		// 加载场景关卡数据
		loadDreamlandSceneT();
		// 加载系统参数数据
		loadDreamlandConfigT();
		// 加载星数奖励数据
		loadDreamlandStarAwardT();
		// 加载商店刷新数据
		loadDreamlandShopRefreshConfigT();
		// 加载商店固定数据
		loadDreamlandShopFixedT();
		// 加载商店随机数据
		loadDreamlandShopRandomT();
		// 加载每日挑战次数数据
		loadDreamlandChallengeT();
	}

	/**
	 * 加载场景关卡数据
	 */
	private void loadDreamlandSceneT() {
		List<DreamlandSceneT> list = ExcelParser.parse(DreamlandSceneT.class);
		for (Iterator<DreamlandSceneT> it = list.iterator(); it.hasNext();) {
			DreamlandSceneT t = it.next();
			if (t.sceneId < 1) {
				it.remove();
				continue;
			}
			scenes.put(t.sceneId, t);
			List<Integer> groups = this.sceneGroups.get(t.sceneGroupId);
			if (groups == null) {
				groups = new ArrayList<Integer>();
			}
			groups.add(t.sceneId);
			this.sceneGroups.put(t.sceneGroupId, groups);
		}
		// 初始化关卡集前后置关系
		List<Integer> groupList = new ArrayList<Integer>(sceneGroups.keySet());
		for (int i = 0; i < groupList.size(); i++) {
			int group_T = groupList.get(i);
			List<Integer> sceneList = sceneGroups.get(group_T);
			// 初始化前一个关卡集节点
			int previous_T = 0;
			int previous_i = i - 1;
			if (previous_i >= 0) {
				previous_T = groupList.get(previous_i);
			}
			// 初始化下一个关卡集节点
			int next_t = 0;
			int next_i = i + 1;
			if (next_i < groupList.size()) {
				next_t = groupList.get(next_i);
			}
			for (int sceneId : sceneList) {
				DreamlandSceneT t = scenes.get(sceneId);
				t.preGroup = previous_T;
				t.postGroup = next_t;
				if (t.preSceneId < 1 && t.preGroup < 1) {// 无前置的关卡为初始关卡
					firstSceneId = t.sceneId;
				}
			}
		}
	}

	/**
	 * 加载系统参数数据
	 */
	private void loadDreamlandConfigT() {
		List<DreamlandConfigT> list = ExcelParser.parse(DreamlandConfigT.class);
		if (!list.isEmpty()) {
			this.config = list.get(0);
		}
		if (this.config == null) {
			LogManager.error(new Exception("Dreamland Config is null"));
		}
	}

	/**
	 * 加载星数奖励数据
	 */
	private void loadDreamlandStarAwardT() {
		List<DreamlandStarAwardT> list = ExcelParser.parse(DreamlandStarAwardT.class);
		for (DreamlandStarAwardT t : list) {
			if (t.starNum < 1) {
				continue;
			}
			this.starAwards.put(t.starNum, t.items);
		}
	}

	/**
	 * 加载商店刷新数据
	 */
	private void loadDreamlandShopRefreshConfigT() {
		List<DreamlandShopRefreshConfigT> list = ExcelParser.parse(DreamlandShopRefreshConfigT.class);
		for (DreamlandShopRefreshConfigT t : list) {
			if (t.times < 1) {
				continue;
			}
			this.shopRefreshConfigs.put(t.times, t);
		}
	}

	/**
	 * 加载商店固定数据
	 */
	private void loadDreamlandShopFixedT() {
		List<DreamlandShopFixedT> list = ExcelParser.parse(DreamlandShopFixedT.class);
		for (DreamlandShopFixedT t : list) {
			if (t.id < 1) {
				continue;
			}
			this.shopFixeds.put(t.id, t);
			this.shopFixedWeights = ArrayUtils.add(this.shopFixedWeights, t.pro);
		}
	}

	/**
	 * 加载商店随机数据
	 */
	private void loadDreamlandShopRandomT() {
		List<DreamlandShopRandomT> list = ExcelParser.parse(DreamlandShopRandomT.class);
		for (DreamlandShopRandomT t : list) {
			if (t.id < 1) {
				continue;
			}
			this.shopRandoms.put(t.id, t);
			this.shopRandomWeights = ArrayUtils.add(this.shopRandomWeights, t.pro);
		}
	}

	/**
	 * 加载每日挑战次数
	 */
	private void loadDreamlandChallengeT() {
		List<DreamlandChallengeT> list = ExcelParser.parse(DreamlandChallengeT.class);
		for (DreamlandChallengeT t : list) {
			if (t.times < 1) {
				continue;
			}
			this.challengeTimes.put(t.times, t);
		}
	}

	/**
	 * 创建控制器
	 * 
	 * @param r
	 * @param db
	 * @return
	 */
	public DreamlandController createDreamlandController(IRole r, Role db) {
		return new DreamlandController(r, db);
	}

	/**
	 * 获得指定关卡数据
	 * 
	 * @param sceneId
	 * @return
	 */
	public DreamlandSceneT getScenes(int sceneId) {
		return scenes.get(sceneId);
	}

	/**
	 * 获得指定组的关卡列表
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Integer> getGroup(int groupId) {
		return sceneGroups.get(groupId);
	}

	/**
	 * @return Returns the config.
	 */
	public DreamlandConfigT getConfig() {
		return config;
	}

	/**
	 * @return Returns the firstSceneId.
	 */
	public int getFirstSceneId() {
		return firstSceneId;
	}

	/**
	 * @return Returns the starAwards.
	 */
	public Map<Integer, String> getStarAwards() {
		return starAwards;
	}

	/**
	 * 获得对应星数的奖励
	 * 
	 * @param star
	 * @return
	 */
	public String getStarAward(int star) {
		return starAwards.get(star);
	}

	/**
	 * 获得商店刷新配置参数
	 * 
	 * @param refreshNum
	 * @return
	 */
	public DreamlandShopRefreshConfigT getShopRefreshConfigs(int refreshNum) {
		return shopRefreshConfigs.get(refreshNum);
	}

	/**
	 * 获取商店兑换物品对象
	 * 
	 * @param itemId
	 * @return
	 */
	public DreamlandShopT getDreamlandShop(int itemId) {
		DreamlandShopT shopT = this.shopFixeds.get(itemId);
		if (shopT == null) {
			return this.shopRandoms.get(itemId);
		}
		return shopT;
	}

	/**
	 * 获得挑战数据
	 * 
	 * @param buyNum
	 * @return
	 */
	public DreamlandChallengeT getChallengeTimes(int buyNum) {
		return challengeTimes.get(buyNum);
	}

	/**
	 * 排行排序
	 */
	private void sortRank() {
		Collections.sort(rankList, new Comparator<DreamlandRank>() {
			@Override
			public int compare(DreamlandRank o1, DreamlandRank o2) {
				if (o1.getStarNum() > o2.getStarNum()) {
					return -1;
				}
				if (o1.getStarNum() < o2.getStarNum()) {
					return 1;
				}
				if (o1.getLayerNum() > o2.getLayerNum()) {
					return -1;
				}
				if (o1.getLayerNum() < o2.getLayerNum()) {
					return 1;
				}
				if (o1.getRankUpdateTime().getTime() < o2.getRankUpdateTime().getTime()) {
					return -1;
				}
				if (o1.getRankUpdateTime().getTime() > o2.getRankUpdateTime().getTime()) {
					return 1;
				}
				return 0;
			}
		});
		if (this.rankList.size() > RankLimit) {
			this.rankList.remove(this.rankList.size() - 1);
		}
	}

	/**
	 * 设置排行基础数据
	 * 
	 * @param role
	 */
	public void setRank(IRole role) {
		setRank(role, null, false);
	}

	/**
	 * 设置排行数据
	 * 
	 * @param role
	 * @param rd
	 * @param isRefreshRank 是否需要刷新排行
	 */
	public void setRank(IRole role, RoleDreamland rd, boolean isRefreshRank) {
		DreamlandRank selfRank = null;
		for (DreamlandRank rank : rankList) {
			if (role.getRoleId().equals(rank.getRoleId())) {
				if (isRefreshRank) {// 需要重排才进行移除操作
					rankList.remove(rank);
				}
				selfRank = rank;
				break;
			}
		}
		// 当前角色未进入排行榜，并且不进行刷新，跳出不处理
		if (selfRank == null && !isRefreshRank) {
			return;
		}
		if (isRefreshRank) {// 刷新排行榜
			selfRank = new DreamlandRank();
			selfRank.setRankUnit(role);
			selfRank.setStarNum(rd.getStarNum());
			selfRank.setLayerNum(rd.getLayerNum());
			selfRank.setRankUpdateTime(rd.getUpdateTime());
			this.rankList.add(selfRank);
			sortRank();
		} else {
			selfRank.setRankUnit(role);// 重新初始化角色基础数据
		}
	}

	/**
	 * @return Returns the rankList.
	 */
	public List<DreamlandRank> getRankList() {
		return rankList;
	}

	/**
	 * 计算商店固定刷新的物品列表
	 * 
	 * @return
	 */
	public List<DreamlandShopFixedT> calculateShopFixedList() {
		List<DreamlandShopFixedT> objects = new ArrayList<DreamlandShopFixedT>(this.shopFixeds.values());
		return FactionBattleUtil.calcRandomPositionByRatio(objects, shopFixedWeights, config.fixedItemNum, true);
	}

	/**
	 * 计算商店随机刷新的物品列表
	 * 
	 * @return
	 */
	public List<DreamlandShopRandomT> calculateShopRandomList() {
		List<DreamlandShopRandomT> objects = new ArrayList<DreamlandShopRandomT>(this.shopRandoms.values());
		return FactionBattleUtil.calcRandomPositionByRatio(objects, shopRandomWeights, config.randomItemNum, true);
	}
}
