/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 阵法进阶事件
 * 
 * @author lxm
 *
 */
@signalslot
public interface IBuffAdvance {
	/**
	 * 进阶类型切换、阵法进阶
	 * 
	 * @param type 0-进阶类型切换，1-进阶
	 * @param currentType 当前选择的进阶类型
	 * @param useBuffIds 消耗6级阵法id
	 */
	void onBuffAdvanceChange(int type, int currentType, String useBuffIds);
}
