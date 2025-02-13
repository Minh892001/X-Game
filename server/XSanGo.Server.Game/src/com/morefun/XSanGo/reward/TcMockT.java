package com.morefun.XSanGo.reward;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/道具相关/掉落算法.xls", sheetName = "掉落伪随机算法")
public class TcMockT {
	@ExcelColumn(index = 1)
	public String tc;

	@ExcelColumn(index = 2)
	public int baseRate;

	@ExcelColumn(index = 3)
	public int addWhenFail;

	@ExcelColumn(index = 4)
	public int reduceWhenSucess;

	@ExcelColumn(index = 5)
	public String mockTc;

	@ExcelColumn(index = 7)
	public int override;

}
