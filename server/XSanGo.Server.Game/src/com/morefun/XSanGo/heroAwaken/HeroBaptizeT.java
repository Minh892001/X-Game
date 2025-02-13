/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: HeroBaptizeT
 * 功能描述：
 * 文件名：HeroBaptizeT.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将洗炼配置
 * 
 * @author zwy
 * @since 2016-4-13
 * @version 1.0
 */
@ExcelTable(fileName = "script/武将相关/武将觉醒脚本.xls", beginRow = 2, sheetName = "洗炼属性")
public class HeroBaptizeT {

	/** 属性类型 */
	@ExcelColumn(index = 0)
	public String propType;

	/** 初始上限 */
	@ExcelColumn(index = 1)
	public int initMax;

	/** 升级属性加成比 */
	@ExcelColumn(index = 2)
	public int propUpPerLv;

	/** 单次洗炼下限 */
	@ExcelColumn(index = 3)
	public int rangeOnceMin;

	/** 单次洗炼上限 */
	@ExcelColumn(index = 4)
	public int rangeOnceMax;
}
