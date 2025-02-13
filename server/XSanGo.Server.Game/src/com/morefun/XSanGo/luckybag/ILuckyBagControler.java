package com.morefun.XSanGo.luckybag;

import com.XSanGo.Protocol.LuckyBagView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface ILuckyBagControler extends IRedPointNotable {
	
	/**
	 * 获取福袋view
	 * @return
	 * @throws NoteException
	 */
	LuckyBagView getLuckBagView() throws NoteException;

	/**
	 * 领取当日充值奖励
	 * @param id
	 * @throws NoteException
	 */
	void receiveDayBag(int id) throws NoteException;

	/**
	 * 领取当月充值天数奖励
	 * @param id
	 * @throws NoteException
	 */
	void receiveMonthBag(int id) throws NoteException;
}
