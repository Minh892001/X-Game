/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: ApiFunctionTypeT
 * 功能描述：
 * 文件名：ApiFunctionTypeT.java
 **************************************************
 */
package com.morefun.XSanGo.api;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 功能类型表
 * 
 * @author zhangwei02.zhang
 * @since 2015年11月11日
 * @version 1.0
 */
@ExcelTable(fileName = "script/活动和礼包/活动列表.xls", sheetName = "功能参数配置", beginRow = 2)
public class ApiFunctionTypeT {

	/** 功能编号 */
	@ExcelColumn(index = 0)
	public int functionId;

	/** api接口 */
	@ExcelColumn(index = 2)
	public String apiInterface;

}
