/**
 * 
 */
package com.morefun.XSanGo.formation;

import java.util.Map;

import com.XSanGo.Protocol.FormationView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.FormationBuffItem;

/**
 * 队伍/阵型/军团的操作接口
 * 
 * @author sulingyun
 * 
 */
public interface IFormationControler {

	/**
	 * 设置阵法书
	 * 
	 * @param formationId
	 *            阵法索引
	 * @param book
	 *            阵法书
	 */
	void setFormationBuff(String formationId, FormationBuffItem book);

	/**
	 * 设置阵位武将
	 * 
	 * @param formationId
	 *            部队编号
	 * @param postion
	 *            阵位索引
	 * @param hero
	 *            武将，可为null
	 * @param checkLastHero
	 *            是否进行最后一名武将检查
	 * @throws NoteException
	 */
	void setFormationPosition(String formationId, int postion, IHero hero,
			boolean checkLastHero) throws NoteException;

	/**
	 * 根据索引获得部队对象
	 * 
	 * @param formationId
	 * @return
	 */
	IFormation getFormation(String formationId);

	/**
	 * 获取阵型配置表
	 * 
	 * @return
	 */
	FormationView[] getFormationViewList();

	/**
	 * 阵型变更（BUFF更改/升级，武将上下阵）时调用，用于刷新数据
	 * 
	 * @param formation
	 */
	void formationChange(IFormation formation);

	/**
	 * 获取部队一
	 * 
	 * @return
	 */
	IFormation getDefaultFormation();

	/**
	 * 获取作为PVP对手时候的部队配置数据
	 * 
	 * @param formationId
	 * @return
	 */
	PvpOpponentFormationView getPvpOpponentFormationView(String formationId);
	
	/**
	 * 获取作为PVP对手时候的部队配置数据
	 * 
	 * @param formationId
	 * @param isCalculationTempProp 是否计算临时属性
	 * @return
	 */
	PvpOpponentFormationView getPvpOpponentFormationView(String formationId, boolean isCalculationTempProp);

	/**
	 * 清空部队配置，当小于2名武将时直接返回，否则会保留1名武将
	 * 
	 * @param formationId
	 */
	void clearFormation(String formationId);

	/**
	 * @return
	 */
	Map<String, IFormation> getFormation();

	void checkData();
}
