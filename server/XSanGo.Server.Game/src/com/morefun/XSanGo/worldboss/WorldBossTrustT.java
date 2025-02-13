package com.morefun.XSanGo.worldboss;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 世界BOSS托管配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/副本和怪物/乱世魔王脚本.xls", sheetName = "托管配置", beginRow = 2)
public class WorldBossTrustT {
	
	@ExcelColumn(index = 0)
	public int level;
	
	@ExcelColumn(index = 1)
	public String items1;
	
	@ExcelColumn(index = 2)
	public int yuanbao1;
	
	/**战力*/
	@ExcelColumn(index = 3)
	public int power;
	
	@ExcelColumn(index = 4)
	public String items2;
	
	@ExcelColumn(index = 5)
	public int yuanbao2;
}