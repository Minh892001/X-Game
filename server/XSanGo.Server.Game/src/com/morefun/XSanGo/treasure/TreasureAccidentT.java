package com.morefun.XSanGo.treasure;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 寻宝矿难参数
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/寻宝大冒险脚本.xls", sheetName = "矿难参数", beginRow = 2)
public class TreasureAccidentT {
	@ExcelColumn(index = 0)
	public int beginLevel;

	@ExcelColumn(index = 1)
	public int endLevel;

	/** 最大救援次数 */
	@ExcelColumn(index = 2)
	public int maxRescueNum;

	/** 矿难间隔 分钟 */
	@ExcelColumn(index = 3)
	public int interval;

	/** 最大矿难次数 */
	@ExcelColumn(index = 4)
	public int maxAccidentNum;

	/** 最大收获次数 */
	@ExcelColumn(index = 5)
	public int maxGainNum;
}
