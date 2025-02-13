/**
 * 
 */
package com.morefun.XSanGo.reward;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/道具相关/掉落算法.xls", sheetName = "掉落表")
public class RewardT {

	/** id */
	@ExcelColumn(index = 1)
	public String id;

	@ExcelColumn(index = 3)
	public int dropLevel;

	@ExcelColumn(index = 4)
	public int picks;

	@ExcelColumn(index = 5)
	public int exp;

	@ExcelColumn(index = 6)
	public int money;

	@ExcelColumn(index = 7)
	public int noDropRate;

	@ExcelComponet(index = 8, columnCount = 2, size = 10)
	public DropItemConfig[] itemConfigs;

	public int getTotalRate() {
		int result = this.noDropRate;
		for (DropItemConfig item : this.itemConfigs) {
			result += item.value;
		}

		return result;
	}
}