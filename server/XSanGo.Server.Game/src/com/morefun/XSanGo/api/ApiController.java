package com.morefun.XSanGo.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.XSanGo.Protocol.ApiActView;
import com.XSanGo.Protocol.ApiRewardsView;
import com.XSanGo.Protocol.ApiTargetView;
import com.XSanGo.Protocol.BuyHeroResult;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.google.gson.reflect.TypeToken;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.ActivityT;
import com.morefun.XSanGo.api.XsgApiManager.Api;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.copy.SmallCopyT;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleApi;
import com.morefun.XSanGo.db.game.RoleHero;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.equip.EquipRebuildT;
import com.morefun.XSanGo.equip.XsgEquipManager;
import com.morefun.XSanGo.event.protocol.IBuy10WineByYuanbao;
import com.morefun.XSanGo.event.protocol.IBuyLimitHero;
import com.morefun.XSanGo.event.protocol.IBuySingleWineByYuanbao;
import com.morefun.XSanGo.event.protocol.ICopyBegin;
import com.morefun.XSanGo.event.protocol.ICopyCompleted;
import com.morefun.XSanGo.event.protocol.IEquipHole;
import com.morefun.XSanGo.event.protocol.IEquipRebuild;
import com.morefun.XSanGo.event.protocol.IEquipStarUp;
import com.morefun.XSanGo.event.protocol.IHeroBreakUp;
import com.morefun.XSanGo.event.protocol.IHeroJoin;
import com.morefun.XSanGo.event.protocol.IHeroPractice;
import com.morefun.XSanGo.event.protocol.IHeroQualityUp;
import com.morefun.XSanGo.event.protocol.IHeroStarUp;
import com.morefun.XSanGo.event.protocol.IItemCountChange;
import com.morefun.XSanGo.event.protocol.IJinbiChange;
import com.morefun.XSanGo.event.protocol.INormalItemUse;
import com.morefun.XSanGo.event.protocol.IPartnerPosReset;
import com.morefun.XSanGo.event.protocol.IReceiveApiReward;
import com.morefun.XSanGo.event.protocol.IResetPractice;
import com.morefun.XSanGo.event.protocol.ITimeBattle;
import com.morefun.XSanGo.event.protocol.ITimeBattleBegin;
import com.morefun.XSanGo.hero.HeroControler;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.EquipGemT;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.NormalItem;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * API处理实现
 * 
 * @author zhangwei02.zhang
 * @since 2015年11月11日
 * @version 1.0
 */
