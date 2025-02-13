package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 寻宝出发
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ITreasureDepart {
	/**
	 * 寻宝出发
	 * @param heroIds 出发武将
	 * @param recommendNum 推荐武将个数
	 */
	public void onDepart(String heroIds, int recommendNum);
}
