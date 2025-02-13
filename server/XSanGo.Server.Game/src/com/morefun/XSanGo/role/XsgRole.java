/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.role;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.XSanGo.Protocol.AccountView;
import com.XSanGo.Protocol.ActivityAnnounceView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.ExtendObject;
import com.XSanGo.Protocol.HeroEquipView;
import com.XSanGo.Protocol.HeroSkillPointView;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RoleView;
import com.XSanGo.Protocol.RoleViewForGM;
import com.XSanGo.Protocol.RoleViewForOtherPlayer;
import com.morefun.XSanGo.AsynSaver;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.IAsynSavable;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ArenaRank.CrossArenaControler;
import com.morefun.XSanGo.ArenaRank.IArenaRankControler;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.AttackCastle.IAttackCastleController;
import com.morefun.XSanGo.AttackCastle.XsgAttackCastleManager;
import com.morefun.XSanGo.MFBI.IMFBIControler;
import com.morefun.XSanGo.MFBI.XsgMFBIManager;
import com.morefun.XSanGo.achieve.IAchieveControler;
import com.morefun.XSanGo.achieve.XsgAchieveManager;
import com.morefun.XSanGo.activity.AnnounceT;
import com.morefun.XSanGo.activity.FootballControler;
import com.morefun.XSanGo.activity.IActivityControler;
import com.morefun.XSanGo.activity.IAnnounceControler;
import com.morefun.XSanGo.activity.IBigDayChargeControler;
import com.morefun.XSanGo.activity.IBigDayConsumeControler;
import com.morefun.XSanGo.activity.IBigSumChargeActivityControler;
import com.morefun.XSanGo.activity.IBigSumConsumeActivityControler;
import com.morefun.XSanGo.activity.ICornucopiaControler;
import com.morefun.XSanGo.activity.IDayChargeControler;
import com.morefun.XSanGo.activity.IDayConsumeControler;
import com.morefun.XSanGo.activity.IDayLoginControler;
import com.morefun.XSanGo.activity.IDayforverLoginControler;
import com.morefun.XSanGo.activity.IFirstJiaControler;
import com.morefun.XSanGo.activity.IFortuneWheelControler;
import com.morefun.XSanGo.activity.IFundControler;
import com.morefun.XSanGo.activity.IInviteActivityControler;
import com.morefun.XSanGo.activity.ILevelRewardControler;
import com.morefun.XSanGo.activity.ILotteryControler;
import com.morefun.XSanGo.activity.IMakeVipControler;
import com.morefun.XSanGo.activity.IOpenServerActiveControler;
import com.morefun.XSanGo.activity.IPowerRewardControler;
import com.morefun.XSanGo.activity.ISeckillControler;
import com.morefun.XSanGo.activity.ISendJunLingControler;
import com.morefun.XSanGo.activity.IShareControler;
import com.morefun.XSanGo.activity.IShootControler;
import com.morefun.XSanGo.activity.ISumChargeActivityControler;
import com.morefun.XSanGo.activity.ISumConsumeActivityControler;
import com.morefun.XSanGo.activity.ResourceBackControler;
import com.morefun.XSanGo.activity.XsgActivityManage;
import com.morefun.XSanGo.activity.XsgAnnounceManager;
import com.morefun.XSanGo.activity.XsgLotteryManage;
import com.morefun.XSanGo.activity.XsgShareManage;
import com.morefun.XSanGo.api.ApiController;
import com.morefun.XSanGo.api.IApiController;
import com.morefun.XSanGo.api.XsgApiManager;
import com.morefun.XSanGo.auction.IAuctionHouseController;
import com.morefun.XSanGo.auction.XsgAuctionHouseManager;
import com.morefun.XSanGo.buyJinbi.IBuyJInbiControler;
import com.morefun.XSanGo.buyJinbi.XsgBuyJInbiManager;
import com.morefun.XSanGo.center.XsgCenterManager;
import com.morefun.XSanGo.chat.IChatControler;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.collect.ICollectHeroSoulController;
import com.morefun.XSanGo.collect.XsgCollectHeroSoulManager;
import com.morefun.XSanGo.colorfulEgg.IColorfullEggController;
import com.morefun.XSanGo.colorfulEgg.XsgColorfullEggManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.copy.ICopyControler;
import com.morefun.XSanGo.copy.IWorshipRankControler;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.copy.XsgWorshipManage;
import com.morefun.XSanGo.crossServer.ITournamentController;
import com.morefun.XSanGo.crossServer.XsgTournamentManager;
import com.morefun.XSanGo.db.game.FriendApplyingHistory;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleAnnounce;
import com.morefun.XSanGo.db.game.RoleCDKRecord;
import com.morefun.XSanGo.db.game.RoleFaction;
import com.morefun.XSanGo.db.game.RoleFriendsRecalled;
import com.morefun.XSanGo.db.game.RoleInviteLog;
import com.morefun.XSanGo.db.game.RoleOpenedMenu;
import com.morefun.XSanGo.db.game.RoleSns;
import com.morefun.XSanGo.db.game.RoleVip;
import com.morefun.XSanGo.db.game.RoleWeixinShare;
import com.morefun.XSanGo.db.game.RoleWorldBoss;
import com.morefun.XSanGo.dreamland.DreamlandController;
import com.morefun.XSanGo.dreamland.IDreamlandController;
import com.morefun.XSanGo.dreamland.XsgDreamlandManager;
import com.morefun.XSanGo.equip.IArtifactControler;
import com.morefun.XSanGo.equip.XsgEquipManager;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.XsgEventManager;
import com.morefun.XSanGo.event.protocol.IAddSalary;
import com.morefun.XSanGo.event.protocol.IGuideComplete;
import com.morefun.XSanGo.event.protocol.IJinbiChange;
import com.morefun.XSanGo.event.protocol.IOffline;
import com.morefun.XSanGo.event.protocol.IRoleHeadAndBorderChange;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.event.protocol.IRoleNameChange;
import com.morefun.XSanGo.event.protocol.IRoleReset;
import com.morefun.XSanGo.event.protocol.ISkillPointBuy;
import com.morefun.XSanGo.event.protocol.IYuanbaoChange;
import com.morefun.XSanGo.faction.IFactionControler;
import com.morefun.XSanGo.faction.XsgFactionManager;
import com.morefun.XSanGo.faction.factionBattle.FactionBattleController;
import com.morefun.XSanGo.faction.factionBattle.IFactionBattleController;
import com.morefun.XSanGo.faction.factionBattle.XsgFactionBattleManager;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.formation.IFormationControler;
import com.morefun.XSanGo.formation.XsgFormationManager;
import com.morefun.XSanGo.formation.datacollector.IFormationDataCollecter;
import com.morefun.XSanGo.formation.datacollector.XsgFormationDataCollecterManager;
import com.morefun.XSanGo.friendsRecall.FriendsRecallCfgT;
import com.morefun.XSanGo.friendsRecall.FriendsRecallController;
import com.morefun.XSanGo.friendsRecall.XsgFriendsRecallManager;
import com.morefun.XSanGo.goodsExchange.IExchangeActivityControler;
import com.morefun.XSanGo.haoqingbao.HaoqingbaoController;
import com.morefun.XSanGo.haoqingbao.IHaoqingbaoController;
import com.morefun.XSanGo.haoqingbao.XsgHaoqingbaoManager;
import com.morefun.XSanGo.hero.BuyHeroSkillT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.IHeroControler;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.hero.market.HeroMarketManager;
import com.morefun.XSanGo.hero.market.IHeroMarketControler;
import com.morefun.XSanGo.heroAdmire.IHeroAdmireControler;
import com.morefun.XSanGo.heroAdmire.XsgHeroAdmireManager;
import com.morefun.XSanGo.heroAwaken.HeroAwakenController;
import com.morefun.XSanGo.heroAwaken.IHeroAwakenController;
import com.morefun.XSanGo.heroAwaken.XsgHeroAwakenManager;
import com.morefun.XSanGo.item.IItemControler;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.itemChip.IItemChipControler;
import com.morefun.XSanGo.itemChip.XsgItemChipManager;
import com.morefun.XSanGo.ladder.ILadderControler;
import com.morefun.XSanGo.ladder.LadderLevelT;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.luckybag.ILuckyBagControler;
import com.morefun.XSanGo.luckybag.XsgLuckyBagManager;
import com.morefun.XSanGo.mail.IMailControler;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.makewine.IMakeWineControler;
import com.morefun.XSanGo.makewine.MakeWineControler;
import com.morefun.XSanGo.monitor.XsgMonitorManager;
import com.morefun.XSanGo.net.GameSessionI;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.notify.INotifyControler;
import com.morefun.XSanGo.notify.XsgNotifyManager;
import com.morefun.XSanGo.onlineAward.IOnlineAwardControler;
import com.morefun.XSanGo.onlineAward.XsgOnlineAwardManager;
import com.morefun.XSanGo.partner.IPartnerControler;
import com.morefun.XSanGo.partner.XsgPartnerManager;
import com.morefun.XSanGo.rankList.IRankListControler;
import com.morefun.XSanGo.rankList.XsgRankListManager;
import com.morefun.XSanGo.reward.IRewardControler;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.shop.IShopControler;
import com.morefun.XSanGo.shop.XsgShopManage;
import com.morefun.XSanGo.sign.ISignController;
import com.morefun.XSanGo.sign.SignController;
import com.morefun.XSanGo.smithyExchange.ISmithyExchangeController;
import com.morefun.XSanGo.smithyExchange.XsgSmithyManager;
import com.morefun.XSanGo.sns.ISnsControler;
import com.morefun.XSanGo.sns.SnsController;
import com.morefun.XSanGo.sns.XsgSnsManager;
import com.morefun.XSanGo.stat.IStatControler;
import com.morefun.XSanGo.stat.XsgStatManager;
import com.morefun.XSanGo.superCharge.ISuperChargeController;
import com.morefun.XSanGo.superCharge.XsgSuperChargeManager;
import com.morefun.XSanGo.superRaffle.ISuperRaffleController;
import com.morefun.XSanGo.superRaffle.XsgSuperRaffleManager;
import com.morefun.XSanGo.task.ITaskControler;
import com.morefun.XSanGo.task.XsgTaskManager;
import com.morefun.XSanGo.temp.FixRewardForBuy10WineByYuanbao;
import com.morefun.XSanGo.temp.IFourthTimeTestForBeforeControler;
import com.morefun.XSanGo.temp.XsgFourthTimeTestManger;
import com.morefun.XSanGo.temp.XsgTemporaryRDActivityManager;
import com.morefun.XSanGo.timeBattle.ITimeBattleControler;
import com.morefun.XSanGo.timeBattle.XsgTimeBattleManage;
import com.morefun.XSanGo.trader.ITraderControler;
import com.morefun.XSanGo.trader.XsgTraderManager;
import com.morefun.XSanGo.treasure.ITreasureControler;
import com.morefun.XSanGo.treasure.XsgTreasureManage;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.IRecoverable;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RecoveryUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.IVipController;
import com.morefun.XSanGo.vip.VipT;
import com.morefun.XSanGo.vip.XsgVipManager;
import com.morefun.XSanGo.worldboss.IWorldBossControler;
import com.morefun.XSanGo.worldboss.WorldBossManager;

