package com.morefun.XSanGo.makewine;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.IntString;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 积分排名奖励
 * @author zhuzhi.yang
 *
 */
@ExcelTable(fileName = "script/活动和礼包/煮酒论英雄.xls", sheetName = "积分排名奖励", beginRow = 2)
public class MakeWineScoreRankAwardT {

	@ExcelColumn(index = 0)
	public int id;
	
	@ExcelColumn(index = 1)
	public int startRank;
	
	@ExcelColumn(index = 2)
	public int stopRank;
	
	/**1：达到该积分段后才能领取对应排名奖励
	   2：如过不填，则不生效，界面列表也不显示*/
	@ExcelColumn(index = 3)
	public int needScore;
	
	@ExcelColumn(index = 4)
	public String items;
	public List<IntString> item_array = new ArrayList<IntString>();
}
