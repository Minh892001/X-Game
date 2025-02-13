/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.formation.IFormation;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 清理部队事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IFormationClear {

	void onFormationClear(IFormation formation);

}
