package com.morefun.XSanGo.auction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 商品刷新费用配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/拍卖行脚本.xls", beginRow = 2, sheetName = "刷新需费用")
public class AuctionRefreshConfigT {
	@ExcelColumn(index = 0)
	public int times;

	@ExcelColumn(index = 1)
	public int AuctionCoin;

}
