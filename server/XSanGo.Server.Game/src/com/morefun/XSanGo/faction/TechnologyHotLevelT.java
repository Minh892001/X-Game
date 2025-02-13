package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会科技热门等级配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "综合类科技")
public class TechnologyHotLevelT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public int studyScore;

	@ExcelColumn(index = 2)
	public int studyMinute;

	@ExcelColumn(index = 3)
	public int exp;
}
