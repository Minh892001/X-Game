package com.morefun.XSanGo.timeBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/副本和怪物/时空战役.xls", beginRow = 2, sheetName = "场景数据")
public class BattleSceneT {
	@ExcelColumn(index = 0)
	public int passId;//关卡ID

	@ExcelColumn(index = 2)
	public int sceneSeq;//关卡序列

	@ExcelColumn(index = 10)
	public int duelMonsterId;//单挑武将ID

	@ExcelColumn(index =12)
	public String vsTips;
	
	
}
