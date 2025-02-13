package com.morefun.XSanGo.faction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.NumberUtil;

@ExcelTable(beginRow = 2, fileName = "script/副本和怪物/公会副本.xls", sheetName = "场景数据")
public class FactionCopyStageT {
	@ExcelColumn(index = 0)
	public int copyId;

	@ExcelColumn(index = 2)
	public int stageNum;

	@ExcelColumn(index = 5)
	public String monster;

	@ExcelColumn(index = 7)
	public int isBoss;

	@ExcelColumn(index = 12)
	public String killDropTc;// 击杀任意怪物必定掉落(除boss)

	@ExcelColumn(index = 13)
	public String bossDropTc;// boss生命值低于某个百分比时必定掉落，连接下一个字段

	@ExcelColumn(index = 14)
	public String bloodHarm;// 达到指定血量伤害掉落上面TC，逗号分割

	@ExcelColumn(index = 15)
	public String randomTc;// 随机掉落，只要挑战就有几率获得

	@ExcelColumn(index = 16)
	public int noneAdd;// 不加成的概率

	@ExcelColumn(index = 17)
	public int weiGuoAdd;// 魏国加成的概率

	@ExcelColumn(index = 18)
	public int shuGuoAdd;// 蜀国加成的概率

	@ExcelColumn(index = 19)
	public int wuGuoAdd;// 吴国加成的概率

	@ExcelColumn(index = 20)
	public int otherAdd;// 群英加成的概率

	@ExcelColumn(index = 21)
	public int addValue;// 加成百分比

	/**
	 * 关卡所有怪物ID,key-位置
	 */
	public Map<Integer, Integer> monsterIndexMap = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * 解析怪物ID
	 */
	public void parserMonster() {
		monsterIndexMap.clear();
		String[] mm = monster.replaceAll("[{}]", "").split(";");
		for (String m : mm) {
			monsterIndexMap.put(NumberUtil.parseInt(m.split(",")[1]), NumberUtil.parseInt(m.split(",")[0]));
		}
	}
}
