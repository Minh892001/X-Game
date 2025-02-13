package com.morefun.XSanGo.ladder;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 等级星级对应分数
 * 
 * @author xiongming.li
 *
 */
@ExcelTable(fileName = "script/互动和聊天/群雄争霸脚本.xls", beginRow = 2, sheetName = "分值计算")
public class LadderLevelScoreT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public int star;

	@ExcelColumn(index = 2)
	public int score;
}
