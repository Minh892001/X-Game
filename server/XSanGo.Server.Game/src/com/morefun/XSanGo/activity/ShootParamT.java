package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author zhouming
 */
@ExcelTable(fileName = "script/活动和礼包/百步穿杨.xls", sheetName = "抽奖配置", beginRow = 2)
public class ShootParamT {

	/**
	 * 类型
	 */
	@ExcelColumn(index = 0)
	public int type;

	/**
	 * 免费刷新周期
	 */
	@ExcelColumn(index = 1)
	public int freeCyc;

	/**
	 * 刷新时间
	 */
	@ExcelColumn(index = 2)
	public String cycTime;

	/**
	 * 免费基础次数
	 */
	@ExcelColumn(index = 3)
	public int freeNum;

	/**
	 * 免费准备间隔
	 */
	@ExcelColumn(index = 4)
	public int freeCd;
}
