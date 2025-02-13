package com.morefun.XSanGo.robot;

import java.util.Random;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/副本和怪物/机器人配置.xls", sheetName = "群雄争霸AI", beginRow = 2)
public class LadderRobotT {
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

	// 等级
	@ExcelColumn(index = 4)
	public String level;

	// 星级
	@ExcelColumn(index = 5)
	public String star;

	// 进阶
	@ExcelColumn(index = 6)
	public String advance;

	// 装备 头部
	@ExcelColumn(index = 7)
	public String equipHead;

	// 装备 头部 星级
	@ExcelColumn(index = 8)
	public String equipHeadStar;

	// 装备 头部 等级
	@ExcelColumn(index = 9)
	public int equipHeadLevel;

	public int[] levelArray;
	public int[] starLevelArray;
	public int[] qLevelArray;

	public void parseAll() {
		String[] lvs = level.split(",");
		levelArray = new int[lvs.length];
		for (int i = 0; i < lvs.length; i++) {
			levelArray[i] = Integer.parseInt(lvs[i]);
		}

		lvs = star.split(",");
		starLevelArray = new int[lvs.length];
		for (int i = 0; i < lvs.length; i++) {
			starLevelArray[i] = Integer.parseInt(lvs[i]);
		}

		lvs = advance.split(",");
		qLevelArray = new int[lvs.length];
		for (int i = 0; i < lvs.length; i++) {
			qLevelArray[i] = Integer.parseInt(lvs[i]);
		}
	}

	public int randomLevel() {
		Random r = new Random();
		return levelArray[r.nextInt(levelArray.length)];
	}

	public int randomStarLevel() {
		Random r = new Random();
		return starLevelArray[r.nextInt(starLevelArray.length)];
	}

	public int randomQLevel() {
		Random r = new Random();
		return qLevelArray[r.nextInt(qLevelArray.length)];
	}

}
