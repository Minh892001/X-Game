/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandController
 * 功能描述：
 * 文件名：DreamlandController.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.XSanGo.Protocol.DreamlandAwardUnit;
import com.XSanGo.Protocol.DreamlandAwardView;
import com.XSanGo.Protocol.DreamlandRankUnit;
import com.XSanGo.Protocol.DreamlandRankView;
import com.XSanGo.Protocol.DreamlandSceneAwardResult;
import com.XSanGo.Protocol.DreamlandShopItemUnit;
import com.XSanGo.Protocol.DreamlandShopItemView;
import com.XSanGo.Protocol.DreamlandShow;
import com.XSanGo.Protocol.DreamlandSweepResult;
import com.XSanGo.Protocol.DreamlandSweepUnit;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SceneStateUnit;
import com.XSanGo.Protocol.SceneStateUnitGroup;
import com.google.gson.reflect.TypeToken;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDreamland;
import com.morefun.XSanGo.event.protocol.IDreamlandBegin;
import com.morefun.XSanGo.event.protocol.IDreamlandBuyChallengeNum;
import com.morefun.XSanGo.event.protocol.IDreamlandBuyShopItem;
import com.morefun.XSanGo.event.protocol.IDreamlandEnd;
import com.morefun.XSanGo.event.protocol.IDreamlandRefreshScene;
import com.morefun.XSanGo.event.protocol.INanHuaLingChange;
import com.morefun.XSanGo.event.protocol.IDreamlandRefreshShop;
import com.morefun.XSanGo.event.protocol.IDreamlandStarAward;
import com.morefun.XSanGo.event.protocol.IDreamlandSweep;
import com.morefun.XSanGo.event.protocol.IRoleHeadAndBorderChange;
import com.morefun.XSanGo.event.protocol.IRoleNameChange;
import com.morefun.XSanGo.event.protocol.IVipLevelUp;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.PVEMovieParam;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 南华幻境处理实现类
 * 
 * @author weiyi.zhao
 * @since 2016-4-19
 * @version 1.0
 */
public class DreamlandController implements IDreamlandController, IVipLevelUp, IRoleNameChange, IRoleHeadAndBorderChange {

	/** 角色接口 */
	private IRole roleRt;

	/** 角色数据对象 */
	private Role roleDB;

	/** 战报上下文 */
	private String fightMovieIdContext;

	/** 战斗开始事件 */
	private IDreamlandBegin eventBegin;

	/** 战斗结束事件 */
	private IDreamlandEnd eventEnd;

	/** 扫荡事件 */
	private IDreamlandSweep eventSweep;

	/** 星数奖励领取 */
	private IDreamlandStarAward eventStarAward;

	/** 关卡场景刷新 */
	private IDreamlandRefreshScene eventRefreshScene;

	/** 商店刷新 */
	private IDreamlandRefreshShop eventRefreshShop;

	/** 商品购买 */
	private IDreamlandBuyShopItem eventBuyShopItem;

	/** 南华令变更 */
	private INanHuaLingChange eventNHLChange;

	/** 每日挑战次数购买 */
	private IDreamlandBuyChallengeNum eventChallengeNum;

	/**
	 * 构造函数
	 * 
	 * @param roleRt
	 * @param roleDB
	 */
	public DreamlandController(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		// 事件注册
		this.eventBegin = this.roleRt.getEventControler().registerEvent(IDreamlandBegin.class);
		this.eventEnd = this.roleRt.getEventControler().registerEvent(IDreamlandEnd.class);
		this.eventSweep = this.roleRt.getEventControler().registerEvent(IDreamlandSweep.class);
		this.eventStarAward = this.roleRt.getEventControler().registerEvent(IDreamlandStarAward.class);
		this.eventRefreshScene = this.roleRt.getEventControler().registerEvent(IDreamlandRefreshScene.class);
		this.eventRefreshShop = this.roleRt.getEventControler().registerEvent(IDreamlandRefreshShop.class);
		this.eventBuyShopItem = this.roleRt.getEventControler().registerEvent(IDreamlandBuyShopItem.class);
		this.eventNHLChange = this.roleRt.getEventControler().registerEvent(INanHuaLingChange.class);
		this.eventChallengeNum = this.roleRt.getEventControler().registerEvent(IDreamlandBuyChallengeNum.class);

		// 句柄注册
		this.roleRt.getEventControler().registerHandler(IVipLevelUp.class, this);
		this.roleRt.getEventControler().registerHandler(IRoleNameChange.class, this);
		this.roleRt.getEventControler().registerHandler(IRoleHeadAndBorderChange.class, this);
	}

