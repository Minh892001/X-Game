/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将录用花费
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/副本和怪物/关卡脚本.xls", sheetName = "武将录用花费", beginRow = 2)
public class CaptureEmploySpandT {
	@ExcelColumn(index = 0)
	public int num;

	@ExcelColumn(index = 1)
	public int cost;

}
