package com.morefun.XSanGo.AttackCastle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 关卡设置
 * 
 * @author qinguofeng
 * @date Jan 27, 2015
 */
@ExcelTable(fileName = "script/副本和怪物/北伐脚本.xls", beginRow = 2, sheetName = "关卡设置")
public class AttackCastleNodeSettingT {
	/** 关卡ID */
	@ExcelColumn(index = 0)
	public int nodeId;
	/** 类型,关卡(0)宝箱(1),补给(2) */
	@ExcelColumn(index = 1)
	public int stageType;
	/** 页码 */
	@ExcelColumn(index = 2)
	public int pageId;
	/** 互斥组 */
	@ExcelColumn(index = 3)
	public int groupIndex;
	/** 关卡图片常态 */
	@ExcelColumn(index = 4)
	public String stagePNG;
	/** 关卡图片选中 */
	@ExcelColumn(index = 5)
	public String stagePNG2;
	/** 坐标 */
	@ExcelColumn(index = 6)
	public String location;
	/** 是否开启 */
	@ExcelColumn(index = 7)
	public int isOpen;
	/** 货币类型 */
	@ExcelColumn(index = 8)
	public int coinType;
	/** 基础金币 */
	@ExcelColumn(index = 9)
	public int coinNum;
	/** 道具TC1 */
	@ExcelColumn(index = 10)
	public String rewardTC1;
	/** 道具TC2 */
	@ExcelColumn(index = 11)
	public String rewardTC2;
	/** 道具TC3 */
	@ExcelColumn(index = 12)
	public String rewardTC3;
	/** 备注 */
	@ExcelColumn(index = 13)
	public String note;
	/** 奖励声望 */
	@ExcelColumn(index = 14)
	public int reputation;
	/** 下个关卡 */
	@ExcelColumn(index = 15)
	public int next;
	/** 关卡初始怒气 */
	@ExcelColumn(index = 16)
	public int startPower;
	/** 关卡匹配战力 */
	@ExcelColumn(index = 17)
	public String matchPower;
	/** 关卡匹配等级 */
	@ExcelColumn(index = 18)
	public String matchLv;
	/** 新手匹配战力 */
	@ExcelColumn(index = 19)
	public String protectMatchPower;
	/** 新手匹配等级 */
	@ExcelColumn(index = 20)
	public String protectMatchLv;
}
