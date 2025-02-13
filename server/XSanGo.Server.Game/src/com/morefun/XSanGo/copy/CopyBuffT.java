package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 副本Buff
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/副本和怪物/关卡脚本.xls", sheetName = "关卡BUFF")
public class CopyBuffT {
	/** ID */
	@ExcelColumn(index = 0)
	public int id;
	/** 触发类型 */
	@ExcelColumn(index = 3)
	public int type;
	/** 触发条件 */
	@ExcelColumn(index = 4)
	public int limit;
	/** 优先等级 */
	@ExcelColumn(index = 5)
	public int priority;
}
