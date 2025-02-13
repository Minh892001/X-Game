package com.morefun.XSanGo.copy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AMD_Copy_autoChallengeTa;
import com.XSanGo.Protocol.AMD_Copy_endChallenge;
import com.XSanGo.Protocol.AMD_Copy_getBigCopyView;
import com.XSanGo.Protocol.AMD_Copy_getSmallCopyViewWithWarmup;
import com.XSanGo.Protocol.BigCopyView;
import com.XSanGo.Protocol.BuyMilitaryOrderView;
import com.XSanGo.Protocol.CaptureView;
import com.XSanGo.Protocol.ChallengeTaAutoResult;
import com.XSanGo.Protocol.ChallengeTaView;
import com.XSanGo.Protocol.ChapterReward;
import com.XSanGo.Protocol.ChapterRewardView;
import com.XSanGo.Protocol.CopyBuff;
import com.XSanGo.Protocol.CopyChallengeResultView;
import com.XSanGo.Protocol.CopyClearResultView;
import com.XSanGo.Protocol.CopyDetail;
import com.XSanGo.Protocol.CopyDifficulty;
import com.XSanGo.Protocol.CopyOccupy;
import com.XSanGo.Protocol.CopySummaryView;
import com.XSanGo.Protocol.CrossMovieView;
import com.XSanGo.Protocol.CrossPvpView;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.DuelReportView;
import com.XSanGo.Protocol.DuelUnitView;
import com.XSanGo.Protocol.EmployCaptureResult;
import com.XSanGo.Protocol.EndChallengeResultView;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.HuDongView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.QualityColor;
import com.XSanGo.Protocol.SceneDuelView;
import com.XSanGo.Protocol.SmallCopyView;
import com.XSanGo.Protocol.SmallCopyViewWithWarmup;
import com.XSanGo.Protocol.WarmupOpponentView;
import com.XSanGo.Protocol.WarmupView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.MovieThreads;
import com.morefun.XSanGo.battle.DuelBattle;
import com.morefun.XSanGo.battle.DuelReport;
import com.morefun.XSanGo.battle.DuelUnit;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleCopy;
import com.morefun.XSanGo.db.game.RoleWorship;
import com.morefun.XSanGo.db.game.ServerCopy;
import com.morefun.XSanGo.event.protocol.IBuyChallengeChance;
import com.morefun.XSanGo.event.protocol.IBuyChapterChallengeChance;
import com.morefun.XSanGo.event.protocol.IBuyHudong;
import com.morefun.XSanGo.event.protocol.IBuyJunLing;
import com.morefun.XSanGo.event.protocol.ICaptureEmploy;
import com.morefun.XSanGo.event.protocol.ICaptureKill;
import com.morefun.XSanGo.event.protocol.ICaptureRelease;
import com.morefun.XSanGo.event.protocol.IChallengeTa;
import com.morefun.XSanGo.event.protocol.IChapterReward;
import com.morefun.XSanGo.event.protocol.ICopyBegin;
import com.morefun.XSanGo.event.protocol.ICopyClear;
import com.morefun.XSanGo.event.protocol.ICopyCompleted;
import com.morefun.XSanGo.event.protocol.IWarmupEscape;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.PVEMovieParam;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.formation.datacollector.BattlePowerSnapshotQueryResult;
import com.morefun.XSanGo.formation.datacollector.XsgFormationDataCollecterManager;
import com.morefun.XSanGo.hero.HeroDialogT;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.VipT;
import com.morefun.XSanGo.vip.XsgVipManager;

class CopyControler implements ICopyControler {
	private final static Log logger = LogFactory.getLog(CopyControler.class);

	private IRole rt;

	private Role db;

	private IWarmupEscape warmupEscapeEvent;

	/** 普通难度进度，表示最近的一个可以挑战的且之前未成功挑战过的副本 */
	private int juniorProgress;

	/** 高手难度进度，表示最近的一个可以挑战的且之前未成功挑战过的副本 */
	private int seniorProgress;

	/** 大神难度进度，表示最近的一个可以挑战的且之前未成功挑战过的副本 */
	private int topProgress;

	/** 热身赛上下文 */
	private WarmupContext warmupContext;

	/** 挑战关卡的上下文状态 */
	private Map<Integer, CopyChallengeResult> copyContext = new HashMap<Integer, CopyChallengeResult>();

	/** 已领取奖励的关卡章节 */
	private String[] alreadyReceiveChapters;

	private ICopyCompleted copyCompletedEvent;

	private ICopyBegin copyBeginEvent;

	private IBuyChallengeChance buyChallengeEvent;

	private ICopyClear copyClearEvent;

	private ICaptureEmploy captureEmployEvent;

	private ICaptureKill captureKillEvent;

	private IChapterReward chapterRewardEvent;

	private ICaptureRelease captureReleaseEvent;

	private IBuyHudong buyHudongEvent;

	private IChallengeTa challengeTaEvent;

	private IBuyChapterChallengeChance buyChapterChallengeEvent;

	private IBuyJunLing buyJunLingEvent;

	// 战报上下文
	private String fightMovieIdContext;

	private int currentCopyId;

	/**
	 * 挑战TA副本ID
	 */
	private int challengeTaCopyId;

	/** 挑战关卡的失败结束时间 控制CD */
	private Map<Integer, Date> copyEndChallengeTaDate = new HashMap<Integer, Date>();

	public CopyControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;

		warmupEscapeEvent = rt.getEventControler().registerEvent(IWarmupEscape.class);

		this.calculateProgress();
		this.alreadyReceiveChapters = TextUtil.isBlank(this.db.getChapterTag()) ? new String[0] : TextUtil.GSON
				.fromJson(this.db.getChapterTag(), String[].class);

