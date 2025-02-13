/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.role;

import java.util.Date;
import java.util.Set;

import com.XSanGo.Protocol.ActivityAnnounceView;
import com.XSanGo.Protocol.HeroEquipView;
import com.XSanGo.Protocol.HeroSkillPointView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RoleView;
import com.XSanGo.Protocol.RoleViewForGM;
import com.XSanGo.Protocol.RoleViewForOtherPlayer;
import com.morefun.XSanGo.ArenaRank.CrossArenaControler;
import com.morefun.XSanGo.ArenaRank.IArenaRankControler;
import com.morefun.XSanGo.AttackCastle.IAttackCastleController;
import com.morefun.XSanGo.MFBI.IMFBIControler;
import com.morefun.XSanGo.achieve.IAchieveControler;
import com.morefun.XSanGo.activity.FootballControler;
import com.morefun.XSanGo.activity.IActivityControler;
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
import com.morefun.XSanGo.api.IApiController;
import com.morefun.XSanGo.auction.IAuctionHouseController;
import com.morefun.XSanGo.buyJinbi.IBuyJInbiControler;
import com.morefun.XSanGo.chat.IChatControler;
import com.morefun.XSanGo.collect.ICollectHeroSoulController;
import com.morefun.XSanGo.colorfulEgg.IColorfullEggController;
import com.morefun.XSanGo.copy.ICopyControler;
import com.morefun.XSanGo.copy.IWorshipRankControler;
import com.morefun.XSanGo.crossServer.ITournamentController;
import com.morefun.XSanGo.db.game.FriendApplyingHistory;
import com.morefun.XSanGo.db.game.RoleFaction;
import com.morefun.XSanGo.db.game.RoleInviteLog;
import com.morefun.XSanGo.db.game.RoleOpenedMenu;
import com.morefun.XSanGo.db.game.RoleSns;
import com.morefun.XSanGo.db.game.RoleWeixinShare;
import com.morefun.XSanGo.db.game.RoleWorldBoss;
import com.morefun.XSanGo.dreamland.IDreamlandController;
import com.morefun.XSanGo.equip.IArtifactControler;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.faction.IFactionControler;
import com.morefun.XSanGo.faction.factionBattle.IFactionBattleController;
import com.morefun.XSanGo.formation.IFormationControler;
import com.morefun.XSanGo.friendsRecall.IFriendsRecallController;
import com.morefun.XSanGo.goodsExchange.IExchangeActivityControler;
import com.morefun.XSanGo.haoqingbao.IHaoqingbaoController;
import com.morefun.XSanGo.hero.IHeroControler;
import com.morefun.XSanGo.hero.market.IHeroMarketControler;
import com.morefun.XSanGo.heroAdmire.IHeroAdmireControler;
import com.morefun.XSanGo.heroAwaken.IHeroAwakenController;
import com.morefun.XSanGo.item.IItemControler;
import com.morefun.XSanGo.itemChip.IItemChipControler;
import com.morefun.XSanGo.ladder.ILadderControler;
import com.morefun.XSanGo.luckybag.ILuckyBagControler;
import com.morefun.XSanGo.mail.IMailControler;
import com.morefun.XSanGo.makewine.IMakeWineControler;
import com.morefun.XSanGo.notify.INotifyControler;
import com.morefun.XSanGo.onlineAward.IOnlineAwardControler;
import com.morefun.XSanGo.partner.IPartnerControler;
import com.morefun.XSanGo.rankList.IRankListControler;
import com.morefun.XSanGo.reward.IRewardControler;
import com.morefun.XSanGo.shop.IShopControler;
import com.morefun.XSanGo.sign.ISignController;
import com.morefun.XSanGo.smithyExchange.ISmithyExchangeController;
import com.morefun.XSanGo.sns.ISnsControler;
import com.morefun.XSanGo.superCharge.ISuperChargeController;
import com.morefun.XSanGo.superRaffle.ISuperRaffleController;
import com.morefun.XSanGo.task.ITaskControler;
import com.morefun.XSanGo.timeBattle.ITimeBattleControler;
import com.morefun.XSanGo.trader.ITraderControler;
import com.morefun.XSanGo.treasure.ITreasureControler;
import com.morefun.XSanGo.vip.IVipController;
import com.morefun.XSanGo.worldboss.IWorldBossControler;

