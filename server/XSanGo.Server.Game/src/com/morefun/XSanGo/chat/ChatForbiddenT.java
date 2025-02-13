package com.morefun.XSanGo.chat;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/聊天禁言参数配置.xls", beginRow = 2, sheetName = "禁言参数")
public class ChatForbiddenT {
	/**
	 * 主公等级
	 */
	@ExcelColumn(index = 0)
	public int level;

	/**
	 * VIP等级
	 */
	@ExcelColumn(index = 1)
	public int vip;

	/**
	 * 超时时限
	 */
	@ExcelColumn(index = 2)
	public int timeout;

	/**
	 * 禁言次数
	 */
	@ExcelColumn(index = 3)
	public int vote_need;

	/**
	 * 解除禁言时长
	 */
	@ExcelColumn(index = 4)
	public int remove_time;

	/**
	 * 同时禁言上限
	 */
	@ExcelColumn(index = 5)
	public int forbidden_times;
	
	/**
	 * 发起禁言消息模板
	 */
	@ExcelColumn(index = 6)
	public String chat_template;
	
	/**
	 * 禁言成功消息模板
	 */
	@ExcelColumn(index = 7)
	public String forbidd_success;
	
	/**
	 * 禁言超时消息
	 */
	@ExcelColumn(index = 8)
	public String timeout_template;
	
	/**
	 * 解除禁言消息
	 */
	@ExcelColumn(index = 9)
	public String remove_template;
}
