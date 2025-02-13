/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleController
 * 功能描述：
 * 文件名：FactionBattleController.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.XSanGo.Protocol.AMD_Faction_startBattle;
import com.XSanGo.Protocol.ActionN;
import com.XSanGo.Protocol.CrossPvpView;
import com.XSanGo.Protocol.DamageN;
import com.XSanGo.Protocol.DuelBuffView;
import com.XSanGo.Protocol.DuelResult;
import com.XSanGo.Protocol.DuelTemplateType;
import com.XSanGo.Protocol.EffectN;
import com.XSanGo.Protocol.EnrollResult;
import com.XSanGo.Protocol.FactionBattleEventView;
import com.XSanGo.Protocol.FactionBattleKitsView;
import com.XSanGo.Protocol.FactionBattleLogView;
import com.XSanGo.Protocol.FactionBattlePersonalRankResultView;
import com.XSanGo.Protocol.FactionBattleRankResultView;
import com.XSanGo.Protocol.FactionBattleResultView;
import com.XSanGo.Protocol.FactionBattleShow;
import com.XSanGo.Protocol.FactionBattleStrongholdView;
import com.XSanGo.Protocol.FactionBattleView;
import com.XSanGo.Protocol.FactionCallBackPrx;
import com.XSanGo.Protocol.GrowableProperty;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.KitsChangeView;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpMovieView;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.SceneDuelView;
import com.XSanGo.Protocol.StrongHoldState;
import com.google.gson.reflect.TypeToken;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.MovieThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.chat.XsgChatManager.AdContentType;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.FactionBattle;
import com.morefun.XSanGo.db.game.FactionBattleConfig;
import com.morefun.XSanGo.db.game.FactionBattleDAO;
import com.morefun.XSanGo.db.game.FactionBattleEvent;
import com.morefun.XSanGo.db.game.FactionBattleEventRatio;
import com.morefun.XSanGo.db.game.FactionBattleLog;
import com.morefun.XSanGo.db.game.FactionBattleMember;
import com.morefun.XSanGo.db.game.FactionBattleRobot;
import com.morefun.XSanGo.db.game.FactionMember;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.event.protocol.IFactionBattleBuyMarchCool;
import com.morefun.XSanGo.event.protocol.IFactionBattleChangeCamp;
import com.morefun.XSanGo.event.protocol.IFactionBattleDiggingTreasure;
import com.morefun.XSanGo.event.protocol.IFactionBattleEnroll;
import com.morefun.XSanGo.event.protocol.IFactionBattleEnter;
import com.morefun.XSanGo.event.protocol.IFactionBattleLeave;
import com.morefun.XSanGo.event.protocol.IFactionBattleMarch;
import com.morefun.XSanGo.event.protocol.IFactionBattleResult;
import com.morefun.XSanGo.event.protocol.IFactionBattleUseKits;
import com.morefun.XSanGo.event.protocol.IFactionBattleUseKitsSteal;
import com.morefun.XSanGo.event.protocol.IOffline;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.faction.factionBattle.XsgFactionBattleManager.ConfigParam;
import com.morefun.XSanGo.faction.factionBattle.XsgFactionBattleManager.EventType;
import com.morefun.XSanGo.faction.factionBattle.XsgFactionBattleManager.Kits;
import com.morefun.XSanGo.faction.factionBattle.XsgFactionBattleManager.KitsMsgType;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.net.GameSessionI;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 公会战处理实现类
 * 
 * @author zwy
 * @since 2016-1-5
 * @version 1.0
 */
public class FactionBattleController implements IFactionBattleController, IOffline {

	/** 角色接口 */
	private IRole roleRt;

	/** 角色数据对象 */
	private Role roleDB;

	/** 是否战斗中 */
	private boolean isBattleing;

	/** 公会战报数据 */
	private PvpMovieView movieView;

	/** 报名事件 */
	private IFactionBattleEnroll eventEnroll;

	/** 阵营变更事件 */
	private IFactionBattleChangeCamp eventChangeCamp;

	/** 进入战场事件 */
	private IFactionBattleEnter eventEnter;

	/** 离开战场事件 */
	private IFactionBattleLeave eventLeave;

	/** 行军事件 */
	private IFactionBattleMarch eventMarch;

	/** 行军冷却事件 */
	private IFactionBattleBuyMarchCool eventBuyMarchCooling;

	/** 挖宝事件 */
	private IFactionBattleDiggingTreasure eventDiggingTreasure;

	/** 使用锦囊事件 */
	private IFactionBattleUseKits eventUseKits;

	/** 使用锦囊：顺手牵羊事件 */
	private IFactionBattleUseKitsSteal eventUseKitsSteal;

	/** 战斗结果 */
	private IFactionBattleResult eventResult;

	public FactionBattleController(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		// 注册事件
		this.eventEnroll = this.roleRt.getEventControler().registerEvent(IFactionBattleEnroll.class);
		this.eventChangeCamp = this.roleRt.getEventControler().registerEvent(IFactionBattleChangeCamp.class);
		this.eventEnter = this.roleRt.getEventControler().registerEvent(IFactionBattleEnter.class);
		this.eventLeave = this.roleRt.getEventControler().registerEvent(IFactionBattleLeave.class);
		this.eventMarch = this.roleRt.getEventControler().registerEvent(IFactionBattleMarch.class);
		this.eventBuyMarchCooling = this.roleRt.getEventControler().registerEvent(IFactionBattleBuyMarchCool.class);
		this.eventDiggingTreasure = this.roleRt.getEventControler().registerEvent(IFactionBattleDiggingTreasure.class);
		this.eventUseKits = this.roleRt.getEventControler().registerEvent(IFactionBattleUseKits.class);
		this.eventUseKitsSteal = this.roleRt.getEventControler().registerEvent(IFactionBattleUseKitsSteal.class);
		this.eventResult = this.roleRt.getEventControler().registerEvent(IFactionBattleResult.class);

		// 注册handler
		this.roleRt.getEventControler().registerHandler(IOffline.class, this);
	}

	@Override
	public void checkFactionBattle2LeaveFaction() throws NoteException {
		// 公会战开战期间
		if (XsgFactionBattleManager.getInstance().isFactionBattleOpenTime()) {
			if (XsgFactionBattleManager.getInstance().isEnrollFactionBattle(roleRt)) {
				throw new NoteException(Messages.getString("FactionBattleController.cannotLeaveFaction"));
			}
		}
	}

	@Override
	public boolean isRedPoint() {
		if (isEnrollTime()) {// 报名时间之内,并且还未报名则显示红点
			if (!XsgFactionBattleManager.getInstance().isEnrollFactionBattle(roleRt)) {
				return true;
			}
		}
		if (XsgFactionBattleManager.getInstance().isFactionBattleOpenTime()) {// 公会战时间之内统一显示红点
			return true;
		}
		return false;
	}

	/**
	 * 是否是在报名时间之内
	 * 
	 * @return
	 */
	private boolean isEnrollTime() {
		if (XsgFactionBattleManager.getInstance().checkFactionBattleWeek()) {
			FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
			Date beginDate = DateUtil.joinTime(baseT.openTime);
			long enrollStartTime = beginDate.getTime() - baseT.enrollStartTime * 60 * 1000;
			// 到了报名时间
			if (System.currentTimeMillis() >= enrollStartTime && System.currentTimeMillis() < beginDate.getTime()) {
				return true;
			}
		}
		return false;
	}

	// /**
	// * 是否公会战报名人
	// *
	// * @return
	// */
	// private boolean isEnrollRole() {
	// FactionBattle fb =
	// roleRt.getFactionControler().getMyFaction().getFactionBattle();
	// if (fb == null) {
	// return false;
	// }
	// return fb.getEnrollRoleId().equals(roleRt.getRoleId());
	// }

	/**
	 * 获取公会回调接口
	 * 
	 * @return
	 */
	@Override
	public FactionCallBackPrx getFactionCallBack() {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(this.roleRt.getAccount(),
				this.roleRt.getRoleId());
		return session == null ? null : session.getFactionCallBack();
	}

	/**
	 * 获得参战公会数据
	 * 
	 * @param role
	 * @return
	 */
	private FactionBattle getFactionBattle(IRole role) {
		IFaction faction = role.getFactionControler().getMyFaction();
		return faction.getFactionBattle();
	}

	/**
	 * 获得指定玩家的公会战参战成员数据
	 * 
	 * @param role
	 * @return
	 */
	private FactionBattleMember getFbm(IRole role) {
		IFaction faction = role.getFactionControler().getMyFaction();
		return faction.getFactionBattleMember(role.getRoleId());
	}