	/**
	 * 重置数据
	 */
	private void resetRoleDreamland() {
		RoleDreamland rd = getRoleDreamland(false);
		Date joinTime = DateUtil.joinTime(XsgDreamlandManager.getInstance().getConfig().resetTime);
		if (rd == null || rd.getLastRefreshTime() == null || DateUtil.isPass(joinTime, rd.getLastRefreshTime())) {
			rd = getRoleDreamland(true);
			String lastRefreshTime = rd == null ? "" : (rd.getLastRefreshTime() == null ? "" : DateUtil.format(rd.getLastRefreshTime()));
			this.eventRefreshScene.onRefreshScene(rd.getLastChallengeSceneId(), lastRefreshTime);

			rd.setChallengeNum(0);
			rd.setBuyNum(0);
			// rd.setTodayScenePlan(null);
			rd.setLastRefreshTime(new Date());
		}
	}

	/**
	 * 获得南华幻境数据结构
	 * 
	 * @param isInit 是否需要初始化
	 * @return
	 */
	private RoleDreamland getRoleDreamland(boolean isInit) {
		RoleDreamland rd = roleDB.getRoleDreamland();
		if (isInit && rd == null) {
			rd = new RoleDreamland();
			rd.setRole(roleDB);
			rd.setRole_id(roleDB.getId());
			roleDB.setRoleDreamland(rd);
		}
		return rd;
	}