		this.copyCompletedEvent = this.rt.getEventControler().registerEvent(ICopyCompleted.class);
		this.copyBeginEvent = this.rt.getEventControler().registerEvent(ICopyBegin.class);
		this.buyChallengeEvent = this.rt.getEventControler().registerEvent(IBuyChallengeChance.class);
		this.copyClearEvent = this.rt.getEventControler().registerEvent(ICopyClear.class);
		this.captureEmployEvent = this.rt.getEventControler().registerEvent(ICaptureEmploy.class);
		this.captureKillEvent = this.rt.getEventControler().registerEvent(ICaptureKill.class);
		this.chapterRewardEvent = this.rt.getEventControler().registerEvent(IChapterReward.class);
		this.captureReleaseEvent = this.rt.getEventControler().registerEvent(ICaptureRelease.class);
		this.buyHudongEvent = this.rt.getEventControler().registerEvent(IBuyHudong.class);
		this.challengeTaEvent = this.rt.getEventControler().registerEvent(IChallengeTa.class);
		this.buyChapterChallengeEvent = this.rt.getEventControler().registerEvent(IBuyChapterChallengeChance.class);
		this.buyJunLingEvent = this.rt.getEventControler().registerEvent(IBuyJunLing.class);
	}

	/**
	 * 包装一个CopyDetail
	 * 
	 * @param rc
	 *            RoleCopy 对象
	 * */
	private CopyDetail wrapCopyDetail(RoleCopy rc) {
		int copyId = rc.getTemplateId();

		int resetCount = getResetCountToday(copyId);
		String refreshTime = XsgCopyManager.getInstance().getNextResetTime();
		short remainCount = (short) getRemainTime(copyId);
		short maxClgCount = XsgCopyManager.getInstance().getMaxChallengeTime(rc.getTemplateId());
		int clearTokenCount = rt.getItemControler().getItemCountInPackage(
				XsgCopyManager.getInstance().getClearItemTemplate());

		ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(copyId);
		// 是否有首杀者
		boolean hasAllStar = (sc != null);
		int junLing = XsgCopyManager.getInstance().getJunLingConsume(copyId);
		SmallCopyView scv = new SmallCopyView(remainCount, maxClgCount, clearTokenCount, hasAllStar,
				Messages.getString("CopyControler.0"), 0, resetCount, //$NON-NLS-1$
				refreshTime, junLing);
		CopyDetail cd = new CopyDetail(rc.getTemplateId(), rc.getStar(), scv);
		// 有首杀者, 更新首杀者信息, 首杀者的信息应该在调用这个方法之前放到缓存中
		if (hasAllStar) {
			IRole role = XsgRoleManager.getInstance().findRoleById(sc.getChampionId());
			if (role != null) {
				scv.firstAllStar = role.getName();
				scv.firstAllStarLvl = role.getVipLevel();
			} else {
				logger.error(Messages.getString("CopyControler.1")); //$NON-NLS-1$
			}
		}

		return cd;
	}

	private List<String> findChampions(Collection<RoleCopy> copys) {
		List<String> championIds = new ArrayList<String>();
		if (copys != null) {
			for (RoleCopy c : copys) {
				ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(c.getTemplateId());
				// 有首次满星击杀者
				if (sc != null && !TextUtil.isBlank(sc.getChampionId())) {
					championIds.add(sc.getChampionId());
				}
			}
		}
		return championIds;
	}

	private void getBigCopyViewInner(final AMD_Copy_getBigCopyView cb) {
		List<CopyDetail> juniorCopyDetailMapList = new ArrayList<CopyDetail>();
		List<CopyDetail> senioCopyDetailMapList = new ArrayList<CopyDetail>();
		List<CopyDetail> topCopyDetailMapList = new ArrayList<CopyDetail>();

		// 初级
		RoleCopy rc = this.db.getRoleCopys().get(XsgCopyManager.getFirstCopyId(CopyDifficulty.Junior));
		while (rc != null && rc.getStar() > 0) {
			CopyDetail cd = wrapCopyDetail(rc);
			if (cd != null) {
				juniorCopyDetailMapList.add(cd);
			}

			rc = this.db.getRoleCopys().get(XsgCopyManager.getInstance().findSmallCopyT(rc.getTemplateId()).nextId);
		}

		// 高手
		rc = this.db.getRoleCopys().get(XsgCopyManager.getFirstCopyId(CopyDifficulty.Senior));
		while (rc != null && rc.getStar() > 0) {
			CopyDetail cd = wrapCopyDetail(rc);
			if (cd != null) {
				senioCopyDetailMapList.add(cd);
			}

			rc = this.db.getRoleCopys().get(XsgCopyManager.getInstance().findSmallCopyT(rc.getTemplateId()).nextId);
		}

		// 大神
		rc = this.db.getRoleCopys().get(XsgCopyManager.getFirstCopyId(CopyDifficulty.Top));
		while (rc != null && rc.getStar() > 0) {
			CopyDetail cd = wrapCopyDetail(rc);
			if (cd != null) {
				topCopyDetailMapList.add(cd);
			}

			rc = this.db.getRoleCopys().get(XsgCopyManager.getInstance().findSmallCopyT(rc.getTemplateId()).nextId);
		}
		int buff = getCopyBuff(-1);
		cb.ice_response(new BigCopyView(juniorCopyDetailMapList.toArray(new CopyDetail[0]), senioCopyDetailMapList
				.toArray(new CopyDetail[0]), topCopyDetailMapList.toArray(new CopyDetail[0]), buff));
	}

	@Override
	public void getBigCopyView(final AMD_Copy_getBigCopyView cb) {
		// 首次击杀者ID list
		List<String> championList = findChampions(db.getRoleCopys().values());

		if (championList.size() > 0) {
			XsgRoleManager.getInstance().loadRoleAsync(championList, new Runnable() {
				@Override
				public void run() {
					getBigCopyViewInner(cb);
				}
			});
		} else {
			getBigCopyViewInner(cb);
		}
	}

	/**
	 * 获取或创建指定模板的持久化副本数据，同时检查挑战次数，根据当前时间决定是否重置
	 * 
	 * @param id
	 * @return
	 */
	private RoleCopy getOrCreateCopyData(int id) {
		Date now = Calendar.getInstance().getTime();
		if (!this.db.getRoleCopys().containsKey(id)) {
			RoleCopy roleCopy = new RoleCopy(GlobalDataManager.getInstance().generatePrimaryKey(), db, id, now);
			this.db.getRoleCopys().put(id, roleCopy);
		}

		RoleCopy roleCopy = this.db.getRoleCopys().get(id);
		if (XsgCopyManager.getInstance().canResetChallenge(roleCopy.getLastTime())) {
			if (roleCopy.getCountToday() > 0) {// 打过才重置，避免重置征收来的次数
				roleCopy.setCountToday((short) 0);
			}
		}
		if (roleCopy.getLastCopyResetTime() != null && !DateUtil.isSameDay(roleCopy.getLastCopyResetTime(), now)) {
			roleCopy.setCopyResetCount(0);
		}

		return roleCopy;
	}

	@Override
	public boolean endChallenge(byte star) throws NoteException {
		CopyChallengeResult temp = this.copyContext.get(this.currentCopyId);
		if (temp == null) {
			throw new NoteException(Messages.getString("CopyControler.2")); //$NON-NLS-1$
		}
		if (temp.copyEnd) {
			throw new NoteException(Messages.getString("CopyControler.3")); //$NON-NLS-1$
		}

		temp.star = star;
		temp.endTime = new Date();
		RoleCopy roleCopy = this.getOrCreateCopyData(temp.templateId);
		SmallCopyT sct = XsgCopyManager.getInstance().findSmallCopyT(temp.templateId);

		boolean isFirst = false;
		boolean isFirstOccupy = false;// 首次占领
		int junLingConsume = 0;
		if (star > 0) {
			rt.getNotifyControler().setAutoNotify(false);
			// 扣除军令
			// 硬编码, 关卡结束的时候扣除剩余其他军令
			int junLingTotalConsume = XsgCopyManager.getInstance().getJunLingConsume(temp.templateId);
			junLingConsume = -(junLingTotalConsume - 1);
			this.rt.getItemControler().changeItemByTemplateCode(Const.PropertyName.JUNLING_TEMPLATE_ID, junLingConsume);
			// 军令小于最大值要开启自动恢复
			if (this.db.getNextJunLingTime() == null
					&& this.rt.getItemControler().getItemCountInPackage(Const.PropertyName.JUNLING_TEMPLATE_ID) < XsgRoleManager
							.getInstance().getRoleLevelConfigT(this.rt.getLevel()).junlingLimit) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.SECOND, XsgGameParamManager.getInstance().getJunLingRecorvInterval());
				this.db.setNextJunLingTime(c.getTime());
			}

			// 增加挑战次数，更新星级等操作
			roleCopy.setCountToday((short) (roleCopy.getCountToday() + 1));
			roleCopy.setCountTotal(roleCopy.getCountTotal() + 1);
			roleCopy.setLastTime(new Date());
			if (star > roleCopy.getStar()) {
				roleCopy.setStar(star);
			}

			this.rt.getRewardControler().acceptReward(temp.rewards);
			this.rt.getHeroControler().winHeroExp(temp.heroExpMap);
			rt.getNotifyControler().setAutoNotify(true);
			// 首次过关，更新进度数据
			isFirst = this.refreshProgressAfterWin(temp.templateId);
			// 刷到好装备要发滚动公告
			ItemView[] itemViews = XsgRewardManager.getInstance().generateItemView(temp.rewards);
			sendNotice(itemViews, sct);
		}

		// 五星过关才触发
		if (star == Const.MaxStar) {
			ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(sct.id);
			if (sc == null && sct.isMiddleCopy() && !isHoldFull()) {
				GlobalDataManager.getInstance().newServerCopy(sct.id, this.rt.getRoleId());
				// GlobalDataManager.getInstance().afterHoldServerCopy(this.rt);
				if (!isHasOccupy()) {
					isFirstOccupy = true;
					// 首次占领奖励
					sendFirstAward();
				}
				roleCopy.setOccupyDate(new Date());
			}
		}

		temp.copyEnd = true;
		if (temp.captureView == null && temp.star > 0) {// 没有俘虏需要处理且过关时才移除数据
			removeTempData();
		}

		int battlePower = this.rt.getCachePower();
		this.copyCompletedEvent.onCopyCompleted(sct, star, isFirst, battlePower, -junLingConsume);
		int seconds = (int) ((temp.endTime.getTime() - temp.beginTime.getTime()) / 1000);
		XsgCopyManager.getInstance().checkBattlePower(this.rt.getAccount(), this.rt.getRoleId(), sct.id, star,
				battlePower, seconds);

		return isFirstOccupy;
	}

	/**
	 * 曾经是否有占领过关卡
	 * 
	 * @return
	 */
	private boolean isHasOccupy() {
		for (RoleCopy r : db.getRoleCopys().values()) {
			if (r.getOccupyDate() != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 发送滚动公告
	 * 
	 * @param itemViews
	 */
	private void sendNotice(ItemView[] itemViews, SmallCopyT copyT) {
		for (ItemView i : itemViews) {
			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(i.templateId);
			if (itemT != null
					&& itemT.getColor().ordinal() > QualityColor.Blue.ordinal()
					&& (itemT.getItemType() == ItemType.EquipItemType || itemT.getItemType() == ItemType.FormationBuffItemType)) {
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
						XsgChatManager.AdContentType.CopyEquip);
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				String content = this.rt.getChatControler().parseAdContentItem(itemT, adT.content);
				content = parseAdContentCopy(content, copyT);
				XsgChatManager.getInstance().sendAnnouncement(content);
			}
		}
	}

	/**
	 * 组织不同难度不同章节的关卡文字
	 * 
	 * @param content
	 * @param timeBattleId
	 * @return
	 */
	private String parseAdContentCopy(String content, SmallCopyT smallCopyT) {
		BigCopyT bigCopyT = XsgCopyManager.getInstance().findBigCopyT(smallCopyT.parentId);
		if (bigCopyT.difficulty == 0) {// 简单
			content = content.replace("~color~", Const.Chat.CHAT_COLOR_GREEN).replace("~diff_type~",
					Messages.getString("CopyControler.38"));
		} else if (bigCopyT.difficulty == 1) {// 高手
			content = content.replace("~color~", Const.Chat.CHAT_COLOR_BLUE).replace("~diff_type~",
					Messages.getString("CopyControler.39"));
		} else {// 大神
			content = content.replace("~color~", Const.Chat.CHAT_COLOR_PURPLE).replace("~diff_type~",
					Messages.getString("CopyControler.40"));
		}
		content = content.replace("~copy_name~", bigCopyT.name + " " + smallCopyT.name);
		return content;
	}

	@Override
	public void failChallenge() throws NoteException {
		XsgFightMovieManager.getInstance().anomalyEndFightMovie(rt.getRoleId(), fightMovieIdContext, 0, (byte) 0);
	}

	/**
	 * 移除临时的上下文数据，副本彻底结束时调用，目前有以下几种情况需要调用：<br>
	 * 1、战斗胜利且无俘虏需要处理时<br>
	 * 2、战斗胜利且俘虏处理完毕时
	 */
	private void removeTempData() {
		this.copyContext.remove(this.currentCopyId);
	}

	/**
	 * 通过关卡后更新进度数据
	 * 
	 * @param templateId
	 * @return 是否首次过关
	 */
	private boolean refreshProgressAfterWin(int templateId) {
		boolean isFirstWin = false;
		int nextId = XsgCopyManager.getInstance().findSmallCopyT(templateId).nextId;

		if (this.juniorProgress == templateId) {
			this.juniorProgress = nextId;
			isFirstWin = true;
		} else if (this.seniorProgress == templateId) {
			this.seniorProgress = nextId;
			isFirstWin = true;
		} else if (this.topProgress == templateId) {
			this.topProgress = nextId;
			isFirstWin = true;
		}

		return isFirstWin;
	}

	@Override
	public short getCountToday(int copyId) {
		return this.getOrCreateCopyData(copyId).getCountToday();
	}

	/**
	 * 计算章节星星数
	 * 
	 * @param parent
	 * @return
	 */
	private int calculateTotalStar(BigCopyT parent) {
		int totalStar = 0;
		for (SmallCopyT sct : parent.getChildren()) {
			if (!sct.isMiddleCopy()) {
				continue;
			}
			RoleCopy rc = this.db.getRoleCopys().get(sct.id);
			if (rc != null) {
				totalStar += rc.getStar();
			}
		}
		return totalStar;
	}

	@Override
	public boolean canChallenge(int copyId) {
		SmallCopyT smallCopyT = XsgCopyManager.getInstance().findSmallCopyT(copyId);
		if (smallCopyT == null) {
			logger.error("SmallCopyT is not found." + copyId); //$NON-NLS-1$
			return false;
		}
		BigCopyT parent = smallCopyT.getParent();
		if (parent.level > this.rt.getLevel()) {
			return false;
		}

		RoleCopy rc = this.getOrCreateCopyData(copyId);
		// 小关卡不能重复打
		if (rc.getStar() > 0 && smallCopyT.isMiniCopy()) {
			return false;
		}

		if (!TextUtil.isBlank(parent.preChapter)) {
			int[] preArray = parent.getPreChapter();
			for (int preId : preArray) {
				if (!this.isAllWin(preId)) {
					return false;
				}
			}
		}

		return rc.getStar() > 0 || this.juniorProgress == copyId || this.seniorProgress == copyId
				|| this.topProgress == copyId;
	}

	private boolean isAllWin(int preId) {
		BigCopyT chapter = XsgCopyManager.getInstance().findBigCopyT(preId);
		for (SmallCopyT sct : chapter.getChildren()) {
			RoleCopy rc = this.db.getRoleCopys().get(sct.id);
			if (rc == null || rc.getStar() < 1) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 刷新进度数据
	 */
	private void calculateProgress() {
		// 普通
		this.juniorProgress = XsgCopyManager.getFirstCopyId(CopyDifficulty.Junior);
		RoleCopy rc = this.db.getRoleCopys().get(this.juniorProgress);
		while (rc != null && rc.getStar() > 0) {
			this.juniorProgress = XsgCopyManager.getInstance().findSmallCopyT(this.juniorProgress).nextId;
			rc = this.db.getRoleCopys().get(this.juniorProgress);
		}

		// 高手
		this.seniorProgress = XsgCopyManager.getFirstCopyId(CopyDifficulty.Senior);
		rc = this.db.getRoleCopys().get(this.seniorProgress);
		while (rc != null && rc.getStar() > 0) {
			this.seniorProgress = XsgCopyManager.getInstance().findSmallCopyT(this.seniorProgress).nextId;
			rc = this.db.getRoleCopys().get(this.seniorProgress);
		}
		// 大神
		this.topProgress = XsgCopyManager.getFirstCopyId(CopyDifficulty.Top);
		rc = this.db.getRoleCopys().get(this.topProgress);
		while (rc != null && rc.getStar() > 0) {
			this.topProgress = XsgCopyManager.getInstance().findSmallCopyT(this.topProgress).nextId;
			rc = this.db.getRoleCopys().get(this.topProgress);
		}
	}

	@Override
	public CopyChallengeResultView beginChallenge(String formationId, int copyId, boolean needMovie)
			throws NoteException, NotEnoughException {
		IFormation formation = this.rt.getFormationControler().getFormation(formationId);
		if (formation == null) {
			throw new NoteException(Messages.getString("CopyControler.5")); //$NON-NLS-1$
		}

		SmallCopyT copyT = XsgCopyManager.getInstance().findSmallCopyT(copyId);
		if (copyT == null) {
			throw new NoteException(Messages.getString("CopyControler.6") + copyId); //$NON-NLS-1$
		}

		if (!this.canChallenge(copyId)) {
			throw new NoteException(Messages.getString("CopyControler.7")); //$NON-NLS-1$
		}

		if (this.getRemainTime(copyId) < 1) {
			throw new NoteException(Messages.getString("CopyControler.8")); //$NON-NLS-1$
		}

		int junLingCount = this.rt.getItemControler().getItemCountInPackage(Const.PropertyName.JUNLING_TEMPLATE_ID);
		if (junLingCount < XsgCopyManager.getInstance().getJunLingConsume(copyId)) {
			throw new NotEnoughException(CurrencyType.MilitaryOrder);
		}

		this.rt.getItemControler().checkPackageFull();

		this.currentCopyId = copyId;
		CopyChallengeResult temp = this.copyContext.get(copyId);
		if (temp == null || (temp.copyEnd && temp.star > 0)) {// 无缓存数据或者已经过关则视为合法
			temp = new CopyChallengeResult();
			temp.templateId = copyId;
			temp.rewards = XsgRewardManager.getInstance().doTc(this.rt,
					this.getOrCreateCopyData(copyId).getCountTotal() > 0 ? copyT.tc : copyT.firstTc);

			int exp = rt.isDoubleExp() ? copyT.exp * 2 : copyT.exp;
			temp.rewards.appendProperty(Const.PropertyName.EXP, exp);

			if (rt.isDoubleItem()) {
				TcResult doubleTc = XsgRewardManager.getInstance().doTc(this.rt,
						this.getOrCreateCopyData(copyId).getCountTotal() > 0 ? copyT.tc : copyT.firstTc);
				temp.rewards = temp.rewards.add(doubleTc);
			}
			this.copyContext.put(copyId, temp);
		}

		// 计算是否抓获俘虏
		int captureId = copyT.randomCatchHero();
		if (captureId > 0) {
			CaptureT ct = XsgCopyManager.getInstance().findCaptureT(copyId, captureId);
			HeroDialogT heroT = XsgHeroManager.getInstance().getHeroDialogT(captureId);
			String name = this.rt.getName();
			temp.captureView = new CaptureView(captureId, ct.releaseExp, XsgRewardManager.getInstance().doTcToItem(
					this.rt, ct.KillTc), heroT.randomReleaseMsg(name), heroT.randomKillMsg(name), false, 0);
		}
		temp.heroExpMap = XsgRewardManager.getInstance().calculateHeroExp(formation.getHeros());
		temp.orignalHeroCount = formation.getHeroCountIncludeSupport();
		temp.copyEnd = false;// 战败或中途退出情况缓存不会被清理，因此这里重置下结束状态
		temp.beginTime = new Date();

		SceneDuelView[] reports = this.tryGenerateDuelReport(formation, copyT);
		// 扣除军令
		// 硬编码, 关卡开始的时候扣除1个军令
		int junLingConsume = -1;
		this.rt.getItemControler().changeItemByTemplateCode(Const.PropertyName.JUNLING_TEMPLATE_ID, junLingConsume);

		// 事件触发
		this.copyBeginEvent.onCopyBegin(copyT, -junLingConsume);
		return this.generateRewardView(temp, formation.generateDuelCandidateData(), reports, needMovie);
	}

	/**
	 * 发送捕获武将的公告
	 * */
	private void sendCaptureAnno(String copyName, HeroT heroT) {
		XsgChatManager manager = XsgChatManager.getInstance();
		List<ChatAdT> adTList = manager.getAdContentMap(XsgChatManager.AdContentType.CaptureHero);
		if (adTList != null && adTList.size() > 0) {
			ChatAdT adt = adTList.get(NumberUtil.random(adTList.size()));
			if (adt != null && adt.onOff == 1) {
				String content = manager.replaceRoleContent(adt.content, rt);
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("~copy_name~", copyName);
				String msg = rt.getChatControler().parseAdContentHeroT(heroT, content, replaceMap);
				XsgChatManager.getInstance().sendAnnouncement(msg);
			}
		}
	}

	/**
	 * 进行单挑匹配，成功则返回相应战报，未触发则直接返回NULL
	 * 
	 * @param formation
	 * @param copyT
	 * @return
	 */
	private SceneDuelView[] tryGenerateDuelReport(IFormation formation, SmallCopyT copyT) {
		List<SceneDuelView> list = new ArrayList<SceneDuelView>();

		RoleCopy roleCopy = this.getOrCreateCopyData(copyT.id);

		if (roleCopy.getStar() == 0) {// 从未挑战成功过本副本，读取事件数据
			for (CopySceneT cst : copyT.getSceneTList()) {
				List<StoryEventT> eventList = cst.getDuelEventTList();
				for (StoryEventT event : eventList) {
					DuelUnit first = null;

					if (event.firstId.startsWith("M-")) {// 发起方为怪物 //$NON-NLS-1$
						first = XsgCopyManager.getInstance().createDuelUnitFromMonster(
								XsgCopyManager.getInstance().findMonsterT(event.getFirstMonsterId()));
					} else if (event.firstId.equals("0")) {// 0表示任意玩家单挑武将 //$NON-NLS-1$
						IHero randomHero = formation.randomDuelHero();
						if (randomHero == null) {
							continue;
						}
						first = randomHero.createDuelUnit();
					} else {// 发起方为武将
						first = this.rt.getHeroControler().getHero(event.getFirstHeroId()).createDuelUnit();
					}

					DuelUnit second = XsgCopyManager.getInstance().createDuelUnitFromMonster(
							XsgCopyManager.getInstance().findMonsterT(event.monsterId));
					DuelBattle battle = new DuelBattle(first, second);
					DuelReport report = battle.fuckEachOther();
					list.add(new SceneDuelView(cst.sceneSeq, event.id, new DuelReportView[] { report.generateView() }));
				}
			}
		} else {// 常规单挑逻辑
			IHero hero = formation.randomDuelHero();
			if (hero == null) {
				return list.toArray(new SceneDuelView[0]);
			}

			Map<Integer, MonsterT> monsterMap = copyT.randomDuelMonsterMap();

			for (final int sceneSeq : monsterMap.keySet()) {
				if (CollectionUtil.exists(list, new IPredicate<SceneDuelView>() {
					@Override
					public boolean check(SceneDuelView item) {
						return item.sceneSeq == sceneSeq;
					}
				})) {
					continue;
				}

				DuelUnit first = hero.createDuelUnit();
				DuelUnit second = XsgCopyManager.getInstance().createDuelUnitFromMonster(monsterMap.get(sceneSeq));
				DuelBattle battle = new DuelBattle(first, second);
				DuelReport report = battle.fuckEachOther();
				list.add(new SceneDuelView(sceneSeq, 0, new DuelReportView[] { report.generateView() }));
			}
		}

		return list.toArray(new SceneDuelView[0]);
	}

	/**
	 * 生成奖励品的视图信息，这里的物品表示广义物品，包括金钱经验等
	 * 
	 * @param ccr
	 * @param myDuelHeros
	 *            我方首发阵容里的单挑武将数据
	 * @param reports
	 *            场景单挑数据
	 * @return
	 */
	private CopyChallengeResultView generateRewardView(CopyChallengeResult ccr, DuelUnitView[] myDuelHeros,
			SceneDuelView[] reports, boolean needMovie) {
		// 武将经验
		List<Property> heroExpList = new ArrayList<Property>();
		for (String heroId : ccr.heroExpMap.keySet()) {
			heroExpList.add(new Property(heroId, ccr.heroExpMap.get(heroId)));
		}

		if (needMovie) {
			// 存储战报上下文
			PVEMovieParam param = new PVEMovieParam("" + ccr.templateId, "");
			fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(XsgFightMovieManager.Type.Copy,
					this.rt, param);
		}

		int buff = getCopyBuff(ccr.templateId);
		// 清除状态, 不管是否开启buff，都要把状态清空
		clearCopyBuff();

		return new CopyChallengeResultView(heroExpList.toArray(new Property[0]), XsgRewardManager.getInstance()
				.generateItemView(ccr.rewards), myDuelHeros, reports, ccr.captureView == null ? new CaptureView[0]
				: new CaptureView[] { ccr.captureView }, fightMovieIdContext, buff);
	}

	private int getCopyBuff(int copyId) {
		int buff = db.getCopyBuff();
		if (copyId >= 0) {
			SmallCopyT copyT = XsgCopyManager.getInstance().findSmallCopyT(copyId);
			if (copyT.copyBuffEffect != 1) { // 副本buff未开启，则不返回buff
				buff = CopyBuff.None.ordinal();
			}
		}
		return buff;
	}

	private void clearCopyBuff() {
		// 清除状态
		if (db.getCopyBuff() != CopyBuff.None.ordinal()) {
			db.setCopyBuff(CopyBuff.None.ordinal());
		}
	}

	@Override
	public int getRemainTime(int copyId) {
		int maxChallengeTime = XsgCopyManager.getInstance().getMaxChallengeTime(copyId);
		int temp = maxChallengeTime - this.getCountToday(copyId);
		return Math.max(temp, 0);
	}

	@Override
	public CopyClearResultView clearCopy(int copyTemplateId) throws NoteException, NotEnoughException {
		String code = XsgCopyManager.getInstance().getClearItemTemplate();
		if (this.rt.getItemControler().getItemCountInPackage(code) < 1) {
			throw new NoteException(Messages.getString("CopyControler.12")); //$NON-NLS-1$
		}
		SmallCopyT copyT = XsgCopyManager.getInstance().findSmallCopyT(copyTemplateId);
		if (copyT.isMiniCopy()) {
			throw new NoteException(Messages.getString("CopyControler.13")); //$NON-NLS-1$
		}
		if (this.getOrCreateCopyData(copyTemplateId).getStar() < Const.MaxStar) {
			throw new NoteException(Messages.getString("CopyControler.14")); //$NON-NLS-1$
		}

		String formationId = this.rt.getFormationControler().getDefaultFormation().getId();
		CopyChallengeResultView mockView = this.beginChallenge(formationId, copyTemplateId, false);
		CopyChallengeResult temp = this.copyContext.get(copyTemplateId);
		temp.heroExpMap = null;

		List<ItemView> additionList = XsgCopyManager.getInstance().getCopyAdditionalItemList(this.rt.getLevel());

		for (ItemView add : additionList) {
			temp.rewards.appendProperty(add.templateId, add.num);
		}

		this.rt.getItemControler().changeItemByTemplateCode(code, -1);
		this.endChallenge((byte) 1);
		ItemView[] itemViews = additionList.toArray(new ItemView[0]);
		sendNotice(itemViews, copyT);

		this.copyClearEvent.onClear(copyT, mockView, additionList);

		// return new CopyClearResultView(copyTemplateId, mockView.items,
		// itemViews, rt.getLevelWealControler().getCopyLevelWeal());
		return new CopyClearResultView(copyTemplateId, mockView.items, itemViews, 0);
	}

	@Override
	public ChapterRewardView getChapterRewardView(int chapterId) {
		BigCopyT template = XsgCopyManager.getInstance().findBigCopyT(chapterId);
		int star = this.calculateTotalStar(template);

		ChapterRewardView view = new ChapterRewardView();
		view.totalStar = star;
		view.rewardList = new ChapterReward[3];
		int[] rewardStars = { template.star1, template.star2, template.star3 };
		ChapterReward[] chapterRewards = view.rewardList;
		// 不同星级的奖励
		for (int i = 0; i < 3; i++) {
			chapterRewards[i] = new ChapterReward((i + 1), (star >= rewardStars[i]), hasReceiveChapterReward(chapterId,
					i + 1));
		}

		return view;
	}

	private boolean canReceiveChapterReward(int chapterId, int level) {
		ChapterRewardView view = this.getChapterRewardView(chapterId);
		if (view != null && view.totalStar > 0) {
			ChapterReward[] rewards = view.rewardList;
			for (ChapterReward r : rewards) {
				if (level == r.level) {
					if (r.canReceive && !r.hasReceived) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	@Override
	public void receiveChapterReward(int chapterId, int level) throws NoteException {
		if (!canReceiveChapterReward(chapterId, level)) {
			throw new NoteException(Messages.getString("CopyControler.15")); //$NON-NLS-1$
		}

		BigCopyT template = XsgCopyManager.getInstance().findBigCopyT(chapterId);
		String tcArray[] = { template.tc1, template.tc2, template.tc3 };
		String tc = tcArray[level - 1];
		this.rt.getRewardControler().acceptReward(XsgRewardManager.getInstance().doTc(this.rt, tc));
		this.setChapterReceiveTag(chapterId, level);
		this.chapterRewardEvent.onGetChapterReward(chapterId, level, tc);
	}

	private void setChapterReceiveTag(int chapterId, int level) {
		if (this.hasReceiveChapterReward(chapterId, level)) {
			return;
		}

		this.alreadyReceiveChapters = Arrays
				.copyOf(this.alreadyReceiveChapters, this.alreadyReceiveChapters.length + 1);
		this.alreadyReceiveChapters[this.alreadyReceiveChapters.length - 1] = wrapChapterRewardKey(chapterId, level);
		this.db.setChapterTag(TextUtil.GSON.toJson(this.alreadyReceiveChapters));
	}

	private String wrapChapterRewardKey(int chapterId, int level) {
		return TextUtil.format("{0}-{1}", chapterId, level); //$NON-NLS-1$
	}

	/**
	 * @param chapterId
	 *            章节索引
	 * @param level
	 *            星级奖励索引 1(10星奖励),2(20星奖励),3(满星奖励)
	 * */
	private boolean hasReceiveChapterReward(int chapterId, int level) {
		String key = wrapChapterRewardKey(chapterId, level);
		for (String k : this.alreadyReceiveChapters) {
			if (key.equals(k)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int getCopyStar(int copyId) {
		return this.getOrCreateCopyData(copyId).getStar();
	}

	@Override
	public void buyChallengeChance(int copyId) throws NotEnoughYuanBaoException, NoteException {
		RoleCopy rc = this.getOrCreateCopyData(copyId);
		int already = rc.getCopyResetCount();
		if (already >= XsgVipManager.getInstance().findVipT(this.rt.getVipLevel()).StageNum) {
			throw new NoteException(Messages.getString("CopyControler.17")); //$NON-NLS-1$
		}

		int price = XsgCopyManager.getInstance().getResetPrice(already + 1);
		if (rt.getTotalYuanbao() < price) {
			throw new NotEnoughYuanBaoException();
		}
		short count = XsgCopyManager.getInstance().getMaxChallengeTime(copyId);

		this.rt.winYuanbao(-price, true);
		rc.setCountToday((short) (rc.getCountToday() - count));
		rc.setCopyResetCount(already + 1);
		rc.setLastCopyResetTime(Calendar.getInstance().getTime());

		this.buyChallengeEvent.onBuyChallengeChance(copyId, price);
	}

	@Override
	public IntIntPair[] buyChapterChallengeChance(int chapterId) throws NotEnoughYuanBaoException, NoteException {
		int needVip = XsgGameParamManager.getInstance().getCopyClearVip();
		int needLevel = XsgGameParamManager.getInstance().getCopyClearLevel();
		if (this.rt.getVipLevel() < needVip && this.rt.getLevel() < needLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("CopyControler.18"), needVip, needLevel)); //$NON-NLS-1$
		}

		int maxCount = XsgVipManager.getInstance().findVipT(this.rt.getVipLevel()).StageNum;
		BigCopyT chapterT = XsgCopyManager.getInstance().findBigCopyT(chapterId);

		int priceYuanbao = 0;
		// 检查每个关卡的剩余次数
		for (SmallCopyT sc : chapterT.getChildren()) {
			if (sc.isMiddleCopy()) {
				RoleCopy roleCopy = getRoleCopy(sc.id);
				if (roleCopy == null || roleCopy.getStar() <= 0) {
					throw new NoteException(
							Messages.getString("CopyControler.19") + sc.name + Messages.getString("CopyControler.20")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if (roleCopy.getCountToday() < 3) {
					throw new NoteException(
							Messages.getString("CopyControler.21") + sc.name + Messages.getString("CopyControler.22")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if (roleCopy.getCopyResetCount() < maxCount) {
					priceYuanbao += XsgCopyManager.getInstance().getResetPrice(roleCopy.getCopyResetCount() + 1);
				}
			}
		}

		// 检查元宝是否够消耗
		if (rt.getTotalYuanbao() < priceYuanbao) {
			throw new NotEnoughYuanBaoException();
		}

		this.rt.winYuanbao(-priceYuanbao, true);

		List<IntIntPair> resList = new ArrayList<IntIntPair>();
		// 重置关卡
		for (SmallCopyT sc : chapterT.getChildren()) {
			RoleCopy roleCopy = getRoleCopy(sc.id);
			if (roleCopy != null && roleCopy.getCopyResetCount() < maxCount) {
				roleCopy.setCountToday((short) 0);
				roleCopy.setCopyResetCount(roleCopy.getCopyResetCount() + 1);
				roleCopy.setLastCopyResetTime(Calendar.getInstance().getTime());
				resList.add(new IntIntPair(sc.id, roleCopy.getCopyResetCount()));
			}
		}

		this.buyChapterChallengeEvent.onBuyChapterChallengeChance(chapterId, priceYuanbao);
		return resList.toArray(new IntIntPair[0]);
	};

	@Override
	public int getResetCountToday(int copyId) {
		return this.getOrCreateCopyData(copyId).getCopyResetCount();
	}

	@Override
	public void releaseCaptured() {
		CopyChallengeResult temp = this.copyContext.get(this.currentCopyId);
		if (temp == null) {
			return;
		}
		if (temp.captureView.handle) {
			throw new IllegalStateException();
		}

		this.rt.winPrestige(XsgCopyManager.getInstance().findCaptureT(temp.templateId, temp.captureView.catchHeroId).releaseExp);
		temp.captureView.handle = true;
		this.removeTempData();
		this.captureReleaseEvent.onCaptureRelease(temp.templateId, temp.captureView);
	}

	@Override
	public int killCaptured() {
		CopyChallengeResult temp = this.copyContext.get(this.currentCopyId);
		if (temp == null) {
			return 0;
		}
		if (temp.captureView.handle) {
			throw new IllegalStateException();
		}

		this.rt.getRewardControler().acceptReward(temp.captureView.killItems);
		CaptureT ct = XsgCopyManager.getInstance().findCaptureT(temp.templateId, temp.captureView.catchHeroId);
		temp.captureView.handle = true;
		this.removeTempData();
		this.captureKillEvent.onCaptureKilled(temp.templateId, temp.captureView);
		return ct.notifyNum;
	}

	@Override
	public EmployCaptureResult employCaptured() throws NotEnoughMoneyException {
		CopyChallengeResult temp = this.copyContext.get(this.currentCopyId);
		if (temp == null) {
			throw new IllegalStateException();
		}
		if (temp.captureView.handle) {
			throw new IllegalStateException();
		}
		int catchHeroId = temp.captureView.catchHeroId;
		int rate = XsgCopyManager.getInstance().findCaptureT(temp.templateId, catchHeroId).employRate;
		int jinbi = XsgCopyManager.getInstance().getEmployPrice(temp.captureView.employTime + 1);
		// 扣钱
		this.rt.winJinbi(-jinbi);

		boolean success = NumberUtil.isHit(rate, Const.Ten_Thousand);
		temp.captureView.employTime++;
		int soulCount = 0;
		String msg = "", soulTemplate = ""; //$NON-NLS-1$ //$NON-NLS-2$
		if (success) {// 成功录用
			msg = XsgHeroManager.getInstance().getHeroDialogT(catchHeroId).randomAcceptEmployMsg(this.rt.getName());
			temp.captureView.handle = true;

			HeroT heroT = XsgHeroManager.getInstance().getHeroT(catchHeroId);
			// 已有武将
			if (this.rt.getHeroControler().getHero(catchHeroId) != null) {
				soulTemplate = XsgHeroManager.getInstance().getSoulTemplateId(catchHeroId);
				soulCount = XsgHeroManager.getInstance().caculateSoulCountForCardTransform(heroT.color);
				this.rt.getItemControler().changeItemByTemplateCode(
						XsgHeroManager.getInstance().getSoulTemplateId(catchHeroId), soulCount);
			} else {
				this.rt.getHeroControler().addHero(heroT, HeroSource.Copy);
			}
		} else {
			msg = XsgHeroManager.getInstance().getHeroDialogT(catchHeroId).randomRejectEmployMsg(this.rt.getName());
		}

		EmployCaptureResult result = new EmployCaptureResult(success, msg, soulTemplate, soulCount);
		if (success) {
			this.removeTempData();// 由于失败可以反复录用，所以只有成功才移除
			this.captureEmployEvent.onCaptureEmploy(result);
		}

		return result;
	}

	@Override
	public CopySummaryView[] getCopyChallengeInfo(int[] idArray) {
		List<CopySummaryView> list = new ArrayList<CopySummaryView>();
		for (int copyId : idArray) {
			short maxChallengeTime = XsgCopyManager.getInstance().getMaxChallengeTime(copyId);
			int remainTime = this.getRemainTime(copyId);
			list.add(new CopySummaryView(remainTime, maxChallengeTime, this.canChallenge(copyId)));
		}

		return list.toArray(new CopySummaryView[0]);
	}

	@Override
	public IntString[] getProgresses() {
		List<IntString> list = new ArrayList<IntString>();

		list.add(new IntString(CopyDifficulty.Junior.ordinal(), this.juniorProgress == 0 ? Messages
				.getString("CopyControler.25") : XsgCopyManager.getInstance() //$NON-NLS-1$
				.findSmallCopyT(this.juniorProgress).name));
		list.add(new IntString(CopyDifficulty.Senior.ordinal(), this.seniorProgress == 0 ? Messages
				.getString("CopyControler.26") : XsgCopyManager.getInstance() //$NON-NLS-1$
				.findSmallCopyT(this.seniorProgress).name));
		list.add(new IntString(CopyDifficulty.Top.ordinal(), this.topProgress == 0 ? Messages
				.getString("CopyControler.27") : XsgCopyManager.getInstance() //$NON-NLS-1$
				.findSmallCopyT(this.topProgress).name));
		return list.toArray(new IntString[0]);
	}

	private void calculateBuff(byte orignHero, byte remainHero, byte killNum, float minTime, float maxTime) {
		if (remainHero > 0) {
			int buff = CopyBuff.None.ordinal();

			List<CopyBuffT> buffList = XsgCopyManager.getInstance().getCopyBuff();
			// 按优先级排序
			Collections.sort(buffList, new Comparator<CopyBuffT>() {
				@Override
				public int compare(CopyBuffT o1, CopyBuffT o2) {
					return o2.priority - o1.priority;
				}
			});
			/**
			 * type 的含义 1 时间小于 2 时间大于 3 杀人几个 4 死亡N人以上任然获胜
			 * */
			loop: for (CopyBuffT t : buffList) {
				switch (t.type) {
				case 1:
					if (minTime < t.limit) {
						buff = t.id;
						break loop;
					}
					break;
				case 2:
					if (maxTime > t.limit) {
						buff = t.id;
						break loop;
					}
					break;
				case 3:
					if (killNum >= t.limit) {
						buff = t.id;
						break loop;
					}
					break;
				case 4:
					int dieHero = orignHero - remainHero;
					if (dieHero >= t.limit) {
						buff = t.id;
						break loop;
					}
					break;
				default:
					break;
				}
			}
			db.setCopyBuff(buff);
		}
	}

	@Override
	public int calculateStar(byte remainHero, byte killNum, float minTime, float maxTime) throws NoteException {
		CopyChallengeResult temp = this.copyContext.get(this.currentCopyId);
		if (temp == null) {
			throw new NoteException(Messages.getString("CopyControler.UneffectOperation")); //$NON-NLS-1$
		}
		temp.star = XsgCopyManager.getInstance().calculateStar(temp.orignalHeroCount, remainHero);
		if (remainHero > 0) {
			XsgFightMovieManager.getInstance().endFightMovie(rt.getRoleId(), fightMovieIdContext, 1, remainHero);
		} else {
			XsgFightMovieManager.getInstance().anomalyEndFightMovie(rt.getRoleId(), fightMovieIdContext, 0, remainHero);
		}
		SmallCopyT copyT = XsgCopyManager.getInstance().findSmallCopyT(temp.templateId);
		if (copyT.copyBuff == 1) { // 开启了副本Buff才计算
			calculateBuff(temp.orignalHeroCount, remainHero, killNum, minTime, maxTime);
		}
		return temp.star;
	}

	@Override
	public void endChallenge(final AMD_Copy_endChallenge __cb) throws NoteException {
		CopyChallengeResult temp = this.copyContext.get(this.currentCopyId);
		if (temp == null) {
			throw new NoteException(Messages.getString("CopyControler.UneffectOperation")); //$NON-NLS-1$
		}
		final boolean isFirstOccupy = this.endChallenge(temp.star);
		final RoleCopy roleCopy = db.getRoleCopys().get(this.currentCopyId);
		ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(roleCopy.getTemplateId());
		int levelWeal = 0;
		if (roleCopy.getStar() > 0) {
			// levelWeal =
			// CopyControler.this.rt.getLevelWealControler().getCopyLevelWeal();
			levelWeal = 0;
		}
		final int copyBuff = getCopyBuff(temp.templateId);
		if (sc != null && !TextUtil.isBlank(sc.getChampionId())) {
			// 获取角色之后的回调
			final int lw = levelWeal;
			Runnable callback = new Runnable() {
				@Override
				public void run() {
					// 包装CopyDetail对象
					CopyDetail detail = wrapCopyDetail(roleCopy);
					EndChallengeResultView resView = new EndChallengeResultView(detail, lw, copyBuff, isFirstOccupy);
					__cb.ice_response(resView);
				}
			};
			// 获取首杀角色信息
			XsgRoleManager.getInstance().loadRoleByIdAsync(sc.getChampionId(), callback, callback);
		} else {
			// 没有首杀者, 直接返回
			CopyDetail detail = wrapCopyDetail(roleCopy);
			EndChallengeResultView resView = new EndChallengeResultView(detail, levelWeal, copyBuff, isFirstOccupy);
			__cb.ice_response(resView);
		}
		if (temp != null && temp.star > 0 && temp.captureView != null) {
			CaptureT ct = XsgCopyManager.getInstance().findCaptureT(roleCopy.getTemplateId(),
					temp.captureView.catchHeroId);
			if (ct != null && ct.notify == 1) {
				XsgCopyManager cpm = XsgCopyManager.getInstance();
				SmallCopyT smallCopyT = cpm.findSmallCopyT(roleCopy.getTemplateId());
				BigCopyT bigCopyT = cpm.findBigCopyT(smallCopyT.parentId);
				String difficulty = "";
				if (bigCopyT.difficulty == 0) {
					difficulty = Messages.getString("CopyControler.38");
				}
				if (bigCopyT.difficulty == 1) {
					difficulty = Messages.getString("CopyControler.39");
				}
				if (bigCopyT.difficulty == 2) {
					difficulty = Messages.getString("CopyControler.40");
				}
				HeroT heroT = XsgHeroManager.getInstance().getHeroT(temp.captureView.catchHeroId);
				sendCaptureAnno(difficulty + smallCopyT.name, heroT);
			}
		}
	}

	@Override
	public int getTotalStar() {
		int result = 0;
		for (RoleCopy rc : this.db.getRoleCopys().values()) {
			result += rc.getStar();
		}

		return result;
	}

	@Override
	public HuDongView getHuDongView(int copyId) {
		resetHudongCount(copyId);
		int maxCount = XsgCopyManager.getInstance().getHudongConfigT().hudongCount;
		int maxWorshipCount = XsgCopyManager.getInstance().getHudongConfigT().worshipCount;
		RoleCopy roleCopy = this.db.getRoleCopys().get(copyId);
		int cd = 0;
		if (copyEndChallengeTaDate.containsKey(copyId)) {
			cd = 60 - (int) (DateUtil.compareTime(new Date(), copyEndChallengeTaDate.get(copyId)) / 1000);
		}
		HuDongView huDongView = new HuDongView(maxCount - db.getWorshipCount(), maxCount, maxWorshipCount
				- roleCopy.getHudongCount(), maxWorshipCount, cd, this.db.getBuyHudongCount());
		return huDongView;
	}

	@Override
	public ChallengeTaView getChallengeTaView(IRole iRole, int copyId) {
		this.db.setWorshipCount(db.getWorshipCount() + 1);
		this.db.setLastHudongDate(new Date());
		String formationId = iRole.getArenaRankControler().findFormationId();
		if (formationId.equals("")) {
			formationId = iRole.getFormationControler().getDefaultFormation().getId();
		}
		PvpOpponentFormationView view = iRole.getFormationControler().getPvpOpponentFormationView(formationId);
		ChallengeTaView challengeTaView = new ChallengeTaView(iRole.getRoleId(), iRole.getName(), iRole.getHeadImage(),
				iRole.getSex(), iRole.getVipLevel(), iRole.getLevel(), view);
		challengeTaCopyId = copyId;
		return challengeTaView;
	}

	@Override
	public RoleCopy getRoleCopy(int copyId) {
		return this.db.getRoleCopys().get(copyId);
	}

	@Override
	public void buyHudong(int copyId) throws NoteException, NotEnoughYuanBaoException {
		resetHudongCount(copyId);
		if (getChallengeTaCount() > 0) {
			throw new NoteException(Messages.getString("CopyControler.30"));
		}
		if (this.db.getBuyHudongCount() >= 10) {
			throw new NoteException(Messages.getString("CopyControler.31"));
		}

		// if (this.db.getBuyHudongCount() >=
		// XsgVipManager.getInstance().findVipT(this.rt.getVipLevel()).StageNum)
		// {
		//			throw new NoteException(Messages.getString("CopyControler.31")); //$NON-NLS-1$
		// }
		// int yuanbao =
		// XsgCopyManager.getInstance().getResetPrice(this.db.getBuyHudongCount()
		// + 1);
		int yuanbao = 100;
		this.rt.winYuanbao(-yuanbao, true);
		// this.db.getRoleCopys().get(copyId).setHudongCount(0);
		// this.db.setBuyHudongCount(this.db.getBuyHudongCount() + 1);
		// this.db.setLastHudongDate(new Date());

		this.db.setWorshipCount(0);
		try {
			buyHudongEvent.onBuyHudong(this.db.getBuyHudongCount(), yuanbao);
		} catch (Exception e) {
			LogManager.error(e);
		}
	}

	/**
	 * 获取关卡的可挑战TA次数
	 * 
	 * @param copyId
	 * @return
	 */
	@Override
	public int getChallengeTaCount() {
		// RoleCopy roleCopy = this.db.getRoleCopys().get(copyId);
		// if (roleCopy == null) {
		// throw new IllegalStateException();
		// }
		HudongConfigT hudongConfigT = XsgCopyManager.getInstance().getHudongConfigT();
		return hudongConfigT.hudongCount - this.db.getWorshipCount();
	}

	/**
	 * 获取自己的可膜拜次数
	 * 
	 * @param copyId
	 * @return
	 */
	@Override
	public int getWorshipCount(int copyId) throws NoteException {
		HudongConfigT hudongConfigT = XsgCopyManager.getInstance().getHudongConfigT();
		// return hudongConfigT.worshipCount - this.db.getWorshipCount();
		RoleCopy roleCopy = this.db.getRoleCopys().get(copyId);
		if (roleCopy == null) {
			throw new IllegalStateException();
		}
		return hudongConfigT.worshipCount - roleCopy.getHudongCount();
	}

	/**
	 * 重置关卡互动次数、总膜拜次数、总关卡互动购买次数
	 */
	@Override
	public void resetHudongCount(int copyId) {
		RoleCopy roleCopy = getOrCreateCopyData(copyId);
		Date lastTime = roleCopy.getLastHudongDate();
		if (lastTime != null && !DateUtil.isSameDay(lastTime, Calendar.getInstance().getTime())) {
			roleCopy.setHudongCount(0);// 关卡膜拜次数
		}
		lastTime = this.db.getLastHudongDate();
		if (lastTime != null && !DateUtil.isSameDay(lastTime, Calendar.getInstance().getTime())) {
			this.db.setWorshipCount(0);// 每天总挑战TA次数
			this.db.setBuyHudongCount(0);// 每天购买挑战TA次数
		}
	}

	@Override
	public int worshipTa(int copyId) throws NoteException {
		ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(copyId);
		if (sc == null) {
			throw new NoteException(Messages.getString("CopyControler.32"));
		}
		resetHudongCount(copyId);
		if (sc.getChampionId().equals(this.db.getId())) {
			throw new NoteException(Messages.getString("CopyControler.33"));
		}
		// if (getHudongCount(copyId) <= 0) {
		//			throw new NoteException(Messages.getString("CopyControler.34")); //$NON-NLS-1$
		// }
		if (getWorshipCount(copyId) <= 0) {
			throw new NoteException(Messages.getString("CopyControler.35")); //$NON-NLS-1$
		}
		for (RoleWorship rw : this.db.getWorships()) {
			if (rw.getByWorship().equals(sc.getChampionId()) && DateUtil.isSameDay(rw.getWorshipDate(), new Date())) {
				throw new NoteException(Messages.getString("CopyControler.36")); //$NON-NLS-1$
			}
		}
		// 膜拜成功
		// this.db.setWorshipCount(this.db.getWorshipCount() + 1);
		// this.db.setLastHudongDate(new Date());
		RoleCopy roleCopy = this.db.getRoleCopys().get(copyId);
		roleCopy.setHudongCount(roleCopy.getHudongCount() + 1);
		roleCopy.setLastHudongDate(new Date());
		RoleWorship roleWorship = new RoleWorship(GlobalDataManager.getInstance().generatePrimaryKey(), copyId,
				this.db, sc.getChampionId(), new Date());
		this.db.getWorships().add(roleWorship);
		int addCount = addCopyCount(copyId);
		this.rt.getWorshipRankControler().addWorshipRank(sc.getChampionId());
		return addCount;
	}

	/**
	 * 增加副本闯关次数
	 * 
	 * @param copyId
	 */
	private int addCopyCount(int copyId) {
		RoleCopy roleCopy = this.db.getRoleCopys().get(copyId);
		int old = roleCopy.getCountToday();
		roleCopy.setCountToday((short) (roleCopy.getCountToday() - 3));
		// 控制最多30次
		int maxCount = XsgCopyManager.getInstance().getMaxChallengeTime(copyId);
		if (maxCount - roleCopy.getCountToday() > 30) {
			roleCopy.setCountToday((short) (maxCount - 30));
		}
		roleCopy.setLastTime(new Date());
		return old - roleCopy.getCountToday();
	}

	@Override
	public void endChallengeTa(int resFlag) throws NoteException {
		ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(challengeTaCopyId);
		if (sc == null) {
			throw new NoteException(Messages.getString("CopyControler.37"));
		}
		if (resFlag == 1) {// 成功
			String byChallenge = sc.getChampionId();
			if (isHoldFull()) {
				GlobalDataManager.getInstance().removeServerCopy(sc.getTemplateId());
			} else {
				if (!isHasOccupy()) {
					// 首次占领奖励
					sendFirstAward();
				}
				sc.setChampionId(this.db.getId());
				RoleCopy roleCopy = db.getRoleCopys().get(challengeTaCopyId);
				roleCopy.setOccupyDate(new Date());
			}
			// GlobalDataManager.getInstance().afterHoldServerCopy(this.rt);
			SmallCopyT smallCopyT = XsgCopyManager.getInstance().findSmallCopyT(challengeTaCopyId);
			BigCopyT bigCopyT = XsgCopyManager.getInstance().findBigCopyT(smallCopyT.parentId);
			String difficultyStr = null;
			if (bigCopyT.difficulty == 0) {
				difficultyStr = Messages.getString("CopyControler.38");
			} else if (bigCopyT.difficulty == 1) {
				difficultyStr = Messages.getString("CopyControler.39");
			} else if (bigCopyT.difficulty == 2) {
				difficultyStr = Messages.getString("CopyControler.40");
			}
			String content = Messages.getString("CopyControler.41");
			content = XsgChatManager.getInstance().replaceRoleContent(content, this.rt);
			content = TextUtil.format(content, bigCopyT.name, smallCopyT.name, difficultyStr);
			this.rt.getChatControler().sendPrivateSystemMsg(content, byChallenge);
			this.challengeTaEvent.onChallengeTaWin(byChallenge);
			challengeTaCopyId = 0;
		} else {
			copyEndChallengeTaDate.put(challengeTaCopyId, new Date());
		}
	}

	@Override
	public BuyMilitaryOrderView getBuyMilitaryOrderView() {
		if (this.db.getBuyJunLingTime() != null) {
			if (DateUtil.isPass(XsgGameParamManager.getInstance().getJunLingResetTime(), this.db.getBuyJunLingTime())) {
				this.db.setBuyJunLingNum(0);
				this.db.setBuyJunLingTime(null);
			}
		}

		VipT vipT = this.rt.getVipController().getVipT();
		int yuanbao = 0, junling = 0;
		int already = this.db.getBuyJunLingNum();
		int maxBuyJunLingNum = vipT.maxBuyJunLingNum;
		if (already < maxBuyJunLingNum) {
			BuyJunLingT bjt = XsgCopyManager.getInstance().findJunLingT(already + 1);
			if (bjt != null) {
				yuanbao = bjt.yuanbao;
				junling = bjt.junling;
			}
		}

		return new BuyMilitaryOrderView(already, maxBuyJunLingNum, yuanbao, junling);
	}

	@Override
	public void buyMilitaryOrder() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		BuyMilitaryOrderView view = this.getBuyMilitaryOrderView();
		if (view.alreadyCount >= view.maxCount) {
			throw new NoteException(Messages.getString("CopyControler.42")); //$NON-NLS-1$
		}
		// chenshaozhi(陈绍治) 2015-05-09 10:37:02
		// 当前体力超过最大体力的20倍的时候，购买体力时，提示玩家：主公，您的军令太多了，不能再任性了。
		int current = this.rt.getItemControler().getItemCountInPackage(Const.PropertyName.JUNLING_TEMPLATE_ID);
		int max = XsgRoleManager.getInstance().getRoleLevelConfigT(this.rt.getLevel()).junlingLimit;
		if (current >= max * 20) {
			throw new NoteException(Messages.getString("CopyControler.43")); //$NON-NLS-1$
		}

		this.rt.reduceCurrency(new Money(CurrencyType.Yuanbao, view.yuanbao));
		this.rt.getItemControler().changeItemByTemplateCode(Const.PropertyName.JUNLING_TEMPLATE_ID, view.militaryOrder);
		this.db.setBuyJunLingNum(view.alreadyCount + 1);
		this.db.setBuyJunLingTime(Calendar.getInstance().getTime());

		// event
		this.buyJunLingEvent.onBuyJunLing(this.db.getBuyJunLingNum(), view.yuanbao, view.militaryOrder);
	}

	@Override
	public void getSmallCopyViewWithWarmup_async(final AMD_Copy_getSmallCopyViewWithWarmup __cb, int copyId)
			throws NoteException {
		if (!this.canChallenge(copyId)) {
			throw new NoteException(Messages.getString("CopyI.5"));
		}

		final int tokenCount = this.rt.getItemControler().getItemCountInPackage(
				XsgCopyManager.getInstance().getClearItemTemplate());
		final short maxChallengeTime = XsgCopyManager.getInstance().getMaxChallengeTime(copyId);
		final short remainTime = (short) this.getRemainTime(copyId);
		final int resetCount = this.getResetCountToday(copyId);

		final ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(copyId);
		final String resetTime = XsgCopyManager.getInstance().getNextResetTime();
		final int junLing = XsgCopyManager.getInstance().getJunLingConsume(copyId);

		// 随机热身赛
		List<WarmupView> list = new ArrayList<WarmupView>();
		this.randomWarmup();
		if (this.warmupContext.getView() != null) {
			list.add(this.warmupContext.getView());
		}
		final WarmupView[] warmupArray = list.toArray(new WarmupView[0]);

		List<String> asynIdList = new ArrayList<String>();
		if (!TextUtil.isBlank(this.warmupContext.getSnapshotRoleId())) {// 需要加载快照对应的玩家数据
			asynIdList.add(this.warmupContext.getSnapshotRoleId());
		}
		if (sc != null) {
			asynIdList.add(sc.getChampionId());
		}

		if (asynIdList.size() == 0) {
			__cb.ice_response(new SmallCopyViewWithWarmup(new SmallCopyView(remainTime, maxChallengeTime, tokenCount,
					false, Messages.getString("CopyI.DefaultCopyChampionName"), 0, resetCount, resetTime, //$NON-NLS-1$
					junLing), warmupArray));
			return;
		}

		XsgRoleManager.getInstance().loadRoleAsync(asynIdList, new Runnable() {

			@Override
			public void run() {
				if (!TextUtil.isBlank(warmupContext.getSnapshotRoleId()) && warmupArray.length > 0) {// 需要加载快照对应的玩家数据
					IRole opponent = XsgRoleManager.getInstance().findRoleById(warmupContext.getSnapshotRoleId());
					// 需要加载时候则数组中必须有一个元素
					warmupArray[0].opponent.opponentLevel = opponent.getLevel();
					warmupArray[0].opponent.opponentName = opponent.getName();
					warmupArray[0].opponent.opponentVip = opponent.getVipLevel();
					warmupArray[0].opponent.opponentIcon = opponent.getHeadImage();
					warmupArray[0].opponent.sex = opponent.getSex();
				}

				// 占领者信息
				IRole championRole = null;
				if (sc != null) {
					championRole = XsgRoleManager.getInstance().findRoleById(sc.getChampionId());
				}
				SmallCopyViewWithWarmup result = null;
				if (championRole == null) {
					result = new SmallCopyViewWithWarmup(new SmallCopyView(remainTime, maxChallengeTime, tokenCount,
							false, Messages.getString("CopyI.DefaultCopyChampionName"), 0, resetCount, resetTime, //$NON-NLS-1$
							junLing), warmupArray);
				} else {
					result = new SmallCopyViewWithWarmup(new SmallCopyView(remainTime, maxChallengeTime, tokenCount,
							true, championRole.getName(), championRole.getVipLevel(), resetCount, resetTime, junLing),
							warmupArray);
				}
				__cb.ice_response(result);
			}
		});
	}

	/**
	 * 随机生成热身赛数据，未命中或无匹配玩家则返回NULL
	 * 
	 * @return
	 */
	private void randomWarmup() {
		this.warmupContext = new WarmupContext();
		this.warmupContext.setState(WarmupState.Init);

		WarmupView view = null;
		WarmupT wt = XsgCopyManager.getInstance().findWarmupT(this.rt.getLevel());
		if (wt != null) { // 依次判断等级，当日总次数，触发率，记录上下文数据
			int todayCount = this.getWarmupTotalCountToday();
			if (todayCount < wt.maxCountOneDay && NumberUtil.isHit(wt.rate, 100)) {
				int myBattlePower = this.rt.getCachePower();
				int startPower = myBattlePower * wt.getMinPowerRate() / 100;
				int endPower = myBattlePower * wt.getMaxPowerRate() / 100;
				int startLevel = this.rt.getLevel() + wt.getMinLevel();
				int endLevel = this.rt.getLevel() + wt.getMaxLevel();
				List<BattlePowerSnapshotQueryResult> snapshotList = XsgFormationDataCollecterManager.getInstance()
						.queryFormationView(startPower, endPower, startLevel, endLevel);
				// 过滤自己作为对手
				CollectionUtil.removeWhere(snapshotList, new IPredicate<BattlePowerSnapshotQueryResult>() {

					@Override
					public boolean check(BattlePowerSnapshotQueryResult item) {
						return item.getRoleId().equals(rt.getRoleId());
					}
				});
				if (snapshotList.size() > 0) {
					BattlePowerSnapshotQueryResult snapshot = snapshotList.get(NumberUtil.random(snapshotList.size()));
					this.warmupContext.setSnapshotRoleId(snapshot.getRoleId());// 设置上下文中的对手ID，用于加载角色数据

					// 创建视图对象，部分数据需要数据线程加载完成后补齐
					view = new WarmupView(this.getWarmupWinCountToday(), this.getWarmupLoseCountToday(), XsgCopyManager
							.getInstance().randomWarmupDialog(), XsgRewardManager.getInstance().doTcToItem(rt, wt.tc),
							new WarmupOpponentView("", 0, 0, "", 0, snapshot.getPvpView()));
					this.db.setTotalWarmupCount(todayCount + 1);
					this.db.setWarmupUpdateTime(Calendar.getInstance().getTime());
				}
			}
		}

		this.warmupContext.setView(view);
	}

	/**
	 * 获取当日热身失败次数，如果更新时间与当前时间不是同一天，则重置计数
	 * 
	 * @return
	 */
	private int getWarmupLoseCountToday() {
		this.checkWarmupDate();
		return this.db.getLoseWarmupCount();
	}

	/**
	 * 获取当日热身胜利次数，如果更新时间与当前时间不是同一天，则重置计数
	 * 
	 * @return
	 */
	private int getWarmupWinCountToday() {
		this.checkWarmupDate();
		return this.db.getWinWarupCount();
	}

	/**
	 * 获取当日总热身次数，如果更新时间与当前时间不是同一天，则重置计数
	 * 
	 * @return
	 */
	private int getWarmupTotalCountToday() {
		this.checkWarmupDate();
		return this.db.getTotalWarmupCount();
	}

	/**
	 * 检查热身赛计数器更新时间
	 */
	private void checkWarmupDate() {
		Date now = Calendar.getInstance().getTime();
		if (this.db.getWarmupUpdateTime() != null && !DateUtil.isSameDay(this.db.getWarmupUpdateTime(), now)) {
			this.db.setTotalWarmupCount(0);
			this.db.setWinWarupCount(0);
			this.db.setLoseWarmupCount(0);
		}
	}

	@Override
	public String beginWarmup() throws NoteException {
		if (this.warmupContext == null || this.warmupContext.getState() != WarmupState.Init
				|| this.warmupContext.getView() == null) {
			throw new NoteException(Messages.getString("CopyControler.UneffectOperation"));
		}

		this.warmupContext.setState(WarmupState.Battle);
		String movieId = XsgFightMovieManager.getInstance().generateMovieId(Type.CopyWarmup, rt, "",
				this.warmupContext.getView().opponent.opponent);
		this.warmupContext.setMovieId(movieId);

		return movieId;
	}

	@Override
	public byte endWarmup(byte remainHero) throws NoteException {
		if (this.warmupContext == null || this.warmupContext.getState() != WarmupState.Battle
				|| this.warmupContext.getView() == null) {
			throw new NoteException(Messages.getString("CopyControler.UneffectOperation"));
		}

		byte star = XsgCopyManager.getInstance().calculateStar(
				this.rt.getFormationControler().getDefaultFormation().getHeroCountIncludeSupport(), remainHero);
		boolean win = star > 0;
		XsgFightMovieManager.getInstance().endFightMovie(this.rt.getRoleId(), this.warmupContext.getMovieId(),
				win ? 1 : 0, remainHero);
		this.warmupContext.setState(WarmupState.End);
		if (win) {// 赢了加胜利次数及结算奖励邮件
			this.db.setWinWarupCount(this.db.getWinWarupCount() + 1);

			Map<String, Integer> rewardMap = new HashMap<String, Integer>();
			for (ItemView iv : this.warmupContext.getView().items) {
				rewardMap.put(iv.templateId, iv.num);
			}
			this.rt.getMailControler().receiveRoleMail(MailTemplate.CopyWarmup, rewardMap);
		} else {
			this.db.setLoseWarmupCount(this.db.getLoseWarmupCount() + 1);
		}

		return star;
	}

	@Override
	public String cancelWarmup(boolean first) throws NoteException {
		if (first && this.getWarmupEscapeCount() > 0
				&& ((this.getWarmupEscapeCount() + 1) % XsgCopyManager.getInstance().getEscapePopCount() == 0)) {
			return Messages.getString("CopyControler.9")
					.replace("{0}", String.valueOf(this.getWarmupEscapeCount() + 1));
		}

		this.addWarmupEscapeCount();
		warmupEscapeEvent.onEscape();
		String title = XsgCopyManager.getInstance().getEscapePopMessage(this.getWarmupEscapeCount());
		if (StringUtils.isNotBlank(title)) {
			XsgChatManager manager = XsgChatManager.getInstance();
			ChatAdT adt = manager.getAdContentMap(XsgChatManager.AdContentType.WarmupEscape).get(0);
			String content = manager.replaceRoleContent(adt.content, rt);
			content = content.replace("{0}", this.getWarmupEscapeCount() + "");
			content = content.replace("{1}", title);
			manager.sendAnnouncement(content);
		}

		return "";
	}

	private void addWarmupEscapeCount() {
		Date now = Calendar.getInstance().getTime();
		if (db.getWarmupEscapeTime() != null && DateUtil.isSameDay(now, db.getWarmupEscapeTime())) {
			db.setWarmupEscapeCount(db.getWarmupEscapeCount() + 1);
		} else {
			db.setWarmupEscapeCount(1);
		}
		db.setWarmupEscapeTime(now);
	}

	private int getWarmupEscapeCount() {
		Date now = Calendar.getInstance().getTime();
		if (db.getWarmupEscapeTime() != null && DateUtil.isSameDay(now, db.getWarmupEscapeTime())) {
			return db.getWarmupEscapeCount();
		}
		return 0;
	}

	@Override
	public int levyCopy(int copyId) throws NoteException {
		ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(copyId);
		if (sc == null) {
			throw new NoteException(Messages.getString("CopyControler.32"));
		}
		if (!sc.getChampionId().equals(rt.getRoleId())) {
			throw new NoteException(Messages.getString("CopyControler.copyNotOccupy"));
		}
		Date levyTime = XsgGameParamManager.getInstance().getServerCopyRewardTime();
		Date levyDate = DateUtil.joinTime(DateUtil.format(levyTime, "HH:mm:ss"));
		RoleCopy roleCopy = this.db.getRoleCopys().get(copyId);
		if (roleCopy.getLevyDate() != null && roleCopy.getLevyDate().getTime() >= levyDate.getTime()) {
			throw new NoteException(Messages.getString("CopyControler.copyLevyed"));
		}
		if (!isCanLevy(copyId)) {
			throw new NoteException(Messages.getString("CopyControler.copyNotLevy"));
		}
		int addCount = addCopyCount(copyId);
		roleCopy.setLevyDate(new Date());
		return addCount;
	}

	@Override
	public void giveCopy(int copyId) throws NoteException {
		ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(copyId);
		if (sc == null) {
			throw new NoteException(Messages.getString("CopyControler.32"));
		}
		if (!sc.getChampionId().equals(rt.getRoleId())) {
			throw new NoteException(Messages.getString("CopyControler.copyNotOccupy"));
		}
		GlobalDataManager.getInstance().removeServerCopy(copyId);
	}

	@Override
	public CopyOccupy[] myOccupyList() throws NoteException {
		List<CopyOccupy> list = new ArrayList<CopyOccupy>();
		List<ServerCopy> serverCopies = GlobalDataManager.getInstance().getAllServerCopy();
		for (ServerCopy c : serverCopies) {
			if (!c.getChampionId().equals(rt.getRoleId()) || !db.getRoleCopys().containsKey(c.getTemplateId())) {
				continue;
			}
			SmallCopyT smallCopyT = XsgCopyManager.getInstance().findSmallCopyT(c.getTemplateId());
			BigCopyT bigCopyT = XsgCopyManager.getInstance().findBigCopyT(smallCopyT.parentId);
			list.add(new CopyOccupy(c.getTemplateId(), bigCopyT.difficulty, bigCopyT.id, bigCopyT.name,
					smallCopyT.name, isCanLevy(c.getTemplateId())));
		}
		Collections.sort(list, new Comparator<CopyOccupy>() {

			@Override
			public int compare(CopyOccupy o1, CopyOccupy o2) {
				int i = o1.diff - o2.diff;
				if (i == 0) {
					i = o1.sectionId - o2.sectionId;
				}
				if (i == 0) {
					i = o1.copyId - o2.copyId;
				}
				return i;
			}
		});
		return list.toArray(new CopyOccupy[0]);
	}

	/**
	 * 关卡是否可征收
	 * 
	 * @param copyId
	 * @return
	 */
	private boolean isCanLevy(int copyId) {
		Date levyTime = XsgGameParamManager.getInstance().getServerCopyRewardTime();
		RoleCopy roleCopy = this.db.getRoleCopys().get(copyId);
		if (roleCopy.getOccupyDate() == null) {
			roleCopy.setOccupyDate(new Date());
		}
		// 当天占领还没到征收时间
		Date levyDate = DateUtil.joinTime(DateUtil.format(levyTime, "HH:mm:ss"));
		if (DateUtil.isSameDay(new Date(), roleCopy.getOccupyDate()) && new Date().getTime() < levyDate.getTime()) {
			return false;
		}
		if (roleCopy.getLevyDate() == null || DateUtil.isPass(levyTime, roleCopy.getLevyDate())) {
			return true;
		}
		return false;
	}

	/**
	 * 关卡占领是否已满
	 * 
	 * @return
	 */
	private boolean isHoldFull() {
		List<ServerCopy> serverCopies = GlobalDataManager.getInstance().getAllServerCopy();
		int count = 0;
		for (ServerCopy s : serverCopies) {
			if (s.getChampionId().equals(rt.getRoleId())) {
				count++;
			}
		}
		VipT vt = XsgVipManager.getInstance().findVipT(rt.getVipLevel());
		return count >= vt.maxServerCopy;
	}

	/**
	 * 发放首次占领奖励
	 */
	private void sendFirstAward() {
		ServerCopyFirstT copyFirstT = XsgCopyManager.getInstance().getCopyFirstT();
		String[] its = copyFirstT.items.split(",");
		Property[] pro = new Property[its.length];
		for (int j = 0; j < pro.length; j++) {
			String[] id_num = its[j].split(":");
			pro[j] = new Property(id_num[0], Integer.parseInt(id_num[1]));
		}
		MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
				.get(MailTemplate.CopyFirstOccupy.value());
		// 邮件发放奖励
		XsgMailManager.getInstance().sendMail(
				new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName, rt
						.getRoleId(), mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance()
						.serializeMailAttach(pro), Calendar.getInstance().getTime()));
	}

	@Override
	public void setCopyProgress(int diff, int copyId) {
		int firstId = 0;
		if (diff == CopyDifficulty.Junior.value()) {
			// 普通
			firstId = XsgCopyManager.getFirstCopyId(CopyDifficulty.Junior);
		} else if (diff == CopyDifficulty.Senior.value()) {
			// 高手
			firstId = XsgCopyManager.getFirstCopyId(CopyDifficulty.Senior);
		} else {
			// 大神
			firstId = XsgCopyManager.getFirstCopyId(CopyDifficulty.Top);
		}

		int tempId = firstId;
		RoleCopy rc = this.db.getRoleCopys().get(tempId);
		while (rc != null && rc.getStar() > 0) {
			if (tempId > copyId) {
				rc.setStar((byte) 0);// 关闭指定关卡之后的关卡
			}
			tempId = XsgCopyManager.getInstance().findSmallCopyT(tempId).nextId;
			rc = this.db.getRoleCopys().get(tempId);
		}

		while (firstId != 0 && firstId <= copyId) {
			rc = this.db.getRoleCopys().get(firstId);
			if (rc == null) {
				rc = new RoleCopy(GlobalDataManager.getInstance().generatePrimaryKey(), db, firstId, new Date());
				this.db.getRoleCopys().put(rc.getTemplateId(), rc);
			}
			rc.setStar((byte) 5);
			firstId = XsgCopyManager.getInstance().findSmallCopyT(firstId).nextId;
		}

		this.calculateProgress();
	}

	@Override
	public void autoChallengeTa(final AMD_Copy_autoChallengeTa __cb, final int copyId) {
		final ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(copyId);
		if (sc == null) {
			__cb.ice_exception(new NoteException(Messages.getString("CopyControler.37")));
			return;
		}
		if (sc.getChampionId().equals(rt.getRoleId())) {
			__cb.ice_exception(new NoteException(Messages.getString("CopyI.5")));
			return;
		}
		rt.getCopyControler().resetHudongCount(copyId);
		if (rt.getCopyControler().getChallengeTaCount() <= 0) {
			__cb.ice_exception(new NoteException(Messages.getString("CopyI.6")));
			return;
		}
		XsgRoleManager.getInstance().loadRoleByIdAsync(sc.getChampionId(), new Runnable() {
			@Override
			public void run() {
				final IRole rivalRole = XsgRoleManager.getInstance().findRoleById(sc.getChampionId());
				final ChallengeTaView challengeTaView = rt.getCopyControler().getChallengeTaView(rivalRole, copyId);

				String myFormationId = rt.getFormationControler().getDefaultFormation().getId();
				final PvpOpponentFormationView myFormationView = rt.getFormationControler()
						.getPvpOpponentFormationView(myFormationId);

				int type = XsgFightMovieManager.getInstance().getFightLifeT(Type.Challenge.ordinal()).id;
				final CrossPvpView pvpView = new CrossPvpView(type, new CrossRoleView(rt.getRoleId(), rt.getName(), rt
						.getHeadImage(), rt.getLevel(), rt.getVipLevel(), rt.getServerId(), rt.getSex(), ""),
						myFormationView, new CrossRoleView(rivalRole.getRoleId(), rivalRole.getName(), rivalRole
								.getHeadImage(), rivalRole.getLevel(), rivalRole.getVipLevel(),
								rivalRole.getServerId(), rivalRole.getSex(), ""), challengeTaView.formationView, "", 0);

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
								int resFlag = movie.winRoleId.equals(rt.getRoleId()) ? 1 : 0;

								int star = XsgCopyManager.getInstance().calculateStar(
										rt.getFormationControler().getDefaultFormation().getHeroCountIncludeSupport(),
										(byte) movie.selfHeroNum);
								if (resFlag == 0) {
									star = 0;
								}

								String movieId = XsgFightMovieManager.getInstance().addFightMovie(
										Type.Challenge,
										rt.getRoleId(),
										rivalRole.getRoleId(),
										1,
										(byte) movie.selfHeroNum,
										myFormationView,
										challengeTaView.formationView,
										new FightMovieView(resFlag, star, movie.soloMovie, movie.fightMovie,
												new byte[0]));
								try {
									endChallengeTa(resFlag);
								} catch (NoteException e) {
									__cb.ice_exception(e);
									return;
								}
								ChallengeTaAutoResult result = new ChallengeTaAutoResult(movie.soloMovie,
										movie.fightMovie, star, resFlag, movieId, rivalRole.getName(), rivalRole
												.getVipLevel(), movie.winType);
								__cb.ice_response(result);
							}
						});
					}
				});
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CopyI.7")));
			}
		});
	}
}