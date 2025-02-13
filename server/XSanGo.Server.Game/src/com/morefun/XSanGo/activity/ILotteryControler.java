package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.GridPageView;
import com.XSanGo.Protocol.LettoryShopView;
import com.XSanGo.Protocol.LotteryCycleView;
import com.XSanGo.Protocol.LotteryScoreRankView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RankPageView;
import com.XSanGo.Protocol.RoleBaseSub;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 大富温 controler
 * 
 * @author sunjie
 */
public interface ILotteryControler extends IRedPointNotable {

	GridPageView gridPageView() throws NoteException;

	LettoryShopView lettoryshopView() throws NoteException;

	RoleBaseSub throwBall(int type,int point) throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException;

	LettoryShopView lettoryshopBuy(int id) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException;
	
	RankPageView lettoryRankView() throws NoteException;
	
	LotteryScoreRankView lotteryScoreRankView() throws NoteException;
	
	LotteryCycleView lotteryCycleView() throws NoteException;
}