/**
 * @author Su LingYun 角色接口
 * 
 */
public interface IRole {

	/**
	 * 获取角色账号
	 * 
	 * @return
	 */
	String getAccount();

	/**
	 * @return 获取角色id
	 */
	String getRoleId();

	/**
	 * @return 获取等级
	 */
	int getLevel();

	/**
	 * 获取名字
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 获得金钱，扣钱则传入负数
	 * 
	 * @param money
	 * @throws NotEnoughMoneyException
	 */
	void winJinbi(int money) throws NotEnoughMoneyException;

	/**
	 * 获取事件接口
	 * 
	 * @return
	 */
	IEventControler getEventControler();

	/**
	 * 当前有多少金币
	 * 
	 * @return
	 */
	long getJinbi();

	/**
	 * 当前有多少完成成就
	 * 
	 * @return
	 */
	int getCompletedAchieve();

	/**
	 * 累计获取了多少金币
	 * 
	 * @return
	 */
	long getJinbiHistory();

	boolean isOnline();

	/**
	 * 异步持久化保存，要求实现者使用数据库线程来执行IO操作，调用线程不阻塞
	 */
	void saveAsyn();

	/**
	 * 获取当前声望值
	 * 
	 * @return
	 */
	int getPrestige();

	/**
	 * 增加声望
	 * 
	 * @param prestige
	 */
	void winPrestige(int prestige);

	void notifyOffline();

	/**
	 * 增加元宝
	 * 
	 * @param yuanbao
	 * @param bind
	 *            是否绑定
	 * @throws NotEnoughYuanBaoException
	 */
	void winYuanbao(int yuanbao, boolean bind) throws NotEnoughYuanBaoException;

	/**
	 * 手动升级
	 * 
	 * @throws NoteException
	 */
	@Deprecated
	void levelUp() throws NoteException;

	/**
	 * 计算元宝总数
	 * 
	 * @return
	 */
	public int getTotalYuanbao();

	void afterEnterGame();

	/**
	 * 计算总战力
	 * 
	 * @return
	 */
	int calculateBattlePower();

	/**
	 * 是否兑换过指定批次的CDK
	 * 
	 * @param category
	 * @return
	 */
	boolean containsCDKGroup(String category);

	/**
	 * 对象销毁，清理各种相关资源
	 */
	void destory();

	/**
	 * 设置在线状态
	 * 
	 * @param online
	 */
	void setOnline(boolean online);

	/**
	 * 获取GM查看视图
	 * 
	 * @return
	 */
	RoleViewForGM getRoleViewForGM();

	/**
	 * 获取视图数据
	 * 
	 * @return
	 */
	RoleView getView();

	/**
	 * 获取武将装备数据
	 * 
	 * @return
	 */
	HeroEquipView[] getHeroEquipView();

	/** 领取俸禄 */
	boolean addSalary() throws NotEnoughYuanBaoException;

	/**
	 * @return 官阶id
	 */
	int getOfficalRankId();

	/**
	 * @return 官阶名称
	 */
	int getOfficalRankName();

	/**
	 * 重命名
	 * 
	 * @param name
	 */
	void rename(String name);

	ICopyControler getCopyControler();

	IItemControler getItemControler();

	IFormationControler getFormationControler();

	IHeroControler getHeroControler();

	IMailControler getMailControler();

	IChatControler getChatControler();

	IFactionControler getFactionControler();

	ISnsControler getSnsController();

	IVipController getVipController();

	ISignController getSignController();

	IRewardControler getRewardControler();

	INotifyControler getNotifyControler();

	IItemChipControler getItemChipControler();

	IHeroMarketControler getHeroMarketControler();

	ITraderControler getTraderControler();

	ITaskControler getTaskControler();

	IBuyJInbiControler getBuyJInbiControler();

	IOnlineAwardControler getOnlineAwardControler();

	IArenaRankControler getArenaRankControler();

	IRankListControler getRankListControler();

	IMFBIControler getMFBIControler();

	ILadderControler getLadderControler();

	ICollectHeroSoulController getCollectHeroSoulControler();

	IHeroAdmireControler getHeroAdmireControler();

	IAttackCastleController getAttackCastleController();

