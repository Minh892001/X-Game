/**
 * 
 */
package com.morefun.XSanGo.trader;

import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.Money;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.IRandomHitable;

/**
 * 神秘商人物品模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/商人脚本.xls", beginRow = 2)
public class TraderCommodityT implements IRandomHitable {
	@ExcelColumn(index = 1)
	public String itemTemplate;

	@ExcelColumn(index = 3)
	public int minCount;

	@ExcelColumn(index = 4)
	public int maxCount;

	@ExcelColumn(index = 5)
	public int rank;

	@ExcelColumn(index = 6)
	public int currencyType;

	@ExcelColumn(index = 7)
	public int price;

	@ExcelColumn(index = 8)
	public int group;

	private Money money;

	public Money getPrice() {
		if (this.money == null) {
			this.money = new Money(CurrencyType.valueOf(this.currencyType),
					this.price);
		}

		return this.money;
	}

	@Override
	public int getRank() {
		return this.rank;
	}
}
