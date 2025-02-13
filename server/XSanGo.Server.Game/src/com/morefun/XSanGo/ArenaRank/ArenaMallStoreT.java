package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2)
public class ArenaMallStoreT {
	//ID
	@ExcelColumn(index = 0)
	public int ID;
	
	//花费元宝
	@ExcelColumn(index = 1)
	public String ItemID;
	
	//获得奖励
	@ExcelColumn(index = 2)
	public String Name;
	
	//最小数量,没用，目前取得是最大值
	@ExcelColumn(index = 3)
	public int NumMin;
	
	//最大数量
	@ExcelColumn(index = 4)
	public int NumMax;
	
	//权重 概率
	@ExcelColumn(index = 5)
	public int Pro;
	
	//货币类型 0:必须消耗竞技币,1:大于等于该vip等级可免费领取
	@ExcelColumn(index = 6)
	public int CoinType;
	
	//单价
	@ExcelColumn(index = 7)
	public int Price;
	
	//可见最小等级,没用
	@ExcelColumn(index = 8)
	public int VIPMin;
	
	//可见最大等级,没用
	@ExcelColumn(index = 9)
	public int VIPMax;
}
