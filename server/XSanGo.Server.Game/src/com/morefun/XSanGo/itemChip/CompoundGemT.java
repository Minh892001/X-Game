package com.morefun.XSanGo.itemChip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 宝石合成
 * 
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/道具相关/道具脚本.xls", beginRow = 2, sheetName = "宝石合成")
public class CompoundGemT {
	/** 宝石模版ID */
	@ExcelColumn(index = 2)
	public String id;
	/** 所需材料 */
	@ExcelColumn(index = 4)
	public String needItem;
	/** 需要的材料数量 */
	@ExcelColumn(index = 5)
	public int needNum;
}
