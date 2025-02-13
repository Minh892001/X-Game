package com.morefun.XSanGo.timeBattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.copy.MonsterT;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/副本和怪物/时空战役.xls", sheetName = "关卡数据", beginRow = 2)
public class TimeBattleT {
	@ExcelColumn(index = 0)
	public int id;// 关卡ID

	@ExcelColumn(index = 1)
	public String name;// 关卡名称
	
	@ExcelColumn(index = 2)
	public int diff;// 难度0 1 2
	
	@ExcelColumn(index = 3)
	public int level;// 需要等级

	@ExcelColumn(index = 7)
	public String tc;// 掉落TC

	@ExcelColumn(index = 12)
	public int times;// 可挑战次数

	@ExcelColumn(index = 4)
	public int junling;// 需要军令

	@ExcelColumn(index = 13)
	public int cdMinute;//CD时间 分钟

	private List<BattleSceneT> sceneTList = new ArrayList<BattleSceneT>();

	public void addSceneT(BattleSceneT cst) {
		this.sceneTList.add(cst);
	}

	/**
	 * 获取可单挑怪物MAP，KEY为场景序列
	 * 
	 * @return
	 */
	public Map<Integer, MonsterT> getDuelMonsterMap() {
		Map<Integer, MonsterT> map = new HashMap<Integer, MonsterT>();
		for (BattleSceneT cst : this.sceneTList) {
			if (cst.duelMonsterId != 0) {
				map.put(cst.sceneSeq, XsgCopyManager.getInstance().findMonsterT(cst.duelMonsterId));
			}
		}
		return map;
	}
}
