/**
 * 
 */
package com.morefun.XSanGo.equip;

import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 装备重铸配置模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/装备脚本.xls", sheetName = "成长重铸", beginRow = 2)
public class EquipRebuildT {
	@ExcelColumn(index = 0)
	public int color;

	@ExcelColumn(index = 1)
	public String itemTemplateId;

	@ExcelColumn(index = 2)
	public int itemCount;

	public QualityColor getColor() {
		return QualityColor.valueOf(color);
	}

}
