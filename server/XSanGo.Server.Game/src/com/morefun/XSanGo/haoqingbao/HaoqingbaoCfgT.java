package com.morefun.XSanGo.haoqingbao;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 豪情宝配置
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/壕情宝.xls", beginRow = 2, sheetName = "配置参数")
public class HaoqingbaoCfgT {
	/** 红包浮动范围, 根据总额和个数算出平均值，生成红包上下浮动范围 */
	@ExcelColumn(index = 0)
	public String range;
	/** 单个红包最小金额,元宝 */
	@ExcelColumn(index = 1)
	public int minNum;
	/** 每天抢红包次数上限 */
	@ExcelColumn(index = 2)
	public int maxAcceptNum;
	/** 红包时效, 小时 */
	@ExcelColumn(index = 3)
	public int timeLimit;
	/** 走马灯公告金额限制，大于这个金额的公告, 元宝 */
	@ExcelColumn(index = 4)
	public int noticeLimit;
	/** 保存记录时长, 天 */
	@ExcelColumn(index = 5)
	public int recordSaveTime;
	/** 红包留言字数上限 */
	@ExcelColumn(index = 6)
	public int maxWords;
	/** 功能是否开放 */
	@ExcelColumn(index = 7)
	public int isOpen;
	/** 退回余额的消息提醒 */
	@ExcelColumn(index = 8)
	public String giveBackMsg;
	/** 每个红包最大上限 */
	@ExcelColumn(index = 9)
	public int maxTotalNum;
	/** 充值状态持续的时间, 分钟 */
	@ExcelColumn(index = 10)
	public int chargeStatusTime;
	/** 好友红包，友情点数增加多少 */
	@ExcelColumn(index = 11)
	public int addFriendPointNum;
}
