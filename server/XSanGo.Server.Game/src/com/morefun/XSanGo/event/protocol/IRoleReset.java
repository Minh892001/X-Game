/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 帐号重置事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IRoleReset {
	void onReset();
}
