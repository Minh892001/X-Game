package com.morefun.XSanGo.sign;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/每日签到脚本.xls", sheetName = "签到抽奖", beginRow = 1)
public class SignRouletteT {

	@ExcelColumn(index = 0)
	public int month;

	@ExcelColumn(index = 1)
	public int day;

	@ExcelColumn(index = 2)
	public int lottery_times;

	@ExcelColumn(index = 3)
	public int gold_need;

	@ExcelColumn(index = 4)
	public String item1;

	@ExcelColumn(index = 5)
	public int num1;

	@ExcelColumn(index = 6)
	public int prob1;

	@ExcelColumn(index = 7)
	public int broadcast1;

	@ExcelColumn(index = 8)
	public String item2;

	@ExcelColumn(index = 9)
	public int num2;

	@ExcelColumn(index = 10)
	public int prob2;

	@ExcelColumn(index = 11)
	public int broadcast2;

	@ExcelColumn(index = 12)
	public String item3;

	@ExcelColumn(index = 13)
	public int num3;

	@ExcelColumn(index = 14)
	public int prob3;

	@ExcelColumn(index = 15)
	public int broadcast3;

	@ExcelColumn(index = 16)
	public String item4;

	@ExcelColumn(index = 17)
	public int num4;

	@ExcelColumn(index = 18)
	public int prob4;

	@ExcelColumn(index = 19)
	public int broadcast4;

	@ExcelColumn(index = 20)
	public String item5;

	@ExcelColumn(index = 21)
	public int num5;

	@ExcelColumn(index = 22)
	public int prob5;

	@ExcelColumn(index = 23)
	public int broadcast5;

	@ExcelColumn(index = 24)
	public String item6;

	@ExcelColumn(index = 25)
	public int num6;

	@ExcelColumn(index = 26)
	public int prob6;

	@ExcelColumn(index = 27)
	public int broadcast6;

	@ExcelColumn(index = 28)
	public String item7;

	@ExcelColumn(index = 29)
	public int num7;

	@ExcelColumn(index = 30)
	public int prob7;

	@ExcelColumn(index = 31)
	public int broadcast7;

	@ExcelColumn(index = 32)
	public String item8;

	@ExcelColumn(index = 33)
	public int num8;

	@ExcelColumn(index = 34)
	public int prob8;

	@ExcelColumn(index = 35)
	public int broadcast8;

	@ExcelColumn(index = 36)
	public String finalTcItem;
}