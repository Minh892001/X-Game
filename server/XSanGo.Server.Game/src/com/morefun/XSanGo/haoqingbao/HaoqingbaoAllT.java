package com.morefun.XSanGo.haoqingbao;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 全服红包
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/壕情宝.xls", beginRow = 2, sheetName = "全服红包")
public class HaoqingbaoAllT {
	/** 最少红包数量 */
	@ExcelColumn(index = 4)
	public int minNum;
}
