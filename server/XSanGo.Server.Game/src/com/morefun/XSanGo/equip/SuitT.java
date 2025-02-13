/**
 * 
 */
package com.morefun.XSanGo.equip;

import com.morefun.XSanGo.common.BattlePropertyMap;
import com.morefun.XSanGo.item.PropertyT;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 套装模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/装备脚本.xls", sheetName = "套装属性")
public class SuitT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelComponet(index = 8, columnCount = 2, size = 2)
	public PropertyT[] properties2;

	@ExcelComponet(index = 12, columnCount = 2, size = 3)
	public PropertyT[] properties3;

	@ExcelComponet(index = 18, columnCount = 2, size = 4)
	public PropertyT[] properties4;

	@ExcelComponet(index = 26, columnCount = 2, size = 5)
	public PropertyT[] properties5;

	@ExcelComponet(index = 36, columnCount = 2, size = 6)
	public PropertyT[] properties6;

	/**
	 * 当穿戴N件装备时候共激活了多少属性
	 * 
	 * @param equipCount
	 *            同时穿戴的装备数量
	 * @return
	 */
	public BattlePropertyMap getBattlePropertyMap(int equipCount) {
		BattlePropertyMap map = new BattlePropertyMap();
		if (equipCount >= 2) {
			for (PropertyT p : this.properties2) {
				if (TextUtil.isNotBlank(p.code)) {
					map.combine(p.code, p.value);
				}
			}
		}
		if (equipCount >= 3) {
			for (PropertyT p : this.properties3) {
				if (TextUtil.isNotBlank(p.code)) {
					map.combine(p.code, p.value);
				}
			}
		}
		if (equipCount >= 4) {
			for (PropertyT p : this.properties4) {
				if (TextUtil.isNotBlank(p.code)) {
					map.combine(p.code, p.value);
				}
			}
		}
		if (equipCount >= 5) {
			for (PropertyT p : this.properties5) {
				if (TextUtil.isNotBlank(p.code)) {
					map.combine(p.code, p.value);
				}
			}
		}
		if (equipCount >= 6) {
			for (PropertyT p : this.properties6) {
				if (TextUtil.isNotBlank(p.code)) {
					map.combine(p.code, p.value);
				}
			}
		}

		return map;
	}

}
