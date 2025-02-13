package com.morefun.XSanGo.AttackCastle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AcceptRewardResultView;
import com.XSanGo.Protocol.AttackCastleShopItemView;
import com.XSanGo.Protocol.AttackCastleShopView;
import com.XSanGo.Protocol.AttackCastleView;
import com.XSanGo.Protocol.CastleNodeView;
import com.XSanGo.Protocol.CastleOpponentView;
import com.XSanGo.Protocol.ClearResultView;
import com.XSanGo.Protocol.CopyClearResultView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.EndAttackCastleView;
import com.XSanGo.Protocol.FormationPosView;
import com.XSanGo.Protocol.FormationSummaryView;
import com.XSanGo.Protocol.FormationView;
import com.XSanGo.Protocol.HeroSoulPair;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleAttackCastle;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IAttackCastleAcceptReward;
import com.morefun.XSanGo.event.protocol.IAttackCastleBegin;
import com.morefun.XSanGo.event.protocol.IAttackCastleEnd;
import com.morefun.XSanGo.event.protocol.IAttackCastleExchange;
import com.morefun.XSanGo.event.protocol.IAttackCastleRefreshStore;
import com.morefun.XSanGo.event.protocol.IAttackCastleReset;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.formation.datacollector.BattlePowerSnapshotQueryResult;
import com.morefun.XSanGo.formation.datacollector.XsgFormationDataCollecterManager;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 北伐 Controller
 * 
 * @author qinguofeng
 * @date Jan 27, 2015
 */
public class AttackCastleController implements IAttackCastleController {

	private final static Log logger = LogFactory.getLog(AttackCastleController.class);

	private final static int CAN_BUY = 1, SOLD_OUT = 0; // 可以购买, 售罄

	private final static String CoinTemplateId = "pop";

	private IRole role;
	private Role roleDB;
	private RoleAttackCastle roleAttackCastle;

	// TODO ... 能用数据库对象的字段尽量直接使用数据库字段
	private int currentNodeId; // 当前关卡ID
	private int attackCastleCoinCount; // 竞技币数量(文档中为声望)
	private int resetNodeCount; // 今日重置关卡次数
	private int refreshShopCount; // 今日兑换商城刷新次数
	private Date lastResetShopDate = Calendar.getInstance().getTime(); // 上次重置商城时间
	private Date lastResetNodeDate; // 上次重置关卡时间
	private int lastResetNodeId = 0; // 上次重置关卡的进度
	private int randomCount; // 执行随机的次数
	private boolean isNormalRandom; // 是否执行普通掉落
	private Map<Integer, Integer> refreshLevelMap = new HashMap<Integer, Integer>();

	private AttackContext attackContext = new AttackContext(); // 战斗上下文
	private OpponentInfo lastOpponentInfo; // 上个对手信息

	private Map<Integer, AttackCastleShopItemView> shopRewards = new HashMap<Integer, AttackCastleShopItemView>();

	/** 已经打过的关卡信息 */
	private Map<Integer, CastleNodeEntity> castleNodeMap = new HashMap<Integer, CastleNodeEntity>();

	private Set<String> matchedOpponentIdSet = new HashSet<String>();

	private IAttackCastleAcceptReward acceptReward;
	private IAttackCastleBegin beginAttackCastle;
	private IAttackCastleEnd endAttackCastle;
	private IAttackCastleExchange exchangeReward;
	private IAttackCastleReset resetAttackCastle;
	private IAttackCastleRefreshStore refreshStore;

	private String movieIdContext;

	public AttackCastleController(IRole r, Role db) {
		this.role = r;
		this.roleDB = db;
		// 加载数据库信息
		roleAttackCastle = roleDB.getRoleAttackCastle();
		if (roleAttackCastle != null) {
			initFromDB();
		}

		IEventControler evtControler = this.role.getEventControler();
		acceptReward = evtControler.registerEvent(IAttackCastleAcceptReward.class);
		beginAttackCastle = evtControler.registerEvent(IAttackCastleBegin.class);
		endAttackCastle = evtControler.registerEvent(IAttackCastleEnd.class);
		exchangeReward = evtControler.registerEvent(IAttackCastleExchange.class);
		resetAttackCastle = evtControler.registerEvent(IAttackCastleReset.class);
		refreshStore = evtControler.registerEvent(IAttackCastleRefreshStore.class);
	}

	@Override
	public AttackCastleView requestAttackCastles() throws NoteException {
		// 检查开放等级
		AttackCastleParamSettingT setting = XsgAttackCastleManager.getInstance().getParamSetting();
		if (role.getLevel() < setting.minLv) {
			throw new NoteException(TextUtil.format(Messages.getString("AttackCastleController.openLevelLimit"),
					setting.minLv));
		}
		// 设置今日重置次数, 每日刷新, 第一次进入北伐系统的时候不设置重置次数
		if (lastResetNodeDate != null) {
			setupResetNodeCount();
		}
		// 重置关卡, 第一次进入北伐系统
		if (needResetNode()) {
			if (roleAttackCastle == null) {
				roleAttackCastle = new RoleAttackCastle(roleDB);
			}
			doResetNode();
			resetNodeCount = 1; // 系统自动重置, 重置次数加一
			lastResetNodeDate = Calendar.getInstance().getTime();
			saveToRole();
		}

		AttackCastleView attackCastleView = buildAttackCastleView();
		return attackCastleView;
	}

	@Override
	public AttackCastleView resetAttackCastles() throws NoteException {
		if (resetNodeCount >= getResetCount(role.getVipLevel())) {
			throw new NoteException(Messages.getString("AttackCastleController.0")); //$NON-NLS-1$
		}
		doResetNode();
		resetNodeCount += 1;
		saveToRole();
		AttackCastleView view = buildAttackCastleView();

		resetAttackCastle.onAttackCastleReset(resetNodeCount);

		return view;
	}

