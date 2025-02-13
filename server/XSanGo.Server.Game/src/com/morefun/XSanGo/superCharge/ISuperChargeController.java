package com.morefun.XSanGo.superCharge;

import com.XSanGo.Protocol.AMD_SuperCharge_getRaffleView;
import com.XSanGo.Protocol.AMD_SuperCharge_getReceivedViews;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RaffleReceivedView;
import com.XSanGo.Protocol.ReceivedRaffleInfo;
import com.XSanGo.Protocol.SuperChargeView;
import com.morefun.XSanGo.role.IRedPointNotable;
import com.morefun.XSanGo.vip.ChargeItemT;

public interface ISuperChargeController  extends IRedPointNotable {

	/**
	 * 获取view
	 * @return
	 * @throws NoteException
	 */
	SuperChargeView getSuperChargeView() throws NoteException;

	/**
	 * 领取充值奖励
	 * @param id
	 * @throws NoteException
	 */
	SuperChargeView receiveReward(int id) throws NoteException;
	/**
	 * 根据充值金额重置抽奖次数
	 * @param template 充值项
	 * @param reduce 抽奖次数消耗
	 */
	void refreshRaffleNum(ChargeItemT template,int reduce);
	/**
	 * 领奖物品信息
	 * @return
	 */
	RaffleReceivedView acceptRaffleReward() throws NoteException ;
	/**
	 * 获取已经领取奖励的历史信息
	 * @param __cb 
	 */
	void getReceivedViews(AMD_SuperCharge_getReceivedViews __cb);

	void getRaffleView(AMD_SuperCharge_getRaffleView __cb);
	
	/**
	 * 增加豪华转盘次数
	 * @param num
	 */
	void addRaffleNum(int num);
}
