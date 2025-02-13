/**
 * 
 */
package com.morefun.XSanGo.trader;

import com.XSanGo.Protocol.DuelReportView;
import com.XSanGo.Protocol.DuelResult;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.TraderView;

/**
 * 神秘商人逻辑控制器
 * 
 * @author sulingyun
 * 
 */
public interface ITraderControler {

	/**
	 * 获取前台视图数据
	 * 
	 * @return
	 */
	TraderView getTraderView();

	/**
	 * 召唤金币神秘商人
	 * 
	 * @return 最新的数据视图
	 * @throws NotEnoughMoneyException
	 * @throws NoteException
	 */
	TraderView callJinbiTrader() throws NotEnoughMoneyException, NoteException;

	/**
	 * 购买物品
	 * 
	 * @param id
	 *            货品编号
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	void buyItem(String id) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException;

	/**
	 * 召唤元宝神秘商人
	 * 
	 * @return 最新的数据视图
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 */
	TraderView callYuanbaoTrader() throws NotEnoughYuanBaoException,
			NoteException;

	/**
	 * 名将探访，金币
	 * 
	 * @return
	 * @throws NotEnoughMoneyException
	 * @throws NoteException
	 */
	TraderView callJinbiHero() throws NotEnoughMoneyException, NoteException;

	/**
	 * 挑战名将
	 * 
	 * @param heroId
	 * @return
	 * @throws NoteException
	 */
	DuelReportView beginChallengeHero(String heroId) throws NoteException;

	/**
	 * 购买名将物品
	 * 
	 * @param id
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	void buyHeroItem(String id) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException;

	/**
	 * 名将探访，元宝
	 * 
	 * @return
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	TraderView callYuanbaoHero() throws NotEnoughYuanBaoException,
			NoteException;

	/**
	 * 领取名将礼物
	 * 
	 * @return
	 */
	ItemView[] acceptConsolation();

	/**
	 * 挑战结束
	 * 
	 * @param result
	 * @throws NoteException
	 */
	void endChallenge(DuelResult result) throws NoteException;

}
