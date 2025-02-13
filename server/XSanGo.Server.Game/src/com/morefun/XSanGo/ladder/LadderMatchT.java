package com.morefun.XSanGo.ladder;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 等级匹配模版
 * 
 * @author xiongming.li
 *
 */
@ExcelTable(fileName = "script/互动和聊天/群雄争霸脚本.xls", beginRow = 2, sheetName = "匹配区间")
public class LadderMatchT {
	/**匹配条件开始等级*/
	@ExcelColumn(index = 0)
	public int selfStart;

	/**匹配条件结束等级*/
	@ExcelColumn(index = 1)
	public int selfEnd;

	/**匹配对手排名高于自己个数*/
	@ExcelColumn(index = 2)
	public int matchHeight;

	/**匹配对手排名低于自己个数*/
	@ExcelColumn(index = 3)
	public int matchLow;

}
