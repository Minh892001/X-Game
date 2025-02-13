/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleKitsT
 * 功能描述：
 * 文件名：FactionBattleKitsT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战战场锦囊数据
 * 
 * @author zwy
 * @since 2016-1-4
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "锦囊表", beginRow = 2)
public class FactionBattleKitsT {

	/** 锦囊编号 */
	@ExcelColumn(index = 0)
	public int kitsId;

	/** 锦囊名称 */
	@ExcelColumn(index = 1)
	public String name;

	/** 图标 */
	@ExcelColumn(index = 2)
	public String icon;

	/** 最大获取数量 */
	@ExcelColumn(index = 3)
	public int maxNum;

	/** CD 单位：秒 */
	@ExcelColumn(index = 4)
	public int cd;

	/** 使用效果 根据实际道具来决定其意义 */
	@ExcelColumn(index = 6)
	public int effect;

	/** 权值 */
	@ExcelColumn(index = 7)
	public int weight;
}