import edu.emory.mathcs.backport.java.util.Collections;

public class XsgRole implements IRole, IAsynSavable {
	/** 合服后的角色名判定规则 */
	private static Pattern combineServerNamePattern = Pattern.compile("^s\\d+\\.\\S+$"); //$NON-NLS-1$

	private Role db;

	/** 是否在线 */
	private boolean online;

	private AsynSaver saver;

	private DelayedTask saveTimer;

	/** 下线事件 */
	private IOffline offlineEvent;

	/** 金币变更事件 */
	private IJinbiChange jinbiEvent;

	/** 物品控制器 */
	private IItemControler itemControler;

	/** 事件控制 */
	private IEventControler eventControler;

	/** 伙伴控制器 */
	private IHeroControler heroControler;

	/** 军团/队伍控制器 */
	private IFormationControler formationControler;

	/** 副本控制器 */
	private ICopyControler copyControler;

	/** 邮件控制 */
	private IMailControler mailControler;

	/** 公会控制 */
	private IFactionControler factionControler;

	/** 社交控制器 */
	private ISnsControler snsControler;

	/** 签到控制器 */
	private ISignController signControler;

	/** VIP功能模块 */
	private IVipController vipControler;

	/** 聊天控制器 */
	private IChatControler chatControler;

	/** 奖励控制模块 */
	private IRewardControler rewardControler;

	/** 通知模块 */
	private INotifyControler notifyControler;

	/** 武将抽卡系统 */
	private IHeroMarketControler heroMarketControler;

	/** 物品碎片 */
	private IItemChipControler itemChipControler;

	/** 神秘商人模块 */
	private ITraderControler traderControler;

	/** VIP功能模块 */
	private ITaskControler taskControler;

	/** 成就功能模块 */
	private IAchieveControler achieveControler;

	/** 大富温功能模块 */
	private ILotteryControler lotteryControler;

	/** 分享功能模块 */
	private IShareControler shareControler;

	/** 全服功能模块 */
	private IOpenServerActiveControler openServerActiveControler;

	/** 点金手功能模块 */
	private IBuyJInbiControler buyJInbiControler;

	/** 活动模块 */
	private IActivityControler activityControler;

	/** 在线礼包功能模块 */
	private IOnlineAwardControler onlineAwardControler;

	private IShopControler shopControler;

	private ITimeBattleControler timeBattleControler;

	private IWorshipRankControler worshipRankControler;

	private IStatControler statControler;

	private IMakeVipControler makeVipControler;

	/** 名将召唤功能模块 */
	private ICollectHeroSoulController collectHeroSoulControler;

	/** 北伐 */
	private IAttackCastleController attackCastleController;

	/** 拍卖行 */
	private IAuctionHouseController auctionHouseController;

	/** 酿酒模块 */
	private IMakeWineControler makeWineControler;

	/**
	 * 需要定时通知的红点
	 */
	private List<IRedPointNotable> timerRedPointList = new ArrayList<IRedPointNotable>();
	/**
	 * 不用定时通知，只在特定时间点通知的红点
	 */
	private List<IRedPointNotable> noTimerRedPointList = new ArrayList<IRedPointNotable>();

	/** 竞技场排行 */
	private IArenaRankControler arenaRankControler;

	/** 铁匠铺兑换 */
	private ISmithyExchangeController smithyControler;

	/** 彩蛋 */
	private IColorfullEggController colorfullEggController;

	/** 购买武将技能点事件 */
	private ISkillPointBuy buySkillPointEvent;

	/** 主公升级事件 */
	private IRoleLevelup levelUpEvent;

	/** 帐号重置事件 */
	private IRoleReset resetEvent;

	/** 各种 排行榜 */
	private IRankListControler rankListControler;

	private IRoleNameChange renameEvent;

	private IRoleHeadAndBorderChange headAndBorderChageEvent;

	private IAddSalary addSalaryEvent;

	private IYuanbaoChange yuanbaoEvent;

	private IGuideComplete guideEvent;

	private IMFBIControler MFBIControler;

	private ILadderControler ladderControler;

	private IHeroAdmireControler heroAdmireControler;

	private ISumChargeActivityControler sumChargeActivityControler;

	private ISumConsumeActivityControler sumConsumeActivityControler;

	private IFormationDataCollecter formationCollecter;

	private IInviteActivityControler inviteActivityControler;

	private IFundControler fundControler;

	private ILevelRewardControler levelRewardControler;

	private IFourthTimeTestForBeforeControler fourthTimeTestForBefore;

	private ISeckillControler seckillControler;

	private IDayChargeControler dayChargeControler;

	private IDayConsumeControler dayConsumeControler;

	private IPowerRewardControler powerRewardControler;

	private IFirstJiaControler firstJiaControler;

	private IDayLoginControler dayLoginControler;

	private IDayforverLoginControler dayforverLoginControler;

	private ISendJunLingControler sendJunLingControler;

	private IBigDayChargeControler bigDayChargeControler;

	private IBigDayConsumeControler bigDayConsumeControler;

	private IBigSumChargeActivityControler bigSumChargeActivityControler;

	private IBigSumConsumeActivityControler bigSumConsumeActivityControler;

	private IFortuneWheelControler fortuneWheelControler;

	private ILuckyBagControler luckyBagControler;

	private ICornucopiaControler cornucopiaControler;

	// private ILevelWealControler levelWealControler;
	private IPartnerControler partnerControler;

	private ISuperChargeController superChargeController;

	private ISuperRaffleController superRaffleController;

	/**
	 * 寻宝
	 */
	private ITreasureControler treasureControler;

	/** 兑换活动控制器 */
	private IExchangeActivityControler exchangeActivityControler;

	// 豪情宝
	private HaoqingbaoController haoqingbaoController;

	/** 老友召回 */
	private FriendsRecallController friendsRecallController;

	/** 武将觉醒控制器 */
	private HeroAwakenController heroAwakenController;

	/** 公会战控制器 */
	private FactionBattleController factionBattleController;

	/** 南华幻境 */
	private DreamlandController dreamlandController;

	/** api */
	private ApiController apiController;

	private IWorldBossControler worldBossControler;

	/** 百步穿杨控制器 */
	private IShootControler shootControler;

	/** 比武大会 */
	private ITournamentController tournamentController;

	private IAnnounceControler announceControler;
	
	private IArtifactControler artifactControler;

	/** 战力控制 */
	private CombatPowerControler combatPowerControler;

	// 资源找回
	private ResourceBackControler resourceBackControler;

	private CrossArenaControler crossArenaControler;

	private FootballControler footballControler;

