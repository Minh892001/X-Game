/**
 * 
 */
package com.morefun.XSanGo.role;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 随机名字模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/参数配置脚本/玩家名称.xls")
public class RandomNameT {
	@ExcelColumn(index = 1)
	public String value0;

	@ExcelColumn(index = 2)
	public String value1;

	@ExcelColumn(index = 3)
	public String value2;

	public String getValue(int i) {
		switch (i) {
		case 0:
			return this.value0;
		case 1:
			return this.value1;
		case 2:
			return this.value2;
		default:
			return "";
		}
	}
}
