/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.ItemView;

/**
 * 兑换码使用事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ICdkUse {
	void onCdkUsed(String cdkey, ItemView[] items);
}
