package com.morefun.XSanGo.luckybag;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/充值福袋.xls", beginRow = 2)
public class LuckyBagItemT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int value;

	@ExcelColumn(index = 2)
	public String item1;

	@ExcelColumn(index = 3)
	public int num1;

	@ExcelColumn(index = 4)
	public String item2;

	@ExcelColumn(index = 5)
	public int num2;

	@ExcelColumn(index = 6)
	public String item3;

	@ExcelColumn(index = 7)
	public int num3;

	@ExcelColumn(index = 8)
	public String item4;

	@ExcelColumn(index = 9)
	public int num4;

	@ExcelColumn(index = 10)
	public String item5;

	@ExcelColumn(index = 11)
	public int num5;

	public List<Property> items = new ArrayList<Property>();
}
