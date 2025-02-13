/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 副本随机PK（热身赛）配置模板
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/互动和聊天/副本PK脚本.xls", sheetName = "奖励列表", beginRow = 2)
public class WarmupT {
	@ExcelColumn(index = 0)
	public int minLevel;

	@ExcelColumn(index = 1)
	public int maxLevel;

	@ExcelColumn(index = 2)
	public String tc;

	@ExcelColumn(index = 4)
	public int maxCountOneDay;

	@ExcelColumn(index = 5)
	public int rate;

	@ExcelColumn(index = 6)
	public String powers;
	private int[] powerArray;

	@ExcelColumn(index = 7)
	public String levels;
	private int[] levelArray;

	public int getMinPowerRate() {
		parsePowerArray();
		return this.powerArray[0];
	}

	public int getMaxPowerRate() {
		parsePowerArray();
		return this.powerArray[1];
	}

	/**
	 * 解析战力上下限数据
	 */
	private void parsePowerArray() {
		if (this.powerArray == null) {
			this.powerArray = TextUtil.GSON.fromJson(this.powers, int[].class);
		}
	}

	public int getMinLevel() {
		parseLevelArray();
		return this.levelArray[0];
	}

	/**
	 * 解析等级上下限数据
	 */
	private void parseLevelArray() {
		if (this.levelArray == null) {
			this.levelArray = TextUtil.GSON.fromJson(this.levels, int[].class);
		}
	}

	public int getMaxLevel() {
		parseLevelArray();
		return this.levelArray[1];
	}

}
