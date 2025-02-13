package com.morefun.XSanGo.template;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/比武大会.xls", beginRow = 2, sheetName = "跨服战参数")
public class CrossConfT {

	@ExcelColumn(index = 0, dataType = DataFormat.DateTime)
	public Date zigeBeginDate;

	@ExcelColumn(index = 1, dataType = DataFormat.DateTime)
	public Date zigeEndDate;

	@ExcelColumn(index = 3)
	public int initScore;

	/** 失败加分 */
	@ExcelColumn(index = 7)
	public int loseScore;

	/** 淘汰赛人数 */
	@ExcelColumn(index = 11)
	public int outPeople;

	/** 每日淘汰赛开打时间 */
	@ExcelColumn(index = 12, dataType = DataFormat.OnlyTime)
	public Date outTime;

	/** 淘汰赛对战间隔(分钟) */
	@ExcelColumn(index = 13)
	public int outInterval;

	@ExcelColumn(index = 16, dataType = DataFormat.DateTime)
	public Date beginApplyDate;

	@ExcelColumn(index = 17, dataType = DataFormat.DateTime)
	public Date endApplyDate;

	/** 第几届比武大会 */
	@ExcelColumn(index = 18)
	public int periodNum;

	@ExcelColumn(index = 19, dataType = DataFormat.DateTime)
	public Date begin32Date;

	@ExcelColumn(index = 20, dataType = DataFormat.DateTime)
	public Date end32Date;

	@ExcelColumn(index = 21, dataType = DataFormat.DateTime)
	public Date begin16Date;

	@ExcelColumn(index = 22, dataType = DataFormat.DateTime)
	public Date end16Date;

	@ExcelColumn(index = 23, dataType = DataFormat.DateTime)
	public Date begin8Date;

	@ExcelColumn(index = 24, dataType = DataFormat.DateTime)
	public Date end8Date;

	@ExcelColumn(index = 25, dataType = DataFormat.DateTime)
	public Date begin4Date;

	@ExcelColumn(index = 26, dataType = DataFormat.DateTime)
	public Date end4Date;

	@ExcelColumn(index = 27, dataType = DataFormat.DateTime)
	public Date begin2Date;

	@ExcelColumn(index = 28, dataType = DataFormat.DateTime)
	public Date end2Date;

	/** 资格赛结束后延迟多少小时生成32强对阵表 */
	@ExcelColumn(index = 33)
	public int delayHour;
}