	private BattlePowerSnapshotQueryResult rematchOpponent(AttackCastleParamSettingT setting,
			AttackCastleNodeSettingT nodeSetting, int castleNodeId, boolean refresh) throws NoteException {
		IFormation selfFormation = role.getFormationControler().getDefaultFormation();
		if (selfFormation == null) {
			throw new NoteException(Messages.getString("AttackCastleController.5")); //$NON-NLS-1$
		}
		// 获取自己的最高战力快照
		BattlePowerSnapshotQueryResult selfBattlePowerResult = XsgFormationDataCollecterManager.getInstance()
				.queryFormationViewById(role.getRoleId());
		// 计算战力和等级匹配范围
		int selfBattlePower = selfFormation.calculateBattlePower();
		// 如果战力快照不为空, 则取历史最高战力匹配
		if (selfBattlePowerResult != null) {
			selfBattlePower = selfBattlePowerResult.getPower();
		}
		int selfLevel = role.getLevel();
		String matchPower = nodeSetting.matchPower;
		String matchLv = nodeSetting.matchLv;
		if (selfLevel <= setting.protectLevel) { // 在保护等级一下，匹配保护等级参数
			matchPower = nodeSetting.protectMatchPower;
			matchLv = nodeSetting.protectMatchLv;
		}
		final MatchProperty match = new MatchProperty(matchPower, matchLv, selfBattlePower, selfLevel,
				(refresh ? 1 : 0));
		// 匹配对手
		final BattlePowerSnapshotQueryResult opponent = searchOpponent(match);
		if (opponent == null) {
			throw new NoteException(Messages.getString("AttackCastleController.6")); //$NON-NLS-1$
		}
		// 组装对手信息
		return opponent;
	}

	/** 是否可以刷新 */
	private RefreshEntity canRefresh(int nodeId) {
		int alreadyNum = 0;
		if (refreshLevelMap.containsKey(nodeId)) {
			alreadyNum = refreshLevelMap.get(nodeId);
		}
		AttackCastleRefreshCostT costT = XsgAttackCastleManager.getInstance().getRefreshCostT(alreadyNum + 1);
		RefreshEntity entity = new RefreshEntity();
		if (costT != null) {
			entity.canRefresh = true;
			entity.price = costT.price;
		} else {
			entity.canRefresh = false;
			entity.price = Integer.MAX_VALUE;
		}
		return entity;
	}

	private void responseOpponentView(final int nodeId, BattlePowerSnapshotQueryResult opponent, final Callback cbfunc)
			throws NoteException {
		lastOpponentInfo = getOpponentInfo(opponent);
		if (lastOpponentInfo == null) {
			throw new NoteException(Messages.getString("AttackCastleController.7")); //$NON-NLS-1$
		}

		String opponentId = opponent.getRoleId();

		Callback onLoadEnd = new Callback() {
			@Override
			public void cb(boolean success, CastleOpponentView castleView) {
				// 查询对手信息成功
				if (success && castleView != null) {
					if (lastOpponentInfo.view != null) {
						// 设置对手的武将信息为之前组装的快照中的武将信息
						castleView.guardHeroArr = lastOpponentInfo.view.guardHeroArr;
					}
					RefreshEntity refreshEntity = canRefresh(nodeId);
					castleView.canRefresh = refreshEntity.canRefresh;
					castleView.refreshPrice = refreshEntity.price;
					lastOpponentInfo.view = castleView;
				} else { // 查询对手信息失败, 将上个对手置空, 下次重新匹配
					lastOpponentInfo = null;
				}
				saveToRole();
				cbfunc.cb(success, castleView);
			}
		};
		// 异步查询对手信息
		XsgRoleManager.getInstance().loadRoleByIdAsync(opponentId, new LoadOpponent(opponentId, onLoadEnd, true),
				new LoadOpponent(opponentId, onLoadEnd, false));
	}

	@Override
	public void getCastleOpponentView(int castleNodeId, final Callback cbfunc) throws NoteException {
		// 不能超越打关卡
		if (castleNodeId > (currentNodeId + 1)) {
			throw new NoteException(Messages.getString("AttackCastleController.1")); //$NON-NLS-1$
		}
		// 已经打过的关卡不能重复打
		if (castleNodeId < currentNodeId) {
			throw new NoteException(Messages.getString("AttackCastleController.2")); //$NON-NLS-1$
		}
		// 获取关卡信息
		AttackCastleNodeSettingT nodeSetting = XsgAttackCastleManager.getInstance().getNodeSetting(castleNodeId);
		if (nodeSetting == null || TextUtil.isBlank(nodeSetting.matchLv) || TextUtil.isBlank(nodeSetting.matchPower)) {
			throw new NoteException(Messages.getString("AttackCastleController.3")); //$NON-NLS-1$
		}

		if (nodeSetting.isOpen == 0) {
			throw new NoteException(Messages.getString("AttackCastleController.4")); //$NON-NLS-1$
		}

		// 获取自己的武将信息
		final IFormation selfFormation = role.getFormationControler().getDefaultFormation();
		if (selfFormation == null) {
			throw new NoteException(Messages.getString("AttackCastleController.5")); //$NON-NLS-1$
		}

		// 记录战斗队伍的人员数量
		attackContext.originHeroCount = selfFormation.getHeroCountIncludeSupport();

		// 上次的对手还没有打过, 直接返回上次的对手
		if (lastOpponentInfo != null) {
			// 是否可以刷新
			RefreshEntity refreshEntity = canRefresh(castleNodeId);
			lastOpponentInfo.view.canRefresh = refreshEntity.canRefresh;
			lastOpponentInfo.view.refreshPrice = refreshEntity.price;
			cbfunc.cb(true, lastOpponentInfo.view);
			return;
		}

		BattlePowerSnapshotQueryResult opponent = rematchOpponent(XsgAttackCastleManager.getInstance()
				.getParamSetting(), nodeSetting, castleNodeId, false);

		responseOpponentView(castleNodeId, opponent, cbfunc);
	}

