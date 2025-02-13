package com.morefun.XSanGo.AttackCastle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/副本和怪物/北伐脚本.xls", beginRow = 2, sheetName = "关卡额外掉整卡")
public class AttackCastleExtraHeroT {
	/** id */
	@ExcelColumn(index = 0)
	public int id;
	/** 名称 */
	@ExcelColumn(index = 1)
	public String name;
	/** 掉落权重 */
	@ExcelColumn(index = 2)
	public int pro;
	/** 是否首次掉落 */
	@ExcelColumn(index = 3)
	public int type;
}