	IAuctionHouseController getAuctionHouseController();

	IFundControler getFundControler();

	ILevelRewardControler getLevelRewardControler();

	IPowerRewardControler getPowerRewardControler();

	IFirstJiaControler getFirstJiaControler();

	IDayLoginControler getDayLoginControler();

	IDayforverLoginControler getDayforverLoginControler();

	ISendJunLingControler getSendJunLingControler();

	IHaoqingbaoController getHaoqingbaoController();

	IFriendsRecallController getFriendsRecallController();

	/**
	 * 武将觉醒控制
	 * 
	 * @return 
	 */
	IHeroAwakenController getHeroAwakenController();

	/**
	 * 公会战控制器
	 * 
	 * @return 
	 */
	IFactionBattleController getFactionBattleController();

	/**
	 * 南华幻境
	 * 
	 * @return 
	 */
	IDreamlandController getDreamlandController();
	
	IApiController getApiController();

	IShootControler getShootControler();

	ITournamentController getTournamentController();

	IAchieveControler getAchieveControler();

	
	IMakeWineControler getMakeWineControler();

	IOpenServerActiveControler getOpenServerActiveControler();

	/**
	 * 货币变更，这里的货币是广义概念，既可以是金币元宝，也可以是某些特定道具
	 * 
	 * @param change
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 */
	void reduceCurrency(Money change) throws NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 获取重命名次数
	 * 
	 * @return
	 */
	int getRenameCount();

	/**
	 * 获取行动力
	 * 
	 * @return
	 */
	int getVit();

	/**
	 * 获取行动力 当天购买次数
	 * 
	 * @return
	 */
	int getVitNum();

	/**
	 * 是否机器人
	 * 
	 * @return
	 */
	boolean isRobot();

	/**
	 * 获取武将技能点信息
	 * 
	 * @return
	 */
	HeroSkillPointView getHeroSkillPointView();

	/**
	 * 修改行动力
	 * 
	 * @param value
	 * @throws NoteException
	 */
	void modifyVit(int value) throws NoteException;

	/**
	 * 获取其他人查看时候提供的数据
	 * 
	 * @return
	 */
	RoleViewForOtherPlayer getViewForOtherPlayer();

	/**
	 * 信息最多20条，超过20条则最新的替换最老的，信息最多保留7天
	 */
	void addFriendApplyingHistory(FriendApplyingHistory historyRecord);

	/**
	 * 获取缓存战力值，不重新计算
	 * 
	 * @return
	 */
	int getCachePower();

	/**
	 * 购买武将技能点
	 * 
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	void buyHeroSkillPoint() throws NotEnoughYuanBaoException, NoteException;

	/**
	 * 设置头像
	 * 
	 * @param img
	 */
	void setHeadImage(String img);

	/**
	 * 获取头像
	 * 
	 * @return
	 */
	String getHeadImage();

	/**
	 * 新增一个扩展头像
	 */
	void addExtHeadImage(String img);

	 /**
	 * 获取额外获得的玩家头像
	 * @return
	 */
	String[] getExtHeadImage();

	 /**
	 * 设置边框
	 * 
	 * @param border
	 */
	void setHeadBorder(String border);

	/**
	 * 获取边框
	 * 
	 * @return
	 */
	String getHeadBorder();

	/**
	 * 新增一个边框
	 */
	void addExtHeadBorder(String border);

	 /**
	 * 获取额外获得的边框集合
	 * @return
	 */
	String[] getExtHeadBorder();

	/**
	 * 更新红点提示
	 * 
	 * @param includeNoTimer
	 *            是否包含非定时通知红点
	 */
	void refreshRedPoint(boolean includeNoTimer);

	/**
	 * 增加兑换码使用记录
	 * 
	 * @param category
	 * @param cdkeyCode
	 */
	void addCDKRecord(String category, String cdkeyCode);

	/**
	 * 设置性别
	 * 
	 * @param sex
	 */
	void setSex(int sex);

	/**
	 * 查询性别
	 * 
	 * @param sex
	 */
	int getSex();

	IActivityControler getActivityControler();

	ILotteryControler getLotteryControler();

	IShareControler getShareControler();

	/**
	 * 获得VIP等级
	 * 
	 * @return
	 */
	int getVipLevel();

