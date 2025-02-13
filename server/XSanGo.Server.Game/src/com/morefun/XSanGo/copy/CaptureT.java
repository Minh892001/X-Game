/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 俘虏配置
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/副本和怪物/关卡脚本.xls", beginRow = 2, sheetName = "关卡武将俘获处理")
public class CaptureT {
	@ExcelColumn(index = 0)
	public int copyId;

	@ExcelColumn(index = 2)
	public int heroId;

	@ExcelColumn(index = 3)
	public int releaseExp;

	@ExcelColumn(index = 4)
	public String KillTc;

	@ExcelColumn(index = 5)
	public int employRate;

	@ExcelColumn(index = 6)
	public int notify;
	
	@ExcelColumn(index = 7)
	public int notifyNum;
}
