package com.morefun.XSanGo.task;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/任务脚本/任务脚本.xls", beginRow = 2)
public class TaskTemplT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 2)
	public int type;

	@ExcelColumn(index = 3)
	public String title;

	@ExcelColumn(index = 4)
	public String content;

	@ExcelColumn(index = 5)
	public int through;

	@ExcelColumn(index = 6)
	public int icon;

	@ExcelColumn(index = 7)
	public int exp;

	@ExcelColumn(index = 8)
	public int gold;

	@ExcelColumn(index = 9)
	public String item;

	@ExcelColumn(index = 10)
	public int itemNum;

	@ExcelColumn(index = 11)
	public String target;

	@ExcelColumn(index = 12)
	public String demand;

	@ExcelColumn(index = 13)
	public int IsGo;

	@ExcelColumn(index = 14)
	public int maxNum;

	@ExcelColumn(index = 15)
	public String starTime;

	@ExcelColumn(index = 16)
	public String endTime;

	@ExcelColumn(index = 17)
	public String starDate;

	@ExcelColumn(index = 18)
	public String endDate;
	
	@ExcelColumn(index = 21)
	public int minLevel;//最小等级
	
	@ExcelColumn(index = 23)
	public int actPoint;//活跃点
}
