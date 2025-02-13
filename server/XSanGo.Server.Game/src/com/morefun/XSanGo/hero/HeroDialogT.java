/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 武将对白模板
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/武将相关/武将脚本.xls", beginRow = 2, sheetName = "武将台词和配音")
public class HeroDialogT {
	@ExcelColumn(index = 0)
	public int id;
	@ExcelColumn(index = 2)
	public CharSequence name;

	@ExcelColumn(index = 15)
	public String releaseMsg;

	@ExcelColumn(index = 16)
	public String killMsg;

	@ExcelColumn(index = 17)
	public String rejectEmployMsg;

	@ExcelColumn(index = 18)
	public String acceptEmployMsg;

	public String randomReleaseMsg(String roleName) {
		return this.replace(this.randomStringFrom(this.releaseMsg), roleName);
	}

	public String randomKillMsg(String roleName) {
		return this.replace(this.randomStringFrom(this.killMsg), roleName);
	}

	private String replace(String input, String roleName) {
		return input.replace("$N", this.name).replace("$R", roleName);
	}

	private String randomStringFrom(String input) {
		String[] array = input.split("\n");
		return array[NumberUtil.random(array.length)];
	}

	public String randomAcceptEmployMsg(String roleName) {
		return this.replace(this.randomStringFrom(this.acceptEmployMsg),
				roleName);
	}

	public String randomRejectEmployMsg(String roleName) {
		return this.replace(this.randomStringFrom(this.rejectEmployMsg),
				roleName);
	}
}