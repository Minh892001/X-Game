/**
 * 
 */
package com.morefun.XSanGo.item;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * @author sulingyun
 * 
 */
public class PropertyT {
	@ExcelColumn(index = 0)
	public String code;

	@ExcelColumn(index = 1)
	public int value;

	/**
	 * 是否有效
	 * 
	 * @return
	 */
	public boolean isEffective() {
		return this.value != 0;
	}
}
