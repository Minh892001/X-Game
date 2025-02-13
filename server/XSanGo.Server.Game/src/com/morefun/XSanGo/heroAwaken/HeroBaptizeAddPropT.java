/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: HeroBaptizeAddPropT
 * 功能描述：
 * 文件名：HeroBaptizeAddPropT.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import com.morefun.XSanGo.heroAwaken.AwakenPropT;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将洗炼附加属性配置
 * 
 * @author zwy
 * @since 2016-4-13
 * @version 1.0
 */
@ExcelTable(fileName = "script/武将相关/武将觉醒脚本.xls", beginRow = 2, sheetName = "洗炼额外属性")
public class HeroBaptizeAddPropT {

	/** 洗炼等级 */
	@ExcelColumn(index = 0)
	public int baptizeLvl;

	/** 附加属性 */
	@ExcelComponet(index = 1, columnCount = 2, size = 2)
	public AwakenPropT[] props;

}
