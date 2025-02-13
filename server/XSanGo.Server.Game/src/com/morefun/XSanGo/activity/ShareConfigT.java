/**
 * 
 */
package com.morefun.XSanGo.activity;

import java.util.HashMap;
import java.util.Map;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 热加载配置
 */
@ExcelTable(fileName = "script/活动和礼包/分享脚本.xls", sheetName = "热加载配置", beginRow = 2)
public class ShareConfigT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int taskId;

	@ExcelColumn(index = 2)
	public String resetTaskTitle;

	@ExcelColumn(index = 3)
	public String resetTaskContent;
	
	@ExcelColumn(index = 4)
	public int order;
	
	@ExcelColumn(index = 5)
	public String bannerTitle;
	
	@ExcelColumn(index = 6)
	public int bannerType;
	
	@ExcelColumn(index = 7)
	public String shareContent;
	
	@ExcelColumn(index = 8)
	public String shareImg;
	
	@ExcelColumn(index = 9)
	public String shareIcon;
	
	@ExcelColumn(index = 10)
	public String shareTitle;
	
	@ExcelColumn(index = 11)
	public String shareLink;
	
	@ExcelColumn(index = 12)
	public String item1;
	
	@ExcelColumn(index = 13)
	public int item1Num;
	
	@ExcelColumn(index = 14)
	public String item2;
	
	@ExcelColumn(index = 15)
	public int item2Num;
	
	@ExcelColumn(index = 16)
	public String item3;
	
	@ExcelColumn(index = 17)
	public int item3Num;
	
	@ExcelColumn(index = 18)
	public int groupId;
	
	@ExcelColumn(index = 19)
	public int isValid;
	
	@ExcelColumn(index = 20)
	public String openTime;
	
	@ExcelColumn(index = 21)
	public String closeTime;
	
	public Map<String,Integer> itemMap = new HashMap<String,Integer>();
}
