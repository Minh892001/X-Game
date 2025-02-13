package com.morefun.XSanGo.chat;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/聊天禁言参数配置.xls", beginRow = 2, sheetName = "静默机制")
public class ChatFilterRuleT {
	/**
	 * 标记条件
	 */
	@ExcelColumn(index = 0)
	public String sign;

	/**
	 * 静默条件
	 */
	@ExcelColumn(index = 1)
	public String silence;

	/**
	 * 短时静默效果
	 */
	@ExcelColumn(index = 2)
	public int shortTime;

	/**
	 * 短静累计次数
	 */
	@ExcelColumn(index = 3)
	public int total;

	/**
	 * 长时静默效果
	 */
	@ExcelColumn(index = 4)
	public int longTime;

}
