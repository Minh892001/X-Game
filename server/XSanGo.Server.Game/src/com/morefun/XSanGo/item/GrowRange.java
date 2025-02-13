/**
 * 
 */
package com.morefun.XSanGo.item;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * 成长范围描述
 * 
 * @author sulingyun
 * 
 */
public class GrowRange {
	@ExcelColumn(index = 0)
	public String data;

	private boolean parse;
	private float min;
	private float max;

	public float getMin() {
		if (!this.parse) {
			this.parseData();
		}

		return this.min;
	}

	public float getMax() {
		if (!this.parse) {
			this.parseData();
		}

		return this.max;
	}

	private void parseData() {
		int index = this.data.indexOf(",");
		if (index < 0) {
			throw new IllegalStateException();
		}

		this.min = Float.parseFloat(this.data.substring(0, index));
		this.max = Float.parseFloat(this.data.substring(index + 1));
		this.parse = true;
	}
}