	public XsgRole(Role role) {
		this.db = role;
		this.saver = new AsynSaver(this);

		// ===================各模块的初始化以及预处理函数调用==================================

		this.eventControler = XsgEventManager.getInstance().createEventControler(this, this.db);

		// 初始化道具
		this.itemControler = XsgItemManager.getInstance().createControler(this, this.db);
		artifactControler = XsgEquipManager.getInstance().createArtifactControler(this, role);
		// 公会
		this.factionControler = XsgFactionManager.getInstance().createFactionControler(this, this.db);
		// 初始化武将
		this.heroControler = XsgHeroManager.getInstance().createHeroControler(this, this.db);
		this.heroControler.initHeroAttendant();
		this.heroControler.checkData();

		// 初始化军团
		this.formationControler = XsgFormationManager.getInstance().createFormationControler(this, this.db);
		this.formationControler.checkData();

		// 副本
		this.copyControler = XsgCopyManager.getInstance().createCopyControler(this, this.db);
		// 邮件
		this.mailControler = XsgMailManager.getInstance().createMailControler(this, db);

		// 聊天
		this.chatControler = XsgChatManager.getInstance().createChatControler(this, db);
		// 奖励
		this.rewardControler = XsgRewardManager.getInstance().createRewardControler(this, db);
		// 通知
		this.notifyControler = XsgNotifyManager.getInstance().createNotifyControler(this);
		// 抽卡
		this.heroMarketControler = HeroMarketManager.getInstance().createHeroMarketControler(this, this.db);
		// 物品碎片
		this.itemChipControler = XsgItemChipManager.getInstance().createChatControler(this, db);
		// 活动
		this.activityControler = XsgActivityManage.getInstance().createActivityControler(this, db);
		this.shopControler = XsgShopManage.getInstance().createShopControler(this, db);
		this.timeBattleControler = XsgTimeBattleManage.getInstance().createTimeBattleControler(this, db);
		
		snsControler = new SnsController(this, db);

		signControler = new SignController(this, db);

		this.traderControler = XsgTraderManager.getInstance().createTraderControler(this, this.db);

		// VIP
		this.vipControler = XsgVipManager.getInstance().createVipControler(this, this.db);
		// 任务
		this.taskControler = XsgTaskManager.getInstance().createTaskControler(this, this.db);
		// 成就
		this.achieveControler = XsgAchieveManager.getInstance().createAchieveControler(this, this.db);

		// 大富温
		this.lotteryControler = XsgLotteryManage.getInstance().createLotteryControler(this, this.db);
		// 全服活动
		this.shareControler = XsgShareManage.getInstance().createShareControler(this, this.db);

		// 全服活动
		this.openServerActiveControler = XsgActivityManage.getInstance().createOpenServerActiveControler(this, this.db);

		// 点金手
		this.buyJInbiControler = XsgBuyJInbiManager.getInstance().createBuyJInbiControler(this, this.db);

		// 在线礼包
		this.onlineAwardControler = XsgOnlineAwardManager.getInstance().create(this, this.db);

		// 战力快照采集器
		this.formationCollecter = XsgFormationDataCollecterManager.getInstance()
				.createFormationCollecter(this, this.db);

		// 竞技场排行
		this.arenaRankControler = XsgArenaRankManager.getInstance().create(this, this.db);

		// 铁匠铺
		this.smithyControler = XsgSmithyManager.getInstance().create(this, this.db);

		// 彩蛋
		this.colorfullEggController = XsgColorfullEggManager.getInstance().createEggController(this, this.db);

		this.checkData();

		this.worshipRankControler = XsgWorshipManage.getInstance().createWorshipRankControler(this);

		this.rankListControler = XsgRankListManager.getInstance().create(this, this.db);

		this.statControler = XsgStatManager.getInstance().createStatControler(this);
		this.MFBIControler = XsgMFBIManager.getInstance().createMFBIControler(this, this.db);

		this.makeVipControler = XsgActivityManage.getInstance().createMakeVipControler(this, this.db);
		// 累计充值和消费
		this.sumChargeActivityControler = XsgActivityManage.getInstance().createSumChargeControler(this, this.db);
		this.sumConsumeActivityControler = XsgActivityManage.getInstance().createSumConsumeControler(this, this.db);
		this.dayChargeControler = XsgActivityManage.getInstance().createDayChargeControler(this, this.db);
		this.dayConsumeControler = XsgActivityManage.getInstance().createDayConsumeControler(this, this.db);

		// 名将召唤
		this.collectHeroSoulControler = XsgCollectHeroSoulManager.getInstance().createCollectHeroSoulControler(this,
				this.db);
		// 群雄争霸
		this.ladderControler = XsgLadderManager.getInstance().createLadderControler(this, this.db);
		// 名将仰慕
		this.heroAdmireControler = XsgHeroAdmireManager.getInstance().createBuyJInbiControler(this, this.db);

		// 北伐
		this.attackCastleController = XsgAttackCastleManager.getInstance().createAttackCastleController(this, this.db);

		// 拍卖行
		this.auctionHouseController = XsgAuctionHouseManager.getInstance().createAuctionHouseController(this, this.db);
		// 邀请好友
		this.inviteActivityControler = XsgActivityManage.getInstance().createInviteActivityControler(this, this.db);
		this.seckillControler = XsgActivityManage.getInstance().createSeckillControler(this, this.db);

		// 成长基金
		this.fundControler = XsgActivityManage.getInstance().createFundControler(this, this.db);

		// 冲级有奖
		this.levelRewardControler = XsgActivityManage.getInstance().createLevelRewardControler(this, this.db);

		// 战力嘉奖
		this.powerRewardControler = XsgActivityManage.getInstance().createPowerRewardControler(this, this.db);

		// 封测活动，内测返还
		this.fourthTimeTestForBefore = XsgFourthTimeTestManger.getInstance().createControler(this, this.db);

		// 第一佳活动
		this.firstJiaControler = XsgActivityManage.getInstance().createFirstJiaControler(this, db);

		// 每日登录活动
		this.dayLoginControler = XsgActivityManage.getInstance().createDayLoginControler(this, db);

		// 每日登录活动
		this.dayforverLoginControler = XsgActivityManage.getInstance().createDayforverLoginControler(this, db);

		// 送军令活动
		this.sendJunLingControler = XsgActivityManage.getInstance().createSendJunLingControler(this, db);

		// 幸运大转盘
		this.fortuneWheelControler = XsgActivityManage.getInstance().createFortuneWheelControler(this, db);

		this.bigDayChargeControler = XsgActivityManage.getInstance().createBigDayChargeControler(this, role);
		this.bigDayConsumeControler = XsgActivityManage.getInstance().createBigDayConsumeControler(this, role);
		this.bigSumChargeActivityControler = XsgActivityManage.getInstance().createBigSumChargeControler(this, role);
		this.bigSumConsumeActivityControler = XsgActivityManage.getInstance().createBigSumConsumeControler(this, role);

		// 等级福利
		// this.levelWealControler = XsgActivityManage.getInstance()
		// .createLevelWealControler(this, role);
		// this.notableList.add(levelWealControler);

		// 兑换活动
		this.exchangeActivityControler = XsgActivityManage.getInstance().createExchangeActivityControler(this, role);

		this.luckyBagControler = XsgLuckyBagManager.getInstance().createBagControler(this, role);

		this.cornucopiaControler = XsgActivityManage.getInstance().createCornucopiaControler(this, role);

		this.partnerControler = XsgPartnerManager.getInstance().createPartnerControler(this, role);
		this.superChargeController = XsgSuperChargeManager.getInstance().createSuperChargeControler(this, role);

		this.superRaffleController = XsgSuperRaffleManager.getInstance().createSuperChargeControler(this, role);

		this.treasureControler = XsgTreasureManage.getInstance().createTreasureControler(this, role);

		// 豪情宝
		this.haoqingbaoController = XsgHaoqingbaoManager.getInstance().createHaoqingbaoController(this, role);

		/** 老友召回 */
		this.friendsRecallController = XsgFriendsRecallManager.getInstance().createFriendsRecallController(this, role);

		// 武将觉醒
		this.heroAwakenController = XsgHeroAwakenManager.getInstance().createHeroAwakenController(this, role);

		// 公会战
		this.factionBattleController = XsgFactionBattleManager.getInstance().createFactionBattleControler(this, role);

		// 南华幻境
		this.dreamlandController = XsgDreamlandManager.getInstance().createDreamlandController(this, role);

		/** api */
		this.apiController = XsgApiManager.getInstance().createApiController(this, role);

		this.shootControler = XsgActivityManage.getInstance().createShootControler(this, role);
		this.worldBossControler = WorldBossManager.getInstance().createWorldBossControler(this, role);

		// 活动公告
		announceControler = XsgAnnounceManager.getInstance().createAnnounceControler(this, role);

		// 战力维护
		this.tournamentController = XsgTournamentManager.getInstance().createTournamentController(this, role);

		// 资源找回
		this.resourceBackControler = XsgActivityManage.getInstance().createResourceBackControler(this, role);

		// 战力维护
		this.combatPowerControler = new CombatPowerControler(this, role);

		// 酿酒
		this.makeWineControler = new MakeWineControler(this, role);

		this.crossArenaControler = new CrossArenaControler(this, role);

		this.footballControler = XsgActivityManage.getInstance().createFootballControler(this, role);
		
		// 20150630临时新增的运营活动需求，元宝十连必送道具
		if (FixRewardForBuy10WineByYuanbao.config.isEnable()) {
			new FixRewardForBuy10WineByYuanbao(this);
		}
		XsgTemporaryRDActivityManager.getInstance().createWrapperForRole(this);
		// ===================各模块初始化到此结束==================================

		this.initEvent();
		this.initRedPointControler();

		if (this.isRobot()) {// 机器人默认无事件和定时保存支持，避免逻辑模块创建的机器人常驻内存
			this.eventControler.destory();
		} else {
			this.scheduleSaveTimer();
		}
	}

