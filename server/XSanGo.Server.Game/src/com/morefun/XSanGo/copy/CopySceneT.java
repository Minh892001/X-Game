/**
 * 
 */
package com.morefun.XSanGo.copy;

import java.util.ArrayList;
import java.util.List;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/副本和怪物/关卡脚本.xls", beginRow = 2, sheetName = "场景数据")
public class CopySceneT {
	@ExcelColumn(index = 0)
	public int copyId;

	@ExcelColumn(index = 2)
	public int sceneSeq;

	@ExcelColumn(index = 10)
	public int duelMonsterId;

	@ExcelColumn(index = 11)
	public int duelRate;

	@ExcelColumn(index = 13)
	public String vsTips;

	private List<StoryEventT> duelEventList = new ArrayList<StoryEventT>();

	public void addDuelEventT(StoryEventT eventT) {
		this.duelEventList.add(eventT);
	}

	public List<StoryEventT> getDuelEventTList() {
		return this.duelEventList;
	}
}
