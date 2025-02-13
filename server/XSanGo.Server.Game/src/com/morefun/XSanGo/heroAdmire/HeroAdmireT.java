package com.morefun.XSanGo.heroAdmire;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/名将仰慕.xls", beginRow = 2, sheetName = "仰慕武将列表")
public class HeroAdmireT {
	// id
	@ExcelColumn(index = 0)
	public int id;

	// 武将名称
	@ExcelColumn(index = 1)
	public String name;

	// 武将星级
	@ExcelColumn(index = 2)
	public int Star;

	// 召唤好感度
	@ExcelColumn(index = 3)
	public int NiceMax;
	
	// 初始赠送
	@ExcelColumn(index = 4)
	public int initialNice;
	
	/** 是否有效,出现在刷新列表中，1:有效，0：无效*/
	@ExcelColumn(index = 5)
	public int isShow;
}
