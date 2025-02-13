/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.script;

/**
 * 测试模板
 * 
 * @author Su LingYun
 * 
 */
@ExcelTable(fileName = "script/程序配置.xls", sheetName = "Sheet1")
public class SomeTemplate {
	@ExcelColumn(index = 0)
	public int id;
	@ExcelColumn(index = 1)
	public String name;
	@ExcelColumn(index = 2)
	public int str;
	@ExcelColumn(index = 3)
	public int def;
	@ExcelColumn(index = 4)
	public int hp;
}
