/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: BulletinTriggerT
 * 功能描述：
 * 文件名：BulletinTriggerT.java
 **************************************************
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 随机PK公告触发参数
 * 
 * @author zhangwei02.zhang
 * @since 2015年11月5日
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/副本PK脚本.xls", sheetName = "随机PK公告触发参数", beginRow = 2)
public class BulletinTriggerT {
	
	@ExcelColumn(index = 0)
	public int dailyTriggerCount;
	
	@ExcelColumn(index = 1)
	public int noticeCount;
	
	@ExcelColumn(index = 2)
	public String noticeTitle;
}
