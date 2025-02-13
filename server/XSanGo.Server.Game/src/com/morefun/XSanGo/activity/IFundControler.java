package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.FundView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 成长基金 controler
 * 
 * @author qinguofeng
 */
public interface IFundControler extends IRedPointNotable {
	/**
	 * 请求成长基金界面
	 * */
	FundView getFundView() throws NoteException;

	/**
	 * 领取某个等级的成长基金奖励
	 * */
	void acceptFundReward(int level) throws NoteException;

	/**
	 * 购买成长基金
	 * */
	void buyFund() throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException;
	
	/**
	 * 是否已经把全部奖励领完了
	 * */
	boolean hasAcceptAll();
	
}
