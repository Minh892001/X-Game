package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 我要做VIP结束答题
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IVipEndAnswer {
	/**
	 * 答题结束
	 * 
	 * @param addVipExp
	 *            得到的VIP经验
	 */
	public void onVipEndAnswer(int addVipExp);

}
