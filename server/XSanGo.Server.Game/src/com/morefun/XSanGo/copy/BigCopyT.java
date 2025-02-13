/**
 * 
 */
package com.morefun.XSanGo.copy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.CopyDifficulty;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 副本大章节模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/副本和怪物/关卡脚本.xls", sheetName = "大关卡参数")
public class BigCopyT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;

	@ExcelColumn(index = 2)
	public int difficulty;

	@ExcelColumn(index = 3)
	public int open;

	@ExcelColumn(index = 4)
	public String preChapter;

	@ExcelColumn(index = 5)
	public int level;

	@ExcelColumn(index = 6)
	public int star1;

	@ExcelColumn(index = 7)
	public String tc1;

	@ExcelColumn(index = 8)
	public int star2;

	@ExcelColumn(index = 9)
	public String tc2;

	@ExcelColumn(index = 10)
	public int star3;

	@ExcelColumn(index = 11)
	public String tc3;

	private List<SmallCopyT> children;
	private int[] preArray;

	public void initChildren(Collection<SmallCopyT> children) {
		Map<Integer, SmallCopyT> childrenMap = new HashMap<Integer, SmallCopyT>();
		List<Integer> nextList = new ArrayList<Integer>();
		for (SmallCopyT smallCopyT : children) {
			smallCopyT.setParent(this);
			childrenMap.put(smallCopyT.id, smallCopyT);
			nextList.add(smallCopyT.nextId);
		}

		int firstId = 0;
		for (int id : childrenMap.keySet()) {
			if (!nextList.contains(id)) {
				firstId = id;
			}
		}

		this.children = new ArrayList<SmallCopyT>();
		while (childrenMap.containsKey(firstId)) {
			SmallCopyT item = childrenMap.get(firstId);
			this.children.add(item);
			firstId = item.nextId;
		}
	}

	public Iterable<SmallCopyT> getChildren() {
		return this.children;
	}

	public CopyDifficulty getDifficulty() {
		return CopyDifficulty.valueOf(difficulty);
	}

	public int[] getPreChapter() {
		if (this.preArray == null) {
			if (TextUtil.isBlank(this.preChapter)) {
				this.preArray = new int[0];
			} else {
				String[] strArray = this.preChapter.split(",");
				this.preArray = new int[strArray.length];
				for (int i = 0; i < strArray.length; i++) {
					this.preArray[i] = NumberUtil.parseInt(strArray[i]);
				}
			}
		}

		return this.preArray;
	}
}
