/**
 * 
 */
package com.morefun.XSanGo.role;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.IntIntPair;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/参数配置脚本/游戏参数配置表.xls", sheetName = "等级上限开放配置", beginRow = 2)
public class LevelConfigT {
	@ExcelColumn(index = 0)
	public int levelLimit;

	@ExcelColumn(index = 1)
	public int levelLimitWhenNewServer;

	@ExcelColumn(index = 2)
	@Deprecated
	public int autoLevelUpLimit;

	@ExcelColumn(index = 3)
	public String openConfig;

	private IntIntPair[] dailyOpens;

	/**
	 * 获取开服时间控制数据
	 * 
	 * @return
	 */
	public IntIntPair[] getDailyOpens() {
		if (this.dailyOpens == null) {
			List<IntIntPair> list = new ArrayList<IntIntPair>();
			String[] configs = this.openConfig.split(",");
			for (String data : configs) {
				String[] subArray = data.split("-");
				list.add(new IntIntPair(NumberUtil.parseInt(subArray[0]),
						NumberUtil.parseInt(subArray[1])));
			}

			this.dailyOpens = list.toArray(new IntIntPair[0]);
		}

		return this.dailyOpens;
	}

}
