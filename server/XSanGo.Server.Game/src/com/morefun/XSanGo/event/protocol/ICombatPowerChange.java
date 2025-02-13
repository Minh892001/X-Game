/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 战力变化事件
 * 
 * @author linyun.su
 * 
 */
@signalslot
public interface ICombatPowerChange {
	void onCombatPowerChange(int old, int newValue);
}
