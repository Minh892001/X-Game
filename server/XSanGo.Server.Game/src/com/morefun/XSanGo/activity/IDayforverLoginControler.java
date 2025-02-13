package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.DayforverLoginView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 等级礼包, 和 战力礼包
 * 
 * @author sunjie
 */
public interface IDayforverLoginControler extends IRedPointNotable {

	/**
	 * 请求等级礼包界面
	 * */
	DayforverLoginView getLevelRewardView() throws NoteException;

	/**
	 * 领取等级奖励
	 * */
	void acceptDayLoginReward(int day) throws NoteException, NotEnoughMoneyException;
	
	/**
	 * 是否已经把全部奖励领完了
	 * */
	boolean hasAcceptAll();

	/**
	 * 更新累计登录天数
	 * */
	int updateLoginCount();
}
