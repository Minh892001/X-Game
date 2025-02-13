/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: ApiRewardT
 * 功能描述：
 * 文件名：ApiRewardT.java
 **************************************************
 */
package com.morefun.XSanGo.api;

import java.util.HashMap;
import java.util.Map;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.TextUtil;

/**
 * api奖励表
 * 
 * @author zhangwei02.zhang
 * @since 2015年11月11日
 * @version 1.0
 */
@ExcelTable(fileName = "script/活动和礼包/活动列表.xls", sheetName = "奖励配置", beginRow = 2)
public class ApiRewardT {

	/** 奖励编号 */
	@ExcelColumn(index = 0)
	public int rewardID;

	/** 对应api编号 */
	@ExcelColumn(index = 1)
	public int apiId;

	/** 奖励物品，编号1:数量1;编号2:数量2;... */
	@ExcelColumn(index = 2)
	public String rewardItem;

	/** 需要达成的数量 */
	@ExcelColumn(index = 3)
	public int targetCount;

	/** 说明 */
	@ExcelColumn(index = 4)
	public String desc;

	/** 奖励物品 */
	private Map<String, Integer> itemCountMap = new HashMap<String, Integer>();

	/**
	 * 奖励数据解析
	 */
	public void parseItems() {
		String[] items = rewardItem.split(",");
		for (String item : items) {
			if (TextUtil.isBlank(item)) {
				continue;
			}
			String[] tmp = item.split(":");
			itemCountMap.put(tmp[0], Integer.parseInt(tmp[1]));
		}
	}

	/**
	 * 返回物品奖励
	 * 
	 * @return
	 */
	public Map<String, Integer> getItemCountMap() {
		return itemCountMap;
	}
}
