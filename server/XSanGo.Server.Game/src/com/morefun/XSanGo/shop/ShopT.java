package com.morefun.XSanGo.shop;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/道具相关/商城脚本.xls", beginRow = 2, sheetName = "商城道具列表")
public class ShopT {
	@ExcelColumn(index = 0)
	public String id;
	@ExcelColumn(index = 1)
	public String templateId;// 道具ID
	@ExcelColumn(index = 2)
	public String name;
	@ExcelColumn(index = 3)
	public String remark;// 描述
	@ExcelColumn(index = 4)
	public int isShow;// 是否显示 0-不显示 1-显示
	@ExcelColumn(index = 6)
	public int num;
	@ExcelColumn(index = 7)
	public int price;// 原价
	@ExcelColumn(index = 10)
	public int discountPrice;// 折扣价
	@ExcelColumn(index = 8)
	public String startTime;
	@ExcelColumn(index = 9)
	public String endTime;
	@ExcelColumn(index = 11)
	public int buyTimes;// 可购买次数
	@ExcelColumn(index = 14)
	public int buyVipLevel;// 购买需要vip等级
	@ExcelColumn(index = 12)
	public int isUseOut;// 购买完是否消失 0-否 1-是
	@ExcelColumn(index = 13)
	public int buyLevel;// 购买需要主公等级
	@ExcelColumn(index = 15)
	public int tag;// 角标 0-限时1-热卖2-新品3-等级礼包
	@ExcelColumn(index = 16)
	public String tips;// 购买提示
	@ExcelColumn(index = 17)
	public String icon;// 不为空就使用
	@ExcelColumn(index = 18)
	public int isShowCountDown;// 是否显示倒计时 0-否 1-是
	@ExcelColumn(index = 19)
	public int discountIcon;// 折扣图标-1-不显示折扣图标0-免费1-1折2-2折……
	
}
