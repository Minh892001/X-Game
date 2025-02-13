/**
 * 
 */
package com.morefun.XSanGo.achieve;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 成就列表
 */
@ExcelTable(fileName = "script/成就脚本/成就脚本.xls", sheetName = "成就列表", beginRow = 2)
public class AchieveT {
	/**成就编号 */
	@ExcelColumn(index = 0)
	public int id;
	
	/**显示序号 */
	@ExcelColumn(index = 1)
	public int order;

	/**功能ID */
	@ExcelColumn(index = 2)
	public int functionId;
	
	/**名称 */
	@ExcelColumn(index = 3)
	public String title;
	
	/**描述内容 */
	@ExcelColumn(index = 4)
	public String content1;
	
	/**前置ID */
	@ExcelColumn(index = 6)
	public int frontId;
	
	/**后置ID */
	@ExcelColumn(index = 7)
	public int nextId;
	
	/**成就类型*/
	@ExcelColumn(index = 8)
	public String type;
	
	/**完成条件*/
	@ExcelColumn(index = 9)
	public String condition	;
	
	/**最大数量 */
	@ExcelColumn(index = 10)
	public int maxNum;
	
	/**奖励元宝 */
	@ExcelColumn(index = 12)
	public int rmby;
	
	/**奖励金币 */
	@ExcelColumn(index = 13)
	public int gold;
	
	/**道具ID */
	@ExcelColumn(index = 14)
	public String item;
	
	/**道具数量 */
	@ExcelColumn(index = 15)
	public int itemNum;
	
	/**是否有效 */
	@ExcelColumn(index = 16)
	public int isOpen;

	/**需求等级 */
	@ExcelColumn(index = 17)
	public int needLvl;
	
	/**是否公告 */
	@ExcelColumn(index = 19)
	public int isNotice;
}
