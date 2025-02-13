package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PowerRewardView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 战力嘉奖
 * 
 * @author qinguofeng
 */
public interface IPowerRewardControler extends IRedPointNotable {

	/**
	 * 请求战力嘉奖界面
	 * */
	PowerRewardView getPowerRewardView() throws NoteException;

	/**
	 * 领取战力嘉奖
	 * */
	void acceptPowerReward(int power) throws NoteException;
	
	/**
	 * 是否已经把全部奖励领完了
	 * */
	boolean hasAcceptAll();
	
}
