/**
 * 
 */
package com.morefun.XSanGo.item;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 阵法进阶
 * 
 * @author xiongming.li
 * 
 */
@ExcelTable(fileName = "script/道具相关/道具脚本.xls", beginRow = 2, sheetName = "阵法进阶")
public class FormationBuffAdvancedT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public int type;

	@ExcelColumn(index = 3)
	public int useBuffNum;

	@ExcelColumn(index = 4)
	public String useItems;

	@ExcelColumn(index = 5)
	public int useCoin;

	/** 增加战力 */
	@ExcelColumn(index = 10)
	public int addPower;

}
