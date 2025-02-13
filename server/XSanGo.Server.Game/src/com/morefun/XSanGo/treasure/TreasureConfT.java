package com.morefun.XSanGo.treasure;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 寻宝参数配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/秘境寻宝.xls", sheetName = "参数配置", beginRow = 2)
public class TreasureConfT {
	@ExcelColumn(index = 0)
	public int openLevel;

	@ExcelColumn(index = 1)
	public int groupNum;

	/** 寻宝时长 分钟 */
	@ExcelColumn(index = 2)
	public int duration;

	/** 额外奖励加成比例 */
	@ExcelColumn(index = 3)
	public int additional;

	/** 推荐武将刷新时间 */
	@ExcelColumn(index = 4)
	public String refreshDate;
	
	/** 运营活动奖励物品 */
	@ExcelColumn(index = 5)
	public String activityItemId;
}
