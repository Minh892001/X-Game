/**
 * 
 */
package com.morefun.XSanGo.copy;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/参数配置脚本/游戏参数配置表.xls", sheetName = "副本关卡次数配置")
public class CopyConfigT {
	@ExcelColumn(index = 0, dataType = DataFormat.OnlyTime)
	public Date resetTime1;

	@ExcelColumn(index = 1, dataType = DataFormat.OnlyTime)
	public Date resetTime2;

	@ExcelColumn(index = 2)
	public int juniorMaxTime;

	@ExcelColumn(index = 3)
	public int seniorMaxTime;

	@ExcelColumn(index = 4)
	public int topMaxTime;
	
	@ExcelColumn(index = 5)
	public int juniorJunLingConsume;

	@ExcelColumn(index = 6)
	public int seniorJunLingConsume;

	@ExcelColumn(index = 7)
	public int topJunLingConsume;

}
