package com.morefun.XSanGo.activity;

import java.util.Map;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author zhouming
 */
@ExcelTable(fileName = "script/活动和礼包/百步穿杨.xls", sheetName = "积分段奖励", beginRow = 2)
public class ShootScoreRewardT {

	/**
	 * 积分数量
	 */
	@ExcelColumn(index = 0)
	public int score;

	/**
	 * 物品
	 */
	@ExcelColumn(index = 1)
	public String items;

	/**
	 * 分解后的物品列表
	 */
	public Map<String, Integer> itemsMap;
	
	/**
	 * 物品图标
	 */
	@ExcelColumn(index = 2)
	public String icons;
}
