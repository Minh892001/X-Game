/**
 * 
 */
package com.morefun.XSanGo.rankList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.LoadingRankList;
import com.XSanGo.Protocol.LoadingRankSub;
import com.XSanGo.Protocol.LoadingRankType;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.RankListSub;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.ArenaRank;
import com.morefun.XSanGo.db.game.RankListDAO;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;

/**
 * 各种 排行榜 全局管理
 * 
 * @author 吕明涛
 * 
 */
public class XsgRankListManager {
	private static XsgRankListManager instance = new XsgRankListManager();
	/** 战力排行榜 */
	private List<RankListSub> combatList = new ArrayList<RankListSub>();
	/** 成就排行榜 */
	private List<RankListSub> achieveList = new ArrayList<RankListSub>();
	/** 大神排行榜 */
	private List<RankListSub> worshipList = new ArrayList<RankListSub>();
	private Map<String, Integer> worshipMap = new HashMap<String, Integer>();
	/** 公会排行榜 */
	private List<RankListSub> factionList = new ArrayList<RankListSub>();

	/** 累计随机显示排行榜 */
	private int loadingSum = -1;
	private int loadingMax = 1000;
	/** 显示排行榜的数量 */
	public int loadingShowNum = 3;

	public static XsgRankListManager getInstance() {
		return instance;
	}

	/**
	 * 创建碎片的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public IRankListControler create(IRole roleRt, Role roleDB) {
		return new RankListControler(roleRt, roleDB);
	}

	private XsgRankListManager() {
		RankListDAO rankListDAO = RankListDAO
				.getFromApplicationContext(ServerLancher.getAc());
		combatList = rankListDAO.findCombat(XsgGameParamManager.getInstance()
				.getRankListCombatAll());
		achieveList =  rankListDAO.findAchieve(XsgGameParamManager.getInstance()
				.getRankListAchieveAll());
		factionList = rankListDAO.findFaction();
		worshipList = rankListDAO.findWorship();
		for (RankListSub sub : worshipList) {
			setWorshipMap(sub.roleId, sub.count);
		}

	}

	public int getCombatListNum() {
		return combatList.size();
	}

	public int getAchieveListNum() {
		return achieveList.size();
	}
	
	public RankListSub getCombat(int i) {
		return combatList.get(i);
	}

	public RankListSub getAchieve(int i) {
		return achieveList.get(i);
	}
	
	public void setAchieveList(RankListSub ach) {
		// 删除已经存在的，加入新排行榜数据，重新排序
		this.rankRemove(this.achieveList, ach.roleId);
		this.achieveList.add(ach);
		this.rankSort(this.achieveList);

		// 排行榜 数量显示数量超出时，删除最后一列
		if (this.getAchieveListNum() > XsgGameParamManager.getInstance()
				.getRankListAchieveAll()) {
			this.achieveList.remove(this.getAchieveListNum() - 1);
		}
	}

	public void addAchieveList(RankListSub ach) {
		this.achieveList.add(ach);
	}
	
	public void setCombatList(RankListSub combat) {
		// 删除已经存在的，加入新排行榜数据，重新排序
		this.rankRemove(this.combatList, combat.roleId);
		this.addCombatList(combat);
		this.rankSort(this.combatList);

		// 排行榜 数量显示数量超出时，删除最后一列
		if (this.getCombatListNum() > XsgGameParamManager.getInstance()
				.getRankListCombatAll()) {
			this.combatList.remove(this.getCombatListNum() - 1);
		}
	}

	public void addCombatList(RankListSub combat) {
		this.combatList.add(combat);
	}

	public int getWorshipListNum() {
		return worshipList.size();
	}

	public RankListSub getWorship(int i) {
		return worshipList.get(i);
	}

	public void setWorshipList(RankListSub worship) {

		// 删除已经存在的，加入新排行榜数据，重新排序
		this.rankRemove(this.worshipList, worship.roleId);
		this.worshipList.add(worship);
		this.rankSort(this.worshipList);

		// 排行榜 数量显示数量超出时，删除最后一列
		if (this.getWorshipListNum() > XsgGameParamManager.getInstance()
				.getRankListWorshipAll()) {
			this.worshipList.remove(this.getWorshipListNum() - 1);
		}

	}

	public void addWorshipList(RankListSub worship) {
		this.worshipList.add(worship);
	}

	public int getWorshipMap(String roleId) {
		if (worshipMap.get(roleId) != null) {
			return worshipMap.get(roleId);
		} else {
			return 0;
		}
	}

	public void setWorshipMap(String roleId, int count) {
		this.worshipMap.put(roleId, count);
	}

	public int getFactionListNum() {
		return factionList.size();
	}

	public RankListSub getFaction(int i) {
		return factionList.get(i);
	}

	public void setFactionList(RankListSub faction) {

		if (this.factionList.size() > 0) {
			// 战力和最后一名对比，小于最后一名，显示负数
			// 排行榜数量是否达到要求数量 和 到了数量后，和最后一名的比较
			int showAll = XsgGameParamManager.getInstance()
					.getRankListFactionAll();
			if (factionList.size() < showAll
					|| faction.level >= factionList.get(factionList.size() - 1).level) {
				// 移除已经存在的排行数据
				// 添加新排行数据和排序
				this.rankRemove(this.factionList, faction.roleId);
				this.factionList.add(faction);
				this.rankFactionSort(this.factionList);
				// 排行榜 数量显示数量超出时，删除最后一列
				if (this.factionList.size() > showAll) {
					this.factionList.remove(this.factionList.size() - 1);
				}
			}
		} else {
			this.factionList.add(faction);
		}
	}

	/**
	 * 移除已经存在的排行榜数据
	 * 
	 * @param rankList
	 * @param roleId
	 */
	private void rankRemove(List<RankListSub> rankList, String roleId) {
		for (RankListSub sub : rankList) {
			if (sub.roleId.equals(roleId)) {
				rankList.remove(sub);
				break;
			}
		}
	}

