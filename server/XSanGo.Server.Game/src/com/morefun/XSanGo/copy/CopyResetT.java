/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 关卡重置配置模板
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/副本和怪物/关卡脚本.xls", beginRow = 2, sheetName = "关卡购买")
public class CopyResetT {
	@ExcelColumn(index = 0)
	public int time;

	@ExcelColumn(index = 1)
	public int price;

}
