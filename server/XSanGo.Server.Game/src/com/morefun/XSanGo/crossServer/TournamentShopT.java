package com.morefun.XSanGo.crossServer;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 比武大会，商城脚本
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/比武大会.xls", beginRow = 2, sheetName = "至尊商城")
public class TournamentShopT {
	/** 类型 */
	@ExcelColumn(index = 0)
	public int type;
	/** 道具ID */
	@ExcelColumn(index = 1)
	public String id;
	/** 货币 */
	@ExcelColumn(index = 2)
	public String coin;
	/** 单价 */
	@ExcelColumn(index = 3)
	public int price;
	/** 限购数量 */
	@ExcelColumn(index = 4)
	public int limit;
	/** 描述信息 */
	@ExcelColumn(index = 5)
	public String desc;
}
