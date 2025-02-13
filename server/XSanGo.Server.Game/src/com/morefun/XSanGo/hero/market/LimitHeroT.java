/**
 * 
 */
package com.morefun.XSanGo.hero.market;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 限时武将列表
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/武将相关/抽卡脚本.xls", beginRow = 2)
public class LimitHeroT {
	@ExcelColumn(index = 0)
	public int id;

	/**0：不开放1：武将2：装备/阵法*/
	@ExcelColumn(index = 1)
	public int type;

	@ExcelColumn(index = 2)
	public String heroId;

	@ExcelColumn(index = 3)
	public String heroName;

	/**魂魄ID*/
	@ExcelColumn(index = 4)
	public String soulId;
	
	@ExcelColumn(index = 5)
	public int minSoul;
	
	@ExcelColumn(index = 6)
	public int maxSoul;

	/**权重*/
	@ExcelColumn(index = 7)
	public int weight;
}
