/**
 * 
 */
package com.morefun.XSanGo.trader;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.IRandomHitable;

/**
 * 随机名将模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/商人脚本.xls", beginRow = 2)
public class TraderHeroT implements IRandomHitable {
	@ExcelColumn(index = 0)
	public int heroId;

	@ExcelColumn(index = 2)
	public byte star;

	@ExcelColumn(index = 3)
	public int rank;

	@ExcelColumn(index = 4)
	public String successTc;

	@ExcelColumn(index = 5)
	public String failTc;

	@ExcelColumn(index = 6)
	public String title;

	@ExcelColumn(index = 7)
	public int color;

	@Override
	public int getRank() {
		return this.rank;
	}
}
