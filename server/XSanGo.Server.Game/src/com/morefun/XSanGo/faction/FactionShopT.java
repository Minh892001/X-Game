package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会商城")
public class FactionShopT {
	/**
	 * 货币类型：1-贡献2-荣誉3-免费
	 * 
	 * @author lixiongming
	 *
	 */
	public enum CoinType {
		DEFAULT, CONTRIBUTION, HONOR, FREE
	}

	/** 编号 */
	@ExcelColumn(index = 0)
	public int id;
	
	/** 道具编号 */
	@ExcelColumn(index = 1)
	public String itemId;
	
	/** 道具名称 */
	@ExcelColumn(index = 2)
	public String name;
	
	/** 数量 */
	@ExcelColumn(index = 3)
	public int num;
	
	/** 货币类型 */
	@ExcelColumn(index = 4)
	public int coinType;
	
	/** 单价 */
	@ExcelColumn(index = 5)
	public int price;
	
	/** 免费条件 */
	@ExcelColumn(index = 6)
	public int freeValue;
}
