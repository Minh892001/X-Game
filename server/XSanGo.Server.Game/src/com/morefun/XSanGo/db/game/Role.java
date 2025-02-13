/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OrderBy;

/**
 * Role entity. @author MyEclipse Persistence Tools
 */
/**
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {
	private static final long serialVersionUID = 4214073951778164546L;

	// Fields
	private String id;

	private String account;

	private int regChannel;

	private String supportId;

	private String name;

	private int level = 1; // 新角色默认1级

	private int prestige;

	/** 总在线时间 */
	private int onlineTime;

	private Date createTime;

	private Date loginTime;

	private Date logoutTime;

	private short state;

	private int version;

	/** 行动力 */
	private int vit;

	private int vitNum; // 领取行动力次数

	private Date vitDate; // 领取行动力时间

	/** 行动力回复时间 */
	private Date vitResetDate;

	/** 上次领取俸禄时间 */
	private String lastSalaryDate;

	/** 改名次数 */
	private int renameCount;

	/** 最后一次元宝改名时间 */
	private Date lastRenameTime;

	/** 武将技能点 */
	private int heroSkillPoint;

	/** 下一次武将技能点恢复时间 */
	private Date nextHeroSkillTime;

	/** 武将技能购买次数 */
	private int heroSkillBuyCount;

	/** 最近一次购买技能点时间 */
	private Date heroSkillBuyTime;

	/** 副本章节领奖记录 */
	private String chapterTag;

	/** 抽卡的魅力值 */
	private int marketCharm;

	/** 购买金币次数 */
	private int buyJinbiNum;

	/** 购买金币时间 */
	private Date buyJinbiTime;

	/** 头像 */
	private String headImage = "";

	/** 获得的扩展头像 ( 多个扩展头像用逗号分割) */
	private String extHeadImage;

	/** 边框 */
	private String headBorder = "";

	/** 获得的头像边框 ( 多个扩展边框用逗号分割) */
	private String extHeadBorder;

	/** 禁言截至时间 */
	private Date silenceExpire;

	private int sex = 1;

	private String completeGuide;

	/** 公会索引编号 */
	private String factionId;

	private RoleVip vip;

	private RoleTrader trader;

	private ChatSet chatSet;

	private RoleOnline onlineAward;

	/** 副本Buff */
	private int copyBuff;

	// 排行榜数据
	private RoleArenaRank arenaRank;

	// 铁匠铺紫装数据
	private RoleSmithy smithy;
	// 铁匠铺蓝装数据
	private RoleBlueSmithy blueSmithy;

	// 邮件历史数据
	private RoleMailHistory mailHistory;

	// 最后刷新商城物品时间
	private Date lastBuyItemDate;

	// 最后刷新时空战役时间
	private Date lastTimeBattleDate;

	// 当天已调整TA次数
	private int worshipCount;

	// 当天已购买挑战TA次数
	private int buyHudongCount;

	// 最后挑战TA时间
	private Date lastHudongDate;

	// 战力
	private int combatPower;

	// 是否第一次 碎片掠夺 0:没有掠夺 1：已经掠夺
	private int chipRobStat;

	private Date quitFactionDate;// 主动退出公会时间

	private Date lastAnswerDate;// 我要做VIP最后答题时间

	private long firstInTime; // 第一次登录时间

	private int signCountForTc;

	// 累计消费活动消费元宝数量
	private int consumeYuanbao;

	// 日消费元宝数
	private int dayConsumeYuanbao;

	/** 累计消费活动中最后一次元宝消费时间 */
	private Date lastConsumeTime;

	// 北伐
	private RoleAttackCastle roleAttackCastle;

	private RoleRedPacket roleRedPacket;

	// 邀请好友人数
	private int inviteNum;

	// 被邀请次数
	private int invitedNum;

	// 邀请码
	private String inviteCode;

	/** 购买军令次数 */
	private int buyJunLingNum;

	/** 购买军令时间 */
	private Date buyJunLingTime;

	/** 下次恢复军令时间 */
	private Date nextJunLingTime;

	private int totalWarmupCount;

	private int winWarupCount;

	private int loseWarmupCount;

	private Date warmupUpdateTime;

	/** 创建角色时所在服务器ID */
	private int serverId;

	/** 成就完成数量 */
	private int achieveCompletedCount;
	/** 热身赛每日逃跑次数 */
	private int warmupEscapeCount;

	/** 热身赛最一次逃跑时间 */
	private Date warmupEscapeTime;

	// 群雄争霸 和 战报
	private RoleLadder ladder;

	// 名将仰慕
	private RoleHeroAdmire heroAdmire;

	// 拍卖行
	private RoleAuctionHouse auctionHouse;

	// 四测返利数据
	private RoleFourthTest fourthTest;

	/** 老友召回邀请数据 */
	private RoleFriendsInvitation roleFriendsInvitation;

	/** 老友召回 被召回数据 */
	private RoleFriendsRecalled roleFriendsRecalled;

	/** 成就进度奖励数据 */
	private RoleAchieveProgressRec roleAchieveProgressRec;

	/** 角色彩蛋 */
	private RoleColorfulEgg roleColorfullEgg;

	private Set<RoleHero> roleHeros = new HashSet<RoleHero>(0);

	private Set<RoleFormation> roleFormations = new HashSet<RoleFormation>(0);

	private Map<String, RoleItem> items = new HashMap<String, RoleItem>(0);

	private Map<Integer, RoleCopy> roleCopys = new HashMap<Integer, RoleCopy>(0);

	// private Set<RoleMail> roleMails = new HashSet<RoleMail>(0);
	private Map<String, RoleMail> roleMailMap = new HashMap<String, RoleMail>();

	private Set<RoleSns> Sns = new HashSet<RoleSns>(0);

	private Set<RoleTotalSignGift> totalSignGift = new HashSet<RoleTotalSignGift>(0);

	private Map<String, RoleSign> Sign = new HashMap<String, RoleSign>(0);

	private Set<RoleVitSending> vitSending = new HashSet<RoleVitSending>(0);

	private Set<RoleVitReceiving> vitReceiving = new HashSet<RoleVitReceiving>(0);

	private Map<String, FriendApplyingHistory> friendApplyingHistory = new HashMap<String, FriendApplyingHistory>();

	private Map<Integer, RoleMarket> roleMarkets = new HashMap<Integer, RoleMarket>();

	private Map<Integer, RoleTask> roleTask = new HashMap<Integer, RoleTask>();

	/** 老友召回任务 */
	private List<RoleRecallTask> roleRecallTask = new ArrayList<RoleRecallTask>();

	/** api记录 */
	private Map<Integer, RoleApi> roleApiMap = new HashMap<Integer, RoleApi>();

	private List<ChatMessageOffline> ChatMessageOfflineList = new ArrayList<ChatMessageOffline>(0);

	private Map<String, RoleCDKRecord> cdkRecords = new HashMap<String, RoleCDKRecord>(0);

	private List<ArenaRankFight> arenaRankFightList = new ArrayList<ArenaRankFight>(0);

	// 已领取的升级礼包
	private List<RoleUpGift> roleUpGifts = new ArrayList<RoleUpGift>();

	// 商城物品可购买数量
	private List<RoleShopItem> shopItems = new ArrayList<RoleShopItem>();

	/** 兑换次数 */
	private List<RoleExchangeItem> exchangeItems = new ArrayList<RoleExchangeItem>();

	// 时空战役可挑战次数
	private Map<Integer, RoleTimeBattle> timeBattle = new HashMap<Integer, RoleTimeBattle>();

	// 膜拜
	private List<RoleWorship> worships = new ArrayList<RoleWorship>();

	/** 宝箱伪随机 */
	private Map<String, RoleMock> roleMocks = new HashMap<String, RoleMock>();

	/** 宝箱伪随机 */
	private Map<String, RoleChestMock> roleChestMocks = new HashMap<String, RoleChestMock>();

	/** 已读公告 */
	private Map<Integer, RoleAnnounce> announces = new HashMap<Integer, RoleAnnounce>();

	private List<RoleLadderReport> ladderReportList = new ArrayList<RoleLadderReport>(0);

	// 名将召唤
	private List<RoleCollectHeroSoul> collectHeroSoulList = new ArrayList<RoleCollectHeroSoul>();

	// 累计充值奖励领取记录
	private Map<Integer, RoleActivityForSumCharge> activityForSumCharge = new HashMap<Integer, RoleActivityForSumCharge>();

	// 累计消费奖励领取记录
	private Map<Integer, RoleActivityForSumConsume> activityForSumConsume = new HashMap<Integer, RoleActivityForSumConsume>();

	// 邀请好友奖励领取记录
	private Map<Integer, RoleInviteActivity> inviteActivity = new HashMap<Integer, RoleInviteActivity>();

	// 邀请好友记录
	private List<RoleInviteLog> roleInviteLogs = new ArrayList<RoleInviteLog>();

	// 成长基金
	private RoleFund fund;

	// 等级奖励
	private RoleLevelReward levelReward;

	// 第一佳
	private RoleFirstJia firstJia;

	// 每日登录
	private RoleDayLoginReward dayLoginReward;

	// 微信分享次数
	private RoleWeixinShare roleWeixinShare;

	// 每日登录
	private RoleDayforverLoginReward dayforverLoginReward;

	// 送军令
	private RoleSendJunLing sendJunLing;

	// 战力嘉奖
	private RolePowerReward powerReward;

	// 任务活跃点信息
	private RoleTaskActive roleTaskActive;

	// 七日任务信息
	private RoleSevenTask roleSevenTask;

	// 成就信息
	private Map<String, RoleAchieve> roleAchieves = new HashMap<String, RoleAchieve>();

	// 分享活动信息
	private Map<Integer, RoleShare> roleShares = new HashMap<Integer, RoleShare>();

	// 开服活动信息
	private Map<Integer, RoleOpenServerActive> openServerAct = new HashMap<Integer, RoleOpenServerActive>();

	// 七日目标信息
	private Map<Integer, RoleSevenProgress> roleSevenTasks = new HashMap<Integer, RoleSevenProgress>();

	// 秒杀的物品
	private Map<Integer, RoleSeckill> roleSeckills = new HashMap<Integer, RoleSeckill>();

	// 日充值奖励领取记录
	private Map<Integer, RoleDayCharge> dayCharges = new HashMap<Integer, RoleDayCharge>();

	// 日消费奖励领取记录
	private Map<Integer, RoleDayConsume> dayConsume = new HashMap<Integer, RoleDayConsume>();

	// big日充值奖励领取记录
	private Map<Integer, RoleBigDayCharge> bigDayCharges = new HashMap<Integer, RoleBigDayCharge>();

	// big日消费奖励领取记录
	private Map<Integer, RoleBigDayConsume> bigDayConsume = new HashMap<Integer, RoleBigDayConsume>();

	// big累计充值奖励领取记录
	private Map<Integer, RoleBigActivityForSumCharge> bigActivityForSumCharge = new HashMap<Integer, RoleBigActivityForSumCharge>();

	// big累计消费奖励领取记录
	private Map<Integer, RoleBigActivityForSumConsume> bigActivityForSumConsume = new HashMap<Integer, RoleBigActivityForSumConsume>();

	// big日消费元宝数
	private int bigDayConsumeYuanbao;

	// big累计消费活动消费元宝数量
	private int bigConsumeYuanbao;

	// 幸运大转盘
	private RoleFortuneWheel roleFortuneWheel;

	// // 购买限时武将次数--控制伪随机
	// private int buyLimitHeroCount;
	// // 购买限时武将元宝--控制伪随机
	// private int buyLimitHeroYuanbao;
	// 限时武将
	private RoleLimitHero roleLimitHero;

	/** 伙伴 */
	private RolePartner rolePartner;

	// 福袋领取记录
	private List<RoleLuckyBag> roleLuckyBags = new ArrayList<RoleLuckyBag>();

	private List<RoleSuperTurntable> roleSuperTurntable = new ArrayList<RoleSuperTurntable>();

	// 超级充值领取记录
	private RoleSuperCharge roleSuperCharge;

	// 聚宝盆
	private Map<Integer, RoleCornucopia> roleCornucopia = new HashMap<Integer, RoleCornucopia>();

	// 是否领取过聚宝盆超值物品
	private boolean isReceiveCornucopia;

	// 寻宝
	private Map<Integer, RoleTreasure> roleTreasures = new HashMap<Integer, RoleTreasure>();

	// 等级福利
	// private RoleLevelWeal roleLevelWeal;

	// 好友送军令
	private Map<String, RoleSnsJunLingLimit> roleSnsJunLingLimit = new HashMap<String, RoleSnsJunLingLimit>();

	// 双倍威望卡过期时间
	private Date doubleExpOutTime;

	// 双倍掉落过期时间
	private Date doubleItemOutTime;

	// 世界boss
	private RoleWorldBoss worldBoss;

	// 公会信息
	private RoleFaction roleFaction;

	// 豪情宝
	private RoleHaoqingbao haoqingbao;

	private List<RoleHaoqingbaoRecord> haoqingbaoRecords = new ArrayList<RoleHaoqingbaoRecord>();

	private List<RoleHaoqingbaoRedPacketRecord> redPacketRecords = new ArrayList<RoleHaoqingbaoRedPacketRecord>();

	private List<RoleClaimRedPacket> claimRedPackets = new ArrayList<RoleClaimRedPacket>();

	// 比武大会
	private RoleTournament tournament;
	// 比武大会，战斗记录
	private List<RoleTournamentRecord> tournamentFightRecords = new ArrayList<RoleTournamentRecord>();

	// 资源找回
	private List<RoleResourceAccept> resourceAcceptList = new ArrayList<RoleResourceAccept>();

	private RoleOpenedMenu roleOpenedMenu;

	private RoleTreasureParam roleTreasureParam;

	private int robotType;// 机器人类型0-竞技场1-群雄

	// 每日登录统计
	private List<RoleDailyLoginStatus> dailyLoginList = new ArrayList<RoleDailyLoginStatus>();

	/** 切磋胜利失败次数记录 */
	private RoleChallengeSummary challengeSummary;

	/** 南华幻境 */
	private RoleDreamland roleDreamland;

	private RoleCrossArena crossArena;

	private Date levelUpDate;// 升级时间

	private RoleFootball football;

	private Map<Integer, RoleArtifact> artifact = new HashMap<Integer, RoleArtifact>();

	/** default constructor */
	public Role() {

	}

	/** minimal constructor */
	public Role(String id, String account, int channel, String supportId, String name, Date createTime, short state,
			int heroSkillPoint, int serverId) {
		this.id = id;
		this.account = account;
		this.regChannel = channel;
		this.supportId = supportId;
		this.name = name;
		this.createTime = new Date(createTime.getTime());
		this.loginTime = new Date(createTime.getTime());
		this.state = state;
		this.onlineTime = 0;
		this.heroSkillPoint = heroSkillPoint;
		this.serverId = serverId;
	}

	// Property accessors

	@Id
	// @GeneratedValue
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "account", nullable = false, length = 16)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "name", unique = true, nullable = false, length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "login_time", nullable = true, length = 19)
	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "logout_time", nullable = true, length = 19)
	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date offlineTime) {
		this.logoutTime = offlineTime;
	}

	@Column(name = "state", nullable = false)
	public short getState() {
		return this.state;
	}

	public void setState(short state) {
		this.state = state;
	}

	@Column(name = "version", nullable = false, columnDefinition = "INT default 0")
	// @Version
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "vit", nullable = false, columnDefinition = "INT default 100")
	public int getVit() {
		return vit;
	}

	public void setVit(int vit) {
		this.vit = vit;
	}

	@Column(name = "vit_num", nullable = false, columnDefinition = "INT default 0")
	public int getVitNum() {
		return vitNum;
	}

	public void setVitNum(int vitNum) {
		this.vitNum = vitNum;
	}

	@Column(name = "vit_date")
	public Date getVitDate() {
		return vitDate;
	}

	public void setVitDate(Date vitDate) {
		this.vitDate = vitDate;
	}

	@Column(name = "vit_reset_date")
	public Date getvitResetDate() {
		return vitResetDate;
	}

	public void setvitResetDate(Date vitResetDate) {
		this.vitResetDate = vitResetDate;
	}

	@Column(name = "prestige", nullable = false)
	public int getPrestige() {
		return prestige;
	}

	public void setPrestige(int prestige) {
		this.prestige = prestige;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<RoleHero> getRoleHeros() {
		return roleHeros;
	}

	public void setRoleHeros(Set<RoleHero> roleGenerals) {
		this.roleHeros = roleGenerals;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<RoleFormation> getRoleFormations() {
		return roleFormations;
	}

	public void setRoleFormations(Set<RoleFormation> roleGroup) {
		this.roleFormations = roleGroup;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "id")
	@Fetch(FetchMode.SELECT)
	public Map<String, RoleItem> getItems() {
		return this.items;
	}

	public void setItems(Map<String, RoleItem> items) {
		this.items = items;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "templateId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleCopy> getRoleCopys() {
		return roleCopys;
	}

	public void setRoleCopys(Map<Integer, RoleCopy> roleCopys) {
		this.roleCopys = roleCopys;
	}

	@Column(name = "online_time", nullable = false)
	public int getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}

	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy =
	// "role")
	// @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	// @Fetch(FetchMode.SELECT)
	// public Set<RoleMail> getRoleMails() {
	// return roleMails;
	// }
	//
	// public void setRoleMails(Set<RoleMail> roleMails) {
	// this.roleMails = roleMails;
	// }

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "id")
	@Fetch(FetchMode.SELECT)
	public Map<String, RoleMail> getRoleMailMap() {
		return roleMailMap;
	}

	public void setRoleMailMap(Map<String, RoleMail> roleMailMap) {
		this.roleMailMap = roleMailMap;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "target")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "applicantRoleId")
	@Fetch(FetchMode.SELECT)
	public Map<String, FriendApplyingHistory> getFriendApplyingHistory() {
		return friendApplyingHistory;
	}

	public void setFriendApplyingHistory(Map<String, FriendApplyingHistory> friendApplyingHistory) {
		this.friendApplyingHistory = friendApplyingHistory;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<RoleSns> getSns() {
		return Sns;
	}

	public void setSns(Set<RoleSns> sns) {
		this.Sns = sns;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "id")
	@Fetch(FetchMode.SELECT)
	public Map<String, RoleSign> getSign() {
		return Sign;
	}

	public void setSign(Map<String, RoleSign> sign) {
		this.Sign = sign;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "type")
	@Fetch(FetchMode.SELECT)
	public Map<String, RoleAchieve> getRoleAchieves() {
		return roleAchieves;
	}

	public void setRoleAchieves(Map<String, RoleAchieve> roleAchieves) {
		this.roleAchieves = roleAchieves;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "taskId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleShare> getRoleShares() {
		return roleShares;
	}

	public void setRoleShares(Map<Integer, RoleShare> roleShares) {
		this.roleShares = roleShares;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "nodeId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleOpenServerActive> getOpenServerAct() {
		return openServerAct;
	}

	public void setOpenServerAct(Map<Integer, RoleOpenServerActive> openServerAct) {
		this.openServerAct = openServerAct;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "sId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleSevenProgress> getRoleSevenTasks() {
		return roleSevenTasks;
	}

	public void setRoleSevenTasks(Map<Integer, RoleSevenProgress> roleSevenTasks) {
		this.roleSevenTasks = roleSevenTasks;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<RoleTotalSignGift> getTotalSignGift() {
		return totalSignGift;
	}

	public void setTotalSignGift(Set<RoleTotalSignGift> totalSignGift) {
		this.totalSignGift = totalSignGift;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<RoleVitSending> getVitSending() {
		return vitSending;
	}

	public void setVitSending(Set<RoleVitSending> actSending) {
		this.vitSending = actSending;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<RoleVitReceiving> getVitReceiving() {
		return vitReceiving;
	}

	public void setVitReceiving(Set<RoleVitReceiving> receiving) {
		this.vitReceiving = receiving;
	}

	@Column(name = "last_salary_date")
	public String getLastSalaryDate() {
		return lastSalaryDate;
	}

	public void setLastSalaryDate(String lastSalaryDate) {
		this.lastSalaryDate = lastSalaryDate;
	}

	@Column(name = "rename_count", nullable = false, columnDefinition = "int default 0")
	public int getRenameCount() {
		return renameCount;
	}

	public void setRenameCount(int renameCount) {
		this.renameCount = renameCount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "type")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleMarket> getRoleMarkets() {
		return roleMarkets;
	}

	public void setRoleMarkets(Map<Integer, RoleMarket> roleMarkets) {
		this.roleMarkets = roleMarkets;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<ArenaRankFight> getArenaRankFightList() {
		return arenaRankFightList;
	}

	public void setArenaRankFightList(List<ArenaRankFight> arenaRankFightList) {
		this.arenaRankFightList = arenaRankFightList;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleTrader getTrader() {
		return trader;
	}

	public void setTrader(RoleTrader trader) {
		this.trader = trader;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public ChatSet getChatSet() {
		return chatSet;
	}

	public void setChatSet(ChatSet chatSet) {
		this.chatSet = chatSet;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleOnline getOnlineAward() {
		return onlineAward;
	}

	public void setOnlineAward(RoleOnline onlineAward) {
		this.onlineAward = onlineAward;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleMailHistory getMailHistory() {
		return mailHistory;
	}

	public void setMailHistory(RoleMailHistory mailHistory) {
		this.mailHistory = mailHistory;
	}

	@Column(name = "hero_skill_point", nullable = false, columnDefinition = "int default 20")
	public int getHeroSkillPoint() {
		return heroSkillPoint;
	}

	public void setHeroSkillPoint(int heroSkillPoint) {
		this.heroSkillPoint = heroSkillPoint;
	}

	@Column(name = "next_hero_skill_time", nullable = true)
	public Date getNextHeroSkillTime() {
		return nextHeroSkillTime;
	}

	public void setNextHeroSkillTime(Date nextHeroSkillTime) {
		this.nextHeroSkillTime = nextHeroSkillTime;
	}

	@Column(name = "hero_skill_buy_count", nullable = false, columnDefinition = "int default 0")
	public int getHeroSkillBuyCount() {
		return heroSkillBuyCount;
	}

	public void setHeroSkillBuyCount(int heroSkillBuyCount) {
		this.heroSkillBuyCount = heroSkillBuyCount;
	}

	@Column(name = "hero_skill_buy_time", nullable = true)
	public Date getHeroSkillBuyTime() {
		return heroSkillBuyTime;
	}

	public void setHeroSkillBuyTime(Date heroSkillBuyTime) {
		this.heroSkillBuyTime = heroSkillBuyTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "taskId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleTask> getRoleTask() {
		return roleTask;
	}

	public void setRoleTask(Map<Integer, RoleTask> roleTask) {
		this.roleTask = roleTask;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleRecallTask> getRoleRecallTask() {
		return roleRecallTask;
	}

	public void setRoleRecallTask(List<RoleRecallTask> roleRecallTask) {
		this.roleRecallTask = roleRecallTask;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleFriendsInvitation getRoleFriendsInvitation() {
		return roleFriendsInvitation;
	}

	public void setRoleFriendsInvitation(RoleFriendsInvitation roleFriendsInvitation) {
		this.roleFriendsInvitation = roleFriendsInvitation;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleFriendsRecalled getRoleFriendsRecalled() {
		return roleFriendsRecalled;
	}

	public void setRoleFriendsRecalled(RoleFriendsRecalled roleFriendsRecalled) {
		this.roleFriendsRecalled = roleFriendsRecalled;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleAchieveProgressRec getRoleAchieveProgressRec() {
		return roleAchieveProgressRec;
	}

	public void setRoleAchieveProgressRec(RoleAchieveProgressRec roleAchieveProgressRec) {
		this.roleAchieveProgressRec = roleAchieveProgressRec;
	}

	@Column(name = "chapter_tag", nullable = true)
	public String getChapterTag() {
		return chapterTag;
	}

	public void setChapterTag(String chapterTag) {
		this.chapterTag = chapterTag;
	}

	@Column(name = "market_charm", nullable = false, columnDefinition = "int default 0")
	public int getMarketCharm() {
		return marketCharm;
	}

	public void setMarketCharm(int marketCharm) {
		this.marketCharm = marketCharm;
	}

	@Column(name = "head_image", nullable = false, columnDefinition = "varchar(10) default ''")
	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	@Column(name = "ext_head_image", nullable = true, columnDefinition = "varchar(64) default ''")
	public String getExtHeadImage() {
		return extHeadImage;
	}

	public void setExtHeadImage(String extHeadImage) {
		this.extHeadImage = extHeadImage;
	}

	@Column(name = "head_border", nullable = true, columnDefinition = "varchar(10) default ''")
	public String getHeadBorder() {
		return headBorder;
	}

	public void setHeadBorder(String headBorder) {
		this.headBorder = headBorder;
	}

	@Column(name = "ext_head_border", nullable = true, columnDefinition = "varchar(64) default ''")
	public String getExtHeadBorder() {
		return extHeadBorder;
	}

	public void setExtHeadBorder(String extHeadBorder) {
		this.extHeadBorder = extHeadBorder;
	}

	@Column(name = "buy_jinbi_num", columnDefinition = "int default 0")
	public int getBuyJinbiNum() {
		return buyJinbiNum;
	}

	public void setBuyJinbiNum(int buyJinbiNum) {
		this.buyJinbiNum = buyJinbiNum;
	}

	@Column(name = "buy_jinbi_time")
	public Date getBuyJinbiTime() {
		return buyJinbiTime;
	}

	public void setBuyJinbiTime(Date buyJinbiTime) {
		this.buyJinbiTime = buyJinbiTime;
	}

	/**
	 * @return the copyBuff
	 */
	@Column(name = "copy_buff", nullable = false)
	public int getCopyBuff() {
		return copyBuff;
	}

	/**
	 * @param copyBuff
	 *            the copyBuff to set
	 */
	public void setCopyBuff(int copyBuff) {
		this.copyBuff = copyBuff;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<ChatMessageOffline> getChatMessageOfflineList() {
		return ChatMessageOfflineList;
	}

	public void setChatMessageOfflineList(List<ChatMessageOffline> ChatMessageOfflineList) {
		this.ChatMessageOfflineList = ChatMessageOfflineList;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "category")
	@Fetch(FetchMode.SELECT)
	public Map<String, RoleCDKRecord> getCdkRecords() {
		return cdkRecords;
	}

	public void setCdkRecords(Map<String, RoleCDKRecord> cdkRecords) {
		this.cdkRecords = cdkRecords;
	}

	@Column(name = "silence_expire", nullable = true)
	public Date getSilenceExpire() {
		return silenceExpire;
	}

	public void setSilenceExpire(Date silenceExpire) {
		this.silenceExpire = silenceExpire;
	}

	@Column(name = "sex", nullable = false, columnDefinition = "int default 1")
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleUpGift> getRoleUpGifts() {
		return roleUpGifts;
	}

	public void setRoleUpGifts(List<RoleUpGift> roleUpGifts) {
		this.roleUpGifts = roleUpGifts;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleVip getVip() {
		return vip;
	}

	public void setVip(RoleVip vip) {
		this.vip = vip;
	}

	@Column(name = "support_id", nullable = false)
	public String getSupportId() {
		return supportId;
	}

	public void setSupportId(String supportId) {
		this.supportId = supportId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleShopItem> getShopItems() {
		return shopItems;
	}

	public void setShopItems(List<RoleShopItem> shopItems) {
		this.shopItems = shopItems;
	}

	@Column(name = "last_buy_item_date")
	public Date getLastBuyItemDate() {
		return lastBuyItemDate;
	}

	public void setLastBuyItemDate(Date lastBuyItemDate) {
		this.lastBuyItemDate = lastBuyItemDate;
	}

	@Column(name = "last_time_battle_date")
	public Date getLastTimeBattleDate() {
		return lastTimeBattleDate;
	}

	public void setLastTimeBattleDate(Date lastTimeBattleDate) {
		this.lastTimeBattleDate = lastTimeBattleDate;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "passId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleTimeBattle> getTimeBattle() {
		return timeBattle;
	}

	public void setTimeBattle(Map<Integer, RoleTimeBattle> timeBattle) {
		this.timeBattle = timeBattle;
	}

	@Column(name = "complete_guide", length = 256, columnDefinition = "varchar(512) default '[]'")
	public String getCompleteGuide() {
		return completeGuide;
	}

	public void setCompleteGuide(String completeGuide) {
		this.completeGuide = completeGuide;
	}

	@Column(name = "faction_id")
	public String getFactionId() {
		return factionId;
	}

	public void setFactionId(String factionId) {
		this.factionId = factionId;
	}

	@Column(name = "worship_count", nullable = false, columnDefinition = "int default 0")
	public int getWorshipCount() {
		return worshipCount;
	}

	public void setWorshipCount(int worshipCount) {
		this.worshipCount = worshipCount;
	}

	@Column(name = "buy_hudong_count", nullable = false, columnDefinition = "int default 0")
	public int getBuyHudongCount() {
		return buyHudongCount;
	}

	public void setBuyHudongCount(int buyHudongCount) {
		this.buyHudongCount = buyHudongCount;
	}

	@Column(name = "last_hudong_date")
	public Date getLastHudongDate() {
		return lastHudongDate;
	}

	public void setLastHudongDate(Date lastHudongDate) {
		this.lastHudongDate = lastHudongDate;
	}

	@OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleWorship> getWorships() {
		return worships;
	}

	public void setWorships(List<RoleWorship> worships) {
		this.worships = worships;
	}

	@Column(name = "last_rename_time")
	public Date getLastRenameTime() {
		return lastRenameTime;
	}

	public void setLastRenameTime(Date lastRenameTime) {
		this.lastRenameTime = lastRenameTime;
	}

	@Column(name = "combat_power", nullable = false, columnDefinition = "int default 0")
	public int getCombatPower() {
		return combatPower;
	}

	public void setCombatPower(int combatPower) {
		this.combatPower = combatPower;
	}

	@Column(name = "chip_rob_stat", nullable = false, columnDefinition = "int default 0")
	public int getChipRobStat() {
		return chipRobStat;
	}

	public void setChipRobStat(int chipRobStat) {
		this.chipRobStat = chipRobStat;
	}

	@Column(name = "quit_faction_date")
	public Date getQuitFactionDate() {
		return quitFactionDate;
	}

	@Column(name = "sign_count_for_tc", nullable = false, columnDefinition = "int default 0")
	public int getSignCountForTc() {
		return signCountForTc;
	}

	public void setSignCountForTc(int c) {
		signCountForTc = c;
	}

	public void setQuitFactionDate(Date quitFactionDate) {
		this.quitFactionDate = quitFactionDate;
	}

	@Column(name = "reg_channel", nullable = false)
	public int getRegChannel() {
		return this.regChannel;
	}

	public void setRegChannel(int regChannel) {
		this.regChannel = regChannel;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleArenaRank getArenaRank() {
		return arenaRank;
	}

	public void setArenaRank(RoleArenaRank arenaRank) {
		this.arenaRank = arenaRank;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "templateId")
	@Fetch(FetchMode.SELECT)
	public Map<String, RoleChestMock> getRoleChestMocks() {
		return roleChestMocks;
	}

	public void setRoleChestMocks(Map<String, RoleChestMock> roleChestMocks) {
		this.roleChestMocks = roleChestMocks;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "announceId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleAnnounce> getAnnounces() {
		return this.announces;
	}

	public void setAnnounces(Map<Integer, RoleAnnounce> announces) {
		this.announces = announces;
	}

	@Column(name = "last_answer_date")
	public Date getLastAnswerDate() {
		return lastAnswerDate;
	}

	public void setLastAnswerDate(Date lastAnswerDate) {
		this.lastAnswerDate = lastAnswerDate;
	}

	// @Column(name = "right_answer_times")
	// public int getRightAnswerTimes() {
	// return rightAnswerTimes;
	// }
	//
	// public void setRightAnswerTimes(int rightAnswerTimes) {
	// this.rightAnswerTimes = rightAnswerTimes;
	// }

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleLadder getLadder() {
		return ladder;
	}

	public void setLadder(RoleLadder ladder) {
		this.ladder = ladder;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "fight_time DESC")
	public List<RoleLadderReport> getLadderReportList() {
		return ladderReportList;
	}

	public void setLadderReportList(List<RoleLadderReport> ladderReportList) {
		this.ladderReportList = ladderReportList;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleCollectHeroSoul> getCollectHeroSoulList() {
		return collectHeroSoulList;
	}

	public void setCollectHeroSoulList(List<RoleCollectHeroSoul> list) {
		this.collectHeroSoulList = list;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleHeroAdmire getHeroAdmire() {
		return heroAdmire;
	}

	public void setHeroAdmire(RoleHeroAdmire heroAdmire) {
		this.heroAdmire = heroAdmire;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleActivityForSumCharge> getActivityForSumCharge() {
		return activityForSumCharge;
	}

	public void setActivityForSumCharge(Map<Integer, RoleActivityForSumCharge> activityForSumCharge) {
		this.activityForSumCharge = activityForSumCharge;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleActivityForSumConsume> getActivityForSumConsume() {
		return activityForSumConsume;
	}

	public void setActivityForSumConsume(Map<Integer, RoleActivityForSumConsume> activityForSumConsume) {
		this.activityForSumConsume = activityForSumConsume;
	}

	@Column(name = "last_consume_time")
	public Date getLastConsumeTime() {
		return lastConsumeTime;
	}

	public void setLastConsumeTime(Date lastConsumeTime) {
		this.lastConsumeTime = lastConsumeTime;
	}

	@Column(name = "consume_yuanbao")
	public int getConsumeYuanbao() {
		return consumeYuanbao;
	}

	public void setConsumeYuanbao(int consumeYuanbao) {
		this.consumeYuanbao = consumeYuanbao;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleAttackCastle getRoleAttackCastle() {
		return roleAttackCastle;
	}

	public void setRoleAttackCastle(RoleAttackCastle roleAttackCastle) {
		this.roleAttackCastle = roleAttackCastle;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleAuctionHouse getAuctionHouse() {
		return auctionHouse;
	}

	public void setAuctionHouse(RoleAuctionHouse house) {
		this.auctionHouse = house;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleRedPacket getRoleRedPacket() {
		return roleRedPacket;
	}

	public void setRoleRedPacket(RoleRedPacket roleRedPacket) {
		this.roleRedPacket = roleRedPacket;
	}

	@Column(name = "invite_num")
	public int getInviteNum() {
		return inviteNum;
	}

	public void setInviteNum(int inviteNum) {
		this.inviteNum = inviteNum;
	}

	/**
	 * @return the invitedNum
	 */
	@Column(name = "invited_num")
	public int getInvitedNum() {
		return invitedNum;
	}

	/**
	 * @param invitedNum
	 *            the invitedNum to set
	 */
	public void setInvitedNum(int invitedNum) {
		this.invitedNum = invitedNum;
	}

	@Column(name = "invite_code")
	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleInviteActivity> getInviteActivity() {
		return inviteActivity;
	}

	public void setInviteActivity(Map<Integer, RoleInviteActivity> inviteActivity) {
		this.inviteActivity = inviteActivity;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "templateId")
	@Fetch(FetchMode.SELECT)
	public Map<String, RoleMock> getRoleMocks() {
		return roleMocks;
	}

	public void setRoleMocks(Map<String, RoleMock> roleMocks) {
		this.roleMocks = roleMocks;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleInviteLog> getRoleInviteLogs() {
		return roleInviteLogs;
	}

	/**
	 * @return the roleSnsJunLingLimit
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "targetId")
	@Fetch(FetchMode.SELECT)
	public Map<String, RoleSnsJunLingLimit> getRoleSnsJunLingLimit() {
		return roleSnsJunLingLimit;
	}

	/**
	 * @param roleSnsJunLingLimit
	 *            the roleSnsJunLingLimit to set
	 */
	public void setRoleSnsJunLingLimit(Map<String, RoleSnsJunLingLimit> roleSnsJunLingLimit) {
		this.roleSnsJunLingLimit = roleSnsJunLingLimit;
	}

	public void setRoleInviteLogs(List<RoleInviteLog> roleInviteLogs) {
		this.roleInviteLogs = roleInviteLogs;
	}

	@Column(name = "buy_junling_num")
	public int getBuyJunLingNum() {
		return buyJunLingNum;
	}

	public void setBuyJunLingNum(int buyJunLingNum) {
		this.buyJunLingNum = buyJunLingNum;
	}

	@Column(name = "buy_junling_time")
	public Date getBuyJunLingTime() {
		return buyJunLingTime;
	}

	public void setBuyJunLingTime(Date buyJunLingTime) {
		this.buyJunLingTime = buyJunLingTime;
	}

	@Column(name = "next_junling_time")
	public Date getNextJunLingTime() {
		return nextJunLingTime;
	}

	public void setNextJunLingTime(Date nextJunLingTime) {
		this.nextJunLingTime = nextJunLingTime;
	}

	/**
	 * 获取成长基金
	 * 
	 * @return the fund
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleFund getFund() {
		return fund;
	}

	/**
	 * @param fund
	 *            the fund to set
	 */
	public void setFund(RoleFund fund) {
		this.fund = fund;
	}

	/**
	 * @return the levelReward
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleLevelReward getLevelReward() {
		return levelReward;
	}

	/**
	 * @param levelReward
	 *            the levelReward to set
	 */
	public void setLevelReward(RoleLevelReward levelReward) {
		this.levelReward = levelReward;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleTaskActive getRoleTaskActive() {
		return roleTaskActive;
	}

	public void setRoleTaskActive(RoleTaskActive roleTaskActive) {
		this.roleTaskActive = roleTaskActive;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleSevenTask getRoleSevenTask() {
		return roleSevenTask;
	}

	public void setRoleSevenTask(RoleSevenTask roleSevenTask) {
		this.roleSevenTask = roleSevenTask;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleWeixinShare getRoleWeixinShare() {
		return roleWeixinShare;
	}

	public void setRoleWeixinShare(RoleWeixinShare roleWeixinShare) {
		this.roleWeixinShare = roleWeixinShare;
	}

	/**
	 * @return the powerReward
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RolePowerReward getPowerReward() {
		return powerReward;
	}

	/**
	 * @return the firstJia
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleFirstJia getFirstJia() {
		return firstJia;
	}

	/**
	 * @return the dayLoginReward
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleDayLoginReward getDayLoginReward() {
		return dayLoginReward;
	}

	/**
	 * @param dayLoginReward
	 *            the dayLoginReward to set
	 */
	public void setDayLoginReward(RoleDayLoginReward dayLoginReward) {
		this.dayLoginReward = dayLoginReward;
	}

	/**
	 * @return the dayforverLoginReward
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleDayforverLoginReward getDayforverLoginReward() {
		return dayforverLoginReward;
	}

	/**
	 * @param dayforverLoginReward
	 *            the dayforverLoginReward to set
	 */
	public void setDayforverLoginReward(RoleDayforverLoginReward dayforverLoginReward) {
		this.dayforverLoginReward = dayforverLoginReward;
	}

	/**
	 * @param firstJia
	 *            the firstJia to set
	 */
	public void setFirstJia(RoleFirstJia firstJia) {
		this.firstJia = firstJia;
	}

	/**
	 * @return the sendJunLing
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleSendJunLing getSendJunLing() {
		return sendJunLing;
	}

	/**
	 * @param sendJunLing
	 *            the sendJunLing to set
	 */
	public void setSendJunLing(RoleSendJunLing sendJunLing) {
		this.sendJunLing = sendJunLing;
	}

	/**
	 * @param powerReward
	 *            the powerReward to set
	 */
	public void setPowerReward(RolePowerReward powerReward) {
		this.powerReward = powerReward;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleFourthTest getFourthTest() {
		return fourthTest;
	}

	public void setFourthTest(RoleFourthTest fourthTest) {
		this.fourthTest = fourthTest;
	}

	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy =
	// "role")
	// @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	// @MapKey(name = "onlineId")
	// @Fetch(FetchMode.SELECT)
	// public Map<Integer, RoleOnline> getOnlineAward() {
	// return onlineAward;
	// }
	//
	// public void setOnlineAward(Map<Integer, RoleOnline> onlineAward) {
	// this.onlineAward = onlineAward;
	// }
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "seckillId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleSeckill> getRoleSeckills() {
		return roleSeckills;
	}

	public void setRoleSeckills(Map<Integer, RoleSeckill> roleSeckills) {
		this.roleSeckills = roleSeckills;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleDayCharge> getDayCharges() {
		return dayCharges;
	}

	public void setDayCharges(Map<Integer, RoleDayCharge> dayCharges) {
		this.dayCharges = dayCharges;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleDayConsume> getDayConsume() {
		return dayConsume;
	}

	public void setDayConsume(Map<Integer, RoleDayConsume> dayConsume) {
		this.dayConsume = dayConsume;
	}

	@Column(name = "day_consume_yuanbao")
	public int getDayConsumeYuanbao() {
		return dayConsumeYuanbao;
	}

	public void setDayConsumeYuanbao(int dayConsumeYuanbao) {
		this.dayConsumeYuanbao = dayConsumeYuanbao;
	}

	@Column(name = "big_day_consume_yuanbao")
	public int getBigDayConsumeYuanbao() {
		return bigDayConsumeYuanbao;
	}

	public void setBigDayConsumeYuanbao(int bigDayConsumeYuanbao) {
		this.bigDayConsumeYuanbao = bigDayConsumeYuanbao;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleBigDayCharge> getBigDayCharges() {
		return bigDayCharges;
	}

	public void setBigDayCharges(Map<Integer, RoleBigDayCharge> bigDayCharges) {
		this.bigDayCharges = bigDayCharges;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleBigDayConsume> getBigDayConsume() {
		return bigDayConsume;
	}

	public void setBigDayConsume(Map<Integer, RoleBigDayConsume> bigDayConsume) {
		this.bigDayConsume = bigDayConsume;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleBigActivityForSumCharge> getBigActivityForSumCharge() {
		return bigActivityForSumCharge;
	}

	public void setBigActivityForSumCharge(Map<Integer, RoleBigActivityForSumCharge> bigActivityForSumCharge) {
		this.bigActivityForSumCharge = bigActivityForSumCharge;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "threshold")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleBigActivityForSumConsume> getBigActivityForSumConsume() {
		return bigActivityForSumConsume;
	}

	public void setBigActivityForSumConsume(Map<Integer, RoleBigActivityForSumConsume> bigActivityForSumConsume) {
		this.bigActivityForSumConsume = bigActivityForSumConsume;
	}

	@Column(name = "big_consume_yuanbao")
	public int getBigConsumeYuanbao() {
		return bigConsumeYuanbao;
	}

	public void setBigConsumeYuanbao(int bigConsumeYuanbao) {
		this.bigConsumeYuanbao = bigConsumeYuanbao;
	}

	/**
	 * @return the roleFortuneWheel
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleFortuneWheel getRoleFortuneWheel() {
		return roleFortuneWheel;
	}

	/**
	 * @param roleFortuneWheel
	 *            the roleFortuneWheel to set
	 */
	public void setRoleFortuneWheel(RoleFortuneWheel roleFortuneWheel) {
		this.roleFortuneWheel = roleFortuneWheel;
	}

	// @Column(name = "buy_limit_hero_count")
	// public int getBuyLimitHeroCount() {
	// return buyLimitHeroCount;
	// }
	//
	// public void setBuyLimitHeroCount(int buyLimitHeroCount) {
	// this.buyLimitHeroCount = buyLimitHeroCount;
	// }
	//
	// @Column(name = "buy_limit_hero_yuanbao")
	// public int getBuyLimitHeroYuanbao() {
	// return buyLimitHeroYuanbao;
	// }
	//
	// public void setBuyLimitHeroYuanbao(int buyLimitHeroYuanbao) {
	// this.buyLimitHeroYuanbao = buyLimitHeroYuanbao;
	// }

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleLimitHero getRoleLimitHero() {
		return roleLimitHero;
	}

	public void setRoleLimitHero(RoleLimitHero roleLimitHero) {
		this.roleLimitHero = roleLimitHero;
	}

	// /**
	// * @return the roleLevelWeal
	// */
	// @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy =
	// "role")
	// public RoleLevelWeal getRoleLevelWeal() {
	// return roleLevelWeal;
	// }
	//
	// /**
	// * @param roleLevelWeal the roleLevelWeal to set
	// */
	// public void setRoleLevelWeal(RoleLevelWeal roleLevelWeal) {
	// this.roleLevelWeal = roleLevelWeal;
	// }

	/**
	 * @return the firstInTime
	 */
	@Column(name = "first_in_time")
	public long getFirstInTime() {
		return firstInTime;
	}

	/**
	 * @param firstInTime
	 *            the firstInTime to set
	 */
	public void setFirstInTime(long firstInTime) {
		this.firstInTime = firstInTime;
	}

	@Column(name = "total_warmup_count")
	public int getTotalWarmupCount() {
		return totalWarmupCount;
	}

	public void setTotalWarmupCount(int totalWarmupCount) {
		this.totalWarmupCount = totalWarmupCount;
	}

	@Column(name = "win_warmup_count")
	public int getWinWarupCount() {
		return winWarupCount;
	}

	public void setWinWarupCount(int winWarupCount) {
		this.winWarupCount = winWarupCount;
	}

	@Column(name = "warmup_update_time")
	public Date getWarmupUpdateTime() {
		return warmupUpdateTime;
	}

	public void setWarmupUpdateTime(Date warmupUpdateTime) {
		this.warmupUpdateTime = warmupUpdateTime;
	}

	@Column(name = "lose_warmup_count")
	public int getLoseWarmupCount() {
		return loseWarmupCount;
	}

	public void setLoseWarmupCount(int loseWarmupCount) {
		this.loseWarmupCount = loseWarmupCount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleLuckyBag> getRoleLuckyBags() {
		return roleLuckyBags;
	}

	@Column(name = "achieve_completed_num")
	public int getAchieveCompletedCount() {
		return achieveCompletedCount;
	}

	public void setAchieveCompletedCount(int achieveCompletedCount) {
		this.achieveCompletedCount = achieveCompletedCount;
	}

	public void setRoleLuckyBags(List<RoleLuckyBag> roleLuckyBags) {
		this.roleLuckyBags = roleLuckyBags;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleExchangeItem> getExchangeItems() {
		return exchangeItems;
	}

	public void setExchangeItems(List<RoleExchangeItem> exchangeItems) {
		this.exchangeItems = exchangeItems;
	}

	/**
	 * @return the redPacketRecords
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleHaoqingbaoRedPacketRecord> getRedPacketRecords() {
		return redPacketRecords;
	}

	/**
	 * @param redPacketRecords
	 *            the redPacketRecords to set
	 */
	public void setRedPacketRecords(List<RoleHaoqingbaoRedPacketRecord> redPacketRecords) {
		this.redPacketRecords = redPacketRecords;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "scriptId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleCornucopia> getRoleCornucopia() {
		return roleCornucopia;
	}

	public void setRoleCornucopia(Map<Integer, RoleCornucopia> roleCornucopia) {
		this.roleCornucopia = roleCornucopia;
	}

	@Column(name = "is_receive_cornucopia")
	public boolean isReceiveCornucopia() {
		return isReceiveCornucopia;
	}

	public void setReceiveCornucopia(boolean isReceiveCornucopia) {
		this.isReceiveCornucopia = isReceiveCornucopia;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public RolePartner getRolePartner() {
		return rolePartner;
	}

	public void setRolePartner(RolePartner rolePartner) {
		this.rolePartner = rolePartner;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "groupNum")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleTreasure> getRoleTreasures() {
		return roleTreasures;
	}

	public void setRoleTreasures(Map<Integer, RoleTreasure> roleTreasures) {
		this.roleTreasures = roleTreasures;
	}

	@Column(name = "double_exp_out_time")
	public Date getDoubleExpOutTime() {
		return doubleExpOutTime;
	}

	public void setDoubleExpOutTime(Date doubleExpOutTime) {
		this.doubleExpOutTime = doubleExpOutTime;
	}

	@Column(name = "double_item_out_time")
	public Date getDoubleItemOutTime() {
		return doubleItemOutTime;
	}

	public void setDoubleItemOutTime(Date doubleItemOutTime) {
		this.doubleItemOutTime = doubleItemOutTime;
	}

	/**
	 * @return the haoqingbao
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleHaoqingbao getHaoqingbao() {
		return haoqingbao;
	}

	/**
	 * @param haoqingbao
	 *            the haoqingbao to set
	 */
	public void setHaoqingbao(RoleHaoqingbao haoqingbao) {
		this.haoqingbao = haoqingbao;
	}

	/**
	 * @return the tournament
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleTournament getTournament() {
		return tournament;
	}

	/**
	 * @param tournament
	 *            the tournament to set
	 */
	public void setTournament(RoleTournament tournament) {
		this.tournament = tournament;
	}

	/**
	 * @return the haoqingbaoRecords
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleHaoqingbaoRecord> getHaoqingbaoRecords() {
		return haoqingbaoRecords;
	}

	/**
	 * @param haoqingbaoRecords
	 *            the haoqingbaoRecords to set
	 */
	public void setHaoqingbaoRecords(List<RoleHaoqingbaoRecord> haoqingbaoRecords) {
		this.haoqingbaoRecords = haoqingbaoRecords;
	}

	/**
	 * @return the claimRedPackets
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleClaimRedPacket> getClaimRedPackets() {
		return claimRedPackets;
	}

	/**
	 * @param claimRedPackets
	 *            the claimRedPackets to set
	 */
	public void setClaimRedPackets(List<RoleClaimRedPacket> claimRedPackets) {
		this.claimRedPackets = claimRedPackets;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleWorldBoss getWorldBoss() {
		return worldBoss;
	}

	public void setWorldBoss(RoleWorldBoss worldBoss) {
		this.worldBoss = worldBoss;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleFaction getRoleFaction() {
		return roleFaction;
	}

	public void setRoleFaction(RoleFaction roleFaction) {
		this.roleFaction = roleFaction;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleSmithy getSmithy() {
		return smithy;
	}

	@Column(name = "warmup_escape_count")
	public int getWarmupEscapeCount() {
		return warmupEscapeCount;
	}

	public void setWarmupEscapeCount(int warmupEscapeCount) {
		this.warmupEscapeCount = warmupEscapeCount;
	}

	@Column(name = "warmup_escape_time")
	public Date getWarmupEscapeTime() {
		return warmupEscapeTime;
	}

	public void setWarmupEscapeTime(Date warmupEscapeTime) {
		this.warmupEscapeTime = warmupEscapeTime;
	}

	@Column(name = "server_id")
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public void setSmithy(RoleSmithy smithy) {
		this.smithy = smithy;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleSuperCharge getRoleSuperCharge() {
		return roleSuperCharge;
	}

	public void setRoleSuperCharge(RoleSuperCharge roleSuperCharge) {
		this.roleSuperCharge = roleSuperCharge;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleSuperTurntable> getRoleSuperTurntable() {
		return roleSuperTurntable;
	}

	public void setRoleSuperTurntable(List<RoleSuperTurntable> roleSuperTurntable) {
		this.roleSuperTurntable = roleSuperTurntable;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "actId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleApi> getRoleApiMap() {
		return roleApiMap;
	}

	public void setRoleApiMap(Map<Integer, RoleApi> roleApiMap) {
		this.roleApiMap = roleApiMap;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleOpenedMenu getRoleOpenedMenu() {
		return roleOpenedMenu;
	}

	public void setRoleOpenedMenu(RoleOpenedMenu roleOpenedMenu) {
		this.roleOpenedMenu = roleOpenedMenu;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleBlueSmithy getBlueSmithy() {
		return blueSmithy;
	}

	public void setBlueSmithy(RoleBlueSmithy blueSmithy) {
		this.blueSmithy = blueSmithy;
	}

	/**
	 * @return the tournamentFightRecords
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleTournamentRecord> getTournamentFightRecords() {
		return tournamentFightRecords;
	}

	/**
	 * @param tournamentFightRecords
	 *            the tournamentFightRecords to set
	 */
	public void setTournamentFightRecords(List<RoleTournamentRecord> tournamentFightRecords) {
		this.tournamentFightRecords = tournamentFightRecords;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleTreasureParam getRoleTreasureParam() {
		return roleTreasureParam;
	}

	public void setRoleTreasureParam(RoleTreasureParam roleTreasureParam) {
		this.roleTreasureParam = roleTreasureParam;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleColorfulEgg getRoleColorfullEgg() {
		return roleColorfullEgg;
	}

	public void setRoleColorfullEgg(RoleColorfulEgg roleColorfullEgg) {
		this.roleColorfullEgg = roleColorfullEgg;
	}

	/**
	 * @return the resourceAcceptList
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleResourceAccept> getResourceAcceptList() {
		return resourceAcceptList;
	}

	/**
	 * @param resourceAcceptList
	 *            the resourceAcceptList to set
	 */
	public void setResourceAcceptList(List<RoleResourceAccept> resourceAcceptList) {
		this.resourceAcceptList = resourceAcceptList;
	}

	/**
	 * @return the dailyLoginList
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public List<RoleDailyLoginStatus> getDailyLoginList() {
		return dailyLoginList;
	}

	/**
	 * @param dailyLoginList
	 *            the dailyLoginList to set
	 */
	public void setDailyLoginList(List<RoleDailyLoginStatus> dailyLoginList) {
		this.dailyLoginList = dailyLoginList;
	}

	@Column(name = "robot_type", nullable = false)
	public int getRobotType() {
		return robotType;
	}

	public void setRobotType(int robotType) {
		this.robotType = robotType;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleChallengeSummary getChallengeSummary() {
		return challengeSummary;
	}

	public void setChallengeSummary(RoleChallengeSummary challengeSummary) {
		this.challengeSummary = challengeSummary;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "role")
	public RoleCrossArena getCrossArena() {
		return crossArena;
	}

	public void setCrossArena(RoleCrossArena crossArena) {
		this.crossArena = crossArena;
	}

	/**
	 * @return Returns the roleDreamland.
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleDreamland getRoleDreamland() {
		return roleDreamland;
	}

	/**
	 * @param roleDreamland
	 *            The roleDreamland to set.
	 */
	public void setRoleDreamland(RoleDreamland roleDreamland) {
		this.roleDreamland = roleDreamland;
	}

	@Column(name = "level_up_date")
	public Date getLevelUpDate() {
		return levelUpDate;
	}

	public void setLevelUpDate(Date levelUpDate) {
		this.levelUpDate = levelUpDate;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
	public RoleFootball getFootball() {
		return football;
	}

	public void setFootball(RoleFootball football) {
		this.football = football;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "role", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "artifactId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleArtifact> getArtifact() {
		return artifact;
	}

	public void setArtifact(Map<Integer, RoleArtifact> artifact) {
		this.artifact = artifact;
	}

}