	/**
	 * 初始化红点提示容器
	 */
	private void initRedPointControler() {
		this.timerRedPointList.clear();
		this.noTimerRedPointList.clear();

		Field[] fields = this.getClass().getDeclaredFields();
		for (Field f : fields) {
			// 必须实现指定的红点接口
			if (!IRedPointNotable.class.isAssignableFrom(f.getType())) {
				continue;
			}

			f.setAccessible(true);
			try {
				IRedPointNotable value = (IRedPointNotable) f.get(this);
				if (value == null) {
					continue;
				}

				RedPoint rp = value.getClass().getAnnotation(RedPoint.class);
				if (rp == null) {
					continue;
				}
				if (rp.isTimer()) {
					this.timerRedPointList.add(value);
				} else {
					this.noTimerRedPointList.add(value);
				}
			} catch (IllegalArgumentException e) {
				LogManager.error(e);
			} catch (IllegalAccessException e) {
				LogManager.error(e);
			}
		}
	}

	private void initEvent() {
		// 离线事件
		this.offlineEvent = this.eventControler.registerEvent(IOffline.class);
		// 金币变更
		this.jinbiEvent = this.eventControler.registerEvent(IJinbiChange.class);
		// 购买技能点
		this.buySkillPointEvent = this.eventControler.registerEvent(ISkillPointBuy.class);
		// 升级
		this.levelUpEvent = this.eventControler.registerEvent(IRoleLevelup.class);
		// 改名
		this.renameEvent = this.eventControler.registerEvent(IRoleNameChange.class);
		// 领取俸禄
		this.headAndBorderChageEvent = this.eventControler.registerEvent(IRoleHeadAndBorderChange.class);
		// 领取俸禄
		this.addSalaryEvent = this.eventControler.registerEvent(IAddSalary.class);
		// 元宝变更事件
		this.yuanbaoEvent = this.eventControler.registerEvent(IYuanbaoChange.class);
		// 完成引导
		this.guideEvent = this.eventControler.registerEvent(IGuideComplete.class);
		// 帐号重置事件
		this.resetEvent = this.eventControler.registerEvent(IRoleReset.class);
	}

	private void checkData() {
		// List<String> heroList = new ArrayList<String>();
		// for (IFormation formation : this.getFormationControler().values()) {
		// for (IHero hero : formation.getHeros()) {
		// String id = hero.getId();
		// if (heroList.contains(id)) {
		// LogManager.warn(TextUtil.format(
		// "[{0}]的部队配置中发现了重复武将[{1}]！！！", this.getName(), id));
		// } else {
		// heroList.add(id);
		// }
		// }
		// }
	}

	@Override
	public String getAccount() {
		return this.db.getAccount();
	}

	@Override
	public String getRoleId() {
		return this.db.getId();
	}

	@Override
	public int getLevel() {
		return (short) this.db.getLevel();
	}

	@Override
	public String getName() {
		return this.db.getName();
	}

	@Override
	public int getVit() {
		return this.db.getVit();
	}

	/**
	 * 根据日期，重置行动力
	 */
	private void resetVitNum() {
		// 是否重置行动力
		if (this.db.getVitDate() == null || !DateUtil.isSameDay(new Date(), this.db.getVitDate())) {
			this.db.setVitNum(0);
			this.db.setVitDate(new Date());
		}
	}

	@Override
	public int getVitNum() {
		this.resetVitNum();
		return this.db.getVitNum();
	}

	@Override
	public void winJinbi(int money) throws NotEnoughMoneyException {
		NumberUtil.checkRange(money, -Const.Ten_Thousand * Const.Ten_Thousand * 5, Const.Ten_Thousand
				* Const.Ten_Thousand * 5);
		long value = this.db.getVip().getJinbi() + money;
		if (value < 0) {
			throw new NotEnoughMoneyException();
		}

		this.db.getVip().setJinbi(value);
		this.getNotifyControler().onPropertyChange(Const.PropertyName.MONEY, value);
		if (money > 0) {
			this.db.getVip().setJinbiHistory(this.getJinbiHistory() + money);
		}

		try {
			this.jinbiEvent.onJinbiChange(money);
		} catch (Exception e) {
			LogManager.error(e);
		}
	}

	@Override
	public INotifyControler getNotifyControler() {
		return this.notifyControler;
	}

	@Override
	public IEventControler getEventControler() {
		return this.eventControler;
	}

	@Override
	public long getJinbi() {
		return this.db.getVip().getJinbi();
	}

	@Override
	public boolean isOnline() {
		return this.online;
	}

	@Override
	public void saveAsyn() {
		this.saver.saveAsyn();
	}

	@Override
	public int getPrestige() {
		return this.db.getPrestige();
	}

	@Override
	public void winPrestige(int prestige) {
		NumberUtil.checkRange(prestige, -Const.Ten_Thousand * 100, Const.Ten_Thousand * 100);
		prestige += this.db.getPrestige();
		// 升级逻辑
		int autoLimit = XsgRoleManager.getInstance().getAutoLevelUpLimit();// 自动升级上限
		int oldLevel = this.getLevel();
		int levelUpExp = XsgRoleManager.getInstance().getLevelUpExp(oldLevel); // 升级所需经验
		int upLevel = 0; // 升了几级
		while (oldLevel + upLevel < autoLimit && prestige >= levelUpExp) {
			prestige -= levelUpExp;
			upLevel++;

			levelUpExp = XsgRoleManager.getInstance().getLevelUpExp(oldLevel + upLevel);
		}

		this.db.setLevel(oldLevel + upLevel);
		if (prestige >= levelUpExp) {// 经验溢出处理
			int maxExp = XsgRoleManager.getInstance().getExpLimit(this.getLevel());
			prestige = Math.min(prestige, maxExp);
		}
		this.db.setPrestige(prestige);

		this.getNotifyControler().onPropertyChange(Const.PropertyName.EXP, this.getPrestige());
		if (upLevel > 0) {
			this.onLevelUp(oldLevel);
		}
	}

	/**
	 * 主公等级变化后的相关处理逻辑
	 */
	private void onLevelUp(int oldLevel) {
		int newLevel = this.getLevel();
		int junLingRec = 0; // 军令恢复

		for (int i = oldLevel + 1; i <= newLevel; i++) {
			RoleLevelConfigT rct = XsgRoleManager.getInstance().getRoleLevelConfigT(i);
			if (rct != null) {
				junLingRec += rct.junlingRecovery;
			}
		}
		if (junLingRec > 0) {
			this.getItemControler().changeItemByTemplateCode(Const.PropertyName.JUNLING_TEMPLATE_ID, junLingRec);
		}
		db.setLevelUpDate(new Date());
		this.getNotifyControler().onPropertyChange(Const.PropertyName.Level, newLevel);

		this.levelUpEvent.onRoleLevelup();
		XsgCenterManager.instance().levelup(this);
	}

	@Override
	public void notifyOffline() {
		this.setOnline(false);
		this.db.setLogoutTime(Calendar.getInstance().getTime());

		long onlineInterval = this.db.getLogoutTime().getTime() - this.db.getLoginTime().getTime();
		this.db.setOnlineTime((int) (this.db.getOnlineTime() + onlineInterval / 1000));

		// 角色下线时候，调用 竞技场 和 掠夺的数据
		this.arenaRankControler.clearData();
		this.itemChipControler.clearData();
		// 重置红点
		this.heroAdmireControler.resetRedPoint();
		this.timeBattleControler.resetRedPoint();
		this.collectHeroSoulControler.resetRedPoint();
		this.cornucopiaControler.setFirstOpen(true);
		this.factionControler.clearCopyRole();

		this.save2LocalFileAsyn();
		this.offlineEvent.onRoleOffline(onlineInterval);
	}

	/**
	 * 保存镜像到本地文件系统
	 */
	private void save2LocalFileAsyn() {
		// this.cloneData();
		// DBThreads.execute(new Runnable() {
		//
		// @Override
		// public void run() {
		// /cachePath/roleId/date.role
		// }
		// });
	}

	@Override
	public void winYuanbao(int rmby, boolean bind) throws NotEnoughYuanBaoException {
		NumberUtil.checkRange(rmby, -Const.Ten_Thousand * 10, Const.Ten_Thousand * 10);

		int yuanbao = this.getTotalYuanbao() + rmby;
		if (yuanbao < 0) {
			throw new NotEnoughYuanBaoException();
		}

		RoleVip vipDb = db.getVip();
		int oldBind = vipDb.getBindYuanbao();
		int oldUnbind = vipDb.getUnbindYuanbao();
		if (rmby < 0) {// 先扣充值元宝再扣系统产出
			vipDb.setUnbindYuanbao(vipDb.getUnbindYuanbao() + rmby);
			if (vipDb.getUnbindYuanbao() < 0) {
				vipDb.setBindYuanbao(vipDb.getBindYuanbao() + vipDb.getUnbindYuanbao());
				vipDb.setUnbindYuanbao(0);
			}
		} else {
			if (bind) {
				vipDb.setBindYuanbao(vipDb.getBindYuanbao() + rmby);
			} else {
				vipDb.setUnbindYuanbao(vipDb.getUnbindYuanbao() + rmby);
			}
		}

		this.getNotifyControler().onYuanBaoChanged(yuanbao);
		this.yuanbaoEvent.onYuanbaoChange(oldBind, oldUnbind, vipDb.getBindYuanbao(), vipDb.getUnbindYuanbao(), rmby);
	}

