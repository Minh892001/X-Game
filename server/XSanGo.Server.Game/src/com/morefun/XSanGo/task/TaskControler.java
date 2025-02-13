/**
 * 
 */
package com.morefun.XSanGo.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.ActPointAward;
import com.XSanGo.Protocol.BuyHeroResult;
import com.XSanGo.Protocol.CopyChallengeResultView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.QualityColor;
import com.XSanGo.Protocol.SevenTarget;
import com.XSanGo.Protocol.SevenTargetStarReward;
import com.XSanGo.Protocol.SevenTargetView;
import com.XSanGo.Protocol.SevenThreeStarReward;
import com.XSanGo.Protocol.TaskItem;
import com.XSanGo.Protocol.TaskType;
import com.XSanGo.Protocol.TaskView;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.activity.UpGiftT;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.copy.SmallCopyT;
import com.morefun.XSanGo.db.game.RedPacket;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleRedPacket;
import com.morefun.XSanGo.db.game.RoleSevenProgress;
import com.morefun.XSanGo.db.game.RoleSevenTask;
import com.morefun.XSanGo.db.game.RoleTask;
import com.morefun.XSanGo.db.game.RoleTaskActive;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IActPointReward;
import com.morefun.XSanGo.event.protocol.IAddSalary;
import com.morefun.XSanGo.event.protocol.IApproveJoin;
import com.morefun.XSanGo.event.protocol.IArenaFight;
import com.morefun.XSanGo.event.protocol.IArenaRevenge;
import com.morefun.XSanGo.event.protocol.IAttackCastleEnd;
import com.morefun.XSanGo.event.protocol.IBuy10WineByJinbi;
import com.morefun.XSanGo.event.protocol.IBuy10WineByYuanbao;
import com.morefun.XSanGo.event.protocol.IBuyJinbi;
import com.morefun.XSanGo.event.protocol.IBuyJunLing;
import com.morefun.XSanGo.event.protocol.IBuyLimitHero;
import com.morefun.XSanGo.event.protocol.IBuySingleWineByJinbi;
import com.morefun.XSanGo.event.protocol.IBuySingleWineByYuanbao;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.ICopyClear;
import com.morefun.XSanGo.event.protocol.ICopyCompleted;
import com.morefun.XSanGo.event.protocol.ICreateFaction;
import com.morefun.XSanGo.event.protocol.IEquipRebuild;
import com.morefun.XSanGo.event.protocol.IEquipSmelt;
import com.morefun.XSanGo.event.protocol.IEquipStrengthen;
import com.morefun.XSanGo.event.protocol.IFactionCopyEndChallenge;
import com.morefun.XSanGo.event.protocol.IFactionDonateTec;
import com.morefun.XSanGo.event.protocol.IFactionDonation;
import com.morefun.XSanGo.event.protocol.IFriendApplying;
import com.morefun.XSanGo.event.protocol.IFriendFight;
import com.morefun.XSanGo.event.protocol.IGetSevenTask;
import com.morefun.XSanGo.event.protocol.IGetTask;
import com.morefun.XSanGo.event.protocol.IHeroBreakUp;
import com.morefun.XSanGo.event.protocol.IHeroJoin;
import com.morefun.XSanGo.event.protocol.IHeroLevelUp;
import com.morefun.XSanGo.event.protocol.IHeroQualityUp;
import com.morefun.XSanGo.event.protocol.IHeroSkillUp;
import com.morefun.XSanGo.event.protocol.IHeroStarUp;
import com.morefun.XSanGo.event.protocol.IHeroUseItem;
import com.morefun.XSanGo.event.protocol.IItemChipStrut;
import com.morefun.XSanGo.event.protocol.IJinbiChange;
import com.morefun.XSanGo.event.protocol.ILadderFight;
import com.morefun.XSanGo.event.protocol.ILadderLevelChange;
import com.morefun.XSanGo.event.protocol.ILevelGift;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.event.protocol.IRoulette;
import com.morefun.XSanGo.event.protocol.IShopping;
import com.morefun.XSanGo.event.protocol.ISmithyBlueMallExchange;
import com.morefun.XSanGo.event.protocol.ISnsFriendPoint;
import com.morefun.XSanGo.event.protocol.ISnsRelationChange;
import com.morefun.XSanGo.event.protocol.ISnsSendJunLing;
import com.morefun.XSanGo.event.protocol.ITimeBattle;
import com.morefun.XSanGo.event.protocol.ITraderCall;
import com.morefun.XSanGo.event.protocol.ITreasureDepart;
import com.morefun.XSanGo.event.protocol.ITreasureGain;
import com.morefun.XSanGo.event.protocol.ITreasureHelpFriend;
import com.morefun.XSanGo.event.protocol.IVipGiftBuy;
import com.morefun.XSanGo.event.protocol.IVipLevelUp;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.sign.RandomRoulette;
import com.morefun.XSanGo.sns.SNSType;
import com.morefun.XSanGo.sns.SnsController.RelationChangeEventActionType;
import com.morefun.XSanGo.task.XsgTaskManager.SevenTargetTemplt;
import com.morefun.XSanGo.trader.TraderType;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.ChargeItemT;
import com.morefun.XSanGo.vip.XsgVipManager;

/**
 * @author 吕明涛
 * 
 */
