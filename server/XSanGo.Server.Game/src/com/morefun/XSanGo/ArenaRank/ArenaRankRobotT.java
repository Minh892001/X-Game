package com.morefun.XSanGo.ArenaRank;

import java.util.Random;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 竞技场机器人配置
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2, sheetName = "机器人匹配区间")
public class ArenaRankRobotT {
	/** ID */
	@ExcelColumn(index = 0)
	public int id;
	/** 开始排名 */
	@ExcelColumn(index = 1)
	public int startRank;
	/** 结束排名 */
	@ExcelColumn(index = 2)
	public int endRank;
	/** 等级,逗号分割 */
	@ExcelColumn(index = 3)
	public String level;
	/** 星级,逗号分割 */
	@ExcelColumn(index = 4)
	public String starLevel;
	/** 进阶,逗号分割 */
	@ExcelColumn(index = 5)
	public String qLevel;
	/** 主公等级 */
	@ExcelColumn(index = 6)
	public String roleLevel;

	public int[] levelArray;
	public int[] starLevelArray;
	public int[] qLevelArray;
	public int[] roleLevelArray;

	public void parseLevelArray() {
		String[] lvs = level.split(",");
		levelArray = new int[lvs.length];
		for (int i = 0; i < lvs.length; i++) {
			levelArray[i] = Integer.parseInt(lvs[i]);
		}
	}

	public void parseStarLevelArray() {
		String[] slvs = starLevel.split(",");
		starLevelArray = new int[slvs.length];
		for (int i = 0; i < slvs.length; i++) {
			starLevelArray[i] = Integer.parseInt(slvs[i]);
		}
	}

	public void parseQLevelArray() {
		String[] qlvs = qLevel.split(",");
		qLevelArray = new int[qlvs.length];
		for (int i = 0; i < qlvs.length; i++) {
			qLevelArray[i] = Integer.parseInt(qlvs[i]);
		}
	}

	public void parseRoleLevelArray() {
		String[] rlvs = roleLevel.split(",");
		roleLevelArray = new int[rlvs.length];
		for (int i = 0; i < rlvs.length; i++) {
			roleLevelArray[i] = Integer.parseInt(rlvs[i]);
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

	public int randomRoleLevel() {
		Random r = new Random();
		return roleLevelArray[r.nextInt(roleLevelArray.length)];
	}
}
