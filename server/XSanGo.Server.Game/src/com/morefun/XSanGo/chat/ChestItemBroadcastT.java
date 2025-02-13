/**
 * 
 */
package com.morefun.XSanGo.chat;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 宝箱相关跑马灯配置
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/互动和聊天/聊天公告走马灯.xls", sheetName = "宝箱公告")
public class ChestItemBroadcastT {
	@ExcelColumn(index = 0)
	public String chestItemId;

	@ExcelColumn(index = 1)
	public int onOff;

	@ExcelColumn(index = 2)
	public String content;
}
