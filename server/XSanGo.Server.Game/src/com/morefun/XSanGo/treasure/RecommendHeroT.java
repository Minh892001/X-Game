package com.morefun.XSanGo.treasure;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 寻宝推荐武将
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/秘境寻宝.xls", sheetName = "推荐武将", beginRow = 2)
public class RecommendHeroT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;

	/**等级,分割*/
	@ExcelColumn(index = 2)
	public String levels;

	/**星级,分割*/
	@ExcelColumn(index = 3)
	public String stars;
	
	/**所在部队位置*/
	@ExcelColumn(index = 4)
	public int index;
	
	/**是否是活动武将0-否 1-是*/
	@ExcelColumn(index = 5)
	public int isActivity;
}
