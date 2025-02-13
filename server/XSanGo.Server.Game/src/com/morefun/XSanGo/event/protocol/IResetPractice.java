package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.hero.IHero;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 重置武将修炼
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IResetPractice {
	/**
	 * 重置武将修炼
	 * @param hero
	 * @param index   位置
	 * @param oldName 重置前名字
	 * @param oldColor 重置前颜色	  
	 * @param oldLevel 重置前等级
	 * @param oldExp   重置前经验
	 * @param newName  重置后名字
	 * @param newColor 重置后颜色
	 */
	public void onResetPractice(IHero hero, int index, String oldName, int oldColor, int oldLevel, int oldExp,
			String newName, int newColor);
}
