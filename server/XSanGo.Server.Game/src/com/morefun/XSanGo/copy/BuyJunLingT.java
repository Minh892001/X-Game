/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 军令购买模板
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/参数配置脚本/购买脚本.xls", sheetName = "购买军令")
public class BuyJunLingT {
	@ExcelColumn(index = 0)
	public int count;

	@ExcelColumn(index = 1)
	public int yuanbao;

	@ExcelColumn(index = 2)
	public int junling;

}