	/**
	 * 排行榜 排序
	 * 
	 * @param rankList
	 */
	private void rankSort(List<RankListSub> rankList) {
		Comparator<RankListSub> comparator = new Comparator<RankListSub>() {
			public int compare(RankListSub s1, RankListSub s2) {
				int order = s2.count - s1.count;
				if (order == 0) {
					order = s2.level - s1.level;
					if (order == 0) {
						order = s2.roleId.compareTo(s1.roleId);
					}
				}

				return order;
			}
		};

		Collections.sort(rankList, comparator);
	}

	/**
	 * 公会排行榜 排序
	 */
	private void rankFactionSort(List<RankListSub> rankList) {
		Comparator<RankListSub> comparator = new Comparator<RankListSub>() {
			public int compare(RankListSub s1, RankListSub s2) {
				int order = s2.level - s1.level;
				if (order == 0) {
					order = s2.count - s1.count;
					if (order == 0) {
						order = s1.roleId.compareTo(s2.roleId);
					}
				}

				return order;
			}
		};

		Collections.sort(rankList, comparator);
	}

	/**
	 * 角色登录显示排行榜数据
	 * 
	 * @return
	 */
	public LoadingRankList LoadingRankList() {
		// 是否充足排行榜显示数据
		this.loadingSum = this.loadingSum > this.loadingMax ? 0
				: ++this.loadingSum;

		int random = this.loadingSum % LoadingRankType.values().length;

		LoadingRankList rankList = new LoadingRankList();
		rankList.type = LoadingRankType.valueOf(random);
		switch (rankList.type) {
		// 竞技场
		case LoadingRankArean:
			rankList.LoadingRank = this.getArenaRankList();
			break;
		// 群雄争霸
		case LoadingLadder:
			rankList.LoadingRank = XsgLadderManager.getInstance()
					.getLadderRankListSub(loadingShowNum);
			break;
		// 大神
		case LoadingRankworship:
			rankList.LoadingRank = this.getRankList(this.worshipList);
			break;
		// 名人堂
		case LoadingRankFame:
			rankList.LoadingRank = this.getFrameRankList();
			break;
		// 战力
		case LoadingRankCombat:
			rankList.LoadingRank = this.getRankList(this.combatList);
			break;
		// 公会
		case LoadingRankFaction:
			rankList.LoadingRank = this.getRankFactionList(this.factionList);
			break;
		}

		return rankList;
	}

	/**
	 * 战力和大神的排行榜
	 * 
	 * @param list
	 * @return
	 */
	private LoadingRankSub[] getRankList(List<RankListSub> list) {
		LoadingRankSub[] LoadingRankArr = null;
		if (list.size() >= loadingShowNum) {
			LoadingRankArr = new LoadingRankSub[loadingShowNum];
			for (int i = 0; i < loadingShowNum; i++) {
				RankListSub sub = list.get(i);

				LoadingRankSub LoadingRank = new LoadingRankSub();
				LoadingRank.roleId = sub.roleId;
				LoadingRank.roleName = sub.roleName;
				LoadingRank.icon = sub.icon;
				LoadingRank.vipLevel = sub.vipLevel;
				LoadingRank.showThing = String.valueOf(sub.count);
				LoadingRankArr[i] = LoadingRank;
			}
		}

		return LoadingRankArr;
	}

	/*
	 * 公会的排行榜
	 */
	private LoadingRankSub[] getRankFactionList(List<RankListSub> list) {
		LoadingRankSub[] LoadingRankArr = null;
		if (list.size() >= loadingShowNum) {
			LoadingRankArr = new LoadingRankSub[loadingShowNum];
			for (int i = 0; i < loadingShowNum; i++) {
				RankListSub sub = list.get(i);

				LoadingRankSub LoadingRank = new LoadingRankSub();
				LoadingRank.roleId = sub.roleId;
				LoadingRank.roleName = sub.roleName;
				LoadingRank.icon = sub.icon;
				LoadingRank.vipLevel = sub.vipLevel;
				LoadingRank.showThing = String.valueOf(sub.level);
				LoadingRankArr[i] = LoadingRank;
			}
		}

		return LoadingRankArr;
	}

