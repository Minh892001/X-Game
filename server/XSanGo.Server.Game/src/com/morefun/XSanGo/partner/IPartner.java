package com.morefun.XSanGo.partner;

import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.common.BattlePropertyMap;
import com.morefun.XSanGo.hero.IHero;

public interface IPartner {

	/**
	 * 获取指定阵位的英雄
	 * 
	 * @param i
	 *            英雄所处的阵位
	 * @return
	 */
	public IHero getHeroByPos(int i);
	/**
	 * 获取指定伙伴位置的属性
	 * 
	 * @param 伙伴位置
	 *            英雄所处的阵位
	 * @return
	 */
	public PartnerPropT getPropByPos(int pos);
	
	/**
	 * 是否包含指定的武将
	 * 
	 * @param i
	 *            武将模板ID
	 * @return
	 */
	public boolean containsHero(int i);
	
	/**
	 * 设置阵位武将
	 * 
	 * @param position
	 * @param hero
	 */
	public void setHeroPosition(int position, IHero hero);
	
	/**
	 * 获取伙伴武将数量
	 * 
	 * @param position
	 * @param hero
	 */
	public int getHeroCount();
	
	/**
	 * 获取当前伙伴的属性加成
	 * @return
	 */
	public BattlePropertyMap getBattlePropertyMap();
	
	/**
	 * 指定位置是否已经开启
	 * @param pos 伙伴位置
	 * @return
	 */
	public boolean isOpened(int pos);
	
	public Map<Integer, IHero> getHeroMap();
	
	public Map<Integer, PartnerPropT> getPropMap();
	
	public Map<Integer, PartnerHeroT> getSpecialHeroMap();
	
	public String getId();
}
