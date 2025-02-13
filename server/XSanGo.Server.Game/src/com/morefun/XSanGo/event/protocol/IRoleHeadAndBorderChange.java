/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 角色修改头像和边框事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IRoleHeadAndBorderChange {

	/**
	 * 主公名字变更
	 * 
	 * @param old
	 *            老头像~边框
	 * @param head
	 *            新头像~边框
	 */
	void onRoleHeadChange(String old, String headAndBorder);

}