	@Override
	public void addNanHuaLing(int value) {
		if (value == 0) {
			return;
		}
		RoleDreamland rd = getRoleDreamland(true);
		int old = rd.getNanHuaLing();
		rd.setNanHuaLing(rd.getNanHuaLing() + value);

		// 通知客户端刷新显示
		this.roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.NanHuaLing, rd.getNanHuaLing());
		// 触发事件
		this.eventNHLChange.onNanHuaLingChange(value, old, rd.getNanHuaLing());
	}

	@Override
	public int getNanHuaLing() {
		RoleDreamland rd = getRoleDreamland(false);
		return rd == null ? 0 : rd.getNanHuaLing();
	}

	// /**
	// * 获得关卡每日进度
	// *
	// * @return
	// */
	// private List<Integer> parseTodayScenePlan() {
	// RoleDreamland rd = getRoleDreamland(false);
	// if (rd == null || TextUtil.isBlank(rd.getTodayScenePlan())) {
	// return new ArrayList<Integer>();
	// }
	// return TextUtil.GSON.fromJson(rd.getTodayScenePlan(), new
	// TypeToken<List<Integer>>() {
	// }.getType());
	// }

	/**
	 * 获得关卡进度对象
	 * 
	 * @return
	 */
	private Map<Integer, Byte> parseScenePlan() {
		RoleDreamland rd = getRoleDreamland(false);
		if (rd == null || TextUtil.isBlank(rd.getScenePlan())) {
			return new LinkedHashMap<Integer, Byte>();
		}
		return TextUtil.GSON.fromJson(rd.getScenePlan(), new TypeToken<LinkedHashMap<Integer, Byte>>() {
		}.getType());
	}

	@Override
	public DreamlandShow dreamlandPage(int groupId) throws NoteException {
		resetRoleDreamland();

		int sceneId = groupId > 0 ? getGroupFirstSceneId(groupId) : getStartSceneId();
		return createDreamlandShow(sceneId);
	}

	/**
	 * 是否可以切换到下一个关卡集
	 * 
	 * @param sceneId
	 * @return
	 */
	private boolean isNextGroup(int sceneId) {
		DreamlandSceneT sceneT = XsgDreamlandManager.getInstance().getScenes(sceneId);
		if (sceneT.postGroup < 1) {
			return false;
		}
		return isFullStar(sceneT.sceneGroupId);
	}

	/**
	 * 指定关卡集是否全满星
	 * 
	 * @param groupId
	 * @return
	 */
	private boolean isFullStar(int groupId) {
		List<Integer> groupList = XsgDreamlandManager.getInstance().getGroup(groupId);
		Map<Integer, Byte> scenePlan = parseScenePlan();
		for (int sid : groupList) {
			if (!scenePlan.containsKey(sid)) {
				return false;
			}
			byte star = scenePlan.get(sid);
			if (star != XsgDreamlandManager.FullStar) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否存在奖励红点
	 * 
	 * @return
	 */
	private boolean isRedPoint() {
		RoleDreamland rd = getRoleDreamland(false);
		// 获得总星级
		int allStarNum = rd == null ? 0 : rd.getStarNum();
		// 已领取的星级列表
		List<Integer> starAwardList = parseStarAward();

		Map<Integer, String> starAwards = XsgDreamlandManager.getInstance().getStarAwards();
		boolean isDraw = false;
		for (int star : starAwards.keySet()) {
			if (star > allStarNum) {
				continue;
			}
			if (starAwardList != null && starAwardList.contains(star)) {
				continue;
			}
			isDraw = true;
		}
		return isDraw;
	}

	/**
	 * 获取当前可开始的关卡
	 * 
	 * @return
	 */
	private int getStartSceneId() {
		RoleDreamland rd = getRoleDreamland(false);
		if (rd == null || rd.getLastChallengeSceneId() == 0) {
			return XsgDreamlandManager.getInstance().getFirstSceneId();
		}
		DreamlandSceneT sceneT = XsgDreamlandManager.getInstance().getScenes(rd.getLastChallengeSceneId());
		if (sceneT.postSceneId < 1) {// 无后置关卡，需要确认是否存在后置关卡集
			if (sceneT.postGroup < 1) {// 无后置关关卡集，默认最后挑战的关卡
				return rd.getLastChallengeSceneId();
			} else {// 存在后置关卡集，验证当前关卡集是否满星通关
				if (isFullStar(sceneT.sceneGroupId)) {
					return getGroupFirstSceneId(sceneT.postGroup);
				}
				return rd.getLastChallengeSceneId();
			}
		}
		return sceneT.postSceneId;
	}

	/**
	 * 指定图集的视图信息
	 * 
	 * @param sceneId
	 * @return
	 */
	private DreamlandShow createDreamlandShow(int sceneId) {
		DreamlandShow show = new DreamlandShow();
		show.curSceneId = getStartSceneId();
		DreamlandSceneT sceneT = XsgDreamlandManager.getInstance().getScenes(sceneId);
		// 获取当前图集的关卡
		SceneStateUnit[] states = createSceneStateUnitView(sceneT.sceneGroupId, show.curSceneId);
		show.group = new SceneStateUnitGroup(states, isNextGroup(sceneId), sceneT.preGroup > 0);
		show.groupId = sceneT.sceneGroupId;

		RoleDreamland rd = getRoleDreamland(false);
		show.buyNum = rd == null ? 0 : rd.getBuyNum();
		show.surplusChallengeNum = XsgGameParamManager.getInstance().getDreamlandChallengeNum() - (rd == null ? 0 : rd.getChallengeNum()) + show.buyNum;
		show.isRedPoint = isRedPoint();
		return show;
	}

	/**
	 * 指定图集的关卡视图信息
	 * 
	 * @param groupId
	 * @param selectedSceneId 选中的关卡编号
	 * @return
	 */
	private SceneStateUnit[] createSceneStateUnitView(int groupId, int selectedSceneId) {
		Map<Integer, Byte> scenePlan = parseScenePlan();
		// 需求变更 单日单关卡不限次数 2016-05-23
		// List<Integer> todayScenePlan = parseTodayScenePlan();
		List<Integer> sceneIdList = XsgDreamlandManager.getInstance().getGroup(groupId);
		SceneStateUnit[] states = null;
		for (int sceneId : sceneIdList) {
			boolean isChallenge = true;// !todayScenePlan.contains(sceneId);
			byte star = 0;
			if (scenePlan.containsKey(sceneId)) {
				star = scenePlan.get(sceneId);
			} else {
				if (selectedSceneId != sceneId) {// 非当前选中关卡默认不可挑战
					isChallenge = false;
				}
			}
			boolean isSweep = isChallenge ? (star == XsgDreamlandManager.FullStar) : false;
			SceneStateUnit state = new SceneStateUnit(sceneId, star, isChallenge, isSweep);
			states = (SceneStateUnit[]) ArrayUtils.add(states, state);
		}
		return states;
	}

	@Override
	public DreamlandShow dreamlandSwitchSceneGroup(int groupId, boolean isFront) throws NoteException {
		resetRoleDreamland();
		if (isFront) {// 向前
			if (!isFullStar(groupId)) {
				throw new NoteException(Messages.getString("DreamlandController.scene.notCanSwitch"));
			}
		}
		int targetGroupId = getPreOrPostGroupId(groupId, isFront);
		if (targetGroupId == 0) {// 如无目标关卡集，默认为当前
			targetGroupId = groupId;
		}
		int firstSceneId = getGroupFirstSceneId(targetGroupId);
		return createDreamlandShow(firstSceneId);
	}

	/**
	 * 获取向前或者向后的关卡集ID
	 * 
	 * @param groupId
	 * @param isFront
	 * @return
	 */
	private int getPreOrPostGroupId(int groupId, boolean isFront) {
		int firstSceneId = getGroupFirstSceneId(groupId);
		DreamlandSceneT sceneT = XsgDreamlandManager.getInstance().getScenes(firstSceneId);
		if (isFront) {
			return sceneT.postGroup;
		} else {
			return sceneT.preGroup;
		}
	}

	/**
	 * 获取关卡图集的第一个关卡编号
	 * 
	 * @param groupId
	 * @return
	 */
	private int getGroupFirstSceneId(int groupId) {
		List<Integer> sceneIdList = XsgDreamlandManager.getInstance().getGroup(groupId);
		return sceneIdList.get(0);
	}

	/**
	 * 战斗检测
	 * 
	 * @param sceneId
	 * @param isSweep 是否扫荡
	 * @throws NoteException
	 */
	private void checkBattle(int sceneId, boolean isSweep) throws NoteException {
		DreamlandSceneT sceneT = XsgDreamlandManager.getInstance().getScenes(sceneId);
		if (sceneT == null) {
			throw new NoteException(Messages.getString("DreamlandController.scene.notAttack"));
		}
		// 验证挑战次数
		RoleDreamland rd = getRoleDreamland(false);
		int allChallengeNum = XsgGameParamManager.getInstance().getDreamlandChallengeNum();
		int challengeNum = 0;
		if (rd != null) {
			allChallengeNum += rd.getBuyNum();
			challengeNum = rd.getChallengeNum();
		}
		if (challengeNum >= allChallengeNum) {// 挑战次数不足
			throw new NoteException(Messages.getString("DreamlandController.scene.ChallengeNum.NotEnough"));
		}
		// 需求变更 单日单关卡不限次数 2016-05-23
		// List<Integer> todayScenePlan = parseTodayScenePlan();
		// if (todayScenePlan.contains(sceneId)) {// 今天以挑战过了
		// throw new
		// NoteException(Messages.getString("DreamlandController.scene.AlreadyChallenge"));
		// }
		if (!isSweep) {
			if (sceneT.preSceneId > 0) {// 存在前置关卡
				DreamlandSceneT previousSceneT = XsgDreamlandManager.getInstance().getScenes(sceneT.preSceneId);
				if (previousSceneT.sceneGroupId == sceneT.sceneGroupId) {// 同一关卡组，只需验证前置是否打过
					Map<Integer, Byte> scenePlan = parseScenePlan();
					if (!scenePlan.containsKey(sceneT.preSceneId)) {// 前置关卡未打
						throw new NoteException(Messages.getString("DreamlandController.scene.preNotThrough"));
					}
				} else {// 不同关卡集，前置关卡集是否满星通关
					if (!isFullStar(sceneT.preGroup)) {
						throw new NoteException(Messages.getString("DreamlandController.scene.BackFullStar"));
					}
				}
			} else {
				if (sceneT.preGroup > 0) {// 前置关卡集是否满星通关
					if (!isFullStar(sceneT.preGroup)) {
						throw new NoteException(Messages.getString("DreamlandController.scene.BackFullStar"));
					}
				}
			}
		} else {
			Map<Integer, Byte> scenePlan = parseScenePlan();
			int star = scenePlan.containsKey(sceneId) ? scenePlan.get(sceneId) : 0;
			if (star != XsgDreamlandManager.FullStar) {// 五星扫荡
				throw new NoteException(Messages.getString("DreamlandController.scene.notSweep"));
			}
		}
	}

	@Override
	public String beginDreamland(int sceneId) throws NoteException {
		resetRoleDreamland();
		checkBattle(sceneId, false);

		PVEMovieParam param = new PVEMovieParam("" + sceneId, "");
		fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(XsgFightMovieManager.Type.Dreamland, this.roleRt, param);
		// 事件触发
		this.eventBegin.onBeginDreamland(sceneId, fightMovieIdContext);
		return fightMovieIdContext;
	}

	@Override
	public DreamlandSceneAwardResult endDreamland(int sceneId, byte remainHero) throws NoteException {
		resetRoleDreamland();
		checkBattle(sceneId, false);
		if (TextUtil.isBlank(fightMovieIdContext)) {
			throw new NoteException(Messages.getString("DreamlandController.scene.error"));
		}
		// 计算星级
		IFormation formation = roleRt.getFormationControler().getDefaultFormation();
		byte heroCount = formation.getHeroCountIncludeSupport();
		byte star = XsgCopyManager.getInstance().calculateStar(heroCount, remainHero);
		// 战斗胜利
		int challengeNum = 0;
		DreamlandSceneAwardResult result = new DreamlandSceneAwardResult();
		if (remainHero > 0 && star > 0) {
			RoleDreamland rd = getRoleDreamland(true);
			rd.setUpdateTime(new Date());
			// 设置进度数据
			Map<Integer, Byte> scenePlan = parseScenePlan();
			int curStar = 0;
			if (scenePlan.containsKey(sceneId)) {
				curStar = scenePlan.get(sceneId);
			} else {
				rd.setLastChallengeSceneId(sceneId);
			}
			if (curStar < star) {// 刷新星级
				scenePlan.put(sceneId, star);
				rd.setScenePlan(TextUtil.GSON.toJson(scenePlan));
				rd.setStarNum(rd.getStarNum() - curStar + star);
			}
			rd.setLayerNum(scenePlan.size());
			// 刷新排行数据
			XsgDreamlandManager.getInstance().setRank(roleRt, rd, true);
			// 计算胜利奖励
			result = calculateStarAward(sceneId, star);
			// 记录挑战次数
			rd.setChallengeNum(rd.getChallengeNum() + 1);
			// 记录今日挑战记录
			// 需求变更 单日单关卡不限次数 2016-05-23
			// List<Integer> todayScenePlan = parseTodayScenePlan();
			// todayScenePlan.add(sceneId);
			// rd.setTodayScenePlan(TextUtil.GSON.toJson(todayScenePlan));
			challengeNum = rd.getChallengeNum();
		} else {
			RoleDreamland rd = getRoleDreamland(false);
			challengeNum = rd == null ? 0 : rd.getChallengeNum();
		}
		// 设置战斗结果
		if (TextUtil.isNotBlank(fightMovieIdContext)) {
			XsgFightMovieManager.getInstance().endFightMovie(roleRt.getRoleId(), fightMovieIdContext, remainHero > 0 ? 1 : 0, remainHero);
		}
		// 事件触发
		this.eventEnd.onEndDreamland(sceneId, heroCount, remainHero, star, challengeNum, TextUtil.GSON.toJson(result.awardItems));
		return result;
	}

	/**
	 * 计算指定星级的奖励
	 * 
	 * @param sceneId
	 * @param star
	 * @return
	 */
	private DreamlandSceneAwardResult calculateStarAward(int sceneId, int star) {
		DreamlandSceneT sceneT = XsgDreamlandManager.getInstance().getScenes(sceneId);
		String tc = star <= 3 ? sceneT.chestTC3 : (star == 4 ? sceneT.chestTC4 : sceneT.chestTC5);
		TcResult tcResult = XsgRewardManager.getInstance().doTc(roleRt, tc);
		// 奖励发放 并生成返回
		DreamlandSceneAwardResult resultView = new DreamlandSceneAwardResult();
		resultView.star = star;
		for (Entry<String, Integer> item : tcResult) {
			roleRt.getRewardControler().acceptReward(item.getKey(), item.getValue());
			resultView.awardItems = (IntString[]) ArrayUtils.add(resultView.awardItems, new IntString(item.getValue(), item.getKey()));
		}
		return resultView;
	}

	@Override
	public DreamlandSweepResult dreamlandSweep(int sceneId) throws NoteException {
		resetRoleDreamland();
		checkBattle(sceneId, true);
		DreamlandSceneT sceneT = XsgDreamlandManager.getInstance().getScenes(sceneId);
		// 计算五星扫荡奖励
		DreamlandSweepResult result = new DreamlandSweepResult();
		// int stopSceneId = 0;
		// int endSceneId = 0;// 结束挑战关卡
		// while (star == XsgDreamlandManager.FullStar) {
		// 计算奖励
		// endSceneId = sceneT.sceneId;
		DreamlandSceneAwardResult awardResult = calculateStarAward(sceneT.sceneId, XsgDreamlandManager.FullStar);
		result.resultUnits = (DreamlandSweepUnit[]) ArrayUtils.add(result.resultUnits, new DreamlandSweepUnit(sceneT.sceneId, XsgDreamlandManager.FullStar, awardResult.awardItems));
		// 递归参数初始化
		// if (sceneT.postSceneId > 0) {
		// star = scenePlan.containsKey(sceneT.postSceneId) ?
		// scenePlan.get(sceneT.postSceneId) : 0;
		// sceneT =
		// XsgDreamlandManager.getInstance().getScenes(sceneT.postSceneId);
		// stopSceneId = sceneT.sceneId;
		// } else {
		// star = 0;
		// }
		// }
		result.stopSceneId = sceneId;
		RoleDreamland rd = getRoleDreamland(true);
		// 记录挑战次数
		rd.setChallengeNum(rd.getChallengeNum() + 1);
		// 记录今日挑战记录
		// 需求变更 单日单关卡不限次数 2016-05-23
		// List<Integer> todayScenePlan = parseTodayScenePlan();
		// todayScenePlan.add(sceneId);
		// rd.setTodayScenePlan(TextUtil.GSON.toJson(todayScenePlan));
		// 触发事件
		this.eventSweep.onSweepDreamland(sceneId, rd.getChallengeNum(), TextUtil.GSON.toJson(awardResult.awardItems));
		return result;
	}

	@Override
	public DreamlandRankView lookDreamlandRank() throws NoteException {
		List<DreamlandRank> rankList = XsgDreamlandManager.getInstance().getRankList();
		DreamlandRankUnit[] ranks = null;
		// 自身排行数据
		DreamlandRankUnit selfUnit = null;
		for (int i = 0; i < rankList.size(); i++) {
			DreamlandRank rank = rankList.get(i);
			rank.getRankUnit().rank = i + 1;
			if (rank.getRoleId().equals(roleRt.getRoleId())) {
				selfUnit = rank.getRankUnit();
			}
			ranks = (DreamlandRankUnit[]) ArrayUtils.add(ranks, rank.getRankUnit());
		}
		if (selfUnit == null) {// 未进入排行榜
			RoleDreamland rd = getRoleDreamland(false);
			int starNum = rd == null ? 0 : rd.getStarNum();
			int layerNum = rd == null ? 0 : rd.getLayerNum();
			selfUnit = new DreamlandRankUnit(101, roleRt.getRoleId(), roleRt.getName(), roleRt.getHeadImage(), roleRt.getVipLevel(), roleRt.getLevel(), starNum, layerNum);
		}
		return new DreamlandRankView(ranks, selfUnit);
	}

	/**
	 * 获得关卡星数奖励对象
	 * 
	 * @return
	 */
	private List<Integer> parseStarAward() {
		RoleDreamland rd = getRoleDreamland(false);
		if (rd == null) {
			return null;
		}
		return TextUtil.GSON.fromJson(rd.getStarAward(), new TypeToken<List<Integer>>() {
		}.getType());
	}

	@Override
	public DreamlandAwardView dreamlandAwardPage() throws NoteException {
		List<Integer> starAwardList = parseStarAward();
		Map<Integer, String> starAwards = XsgDreamlandManager.getInstance().getStarAwards();

		// 星数奖励视图
		DreamlandAwardUnit[] awardUnits = null;
		for (int star : starAwards.keySet()) {
			boolean isDraw = starAwardList == null ? false : starAwardList.contains(star);
			awardUnits = (DreamlandAwardUnit[]) ArrayUtils.add(awardUnits, new DreamlandAwardUnit(star, isDraw));
		}
		// 总星数
		RoleDreamland rd = getRoleDreamland(false);
		int allStarNum = rd == null ? 0 : rd.getStarNum();
		return new DreamlandAwardView(allStarNum, awardUnits);
	}

	@Override
	public IntString[] drawStarAward(int star) throws NoteException {
		RoleDreamland rd = getRoleDreamland(false);
		int allStarNum = rd == null ? 0 : rd.getStarNum();
		if (star > allStarNum) {// 星数未达到
			throw new NoteException(Messages.getString("DreamlandController.star.notEnough"));
		}
		String awardItems = XsgDreamlandManager.getInstance().getStarAward(star);
		if (TextUtil.isBlank(awardItems)) {
			throw new NoteException(Messages.getString("DreamlandController.star.notEnough"));
		}
		List<Integer> starAwardList = parseStarAward();
		if (starAwardList != null && starAwardList.contains(star)) {
			throw new NoteException(Messages.getString("DreamlandController.star.draw"));
		}

		IntString[] itemAwards = null;
		// 计算奖励获得
		for (String items : StringUtils.split(awardItems, ";")) {
			String[] item = StringUtils.split(items, ":");
			roleRt.getRewardControler().acceptReward(item[0], Integer.parseInt(item[1]));
			itemAwards = (IntString[]) ArrayUtils.add(itemAwards, new IntString(Integer.parseInt(item[1]), item[0]));
		}
		// 领取进度存档
		if (starAwardList == null) {
			starAwardList = new ArrayList<Integer>();
		}
		starAwardList.add(star);
		rd = getRoleDreamland(true);
		rd.setStarAward(TextUtil.GSON.toJson(starAwardList));
		// 事件触发
		this.eventStarAward.onDrawStarAward(star, TextUtil.GSON.toJson(itemAwards));
		return itemAwards;
	}

	/**
	 * 商店刷新后的物品列表
	 * 
	 * @return
	 */
	private Map<Integer, Byte> doRefreshShopList() {
		Map<Integer, Byte> itemMaps = new LinkedHashMap<Integer, Byte>();
		// 固定物品列表
		List<DreamlandShopFixedT> fixedList = XsgDreamlandManager.getInstance().calculateShopFixedList();
		for (DreamlandShopFixedT item : fixedList) {
			itemMaps.put(item.id, (byte) 0);
		}
		// 随机物品列表
		List<DreamlandShopRandomT> randomList = XsgDreamlandManager.getInstance().calculateShopRandomList();
		for (DreamlandShopRandomT item : randomList) {
			itemMaps.put(item.id, (byte) 0);
		}
		// 数据刷新存档
		RoleDreamland rd = getRoleDreamland(true);
		rd.setShopItems(TextUtil.GSON.toJson(itemMaps));
		return itemMaps;
	}

	/**
	 * 获得关卡商店兑换物品列表对象
	 * 
	 * @return
	 */
	private Map<Integer, Byte> parseShopItems() {
		RoleDreamland rd = getRoleDreamland(false);
		if (rd == null) {
			return null;
		}
		return TextUtil.GSON.fromJson(rd.getShopItems(), new TypeToken<LinkedHashMap<Integer, Byte>>() {
		}.getType());
	}

	/**
	 * 重置商店兑换数据
	 */
	private void resetRoleDreamlandShop() {
		RoleDreamland rd = getRoleDreamland(false);
		Date joinTime = DateUtil.joinTime(XsgDreamlandManager.getInstance().getConfig().refreshTime);
		if (rd == null || rd.getLastShopRefreshTime() == null || DateUtil.isPass(joinTime, rd.getLastShopRefreshTime())) {
			rd = getRoleDreamland(true);
			String items = rd.getShopItems();
			rd.setLastShopRefreshTime(new Date());
			rd.setShopRefreshNum(0);
			doRefreshShopList();

			this.eventRefreshShop.onRefreshDreamlandShop(true, 0, items, rd.getShopItems());
		}
	}

	@Override
	public DreamlandShopItemView dreamlandShopPage() throws NoteException {
		resetRoleDreamlandShop();
		return createDreamlandShopItemView();
	}

	/**
	 * 商店物品列表视图数据
	 * 
	 * @return
	 */
	private DreamlandShopItemView createDreamlandShopItemView() {
		Map<Integer, Byte> itemMaps = parseShopItems();

		// 商店界面数据视图
		DreamlandShopItemUnit[] itemUnits = null;
		for (int itemId : itemMaps.keySet()) {
			DreamlandShopT shopT = XsgDreamlandManager.getInstance().getDreamlandShop(itemId);
			boolean isExchange = itemMaps.get(itemId) == 0 ? false : true;
			int itemPrice = shopT.itemNum * shopT.price;
			itemUnits = (DreamlandShopItemUnit[]) ArrayUtils.add(itemUnits, new DreamlandShopItemUnit(itemId, shopT.itemCode, shopT.itemNum, itemPrice, shopT.rebate, isExchange));
		}
		RoleDreamland rd = getRoleDreamland(false);
		int refreshNum = rd == null ? 0 : rd.getShopRefreshNum();
		return new DreamlandShopItemView(getNanHuaLing(), refreshNum, itemUnits);
	}

	@Override
	public DreamlandShopItemView dreamlandRefreshShop() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		RoleDreamland rd = getRoleDreamland(false);
		int refreshNum = rd == null ? 0 : rd.getShopRefreshNum();
		DreamlandShopRefreshConfigT configT = XsgDreamlandManager.getInstance().getShopRefreshConfigs(refreshNum + 1);
		if (configT == null) {
			throw new NoteException(Messages.getString("DreamlandController.shop.refreshNum.notEnough"));
		}
		// 验证消耗
		roleRt.getItemControler().isItemNumEnough(configT.coin, configT.pay);
		// 扣除道具
		roleRt.getRewardControler().acceptReward(configT.coin, -configT.pay);
		// 增加次数
		rd = getRoleDreamland(true);
		rd.setShopRefreshNum(rd.getShopRefreshNum() + 1);
		String shopItems = rd.getShopItems();
		doRefreshShopList();
		// 触发事件
		this.eventRefreshShop.onRefreshDreamlandShop(false, refreshNum, shopItems, rd.getShopItems());
		return createDreamlandShopItemView();
	}

	@Override
	public IntString buyDreamlandShopItem(int id) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		resetRoleDreamlandShop();
		DreamlandShopT shopT = XsgDreamlandManager.getInstance().getDreamlandShop(id);
		if (shopT == null) {
			throw new NoteException(Messages.getString("DreamlandController.shop.item.notEnough"));
		}
		Map<Integer, Byte> itemMaps = parseShopItems();
		if (itemMaps == null || !itemMaps.containsKey(id) || itemMaps.get(id) == 1) {
			throw new NoteException(Messages.getString("DreamlandController.shop.item.notEnough"));
		}
		// 验证消耗
		int itemPrice = shopT.itemNum * shopT.price;
		roleRt.getItemControler().isItemNumEnough(shopT.coin, itemPrice);
		// 扣除道具
		roleRt.getRewardControler().acceptReward(shopT.coin, -itemPrice);
		// 获得道具
		roleRt.getRewardControler().acceptReward(shopT.itemCode, shopT.itemNum);
		// 更改物品状态
		RoleDreamland rd = getRoleDreamland(true);
		itemMaps.put(id, (byte) 1);
		rd.setShopItems(TextUtil.GSON.toJson(itemMaps));
		// 触发事件
		this.eventBuyShopItem.onBuyShopItem(id, shopT.itemCode, shopT.itemNum);
		return new IntString(shopT.itemNum, shopT.itemCode);
	}

	@Override
	public int buyChallengeNum() throws NoteException, NotEnoughYuanBaoException {
		resetRoleDreamland();
		RoleDreamland rd = getRoleDreamland(false);
		// 狗屁莫名其妙的需要修改-20160524
		// int challengeNum = rd == null ? 0 : rd.getChallengeNum();
		// if (challengeNum <
		// XsgGameParamManager.getInstance().getDreamlandChallengeNum()) {
		// throw new
		// NoteException(Messages.getString("DreamlandController.challengeNum.enough"));
		// }
		int buyNum = rd == null ? 0 : rd.getBuyNum();
		DreamlandChallengeT challengeT = XsgDreamlandManager.getInstance().getChallengeTimes(buyNum + 1);
		if (challengeT == null) {
			throw new NoteException(Messages.getString("DreamlandController.challengeNum.max"));
		}
		if (challengeT.vipLvl > roleRt.getVipLevel()) {
			throw new NoteException(Messages.getString("DreamlandController.challengeNum.vipLvlNotEnough"));
		}
		// 扣除元宝
		roleRt.winYuanbao(-challengeT.price, true);

		rd = getRoleDreamland(true);
		rd.setBuyNum(rd.getBuyNum() + 1);

		// 触发事件
		this.eventChallengeNum.onBuyChallengeNum(rd.getBuyNum());
		int surplusChallenge = XsgGameParamManager.getInstance().getDreamlandChallengeNum() - rd.getChallengeNum() + rd.getBuyNum();
		return surplusChallenge;
	}

	@Override
	public void onVipLevelUp(int newLevel) {
		XsgDreamlandManager.getInstance().setRank(roleRt);
	}

	@Override
	public void onRoleNameChange(String old, String name) {
		XsgDreamlandManager.getInstance().setRank(roleRt);
	}

	@Override
	public void onRoleHeadChange(String old, String headAndBorder) {
		XsgDreamlandManager.getInstance().setRank(roleRt);
	}
}
