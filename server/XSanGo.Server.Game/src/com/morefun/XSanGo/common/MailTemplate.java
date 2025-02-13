/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: MailTemplate
 * 功能描述：
 * 文件名：MailTemplate.java
 **************************************************
 */
package com.morefun.XSanGo.common;

/**
 * 邮件模版
 * 
 * @author weiyi.zhao
 * @since 2016-5-27
 * @version 1.0
 */
public enum MailTemplate {
	/** 默认,站位，不用 */
	DEFAULT_ID(0),
	/** 每日排名 奖励 邮件 */
	ARENARANK_DAILY(1),
	/** 历史最高纪录 奖励 */
	ARENARANK_HISTORY(2),
	/** 防守嘲讽模式 胜利 */
	ARENARANK_SNEER_DEFEND(3),
	/** 挑战嘲讽模式 胜利 */
	ARENARANK_SNEER_WIN(4),
	/** 当日首胜 */
	ARENARANK_FIRST(5),
	/** 防守嘲讽模式 失败 */
	ARENARANK_SNEER_DEFEND_FAIL(6),
	/** 首充奖励 */
	First_Charge_Monthly(7),
	/** 群雄争霸赛季奖励 */
	LadderSeason(8),
	/** 团队副本开启 */
	FactionCopyOpen(9),
	/** 团队副本通关 */
	FactionCopyPassAll(10),
	/** 限时通关副本奖励 */
	FactionCopyPassInTime(11),
	/** 物品竞拍成功 */
	AuctionBuySuccess(12),
	/** 一口价购买 */
	AuctionBuyByFixedPrice(13),
	/** 物品竞拍失败 */
	AuctionBuyFail(14),
	/** 拍卖成功 */
	AuctionSaleSuccess(15),
	/** 物品下架 */
	AuctionItemDown(16),
	/** 拍卖未成交 */
	AuctionTimeout(17),
	/** 关卡占领奖励 */
	ServerCopyReward(18),
	/** 自己竞价之后重新一口价购买 */
	AuctionBuySelf(19),
	/** 充值获取拍卖币 */
	ReceiveAuctionMoneyAfterCharge(20),
	/** 挑战嘲讽模式失败 */
	SNEER_FAIL(21),
	/** 公会战排名奖励 */
	GVG_AWARD(22),
	/** 副本热身奖励 */
	CopyWarmup(23),
	/** 公会战排名活动 */
	GVG_ACTIVITY_AWARD(24),
	/** 月卡奖励 */
	MonthCard(25),
	/** 限时武将活动 */
	LimitHeroActivity(26),
	/** 热血公会战活动 */
	WarmGvgActivity(27),
	/** 武将传承 */
	HeroInherit(28),
	/** 世界BOSS排行榜 */
	WorldBossRank(29),
	/** 百步穿杨积分排行奖 */
	MarksManActivity(30),
	/** 世界BOSS活动 */
	WorldBossActivity(31),
	/** 世界BOSS购买鼓舞返还 */
	WorldBossInspireReturn(32),
	/** 老友回归，邀请奖励 */
	FriendsRecallInvitation(33),
	/** 老友回归，回归奖励 */
	FriendsRecallRecalled(34),
	/** 合服退回拍卖品 */
	SendBackAuctionItem(35),
	/** 合服退回拍卖币 */
	SendBackAcutionIcon(36),
	/** 超级转转转 */
	SuperRotation(37),
	/** 世界BOSS参与奖 */
	WorldBossJoin(38),
	/** 世界BOSS尾刀 */
	WorldBossTail(39),
	/** 比武大会冠军 */
	TournamentChampion(40),
	/** 比武大会淘汰赛奖励 */
	TournamentKnockOut(41),
	/** 比武大会积分奖励 */
	TournamentScore(42),
	/** 比武大会竞猜成功 */
	TournamentBetSuccess(43),
	/** 比武大会竞猜失败 */
	TournamentBetFailure(44),
	/** 首次占领关卡奖励 */
	CopyFirstOccupy(45),
	/** 寻宝活动 */
	TreasureActivityHero(46),
	/** 公会分配物品 */
	FactionAllotItem(47),
	/** 公会分配自己物品 */
	FactionAllotSelfItem(48),
	/** 大富温积分排行奖 */
	LotteryActivity(49),
	/** 大富温积分排行奖 */
	LotteryCycleAward(50),
	/** 比武大会占位 */
	x6(51),
	/** 援救寻宝好友的奖励 */
	RescueFriendAward(52),
	/** 援救寻宝自己的奖励 */
	RescueSelfAward(53),
	/** 分享奖励 */
	ShareAward(54),
	/** 比武大会每日首胜 */
	TreasureWinNum(55),
	/** 世界BOSS托管奖 */
	WorldBossTrust(56),
	/** 百步穿杨积分排名奖励 */
	MakeWineScoreRank(57),
	/** 仓库自动分配 */
	FactionAutoAllot(58),
	/** 公会战排名奖励 */
	FactionBattleRank(59),
	/** 公会战个人排名奖励 */
	FactionBattlePersonalRank(60), FootballWin(61), FootballFail(62);

	/**
	 * 返回枚举值
	 * 
	 * @return
	 */
	public int value() {
		return _value;
	}

	/**
	 * 构造函数
	 * 
	 * @param value
	 */
	private MailTemplate(int value) {
		this._value = value;
	}

	/** 枚举值 */
	private final int _value;
}
