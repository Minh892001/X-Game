package com.morefun.XSanGo.onlineAward;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/在线时长礼包.xls", beginRow = 2, sheetName = "在线时长礼包脚本")
public class OnlineAwardT {
	//礼包ID
	@ExcelColumn(index = 0)
	public int GiftId;
	
	//礼包名称
	@ExcelColumn(index = 1)
	public String GiftName;
	
	//需要等级
	@ExcelColumn(index = 2)
	public int ReqLvl;
	
	//所属渠道
	@ExcelColumn(index = 3)
	public int ChannelId;
	
	//在线时长
	@ExcelColumn(index = 4)
	public int ReqTime;
	
	//礼包TC
	@ExcelColumn(index = 5)
	public String GiftTC;
}
