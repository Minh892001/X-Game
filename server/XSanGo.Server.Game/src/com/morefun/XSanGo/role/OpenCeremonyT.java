/**
 * 
 */
package com.morefun.XSanGo.role;

import java.util.ArrayList;
import java.util.List;

import com.morefun.XSanGo.copy.StoryEventT;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/副本和怪物/开篇剧情.xls", sheetName = "出场和剧情脚本", beginRow = 2)
public class OpenCeremonyT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 14)
	public String vsTips;

	private List<StoryEventT> duelEventList;

	public List<StoryEventT> getDuelEventTList() {
		if (this.duelEventList != null) {
			return this.duelEventList;
		}

		this.duelEventList = new ArrayList<StoryEventT>();
		String[] steps = this.vsTips.split(",");
		for (String step : steps) {
			if (step.startsWith("X-")) {
				StoryEventT eventT = XsgCopyManager.getInstance()
						.getStoryEventT(Integer.parseInt(step.substring(2)));
				if (eventT.eventType == 2) {
					this.duelEventList.add(eventT);
				}
			}
		}

		return this.duelEventList;
	}

}
