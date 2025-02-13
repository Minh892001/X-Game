/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: CompositeChestT
 * 功能描述：
 * 文件名：CompositeChestT.java
 **************************************************
 */
package com.morefun.XSanGo.item;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 复合宝箱道具表
 * 
 * @author zwy
 * @since 2015-12-2
 * @version 1.0
 */
@ExcelTable(fileName = "script/道具相关/道具脚本.xls", sheetName = "复合宝箱脚本", beginRow = 2)
public class CompositeChestT {

	/** 宝箱编号 */
	@ExcelColumn(index = 0)
	public String itemId;

	/** 道具产出列表 */
	@ExcelColumn(index = 1)
	public String awardItems;

	/** 道具产出列表 */
	//@ExcelComponet(index = 1, columnCount = 2, size = 10)
	public PropertyT[] items;
}
