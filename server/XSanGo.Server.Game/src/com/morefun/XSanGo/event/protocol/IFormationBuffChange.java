/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.item.FormationBuffItem;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 部队阵法书变更事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IFormationBuffChange {

	void onFormationBuffChange(IFormation formation, FormationBuffItem book);

}
