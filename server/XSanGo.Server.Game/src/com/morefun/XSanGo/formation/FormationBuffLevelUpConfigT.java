/**
 * 
 */
package com.morefun.XSanGo.formation;

import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 阵法升级算法模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/道具脚本.xls", sheetName = "阵法升级算法", beginRow = 2)
public class FormationBuffLevelUpConfigT {
	@ExcelColumn(index = 0)
	public int colorValue;

	@ExcelColumn(index = 2)
	public int provideExpBase;

	@ExcelComponet(index = 3, columnCount = 2, size = 5)
	public FormationBuffLevelUpConditionT[] conditions;

	public QualityColor getColor() {
		return QualityColor.valueOf(colorValue);
	}
}