public class ApiController implements IApiController, IHeroStarUp, IHeroQualityUp, IHeroBreakUp, IEquipStarUp,
		IBuySingleWineByYuanbao, IBuy10WineByYuanbao, IBuyLimitHero, IResetPractice, IHeroPractice, IJinbiChange,
		IPartnerPosReset, IEquipHole, IEquipRebuild, ITimeBattleBegin, ITimeBattle, ICopyBegin, ICopyCompleted,
		INormalItemUse, IHeroJoin, IItemCountChange {

	/** 角色接口 */
	private IRole roleRt;

	/** 角色数据对象 */
	private Role roleDB;

	/** API领奖事件 */
	private IReceiveApiReward receiveApiReward;

	/**
	 * 构造函数
	 * 
	 * @param roleRt
	 * @param roleDB
	 */
	public ApiController(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		this.receiveApiReward = this.roleRt.getEventControler().registerEvent(IReceiveApiReward.class);

		this.roleRt.getEventControler().registerHandler(IHeroStarUp.class, this);
		this.roleRt.getEventControler().registerHandler(IHeroQualityUp.class, this);
		this.roleRt.getEventControler().registerHandler(IHeroBreakUp.class, this);
		this.roleRt.getEventControler().registerHandler(IEquipStarUp.class, this);
		this.roleRt.getEventControler().registerHandler(IBuySingleWineByYuanbao.class, this);
		this.roleRt.getEventControler().registerHandler(IBuy10WineByYuanbao.class, this);
		this.roleRt.getEventControler().registerHandler(IBuyLimitHero.class, this);
		this.roleRt.getEventControler().registerHandler(IResetPractice.class, this);
		this.roleRt.getEventControler().registerHandler(IHeroPractice.class, this);
		this.roleRt.getEventControler().registerHandler(IJinbiChange.class, this);
		this.roleRt.getEventControler().registerHandler(IPartnerPosReset.class, this);
		this.roleRt.getEventControler().registerHandler(IEquipHole.class, this);
		this.roleRt.getEventControler().registerHandler(IEquipRebuild.class, this);
		this.roleRt.getEventControler().registerHandler(ITimeBattleBegin.class, this);
		this.roleRt.getEventControler().registerHandler(ITimeBattle.class, this);
		this.roleRt.getEventControler().registerHandler(ICopyBegin.class, this);
		this.roleRt.getEventControler().registerHandler(ICopyCompleted.class, this);
		this.roleRt.getEventControler().registerHandler(INormalItemUse.class, this);
		this.roleRt.getEventControler().registerHandler(IHeroJoin.class, this);
		this.roleRt.getEventControler().registerHandler(IItemCountChange.class, this);

		// 清理过期无效数据
		clearActiveApiResult();
	}

	/**
	 * 清理过期或者无效活动数据
	 * 
	 */
	private void clearActiveApiResult() {
		List<Integer> removeList = new ArrayList<Integer>();
		for (RoleApi ra : roleDB.getRoleApiMap().values()) {
			if (!XsgApiManager.getInstance().getApiActivitys().containsKey(ra.getActId())) {
				removeList.add(ra.getActId());
			} else {
				ActivityT at = XsgApiManager.getInstance().getApiActivitys().get(ra.getActId());
				if (!isOpenApi(at)) {
					removeList.add(ra.getActId());// 活动失效，移除数据
				}
			}
		}
		for (int activeId : removeList) {
			roleDB.getRoleApiMap().remove(activeId);
		}
	}

	/**
	 * 初始化活动进度，角色登录初始化
	 * 
	 */
	public void initActiveApiProcess() {
		for (ApiTypeT att : XsgApiManager.getInstance().getApiIdsTypeMap().values()) {
			if (att.getRewardsMap().isEmpty()) {// 奖励都没，别玩了
				continue;
			}
			if (!XsgApiManager.getInstance().getApiActiveityGroups().containsKey(att.apiId)) {// 没活动可处理，回去玩吧
				continue;
			}
			if (Api.getType(att.getFuncInterface()) == 1 && att.isCalculationHistory == 1) {// 返回类活动并且是计算历史数据需要进行处理
				Map<Integer, Integer> meetColl = null;
				for (ActivityT at : XsgApiManager.getInstance().getApiActiveityGroups().get(att.apiId)) {
					if (!isOpenApi(at)) {
						continue;
					}
					RoleApi ra = roleDB.getRoleApiMap().get(at.id);
					if (ra != null) {
						if (parseAwardHistory(ra.getRewardsHistory()).size() >= att.getRewardsMap().size()) {
							continue;// 奖励都领取完了，可以滚蛋了
						}
						// if (parseProcess(ra.getProcess()).size() >=
						// att.getRewardsMap().size()) {
						// continue;// 都有达标
						// }
					}
					if (meetColl == null) {// 防止同一API多个活动发布 减少计算次数
						meetColl = calculationRewardList(at.id, att);
					}
					processTargetResult(at, att, Api.getApi(att.getFuncInterface()), meetColl, 0);
				}
			}
		}
	}

	/**
	 * 计算符合条件返回类活动的奖励节点
	 * 
	 * @param activeId
	 *            活动编号
	 * @param att
	 * @return
	 */
	private Map<Integer, Integer> calculationRewardList(int activeId, ApiTypeT att) {
		int value = 0;
		switch (Api.getApi(att.getFuncInterface())) {
		case HeroStarUp:
			value = calculationHeroStar(att.param1, att.getMaxApiRewardT().targetCount);
			break;
		case HeroBreakUp:
			value = calculationHeroBreakLevel(att.param1, att.getMaxApiRewardT().targetCount);
			break;
		case HeroQualityUp:
			value = calculationHeroQualityLevel(att.param1, att.getMaxApiRewardT().targetCount);
			break;
		case EquipStarUp:
			value = calculationEquipStar(att.param1, att.getMaxApiRewardT().targetCount);
			break;
		default:
			break;
		}
		if (value < 1) {
			return new HashMap<Integer, Integer>();
		}
		return calculationRewardList(activeId, att, value, 0);
	}

	/**
	 * 计算符合条件返回类活动的奖励节点
	 * 
	 * @param activeId
	 *            活动编号
	 * @param att
	 *            API对象
	 * @param value
	 *            当前条件
	 * @param validateParam
	 *            验证条件
	 * @return
	 */
	private Map<Integer, Integer> calculationRewardList(int activeId, ApiTypeT att, int value, int validateParam) {
		Map<Integer, Integer> meetColl = new HashMap<Integer, Integer>();
		if (att.isCalculationHistory == 1) {// 需要计算历史
			for (ApiRewardT art : att.getRewardsMap().values()) {
				meetColl.put(art.rewardID, value);
			}
		} else {
			Map<Integer, Integer> process = parseProcess(activeId);
			for (ApiRewardT art : att.getRewardsMap().values()) {
				if ((process.isEmpty() && art.targetCount > validateParam) || process.containsKey(art.rewardID)) {
					meetColl.put(art.rewardID, value);
				}
			}
		}
		return meetColl;
	}

	/**
	 * 计算返回类活动武将星级处理
	 * 
	 * @param param
	 * @param maxConditionValue
	 *            最高条件参数
	 * @return
	 */
	private int calculationHeroStar(String param, int maxConditionValue) {
		if (TextUtil.isNotBlank(param)) {// 指定武将
			IHero hero = roleRt.getHeroControler().getHero(Integer.parseInt(param));
			return hero == null ? 0 : hero.getStar();
		} else {// 任意武将，则获取最高等级
			int value = 0;
			for (RoleHero hero : roleDB.getRoleHeros()) {
				if (value == 0 || hero.getStar() > value) {
					value = hero.getStar();
					if (value >= maxConditionValue) {
						break;
					}
				}
			}
			return value;
		}
	}

	/**
	 * 计算返回类活动武将突破等级处理
	 * 
	 * @param param
	 * @param maxConditionValue
	 *            最高条件参数
	 * @return
	 */
	private int calculationHeroBreakLevel(String param, int maxConditionValue) {
		if (TextUtil.isNotBlank(param)) {// 指定武将
			IHero hero = roleRt.getHeroControler().getHero(Integer.parseInt(param));
			return hero == null ? 0 : hero.getBreakLevel();
		} else {// 任意武将，则获取最高等级
			int value = 0;
			for (RoleHero hero : roleDB.getRoleHeros()) {
				if (value == 0 || hero.getBreakLevel() > value) {
					value = hero.getBreakLevel();
					if (value >= maxConditionValue) {
						break;
					}
				}
			}
			return value;
		}
	}

	/**
	 * 计算返回类活动武将品质等级处理
	 * 
	 * @param param
	 * @param maxConditionValue
	 *            最高条件参数
	 * @return
	 */
	private int calculationHeroQualityLevel(String param, int maxConditionValue) {
		if (TextUtil.isNotBlank(param)) {// 指定武将
			IHero hero = roleRt.getHeroControler().getHero(Integer.parseInt(param));
			return hero == null ? 0 : hero.getQualityLevel();
		} else {// 任意武将，则获取最高等级
			int value = 0;
			for (RoleHero hero : roleDB.getRoleHeros()) {
				if (value == 0 || hero.getColor() > value) {
					value = hero.getColor();
					if (value >= maxConditionValue) {
						break;
					}
				}
			}
			return value;
		}
	}

	/**
	 * 计算返回类活动装备星级处理
	 * 
	 * @param param
	 * @param maxConditionValue
	 *            最高条件参数
	 * @return
	 */
	private int calculationEquipStar(String param, int maxConditionValue) {
		int value = 0;
		if (TextUtil.isNotBlank(param)) {// 指定装备
			List<EquipItem> itemList = roleRt.getItemControler().findEquipByTemplateCode(param);
			for (EquipItem ei : itemList) {
				if (value == 0 || ei.getStar() > value) {
					value = ei.getStar();
					if (value >= maxConditionValue) {
						break;
					}
				}
			}
		} else {// 任意装备，则获取最高等级
			for (IItem item : roleRt.getItemControler().getItemList()) {
				if (item instanceof EquipItem) {
					EquipItem ei = (EquipItem) item;
					if (value == 0 || ei.getStar() > value) {
						value = ei.getStar();
						if (value >= maxConditionValue) {
							break;
						}
					}
				}
			}
		}
		return value;
	}

	/**
	 * API活动是否开放
	 * 
	 * @param at
	 * @return
	 */
	private boolean isOpenApi(ActivityT at) {
		return roleRt.getActivityControler().isOpenApi(at);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		ApiTypeT apiType = null;
		RoleApi ra = null;
		for (ActivityT at : XsgApiManager.getInstance().getApiActivitys().values()) {
			if (!isOpenApi(at)) {// 未开启
				continue;
			}
			apiType = XsgApiManager.getInstance().getApiIdsTypeMap(at.apiId);
			if (apiType.getRewardsMap().isEmpty()) {// 无奖励节点
				continue;
			}
			ra = roleDB.getRoleApiMap().get(at.id);
			if (ra == null) {// 未产生任何进度或者奖励数据
				continue;
			}
			for (int rewardID : apiType.getRewardsMap().keySet()) {
				if (getStatus(at.id, rewardID) == 2) {// 存在可领奖
					return new MajorUIRedPointNote(MajorMenu.Api, false);
				}
			}
		}
		return null;
	}

	@Override
	public List<ApiActView> openApiAct() throws NoteException {
		List<ApiActView> result = new ArrayList<ApiActView>();
		List<ApiTargetView> targetList = null;
		List<ApiRewardsView> rList = null;
		RoleApi ra = null;
		for (ActivityT at : XsgApiManager.getInstance().getApiActivitys().values()) {
			if (!isOpenApi(at)) {// 未开启
				continue;
			}
			ApiTypeT apiType = XsgApiManager.getInstance().getApiIdsTypeMap(at.apiId);
			targetList = new ArrayList<ApiTargetView>();
			ra = roleDB.getRoleApiMap().get(at.id);
			// 需要达成的目标
			int type = Api.getType(apiType.getFuncInterface());
			Map<Integer, Integer> process = null;
			for (Map.Entry<Integer, ApiRewardT> entry : apiType.getRewardsMap().entrySet()) {
				rList = new ArrayList<ApiRewardsView>();
				// 当前目标的奖励内容
				for (Map.Entry<String, Integer> item : entry.getValue().getItemCountMap().entrySet()) {
					rList.add(new ApiRewardsView(item.getKey(), item.getValue()));
				}
				int curCount = 0;
				if (ra != null) {// 计算当前进度
					if (type == 1) {// 返回类
						process = process == null ? parseProcess(ra.getProcess()) : process;
						if (process.containsKey(entry.getValue().rewardID)) {
							curCount = process.get(entry.getValue().rewardID);
						}
					} else if (type == 2) {// 计数类
						curCount = Integer.parseInt(ra.getProcess());
					}
				}
				targetList.add(new ApiTargetView(entry.getValue().rewardID, curCount, entry.getValue().targetCount,
						rList.toArray(new ApiRewardsView[0]), getStatus(at.id, entry.getKey()), entry.getValue().desc));
			}
			String startTime = DateUtil.format(DateUtil.parseDate(at.startTime));
			String endTime = DateUtil.format(DateUtil.parseDate(at.endTime));
			result.add(new ApiActView(at.id, startTime, endTime, at.name, at.intro, at.icon, apiType.isGo,
					apiType.goTo, targetList.toArray(new ApiTargetView[0])));
		}
		return result;
	}

	@Override
	public void receiveApiReward(int actId, int rewardId) throws NoteException {
		ActivityT at = XsgApiManager.getInstance().getApiActivitys().get(actId);
		if (!isOpenApi(at)) {
			throw new NoteException(Messages.getString("ApiController.1"));
		}
		ApiTypeT apiType = XsgApiManager.getInstance().getApiIdsTypeMap(at.apiId);
		if (!apiType.getRewardsMap().containsKey(rewardId)) {
			throw new NoteException(Messages.getString("ApiController.2"));
		}
		int status = getStatus(actId, rewardId);
		if (status == 1) {// 未达成
			throw new NoteException(Messages.getString("ApiController.3"));
		}
		if (status == 3) {// 已领奖
			throw new NoteException(Messages.getString("ApiController.4"));
		}
		ApiRewardT rt = apiType.getRewardsMap().get(rewardId);
		RoleApi ra = roleDB.getRoleApiMap().get(actId);
		// 发放奖励
		this.roleRt.getRewardControler().acceptReward(rt.getItemCountMap().entrySet());

		int[] results = TextUtil.GSON.fromJson(ra.getRewardsHistory(), int[].class);
		results = ArrayUtils.add(results, rewardId);
		ra.setRewardsHistory(TextUtil.GSON.toJson(results));

		// 触发事件
		this.receiveApiReward.onReceiveApiReward(actId, rewardId);
	}

	/**
	 * 解析领奖历史
	 * 
	 * @param strContent
	 * @return
	 */
	private List<Integer> parseAwardHistory(String strContent) {
		Integer[] arrays = TextUtil.GSON.fromJson(strContent, Integer[].class);
		if (arrays == null) {
			return new ArrayList<Integer>();
		}
		return new ArrayList<Integer>(Arrays.asList(arrays));
	}

	/**
	 * 解析某个活动API活动进度
	 * 
	 * @param activeId
	 * @return
	 */
	private Map<Integer, Integer> parseProcess(int activeId) {
		Map<Integer, RoleApi> map = roleDB.getRoleApiMap();
		if (map == null || !map.containsKey(activeId)) {
			return new HashMap<Integer, Integer>();
		}
		RoleApi ra = map.get(activeId);
		return parseProcess(ra.getProcess());
	}

	/**
	 * 解析活动进度
	 * 
	 * @param process
	 * @return
	 */
	private Map<Integer, Integer> parseProcess(String process) {
		if (TextUtil.isNotBlank(process)) {
			return TextUtil.GSON.fromJson(process, new TypeToken<Map<Integer, Integer>>() {
			}.getType());
		}
		return new HashMap<Integer, Integer>();
	}

	/**
	 * 是否领取某个目标条件的奖励
	 * 
	 * @param activeId
	 * @param rewardId
	 * @return
	 */
	private boolean hasReceivedReward(int activeId, int rewardId) {
		RoleApi ra = roleDB.getRoleApiMap().get(activeId);
		if (ra == null || TextUtil.isBlank(ra.getRewardsHistory())) {
			return false;
		}
		return parseAwardHistory(ra.getRewardsHistory()).contains(rewardId);
	}

	/**
	 * 1:目标未完成; 2:未领奖; 3:已领奖
	 * 
	 * @param actId
	 * @return
	 */
	private int getStatus(int actId, int rewardId) {
		Map<Integer, RoleApi> map = roleDB.getRoleApiMap();
		if (map == null || !map.containsKey(actId)) {
			return 1;// 未达成
		}
		if (hasReceivedReward(actId, rewardId)) {
			return 3;// 已领奖
		}
		ActivityT at = XsgApiManager.getInstance().getApiActivitys().get(actId);
		if (at != null) {
			ApiTypeT apiType = XsgApiManager.getInstance().getApiIdsTypeMap(at.apiId);
			ApiRewardT art = apiType.getRewardsMap().get(rewardId);
			if (art != null) {
				int type = Api.getType(apiType.getFuncInterface());
				RoleApi ra = map.get(actId);
				if (type == 1) {// 返回类
					Map<Integer, Integer> process = parseProcess(ra.getProcess());
					if (process.containsKey(rewardId) && process.get(rewardId) >= art.targetCount) {
						return 2;// 已达成未领奖
					}
				} else if (type == 2) {// 计数类
					if (Integer.parseInt(ra.getProcess()) >= art.targetCount) {
						return 2;// 已达成未领奖
					}
				}
			}
		}
		return 1;// 未达成
	}

	/**
	 * 处理待完成目标条件 默认+1
	 * 
	 * @param key
	 *            APIkey
	 * @param param1
	 *            条件参数
	 */
	private void processTarget(Api key, Object param1) {
		processTarget(key, param1, 1);
	}

	/**
	 * 处理待完成目标条件 默认单参数
	 * 
	 * @param key
	 *            APIkey
	 * @param param1
	 *            条件参数
	 * @param value
	 *            次数值
	 */
	private void processTarget(Api key, Object param1, int value) {
		processTarget(key, param1, "", value, 0);
	}

	/**
	 * 处理待完成目标条件 默认单参数
	 * 
	 * @param key
	 *            APIkey
	 * @param param1
	 *            条件参数
	 * @param value
	 *            次数值
	 * @param validateParam
	 *            验证参数
	 */
	private void processTarget(Api key, Object param1, int value, int validateParam) {
		processTarget(key, param1, "", value, validateParam);
	}

	/**
	 * 处理待完成目标条件
	 * 
	 * @param key
	 *            APIkey
	 * @param param1
	 *            条件参数
	 * @param param2
	 *            条件参数
	 * @param value
	 *            次数值
	 * @param validateParam
	 *            验证参数
	 */
	private void processTarget(Api key, Object param1, Object param2, int value, int validateParam) {
		List<ApiTypeT> typeList = XsgApiManager.getInstance().getApiInterFaceTypeMap().get(key.getValue());
		if (typeList == null || typeList.size() == 0) {
			return;
		}
		List<ActivityT> mlist = null;
		for (ApiTypeT tt : typeList) {
			if (tt.getRewardsMap().isEmpty()) {// 无奖励 干你蛋
				continue;
			}
			if (!XsgApiManager.getInstance().getApiActiveityGroups().containsKey(tt.apiId)) {// 没活动可处理，回去玩吧
				continue;
			}
			if (TextUtil.isNotBlank(tt.param1) && (param1 == null || !tt.param1.equals(param1.toString()))) {
				continue;
			}
			if (TextUtil.isNotBlank(tt.param2) && (param2 == null || !tt.param2.equals(param2.toString()))) {
				continue;
			}
			mlist = XsgApiManager.getInstance().getApiActiveityGroups().get(tt.apiId);
			for (ActivityT at : mlist) {
				if (!isOpenApi(at)) {
					continue;
				}
				RoleApi ra = roleDB.getRoleApiMap().get(at.id);
				if (ra != null) {
					if (parseAwardHistory(ra.getRewardsHistory()).size() >= tt.getRewardsMap().size()) {
						continue;// 奖励都领取完了，可以滚蛋了
					}
					// if (key.getType() == 1 &&
					// parseProcess(ra.getProcess()).size() >=
					// tt.getRewardsMap().size()) {
					// continue;// 都有达标
					// }
				}
				Map<Integer, Integer> meetColl = null;// 符合返回类活动
				if (key.getType() == 1) {// 返回类活动
					if (validateParam > 0 && value <= validateParam) { // 验证参数必须大于条件参数
						continue;
					}
					meetColl = calculationRewardList(at.id, tt, value, validateParam);
					if (meetColl.isEmpty()) {// 不存在复合要求的
						continue;
					}
				}
				processTargetResult(at, tt, key, meetColl, value);
			}
		}
	}

	/**
	 * 处理API进度结果
	 * 
	 * @param at
	 * @param tt
	 * @param key
	 * @param meetList
	 * @param value
	 */
	private void processTargetResult(ActivityT at, ApiTypeT tt, Api key, Map<Integer, Integer> meetColl, int value) {
		RoleApi ra = roleDB.getRoleApiMap().get(at.id);
		String resultProcess = null;
		if (key.getType() == 1) {// 返回类
			// List<Integer> result = ra == null ? new ArrayList<Integer>() :
			// getString2List(ra.getProcess());
			// boolean isRight = false;
			// for (int meetid : meetColl) {
			// if (!result.contains(meetid)) {
			// result.add(meetid);
			// isRight = true;
			// }
			// }
			// if (!isRight) {
			// return;// 滚吧 没有符合条件的
			// }
			// resultProcess = TextUtil.GSON.toJson(result.toArray(new
			// Integer[0]));
			resultProcess = TextUtil.GSON.toJson(meetColl);
		} else if (key.getType() == 2) {// 计数类
			resultProcess = String.valueOf(ra == null ? value : Integer.parseInt(ra.getProcess()) + value);
		}
		Date now = Calendar.getInstance().getTime();
		if (ra == null) {
			String index = GlobalDataManager.getInstance().generatePrimaryKey();
			ra = new RoleApi(index, at.id, roleDB, resultProcess, "", now);
			roleDB.getRoleApiMap().put(ra.getActId(), ra);
		} else {
			ra.setProcess(resultProcess);
			ra.setTime(now);
		}
	}

	@Override
	public void onHeroStarUp(IHero hero, int beforeStar) {
		processTarget(Api.HeroStarUp, hero.getTemplateId(), hero.getStar(), beforeStar);
	}

	@Override
	public void onHeroBreakUp(IHero hero, int beforeBreakLevel) {
		processTarget(Api.HeroBreakUp, hero.getTemplateId(), hero.getBreakLevel(), beforeBreakLevel);
		// 消耗功勋
		processTarget(Api.ItemConsumption, HeroControler.MED3,
				XsgHeroManager.getInstance().findBreakLevelUpT(hero.getBreakLevel()).med3Count);
		// 突破丹
		processTarget(Api.ItemConsumption, HeroControler.DrugForBreak,
				XsgHeroManager.getInstance().findBreakLevelUpT(hero.getBreakLevel()).drugCount);
	}

	@Override
	public void onHeroQualityUp(IHero hero, int beforeQualityLevel) {
		processTarget(Api.HeroQualityUp, hero.getTemplateId(), hero.getQualityLevel(), beforeQualityLevel);
		// 消耗功勋
		processTarget(Api.ItemConsumption, HeroControler.MED3,
				XsgHeroManager.getInstance().findQualityLevelUpT(hero.getQualityLevel()).med1Count);
	}

	@Override
	public void onEquipStarUp(EquipItem equip, int uplevel, List<EquipItem> deleteList, int money, int addExp,
			Map<String, Integer> consumeStars) {
		processTarget(Api.EquipStarUp, equip.getTemplate().getId(), equip.getStar(), equip.getStar() - uplevel);
		// 升星石消耗
		for (String key : consumeStars.keySet()) {
			int useNum = consumeStars.get(key);
			processTarget(Api.ItemConsumption, key, useNum);
		}
	}

	@Override
	public void onBuy10WineByYuanbao(List<BuyHeroResult> list) {
		processTarget(Api.Buy10WineByYuanbao, null);
	}

	@Override
	public void onBuyWineByYuanbao(BuyHeroResult result, boolean isFree) {
		processTarget(Api.BuyWineByYuanbao, null);
	}

	@Override
	public void onBuyLimitHero(List<BuyHeroResult> results) {
		processTarget(Api.JuXianZhang, null);
	}

	@Override
	public void onResetPractice(IHero hero, int index, String oldName, int oldColor, int oldLevel, int oldExp,
			String newName, int newColor) {
		processTarget(Api.ItemConsumption, HeroControler.XDAN);
	}

	@Override
	public void onHeroPractice(IHero hero, int index, String name, int coloe, int oldLevel, int oldExp, int addExp,
			int newLevel, int newExp, int sumGx) {
		processTarget(Api.ItemConsumption, HeroControler.MED3, sumGx);
	}

	@Override
	public void onJinbiChange(long change) throws Exception {
		if (change < 0) {
			processTarget(Api.ItemConsumption, Const.PropertyName.MONEY, (int) -change);
		}
	}

	@Override
	public void onCopyBegin(SmallCopyT templete, int junling) {
		processTarget(Api.ItemConsumption, Const.PropertyName.JUNLING_TEMPLATE_ID, junling);
	}

	@Override
	public void onCopyCompleted(SmallCopyT templete, int star, boolean firstPass, int fightPower, int junling) {
		processTarget(Api.ItemConsumption, Const.PropertyName.JUNLING_TEMPLATE_ID, junling);
	}

	@Override
	public void onBattleBegin(int id, boolean isClear, int junling) {
		processTarget(Api.ItemConsumption, Const.PropertyName.JUNLING_TEMPLATE_ID, junling);
	}

	@Override
	public void onPassBattle(int id, boolean isClear, int junling) {
		processTarget(Api.ItemConsumption, Const.PropertyName.JUNLING_TEMPLATE_ID, junling);
	}

	@Override
	public void onPartnerPositionReset(int pos, int id, String specialHeroCode, int costNum) {
		processTarget(Api.ItemConsumption, "ydan", costNum);
	}

	@Override
	public void onEquipHole(EquipItem equip) {
		EquipGemT gemT = XsgItemManager.getInstance().getEquipGemWithQuality(equip.getQuatityColor().ordinal());
		processTarget(Api.ItemConsumption, gemT.holeToolId, gemT.num);
	}

	@Override
	public void onEquipRebuild(EquipItem equip) {
		EquipRebuildT template = XsgEquipManager.getInstance().findRebuildT(equip.getQuatityColor());
		processTarget(Api.ItemConsumption, template.itemTemplateId, template.itemCount);
	}

	@Override
	public void onItemUse(NormalItem item, int count, int realCount) {
		processTarget(Api.ItemConsumption, item.getTemplate().getId(), realCount);
	}

	@Override
	public void onHeroJoin(IHero hero, HeroSource source) {
		processTarget(Api.HeroStarUp, hero.getTemplateId(), hero.getStar(), 0);
	}

	@Override
	public void onItemCountChange(IItem item, int change) {
		if (change > 0 && item instanceof EquipItem) {
			EquipItem equip = (EquipItem) item;
			processTarget(Api.EquipStarUp, equip.getTemplate().getId(), equip.getStar(), 0);
		}
	}
}
