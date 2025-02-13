/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: HeroAwakenT
 * 功能描述：
 * 文件名：HeroAwakenT.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将觉醒配置
 * 
 * @author weiyi.zhao
 * @since 2016-4-12
 * @version 1.0
 */
@ExcelTable(fileName = "script/武将相关/武将觉醒脚本.xls", beginRow = 2, sheetName = "觉醒脚本")
public class HeroAwakenT {

	/** 武将编号 */
	@ExcelColumn(index = 0)
	public int heroId;

	/** 需要道具 */
	@ExcelColumn(index = 2)
	public String needItemCode;

	/** 需要道具数量 */
	@ExcelColumn(index = 3)
	public int needItemCount;

	/** 需要金币 */
	@ExcelColumn(index = 4)
	public int needGold;

	/** 需要步数 */
	@ExcelColumn(index = 5)
	public int needSteps;

	/** 属性 */
	@ExcelColumn(index = 6)
	public String prop;
}
