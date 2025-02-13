/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandConfigT
 * 功能描述：
 * 文件名：DreamlandConfigT.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 参数配置
 * 
 * @author weiyi.zhao
 * @since 2016-4-19
 * @version 1.0
 */
@ExcelTable(fileName = "script/副本和怪物/南华幻境.xls", beginRow = 2, sheetName = "参数配置")
public class DreamlandConfigT {

	/** 副本重置时间 */
	@ExcelColumn(index = 0, dataType = DataFormat.OnlyTime)
	public Date resetTime;

	/** 商店刷新时间 */
	@ExcelColumn(index = 1, dataType = DataFormat.OnlyTime)
	public Date refreshTime;

	/** 固定商品数量 */
	@ExcelColumn(index = 2)
	public int fixedItemNum;

	/** 随机商品数量 */
	@ExcelColumn(index = 3)
	public int randomItemNum;
}
