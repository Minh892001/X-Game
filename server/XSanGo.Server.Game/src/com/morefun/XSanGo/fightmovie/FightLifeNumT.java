package com.morefun.XSanGo.fightmovie;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 战斗生命参数
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/参数配置脚本/游戏参数配置表.xls", sheetName = "战斗生命参数", beginRow = 2)
public class FightLifeNumT {
	/** 功能ID */
	@ExcelColumn(index = 0)
	public int id;
	/** 名称 */
	@ExcelColumn(index = 1)
	public String name;
	/** 类型，0,pvp;1pve */
	@ExcelColumn(index = 2)
	public int type;
	/** 声明系数 */
	@ExcelColumn(index = 3)
	public int num;
	/** 是否采用新的战斗机制 */
	@ExcelColumn(index = 7)
	public int newBattle;
}
