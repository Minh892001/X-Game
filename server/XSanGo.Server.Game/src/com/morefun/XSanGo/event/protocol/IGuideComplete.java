/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 完成引导事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IGuideComplete {

	void onGuideCompleted(int guideId);

}
