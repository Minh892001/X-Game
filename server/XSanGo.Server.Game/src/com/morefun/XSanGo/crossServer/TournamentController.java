package com.morefun.XSanGo.crossServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.LocalException;
import Ice.UserException;

import com.XSanGo.Protocol.AMD_Tournament_beginFightWith;
import com.XSanGo.Protocol.AMD_Tournament_bet;
import com.XSanGo.Protocol.AMD_Tournament_enterPVPView;
import com.XSanGo.Protocol.AMD_Tournament_enterTournament;
import com.XSanGo.Protocol.AMD_Tournament_fightWith;
import com.XSanGo.Protocol.AMD_Tournament_getBetView;
import com.XSanGo.Protocol.AMD_Tournament_getFightMovieByRecordId;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutMovie;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutMovieList;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutView;
import com.XSanGo.Protocol.AMD_Tournament_getRankList;
import com.XSanGo.Protocol.AMD_Tournament_openTournamentView;
import com.XSanGo.Protocol.AMD_Tournament_refreshPVPView;
import com.XSanGo.Protocol.AMD_Tournament_setupFormation;
import com.XSanGo.Protocol.AMD_Tournament_signup;
import com.XSanGo.Protocol.BetView;
import com.XSanGo.Protocol.Callback_CrossServerCallback_apply;
import com.XSanGo.Protocol.Callback_CrossServerCallback_crossBet;
import com.XSanGo.Protocol.Callback_CrossServerCallback_endChallenge;
import com.XSanGo.Protocol.Callback_CrossServerCallback_getCrossRank;
import com.XSanGo.Protocol.Callback_CrossServerCallback_getMyRankScore;
import com.XSanGo.Protocol.Callback_CrossServerCallback_getRoleFormationView;
import com.XSanGo.Protocol.Callback_CrossServerCallback_getSchedule;
import com.XSanGo.Protocol.Callback_CrossServerCallback_getScheduleMovieData;
import com.XSanGo.Protocol.Callback_CrossServerCallback_getScheduleMovieList;
import com.XSanGo.Protocol.Callback_CrossServerCallback_isApply;
import com.XSanGo.Protocol.Callback_CrossServerCallback_isOut;
import com.XSanGo.Protocol.Callback_CrossServerCallback_matchRival;
import com.XSanGo.Protocol.Callback_CrossServerCallback_refreshRival;
import com.XSanGo.Protocol.Callback_CrossServerCallback_saveBattle;
import com.XSanGo.Protocol.CrossMovieView;
import com.XSanGo.Protocol.CrossPvpView;
import com.XSanGo.Protocol.CrossRankView;
import com.XSanGo.Protocol.CrossRivalView;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.CrossScheduleView;
import com.XSanGo.Protocol.CrossServerCallbackPrx;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.FightFormations;
import com.XSanGo.Protocol.FightMovieByteView;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.FightRecordItemView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.KnockoutView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.MyFormationView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PVPInfoView;
import com.XSanGo.Protocol.PreSignupView;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.ShopBuyResultView;
import com.XSanGo.Protocol.SignupResultView;
import com.XSanGo.Protocol.SignupView;
import com.XSanGo.Protocol.StageIndex;
import com.XSanGo.Protocol.TournamentShopItemView;
import com.XSanGo.Protocol.TournamentShopView;
import com.XSanGo.Protocol.TournamentStatusView;
import com.XSanGo.Protocol.TournamentView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.MovieThreads;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleTournament;
import com.morefun.XSanGo.db.game.RoleTournamentBet;
import com.morefun.XSanGo.db.game.RoleTournamentRecord;
import com.morefun.XSanGo.event.protocol.ITournamentBeginFight;
import com.morefun.XSanGo.event.protocol.ITournamentBet;
import com.morefun.XSanGo.event.protocol.ITournamentBuyRefreshCount;
import com.morefun.XSanGo.event.protocol.ITournamentFight;
import com.morefun.XSanGo.fightmovie.FightLifeNumT;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.FindMovieCallback;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.formation.IFormationControler;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

/**
 * 比武大会
 */
@RedPoint(isTimer = true)
public class TournamentController implements ITournamentController {
	private final static Log logger = LogFactory.getLog(TournamentController.class);

	private IRole role;
	private Role db;
	private RoleTournament tournament;

	private Map<String, CrossRivalView> opponentIdMap = new HashMap<String, CrossRivalView>();
	private String fightMovieIdContext;

	private ITournamentBuyRefreshCount buyRefreshCount;
	private ITournamentFight fightEvent;
	private ITournamentBeginFight beginFight;
	private ITournamentBet betEvent;

	private long lastFightTime;

	public TournamentController(IRole r, Role db) {
		role = r;
		this.db = db;

		this.tournament = db.getTournament();
		if (tournament == null) {
			init();
		}

		buyRefreshCount = role.getEventControler().registerEvent(ITournamentBuyRefreshCount.class);
		fightEvent = role.getEventControler().registerEvent(ITournamentFight.class);
		beginFight = role.getEventControler().registerEvent(ITournamentBeginFight.class);
		betEvent = role.getEventControler().registerEvent(ITournamentBet.class);

		checkReset(XsgTournamentManager.getInstance().getConfig());
	}

	private void init() {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		String myFormationViewStr = TextUtil.GSON.toJson(getMyCurrentFromation());
		int stageIndex = 0;
		Date signupStartTime = new Date();
		if (config != null) {
			stageIndex = config.stageIndex;
			signupStartTime = config.signupStartTime;
		}

		tournament = new RoleTournament(role.getRoleId(), db, stageIndex, 0, signupStartTime, 0, 0, myFormationViewStr,
				0, 0, null, null, null);

		saveToRole();
	}

	public MyFormationView getMyCurrentFromation() {
		IFormationControler controler = role.getFormationControler();
		IFormation myFormation = controler.getDefaultFormation();
		PvpOpponentFormationView opponentFormationView = controler.getPvpOpponentFormationView(myFormation.getId());
		MyFormationView formation = new MyFormationView(myFormation.getView(), opponentFormationView);
		return formation;
	}

	/**
	 * 重新开始一个赛季
	 */
	private void restart(TournamentConfigT config) {
		dailyReset(config);
		tournament.setNum(config.stageIndex);
		tournament.setHasSignup(0);
		tournament.setStartDate(config.signupStartTime);
		tournament.setShopBuyCount(null);
		saveToRole();
	}

	/**
	 * 重置每日次数
	 */
	private void dailyReset(TournamentConfigT config) {
		tournament.setFightCount(0);
		tournament.setRefreshCount(0);
		tournament.setWinNum(0);
		tournament.setBuyFightCount(0);
		tournament.setBuyRefreshCount(0);
		tournament.setLastResetDate(Calendar.getInstance().getTime());
		saveToRole();
	}

	private void clearFightRecords() {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		List<RoleTournamentRecord> list = db.getTournamentFightRecords();
		if (list != null) {
			Date current = Calendar.getInstance().getTime();
			List<RoleTournamentRecord> removeList = new ArrayList<RoleTournamentRecord>();
			for (RoleTournamentRecord record : list) {
				// 移除30天前的战报
				if (DateUtil.diffDate(current, record.getCreateDate()) > 30) {
					removeList.add(record);
				}
			}
			db.getTournamentFightRecords().removeAll(removeList);
		}
		list = db.getTournamentFightRecords();
		if (list != null && list.size() > config.figthRecordCount) {
			Collections.sort(list, new Comparator<RoleTournamentRecord>() {
				@Override
				public int compare(RoleTournamentRecord o1, RoleTournamentRecord o2) {
					return new Long(o2.getCreateDate().getTime()).compareTo(o1.getCreateDate().getTime());
				}
			});
			List<RoleTournamentRecord> removeList = list.subList(config.figthRecordCount, list.size());
			db.getTournamentFightRecords().removeAll(removeList);
			saveToRole();
		}
	}

