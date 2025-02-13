/**
 * 
 */
package com.morefun.XSanGo.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.XSanGo.Protocol.IntString;
import com.morefun.XSanGo.role.GameParamT;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 逻辑参数配置管理类
 * 
 * @author sulingyun
 * 
 */
public class XsgGameParamManager {
	private static XsgGameParamManager instance = new XsgGameParamManager();

	public static XsgGameParamManager getInstance() {
		return instance;
	}

	private Map<String, String> gameParamMap;

	private XsgGameParamManager() {
		this.gameParamMap = new HashMap<String, String>();
		for (GameParamT param : ExcelParser.parse(GameParamT.class)) {
			this.gameParamMap.put(param.id, param.value);
		}
	}

	/**
	 * 魅力最大值
	 * 
	 * @return
	 */
	public int getMaxMarketCharm() {
		return NumberUtil.parseInt(this.getParamValue("Market.MaxCharm"));
	}

	/**
	 * 获得七日任务开启状态
	 * 
	 * @return
	 */
	public int getSevenTaskOpenStatus() {
		return NumberUtil.parseInt(this.getParamValue("SevenDaysOpen"));
	}

	private String getParamValue(String key) {
		return this.gameParamMap.get(key);
	}

	public int getMarketCharmByJinbi() {
		return NumberUtil.parseInt(this.getParamValue("Market.CharmByJinbi"));
	}

	public int getMarketCharmByYuanbao() {
		return NumberUtil.parseInt(this.getParamValue("Market.CharmByYuanbao"));
	}

	/**
	 * 行动力回复的间隔时间，单位：秒
	 * 
	 * @return
	 */
	public int getVitResetTime() {
		return NumberUtil.parseInt(this.getParamValue("Role.VitResetTime"));
	}

	/**
	 * 行动力回复的值
	 * 
	 * @return
	 */
	public int getVitResetValue() {
		return NumberUtil.parseInt(this.getParamValue("Role.VitResetValue"));
	}

	/**
	 * 掠夺消耗的行动力
	 * 
	 * @return
	 */
	public int getRobVit() {
		return NumberUtil.parseInt(this.getParamValue("ItemChip.RobVit"));
	}

	/**
	 * 消耗挑战令
	 * 
	 * @return
	 */
	public int getArenaConsume() {
		return NumberUtil.parseInt(this.getParamValue("Arena.Consume"));
	}

	/**
	 * 竞技场排行榜显示数量
	 * 
	 * @return
	 */
	public int getArenaReportNum() {
		return NumberUtil.parseInt(this.getParamValue("Arena.reportNum"));
	}

	/**
	 * 挑战令购买 间隔时间
	 * 
	 * @return
	 */
	public int getArenaHour() {
		return NumberUtil.parseInt(this.getParamValue("Arena.Hour"));
	}

	/**
	 * 竞技场商品显示数量
	 * 
	 * @return
	 */
	public int[] getArenaMallNum() {
		int[] resMallNum = new int[2];

		resMallNum[0] = NumberUtil.parseInt(this.getParamValue("Arena.Convert").split(",")[0]);
		resMallNum[1] = NumberUtil.parseInt(this.getParamValue("Arena.Convert").split(",")[1]);

		return resMallNum;
	}

	/**
	 * 铁匠铺商品显示数量
	 * 
	 * @return
	 */
	public int[] getSmithyMallNum() {
		int[] resMallNum = new int[2];

		resMallNum[0] = NumberUtil.parseInt(this.getParamValue("Smithy.Convert").split(",")[0]);
		resMallNum[1] = NumberUtil.parseInt(this.getParamValue("Smithy.Convert").split(",")[1]);

		return resMallNum;
	}

	/**
	 * 铁匠铺蓝装商品显示数量
	 * 
	 * @return
	 */
	public int[] getBlueSmithyMallNum() {
		int[] resMallNum = new int[2];

		resMallNum[0] = NumberUtil.parseInt(this.getParamValue("Smithy.Convert.Blue").split(",")[0]);
		resMallNum[1] = NumberUtil.parseInt(this.getParamValue("Smithy.Convert.Blue").split(",")[1]);

		return resMallNum;
	}

	/**
	 * 挑战刷新时间
	 * 
	 * @return
	 */
	public String getArenaInterval() {
		return this.getParamValue("Arena.Interval");
	}

	/**
	 * 每日商城刷新时间
	 * 
	 * @return
	 */
	public String getMall() {
		return this.getParamValue("Arena.Mall");
	}

	/**
	 * 每日铁匠铺紫装商城刷新时间
	 * 
	 * @return
	 */
	public String getSmithyMall() {
		return this.getParamValue("Smithy.Refresh");
	}

