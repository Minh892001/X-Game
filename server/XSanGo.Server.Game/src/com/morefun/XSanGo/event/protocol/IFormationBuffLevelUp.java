/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.item.FormationBuffItem;

/**
 * 升级阵法书事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IFormationBuffLevelUp {
	void onFormationBuffLevelUp(FormationBuffItem buff, int money, int expDiff, int beforeLevel, int beforeExp, int afterLevel, int afterExp);

}
