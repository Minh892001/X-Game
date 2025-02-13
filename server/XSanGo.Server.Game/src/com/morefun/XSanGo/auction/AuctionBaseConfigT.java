package com.morefun.XSanGo.auction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 拍卖行基本参数配置
 * 
 * @author qinguofeng
 * @date Mar 27, 2015
 */
@ExcelTable(fileName = "script/互动和聊天/拍卖行脚本.xls", beginRow = 2, sheetName = "基本参数")
public class AuctionBaseConfigT {
	/** 开启等级 */
	@ExcelColumn(index = 0)
	public int level;

	/** 拍卖所需vip */
	@ExcelColumn(index = 1)
	public int vipLevel;

	/** 商品显示数量 */
	@ExcelColumn(index = 2)
	public int showNum;

	/** 拍卖持续时间 */
	@ExcelColumn(index = 3)
	public int auctionTime;

	/** 托管费 */
	@ExcelColumn(index = 4)
	public int trusteeFee;

	/** 手续费 */
	@ExcelColumn(index = 5)
	public int handingCharge;

	/** 一口价最低比例 */
	@ExcelColumn(index = 6)
	public int fixedPrice;

	/** 竞拍加成, 每次叫价的涨幅, 起拍价的百分比 */
	@ExcelColumn(index = 7)
	public int auctionAddition;

	/** 货币兑换比例 */
	@ExcelColumn(index = 8)
	public int exchangeRate;

	/** 元宝单次最大兑换数量 */
	@ExcelColumn(index = 9)
	public int maxExchangeNum;

	/** 最小拍卖金额 */
	@ExcelColumn(index = 10)
	public int minSellPrice;

	/** 拍卖行系统总的拍卖数量限制 */
	@ExcelColumn(index = 11)
	public int maxAuctionItemCount;

	/** 拍卖行最高价格限制 */
	@ExcelColumn(index = 12)
	public int maxPrice;

	/** 商城刷新时间 */
	@ExcelColumn(index = 13)
	public String refreshTime;

	/** 固定物品数量 */
	@ExcelColumn(index = 14)
	public int fixedNum;

	/** 随机物品数量 */
	@ExcelColumn(index = 15)
	public int randomNum;
}