	/**
	 * 得到竞技币
	 */
	@Override
	public void winChallegeMoney(int num) {
		this.getArenaRankControler().mondifyChallengeMoney(num);
	}

	@Override
	public int getTotalYuanbao() {
		return this.db.getVip().getBindYuanbao() + this.db.getVip().getUnbindYuanbao();
	}

	@Override
	public void afterEnterGame() {
		this.db.setLoginTime(Calendar.getInstance().getTime());

		FriendsRecallCfgT friendsRecallCfgT = XsgFriendsRecallManager.getInstance().getFriendsRecallCfgT();
		if (friendsRecallCfgT != null) {
			if (db.getLevel() >= friendsRecallCfgT.roleLevel) {
				// 离线时间超过规定天数，获得老友回归资格
				Date logoutTime = db.getLogoutTime();
				if (logoutTime != null) {
					if (db.getRoleFriendsRecalled() == null
							&& DateUtil.diffDate(db.getLoginTime(), logoutTime) >= XsgFriendsRecallManager
									.getInstance().getFriendsRecallCfgT().offlineDays) {
						db.setRoleFriendsRecalled(new RoleFriendsRecalled(db, "", 0, 0, 0, db.getLoginTime()));
					}
				}
			}
		}
		if (db.getRoleOpenedMenu() == null) {
			db.setRoleOpenedMenu(new RoleOpenedMenu(GlobalDataManager.getInstance().generatePrimaryKey(), db));
		}

		// 处理小林志玲的福利
		this.getSnsController().processBenefitFromMsLing();

		// 登录后调用，在线礼包时间 重置
		this.onlineAwardControler.afterEnterGame();
		// 累计登录
		this.dayLoginControler.updateLoginCount();
		// 累计登录 永久
		this.dayforverLoginControler.updateLoginCount();
		this.taskControler.initRoleRedPacket();
		// 红点提示
		// this.refreshRedPoint(true);
		// 发送数据中心数据
		this.MFBIControler.sendRoleLogin();
		// 发送邮件
		this.mailControler.selectMailViewList();

		// 发送推送通知
		this.notifyControler.pushMsgs();
		// 七日任务开始记录
		this.taskControler.addSevenTask();

		// 处理API活动进度
		this.apiController.initActiveApiProcess();

		this.openServerActiveControler.update4Login();

		this.shareControler.update4Login();
		// 比武大会冠军登录
		this.tournamentController.championLogin();
		// 记录登录状态，用于资源返还活动
		this.resourceBackControler.addLoginStatus();
		// 酿酒重置数据
		this.makeWineControler.checkUpdate();
	}

	@Override
	public int calculateBattlePower() {
		if (XsgSnsManager.roleOfMsLing != null && this.getRoleId().equals(XsgSnsManager.roleOfMsLing.getRoleId())) {
			return 201314;
		}
		return this.getFormationControler().getDefaultFormation().calculateBattlePower();
	}

	@Override
	public void destory() {
		// 移除定时保存等定时器
		if (this.saveTimer != null) {
			this.saveTimer.cancel();
		}

		this.eventControler.destory();
	}

	@Override
	public void setOnline(boolean online) {
		this.online = online;
	}

	@Override
	public RoleViewForGM getRoleViewForGM() {
		AccountView av = new AccountView();
		av.username = this.getAccount();
		int expForBeiFa = this.getAttackCastleController().getCoin();
		int moneyForArena = this.getArenaRankControler().getRoleArenaRank().getChallengeMoney();
		long moneyForAuction = this.getAuctionHouseController().getAuctionMoney();
		return new RoleViewForGM(getView(), this.getCopyControler().getProgresses(), av, DateUtil.toString(this
				.getCreateTime().getTime()), DateUtil.toString(this.getLoginTime().getTime()), expForBeiFa,
				moneyForArena, moneyForAuction, this.isOnline(), getHeroEquipView(),
				this.itemControler.getItemEquipViewList4GM());
	}

	@Override
	public RoleView getView() {
		HeroView[] heroArray = this.heroControler.getHeroViewList();
		int remainTimes = 0;
		if (!new SimpleDateFormat("yyyyMMdd").format(new Date()).equals( //$NON-NLS-1$
				db.getLastSalaryDate())) {
			remainTimes = 1;
		}
		boolean sevenTaskStatus = false;
		if (XsgGameParamManager.getInstance().getSevenTaskOpenStatus() == 0) {
			sevenTaskStatus = true;
		} else {
			sevenTaskStatus = this.taskControler.isCompletedSevenTask();
		}
		int maxTournamentRank = 0;
		if (tournamentController != null) {
			maxTournamentRank = tournamentController.getMaxRank();
		}

		long levelUpDate = db.getLevelUpDate() == null ? getCreateTime().getTime() / 1000 : db.getLevelUpDate()
				.getTime() / 1000;

		ExtendObject extendObject = new ExtendObject(this.db.getAuctionHouse().getMoney(), sevenTaskStatus == true ? 0
				: 1, this.getExtHeadImage(), this.getExtHeadBorder(), this.dreamlandController.getNanHuaLing());

		return new RoleView(this.getCompletedGuides(), this.db.getSex(), this.getHeadImage(),
				this.generateAnnounceViewList(), this.getRoleId(), this.db.getSupportId(), this.getName(),
				(short) getLevel(), getVipLevel(), this.vipControler.getExperience(), getPrestige(), XsgRoleManager
						.getInstance().getLevelUpExp(this.getLevel()), (short) getOfficalRankId(), remainTimes,
				this.getCachePower(), this.getTotalYuanbao(), getJinbi(), heroArray,
				this.itemControler.getItemViewList(), this.formationControler.getFormationViewList(), this.getVit(),
				this.getVitNum(), this.factionControler.isInFaction(), this.vipControler.hasFirstCharge(),
				LuaSerializer.serialize(extendObject), maxTournamentRank, getCreateTime().getTime() / 1000, levelUpDate);
	}

	@Override
	public HeroEquipView[] getHeroEquipView() {
		return this.getHeroControler().getEquipDetails().toArray(new HeroEquipView[0]);
	}

	@Override
	public HeroSkillPointView getHeroSkillPointView() {
		this.heroControler.refreshHeroSkillData();

		long now = System.currentTimeMillis();
		int remainSeconds = -1;
		if (this.db.getNextHeroSkillTime() != null) {
			remainSeconds = (int) ((this.db.getNextHeroSkillTime().getTime() - now) / 1000);
		}

		return new HeroSkillPointView(this.db.getHeroSkillPoint(), remainSeconds, this.db.getHeroSkillBuyCount());
	}

	@Override
	public void rename(String name) {
		String old = this.getName();
		this.db.setName(name);
		if (!combineServerNamePattern.matcher(this.getName()).matches()) {// 经过合服后被系统修改的玩家改名不记次数
			this.db.setRenameCount(this.db.getRenameCount() + 1);
		}
		this.saveAsyn();

		this.renameEvent.onRoleNameChange(old, name);
	}

	@Override
	public void save(byte[] data) {
		XsgRoleManager.getInstance().saveRole((Role) TextUtil.bytesToObject(data));
	}

	/**
	 * 定时保存玩家数据到数据库
	 */
	private void scheduleSaveTimer() {
		int interval = 10 * 60 * 1000;// N分钟
		this.saveTimer = new DelayedTask(interval, interval) {

			@Override
			public void run() {
				IRole role = XsgRoleManager.getInstance().findRoleById(getRoleId());
				// 内存中找不到或者存在重复对象，此时当前角色对象不应被使用，避免出现数据错乱
				if (role == null || !role.equals(XsgRole.this)) {
					LogManager.warn(TextUtil.format("[{0},{1}] role timer task check an error.", getAccount(),
							getName()));
					destory();// 调用此方法会销毁定时任务自身
					return;
				}

				if (role.isOnline()) {
					saveAsyn();
				}
			}
		};
		LogicThread.scheduleTask(this.saveTimer);
	}

	@Override
	public ICopyControler getCopyControler() {
		return this.copyControler;
	}

	@Override
	public IItemControler getItemControler() {
		return this.itemControler;
	}

	@Override
	public IFormationControler getFormationControler() {
		return formationControler;
	}

	@Override
	public IHeroControler getHeroControler() {
		return this.heroControler;
	}

	public IMailControler getMailControler() {
		return mailControler;
	}

	public void setMailControler(IMailControler mailControler) {
		this.mailControler = mailControler;
	}

	public IChatControler getChatControler() {
		return chatControler;
	}

	@Override
	public IVipController getVipController() {
		return vipControler;
	}