	private int getShopBuyCount(String id) {
		if (TextUtil.isBlank(tournament.getShopBuyCount())) {
			return 0;
		}
		IntString[] list = TextUtil.GSON.fromJson(tournament.getShopBuyCount(), IntString[].class);
		for (IntString is : list) {
			if (is.strValue.equals(id)) {
				return is.intValue;
			}
		}
		return 0;
	}

	private void addShopBuyCount(String id, int num) {
		List<IntString> list = new ArrayList<IntString>();
		if (TextUtil.isNotBlank(tournament.getShopBuyCount())) {
			list = new ArrayList<IntString>(Arrays.asList(TextUtil.GSON.fromJson(tournament.getShopBuyCount(),
					IntString[].class)));
		}
		IntString pair = null;
		for (IntString is : list) {
			if (is.strValue.equals(id)) {
				pair = is;
				break;
			}
		}
		if (pair == null) {
			pair = new IntString(0, id);
			list.add(pair);
		}
		pair.intValue = pair.intValue + num;
		tournament.setShopBuyCount(TextUtil.GSON.toJson(list.toArray(new IntString[0])));
		saveToRole();
	}

	/**
	 * 检查是否需要重置
	 */
	private void checkReset(TournamentConfigT config) {
		if (config != null) {
			// 是否是新赛季
			if (config.stageIndex != tournament.getNum()) {
				restart(config);
			} else if (tournament.getLastResetDate() == null
					|| DateUtil.isPass(DateUtil.joinTime(config.resetTime), tournament.getLastResetDate())) { // 每日重置
				dailyReset(config);
			}
		}
	}

