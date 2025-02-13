package com.morefun.XSanGo.activity;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/蹴鞠配置表.xls", sheetName = "赛事参数", beginRow = 2)
public class FootballMatchT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String date;

	@ExcelColumn(index = 2)
	public String time;

	@ExcelColumn(index = 3)
	public String leftCountry;

	@ExcelColumn(index = 4)
	public String leftCountryFlag;

	@ExcelColumn(index = 5)
	public String rightCountry;

	@ExcelColumn(index = 6)
	public String rightCountryFlag;

	@ExcelColumn(index = 7)
	public double leftOdds;

	@ExcelColumn(index = 8)
	public double rightOdds;

	/** 平局倍率 */
	@ExcelColumn(index = 9)
	public double drawOdds;

	@ExcelColumn(index = 10)
	public int minBet;

	@ExcelColumn(index = 11)
	public int maxBet;

	@ExcelColumn(index = 12, dataType = DataFormat.DateTime)
	public Date beginBetDate;

	@ExcelColumn(index = 13, dataType = DataFormat.DateTime)
	public Date endBetDate;

	@ExcelColumn(index = 14)
	public int gameType;

	@ExcelColumn(index = 15)
	public int isOver;

	@ExcelColumn(index = 16)
	public String score;

	/** 是否返利 */
	@ExcelColumn(index = 17)
	public int isRebate;

	/** 返利比例 */
	@ExcelColumn(index = 18)
	public int rebateScale;

	/** 返利押注条件 */
	@ExcelColumn(index = 19)
	public int rebateBetNum;

	@ExcelColumn(index = 20)
	public int leftCountryId;

	@ExcelColumn(index = 21)
	public int rightCountryId;
}
