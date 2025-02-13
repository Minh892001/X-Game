package com.morefun.XSanGo.superCharge;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/超级充值.xls", beginRow = 2,sheetName = "抽奖次数")
public class RaffleNumT {
	@ExcelColumn(index = 0)
	public int money;

	@ExcelColumn(index = 1)
	public int raffleNum;
	
	@ExcelColumn(index = 2, dataType = DataFormat.DateTime)
	public Date beginTime;

	@ExcelColumn(index = 3, dataType = DataFormat.DateTime)
	public Date endTime;
	
	@ExcelColumn(index = 4)
	public int acRaffleNum;

	@ExcelColumn(index = 5)
	public String content;

	@ExcelColumn(index = 6)
	public String helpIno;
	/**活动期间每抽奖10次的邮箱奖励*/
	@ExcelColumn(index = 7)
	public String reqItem;
}
