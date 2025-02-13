/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 武将群战技能效果模板
 * 
 * @author sulingyun
 *
 */
public class SkillEffectT {
	@ExcelColumn(index = 0)
	public String type;

	@ExcelColumn(index = 1)
	public String code;

	@ExcelColumn(index = 2)
	public float initValue;

	@ExcelColumn(index = 3)
	public float perLevel;

	public boolean isEffective() {
		return !TextUtil.isBlank(code);
	}
}