	@Override
	public ActivityAnnounceView[] generateAnnounceViewList() {
		List<ActivityAnnounceView> list = new ArrayList<ActivityAnnounceView>();
		List<AnnounceT> map = XsgAnnounceManager.getInstance().getAllAnnounceT();
		for (AnnounceT template : map) {
			if (TextUtil.isNotBlank(template.showChannel)) {
				List<String> showChannelIds = TextUtil.stringToList(template.showChannel);
				if (!showChannelIds.contains(String.valueOf(db.getRegChannel()))) {
					continue;
				}
			} else if (TextUtil.isNotBlank(template.excludeChannel)) {
				List<String> excludeChannelIds = TextUtil.stringToList(template.excludeChannel);
				if (excludeChannelIds.contains(String.valueOf(db.getRegChannel()))) {
					continue;
				}
			}
			list.add(new ActivityAnnounceView(template.id, template.type, this.db.getAnnounces().containsKey(
					template.id), template.firstTitle, template.secondTitle, template.content));
		}
		Collections.sort(list, new Comparator<ActivityAnnounceView>() {

			@Override
			public int compare(ActivityAnnounceView o1, ActivityAnnounceView o2) {
				return o2.id - o1.id;// id倒序
			}
		});
		return list.toArray(new ActivityAnnounceView[0]);
	}

	@Override
	public IRewardControler getRewardControler() {
		return rewardControler;
	}

	@Override
	public void levelUp() throws NoteException {
		int levelUpExp = XsgRoleManager.getInstance().getLevelUpExp(getLevel());
		if (this.getPrestige() < levelUpExp) {
			throw new NoteException(Messages.getString("XsgRole.1")); //$NON-NLS-1$
		}

		if (this.getLevel() >= XsgRoleManager.getInstance().getCurrentLevelLimit()) {
			throw new NoteException(Messages.getString("XsgRole.2")); //$NON-NLS-1$
		}

		int old = this.getLevel();
		this.winPrestige(-levelUpExp);
		this.db.setLevel(old + 1);
		this.onLevelUp(old);
	}

	@Override
	public boolean addSalary() throws NotEnoughYuanBaoException {
		if (new SimpleDateFormat("yyyyMMdd").format(new Date()).equals( //$NON-NLS-1$
				db.getLastSalaryDate())) {
			return false;
		}

		// 取得官阶奖励 配置数据
		LadderLevelT levelT;
		if (getLadderControler().getLadder() != null) {
			levelT = XsgLadderManager.getInstance().getLevelMap(getLadderControler().getLadder().getLadderLevel());
		} else {
			levelT = XsgLadderManager.getInstance().getLevelMap(XsgLadderManager.getInstance().getInitT().initLevel);
		}

		// 获得奖励物品元宝 和 宝箱
		getRewardControler().acceptReward(levelT.item1, levelT.Salary1);
		getRewardControler().acceptReward(levelT.item2, levelT.Salary2);

		db.setLastSalaryDate(new SimpleDateFormat("yyyyMMdd") //$NON-NLS-1$
				.format(new Date()));

		// 添加 领取俸禄事件
		this.addSalaryEvent.onAddSalary(this.getRoleId(), levelT.Salary1);

		return true;
	}

	private int getOfficalRankIdofLevel() {
		if (getLadderControler().getLadder() != null) {
			return getLadderControler().clearLadder().getLadderLevel();
		} else {
			return XsgLadderManager.getInstance().getInitT().initLevel;
		}
	}

	@Override
	public int getOfficalRankId() {
		return getOfficalRankIdofLevel();
	}

	@Override
	public int getOfficalRankName() {
		return XsgLadderManager.getInstance().getLevelMap(getOfficalRankIdofLevel()).name;
	}

	@Override
	public IItemChipControler getItemChipControler() {
		return this.itemChipControler;
	}

	public IHeroMarketControler getHeroMarketControler() {
		return this.heroMarketControler;
	}

	@Override
	public void reduceCurrency(Money change) throws NotEnoughMoneyException, NotEnoughYuanBaoException {
		switch (change.type) {
		case VipLevel:
			if (this.getVipLevel() < change.num) {
				throw new IllegalStateException();
			}
			break;
		case Jinbi:
			this.winJinbi(-change.num);
			break;
		case Yuanbao:
			this.winYuanbao(-change.num, true);
			break;
		default:
			break;
		}
	}

	@Override
	public int getRenameCount() {
		return this.db.getRenameCount();
	}

	@Override
	public ITraderControler getTraderControler() {
		return this.traderControler;
	}

	@Override
	public final boolean isRobot() {
		return XsgRoleManager.getInstance().isRobotAccount(this.db.getAccount());
	}

	@Override
	public ITaskControler getTaskControler() {
		return this.taskControler;
	}

	@Override
	public long getJinbiHistory() {
		return this.db.getVip().getJinbiHistory();
	}

	/**
	 * 修改行动力
	 */
	@Override
	public void modifyVit(int value) throws NoteException {
		int newVit = this.getVit() + value;
		if (newVit < 0)
			newVit = 0;

		this.db.setVit(newVit);
	}

	@Override
	public RoleViewForOtherPlayer getViewForOtherPlayer() {
		IFormation formation = this.getFormationControler().getDefaultFormation();
		int combatPower = getCachePower();
		List<ItemView> buffList = new ArrayList<ItemView>();
		if (formation.getBuff() != null) {
			buffList.add(formation.getBuff().getView());
		}
		return new RoleViewForOtherPlayer(getRoleId(), getName(), this.getHeadImage(), (short) getLevel(),
				this.getVipLevel(), combatPower, this.getFactionControler().getFactionName(),
				buffList.toArray(new ItemView[0]), formation.getSummaryView());
	}

	@Override
	public void addFriendApplyingHistory(FriendApplyingHistory historyRecord) {
		historyRecord.setTarget(db);
		db.getFriendApplyingHistory().put(historyRecord.getApplicantRoleId(), historyRecord);
	}

	@Override
	public int getCachePower() {
		if (XsgSnsManager.roleOfMsLing != null && db.getId().equals(XsgSnsManager.roleOfMsLing.getRoleId())) {
			return 201314;
		}

		return this.db.getCombatPower();
	}

	public IFactionControler getFactionControler() {
		return factionControler;
	}

	public ISnsControler getSnsController() {
		return snsControler;
	}

	public ISignController getSignController() {
		return signControler;
	}

	public ICollectHeroSoulController getCollectHeroSoulControler() {
		return collectHeroSoulControler;
	}

	public IAttackCastleController getAttackCastleController() {
		return attackCastleController;
	}

	public IAuctionHouseController getAuctionHouseController() {
		return auctionHouseController;
	}

	@Override
	public void buyHeroSkillPoint() throws NotEnoughYuanBaoException, NoteException {
		HeroSkillPointView hspv = this.getHeroSkillPointView();
		int maxPoint = this.vipControler.getMaxSkillPoint();
		if (hspv.heroSkillPoint >= maxPoint) {
			throw new NoteException(Messages.getString("XsgRole.5")); //$NON-NLS-1$
		}
		VipT vt = XsgVipManager.getInstance().findVipT(this.getVipController().getLevel());
		if (hspv.buyTime >= vt.SkillNum) {
			throw new NoteException(Messages.getString("XsgRole.6")); //$NON-NLS-1$
		}

		// 查找模板
		int newCount = hspv.buyTime + 1;
		BuyHeroSkillT bhst = XsgHeroManager.getInstance().findBuyHeroSkillT(newCount);
		if (bhst == null) {
			throw new NoteException(Messages.getString("XsgRole.7")); //$NON-NLS-1$
		}

		// 扣元宝
		try {
			this.reduceCurrency(new Money(CurrencyType.Yuanbao, bhst.yuanbao));
		} catch (NotEnoughMoneyException e) {
			LogManager.error(e);
		}
		// 更新数据
		int newPoint = hspv.heroSkillPoint + bhst.skillPoint;
		this.db.setHeroSkillPoint(newPoint);
		this.db.setHeroSkillBuyCount(newCount);
		this.db.setHeroSkillBuyTime(Calendar.getInstance().getTime());

		if (newPoint >= maxPoint) {
			this.db.setNextHeroSkillTime(null);
		}

		this.buySkillPointEvent.onSkillPointBuy(bhst, newPoint);
	}

	@Override
	public IBuyJInbiControler getBuyJInbiControler() {
		return this.buyJInbiControler;
	}

	/**
	 * 获得头像
	 */
	@Override
	public String getHeadImage() {
		return this.db.getHeadImage() + Const.HEAD_BORDER_SPLIT
				+ (TextUtil.isBlank(this.db.getHeadBorder()) ? "" : this.db.getHeadBorder());
	}

	/**
	 * 设置头像
	 */
	@Override
	public void setHeadImage(String img) {
		String old = this.getHeadImage();
		this.db.setHeadImage(img);

		this.headAndBorderChageEvent.onRoleHeadChange(old, this.getHeadImage());
	}

	/**
	 * 获取玩家额外获得的头像
	 */
	@Override
	public String[] getExtHeadImage() {
		String[] array = new String[0];
		if (TextUtil.isNotBlank(this.db.getExtHeadImage())) {
			array = this.db.getExtHeadImage().split(Const.EXTRA_HEAD_SPLIT);
		}
		return array;
	}

