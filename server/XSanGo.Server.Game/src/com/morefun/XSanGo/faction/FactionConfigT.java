package com.morefun.XSanGo.faction;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "基本参数")
public class FactionConfigT {
	@ExcelColumn(index = 0)
	public int factionNum;// 公会列表数量

	@ExcelColumn(index = 1)
	public int applyNum;// 申请列表数量

	@ExcelColumn(index = 2)
	public int createNum;// 创建公会花费 元宝

	@ExcelColumn(index = 3)
	public int historyNum;// 历史记录数量

	@ExcelColumn(index = 4)
	public int quitJoinMinute;// 退出加入间隔 分钟

	@ExcelColumn(index = 5)
	public int quitCreateMinute;// 退出创建间隔 分钟

	@ExcelColumn(index = 6)
	public String shopRefreshDate;// 时:分

	@ExcelColumn(index = 7)
	public int shopNum;// 商城物品数量

	/** 开启公会战需要的公会等级 */
	@ExcelColumn(index = 8)
	public int openGvgLevel;

	/** 公会战开战星期,分割 */
	@ExcelColumn(index = 9)
	public String openWeekDay;

	/** 公会战开战时间点 */
	@ExcelColumn(index = 10)
	public String beginTime;

	/** 开战前多少分钟开始报名 */
	@ExcelColumn(index = 11)
	public int beforeMinute;

	/** 战斗等待秒 */
	@ExcelColumn(index = 13)
	public int waitScond;

	/** 死亡等待秒 */
	@ExcelColumn(index = 14)
	public int deathWait;

	/** 公会战持续时间分钟 */
	@ExcelColumn(index = 15)
	public int gvgMinute;

	/** 战胜获得荣誉 */
	@ExcelColumn(index = 16)
	public int winHonor;

	/** 复活元宝 */
	@ExcelColumn(index = 19)
	public int reviveMoney;

	/** 列表人数区间,分割 */
	@ExcelColumn(index = 20)
	public String randomPeople;

	/** 个人排行榜条数 */
	@ExcelColumn(index = 21)
	public int peopleRankSize;

	/** 公会排行榜条数 */
	@ExcelColumn(index = 22)
	public int factionRankSize;

	/** 活动开始时间 */
	@ExcelColumn(index = 25, dataType = DataFormat.DateTime)
	public Date activityStartDate;

	/** 活动结束时间 */
	@ExcelColumn(index = 26, dataType = DataFormat.DateTime)
	public Date activityEndDate;
	
	/** 打败对方扣除荣誉 */
	@ExcelColumn(index = 27)
	public int deductHonor;
	
	/** 热血公会战活动开始时间 */
	@ExcelColumn(index = 28, dataType = DataFormat.DateTime)
	public Date warmStartDate;

	/** 热血公会战活动结束时间 */
	@ExcelColumn(index = 29, dataType = DataFormat.DateTime)
	public Date warmEndDate;
	
	/** 每日可发送邮件次数*/
	@ExcelColumn(index = 30)
	public int sendMailTimes;
	
	/** 仓库自动分配时间点 */
	@ExcelColumn(index = 31)
	public String autoAllotTime;
}
