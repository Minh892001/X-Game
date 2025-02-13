/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 部队阵位变更事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IFormationPosChange {
	void onFormationPositionChange(IFormation formation, int pos, IHero hero);
}
