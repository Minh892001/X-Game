package com.morefun.XSanGo.makewine;

import java.util.Date;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 材料领取
 * @author zhuzhi.yang
 *
 */
@ExcelTable(fileName = "script/活动和礼包/煮酒论英雄.xls", sheetName = "材料领取", beginRow = 2)
public class MaterialReceiveT {

	@ExcelColumn(index = 0)
	public String setFodderTime;
	public Date receiveTime;
	
	@ExcelColumn(index = 1)
	public String itemID;
	
	@ExcelColumn(index = 2)
	public int num;
	
}
