/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 大富温 常量
 */
@ExcelTable(fileName = "script/活动和礼包/大富翁活动脚本.xls", sheetName = "设定集", beginRow = 2)
public class LotteryCommParaT {

	// 优先抽取【棋盘道具列表】中LotteryType=1的道具的数量
	@ExcelColumn(index = 0)
	public int lotteryNum;

	// 投掷一次获得积分
	@ExcelColumn(index = 1)
	public int score1;

	// 消耗每元宝获得的积分
	@ExcelColumn(index = 2)
	public int score2;

	// 摇骰每1点获得几任性值
	@ExcelColumn(index = 3)
	public int score3;

	// 消耗1元宝获得的任性值
	@ExcelColumn(index = 4)
	public int score4;

	// 任性值 达到的上限
	@ExcelColumn(index = 5)
	public int luckyNum;

	// 每摇骰多少次，自动获得1颗遥控骰子
	@ExcelColumn(index = 6)
	public int lotteryTeleoperationNum;

	// 神秘商店道具数量
	@ExcelColumn(index = 7)
	public int matrialNum;

	// 描述
	@ExcelColumn(index = 8)
	public String text1;

	// 开始时间
	@ExcelColumn(index = 9)
	public String startTime;

	// 结束时间
	@ExcelColumn(index = 10)
	public String endTime;

	// 神秘商店刷新(小时)
	@ExcelColumn(index = 11)
	public int shopReset;

	// 投掷骰子等级限制
	@ExcelColumn(index = 12)
	public int minLevel;
}
