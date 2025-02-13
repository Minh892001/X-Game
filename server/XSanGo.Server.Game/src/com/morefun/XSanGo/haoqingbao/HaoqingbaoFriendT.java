package com.morefun.XSanGo.haoqingbao;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 豪情宝，好友红包
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/壕情宝.xls", beginRow = 2, sheetName = "好友红包")
public class HaoqingbaoFriendT {
	/** 红包派发范围 */
	@ExcelColumn(index = 1)
	public int range;
	/** 最少红包数量 */
	@ExcelColumn(index = 5)
	public int minNum;
}