	/**
	 * 登录加载显示竞技场排行
	 * 
	 * @param list
	 * @return
	 */
	private LoadingRankSub[] getArenaRankList() {
		LoadingRankSub[] LoadingRankArr = null;
		if (XsgArenaRankManager.getInstance().ArenaRankLevelMap.size() >= loadingShowNum) {
			LoadingRankArr = new LoadingRankSub[loadingShowNum];
			for (int i = 0; i < loadingShowNum; i++) {
				ArenaRank arenaRank = XsgArenaRankManager.getInstance().ArenaRankLevelMap
						.get(i + 1);

				IRole arenaRole = XsgRoleManager.getInstance().findRoleById(
						arenaRank.getRoleId());
				if (arenaRole != null) {
					LoadingRankSub LoadingRank = new LoadingRankSub();
					LoadingRank.roleId = arenaRole.getRoleId();
					LoadingRank.roleName = arenaRole.getName();
					LoadingRank.icon = arenaRole.getHeadImage();
					LoadingRank.vipLevel = arenaRole.getVipLevel();
					LoadingRank.showThing = String
							.valueOf(arenaRole.getLevel());
					LoadingRankArr[i] = LoadingRank;
				} else {
					LoadingRankArr = null;
					break;
				}
			}
		}

		return LoadingRankArr;
	}

	/**
	 * 名人堂
	 * 
	 * @return
	 */
	private LoadingRankSub[] getFrameRankList() {
		LoadingRankSub[] LoadingRankArr = null;
		if (XsgArenaRankManager.getInstance().ArenaRankLevelMap.size() >= loadingShowNum) {
			LoadingRankArr = new LoadingRankSub[loadingShowNum];

			int i = 0;
			for (Property pro : GlobalDataManager.getInstance()
					.getHallOfFameRoleIdListOrderByCount()) {
				if (i >= loadingShowNum)
					break;

				IRole arenaRole = XsgRoleManager.getInstance().findRoleById(
						pro.code);
				if (arenaRole != null) {
					LoadingRankSub LoadingRank = new LoadingRankSub();
					LoadingRank.roleId = arenaRole.getRoleId();
					LoadingRank.roleName = arenaRole.getName();
					LoadingRank.icon = arenaRole.getHeadImage();
					LoadingRank.vipLevel = arenaRole.getVipLevel();
					LoadingRank.showThing = String.valueOf(pro.value);
					LoadingRankArr[i] = LoadingRank;
				} else {
					LoadingRankArr = null;
					break;
				}

				i++;
			}
			if (i < loadingShowNum){
				return null;
			}
		}

		return LoadingRankArr;
	}

	/**
	 * 查询自身排名
	 * 
	 * @param rankList
	 * @param roleId
	 * @return
	 */
	private int selectOwnRank(List<RankListSub> rankList, String roleId) {
		int rankNum = -1;
		for (int i = 0; i < rankList.size(); i++) {
			RankListSub sub = rankList.get(i);
			if (sub.roleId.equals(roleId)) {
				rankNum = i + 1;
				break;
			}
		}
		return rankNum;
	}

	/**
	 * 自身战力排行
	 * 
	 * @param roleId
	 * @return
	 */
	public int selectOwnCombatRank(String roleId) {
		return this.selectOwnRank(this.combatList, roleId);
	}

	/**
	 * 自身成就排行
	 * 
	 * @param roleId
	 * @return
	 */
	public int selectOwnAchieveRank(String roleId) {
		return this.selectOwnRank(this.achieveList, roleId);
	}
	
	/**
	 * 自身大神排行
	 * 
	 * @param roleId
	 * @return
	 */
	public int selectOwnWorshipRank(String roleId) {
		return this.selectOwnRank(this.worshipList, roleId);
	}

	/**
	 * 自身工会
	 * 
	 * @param roleId
	 * @return
	 */
	public int selectOwnFactionRank(String factionId) {
		return this.selectOwnRank(this.factionList, factionId);
	}

	/**
	 * 获取各种排行榜
	 * 
	 * @param type
	 * @return
	 */
	public List<RankListSub> getRankList(int type) {
		int showNum = 0;
		if (type == Const.RankList.COMBAT) {
			showNum = XsgGameParamManager.getInstance().getRankListCombatNum();
			if (showNum > getCombatListNum()) {
				showNum = getCombatListNum();
			}
			return combatList.subList(0, showNum);
		} else if (type == Const.RankList.ACHIEVE) {
			showNum = XsgGameParamManager.getInstance().getRankListAchieveNum();
			if (showNum > getAchieveListNum()) {
				showNum = getAchieveListNum();
			}
			return achieveList.subList(0, showNum);
		}else if (type == Const.RankList.FACTION) {
			showNum = XsgGameParamManager.getInstance().getRankListFactionNum();
			if (showNum > getFactionListNum()) {
				showNum = getFactionListNum();
			}
			return factionList.subList(0, showNum);
		} else if (type == Const.RankList.WORSHIP) {
			showNum = XsgGameParamManager.getInstance().getRankListWorshipNum();
			if (showNum > getWorshipListNum()) {
				showNum = getWorshipListNum();
			}
			return worshipList.subList(0, showNum);
		}
		return new ArrayList<RankListSub>();
	}
}
