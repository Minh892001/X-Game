package com.morefun.XSanGo.achieve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.XSanGo.Protocol.AchieveInfoSub;
import com.XSanGo.Protocol.AchievePageView;
import com.XSanGo.Protocol.AchieveProAwardSub;
import com.XSanGo.Protocol.CopyChallengeResultView;
import com.XSanGo.Protocol.CopyDifficulty;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.HeroPracticeView;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.QualityColor;
import com.XSanGo.Protocol.TaskType;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.achieve.XsgAchieveManager.AchieveTemplate;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.copy.SmallCopyT;
import com.morefun.XSanGo.db.game.AchieveFirstNotify;
import com.morefun.XSanGo.db.game.HeroPractice;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleAchieve;
import com.morefun.XSanGo.db.game.RoleAchieveProgressRec;
import com.morefun.XSanGo.db.game.RoleTask;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IArenaFight;
import com.morefun.XSanGo.event.protocol.IArenaFirstWin;
import com.morefun.XSanGo.event.protocol.IAttackCastleEnd;
import com.morefun.XSanGo.event.protocol.IAuctionHouseSettle;
import com.morefun.XSanGo.event.protocol.IBuyFactionShop;
import com.morefun.XSanGo.event.protocol.ICombatPowerChange;
import com.morefun.XSanGo.event.protocol.ICopyClear;
import com.morefun.XSanGo.event.protocol.ICopyCompleted;
import com.morefun.XSanGo.event.protocol.IEquipRebuild;
import com.morefun.XSanGo.event.protocol.IEquipStarUp;
import com.morefun.XSanGo.event.protocol.IEquipStrengthen;
import com.morefun.XSanGo.event.protocol.IFactionBattleDiggingTreasure;
import com.morefun.XSanGo.event.protocol.IFactionCopyEndChallenge;
import com.morefun.XSanGo.event.protocol.IFactionDonation;
import com.morefun.XSanGo.event.protocol.IFormationBuffLevelUp;
import com.morefun.XSanGo.event.protocol.IFormationPosChange;
import com.morefun.XSanGo.event.protocol.IFriendApplying;
import com.morefun.XSanGo.event.protocol.IFriendFight;
import com.morefun.XSanGo.event.protocol.IGetAchieve;
import com.morefun.XSanGo.event.protocol.IGetTask;
import com.morefun.XSanGo.event.protocol.IHeroBreakUp;
import com.morefun.XSanGo.event.protocol.IHeroJoin;
import com.morefun.XSanGo.event.protocol.IHeroPractice;
import com.morefun.XSanGo.event.protocol.IHeroSkillUp;
import com.morefun.XSanGo.event.protocol.IHeroStarUp;
import com.morefun.XSanGo.event.protocol.IItemCountChange;
import com.morefun.XSanGo.event.protocol.IJinbiChange;
import com.morefun.XSanGo.event.protocol.ILadderFight;
import com.morefun.XSanGo.event.protocol.ILadderLevelChange;
import com.morefun.XSanGo.event.protocol.ILuckyStar;
import com.morefun.XSanGo.event.protocol.IOnlineAward;
import com.morefun.XSanGo.event.protocol.IPartnerPosChange;
import com.morefun.XSanGo.event.protocol.IPartnerPosOpen;
import com.morefun.XSanGo.event.protocol.IPartnerPosReset;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.event.protocol.ISnsFriendPoint;
import com.morefun.XSanGo.event.protocol.ISnsRelationChange;
import com.morefun.XSanGo.event.protocol.ISnsSendJunLing;
import com.morefun.XSanGo.event.protocol.ITimeBattle;
import com.morefun.XSanGo.event.protocol.ITraderCall;
import com.morefun.XSanGo.event.protocol.ITreasureAddGroup;
import com.morefun.XSanGo.event.protocol.ITreasureGain;
import com.morefun.XSanGo.event.protocol.IWarmupEscape;
import com.morefun.XSanGo.event.protocol.IWorldBossBuyInspire;
import com.morefun.XSanGo.event.protocol.IWorldBossEndChallenge;
import com.morefun.XSanGo.event.protocol.IWorldBossTailAward;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.PracticePropT;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.FormationBuffItem;
import com.morefun.XSanGo.item.GemT;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.partner.IPartner;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.sns.SNSType;
import com.morefun.XSanGo.sns.SnsController.RelationChangeEventActionType;
import com.morefun.XSanGo.task.TaskTemplT;
import com.morefun.XSanGo.task.XsgTaskManager;
import com.morefun.XSanGo.timeBattle.TimeBattleT;
import com.morefun.XSanGo.timeBattle.XsgTimeBattleManage;
import com.morefun.XSanGo.trader.TraderType;
import com.morefun.XSanGo.treasure.TreasureConfT;
import com.morefun.XSanGo.treasure.XsgTreasureManage;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint
public class AchieveControler implements IAchieveControler, IFriendApplying, IWarmupEscape, ICopyCompleted,
		IArenaFirstWin, ITreasureGain, ILuckyStar, ISnsSendJunLing, ILadderLevelChange, ITreasureAddGroup,
		IBuyFactionShop, ICopyClear, IFactionBattleDiggingTreasure, IOnlineAward, IGetTask, IHeroPractice, IAuctionHouseSettle,
		IPartnerPosOpen, IWorldBossEndChallenge, IWorldBossTailAward, IWorldBossBuyInspire, IRoleLevelup, IJinbiChange,
		ISnsRelationChange, IHeroJoin, IItemCountChange, IArenaFight, ITraderCall, ITimeBattle, ILadderFight,
		IFactionCopyEndChallenge, IFactionDonation, IAttackCastleEnd, IHeroBreakUp, IFriendFight, ISnsFriendPoint,
		ICombatPowerChange, IFormationPosChange, IHeroStarUp, IEquipRebuild, IEquipStrengthen, IEquipStarUp,
		IHeroSkillUp, IFormationBuffLevelUp, IPartnerPosChange, IPartnerPosReset{

	private IRole rt;
	private Role db;
	private IGetAchieve getAchieveEvent;

	@Override
	public MajorUIRedPointNote getRedPointNote() {

		boolean flag = false;
		for (int aId : XsgAchieveManager.getInstance().getAchieveMap().keySet()) {
			if (getAchieveStatus(aId) == 1) {
				flag = true;
				break;
			}
		}
		// 增加成就进度奖励的红点
		// if (!flag) {
		// flag = isCanRecProgressAward();
		// }
		if (flag) {
			return new MajorUIRedPointNote(MajorMenu.Achieve, false);
		}
		return null;
	}

	public AchieveControler(IRole rt, Role db) {
		super();
		this.rt = rt;
		this.db = db;
		getAchieveEvent = this.rt.getEventControler().registerEvent(IGetAchieve.class);

		IEventControler evtContrl = rt.getEventControler();

		evtContrl.registerHandler(ICombatPowerChange.class, this);
		evtContrl.registerHandler(IPartnerPosReset.class, this);
		evtContrl.registerHandler(IFriendApplying.class, this);
		evtContrl.registerHandler(IPartnerPosChange.class, this);
		evtContrl.registerHandler(IWarmupEscape.class, this);
		evtContrl.registerHandler(IArenaFirstWin.class, this);
		evtContrl.registerHandler(ITreasureGain.class, this);
		evtContrl.registerHandler(ILuckyStar.class, this);
		evtContrl.registerHandler(ISnsSendJunLing.class, this);
		evtContrl.registerHandler(ILadderLevelChange.class, this);
		evtContrl.registerHandler(ITreasureAddGroup.class, this);
		evtContrl.registerHandler(IBuyFactionShop.class, this);
		evtContrl.registerHandler(ICopyClear.class, this);
		evtContrl.registerHandler(ICopyCompleted.class, this);
		evtContrl.registerHandler(IEquipStrengthen.class, this);
		evtContrl.registerHandler(IHeroSkillUp.class, this);
		evtContrl.registerHandler(IRoleLevelup.class, this);
		evtContrl.registerHandler(IJinbiChange.class, this);
		evtContrl.registerHandler(IHeroStarUp.class, this);
		evtContrl.registerHandler(IHeroJoin.class, this);
		evtContrl.registerHandler(IArenaFight.class, this);
		evtContrl.registerHandler(ITraderCall.class, this);
		evtContrl.registerHandler(ITimeBattle.class, this);
		evtContrl.registerHandler(IEquipRebuild.class, this);
		evtContrl.registerHandler(ILadderFight.class, this);
		evtContrl.registerHandler(IFactionDonation.class, this);
		evtContrl.registerHandler(IFactionCopyEndChallenge.class, this);
		evtContrl.registerHandler(IAttackCastleEnd.class, this);
		evtContrl.registerHandler(IFriendFight.class, this);
		evtContrl.registerHandler(ISnsFriendPoint.class, this);
		evtContrl.registerHandler(IHeroBreakUp.class, this);
		evtContrl.registerHandler(IHeroPractice.class, this);
		evtContrl.registerHandler(IItemCountChange.class, this);
		evtContrl.registerHandler(IEquipStarUp.class, this);
		evtContrl.registerHandler(IFormationBuffLevelUp.class, this);
		evtContrl.registerHandler(IPartnerPosOpen.class, this);
		evtContrl.registerHandler(ISnsRelationChange.class, this);
		evtContrl.registerHandler(IAuctionHouseSettle.class, this);
		evtContrl.registerHandler(IGetTask.class, this);
		evtContrl.registerHandler(IOnlineAward.class, this);
		evtContrl.registerHandler(IFactionBattleDiggingTreasure.class, this);

		evtContrl.registerHandler(IWorldBossEndChallenge.class, this);
		evtContrl.registerHandler(IWorldBossTailAward.class, this);
		evtContrl.registerHandler(IWorldBossBuyInspire.class, this);
		evtContrl.registerHandler(IFormationPosChange.class, this);
		evtContrl.registerHandler(IFactionBattleDiggingTreasure.class, this);
	}

	/**
	 * 获得成就进度奖励视图
	 * 
	 * @return
	 */
	public String getAchieveProgressView() throws NoteException {
		TreeMap<Integer, AchieveProAwardT> proConfigMap = XsgAchieveManager.getInstance().getProConfigMap();
		AchieveProAwardSub[] view = new AchieveProAwardSub[proConfigMap.size()];
		int index = 0;
		for (AchieveProAwardT t : proConfigMap.values()) {
			AchieveProAwardSub as = new AchieveProAwardSub();
			as.progress = t.progress;
			as.awards = XsgAchieveManager.getInstance().convertMap2StringIntArray(t.itemMap);
			as.status = getProgressStatus(t.progress);
			as.tipstx = t.tipsTx;
			view[index] = as;
			index += 1;
		}
		return LuaSerializer.serialize(view);
	}

	/**
	 * 是否可领取 进度奖励
	 * 
	 * @return
	 */
	private boolean isCanRecProgressAward() {
		TreeMap<Integer, AchieveProAwardT> proConfigMap = XsgAchieveManager.getInstance().getProConfigMap();
		for (Integer progress : proConfigMap.keySet()) {
			if (getProgressStatus(progress) == 1)
				return true;
		}
		return false;
	}

	/**
	 * 获得成就进度奖励状态 0:未完成 1：可领取 2：已领取
	 * 
	 * @param progress
	 * @return
	 */
	private int getProgressStatus(int progress) {
		TreeMap<Integer, AchieveProAwardT> proConfigMap = XsgAchieveManager.getInstance().getProConfigMap();
		AchieveProAwardT t = proConfigMap.get(progress);
		if (t == null)
			return 0;
		RoleAchieveProgressRec roleAchieveProgressRec = db.getRoleAchieveProgressRec();
		if (roleAchieveProgressRec != null && !TextUtil.isBlank(roleAchieveProgressRec.getReceived())) {
			if (roleAchieveProgressRec.getReceived().indexOf("," + progress + ",") != -1) {
				return 2;
			}
		}
		if (db.getAchieveCompletedCount() >= progress) {
			return 1;
		}
		return 0;
	}

	public String recProgressAward(int progress) throws NoteException {
		TreeMap<Integer, AchieveProAwardT> proConfigMap = XsgAchieveManager.getInstance().getProConfigMap();
		AchieveProAwardT t = proConfigMap.get(progress);
		if (t == null)
			throw new NoteException(Messages.getString("AchieveControler.0"));
		if (getProgressStatus(progress) != 1)
			throw new NoteException(Messages.getString("AchieveControler.1"));
		RoleAchieveProgressRec roleAchieveProgressRec = db.getRoleAchieveProgressRec();
		if (roleAchieveProgressRec != null) {
			if (!TextUtil.isBlank(roleAchieveProgressRec.getReceived())) {
				roleAchieveProgressRec.setReceived(roleAchieveProgressRec.getReceived() + progress + ",");
			} else {
				roleAchieveProgressRec.setReceived("," + progress + ",");
			}
			roleAchieveProgressRec.setUpdateDate(new Date());
		} else {
			roleAchieveProgressRec = new RoleAchieveProgressRec(GlobalDataManager.getInstance().generatePrimaryKey(),
					db, "," + progress + ",", new Date());
			db.setRoleAchieveProgressRec(roleAchieveProgressRec);
		}
		String headPrefix = "tx:"; // 头像奖励标识
		if (t.itemMap != null && t.itemMap.size() > 0) {
			for (String item : t.itemMap.keySet()) {
				if (!item.startsWith(headPrefix)) {
					this.rt.getRewardControler().acceptReward(item, t.itemMap.get(item));
				} else {
					// 新增一个扩展头像字段
					this.rt.addExtHeadImage(item.substring(item.indexOf(headPrefix) + headPrefix.length(),
							item.length()));

					// 头像属性变更通知
					this.rt.getNotifyControler().onStrPropertyChange(Const.PropertyName.HEAD_IMAGE,
							LuaSerializer.serialize(this.rt.getExtHeadImage()));
				}
			}
		}
		return getAchieveProgressView();
	}

	/**
	 * 红点提示
	 */
	private void notifyRedPoint() {
		this.rt.getNotifyControler().onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.Achieve, false));
	}

	/**
	 * 是否已经领取过成就奖励
	 * 
	 * @param AchieveId
	 * @return
	 */
	private boolean isRecAward(int achieveId) {
		Map<Integer, AchieveT> achieveMap = XsgAchieveManager.getInstance().getAchieveMap();
		AchieveT baseConfig = achieveMap.get(achieveId);
		if (null == baseConfig)
			return false;
		String type = baseConfig.type;
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		// if (null == achieve4Type) {
		// 3.0.5版本完善了成就进度，部分成就为了记录进度，新增了成就类型。所以为了兼容老数据，判断玩家是否完成改成就，不能感觉单纯的老类型判断
		for (RoleAchieve ra : roleAchieves.values()) {
			String received = ra.getReceived();
			if (null != received && Arrays.asList(received.split(",")).contains(String.valueOf(achieveId))) {
				return true;
			}
		}
		// return false;
		// }
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (achieve4Type != null) {
			String received = achieve4Type.getReceived();
			if (null != received && received.indexOf(achieveId + ",") != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 领取奖励 跟新数据库持久化对象
	 * 
	 * @param AchieveId
	 */
	private void addAchieveAward2DB(int achieveId) {
		Map<Integer, AchieveT> achieveMap = XsgAchieveManager.getInstance().getAchieveMap();
		AchieveT baseConfig = achieveMap.get(achieveId);
		String type = baseConfig.type;
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null == achieve4Type) {
			// 这两种类型由于前期无需跟新 即这里增加
			if (!type.equals(AchieveTemplate.AllAchievement.name()) && !type.equals(AchieveTemplate.PVP.name())
					&& !type.equals(AchieveTemplate.PVE1.name()) && !type.equals(AchieveTemplate.PVE2.name()))
				return;
			achieve4Type = new RoleAchieve(GlobalDataManager.getInstance().generatePrimaryKey(), db, type, "",
					new Date());
			achieve4Type.setRecDate(new Date());
			roleAchieves.put(type, achieve4Type);
		}

		String received = achieve4Type.getReceived();
		if (null != received)
			achieve4Type.setReceived(received + achieveId + ",");
		else
			achieve4Type.setReceived(achieveId + ",");
		db.setAchieveCompletedCount(db.getAchieveCompletedCount() + 1);
		achieve4Type.setRecDate(new Date());
	}

	/**
	 * 跟新成就状态
	 * 
	 * @param type
	 *            成就类型
	 * @param para
	 *            参数
	 */
	public void updateAchieveProgress(String type, String para) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		if (map == null || map.size() == 0)
			return;
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		// 一个类型下的所有成就都领取完 不做更新进度处理
		if (null != achieve4Type && !TextUtil.isBlank(achieve4Type.getReceived())) {
			int recNum = achieve4Type.getReceived().split(",").length;
			int count4Type = map.size();
			if (recNum >= count4Type)
				return;
		}
		// 跟新前当前类型下的成就状态信息
		TreeMap<Integer, Byte> frontStatusMap = getStatusByType(type);
		Map<Integer, Integer> infoMap;
		switch (AchieveTemplate.getAchieveTemplate(type)) {
		case FiveStarPassNum:// 5星通关的关卡达到n个
		case XinshouSweepNum:// 累积扫荡新手关卡n次
		case GaoshouSweepNum:// 累积扫荡高手关卡n次
		case GodSweepNum:// 累积扫荡大神关卡n次
		case XinshouStarNum: // 新手关卡星数达到n颗
		case GaoshouStarNum:// 高手关卡星数达到n颗
		case GodStarNum:// 大神关卡星数达到n颗
		case GodNum:// 挑战大神难度n次
		case InfernalNum:// 挑战地狱难度n次
		case SweepXinshouNum:// 扫荡新手难度n次
		case SweepGaoshouNum:// 扫荡高手难度n次
		case GoldNum:
		case LearnedPoint:
		case FailedTimes:// 竞技场失败n次
		case BeifaStars: // 北伐累积获得n星
		case PassNums:// 通关关卡达到n个
		case LoserNums:// 随机pk认怂333次（最强忍者）
		case TotalHurt:// 对乱世魔王累积造成n点伤害
		case SendJunling:// 送好友军令累积n枚
		case QieChuoCount:// 与好友切磋累积n次
		case GuildDHurtCount:// 团队副本累积伤害达到n
		case DonateJiangzhang:// 累积捐献公会奖章n个
		case AuctionSuccessNums:// 累积竞拍成功n次
		case AuctionNums:// 累积拍卖n次
		case OnlineGiftNums:// 累积领在线礼包n个
		case LuckManNum:// 累计获得运气王n次
		case CompleteDayTaskNum:// 累积完成n个日常任务
		case CompleteMainTaskNum:// 累积完成n个主线任务
		case SPAwardTimes:// 累积获得n次尾刀奖励
		case GuildGiftNums:// 累计兑换N个公会礼包
		case StoneCounts:// 寻宝累计获得N个宝石
		case XunbaoCountTime:// 寻宝累积n小时
			addNums4EveryAction(type, Integer.valueOf(para));
			break;
		case Guanjie:// 累积n季官阶为相国
			ladderLvl(type, para);
			break;
		case Power:
			pointRiseEveryAction(type, Integer.valueOf(para));
			break;
		case KingLv:
			pointRiseEveryAction(type, rt.getLevel());
			break;
		case HeroNum:
			int heroNum = rt.getHeroControler().getHeroNum();
			pointRiseEveryAction(type, heroNum);
			break;
		case FriendNums:// 好友数量
			int friendNum = rt.getSnsController().getFriends().size();
			pointRiseEveryAction(type, friendNum);
			break;
		case FriendPoint:// 与任意好友的友情点累积达到n
			int maxPoint = rt.getSnsController().getMaxFriendPoint();
			pointRiseEveryAction(type, maxPoint);
			break;
		case StoneLvl4Counts:// n种X级宝石
			infoMap = getStonelvlNum();
			pointRiseEveryAction(type, infoMap.get(4) == null ? 0 : infoMap.get(4));
			break;
		case StoneLvl5Counts:// n种X级宝石
			infoMap = getStonelvlNum();
			pointRiseEveryAction(type, infoMap.get(5) == null ? 0 : infoMap.get(5));
			break;
		case StoneLvl6Counts:// n种X级宝石
			infoMap = getStonelvlNum();
			pointRiseEveryAction(type, infoMap.get(6) == null ? 0 : infoMap.get(6));
			break;
		case BattleHeroStar:
			infoMap = getBattleHeroStarInfo();
			comparaStringEveryAction(type, infoMap);
			break;
		case HeroStar:
			infoMap = getHeroStarNumInfo();
			comparaStringEveryAction(type, infoMap);
			break;
		case HeroStar5:
			infoMap = getHeroStarNumInfo();
			pointRiseEveryAction(type, infoMap.get(5) == null ? 0 : infoMap.get(5));
			break;
		case BreakThrough:
			infoMap = getHeroBreakLvlInfo();
			comparaStringEveryAction(type, infoMap);
			break;
		case BreakThrough1:
			infoMap = getHeroBreakLvlInfo();
			pointRiseEveryAction(type, infoMap.get(1) == null ? 0 : infoMap.get(1));
			break;
		case BreakThrough2:
			infoMap = getHeroBreakLvlInfo();
			pointRiseEveryAction(type, infoMap.get(2) == null ? 0 : infoMap.get(2));
			break;
		case BreakThrough3:
			infoMap = getHeroBreakLvlInfo();
			pointRiseEveryAction(type, infoMap.get(3) == null ? 0 : infoMap.get(3));
			break;
		case BreakThrough4:
			infoMap = getHeroBreakLvlInfo();
			pointRiseEveryAction(type, infoMap.get(4) == null ? 0 : infoMap.get(4));
			break;
		case BreakThrough5:
			infoMap = getHeroBreakLvlInfo();
			pointRiseEveryAction(type, infoMap.get(5) == null ? 0 : infoMap.get(5));
			break;
		// case LawQlvlNum:// 拥有n种不同种类的X色品质的阵法
		// lawQlvlNumEveryAction(type, getLawQlvlNum());
		// break;
		case LawLvlBlueNum:// 拥有n种x级的蓝色品质阵法
			lawQlvlEveryAction(type, getLawQlvlNum(QualityColor.Blue.value()));
			break;
		case LawLvlPurpleNum:// 拥有n种x级的紫色品质阵法
			lawQlvlEveryAction(type, getLawQlvlNum(QualityColor.Violet.value()));
			break;
		case PracticeNum:
			pointRiseEveryAction(type, getHeroPracticeNumInfo());
			break;
		case Partnar:// 开启N个伙伴位
			pointRiseEveryAction(type, getHeroPartnerPositNum());
			break;
		case PracticeTop:// n名武将x个属性修炼到顶级
			List<List<Integer>> list = getHeroNMaxPlvl();
			comparaHeroPMaxLvlStringEveryAction(type, list);
			break;
		case PracticeTop8:// 8个属性修炼到顶级的武将数量：%s
			int count = 0;
			list = getHeroNMaxPlvl();
			for (List<Integer> propsList : list) {
				if (propsList.size() >= 8) {
					count += 1;
				}
			}
			// comparaHeroPMaxLvlStringEveryAction(type, list);
			pointRiseEveryAction(type, count);
			break;
		case PracticeLv10:// 任意1个属性修炼到10级的武将数量：%s
			comparaHeroP10LvlStringEveryAction(type);
			break;
		case PracticeLv:// n名武将任意1个属性修炼到y级
			List<Map<Integer, Integer>> lvs = getHeroOnePlvl();
			comparaHeroPLvlStringEveryAction(type, lvs);
			break;
		case HeroAll:// 子成就全部领完之后调用
		case EquipAll:
		case ArenaAll:// 完成竞技场所有累积成就
		case QunxiongAll:
		case ChuzhengAll: // 完成出征所有累积计成就
		case ShiKongAll: // 完成时空战役所有累积成就
		case LuanshiAll:// 完成乱世魔王所有累积成就
		case FriendAll:// 完成好友所有累积成就
		case GuildAll:// 完成公会所有累积成就
		case TaskAll:// 完成任务所有累积成就
		case BeifaStarAll:
		case PVEAll:
			updateSpecialAchieveprogress(type, para);
			break;
		case EquipTypes:
//			pointRiseEveryAction(type, getDifferentTypeEquips(ItemType.EquipItemType, null));
			updateDifferentTypeEquips(para, type);
			break;
		case LawQlvlNumB:
			pointRiseEveryAction(type, getDifferentTypeEquips(ItemType.FormationBuffItemType, QualityColor.Blue));
			break;
		case LawQlvlNumP:
			pointRiseEveryAction(type, getDifferentTypeEquips(ItemType.FormationBuffItemType, QualityColor.Violet));
			break;
		case PartnarIsYuanfen:// 专属武将亦是缘分武将的达到n位
			pointRiseEveryAction(type, getPartnarSpecialFateNum());
			break;
		case lineCounts:// 拥有n个寻宝队伍
			pointRiseEveryAction(type, Integer.valueOf(para));
			break;
		case EquipLvlNum:
			infoMap = getEquipLvlNum();
			comparaStringEveryAction(type, infoMap);
			break;
		case EquipLvlNumFull:
			infoMap = getEquipLvlNum();
			pointRiseEveryAction(type, infoMap.get(180) == null ? 0 : infoMap.get(180));
			break;
		case EquipQLvlNum:
			infoMap = getEquipQlvlNum();
			comparaStringEveryAction(type, infoMap);
			break;
		case EquipStarNum:
			infoMap = getEquipStarNum();
			comparaStringEveryAction(type, infoMap);
			break;
		case BuyPlusDays:// 购买伤害加成累积n天
		case GuildFightDays:// 累积n天参加公会战
		case FirstWinDays:// 累积n天领取竞技场每日首胜奖励
		case StarHeroDays:// 累积召唤名将n天
		case BussinessManDays:// 累积集市召唤商人n天
		case loginDayCounts:// 累积登录n天
		case ConLuanshiDays: // 累计参加与乱世魔王的战斗n天
			addoneDayNoContinue(type);
			break;
		case JoinQunXiong:// 累计n天参加群雄争霸
			// addoneDayContinue(type);
			addoneDayNoContinue(type);
			break;
		case BeifaStarsDays50:
		case BeifaStarsDays: // 累积n天每天有1次北伐x星(多个成就的X值是定值)
			if (beifaStarTotalNum(type))
				addoneDayNoContinue(type);
			break;
		case ConWinDays: // 累积n天竞技场x连胜
			arenaSuccessNumsEveryDay(type, Integer.valueOf(para));
			break;
		default:
			return;
		}
		List<Integer> changeStatus = getChangeAchieveIds(frontStatusMap, getStatusByType(type));
		if (changeStatus.size() > 0) {
			// 协议
			this.rt.getNotifyControler().showAchieve(changeStatus);
			notifyRedPoint();
			notifyAllServer(changeStatus);
		}
	}

	/**
	 * 全服公告
	 * 
	 * @param achieveId
	 */
	private void notifyAllServer(List<Integer> aIds) {
		Map<Integer, AchieveT> achieveMap = XsgAchieveManager.getInstance().getAchieveMap();
		Map<Integer, AchieveFirstNotify> notifyMap = XsgAchieveManager.getInstance().getNotifyMap();
		for (Integer aId : aIds) {
			AchieveT achieveT = achieveMap.get(aId);
			if (achieveT == null)
				continue;
			if (achieveT.isNotice == 0)
				continue;
			boolean isSend = false;
			if (notifyMap == null) {
				notifyMap = new HashMap<Integer, AchieveFirstNotify>();
				AchieveFirstNotify notify = new AchieveFirstNotify(
						GlobalDataManager.getInstance().generatePrimaryKey(), aId, db.getId(), new Date());
				notifyMap.put(notify.getAchieveId(), notify);
				XsgAchieveManager.getInstance().setNotifyMap(notifyMap);
				XsgAchieveManager.getInstance().save2DbAsync(notify);
				isSend = true;
			} else {
				if (notifyMap.get(aId) == null) {
					AchieveFirstNotify notify = new AchieveFirstNotify(GlobalDataManager.getInstance()
							.generatePrimaryKey(), aId, db.getId(), new Date());
					notifyMap.put(notify.getAchieveId(), notify);
					XsgAchieveManager.getInstance().save2DbAsync(notify);
					isSend = true;
				}
			}
			if (isSend) {
				// 公告
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
						XsgChatManager.AdContentType.AchieveFirst);
				if (adTList != null && adTList.size() > 0) {
					final ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
					if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
						String content = XsgChatManager.getInstance().replaceRoleContent(chatAdT.content, rt);
						content = content.replace("~achieve_title~", achieveT.title);
						content = content.replace("~achieve_content~", achieveT.content1);
						content = content.replace("%s", String.valueOf(achieveT.maxNum));
						XsgChatManager.getInstance().sendAnnouncement(content);
					}
				}
			}
		}
	}

	/**
	 * 获得当前类型下的所有成就状态
	 * 
	 * @param type
	 * @return
	 */
	private TreeMap<Integer, Byte> getStatusByType(String type) {
		TreeMap<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		if (null == map || map.size() == 0)
			return null;
		TreeMap<Integer, Byte> statusMap = new TreeMap<Integer, Byte>();
		for (AchieveT t : map.values()) {
			statusMap.put(t.id, getAchieveStatus(t.id));
		}
		return statusMap;
	}

	/**
	 * 获得跟新前后两次 状态快照的变更数据
	 * 
	 * @param front
	 * @param current
	 * @return
	 */
	private List<Integer> getChangeAchieveIds(TreeMap<Integer, Byte> front, TreeMap<Integer, Byte> current) {
		List<Integer> list = new ArrayList<Integer>();
		if (null == front || null == current)
			return null;
		for (int aid : front.keySet()) {
			byte fStatus = front.get(aid);
			byte cStatus = current.get(aid);
			if (fStatus != cStatus && cStatus == 1)
				list.add(aid);
		}
		return list;
	}

	/**
	 * 累积n季官阶为相国
	 * 
	 * @param type
	 * @param para
	 */
	private void ladderLvl(String type, String para) {
		// 官阶的判断 跟新时间是否是一个月
		if (1 == Integer.valueOf(para)) {
			Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
			RoleAchieve achieve4Type = roleAchieves.get(type);
			if (achieve4Type == null) {
				addNums4EveryAction(type, 1);
			} else {
				if (!DateUtil.isSameMonth(achieve4Type.getUpdateDate(), new Date())) {
					addNums4EveryAction(type, 1);
					achieve4Type.setUpdateDate(new Date());
				}
			}
		}

	}

	/**
	 * 竞技场每天连胜的处理
	 * 
	 * @param type
	 * @param result
	 */
	private void arenaSuccessNumsEveryDay(String type, int result) {
		boolean flag = false;
		TreeMap<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null == achieve4Type) {
			achieve4Type = new RoleAchieve(GlobalDataManager.getInstance().generatePrimaryKey(), db, type, "", null);
			achieve4Type.setContinuedayinfo(result + "");
			roleAchieves.put(type, achieve4Type);
		} else {
			if (null != achieve4Type.getUpdateDate() && DateUtil.isSameDay(achieve4Type.getUpdateDate(), new Date()))
				return;
			if (result == 1)// 胜利
			{
				int frontInfo = TextUtil.isBlank(achieve4Type.getContinuedayinfo()) ? 0 : Integer.valueOf(achieve4Type
						.getContinuedayinfo());
				int curNum = frontInfo + 1;
				achieve4Type.setContinuedayinfo(String.valueOf(curNum));
				// 若大于条件X次的 返回 成功
				int needSuccessNums = Integer.valueOf(map.firstEntry().getValue().condition.split("_")[1]);
				if (curNum >= needSuccessNums)
					flag = true;
			} else {
				achieve4Type.setContinuedayinfo(String.valueOf(0));
			}
			if (flag) {
				int frontNum = 0;
				if (!TextUtil.isBlank(achieve4Type.getProgress()))
					frontNum = Integer.valueOf(achieve4Type.getProgress());
				achieve4Type.setProgress(String.valueOf(frontNum + 1));
				achieve4Type.setContinuedayinfo(0 + "");
				achieve4Type.setUpdateDate(new Date());
			}
		}
	}

	/**
	 * 北伐总星数是否满足成就的配置数量
	 * 
	 * @param type
	 * @return
	 */
	private boolean beifaStarTotalNum(String type) {
		TreeMap<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		int configStarNum = Integer.valueOf(map.firstEntry().getValue().condition.split("_")[1]);
		int curStarCount = rt.getAttackCastleController().getTotalStar();
		if (curStarCount >= configStarNum)
			return true;
		return false;
	}
	
	/**
	 * 更新n种不同装备
	 * 
	 * @return
	 */
	private void updateDifferentTypeEquips(String templateId, String type) {
		// 已经包含了该类型
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null != achieve4Type
				&& achieve4Type.getProgress().indexOf(templateId + ",") != -1) {
			return;
		}
		saveString2DbProgress(type, templateId);
	}
	/**
	 * n件不同阵法
	 * 
	 * @return
	 */
	private int getDifferentTypeEquips(ItemType iType, QualityColor qColor) {
		List<IItem> items = rt.getItemControler().getItemList();
		HashSet<String> set = new HashSet<String>();
		for (IItem item : items) {
			if (iType != null && item.getView().iType != iType)
				continue;
			if (qColor != null && item.getTemplate().getColor() != qColor)
				continue;
			set.add(item.getView().templateId);
		}
		return set.size();
	}

	/**
	 * 专属也是缘分武将的数量
	 * 
	 * @return
	 */
	private int getPartnarSpecialFateNum() {
		return rt.getPartnerControler().getActivedRelationPartner();
	}

	/**
	 * n件x级装备
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getEquipLvlNum() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		ItemView[] items = rt.getItemControler().getItemViewList();
		for (int i = 0; i < items.length; i++) {
			ItemView view = items[i];
			if (view.iType != ItemType.EquipItemType)
				continue;
			EquipItem equip = rt.getItemControler().getEquipItem(view.id);
			if (null == map.get(equip.getLevel()))
				map.put(equip.getLevel(), 1);
			else
				map.put(equip.getLevel(), map.get(equip.getLevel()) + 1);
		}
		return map;
	}

	/**
	 * n件x星装备
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getEquipStarNum() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		ItemView[] items = rt.getItemControler().getItemViewList();
		for (int i = 0; i < items.length; i++) {
			ItemView view = items[i];
			EquipItem equip = rt.getItemControler().getEquipItem(view.id);
			if (equip == null)
				continue;
			if (map.get((int) equip.getStar()) == null)
				map.put((int) equip.getStar(), 1);
			else
				map.put((int) equip.getStar(), map.get((int) equip.getStar()) + 1);
		}
		return map;
	}

	/**
	 * n件x色装备
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getEquipQlvlNum() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		ItemView[] items = rt.getItemControler().getItemViewList();
		for (int i = 0; i < items.length; i++) {
			ItemView view = items[i];
			if (view.iType != ItemType.EquipItemType)
				continue;
			EquipItem equip = rt.getItemControler().getEquipItem(view.id);
			if (null == map.get(equip.getQuatityColor().value()))
				map.put(equip.getQuatityColor().value(), 1);
			else
				map.put(equip.getQuatityColor().value(), map.get(equip.getQuatityColor().value()) + 1);

		}
		return map;
	}

	/**
	 * 拥有n种不同种类的X色品质的阵法
	 * 
	 * @return
	 */
	private Map<Integer, List<String>> getLawQlvlNum() {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		ItemView[] items = rt.getItemControler().getItemViewList();
		for (int i = 0; i < items.length; i++) {
			ItemView view = items[i];
			if (view.iType != ItemType.FormationBuffItemType)
				continue;
			int qLvl = rt.getItemControler().getItem(view.id).getTemplate().getColor().value();
			// FormationBuffItem it = (FormationBuffItem)
			// rt.getItemControler().getItem(view.id);
			// int lvl = it.getLevel();
			List<String> list = map.get(qLvl);
			if (list == null) {
				list = new ArrayList<String>();
				list.add(view.id);
				map.put(qLvl, list);
			} else {
				if (!list.contains(view.id))
					list.add(view.id);
			}
		}

		return map;
	}

	/**
	 * 拥有n种x等级的宝石
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getStonelvlNum() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		ItemView[] items = rt.getItemControler().getItemViewList();
		for (int i = 0; i < items.length; i++) {
			ItemView view = items[i];
			if (!(XsgItemManager.getInstance().findAbsItemT(view.templateId) instanceof GemT))
				continue;
			StringBuilder sb = new StringBuilder(view.templateId);
			// 通过模版ID最后的数字来确定等级 很蛋疼
			int gemLevel = Integer.parseInt(sb.reverse().substring(0, 1));
			if (null == map.get(gemLevel))
				map.put(gemLevel, view.num);
			else
				map.put(gemLevel, map.get(gemLevel) + view.num);
		}
		return map;
	}

	/**
	 * 拥有n种x级的Y色品质阵法
	 * 
	 * @return
	 */
	private Map<Integer, List<String>> getLawQlvlNum(int qLevel) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		ItemView[] items = rt.getItemControler().getItemViewList();
		for (int i = 0; i < items.length; i++) {
			ItemView view = items[i];
			if (view.iType != ItemType.FormationBuffItemType)
				continue;
			int qLvl = rt.getItemControler().getItem(view.id).getTemplate().getColor().value();
			if (qLevel != qLvl)
				continue;
			FormationBuffItem it = (FormationBuffItem) rt.getItemControler().getItem(view.id);
			int lvl = it.getLevel();
			List<String> list = map.get(lvl);
			if (list == null) {
				list = new ArrayList<String>();
				list.add(view.id);
				map.put(lvl, list);
			} else {
				if (!list.contains(view.id))
					list.add(view.id);
			}
		}

		return map;
	}

	/**
	 * n名武将任意1个属性修炼到y级
	 * 
	 * @return
	 */
	private List<Map<Integer, Integer>> getHeroOnePlvl() {
		List<Map<Integer, Integer>> list = new ArrayList<Map<Integer, Integer>>();
		List<IHero> allHero = rt.getHeroControler().getAllHero();
		for (IHero hero : allHero) {
			List<HeroPracticeView> views = hero.getHeroPracticeView();
			Map<Integer, Integer> lvlMap = new HashMap<Integer, Integer>();
			for (HeroPracticeView view : views) {
				lvlMap.put(view.level, view.id);
			}
			list.add(lvlMap);
		}
		return list;
	}

	/**
	 * n名武将x个属性修炼到顶级
	 * 
	 * @return
	 */
	private List<List<Integer>> getHeroNMaxPlvl() {
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		List<IHero> allHero = rt.getHeroControler().getAllHero();
		for (IHero hero : allHero) {
			List<HeroPracticeView> views = hero.getHeroPracticeView();
			// 一个武将的满级修炼属性
			List<Integer> maxlLvls = new ArrayList<Integer>();

			for (HeroPracticeView view : views) {
				HeroPractice hp = hero.getHeroByScriptId(view.id);
				if (hp == null) {
					continue;
				}
				PracticePropT propT = XsgHeroManager.getInstance().getPracticePropMap().get(view.id);
				if (hp.getLevel() >= propT.maxLevel)
					maxlLvls.add(view.id);
			}
			if (maxlLvls.size() > 0)
				list.add(maxlLvls);
		}
		return list;
	}

	/**
	 * n名武将x个属性修炼到Level级
	 * 
	 * @return
	 */
	private List<List<Integer>> getHeroNPlvl(int level) {
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		List<IHero> allHero = rt.getHeroControler().getAllHero();
		for (IHero hero : allHero) {
			List<HeroPracticeView> views = hero.getHeroPracticeView();
			// 一个武将的level级修炼属性
			List<Integer> nlLvls = new ArrayList<Integer>();

			for (HeroPracticeView view : views) {
				HeroPractice hp = hero.getHeroByScriptId(view.id);
				if (hp == null) {
					continue;
				}
				if (hp.getLevel() >= level)
					nlLvls.add(view.id);
			}
			if (nlLvls.size() > 0)
				list.add(nlLvls);
		}
		return list;
	}

	/**
	 * 累计N天的 行为
	 * 
	 * @param type
	 */
	private void addoneDayNoContinue(String type) {
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null == achieve4Type) {
			achieve4Type = new RoleAchieve(GlobalDataManager.getInstance().generatePrimaryKey(), db, type, 1 + "",
					new Date());
			roleAchieves.put(type, achieve4Type);
			return;
		}
		// 同一天 不计算
		if (DateUtil.isSameDay(achieve4Type.getUpdateDate(), new Date()))
			return;
		int frontNum = 0;
		if (!TextUtil.isBlank(achieve4Type.getProgress()))
			frontNum = Integer.valueOf(achieve4Type.getProgress());
		achieve4Type.setProgress(String.valueOf(frontNum + 1));
		achieve4Type.setUpdateDate(new Date());
	}

	/**
	 * 连续N天的行为(前后操作间隔时间为1天)
	 * 
	 * @param type
	 */
	private void addoneDayContinue(String type) {
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null == achieve4Type) {
			achieve4Type = new RoleAchieve(GlobalDataManager.getInstance().generatePrimaryKey(), db, type, 1 + "",
					new Date());
			roleAchieves.put(type, achieve4Type);
			return;
		}
		// 同一天 不计算
		if (DateUtil.isSameDay(achieve4Type.getUpdateDate(), new Date()))
			return;
		int frontNum = 0;
		if (!TextUtil.isBlank(achieve4Type.getProgress()))
			frontNum = Integer.valueOf(achieve4Type.getProgress());
		// 判断前后两次间隔时间 不在一天内 重置
		if (DateUtil.computerDaysBy2Date(achieve4Type.getUpdateDate().getTime(), System.currentTimeMillis()) != 1)
			achieve4Type.setProgress(1 + "");
		else
			achieve4Type.setProgress(String.valueOf(frontNum + 1));

		// 达成的要存库 标记
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		for (AchieveT t : map.values()) {
			// 只有未完成状态可更新
			if (getAchieveStatus(t.id) != 0)
				continue;
			if (Integer.valueOf(achieve4Type.getProgress()) >= Integer.valueOf(t.condition)) {
				if (TextUtil.isBlank(achieve4Type.getContinuedayinfo())) {
					achieve4Type.setContinuedayinfo(t.id + ",");
				} else {
					if (achieve4Type.getContinuedayinfo().indexOf(t.id + ",") == -1)
						achieve4Type.setContinuedayinfo(achieve4Type.getContinuedayinfo() + t.id + ",");
				}

			}
		}
		achieve4Type.setUpdateDate(new Date());
	}

	/**
	 * 获得 n名上阵武将达到x星的信息
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getBattleHeroStarInfo() {
		Map<Integer, Integer> infos = new HashMap<Integer, Integer>();
		if (null != rt.getFormationControler().getDefaultFormation()) {
			Iterable<IHero> heros = rt.getFormationControler().getDefaultFormation().getHeros();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero hero = (IHero) iter.next();
				int star = hero.getStar();
				if (null == infos.get(star))
					infos.put(star, 1);
				else
					infos.put(star, infos.get(star) + 1);
			}
		}
		return infos;
	}

	/**
	 * 拥有n名n星武将
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getHeroStarNumInfo() {
		Map<Integer, Integer> infos = new HashMap<Integer, Integer>();
		if (null != rt.getHeroControler().getAllHero()) {
			Iterable<IHero> heros = rt.getHeroControler().getAllHero();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero hero = (IHero) iter.next();
				int star = hero.getStar();
				if (null == infos.get(star))
					infos.put(star, 1);
				else
					infos.put(star, infos.get(star) + 1);
			}

		}
		return infos;
	}

	/**
	 * n名武将突破到XX称号
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getHeroBreakLvlInfo() {
		Map<Integer, Integer> infos = new HashMap<Integer, Integer>();
		if (null != rt.getHeroControler().getAllHero()) {
			Iterable<IHero> heros = rt.getHeroControler().getAllHero();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero hero = (IHero) iter.next();
				int breakLvl = hero.getBreakLevel();
				if (null == infos.get(breakLvl))
					infos.put(breakLvl, 1);
				else
					infos.put(breakLvl, infos.get(breakLvl) + 1);
			}

		}
		return infos;
	}

	/**
	 * 任意一名武将修炼n个属性
	 * 
	 * @return
	 */
	private int getHeroPracticeNumInfo() {
		int num = 0;
		if (null != rt.getHeroControler().getAllHero()) {
			Iterable<IHero> heros = rt.getHeroControler().getAllHero();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero hero = (IHero) iter.next();
				int curNum = hero.getPracticeSize();
				if (curNum > num)
					num = curNum;
			}
		}
		return num;
	}

	/**
	 * 获得开启的伙伴位数量
	 * 
	 * @return
	 */
	private int getHeroPartnerPositNum() {
		return rt.getPartnerControler().getOpenedPartner();
	}

	/**
	 * 
	 * @param 获取成就状态
	 *            (-1:错误 0:未完成 1：可领取 2：已领取 3：完成整个条目)
	 * @return
	 */
	private byte getAchieveStatus(int achieveId) {
		Map<Integer, AchieveT> achieveInfoMap = XsgAchieveManager.getInstance().getAchieveMap();
		AchieveT t = achieveInfoMap.get(achieveId);
		if (null == t)
			return -1;
		String type = t.type;
		TreeMap<Integer, AchieveT> type4Map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		// 完成条件
		String condition = t.condition;
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve roleAchieve = roleAchieves.get(type);
		// 前置判断
		if (t.frontId != 0) {
			AchieveT front = achieveInfoMap.get(t.frontId);
			if (null != front) {
				if (!isRecAward(t.frontId))
					return 0;
			}
		}
		// 已领取
		if (isRecAward(achieveId)) {
			if (type4Map.lastKey() == achieveId)
				return 3;
			return 2;
		}
		// 判断可领取的状态
		switch (AchieveTemplate.getAchieveTemplate(type)) {
		case KingLv:
		case XinshouSweepNum:// 累积扫荡新手关卡n次
		case GaoshouSweepNum:// 累积扫荡高手关卡n次
		case GodSweepNum:// 累积扫荡大神关卡n次
		case XinshouStarNum: // 新手关卡星数达到n颗
		case GaoshouStarNum:// 高手关卡星数达到n颗
		case GodStarNum:// 大神关卡星数达到n颗
		case GodNum:// 挑战大神难度n次
		case InfernalNum:// 挑战地狱难度n次
		case SweepXinshouNum:// 扫荡新手难度n次
		case SweepGaoshouNum:// 扫荡高手难度n次
		case GoldNum:
		case Power:
		case LearnedPoint:
		case HeroNum:
		case FriendNums:// 好友数量
		case PracticeNum:
		case LawQlvlNumB:
		case LawQlvlNumP:
		case FirstWinDays:
		case FailedTimes:
		case BeifaStars:
		case PassNums:
		case LoserNums:
		case BuyPlusDays:
		case TotalHurt:
		case SendJunling:
		case QieChuoCount:
		case GuildFightDays:
		case GuildDHurtCount:
		case DonateJiangzhang:
		case AuctionSuccessNums:// 累积竞拍成功n次
		case AuctionNums:// 累积拍卖n次
		case StarHeroDays:// 累积召唤名将n天
		case ConLuanshiDays:
		case BussinessManDays:// 累积集市召唤商人n天
		case OnlineGiftNums:// 累积领在线礼包n个
		case loginDayCounts:// 累积登录n天
		case LuckManNum:// 累计获得运气王n次
		case CompleteDayTaskNum:// 累积完成n个日常任务
		case CompleteMainTaskNum:// 累积完成n个主线任务
		case Partnar:// 开启伙伴位N个
		case PartnarIsYuanfen:
		case FriendPoint:// 与任意好友的友情点累积达到n
		case SPAwardTimes:// 累积获得n次尾刀奖励
		case GuildGiftNums:// 累计兑换N个公会礼包
		case Guanjie:// 累积n季官阶为相国
		case lineCounts:// 拥有N个寻宝队
		case StoneCounts:// 寻宝累计获得N个宝石
		case XunbaoCountTime:// 寻宝累积n小时
		case JoinQunXiong:// 连续n天参加群雄争霸
		case PracticeLv10:// 任意1个属性修炼到10级的武将数量：%s
		case HeroStar5:// 拥有5星武将数量：%s
		case EquipLvlNumFull: // 拥有180级装备数量：%s
		case StoneLvl4Counts:
		case StoneLvl5Counts:
		case StoneLvl6Counts:
			if (roleAchieve != null && !TextUtil.isBlank(roleAchieve.getProgress())) {
				if (Integer.valueOf(roleAchieve.getProgress()) >= Integer.valueOf(condition))
					return 1;
			}
			break;
		// case JoinQunXiong:// 连续n天参加群雄争霸
		// if (roleAchieve != null &&
		// !TextUtil.isBlank(roleAchieve.getContinuedayinfo())) {
		// if (roleAchieve.getContinuedayinfo().indexOf(achieveId + ",") != -1)
		// return 1;
		// }
		// break;
		case FiveStarPassNum:// 5星通关的关卡达到n个
			if (roleAchieve != null && !TextUtil.isBlank(roleAchieve.getProgress())) {
				if (Integer.valueOf(roleAchieve.getProgress()) >= Integer.valueOf(condition.split("_")[1]))
					return 1;
			}
			break;
		case BeifaStarsDays: // 累积n天每天有1次北伐x星(多个成就的X值是定值)
		case BeifaStarsDays50:
		case ConWinDays: // 累积n天竞技场x连胜
		case BreakThrough1:
		case BreakThrough2:
		case BreakThrough3:
		case BreakThrough4:
		case BreakThrough5:
		case PracticeTop8:
			if (roleAchieve != null && !TextUtil.isBlank(roleAchieve.getProgress())) {
				if (Integer.valueOf(roleAchieve.getProgress()) >= Integer.valueOf(condition.split("_")[0]))
					return 1;
			}
			break;
		case BattleHeroStar:
		case HeroStar:
		case BreakThrough:
		case EquipLvlNum:
		case EquipQLvlNum:
		case EquipStarNum:
		case PracticeTop:
		case PracticeLv:
		case LawLvlBlueNum:// 拥有n种x级的蓝色品质阵法
		case LawLvlPurpleNum:// 拥有n种x级的紫色品质阵法
			// case StoneLvlCounts:// n种X级宝石
			if (roleAchieve != null && !TextUtil.isBlank(roleAchieve.getProgress())) {
				if (roleAchieve.getProgress().indexOf(achieveId + ",") != -1)
					return 1;
			}
			break;
		case HeroAll:
		case ArenaAll:
		case QunxiongAll:
		case ChuzhengAll:
		case ShiKongAll:
		case LuanshiAll:
		case FriendAll:
		case GuildAll:
		case TaskAll:
		case EquipAll:
		case BeifaStarAll:
		case PVEAll:
			if (isCompletedSpecialAchieve(type, achieveId))
				return 1;
			break;
		case AllAchievement:// 达成所有成就
			if (isCompletedAllAchieve(type, achieveId))
				return 1;
			break;
		case PVP:
		case PVE1:
		case PVE2:
			if (isCanRecPvTypeAchieve(achieveId))
				return 1;
			break;
		case EquipTypes:
			if (roleAchieve != null && !TextUtil.isBlank(roleAchieve.getProgress())) {
				if (roleAchieve.getProgress().split(",").length >= Integer.valueOf(condition))
					return 1;
			}
			break;
		default:
			return -1;
		}
		return 0;
	}

	/**
	 * 嵌套类型成就 该成就依赖于其他系统 全部特殊类型成就完成
	 * 
	 * @return
	 */
	private boolean isCanRecPvTypeAchieve(int id) {
		AchieveT t = XsgAchieveManager.getInstance().getAchieveMap().get(id);
		if (null == t)
			return false;
		String[] types = t.condition.split(",");
		for (int i = 0; i < types.length; i++) {
			String specialType = types[i];
			// 查看这个类型成就是否完成
			TreeMap<Integer, AchieveT> specialMap = XsgAchieveManager.getInstance().getAchieveInfoMap()
					.get(specialType);
			// 没有配置这条
			if (null == specialMap || specialMap.size() == 0)
				return false;
			// 查看状态 递归调用 (这里理论上不出现 同时避免死循环)
			if (specialMap.firstKey() == id)
				return false;
			if (getAchieveStatus(specialMap.firstKey()) != 2 && getAchieveStatus(specialMap.firstKey()) != 3)
				return false;
		}
		return true;
	}

	/**
	 * 特殊成就是否完成
	 * 
	 * @param achieveId
	 * @param type
	 * @return
	 */
	private boolean isCompletedSpecialAchieve(String type, int achieveId) {
		int functionId = XsgAchieveManager.getInstance().getFunctionByID(achieveId);
		if (functionId < 0)
			return false;
		Map<String, AchieveCataLogT> cMap = XsgAchieveManager.getInstance().getAchieveCataLogMap().get(functionId);
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve roleAchieve = roleAchieves.get(type);
		if (roleAchieve == null || TextUtil.isBlank(roleAchieve.getProgress())) {
			return false;
		}
		for (AchieveCataLogT tp : cMap.values()) {
			if (tp.type.equals(type))
				continue;
			TreeMap<Integer, AchieveT> typeMap = XsgAchieveManager.getInstance().getAchieveInfoMap().get(tp.type);
			if (typeMap == null || typeMap.size() == 0)
				continue;
			if (roleAchieve.getProgress().indexOf(tp.type + ",") == -1)
				return false;
		}
		return true;
	}

	/**
	 * 是否完成全部成就
	 * 
	 * @param achieveId
	 * @param type
	 * @return
	 */
	private boolean isCompletedAllAchieve(String type, int achieveId) {
		int functionId = XsgAchieveManager.getInstance().getFunctionByID(achieveId);
		if (functionId < 0)
			return false;
		int needCount = XsgAchieveManager.getInstance().getAchieveCount();
		int currentCompleted = getCompletedCount();
		if (currentCompleted < needCount)
			return false;
		return true;
	}

	/**
	 * 获得所有完成的成就
	 * 
	 * @return
	 */
	private int getCompletedCount() {
		return db.getAchieveCompletedCount();
	}

	/**
	 * 适用于每个行为 累加次数的成就
	 * 
	 * @param type
	 * @param addNums
	 */
	private void addNums4EveryAction(String type, int addNums) {
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null == achieve4Type) {
			achieve4Type = new RoleAchieve(GlobalDataManager.getInstance().generatePrimaryKey(), db, type,
					String.valueOf(addNums), new Date());
			roleAchieves.put(type, achieve4Type);
			return;
		}
		int frontNum = 0;
		if (null != achieve4Type.getProgress() && !achieve4Type.getProgress().isEmpty())
			frontNum = Integer.valueOf(achieve4Type.getProgress());
		achieve4Type.setProgress(String.valueOf(frontNum + addNums));
	}

	/**
	 * 适用于某个行为 xx值达到xxx(如：战力)
	 * 
	 * @param type
	 * @param point
	 */
	private void pointRiseEveryAction(String type, int point) {
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null == achieve4Type) {
			achieve4Type = new RoleAchieve(GlobalDataManager.getInstance().generatePrimaryKey(), db, type,
					String.valueOf(point), new Date());
			roleAchieves.put(type, achieve4Type);
			return;
		}
		int frontNum = 0;
		if (null != achieve4Type.getProgress() && !achieve4Type.getProgress().isEmpty())
			frontNum = Integer.valueOf(achieve4Type.getProgress());
		if (frontNum < point)
			achieve4Type.setProgress(String.valueOf(point));
	}

	/**
	 * 适用于字符标签 直接记录成就ID到进度 表示已完成(如：拥有n名n星武将 xx_xx(数量_数量))
	 * 
	 * @param type
	 * @param infoMap
	 */
	private void comparaStringEveryAction(String type, Map<Integer, Integer> infoMap) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		for (AchieveT t : map.values()) {
			// 只有未完成状态可更新
			if (getAchieveStatus(t.id) != 0)
				continue;
			String[] nums = t.condition.split("_");
			int needNum1 = Integer.valueOf(nums[0]);// N名
			int needNum2 = Integer.valueOf(nums[1]);// 条件 N星
			int p1 = 0;// 统计出的数量 例如名次
			for (Integer num : infoMap.keySet()) {
				// num 星
				if (num >= needNum2)
					p1 += infoMap.get(num);
			}
			if (p1 >= needNum1) {
				saveString2DbProgress(type, String.valueOf(t.id));
			}
		}
	}

	/**
	 * 适用于拥有n种不同种类的X色品质的阵法
	 * 
	 * @param type
	 * @param infoMap
	 */
	private void lawQlvlNumEveryAction(String type, Map<Integer, List<String>> infoMap) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		for (AchieveT t : map.values()) {
			// 只有未完成状态可更新
			if (getAchieveStatus(t.id) != 0)
				continue;
			String[] nums = t.condition.split("_");
			int needNum1 = Integer.valueOf(nums[0]);// N种
			int needNum2 = Integer.valueOf(nums[1]);// X色/级
			int p1 = 0;// 统计出的数量 例如名次
			for (Integer clr : infoMap.keySet()) {
				// num 星
				if (clr == needNum2)
					p1 += infoMap.get(clr).size();
			}
			if (p1 >= needNum1) {
				saveString2DbProgress(type, String.valueOf(t.id));
			}
		}
	}

	/**
	 * 拥有n种x级的蓝色/紫色品质阵法
	 * 
	 * @param type
	 * @param infoMap
	 */
	private void lawQlvlEveryAction(String type, Map<Integer, List<String>> infoMap) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		for (AchieveT t : map.values()) {
			// 只有未完成状态可更新
			if (getAchieveStatus(t.id) != 0)
				continue;
			String[] nums = t.condition.split("_");
			int needNum1 = Integer.valueOf(nums[0]);// N种
			int needNum2 = Integer.valueOf(nums[1]);// 级
			int p1 = 0;// 统计出的数量 例如名次
			for (Integer clr : infoMap.keySet()) {
				// num 星
				if (clr >= needNum2)
					p1 += infoMap.get(clr).size();
			}
			if (p1 >= needNum1) {
				saveString2DbProgress(type, String.valueOf(t.id));
			}
		}
	}

	/**
	 * 增加 记录到成就进度后面(xxx,xxx,para,...)
	 */
	private void saveString2DbProgress(String type, String para) {
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null == achieve4Type) {
			achieve4Type = new RoleAchieve(GlobalDataManager.getInstance().generatePrimaryKey(), db, type, para + ",",
					new Date());
			roleAchieves.put(type, achieve4Type);
		} else {
			if (null == achieve4Type.getProgress()) {
				achieve4Type.setProgress(para + ",");
			} else {
				if (achieve4Type.getProgress().indexOf(para + ",") == -1)
					achieve4Type.setProgress(achieve4Type.getProgress() + para + ",");
			}
		}

	}

	/**
	 * 适用于n名武将任意1个属性修炼到y级
	 * 
	 * @param type
	 * @param infoMap
	 */
	private void comparaHeroPLvlStringEveryAction(String type, List<Map<Integer, Integer>> list) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		for (AchieveT t : map.values()) {
			// 只有未完成状态可更新
			if (getAchieveStatus(t.id) != 0)
				continue;
			String[] nums = t.condition.split("_");
			int needNum1 = Integer.valueOf(nums[0]);// N名
			int needNum2 = Integer.valueOf(nums[1]);// 条件 Y级
			int p1 = list.size();// 统计出的数量 例如名次
			if (p1 < needNum1)// 武将不够
				continue;
			int p2 = 0;
			for (Map<Integer, Integer> detail : list) {
				// 须有一项 达Y级
				for (Integer lvl : detail.keySet()) {
					if (lvl >= needNum2)// 满足条件
					{
						p2 += 1;
						break;
					}
				}
			}
			if (p2 >= needNum1) {
				saveString2DbProgress(type, String.valueOf(t.id));
			}
		}
	}

	/**
	 * n名武将x个属性修炼到顶级
	 * 
	 * @param type
	 * @param list
	 */
	private void comparaHeroPMaxLvlStringEveryAction(String type, List<List<Integer>> list) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		for (AchieveT t : map.values()) {
			// 只有未完成状态可更新
			if (getAchieveStatus(t.id) != 0)
				continue;
			String[] nums = t.condition.split("_");
			int needNum1 = Integer.valueOf(nums[0]);// N名
			int needNum2 = Integer.valueOf(nums[1]);// 条件X个
			int p1 = list.size();// 统计出的数量 例如名次
			if (p1 < needNum1)// 武将不够
				continue;
			int p2 = 0;
			for (List<Integer> detail : list) {
				if (detail.size() >= needNum2)
					p2 += 1;
			}
			if (p2 >= needNum1) {
				saveString2DbProgress(type, String.valueOf(t.id));
			}
		}
	}

	/**
	 * 任意1个属性修炼到10级的武将数量：%s
	 * 
	 * @param type
	 * @param list
	 */
	private void comparaHeroP10LvlStringEveryAction(String type) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		for (AchieveT t : map.values()) {
			// 只有未完成状态可更新
			if (getAchieveStatus(t.id) != 0)
				continue;
			String[] nums = t.condition.split("_");
			int needNum1 = Integer.valueOf(nums[0]);// N名

			List<List<Integer>> list = getHeroNPlvl(10);
			int p1 = list.size();// 统计出的数量 例如名次
			// if (p1 < needNum1)// 武将不够
			// continue;
			// saveString2DbProgress(type, String.valueOf(p1));
			// addNums4EveryAction(type, p1);
			pointRiseEveryAction(type, p1);
		}
	}

	/**
	 * 适用于特殊类型成就(完成一系列类型所有成就的成就)
	 * 
	 * @param type
	 * @param para
	 */
	private void updateSpecialAchieveprogress(String type, String para) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		for (AchieveT t : map.values()) {
			// 只有未完成状态可更新
			if (getAchieveStatus(t.id) != 0)
				continue;
			saveString2DbProgress(type, para);
		}
	}

	@Override
	public AchievePageView achievePageView(int fId) throws NoteException {
		AchievePageView view = null;
		Map<Integer, AchieveT> achieveInfoMap = XsgAchieveManager.getInstance().getAchieveMap();
		int maxNum = achieveInfoMap.size();
		int completedNum = db.getAchieveCompletedCount();
		List<AchieveInfoSub> achieveList = new ArrayList<AchieveInfoSub>();
		for (String type : XsgAchieveManager.getInstance().getSplitTypeMap().keySet()) {
			List<List<AchieveT>> achieves = XsgAchieveManager.getInstance().getSplitTypeMap().get(type);
			if (achieves == null || achieves.size() == 0)
				continue;
			for (List<AchieveT> details : achieves) {
				for (AchieveT achieve : details) {
					if (rt.getLevel() < achieve.needLvl)
						continue;
					// 当前成就状态
					byte status = getAchieveStatus(achieve.id);
					AchieveInfoSub sub = new AchieveInfoSub();
					sub.order = achieve.order;
					sub.id = achieve.id;
					sub.status = status;
					sub.progress = getViewProgress(achieve, status);
					// 无前后置的成就 直接显示
					if (achieve.nextId == 0 && achieve.frontId == 0) {
						achieveList.add(sub);
					} else {
						// 排在前面的 未完成 可领取的 优先显示
						if (status == 0 || status == 1) {
							achieveList.add(sub);
							break;
						} else {
							// 是否最后一个的判断
							if (achieve.nextId == 0) {
								achieveList.add(sub);
								break;
							} else {
								// 下一个成就不满足等级条件 则显示本条成就
								int nextNeedLvl = achieveInfoMap.get(achieve.nextId).needLvl;
								if (rt.getLevel() < nextNeedLvl) {
									achieveList.add(sub);
									break;
								}
							}

						}
					}
				}
			}
		}
		Collections.sort(achieveList, new Comparator<AchieveInfoSub>() {
			@Override
			public int compare(AchieveInfoSub o1, AchieveInfoSub o2) {
				return o1.order - o2.order;
			}
		});
		view = new AchievePageView(isCanRecProgressAward() ? 1 : 0, completedNum, maxNum,
				achieveList.toArray(new AchieveInfoSub[0]));
		return view;
	}

	/**
	 * 获得成就的界面显示进度
	 * 
	 * @param achieve
	 * @param status
	 * @return
	 */
	private int getViewProgress(AchieveT achieve, byte status) {
		// 可领取或已完成条目 也显示进度
		// if (status == 1 || status == 2 || status == 3) {
		// return achieve.maxNum;
		// }

		if (status == -1)
			return 0;
		// 处理未完成的
		if (achieve.maxNum == 0)
			return 0;
		if (achieve.type.equals(AchieveTemplate.KingLv.name())) {
			return rt.getLevel() > achieve.maxNum ? achieve.maxNum : rt.getLevel();
		}
		if (achieve.type.equals(AchieveTemplate.AllAchievement.name())) {
			return getCompletedCount();
		}
		if (achieve.type.equals(AchieveTemplate.Partnar.name())) {
			return getHeroPartnerPositNum() > achieve.maxNum ? achieve.maxNum : getHeroPartnerPositNum();
		}
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(achieve.type);
		if (achieve4Type == null || TextUtil.isBlank(achieve4Type.getProgress())) {
			// 3.0.5版本完善了成就进度，部分成就为了记录进度，新增了成就类型。所以为了兼容老数据，判断玩家是否完成改成就，不能感觉单纯的老类型判断
			for (RoleAchieve ra : roleAchieves.values()) {
				String received = ra.getReceived();
				if (null != received && Arrays.asList(received.split(",")).contains(String.valueOf(achieve.id))) {
					return achieve.maxNum;
				}
			}
			return 0;
		}
		switch (AchieveTemplate.getAchieveTemplate(achieve.type)) {
		case FiveStarPassNum:// 5星通关的关卡达到n个
		case XinshouSweepNum:// 累积扫荡新手关卡n次
		case GaoshouSweepNum:// 累积扫荡高手关卡n次
		case GodSweepNum:// 累积扫荡大神关卡n次
		case XinshouStarNum: // 新手关卡星数达到n颗
		case GaoshouStarNum:// 高手关卡星数达到n颗
		case GodStarNum:// 大神关卡星数达到n颗
		case GodNum:// 挑战大神难度n次
		case InfernalNum:// 挑战地狱难度n次
		case SweepXinshouNum:// 扫荡新手难度n次
		case SweepGaoshouNum:// 扫荡高手难度n次
		case LearnedPoint:
		case FailedTimes:// 竞技场失败n次
		case BeifaStars: // 北伐累积获得n星
		case PassNums:// 通关关卡达到n个
		case LoserNums:// 随机pk认怂333次（最强忍者）
		case TotalHurt:// 对乱世魔王累积造成n点伤害
		case SendJunling:// 送好友军令累积n枚
		case QieChuoCount:// 与好友切磋累积n次
		case GuildDHurtCount:// 团队副本累积伤害达到n
		case DonateJiangzhang:// 累积捐献公会奖章n个
		case AuctionSuccessNums:// 累积竞拍成功n次
		case AuctionNums:// 累积拍卖n次
		case OnlineGiftNums:// 累积领在线礼包n个
		case LuckManNum:// 累计获得运气王n次
		case CompleteDayTaskNum:// 累积完成n个日常任务
		case CompleteMainTaskNum:// 累积完成n个主线任务
		case SPAwardTimes:// 累积获得n次尾刀奖励
		case BuyPlusDays:
		case XunbaoCountTime:
		case StarHeroDays:
		case BussinessManDays:
		case loginDayCounts:
		case PartnarIsYuanfen:
		case ConWinDays:
		case JoinQunXiong:
		case FirstWinDays:
		case FriendNums:
		case BeifaStarsDays:
		case BeifaStarsDays50:
		case HeroNum:
		case ConLuanshiDays:
		case StoneCounts:
		case GuildFightDays:
		case GuildGiftNums:
		case Guanjie:
		case GoldNum:
		case Power:
		case FriendPoint:
		case PracticeLv10:
		case HeroStar5:
		case EquipLvlNumFull:
		case lineCounts:
		case StoneLvl4Counts:
		case StoneLvl5Counts:
		case StoneLvl6Counts:
		case BreakThrough1:
		case BreakThrough2:
		case BreakThrough3:
		case BreakThrough4:
		case BreakThrough5:
		case PracticeTop8:
			return Integer.valueOf(achieve4Type.getProgress());
		case LawQlvlNumB: // 收集蓝色，紫色阵法 补丁，修复已领取的进度不满足bug (已领取  19/25)
		case LawQlvlNumP:
			// 已领取
			if (isRecAward(achieve.id)) {
				return achieve.maxNum;
			}
			return Integer.valueOf(achieve4Type.getProgress());
		case EquipTypes: // 装备种类
			return achieve4Type.getProgress().split(",").length;
		default:
			return 0;
		}
	}

	@Override
	public AchievePageView achieveReward(int id) throws NoteException {

		AchieveT t = XsgAchieveManager.getInstance().getAchieveMap().get(id);
		if (null == t)
			throw new NoteException(Messages.getString("AchieveControler.0"));
		int functionId = XsgAchieveManager.getInstance().getFunctionByID(id);
		if (functionId < 0)
			throw new NoteException(Messages.getString("AchieveControler.0"));
		if (getAchieveStatus(id) != 1)
			throw new NoteException(Messages.getString("AchieveControler.1"));
		Map<String, String> specialConfig = XsgAchieveManager.getInstance().getSpecialTypeMap();
		TreeMap<Integer, Byte> frontStatusMap = null;
		String target = "";
		if (null != specialConfig.get(t.type)) {
			// 关联目标成就类型
			target = specialConfig.get(t.type);
			// 跟新前当前类型下的成就状态信息
			frontStatusMap = getStatusByType(target);
		}
		addAchieveAward2DB(id);
		// 发送奖励
		try {
			achieveAward(id);
		} catch (NotEnoughMoneyException e) {
			e.printStackTrace();
		}
		getAchieveEvent.onGetAchieve(id);
		// 特殊成就类型的更新
		updateSpecialAchieve(functionId, t.type);
		// 嵌套类型的客户端通知判断
		isNotifySpecialType(frontStatusMap, target);
		isNotifyAllCompleted();
		return achievePageView(functionId);
	}

	private void isNotifySpecialType(TreeMap<Integer, Byte> frontStatusMap, String target) {
		// 只做客户端提醒的更新 此处处理关联成就类型 PVP PVE1...
		if (null != frontStatusMap && !TextUtil.isBlank(target)) {
			List<Integer> changeStatus = getChangeAchieveIds(frontStatusMap, getStatusByType(target));
			if (changeStatus.size() > 0) {
				// 协议
				this.rt.getNotifyControler().showAchieve(changeStatus);
				notifyAllServer(changeStatus);
			}
		}
	}

	private void isNotifyAllCompleted() {
		// 只做客户端提醒的更新 全部完成成就的判断
		if (null != XsgAchieveManager.getInstance().getAchieveInfoMap().get(AchieveTemplate.AllAchievement.name())) {
			int needCount = XsgAchieveManager.getInstance().getAchieveCount();
			int currentCompleted = getCompletedCount();
			if (currentCompleted >= needCount) {
				int aId = XsgAchieveManager.getInstance().getAchieveInfoMap()
						.get(AchieveTemplate.AllAchievement.name()).firstKey();
				List<Integer> status = new ArrayList<Integer>();
				status.add(aId);
				this.rt.getNotifyControler().showAchieve(status);
				notifyRedPoint();
				notifyAllServer(status);
			}

		}
	}

	private void updateSpecialAchieve(int functionid, String type) {
		// 找到这个FUNCTION下的特殊类型成就
		String specialType = XsgAchieveManager.getInstance().getSpecialType4FId(functionid);
		if (TextUtil.isBlank(specialType))
			return;
		Map<String, TreeMap<Integer, AchieveT>> baseMap = XsgAchieveManager.getInstance().getAchieveInfoMap();
		TreeMap<Integer, AchieveT> map = baseMap.get(type);
		int count = map.size();
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve = roleAchieves.get(type);
		if (TextUtil.isBlank(achieve.getReceived()))
			return;
		int completedSize = achieve.getReceived().split(",").length;
		// 该类型是否全部达成的判断
		if (completedSize < count)
			return;
		updateAchieveProgress(specialType, type);
	}

	/**
	 * 成就奖励
	 * 
	 * @param achieveId
	 * @throws NotEnoughMoneyException
	 */
	private void achieveAward(int achieveId) throws NotEnoughMoneyException {
		Map<Integer, AchieveT> achieveMap = XsgAchieveManager.getInstance().getAchieveMap();
		AchieveT achieveT = achieveMap.get(achieveId);
		if (null == achieveT)
			return;
		this.rt.winJinbi(achieveT.gold);
		try {
			this.rt.winYuanbao(achieveT.rmby, true);
		} catch (NotEnoughYuanBaoException e) {
			LogManager.error(e);
		}
		if (!TextUtil.isBlank(achieveT.item)) {
			this.rt.getRewardControler().acceptReward(achieveT.item, achieveT.itemNum);
		}
	}

	@Override
	public void onAttackCastleEnd(int nodeIndex, byte heroCount, byte heroRemain, int star) {

		updateAchieveProgress(AchieveTemplate.BeifaStars.name(), star + "");
		updateAchieveProgress(AchieveTemplate.BeifaStarsDays.name(), "");
		updateAchieveProgress(AchieveTemplate.BeifaStarsDays50.name(), "");
	}

	/** 公会捐赠事件 */
	@Override
	public void onFactionDonation(String roleId, int num) {

		updateAchieveProgress(AchieveTemplate.DonateJiangzhang.name(), num + "");
	}

	/** 时空战役事件 */
	@Override
	public void onPassBattle(int id, boolean isClear, int junling) {

		TimeBattleT t = XsgTimeBattleManage.getInstance().getTimeBattleTById(id);
		if (isClear) {
			if (t.diff == 0)
				updateAchieveProgress(AchieveTemplate.SweepXinshouNum.name(), 1 + "");
			if (t.diff == 1)
				updateAchieveProgress(AchieveTemplate.SweepGaoshouNum.name(), 1 + "");
		} else {
			if (t.diff == 2)
				updateAchieveProgress(AchieveTemplate.GodNum.name(), 1 + "");
			if (t.diff == 3)
				updateAchieveProgress(AchieveTemplate.InfernalNum.name(), 1 + "");
		}

	}

	/** 召唤 */
	@Override
	public void onTraderCalled(TraderType traderType, CurrencyType currencyType) {

		if (traderType == TraderType.Hero)
			updateAchieveProgress(AchieveTemplate.StarHeroDays.name(), "");
		if (traderType == TraderType.Trader)
			updateAchieveProgress(AchieveTemplate.BussinessManDays.name(), "");
	}

	/** 群雄争霸 */
	@Override
	public void onFight(String rivalId, int resFlag, int fightStar) {
		updateAchieveProgress(AchieveTemplate.JoinQunXiong.name(), "");
	}

	@Override
	public void onFriendFight(String targetId, int resFlag) {

		updateAchieveProgress(AchieveTemplate.QieChuoCount.name(), 1 + "");
	}

	@Override
	public void onHeroJoin(IHero hero, HeroSource source) {

		updateAchieveProgress(AchieveTemplate.HeroNum.name(), "");
		updateAchieveProgress(AchieveTemplate.PartnarIsYuanfen.name(), "");
	}

	/** 武将星级变更事件 */
	@Override
	public void onHeroStarUp(IHero hero, int beforeStar) {

		updateAchieveProgress(AchieveTemplate.HeroStar.name(), "");
		updateAchieveProgress(AchieveTemplate.HeroStar5.name(), "");
		// N名上阵武将达到X星
		updateAchieveProgress(AchieveTemplate.BattleHeroStar.name(), "");

	}

	@Override
	public void onFormationPositionChange(IFormation formation, int pos, IHero hero) {
		// N名上阵武将达到X星
		updateAchieveProgress(AchieveTemplate.BattleHeroStar.name(), "");

		updateAchieveProgress(AchieveTemplate.PartnarIsYuanfen.name(), "");
	}

	@Override
	public void onHeroBreakUp(IHero hero, int beforeBreakLevel) {
		updateAchieveProgress(AchieveTemplate.BreakThrough.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough1.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough2.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough3.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough4.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough5.name(), "");
	}

	@Override
	public void onJinbiChange(long change) throws Exception {
		if (change > 0)
			updateAchieveProgress(AchieveTemplate.GoldNum.name(), String.valueOf(change));
	}

	@Override
	public void onHeroPractice(IHero hero, int index, String name, int coloe, int oldLevel, int oldExp, int addExp,
			int newLevel, int newExp, int sumGx) {
		updateAchieveProgress(AchieveTemplate.PracticeNum.name(), "");
		updateAchieveProgress(AchieveTemplate.PracticeLv.name(), "");
		updateAchieveProgress(AchieveTemplate.PracticeLv10.name(), "");
		updateAchieveProgress(AchieveTemplate.PracticeTop.name(), "");
		updateAchieveProgress(AchieveTemplate.PracticeTop8.name(), "");
	}

	@Override
	public void onItemCountChange(IItem item, int change) {
		if (change <= 0)
			return;
		if (item.getView().iType == ItemType.EquipItemType) {
			// updateAchieveProgress(AchieveTemplate.EquipQLvlNum.name(), "");
			updateAchieveProgress(AchieveTemplate.EquipTypes.name(), item.getTemplate().getId());
		}
		if (item.getView().iType == ItemType.FormationBuffItemType) {
			if (item.getTemplate().getColor() == QualityColor.Blue) {
				updateAchieveProgress(AchieveTemplate.LawQlvlNumB.name(), "");
			} else if (item.getTemplate().getColor() == QualityColor.Violet) {
				updateAchieveProgress(AchieveTemplate.LawQlvlNumP.name(), "");
			}
		}
		if (XsgItemManager.getInstance().findAbsItemT(item.getView().templateId) instanceof GemT) {
			updateAchieveProgress(AchieveTemplate.StoneLvl4Counts.name(), "");
			updateAchieveProgress(AchieveTemplate.StoneLvl5Counts.name(), "");
			updateAchieveProgress(AchieveTemplate.StoneLvl6Counts.name(), "");
		}

	}

	@Override
	public void onEquipRebuild(EquipItem equip) {
		updateAchieveProgress(AchieveTemplate.EquipQLvlNum.name(), "");
	}

	@Override
	public void onEquipStrengthen(int auto, EquipItem equip, int beforeLevel, int afterLevel) {
		updateAchieveProgress(AchieveTemplate.EquipLvlNum.name(), "");
		updateAchieveProgress(AchieveTemplate.EquipLvlNumFull.name(), "");
	}

	@Override
	public void onEquipStarUp(EquipItem equip, int uplevel, List<EquipItem> deleteList, int money, int addExp,
			Map<String, Integer> consumeStars) {
		// updateAchieveProgress(AchieveTemplate.EquipQLvlNum.name(), "");
		updateAchieveProgress(AchieveTemplate.EquipStarNum.name(), "");
	}

	@Override
	public void onAddFriendPoint(String targetId, int addNum, int currentNum) {
		updateAchieveProgress(AchieveTemplate.FriendPoint.name(), "");
	}

	@Override
	public void onRoleLevelup() {
		updateAchieveProgress(AchieveTemplate.KingLv.name(), "");
	}

	@Override
	public void onHeroSkillUp(IHero hero, String name, int oldLevel, int newLevel) {
		updateAchieveProgress(AchieveTemplate.LearnedPoint.name(), String.valueOf(newLevel - oldLevel));
	}

	@Override
	public void onFormationBuffLevelUp(FormationBuffItem buff, int money, int expDiff, int beforeLevel, int beforeExp,
			int afterLevel, int afterExp) {
		updateAchieveProgress(AchieveTemplate.LawLvlBlueNum.name(), "");
		updateAchieveProgress(AchieveTemplate.LawLvlPurpleNum.name(), "");
	}

	@Override
	public void onPartnerPositionChange(int pos, int id, String specialHeroCode) {
		updateAchieveProgress(AchieveTemplate.Partnar.name(), "");
	}

	@Override
	public void onArenaFight(int resFlag, int roleRank, int rivalRank, int sneerId, String reward) {
		updateAchieveProgress(AchieveTemplate.ConWinDays.name(), resFlag + "");
		if (resFlag == 0)
			updateAchieveProgress(AchieveTemplate.FailedTimes.name(), 1 + "");
	}

	@Override
	public void relationChanged(String target, SNSType relationType, RelationChangeEventActionType actionType) {
		updateAchieveProgress(AchieveTemplate.FriendNums.name(), "");
	}

	/** 累积竞拍成功n次 */
	@Override
	public void onAuctionHouseSettle(String sellerId, String bidderId, String templateId, int num, long price,
			int type, int success) {
		// if (sellerId.equals(rt.getRoleId())) {
		// if (success == 1)
		// updateAchieveProgress(AchieveTemplate.AuctionSuccessNums.name(), 1 +
		// "");
		// updateAchieveProgress(AchieveTemplate.AuctionNums.name(), 1 + "");
		// }
	}

	@Override
	public void onGetTask(int taskId, int before, int after, int change) {
		TaskTemplT taskTempl = XsgTaskManager.getInstance().taskTemplMap.get(taskId);
		if (taskTempl.type == TaskType.daily.value())
			// 日常
			updateAchieveProgress(AchieveTemplate.CompleteDayTaskNum.name(), 1 + "");
		if (taskTempl.type == TaskType.pass.value()) {
			// 过滤天作之合任务
			if (taskTempl.target.equalsIgnoreCase(XsgTaskManager.GET_HERO_FIRST)) {
				return;
			}
			// 主线
			updateAchieveProgress(AchieveTemplate.CompleteMainTaskNum.name(), 1 + "");
		}
	}

	@Override
	public void onOnlineAward(int onlineGiftId, ItemView[] itemView) throws Exception {
		updateAchieveProgress(AchieveTemplate.OnlineGiftNums.name(), 1 + "");
	}

	/** 出征 */
	@Override
	public void onCopyCompleted(SmallCopyT templete, int star, boolean firstPass, int fightPower, int junling) {
		updateAchieveProgress(AchieveTemplate.PassNums.name(), 1 + "");
		if (star == 5)
			updateAchieveProgress(AchieveTemplate.FiveStarPassNum.name(), 1 + "");
		CopyDifficulty d = templete.getParent().getDifficulty();
		if (d == CopyDifficulty.Junior)
			updateAchieveProgress(AchieveTemplate.XinshouStarNum.name(), star + "");
		if (d == CopyDifficulty.Senior)
			updateAchieveProgress(AchieveTemplate.GaoshouStarNum.name(), star + "");
		if (d == CopyDifficulty.Top)
			updateAchieveProgress(AchieveTemplate.GodStarNum.name(), star + "");
	}

	@Override
	public void onClear(SmallCopyT copyT, CopyChallengeResultView mockView, List<ItemView> additionList) {
		CopyDifficulty d = copyT.getParent().getDifficulty();
		if (d == CopyDifficulty.Junior)
			updateAchieveProgress(AchieveTemplate.XinshouSweepNum.name(), 1 + "");
		if (d == CopyDifficulty.Senior)
			updateAchieveProgress(AchieveTemplate.GaoshouSweepNum.name(), 1 + "");
		if (d == CopyDifficulty.Top)
			updateAchieveProgress(AchieveTemplate.GodSweepNum.name(), 1 + "");

	}

	@Override
	public void onBuyInspire(int yuanbao) {
		updateAchieveProgress(AchieveTemplate.BuyPlusDays.name(), "");
	}

	@Override
	public void onWorldBossTailAward(int hp) {
		updateAchieveProgress(AchieveTemplate.SPAwardTimes.name(), 1 + "");
	}

	@Override
	public void onEndChallenge(int harm) {
		updateAchieveProgress(AchieveTemplate.ConLuanshiDays.name(), "");
		updateAchieveProgress(AchieveTemplate.TotalHurt.name(), harm + "");

	}

	/**
	 * 登录游戏时处理
	 */
	@Override
	public void updateAchieveLogin() {
		// 累计登录
		updateAchieveProgress(AchieveTemplate.loginDayCounts.name(), "");
		updateAchieveProgress(AchieveTemplate.FriendNums.name(), "");
		insert2DB4Login(AchieveTemplate.KingLv.name(), rt.getLevel());
		insert2DB4Login(AchieveTemplate.Partnar.name(), getHeroPartnerPositNum());
		insert2DB4Login(AchieveTemplate.HeroNum.name(), rt.getHeroControler().getHeroNum());
		// 寻宝队伍
		insert2DB4Login(AchieveTemplate.lineCounts.name(), db.getRoleTreasures().size());
		// 主线任务
		insert2DB4Login(AchieveTemplate.CompleteMainTaskNum.name(), getMainTaskNum());

		// 武将突破到神
		updateAchieveProgress(AchieveTemplate.BreakThrough.name(), "");
		// 拥有五星武将数量
		updateAchieveProgress(AchieveTemplate.HeroStar5.name(), "");

		updateAchieveProgress(AchieveTemplate.BreakThrough1.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough2.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough3.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough4.name(), "");
		updateAchieveProgress(AchieveTemplate.BreakThrough5.name(), "");
		updateAchieveProgress(AchieveTemplate.PracticeTop8.name(), "");
		updateAchieveProgress(AchieveTemplate.PracticeLv10.name(), "");
		updateAchieveProgress(AchieveTemplate.StoneLvl4Counts.name(), "");
		updateAchieveProgress(AchieveTemplate.StoneLvl5Counts.name(), "");
		updateAchieveProgress(AchieveTemplate.StoneLvl6Counts.name(), "");
		updateAchieveProgress(AchieveTemplate.LawQlvlNumB.name(), "");
		updateAchieveProgress(AchieveTemplate.LawQlvlNumP.name(), "");
		updateAchieveProgress(AchieveTemplate.EquipLvlNumFull.name(), "");

		updateAchieveProgress(AchieveTemplate.KingLv.name(), "");
	}

	/**
	 * 获取已完成的主线任务数量
	 * 
	 * @return
	 */
	public int getMainTaskNum() {
		int count = 0;
		// 检查领取任务的数据
		Map<Integer, RoleTask> roleTakMap = this.db.getRoleTask();
		if (roleTakMap == null || roleTakMap.size() == 0)
			return count;
		for (RoleTask task : roleTakMap.values()) {
			TaskTemplT taskTempl = XsgTaskManager.getInstance().taskTemplMap.get(task.getTaskId());
			if (null == taskTempl)
				continue;
			if (taskTempl.type == TaskType.pass.value() && task.getState() == 3)
				count += 1;
		}
		return count;
	}

	/**
	 * 处理一些 老玩家原先就完成的成就
	 * 
	 * @param type
	 * @param para
	 */
	private void insert2DB4Login(String type, int para) {
		Map<Integer, AchieveT> map = XsgAchieveManager.getInstance().getAchieveInfoMap().get(type);
		if (map == null || map.size() == 0)
			return;
		Map<String, RoleAchieve> roleAchieves = db.getRoleAchieves();
		RoleAchieve achieve4Type = roleAchieves.get(type);
		if (null == achieve4Type) {
			achieve4Type = new RoleAchieve(GlobalDataManager.getInstance().generatePrimaryKey(), db, type, para + "",
					new Date());
			roleAchieves.put(type, achieve4Type);
		}
	}

	@Override
	public void onBuyFactionShop(int id) {
		updateAchieveProgress(AchieveTemplate.GuildGiftNums.name(), 1 + "");
	}

	/** 公会副本结束挑战事件 */
	@Override
	public void onFactionCopyEndChallenge(String roleId, String factionId, int copyId, int harm) {
		updateAchieveProgress(AchieveTemplate.GuildDHurtCount.name(), harm + "");
	}

	@Override
	public void onAddGroup(int currentGroupNum) {
		updateAchieveProgress(AchieveTemplate.lineCounts.name(), currentGroupNum + "");
	}

	@Override
	public void onLevelChange(int level) {
		updateAchieveProgress(AchieveTemplate.Guanjie.name(), level + "");
	}

	@Override
	public void onSnsSendJunLing(String sender, String accepter, int num) {
		updateAchieveProgress(AchieveTemplate.SendJunling.name(), num + "");
	}

	@Override
	public void onLuckyStar() {
		updateAchieveProgress(AchieveTemplate.LuckManNum.name(), 1 + "");
	}

	@Override
	public void onGain(ItemView[] items, String addArr) {
		int num = 0;
		for (int i = 0; i < items.length; i++) {
			ItemView view = items[i];
			if (XsgItemManager.getInstance().findAbsItemT(view.templateId) instanceof GemT) {
				num += view.num;
			}
		}
		if (num > 0)
			updateAchieveProgress(AchieveTemplate.StoneCounts.name(), num + "");
		TreasureConfT t = XsgTreasureManage.getInstance().getTreasureConfT();
		updateAchieveProgress(AchieveTemplate.XunbaoCountTime.name(), t.duration / 60 + "");
	}

	@Override
	public void onFirstWin() {
		updateAchieveProgress(AchieveTemplate.FirstWinDays.name(), "");
	}

	@Override
	public void onEscape() {
		updateAchieveProgress(AchieveTemplate.LoserNums.name(), 1 + "");
	}

	@Override
	public void onHeroPositionChange(IPartner partner, int pos, IHero hero) {
		updateAchieveProgress(AchieveTemplate.PartnarIsYuanfen.name(), "");
	}

	@Override
	public void onApplyingHappend(String target) {
		updateAchieveProgress(AchieveTemplate.FriendNums.name(), "");
	}

	@Override
	public void onPartnerPositionReset(int pos, int id, String specialHeroCode, int costNum) {
		updateAchieveProgress(AchieveTemplate.PartnarIsYuanfen.name(), "");
	}

	@Override
	public void onCombatPowerChange(int old, int newValue) {
		updateAchieveProgress(AchieveTemplate.Power.name(), newValue + "");
	}

	/**
	 * 累计参加公会战天数(通过挖宝事件触发)
	 */
	@Override
	public void onDiggingTreasure(int strongholdId, int forage, IntString[] views) {
		updateAchieveProgress(AchieveTemplate.GuildFightDays.name(), "");
	}
}