	private Date currentRemoteDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(XsgCrossServerManager.getInstance().currentRemoteTime());
		return cal.getTime();
	}

	private void saveToRole() {
		tournament.setUpdateDate(Calendar.getInstance().getTime());
		db.setTournament(tournament);
	}

	/** 报名开始 */
	private boolean isSignupTime(TournamentConfigT config) throws NoteException {
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		if (config.signupStartTime.getTime() <= current && current <= config.qtEndDate.getTime()) {
			return true;
		}
		return false;
	}

	/**
	 * 是否在报名时间之前
	 */
	private boolean isBeforeSignup(TournamentConfigT config) {
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		if (current < config.signupStartTime.getTime()) {
			return true;
		}
		return false;
	}

	/**
	 * 是否在报名时间之后
	 */
	private boolean isAfterSignup(TournamentConfigT config) {
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		if (current > config.qtEndDate.getTime()) {
			return true;
		}
		return false;
	}

	private StageWarp getCurrentStageIndex(TournamentConfigT config) {
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		long signupStart = config.signupStartTime.getTime();
		long signupEnd = config.signupEndTime.getTime();
		long qtEnd = config.qtEndDate.getTime();
		long s32Start = config.s32Start.getTime();
		long s32End = config.s32End.getTime();
		long s16End = config.s16End.getTime();
		long s8End = config.s8End.getTime();
		long s2End = config.shalfEnd.getTime();
		long s1End = config.sfinalEnd.getTime();
		XsgTournamentManager manager = XsgTournamentManager.getInstance();
		StageIndex stageIndex = StageIndex.NotStart;
		String nextStageStartTime = DateUtil.format(config.signupStartTime);
		String nextStageDesc = Messages.getString("TournamentControler.signup");
		do {
			// 还没开始
			if (current < signupStart) {
				break;
			}
			// 报名阶段
			if (signupStart <= current && current <= signupEnd) {
				stageIndex = StageIndex.SignUp;
				nextStageStartTime = DateUtil.format(config.qtStartDate, "yyyy-MM-dd HH:mm");
				nextStageDesc = manager.getStageIndexDesc(StageIndex.QT);
				break;
			}
			// 资格赛, 资格赛和淘汰赛之间可能有时间间隔，玩家在这段时间仍然可以进入匹配界面，把淘汰赛开始的时间作为淘汰赛结束的时间
			if (current <= s32Start) {
				stageIndex = StageIndex.QT;
				nextStageStartTime = DateUtil.format(config.s32Start, "yyyy-MM-dd HH:mm");
				nextStageDesc = manager.getStageIndexDesc(StageIndex.S32);
				break;
			}
			// 32强
			if (current <= s32End) {
				stageIndex = StageIndex.S32;
				nextStageStartTime = DateUtil.format(config.s16Start, "yyyy-MM-dd HH:mm");
				nextStageDesc = manager.getStageIndexDesc(StageIndex.S16);
				break;
			}
			// 16强
			if (current <= s16End) {
				stageIndex = StageIndex.S16;
				nextStageStartTime = DateUtil.format(config.s8Start, "yyyy-MM-dd HH:mm");
				nextStageDesc = manager.getStageIndexDesc(StageIndex.S8);
				break;
			}
			// 8强
			if (current <= s8End) {
				stageIndex = StageIndex.S8;
				nextStageStartTime = DateUtil.format(config.shalfStart, "yyyy-MM-dd HH:mm");
				nextStageDesc = manager.getStageIndexDesc(StageIndex.S4);
				break;
			}
			// 半决赛
			if (current <= s2End) {
				stageIndex = StageIndex.S4;
				nextStageStartTime = DateUtil.format(config.sfinalStart, "yyyy-MM-dd HH:mm");
				nextStageDesc = manager.getStageIndexDesc(StageIndex.S2);
				break;
			}
			// 决赛
			if (current <= s1End) {
				stageIndex = StageIndex.S2;
				nextStageStartTime = DateUtil.format(config.sfinalEnd, "yyyy-MM-dd HH:mm");
				nextStageDesc = Messages.getString("TournamentControler.champion");
				break;
			}

			stageIndex = StageIndex.End;
		} while (false);
		StageWarp warp = new StageWarp(stageIndex, XsgTournamentManager.getInstance().getStageIndexDesc(stageIndex),
				nextStageStartTime, nextStageDesc);
		return warp;
	}

	private TournamentView getTournamentView(TournamentConfigT config) {
		final int Minute = 60 * 1000;
		StageWarp stage = getCurrentStageIndex(config);
		int totalDays = (int) (DateUtil.compareTime(config.sfinalEnd, config.qtStartDate) / Minute);
		int lastDays = (int) (DateUtil.compareTime(config.sfinalEnd, currentRemoteDate()) / Minute);
		lastDays = Math.min(lastDays, totalDays);
		int qtDays = (int) (DateUtil.compareTime(config.s32Start, config.qtStartDate) / Minute);
		int s32Days = (int) (DateUtil.compareTime(config.s16Start, config.s32Start) / Minute);
		int s16Days = (int) (DateUtil.compareTime(config.s8Start, config.s16Start) / Minute);
		int s8Days = (int) (DateUtil.compareTime(config.shalfStart, config.s8Start) / Minute);
		int s2Days = (int) (DateUtil.compareTime(config.sfinalStart, config.shalfStart) / Minute);
		int s1Days = (int) (DateUtil.compareTime(config.sfinalEnd, config.sfinalStart) / Minute);
		return new TournamentView(config.stageIndex, stage.currentStage, stage.stageDesc, totalDays, lastDays,
				stage.nextStageDate, stage.nextStageDesc, qtDays, s32Days, s16Days, s8Days, s2Days, s1Days,
				canSetupFormation(config), isQTEnd(), false);
	}

	private void openView(SignupView view, TournamentConfigT config) throws NoteException {
		TournamentView tournamentView = getTournamentView(config);
		view.mainView = tournamentView;
		view.currentStageDesc = XsgTournamentManager.getInstance().getStageIndexDesc(StageIndex.QT); // 固定显示资格赛
	}

	/**
	 * 整个赛季是否已经结束
	 */
	private boolean tournamentOver(TournamentConfigT config) {
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		if (current > config.sfinalEnd.getTime()) {
			return true;
		}
		return false;
	}

	@Override
	public void openSignup(final AMD_Tournament_enterTournament __cb) {
		final int Tournament_BeforeSignup = 0; // 报名时间未到
		final int Tournament_CanSignup = 1; // 可以报名
		final int Tournament_AfterSignup = 2; // 报名时间已过
		final int Tournament_NotBegin = 0; // 比武大会未开启
		final int Tournament_Begin = 1; // 比武大会正常开启
		final int Tournament_Over = 2; // 比武大会已结束

		final TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		final SignupView fview = new SignupView();
		if (config == null || config.isOpen == 0 || !XsgCrossServerManager.getInstance().isUsable()) {// 比武大会未开启
			fview.num = (config == null ? 0 : config.stageIndex);
			fview.isOpen = Tournament_NotBegin;
			fview.showDesc = Messages.getString("TournamentControler.nextPrepare");
			__cb.ice_response(LuaSerializer.serialize(fview));
			return;
		}
		checkReset(config);
		clearFightRecords();
		// 比武大会开启
		final CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_isApply(role.getRoleId(), new Callback_CrossServerCallback_isApply() {

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("openSignup-isApply", __ex);
			}

			@Override
			public void response(boolean __ret) {
				try {
					// 同步报名状态
					tournament.setHasSignup(__ret ? 1 : 0);
					SignupView view = fview;
					// 比武大会已经结束
					if (tournamentOver(config) && !isBeforeSignup(config) && !isSignupTime(config)) {
						view.num = config.stageIndex;
						view.isOpen = Tournament_Over;
						view.showDesc = Messages.getString("TournamentControler.over");
					} else { // 尚未结束
						String startTimeStr = DateUtil.format(config.signupStartTime, "yyyy-MM-dd HH:mm");
						String fightTimeStr = DateUtil.format(config.qtStartDate, "MM-dd HH:mm");
						view = new SignupView(config.stageIndex, "", startTimeStr, fightTimeStr, Tournament_Begin,
								tournament.getHasSignup(), Tournament_CanSignup, "", null);
					}

					openView(view, config);

					// 没有报名，但是不在报名时间内（未到 或者 已过）
					if (tournament.getHasSignup() == 0) {
						if (isBeforeSignup(config)) { // 报名未开始
							view.canSignup = Tournament_BeforeSignup;
							view.showDesc = Messages.getString("TournamentControler.signupNotStart");
						}
						if (isAfterSignup(config)) { // 报名时间已过
							view.canSignup = Tournament_AfterSignup;
							view.showDesc = Messages.getString("TournamentControler.signupOver");
						}
					}
					if (view.mainView.qtFinished) { // 预选赛结束，看是否被淘汰
						final SignupView returnview = view;
						server.begin_isOut(role.getRoleId(), new Callback_CrossServerCallback_isOut() {
							@Override
							public void exception(LocalException __ex) {
								unknownException(__cb);
								logWarn("openTournamentView-isOut", __ex);
							}

							@Override
							public void response(boolean __ret) {
								returnview.mainView.isInKnockOut = __ret;
								__cb.ice_response(LuaSerializer.serialize(returnview));
							}

							@Override
							public void exception(UserException __ex) {
								__cb.ice_exception(__ex);
							}
						});
					} else {
						__cb.ice_response(LuaSerializer.serialize(view));
					}
				} catch (NoteException e) {
					__cb.ice_exception(e);
				}
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public PreSignupView preSignup() throws NoteException {
		TournamentAreaConfigT areaConfig = XsgTournamentManager.getInstance().getAreaConfig();
		if (areaConfig == null) {
			throw new NoteException(Messages.getString("TournamentControler.notStart"));
		}
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		int lastTime = Long.valueOf(
				(config.signupEndTime.getTime() - XsgCrossServerManager.getInstance().currentRemoteTime()) / 1000)
				.intValue();
		// 0,不能报名; 1,可以报名
		PreSignupView view = new PreSignupView(role.getLevel() < areaConfig.lvLimit ? 0 : 1, areaConfig.lvLimit,
				lastTime);
		return view;
	}

	@Override
	public void doSignup(final AMD_Tournament_signup __cb) throws NoteException {
		final int Failure = 0; // 失败
		final int Success = 1; // 成功
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		OpenLevelConfigT openCofig = XsgTournamentManager.getInstance().getOpenConfigT();
		if (openCofig == null || role.getLevel() < openCofig.openLevel) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.levelLimit")));
			return;
		}
		TournamentAreaConfigT areaConfig = XsgTournamentManager.getInstance().getAreaConfig();
		if (areaConfig == null) {
			throw new NoteException(Messages.getString("TournamentControler.notStart"));
		}
		final SignupResultView result = new SignupResultView();
		// 检查是否报过名
		if (tournament.getHasSignup() == 1) { // 已经报过名
			result.status = Failure;
			result.tips = Messages.getString("TournamentControler.signupOver");
			__cb.ice_response(LuaSerializer.serialize(result));
			return;
		}
		// 检查报名时间
		if (!isSignupTime(config)) {
			result.status = Failure;
			result.tips = Messages.getString("TournamentControler.notSignupTime");
			__cb.ice_response(LuaSerializer.serialize(result));
			return;
		}
		// 报名
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossRoleView view = new CrossRoleView(role.getRoleId(), role.getName(), role.getHeadImage(), role.getLevel(),
				role.getVipLevel(), role.getServerId(), role.getSex(), role.getFactionControler().getFactionName());
		// 保存阵容
		MyFormationView myFormation = getMyCurrentFromation();
		tournament.setMyFormation(TextUtil.GSON.toJson(myFormation));
		saveToRole();
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_apply(view, myFormation.heros, new Callback_CrossServerCallback_apply() {
			@Override
			public void response() {
				// 更新报名状态
				tournament.setHasSignup(1);
				saveToRole();
				result.status = Success;
				result.tips = Messages.getString("TournamentControler.signupSuccess");
				__cb.ice_response(LuaSerializer.serialize(result));
			}

			@Override
			public void exception(UserException __ex) {
				result.status = Failure;
				result.tips = XsgCrossServerManager.getNoteException(__ex).reason;
				__cb.ice_response(LuaSerializer.serialize(result));
			}

			@Override
			public void exception(LocalException __ex) {
				result.status = Failure;
				result.tips = Messages.getString("TournamentControler.fixingnow");
				__cb.ice_response(LuaSerializer.serialize(result));
				logWarn("doSignup-apply", __ex);
			}

		});
	}

	public static class StageWarp {
		public StageWarp(StageIndex currentStage, String stageDesc, String nextStageDate, String nextStageDesc) {
			super();
			this.currentStage = currentStage;
			this.stageDesc = stageDesc;
			this.nextStageDate = nextStageDate;
			this.nextStageDesc = nextStageDesc;
		}

		public StageIndex currentStage;
		public String stageDesc;
		public String nextStageDate;
		public String nextStageDesc;
	}

	/**
	 * 是否报过名
	 */
	private boolean hasSignup() {
		return tournament.getHasSignup() == 1;
	}

	@Override
	public void getPVPInfoView(final AMD_Tournament_enterPVPView __cb) throws NoteException {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		int lastFightCount = config.challengeCount - tournament.getFightCount();
		int lastRefreshCount = config.refreshCount - tournament.getRefreshCount();
		MyFormationView myFormation = TextUtil.GSON.fromJson(tournament.getMyFormation(), MyFormationView.class);
		// 资格赛剩余秒
		int lastTime = (int) (DateUtil.compareTime(config.qtEndDate, currentRemoteDate()) / 1000);
		if (lastTime < 0) {
			lastTime = 0;
		}
		long interval = config.cdTime * 60 * 1000L;
		long current = System.currentTimeMillis();
		int lastTimes = (int) ((interval - (current - lastFightTime)) / 1000);
		if (lastTimes < 0) {
			lastTimes = 0;
		}
		final PVPInfoView view = new PVPInfoView(lastTime, tournament.getBuyFightCount(), config.challengeCount,
				lastFightCount, tournament.getBuyRefreshCount(), config.refreshCount, lastRefreshCount, myFormation, 0,
				0, canSetupFormation(config), null, !isQTTime(), false, hasSignup(), lastTimes);
		if (!view.hasSignup) { // 没有报名，直接返回结果
			__cb.ice_response(LuaSerializer.serialize(view));
			return;
		}
		final AtomicInteger count = new AtomicInteger(0);
		// 获取排名
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_getMyRankScore(role.getRoleId(), new Callback_CrossServerCallback_getMyRankScore() {
			@Override
			public void response(IntIntPair __ret) {
				view.myRank = __ret.first;
				view.myScore = __ret.second;
				view.isInKnockout = (view.myRank <= 32);
				int getCount = count.incrementAndGet();
				if (getCount >= 2) {
					__cb.ice_response(LuaSerializer.serialize(view));
				}
			}

			@Override
			public void exception(LocalException __ex) {
				int getCount = count.incrementAndGet();
				if (getCount >= 2) {
					unknownException(__cb);
				}
				logWarn("getPVPInfoView-getMyRankScore", __ex);
			}

			@Override
			public void exception(UserException __ex) {
				int getCount = count.incrementAndGet();
				if (getCount >= 2) {
					__cb.ice_exception(__ex);
				}
			}
		});
		// 匹配对手
		server.begin_matchRival(role.getRoleId(), new Callback_CrossServerCallback_matchRival() {

			@Override
			public void response(CrossRivalView[] __ret) {
				view.opponentViews = __ret;
				// 缓存对手
				cacheRivalContext(__ret);
				int getCount = count.incrementAndGet();
				if (getCount >= 2) {
					__cb.ice_response(LuaSerializer.serialize(view));
				}
			}

			@Override
			public void exception(LocalException __ex) {
				int getCount = count.incrementAndGet();
				if (getCount >= 2) {
					unknownException(__cb);
				}
				logWarn("getPVPInfoView-matchRival", __ex);
			}

			@Override
			public void exception(UserException __ex) {
				int getCount = count.incrementAndGet();
				if (getCount >= 2) {
					__cb.ice_exception(__ex);
				}
			}
		});
	}

	/** 缓存对手上下文 */
	private void cacheRivalContext(CrossRivalView[] view) {
		opponentIdMap.clear();
		for (CrossRivalView v : view) {
			opponentIdMap.put(v.roleView.roleId, v);
		}
	}

	@Override
	public void refreshPVP(final AMD_Tournament_refreshPVPView __cb) throws NoteException {
		// 检查刷新次数限制
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		if (!hasSignup()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.hasNoSignup")));
			return;
		}
		// 判断资格赛时间
		if (!isQTTime()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.qtOver")));
			return;
		}
		if (tournament.getRefreshCount() >= config.refreshCount) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.refreshCountOver")));
			return;
		}
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_refreshRival(role.getRoleId(), new Callback_CrossServerCallback_refreshRival() {

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("refreshPVP-refreshRival", __ex);
			}

			@Override
			public void response(CrossRivalView[] __ret) {
				// 更新刷新次数
				tournament.setRefreshCount(tournament.getRefreshCount() + 1);
				saveToRole();
				// 缓存对手
				cacheRivalContext(__ret);
				__cb.ice_response(LuaSerializer.serialize(__ret));
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	private DatePair getCurrentChangeDate(TournamentConfigT config, StageWarp stage) {
		Date startDate = new Date();
		switch (stage.currentStage) {
		case S32:
			startDate = config.s32Start;
			break;
		case S16:
			startDate = config.s16Start;
			break;
		case S8:
			startDate = config.s8Start;
			break;
		case S4:
			startDate = config.shalfStart;
			break;
		case S2:
			startDate = config.sfinalStart;
			break;
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(DateUtil.joinTime(startDate, config.fightTime));
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(DateUtil.joinTime(startDate, config.fightTime));
		Date startTime = DateUtil.addHours(cal1, -config.changeFormationStartTime).getTime();
		Date endTime = DateUtil.addHours(cal2, -config.changeFormationEndTime).getTime();
		return new DatePair(startTime, endTime);
	}

	private DatePair getNextChangeDate(TournamentConfigT config, StageWarp stage) {
		String nextFightStr = stage.nextStageDate.replaceFirst("[0-9]+:[0-9]+", config.fightTime);
		Calendar nextFightDate = Calendar.getInstance();
		nextFightDate.setTime(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", nextFightStr));
		Calendar nextFightDate2 = Calendar.getInstance();
		nextFightDate2.setTimeInMillis(nextFightDate.getTimeInMillis());
		Date startTime = DateUtil.addHours(nextFightDate, -config.changeFormationStartTime).getTime();
		Date endTime = DateUtil.addHours(nextFightDate2, -config.changeFormationEndTime).getTime();
		return new DatePair(startTime, endTime);
	}

	private boolean canSetupFormation(TournamentConfigT config) {
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		// 资格赛阶段可以随意更换阵容
		long qtEndTime = config.s32Start.getTime();
		if (current <= qtEndTime) {
			return true;
		}
		StageWarp stage = getCurrentStageIndex(config);
		if (stage == null || StageIndex.End.equals(stage.currentStage)) {
			return false;
		}
		Date currentDate = new Date(current);
		DatePair currentDatePair = getCurrentChangeDate(config, stage);
		if (DateUtil.isBetween(currentDate, currentDatePair.start, currentDatePair.end)) {
			return true;
		}
		DatePair nextDatePair = getNextChangeDate(config, stage);
		if (DateUtil.isBetween(currentDate, nextDatePair.start, nextDatePair.end)) {
			return true;
		}
		return false;
	}

	private Date joinTime(String timeStr) {
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(current);
		String[] timeSlice = timeStr.split(":");
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSlice[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(timeSlice[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(timeSlice[2]));
		return cal.getTime();
	}

	@Override
	public void setupFormation(final AMD_Tournament_setupFormation __cb) throws NoteException {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		if (!canSetupFormation(config)) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.setupFormationTime")));
			return;
		}

		final MyFormationView myFormation = getMyCurrentFromation();
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		CrossRoleView view = new CrossRoleView(role.getRoleId(), role.getName(), role.getHeadImage(), role.getLevel(),
				role.getVipLevel(), role.getServerId(), role.getSex(), role.getFactionControler().getFactionName());
		server.begin_saveBattle(view, myFormation.heros, new Callback_CrossServerCallback_saveBattle() {
			@Override
			public void response() {
				tournament.setMyFormation(TextUtil.GSON.toJson(myFormation));
				saveToRole();
				__cb.ice_response();
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("setupFormation-saveBattle", __ex);
			}
		});
	}

	@Override
	public MyFormationView openSetupFormation() throws NoteException {
		return TextUtil.GSON.fromJson(tournament.getMyFormation(), MyFormationView.class);
	}

	private PvpOpponentFormationView getMySetupFormation() {
		MyFormationView formation = TextUtil.GSON.fromJson(tournament.getMyFormation(), MyFormationView.class);
		return formation.heros;
	}

	@Override
	public int buyRefreshCount() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		XsgTournamentManager manager = XsgTournamentManager.getInstance();
		TournamentConfigT config = manager.getConfig();
		if (config.isOpen == 0) {
			throw new NoteException(Messages.getString("TournamentControler.nextPrepare"));
		}
		TournamentRefreshCostT costT = manager.getRefreshCost(tournament.getBuyRefreshCount() + 1);
		if (costT == null) {
			throw new NoteException(Messages.getString("TournamentControler.buyCountOut"));
		}
		int money = costT.cost;
		if (role.getTotalYuanbao() < money) {
			throw new NotEnoughYuanBaoException();
		}
		// 扣除元宝
		role.reduceCurrency(new Money(CurrencyType.Yuanbao, money));
		// 增加次数
		tournament.setRefreshCount(Math.max(0, tournament.getRefreshCount() - costT.getCount));
		tournament.setBuyRefreshCount(tournament.getBuyRefreshCount() + 1);
		saveToRole();
		buyRefreshCount.onBuyRefreshCount(money);
		return config.refreshCount - tournament.getRefreshCount();
	}

	@Override
	public int buyFightCount() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		XsgTournamentManager manager = XsgTournamentManager.getInstance();
		TournamentConfigT config = manager.getConfig();
		if (config.isOpen == 0) {
			throw new NoteException(Messages.getString("TournamentControler.nextPrepare"));
		}
		TournamentFightCostT costT = manager.getFightCost(tournament.getBuyFightCount() + 1);
		if (costT == null) {
			throw new NoteException(Messages.getString("TournamentControler.buyFightCountOut"));
		}
		int money = costT.cost;
		if (role.getTotalYuanbao() < money) {
			throw new NotEnoughYuanBaoException();
		}
		// 扣除元宝
		role.reduceCurrency(new Money(CurrencyType.Yuanbao, money));
		// 增加次数
		tournament.setFightCount(Math.max(0, tournament.getFightCount() - costT.getCount));
		tournament.setBuyFightCount(tournament.getBuyFightCount() + 1);
		saveToRole();
		return config.challengeCount - tournament.getFightCount();
	}

	@Override
	public void getRankList(final AMD_Tournament_getRankList __cb) throws NoteException {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_getCrossRank(role.getRoleId(), new Callback_CrossServerCallback_getCrossRank() {
			@Override
			public void response(CrossRankView __ret) {
				__cb.ice_response(LuaSerializer.serialize(__ret));
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("getRankList-getCrossRank", __ex);
			}
		});
	}

	@Override
	public FightRecordItemView[] getFightRecords() throws NoteException {
		List<RoleTournamentRecord> list = db.getTournamentFightRecords();
		List<FightRecordItemView> returnList = new ArrayList<FightRecordItemView>();
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		if (list != null && list.size() > 0) {
			for (RoleTournamentRecord record : list) {
				CrossRoleView crview = TextUtil.GSON.fromJson(record.getRoleViewData(), CrossRoleView.class);
				FightRecordItemView view = new FightRecordItemView(crview, record.getAddScore(), record.getScore(),
						record.getWin(), record.getPower(),
						(int) ((current - record.getCreateDate().getTime()) / 1000), record.getId(),
						record.getMovieId());
				returnList.add(view);
			}
		}
		Collections.sort(returnList, new Comparator<FightRecordItemView>() {
			@Override
			public int compare(FightRecordItemView o1, FightRecordItemView o2) {
				return Integer.valueOf(o1.fightDate).compareTo(o2.fightDate);
			}
		});
		return returnList.toArray(new FightRecordItemView[0]);
	}

	private RoleTournamentRecord getTournamentFightRecord(String recordId) {
		if (db.getTournamentFightRecords() != null) {
			for (RoleTournamentRecord record : db.getTournamentFightRecords()) {
				if (record.getId().equals(recordId)) {
					return record;
				}
			}
		}
		return null;
	}

	@Override
	public void getFightMovieByRecordId(String recordId, final AMD_Tournament_getFightMovieByRecordId __cb) {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		if (TextUtil.isBlank(recordId)) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.recordNotExist")));
			return;
		}
		RoleTournamentRecord record = getTournamentFightRecord(recordId);
		if (record == null) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.recordNotExist")));
			return;
		}
		// 本地存储的战报
		if (record.getLocal() == 1) {
			XsgFightMovieManager.getInstance().findFightMovie(record.getMovieId(), new FindMovieCallback() {
				@Override
				public void onFindMovie(FightMovieView movieView) {
					if (movieView != null) {
						__cb.ice_response(LuaSerializer.serialize(new FightMovieView[] { movieView }));
					} else {
						__cb.ice_response(LuaSerializer.serialize(new FightMovieView[] {}));
					}
				}
			});
		}
	}

	/**
	 * 是否处于淘汰赛
	 */
	private boolean isKOTime() {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		long kostart = config.s32Start.getTime();
		long koend = config.sfinalEnd.getTime();
		return (kostart <= current && current <= koend);
	}

	/**
	 * 是否处于资格赛
	 */
	private boolean isQTTime() {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		long qtstart = config.qtStartDate.getTime();
		long qtend = config.qtEndDate.getTime();
		return (qtstart <= current && current <= qtend);
	}

	/**
	 * 资格赛是否结束
	 */
	private boolean isQTEnd() {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		long qtend = config.qtEndDate.getTime();
		return (current > qtend);
	}

	private void updateFightTime() {
		lastFightTime = System.currentTimeMillis();
	}

	private boolean checkBeginFight(TournamentConfigT config, String opponentId, Ice.AMDCallback __cb) {
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return false;
		}
		// 检查请求间隔
		long current = System.currentTimeMillis();
		long interval = config.cdTime * 60 * 1000L;
		if ((current - lastFightTime) < interval) {
			__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.TooMoreEnter")));
			return false;
		}
		// 判断资格赛时间
		if (!isQTTime()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.qtOver")));
			return false;
		}
		// 判断可挑战次数
		int lastCount = config.challengeCount - tournament.getFightCount();
		if (lastCount <= 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fightCountOut")));
			return false;
		}
		// 检查对手是否是匹配到的对手
		CrossRivalView rivalView = opponentIdMap.get(opponentId);
		if (rivalView == null) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.opponentNotExist")));
			return false;
		}
		// 检查后端系统可用性
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return false;
		}
		return true;
	}

	@Override
	public void beginFight(final String opponentId, final AMD_Tournament_beginFightWith __cb) {
		FightLifeNumT numT = XsgFightMovieManager.getInstance().getFightLifeT(
				XsgFightMovieManager.Type.Tournament.ordinal());
		if (numT.newBattle == 1) { // 采用新的战斗机制，不应该请求这个接口
			__cb.ice_exception(new NoteException(Messages.getString("ResourceBackControler.invalidParam")));
			return;
		}
		final TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (!checkBeginFight(config, opponentId, __cb)) {
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_getRoleFormationView(opponentId, new Callback_CrossServerCallback_getRoleFormationView() {

			@Override
			public void response(PvpOpponentFormationView __ret) {
				// 战斗次数加1
				tournament.setFightCount(tournament.getFightCount() + 1);
				FightFormations formations = new FightFormations(getMySetupFormation(), __ret,
						new FightMovieByteView[0], 0, 0, 0);
				beginFight.onBeginFight(opponentId, 1, config.challengeCount - tournament.getFightCount());
				__cb.ice_response(formations);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("beginFight-getRoleFormationView", __ex);
			}

		});
	}

	@Override
	public String endFight(final String opponentId, final int flag, int remainHeroCount, final int power)
			throws NoteException {
		final CrossRivalView rivalView = opponentIdMap.get(opponentId);
		if (rivalView == null) {
			throw new NoteException(Messages.getString("TournamentControler.opponentNotExist"));
		}

		if (!XsgCrossServerManager.getInstance().isUsable()) {
			throw new NoteException(Messages.getString("TournamentControler.fixingnow"));
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		CrossRoleView myView = new CrossRoleView(role.getRoleId(), role.getName(), role.getHeadImage(),
				role.getLevel(), role.getVipLevel(), role.getServerId(), role.getSex(), role.getFactionControler()
						.getFactionName());

		fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(XsgFightMovieManager.Type.Tournament,
				role.getRoleId(), opponentId, getMyCurrentFromation().heros, rivalView.pvpView);

		server.begin_endChallenge(myView, flag == 1, opponentId, new Callback_CrossServerCallback_endChallenge() {
			@Override
			public void response(String __ret) {
				// 增加战报记录
				RoleTournamentRecord record = new RoleTournamentRecord(GlobalDataManager.getInstance()
						.generatePrimaryKey(), db, 1, TextUtil.GSON.toJson(rivalView.roleView), flag,
						(flag == 1) ? rivalView.winScore : 5, rivalView.score, power, fightMovieIdContext,
						currentRemoteDate());
				db.getTournamentFightRecords().add(record);

				if (flag == 1) {
					// 处理首胜次数奖励
					TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
					tournament.setWinNum(tournament.getWinNum() + 1);
					if (tournament.getWinNum() == config.winNum) {
						Map<String, Integer> rewardMap = new HashMap<String, Integer>();
						String rewardItemPairs[] = config.winNumItems.split(",");
						for (String pair : rewardItemPairs) {
							String item[] = pair.split(":");
							rewardMap.put(item[0], Integer.parseInt(item[1]));
						}
						XsgMailManager.getInstance().sendTemplate(role.getRoleId(), MailTemplate.TreasureWinNum,
								rewardMap, null);
					}
				}
				updateFightTime();
				// 事件
				fightEvent.onFight(opponentId, flag, __ret);
			}

			@Override
			public void exception(UserException __ex) {

			}

			@Override
			public void exception(LocalException __ex) {
				logWarn("endFight-endChallenge", __ex);
			}
		});
		return fightMovieIdContext;
	}

	@Override
	public void getKnockOutView(final AMD_Tournament_getKnockOutView __cb) {
		final TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		// if (current < config.s32Start.getTime() || config.sfinalEnd.getTime()
		// < current) {
		// __cb.ice_exception(new
		// NoteException(Messages.getString("TournamentControler.notInKnockoutTime")));
		// return;
		// }
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_getSchedule(new Callback_CrossServerCallback_getSchedule() {
			@Override
			public void response(CrossScheduleView[] __ret) {
				long current = XsgCrossServerManager.getInstance().currentRemoteTime();
				Date startFightDate = joinTime(config.fightTime);
				long startFightTime = startFightDate.getTime();
				int lastFightTime = (int) ((startFightTime - current) / 1000);
				if (current > config.sfinalEnd.getTime()) {
					lastFightTime = -1;
				}
				KnockoutView view = new KnockoutView(__ret, lastFightTime);
				__cb.ice_response(LuaSerializer.serialize(view));
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
				logger.warn("getKnockOutView-getSchedule");
			}
		});
	}

	@Override
	public void getKnockOutMovie(int id, int index, final AMD_Tournament_getKnockOutMovie __cb) {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		// long current =
		// XsgCrossServerManager.getInstance().currentRemoteTime();
		// if (current < config.s32Start.getTime() || config.sfinalEnd.getTime()
		// < current) {
		// __cb.ice_exception(new
		// NoteException(Messages.getString("TournamentControler.notInKnockoutTime")));
		// return;
		// }
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_getScheduleMovieData(id, index, new Callback_CrossServerCallback_getScheduleMovieData() {

			@Override
			public void response(CrossMovieView __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("getKnockOutMovie-getScheduleMovieData", __ex);
			}
		});
	}

	@Override
	public void getBetView(final AMD_Tournament_getBetView __cb) {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		// if (current < config.s32Start.getTime() || config.sfinalEnd.getTime()
		// < current) {
		// __cb.ice_exception(new
		// NoteException(Messages.getString("TournamentControler.notInKnockoutTime")));
		// return;
		// }
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_getSchedule(new Callback_CrossServerCallback_getSchedule() {

			@Override
			public void response(CrossScheduleView[] __ret) {
				List<BetView> betViewList = new ArrayList<BetView>();
				if (__ret != null && __ret.length > 0) {
					List<RoleTournamentBet> myBetList = XsgTournamentBetManager.getInstance().getTournamentBetByRoleId(
							role.getRoleId());
					Map<Integer, RoleTournamentBet> myBetMap = new HashMap<Integer, RoleTournamentBet>();
					if (myBetList != null) {
						for (RoleTournamentBet bet : myBetList) {
							myBetMap.put(bet.getFightId(), bet);
						}
					}
					for (CrossScheduleView v : __ret) {
						RoleTournamentBet myBet = myBetMap.get(v.id);
						String betRoleId = null;
						int result = 0;
						int winornot = 0;
						if (myBet != null) {
							betRoleId = myBet.getBetRoleId();
							result = myBet.getResult();
							winornot = myBet.getWinornot();
						}
						BetView view = new BetView(v.id, betRoleId, winornot, result);
						betViewList.add(view);
					}
				}
				__cb.ice_response(LuaSerializer.serialize(betViewList));
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("getBetView-getSchedule", __ex);
			}
		});
	}

	@Override
	public void bet(final AMD_Tournament_bet __cb, final int stage, final int id, final String roleId, int num)
			throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		final String HeroWine = "wine3";
		final TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		long current = XsgCrossServerManager.getInstance().currentRemoteTime();
		if (current < config.s32Start.getTime() || config.sfinalEnd.getTime() < current) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.notInKnockoutTime")));
			return;
		}
		long fightTime = joinTime(config.fightTime).getTime();
		if (current >= fightTime) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.cannotBet")));
			return;
		}
		// 检查是否已经下过注(策划们决定每轮只能压一次胜和一次败，也就是两次)
		List<RoleTournamentBet> myBets = XsgTournamentBetManager.getInstance().getTournamentBetByRoleId(
				role.getRoleId());
		if (myBets != null && myBets.size() > 0) {
			int wineCount = 0;
			for (RoleTournamentBet bet : myBets) {
				if (config.stageIndex == bet.getNum() && bet.getFightId() == id) {
					__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.betSuccess")));
					return;
				}
				if (bet.getStage() == stage && config.stageIndex == bet.getNum()) {
					int winTimeLimit = config.getWineTime(stage);
					if ((++wineCount) >= winTimeLimit) { // 超过敬酒次数
						__cb.ice_exception(new NoteException(TextUtil.format(
								Messages.getString("TournamentControler.betCount"), winTimeLimit)));
						return;
					}
				}
			}
		}
		// 检查英雄酒是否充足
		if (role.getItemControler().getItemCountInPackage(HeroWine) < 1) {
			throw new NoteException(Messages.getString("TournamentControler.wineNotEnough"));
		}
		// 扣除英雄酒
		role.getItemControler().changeItemByTemplateCode(HeroWine, -1);
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		// 发送胜利者ID
		server.begin_crossBet(roleId, new Callback_CrossServerCallback_crossBet() {

			@Override
			public void response() {
				// 下注
				RoleTournamentBet bet = new RoleTournamentBet(GlobalDataManager.getInstance().generatePrimaryKey(),
						role.getRoleId(), config.stageIndex, stage, 1, id, roleId, 1, 0, currentRemoteDate());
				XsgTournamentBetManager.getInstance().addTournamentBet(bet);
				TournamentController.this.betEvent.onBet(bet);
				__cb.ice_response(1);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("bet-crossBet", __ex);
			}
		});
	}

	@Override
	public void openTournamentView(final AMD_Tournament_openTournamentView __cb) throws NoteException {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		final TournamentView view = getTournamentView(config);
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_isOut(role.getRoleId(), new Callback_CrossServerCallback_isOut() {
			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("openTournamentView-isOut", __ex);
			}

			@Override
			public void response(boolean __ret) {
				view.isInKnockOut = __ret;
				__cb.ice_response(LuaSerializer.serialize(view));
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void getKnockOutMovieList(final AMD_Tournament_getKnockOutMovieList __cb, int id) {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config.isOpen == 0) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.nextPrepare")));
			return;
		}
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.fixingnow")));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_getScheduleMovieList(id, new Callback_CrossServerCallback_getScheduleMovieList() {

			@Override
			public void response(String[] __ret) {
				__cb.ice_response(LuaSerializer.serialize(__ret));
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("getKnockOutMovieList-getScheduleMovieList", __ex);
			}
		});
	}

	private void unknownException(Ice.AMDCallback cb) {
		cb.ice_exception(new NoteException(Messages.getString("TournamentControler.innerError")));
	}

	private void logWarn(String tag, Exception e) {
		logger.warn(TextUtil.format("[{0}]:[{1}]:[{2}] Exception...", tag, role.getRoleId(), role.getName()), e);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		// 聊天回调接口未注册则不发送
		if (role.getChatControler().getChatCb() == null) {
			return null;
		}
		OpenLevelConfigT openConfig = XsgTournamentManager.getInstance().getOpenConfigT();
		if (openConfig == null) {
			return null;
		}
		if (openConfig.isOpen != 1 || role.getLevel() < openConfig.openLevel) {
			return null;
		}
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config != null && (config.isOpen == 1) && config.signupStartTime != null && config.sfinalEnd != null) {
			long current = XsgCrossServerManager.getInstance().currentRemoteTime();
			if (config.signupStartTime.getTime() <= current && current <= config.sfinalEnd.getTime()) {
				if (!hasSignup()) {
					return new MajorUIRedPointNote(MajorMenu.TournamentMenu, true);
				}
			}
		}
		return null;
	}

	@Override
	public TournamentStatusView getTournamentStatus() throws NoteException {
		TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (config == null) {
			throw new NoteException(Messages.getString("TournamentControler.notStart"));
		}
		StageWarp stage = getCurrentStageIndex(config);
		TournamentStatusView view = new TournamentStatusView(stage.stageDesc, stage.nextStageDate, stage.nextStageDesc);
		return view;
	}

	@Override
	public void setMaxRank(int rank) {
		tournament.setMaxHistoryRank(rank);
		saveToRole();
	}

	@Override
	public void setChampionNum(int num) {
		tournament.setLastChampionNum(num);
		saveToRole();
	}

	@Override
	public int getMaxRank() {
		if (tournament != null) {
			return tournament.getMaxHistoryRank();
		}
		return 0;
	}

	public void championLogin() {
		if (tournament != null && tournament.getLastChampionNum() > 0) {
			TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
			if (config != null) {
				final int lastNum = tournament.getLastChampionNum();
				boolean showTips = false;
				if (lastNum == config.stageIndex) { // 本届冠军
					showTips = true;
				}
				if (!showTips) {
					// 上届冠军，并且本届尚未结束
					long current = XsgCrossServerManager.getInstance().currentRemoteTime();
					if ((config.stageIndex - lastNum) == 1 && current < config.sfinalEnd.getTime()) {
						showTips = true;
					}
				}
				// 显示上线公告
				if (showTips) {
					List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
							XsgChatManager.AdContentType.TournamentChampionLogin);
					if (adTList != null && adTList.size() > 0) {
						final ChatAdT adt = adTList.get(NumberUtil.random(adTList.size()));
						if (adt != null && adt.onOff == 1) {
							// 延迟5s发送公告，让玩家自己也可以看到公告
							LogicThread.scheduleTask(new DelayedTask(5 * 1000L) {
								@Override
								public void run() {
									String content = XsgChatManager.getInstance().replaceRoleContent(adt.content, role);
									content = content.replace("~param~", "" + lastNum);
									XsgChatManager.getInstance().sendAnnouncement(content);
								}
							});
						}
					}
				}
			}
		}
	}

	@Override
	public int getWinNum() {
		return tournament.getWinNum();
	}

	/**
	 * 新机制战斗结束
	 */
	private void fightEnd(final TournamentConfigT config, final CrossRivalView rivalView, int remainHeroCount,
			final int power, final PvpOpponentFormationView selfFormation,
			final PvpOpponentFormationView opponentFormation, final CrossMovieView movie,
			final AMD_Tournament_fightWith __cb) {

		if (!checkBeginFight(config, rivalView.roleView.roleId, __cb)) {
			return;
		}

		final int flag = movie.winRoleId.equals(role.getRoleId()) ? 1 : 0;
		final String opponentId = rivalView.roleView.roleId;
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		CrossRoleView myView = new CrossRoleView(role.getRoleId(), role.getName(), role.getHeadImage(),
				role.getLevel(), role.getVipLevel(), role.getServerId(), role.getSex(), role.getFactionControler()
						.getFactionName());

		updateFightTime();

		server.begin_endChallenge(myView, flag == 1, opponentId, new Callback_CrossServerCallback_endChallenge() {
			@Override
			public void response(String __ret) {
				// 保存战报录像
				int star = XsgCopyManager.getInstance().calculateStar((byte) selfFormation.heros.length,
						(byte) movie.selfHeroNum);// 计算星级
				if (flag == 0) {
					star = 0;
				}
				String movieId = XsgFightMovieManager.getInstance().addFightMovie(Type.Tournament, role.getRoleId(),
						rivalView.roleView.roleId, flag, (byte) movie.selfHeroNum, selfFormation, opponentFormation,
						new FightMovieView(flag, star, movie.soloMovie, movie.fightMovie, new byte[0]));

				int addScore = NumberUtil.parseInt(__ret.split(",")[2]);
				// 增加战报记录
				RoleTournamentRecord record = new RoleTournamentRecord(GlobalDataManager.getInstance()
						.generatePrimaryKey(), db, 1, TextUtil.GSON.toJson(rivalView.roleView), flag, addScore,
						rivalView.score, power, movieId, currentRemoteDate());
				db.getTournamentFightRecords().add(record);

				if (flag == 1) {
					// 处理首胜次数奖励
					TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
					tournament.setWinNum(tournament.getWinNum() + 1);
					if (tournament.getWinNum() == config.winNum) {
						Map<String, Integer> rewardMap = new HashMap<String, Integer>();
						String rewardItemPairs[] = config.winNumItems.split(",");
						for (String pair : rewardItemPairs) {
							String item[] = pair.split(":");
							rewardMap.put(item[0], Integer.parseInt(item[1]));
						}
						XsgMailManager.getInstance().sendTemplate(role.getRoleId(), MailTemplate.TreasureWinNum,
								rewardMap, null);
					}
				}
				FightFormations formations = new FightFormations(selfFormation, opponentFormation,
						new FightMovieByteView[] { new FightMovieByteView(movie.soloMovie, movie.fightMovie) }, (role
								.getRoleId().equals(movie.winRoleId) ? 1 : 0), movie.selfHeroNum, movie.winType);
				// 战斗次数加1
				tournament.setFightCount(tournament.getFightCount() + 1);
				__cb.ice_response(formations);
				fightEvent.onFight(opponentId, flag, __ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("fightEnd-endChallenge", __ex);
			}
		});
	}

	@Override
	public void fight(final String opponentId, final AMD_Tournament_fightWith __cb) {
		final TournamentConfigT config = XsgTournamentManager.getInstance().getConfig();
		if (!checkBeginFight(config, opponentId, __cb)) {
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();
		server.begin_getRoleFormationView(opponentId, new Callback_CrossServerCallback_getRoleFormationView() {

			@Override
			public void response(final PvpOpponentFormationView __ret) {
				final CrossRivalView rivalView = opponentIdMap.get(opponentId);
				if (rivalView != null) {
					final PvpOpponentFormationView selfFormation = getMySetupFormation();
					int type = XsgFightMovieManager.getInstance().getFightLifeT(Type.Tournament.ordinal()).id;
					final CrossPvpView pvpView = new CrossPvpView(type, new CrossRoleView(role.getRoleId(), role
							.getName(), role.getHeadImage(), role.getLevel(), role.getVipLevel(), role.getServerId(),
							role.getSex(), ""), selfFormation, new CrossRoleView(rivalView.roleView.roleId,
							rivalView.roleView.roleName, rivalView.roleView.headImg, rivalView.roleView.level,
							rivalView.roleView.vipLevel, rivalView.roleView.serverId, rivalView.roleView.sex, ""),
							__ret, "", 0);

					MovieThreads.execute(new Runnable() {

						@Override
						public void run() {
							final String content = HttpUtil.sendPost(XsgLadderManager.getInstance().movieUrl,
									TextUtil.GSON.toJson(pvpView));
							LogicThread.execute(new Runnable() {

								@Override
								public void run() {
									CrossMovieView movie = TextUtil.GSON.fromJson(content, CrossMovieView.class);
									if (movie == null) {
										__cb.ice_exception(new NoteException(Messages.getString("FactionControler.59")));
										return;
									}
									XsgLadderManager.getInstance().replaceNull(movie);

									fightEnd(config, rivalView, movie.selfHeroNum, __ret.view.battlePower,
											selfFormation, __ret, movie, __cb);
								}
							});
						}
					});
				} else {
					__cb.ice_exception(new NoteException(Messages.getString("TournamentControler.opponentNotExist")));
				}
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				unknownException(__cb);
				logWarn("beginFight-getRoleFormationView", __ex);
			}

		});
	}

	public TournamentShopView getShopView() throws NoteException {
		List<TournamentShopT> shopItems = XsgTournamentManager.getInstance().getShopItems();
		TournamentShopView view = new TournamentShopView();
		List<TournamentShopItemView> itemList = new ArrayList<TournamentShopItemView>();
		if (shopItems != null) {
			for (TournamentShopT shopT : shopItems) {
				TournamentShopItemView item = new TournamentShopItemView(shopT.type, shopT.id, shopT.price,
						shopT.limit, getShopBuyCount(shopT.id), shopT.desc, shopT.coin);
				itemList.add(item);
			}
		}
		view.items = itemList.toArray(new TournamentShopItemView[0]);
		view.coint = tournament.getCoin();
		view.ybcoin = tournament.getYbcoin();
		return view;
	}

	public void addCoin(int num) {
		tournament.setCoin(tournament.getCoin() + num);
		saveToRole();
	}

	public void addYBCoin(int num) {
		tournament.setYbcoin(tournament.getYbcoin() + num);
		saveToRole();
	}

	/**
	 * 增加比武大会货币
	 * 
	 * @param type
	 *            类型，0：至尊币。1：至尊银币
	 * */
	public void addTournamentCoin(int type, int num) {
		switch (type) {
		case 0:
			addCoin(num);
			break;
		case 1:
			addYBCoin(num);
			break;
		}
	}

	/**
	 * 获取比武大会货币
	 * 
	 * @param type
	 *            类型，0：至尊币。1：至尊银币
	 * */
	public int getTournamentCoin(int type) {
		switch (type) {
		case 0:
			return tournament.getCoin();
		case 1:
			return tournament.getYbcoin();
		}
		return 0;
	}

	@Override
	public ShopBuyResultView buyShopItem(String id, int num) throws NoteException {
		final int TZZYB = 1;
		final int TZZB = 0;
		if (num <= 0) {
			throw new NoteException(Messages.getString("TournamentControler.zeroLimit"));
		}
		TournamentShopT shopT = XsgTournamentManager.getInstance().getShopItem(id);
		int coinType = TZZB;
		if (Const.PropertyName.ZZYB.equals(shopT.coin)) {
			coinType = TZZYB;
		}
		if (shopT != null) {
			// 检查购买限制
			int buyCount = getShopBuyCount(id);
			if ((buyCount + num) > shopT.limit) {
				throw new NoteException(Messages.getString("TournamentControler.buyLimit"));
			}
			int price = shopT.price * num;
			// 检查钱币是否足够
			if (getTournamentCoin(coinType) < price) {
				String tips = Messages.getString("TournamentControler.zzbNotEnough");
				if (TZZYB == coinType) {
					tips = Messages.getString("TournamentControler.zzybNotEnough");
				}
				throw new NoteException(tips);
			}
			// 扣钱币
			addTournamentCoin(coinType, -price);
			// 加道具
			role.getRewardControler().acceptReward(shopT.id, num);
			// 增加购买计数
			addShopBuyCount(shopT.id, num);
		}
		return new ShopBuyResultView(getShopBuyCount(id), tournament.getCoin(), tournament.getYbcoin());
	}

	public static class DatePair {
		public Date start;
		public Date end;

		public DatePair(Date start, Date end) {
			this.start = start;
			this.end = end;
		}
	}
}
