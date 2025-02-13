package com.morefun.XSanGo.activity;

import java.util.Map;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author zhouming
 */
@ExcelTable(fileName = "script/活动和礼包/百步穿杨.xls", sheetName = "积分排名奖励", beginRow = 2)
public class ShootScoreRankT {

	/**
	 * 编号
	 */
	@ExcelColumn(index = 0)
	public int id;

	/**
	 * 起始
	 */
	@ExcelColumn(index = 1)
	public int startRank;

	/**
	 * 截止
	 */
	@ExcelColumn(index = 2)
	public int stopRank;

	/**
	 * 物品
	 */
	@ExcelColumn(index = 3)
	public String items;

	/**
	 * 分解后的奖励列表
	 */
	public Map<String, Integer> rewardMap;
}
