package com.morefun.XSanGo.robot;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/副本和怪物/机器人配置.xls", beginRow = 2)
public class RobotFormationT {
	// id
	@ExcelColumn(index = 0)
	public int id;

	// 位置
	@ExcelColumn(index = 1)
	public int pos;

	// 武将ID
	@ExcelColumn(index = 2)
	public int hId;

	// 武将名字
	@ExcelColumn(index = 3)
	public String name;

	// 等级倍数
//	@ExcelColumn(index = 4)
//	public int lvD;

	// 星级
//	@ExcelColumn(index = 5)
//	public String star;

	// 进阶
//	@ExcelColumn(index = 6)
//	public String QLevel;

	// 装备 头部
	@ExcelColumn(index = 7)
	public String equipHead;

	// 装备 头部 星级
	@ExcelColumn(index = 8)
	public String equipHeadStar;

	// 装备 手上
	@ExcelColumn(index = 9)
	public String equipHand;

	// 装备 手上 星级
	@ExcelColumn(index = 10)
	public String equipHandStar;

	// 装备 身体
	@ExcelColumn(index = 11)
	public String equipBody;

	// 装备 身体 星级
	@ExcelColumn(index = 12)
	public String equipBodyStar;

	// 装备 靴子
	@ExcelColumn(index = 13)
	public String equipFoot;

	// 装备 靴子 星级
	@ExcelColumn(index = 14)
	public String equipFootStar;

	// 装备 饰品
	@ExcelColumn(index = 15)
	public String equipJewelry;

	// 装备 饰品 星级
	@ExcelColumn(index = 16)
	public String equipJewelryStar;

	// 装备 坐骑
	@ExcelColumn(index = 17)
	public String equipMount;

	// 装备 坐骑 星级
	@ExcelColumn(index = 18)
	public String equipMountStar;

	@ExcelComponet(index = 7, columnCount = 2, size = 6)
	public EquipConfig[] equips;
}
