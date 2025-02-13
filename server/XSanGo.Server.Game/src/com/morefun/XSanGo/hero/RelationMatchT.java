/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * 缘分匹配配置
 * 
 * @author sulingyun
 * 
 */
public class RelationMatchT {
	@ExcelColumn(index = 0)
	public int needObjCount;

	@ExcelColumn(index = 1)
	public String propertyName;

	@ExcelColumn(index = 2)
	public int value;
}
