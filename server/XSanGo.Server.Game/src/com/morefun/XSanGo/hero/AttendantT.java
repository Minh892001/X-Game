/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * 随处模板数据
 * 
 * @author sulingyun
 * 
 */
public class AttendantT {
	@ExcelColumn(index = 0)
	public String propertyCode;

	@ExcelColumn(index = 1)
	public int defaultValue;

	@ExcelColumn(index = 2)
	public int specialHeroId;

	@ExcelColumn(index = 4)
	public int specialValue;

}
