package com.morefun.XSanGo.worldboss;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 世界BOSS基础配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/副本和怪物/乱世魔王脚本.xls", sheetName = "参数设置", beginRow = 2)
public class WorldBossConfT {

	/** 开启星期 */
	@ExcelColumn(index = 0)
	public String openWorkDay;

	@ExcelColumn(index = 1, dataType = DataFormat.OnlyTime)
	public Date beginTime;

	@ExcelColumn(index = 2, dataType = DataFormat.OnlyTime)
	public Date endTime;

	@ExcelColumn(index = 3)
	public int openLevel;

	@ExcelColumn(index = 4)
	public int cdYuanbao;

	/** 鼓舞购买元宝 */
	@ExcelColumn(index = 6)
	public int inspireYuanbao;

	/** 鼓舞加成类型 */
	@ExcelColumn(index = 7)
	public int inspireType;

	/** 鼓舞加成值 */
	@ExcelColumn(index = 8)
	public int inspireValue;

	/** 玩家系数，用来生成boss血量 */
	@ExcelColumn(index = 9)
	public int factor;

	/** 人数小于minPeopleNum时的默认人数 */
	@ExcelColumn(index = 10)
	public int defaultPeopleNum;

	/** 最小参战人数 */
	@ExcelColumn(index = 11)
	public int minPeopleNum;

	/** 单次挑战总血量最大百分比伤害 */
	@ExcelColumn(index = 12)
	public int maxHarm;

	/** 可进入世界BOSS界面时间 */
	@ExcelColumn(index = 14, dataType = DataFormat.OnlyTime)
	public Date enterbleDate;

	/** 返还元宝折扣 */
	@ExcelColumn(index = 15)
	public double returnSale;

	/** 每天可使用托管次数 */
	@ExcelColumn(index = 16)
	public int trustNum;
}
