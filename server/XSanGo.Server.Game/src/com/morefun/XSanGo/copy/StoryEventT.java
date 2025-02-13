/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 剧情事件
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/副本和怪物/关卡脚本.xls", sheetName = "特殊事件", beginRow = 2)
public class StoryEventT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int eventType;

	@ExcelColumn(index = 5)
	public String firstId;

	@ExcelColumn(index = 6)
	public int monsterId;

	public int getFirstMonsterId() {
		return Integer.parseInt(firstId.substring(2));
	}

	public int getFirstHeroId() {
		return Integer.parseInt(firstId);
	}
}
