/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandChallengeT
 * 功能描述：
 * 文件名：DreamlandChallengeT.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 功能描述
 * 
 * @author weiyi.zhao
 * @since 2016-5-16
 * @version 1.0
 */
@ExcelTable(fileName = "script/副本和怪物/南华幻境.xls", beginRow = 2, sheetName = "挑战次数购买")
public class DreamlandChallengeT {

	/** 挑战次数 */
	@ExcelColumn(index = 0)
	public int times;

	/** 所需VIP条件 */
	@ExcelColumn(index = 1)
	public int vipLvl;

	/** 价格 */
	@ExcelColumn(index = 2)
	public int price;
}
