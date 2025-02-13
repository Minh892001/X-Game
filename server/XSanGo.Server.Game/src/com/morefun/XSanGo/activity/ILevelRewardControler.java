package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.LevelRewardView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 等级礼包, 和 战力礼包
 * 
 * @author qinguofeng
 */
public interface ILevelRewardControler extends IRedPointNotable {

	/**
	 * 请求等级礼包界面
	 * */
	LevelRewardView getLevelRewardView() throws NoteException;

	/**
	 * 领取等级奖励
	 * */
	void acceptLevelReward(int level) throws NoteException, NotEnoughMoneyException;
	
	/**
	 * 是否已经把全部奖励领完了
	 * */
	boolean hasAcceptAll();

}
