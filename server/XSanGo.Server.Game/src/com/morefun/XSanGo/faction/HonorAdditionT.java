package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 荣誉加成
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会战奖励浮动")
public class HonorAdditionT {
	@ExcelColumn(index = 0)
	public int num;// 连胜次数

	@ExcelColumn(index = 1)
	public int addHonor;// 连胜增加荣誉

	@ExcelColumn(index = 2)
	public int killAddHonor;// 终结连胜增加荣誉
}
