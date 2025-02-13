/**
 * 
 */
package com.morefun.XSanGo.temp;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/活动和礼包/临时运营活动.xls", sheetName = "活动配置表")
public class TemporaryWrapperT {
	@ExcelColumn(index = 1)
	public String className;

	@ExcelColumn(index = 2)
	public int open;

	@ExcelColumn(index = 3)
	public String params;

	@ExcelColumn(index = 4, dataType = DataFormat.DateTime)
	public Date beginTime;

	@ExcelColumn(index = 5, dataType = DataFormat.DateTime)
	public Date endTime;
}
