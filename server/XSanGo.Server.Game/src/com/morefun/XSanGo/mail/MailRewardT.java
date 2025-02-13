/**
 * 
 */
package com.morefun.XSanGo.mail;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 发送邮件奖励配置模板
 * 
 * @author 吕明涛
 * 
 */
@ExcelTable(fileName = "script/公告邮件/邮件脚本.xls", beginRow = 2, sheetName = "特殊邮件格式")
public class MailRewardT {
	//模板ID
	@ExcelColumn(index = 0)
	public int id;
	
	//邮件类型，0：公告邮件 1：系统邮件，阅后即焚类型
	@ExcelColumn(index = 1)
	public int type;
	
	//邮件标题
	@ExcelColumn(index = 2)
	public String title;
	
	//邮件内容
	@ExcelColumn(index = 3)
	public String body;
	
	//发送者署名
	@ExcelColumn(index = 4)
	public String sendName;
	
}