	@Override
	public EndAttackCastleView endAttackCastle(int castleNodeId, byte remainHero) throws NoteException {
		AttackCastleNodeSettingT nodeSetting = XsgAttackCastleManager.getInstance().getNodeSetting(castleNodeId);
		if (nodeSetting == null) {
			throw new NoteException(Messages.getString("AttackCastleController.8")); //$NON-NLS-1$
		}
		// logger.info("endAttackCastle: heroCount: " + remainHero);
		EndAttackCastleView endAttackView = new EndAttackCastleView();
		// 结束战斗, 计算星级
		int star = XsgCopyManager.getInstance().calculateStar(attackContext.originHeroCount, remainHero);
		// 战斗胜利
		if (remainHero > 0 && star > 0) {
			endAttackView.star = star;
			attackContext.remainStarCount = star;
			// logger.info("Star: " + attackContext.remainStarCount + ", " +
			// star);
			CastleNodeEntity entity = new CastleNodeEntity(castleNodeId, star);
			castleNodeMap.put(entity.getId(), entity);
			// 设置 lastopponentinfo 为 null, 表示此关卡已过
			lastOpponentInfo = null;
			currentNodeId = castleNodeId;
			saveToRole();

			endAttackView.addGold = addTechnologyGold();
		}
		if (!TextUtil.isBlank(movieIdContext)) {
			XsgFightMovieManager.getInstance().endFightMovie(role.getRoleId(), movieIdContext, remainHero > 0 ? 1 : 0,
					remainHero);
		}

		endAttackCastle.onAttackCastleEnd(castleNodeId, attackContext.originHeroCount, remainHero, star);

		return endAttackView;
	}

	/**
	 * 公会科技增加金币
	 * 
	 * @return
	 */
	private int addTechnologyGold() {
		int addGold = role.getFactionControler().getTechnologyValue(102);
		if (addGold > 0) {
			try {
				role.winJinbi(addGold);
			} catch (NotEnoughMoneyException e) {
			}
		}
		return addGold;
	}

	private ExtraHeroSoul getExtraHeroRewardId(int starCount, TcResult result) {
		boolean alreadyHas = false;
		int extraHeroId = 0;
		int heroSoulNum = 0;
		if (starCount >= 5) {
			randomCount++;
			extraHeroId = getRandomExtraHero();
			if (extraHeroId > 0) {
				isNormalRandom = true;
				randomCount = 0;

				IHero hero = role.getHeroControler().getHero(extraHeroId);
				if (hero != null) { // 转为將魂
					String soulTemplateId = XsgHeroManager.getInstance().getSoulTemplateId(extraHeroId);
					heroSoulNum = XsgHeroManager.getInstance().caculateSoulCountForCardTransform(
							hero.getTemplate().color);
					result.appendProperty(soulTemplateId, heroSoulNum);
					alreadyHas = true;
				} else { // 增加武将
					HeroT heroT = XsgHeroManager.getInstance().getHeroT(extraHeroId);
					if (heroT != null) {
						role.getHeroControler().addHero(heroT, HeroSource.AttackCastle);
					} else {
						logger.error(TextUtil.format("{0} missing heroT with id {1}", role.getRoleId(), extraHeroId));
					}
				}
			}
		}
		return new ExtraHeroSoul(alreadyHas, extraHeroId, heroSoulNum);
	}

	@Override
	public AcceptRewardResultView acceptRewards(int castleNodeId, int starCount) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		if (castleNodeId > (currentNodeId + 1)) {
			throw new NoteException(Messages.getString("AttackCastleController.9")); //$NON-NLS-1$
		}
		AttackCastleNodeSettingT rewardSetting = XsgAttackCastleManager.getInstance().getRewardSetting(castleNodeId);
		if (rewardSetting == null) {
			throw new NoteException(Messages.getString("AttackCastleController.10")); //$NON-NLS-1$
		}
		if (starCount > 3 && attackContext.remainStarCount < starCount) {
			throw new NoteException(Messages.getString("AttackCastleController.11")); //$NON-NLS-1$
		}

		// 宝箱的节点编号比关卡多1
		CastleNodeEntity castleEntity = castleNodeMap.get(castleNodeId - 1);
		if (castleEntity == null) {
			throw new NoteException(Messages.getString("AttackCastleController.12")); //$NON-NLS-1$
		}
		if (castleEntity.hasAcceptReward()) {
			throw new NoteException(Messages.getString("AttackCastleController.13")); //$NON-NLS-1$
		}

		String rewardTC = getRewardTC(starCount, rewardSetting);
		if (TextUtil.isBlank(rewardTC)) {
			throw new NoteException(Messages.getString("AttackCastleController.14")); //$NON-NLS-1$
		}

		XsgRewardManager rewardManager = XsgRewardManager.getInstance();
		// 执行宝箱TC
		TcResult result = rewardManager.doTc(this.role, rewardTC);
		if (result == null) {
			throw new NoteException(Messages.getString("AttackCastleController.15")); //$NON-NLS-1$
		}
		// 金币=基础金币X（1+星级*5%+玩家等级*1%）
		int money = rewardSetting.coinNum * (100 + starCount * 5 + role.getLevel()) / 100;

		switch (rewardSetting.coinType) {
		case 0: // 金币
			result.appendProperty(Const.PropertyName.MONEY, money);
			break;
		case 1: // 元宝
			result.appendProperty(Const.PropertyName.RMBY, money);
			break;
		}

		ExtraHeroSoul extraHero = getExtraHeroRewardId(starCount, result);

		// 给玩家加宝箱和金币
		ItemView[] rewards = rewardManager.generateItemView(result);
		if (rewards != null && rewards.length > 0) {
			role.getRewardControler().acceptReward(rewards);
		}
		// 奖励声望
		addCoin(rewardSetting.reputation);
		castleEntity.acceptReward();
		currentNodeId = castleNodeId;
		attackContext.remainStarCount = 0;
		saveToRole();
		AcceptRewardResultView view = new AcceptRewardResultView(getCoin(), rewards, extraHero.extraHeroId,
				extraHero.alreadyHas, extraHero.heroSoulNum);

		acceptReward.onAcceptReward(castleNodeId, rewardSetting.reputation, rewards);

