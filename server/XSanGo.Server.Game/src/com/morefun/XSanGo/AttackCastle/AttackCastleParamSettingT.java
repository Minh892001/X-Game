package com.morefun.XSanGo.AttackCastle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 参数设置
 * 
 * @author qinguofeng
 * @date Jan 27, 2015
 */
@ExcelTable(fileName = "script/副本和怪物/北伐脚本.xls", beginRow = 2, sheetName = "参数设置")
public class AttackCastleParamSettingT {
	/** 挑战次数 */
	@ExcelColumn(index = 0)
	public int attackNum;
	/** 重置时间 */
	@ExcelColumn(index = 1)
	public String resetTime;
	/** 每日次数刷新时间 */
	@ExcelColumn(index = 2)
	public String resetChanceTime;
	/** 额外次数， 下面的Vip等级以上增加额外次数，总的能挑战次数等于挑战次数加上额外次数 */
	@ExcelColumn(index = 3)
	public int extraNum;
	/** 额外次数，所需VIP等级 */
	@ExcelColumn(index = 4)
	public int vipLv;
	/** 最低开放等级 */
	@ExcelColumn(index = 6)
	public int minLv;
	/** 商城刷新出的商品数量 */
	@ExcelColumn(index = 9)
	public int goodsNum;
	/** 扫荡所需Vip等级 */
	@ExcelColumn(index = 10)
	public int minClearVipLevel;
	/** 扫荡所需剩余次数 */
	@ExcelColumn(index = 11)
	public int maxClearLastTime;
	/** 开启扫荡所在关卡 */
	@ExcelColumn(index = 12)
	public int startClearLevel;
	/** 刷新需要的元宝数量 */
	@ExcelColumn(index = 13)
	public int refreshPrice;
	/** 新手保护等级 */
	@ExcelColumn(index = 14)
	public int protectLevel;
	/** 首次掉落概率 */
	@ExcelColumn(index = 15)
	public String firstLoot;
	/** 正常掉落概率 */
	@ExcelColumn(index = 16)
	public String normalLoot;
	
	public int minFirstLoot() {
		return Integer.parseInt(firstLoot.split(":")[0]);
	}
	
	public int maxFirstLoot() {
		return Integer.parseInt(firstLoot.split(":")[1]);
	}
	
	public int minNormalLoot() {
		return Integer.parseInt(normalLoot.split(":")[0]);
	}
	
	public int maxNormalLoot() {
		return Integer.parseInt(normalLoot.split(":")[1]);
	}
}
