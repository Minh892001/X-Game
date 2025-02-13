package com.morefun.XSanGo.crossServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.XSanGo.Protocol.StageIndex;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 比武大会
 * 
 * @author guofeng.qin
 */
public class XsgTournamentManager {
	private static XsgTournamentManager instance = new XsgTournamentManager();

	private TournamentConfigT tournamentConfigT;
	private TournamentAreaConfigT tournamentAreaConfigT;
	private Map<Integer, TournamentRefreshCostT> refreshCostMap = new HashMap<Integer, TournamentRefreshCostT>();
	private Map<Integer, TournamentFightCostT> fightCostMap = new HashMap<Integer, TournamentFightCostT>();
	private Map<Integer, TournamentRewardT> rewardMap = new HashMap<Integer, TournamentRewardT>();
	/**
	 * 积分奖励
	 */
	private Map<Integer, TournamentScoreRewardT> scoreRewardMap = new HashMap<Integer, TournamentScoreRewardT>();
	private Map<StageIndex, StageDesc> stageIndexMap = new HashMap<StageIndex, StageDesc>();
	private OpenLevelConfigT openConfigT;
	private static final String TournamentOpenLevelKey = "biwudahui";
	private Map<String, TournamentShopT> shopItemMap = new HashMap<String, TournamentShopT>();

	private XsgTournamentManager() {
		initStageIndexMap();
		loadTournamentOpenScript();
//		loadTournamentScript();
	}

	private void initStageIndexMap() {
		stageIndexMap.put(StageIndex.NotStart, new StageDesc(-1, Messages.getString("XsgTournamentManager.notStart")));
		stageIndexMap.put(StageIndex.SignUp, new StageDesc(-1, Messages.getString("XsgTournamentManager.signupstr")));
		stageIndexMap.put(StageIndex.QT, new StageDesc(7, Messages.getString("XsgTournamentManager.qtStage")));
		stageIndexMap.put(StageIndex.S32, new StageDesc(6, Messages.getString("XsgTournamentManager.s32Stage")));
		stageIndexMap.put(StageIndex.S16, new StageDesc(5, Messages.getString("XsgTournamentManager.s16Stage")));
		stageIndexMap.put(StageIndex.S8, new StageDesc(4, Messages.getString("XsgTournamentManager.s8Stage")));
		stageIndexMap.put(StageIndex.S4, new StageDesc(3, Messages.getString("XsgTournamentManager.s2Stage")));
		stageIndexMap.put(StageIndex.S2, new StageDesc(2, Messages.getString("XsgTournamentManager.s1Stage")));
		stageIndexMap.put(StageIndex.End, new StageDesc(1, Messages.getString("XsgTournamentManager.send")));
	}
	
	public void loadTournamentOpenScript() {
		// 开放等级
		List<OpenLevelConfigT> openLevelConfigs = ExcelParser.parse(OpenLevelConfigT.class);
		if (openLevelConfigs != null) {
			for (OpenLevelConfigT cfg : openLevelConfigs) {
				if (TournamentOpenLevelKey.equals(cfg.id)) {
					openConfigT = cfg;
					break;
				}
			}
		}
	}

	public void loadTournamentScript() {
		// 参数配置
		List<TournamentConfigT> list = ExcelParser.parse(TournamentConfigT.class);
		if (list != null && list.size() > 0) {
			tournamentConfigT = list.get(0);
		}
		// 跨服配置
		List<TournamentAreaConfigT> areaList = ExcelParser.parse(TournamentAreaConfigT.class);
		if (areaList != null) {
			tournamentAreaConfigT = null;
			for (TournamentAreaConfigT cfg : areaList) {
				if (TextUtil.isNotBlank(cfg.server)) {
					Set<String> serverSet = new HashSet<String>(Arrays.asList(cfg.server.split(",")));
					if (serverSet.contains(ServerLancher.getServerId() + "")) {
						tournamentAreaConfigT = cfg;
						break;
					}
				}
			}
		}
		// 刷新消耗
		List<TournamentRefreshCostT> refreshCostList = ExcelParser.parse(TournamentRefreshCostT.class);
		if (refreshCostList != null) {
			refreshCostMap.clear();
			for (TournamentRefreshCostT t : refreshCostList) {
				refreshCostMap.put(t.count, t);
			}
		}
		// 战斗次数购买消耗
		List<TournamentFightCostT> fightCostList = ExcelParser.parse(TournamentFightCostT.class);
		if (fightCostList != null) {
			fightCostMap.clear();
			for (TournamentFightCostT t : fightCostList) {
				fightCostMap.put(t.count, t);
			}
		}
		// 奖励
		List<TournamentRewardT> rewardList = ExcelParser.parse(TournamentRewardT.class);
		if (rewardList != null) {
			rewardMap.clear();
			for (TournamentRewardT reward : rewardList) {
				rewardMap.put(reward.type, reward);
			}
		}
		List<TournamentScoreRewardT> scoreList = ExcelParser.parse(TournamentScoreRewardT.class);
		for (TournamentScoreRewardT s : scoreList) {
			scoreRewardMap.put(s.score, s);
		}
		
		// 竞技场商城
		List<TournamentShopT> shopItemList = ExcelParser.parse(TournamentShopT.class);
		if (shopItemList != null) {
			shopItemMap.clear();
			for (TournamentShopT shopT : shopItemList) {
				shopItemMap.put(shopT.id, shopT);
			}
		}
		
		XsgTournamentBetManager.getInstance().clearOldData(tournamentConfigT.stageIndex);
	}

