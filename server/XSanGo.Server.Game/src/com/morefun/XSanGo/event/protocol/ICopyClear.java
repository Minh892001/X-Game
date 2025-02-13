/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import java.util.List;

import com.XSanGo.Protocol.CopyChallengeResultView;
import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.copy.SmallCopyT;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 副本扫荡事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ICopyClear {

	void onClear(SmallCopyT copyT, CopyChallengeResultView mockView,
			List<ItemView> additionList);

}
