package com.morefun.XSanGo.template;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 32强对阵配置
 * 
 * @author xiongming.li
 *
 */
@ExcelTable(fileName = "script/比武大会.xls", beginRow = 2, sheetName = "上帝之手")
public class CrossScheduleConfT {
	@ExcelColumn(index = 0)
	public int crossId;

	@ExcelColumn(index = 2)
	public String leftRoleId;
	
	@ExcelColumn(index = 4)
	public String rightRoleId;
}
