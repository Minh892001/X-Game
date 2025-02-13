/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: StrongholdStateT
 * 功能描述：
 * 文件名：StrongholdStateT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * 据点状态
 * 
 * @author zwy
 * @since 2016-1-5
 * @version 1.0
 */
public class StrongholdStateT {

	/** 状态名称 */
	@ExcelColumn(index = 0)
	public String stateName;

	/** 状态人数范围 */
	@ExcelColumn(index = 1)
	public String stateRoleNum;

	/** 挖宝收益 */
	@ExcelColumn(index = 2)
	public int diggingTreasureIncome;
}
