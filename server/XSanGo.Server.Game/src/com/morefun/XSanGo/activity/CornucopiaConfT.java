package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 聚宝盆配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/活动和礼包/聚宝盆.xls", sheetName = "配置参数", beginRow = 2)
public class CornucopiaConfT {
	@ExcelColumn(index = 0)
	public int vipLevel;

	/** 可以持续领取的天数 */
	@ExcelColumn(index = 1)
	public int receiveDays;

	@ExcelColumn(index = 2)
	public String superItem1;

	@ExcelColumn(index = 3)
	public int superNum1;

	@ExcelColumn(index = 4)
	public String superItem2;

	@ExcelColumn(index = 5)
	public int superNum2;

	@ExcelColumn(index = 6)
	public String superItem3;

	@ExcelColumn(index = 7)
	public int superNum3;

	@ExcelColumn(index = 8)
	public String superItem4;

	@ExcelColumn(index = 9)
	public int superNum4;
	
	/**一键购买折扣*/
	@ExcelColumn(index = 10)
	public int discount;

	public List<Property> superItems = new ArrayList<Property>();
}
