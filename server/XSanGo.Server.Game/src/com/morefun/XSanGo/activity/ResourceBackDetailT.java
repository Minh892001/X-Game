package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.Money;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 资源找回，详细配置
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/活动和礼包/资源找回.xls", sheetName = "详细配置", beginRow = 2)
public class ResourceBackDetailT {
	/** ID */
	@ExcelColumn(index = 0)
	public int id;
	/** 玩法类型 */
	@ExcelColumn(index = 1)
	public int type;
	/** 玩法名称 */
	@ExcelColumn(index = 2)
	public String name;
	/** 等级限制 */
	@ExcelColumn(index = 3)
	public int levelLimit;
	/** 奖励 */
	@ExcelColumn(index = 4)
	public String rewards;
	/** 售价1 */
	@ExcelColumn(index = 5)
	public String price1;
	/** 售价2 */
	@ExcelColumn(index = 6)
	public String price2;

	private Money parseMoney(String price) {
		String[] priceStr = price.split(":");
		Money m = new Money();
		m.num = Integer.parseInt(priceStr[1]);
		// 金币
		if (Integer.parseInt(priceStr[0]) == 1) {
			m.type = CurrencyType.Jinbi;
		}
		if (Integer.parseInt(priceStr[0]) == 2) {
			m.type = CurrencyType.Yuanbao;
		}
		return m;
	}

	public Money getPrice1() {
		return parseMoney(price1);
	}

	public Money getPrice2() {
		return parseMoney(price2);
	}
}
