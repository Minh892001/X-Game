package com.morefun.XSanGo.treasure;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 寻宝队伍开放配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/秘境寻宝.xls", sheetName = "寻宝部队", beginRow = 2)
public class TreasureGroupT {
	@ExcelColumn(index = 0)
	public int groupNum;
	
	@ExcelColumn(index = 1)
	public int openLevel;
	
	@ExcelColumn(index = 2)
	public int openVipLevel;
}
