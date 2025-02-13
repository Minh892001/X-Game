package com.morefun.XSanGo.friendsRecall;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * 老友召回任务数据
 * 
 * @author zhangwei02.zhang
 * @since 2015年10月29日
 * @version 1.0
 */
public class FriendsRecallBaseT {
	/** 任务编号 */
	@ExcelColumn(index = 0)
	public int taskId;

	/** 显示序号 */
	@ExcelColumn(index = 1)
	public int showOrder;

	/** Type */
	@ExcelColumn(index = 2)
	public int taskType;

	/** 标题 */
	@ExcelColumn(index = 3)
	public String title;

	/** 内容描述 */
	@ExcelColumn(index = 4)
	public String content;

	/** 前置任务 */
	@ExcelColumn(index = 5)
	public int prefixTask;

	/** 后置任务 */
	public int suffixTask;

	/** 图标 */
	@ExcelColumn(index = 6)
	public String icon;

	/** 主公经验 */
	@ExcelColumn(index = 7)
	public int rewardExp;

	/** 奖励金币 */
	@ExcelColumn(index = 8)
	public int rewardGold;

	/** 奖励道具 */
	@ExcelColumn(index = 9)
	public String rewardItem;

	/** 奖励数量 */
	@ExcelColumn(index = 10)
	public int rewardItemCount;

	/** 任务目标 */
	@ExcelColumn(index = 11)
	public String target;

	/** 完成条件 */
	@ExcelColumn(index = 12)
	public int demand;

	/** 最大数量 */
	@ExcelColumn(index = 13)
	public int maxCount;

	/** 开始时间 */
	@ExcelColumn(index = 14)
	public String startTime;

	/** 结束时间 */
	@ExcelColumn(index = 15)
	public String endTime;
	
	/** 结尾任务 */
	@ExcelColumn(index = 16)
	public int endTask;
}
