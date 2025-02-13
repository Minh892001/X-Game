/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: HeroBaptizeConfigT
 * 功能描述：
 * 文件名：HeroBaptizeConfigT.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将洗炼参数配置
 * 
 * @author zwy
 * @since 2016-4-13
 * @version 1.0
 */
@ExcelTable(fileName = "script/武将相关/武将觉醒脚本.xls", beginRow = 2, sheetName = "洗炼参数")
public class HeroBaptizeConfigT {

	/** 洗练等级 */
	@ExcelColumn(index = 0)
	public int baptizeLvl;

	/** 单次洗炼消耗 */
	@ExcelColumn(index = 1)
	public String expendOnce;

	/** 洗炼升级消耗 */
	@ExcelColumn(index = 2)
	public String levelUpExpend;
}
