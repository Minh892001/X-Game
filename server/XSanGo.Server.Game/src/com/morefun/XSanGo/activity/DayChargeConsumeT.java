package com.morefun.XSanGo.activity;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 日充值消费模版
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/活动和礼包/充值和消费.xls", beginRow = 2)
public class DayChargeConsumeT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1, dataType = DataFormat.DateTime)
	public Date startDate;

	@ExcelColumn(index = 2, dataType = DataFormat.DateTime)
	public Date endDate;

	@ExcelColumn(index = 3)
	public int isValid;// 是否有效

	@ExcelComponet(index = 4, columnCount = 3, size = 4)
	public DayChargeConsumeItemT[] items;

	// @ExcelColumn(index = 4)
	// public int money1;
	//
	// @ExcelColumn(index = 5)
	// public String itemId1;
	//
	// @ExcelColumn(index = 7)
	// public int money2;
	//
	// @ExcelColumn(index = 8)
	// public String itemId2;
	//
	// @ExcelColumn(index = 10)
	// public int money3;
	//
	// @ExcelColumn(index = 11)
	// public String itemId3;
	//
	// @ExcelColumn(index = 13)
	// public int money4;
	//
	// @ExcelColumn(index = 14)
	// public String itemId4;

}