	/**
	 * 新增扩展头像
	 */
	@Override
	public void addExtHeadImage(String img) {
		if (TextUtil.isNotBlank(this.db.getExtHeadImage()) && this.db.getExtHeadImage().indexOf(img) != -1) {
			return;
		}
		this.db.setExtHeadImage((TextUtil.isBlank(this.db.getExtHeadImage()) ? "" : this.db.getExtHeadImage()) + img
				+ Const.EXTRA_HEAD_SPLIT);
	}

	/**
	 * 设置边框
	 * 
	 * @param border
	 */
	@Override
	public void setHeadBorder(String border) {
		String old = this.getHeadImage();
		this.db.setHeadBorder(border);

		this.headAndBorderChageEvent.onRoleHeadChange(old, this.getHeadImage());
	}

	/**
	 * 获取边框
	 * 
	 * @return
	 */
	@Override
	public String getHeadBorder() {
		return this.db.getHeadBorder();
	}

	/**
	 * 新增一个边框
	 */
	@Override
	public void addExtHeadBorder(String border) {
		if (TextUtil.isNotBlank(this.db.getExtHeadBorder()) && this.db.getExtHeadBorder().indexOf(border) != -1) {
			return;
		}
		this.db.setExtHeadBorder((TextUtil.isBlank(this.db.getExtHeadBorder()) ? "" : this.db.getExtHeadBorder())
				+ border + Const.EXTRA_HEAD_SPLIT);
	}

	/**
	 * 获取额外获得的边框集合
	 * 
	 * @return
	 */
	@Override
	public String[] getExtHeadBorder() {
		if (TextUtil.isNotBlank(this.db.getExtHeadBorder())) {
			return this.db.getExtHeadBorder().split(Const.EXTRA_HEAD_SPLIT);
		}
		return new String[0];
	}

	@Override
	public void setCombatPower(int combatPower) {
		this.db.setCombatPower(combatPower);
		this.notifyControler.onPropertyChange(Const.PropertyName.CombatPower, combatPower);
	}

	@Override
	public IOnlineAwardControler getOnlineAwardControler() {
		return this.onlineAwardControler;
	}

	@Override
	public void refreshRedPoint(boolean includeNoTimer) {
		List<IRedPointNotable> controlerList = new ArrayList<IRedPointNotable>();
		controlerList.addAll(timerRedPointList);
		if (includeNoTimer) {
			controlerList.addAll(noTimerRedPointList);
		}

		List<MajorUIRedPointNote> list = new ArrayList<MajorUIRedPointNote>();
		for (IRedPointNotable controler : controlerList) {
			try {
				long begin = System.currentTimeMillis();
				MajorUIRedPointNote note = controler.getRedPointNote();
				XsgMonitorManager.getInstance().process(
						TextUtil.format("{0}.getRedPointNote", controler.getClass().getSimpleName()),
						(int) (System.currentTimeMillis() - begin));
				if (note != null) {
					list.add(note);
				}

			} catch (Exception e) {
				LogManager.error(e);
			}
		}

		this.notifyControler.onMajorUIRedPointChange(list.toArray(new MajorUIRedPointNote[0]));
	}

	@Override
	public boolean containsCDKGroup(String category) {
		return this.db.getCdkRecords().containsKey(category);
	}

	@Override
	public void addCDKRecord(String category, String cdkeyCode) {
		this.db.getCdkRecords().put(
				category,
				new RoleCDKRecord(GlobalDataManager.getInstance().generatePrimaryKey(), db, category, cdkeyCode,
						Calendar.getInstance().getTime()));
	}

	@Override
	public IArenaRankControler getArenaRankControler() {
		return this.arenaRankControler;
	}

	@Override
	public IRankListControler getRankListControler() {
		return this.rankListControler;
	}

	@Override
	public IMFBIControler getMFBIControler() {
		return this.MFBIControler;
	}

	@Override
	public void setSex(int sex) {
		sex %= 2;
		this.db.setSex(sex);
	}

	@Override
	public int getSex() {
		return this.db.getSex();
	}

	@Override
	public IActivityControler getActivityControler() {
		return this.activityControler;
	}

	@Override
	public int getVipLevel() {
		return this.getVipController().getLevel();
	}

	@Override
	public IShopControler getShopControler() {
		return this.shopControler;
	}

	public void completeGuide(int guideId) {
		int[] already = this.getCompletedGuides();
		Arrays.sort(already);
		if (Arrays.binarySearch(already, guideId) > -1) {
			return;
		}

		already = Arrays.copyOf(already, already.length + 1);
		already[already.length - 1] = guideId;
		this.db.setCompleteGuide(TextUtil.GSON.toJson(already));

		this.guideEvent.onGuideCompleted(guideId);
	}

	/**
	 * 获取已完成引导
	 * 
	 * @return
	 */
	private int[] getCompletedGuides() {
		int[] already = TextUtil.GSON.fromJson(this.db.getCompleteGuide(), int[].class);
		if (already == null) {
			already = new int[0];
		}
		return already;
	}

	@Override
	public ITimeBattleControler getTimeBattleControler() {
		return this.timeBattleControler;
	}

	@Override
	public Date getLogoutTime() {
		return this.db.getLogoutTime();
	}

	@Override
	public IWorshipRankControler getWorshipRankControler() {
		return this.worshipRankControler;
	}

	@Override
	public IMakeVipControler getMakeVipControler() {
		return this.makeVipControler;
	}

	@Override
	public Date getLastRenameTime() {
		return this.db.getLastRenameTime();
	}

	@Override
	public void resetLastRenameTime() {
		this.db.setLastRenameTime(Calendar.getInstance().getTime());
	}

	@Override
	public void beforeEnterGame() {
		this.shopControler.refreshShop();
		this.refreshJunLing();
		// 保证过了第三关的人有潘凤
		int panFengId = 4205;
		int star = this.copyControler.getCopyStar(1021);
		IHero panFeng = this.heroControler.getHero(panFengId);
		if (star > 0 && panFeng == null) {
			this.heroControler.addHero(XsgHeroManager.getInstance().getHeroT(panFengId), HeroSource.Gift);
		}
	}

	@Override
	public void refreshJunLing() {
		IRecoverable recovery = new IRecoverable() {
			@Override
			public Date getNextRecTime() {
				return db.getNextJunLingTime();
			}

			@Override
			public void setTime(Date time) {
				db.setNextJunLingTime(time);
			}

			@Override
			public int getValue() {
				return itemControler.getItemCountInPackage(Const.PropertyName.JUNLING_TEMPLATE_ID);
			}

			@Override
			public int getLimit() {
				RoleLevelConfigT rct = XsgRoleManager.getInstance().getRoleLevelConfigT(getLevel());
				return rct == null ? 0 : rct.junlingLimit;
			}

			@Override
			public void changeValue(int change) {
				itemControler.changeItemByTemplateCode(Const.PropertyName.JUNLING_TEMPLATE_ID, change);
			}

			@Override
			public long getInterval() {
				return XsgGameParamManager.getInstance().getJunLingRecorvInterval() * 1000;
			}
		};

		RecoveryUtil.recovery(recovery);
	}

	@Override
	public void setChipRobStat(int chipRobStat) {
		this.db.setChipRobStat(chipRobStat);
	}

	@Override
	public int getChipRobStat() {
		return this.db.getChipRobStat();
	}

	@Override
	public Date getLoginTime() {
		return this.db.getLoginTime();
	}

	@Override
	public int getAccountChannel() {
		return this.db.getRegChannel();
	}

	@Override
	public boolean readAnnounce(int id) {
		if (this.db.getAnnounces().containsKey(id)) {
			return true;
		}
		List<AnnounceT> list = XsgAnnounceManager.getInstance().getAllAnnounceT();
		for (AnnounceT a : list) {
			// 特殊类型不改变已读
			if (a.id == id && a.type == 2) {
				return false;
			}
		}
		this.db.getAnnounces().put(id, new RoleAnnounce(GlobalDataManager.getInstance().generatePrimaryKey(), db, id));
		return true;
	}

	@Override
	public byte[] cloneData() {
		return TextUtil.objectToBytes(this.db);
	}

	@Override
	public ILadderControler getLadderControler() {
		return this.ladderControler;
	}

	@Override
	public IHeroAdmireControler getHeroAdmireControler() {
		return this.heroAdmireControler;
	}

	@Override
	public ISumChargeActivityControler getSumChargeActivityControler() {
		return this.sumChargeActivityControler;
	}

	@Override
	public ISumConsumeActivityControler getSumConsumeActivityControler() {
		return this.sumConsumeActivityControler;
	}

	@Override
	public String getRemoteIp() {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(this.getAccount(), this.getRoleId());
		return session == null ? "" : session.getDevice().ip; //$NON-NLS-1$
	}

	@Override
	public String getRemoteAddress() {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(this.getAccount(), this.getRoleId());
		return session == null ? "" : session.getRemoteAddress(); //$NON-NLS-1$
	}

	@Override
	public String getDeviceId() {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(this.getAccount(), this.getRoleId());
		return session == null ? "" : session.getDevice().mac; //$NON-NLS-1$
	}

	@Override
	public int getCurrentChannel() {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(this.getAccount(), this.getRoleId());
		return session == null ? -1 : session.getDevice().currentChannel;
	}

	@Override
	public Date getCreateTime() {
		return this.db.getCreateTime();
	}

