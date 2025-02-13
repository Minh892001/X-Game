package com.morefun.XSanGo.AttackCastle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 北伐刷新消耗
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/副本和怪物/北伐脚本.xls", beginRow = 2, sheetName = "换队元宝")
public class AttackCastleRefreshCostT {
	/** 次数 */
	@ExcelColumn(index = 0)
	public int num;
	/** 价格 */
	@ExcelColumn(index = 1)
	public int price;
}
