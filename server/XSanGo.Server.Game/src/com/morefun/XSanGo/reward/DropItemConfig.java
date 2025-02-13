/**
 * 
 */
package com.morefun.XSanGo.reward;

import java.util.regex.Pattern;

import com.morefun.XSanGo.script.ExcelColumn;

/**
 * @author sulingyun
 * 
 */
public class DropItemConfig {
	/** TC跳转规则 */
	private static Pattern tcPattern = Pattern.compile("^[A-Z]{1}\\S+$");
	@ExcelColumn(index = 0)
	public String code;

	@ExcelColumn(index = 1)
	public int value;

	/**
	 * 是否指向另一个TC
	 * 
	 * @return
	 */
	public boolean isTcRedirect() {
		// this.code.charAt(0) == 'T'
		return tcPattern.matcher(this.code).matches();
	}

	/**
	 * 是否表示一个碎片
	 * 
	 * @return
	 */
	public boolean isPiece() {
		return this.code.endsWith("-x");
	}
}
