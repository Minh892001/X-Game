/**
 * 
 */
package com.morefun.XSanGo.role;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 角色行动力购买
 * @author 吕明涛
 * 
 */
@ExcelTable(fileName = "script/互动和聊天/掠夺脚本.xls", sheetName = "行动力购买配置", beginRow = 2)
public class RoleVitBuyT {
	@ExcelColumn(index = 0)
	public int bugNum;

	@ExcelColumn(index = 1)
	public int cost;

	@ExcelColumn(index = 2)
	public int vidAdd;
}
