package com.morefun.XSanGo.item;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 装备镶嵌模版
 * 
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/道具相关/装备脚本.xls", beginRow = 2, sheetName = "装备镶嵌")
public class EquipGemT {
	/** 装备品质 */
	@ExcelColumn(index = 0)
	public int qColor;
	/** 默认孔数量 */
	@ExcelColumn(index = 1)
	public int holeNum;
	/** 开孔道具模版 */
	@ExcelColumn(index = 2)
	public String holeToolId;
	/** 需要消耗的道具数量 */
	@ExcelColumn(index = 3)
	public int num;
	/** 第1个孔需要的等级 */
	@ExcelColumn(index = 4)
	public int lvl1;
	/** 第2个孔需要的等级 */
	@ExcelColumn(index = 5)
	public int lvl2;
	/** 第3个孔需要的等级 */
	@ExcelColumn(index = 6)
	public int lvl3;
	/** 第4个孔需要的等级 */
	@ExcelColumn(index = 7)
	public int lvl4;

	@ExcelColumn(index = 8)
	public String lvl5Info;

	@ExcelColumn(index = 9)
	public String lvl6Info;

	/** 最多的孔数量 */
	@ExcelColumn(index = 10)
	public int maxHoleNum;

	public int getLevelLimit(int holeNum) {
		int[] lvs = { lvl1, lvl2, lvl3, lvl4 };
		if (maxHoleNum > 4) {
			lvs = new int[] { lvl1, lvl2, lvl3, lvl4, NumberUtil.parseInt(lvl5Info.split(",")[1]),
					NumberUtil.parseInt(lvl6Info.split(",")[1]) };
		}
		return lvs[holeNum - 1];
	}
}
