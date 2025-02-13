/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 大富温 神秘商店
 */
@ExcelTable(fileName = "script/活动和礼包/大富翁活动脚本.xls", sheetName = "神秘商店", beginRow = 2)
public class LotteryMysticalShopT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String item;

	@ExcelColumn(index = 2)
	public String itemName;

	@ExcelColumn(index = 3)
	public int num;

	@ExcelColumn(index = 4)
	public int pro;

	@ExcelColumn(index = 5)
	public int coinType;

	@ExcelColumn(index = 6)
	public int price;

	@ExcelColumn(index = 7)
	public int price2;

	@ExcelColumn(index = 8)
	public int discount;
	
	@ExcelColumn(index = 9)
	public int preview;
	
	public int startRange;
	
	public int endRange;

}
