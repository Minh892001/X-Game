package com.morefun.XSanGo.worldboss;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 最高伤害配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/参数配置脚本/游戏参数配置表.xls", sheetName = "单次伤害上限", beginRow = 2)
public class MaxHarmConfT {

	@ExcelColumn(index = 0)
	public String level;

	/**
	 * 1-公会副本地狱难度
	 * 2-世界BOSS
	 */
	@ExcelColumn(index = 1)
	public int type;

	@ExcelColumn(index = 2)
	public int maxHarm;

}
