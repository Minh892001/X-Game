package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/我要做VIP.xls", sheetName = "题库", beginRow = 2)
public class AnswerT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String subject;// 题目内容

	@ExcelColumn(index = 2)
	public String answerA;

	@ExcelColumn(index = 3)
	public String answerB;

	@ExcelColumn(index = 4)
	public String answerC;

	@ExcelColumn(index = 5)
	public String answerD;

	@ExcelColumn(index = 6)
	public String rightAnswer;// 正确答案

	@ExcelColumn(index = 7)
	public int vipExp;// vip经验
}
