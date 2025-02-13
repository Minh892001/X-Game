package com.morefun.XSanGo.heroAdmire;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/名将仰慕.xls", beginRow = 2, sheetName = "好感度物品")
public class HeroAdmireItemT {
	// ID
	@ExcelColumn(index = 0)
	public int id;
	
	// 分类
	@ExcelColumn(index = 1)
	public int type;

	// 物品ID
	@ExcelColumn(index = 2)
	public String itemId;

	// 物品名
	@ExcelColumn(index = 3)
	public String name;

	// 数量
	@ExcelColumn(index = 4)
	public int num;
	
	// 增加好感度
	@ExcelColumn(index = 5)
	public int niceValue;
	
	/** 是否有效,出现在刷新列表中，1:有效，0：无效*/
	@ExcelColumn(index = 6)
	public int isShow;

	@ExcelColumn(index = 7)
	public int pro; // 权值
}
