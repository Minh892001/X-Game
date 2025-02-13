package com.morefun.XSanGo.task;

import java.util.HashMap;
import java.util.Map;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/任务脚本/任务脚本.xls", beginRow = 2)
public class StarAwardTemplT {
	@ExcelColumn(index = 0)
	public int starNum;

	@ExcelColumn(index = 1)
	public String reawad;
	
	@ExcelColumn(index = 2)
	public String icon;
	
	public Map<String,Integer> award = new HashMap<String,Integer>();
}
