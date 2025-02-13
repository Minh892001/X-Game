/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandShopT
 * 功能描述：
 * 文件名：DreamlandShopT.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * 南华兑换商店数据
 * 
 * @author weiyi.zhao
 * @since 2016-4-20
 * @version 1.0
 */
public abstract class DreamlandShopT {

	/** 编号 */
	@ExcelColumn(index = 0)
	public int id;

	/** 物品代码 */
	@ExcelColumn(index = 1)
	public String itemCode;

	/** 物品数量 */
	@ExcelColumn(index = 3)
	public int itemNum;

	/** 权值 */
	@ExcelColumn(index = 4)
	public int pro;

	/** 货币代码 */
	@ExcelColumn(index = 5)
	public String coin;

	/** 单价 */
	@ExcelColumn(index = 6)
	public int price;

	/** 折扣 */
	@ExcelColumn(index = 7)
	public int rebate;

}