	/**
	 * 公会基础检测 主要检测是否有公会和主配置
	 * 
	 * @throws NoteException
	 */
	private void checkBaseFactionBattle() throws NoteException {
		if (!roleRt.getFactionControler().isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		if (baseT == null) {
			throw new NoteException(Messages.getString("FactionBattleController.error"));
		}
	}

	/**
	 * 公会参战检测 针对基础检测和在公会战中的操作检测
	 * 
	 * @throws NoteException
	 */
	private void checkFactionBattleJoin() throws NoteException {
		checkBaseFactionBattle();
		if (!XsgFactionBattleManager.getInstance().isEnterFactionBattle(roleRt)) {
			throw new NoteException(Messages.getString("FactionBattleController.notJoin"));
		}
	}

	/**
	 * 公会战参战成员复活状态检测
	 * 
	 * @throws NoteException
	 */
	private void checkFactionBattleMemberRelive() throws NoteException {
		FactionBattleMember fbm = getFbm(roleRt);
		if (fbm.getReliveEndTime() != null && fbm.getReliveEndTime().getTime() >= System.currentTimeMillis()) {
			long stime = (fbm.getReliveEndTime().getTime() - System.currentTimeMillis()) / 1000;
			stime = stime < 1 ? 1 : stime;
			throw new NoteException(MessageFormat.format(Messages.getString("FactionBattleController.death"), stime));
		}
	}

	/**
	 * 战斗中检测
	 * 
	 * @throws NoteException
	 */
	private void checkFactionBattleMemberIsBattle() throws NoteException {
		if (isBattleing) {
			throw new NoteException(Messages.getString("FactionBattleController.battleing"));
		}
	}

	@Override
	public FactionBattleShow openFactionBattle() throws NoteException {
		checkBaseFactionBattle();// 基础检测
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();

		FactionBattleShow show = new FactionBattleShow();
		IFaction faction = roleRt.getFactionControler().getMyFaction();
		show.changeCampPrice = baseT.refreshCampPrice;
		if (isEnrollTime()) {// 已开始报名
			show.state = 1;
			show.time = getSurplusEnrollTime();
			FactionBattle fb = faction.getFactionBattle();
			if (fb != null) {
				FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(
						fb.getCampStrongholdId());
				show.campName = sceneT.name;
				show.campIcon = sceneT.icon;
			}
		} else {
			if (XsgFactionBattleManager.getInstance().isFactionBattleOpenTime()) {// 已开战
				show.state = 2;
			}
		}
		if (XsgFactionBattleManager.getInstance().isEnrollFactionBattle(roleRt)) {// 已报名参加
			show.isEnroll = true;
		}
		show.isCanChangeCamp = isCanChangeCamp(roleRt);
		return show;
	}

	@Override
	public EnrollResult enrollFactionBattle() throws NoteException {
		checkBaseFactionBattle();// 基础检测
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		if (!isEnrollTime()) {// 未到报名时间或者已开战
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(baseT.openTime);
			startDate.add(Calendar.MINUTE, -baseT.enrollStartTime);
			String enrollStartTime = DateUtil.format(startDate.getTime(), DateUtil.HHMM_PATTERN);
			String enrollCloseTime = DateUtil.format(baseT.openTime, DateUtil.HHMM_PATTERN);
			throw new NoteException(TextUtil.format(Messages.getString("FactionBattleController.notTheEnrollTime"),
					baseT.openWeek, enrollStartTime, enrollCloseTime));
		}
		if (XsgFactionBattleManager.getInstance().isEnrollFactionBattle(roleRt)) {// 已报名
			throw new NoteException(Messages.getString("FactionBattleController.repeatEnroll"));
		}
		IFaction faction = roleRt.getFactionControler().getMyFaction();
		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
		if (my.getDutyId() < Const.Faction.DUTY_ELDER) {// 权限不够
			throw new NoteException(Messages.getString("FactionBattleController.withoutPermission"));
		}
		if (baseT.needFactionLvl > faction.getLevel()) {// 等级不够
			throw new NoteException(TextUtil.format(Messages.getString("FactionBattleController.levelNoEnough"),
					baseT.needFactionLvl));
		}
		int campId = randomCamp(0);
		enrollFactionBattleSuccess(campId);

		eventEnroll.onEnroll(campId, my.getDutyId(), faction.getId());

		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(campId);
		return new EnrollResult(sceneT.name, sceneT.icon, getSurplusEnrollTime());
	}

	/**
	 * 成功报名公会战
	 * 
	 * @param campId
	 * 
	 */
	private void enrollFactionBattleSuccess(int campId) {
		FactionBattle fb = new FactionBattle();
		fb.setFactionId(roleDB.getFactionId());
		fb.setEnrollTime(new Date());
		fb.setCampStrongholdId(campId);
		fb.setEnrollRoleId(roleRt.getRoleId());
		fb.setUpdateTime(new Date());
		IFaction faction = roleRt.getFactionControler().getMyFaction();
		faction.setFactionBattle(fb);
		XsgFactionBattleManager.getInstance().addJoinFaction(faction.getId());

		// 发送公告
		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(campId);
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		String startTime = DateUtil.format(baseT.openTime, DateUtil.HHMM_PATTERN);
		XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(faction, "Signup_Suc",
				getDutyName(my.getDutyId()), orgnizeRoleText(roleRt), sceneT.name, startTime);
	}

	/**
	 * 随机获取阵营
	 * 
	 * @param campId
	 * 
	 * @return
	 */
	private int randomCamp(int campId) {
		int[] temps = ArrayUtils.clone(XsgFactionBattleManager.getInstance().getCamps());
		if (temps.length == 1) {
			return temps[0];
		}
		FactionBattleConfig config = XsgFactionBattleManager.getInstance().getRunTimeConfig();
		// 已报名的阵营
		List<Integer> enrollList = null;
		if (config != null) {
			IFaction faction = roleRt.getFactionControler().getMyFaction();
			// 上一轮第一名直接返回当时报名的阵营
			if (faction.getId().equals(config.getFirstFactionId())) {
				return config.getFirstCampId();
			}
			if (config.getFirstCampId() > 0) {// 去除榜首的阵营
				temps = ArrayUtils.removeElement(temps, config.getFirstCampId());
			}
			enrollList = TextUtil.GSON.fromJson(config.getEnrollCampId(), new TypeToken<List<Integer>>() {
			}.getType());
		}
		if (campId > 0) {// 阵营变更随机，需要排除已随机到的初始点
			temps = ArrayUtils.removeElement(temps, campId);
			return temps[NumberUtil.random(temps.length)];
		}
		// 已报名阵营已满重新随机
		if (enrollList != null) {
			if (enrollList.size() == temps.length) {
				enrollList.clear();
			}
			for (int id : enrollList) {
				temps = ArrayUtils.removeElement(temps, id);
			}
		}
		int radCampId = temps[NumberUtil.random(temps.length)];
		enrollList = enrollList == null ? new ArrayList<Integer>() : enrollList;
		enrollList.add(radCampId);
		XsgFactionBattleManager.getInstance().saveRuntimeConfig(ConfigParam.InitEnroll,
				TextUtil.GSON.toJson(enrollList));
		return radCampId;
	}

	/**
	 * 剩余的报名时间 单位秒
	 * 
	 * @return
	 */
	private int getSurplusEnrollTime() {
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		return (int) ((DateUtil.joinTime(baseT.openTime).getTime() - System.currentTimeMillis()) / 1000);
	}

	/**
	 * 获取职业对应中文名称
	 * 
	 * @param dutyId
	 * @return
	 */
	private String getDutyName(int dutyId) {
		switch (dutyId) {
		case Const.Faction.DUTY_BOSS:
			return Messages.getString("CenterI.23");
		case Const.Faction.DUTY_ELDER:
			return Messages.getString("CenterI.24");
		default:
			return Messages.getString("CenterI.25");
		}
	}

	@Override
	public EnrollResult changeFactionBattleCamp() throws NoteException, NotEnoughYuanBaoException {
		checkBaseFactionBattle();// 基础检测
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		if (!XsgFactionBattleManager.getInstance().isEnrollFactionBattle(roleRt)) {// 未报名不能改变阵营
			throw new NoteException(Messages.getString("FactionBattleController.notEnroll"));
		}
		if (!isCanChangeCamp(roleRt)) {
			throw new NoteException(Messages.getString("FactionBattleController.notChangeCamp"));
		}
		if (!isEnrollTime()) {// 未到报名时间或者已开战
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(baseT.openTime);
			startDate.add(Calendar.MINUTE, -baseT.enrollStartTime);
			String enrollStartTime = DateUtil.format(startDate.getTime(), DateUtil.HHMM_PATTERN);
			String enrollCloseTime = DateUtil.format(baseT.openTime, DateUtil.HHMM_PATTERN);
			throw new NoteException(TextUtil.format(Messages.getString("FactionBattleController.notTheEnrollTime"),
					baseT.openWeek, enrollStartTime, enrollCloseTime));
		}
		IFaction faction = roleRt.getFactionControler().getMyFaction();
		// if (!roleRt.getRoleId().equals(fb.getEnrollRoleId())) {// 报名人才能改变阵营
		if (!faction.getBossId().equals(roleRt.getRoleId())) {// 会长才能更换阵营
			throw new NoteException(Messages.getString("FactionBattleController.notEnrollRole"));
		}
		if (XsgFactionBattleManager.getInstance().isFactionBattleOpenTime()) {// 已开战不能改变阵营
			throw new NoteException(Messages.getString("FactionBattleController.canNotChangeByStart"));
		}
		// 扣钱
		roleRt.winYuanbao(-baseT.refreshCampPrice, true);
		FactionBattle fb = getFactionBattle(roleRt);
		int campId = randomCamp(fb.getCampStrongholdId());
		changeCamp(campId);

		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
		eventChangeCamp.onChangeCamp(campId, my.getDutyId());

		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(campId);
		return new EnrollResult(sceneT.name, sceneT.icon, getSurplusEnrollTime());
	}

	/**
	 * 改变阵营
	 * 
	 * @param campId
	 */
	private void changeCamp(int campId) {
		FactionBattle fb = getFactionBattle(roleRt);
		fb.setCampStrongholdId(campId);
		// 发送公告
		IFaction faction = roleRt.getFactionControler().getMyFaction();
		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(campId);
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		String startTime = DateUtil.format(baseT.openTime, DateUtil.HHMM_PATTERN);
		XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(faction, "Signup_Change",
				getDutyName(my.getDutyId()), orgnizeRoleText(roleRt), sceneT.name, startTime);
	}

	@Override
	public FactionBattleView enterFactionBattle() throws NoteException {
		checkBaseFactionBattle();// 基础检测
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		if (!XsgFactionBattleManager.getInstance().isEnrollFactionBattle(roleRt)) {// 未报名不能参战
			throw new NoteException(Messages.getString("FactionBattleController.notEnroll1"));
		}
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtil.joinTime(baseT.openTime));
		if (!XsgFactionBattleManager.getInstance().isFactionBattleOpenTime()) {// 未开战
			String startTime = DateUtil.format(startDate.getTime(), DateUtil.HHMM_PATTERN);
			throw new NoteException(TextUtil.format(Messages.getString("FactionBattleController.notStart"), startTime));
		}
		startDate.add(Calendar.MINUTE, baseT.continueTime);
		if (System.currentTimeMillis() > startDate.getTimeInMillis()) {// 已结束
			String endTime = DateUtil.format(startDate.getTime(), DateUtil.HHMM_PATTERN);
			throw new NoteException(TextUtil.format(Messages.getString("FactionBattleController.end"), endTime));
		}
		if (baseT.needRoleLvl > roleDB.getLevel()) {// 等级不足
			throw new NoteException(TextUtil.format(Messages.getString("FactionBattleController.roleLevelNoEnough"),
					baseT.needRoleLvl));
		}
		int strongholdId = getFactionBattle(roleRt).getCampStrongholdId();
		FactionBattleMember fbm = getFbm(roleRt);
		if (fbm != null) {// 上次离开所在的据点
			strongholdId = fbm.getStrongholdId();
		}
		// 据点移动
		if (!XsgFactionBattleManager.getInstance().isEnterFactionBattle(roleRt)) {// 针对客户端战场内部切换的处理
			boolean isFirstEnter = getFbm(roleRt) == null ? true : false;
			moveStronghold(roleRt, strongholdId, 0);
			// 首次进入获取锦囊
			int kitsId = 0;
			if (isFirstEnter) {
				kitsId = gainFactionBattleKits(roleRt, XsgFactionBattleManager.KitsMsgType.First);
			}
			// 进入战场 参战列表中增加此玩家
			XsgFactionBattleManager.getInstance().getJoinRoleList().add(roleRt.getRoleId());
			this.eventEnter.onEnter(strongholdId, kitsId);
		}
		return createBattleView();
	}

