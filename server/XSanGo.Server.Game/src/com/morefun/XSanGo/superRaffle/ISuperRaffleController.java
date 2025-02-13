package com.morefun.XSanGo.superRaffle;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RaffleReceivedView;
import com.XSanGo.Protocol.RaffleView;
import com.XSanGo.Protocol.ReceivedRaffleInfo;
import com.morefun.XSanGo.role.IRedPointNotable;
/**
 * 该控制器仅用于红点刷新
 * @author xiaojun.zhang
 *
 */
public interface ISuperRaffleController extends IRedPointNotable {

	/**
	 * 获取转盘视图
	 * @return
	 */
	RaffleView getRaffleView();
	/**
	 * 领奖物品信息
	 * @return
	 */
	RaffleReceivedView acceptRaffleReward() throws NoteException ;
	/**
	 * 获取已经领取奖励的历史信息
	 */
	ReceivedRaffleInfo[] getReceivedViews();
}
