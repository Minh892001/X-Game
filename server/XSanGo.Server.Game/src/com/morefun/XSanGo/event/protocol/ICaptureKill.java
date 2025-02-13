/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.CaptureView;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 俘虏斩杀事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ICaptureKill {

	void onCaptureKilled(int copyId, CaptureView view);

}
