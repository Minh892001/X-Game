/**
 * 
 */
package com.morefun.XSanGo.temp;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 充值记录
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/封测返利.xls", sheetName = "充值记录")
public class ChargeHistoryT {
	@ExcelColumn(index = 0)
	public String account;

	@ExcelColumn(index = 1)
	public int rmb;
}
