package com.morefun.XSanGo.worldboss;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
/**
 * 世界BOSS关卡
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/副本和怪物/乱世魔王脚本.xls", sheetName = "关卡数据", beginRow = 2)
public class WorldBossCustomsT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;

	@ExcelColumn(index = 2)
	public int minLevel;

	/**参战掉落物品*/
	@ExcelColumn(index = 5)
	public String items;
	
	/**CD秒*/
	@ExcelColumn(index = 9)
	public int cd;
	
	/**场景资源*/
	@ExcelColumn(index = 10)
	public int scene;
	
	/**怪物位置*/
	@ExcelColumn(index = 13)
	public int position;
	
	/**参与奖*/
	@ExcelColumn(index = 14)
	public String joinItems;
	
	/**参与奖*/
	public ItemView[] joinIt = new ItemView[0];
	
	/**怪物数据*/
	public List<WorldBossMonsterT> monsterTs = new ArrayList<WorldBossMonsterT>();
}
