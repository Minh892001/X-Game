package com.morefun.XSanGo.auction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 拍卖行商品
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/拍卖行脚本.xls", beginRow = 2)
public class AuctionShopRewardT {
	/** 编号 */
	@ExcelColumn(index = 0)
	public int id;
	/** 道具编号 */
	@ExcelColumn(index = 1)
	public String itemId;
	/** 道具名称 */
	@ExcelColumn(index = 2)
	public String name;
	/** 最小数量 */
	@ExcelColumn(index = 3)
	public int minNum;
	/** 最大数量 */
	@ExcelColumn(index = 4)
	public int maxNum;
	/** 权重 */
	@ExcelColumn(index = 5)
	public int weight;
	/** 货币类型 */
	@ExcelColumn(index = 6)
	public int coinType;
	/** 单价 */
	@ExcelColumn(index = 7)
	public int price;
	/** 可见最小等级 */
	@ExcelColumn(index = 8)
	public int minVipLv;
	/** 可见最大等级 */
	@ExcelColumn(index = 9)
	public int maxVipLv;
}
