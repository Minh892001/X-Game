package com.morefun.XSanGo.worldboss;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 世界BOSS伤害排行奖励
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/副本和怪物/乱世魔王脚本.xls", sheetName = "伤害排行奖励", beginRow = 2)
public class WorldBossHurtRankT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int rank;

	@ExcelColumn(index = 2)
	public String items;
}
