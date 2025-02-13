/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.EmployCaptureResult;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 副本俘虏招募事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ICaptureEmploy {

	void onCaptureEmploy(EmployCaptureResult result);

}
