package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2, sheetName = "竞技场匹配")
public class ArenaRankMatchT {
	//次数
	@ExcelColumn(index = 0)
	public int id;
	
	//排名开始
	@ExcelColumn(index = 1)
	public int start;
	
	//排名结束
	@ExcelColumn(index = 2)
	public int end;
	
	//匹配区域1起始
	@ExcelColumn(index = 3)
	public int oneStart;
	
	//匹配区域1截止
	@ExcelColumn(index = 4)
	public int oneEnd;
	
	//匹配区域2起始
	@ExcelColumn(index = 5)
	public int twoStart;
	
	//匹配区域1截止
	@ExcelColumn(index = 6)
	public int twoEnd;
	
	//匹配区域3起始
	@ExcelColumn(index = 7)
	public int threeStart;
	
	//匹配区域1截止
	@ExcelColumn(index = 8)
	public int threeEnd;
	
}
