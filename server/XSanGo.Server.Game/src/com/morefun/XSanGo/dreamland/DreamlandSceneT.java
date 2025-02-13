/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandSceneT
 * 功能描述：
 * 文件名：DreamlandSceneT.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 关卡配置
 * 
 * @author weiyi.zhao
 * @since 2016-4-19
 * @version 1.0
 */
@ExcelTable(fileName = "script/副本和怪物/南华幻境.xls", beginRow = 2, sheetName = "关卡脚本")
public class DreamlandSceneT {

	/** 关卡编号 */
	@ExcelColumn(index = 0)
	public int sceneId;

	/** 关卡类型 1战斗关卡 2宝箱关卡 */
	// @ExcelColumn(index = 1)
	// public byte sceneType;

	/** 关卡集 */
	@ExcelColumn(index = 1)
	public int sceneGroupId;

	/** 前一个子集 */
	@ExcelColumn(index = 3)
	public int preSceneId;

	/** 下一个子集关卡 */
	@ExcelColumn(index = 4)
	public int postSceneId;

	/** 三星宝箱 《=3星 */
	@ExcelColumn(index = 8)
	public String chestTC3;

	/** 四星宝箱 */
	@ExcelColumn(index = 9)
	public String chestTC4;

	/** 五星宝箱 */
	@ExcelColumn(index = 10)
	public String chestTC5;

	/** 前一集 */
	public int preGroup;
	
	/** 下一集 */
	public int postGroup;
}
