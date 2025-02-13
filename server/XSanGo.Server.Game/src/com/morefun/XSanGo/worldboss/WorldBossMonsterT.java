package com.morefun.XSanGo.worldboss;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 世界BOSS怪物数据
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/副本和怪物/乱世魔王脚本.xls", sheetName = "怪物数据", beginRow = 2)
public class WorldBossMonsterT {
	@ExcelColumn(index = 0)
	public int customsId;

	@ExcelColumn(index = 1)
	public int id;

	@ExcelColumn(index = 8)
	public int level;

	@ExcelColumn(index = 15)
	public double hp;
}