	IShopControler getShopControler();

	/**
	 * 完成引导步骤
	 * 
	 * @param guideId
	 */
	void completeGuide(int guideId);

	ITimeBattleControler getTimeBattleControler();

	/**
	 * 得到竞技币
	 */
	void winChallegeMoney(int num);

	/**
	 * 增加拍卖币
	 * 
	 * @param num
	 *            为正表示增加, 为负表示减少
	 * */
	void addAuctionCoin(int num) throws NoteException;

	/**
	 * 获取上次下线时间
	 * 
	 * @return
	 */
	Date getLogoutTime();

	IWorshipRankControler getWorshipRankControler();

	/**
	 * 获取最后一次元宝改名时间
	 * 
	 * @return
	 */
	Date getLastRenameTime();

	/**
	 * 重置元宝改名时间
	 */
	void resetLastRenameTime();

	/**
	 * 保存战力
	 * 
	 * @param combatPower
	 */
	void setCombatPower(int combatPower);

	/**
	 * 进游戏前执行逻辑
	 * 
	 */
	void beforeEnterGame();

	/**
	 * 保存掠夺的次数
	 * 
	 * @param chipRobStat
	 */
	void setChipRobStat(int chipRobStat);

	/**
	 * 查询掠夺次数
	 * 
	 * @return
	 */
	int getChipRobStat();

	/**
	 * 获取登录时间
	 * 
	 * @return
	 */
	Date getLoginTime();

	/**
	 * 获取帐号所属渠道
	 * 
	 * @return
	 */
	int getAccountChannel();

	/**
	 * 指定公告设置为已读
	 * 
	 * @param id
	 */
	boolean readAnnounce(int id);

	IMakeVipControler getMakeVipControler();

	/**
	 * 获取累计充值活动控制器
	 * 
	 * @return
	 */
	ISumChargeActivityControler getSumChargeActivityControler();

	/**
	 * 获取累计消费活动控制器
	 * 
	 * @return
	 */
	ISumConsumeActivityControler getSumConsumeActivityControler();

	/**
	 * 获取用户IP地址
	 * 
	 * @return
	 */
	String getRemoteIp();

	/**
	 * 获取角色创建时间
	 * 
	 * @return
	 */
	Date getCreateTime();

	/**
	 * 设置退出公会时间
	 * 
	 * @param date
	 */
	void setQuitFactionTime(Date date);

	/**
	 * 获取邀请好友
	 * 
	 * @return
	 */
	IInviteActivityControler getInviteActivityControler();

	/**
	 * 获取客户端设备号
	 * 
	 * @return
	 */
	String getDeviceId();

	/**
	 * 获取当前登录所在的渠道号
	 * */
	int getCurrentChannel();

	/**
	 * 添加邀请记录
	 * 
	 * @param roleInviteLog
	 */
	void addInviteLog(RoleInviteLog roleInviteLog);

	/**
	 * 刷新军令，计算恢复数量
	 */
	void refreshJunLing();

	/**
	 * 回收为系统帐号
	 */
	void setAccount2System();

	/**
	 * 获取秒杀
	 * 
	 * @return
	 */
	ISeckillControler getSeckillControler();

	/**
	 * 获取日累计充值
	 * 
	 * @return
	 */
	IDayChargeControler getDayChargeControler();

	/**
	 * 获取日累计消费
	 * 
	 * @return
	 */
	IDayConsumeControler getDayConsumeControler();

	/**
	 * 帐号被重置时触发
	 */
	void onDeleted();

	/**
	 * 生成游戏内公告列表
	 * 
	 * @return
	 */
	ActivityAnnounceView[] generateAnnounceViewList();

	/**
	 * 获取big日累计充值
	 * 
	 * @return
	 */
	IBigDayChargeControler getBigDayChargeControler();

	/**
	 * 获取big日累计消费
	 * 
	 * @return
	 */
	IBigDayConsumeControler getBigDayConsumeControler();

	/**
	 * 获取Big累计充值活动控制器
	 * 
	 * @return
	 */
	IBigSumChargeActivityControler getBigSumChargeActivityControler();

	/**
	 * 获取Big累计消费活动控制器
	 * 
	 * @return
	 */
	IBigSumConsumeActivityControler getBigSumConsumeActivityControler();

