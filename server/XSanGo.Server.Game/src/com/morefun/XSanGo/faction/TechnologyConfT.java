package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会科技配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会科技参数")
public class TechnologyConfT {
	@ExcelColumn(index = 0)
	public int openLevel;

	/** 入会多少分钟后可捐赠 */
	@ExcelColumn(index = 1)
	public int joinMinute;

	/** 捐赠需要的微章 */
	@ExcelColumn(index = 2)
	public int useWeiZhang;

	/** 捐赠微章获得荣誉 */
	@ExcelColumn(index = 3)
	public int weiZhangHonor;

	/** 捐赠微章增加经验 */
	@ExcelColumn(index = 4)
	public int weiZhangExp;

	/** 每次捐赠CD秒 */
	@ExcelColumn(index = 5)
	public int oneCD;

	/** CD上限秒 */
	@ExcelColumn(index = 6)
	public int maxCD;

	/** 清空CD元宝 */
	@ExcelColumn(index = 7)
	public int clearCDYuanbao;

	/** 元宝捐赠概率 % */
	@ExcelColumn(index = 8)
	public int yuanbaoPro;

	/** 元宝捐赠数量 */
	@ExcelColumn(index = 9)
	public int donateYuanbao;

	/** 元宝捐赠荣誉 */
	@ExcelColumn(index = 10)
	public int yuanbaoHonor;

	/** 元宝捐赠经验 */
	@ExcelColumn(index = 11)
	public int yuanbaoExp;
	
	/** 每天可设置推荐次数 */
	@ExcelColumn(index = 13)
	public int recommendNum;
}
