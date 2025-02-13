/**
 * 
 */
package com.morefun.XSanGo.activity;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公告模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/公告邮件/公告.xls", sheetName = "公告", beginRow = 2)
public class AnnounceT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int type;
	
	@ExcelColumn(index = 2)
	public String firstTitle;

	@ExcelColumn(index = 3)
	public String secondTitle;

	@ExcelColumn(index = 4)
	public String content;

	@ExcelColumn(index = 5)
	public int open;

	@ExcelColumn(index = 6, dataType = DataFormat.DateTime)
	public Date beginTime;

	@ExcelColumn(index = 7, dataType = DataFormat.DateTime)
	public Date endTime;
	
	/**只在某些渠道显示*/
	@ExcelColumn(index = 8)
	public String showChannel;
	
	/**不在某些渠道显示*/
	@ExcelColumn(index = 9)
	public String excludeChannel;

	public boolean isOpen() {
		return this.open == 1;
	}

}
