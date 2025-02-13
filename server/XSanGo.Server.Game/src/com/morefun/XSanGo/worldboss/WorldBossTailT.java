package com.morefun.XSanGo.worldboss;

import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 世界BOSS尾刀奖励
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/副本和怪物/乱世魔王脚本.xls", sheetName = "尾刀奖励", beginRow = 2)
public class WorldBossTailT {
	@ExcelColumn(index = 0)
	public int id;

	/**尾刀生命%*/
	@ExcelColumn(index = 1)
	public int hp;

	@ExcelColumn(index = 2)
	public String itemIds;
	
	public ItemView[] items;
}
