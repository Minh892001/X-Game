package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会科技等级配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "阵营类科技")
public class TechnologyLevelT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public int studyScore;

	@ExcelColumn(index = 2)
	public int studyMinute;

	@ExcelColumn(index = 3)
	public int exp;
}