@RedPoint(isTimer = true)
class TaskControler implements ITaskControler, ICopyClear, IArenaRevenge, ICreateFaction, ITreasureDepart,
		IFriendApplying, IHeroBreakUp, ICopyCompleted, IEquipStrengthen, IHeroSkillUp, IRoleLevelup, IJinbiChange,
		IHeroStarUp, IHeroJoin, ILevelGift, IHeroUseItem, IHeroQualityUp, IItemChipStrut, IArenaFight, ITraderCall,
		ITimeBattle, IEquipRebuild, IBuySingleWineByJinbi, IBuySingleWineByYuanbao, IBuy10WineByJinbi,
		IBuy10WineByYuanbao, IVipLevelUp, IVipGiftBuy, ILadderFight, IFactionCopyEndChallenge, IFactionDonation,
		IAttackCastleEnd, IEquipSmelt, IAddSalary, IShopping, IRoulette, ICharge, IBuyJinbi, IFriendFight,
		IHeroLevelUp, ISnsFriendPoint, ITreasureGain, ISnsSendJunLing, IApproveJoin, ILadderLevelChange,
		ISnsRelationChange, ISmithyBlueMallExchange, IBuyLimitHero, IBuyJunLing, ITreasureHelpFriend, IFactionDonateTec {

	private static final Log log = LogFactory.getLog(TaskControler.class);

	private IRole roleRt;
	private Role roleDb;
	private IGetTask getTaskEvent;
	private IGetSevenTask getSevenTaskEvent;
	private IActPointReward actPointReward;

	public TaskControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;
		getTaskEvent = this.roleRt.getEventControler().registerEvent(IGetTask.class);
		if (roleDb.getRoleTaskActive() == null) {
			roleDb.setRoleTaskActive(new RoleTaskActive(GlobalDataManager.getInstance().generatePrimaryKey(), roleDb,
					0, "", null));
		}
		getSevenTaskEvent = this.roleRt.getEventControler().registerEvent(IGetSevenTask.class);
		actPointReward = this.roleRt.getEventControler().registerEvent(IActPointReward.class);

		IEventControler evtContrl = roleRt.getEventControler();
		// 地图过关完成任务事件
		evtContrl.registerHandler(IBuyLimitHero.class, this);
		evtContrl.registerHandler(ICopyClear.class, this);
		evtContrl.registerHandler(IArenaRevenge.class, this);
		evtContrl.registerHandler(ITreasureDepart.class, this);
		evtContrl.registerHandler(IFriendApplying.class, this);
		evtContrl.registerHandler(IHeroBreakUp.class, this);
		evtContrl.registerHandler(ISnsRelationChange.class, this);
		evtContrl.registerHandler(ILadderLevelChange.class, this);
		evtContrl.registerHandler(IApproveJoin.class, this);
		evtContrl.registerHandler(ITreasureGain.class, this);
		evtContrl.registerHandler(ICopyCompleted.class, this);
		evtContrl.registerHandler(IEquipStrengthen.class, this);
		evtContrl.registerHandler(IHeroSkillUp.class, this);
		evtContrl.registerHandler(IRoleLevelup.class, this);
		evtContrl.registerHandler(IJinbiChange.class, this);
		evtContrl.registerHandler(IHeroStarUp.class, this);
		evtContrl.registerHandler(IBuySingleWineByJinbi.class, this);
		evtContrl.registerHandler(IHeroJoin.class, this);
		evtContrl.registerHandler(ILevelGift.class, this);
		evtContrl.registerHandler(IHeroUseItem.class, this);
		evtContrl.registerHandler(IHeroQualityUp.class, this);
		evtContrl.registerHandler(IItemChipStrut.class, this);
		evtContrl.registerHandler(IArenaFight.class, this);
		evtContrl.registerHandler(ITraderCall.class, this);
		evtContrl.registerHandler(ITimeBattle.class, this);
		evtContrl.registerHandler(IEquipRebuild.class, this);
		evtContrl.registerHandler(IBuySingleWineByYuanbao.class, this);
		evtContrl.registerHandler(IBuy10WineByJinbi.class, this);
		evtContrl.registerHandler(IBuy10WineByYuanbao.class, this);
		evtContrl.registerHandler(IVipLevelUp.class, this);
		evtContrl.registerHandler(IVipGiftBuy.class, this);
		evtContrl.registerHandler(IEquipSmelt.class, this);
		evtContrl.registerHandler(IAddSalary.class, this);
		evtContrl.registerHandler(IShopping.class, this);
		evtContrl.registerHandler(IRoulette.class, this);
		evtContrl.registerHandler(ICharge.class, this);
		evtContrl.registerHandler(ILadderFight.class, this);
		evtContrl.registerHandler(IFactionDonation.class, this);
		evtContrl.registerHandler(IFactionCopyEndChallenge.class, this);
		evtContrl.registerHandler(IAttackCastleEnd.class, this);
		evtContrl.registerHandler(IBuyJinbi.class, this);
		evtContrl.registerHandler(IFriendFight.class, this);
		evtContrl.registerHandler(IHeroLevelUp.class, this);
		evtContrl.registerHandler(ISnsFriendPoint.class, this);
		evtContrl.registerHandler(ISnsSendJunLing.class, this);
		evtContrl.registerHandler(ISmithyBlueMallExchange.class, this);
		evtContrl.registerHandler(IBuyJunLing.class, this);
		evtContrl.registerHandler(IFactionDonateTec.class, this);
		evtContrl.registerHandler(ITreasureHelpFriend.class, this);
	}

	/**
	 * 获取活跃点宝箱领奖状态(奖励状态 0:不可领取 1:可领取 2:已领取)
	 * 
	 * @param id
	 * @return
	 */
	private int getActPointStatus(int id) {
		Map<Integer, TaskActivePointTemplT> baseConfig = XsgTaskManager.getInstance().getActiveAwardMap();
		TaskActivePointTemplT t = baseConfig.get(id);
		RoleTaskActive dbActive = roleDb.getRoleTaskActive();
		if (null == dbActive)
			return 0;
		if (TextUtil.isBlank(dbActive.getReceived())) {
			if (dbActive.getPoint() >= t.point)
				return 1;
		} else {
			if (dbActive.getReceived().indexOf(id + ",") != -1)
				return 2;
			if (dbActive.getPoint() >= t.point)
				return 1;
		}
		return 0;
	}

	/**
	 * 增加活跃点
	 * 
	 * @param point
	 */
	private void addActPoint(int point) {
		if (point <= 0)
			return;
		RoleTaskActive dbActive = roleDb.getRoleTaskActive();
		if (null == dbActive) {
			dbActive = new RoleTaskActive(GlobalDataManager.getInstance().generatePrimaryKey(), roleDb, 0, "", null);
			roleDb.setRoleTaskActive(dbActive);
		}
		if (dbActive.getUpdateDate() != null) {
			if (!DateUtil.isSameDay(dbActive.getUpdateDate(), new Date())) {
				if (!TextUtil.isBlank(dbActive.getReceived())) {
					dbActive.setReceived("");
				}
				dbActive.setPoint(0 + point); // 当日首次完成带活跃点的任务，加上活跃点
				dbActive.setUpdateDate(new Date());
				return;
			}
		}
		dbActive.setPoint(dbActive.getPoint() + point);
		dbActive.setUpdateDate(new Date());
	}

	/**
	 * 领取活跃点奖励
	 * 
	 * @param id
	 */
	public void recActPointAward(int id) throws NoteException {
		Map<Integer, TaskActivePointTemplT> baseConfig = XsgTaskManager.getInstance().getActiveAwardMap();
		TaskActivePointTemplT t = baseConfig.get(id);
		if (null == t)
			throw new NoteException(Messages.getString("TaskControler.7"));
		if (getActPointStatus(id) != 1)
			throw new NoteException(Messages.getString("TaskControler.8"));
		if (!TextUtil.isBlank(t.award)) {
			ItemView[] view = XsgRewardManager.getInstance().doTcToItem(roleRt, t.award);
			this.roleRt.getRewardControler().acceptReward(view);
			upDateActPointAward2DB(id);
			actPointReward.onAccept(t.point);
		}
	}

	/**
	 * 领取总星数量奖励
	 * 
	 * @param star
	 */
	public void receiveStarAward(int star) throws NoteException {
		RoleSevenTask roleSevenTask = roleDb.getRoleSevenTask();
		if (roleSevenTask == null)
			throw new NoteException(Messages.getString("TaskControler.7"));
		StarAwardTemplT t = XsgTaskManager.getInstance().starAwardT.get(star);
		if (null == t)
			throw new NoteException(Messages.getString("TaskControler.7"));
		int status = getStarNumStatus(star);
		if (status != 1)
			throw new NoteException(Messages.getString("TaskControler.8"));
		if (TextUtil.isBlank(roleSevenTask.getStarAwardRecs()))
			roleSevenTask.setStarAwardRecs(star + ",");
		else
			roleSevenTask.setStarAwardRecs(roleSevenTask.getStarAwardRecs() + star + ",");
		for (String item : t.award.keySet()) {
			this.roleRt.getRewardControler().acceptReward(item, t.award.get(item));
		}
		getSevenTaskEvent.onGetSevenTask(3, star);
	}

	/**
	 * 领取日常单个星级奖励
	 * 
	 * @param index
	 */
	public String receiveTodayAward(int index) throws NoteException {

		SevenTaskTemplT t = XsgTaskManager.getInstance().sevenTaskBaseT.get(index);
		if (null == t)
			throw new NoteException(Messages.getString("TaskControler.7"));
		int status = getSevenTargetStatus(index);
		if (status != 1) {
			if (status == 0)
				throw new NoteException(t.prompt);
			else
				throw new NoteException(Messages.getString("TaskControler.8"));
		}
		RoleSevenProgress progressInfo = roleDb.getRoleSevenTasks().get(index);
		if (null == progressInfo && t.target.equals(SevenTargetTemplt.KingLv.name())) {
			progressInfo = new RoleSevenProgress(GlobalDataManager.getInstance().generatePrimaryKey(), roleDb, t.id,
					null, new Date());
			roleDb.getRoleSevenTasks().put(t.id, progressInfo);
		} else {
			progressInfo.setRecDate(new Date());
		}
		this.roleRt.getRewardControler().acceptReward(t.item, t.itemNum);
		addCountStar(1);
		SevenTargetView view = getSevenTargetViewInfo();
		getSevenTaskEvent.onGetSevenTask(1, index);
		return LuaSerializer.serialize(view);
	}

	/**
	 * 领取三星奖励
	 * 
	 * @param index
	 */
	public void receiveThreeStarAward() throws NoteException {
		int day = getCurDay();
		RoleSevenTask roleSevenTask = roleDb.getRoleSevenTask();
		if (roleSevenTask == null)
			throw new NoteException(Messages.getString("TaskControler.7"));
		int status = getThreeStarStatus(day);
		if (status != 1)
			throw new NoteException(Messages.getString("TaskControler.8"));
		ThreeStarTemplT t = XsgTaskManager.getInstance().threeStarT.get(day);
		if (TextUtil.isBlank(roleSevenTask.getThreeStarRecs()))
			roleSevenTask.setThreeStarRecs(day + ",");
		else
			roleSevenTask.setThreeStarRecs(roleSevenTask.getThreeStarRecs() + day + ",");
		// 发奖
		if (!TextUtil.isBlank(t.item1))
			this.roleRt.getRewardControler().acceptReward(t.item1, t.itemNum1);
		if (!TextUtil.isBlank(t.item2))
			this.roleRt.getRewardControler().acceptReward(t.item2, t.itemNum2);
		if (!TextUtil.isBlank(t.item3))
			this.roleRt.getRewardControler().acceptReward(t.item3, t.itemNum3);
		getSevenTaskEvent.onGetSevenTask(2, day);
	}

	/**
	 * 跟新活跃奖励数据到DB
	 * 
	 * @param id
	 */
	private void upDateActPointAward2DB(int id) {
		RoleTaskActive dbActive = roleDb.getRoleTaskActive();
		if (null == dbActive)
			return;
		if (TextUtil.isBlank(dbActive.getReceived())) {
			dbActive.setReceived(id + ",");
		} else {
			dbActive.setReceived(dbActive.getReceived() + id + ",");
		}
		dbActive.setRecDate(new Date());
	}

	/**
	 * 零点重置(打开页面调用)
	 */
	public void resertActPoint4ZeroTime() {
		RoleTaskActive dbActive = roleDb.getRoleTaskActive();
		if (null == dbActive || dbActive.getUpdateDate() == null)
			return;
		if (DateUtil.isSameDay(dbActive.getUpdateDate(), new Date()))
			return;
		if (!TextUtil.isBlank(dbActive.getReceived())) {
			dbActive.setReceived("");
		}
		dbActive.setPoint(0);
	}

	/**
	 * 显示任务列表 任务类型 ，1：过关，2：日常，3：成就，4:日常中的限时任务
	 */
	@Override
	public TaskView selectTask() throws NoteException {
		TaskView allView = null;
		List<TaskItem> taskViewList = new ArrayList<TaskItem>();
		Map<Integer, RoleTask> roleTaskMap = this.roleDb.getRoleTask();

		// 初始化任务 和 新增 任务
		taskViewList = this.initTask(roleTaskMap);

		// 已经存在的任务
		Map<Integer, TaskTemplT> taskTemplMap = XsgTaskManager.getInstance().taskTemplMap;
		for (RoleTask task : roleTaskMap.values()) {
			TaskTemplT taskTempl = taskTemplMap.get(task.getTaskId());
			if (taskTempl != null) {
				// 达到指定等级才显示
				if (this.roleRt.getLevel() < taskTempl.minLevel) {
					continue;
				}
				if (taskTempl.target.equalsIgnoreCase(XsgTaskManager.GET_HERO_FIRST)
						&& task.getState() != XsgTaskManager.taskStat.obtain.ordinal()) {
					continue;
				}
				// 已经过期的任务不显示
				if (!TextUtil.isBlank(taskTempl.endDate)
						&& System.currentTimeMillis() > DateUtil.parseDate(taskTempl.endDate).getTime()) {
					continue;
				}
				// 日常任务 和 日常限时
				if (taskTempl.type == TaskType.daily.ordinal() || taskTempl.type == TaskType.vipTask.ordinal()
						|| taskTempl.type == TaskType.vipMonth.ordinal()
						|| taskTempl.type == TaskType.redPacket.ordinal()) {

					TaskItem taskView = this.selectDailyTask(task, taskTempl, roleTaskMap);
					if (taskView != null) {
						taskViewList.add(taskView);
					}
					// 日常限时任务
				} else if (taskTempl.type == TaskType.dailyTime.ordinal()) {
					// 错过完成时间的不显示
					if (TextUtil.isBlank(taskTempl.endTime)
							|| System.currentTimeMillis() < DateUtil.joinTime(taskTempl.endTime).getTime()) {
						TaskItem taskView = this.selectDailyTask(task, taskTempl, roleTaskMap);
						if (taskView != null) {
							taskViewList.add(taskView);
						}
					}
					// 除日常中总是显示的任务外，不显示已经完成的任务
				} else if (task.getState() != XsgTaskManager.taskStat.finish.ordinal()) {
					// 判断任务是否已经完成
					this.autoFininshTask(taskTempl, task);
					taskViewList.add(this.setTaskView(task, taskTempl.type));
				}
			} else {
				log.warn("不存在的任务模板：" + task.getTaskId()); //$NON-NLS-1$
			}
		}
		resertActPoint4ZeroTime();
		int curActPoint = 0;
		RoleTaskActive dbActive = roleDb.getRoleTaskActive();
		if (null != dbActive)
			curActPoint = dbActive.getPoint();
		boolean isCompleted = isCompletedSevenTask();
		if (XsgGameParamManager.getInstance().getSevenTaskOpenStatus() == 0) {
			isCompleted = true;
		}
		allView = new TaskView(taskViewList.toArray(new TaskItem[0]), curActPoint, getactPointView(),
				isCompleted == true ? null : getSevenTargetViewInfo());
		return allView;
	}

	/**
	 * 活跃点信息相关客户端显示信息
	 * 
	 * @return
	 */
	private ActPointAward[] getactPointView() {
		Map<Integer, TaskActivePointTemplT> baseConfig = XsgTaskManager.getInstance().getActiveAwardMap();
		ActPointAward[] actPointAwards = new ActPointAward[baseConfig.size()];
		int index = 0;
		for (TaskActivePointTemplT t : baseConfig.values()) {
			ActPointAward info = new ActPointAward();
			info.id = t.id;
			info.icons = t.iCon;
			info.point = t.point;
			info.state = getActPointStatus(t.id);
			info.awards = wrapRewardItem(XsgRewardManager.getInstance().doTcToItem(roleRt, t.award));
			actPointAwards[index] = info;
			index += 1;
		}
		return actPointAwards;
	}

	/**
	 * 七日目标相关客户端显示信息
	 * 
	 * @return
	 */
	private SevenTargetView getSevenTargetViewInfo() {
		int starCount = 0;
		RoleSevenTask dbSevenInfo = roleDb.getRoleSevenTask();
		if (null != dbSevenInfo)
			starCount = dbSevenInfo.getStarCount();
		SevenTargetView sevenTarget = new SevenTargetView();
		sevenTarget.currentStar = starCount;
		sevenTarget.day = getCurDay();
		// 七日目标数据
		TreeMap<Integer, TreeMap<Integer, SevenTaskTemplT>> sevenTaskT = XsgTaskManager.getInstance().sevenTaskT;
		SevenTarget[] sts = new SevenTarget[sevenTaskT.keySet().size()];
		int dayIndex = 0;
		for (Integer day : sevenTaskT.keySet()) {
			Map<Integer, SevenTaskTemplT> dayOfMap = sevenTaskT.get(day);
			SevenTarget st = new SevenTarget();
			st.dayIndex = day;
			int[] taskIds = new int[dayOfMap.size()];
			String[] contents = new String[dayOfMap.size()];
			int[] states = new int[dayOfMap.size()];
			IntString[] golds = new IntString[dayOfMap.size()];
			IntIntPair[] progress = new IntIntPair[dayOfMap.size()];
			int index = 0;
			for (SevenTaskTemplT t : dayOfMap.values()) {
				taskIds[index] = t.id;
				contents[index] = t.content;
				states[index] = getSevenTargetStatus(t.id);
				golds[index] = new IntString(t.itemNum, t.item);
				RoleSevenProgress dbInfo = roleDb.getRoleSevenTasks().get(t.id);
				int curProgress = 0;
				if (null != dbInfo && !TextUtil.isBlank(dbInfo.getProgress())) {
					curProgress = Integer.valueOf(dbInfo.getProgress());
				}
				if (t.target.equals(SevenTargetTemplt.KingLv.name()))
					curProgress = roleRt.getLevel();
				if (curProgress >= t.maxNum)
					curProgress = t.maxNum;
				progress[index] = new IntIntPair(curProgress, t.maxNum);
				index += 1;
			}
			st.contents = contents;
			st.golds = golds;
			st.taskId = taskIds;
			st.states = states;
			st.progress = progress;
			sts[dayIndex] = st;
			dayIndex += 1;
		}
		sevenTarget.sevenTargets = sts;
		// 星级奖励
		TreeMap<Integer, StarAwardTemplT> starCountBaseMap = XsgTaskManager.getInstance().starAwardT;
		SevenTargetStarReward[] sevenTargetStarRewards = new SevenTargetStarReward[starCountBaseMap.size()];
		int starCountIndex = 0;
		for (StarAwardTemplT t : starCountBaseMap.values()) {
			SevenTargetStarReward sr = new SevenTargetStarReward();
			sr.star = t.starNum;
			sr.state = getStarNumStatus(t.starNum);
			sr.icons = t.icon;
			sr.reward = XsgTaskManager.getInstance().getIntString(t.reawad);
			sevenTargetStarRewards[starCountIndex] = sr;
			starCountIndex += 1;
		}
		sevenTarget.sevenTargetStarRewards = sevenTargetStarRewards;
		// 七日三星奖励
		Map<Integer, ThreeStarTemplT> threeStarCfg = XsgTaskManager.getInstance().threeStarT;
		SevenThreeStarReward[] sevenThreeStarRewards = new SevenThreeStarReward[threeStarCfg.size()];
		int threeStarIndex = 0;
		for (ThreeStarTemplT t : threeStarCfg.values()) {
			SevenThreeStarReward sr = new SevenThreeStarReward();
			sr.awards = XsgTaskManager.getInstance().getIntString(t);
			sr.sceneId = t.icon;
			sr.states = getThreeStarStatus(t.day);
			sr.title = t.title;
			sevenThreeStarRewards[threeStarIndex] = sr;
			threeStarIndex += 1;
		}
		sevenTarget.sevenThreeStarRewards = sevenThreeStarRewards;
		return sevenTarget;
	}

	/**
	 * 是否全部完成7日任务
	 * 
	 * @return
	 */
	public boolean isCompletedSevenTask() {
		Map<Integer, RoleSevenProgress> detailInfoMap = roleDb.getRoleSevenTasks();
		if (null == detailInfoMap || detailInfoMap.size() == 0)
			return false;
		if (detailInfoMap.size() < XsgTaskManager.getInstance().sevenTaskBaseT.size())
			return false;
		for (RoleSevenProgress db : detailInfoMap.values()) {
			if (db.getRecDate() == null)
				return false;
		}
		RoleSevenTask rs = roleDb.getRoleSevenTask();
		if (null == rs)
			return false;
		// 总星奖励
		if (TextUtil.isBlank(rs.getStarAwardRecs()))
			return false;
		int recNum = rs.getStarAwardRecs().split(",").length;
		if (recNum < XsgTaskManager.getInstance().starAwardT.size())
			return false;
		// 看当天的 三星奖励
		int lastThreeStarStatus = getThreeStarStatus(XsgTaskManager.getInstance().sevenTaskT.keySet().size());
		// 先领完最后一档三星
		if (lastThreeStarStatus == 1)
			return false;
		return true;
	}

	/**
	 * 获得七日目标 当前处于第几天
	 * 
	 * @return
	 */
	private int getCurDay() {
		RoleSevenTask dbSevenInfo = roleDb.getRoleSevenTask();
		if (null == dbSevenInfo || null == dbSevenInfo.getJoinDate())
			return 1;
		// 实际天数 相差的天数 +1
		int days = DateUtil.computerDaysBy2Date(dbSevenInfo.getJoinDate().getTime(), new Date().getTime()) + 1;
		// if (days >= XsgTaskManager.getInstance().sevenTaskT.size())
		// return XsgTaskManager.getInstance().sevenTaskT.size();
		return days;
	}

	/**
	 * 更新七日目标进度
	 * 
	 * @param type
	 */
	private void updateSevenTask(String type, String para) {
		if (XsgGameParamManager.getInstance().getSevenTaskOpenStatus() == 0)
			return;
		Map<Integer, SevenTaskTemplT> config = XsgTaskManager.getInstance().sevenTaskDetailT.get(type);
		if (null == config || config.size() == 0)
			return;
		boolean redPoint = false;
		for (SevenTaskTemplT t : config.values()) {
			switch (SevenTargetTemplt.getSevenTargetTemplt(t.target)) {
			case HeroLv:
				arrive2ConfigNum(t, getMaxHeroLvl(), true);
				break;
			case EquipLv:
				arrive2ConfigNum(t, getMaxEquipLvl(), true);
				break;
			case BreakUp:
				arrive2ConfigNum(t, getMaxHeroBreakUp(), true);
				break;
			case HeroStar:
				arrive2ConfigNum(t, getMaxHeroStarLvl(), true);
				break;
			case SkillLv:
				arrive2ConfigNum(t, getMaxHeroSkillLvl(), true);
				break;
			case PVPRank:
			case CraftRank:
				arrive2ConfigNum(t, Integer.valueOf(para), true);
				break;
			case Friends:
				arrive2ConfigNum(t, roleRt.getSnsController().getFriends().size(), true);
				break;
			case JionGuid:
				add2EveryDaySevenTask(SevenTargetTemplt.JionGuid.name(), 1);
			case GuildMedal:
			case Visit:
			case Harvest:
			case ShopBuy:
			case PVPS:
			case Fight:
			case Craft:
				arrive2ConfigNum(t, Integer.valueOf(para), false);
				break;
			case fortPass:
			case ST:
			case Expedition:
				compareWithString(t, para);
			default:
				break;

			}
			if ((!redPoint) && getSevenTargetStatus(t.id) == 1) {
				redPoint = true;
			}
		}
		// 触发红点
		if (redPoint) {
			roleRt.getNotifyControler().onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.TaskMenu, false));
		}
	}

	/**
	 * 获得武将的最大等级
	 * 
	 * @return
	 */
	private int getMaxHeroLvl() {
		int maxLvl = 0;
		if (null != roleRt.getHeroControler().getAllHero()) {
			Iterable<IHero> heros = roleRt.getHeroControler().getAllHero();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero he = (IHero) iter.next();
				if (maxLvl < he.getLevel())
					maxLvl = he.getLevel();
			}
		}
		return maxLvl;
	}

	/**
	 * 获得最大等级的装备
	 * 
	 * @return
	 */
	private int getMaxEquipLvl() {
		int maxLvl = 0;
		ItemView[] items = roleRt.getItemControler().getItemViewList();
		for (int i = 0; i < items.length; i++) {
			ItemView view = items[i];
			if (view.iType != ItemType.EquipItemType)
				continue;
			EquipItem equip = roleRt.getItemControler().getEquipItem(view.id);
			if (equip.getLevel() > maxLvl)
				maxLvl = equip.getLevel();
		}
		return maxLvl;
	}

	/**
	 * 获得武将的最大技能等级
	 * 
	 * @return
	 */
	private int getMaxHeroSkillLvl() {
		int maxSkillLvl = 0;
		if (null != roleRt.getHeroControler().getAllHero()) {
			Iterable<IHero> heros = roleRt.getHeroControler().getAllHero();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero he = (IHero) iter.next();
				IntIntPair[] skills = he.getSkills();
				for (int i = 0; i < skills.length; i++) {
					IntIntPair p = skills[i];
					int skillLvl = p.second;
					if (maxSkillLvl < skillLvl)
						maxSkillLvl = skillLvl;
				}
			}
		}
		return maxSkillLvl;
	}

	/**
	 * 获得武将的最大星级
	 * 
	 * @return
	 */
	private int getMaxHeroStarLvl() {
		int star = 0;
		if (null != roleRt.getHeroControler().getAllHero()) {
			Iterable<IHero> heros = roleRt.getHeroControler().getAllHero();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero he = (IHero) iter.next();
				if (star < he.getStar())
					star = he.getStar();
			}
		}
		return star;
	}

	/**
	 * 获得武将的最大进阶等级
	 * 
	 * @return
	 */
	private int getMaxHeroBreakUp() {
		int lvl = 0;
		if (null != roleRt.getHeroControler().getAllHero()) {
			Iterable<IHero> heros = roleRt.getHeroControler().getAllHero();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero he = (IHero) iter.next();
				if (lvl < he.getQualityLevel())
					lvl = he.getQualityLevel();
			}
		}
		return lvl;
	}

	/**
	 * 获取7日目标可领奖状态(完成状态 0:未完成 1:完成未领取 2:已领取 )
	 * 
	 * @param id
	 * @return
	 */
	private int getSevenTargetStatus(int id) {
		SevenTaskTemplT t = XsgTaskManager.getInstance().sevenTaskBaseT.get(id);
		if (null == t)
			return 0;
		// 属于提前完成 只有到那天才可领
		if (t.day > getCurDay()) {
			return 0;
		}
		RoleSevenProgress dbInfo = roleDb.getRoleSevenTasks().get(id);
		if (dbInfo == null) {
			if (t.target.equals(SevenTargetTemplt.KingLv.name())) {
				if (roleRt.getLevel() >= Integer.valueOf(t.demand))
					return 1;
			} else {
				return 0;
			}
		} else {
			if (null != dbInfo.getRecDate())
				return 2;
			if (t.target.equals(SevenTargetTemplt.KingLv.name())) {
				if (roleRt.getLevel() >= Integer.valueOf(t.demand))
					return 1;
			} else {
				if (TextUtil.isBlank(dbInfo.getProgress()))
					return 0;
				// 群雄争霸 竞技厂取小
				if (t.target.equals(SevenTargetTemplt.CraftRank.name())
						|| t.target.equals(SevenTargetTemplt.PVPRank.name())) {
					if (Integer.valueOf(dbInfo.getProgress()) <= Integer.valueOf(t.demand))
						return 1;
					else
						return 0;
				}
				int needNum = 0;
				if (t.target.equals(SevenTargetTemplt.fortPass.name()) || t.target.equals(SevenTargetTemplt.ST.name())
						|| t.target.equals(SevenTargetTemplt.Expedition.name())) {
					needNum = Integer.valueOf(t.demand.split(",")[1]);
				} else {
					needNum = Integer.valueOf(t.demand);
				}
				if (Integer.valueOf(dbInfo.getProgress()) >= needNum)
					return 1;
			}
		}
		return 0;
	}

	/**
	 * 获取7日目标3星奖励领奖状态(完成状态 0:未完成 1:完成未领取 2:已领取 )
	 * 
	 * @param day
	 * @return
	 */
	private int getThreeStarStatus(int day) {
		ThreeStarTemplT t = XsgTaskManager.getInstance().threeStarT.get(day);
		if (null == t)
			return 0;
		Map<Integer, SevenTaskTemplT> map = XsgTaskManager.getInstance().sevenTaskT.get(day);
		if (null == map || map.size() == 0)
			return 0;
		RoleSevenTask roleSevenTask = roleDb.getRoleSevenTask();
		if (null != roleSevenTask) {
			if (!TextUtil.isBlank(roleSevenTask.getThreeStarRecs())) {
				if (roleSevenTask.getThreeStarRecs().indexOf(day + ",") != -1)
					return 2;
			}
		}
		// 必须当天才可领
		if (t.day != getCurDay()) {
			return 0;
		}
		// 今天的所有目标 全部领取
		for (SevenTaskTemplT detail : map.values()) {
			// 今天是否是今天的判断
			RoleSevenProgress dbInfo = roleDb.getRoleSevenTasks().get(detail.id);
			if (dbInfo == null || dbInfo.getRecDate() == null)
				return 0;
			if (!DateUtil.isSameDay(dbInfo.getRecDate(), new Date()))
				return 0;
			if (getSevenTargetStatus(detail.id) != 2)
				return 0;
		}
		return 1;
	}

	/**
	 * 获取7日目标星级奖励领奖状态(完成状态 0:未完成 1:完成未领取 2:已领取 )
	 * 
	 * @param starNum
	 * @return
	 */
	private int getStarNumStatus(int starNum) {
		StarAwardTemplT t = XsgTaskManager.getInstance().starAwardT.get(starNum);
		if (t == null)
			return 0;
		RoleSevenTask roleSevenTask = roleDb.getRoleSevenTask();
		if (null != roleSevenTask) {
			if (!TextUtil.isBlank(roleSevenTask.getStarAwardRecs())) {
				if (roleSevenTask.getStarAwardRecs().indexOf(starNum + ",") != -1)
					return 2;
			}
			if (roleSevenTask.getStarCount() >= starNum)
				return 1;
		}
		return 0;
	}

	/**
	 * 增加总星数量
	 * 
	 * @param num
	 */
	private void addCountStar(int num) {
		if (num <= 0)
			return;
		RoleSevenTask roleSevenTask = roleDb.getRoleSevenTask();
		// 第一次获得星的时候保存加入时间 作为天数的判断
		if (null == roleSevenTask) {
			roleSevenTask = new RoleSevenTask(GlobalDataManager.getInstance().generatePrimaryKey(), roleDb, num, "",
					"", new Date());
			roleDb.setRoleSevenTask(roleSevenTask);
		} else {
			roleSevenTask.setStarCount(roleSevenTask.getStarCount() + num);
		}
	}

	/**
	 * 七日任务第一天的开始记录
	 */
	public void addSevenTask() {
		if (XsgGameParamManager.getInstance().getSevenTaskOpenStatus() == 0)
			return;
		RoleSevenTask roleSevenTask = roleDb.getRoleSevenTask();
		// 第一次获得星的时候保存加入时间 作为天数的判断
		if (null == roleSevenTask) {
			roleSevenTask = new RoleSevenTask(GlobalDataManager.getInstance().generatePrimaryKey(), roleDb, 0, "", "",
					new Date());
			roleDb.setRoleSevenTask(roleSevenTask);
		}
		if (!TextUtil.isBlank(roleRt.getFactionControler().getFactionId()))
			add2EveryDaySevenTask(SevenTargetTemplt.JionGuid.name(), 1);
		int friendsCount = roleRt.getSnsController().getFriends().size();
		if (friendsCount > 0)
			updateSevenTask(SevenTargetTemplt.Friends.name(), "");

	}

	private void add2EveryDaySevenTask(String type, int value) {
		Map<Integer, RoleSevenProgress> roleSevenTasks = roleDb.getRoleSevenTasks();
		Map<Integer, SevenTaskTemplT> sevenTaskDetailT = XsgTaskManager.getInstance().sevenTaskDetailT.get(type);
		for (SevenTaskTemplT t : sevenTaskDetailT.values()) {
			RoleSevenProgress info = roleSevenTasks.get(t.id);
			if (null == info) {
				info = new RoleSevenProgress(GlobalDataManager.getInstance().generatePrimaryKey(), roleDb, t.id, value
						+ "", null);
				roleDb.getRoleSevenTasks().put(t.id, info);
			}
		}
	}

	/**
	 * 增加数值
	 * 
	 * @param t
	 * @param num
	 * @param isSpcial
	 *            特殊数值 需要取最大值存的
	 */
	private void arrive2ConfigNum(SevenTaskTemplT t, int num, boolean isSpcial) {
		RoleSevenProgress dbInfo = roleDb.getRoleSevenTasks().get(t.id);
		if (null == dbInfo) {
			dbInfo = new RoleSevenProgress(GlobalDataManager.getInstance().generatePrimaryKey(), roleDb, t.id,
					num + "", null);
			roleDb.getRoleSevenTasks().put(t.id, dbInfo);
		} else {
			// 领取过的 不处理
			if (dbInfo.getRecDate() != null)
				return;
			if (TextUtil.isBlank(dbInfo.getProgress())) {
				dbInfo.setProgress(num + "");
			} else {
				if (isSpcial) {
					if (t.target.equals(SevenTargetTemplt.PVPRank.name())
							|| t.target.equals(SevenTargetTemplt.CraftRank.name())) {
						if (Integer.valueOf(dbInfo.getProgress()) <= num)
							return;
					} else if (Integer.valueOf(dbInfo.getProgress()) >= num) {
						return;
					}
					dbInfo.setProgress(num + "");
				} else {
					// 做累计叠加
					dbInfo.setProgress(String.valueOf(Integer.valueOf(dbInfo.getProgress()) + num));
				}
			}
		}
	}

	/**
	 * 一些副本信息的处理
	 * 
	 * @param t
	 * @param para
	 */
	private void compareWithString(SevenTaskTemplT t, String para) {
		String dId = t.demand.split(",")[0];
		if (dId.equals(para))
			arrive2ConfigNum(t, 1, false);
	}

	/**
	 * 领取任务奖励
	 */
	@Override
	public TaskView finishTask(int taskId, int type) throws NoteException, NotEnoughMoneyException {

		TaskTemplT taskTempl = XsgTaskManager.getInstance().taskTemplMap.get(taskId);

		// 检查领取任务的数据
		Map<Integer, RoleTask> roleTakMap = this.roleDb.getRoleTask();
		RoleTask roleTask = roleTakMap.get(taskId);
		this.checkFinishTask(roleTask, taskTempl, type);

		if (taskTempl.type == TaskType.redPacket.ordinal()) {// 红包任务
			if (roleTask.getNum() <= 0) {
				throw new NoteException(Messages.getString("TaskControler.1")); //$NON-NLS-1$
			}
			roleTask.setNum(roleTask.getNum() - 1);
			if (roleTask.getNum() == 0) {
				// roleTask.setState(XsgTaskManager.taskStat.finish.ordinal());
				roleTakMap.remove(roleTask.getTaskId());
			}
		} else {
			// 保存 完成任务 数据
			roleTask.setState(XsgTaskManager.taskStat.finish.ordinal());
			roleTask.setTaskDate(new Date());
			// 完成一次手不释卷任务
			if (taskTempl.type == TaskType.daily.value() || taskTempl.type == TaskType.dailyTime.value()
					|| taskTempl.type == TaskType.vipTask.value() || taskTempl.type == TaskType.vipMonth.value()) {
				onTaskEvent(XsgTaskManager.OVER, 0, 1);
			}
		}

		// 得到任务奖励
		this.taskAward(taskTempl);

		// 除了日常任务，完成当前任务，自动添加下一个可以完成的任务
		TaskType taskType = TaskType.valueOf(type);
		TaskTemplT nextTaskT = null;
		switch (taskType) {
		case pass:
			nextTaskT = XsgTaskManager.getInstance().getTaskThroughMap_t1(taskId);
			break;
		case success:
			nextTaskT = XsgTaskManager.getInstance().getTaskThroughMap_t3(taskId);
			break;
		case guideTask:
			nextTaskT = XsgTaskManager.getInstance().getTaskThroughMap_t6(taskId);
			break;
		default:
			break;
		}

		// 保存数据库的数据
		if (nextTaskT != null) {
			RoleTask newTask = this.addRoleTask(nextTaskT);
			this.autoFininshTask(nextTaskT, newTask);

			roleTakMap.put(nextTaskT.id, newTask);
		}
		// 增加活跃点
		int actBefore = 0;
		if (roleDb.getRoleTaskActive() != null) {
			actBefore = roleDb.getRoleTaskActive().getPoint();
		}
		addActPoint(taskTempl.actPoint);
		int actAfter = 0;
		if (roleDb.getRoleTaskActive() != null) {
			actAfter = roleDb.getRoleTaskActive().getPoint();
		}
		getTaskEvent.onGetTask(taskId, actBefore, actAfter, taskTempl.actPoint);
		return this.selectTask();
	}

	/**
	 * 自动完成下一个任务
	 */
	private void autoFininshTask(TaskTemplT taskTempl, RoleTask roleTask) {

		// 主线任务
		if (taskTempl.target.equals(XsgTaskManager.FORT_PASS)) {
			// 通关ID 和 星级
			int deNum = Integer.valueOf(taskTempl.demand.split(",")[0]); //$NON-NLS-1$
			if (this.roleRt.getCopyControler().getCopyStar(deNum) >= NumberUtil
					.parseInt(taskTempl.demand.split(",")[1])) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			}

		} else if (taskTempl.target.equals(XsgTaskManager.KING_LV)) {
			if (this.roleRt.getLevel() >= Integer.valueOf(taskTempl.demand)) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			}

		} else if (taskTempl.target.equals(XsgTaskManager.HERO_NUM)) {
			if (this.roleRt.getHeroControler().getHeroNum() >= Integer.valueOf(taskTempl.demand)) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			} else {
				roleTask.setNum(this.roleRt.getHeroControler().getHeroNum());
			}

		} else if (taskTempl.target.equals(XsgTaskManager.GOLD_NUM)) {
			if (this.roleRt.getJinbiHistory() >= Long.valueOf(taskTempl.demand)) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			} else {
				roleTask.setNum((int) this.roleRt.getJinbiHistory());
			}

		} else if (taskTempl.target.equals(XsgTaskManager.HERO_STAR)) {
			if (this.roleRt.getHeroControler().getHeroNumWhenStarGreaterThan(Integer.valueOf(taskTempl.demand)) > 0) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			}
		} else if (taskTempl.target.equals(XsgTaskManager.FORT_PASS_G)) {
			// 通关ID 和 星级
			int deNum = Integer.valueOf(taskTempl.demand.split(",")[0]); //$NON-NLS-1$
			if (this.roleRt.getCopyControler().getCopyStar(deNum) > 0) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			}
		} else if (taskTempl.target.equals(XsgTaskManager.SUMMON_HERO)) {
			if (this.roleRt.getHeroControler().getHero(Integer.valueOf(taskTempl.demand)) != null) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			}
		} else if (taskTempl.target.equals(XsgTaskManager.GET_REWARD)) {
			if (this.roleRt.getActivityControler().isGetGift(Integer.valueOf(taskTempl.demand))) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			}
		} else if (taskTempl.target.equals(XsgTaskManager.FriendPoint)) {
			if (this.roleRt.getSnsController().getMaxFriendPoint() >= Integer.valueOf(taskTempl.demand)) {
				roleTask.setState(XsgTaskManager.taskStat.obtain.ordinal());
			} else {
				roleTask.setNum(this.roleRt.getSnsController().getMaxFriendPoint());
			}
		}
	}

	/**
	 * 检查完成任务情况
	 * 
	 * @param roleTask
	 * @param taskTempl
	 * @param type
	 * @throws NoteException
	 */
	private void checkFinishTask(RoleTask roleTask, TaskTemplT taskTempl, int type) throws NoteException {
		if (roleTask == null) {
			throw new NoteException(Messages.getString("TaskControler.4")); //$NON-NLS-1$
		}

		if (roleTask.getState() != 1) {
			throw new NoteException(Messages.getString("TaskControler.5")); //$NON-NLS-1$
		}

		// 日常任务类型，超出的一定时间范围
		if (type == TaskType.dailyTime.ordinal() && !TextUtil.isBlank(taskTempl.starTime)
				&& !TextUtil.isBlank(taskTempl.endTime)) {
			if (!DateUtil.isBetween(DateUtil.joinTime(taskTempl.starTime), DateUtil.joinTime(taskTempl.endTime))) {
				throw new NoteException(Messages.getString("TaskControler.6")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * 任务奖励
	 * 
	 * @param taskTempl
	 * @throws NotEnoughMoneyException
	 */
	private void taskAward(TaskTemplT taskTempl) throws NotEnoughMoneyException {

		this.roleRt.winJinbi(taskTempl.gold);
		this.roleRt.winPrestige(taskTempl.exp);

		if (!TextUtil.isBlank(taskTempl.item)) {

			// VIP任务，特殊处理， 得到扫荡令
			if (taskTempl.target.equals(XsgTaskManager.VIP)) {
				if (this.roleRt.getVipLevel() > 0) {
					int med2Num = XsgVipManager.getInstance().findVipT(this.roleRt.getVipLevel()).med2;
					this.roleRt.getRewardControler().acceptReward(taskTempl.item, med2Num);
				}
			} else {
				this.roleRt.getRewardControler().acceptReward(taskTempl.item, taskTempl.itemNum);
			}
		}
	}

	/**
	 * 初始化任务 和 已经存在的任务中，没有初始化任务的，二次添加
	 * 
	 * @param roleTaskMap
	 * @return
	 */
	private List<TaskItem> initTask(Map<Integer, RoleTask> roleTaskMap) {
		Map<Integer, RoleTask> newRoleTaskMap = new HashMap<Integer, RoleTask>(); // 集合无法在迭代中添加元素
		List<TaskItem> restaskViewList = new ArrayList<TaskItem>();

		// 前置任务为空的，默认为初始化任务
		for (TaskTemplT templ : XsgTaskManager.getInstance().taskTemplInitList) {
			// 1.第一次 初始化 添加任务
			// 2.已经存在的任务中，没有初始化任务的，二次添加
			if (roleTaskMap.get(templ.id) == null) {
				// 任务的日期必须在有效范围中
				if (DateUtil.isBetween(DateUtil.parseDate(templ.starDate), DateUtil.parseDate(templ.endDate))) {
					// 日常限时任务, 错过完成时间的不显示
					if (templ.type == TaskType.dailyTime.ordinal()
							&& DateUtil.isPass(DateUtil.joinTime(templ.endTime), DateUtil.joinTime(templ.starTime))) {
						continue;
					}
					RoleTask roleTask = this.addRoleTask(templ);
					this.autoFininshTask(templ, roleTask);

					// 保存数据库的数据 和 返回客户端的数据
					roleTaskMap.put(templ.id, roleTask);
					// 达到指定等级才显示
					if (this.roleRt.getLevel() >= templ.minLevel) {
						restaskViewList.add(this.setTaskView(roleTask, templ.type));
					}
				}
			}
		}

		// 2016-03-30 补充xls脚本新配的任务,任务列表断掉的问题
		// 已完成当前任务，以当前任务为前置任务的任务不存在，则绑定到roleTask里
		Map<Integer, TaskTemplT> taskTemplMap = XsgTaskManager.getInstance().taskTemplMap;
		for (RoleTask task : roleTaskMap.values()) {
			newRoleTaskMap.put(task.getTaskId(), task);

			TaskTemplT taskTempl = taskTemplMap.get(Integer.valueOf(task.getTaskId()));
			// 已完成当前任务，并且下个任务没有出现
			if (task.getState() >= XsgTaskManager.taskStat.finish.ordinal()) {
				// 除了日常任务，完成当前任务，自动添加下一个可以完成的任务
				TaskType taskType = TaskType.valueOf(taskTempl.type);
				TaskTemplT nextTaskT = null;
				switch (taskType) {
				case pass:
					nextTaskT = XsgTaskManager.getInstance().getTaskThroughMap_t1(task.getTaskId());
					break;
				case success:
					nextTaskT = XsgTaskManager.getInstance().getTaskThroughMap_t3(task.getTaskId());
					break;
				case guideTask:
					nextTaskT = XsgTaskManager.getInstance().getTaskThroughMap_t6(task.getTaskId());
					break;
				default:
					break;
				}

				// 保存数据库的数据
				if (nextTaskT != null && roleTaskMap.get(Integer.valueOf(nextTaskT.id)) == null) {
					RoleTask newTask = this.addRoleTask(nextTaskT);
					this.autoFininshTask(nextTaskT, newTask);

					newRoleTaskMap.put(nextTaskT.id, newTask);

					// 增加活跃点
					int actBefore = 0;
					if (roleDb.getRoleTaskActive() != null) {
						actBefore = roleDb.getRoleTaskActive().getPoint();
					}
					addActPoint(taskTempl.actPoint);
					int actAfter = 0;
					if (roleDb.getRoleTaskActive() != null) {
						actAfter = roleDb.getRoleTaskActive().getPoint();
					}
					getTaskEvent.onGetTask(taskTempl.id, actBefore, actAfter, taskTempl.actPoint);
				}
			}
		}

		roleTaskMap = newRoleTaskMap;
		this.roleDb.setRoleTask(newRoleTaskMap);
		return restaskViewList;
	}

	/**
	 * 任务显示 设置
	 * 
	 * @param roleTask
	 * @param templ
	 * @param taskTime
	 * @return
	 */
	private TaskItem setTaskView(RoleTask roleTask, int taskType) {
		TaskItem view = new TaskItem();

		view.taskId = roleTask.getTaskId();
		view.type = taskType;
		view.taskNum = roleTask.getNum();
		view.taskState = roleTask.getState();

		long taskTime = System.currentTimeMillis() - roleTask.getTaskDate().getTime();
		view.taskTime = taskTime < 0 ? 0 : taskTime / 1000;

		return view;
	}

	// 新增角色的任务
	private RoleTask addRoleTask(TaskTemplT templ) {
		return new RoleTask(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb, templ.id, 0, 0,
				new Date());
	}

	/**
	 * 查询日常任务 任务状态 ,0:未完成，1：未领取，2：过时未完成，3：完成
	 */
	private TaskItem selectDailyTask(RoleTask task, TaskTemplT taskTempl, Map<Integer, RoleTask> roleTakMap) {

		TaskItem taskView = null;

		// 和当前日期不同，重置日常任务
		if (!DateUtil.isSameDay(System.currentTimeMillis(), task.getTaskDate().getTime())) {
			task.setNum(0);
			task.setState(XsgTaskManager.taskStat.unFinish.ordinal());
			task.setTaskDate(new Date());
			if (taskTempl.type == TaskType.redPacket.value()) {
				task.setState(XsgTaskManager.taskStat.finish.ordinal());
			}
		}

		// 已经完成不在显示
		if (task.getState() != 3) {
			taskView = this.setTaskView(task, taskTempl.type);

			// 是否有未完成的和未过时的任务
			if (task.getState() < XsgTaskManager.taskStat.outTime.ordinal()) {
				// 有时间限制的任务
				if (!TextUtil.isBlank(taskTempl.starTime) && !TextUtil.isBlank(taskTempl.endTime)) {
					// 时间未到，状态显示，0:未完成
					// 时间在范围内，状态显示，1：未领取
					// 超出范围未领取，状态显示，2：过时未完成
					if (DateUtil.compareTime(new Date(), DateUtil.joinTime(taskTempl.starTime)) < 0) {
						taskView.taskState = XsgTaskManager.taskStat.unFinish.ordinal();
						taskView.taskTime = DateUtil.compareTime(new Date(), DateUtil.joinTime(taskTempl.starTime));
					} else if (DateUtil.compareTime(DateUtil.joinTime(taskTempl.endTime), new Date()) <= 0) {
						taskView.taskState = XsgTaskManager.taskStat.outTime.ordinal();
					} else {
						taskView.taskState = XsgTaskManager.taskStat.obtain.ordinal();
					}

					task.setState(taskView.taskState);
				}

				// VIP任务, VIP等级不足，不显示
				if (taskTempl.type == TaskType.vipTask.ordinal()) {
					// VIP等级到达一定等级，领取奖励
					if (taskTempl.target.equals(XsgTaskManager.VIP)) {
						if (this.roleRt.getVipLevel() >= Integer.valueOf(taskTempl.demand)) {
							taskView.taskState = XsgTaskManager.taskStat.obtain.ordinal();
							task.setState(taskView.taskState);
						} else {
							task = null;
						}
					}
					// VIP月卡
				} else if (taskTempl.type == TaskType.vipMonth.ordinal()) {
					if (taskTempl.target.equals(XsgTaskManager.VIPM)) {
						if (this.roleRt.getVipController().hasMonthCard()) {
							taskView.taskState = XsgTaskManager.taskStat.obtain.ordinal();
						}
					}
					task.setState(taskView.taskState);
				}
			}
		}

		// 保存数据
		if (task != null) {
			roleTakMap.put(task.getTaskId(), task);
		}

		return taskView;
	}

	// 和当前日期不同，重置日常任务
	private void dailyTaskReset(RoleTask task) {
		if (task != null
				&& !DateUtil
						.isSameDay(DateUtil.getFirstSecondOfToday().getTimeInMillis(), task.getTaskDate().getTime())) {
			task.setState(XsgTaskManager.taskStat.unFinish.ordinal());
			task.setNum(0);
			task.setTaskDate(new Date());
		}
	}

	/**
	 * 地图过关完成任务事件 任务状态 ,0:未完成，1：未领取，2：过时未完成，3：完成
	 */
	@Override
	public void onCopyCompleted(SmallCopyT templete, int star, boolean firstPass, int fightPower, int junling) {
		if (star > 0) {
			Map<Integer, RoleTask> roleTakMap = this.roleDb.getRoleTask();

			Map<String, TaskTemplT> passMap = XsgTaskManager.getInstance().targetPassMap.get(XsgTaskManager.FORT_PASS);
			Map<String, TaskTemplT> passGMap = XsgTaskManager.getInstance().targetPassMap
					.get(XsgTaskManager.FORT_PASS_G);

			// 普通过关
			TaskTemplT templ = passMap.get(String.valueOf(templete.id));
			if (templ != null) {
				RoleTask task = roleTakMap.get(templ.id);
				if (task != null && task.getState() == 0 && star >= NumberUtil.parseInt(templ.demand.split(",")[1])) {
					task.setState(XsgTaskManager.taskStat.obtain.ordinal());
					roleTakMap.put(templ.id, task);
				}
			}

			// 引导 使用 普通过关
			templ = passGMap.get(String.valueOf(templete.id));
			if (templ != null) {
				RoleTask task = roleTakMap.get(templ.id);
				if (task != null && task.getState() == 0) {
					task.setState(XsgTaskManager.taskStat.obtain.ordinal());
					roleTakMap.put(templ.id, task);
				}
			}

			// 通关某难度任意副本X次
			List<TaskTemplT> passNumList = XsgTaskManager.getInstance().targetMap.get(XsgTaskManager.FORT_PASS_NUM);

			if (passNumList != null) {
				for (TaskTemplT taskTemplT : passNumList) {
					RoleTask task = roleTakMap.get(taskTemplT.id);

					// 和当前日期不同，重置日常任务
					this.dailyTaskReset(task);

					if (task != null && task.getState() == 0) {
						// 通关次数 和 星级
						int deNum = Integer.valueOf(taskTemplT.demand.split(",")[0]); //$NON-NLS-1$
						int deStar = Integer.valueOf(taskTemplT.demand.split(",")[1]); //$NON-NLS-1$

						if (templete.getParent().difficulty == deStar || deStar == 0) {
							task.setNum(task.getNum() + 1);
							if (task.getNum() >= deNum) {
								task.setState(XsgTaskManager.taskStat.obtain.ordinal());

								this.notifyRedPoint();
							}

							roleTakMap.put(taskTemplT.id, task);
						}
					}
				}
			}
			// 首次通关大神难度
			RedPacketT redPacketT = XsgTaskManager.getInstance().tongGuanT.get(templete.id);
			if (firstPass && redPacketT != null && redPacketT.isOpen == XsgTaskManager.OPEN) {
				RedPacket redPacket = new RedPacket(GlobalDataManager.getInstance().generatePrimaryKey(),
						redPacketT.taskId, this.roleRt.getRoleId(), new Date());
				XsgTaskManager.getInstance().redPacketSystem.add(redPacket);
				// 异步保存
				saveRedPacket(redPacket);
				// 公告
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.pass);
				if (adTList != null && adTList.size() > 0) {
					ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
					if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
						String content = XsgChatManager.getInstance().replaceRoleContent(chatAdT.content, this.roleRt);
						content = content.replaceAll("~chapter_name~", //$NON-NLS-1$
								templete.getParent().name);
						XsgChatManager.getInstance().sendAnnouncement(content);
					}
				}
			}
		}
		updateSevenTask(SevenTargetTemplt.fortPass.name(), templete.id + "");
	}

	/**
	 * 是否是 日常任务 完成条件，次数 还是 指定条件
	 * 
	 * @param target
	 *            任务目标
	 * @param flag
	 *            完成任务的类型，0：次数，1：指定条件
	 * @param value
	 *            0：次数，1：指定条件的值
	 */
	private void onTaskEvent(String target, int flag, long value) {
		Map<Integer, RoleTask> roleTakMap = this.roleDb.getRoleTask();
		List<TaskTemplT> targetList = XsgTaskManager.getInstance().targetMap.get(target);

		if (targetList != null) {
			for (TaskTemplT templ : targetList) {
				RoleTask task = roleTakMap.get(templ.id);

				if (task != null) {
					// 日常任务时，和当前日期不同，重置
					if (templ.type == TaskType.daily.ordinal()) {
						this.dailyTaskReset(task);
					}

					// 任务类型未完成
					if (task.getState() == 0) {
						// 完成条件，0：次数，1：指定条件
						if (flag == 0) {
							task.setNum(task.getNum() + (int) value);
							if (task.getNum() >= Integer.valueOf(templ.demand)) {
								task.setState(XsgTaskManager.taskStat.obtain.ordinal());
								this.notifyRedPoint();
							}
						} else {
							if (value >= Integer.valueOf(templ.demand)) {
								task.setState(XsgTaskManager.taskStat.obtain.ordinal());
								this.notifyRedPoint();
							}
						}
						roleTakMap.put(templ.id, task);
					}

					// 触发红点
					if (task.getState() == XsgTaskManager.taskStat.obtain.ordinal()) {
						roleRt.getNotifyControler().onMajorUIRedPointChange(
								new MajorUIRedPointNote(MajorMenu.TaskMenu, false));
					}
				}
			}
		}
	}

	// 强化装备
	@Override
	public void onEquipStrengthen(int auto, EquipItem equip, int beforeLevel, int afterLevel) {
		this.onTaskEvent(XsgTaskManager.STRENGTHEN, 0, 1);
		updateSevenTask(SevenTargetTemplt.EquipLv.name(), "");
	}

	// 技能升级
	@Override
	public void onHeroSkillUp(IHero hero, String name, int oldLevel, int newLevel) {
		this.onTaskEvent(XsgTaskManager.SKILL_UP, 0, newLevel - oldLevel);
		updateSevenTask(SevenTargetTemplt.SkillLv.name(), "");
		// 引导任务 技能升级
		// this.onTaskEvent(XsgTaskManager.SKILL_UP_G, 1, 1);
	}

	// 角色等级
	@Override
	public void onRoleLevelup() {
		this.onTaskEvent(XsgTaskManager.KING_LV, 1, this.roleRt.getLevel());
	}

	// 游戏币数量变化
	@Override
	public void onJinbiChange(long change) throws Exception {
		this.onTaskEvent(XsgTaskManager.GOLD_NUM, 1, this.roleRt.getJinbiHistory());
	}

	// 武将的星级
	@Override
	public void onHeroStarUp(IHero hero, int beforeStar) {
		this.onTaskEvent(XsgTaskManager.HERO_STAR, 1, hero.getStar());
		updateSevenTask(SevenTargetTemplt.HeroStar.name(), "");
	}

	// 千杯不醉 抽卡
	@Override
	public void onBuyWineByJinbi(BuyHeroResult result, boolean free) {
		this.onTaskEvent(XsgTaskManager.DRAW_CARD, 0, 1);
		this.onTaskEvent(XsgTaskManager.RecruitBuyOne, 0, 1);
	}

	@Override
	public void onBuy10WineByJinbi(List<BuyHeroResult> list) {
		this.onTaskEvent(XsgTaskManager.DRAW_CARD, 0, 10);
		this.onTaskEvent(XsgTaskManager.RecruitBuyOne, 0, 10);
	}

	@Override
	public void onBuyWineByYuanbao(BuyHeroResult result, boolean free) {
		this.onTaskEvent(XsgTaskManager.DRAW_CARD, 0, 1);
		this.onTaskEvent(XsgTaskManager.RecruitBuyOne, 0, 1);
		List<BuyHeroResult> list = new ArrayList<BuyHeroResult>();
		list.add(result);
		firstHeroTask(list);
	}

	@Override
	public void onBuy10WineByYuanbao(List<BuyHeroResult> list) {
		this.onTaskEvent(XsgTaskManager.DRAW_CARD, 0, 10);
		this.onTaskEvent(XsgTaskManager.RecruitBuyOne, 0, 10);
		firstHeroTask(list);
	}

	@Override
	public void onBuyLimitHero(List<BuyHeroResult> results) {
		this.onTaskEvent(XsgTaskManager.RecruitBuyOne, 0, 10);
		firstHeroTask(results);
	}

	/**
	 * 第一个英雄获得的任务
	 */
	private void firstHeroTask(List<BuyHeroResult> results) {
		Map<Integer, RoleTask> roleTakMap = this.roleDb.getRoleTask();
		List<TaskTemplT> passNumList = XsgTaskManager.getInstance().targetMap.get(XsgTaskManager.GET_HERO_FIRST);
		if (passNumList != null) {
			for (TaskTemplT taskTemplT : passNumList) {
				if (roleRt.getLevel() < taskTemplT.minLevel)
					continue;
				RoleTask task = roleTakMap.get(taskTemplT.id);
				if (task != null && task.getState() == 0) {
					// 英雄ID
					int heroId = Integer.valueOf(taskTemplT.demand);
					boolean isUpdate = false;
					for (BuyHeroResult result : results) {
						if (result.heroTemplateId == heroId) {
							isUpdate = true;
							break;
						}
					}
					if (isUpdate) {
						task.setNum(task.getNum() + 1);
						if (task.getNum() >= 1) {
							task.setState(XsgTaskManager.taskStat.obtain.ordinal());
							this.notifyRedPoint();
						}
						roleTakMap.put(taskTemplT.id, task);
					}
				}
			}
		}
	}

	// 引导任务 召唤李典
	@Override
	public void onHeroJoin(IHero hero, HeroSource source) {
		// 武将数量 到达一定的武将领取奖励
		this.onTaskEvent(XsgTaskManager.HERO_NUM, 1, this.roleRt.getHeroControler().getHeroNum());

		if (source == HeroSource.Summon) {
			List<TaskTemplT> targetList = XsgTaskManager.getInstance().targetMap.get(XsgTaskManager.SUMMON_HERO);

			Map<Integer, RoleTask> roleTakMap = this.roleDb.getRoleTask();
			if (targetList != null) {
				for (TaskTemplT templ : targetList) {
					RoleTask task = roleTakMap.get(templ.id);

					if (task != null) {
						if (hero.getTemplateId() == Integer.valueOf(templ.demand)) {
							task.setState(XsgTaskManager.taskStat.obtain.ordinal());
							roleTakMap.put(templ.id, task);
							this.notifyRedPoint();
						}
					}
				}
			}
		}
	}

	// 引导任务 今天开始做VIP
	@Override
	public void onGetGift(UpGiftT upGiftT) throws Exception {
		this.onTaskEvent(XsgTaskManager.GET_REWARD, 1, upGiftT.id);
	}

	// 引导任务 升级捷径 给潘凤吃小经验丹
	@Override
	public void onItemUseForHero(String itemTemplate, int count, IHero hero) {

		Map<Integer, RoleTask> roleTakMap = this.roleDb.getRoleTask();
		List<TaskTemplT> targetList = XsgTaskManager.getInstance().targetMap.get(XsgTaskManager.USE_EXPG);

		if (targetList != null) {
			for (TaskTemplT templ : targetList) {
				RoleTask task = roleTakMap.get(templ.id);

				if (task != null) {
					// 格式化任务完成条件
					String[] demandArr = templ.demand.split(","); //$NON-NLS-1$
					int heroId = Integer.valueOf(demandArr[0]);
					String tempId = demandArr[1];
					int demandCount = Integer.valueOf(demandArr[2]);

					// 判断完成次数
					if (itemTemplate.equals(tempId) && hero.getTemplateId() == heroId) {
						task.setNum(task.getNum() + count);
						if (task.getNum() >= demandCount) {
							task.setState(XsgTaskManager.taskStat.obtain.ordinal());
						}
						roleTakMap.put(templ.id, task);
					}
				}
			}
		}
	}

	// 引导任务 给任意武将进阶
	@Override
	public void onHeroQualityUp(IHero hero, int beforeQualityLeve) {
		updateSevenTask(SevenTargetTemplt.BreakUp.name(), "");
		this.onTaskEvent(XsgTaskManager.ADVANCED_HERO, 1, 1);
		int qualityLevel = hero.getQualityLevel();
		if (qualityLevel <= roleDb.getRoleTaskActive().getMaxQualityLevel()) {
			return;
		}
		roleDb.getRoleTaskActive().setMaxQualityLevel(qualityLevel);
		if (heroQualityLevelCount(qualityLevel) > 1) {
			return;
		}
		Integer taskId = null;
		// 首次进阶武将到指定品质
		RedPacketT redPacketT = XsgTaskManager.getInstance().heroAdvanceT.get(qualityLevel);
		if (redPacketT != null && redPacketT.isOpen == XsgTaskManager.OPEN) {
			taskId = redPacketT.taskId;
		}
		if (taskId != null) {
			RedPacket redPacket = new RedPacket(GlobalDataManager.getInstance().generatePrimaryKey(), taskId,
					this.roleRt.getRoleId(), new Date());
			XsgTaskManager.getInstance().redPacketSystem.add(redPacket);
			// 异步保存
			saveRedPacket(redPacket);
			// 公告
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.advance);
			if (adTList != null && adTList.size() > 0) {
				ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
					String content = XsgChatManager.getInstance().replaceRoleContent(chatAdT.content, this.roleRt);
					content = XsgChatManager.getInstance().replaceHeroContent(content, hero);
					XsgChatManager.getInstance().sendAnnouncement(content);
				}
			}
		}
	}

	/**
	 * 获取指定品质武将的个数(验证玩家首次进阶武将到指定品质)
	 * 
	 * @param qualityLevel
	 * @return
	 */
	private int heroQualityLevelCount(int qualityLevel) {
		HeroView[] hvs = this.roleRt.getHeroControler().getHeroViewList();
		int count = 0;
		for (HeroView h : hvs) {
			if (h.qualityLevel >= qualityLevel) {
				count++;
			}
		}
		return count;
	}

	// 引导任务 碎片 合成后炫耀
	@Override
	public void onStrut(String templId) {
		this.onTaskEvent(XsgTaskManager.COMPOSE_SHOW, 1, 1);
	}

	// 引导任务 打一场竞技场比赛
	@Override
	public void onArenaFight(int resFlag, int roleRank, int rivalRank, int sneerId, String reward) {
		this.onTaskEvent(XsgTaskManager.ARENA, 1, 1);
		// 竞技场作战3次
		this.onTaskEvent(XsgTaskManager.PVPS, 0, 1);
		updateSevenTask(SevenTargetTemplt.PVPS.name(), 1 + "");
		updateSevenTask(SevenTargetTemplt.PVPRank.name(), roleRank + "");
	}

	// 时空战役
	@Override
	public void onPassBattle(int id, boolean isClear, int junling) {
		this.onTaskEvent(XsgTaskManager.ST, 0, 1);

		// 指定关卡时空战役
		// Map<Integer, RoleTask> roleTakMap = this.roleDb.getRoleTask();
		// List<TaskTemplT> targetList = XsgTaskManager.getInstance().targetMap
		// .get(XsgTaskManager.ST);
		// if (targetList != null) {
		// for (TaskTemplT templ : targetList) {
		// RoleTask task = roleTakMap.get(templ.id);
		// if (task != null) {
		// this.dailyTaskReset(task);
		// if (task.getState() == 0) {
		// String[] numAndId = templ.demand.split(",");
		// if (numAndId.length <= 1) {
		// continue;
		// }
		// // 通关次数 和 关卡ID
		// int deNum = Integer.valueOf(numAndId[0]);
		// int deId = Integer.valueOf(numAndId[1]);
		// if (id == deId) {
		// task.setNum(task.getNum() + 1);
		// if (task.getNum() >= deNum) {
		// task.setState(XsgTaskManager.taskStat.obtain
		// .ordinal());
		// roleRt.refreshRedPoint();
		// }
		// roleTakMap.put(templ.id, task);
		// }
		// }
		// }
		// }
		// }
		updateSevenTask(SevenTargetTemplt.ST.name(), id + "");
	}

	// 名将探访
	@Override
	public void onTraderCalled(TraderType traderType, CurrencyType currencyType) {
		if (traderType == TraderType.Hero) {
			this.onTaskEvent(XsgTaskManager.VISIT, 0, 1);
			updateSevenTask(SevenTargetTemplt.Visit.name(), 1 + "");
		}
	}

	// 装备重铸事件
	@Override
	public void onEquipRebuild(EquipItem equip) {
		this.onTaskEvent(XsgTaskManager.RECASTING, 0, 1);
	}

	/**
	 * 客户端 是否红点显示
	 */
	@Override
	public MajorUIRedPointNote getRedPointNote() {
		receiveRedPacket();
		boolean note = false;
		try {
			TaskView view = this.selectTask();
			TaskItem[] taskItems = view.taskItems;
			for (int i = 0; i < taskItems.length; i++) {
				if (taskItems[i].taskState == 1) {
					note = true;
					break;
				}
			}
			// 宝箱的红点
			ActPointAward[] actPointAwards = view.actPointAwards;
			for (int i = 0; i < actPointAwards.length; i++) {
				if (actPointAwards[i].state == 1) {
					note = true;
					break;
				}
			}
			if (!note) {
				SevenTargetView sevenTarget = view.sevenTarget;
				note = isCanRecSevenTask(sevenTarget);
			}
		} catch (NoteException e) {
			e.printStackTrace();
		}
		return note ? new MajorUIRedPointNote(MajorMenu.TaskMenu, false) : null;
	}

	/**
	 * 七日相关 是否有奖励可领
	 * 
	 * @return
	 */
	private boolean isCanRecSevenTask(SevenTargetView tView) {
		try {
			if (XsgGameParamManager.getInstance().getSevenTaskOpenStatus() == 0) {
				return false;
			}
			if (null != tView) {
				SevenTarget[] sevenTargets = tView.sevenTargets;
				for (int i = 0; i < sevenTargets.length; i++) {
					int[] states = sevenTargets[i].states;
					for (int j = 0; j < states.length; j++) {
						if (states[j] == 1) {
							return true;
						}
					}
				}
				SevenTargetStarReward[] sevenTargetStarRewards = tView.sevenTargetStarRewards;
				for (int i = 0; i < sevenTargetStarRewards.length; i++) {
					if (sevenTargetStarRewards[i].state == 1) {
						return true;
					}
				}
				SevenThreeStarReward[] sevenThreeStarRewards = tView.sevenThreeStarRewards;
				for (int i = 0; i < sevenThreeStarRewards.length; i++) {
					if (sevenThreeStarRewards[i].states == 1) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 领取全局红包
	 */
	private void receiveRedPacket() {
		List<String> ids = TextUtil.stringToList(this.roleDb.getRoleRedPacket().getRedPacketIds());
		for (RedPacket rp : XsgTaskManager.getInstance().redPacketSystem) {
			// 不是自己触发的红包并且没有领取过
			if (!rp.getSenderId().equals(this.roleRt.getRoleId()) && !ids.contains(rp.getId())) {
				TaskTemplT templT = XsgTaskManager.getInstance().taskTemplMap.get(rp.getTaskId());
				// 领取等级不够
				if (this.roleRt.getLevel() < templT.minLevel) {
					continue;
				}
				RoleTask roleTask = this.roleDb.getRoleTask().get(rp.getTaskId());
				if (roleTask == null) {
					roleTask = new RoleTask(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb,
							rp.getTaskId(), 1, 1, new Date());
					this.roleDb.getRoleTask().put(roleTask.getTaskId(), roleTask);
				} else {
					dailyTaskReset(roleTask);
					roleTask.setNum(roleTask.getNum() + 1);
					roleTask.setState(1);
					roleTask.setTaskDate(new Date());
				}
				ids.add(rp.getId());
				this.roleDb.getRoleRedPacket().setRedPacketIds(TextUtil.join(ids, ",")); //$NON-NLS-1$
			}
		}
	}

	// vip升级
	@Override
	public void onVipLevelUp(int newLevel) {
		Integer taskId = null;
		RedPacketT redPacketT = XsgTaskManager.getInstance().vipUpT.get(newLevel);
		if (redPacketT != null && redPacketT.isOpen == XsgTaskManager.OPEN) {
			taskId = redPacketT.taskId;
		}
		if (taskId != null) {
			RedPacket redPacket = new RedPacket(GlobalDataManager.getInstance().generatePrimaryKey(), taskId,
					this.roleRt.getRoleId(), new Date());
			XsgTaskManager.getInstance().redPacketSystem.add(redPacket);
			// 异步保存
			saveRedPacket(redPacket);
			// 公告
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.vipUp);
			if (adTList != null && adTList.size() > 0) {
				ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
					String content = XsgChatManager.getInstance().replaceRoleContent(chatAdT.content, this.roleRt);
					XsgChatManager.getInstance().sendAnnouncement(content);
				}
			}
		}
	}

	/**
	 * 异步保存全局红包
	 * 
	 * @param redPacket
	 */
	public void saveRedPacket(final RedPacket redPacket) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO dao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.save(redPacket);
			}
		});
	}

	@Override
	public void onVipGiftBuy(int vipLevel, String reward, int num) {
		Integer taskId = null;
		RedPacketT redPacketT = XsgTaskManager.getInstance().vipGiftT.get(vipLevel);
		if (redPacketT != null && redPacketT.isOpen == XsgTaskManager.OPEN) {
			taskId = redPacketT.taskId;
		}
		if (taskId != null) {
			RedPacket redPacket = new RedPacket(GlobalDataManager.getInstance().generatePrimaryKey(), taskId,
					this.roleRt.getRoleId(), new Date());
			XsgTaskManager.getInstance().redPacketSystem.add(redPacket);
			// 异步保存
			saveRedPacket(redPacket);
			// 公告
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
					XsgChatManager.AdContentType.buyVipGift);
			if (adTList != null && adTList.size() > 0) {
				ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
					String content = chatAdT.content.replaceAll("~gift_lv~", String.valueOf(vipLevel)); //$NON-NLS-1$
					content = XsgChatManager.getInstance().replaceRoleContent(content, this.roleRt);
					XsgChatManager.getInstance().sendAnnouncement(content);
				}
			}
		}
	}

	@Override
	public void initRoleRedPacket() {
		RoleRedPacket roleRedPacket = this.roleDb.getRoleRedPacket();
		if (roleRedPacket == null) {
			roleRedPacket = new RoleRedPacket(GlobalDataManager.getInstance().generatePrimaryKey(),
					"", false, this.roleDb); //$NON-NLS-1$
			this.roleDb.setRoleRedPacket(roleRedPacket);
		} else {
			// 移除老数据
			List<String> ids = TextUtil.stringToList(roleRedPacket.getRedPacketIds());
			List<String> current = new ArrayList<String>();
			for (String id : ids) {
				for (RedPacket rp : XsgTaskManager.getInstance().redPacketSystem) {
					if (id.equals(rp.getId())) {
						current.add(id);
						break;
					}
				}
			}
			roleRedPacket.setRedPacketIds(TextUtil.join(current, ",")); //$NON-NLS-1$
		}
	}

	// 群雄争霸挑战事件
	@Override
	public void onFight(String rivalId, int resFlag, int fightStar) {
		this.onTaskEvent(XsgTaskManager.LADDER, 0, 1);
		updateSevenTask(SevenTargetTemplt.Craft.name(), 1 + "");
	}

	// 公会副本挑战事件
	@Override
	public void onFactionCopyEndChallenge(String roleId, String factionId, int copyId, int dropBlood) {
		this.onTaskEvent(XsgTaskManager.FACTION_COPY, 0, 1);
	}

	// 公会捐赠事件
	@Override
	public void onFactionDonation(String roleId, int num) {
		this.onTaskEvent(XsgTaskManager.FACTION_DONATION, 0, 1);
		updateSevenTask(SevenTargetTemplt.GuildMedal.name(), 1 + "");
	}

	// 北伐事件
	@Override
	public void onAttackCastleEnd(int nodeIndex, byte heroCount, byte heroRemain, int star) {
		this.onTaskEvent(XsgTaskManager.EXPEDITION, 0, 1);
		updateSevenTask(SevenTargetTemplt.Expedition.name(), nodeIndex + "");
	}

	// 寻宝收获1次
	@Override
	public void onGain(ItemView[] items, String addArr) {
		this.onTaskEvent(XsgTaskManager.HARVEST, 0, 1);
	}

	@Override
	public void onEquipSmelt(EquipItem[] smeltEquips, ItemView[] rewardItem, int money) {
		if (smeltEquips != null) {
			for (EquipItem item : smeltEquips) {
				// 熔炼绿装一次
				if (item != null && item.getQuatityColor().equals(QualityColor.Green)) {
					this.onTaskEvent(XsgTaskManager.SMELT, 0, 1);
				}
			}
		}

		if (smeltEquips != null) {
			for (EquipItem item : smeltEquips) {
				// 熔炼蓝装装一次
				if (item != null && item.getQuatityColor().equals(QualityColor.Blue)) {
					this.onTaskEvent(XsgTaskManager.SMELT_BLUE, 0, 1);
				}
			}
		}
	}

	@Override
	public void onAddSalary(String roleId, int yuanbao) {
		// 领俸禄
		this.onTaskEvent(XsgTaskManager.PAYMENT, 0, 1);
	}

	@Override
	public void onShopping(String shopId, String templateId, int num, int price, int type) throws Exception {
		// 免费购买商城每日登录礼包, 硬编码每日登录礼包的模版ID
		if (type == 1 && price == 0 && "ch_shop001".equals(templateId)) { //$NON-NLS-1$
			this.onTaskEvent(XsgTaskManager.FREE_BUY, 0, 1);
		}
		if (price > 0)
			updateSevenTask(SevenTargetTemplt.ShopBuy.name(), 1 + "");
	}

	@Override
	public void onRoulette(int times, RandomRoulette award) {
		this.onTaskEvent(XsgTaskManager.DRAW_SIGN, 0, 1);
	}

	@Override
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		ChargeItemT itemT = XsgVipManager.getInstance().getChargeItemT(params.item);
		List<TaskTemplT> targetList = XsgTaskManager.getInstance().targetMap.get(XsgTaskManager.RECHARGE);
		if (itemT != null && targetList != null && targetList.size() > 0) {
			// 每日充值任务, 充多少钱看作这个任务被完成了多少次, 当(累计)达到指定数量的时候任务完成
			this.onTaskEvent(XsgTaskManager.RECHARGE, 0, itemT.rmb);
		}
	}

	@Override
	public void onbuy(int num, int crit, int jinbi) {
		this.onTaskEvent(XsgTaskManager.BUY_JINBI, 0, 1);
	}

	@Override
	public void onFriendFight(String targetId, int resFlag) {
		this.onTaskEvent(XsgTaskManager.FIGHT, 0, 1);
		updateSevenTask(SevenTargetTemplt.Fight.name(), 1 + "");
	}

	@Override
	public void onHeroLevelUp(int tempId, int lvl) {
		this.onTaskEvent(XsgTaskManager.HeroLvUp, 0, 1);
		updateSevenTask(SevenTargetTemplt.HeroLv.name(), "");
	}

	@Override
	public void onAddFriendPoint(String targetId, int addNum, int currentNum) {
		this.onTaskEvent(XsgTaskManager.FriendPoint, 1, roleRt.getSnsController().getMaxFriendPoint());
		this.onTaskEvent(XsgTaskManager.FRIENDPOINTUP, 0, addNum);
	}

	/**
	 * 生成物品数组
	 * 
	 * @param items
	 *            奖励配置表
	 * @return 物品数组
	 */
	private IntString[] wrapRewardItem(ItemView[] itemView) {
		if (itemView == null || itemView.length == 0) {
			return null;
		}
		IntString[] items = new IntString[itemView.length];
		for (int i = 0; i < itemView.length; i++) {
			String itemId = itemView[i].templateId;
			int itemNum = itemView[i].num;
			items[i] = new IntString(itemNum, itemId);
		}
		return items;
	}

	@Override
	public void onSnsSendJunLing(String sender, String accepter, int num) {
		if (sender.equals(roleRt.getRoleId()))
			this.onTaskEvent(XsgTaskManager.PRESENT, 0, 1);
	}

	@Override
	public void onApproveJoin(String factionId, String roleId) {
		if (roleId == roleRt.getRoleId())
			updateSevenTask(SevenTargetTemplt.JionGuid.name(), 1 + "");
	}

	@Override
	public void onLevelChange(int level) {
		updateSevenTask(SevenTargetTemplt.CraftRank.name(), level + "");
	}

	@Override
	public void relationChanged(String target, SNSType relationType, RelationChangeEventActionType actionType) {
		updateSevenTask(SevenTargetTemplt.Friends.name(), "");
	}

	@Override
	public void onHeroBreakUp(IHero hero, int beforeBreakLevel) {
	}

	@Override
	public void onApplyingHappend(String target) {
		updateSevenTask(SevenTargetTemplt.Friends.name(), "");
	}

	@Override
	public void onDepart(String heroIds, int recommendNum) {
		updateSevenTask(SevenTargetTemplt.Harvest.name(), 1 + "");
	}

	@Override
	public void onCreateFaction(String factionId) {
		updateSevenTask(SevenTargetTemplt.JionGuid.name(), 1 + "");

	}

	@Override
	public void onExchange(String itemId, int cost) {
		List<EquipItem> list = roleRt.getItemControler().findEquipByTemplateCode(itemId);
		if (list != null) {
			EquipItem item = list.get(0);
			if (item != null && item.getQuatityColor().equals(QualityColor.Blue)) {
				this.onTaskEvent(XsgTaskManager.EXCHANGE_BLUE, 0, 1);
			}
		}
	}

	@Override
	public void onFight(int resFlag, int roleRank, int rivalRank) {
		// 竞技场作战3次
		this.onTaskEvent(XsgTaskManager.PVPS, 0, 1);
	}

	private void notifyRedPoint() {
		MajorUIRedPointNote redPoint = getRedPointNote();
		if (redPoint != null) {
			roleRt.getNotifyControler().onMajorUIRedPointChange(redPoint);
		}
	}

	@Override
	public void onClear(SmallCopyT copyT, CopyChallengeResultView mockView, List<ItemView> additionList) {
		if (null == copyT)
			return;
		Map<Integer, RoleTask> roleTakMap = this.roleDb.getRoleTask();
		List<TaskTemplT> passNumList = XsgTaskManager.getInstance().targetMap.get(XsgTaskManager.FORT_WIPE);
		if (passNumList != null) {
			for (TaskTemplT taskTemplT : passNumList) {
				if (roleRt.getLevel() < taskTemplT.minLevel)
					continue;
				RoleTask task = roleTakMap.get(taskTemplT.id);
				if (task != null && task.getState() == 0) {
					// 关卡ID
					int deID = Integer.valueOf(taskTemplT.demand.split(",")[0]); //$NON-NLS-1$
					int deNum = Integer.valueOf(taskTemplT.demand.split(",")[1]); //$NON-NLS-1$
					boolean isUpdate = false;
					// 任意关卡
					if (deID == 0 || copyT.id == deID) {
						isUpdate = true;
					}
					if (isUpdate) {
						task.setNum(task.getNum() + 1);
						if (task.getNum() >= deNum) {
							task.setState(XsgTaskManager.taskStat.obtain.ordinal());
							this.notifyRedPoint();
						}
						roleTakMap.put(taskTemplT.id, task);
					}
				}
			}
		}
	}

	/**
	 * 购买军令
	 */
	@Override
	public void onBuyJunLing(int todayNum, int yuanbao, int militaryOrder) {
		this.onTaskEvent(XsgTaskManager.BuyMed, 0, 1);
	}

	/**
	 * 寻宝救援好友
	 */
	@Override
	public void onTreasureHelpFriend() {
		this.onTaskEvent(XsgTaskManager.TreasureHelpFriends, 0, 1);
	}

	/**
	 * 捐献工会科技
	 */
	@Override
	public void onDonateTec() {
		this.onTaskEvent(XsgTaskManager.GuildDonateTec, 0, 1);
	}
}
