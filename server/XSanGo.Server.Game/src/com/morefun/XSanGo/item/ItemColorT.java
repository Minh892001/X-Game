/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: ItemColorT
 * 功能描述：
 * 文件名：ItemColorT.java
 **************************************************
 */
package com.morefun.XSanGo.item;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 物品品质颜色对照数据
 * 
 * @author weiyi.zhao
 * @since 2016-3-8
 * @version 1.0
 */
@ExcelTable(fileName = "script/参数配置脚本/对照说明表.xls", sheetName = "物品品质说明", beginRow = 2)
public class ItemColorT {

	/** 品质 */
	@ExcelColumn(index = 1)
	public int quality;

	/** 色值 */
	@ExcelColumn(index = 3)
	public String colorValue;
}
