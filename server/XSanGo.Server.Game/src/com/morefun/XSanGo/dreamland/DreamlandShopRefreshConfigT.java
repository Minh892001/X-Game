/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandShopRefreshConfigT
 * 功能描述：
 * 文件名：DreamlandShopRefreshConfigT.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 商店刷新参数配置
 * 
 * @author weiyi.zhao
 * @since 2016-4-19
 * @version 1.0
 */
@ExcelTable(fileName = "script/副本和怪物/南华幻境.xls", beginRow = 2, sheetName = "商店刷新脚本")
public class DreamlandShopRefreshConfigT {

	/** 次数 */
	@ExcelColumn(index = 0)
	public int times;

	/** 货币代码 */
	@ExcelColumn(index = 1)
	public String coin;

	/** 花费 */
	@ExcelColumn(index = 2)
	public int pay;
}
