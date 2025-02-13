/**
 * 
 */
package com.morefun.XSanGo.hero;

import java.util.ArrayList;
import java.util.List;

import com.morefun.XSanGo.script.CellWrap;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 缘分模板数据
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/武将相关/缘分与随从脚本.xls", sheetName = "缘分列表", beginRow = 2)
public class RelationT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;

	@ExcelColumn(index = 2)
	public String type;
	
	@ExcelColumn(index = 3)
	public int nextId;
	
	@ExcelColumn(index = 4)
	public int jinbi;

	@ExcelComponet(index = 5, columnCount = 1, size = 5)
	public CellWrap[] targetList;

	@ExcelComponet(index = 11, columnCount = 3, size = 2)
	public RelationMatchT[] matches;

	private Object parseResult;

	/**
	 * 缘分匹配，返回匹配成功缘分
	 * 
	 * @param proxy
	 * @return
	 */
	public List<RelationMatchT> match(IHeroRelationProxy proxy) {
		List<RelationMatchT> list = new ArrayList<RelationMatchT>();
		int match = 0;
		if (this.type.equals("武将")) {
			match = this.heroMatch(proxy);
		} else if (this.type.equals("装备")) {
			match = this.equipMatch(proxy);
		} else if (this.type.equals("阵法")) {
			match = this.formationMatch(proxy);
		}

		// 第一个都满足不了，肯定什么都激活不了
		if (match >= this.matches[0].needObjCount) {
			list.add(this.matches[0]);
		}

		// 有两个子缘分，且满足大的那个则返回大的那个
		if (this.matches[1].needObjCount > 0
				&& match >= this.matches[1].needObjCount) {
			list.add(this.matches[1]);
		}

		return list;

	}

	private int formationMatch(IHeroRelationProxy proxy) {
		return proxy.isFormationBuffEquals(this.targetList[0].value) ? 1 : 0;
	}

	private int equipMatch(IHeroRelationProxy proxy) {
		return proxy.hasEquip(this.targetList[0].value) ? 1 : 0;
	}

	private int heroMatch(IHeroRelationProxy proxy) {
		Integer[] heroIdArray;
		if (this.parseResult == null) {
			List<Integer> list = new ArrayList<Integer>();
			for (CellWrap cell : this.targetList) {
				if (TextUtil.isBlank(cell.value)
						|| cell.value.equals("0")) {
					continue;
				}

				list.add(Integer.parseInt(cell.value));
			}
			this.parseResult = list.toArray(new Integer[0]);
		}

		heroIdArray = (Integer[]) this.parseResult;
		int match = proxy.howManyHeroInTheSameGroup(heroIdArray);

		return match;
	}

	/**
	 * 根据匹配对象数获取匹配信息
	 * 
	 * @param objCount
	 * @return
	 */
	public RelationMatchT getMatchByObjCount(int objCount) {
		for (RelationMatchT match : this.matches) {
			if (match.needObjCount == objCount) {
				return match;
			}
		}

		return null;
	}
}
