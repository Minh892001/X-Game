/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandBuyChallengeNum
 * 功能描述：
 * 文件名：IDreamlandBuyChallengeNum.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 每日挑战次数购买
 * 
 * @author weiyi.zhao
 * @since 2016-5-16
 * @version 1.0
 */
@signalslot
public interface IDreamlandBuyChallengeNum {

	/**
	 * 购买挑战次数事件
	 * 
	 * @param challengeNum
	 */
	void onBuyChallengeNum(int challengeNum);
}
