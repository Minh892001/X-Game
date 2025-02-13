/**
 * 
 */
package com.morefun.XSanGo.equip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/装备脚本.xls", sheetName = "升星配置", beginRow = 2)
public class EquipStarT {
	@ExcelColumn(index = 0)
	public int star;

	@ExcelComponet(index = 1, columnCount = 2, size = 5)
	public EquipStarUpConditionT[] conditions;

	// public int getMoney(QualityColor color) {
	// int value = color.ordinal();
	// switch (value) {
	// case 0:
	// return this.money0;
	// case 1:
	// return this.money1;
	// case 2:
	// return this.money2;
	// case 3:
	// return this.money3;
	// case 4:
	// return this.money4;
	// default:
	// throw new IllegalArgumentException();
	// }
	// }
}
