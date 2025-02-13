/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 怪物模板
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/副本和怪物/怪物脚本.xls", beginRow = 2, sheetName = "怪物配置")
public class MonsterT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 2)
	public String name;

	@ExcelColumn(index = 4)
	public int color;

	@ExcelColumn(index = 5)
	public int quality;

	@ExcelColumn(index = 6)
	public int star;

	@ExcelColumn(index = 7)
	public int level;

	@ExcelColumn(index = 8)
	public int templateId;

	@ExcelColumn(index = 11)
	public int brave;

	@ExcelColumn(index = 12)
	public int calm;

	@ExcelColumn(index = 13)
	public int hp;

	@ExcelColumn(index = 14)
	public int power;

	@ExcelColumn(index = 15)
	public int intel;

	@ExcelColumn(index = 22)
	public int dodge;

	@ExcelColumn(index = 29)
	public int damageResRate;

	@ExcelColumn(index = 31)
	public int critRate;

	@ExcelColumn(index = 32)
	public int critResRate;

}
