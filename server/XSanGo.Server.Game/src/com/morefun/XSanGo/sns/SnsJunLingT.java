package com.morefun.XSanGo.sns;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/好友脚本.xls", sheetName = "军令", beginRow = 2)
public class SnsJunLingT {
	@ExcelColumn(index = 0)
	public int vipLv;
	@ExcelColumn(index = 1)
	public int maxNum;
	@ExcelColumn(index = 2)
	public int sendMaxNum;
	@ExcelColumn(index = 3)
	public int sendNum;
	@ExcelColumn(index = 4)
	public int acceptMaxNum;
//	@ExcelColumn(index = 5)
//	public int acceptNum;
	@ExcelColumn(index = 5)
	public int friendPoint;
}