	/**
	 * 每日铁匠铺蓝装商城刷新时间
	 * 
	 * @return
	 */
	public String getBlueSmithyMall() {
		return this.getParamValue("Smithy.Refresh.Blue");
	}

	/**
	 * 每日排名结算时间
	 * 
	 * @return
	 */
	public String getRefresh() {
		return this.getParamValue("Arena.Refresh");
	}

	/**
	 * 开启竞技场等级
	 * 
	 * @return
	 */
	public int getArenaLevel() {
		return NumberUtil.parseInt(this.getParamValue("Arena.Level"));
	}

	/**
	 * 竞技场初始排名
	 * 
	 * @return
	 */
	public int getArenaStartRank() {
		return NumberUtil.parseInt(this.getParamValue("Arena.StartRank"));
	}

	/**
	 * 每日赠送挑战令
	 * 
	 * @return
	 */
	public int getArenaToken() {
		return NumberUtil.parseInt(this.getParamValue("Arena.Token"));
	}

	/**
	 * 红包索要消息显示上限
	 * */
	public int getMaxClaimNum() {
		return NumberUtil.parseInt(this.getParamValue("Hongbao.Askfor.MaxMsg"));
	}

	/**
	 * 战报显示数量
	 * 
	 * @return
	 */
	public int getReportNum() {
		return NumberUtil.parseInt(this.getParamValue("Role.ReportNum"));
	}

