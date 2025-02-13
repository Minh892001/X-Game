package com.morefun.XSanGo.template;

import java.util.ArrayList;
import java.util.List;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/比武大会.xls", beginRow = 2, sheetName = "跨服区间")
public class CrossRangeT {
	
	@ExcelColumn(index = 0)
	public int id;
	
	/**逗号分割的服务器ID*/
	@ExcelColumn(index = 1)
	public String serverIds;
	
	public List<Integer> serverList = new ArrayList<Integer>();
}
