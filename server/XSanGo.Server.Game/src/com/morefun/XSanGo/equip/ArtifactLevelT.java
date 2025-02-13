/**
 * 
 */
package com.morefun.XSanGo.equip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 神器
 */
@ExcelTable(fileName = "script/道具相关/神器脚本.xls", sheetName = "神器淬炼", beginRow = 2)
public class ArtifactLevelT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 2)
	public int level;

	@ExcelColumn(index = 3)
	public String upgradeItems;

	@ExcelColumn(index = 4)
	public int upgradeGold;

	@ExcelComponet(index = 5, columnCount = 2, size = 3)
	public ArtifactPropertyT[] propertys;
}
