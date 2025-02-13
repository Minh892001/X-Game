/**
 * 
 */
package com.morefun.XSanGo.formation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.item.FormationBuffItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;

/**
 * 玩家军团管理
 * 
 * @author sulingyun
 * 
 */
public class XsgFormationManager {

	private static XsgFormationManager instance = new XsgFormationManager();

	public static XsgFormationManager getInstance() {
		return instance;
	}

	private Map<QualityColor, FormationBuffLevelUpConfigT> buffLevelUpMap;
	private Map<Integer, PositionOpenT> positionTMap;

	private XsgFormationManager() {
		this.buffLevelUpMap = new HashMap<QualityColor, FormationBuffLevelUpConfigT>();
		List<FormationBuffLevelUpConfigT> list = ExcelParser
				.parse(FormationBuffLevelUpConfigT.class);
		for (FormationBuffLevelUpConfigT configT : list) {
			this.buffLevelUpMap.put(configT.getColor(), configT);
		}

		this.positionTMap = CollectionUtil.toMap(
				ExcelParser.parse(PositionOpenT.class), "postion");
	}

	public IFormationControler createFormationControler(IRole rt, Role db) {
		return new FormationControler(rt, db);
	}

	/**
	 * 计算阵法书被消耗时提供的升级经验
	 * 
	 * @param item
	 * @return
	 */
	public int caculateBuffProvideExp(FormationBuffItem item) {
		FormationBuffLevelUpConfigT configT = this.findBuffLevelUpConfigT(item
				.getColor());
		int sum = configT.provideExpBase + item.getExp();

		for (int i = 2; i <= item.getLevel(); i++) {
			sum += configT.conditions[i-2].exp;
		}

		return sum;
	}

	public FormationBuffLevelUpConfigT findBuffLevelUpConfigT(QualityColor color) {
		return this.buffLevelUpMap.get(color);
	}

	/**
	 * 是否战斗位置
	 * 
	 * @param position
	 * @return
	 */
	public boolean isBattlePosition(int position) {
		return position > -1 && position < 9;
	}

	/**
	 * 获取指定阵位的开启等级
	 * 
	 * @param position
	 * @return
	 */
	public int getFormationPosOpenLevel(int position) {
		return this.positionTMap.containsKey(position) ? this.positionTMap
				.get(position).openLevel : 0;
	}

	/**
	 * 指定的阵位是否接受单挑
	 * 
	 * @param pos
	 * @return
	 */
	public boolean isDuelPosition(byte pos) {
		return pos > 2 && pos < 6;
	}

}
