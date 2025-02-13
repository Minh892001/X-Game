package com.morefun.XSanGo.treasure;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 寻宝阶段配置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/寻宝大冒险脚本.xls", sheetName = "寻宝参数", beginRow = 2)
public class TreasureStageT {
	@ExcelColumn(index = 0)
	public int stage;
	
	@ExcelColumn(index = 1)
	public int minute;
	
	@ExcelColumn(index = 2)
	public int speedPrice;
}
