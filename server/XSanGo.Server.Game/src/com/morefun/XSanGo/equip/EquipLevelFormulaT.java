/**
 * 
 */
package com.morefun.XSanGo.equip;

import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 装备强化金钱公式模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/装备脚本.xls", sheetName = "强化金钱公式", beginRow = 2)
public class EquipLevelFormulaT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public int jinbi0;

	@ExcelColumn(index = 2)
	public int jinbi1;

	@ExcelColumn(index = 3)
	public int jinbi2;

	@ExcelColumn(index = 4)
	public int jinbi3;

	@ExcelColumn(index = 5)
	public int jinbi4;

	private int[] array;

	public int getJinbi(QualityColor color) {
		if (this.array == null) {
			this.array = new int[] { this.jinbi0, this.jinbi1, this.jinbi2,
					this.jinbi3, this.jinbi4 };
		}

		return this.array[color.ordinal()];
	}
}
