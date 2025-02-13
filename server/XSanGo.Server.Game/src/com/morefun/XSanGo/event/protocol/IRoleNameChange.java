/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 角色改名事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IRoleNameChange {

	/**
	 * 主公名字变更
	 * 
	 * @param old
	 *            老名字
	 * @param name
	 *            新名字
	 */
	void onRoleNameChange(String old, String name);

}
