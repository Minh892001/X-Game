package com.morefun.XSanGo.worldboss;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 世界BOSS活动配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/副本和怪物/乱世魔王脚本.xls", sheetName = "活动奖励", beginRow = 2)
public class WorldBossActivityT {

	@ExcelColumn(index = 0, dataType = DataFormat.DateTime)
	public Date beginDate;

	@ExcelColumn(index = 1, dataType = DataFormat.DateTime)
	public Date endDate;

	@ExcelColumn(index = 2)
	public String itemId;

	@ExcelColumn(index = 3)
	public int itemNum;
}
