/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.common.BattlePropertyMap;
import com.morefun.XSanGo.item.PropertyT;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将突破配置
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/武将相关/武将脚本.xls", sheetName = "突破属性", beginRow = 2)
public class HeroBreakT {

	@ExcelColumn(index = 0)
	public int heroId;

	@ExcelColumn(index = 3)
	public int breakLevel;

	@ExcelColumn(index = 4)
	public int propertyCount;

	@ExcelComponet(index = 5, columnCount = 2, size = 8)
	public PropertyT[] properties;

	public BattlePropertyMap getBattlePropertyMap() {
		BattlePropertyMap map = new BattlePropertyMap();
		for (int i = 0; i < this.propertyCount; i++) {
			PropertyT pt = this.properties[i];
			if (pt.isEffective()) {
				map.combine(pt.code, pt.value);
			}
		}

		return map;
	}

}
