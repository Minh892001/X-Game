/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.CaptureView;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 俘虏释放事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ICaptureRelease {

	void onCaptureRelease(int copyId, CaptureView view);

}
