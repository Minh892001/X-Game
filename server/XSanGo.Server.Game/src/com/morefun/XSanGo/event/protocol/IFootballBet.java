package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 欧洲杯押注
 * 
 * @author xiongming.li
 *
 */
@signalslot
public interface IFootballBet {
	/**
	 * 欧洲杯押注
	 * @param id 场次
	 * @param num 数量
	 * @param countryId 押注国家,-1是平局
	 */
	public void onFootballBet(int id, int num, int countryId);
}