	@Override
	public void setQuitFactionTime(Date date) {
		this.db.setQuitFactionDate(date);
	}

	@Override
	public void addAuctionCoin(int num) throws NoteException {
		getAuctionHouseController().addAuctionMoney(num);
	}

	@Override
	public IInviteActivityControler getInviteActivityControler() {
		return this.inviteActivityControler;
	}

	@Override
	public void addInviteLog(RoleInviteLog roleInviteLog) {
		roleInviteLog.setRole(db);
		boolean exist = false;
		for (RoleInviteLog r : this.db.getRoleInviteLogs()) {
			if (r.getMac() != null && r.getMac().equals(roleInviteLog.getMac())) {
				exist = true;
				break;
			}
		}
		db.getRoleInviteLogs().add(roleInviteLog);
		if (!exist) {
			this.db.setInviteNum(this.db.getInviteNum() + 1);
		}
	}

	@Override
	public void setAccount2System() {
		this.db.setAccount(XsgRoleManager.Robot_Account);
	}

	@Override
	public IFundControler getFundControler() {
		return fundControler;
	}

	@Override
	public ILevelRewardControler getLevelRewardControler() {
		return levelRewardControler;
	}

	@Override
	public ISeckillControler getSeckillControler() {
		return this.seckillControler;
	}

	@Override
	public IDayChargeControler getDayChargeControler() {
		return this.dayChargeControler;
	}

	@Override
	public IDayConsumeControler getDayConsumeControler() {
		return this.dayConsumeControler;
	}

	@Override
	public IPowerRewardControler getPowerRewardControler() {
		return this.powerRewardControler;
	}

	@Override
	public void onDeleted() {
		this.resetEvent.onReset();
	}

	@Override
	public IFirstJiaControler getFirstJiaControler() {
		return firstJiaControler;
	}

	@Override
	public IDayLoginControler getDayLoginControler() {
		return dayLoginControler;
	}

	@Override
	public IDayforverLoginControler getDayforverLoginControler() {
		return dayforverLoginControler;
	}

	public ISendJunLingControler getSendJunLingControler() {
		return sendJunLingControler;
	}

	@Override
	public IBigDayChargeControler getBigDayChargeControler() {
		return bigDayChargeControler;
	}

	@Override
	public IBigDayConsumeControler getBigDayConsumeControler() {
		return bigDayConsumeControler;
	}

	@Override
	public IBigSumChargeActivityControler getBigSumChargeActivityControler() {
		return bigSumChargeActivityControler;
	}

	@Override
	public IBigSumConsumeActivityControler getBigSumConsumeActivityControler() {
		return bigSumConsumeActivityControler;
	}

	@Override
	public IFortuneWheelControler getFortuneWheelControler() {
		return fortuneWheelControler;
	}

	@Override
	public boolean isCompleteGuide(int guideId) {
		int[] completes = this.getCompletedGuides();
		for (int i : completes) {
			if (i == guideId) {
				return true;
			}
		}

		return false;
	}

	public void updateFirstInTime(long firstInTime) {
		if (db.getFirstInTime() <= 0L) {
			db.setFirstInTime(firstInTime);
		}
	}

	public long getFirstInTime() {
		return db.getFirstInTime();
	}

	@Override
	public ILuckyBagControler getLuckyBagControler() {
		return this.luckyBagControler;
	}

	/**
	 * 聊天禁言超时时间
	 */
	@Override
	public Date getSilenceExpire() {
		return db.getSilenceExpire();
	}

	@Override
	public void setSilenceExpire(Date date) {
		db.setSilenceExpire(date);
	}

	@Override
	public ICornucopiaControler getCornucopiaControler() {
		return this.cornucopiaControler;
	}

	@Override
	public IExchangeActivityControler getExchangeItemControler() {
		return this.exchangeActivityControler;
	}

	@Override
	public IPartnerControler getPartnerControler() {
		return partnerControler;
	}

	public IShootControler getShootControler() {
		return shootControler;
	}

	/**
	 * 获取好友/仇人/黑名单列表
	 */
	@Override
	public Set<RoleSns> getSns() {
		return db.getSns();
	}

	// @Override
	// public ILevelWealControler getLevelWealControler() {
	// return this.levelWealControler;
	// }
	@Override
	public ITreasureControler getTreasureControler() {
		return this.treasureControler;
	}

	@Override
	public boolean isDoubleExp() {
		if (db.getDoubleExpOutTime() != null && DateUtil.compareTime(db.getDoubleExpOutTime(), new Date()) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDoubleItem() {
		if (db.getDoubleItemOutTime() != null && DateUtil.compareTime(db.getDoubleItemOutTime(), new Date()) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public IntIntPair[] getDoubleCardTime() throws NoteException {
		IntIntPair[] pairs = new IntIntPair[] { new IntIntPair(0, 0), new IntIntPair(1, 0) };
		if (isDoubleExp()) {
			pairs[0] = new IntIntPair(0, (int) (DateUtil.compareTime(db.getDoubleExpOutTime(), new Date()) / 1000));
		}
		if (isDoubleItem()) {
			pairs[1] = new IntIntPair(1, (int) (DateUtil.compareTime(db.getDoubleItemOutTime(), new Date()) / 1000));
		}
		return pairs;
	}

	@Override
	public IHaoqingbaoController getHaoqingbaoController() {
		return haoqingbaoController;
	}

	@Override
	public IWorldBossControler getWorldBossControler() {
		return worldBossControler;
	}

	@Override
	public RoleFaction getRoleFaction() {
		return db.getRoleFaction();
	}

	@Override
	public RoleWorldBoss getRoleWorldBoss() {
		return db.getWorldBoss();
	}

	@Override
	public FriendsRecallController getFriendsRecallController() {
		return friendsRecallController;
	}

	@Override
	public long friendsRecallTime() {
		if (this.db.getRoleFriendsRecalled() != null && this.db.getRoleFriendsRecalled().getState() == 2) {
			return this.db.getRoleFriendsRecalled().getRecallTime().getTime();
		}
		return 0;
	}

	@Override
	public ISmithyExchangeController getSmithyExchangeController() {
		return smithyControler;
	}

	@Override
	public ISuperChargeController getSuperChargeControlle() {
		return superChargeController;
	}

	@Override
	public int getServerId() {
		return this.db.getServerId();
	}

	@Override
	public int getRenameYuanbao() {
		if (combineServerNamePattern.matcher(this.getName()).matches()) {
			return 0;// 合服后被系统强制改名后进行的改名免费
		}
		if (this.db.getRenameCount() == 1) {
			return 0;// 创角后的首次改名免费（即第二次改名，创角算第一次改名）
		}
		// 合服后免费改名
		return XsgRoleManager.getInstance().getRenameConfigT().yuanbao;
	}

	@Override
	public IAchieveControler getAchieveControler() {
		return achieveControler;
	}

	@Override
	public ILotteryControler getLotteryControler() {
		return lotteryControler;
	}

	@Override
	public IOpenServerActiveControler getOpenServerActiveControler() {
		return openServerActiveControler;
	}

	@Override
	public IHeroAwakenController getHeroAwakenController() {
		return heroAwakenController;
	}

	@Override
	public RoleOpenedMenu getRoleOpenedMenu() {
		return db.getRoleOpenedMenu();
	}

	@Override
	public IApiController getApiController() {
		return apiController;
	}

	@Override
	public ITournamentController getTournamentController() {
		return tournamentController;
	}

	@Override
	public int getCompletedAchieve() {
		return db.getAchieveCompletedCount();
	}

	@Override
	public IShareControler getShareControler() {
		return shareControler;
	}

	@Override
	public IFactionBattleController getFactionBattleController() {
		return factionBattleController;
	}

	@Override
	public IDreamlandController getDreamlandController() {
		return dreamlandController;
	}

	@Override
	public IColorfullEggController getColorfullEggController() {
		return colorfullEggController;
	}

	/**
	 * @return the resourceBackControler
	 */
	public ResourceBackControler getResourceBackControler() {
		return resourceBackControler;
	}

	@Override
	public void setAccidentRedPoint(boolean bool) {
		db.getRoleTreasureParam().setAccidentRedPoint(bool);
	}

	@Override
	public ISuperRaffleController getSuperRaffleControlle() {
		return superRaffleController;
	}

	@Override
	public IMakeWineControler getMakeWineControler() {
		return makeWineControler;
	}

	@Override
	public int getCombatPower() {
		return db.getCombatPower();
	}

	@Override
	public RoleWeixinShare getRoleWeixinShare() {
		return db.getRoleWeixinShare();
	}

	@Override
	public RoleWeixinShare addRoleWeixinShare(int num) {
		RoleWeixinShare shareInfo = new RoleWeixinShare(GlobalDataManager.getInstance().generatePrimaryKey(), db, num,
				new Date());
		db.setRoleWeixinShare(shareInfo);
		return shareInfo;
	}

	@Override
	public void setRobotType(int type) {
		db.setRobotType(type);
	}

	@Override
	public CrossArenaControler getCrossArenaControler() {
		return this.crossArenaControler;
	}

	@Override
	public FootballControler getFootballControler() {
		return this.footballControler;
	}

	@Override
	public IArtifactControler getArtifactControler() {
		return this.artifactControler;
	}

}
