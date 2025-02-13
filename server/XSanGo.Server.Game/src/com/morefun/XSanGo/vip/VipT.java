package com.morefun.XSanGo.vip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/参数配置脚本/VIP脚本.xls", sheetName = "VIP", beginRow = 2)
public class VipT {

	@ExcelColumn(index = 0)
	public int VIPLv;
	@ExcelColumn(index = 1)
	public int BuyYB;

	@ExcelColumn(index = 2)
	public int Quality;

	@ExcelColumn(index = 3)
	public String Reward;

	@ExcelColumn(index = 4)
	public int Num;

	@ExcelColumn(index = 5)
	public int Price;

	@ExcelColumn(index = 6)
	public int PriceNow;
	@ExcelColumn(index = 7)
	public String Desc;
	@ExcelColumn(index = 8)
	public int med2;
	@ExcelColumn(index = 9)
	public int GoldNum;

	@ExcelColumn(index = 10)
	public int maxSkillPoint;

	/** 关卡重置次数 */
	@ExcelColumn(index = 11)
	public int StageNum;
	@ExcelColumn(index = 12)
	public int PhyNum;
	@ExcelColumn(index = 13)
	public int FriendNum;
	@ExcelColumn(index = 14)
	public int EnemyNum;
	@ExcelColumn(index = 15)
	public int BlacklistNum;

	@ExcelColumn(index = 16)
	public int SkillNum;

	@ExcelColumn(index = 17)
	public String GoldCritTimes;

	@ExcelColumn(index = 18)
	public String equipStrengthCrit;

	@ExcelColumn(index = 19)
	public int maxServerCopy;

	/** 竞拍数量 */
	@ExcelColumn(index = 20)
	public int buyNum;

	/** 拍卖数量 */
	@ExcelColumn(index = 21)
	public int sellNum;

	@ExcelColumn(index = 22)
	public int maxBuyJunLingNum;
}
