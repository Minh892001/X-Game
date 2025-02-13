/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.formation;

import com.XSanGo.Protocol.DuelUnitView;
import com.XSanGo.Protocol.FormationSummaryView;
import com.XSanGo.Protocol.FormationView;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.FormationBuffItem;

/**
 * 军团操作接口
 * 
 * @author Su LingYun
 * 
 */
public interface IFormation {

	/**
	 * 获取指定阵位的英雄
	 * 
	 * @param i
	 *            英雄所处的阵位
	 * @return
	 */
	IHero getHeroByPos(int i);

	/**
	 * 是否包含指定的武将
	 * 
	 * @param i
	 *            武将模板ID
	 * @return
	 */
	boolean containsHero(int i);

	/**
	 * 获取当前使用的阵法书
	 * 
	 * @return
	 */
	FormationBuffItem getBuff();

	/**
	 * 设置阵法书
	 * 
	 * @param book
	 */
	void setBuff(FormationBuffItem book);

	/**
	 * 设置阵位武将
	 * 
	 * @param position
	 * @param hero
	 */
	void setHeroPosition(int position, IHero hero);

	/**
	 * 获取部队编号，0-2分别表示部队1-3
	 * 
	 * @return
	 */
	byte getIndex();

	/**
	 * 获取所有阵型中的武将
	 * 
	 * @return
	 */
	Iterable<IHero> getHeros();

	/**
	 * 获取唯一标识
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 获取视图数据
	 * 
	 * @return
	 */
	FormationView getView();

	/**
	 * 获取上阵武将数量，不包括援军
	 * 
	 * @return
	 */
	int getHeroCountExcludeSupport();

	/**
	 * 部队摘要信息视图，一般在给其他玩家展示时调用
	 * 
	 * @return
	 */
	FormationSummaryView[] getSummaryView();
	
	/**
	 * 援军摘要信息视图，一般在给其他玩家展示时调用
	 * 
	 * @return
	 */
	FormationSummaryView[] getSupportSummaryView();
	
	

	/**
	 * 从上阵武将中随机出一位单挑武将
	 * 
	 * @return
	 */
	IHero randomDuelHero();

	/**
	 * 计算战力
	 * 
	 * @return
	 */
	int calculateBattlePower();

	/**
	 * 部队中武将总数
	 * 
	 * @return
	 */
	byte getHeroCountIncludeSupport();

	/**
	 * 获取首发阵容单挑战斗数据
	 * 
	 * @return
	 */
	DuelUnitView[] generateDuelCandidateData();
	
	/**
	 * 获取当前使用的阵法进阶类型
	 * @return
	 */
	int getAdvancedType();
	
	/**
	 * 获取所有阵法进阶
	 * @return
	 */
	String getAdvanceds();
	
	/**
	 * 更新使用的阵法进阶类型
	 * @param type
	 */
	void setAdvancedType(int type);
	
	/**
	 * 更新阵法进阶
	 * @param advanceds
	 */
	void setAdvanceds(String advanceds);
}