	/**
	 * 幸运大转盘
	 * */
	IFortuneWheelControler getFortuneWheelControler();

	/**
	 * 是否完成过指定引导步骤
	 * 
	 * 
	 * @param guideId
	 * @return
	 */
	boolean isCompleteGuide(int guideId);

	/**
	 * 更新firstInTime
	 * */
	void updateFirstInTime(long firstInTime);

	/**
	 * 获取firstInTime
	 * */
	long getFirstInTime();

	/**
	 * 获取福袋控制类
	 * 
	 * @return
	 */
	ILuckyBagControler getLuckyBagControler();

	/**
	 * 禁言超时时间
	 * 
	 * @return
	 */
	Date getSilenceExpire();

	/**
	 * 设置禁言超时时间
	 */
	void setSilenceExpire(Date date);

	/**
	 * 获取聚宝盆控制类
	 * 
	 * @return
	 */
	ICornucopiaControler getCornucopiaControler();

	/**
	 * 兑换控制类
	 * 
	 * @return
	 */
	IExchangeActivityControler getExchangeItemControler();

	/**
	 * 伙伴系统控制类
	 * 
	 * @return
	 */
	public IPartnerControler getPartnerControler();

	/**
	 * 获取好友/仇人/黑名单列表
	 */
	Set<RoleSns> getSns();

	/**
	 * 寻宝控制类
	 * 
	 * @return
	 */
	ITreasureControler getTreasureControler();

	/**
	 * 等级福利
	 * */
	// ILevelWealControler getLevelWealControler();

	/**
	 * 获取远端的IP和端口信息
	 * 
	 * @return
	 */
	String getRemoteAddress();

	/**
	 * 是否有双倍威望
	 * 
	 * @return
	 */
	boolean isDoubleExp();

	/**
	 * 是否有双倍掉落
	 * 
	 * @return
	 */
	boolean isDoubleItem();

	/**
	 * 获取双倍卡剩余时间 0经验 1掉落
	 * 
	 * @return
	 * @throws NoteException
	 */
	IntIntPair[] getDoubleCardTime() throws NoteException;

	/**
	 * 获取世界boss控制起
	 * 
	 * @return
	 */
	IWorldBossControler getWorldBossControler();

	/**
	 * 获取公会数据
	 * 
	 * @return
	 */
	RoleFaction getRoleFaction();

	/**
	 * 获取世界BOSS数据
	 * 
	 * @return
	 */
	RoleWorldBoss getRoleWorldBoss();

	/**
	 * 获取铁匠铺兑换系统控制器
	 * 
	 */
	ISmithyExchangeController getSmithyExchangeController();

	/**
	 * 获取超级充值控制器
	 * 
	 */
	ISuperChargeController getSuperChargeControlle();

	/**
	  * 获取超级转盘控制器
	  * 
	  */
	ISuperRaffleController getSuperRaffleControlle();

	/**
	 * 老友召回的回归时间
	 * 
	 * @return
	 */
	long friendsRecallTime();

	/**
	 * 获取创建角色时所在的服务器ID，框架使用，不可删除
	 * 
	 * @return
	 */
	int getServerId();

	/**
	 * 获取改名需要的元宝数
	 * 
	 * @return
	 */
	int getRenameYuanbao();

	/**
	 * 获取菜单打开记录
	 * 
	 * @return
	 */
	RoleOpenedMenu getRoleOpenedMenu();

	/**
	 * 获取彩蛋控制器
	 * @return
	 */
	IColorfullEggController getColorfullEggController();

	ResourceBackControler getResourceBackControler();

	/**
	 * 更新矿难红点
	 * @param bool
	 */
	void setAccidentRedPoint(boolean bool);

	 /**
	 * 获取战力
	 * @return
	 */
	int getCombatPower();

	
	RoleWeixinShare getRoleWeixinShare();

	/** 微信分享 */
	RoleWeixinShare addRoleWeixinShare(int num);

	/**
	 * 设置机器人类型
	 * @param type
	 */
	void setRobotType(int type);

	/**
	 * 获取跨服竞技场控制器
	 * 
	 * @return
	 */
	CrossArenaControler getCrossArenaControler();
	
	FootballControler getFootballControler();
	
	IArtifactControler getArtifactControler();
}