	public int getMarketOnInTenPrice() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Jinbi"));
	}

	public int getMarketOnInTen10Price() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Jinbi10"));
	}

	public int getMarketOnInHundredPrice() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Yuanbao"));
	}

	public int getMarketOnInHundred10Price() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Yuanbao10"));
	}

	public int getFistJuniorCopy() {
		return NumberUtil.parseInt(this.getParamValue("Copy.First.Junior"));
	}

	public int getFistSeniorCopy() {
		return NumberUtil.parseInt(this.getParamValue("Copy.First.Senior"));
	}

	public int getFistTopCopy() {
		return NumberUtil.parseInt(this.getParamValue("Copy.First.Top"));
	}

	public int getJinbiTraderCallPrice() {
		return NumberUtil.parseInt(this.getParamValue("Trader.Trader.Jinbi"));
	}

	public int getYuanbaoTraderCallPrice() {
		return NumberUtil.parseInt(this.getParamValue("Trader.Trader.Yuanbao"));
	}

	public int getJinbiHeroCallPrice() {
		return NumberUtil.parseInt(this.getParamValue("Trader.Hero.Jinbi"));
	}

	public int getYuanbaoHeroCallPrice() {
		return NumberUtil.parseInt(this.getParamValue("Trader.Hero.Yuanbao"));
	}

	/**
	 * 排行榜 战力排行榜 显示数量
	 * 
	 * @return
	 */
	public int getRankListCombatNum() {
		return NumberUtil.parseInt(this.getParamValue("RankList.Combat"));
	}

	/**
	 * 排行榜 战力排行榜 显示数量
	 * 
	 * @return
	 */
	public int getRankListAchieveNum() {
		return NumberUtil.parseInt(this.getParamValue("RankList.Achieve"));
	}

	/**
	 * 排行榜 大神排行榜 显示数量
	 * 
	 * @return
	 */
	public int getRankListWorshipNum() {
		return NumberUtil.parseInt(this.getParamValue("RankList.Worship"));
	}

	/**
	 * 排行榜 战力排行榜 全部排行
	 * 
	 * @return
	 */
	public int getRankListCombatAll() {
		return NumberUtil.parseInt(this.getParamValue("RankList.CombatAll"));
	}

	/**
	 * 排行榜 成就排行榜 全部排行
	 * 
	 * @return
	 */
	public int getRankListAchieveAll() {
		return NumberUtil.parseInt(this.getParamValue("RankList.AchieveAll"));
	}

	/**
	 * 排行榜 大神排行榜 全部排行
	 * 
	 * @return
	 */
	public int getRankListWorshipAll() {
		return NumberUtil.parseInt(this.getParamValue("RankList.WorshipAll"));
	}

	/**
	 * 排行榜 显示所需等级限制
	 * 
	 * @return
	 */
	public int getRankLevel() {
		return NumberUtil.parseInt(this.getParamValue("RankList.Level"));
	}

	/**
	 * 排行榜 公会排行榜 显示数量
	 * 
	 * @return
	 */
	public int getRankListFactionNum() {
		return NumberUtil.parseInt(this.getParamValue("RankList.FactionNum"));
	}

	/**
	 * 排行榜 公会排行榜 全部排行
	 * 
	 * @return
	 */
	public int getRankListFactionAll() {
		return NumberUtil.parseInt(this.getParamValue("RankList.FactionAll"));
	}

	/**
	 * 金币单抽赠送的美酒数量
	 * 
	 * @return
	 */
	public int getMarketWinForJinbi() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Jinbi.Wine"));
	}

	public String getMarketWineCode() {
		return this.getParamValue("Market.Price.WineCode");
	}

	/**
	 * 金币十连抽赠送美酒数量
	 * 
	 * @return
	 */
	public int getMarketWinForJinbi10() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Jinbi10.Wine"));
	}

	/**
	 * 元宝单抽赠送美酒数量
	 * 
	 * @return
	 */
	public int getMarketWinForYuanbao() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Yuanbao.Wine"));
	}

	/**
	 * 元宝十连抽赠送美酒数量
	 * 
	 * @return
	 */
	public int getMarketWinForYuanbao10() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Yuanbao10.Wine"));
	}

	/**
	 * 获取武将技能点恢复时间间隔
	 * 
	 * @return
	 */
	public int getSkillPointInterval() {
		return NumberUtil.parseInt(this.getParamValue("Skill.Interval"));
	}

	/**
	 * 获取商人固定货物数量
	 * 
	 * @return
	 */
	public int getTraderFixedGoodsCount() {
		String[] array = this.getParamValue("Trader.Trader.Barter").split(",");
		return NumberUtil.parseInt(array[0]);
	}

	/**
	 * 获取商人随机货物数量
	 * 
	 * @return
	 */
	public int getTraderRandomGoodsCount() {
		String[] array = this.getParamValue("Trader.Trader.Barter").split(",");
		return NumberUtil.parseInt(array[1]);
	}

	/**
	 * 获取名将固定货物数量
	 * 
	 * @return
	 */
	public int getHeroTraderFixedGoodsCount() {
		String[] array = this.getParamValue("Trader.Hero.Barter").split(",");
		return NumberUtil.parseInt(array[0]);
	}

	/**
	 * 获取名将随机货物数量
	 * 
	 * @return
	 */
	public int getHeroTraderRandomGoodsCount() {
		String[] array = this.getParamValue("Trader.Hero.Barter").split(",");
		return NumberUtil.parseInt(array[1]);
	}

	/**
	 * 获取结算关卡占领奖励的时间
	 * 
	 * @return
	 */
	public Date getServerCopyRewardTime() {
		return DateUtil.parseDate("HH:mm:ss", this.getParamValue("Stage.Owner.Interval"));
	}

	/**
	 * 获取充值赠送的拍卖币兑换汇率
	 * 
	 * @return 每充1块钱赠送多少拍卖币
	 */
	public int getAuctionExchageRate() {
		return NumberUtil.parseInt(this.getParamValue("Auction.Exchange.ChargeRate"));
	}

	/**
	 * 获取一键扫荡需要VIP
	 * 
	 * @return
	 */
	public int getCopyClearVip() {
		return NumberUtil.parseInt(this.getParamValue("Copy.Clear.VIP"));
	}

	/**
	 * 获取一键扫荡需要玩家等级
	 * 
	 * @return
	 */
	public int getCopyClearLevel() {
		return NumberUtil.parseInt(this.getParamValue("Copy.Clear.Level"));
	}

	/**
	 * 初始军令个数
	 * 
	 * @return
	 */
	public int getDefaultJunLing() {
		return NumberUtil.parseInt(this.getParamValue("Role.Med1.Default"));
	}

	/**
	 * 军令恢复时间间隔，单位：秒
	 * 
	 * @return
	 */
	public int getJunLingRecorvInterval() {
		return NumberUtil.parseInt(this.getParamValue("Role.Med1.Interval"));
	}

	/**
	 * 购买军令次数重置时间
	 * 
	 * @return
	 */
	public Date getJunLingResetTime() {
		return DateUtil.parseDate("HH:mm:ss", this.getParamValue("Role.Med1.Refresh"));
	}

	/**
	 * 获取武将下野保存武将的花费
	 * */
	public int getHeroResetCost() {
		return NumberUtil.parseInt(getParamValue("Hero.Reset.Cost"));
	}

	/**
	 * 获取武将下野返回的经验丹模版ID
	 * */
	public String getHeroResetReturnExpTID() {
		return getParamValue("Hero.Reset.ReturnExp");
	}

	/**
	 * 获取武将下野最低等级要求
	 * */
	public int getHeroResetMinLevel() {
		return NumberUtil.parseInt(getParamValue("Hero.Reset.ReqLevel"));
	}

	/**
	 * 绿将变蓝色的进阶等级
	 * */
	public int getHeroG2BLevel() {
		return NumberUtil.parseInt(getParamValue("Hero.Upgrade.G2BLevel"));
	}

	/**
	 * 蓝将变紫色的进阶等级
	 * */
	public int getHeroB2PLevel() {
		return NumberUtil.parseInt(getParamValue("Hero.Upgrade.B2PLevel"));
	}

	/**
	 * 帐号重置等级，超出无法使用该功能
	 * 
	 * @return
	 */
	public int getAccountResetLevel() {
		return NumberUtil.parseInt(this.getParamValue("Account.ResetLevel"));
	}

	/**
	 * 获取修炼丹物品编号
	 * 
	 * @return
	 */
	public String getXiuLianCode() {
		return this.getParamValue("Hero.Xiulian.ResetCode");
	}

	/**
	 * 购买限时武将获得魅力
	 * 
	 * @return
	 */
	public int getBuyLimitHeroCharm() {
		return NumberUtil.parseInt(this.getParamValue("Market.CharmByHunxia"));
	}

	/**
	 * 购买限时武将获得美酒
	 * 
	 * @return
	 */
	public int getBuyLimitHeroWine() {
		return NumberUtil.parseInt(this.getParamValue("Market.Price.Hunxia.Wine"));
	}

	/**
	 * 获取送军令等级限制
	 * */
	public int getSendJunlingLevelLimit() {
		return NumberUtil.parseInt(this.getParamValue("Role.Med1.Supply.StartLevel"));
	}

	/**
	 * 竞技场嘲讽模式等级限制
	 * */
	public int getArenaTauntLevel() {
		return NumberUtil.parseInt(this.getParamValue("Arena.Taunt.Level"));
	}

	/**
	 * 累计登录活动等级限制
	 * */
	public int getDayLoginLevelLimit() {
		return NumberUtil.parseInt(this.getParamValue("Activity.Login.LvRequest"));
	}

	/**
	 * 永久累计登录活动等级限制
	 * */
	public int getDayforverLoginLevelLimit() {
		return NumberUtil.parseInt(this.getParamValue("Activity.ForverLogin.LvRequest"));
	}

	/**
	 * 返回升星石模版ID
	 * */
	public String getStarUpReturnTempID() {
		return getParamValue("Equip.Cube.ReturnExp");
	}

	/**
	 * 武将传承消耗的道具模版ID
	 * */
	public String getHeroInheritConsumeId() {
		return getParamValue("Hero.Inherit.Property");
	}

	/**
	 * 武将传承消耗道具的数量
	 * */
	public int getHeroInheritConsumeNum() {
		return NumberUtil.parseInt(getParamValue("Hero.Inherit.Level"));
	}

	/**
	 * 武将传承等级限制
	 * */
	public int getHeroInheritLevel() {
		return NumberUtil.parseInt(getParamValue("Hero.Inherit.ReqLevel"));
	}

	/**
	 * 宝石公告等级
	 * */
	public int getGemNoticeLevel() {
		return NumberUtil.parseInt(getParamValue("Equip.GemCompound.Level"));
	}

	/**
	 * 背包大小上限
	 * 
	 * @return
	 */
	public int getBagItemLimit() {
		return NumberUtil.parseInt(getParamValue("bagItemLimit"));
	}

	/**
	 * 聊天消息缓存数量
	 **/
	public int getChatMsgSave() {
		return NumberUtil.parseInt(getParamValue("chatMsgSave"));
	}

	public int resetAttendantLevelLimit() {
		// TODO Auto-generated method stub Attendant.Spacial.Open
		return NumberUtil.parseInt(getParamValue("Attendant.Spacial.Open"));
	}

	/**
	 * 伙伴重置武将时消耗道具及数量
	 * 
	 * @return
	 */
	public IntString getPartnerPropResetCost() {
		String itemCode = this.getParamValue("Changehuoban").split(",")[0];
		int num = NumberUtil.parseInt(this.getParamValue("Changehuoban").split(",")[1]);
		IntString cost = new IntString(num, itemCode);
		return cost;
	}

	/**
	 * 武将觉醒重置洗炼道具返回比率
	 * 
	 * @return
	 */
	public int getHeroAwakenBaptizeRebate() {
		return NumberUtil.parseInt(getParamValue("xilianchongzhi"));
	}

	/**
	 * 武将觉醒重置洗炼返还的道具
	 * 
	 * @return
	 */
	public String getHeroAwakenItemRebate() {
		return getParamValue("xiliandaoju");
	}

	/**
	 * 南华幻境每日挑战次数
	 * 
	 * @return
	 */
	public int getDreamlandChallengeNum() {
		return NumberUtil.parseInt(getParamValue("nanhuatiaozhancishu"));
	}

	/**
	 * 神器开放等级
	 * 
	 * @return
	 */
	public int getArtifactOpenLevel() {
		return NumberUtil.parseInt(getParamValue("ShenQi.Open"));
	}
}
