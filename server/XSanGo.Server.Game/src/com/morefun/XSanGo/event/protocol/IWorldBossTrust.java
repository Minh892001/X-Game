package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 世界BOSS托管
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IWorldBossTrust {
	/**
	 * 世界BOSS托管、取消
	 * 
	 * @param type
	 *            0-托管 1-取消
	 * @param yuanbao 元宝
	 */
	public void onTrust(int type, int yuanbao);
}
