package com.morefun.XSanGo.collect;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 *
 * @author qinguofeng
 * @date Apr 17, 2015
 */
@ExcelTable(fileName = "script/武将相关/魂魄转盘脚本.xls", beginRow = 1, sheetName = "将魂数量")
public class CollectHeroSoulNumT {
	@ExcelColumn(index = 0)
	public int num1;  // 随机数量
	@ExcelColumn(index = 1)
	public int prob1; // 权重
	@ExcelColumn(index = 2)
	public int num2;
	@ExcelColumn(index = 3)
	public int prob2;
	@ExcelColumn(index = 4)
	public int num3;
	@ExcelColumn(index = 5)
	public int prob3;
}
