/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 升级礼包
 */
@ExcelTable(fileName = "script/活动和礼包/升级奖励.xls", sheetName = "升级奖励脚本", beginRow = 1)
public class UpGiftT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 2)
	public int level;

	@ExcelColumn(index = 3)
	public int yuanbao;

	@ExcelColumn(index = 4)
	public int vip;

	@ExcelColumn(index = 5)
	public int vipExp;

	@Override
	public String toString() {
		return "UpGiftT [id=" + id + ", level=" + level + ", yuanbao="
				+ yuanbao + ", vip=" + vip + ", vipExp=" + vipExp + "]";
	}
}
