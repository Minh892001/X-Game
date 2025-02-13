package com.morefun.XSanGo.ladder;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/群雄争霸脚本.xls", beginRow = 2, sheetName = "争霸赛等级")
public class LadderLevelT {
	// 编号
	@ExcelColumn(index = 0)
	public int id;

	// 争霸赛等级
	@ExcelColumn(index = 1)
	public int lv;

	// 等级升级所需的星级
	@ExcelColumn(index = 2)
	public int star;
	
	// 官阶
	@ExcelColumn(index = 3)
	public int name;
	
	// 道具ID1
	@ExcelColumn(index = 4)
	public String item1;
	
	// 奖励1 数量
	@ExcelColumn(index = 5)
	public int Salary1;
	
	// 道具ID2
	@ExcelColumn(index = 6)
	public String item2;
	
	// 奖励2 数量
	@ExcelColumn(index = 7)
	public int Salary2;
	
	//宝箱图片
	@ExcelColumn(index = 8)
	public int icon;
	
	@ExcelColumn(index = 9)
	public String $S_str;

	@ExcelColumn(index = 10)
	public String $s_str_m;

	@ExcelColumn(index = 11)
	public String $s_str_w;

	@ExcelColumn(index = 12)
	public String $C_str_m;

	@ExcelColumn(index = 13)
	public String $C_str_w;

	@ExcelColumn(index = 14)
	public String $c_str_m;

	@ExcelColumn(index = 15)
	public String $c_str_w;

	@ExcelColumn(index = 16)
	public String $R_str;

	@ExcelColumn(index = 17)
	public String $r_str_m;

	@ExcelColumn(index = 18)
	public String $r_str_w;
}