	public OpenLevelConfigT getOpenConfigT() {
		return openConfigT;
	}

	public TournamentConfigT getConfig() {
		return tournamentConfigT;
	}

	public TournamentAreaConfigT getAreaConfig() {
		return tournamentAreaConfigT;
	}

	public TournamentRefreshCostT getRefreshCost(int count) {
		return refreshCostMap.get(count);
	}

	public TournamentFightCostT getFightCost(int count) {
		return fightCostMap.get(count);
	}

	public static XsgTournamentManager getInstance() {
		return instance;
	}

	public String getStageIndexDesc(StageIndex index) {
		return stageIndexMap.get(index).desc;
	}

	public TournamentController createTournamentController(IRole rt, Role db) {
		return new TournamentController(rt, db);
	}

	public TournamentRewardT getReward(int rank) {
		TournamentRewardT rewardT = rewardMap.get(rank);
		return rewardT;
	}

	public TournamentScoreRewardT getScoreReward(int score) {
		TournamentScoreRewardT rewardT = scoreRewardMap.get(score);
		return rewardT;
	}

	public TournamentShopT getShopItem(String id) {
		return shopItemMap.get(id);
	}

	public List<TournamentShopT> getShopItems() {
		return new ArrayList<TournamentShopT>(shopItemMap.values());
	}

	/**
	 * 发送押注成功邮件
	 * */
	public void sendBetSuccessMail(String accepterId, Map<String, Integer> rewardMap) {
		sendRoleMail(MailTemplate.TournamentBetSuccess, accepterId, null, rewardMap);
	}

	/**
	 * 发送押注失败邮件
	 * */
	public void sendBetFailureMail(String accepterId, Map<String, Integer> rewardMap) {
		sendRoleMail(MailTemplate.TournamentBetFailure, accepterId, null, rewardMap);
	}

	/**
	 * 发送比武大会奖励邮件
	 * */
	public void sendTournamentMail(String accepterId, int num, int rank, Map<String, Integer> rewardMap) {
		if (rank == 1) {// 冠军
			sendChampionMail(accepterId, num, rewardMap);
		} else {
			sendKnockOutMail(accepterId, String.valueOf(rank), rewardMap);
		}
	}

	/**
	 * 发送冠军奖励邮件
	 * */
	private void sendChampionMail(String accepterId, int num, Map<String, Integer> rewardMap) {
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("$m", num + "");

		sendRoleMail(MailTemplate.TournamentChampion, accepterId, replaceMap, rewardMap);
	}

	/**
	 * 发送排名奖励
	 * */
	private void sendKnockOutMail(String accepterId, String rank, Map<String, Integer> rewardMap) {
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("$a", rank);
		sendRoleMail(MailTemplate.TournamentKnockOut, accepterId, replaceMap, rewardMap);
	}

	/** 发送积分奖励 */
	public void sendScoreMail(String accepterId, int score, Map<String, Integer> rewardMap) {
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("$e", String.valueOf(score));
		sendRoleMail(MailTemplate.TournamentScore, accepterId, replaceMap, rewardMap);
	}

	/**
	 * 发送邮件
	 * */
	private void sendRoleMail(MailTemplate templateId, String accepterId, Map<String, String> replaceMap,
			Map<String, Integer> rewardMap) {
		XsgMailManager.getInstance().sendTemplate(accepterId, templateId, rewardMap, replaceMap);
	}

	/** 比武大会阶段的描述和奖励脚本中的类型字段映射 */
	public static final class StageDesc {
		public int rewardType;
		public String desc;

		public StageDesc(int rewardType, String desc) {
			this.rewardType = rewardType;
			this.desc = desc;
		}
	}
}
