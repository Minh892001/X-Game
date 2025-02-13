package com.morefun.XSanGo.AttackCastle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 北伐兑换商城设置
 * 
 * @author qinguofeng
 * @date Feb 3, 2015
 */
@ExcelTable(fileName = "script/副本和怪物/北伐脚本.xls", beginRow = 2, sheetName = "兑换商城")
public class AttackCastleExchangeSettingT {
	/** 次数 */
	@ExcelColumn(index = 0)
	public int num;
	/** 花费 */
	@ExcelColumn(index = 1)
	public int consume;
}
