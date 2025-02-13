/**
 * 
 */
package com.morefun.XSanGo.robot;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 装备配置
 * 
 * @author sulingyun
 *
 */
public class EquipConfig {
	/** 装备ID随机范围 */
	@ExcelColumn(index = 0)
	public String idRange;

	/** 装备星级随机范围 */
	@ExcelColumn(index = 1)
	public String starRange;
	
	public int level;

	public String randomEquipId() {
		String[] strArr = idRange.split(",");
		String resStr = strArr[NumberUtil.random(strArr.length)];
		return resStr;
	}

	public int randomEquipStar() {
		String[] strArr = starRange.split(",");
		String resStr = strArr[NumberUtil.random(strArr.length)];
		return NumberUtil.parseInt(resStr);
	}
}