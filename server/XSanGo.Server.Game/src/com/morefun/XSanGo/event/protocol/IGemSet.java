package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 宝石镶嵌
 * @author guofeng.qin
 */
@signalslot
public interface IGemSet {
	/**
	 * 宝石镶嵌
	 * */
	void onSetGem(String templateId, int level);
}
