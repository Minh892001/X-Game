package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 关卡首次占领奖励
 * 
 * @author lxm
 *
 */
@ExcelTable(fileName = "script/互动和聊天/关卡占领奖励.xls", sheetName = "首次占领奖励", beginRow = 2)
public class ServerCopyFirstT {
	@ExcelColumn(index = 0)
	public int count;
	
	@ExcelColumn(index = 1)
	public String items;
}
