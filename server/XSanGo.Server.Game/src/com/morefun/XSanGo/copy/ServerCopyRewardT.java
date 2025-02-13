/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/互动和聊天/关卡占领奖励.xls", sheetName = "奖励列表", beginRow = 2)
public class ServerCopyRewardT {
	@ExcelColumn(index = 0)
	public int count;

	@ExcelColumn(index = 1)
	public String items;

	private Property[] array;

	/**
	 * 解析奖励物品
	 * 
	 * @return
	 */
	public Property[] parseItems() {
		if (this.array == null) {
			String[] temp = this.items.split(",");
			this.array = new Property[temp.length];
			for (int i = 0; i < temp.length; i++) {
				String[] subTemp = temp[i].split(":");
				this.array[i] = new Property(subTemp[0],
						NumberUtil.parseInt(subTemp[1]));
			}
		}
		return this.array;
	}
}