		return view;
	}

	/**
	 * 获取额外掉落
	 * */
	private int getRandomExtraHero() {
		AttackCastleParamSettingT settingT = XsgAttackCastleManager.getInstance().getParamSetting();
		if (settingT != null) {
			int total = 0;
			int minCount = 0;
			List<AttackCastleExtraHeroT> extraHeroList = new ArrayList<AttackCastleExtraHeroT>();
			if (isNormalRandom) {
				minCount = settingT.minNormalLoot();
				total = settingT.maxNormalLoot() - randomCount + 1;
				extraHeroList = XsgAttackCastleManager.getInstance().getNormalExtraHeroT();
			} else {
				minCount = settingT.minFirstLoot();
				total = settingT.maxFirstLoot() - randomCount + 1;
				extraHeroList = XsgAttackCastleManager.getInstance().getFirstExtraHeroT();
			}

			if (randomCount >= minCount) {
				if (total <= 1 || NumberUtil.isHit(1, total)) {
					// 执行计算额外掉落逻辑
					List<RandomShopReward> randomList = new ArrayList<RandomShopReward>();
					for (AttackCastleExtraHeroT extraT : extraHeroList) {
						randomList.add(new RandomShopReward(extraT.id, extraT.pro));
					}
					RandomRange<RandomShopReward> randomRewardGen = new RandomRange<RandomShopReward>(randomList);
					RandomShopReward randomReward = randomRewardGen.random();

					return randomReward.id;
				}
			}
		}

		return 0;
	}

	@Override
	public CastleNodeView beginAttackCastle(int castleNodeIndex) throws NoteException {
		if (lastOpponentInfo == null) {
			throw new NoteException(Messages.getString("AttackCastleController.16")); //$NON-NLS-1$
		}
		CastleNodeView castleNodeView = new CastleNodeView();
		castleNodeView.id = castleNodeIndex;
		castleNodeView.opponent = lastOpponentInfo.battlePower.getPvpView();
		movieIdContext = XsgFightMovieManager.getInstance().generateMovieId(Type.AttackCastle, this.role,
				lastOpponentInfo.view.id, castleNodeView.opponent);
		castleNodeView.movieId = movieIdContext;

		beginAttackCastle.onAttackCastleBegin(castleNodeIndex, lastOpponentInfo.battlePower.getRoleId());

		return castleNodeView;
	}

	@Override
	public void refreshOpponent(int castleNodeId, final Callback cbfunc) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		// 不能超越打关卡
		if (castleNodeId > (currentNodeId + 1)) {
			throw new NoteException(Messages.getString("AttackCastleController.1")); //$NON-NLS-1$
		}
		// 已经打过的关卡不能重复打
		if (castleNodeId < currentNodeId) {
			throw new NoteException(Messages.getString("AttackCastleController.2")); //$NON-NLS-1$
		}
		// 获取关卡信息
		AttackCastleNodeSettingT nodeSetting = XsgAttackCastleManager.getInstance().getNodeSetting(castleNodeId);
		if (nodeSetting == null || TextUtil.isBlank(nodeSetting.matchLv) || TextUtil.isBlank(nodeSetting.matchPower)) {
			throw new NoteException(Messages.getString("AttackCastleController.3")); //$NON-NLS-1$
		}

		if (nodeSetting.isOpen == 0) {
			throw new NoteException(Messages.getString("AttackCastleController.4")); //$NON-NLS-1$
		}

		RefreshEntity refreshEntity = canRefresh(castleNodeId);
		if (!refreshEntity.canRefresh) {
			throw new NoteException(Messages.getString("AttackCastleController.canNotRefresh"));
		}

		if (role.getTotalYuanbao() < refreshEntity.price) {
			throw new NotEnoughYuanBaoException();
		}
		role.reduceCurrency(new Money(CurrencyType.Yuanbao, refreshEntity.price));

		if (!refreshLevelMap.containsKey(castleNodeId)) {
			refreshLevelMap.put(castleNodeId, 1);
		} else {
			refreshLevelMap.put(castleNodeId, refreshLevelMap.get(castleNodeId) + 1);
		}

		AttackCastleParamSettingT setting = XsgAttackCastleManager.getInstance().getParamSetting();
		BattlePowerSnapshotQueryResult opponent = rematchOpponent(setting, nodeSetting, castleNodeId, true);

		saveToRole();

		responseOpponentView(castleNodeId, opponent, cbfunc);
	}

	@Override
	public void exitAttackCastle(int castleNodeId) throws NoteException {
		if (!TextUtil.isBlank(movieIdContext)) {
			XsgFightMovieManager.getInstance().anomalyEndFightMovie(this.role.getRoleId(), movieIdContext, 0, (byte) 0);
		}
	}

	/**
	 * 获取总共可以重置的次数
	 * 
	 * @param vipLevel
	 *            vip等级
	 * @return 总共可以重置的次数
	 * */
	private int getResetCount(int vipLevel) {
		AttackCastleParamSettingT paramSetting = XsgAttackCastleManager.getInstance().getParamSetting();
		int count = 0;
		if (paramSetting != null) {
			count += paramSetting.attackNum;
			// 达到指定vip等级要加上额外次数
			if (vipLevel >= paramSetting.vipLv) {
				count += paramSetting.extraNum;
			}
		}
		return count;
	}

	/** 重置 */
	private void doResetNode() throws NoteException {
		AttackCastleParamSettingT paramSetting = XsgAttackCastleManager.getInstance().getParamSetting();
		if (paramSetting == null) {
			throw new NoteException(Messages.getString("AttackCastleController.17")); //$NON-NLS-1$
		}

		lastResetNodeId = currentNodeId;
		lastOpponentInfo = null;
		currentNodeId = 0;
		castleNodeMap.clear();
		attackContext = new AttackContext();
		if (matchedOpponentIdSet == null) {
			matchedOpponentIdSet = new HashSet<String>();
		}
		matchedOpponentIdSet.clear();

		if (refreshLevelMap == null) {
			refreshLevelMap = new HashMap<Integer, Integer>();
		}
		refreshLevelMap.clear();
	}

	/**
	 * 获取某星级对应的奖品TC
	 * 
	 * @return 如果返回 null, 则表示所选星级非法
	 * */
	private String getRewardTC(int starCount, AttackCastleNodeSettingT nodeSetting) {
		String rewardTC = null;
		switch (starCount) {
		case 3:
			rewardTC = nodeSetting.rewardTC1;
			break;
		case 4:
			rewardTC = nodeSetting.rewardTC2;
			break;
		case 5:
			rewardTC = nodeSetting.rewardTC3;
			break;
		}
		return rewardTC;
	}

	/** 获取武将和配置的映射关系 */
	private Map<String, Byte> getHeroPositionMap(FormationView view) {
		Map<String, Byte> posMap = new HashMap<String, Byte>();
		FormationPosView[] posList = view.postions;
		if (posList != null) {
			for (FormationPosView pos : posList) {
				posMap.put(pos.heroId, pos.index);
			}
		}
		return posMap;
	}

	/** 组装对手的武将信息 */
	private OpponentInfo getOpponentInfo(BattlePowerSnapshotQueryResult result) {
		OpponentInfo opponent = null;
		if (result != null) {
			PvpOpponentFormationView formation = result.getPvpView();
			if (formation != null && formation.heros != null && formation.view != null) {
				// 获取武将配置映射
				Map<String, Byte> posMap = getHeroPositionMap(formation.view);
				HeroView[] heros = formation.heros;
				List<FormationSummaryView> list = new ArrayList<FormationSummaryView>();
				for (HeroView hero : heros) {
					Byte pos = posMap.get(hero.id);
					if (pos != null) {
						list.add(new FormationSummaryView(pos, hero.templateId, hero.qualityLevel, hero.star,
								hero.level, hero.color, hero.breakLevel, hero.awakenState == 2 ? true : false));
					}
				}
				CastleOpponentView castleView = new CastleOpponentView();
				castleView.guardHeroArr = list.toArray(new FormationSummaryView[0]);
				opponent = new OpponentInfo(result, castleView);
			}
		}
		return opponent;
	}

	private static class RefreshEntity {
		public boolean canRefresh;
		public int price;
	}

	private static class RandomShopReward implements IRandomHitable {
		public int id;
		public int rank;

		public RandomShopReward(int id, int rank) {
			this.id = id;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}

		/**
		 * 出现概率设置为0, 使他不会被随机出来
		 * */
		public void disappear() {
			this.rank = 0;
		}
	}

	/** 用于异步查询IRole后的回调 */
	private static class LoadOpponent implements Runnable {
		private String _id;
		private Callback _cb;
		private boolean _success;

		/**
		 * 用于异步查询后的回调
		 * 
		 * @param id
		 *            查询的用户的Id
		 * @param cb
		 *            查询结束后的回调函数
		 * @param success
		 *            查询结果是否成功
		 * */
		public LoadOpponent(String id, Callback cb, boolean success) {
			_id = id;
			_cb = cb;
			_success = success;
		}

		@Override
		public void run() {
			CastleOpponentView castleView = null;
			// 异步查询成功
			if (_success && !TextUtil.isBlank(_id)) {
				IRole opponentRole = XsgRoleManager.getInstance().findRoleById(_id);
				if (opponentRole != null) {
					castleView = new CastleOpponentView();
					castleView.id = opponentRole.getRoleId();
					castleView.name = opponentRole.getName();
					castleView.icon = opponentRole.getHeadImage();
					castleView.level = opponentRole.getLevel();
					castleView.vip = opponentRole.getVipLevel();
					castleView.sex = opponentRole.getSex();
					castleView.prestige = opponentRole.getPrestige();
				}
			}
			if (_cb != null) {
				_cb.cb(_success, castleView);
			}
		}
	};

	/** 是否需要重置, 只有第一次进入北伐系统才会产生一次重置, 否则只能玩家手动重置 */
	private boolean needResetNode() {
		if (lastResetNodeDate == null) {
			return true;
		}
		return false;
	}

	/** 根据日期调整重置次数 */
	private void setupResetNodeCount() {
		AttackCastleParamSettingT setting = XsgAttackCastleManager.getInstance().getParamSetting();
		if (setting != null && !TextUtil.isBlank(setting.resetChanceTime)) {
			if (DateUtil.isPass(DateUtil.joinTime(setting.resetChanceTime), lastResetNodeDate)) {
				resetNodeCount = 0;
				lastResetNodeDate = Calendar.getInstance().getTime();
				saveToRole();
			}
		}
	}

	private AttackCastleView buildAttackCastleView() {
		AttackCastleView attackCastleView = new AttackCastleView();
		attackCastleView.currentNodeId = currentNodeId;
		attackCastleView.attackCount = getResetCount(role.getVipLevel()) - resetNodeCount;

		// 重置时间,秒为单位
		Date resetTime = getResetShopDate();
		Date currentTime = Calendar.getInstance().getTime();
		long resetInterval = DateUtil.compareTime(resetTime, currentTime) / 1000;
		attackCastleView.resetTime = resetInterval;

		attackCastleView.prestige = getCoin();

		// 设置已玩过的关卡信息
		List<CastleNodeView> nodeViewList = new ArrayList<CastleNodeView>();
		for (CastleNodeEntity entity : castleNodeMap.values()) {
			CastleNodeView view = new CastleNodeView();
			view.id = entity.getId();
			Property prop = new Property("star", entity.getStar()); //$NON-NLS-1$
			view.properties = new Property[] { prop };
			nodeViewList.add(view);
		}
		AttackCastleParamSettingT setting = XsgAttackCastleManager.getInstance().getParamSetting();
		// 扫荡VIp等级
		attackCastleView.clearVipLevel = setting.minClearVipLevel;
		// 是否可以扫荡
		attackCastleView.clear = canClear();
		attackCastleView.castleNodes = nodeViewList.toArray(new CastleNodeView[0]);
		return attackCastleView;
	}

	/**
	 * 获取某个时刻下次（今天或者明天的这个时刻，如果今天的这个时刻已经过去，就返回明天的）的Date表示形式
	 * */
	private Date getNextDateOfTimeStr(String timeStr) {
		Calendar time = DateUtil.convertCal(DateUtil.parseDate("HH:mm", timeStr)); //$NON-NLS-1$
		long currentInterval = DateUtil
				.betweenTaskHourMillis(time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
		long current = System.currentTimeMillis();
		Date nextDate = new Date(current + currentInterval);
		return nextDate;
	}

	/**
	 * 返回重置商城的Date
	 * */
	private Date getResetShopDate() {
		AttackCastleParamSettingT paramSetting = XsgAttackCastleManager.getInstance().getParamSetting();
		Date resetTime = getNextDateOfTimeStr(paramSetting.resetTime);
		return resetTime;
	}

	/**
	 * 返回重置关卡的Date, 0点
	 * */
	private Date getResetNodeDate() {
		Date resetTime = getNextDateOfTimeStr("00:00"); //$NON-NLS-1$
		return resetTime;
	}

	/** 匹配一个对手 */
	private BattlePowerSnapshotQueryResult searchOpponent(MatchProperty match) {
		BattlePowerSnapshotQueryResult opponent = null;
		int count = 0;
		while (opponent == null) {
			List<BattlePowerSnapshotQueryResult> opponentList = null;
			try {
				opponentList = XsgFormationDataCollecterManager.getInstance().queryFormationView(match.minPower(),
						match.maxPower(), match.minLevel(), match.maxLevel());
			} catch (Exception e) {
				logger.error("XsgFormationDataCollecterManager.getInstance().queryFormationView", e);
				opponentList = null;
			}
			if (opponentList != null && opponentList.size() > 0) {
				// logger.info("opponentList size: " + opponentList.size());
				// 乱序
				Collections.shuffle(opponentList, new Random());
				// 按照战力从高到低排序
				// Collections.sort(opponentList,
				// new Comparator<BattlePowerSnapshotQueryResult>() {
				// @Override
				// public int compare(
				// BattlePowerSnapshotQueryResult o1,
				// BattlePowerSnapshotQueryResult o2) {
				// return o2.getPower() - o1.getPower();
				// }
				// });
				for (BattlePowerSnapshotQueryResult result : opponentList) {
					if (result == null) {
						continue;
					}
					String opponentId = result.getRoleId();
					boolean canMatchSelf = XsgAttackCastleManager.getInstance().canMatchSelf(currentNodeId + 1);
					// 匹配到自己, 但是当前不能匹配自己, 则跳过
					if (role.getRoleId().equals(opponentId) && !canMatchSelf) {
						continue;
					}
					// 匹配到的敌人不是今天打过的人
					if (!matchedOpponentIdSet.contains(opponentId)) {
						opponent = result;
						matchedOpponentIdSet.add(opponentId);
						break;
					}
				}
			}
			// 最多匹配100次, 匹配不到视为系统异常
			if (opponent != null || count > 100) {
				break;
			}
			match.rematch();
			count++;
		}
		// logger.info("opponent size: " + (matchedOpponentIdSet.size()));
		return opponent;
	}

	/** 匹配的属性,战力和等级范围 */
	static class MatchProperty {
		int minPowerPecent;
		int maxPowerPecent;
		int minLevelInterval;
		int maxLevelInterval;

		int selfPower;
		int selfLevel;

		int direction; // 匹配方向，0：双向；1：向下；2：向上

		/** 重新匹配的次数 */
		int rematchCount = 0;

		public MatchProperty(String powerStr, String levelStr, int selfPower, int selfLevel, int direction)
				throws NoteException {
			String[] powerLimitStr = powerStr.split(";"); //$NON-NLS-1$
			String[] levelLimitStr = levelStr.split(";"); //$NON-NLS-1$
			if (powerLimitStr.length < 2 || levelLimitStr.length < 2) {
				throw new NoteException(Messages.getString("AttackCastleController.23")); //$NON-NLS-1$
			}
			minPowerPecent = Integer.parseInt(powerLimitStr[0]);
			maxPowerPecent = Integer.parseInt(powerLimitStr[1]);
			minLevelInterval = Integer.parseInt(levelLimitStr[0]);
			maxLevelInterval = Integer.parseInt(levelLimitStr[1]);

			this.selfPower = selfPower;
			this.selfLevel = selfLevel;

			this.direction = direction;
		}

		/** 放宽匹配条件 */
		public void rematch() {
			rematchCount++;

			switch (direction) {
			case 0: // 双向
				minPowerPecent -= 5;
				maxPowerPecent += 5;
				break;
			case 1: // 向下
				minPowerPecent -= 5;
				break;
			case 2: // 向上
				maxPowerPecent += 5;
				break;
			default:
				break;
			}

			// 重新匹配3次以上
			if (rematchCount > 3) {
				minLevelInterval -= 2;
				maxLevelInterval += 2;
			}
		}

		public int minPower() {
			return minPowerPecent * selfPower / 100;
		}

		public int maxPower() {
			return maxPowerPecent * selfPower / 100;
		}

		public int minLevel() {
			return minLevelInterval + selfLevel;
		}

		public int maxLevel() {
			return maxLevelInterval + selfLevel;
		}

		public int getRematchCount() {
			return rematchCount;
		}
	}

	/**
	 * 北伐战斗上下文
	 * */
	static class AttackContext {
		public byte originHeroCount; // 武将初始数量
		public int remainStarCount; // 剩余的星数,
									// 每过一关会加对应的星级到剩余星数上,选取某个星数的奖品会减掉对应的星数
	}

	public static interface Callback {
		void cb(boolean success, CastleOpponentView castleView);
	}

	private boolean needInitShop() {
		if (lastResetShopDate == null || shopRewards == null || shopRewards.size() <= 0) {
			return true;
		}
		return DateUtil.isPass(getResetShopDate(), lastResetShopDate);
	}

	/**
	 * 刷新商城商品
	 * */
	private void doRefreshShopRewards() throws NoteException {
		AttackCastleParamSettingT paramSetting = XsgAttackCastleManager.getInstance().getParamSetting();
		if (paramSetting == null) {
			throw new NoteException(Messages.getString("AttackCastleController.24")); //$NON-NLS-1$
		}
		int goodsCount = paramSetting.goodsNum;
		List<AttackCastleShopRewardT> randomList = new ArrayList<AttackCastleShopRewardT>();
		Map<Integer, AttackCastleShopRewardT> rewards = XsgAttackCastleManager.getInstance().getShopRewardsMap();
		for (AttackCastleShopRewardT r : rewards.values()) {
			if (r.maxNum > 0) {
				randomList.add(r);
			}
		}
		if (randomList.size() <= 0) {
			logger.error(Messages.getString("AttackCastleController.25")); //$NON-NLS-1$
		}

		// 合并概率
		List<RandomShopReward> randomRewardList = new ArrayList<RandomShopReward>();
		Map<Integer, RandomShopReward> randomRewardMap = new HashMap<Integer, RandomShopReward>();
		for (AttackCastleShopRewardT storeT : rewards.values()) {
			RandomShopReward r = new RandomShopReward(storeT.id, storeT.weight);
			randomRewardList.add(r);
			randomRewardMap.put(r.id, r);
		}

		shopRewards.clear();
		// 概率计算
		for (int i = 0; i < goodsCount; i++) {
			RandomRange<RandomShopReward> randomRewardGen = new RandomRange<RandomShopReward>(randomRewardList);
			RandomShopReward randomReward = randomRewardGen.random();
			AttackCastleShopRewardT t = rewards.get(randomReward.id);
			int num = NumberUtil.random(t.minNum, t.maxNum + 1);
			int price = num * t.price;
			AttackCastleShopItemView shopView = new AttackCastleShopItemView(t.id, t.itemId, num, price, t.coinType,
					CAN_BUY);
			shopRewards.put(shopView.id, shopView);
			// 随机出来过的概率设置为0, 使他不会再被随机出来
			randomRewardMap.get(randomReward.id).disappear();
		}
	}

	@Override
	public AttackCastleShopView getShopRewardList() throws NoteException {
		if (needInitShop()) {
			doRefreshShopRewards();
			refreshShopCount = 0;
			lastResetShopDate = Calendar.getInstance().getTime();
		}
		AttackCastleShopView shopView = new AttackCastleShopView();
		shopView.refreshCount = refreshShopCount;
		shopView.itemList = shopRewards.values().toArray(new AttackCastleShopItemView[0]);
		saveToRole();
		return shopView;
	}

	@Override
	public AttackCastleShopView refreshShopRewardList() throws NoteException {
		AttackCastleExchangeSettingT exchangeSetting = XsgAttackCastleManager.getInstance().getExchangeSetting(
				refreshShopCount + 1);
		if (exchangeSetting == null) {
			throw new NoteException(Messages.getString("AttackCastleController.26")); //$NON-NLS-1$
		}
		int price = exchangeSetting.consume;
		if (getCoin() < price) {
			throw new NoteException(Messages.getString("AttackCastleController.27")); //$NON-NLS-1$
		}
		doRefreshShopRewards();

		refreshShopCount++;

		reduceCoin(price);

		AttackCastleShopView shopView = new AttackCastleShopView();
		shopView.refreshCount = refreshShopCount;
		shopView.itemList = shopRewards.values().toArray(new AttackCastleShopItemView[0]);
		saveToRole();

		refreshStore.onRefreshStore(refreshShopCount, price);

		return shopView;
	}

	private boolean canClear() {
		AttackCastleParamSettingT settingT = XsgAttackCastleManager.getInstance().getParamSetting();
		if (lastResetNodeId > 0 && role.getVipLevel() >= settingT.minClearVipLevel
				&& (getResetCount(role.getVipLevel()) - resetNodeCount) <= settingT.maxClearLastTime) {
			return true;
		}
		return false;
	}

	@Override
	public ClearResultView clear() throws NoteException {
		AttackCastleParamSettingT settingT = XsgAttackCastleManager.getInstance().getParamSetting();
		if (role.getLevel() < settingT.minClearVipLevel) {
			throw new NoteException(Messages.getString("AttackCastleController.levelNotEnough"));
		}
		if (currentNodeId > (settingT.startClearLevel - 1)) {
			throw new NoteException(TextUtil.format(Messages.getString("AttackCastleController.levelLimit"),
					settingT.startClearLevel));
		}
		if (!canClear()) {
			throw new NoteException(Messages.getString("AttackCastleController.canNotClear"));
		}
		// 扫荡
		List<CopyClearResultView> views = new ArrayList<CopyClearResultView>();
		List<HeroSoulPair> extraHeroViews = new ArrayList<HeroSoulPair>();
		List<ItemView> rewardItems = new ArrayList<ItemView>();
		XsgRewardManager rewardManager = XsgRewardManager.getInstance();
		// 上次停留在宝箱关卡，则自动往前一关, 也就是扫荡的时候把最后这个没开启的宝箱开掉
		if (lastResetNodeId % 2 != 0) {
			lastResetNodeId += 1;
		}
		for (int i = 1; i <= lastResetNodeId; i++) {
			if (i % 2 != 0) { // 战斗节点
				CastleNodeEntity entity = new CastleNodeEntity(i, 5);
				castleNodeMap.put(entity.getId(), entity);
				attackContext.remainStarCount = entity.getStar();
			} else { // 奖励节点，领取奖励
				AttackCastleNodeSettingT rewardSetting = XsgAttackCastleManager.getInstance().getRewardSetting(i);
				// 宝箱的节点编号比关卡多1
				CastleNodeEntity castleEntity = castleNodeMap.get(i - 1);
				if (rewardSetting != null && castleEntity != null && !castleEntity.hasAcceptReward()) {
					String rewardTC = getRewardTC(castleEntity.getStar(), rewardSetting);
					if (!TextUtil.isBlank(rewardTC)) {
						// 执行宝箱TC
						TcResult result = rewardManager.doTc(this.role, rewardTC);
						// 金币=基础金币X（1+星级*5%+玩家等级*1%）
						int money = rewardSetting.coinNum * (100 + castleEntity.getStar() * 5 + role.getLevel()) / 100;
						switch (rewardSetting.coinType) {
						case 0: // 金币
							result.appendProperty(Const.PropertyName.MONEY, money);
							break;
						case 1: // 元宝
							result.appendProperty(Const.PropertyName.RMBY, money);
							break;
						}
						
						result.appendProperty(Const.PropertyName.MONEY, role.getFactionControler().getTechnologyValue(102));

						// 附加声望到显示的奖励列表里面
						if (rewardSetting.reputation > 0) {
							result.appendProperty(CoinTemplateId, rewardSetting.reputation);
						}
						// 计算额外整卡奖励
						ExtraHeroSoul extraHero = getExtraHeroRewardId(5, result);
						extraHeroViews.add(new HeroSoulPair(i, extraHero.extraHeroId, extraHero.alreadyHas,
								extraHero.heroSoulNum));
						views.add(new CopyClearResultView(i, rewardManager.generateItemView(result), new ItemView[0], 0));

						ItemView[] itemViews = rewardManager.generateItemView(result);
						rewardItems.addAll(Arrays.asList(itemViews));

						castleEntity.acceptReward();
					}
				}

			}
		}

		// 给玩家加宝箱和金币及声望
		if (rewardItems != null && rewardItems.size() > 0) {
			role.getRewardControler().acceptReward(rewardItems.toArray(new ItemView[0]));
		}

		currentNodeId = lastResetNodeId;

		saveToRole();

		return new ClearResultView(views.toArray(new CopyClearResultView[0]),
				extraHeroViews.toArray(new HeroSoulPair[0]));
	}

	@Override
	public void exchangeItem(int itemId) throws NoteException {
		AttackCastleShopItemView rewardView = shopRewards.get(itemId);
		if (rewardView == null) {
			throw new NoteException(Messages.getString("AttackCastleController.28")); //$NON-NLS-1$
		}
		if (getCoin() < rewardView.price) {
			throw new NoteException(Messages.getString("AttackCastleController.29")); //$NON-NLS-1$
		}
		if (rewardView.flag == 0) {
			throw new NoteException(Messages.getString("AttackCastleController.30")); //$NON-NLS-1$
		}
		// type 大于0表示该vip等级以上的才可以购买
		if (rewardView.coinType > 0 && rewardView.coinType > role.getVipLevel()) {
			throw new NoteException(Messages.getString("AttackCastleController.31")); //$NON-NLS-1$
		}

		// 减少对应的声望
		reduceCoin(rewardView.price);
		// 增加装备
		role.getItemControler().changeItemByTemplateCode(rewardView.itemId, rewardView.num);
		// 设置已售罄标记
		rewardView.flag = SOLD_OUT;
		saveToRole();

		exchangeReward.onExchange(rewardView);
	}

	/**
	 * 获取声望
	 * */
	@Override
	public int getCoin() {
		return attackCastleCoinCount;
	}

	@Override
	public void addCoin(int num) {
		attackCastleCoinCount += num;
		if (roleAttackCastle == null) {
			return;
		}
		roleAttackCastle.setAttackCastleCoinCount(attackCastleCoinCount);
	}

	/**
	 * 减少声望
	 * 
	 * @throws NoteException
	 *             声望不足
	 * */
	private void reduceCoin(int num) throws NoteException {
		if (num > attackCastleCoinCount) {
			throw new NoteException(Messages.getString("AttackCastleController.32")); //$NON-NLS-1$
		}
		attackCastleCoinCount -= num;
	}

	private void saveToRole() {
		if (roleAttackCastle == null) {
			return;
		}

		InnerDBWarper warper = new InnerDBWarper();
		warper.attackContext = attackContext;
		warper.lastOpponentInfo = lastOpponentInfo;
		warper.shopRewardsList = new ArrayList<AttackCastleShopItemView>(shopRewards.values());
		warper.castleNodeList = new ArrayList<CastleNodeEntity>(castleNodeMap.values());
		warper.matchedOpponent = matchedOpponentIdSet;
		warper.lastResetNodeId = lastResetNodeId;
		warper.refreshLevelMap = refreshLevelMap;
		warper.randomCount = randomCount;
		warper.isNormalRandom = isNormalRandom;
		String jsonStr = TextUtil.GSON.toJson(warper);

		roleAttackCastle.setCurrentNodeId(currentNodeId);
		roleAttackCastle.setAttackCastleCoinCount(attackCastleCoinCount);
		roleAttackCastle.setResetNodeCount(resetNodeCount);
		roleAttackCastle.setRefreshShopCount(refreshShopCount);
		roleAttackCastle.setLastResetNodeDate(lastResetNodeDate);
		roleAttackCastle.setLastResetShopDate(lastResetShopDate);
		roleAttackCastle.setExtraJsonStr(jsonStr);
		roleDB.setRoleAttackCastle(roleAttackCastle);
	}

	private void initFromDB() {
		if (roleAttackCastle == null) {
			return;
		}
		currentNodeId = roleAttackCastle.getCurrentNodeId();
		attackCastleCoinCount = roleAttackCastle.getAttackCastleCoinCount();
		resetNodeCount = roleAttackCastle.getResetNodeCount();
		refreshShopCount = roleAttackCastle.getRefreshShopCount();
		lastResetNodeDate = roleAttackCastle.getLastResetNodeDate();
		lastResetShopDate = roleAttackCastle.getLastResetShopDate();
		String jsonStr = roleAttackCastle.getExtraJsonStr();
		InnerDBWarper warper = TextUtil.GSON.fromJson(jsonStr, InnerDBWarper.class);
		if (warper != null) {
			attackContext = warper.attackContext;
			lastOpponentInfo = warper.lastOpponentInfo;
			List<AttackCastleShopItemView> shopRewardsList = warper.shopRewardsList;
			List<CastleNodeEntity> castleNodeList = warper.castleNodeList;
			matchedOpponentIdSet = warper.matchedOpponent;
			if (shopRewardsList != null) {
				for (AttackCastleShopItemView view : shopRewardsList) {
					shopRewards.put(view.id, view);
				}
			}
			if (castleNodeList != null) {
				for (CastleNodeEntity entity : castleNodeList) {
					castleNodeMap.put(entity.getId(), entity);
				}
			}
			lastResetNodeId = warper.lastResetNodeId;
			refreshLevelMap = warper.refreshLevelMap;
			randomCount = warper.randomCount;
			isNormalRandom = warper.isNormalRandom;
			if (refreshLevelMap == null) {
				refreshLevelMap = new HashMap<Integer, Integer>();
			}
		}
	}

	/** 对手信息 */
	private static class OpponentInfo {
		private BattlePowerSnapshotQueryResult battlePower;
		private CastleOpponentView view;

		public OpponentInfo(BattlePowerSnapshotQueryResult result, CastleOpponentView v) {
			this.battlePower = result;
			this.view = v;
		}
	}

	/** 内部的DB包装类, 用于用json序列化和反序列化数据 */
	private static class InnerDBWarper {
		AttackContext attackContext; // 战斗上下文
		OpponentInfo lastOpponentInfo; // 上个对手信息
		List<AttackCastleShopItemView> shopRewardsList; // 商城商品列表
		List<CastleNodeEntity> castleNodeList; // 已经打过的关卡信息
		Set<String> matchedOpponent; // 已经匹配过的对手
		Map<Integer, Integer> refreshLevelMap; // 刷新过的关卡
		int lastResetNodeId;
		int randomCount = 0; // 执行随机的次数
		boolean isNormalRandom = false; // 是否执行普通掉落
	}

	public static class ExtraHeroSoul {
		public boolean alreadyHas = false;
		public int extraHeroId = 0;
		public int heroSoulNum = 0;

		public ExtraHeroSoul(boolean already, int extraHeroId, int num) {
			this.alreadyHas = already;
			this.extraHeroId = extraHeroId;
			this.heroSoulNum = num;
		}
	}

	@Override
	public int getTotalStar() {
		int total = 0;
		if (castleNodeMap != null && castleNodeMap.size() > 0) {
			for (CastleNodeEntity entity : castleNodeMap.values()) {
				if (entity != null) {
					total += entity.getStar();
				}
			}
		}
		return total;
	}
}
