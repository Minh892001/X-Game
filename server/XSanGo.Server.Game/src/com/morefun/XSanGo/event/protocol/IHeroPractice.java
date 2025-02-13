package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.hero.IHero;

/**
 * 武将修炼
 * 
 * @author lixiongming
 * 
 */
@signalslot
public interface IHeroPractice {
	/**
	 * 武将修炼
	 * @param hero
	 * @param index 第几个格子1-8
	 * @param prop  属性名
	 * @param color	属性颜色
	 * @param oldLevel 修炼前等级
	 * @param oldExp	修炼前经验
	 * @param addExp	本次修炼增加经验
	 * @param newLevel	修炼后等级
	 * @param newExp	修炼后经验
	 * @param sumGx     消耗功勋
	 */
	public void onHeroPractice(IHero hero, int index, String prop, int color, int oldLevel, int oldExp, int addExp,
			int newLevel, int newExp,int sumGx);
}
