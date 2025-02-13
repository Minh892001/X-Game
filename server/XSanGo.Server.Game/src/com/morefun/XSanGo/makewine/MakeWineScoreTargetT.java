package com.morefun.XSanGo.makewine;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 酿酒积分目标
 * @author zhuzhi.yang
 *
 */
@ExcelTable(fileName = "script/活动和礼包/煮酒论英雄.xls", sheetName = "酿酒积分目标", beginRow = 2)
public class MakeWineScoreTargetT {

	@ExcelColumn(index = 0)
	public int compoundScoreGoal;
	
	@ExcelColumn(index = 1)
	public String itemID;
	
	public int itemNum;
	
}
