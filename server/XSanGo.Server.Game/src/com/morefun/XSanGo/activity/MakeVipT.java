package com.morefun.XSanGo.activity;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/我要做VIP.xls", sheetName = "开放与奖励", beginRow = 2)
public class MakeVipT {
	@ExcelColumn(index = 0)
	public int isOpen;

	@ExcelColumn(index = 1, dataType = DataFormat.OnlyTime)
	public Date beginTime;

	@ExcelColumn(index = 2, dataType = DataFormat.OnlyTime)
	public Date endTime;

	@ExcelColumn(index = 3)
	public int rightTimes;

	@ExcelColumn(index = 4)
	public int bonusTimes;// 答对次数达到时的奖励倍数
	
	@ExcelColumn(index = 5)
	public int level;
	
	@ExcelColumn(index = 6)
	public int subjectNum;

}
