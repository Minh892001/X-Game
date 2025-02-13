/**
 * 
 */
package com.morefun.XSanGo.item;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 宝箱钥匙对应表
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/道具相关/道具脚本.xls", sheetName = "宝箱钥匙对应表", beginRow = 2)
public class ChestKeyT {
	@ExcelColumn(index = 0)
	public String chestItem;

	@ExcelColumn(index = 1)
	public String keyItem;
}
