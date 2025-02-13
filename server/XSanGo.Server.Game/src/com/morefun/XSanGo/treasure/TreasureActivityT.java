package com.morefun.XSanGo.treasure;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 寻宝活动奖励
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/秘境寻宝.xls", sheetName = "活动产出", beginRow = 2)
public class TreasureActivityT {
	/**队伍编号*/
	@ExcelColumn(index = 0)
	public int groupNum;

	@ExcelColumn(index = 1)
	public String tc;

	@ExcelColumn(index = 2, dataType = DataFormat.DateTime)
	public Date startDate;

	@ExcelColumn(index = 3, dataType = DataFormat.DateTime)
	public Date endDate;
}
