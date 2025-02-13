/**
 * 
 */
package com.morefun.XSanGo.hero;

/**
 * @author sulingyun
 * 
 */
public interface IHeroRelationProxy {

	/**
	 * 指定的武将列表中有多少人是在同一阵型配置里的
	 * 
	 * @param heroIdArray
	 * @return
	 */
	int howManyHeroInTheSameGroup(Integer[] heroIdArray);

	/**
	 * 是否装备了指定物品
	 * 
	 * @param equipTemplate
	 * @return
	 */
	boolean hasEquip(String equipTemplate);

	/**
	 * 是否使用了指定阵法书
	 * 
	 * @param buffId
	 * @return
	 */
	boolean isFormationBuffEquals(String buffId);

}
