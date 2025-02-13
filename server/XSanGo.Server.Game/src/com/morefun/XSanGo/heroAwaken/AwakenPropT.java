/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: AwakenPropT
 * 功能描述：
 * 文件名：AwakenPropT.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * 洗炼额外属性
 * 
 * @author zwy
 * @since 2016-4-13
 * @version 1.0
 */
public class AwakenPropT {

	/** 属性编号 */
	@ExcelColumn(index = 0)
	public String propType;

	/** 附加属性值 */
	@ExcelColumn(index = 1)
	public int propValue;
}
