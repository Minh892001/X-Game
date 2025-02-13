package com.morefun.XSanGo.faction;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会副本配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/副本和怪物/公会副本.xls", sheetName = "参数配置")
public class FactionCopyConfT {
	@ExcelColumn(index = 0)
	public int challengeNum;

	@ExcelColumn(index = 1)
	public String resetTime;// 挑战次数重置时间

	@ExcelColumn(index = 2, dataType = DataFormat.OnlyTime)
	public Date startTime;// 挑战副本开启时间

	@ExcelColumn(index = 3, dataType = DataFormat.OnlyTime)
	public Date endTime;// 挑战副本结束时间

	@ExcelColumn(index = 5)
	public int vipLevel; // 需要VIP等级

	@ExcelColumn(index = 6)
	public int factionLevel;

	@ExcelColumn(index = 7)
	public int openAuth;// 开启权限 0-所有人 1-会长 2-会长和长老

	@ExcelColumn(index = 8)
	public int challengeTime;// 挑战时长 秒
	
	@ExcelColumn(index = 9)
	public int maxGold;// 挑战最多奖励金币
	
	@ExcelColumn(index = 10)
	public float scale;// 伤害金币比例
	
	@ExcelColumn(index = 11)
	public int openCopyNum;//每天副本可开启次数

	@ExcelColumn(index = 12)
	public int quitMinute;//副本挑战异常退出等待时间
}
