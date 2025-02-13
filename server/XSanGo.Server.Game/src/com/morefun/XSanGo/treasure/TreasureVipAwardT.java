package com.morefun.XSanGo.treasure;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 寻宝vip tc产出
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/秘境寻宝.xls", sheetName = "tc产出", beginRow = 2)
public class TreasureVipAwardT {
	@ExcelColumn(index = 0)
	public int startVip;

	@ExcelColumn(index = 1)
	public int endVip;

	/** tc */
	@ExcelColumn(index = 2)
	public String tc;
	
	/**基础奖励个数*/
	@ExcelColumn(index = 3)
	public int baseValue;
}