	/**
	 * 创建公会战场主界面视图
	 * 
	 * @return
	 */
	private FactionBattleView createBattleView() {
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		FactionBattleView view = new FactionBattleView();
		// 计算剩余时间
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtil.joinTime(baseT.openTime));
		startDate.add(Calendar.MINUTE, baseT.continueTime);
		view.surplusTime = (int) ((startDate.getTimeInMillis() - System.currentTimeMillis()) / 1000);
		// 据点状态
		Map<Integer, StrongHold> strongholdDatas = XsgFactionBattleManager.getInstance().getStrongholdDatas();
		view.states = createStrongHoldStateView(roleRt,
				ArrayUtils.toPrimitive(strongholdDatas.keySet().toArray(new Integer[0])));
		// 成员数据
		FactionBattleMember fbm = getFbm(roleRt);
		view.forage = fbm.getForage();
		view.badge = fbm.getBadge();
		view.strongholdId = fbm.getStrongholdId();
		view.marchingCoolingTime = getMarkchingCoolingTime();
		view.diggingTreasureTime = getDiggingTreasureTime(roleRt);
		view.marchingNum = fbm.getMarchingCoolingCDNum();
		view.isOpenMarching = fbm.getDeaths() < 1 ? false : true;
		// 默认打开据点角色列表
		view.views = createFactionBattleStrongholdView(roleRt, fbm.getStrongholdId());
		// 锦囊数据
		view.kitses = getKitsView();
		// 随机事件数据
		FactionBattleEvent event = XsgFactionBattleManager.getInstance().getRandomEvent();
		if (event != null && event.getDrawState() == 0) {// 已领取道具的事件不再进行展示
			FactionBattleRandomEventT eventT = XsgFactionBattleManager.getInstance().getEventTs()
					.get(event.getEventId());
			view.eView = new FactionBattleEventView(event.getStrongholdId(), eventT.icon);
		}
		return view;
	}

	/**
	 * 据点状态视图数据
	 * 
	 * @param role
	 * @param strongholdIds
	 * @return
	 */
	private StrongHoldState[] createStrongHoldStateView(IRole role, int... strongholdIds) {
		StrongHoldState[] views = null;
		for (int strongholdId : strongholdIds) {
			StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
			StrongHoldState sstate = new StrongHoldState(strongholdId, (byte) sh.occupyState(),
					(byte) sh.convertCampId(getFactionBattle(role).getCampStrongholdId()));
			views = (StrongHoldState[]) ArrayUtils.add(views, sstate);
		}
		return views;
	}

	/**
	 * 创建据点角色列表视图
	 * 
	 * @param role
	 * @param strongholdId
	 *            据点编号
	 * @return
	 */
	private FactionBattleStrongholdView createFactionBattleStrongholdView(IRole role, int strongholdId) {
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
		FactionBattleStrongholdView view = new FactionBattleStrongholdView();
		if (XsgFactionBattleManager.getInstance().isInitCamp(strongholdId)) {// 大本营无须玩家列表
			view.isBaseCamp = true;
		} else {
			int debuffLvl = getFbm(role).getDeBuffLvl();
			FactionBattleDebuffT debuff = XsgFactionBattleManager.getInstance().getDebuffs().get(debuffLvl);
			view.debuffLvl = debuff == null ? 0 : debuff.debuffValue;
			// 据点人数
			view.attackRoleNum = sh.getAttackRoleList().size();
			view.defendRoleNum = sh.getDefendRoleList().size() + sh.getDefendRobotList().size();
			view.curRoleNum = sh.number();

			FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(strongholdId);
			FactionBattleStrongholdT st = XsgFactionBattleManager.getInstance().getBattleStrongholds().get(sceneT.type);
			view.maxRoleNum = st.loadRoleNum;
			// 状态和收益
			StrongholdStateT sst = getStrongholdStateT(strongholdId);
			if (sst != null) {
				view.stateName = sst.stateName;
				view.incomePer = sst.diggingTreasureIncome;
			}
			// 占领方收益显示多倍
			if (isOccupy(role, strongholdId)) {
				FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
				float factionRatio = baseT.occupationGain / 100F;
				view.occupyIncomePer = (int) (sst == null ? 0 : factionRatio);
			}

			// 计算当前玩家在当前据点本公会的人数
			List<String> list = isOccupy(role, strongholdId) ? sh.getDefendRoleList() : sh.getAttackRoleList();
			for (String roleId : list) {
				IRole targetRole = XsgRoleManager.getInstance().findRoleById(roleId);
				if (targetRole.getFactionControler().getFactionId().equals(role.getFactionControler().getFactionId())) {
					view.factionRoleNum++;
				}
			}
			Date attackTime = getFbm(role).getAttackWaitEndTime();
			if (attackTime != null) {
				long delayTime = attackTime.getTime() - System.currentTimeMillis();
				view.attackTime = (int) (delayTime > 0 ? (delayTime / 1000) : 0);
			}
			if (!isOccupy(role, strongholdId)) {
				view.isHiddenAttack = true;
				for (String roleId : sh.getAttackRoleList()) {
					IRole targetRole = XsgRoleManager.getInstance().findRoleById(roleId);
					if (!targetRole.getFactionControler().getFactionId()
							.equals(role.getFactionControler().getFactionId())) {
						view.isHiddenAttack = false;
						break;
					}
				}
			}
		}
		return view;
	}

	/**
	 * 获得当前公会战角色自身的锦囊视图
	 * 
	 * @return
	 */
	private FactionBattleKitsView[] getKitsView() {
		FactionBattleKitsView[] views = new FactionBattleKitsView[4];
		Map<Integer, FactionBattleKits> kitses = parseKits();
		views[0] = createKit(kitses, Kits.Steal);
		views[1] = createKit(kitses, Kits.Rxql);
		views[2] = createKit(kitses, Kits.LanXiang);
		views[3] = createKit(kitses, Kits.ZiYang);
		return views;
	}

	/**
	 * 锦囊视图
	 * 
	 * @param kitses
	 * @param kitId
	 * @return
	 */
	private FactionBattleKitsView createKit(Map<Integer, FactionBattleKits> kitses, int kitId) {
		FactionBattleKits fbk = kitses.get(kitId);
		if (fbk == null) {
			return new FactionBattleKitsView(kitId, 0, 0);
		}
		return new FactionBattleKitsView(kitId, fbk.getNum(), getFactionBattleKitsCdTime(fbk));
	}

	/**
	 * 是否占领当前据点
	 * 
	 * @param role
	 * @param strongholdId
	 * @return
	 */
	private boolean isOccupy(IRole role, int strongholdId) {
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
		return getFactionBattle(role).getCampStrongholdId() == sh.getOccupyCampId();
	}

	/**
	 * 是否同阵营
	 * 
	 * @param role
	 * @param targetRole
	 * @return
	 */
	private boolean isSameCamp(IRole role, IRole targetRole) {
		return getFactionBattle(role).getCampStrongholdId() == getFactionBattle(targetRole).getCampStrongholdId();
	}

	/**
	 * 是否能更换阵营
	 * 
	 * @param role
	 * @return
	 */
	private boolean isCanChangeCamp(IRole role) {
		FactionBattleConfig config = XsgFactionBattleManager.getInstance().getRunTimeConfig();
		if (config == null) {
			return true;
		}
		return !role.getFactionControler().getFactionId().equals(config.getFirstFactionId());
	}

	/**
	 * 进入据点（移动）
	 * 
	 * @param role
	 * @param strongholdId
	 *            进入的据点
	 * @return 返回是否需要进行状态通知
	 */
	private boolean enterStronghold(IRole role, int strongholdId) {
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId, true);
		boolean isStateNotify = false;
		if (XsgFactionBattleManager.getInstance().isInitCamp(strongholdId)) {// 大本营
			sh.addDefendRole(role.getRoleId());
		} else {// 据点
			if (sh.isCanOccupy()) {// 第一个进入
				sh.occupy(getFactionBattle(role).getCampStrongholdId());
				isStateNotify = true;
			}
			if (isOccupy(role, strongholdId)) {// 守方
				sh.addDefendRole(role.getRoleId());
			} else {
				sh.addAttackRole(role.getRoleId());
			}
		}

		// 参战成员对象
		FactionBattleMember fbm = getFbm(role);
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		if (fbm == null) {
			fbm = new FactionBattleMember();
			fbm.setJoinTime(new Date());
			fbm.setRoleId(role.getRoleId());
			IFaction faction = role.getFactionControler().getMyFaction();
			faction.addFactionBattleMember(fbm);

			// 设置战斗前置时间
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, baseT.frontCdTime);
			fbm.setAttackWaitEndTime(cal.getTime());
		}
		fbm.setStrongholdId(strongholdId);
		fbm.setEnterStrongholdTime(new Date());
		return isStateNotify;
	}

	/**
	 * 据点占领
	 * 
	 * @param role
	 *            占领者
	 * @param sh
	 *            据点对象
	 */
	private void occupyStronghold(IRole role, StrongHold sh) {
		sh.occupy(getFactionBattle(role).getCampStrongholdId());
		// 守方和攻方数据转移
		for (Iterator<String> it = sh.getAttackRoleList().iterator(); it.hasNext();) {
			String tempRoleId = it.next();
			IRole targetRole = XsgRoleManager.getInstance().findRoleById(tempRoleId);
			if (isSameCamp(role, targetRole)) {
				sh.addDefendRole(tempRoleId);
				it.remove();
			}
		}
		sendStrongholdOccupyMessage(role);
	}

	/**
	 * 离开据点（移动）
	 * 
	 * @param role
	 * @param strongholdId
	 *            据点
	 * @return 返回是否需要进行状态通知
	 */
	private boolean leaveStronghold(IRole role, int strongholdId) {
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
		// LogManager.warn(role.getName() + ">>leaveStronghold_old>>" +
		// sh.toString());
		boolean isStateNotify = false;
		if (XsgFactionBattleManager.getInstance().isInitCamp(strongholdId)) {// 大本营
			sh.removeDefendRole(role.getRoleId());
		} else {
			if (isOccupy(role, strongholdId)) {// 属于守方
				sh.removeDefendRole(role.getRoleId());
				if (sh.isCanOccupy()) {// 当前无守方，则从攻方获取第一个为占领者
					if (sh.getAttackRoleList().isEmpty()) {// 无攻方 则重置为无人占领
						sh.resetOccupy();
					} else {
						String troleId = sh.getAttackRoleList().get(0);
						IRole trole = XsgRoleManager.getInstance().findRoleById(troleId);
						occupyStronghold(trole, sh);
					}
					isStateNotify = true;
				}
			} else {// 攻方
				sh.removeAttackRole(role.getRoleId());
			}
		}
		// LogManager.warn(role.getName() + ">>leaveStronghold_new>>" +
		// sh.toString());
		return isStateNotify;
	}

	/**
	 * 据点移动
	 * 
	 * @param role
	 * @param enterStrongholdId
	 *            进入据点编号
	 * @param leaveStrongholdId
	 *            离开据点编号
	 */
	private void moveStronghold(IRole role, int enterStrongholdId, int leaveStrongholdId) {
		if (enterStrongholdId == leaveStrongholdId) {// 相同不处理
			return;
		}
		// 发生状态改变的据点
		int[] changeStrongholdIds = null;
		// 据点角色列表数据改变
		int[] changes = null;
		// 离开据点
		if (leaveStrongholdId > 0) {
			if (leaveStronghold(role, leaveStrongholdId)) {
				changeStrongholdIds = ArrayUtils.add(changeStrongholdIds, leaveStrongholdId);
			}
			changes = ArrayUtils.add(changes, leaveStrongholdId);
		}
		// 进入据点
		if (enterStrongholdId > 0) {
			if (enterStronghold(role, enterStrongholdId)) {
				changeStrongholdIds = ArrayUtils.add(changeStrongholdIds, enterStrongholdId);
			}
			changes = ArrayUtils.add(changes, enterStrongholdId);
		}
		// 通知据点改变
		if (changeStrongholdIds != null) {
			sendStrongholdStateNotify(changeStrongholdIds);
		}
		// 据点角色列表通知
		if (changes != null) {
			sendStrongholdRoleListNotify(changes);
		}
	}

	/**
	 * 据点角色变更通知，发送指定据点的玩家
	 * 
	 * @param strongholdIds
	 */
	private void sendStrongholdRoleListNotify(int... strongholdIds) {
		for (int strongholdId : strongholdIds) {
			StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
			if (XsgFactionBattleManager.getInstance().isInitCamp(strongholdId)) {
				continue;// 大本营无须处理通知
			}
			List<String> roleList = new ArrayList<String>();
			roleList.addAll(sh.getDefendRoleList());
			roleList.addAll(sh.getAttackRoleList());

			for (String roleId : roleList) {
				IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
				if (role != null && role.isOnline()) {
					FactionBattleStrongholdView notify = createFactionBattleStrongholdView(role, strongholdId);
					FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
					if (callBack != null) {
						callBack.begin_strongholdRoleListChangeNotify(LuaSerializer.serialize(notify));
					}
				}
			}
		}
	}

	/**
	 * 据点行军剩余时间 秒
	 * 
	 * @return
	 */
	private int getMarkchingCoolingTime() {
		FactionBattleMember fbm = getFbm(roleRt);
		if (fbm != null) {// 成员数据
			if (fbm.getMarchingCoolingEndTime() != null) {
				long surplusTime = fbm.getMarchingCoolingEndTime().getTime() - System.currentTimeMillis();
				if (surplusTime > 0) {
					return (int) (surplusTime / 1000);
				}
			}
		}
		return 0;
	}

	/**
	 * 挖宝剩余时间 秒
	 * 
	 * @param role
	 * @return
	 */
	private int getDiggingTreasureTime(IRole role) {
		FactionBattleMember fbm = getFbm(role);
		if (fbm != null) {// 成员数据
			if (fbm.getDiggingTreasureEndTime() != null) {
				long surplusTime = fbm.getDiggingTreasureEndTime().getTime() - System.currentTimeMillis();
				if (surplusTime > 0) {
					return (int) (surplusTime / 1000);
				}
			}
		}
		return 0;
	}

	/**
	 * 解析角色公会战锦囊数据
	 * 
	 * @return
	 */
	private Map<Integer, FactionBattleKits> parseKits() {
		return parseKits(roleRt);
	}

	/**
	 * 解析角色公会战锦囊数据
	 * 
	 * @return
	 */
	private Map<Integer, FactionBattleKits> parseKits(IRole role) {
		FactionBattleMember fbm = getFbm(role);
		if (fbm != null && TextUtil.isNotBlank(fbm.getItems())) {// 成员数据
			return TextUtil.GSON.fromJson(fbm.getItems(), new TypeToken<Map<Integer, FactionBattleKits>>() {
			}.getType());
		}
		return new HashMap<Integer, FactionBattleKits>();
	}

	/**
	 * 锦囊CD时间
	 * 
	 * @param kits
	 * @return
	 */
	private int getFactionBattleKitsCdTime(FactionBattleKits kits) {
		if (kits.getTime() > 0) {
			long surplusTime = kits.getTime() - System.currentTimeMillis();
			if (surplusTime > 0) {
				return (int) (surplusTime / 1000);
			}
		}
		return 0;
	}

	/**
	 * 获得公会战锦囊,通知玩家本身
	 * 
	 * @param role
	 *            获取锦囊者
	 * @param type
	 *            获取方式( 1.首次进入战场 2.战斗胜利获得 3.战斗失败复活后获得)
	 * @return 返回获取的锦囊编号
	 */
	private int gainFactionBattleKits(IRole role, int type) {
		FactionBattleKitsT kitsT = null;
		if (type == KitsMsgType.First) {
			kitsT = XsgFactionBattleManager.getInstance().randomKits(Kits.Rxql);
		} else {
			kitsT = XsgFactionBattleManager.getInstance().randomKits(0);
		}
		if (kitsT != null) {
			FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
			Map<Integer, FactionBattleKits> kitses = parseKits(role);
			String content = null;
			String icon = "";
			boolean isSuccess = addKits(kitses, kitsT.kitsId, 1);
			if (isSuccess) {
				content = XsgFactionBattleManager.getInstance().getKitsMsg(type);
				FactionBattleMember fbm = getFbm(role);
				fbm.setItems(TextUtil.GSON.toJson(kitses));
				icon = kitsT.icon;
				// 变更通知
				if (type != XsgFactionBattleManager.KitsMsgType.First) {// 首次进入,不处理变更通知
					if (callBack != null) {
						callBack.begin_kitsChangeNotify(LuaSerializer
								.serialize(new KitsChangeView[] { new KitsChangeView(kitsT.kitsId, 1) }));
					}
				}
			} else {
				content = Messages.getString("FactionBattleController.kitsFulltip");
			}
			content = MessageFormat.format(content, kitsT.name);
			if (callBack != null) {
				callBack.begin_gainKitsMessageNotify(content, icon);
			}
			return kitsT.kitsId;
		}
		return 0;
	}

	/**
	 * 增加锦囊
	 * 
	 * @param kitses
	 *            角色锦囊数据表
	 * @param kitsId
	 *            锦囊编号
	 * @param num
	 *            数量
	 * @return 成功与否
	 */
	private boolean addKits(Map<Integer, FactionBattleKits> kitses, int kitsId, int num) {
		FactionBattleKitsT kitsT = XsgFactionBattleManager.getInstance().getBattleKitsT(kitsId);
		FactionBattleKits kits = kitses.get(kitsId);
		if (kits == null) {
			kits = new FactionBattleKits();
			kits.setId(kitsId);
		}
		if (kits.getNum() < kitsT.maxNum) {
			kits.setNum(kits.getNum() + num);
			if (kits.getNum() > kitsT.maxNum) {
				kits.setNum(kitsT.maxNum);
			}
			kitses.put(kitsT.kitsId, kits);
			return true;
		}
		return false;
	}

	/**
	 * 使用锦囊
	 * 
	 * @param kitses
	 *            角色锦囊数据表
	 * @param kitsId
	 *            锦囊编号
	 * @param num
	 *            数量
	 */
	private void useKits(Map<Integer, FactionBattleKits> kitses, int kitsId, int num) {
		FactionBattleKits kits = kitses.get(kitsId);
		FactionBattleKitsT kt = XsgFactionBattleManager.getInstance().getBattleKitsT(kitsId);
		// 扣除消耗的锦囊
		kits.setNum(kits.getNum() - 1);
		// 设置CD时间
		if (kt.cd > 0) {// 存在CD时间，进行CD时间计算
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, kt.cd);
			kits.setTime(cal.getTimeInMillis());
		}
	}

	@Override
	public void leaveFactionBattle() throws NoteException {
		checkFactionBattleJoin();// 基础检测
		checkFactionBattleMemberIsBattle();// 战中检测
		FactionBattleMember fbm = getFbm(roleRt);
		leaveBattle();
		this.eventLeave.onLeave(fbm.getStrongholdId());
	}

	/**
	 * 离开战场
	 */
	private void leaveBattle() {
		FactionBattleMember fbm = getFbm(roleRt);
		// 据点移动
		// LogManager.warn(roleRt.getName() + ">>leaveBattle>>");
		moveStronghold(roleRt, 0, fbm.getStrongholdId());
		// 离开战场 从参战列表中移除该玩家
		XsgFactionBattleManager.getInstance().getJoinRoleList().remove(roleRt.getRoleId());
		// 重置角色回到初始点
		FactionBattle fb = getFactionBattle(roleRt);
		fbm.setStrongholdId(fb.getCampStrongholdId());
	}

	/**
	 * 发送据点状态通知包，通知所有参战的成员
	 * 
	 * @param strongholdIds
	 */
	private void sendStrongholdStateNotify(int... strongholdIds) {
		if (strongholdIds.length == 0) {
			return;
		}
		List<String> notifyRoleList = XsgFactionBattleManager.getInstance().getJoinRoleList();
		for (String roleId : notifyRoleList) {
			IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
			if (role != null) {
				FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
				if (callBack != null) {
					StrongHoldState[] views = createStrongHoldStateView(role, strongholdIds);
					callBack.begin_strongholdStateNotify(LuaSerializer.serialize(views));
				}
			}
		}
	}

	@Override
	public FactionBattleRankResultView lookFactionBattleRank() throws NoteException {
		// checkFactionBattleJoin();// 基础检测
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtil.joinTime(baseT.openTime));
		if (isEnrollTime()) {// 报名时间之内,无排行
			throw new NoteException(Messages.getString("FactionBattleController.notStart2rank"));
		}
		if (XsgFactionBattleManager.getInstance().isFactionBattleOpenTime()) {
			if (System.currentTimeMillis() - startDate.getTimeInMillis() < 5 * 60 * 1000) {// 开战5分钟之后才能查看排行榜信息
				throw new NoteException(Messages.getString("FactionBattleController.rankFail"));
			}
		}
		return XsgFactionBattleManager.getInstance().createFactionBattleRankResultView(roleDB.getFactionId());
	}

	@Override
	public FactionBattlePersonalRankResultView lookFactionBattlePersonalRank() throws NoteException {
		// checkFactionBattleJoin();// 基础检测
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtil.joinTime(baseT.openTime));
		if (isEnrollTime()) {// 报名时间之内,无排行
			throw new NoteException(Messages.getString("FactionBattleController.notStart2rank"));
		}
		if (XsgFactionBattleManager.getInstance().isFactionBattleOpenTime()) {
			if (System.currentTimeMillis() - startDate.getTimeInMillis() < 5 * 60 * 1000) {// 开战5分钟之后才能查看排行榜信息
				throw new NoteException(Messages.getString("FactionBattleController.rankFail"));
			}
		}
		return XsgFactionBattleManager.getInstance().createFactionBattlePersonalRankResultView(roleRt);
	}

	@Override
	public FactionBattleStrongholdView marching(boolean isUseKits, int strongholdId) throws NoteException {
		// LogManager.warn(roleRt.getName() + ">>>marching_protocol");
		checkFactionBattleJoin();// 基础检测
		checkFactionBattleMemberRelive();// 复活检测
		checkFactionBattleMemberIsBattle();// 战中检测
		FactionBattleMember fbm = getFbm(roleRt);
		if (fbm.getStrongholdId() == strongholdId) {// 原封不动，神经病
			throw new NoteException(Messages.getString("FactionBattleController.sameStronghold"));
		}
		if (!isUseKits) {// 正常行军
			if (fbm.getMarchingCoolingEndTime() != null
					&& fbm.getMarchingCoolingEndTime().getTime() >= System.currentTimeMillis()) {
				throw new NoteException(Messages.getString("FactionBattleController.haveMarchingCoolingTime"));
			}
			FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(fbm.getStrongholdId());
			List<String> strongholdList = TextUtil.stringToList(sceneT.connectPoint);
			if (!strongholdList.contains(String.valueOf(strongholdId))) {
				throw new NoteException(Messages.getString("FactionBattleController.tooFarAway"));
			}
		} else {// 使用锦囊：日行千里
			Map<Integer, FactionBattleKits> kitses = parseKits();
			checkUseKits(XsgFactionBattleManager.Kits.Rxql, kitses);// 检测锦囊使用
			// 使用锦囊
			useKits(kitses, XsgFactionBattleManager.Kits.Rxql, 1);
			fbm.setItems(TextUtil.GSON.toJson(kitses));
			// 锦囊变更通知，通知玩家本身
			KitsChangeView reduce = new KitsChangeView(XsgFactionBattleManager.Kits.Rxql, -1);
			getFactionCallBack().begin_kitsChangeNotify(LuaSerializer.serialize(new KitsChangeView[] { reduce }));
		}
		int oldStrongHoldId = fbm.getStrongholdId();
		// 行军
		marchingStronghold(strongholdId, isUseKits);

		// 创建返回视图并返回
		FactionBattleStrongholdView view = createFactionBattleStrongholdView(roleRt, strongholdId);
		// 触发事件
		this.eventMarch.onMarch(oldStrongHoldId, strongholdId, isUseKits);
		return view;
	}

	/**
	 * 据点行军
	 * 
	 * @param strongholdId
	 *            目的地
	 * @param isUseKits
	 *            是否使用锦囊：日行千里
	 */
	private void marchingStronghold(int strongholdId, boolean isUseKits) {
		FactionBattleMember fbm = getFbm(roleRt);
		// 据点移动
		// LogManager.warn(roleRt.getName() + ">>marchingStronghold>>");
		moveStronghold(roleRt, strongholdId, fbm.getStrongholdId());
		// 使用锦囊：日行千里处理
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		if (!isUseKits) {// 使用锦囊：日行千里不影响本身的时间冷却
			Calendar coolingTime = Calendar.getInstance();
			coolingTime.add(Calendar.SECOND, baseT.marchCoolingTime);
			fbm.setMarchingCoolingEndTime(coolingTime.getTime());
			// 通知行军时间
			getFactionCallBack().begin_strongholdMarchingTimeNotify(baseT.marchCoolingTime);
		}
	}

	/**
	 * 发送据点占领消息
	 * 
	 * @param role
	 *            实际占领触发者
	 */
	private void sendStrongholdOccupyMessage(IRole role) {
		IFaction faction = role.getFactionControler().getMyFaction();
		FactionMember my = faction.getMemberByRoleId(role.getRoleId());
		FactionBattleMember fbm = getFbm(role);
		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(fbm.getStrongholdId());
		XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(faction, "Sociaty_Occupied",
				getDutyName(my.getDutyId()), orgnizeRoleText(role), sceneT.name);

		FactionBattle fb = getFactionBattle(role);
		int campStrongholdId = fb.getCampStrongholdId();
		FactionBattleSceneT campSceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(campStrongholdId);
		// 大类型据点发送系统公告
		if (sceneT.type == 4) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("~force_name_1~", campSceneT.name);
			params.put("~role_id~", role.getRoleId());
			params.put("~role_name~", role.getName());
			params.put("~role_vip~", role.getVipLevel());
			params.put("~stronghold~", sceneT.name);
			XsgChatManager.getInstance().sendAnnouncement(AdContentType.OccupyStronghold, params);
		}
	}

	/**
	 * 发送据点失去消息
	 * 
	 * @param role
	 *            实际失去触发者
	 */
	private void sendStrongholdLoseMessage(IRole role) {
		IFaction faction = role.getFactionControler().getMyFaction();
		FactionBattleMember fbm = getFbm(role);
		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(fbm.getStrongholdId());
		XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(faction, "Sociaty_Lose", sceneT.name);
	}

	@Override
	public void buyMarchingCooling() throws NoteException, NotEnoughYuanBaoException {
		checkFactionBattleJoin();// 基础检测
		checkFactionBattleMemberRelive();// 复活检测
		FactionBattleMember fbm = getFbm(roleRt);
		Date coolingEndTime = fbm.getMarchingCoolingEndTime();
		if (fbm.getDeaths() < 1) {// 需要死亡一次才能使用行军冷却功能
			throw new NoteException(Messages.getString("FactionBattleController.marchingNotUse"));
		}
		if (coolingEndTime == null || coolingEndTime.getTime() < System.currentTimeMillis()) {
			throw new NoteException(Messages.getString("FactionBattleController.canMarching"));
		}
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		int money = baseT.marchCdPrice + fbm.getMarchingCoolingCDNum() * baseT.marchCdAddPrice;
		roleRt.winYuanbao(-money, true);
		fbm.setMarchingCoolingCDNum(fbm.getMarchingCoolingCDNum() + 1);
		fbm.setMarchingCoolingEndTime(new Date());
		// 通知行军时间
		getFactionCallBack().begin_strongholdMarchingTimeNotify(0);

		this.eventBuyMarchCooling.onBuyMarchCooling(fbm.getMarchingCoolingCDNum(), coolingEndTime == null ? ""
				: DateUtil.format(coolingEndTime));
	}

	@Override
	public IntString[] diggingTreasure() throws NoteException {
		checkFactionBattleJoin();// 基础检测
		checkFactionBattleMemberRelive();// 复活检测
		FactionBattleMember fbm = getFbm(roleRt);
		if (fbm.getDiggingTreasureEndTime() != null
				&& fbm.getDiggingTreasureEndTime().getTime() >= System.currentTimeMillis()) {
			throw new NoteException(Messages.getString("FactionBattleController.haveDiggingTreasureCoolingTime"));
		}
		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(fbm.getStrongholdId());
		FactionBattleStrongholdT st = XsgFactionBattleManager.getInstance().getBattleStrongholds().get(sceneT.type);
		if (st == null) {
			throw new NoteException(Messages.getString("FactionBattleController.diggingTreasureFail"));
		}
		return diggingTreasureAward(fbm);
	}

	/**
	 * 挖宝奖励处理
	 * 
	 * @param fbm
	 * @return
	 */
	private IntString[] diggingTreasureAward(FactionBattleMember fbm) {
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(fbm.getStrongholdId());
		FactionBattleDiggingTreasureT dtt = XsgFactionBattleManager.getInstance().getBattleDiggingTreasures()
				.get(sceneT.type);
		IntString[] views = null;
		// 事件奖励加成
		String itemStr = "";
		IntString eventAwards = calculationEventAward(fbm.getStrongholdId());
		if (eventAwards != null) {
			views = (IntString[]) ArrayUtils.add(views, eventAwards);
			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(eventAwards.strValue);
			String itemColor = roleRt.getItemControler().getItemColor(itemT.getColor().value());
			itemStr = FactionBattleUtil.join(itemStr, "，<font color='ff", itemColor, "'>" + itemT.getName(), "*",
					eventAwards.intValue, "</font>");
		}
		// 阵营占领系数
		float campRatio = isOccupy(roleRt, fbm.getStrongholdId()) ? baseT.occupationGain / 100F : 1;
		// 据点负载系数
		float loadRatio = getStrongholdRatio(fbm.getStrongholdId());
		// 据点基础系数
		float baseRatio = dtt.strongholdRatio / 100F;
		// 事件系数
		float eventRatio = getEventRatio();
		// 获取TC执行结果
		TcResult tcResult = calculationDiggingTreasureTcAward(dtt);

		// 产出加成倍数
		float ratio = loadRatio * baseRatio * campRatio * eventRatio;
		int badge = 0;
		for (Entry<String, Integer> item : tcResult) {
			int count = (int) (item.getValue() * ratio);
			count = count < 1 ? 1 : count;// 容错处理，防止倍数小于1时，计算数量为0
			// LogManager.warn(roleRt.getName() + ">>diggingTreasureAward:" +
			// count + ">>" + item.getKey() + ">>" + item.getValue() +
			// ">>loadRatio:" + loadRatio + ">>baseRatio:" + baseRatio +
			// ">>factionRatio:" + factionRatio + ">>eventRatio:" + eventRatio);
			roleRt.getRewardControler().acceptReward(item.getKey(), count);
			views = (IntString[]) ArrayUtils.add(views, new IntString(count, item.getKey()));

			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(item.getKey());
			String itemColor = roleRt.getItemControler().getItemColor(itemT.getColor().value());
			itemStr = FactionBattleUtil.join(itemStr, "，<font color='ff", itemColor, "'>" + itemT.getName(), "*",
					count, "</font>");

			// 抠取徽章进行公会战排行处理
			if (item.getKey().equals(XsgFactionBattleManager.BADGE)) {
				badge += count;
			}
		}
		addBadgeRefreshRank(roleRt, badge);
		// 粮草奖励
		int forageNum = (int) (dtt.baseForage * ratio);
		views = (IntString[]) ArrayUtils.add(views, new IntString(forageNum, XsgFactionBattleManager.FORAGE));
		addForage(forageNum);
		// 设置挖宝时间
		Calendar dtCdTime = Calendar.getInstance();
		dtCdTime.add(Calendar.SECOND, baseT.diggingTreasureCdTime);
		fbm.setDiggingTreasureEndTime(dtCdTime.getTime());
		fbm.setDiggingTreasureCDNum(0);// 重置减CD次数
		// 挖宝时间通知
		getFactionCallBack().begin_diggingTreasureTimeNotify(baseT.diggingTreasureCdTime);
		this.eventDiggingTreasure.onDiggingTreasure(fbm.getStrongholdId(), forageNum, views);

		addLog(roleRt, "FactionBattleController.log_wb", 1, sceneT.name, forageNum, itemStr);
		return views;
	}

	/**
	 * 发送事件中挖宝通知
	 * 
	 * @param event
	 */
	private void sendEventAwardNotify(FactionBattleEvent event) {
		IFaction faction = roleRt.getFactionControler().getMyFaction();
		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
		String[] items = StringUtils.split(event.getEventItems(), ',');
		AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(items[0]);
		FactionBattleSceneT scenet = XsgFactionBattleManager.getInstance().getBattleSceneT(event.getStrongholdId());
		// 发送公会公告
		String itemStr = XsgChatManager.getInstance().orgnizeItemText("", itemT);
		XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(faction, "Sociaty_Treasure",
				getDutyName(my.getDutyId()), orgnizeRoleText(roleRt), scenet.name, itemStr);
		// 发送系统公告
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("~role_id~", roleRt.getRoleId());
		params.put("~role_name~", roleRt.getName());
		params.put("~role_vip~", roleRt.getVipLevel());
		params.put("~stronghold~", scenet.name);
		params.put("~item~", itemStr);
		XsgChatManager.getInstance().sendAnnouncement(AdContentType.DiggingTreasure, params);
	}

	/**
	 * 计算据点事件额外奖励
	 * 
	 * @param strongholdId
	 * @return
	 */
	private IntString calculationEventAward(int strongholdId) {
		final FactionBattleEvent event = XsgFactionBattleManager.getInstance().getRandomEvent();
		if (event != null && event.getStrongholdId() == strongholdId) {// 当前存在事件并且与玩家实际挖宝的据点一致
			FactionBattleRandomEventT eventT = XsgFactionBattleManager.getInstance().getEventTs()
					.get(event.getEventId());
			if (event.getDrawState() == 0 && TextUtil.isNotBlank(event.getEventItems())
					&& eventT.eventType == EventType.Item) {
				FactionBattleEventRatio fber = XsgFactionBattleManager.getInstance().getEventRatio(strongholdId,
						roleDB.getId());
				int ratio = eventT.diggingtreasureRatio;
				if (fber != null) {
					ratio += eventT.addRatio * fber.getRandomNum();
				}
				int rad = NumberUtil.random(10000);
				if (ratio > rad) {// 掉落成功
					String[] items = StringUtils.split(event.getEventItems(), ',');
					roleRt.getRewardControler().acceptReward(items[0], Integer.parseInt(items[1]));
					// 清除当前据点事件概率数据
					XsgFactionBattleManager.getInstance().clearFactionBattleEventRatio(strongholdId);
					sendEventAwardNotify(event);
					// 设置已挖走状态，并保存数据
					event.setDrawState((byte) 1);
					// 通知客户端刷新
					XsgFactionBattleManager.getInstance().clearFactionBattleEventNotify();
					DBThreads.execute(new Runnable() {
						@Override
						public void run() {
							FactionBattleDAO dao = FactionBattleDAO.getFromApplicationContext(ServerLancher.getAc());
							dao.saveFactionBattleEvent(event);
						}
					});
					return new IntString(Integer.parseInt(items[1]), items[0]);
				} else {
					if (fber == null) {
						fber = new FactionBattleEventRatio();
						fber.setId(GlobalDataManager.getInstance().generatePrimaryKey());
						fber.setRoleId(roleDB.getId());
						fber.setStrongholdId(strongholdId);
					}
					fber.setRandomNum(fber.getRandomNum() + 1);
					XsgFactionBattleManager.getInstance().addEventRatio(fber);
					saveFactionBattleEventRatio(strongholdId);
				}
			}
		}
		return null;
	}

	/**
	 * 计算挖宝TC生成
	 * 
	 * @param dtt
	 * @return
	 */
	private TcResult calculationDiggingTreasureTcAward(FactionBattleDiggingTreasureT dtt) {
		int excuteNum = 0;
		String[] tcs = StringUtils.split(dtt.tcAccount, ",");
		if (tcs.length < 2) {
			excuteNum = Integer.parseInt(tcs[0]);
		} else {
			excuteNum = NumberUtil.randomContain(Integer.parseInt(tcs[0]), Integer.parseInt(tcs[1]));
		}
		TcResult tcResult = XsgRewardManager.getInstance().doTc(roleRt, dtt.tc);
		for (int i = 1; i < excuteNum; i++) {
			tcResult.add(XsgRewardManager.getInstance().doTc(roleRt, dtt.tc));
		}
		return tcResult;
	}

	/**
	 * 增加粮草
	 * 
	 * @param forageNum
	 */
	private void addForage(int forageNum) {
		FactionBattleMember fbm = getFbm(roleRt);
		fbm.setForage(fbm.getForage() + forageNum);
		FactionBattle fb = getFactionBattle(roleRt);
		fb.setForage(fb.getForage() + forageNum);
		// fb.setUpdateTime(new Date());
		IFaction faction = roleRt.getFactionControler().getMyFaction();
		faction.setScore(faction.getScore() + forageNum);

		// 通知界面数据更新
		sendFactionResourceNotify(roleRt);
	}

	/**
	 * 公会战资源更新通知，通知玩家本身
	 * 
	 * @param role
	 */
	private void sendFactionResourceNotify(IRole role) {
		FactionBattleMember fbm = getFbm(role);
		FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
		if (callBack != null) {
			callBack.begin_factionBattleResourceNotify(fbm.getBadge(), fbm.getForage());
		}
	}

	/**
	 * 据点负载系数
	 * 
	 * @param strongHoldId
	 * @return
	 */
	private float getStrongholdRatio(int strongholdId) {
		StrongholdStateT st = getStrongholdStateT(strongholdId);
		if (st != null) {
			return st.diggingTreasureIncome / 100F;
		}
		return 1;// 默认一倍 防止脚本配置错误
	}

	/**
	 * 获得符合要求的据点状态
	 * 
	 * @param strongholdId
	 * @return
	 */
	private StrongholdStateT getStrongholdStateT(int strongholdId) {
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
		int curRoleNum = sh.number(); // 当前据点玩家数据
		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(strongholdId);
		FactionBattleStrongholdT st = XsgFactionBattleManager.getInstance().getBattleStrongholds().get(sceneT.type);

		for (StrongholdStateT sst : st.states) {
			List<String> roleNumStrs = TextUtil.stringToList(sst.stateRoleNum);
			int minRoleNum = Integer.parseInt(roleNumStrs.get(0));
			int maxRoleNum = Integer.parseInt(roleNumStrs.get(1));
			if (curRoleNum >= minRoleNum && curRoleNum <= maxRoleNum) {
				return sst;
			}
		}
		return null;
	}

	/**
	 * 事件系数
	 * 
	 * @return
	 */
	private float getEventRatio() {
		FactionBattleEvent event = XsgFactionBattleManager.getInstance().getRandomEvent();
		if (event != null) {
			FactionBattleRandomEventT eventT = XsgFactionBattleManager.getInstance().getEventTs()
					.get(event.getEventId());
			if (getFbm(roleRt).getStrongholdId() == event.getStrongholdId()
					&& eventT.eventType == EventType.DoubleIncome) {// 双倍收益
				return 2;
			}
		}
		return 1;// 默认值
	}

	/**
	 * 检测锦囊使用
	 * 
	 * @param kitsId
	 * @param kitses
	 * @throws NoteException
	 */
	private void checkUseKits(int kitsId, Map<Integer, FactionBattleKits> kitses) throws NoteException {
		if (kitses == null || kitses.isEmpty()) {// 无任何锦囊
			throw new NoteException(Messages.getString("FactionBattleController.kitsNumNoEnough"));
		}
		FactionBattleKitsT kt = XsgFactionBattleManager.getInstance().getBattleKitsT(kitsId);
		if (kt == null) {// 非法锦囊
			throw new NoteException(Messages.getString("FactionBattleController.kitsNotExist"));
		}
		FactionBattleKits kits = kitses.get(kitsId);
		if (kits == null || kits.getNum() < 1) {// 无当前使用的锦囊
			throw new NoteException(Messages.getString("FactionBattleController.kitsNumNoEnough"));
		}
		if (XsgFactionBattleManager.Kits.Steal == kitsId) {
			if (XsgFactionBattleManager.getInstance().isInitCamp(getFbm(roleRt).getStrongholdId())) {
				throw new NoteException(MessageFormat.format(Messages.getString("FactionBattleController.kitsUseFail"),
						kt.name));
			}
		}
		long surplusTime = kits.getTime() - System.currentTimeMillis();
		if (surplusTime > 0) {// CD时间未到
			throw new NoteException(MessageFormat.format(Messages.getString("FactionBattleController.kitsCdTime"),
					kt.name, surplusTime / 1000));
		}
	}

	@Override
	public String useKits(int kitsId) throws NoteException {
		checkFactionBattleJoin();// 基础检测
		checkFactionBattleMemberRelive();// 复活检测
		Map<Integer, FactionBattleKits> kitses = parseKits();
		checkUseKits(kitsId, kitses);// 检测锦囊使用

		String returnMsg = "";
		KitsChangeView change = new KitsChangeView();
		switch (kitsId) {
		case XsgFactionBattleManager.Kits.Steal:// 神偷
			returnMsg = useKits_Steal(kitses, change);
			break;
		case XsgFactionBattleManager.Kits.Rxql:// 飞驰
			return ""; // 通过行军去实现
		case XsgFactionBattleManager.Kits.LanXiang:// 蓝翔
			useKits_LanXiang();
			break;
		case XsgFactionBattleManager.Kits.ZiYang:// 滋养
			useKits_ZiYang();
			break;
		}
		FactionBattleMember fbm = getFbm(roleRt);
		// 使用锦囊
		useKits(kitses, kitsId, 1);
		fbm.setItems(TextUtil.GSON.toJson(kitses));
		// 锦囊变更通知，通知玩家本身
		KitsChangeView reduce = new KitsChangeView(kitsId, -1);
		KitsChangeView[] changes = null;
		if (kitsId == XsgFactionBattleManager.Kits.Steal) {// 神偷
			if (change.num != 0) {
				changes = new KitsChangeView[] { change };
			}
		}
		changes = (KitsChangeView[]) ArrayUtils.add(changes, reduce);
		getFactionCallBack().begin_kitsChangeNotify(LuaSerializer.serialize(changes));
		// 触发事件
		this.eventUseKits.onUseKits(kitsId);
		return returnMsg;
	}

	/**
	 * 使用锦囊：神偷
	 * 
	 * @param kitses
	 *            使用的锦囊列表
	 * @param change
	 *            增加的锦囊
	 * @return
	 * @throws NoteException
	 */
	private String useKits_Steal(Map<Integer, FactionBattleKits> kitses, KitsChangeView change) throws NoteException {
		FactionBattleMember fbm = getFbm(roleRt);
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(fbm.getStrongholdId());
		List<String> roleList = null;
		if (isOccupy(roleRt, fbm.getStrongholdId())) {// 守方
			roleList = sh.getAttackRoleList();
		} else {
			roleList = sh.getDefendRoleList();
		}
		if (roleList == null || roleList.isEmpty()) {// 无人可偷
			throw new NoteException(Messages.getString("FactionBattleController.kitsUseNotTarget"));
		}

		String targetRoleId = roleList.get(NumberUtil.random(roleList.size()));
		IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetRoleId);

		Map<Integer, FactionBattleKits> targetKitses = parseKits(targetRole);
		List<Integer> targetKitsList = new ArrayList<Integer>();
		if (targetKitses != null) {
			for (FactionBattleKits fbk : targetKitses.values()) {
				if (fbk.getNum() > 0 && fbk.getId() != XsgFactionBattleManager.Kits.Steal) {// 需要排除顺手牵羊
					targetKitsList.add(fbk.getId());
				}
			}
		}
		if (targetKitsList.isEmpty()) {// 无锦囊可偷
			throw new NoteException(Messages.getString("FactionBattleController.kitsUseFail_1001"));
		}

		int kitsId = targetKitsList.get(NumberUtil.random(targetKitsList.size()));
		FactionBattleKitsT kitsT = XsgFactionBattleManager.getInstance().getBattleKitsT(kitsId);
		// 扣除对方的被偷的锦囊
		FactionBattleKits targetKits = targetKitses.get(kitsId);
		targetKits.setNum(targetKits.getNum() - 1);
		FactionBattleMember targetFbm = getFbm(targetRole);
		targetFbm.setItems(TextUtil.GSON.toJson(targetKitses));
		// 给对方发送通知消息
		FactionCallBackPrx t_callBack = targetRole.getFactionBattleController().getFactionCallBack();
		if (t_callBack != null) {
			String message = MessageFormat.format(Messages.getString("FactionBattleController.kitsUseSuccess_1001_1"),
					roleDB.getName(), kitsT.name);
			t_callBack.begin_factionBattleMessageNotify(message);
			// 通知对方锦囊扣除通知
			t_callBack.begin_kitsChangeNotify(LuaSerializer.serialize(new KitsChangeView[] { new KitsChangeView(kitsId,
					-1) }));
		}
		// 使用者获得偷窃的锦囊
		boolean isSuccess = addKits(kitses, kitsId, 1);
		this.eventUseKitsSteal.onSteal(!isSuccess, targetRoleId, kitsId);
		if (!isSuccess) {
			return MessageFormat.format(Messages.getString("FactionBattleController.kitsFull"), kitsT.name);
		}
		change.kitsId = kitsId;
		change.num = 1;
		return MessageFormat.format(Messages.getString("FactionBattleController.kitsUseSuccess_1001"),
				targetRole.getName(), kitsT.name);
	}

	/**
	 * 使用锦囊：蓝翔
	 * 
	 * @throws NoteException
	 */
	private void useKits_LanXiang() throws NoteException {
		FactionBattleMember fbm = getFbm(roleRt);
		if (fbm.getDiggingTreasureEndTime() == null
				|| fbm.getDiggingTreasureEndTime().getTime() < System.currentTimeMillis()) {
			throw new NoteException(Messages.getString("FactionBattleController.kitsUseFail_1003"));
		}
		FactionBattleKitsT kitsT = XsgFactionBattleManager.getInstance().getBattleKitsT(
				XsgFactionBattleManager.Kits.LanXiang);
		long diggingTreasureTime = getDiggingTreasureTime(roleRt);
		diggingTreasureTime -= kitsT.effect;
		if (diggingTreasureTime > 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, (int) (diggingTreasureTime));
			fbm.setDiggingTreasureEndTime(cal.getTime());
		} else {
			fbm.setDiggingTreasureEndTime(new Date());
			diggingTreasureTime = 0;
		}
		// 挖宝时间通知
		getFactionCallBack().begin_diggingTreasureTimeNotify((int) diggingTreasureTime);
	}

	/**
	 * 使用锦囊：滋养
	 * 
	 * @throws NoteException
	 */
	private void useKits_ZiYang() throws NoteException {
		FactionBattleMember fbm = getFbm(roleRt);
		if (fbm.getDeBuffLvl() < 1) {
			throw new NoteException(Messages.getString("FactionBattleController.kitsUseFail_1004"));
		}
		FactionBattleKitsT kitsT = XsgFactionBattleManager.getInstance().getBattleKitsT(
				XsgFactionBattleManager.Kits.ZiYang);
		fbm.setDeBuffLvl(fbm.getDeBuffLvl() - kitsT.effect);
		if (fbm.getDeBuffLvl() < 0) {
			fbm.setDeBuffLvl(0);
		}
		sendStrongholdRoleListNotify(fbm.getStrongholdId());
	}

	/**
	 * 保存角色事件数据
	 * 
	 * @param strongholdId
	 */
	private void saveFactionBattleEventRatio(int strongholdId) {
		final FactionBattleEventRatio fber = XsgFactionBattleManager.getInstance().getEventRatio(strongholdId,
				roleDB.getId());
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FactionBattleDAO dao = FactionBattleDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.saveFactionBattleEventRatio(fber);
			}
		});
	}

	/**
	 * 战斗检测
	 * 
	 * @throws NoteException
	 */
	private void checkBattle() throws NoteException {
		FactionBattleMember fbm = getFbm(roleRt);
		// 检测自身是否在攻击等待时间
		if (fbm.getAttackWaitEndTime() != null && fbm.getAttackWaitEndTime().getTime() >= System.currentTimeMillis()) {
			if (fbm.getKillNum() == 0) {
				long stime = (fbm.getAttackWaitEndTime().getTime() - System.currentTimeMillis()) / 1000;
				stime = stime < 1 ? 1 : stime;
				throw new NoteException(MessageFormat.format(
						Messages.getString("FactionBattleController.battleFail_cd1"), stime));
			} else {
				throw new NoteException(Messages.getString("FactionBattleController.battleFail_cd"));
			}
		}
		// 自身战斗中
		if (isBattleing()) {
			throw new NoteException(Messages.getString("FactionBattleController.battleFail_busy"));
		}
		// 大本营
		if (XsgFactionBattleManager.getInstance().isInitCamp(fbm.getStrongholdId())) {
			throw new NoteException(Messages.getString("FactionBattleController.battleFail_Initial_camp"));
		}
	}

	@Override
	public void startBattle_async(AMD_Faction_startBattle __cb, byte type) throws NoteException {
		String targetRoleId = "";
		FactionBattleRobotEntity robotEntity = null;
		// LogManager.warn(roleRt.getName() +
		// ">>>startBattle_async_1_protocol");
		try {
			checkFactionBattleJoin();// 基础检测
			checkFactionBattleMemberRelive();// 复活检测
			checkBattle();// 战斗检测
			robotEntity = randomTargetRobot();
			if (robotEntity == null) {
				targetRoleId = randomTargetRole(type);
			} else {
				targetRoleId = robotEntity.getRobot().getRobotRoleId();
			}
		} catch (NoteException e) {
			__cb.ice_exception(e);
			return;
		}
		if (robotEntity == null) {// 非机器人
			IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetRoleId);
			FactionBattleActor opponent = new FactionBattleActor(targetRole);
			// 战斗处理
			int strongholdId = getFbm(roleRt).getStrongholdId();
			doFactionBattleGenerateReport(opponent, strongholdId, __cb);
		} else {// 机器人
			loadBattleRobotOpponent(targetRoleId, robotEntity, __cb);
		}
	}

	/**
	 * 加载机器人数据并进行战斗
	 * 
	 * @param targetRoleId
	 * @param robotEntity
	 * @param __cb
	 * @throws NoteException
	 */
	private void loadBattleRobotOpponent(final String targetRoleId, final FactionBattleRobotEntity robotEntity,
			final AMD_Faction_startBattle __cb) throws NoteException {
		XsgRoleManager.getInstance().loadRoleByIdAsync(targetRoleId, new Runnable() {
			@Override
			public void run() {
				IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetRoleId);
				FactionBattleActor opponent = new FactionBattleActor(targetRole, robotEntity);
				// 战斗处理
				int strongholdId = getFbm(roleRt).getStrongholdId();
				doFactionBattleGenerateReport(opponent, strongholdId, __cb);
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("FactionBattleController.battleFail_nobody")));
			}
		});
	}

	/**
	 * 随机挑选一个机器人
	 * 
	 * @return
	 */
	private FactionBattleRobotEntity randomTargetRobot() throws NoteException {
		FactionBattleMember fbm = getFbm(roleRt);
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(fbm.getStrongholdId());
		if (sh.getDefendRobotList().isEmpty()) {
			return null;
		}
		List<FactionBattleRobotEntity> cloneList = new ArrayList<FactionBattleRobotEntity>(sh.getDefendRobotList());
		for (Iterator<FactionBattleRobotEntity> it = cloneList.iterator(); it.hasNext();) {
			FactionBattleRobotEntity entity = it.next();
			if (entity.isBattle()) {// 战中
				it.remove();
			}
			FactionBattleRobot robot = entity.getRobot();
			if (robot.getBeAttackWaitEndTime() != null
					&& robot.getBeAttackWaitEndTime().getTime() >= System.currentTimeMillis()) {// 受击判断
				it.remove();
			}
			// 临时解决机器人打不死的情况
			if (entity.getRobot().getStrongholdId() != fbm.getStrongholdId()) {
				entity.getRobot().setStrongholdId(fbm.getStrongholdId());
			}
		}
		if (cloneList.isEmpty()) {
			throw new NoteException(Messages.getString("FactionBattleController.battleFail_busy1"));
		}
		return cloneList.get(NumberUtil.random(cloneList.size()));
	}

	/**
	 * 随机一个挑战对象
	 * 
	 * @param type
	 *            0守方 1攻方
	 * @return
	 * @throws NoteException
	 */
	private String randomTargetRole(byte type) throws NoteException {
		FactionBattleMember fbm = getFbm(roleRt);
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(fbm.getStrongholdId());
		List<String> targetRoleList = new ArrayList<String>();
		if (type == 0) {// 选择守方
			if (isOccupy(roleRt, fbm.getStrongholdId())) {// 自己本身就是守方
				throw new NoteException(Messages.getString("FactionBattleController.battleFail_sameFaction"));
			}
			targetRoleList.addAll(sh.getDefendRoleList());
		} else if (type == 1) {// 选择攻方
			if (sh.getAttackRoleList().isEmpty()) {
				throw new NoteException(Messages.getString("FactionBattleController.battleFail_nobody"));
			}
			targetRoleList.addAll(sh.getAttackRoleList());
		}

		List<String> targetList = new ArrayList<String>();
		List<String> sameCampList = new ArrayList<String>();
		// 过滤掉不可攻击的角色
		for (Iterator<String> it = targetRoleList.iterator(); it.hasNext();) {
			String roleId = it.next();
			IRole targetRole = XsgRoleManager.getInstance().findRoleById(roleId);
			// 同阵营
			if (isSameCamp(roleRt, targetRole)) {
				sameCampList.add(roleId);
				continue;
			}
			FactionBattleMember targetFbm = getFbm(targetRole);
			// 复活中
			if (targetFbm.getReliveEndTime() != null
					&& targetFbm.getReliveEndTime().getTime() >= System.currentTimeMillis()) {
				continue;
			}
			// 休息中
			if (targetFbm.getBeAttackWaitEndTime() != null
					&& targetFbm.getBeAttackWaitEndTime().getTime() >= System.currentTimeMillis()) {
				continue;
			}
			// 开战中
			if (targetRole.getFactionBattleController().isBattleing()) {
				continue;
			}
			targetList.add(roleId);
		}
		if (sameCampList.size() == targetRoleList.size()) {// 全部为同阵营
			throw new NoteException(Messages.getString("FactionBattleController.battleFail_nobody"));
		}
		if (targetList.isEmpty()) {
			throw new NoteException(Messages.getString("FactionBattleController.battleFail_busy1"));
		}
		return targetList.get(NumberUtil.random(targetList.size()));
	}

	/**
	 * 战斗处理
	 * 
	 * @param opponent
	 *            对手信息
	 * @param strongholdId
	 *            战斗据点
	 * @param __cb
	 *            网络回调
	 * @param isRobot
	 *            是否机器人
	 */
	private void doFactionBattleGenerateReport(final FactionBattleActor opponent, final int strongholdId,
			final AMD_Faction_startBattle __cb) {
		// 设置双方战斗状态
		setBattleing(true);
		opponent.setBattle();
		// 生成战斗对象数据
		final CrossPvpView pvpView = createFactionBattlePvpView(opponent);
		final FactionBattleActor self = new FactionBattleActor(roleRt);
		// 战斗处理
		MovieThreads.execute(new Runnable() {
			@Override
			public void run() {
				final String content = HttpUtil.sendPost(XsgLadderManager.getInstance().movieUrl,
						TextUtil.GSON.toJson(pvpView));
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						final PvpMovieView movieView = TextUtil.GSON.fromJson(content, PvpMovieView.class);
						if (movieView != null) {// 成功
							LogicThread.execute(new Runnable() {// 回调逻辑层
										@Override
										public void run() {
											// 取消战斗状态
											setBattleing(false);
											opponent.cancelBattle();
											// 缓存战斗
											replaceNull(movieView);
											setMovieView(movieView);
											// 设置攻击和受击等待时间、死亡复活时间、计算奖励、据点占领计算、并通知客户端展示结果
											calculationFactionBattleResult(__cb, self, opponent, strongholdId,
													movieView);
										}
									});
						} else {// 失败
							LogicThread.execute(new Runnable() {// 回调逻辑层
										@Override
										public void run() {
											// 取消战斗状态
											setBattleing(false);
											opponent.cancelBattle();
											// 事件触发
											eventResult.onResult(strongholdId, opponent.getActorRoleId(), opponent
													.isRobot(), -1, false, 0, 0, 0, 0, false, getFbm(roleRt)
													.getStrongholdId());
										}
									});
							__cb.ice_exception(new NoteException(Messages
									.getString("FactionBattleController.battleFail_error")));
						}
					}
				});
			}
		});
	}

	/**
	 * 创建战斗对象数据
	 * 
	 * @param opponent
	 *            对手信息
	 * @return
	 */
	private CrossPvpView createFactionBattlePvpView(FactionBattleActor opponent) {
		int type = XsgFightMovieManager.getInstance().getFightLifeT(Type.FactionBattle.ordinal()).id;
		CrossPvpView pvpView = FactionBattleUtil.initCrossPvpView(type, roleRt, opponent.getRole(), opponent.isRobot());
		FactionBattleMember fbm = getFbm(roleRt);
		// 机器人名称处理
		if (opponent.isRobot()) {
			pvpView.rightRoleView.roleName = opponent.getRobotName();
		}
		// DEBUFF计算
		calculationDebuffEffect(pvpView.leftPvpView, fbm.getDeBuffLvl());
		calculationDebuffEffect(pvpView.rightPvpView, opponent.getDeBuffLvl());
		// 战斗场景数据
		pvpView.sceneID = XsgFactionBattleManager.getInstance().getBattleBaseT().sceneId;
		pvpView.sceneName = XsgFactionBattleManager.getInstance().getBattleBaseT().sceneName;
		return pvpView;
	}

	/**
	 * 计算DEBUFF的战斗效果
	 * 
	 * @param view
	 * @param debuffLvl
	 */
	private void calculationDebuffEffect(PvpOpponentFormationView view, int debuffLvl) {
		if (debuffLvl > 0) {
			FactionBattleDebuffT debuffT = XsgFactionBattleManager.getInstance().getDebuffs().get(debuffLvl);
			if (debuffT != null) {
				HeroView[] heros = view.heros;
				for (HeroView hview : heros) {
					GrowableProperty[] properties = hview.properties;
					for (GrowableProperty pr : properties) {
						if (pr.code.equals(Const.PropertyName.Hero.HP)) {// 血量处理
							pr.value = pr.value * (100 - debuffT.debuffValue) / 100;
							pr.grow = pr.grow * (100 - debuffT.debuffValue) / 100;
							break;
						}
					}
				}
				// 战力处理
				if (view.view != null) {
					view.view.battlePower = view.view.battlePower * (100 - debuffT.debuffValue) / 100;
				}
			}
		}
	}

	/**
	 * 计算双方战斗结果状态
	 * 
	 * @param __cb
	 * @param self
	 *            自己
	 * @param opponent
	 *            对手信息
	 * @param strongholdId
	 *            战斗据点
	 * @param movieView
	 *            战斗结果对象
	 */
	private void calculationFactionBattleResult(AMD_Faction_startBattle __cb, FactionBattleActor self,
			FactionBattleActor opponent, int strongholdId, PvpMovieView movieView) {
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		// 发起方设置攻击等待时间
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, baseT.attackWaitTime);
		self.getFbm().setAttackWaitEndTime(cal.getTime());

		// 战斗结果计算
		boolean isOpponentWin = false;
		if (movieView.winRoleId.equals(self.getActorRoleId())) {// 自己胜利了
			calculationFactionBattleResult(__cb, self, opponent, strongholdId, true);
		} else {// 自己失败了
			calculationFactionBattleResult(__cb, self, opponent, strongholdId, false);
			isOpponentWin = true;
		}
		// 接受方设置受击等待时间
		if (!opponent.isRobot() || isOpponentWin) {
			cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, baseT.beAttackWaitTime);
			opponent.setBeAttackWaitEndTime(cal.getTime());
		}
	}

	/**
	 * 战斗最终结果计算，只针对发起着进行胜败结果处理
	 * 
	 * @param __cb
	 *            网络回调
	 * @param self
	 *            自己
	 * @param opponent
	 *            对手信息
	 * @param strongholdId
	 *            战斗据点
	 * @param isWin
	 *            是否胜利
	 */
	private void calculationFactionBattleResult(AMD_Faction_startBattle __cb, FactionBattleActor self,
			FactionBattleActor opponent, int strongholdId, boolean isWin) {
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		FactionBattleMember selfFbm = self.getFbm();
		// 设置败方复活时间，无须考虑是否发起方
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, baseT.reviveTime);
		if (isWin) {// 发起方胜利
			if (!opponent.isRobot()) {// 对手非机器人正常处理
				FactionBattleMember fbm = opponent.getFbm();
				fbm.setReliveEndTime(cal.getTime());
				fbm.setDeaths(fbm.getDeaths() + 1);
				if (fbm.getDeaths() == 1 && opponent.isOnline()) {// 首次死亡通知行军冷却开启
					opponent.getFactionBattleController().getFactionCallBack().begin_openMarchCoolingNotify();
				}
			}
		} else {// 发起方失败
			selfFbm.setReliveEndTime(cal.getTime());
			selfFbm.setDeaths(selfFbm.getDeaths() + 1);
			if (selfFbm.getDeaths() == 1 && self.isOnline()) {// 首次死亡通知行军冷却开启
				self.getFactionBattleController().getFactionCallBack().begin_openMarchCoolingNotify();
			}
		}

		if (isWin) {// 发起方胜利计算奖励，绝对人非机器人
			// 胜方Debuff
			addDebuff(self);
			// 败方清理debuff
			if (!opponent.isRobot()) {
				opponent.getFbm().setDeBuffLvl(0);
			}
			// 击杀数
			addKillNum();
			// 徽章
			int badge = calculationBadge(opponent);
			// 胜方挖宝CD
			int win_cd = calculationWB_CD(self, baseT.winMinusCdTime);
			// 胜方据点占领计算
			boolean isOccupy = calculationStrongholdOccupy(self, opponent, strongholdId);
			// 败方中断击杀，并发送公告
			resetEvenKillNum(self, opponent);
			// 败方获得减挖宝CD
			int fail_cd = calculationWB_CD(opponent, baseT.failMinusCdTime);
			// 记录自方日志
			if (opponent.isRobot()) {
				if (win_cd > 0) {
					addLog(self, "FactionBattleController.log_win_npc", 0, opponent.getActorName(), badge, win_cd);
				} else {
					addLog(self, "FactionBattleController.log_win_npc1", 0, opponent.getActorName(), badge);
				}
			} else {
				if (win_cd > 0) {
					addLog(self, "FactionBattleController.log_win", 0, opponent.getFactionName(),
							opponent.getVipColor(), opponent.getActorName(), badge, win_cd);
				} else {
					addLog(self, "FactionBattleController.log_win1", 0, opponent.getFactionName(),
							opponent.getVipColor(), opponent.getActorName(), badge);
				}
			}
			// 记录对手日志
			addLog(opponent, "FactionBattleController.log_fail2", 0, self.getFactionName(), self.getVipColor(),
					self.getActorName());
			// 发送战斗结果
			sendFactionBattleResult(__cb, opponent, true, badge, win_cd, fail_cd);
			// 锦囊
			int kitsId = 0;
			if (NumberUtil.isHit(baseT.winGetItemChance, 100)) {
				kitsId = gainFactionBattleKits(roleRt, KitsMsgType.Win);
			}
			// 事件触发
			this.eventResult.onResult(strongholdId, opponent.getActorRoleId(), opponent.isRobot(), 200, true, badge,
					win_cd, kitsId, selfFbm.getEvenkillNumm(), isOccupy, selfFbm.getStrongholdId());
		} else {// 发起方失败
			// 胜方Debuff
			addDebuff(opponent);
			// 败方清理debuff
			self.getFbm().setDeBuffLvl(0);
			// 胜方据点占领计算
			calculationStrongholdOccupy(opponent, self, strongholdId);
			// 败方中断击杀，并发送公告
			resetEvenKillNum(opponent, self);
			// 败方获得减挖宝CD
			int fail_cd = calculationWB_CD(self, baseT.failMinusCdTime);
			// 记录失败日志
			if (opponent.isRobot()) {
				if (fail_cd > 0) {
					addLog(self, "FactionBattleController.log_fail_npc", 0, opponent.getActorName(), fail_cd);
				} else {
					addLog(self, "FactionBattleController.log_fail_npc1", 0, opponent.getActorName());
				}
			} else {
				if (fail_cd > 0) {
					addLog(self, "FactionBattleController.log_fail", 0, opponent.getFactionName(),
							opponent.getVipColor(), opponent.getActorName(), fail_cd);
				} else {
					addLog(self, "FactionBattleController.log_fail1", 0, opponent.getFactionName(),
							opponent.getVipColor(), opponent.getActorName());
				}
			}

			addLog(opponent, "FactionBattleController.log_win2", 0, self.getFactionName(), self.getVipColor(),
					roleRt.getName());
			// 发送战斗结果
			sendFactionBattleResult(__cb, opponent, false, 0, 0, fail_cd);
			// 锦囊
			int kitsId = 0;
			if (NumberUtil.isHit(baseT.loseGetItemChance, 100)) {
				kitsId = gainFactionBattleKits(roleRt, KitsMsgType.Revive);
			}
			// 事件触发
			this.eventResult.onResult(strongholdId, opponent.getActorRoleId(), opponent.isRobot(), 200, false, 0,
					fail_cd, kitsId, 0, false, selfFbm.getStrongholdId());
		}
	}

	/**
	 * 记录击杀数
	 */
	private void addKillNum() {
		FactionBattleMember fbm = getFbm(roleRt);
		fbm.setKillNum(fbm.getKillNum() + 1);
		fbm.setEvenkillNumm(fbm.getEvenkillNumm() + 1);
		FactionBattle fb = getFactionBattle(roleRt);
		fb.setKillNum(fb.getKillNum() + 1);
		// fb.setUpdateTime(new Date());

		if (fbm.getEvenkillNumm() % 5 == 0) {// 发起者触发5倍数连杀公告

			int campStrongholdId = fb.getCampStrongholdId();
			FactionBattleSceneT campSceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(campStrongholdId);
			// 系统公告
			Map<String, Object> params = new HashMap<String, Object>();
			IFaction faction = roleRt.getFactionControler().getMyFaction();
			params.put("~force_name_1~x", campSceneT.name);
			params.put("~force_player_1~", orgnizeRoleText(roleRt));
			params.put("~kill_time~", fbm.getEvenkillNumm());
			XsgChatManager.getInstance().sendAnnouncement(AdContentType.GvgSuccessionWin, params);

			// 公会消息
			FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
			XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(faction, "Sociaty_Kill",
					getDutyName(my.getDutyId()), orgnizeRoleText(roleRt), fbm.getEvenkillNumm());
		}
		// 发送N连胜特效
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		if (fbm.getEvenkillNumm() == baseT.evenKillParam) {
			List<String> notifyRoleList = XsgFactionBattleManager.getInstance().getJoinRoleList();
			for (String roleId : notifyRoleList) {
				IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
				if (role != null && role.isOnline()) {
					FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
					if (callBack != null) {
						callBack.begin_factionBattleEvenkillNotify();
					}
				}
			}
		}
	}

	/**
	 * 失败，终结连杀
	 * 
	 * @param winner
	 *            胜利者
	 * @param failer
	 *            失败者
	 */
	private void resetEvenKillNum(FactionBattleActor winner, FactionBattleActor failer) {
		if (failer.isRobot()) {
			return;
		}
		FactionBattleMember l_fbm = failer.getFbm();
		// 系统公告
		if (l_fbm.getEvenkillNumm() > 0 && l_fbm.getEvenkillNumm() >= 5) {
			IFaction l_faction = failer.getRole().getFactionControler().getMyFaction();
			FactionMember l_my = l_faction.getMemberByRoleId(failer.getActorRoleId());
			if (!winner.isRobot()) {// 胜方非机器人
				IFaction w_faction = winner.getRole().getFactionControler().getMyFaction();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("~force_name_1~", winner.getCampName());
				params.put("~force_player_1~", orgnizeRoleText(winner.getRole()));

				params.put("~force_name_2~", failer.getCampName());
				params.put("~force_player_2~", orgnizeRoleText(failer.getRole()));
				params.put("~param_1~", l_fbm.getEvenkillNumm());
				XsgChatManager.getInstance().sendAnnouncement(AdContentType.GvgWinOver, params);
				// 公会消息
				FactionMember w_my = w_faction.getMemberByRoleId(winner.getActorRoleId());
				XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(l_faction, "Sociaty_BeStopKill",
						getDutyName(l_my.getDutyId()), orgnizeRoleText(failer.getRole()), l_fbm.getEvenkillNumm(),
						orgnizeRoleText(winner.getRole()));
				XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(w_faction, "Sociaty_StopKill",
						getDutyName(w_my.getDutyId()), orgnizeRoleText(winner.getRole()),
						orgnizeRoleText(failer.getRole()), l_fbm.getEvenkillNumm());
			} else {// 胜方机器人
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("~npc_player_1~", winner.getActorName());

				params.put("~force_name_2~", failer.getCampName());
				params.put("~force_player_2~", orgnizeRoleText(failer.getRole()));
				params.put("~param_1~", l_fbm.getEvenkillNumm());
				XsgChatManager.getInstance().sendAnnouncement(AdContentType.FactionBattleWinOver, params);
				// 公会消息
				XsgFactionBattleManager.getInstance().sendFactionBattleFactionMessage(l_faction,
						"Sociaty_BeStopKillNPC", getDutyName(l_my.getDutyId()), orgnizeRoleText(failer.getRole()),
						l_fbm.getEvenkillNumm(), winner.getActorName());
			}
		}
		l_fbm.setEvenkillNumm(0);
	}

	/**
	 * 计算发起方获取的徽章数量
	 * 
	 * @param opponent
	 *            对手信息
	 * @return
	 */
	private int calculationBadge(FactionBattleActor opponent) {
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		int badge = baseT.badge;
		// 连杀徽章加成
		FactionBattleMember fbm = getFbm(roleRt);
		int evenKill = fbm.getEvenkillNumm();
		if (evenKill > XsgFactionBattleManager.getInstance().getMaxEvenKillNum()) {
			evenKill = XsgFactionBattleManager.getInstance().getMaxEvenKillNum();
		}
		FactionBattleAddAwardT addT = XsgFactionBattleManager.getInstance().getBattleAddAwards().get(evenKill);
		if (addT != null) {
			badge += addT.addBadge;
		}
		// 中断对方连杀加成
		if (!opponent.isRobot()) {// 非机器人
			fbm = getFbm(opponent.getRole());
			evenKill = fbm.getEvenkillNumm();
			if (evenKill > XsgFactionBattleManager.getInstance().getMaxEvenKillNum()) {
				evenKill = XsgFactionBattleManager.getInstance().getMaxEvenKillNum();
			}
			addT = XsgFactionBattleManager.getInstance().getBattleAddAwards().get(evenKill);
			if (addT != null) {
				badge += addT.killAddBadge;
			}
		}
		if (badge > 15) {// 硬性代码，防止所谓的产出过高
			badge = 15;
		}
		// 添加到公会战数据并刷现排行榜
		addBadgeRefreshRank(roleRt, badge);
		// 放入当事人背包
		roleRt.getRewardControler().acceptReward(XsgFactionBattleManager.BADGE, badge);
		return badge;
	}

	/**
	 * 刷新公会战徽章数据和排行数据
	 * 
	 * @param role
	 * @param badge
	 */
	private void addBadgeRefreshRank(IRole role, int badge) {
		if (badge < 1) {
			return;
		}
		FactionBattleMember fbm = getFbm(role);
		fbm.setBadge(fbm.getBadge() + badge);
		fbm.setUpdateTime(new Date());

		FactionBattle fb = getFactionBattle(role);
		fb.setBadge(fb.getBadge() + badge);
		fb.setUpdateTime(new Date());

		// 刷新排行数据
		XsgFactionBattleManager.getInstance().setRankList(fb);
		// 刷新个人排行数据
		XsgFactionBattleManager.getInstance().setPersonalRankList(fbm);
		// 通知客户端刷新
		sendFactionResourceNotify(role);
	}

	/**
	 * 增加BUFF
	 * 
	 * @param actor
	 *            参战者
	 */
	private void addDebuff(FactionBattleActor actor) {
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		if (actor.getDeBuffLvl() >= baseT.debuffMaxLvl) {
			return;
		}
		actor.addDebuffLvl(1);
	}

	/**
	 * 计算挖宝CD
	 * 
	 * @param actor
	 *            参战者
	 * @return
	 */
	private int calculationWB_CD(FactionBattleActor actor, int minusCd) {
		if (actor.isRobot()) {// 机器人无挖宝
			return 0;
		}
		FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
		FactionBattleMember fbm = actor.getFbm();
		// 记录减挖宝CD次数
		fbm.setDiggingTreasureCDNum(fbm.getDiggingTreasureCDNum() + 1);
		if (fbm.getDiggingTreasureCDNum() >= (baseT.maxMinusCd + 1)) {
			return 0;
		}
		// 当前挖宝CD剩余时间
		long diggingTreasureTime = getDiggingTreasureTime(actor.getRole());
		if (diggingTreasureTime < minusCd) {
			minusCd = (int) diggingTreasureTime;
		}
		diggingTreasureTime -= minusCd;
		if (diggingTreasureTime > 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, (int) (diggingTreasureTime));
			fbm.setDiggingTreasureEndTime(cal.getTime());
		} else {
			fbm.setDiggingTreasureEndTime(new Date());
			diggingTreasureTime = 0;
		}
		if (actor.isOnline()) {// 在线进行挖宝时间通知
			actor.getFactionBattleController().getFactionCallBack()
					.begin_diggingTreasureTimeNotify((int) diggingTreasureTime);
		}
		return minusCd;
	}

	/**
	 * 计算战后据点占领情况
	 * 
	 * @param winner
	 *            胜方
	 * @param loser
	 *            败方
	 * @param strongholdId
	 *            战斗据点
	 * @return
	 */
	private boolean calculationStrongholdOccupy(FactionBattleActor winner, FactionBattleActor loser, int strongholdId) {
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
		// 发生状态改变的据点
		int[] changeStrongholdIds = null;
		// 据点角色列表数据改变
		int[] changes = null;
		boolean isOccupy = false;
		if (loser.isRobot() || isOccupy(loser.getRole(), strongholdId)) {// 失败方为当前占领方，需要重新计算是否继续占领
			if (loser.isRobot()) {// 机器人销毁
				loser.clearRobot();
			} else {
				sh.removeDefendRole(loser.getActorRoleId());
			}
			if (sh.isCanOccupy()) {// 守方无人
				if (sh.getAttackRoleList().isEmpty()) {// 攻方无人，重置为无人状态
					sh.resetOccupy();
				} else {
					if (roleRt.getRoleId().equals(winner.getActorRoleId())) {// 胜方为发起者则直接占领
						if (winner.getFbm().getStrongholdId() != strongholdId) {// 当前胜方已离开战斗据点
							String roleId = sh.getAttackRoleList().get(0);
							IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
							occupyStronghold(role, sh);
						} else {
							occupyStronghold(winner.getRole(), sh);
						}
					} else {// 胜方为被攻击方，则为攻方第一人占领非胜方占领
						String roleId = sh.getAttackRoleList().get(0);
						IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
						occupyStronghold(role, sh);
					}
				}
				changeStrongholdIds = ArrayUtils.add(changeStrongholdIds, strongholdId);
				if (!loser.isRobot()) {
					sendStrongholdLoseMessage(loser.getRole());
				}
				isOccupy = true;
			}
		} else {// 失败者为攻击方，只做移除
			sh.removeAttackRole(loser.getActorRoleId());
		}
		changes = ArrayUtils.add(changes, strongholdId);
		// LogManager.warn(winner.getName() + "calculationStrongholdOccupy>>" +
		// sh.toString());
		// 失败方回到自己公会所在大本营
		if (!loser.isRobot()) {
			FactionBattle fb = getFactionBattle(loser.getRole());
			enterStronghold(loser.getRole(), fb.getCampStrongholdId());
		}
		// 通知据点改变
		if (changeStrongholdIds != null) {
			sendStrongholdStateNotify(changeStrongholdIds);
		}
		// 据点角色列表通知
		if (changes != null) {
			sendStrongholdRoleListNotify(changes);
		}
		return isOccupy;
	}

	/**
	 * 发送战斗结果数据
	 * 
	 * @param __cb
	 *            网络协议回调
	 * @param opponent
	 *            对手
	 * @param isWin
	 *            发起方是否胜利
	 * @param badge
	 *            发起方获得徽章
	 * @param win_cd
	 *            胜方挖宝CD
	 * @param fail_cd
	 *            败方挖宝CD
	 */
	private void sendFactionBattleResult(AMD_Faction_startBattle __cb, FactionBattleActor opponent, boolean isWin,
			int badge, int win_cd, int fail_cd) {
		if (roleRt.isOnline()) {// 发起者不在线，不进行处理
			FactionBattleResultView result = new FactionBattleResultView();
			FactionBattleMember fbm = getFbm(roleRt);
			result.isWin = isWin;
			if (isWin) {
				result.content = opponent.isRobot() ? getMessageT("Sociaty_YouWinNPC").content
						: getMessageT("Sociaty_YouWin").content;
				result.killNum = fbm.getEvenkillNumm();
				result.cd = win_cd;
				result.badge = badge;
				FactionBattleDebuffT t = XsgFactionBattleManager.getInstance().getDebuffs().get(fbm.getDeBuffLvl());
				result.debuffDesc = t.debuffDesc;
			} else {
				result.content = opponent.isRobot() ? getMessageT("Sociaty_YouLoseNPC").content
						: getMessageT("Sociaty_YouLose").content;
				result.cd = fail_cd;
				result.strongholdId = fbm.getStrongholdId();
				FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
				result.recoveryTime = baseT.reviveTime;
			}
			if (opponent.isRobot()) {
				result.content = MessageFormat.format(result.content, opponent.getActorName());
			} else {
				result.content = MessageFormat.format(result.content, opponent.getCampName(), opponent.getActorName());
			}
			__cb.ice_response(LuaSerializer.serialize(result));// 发起方 协议返回
		}
		if (!opponent.isRobot() && opponent.isOnline()) {// 目标方
			FactionCallBackPrx callBack = opponent.getFactionBattleController().getFactionCallBack();
			if (!isWin) {// 发起方失败
				FactionBattle fb = getFactionBattle(roleRt);
				FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(
						fb.getCampStrongholdId());
				callBack.begin_factionBattleMessageNotify(MessageFormat.format(
						getMessageT("Sociaty_BeattackWin").content, sceneT.name, roleRt.getName(), opponent.getFbm()
								.getDeBuffLvl()));
			} else {
				FactionBattleResultView result = new FactionBattleResultView();
				result.content = getMessageT("Sociaty_YouLose").content;
				result.cd = fail_cd;
				FactionBattleBaseT baseT = XsgFactionBattleManager.getInstance().getBattleBaseT();
				result.recoveryTime = baseT.reviveTime;
				result.strongholdId = opponent.getFbm().getStrongholdId();

				IFaction faction = roleRt.getFactionControler().getMyFaction();
				result.content = MessageFormat.format(result.content, faction.getName(), roleRt.getName());
				callBack.begin_factionBattleResultNotify(LuaSerializer.serialize(result));
				opponent.getFactionBattleController().setMovieView(movieView);
			}
		}
	}

	@Override
	public void resultConfirm() throws NoteException {
		checkFactionBattleMemberRelive();
		this.movieView = null;
	}

	@Override
	public boolean isBattleing() {
		return isBattleing;
	}

	@Override
	public void setBattleing(boolean isBattleing) {
		this.isBattleing = isBattleing;
	}

	@Override
	public void setMovieView(PvpMovieView movieView) {
		this.movieView = movieView;
	}

	@Override
	public PvpMovieView lookFactionBattleMovieView() throws NoteException {
		if (movieView == null) {
			throw new NoteException(Messages.getString("FactionBattleController.movieViewinvalid"));
		}
		return movieView;
	}

	@Override
	public FactionBattleLogView[] lookFactionBattleLog(byte logType) throws NoteException {
		List<FactionBattleLog> logs = XsgFactionBattleManager.getInstance().getFactionLogs(roleRt.getRoleId());
		FactionBattleLogView[] views = new FactionBattleLogView[0];
		if (logs != null) {
			for (int i = logs.size() - 1; i >= 0; i--) {
				FactionBattleLog log = logs.get(i);
				if (log.getType() == logType) {
					String timeStr = DateUtil.format(log.getTime(), DateUtil.HHMM_PATTERN);
					String content = MessageFormat.format(Messages.getString(log.getLogKey()),
							TextUtil.GSON.fromJson(log.getLogValue(), Object[].class));
					views = (FactionBattleLogView[]) ArrayUtils.add(views, new FactionBattleLogView(timeStr, content));
				}
			}
		}
		return views;
	}

	/**
	 * 公会配置消息
	 * 
	 * @param msgId
	 * @return
	 */
	private FactionBattleMessageT getMessageT(String msgId) {
		return XsgFactionBattleManager.getInstance().getBattleMessages().get(msgId);
	}

	/**
	 * 组织玩家格式文本
	 * 
	 * @param rt
	 * @return
	 */
	private String orgnizeRoleText(IRole rt) {
		return MessageFormat.format("{0}|{1}|{2}", rt.getRoleId(), rt.getName(), rt.getVipLevel());
	}

	/**
	 * 增加公会战日志
	 * 
	 * @param actor
	 *            参战者
	 * @param log_key
	 *            日志KEY
	 * @param type
	 *            日志类型
	 * @param params
	 *            日志参数
	 */
	private void addLog(FactionBattleActor actor, String log_key, int type, Object... params) {
		if (actor.isRobot()) {
			return;
		}
		addLog(actor.getRole(), log_key, type, params);
	}

	/**
	 * 增加公会战日志
	 * 
	 * @param role
	 *            参战者
	 * @param log_key
	 *            日志KEY
	 * @param type
	 *            日志类型
	 * @param params
	 *            日志参数
	 */
	private void addLog(IRole role, String log_key, int type, Object... params) {
		final FactionBattleLog fblog = new FactionBattleLog();
		fblog.setLogKey(log_key);
		fblog.setType((byte) type);
		fblog.setTime(new Date());
		fblog.setLogValue(TextUtil.GSON.toJson(params));
		fblog.setRole_id(role.getRoleId());
		fblog.setId(GlobalDataManager.getInstance().generatePrimaryKey());

		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FactionBattleDAO dao = FactionBattleDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.saveFactionBattleLog(fblog);
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						XsgFactionBattleManager.getInstance().addFactionBattleLogs(fblog);
					}
				});
			}
		});
	}

	/**
	 * 去除战报里面的NULL对象
	 * 
	 * @param movieView
	 */
	private void replaceNull(PvpMovieView movieView) {
		try {
			if (movieView.soloMovie.length > 0 && movieView.soloMovie[0].reports.length > 0) {
				for (ActionN a : movieView.soloMovie[0].reports[0].soloReport[0].actions) {
					if (a.damages != null) {
						if (a.damages[0].buffs == null) {
							a.damages[0].buffs = new DuelBuffView[0];
						} else {
							if (a.damages[0].buffs[0] == null) {
								a.damages[0].buffs = new DuelBuffView[0];
							}
						}
					} else {
						a.damages = new DamageN[0];
					}
					if (a.effects == null) {
						a.effects = new EffectN[0];
					}
				}
				movieView.soloMovie[0].reports[0].result = DuelResult.DuelResultFail;
				if (movieView.soloMovie[0].reports[0].firsts.length > 0) {
					movieView.soloMovie[0].reports[0].firsts[0].type = DuelTemplateType.DuelTemplateTypeHero;
				}
				if (movieView.soloMovie[0].reports[0].seconds.length > 0) {
					movieView.soloMovie[0].reports[0].seconds[0].type = DuelTemplateType.DuelTemplateTypeHero;
				}
			}
		} catch (Exception e) {
			LogManager.error(e);
			movieView.soloMovie = new SceneDuelView[0];
		}
	}

	@Override
	public void onRoleOffline(long onlineInterval) {
		if (!roleRt.getFactionControler().isInFaction()) {// 无公会
			return;
		}
		if (!XsgFactionBattleManager.getInstance().isEnterFactionBattle(roleRt)) {// 未参战
			return;
		}
		// leaveBattle();
		// 设置非战斗状态
		isBattleing = false;
		// 清理战报数据
		movieView = null;
	}
}
