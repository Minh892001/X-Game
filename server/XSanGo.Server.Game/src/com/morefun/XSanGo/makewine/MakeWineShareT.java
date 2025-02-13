package com.morefun.XSanGo.makewine;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.IntString;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 酿酒和分享配置
 * @author zhuzhi.yang
 *
 */
@ExcelTable(fileName = "script/活动和礼包/煮酒论英雄.xls", sheetName = "酿酒和分享配置", beginRow = 2)
public class MakeWineShareT {

	@ExcelColumn(index = 0)
	public int id;
	
	@ExcelColumn(index = 1)
	public String item;
	
	@ExcelColumn(index = 2)
	public String needItem;
	public List<IntString> list_needItem = new ArrayList<IntString>();
	
	@ExcelColumn(index = 3)
	public int compoundScore;
	
	@ExcelColumn(index = 4)
	public int compoundNum;
	
	@ExcelColumn(index = 5)
	public int isShare;
	
	@ExcelColumn(index = 6)
	public int shareNum;
	
	@ExcelColumn(index = 7)
	public int shareScore;
	
	/**
	 * 实际分享瓶数（总瓶数-系统扣掉的）
	 */
	@ExcelColumn(index = 8)
	public int lootAmount;
	
	/**
	 * 领取分享扣掉的瓶数
	 */
	@ExcelColumn(index = 9)
	public int lootNum;
	
	/**
	 * 分享获得的道具
	 */
	@ExcelColumn(index = 10)
	public String shareItem;
	
	/**
	 * 分享获得的道具数量
	 */
	@ExcelColumn(index = 11)
	public int selfNum;
	
	/**
	 * 领取分享获得的道具
	 */
	@ExcelColumn(index = 12)
	public String shareReceiveItem;
	
	/**
	 * 领取分享获得的道具数量
	 */
	@ExcelColumn(index = 13)
	public int getNum;
	
}
