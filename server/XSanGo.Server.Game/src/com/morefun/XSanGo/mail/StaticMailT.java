/**
 * 
 */
package com.morefun.XSanGo.mail;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 全服邮件配置模板
 * 
 * @author 吕明涛
 * 
 */
@ExcelTable(fileName = "script/公告邮件/邮件脚本.xls", beginRow = 2, sheetName = "邮件")
public class StaticMailT {
	//模板ID
	@ExcelColumn(index = 0)
	public int id;
	
	//邮件类型，0：公告邮件 1：系统邮件，阅后即焚类型
	@ExcelColumn(index = 1)
	public int type;
	
	//发送对象，0：全部玩家，其他情况，格式：玩家名称,玩家名称…
	@ExcelColumn(index = 2)
	public String sendObj;	
	
	//等级限制
	@ExcelColumn(index = 3)
	public int level;
	
	//VIP等级限制
	@ExcelColumn(index = 4)
	public int vipLevel;
	
	//邮件标题
	@ExcelColumn(index = 5)
	public String title;
	
	//邮件内容
	@ExcelColumn(index = 6)
	public String body;
	
	//使用TC,参见掉落算法
	@ExcelColumn(index = 7)
	public String attachStr;
	
	//邮件有效开始时间
	@ExcelColumn(index = 8)
	public String starDate;
	
	//邮件有效结束时间
	@ExcelColumn(index = 9)
	public String endDate;
	
	//发送者署名
	@ExcelColumn(index = 10)
	public String sendName;
	
	//发送时间
	@ExcelColumn(index = 11)
	public String sendDate;
	
	//邮件到期删除 时间
	@ExcelColumn(index = 12)
	public String deleteDate;
	
	//渠道匹配 多个,分割
	@ExcelColumn(index = 13)
	public String channels;
}
