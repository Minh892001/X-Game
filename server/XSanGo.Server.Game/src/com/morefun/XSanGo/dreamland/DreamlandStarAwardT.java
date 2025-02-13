/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandStarAwardT
 * 功能描述：
 * 文件名：DreamlandStarAwardT.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 星星奖励配置
 * 
 * @author weiyi.zhao
 * @since 2016-4-19
 * @version 1.0
 */
@ExcelTable(fileName = "script/副本和怪物/南华幻境.xls", beginRow = 2, sheetName = "星数奖励")
public class DreamlandStarAwardT {

	/** 星数 */
	@ExcelColumn(index = 1)
	public int starNum;

	/** 奖励组 */
	@ExcelColumn(index = 2)
	public String items;
}
