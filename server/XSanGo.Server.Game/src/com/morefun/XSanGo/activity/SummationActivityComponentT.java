/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * 累计充值或消费的活动配置模板
 * 
 * @author sulingyun
 *
 */
public class SummationActivityComponentT {
	@ExcelColumn(index = 0)
	public int thresholdVal;

	@ExcelColumn(index = 1)
	public String tc;

	// @ExcelColumn(index = 2)
	// public String desc;

}
