package com.morefun.XSanGo.makewine;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 积分兑换配置
 * @author zhuzhi.yang
 *
 */
@ExcelTable(fileName = "script/活动和礼包/煮酒论英雄.xls", sheetName = "积分兑换配置", beginRow = 2)
public class MakeWineScoreExchangeT {

	@ExcelColumn(index = 0)
	public int id;
	
	@ExcelColumn(index = 1)
	public String item;
	
	@ExcelColumn(index = 2)
	public int num;
	
	@ExcelColumn(index = 3)
	public int needCompoundScore;
	
}
