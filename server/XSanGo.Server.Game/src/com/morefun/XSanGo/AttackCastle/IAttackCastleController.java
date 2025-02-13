package com.morefun.XSanGo.AttackCastle;

import com.XSanGo.Protocol.AcceptRewardResultView;
import com.XSanGo.Protocol.AttackCastleShopView;
import com.XSanGo.Protocol.AttackCastleView;
import com.XSanGo.Protocol.CastleNodeView;
import com.XSanGo.Protocol.CastleOpponentView;
import com.XSanGo.Protocol.ClearResultView;
import com.XSanGo.Protocol.EndAttackCastleView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.AttackCastle.AttackCastleController.Callback;

/**
 *
 * @author qinguofeng
 * @date Jan 27, 2015
 */
public interface IAttackCastleController {

	/**
	 * 请求开始北伐
	 * 
	 * @return {@link AttackCastleView}
	 * */
	AttackCastleView requestAttackCastles() throws NoteException;

	/**
	 * 重置北伐
	 * 
	 * @return {@link AttackCastleView}
	 * */
	AttackCastleView resetAttackCastles() throws NoteException;

	/**
	 * 获取关卡信息
	 * 
	 * @return {@link CastleOpponentView}
	 * */
	void getCastleOpponentView(int castleNodeId, final Callback cbfunc)
			throws NoteException;

	/**
	 * 开始某一关卡
	 * 
	 * @param castleNodeIndex
	 *            关卡ID
	 * @return {@link CastleNodeView}
	 * */
	CastleNodeView beginAttackCastle(int castleNodeId) throws NoteException;
	
	/**
	 * 退出一个关卡
	 * */
	void exitAttackCastle(int castleNodeId) throws NoteException;

	/**
	 * 结束一个关卡
	 * 
	 * @param remainHero
	 *            剩余武将数量
	 * @param castleNodeIndex
	 *            关卡ID
	 * @return {@link EndAttackCastleView}
	 * */
	EndAttackCastleView endAttackCastle(int castleNodeId, byte remainHero)
			throws NoteException;

	/**
	 * 领取奖励
	 * 
	 * @param rewards
	 *            领取的奖励
	 * */
	AcceptRewardResultView acceptRewards(int castleNodeId, int starCount)
			throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException;

	/**
	 * 获取商城商品列表
	 * 
	 * @return {@link AttackCastleShopView}[]
	 * */
	AttackCastleShopView getShopRewardList() throws NoteException;

	/**
	 * 刷新商城商品列表
	 * 
	 * @return {@link AttackCastleShopView}[]
	 * */
	AttackCastleShopView refreshShopRewardList() throws NoteException;

	/**
	 * 兑换商品
	 * 
	 * @param itemId
	 *            商品ID
	 * */
	void exchangeItem(int itemId) throws NoteException;

	/**
	 * 获取北伐声望
	 * 
	 * @return
	 */
	int getCoin();
	
	/**
	 * 扫荡
	 * */
	ClearResultView clear() throws NoteException;
	
	/**
	 * 换人
	 * */
	void refreshOpponent(int castleNodeId, final Callback cbfunc) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 获取总星数
	 * */
	int getTotalStar();

	/**
	 * 增加声望
	 * */
	void addCoin(int num);
}
