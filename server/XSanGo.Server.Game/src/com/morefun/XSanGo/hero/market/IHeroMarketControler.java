/**
 * 
 */
package com.morefun.XSanGo.hero.market;

import com.XSanGo.Protocol.BuyHeroResult;
import com.XSanGo.Protocol.MarketView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 武将抽卡系统控制接口
 * 
 * @author sulingyun
 * 
 */
public interface IHeroMarketControler extends IRedPointNotable {

	/**
	 * 获取前台视图数据
	 * 
	 * @return
	 */
	MarketView getView();

	/**
	 * 十里挑一
	 * 
	 * @return
	 * @throws NotEnoughMoneyException
	 * @throws NoteException
	 */
	BuyHeroResult oneInTen() throws NotEnoughMoneyException, NoteException;

	/**
	 * 十里挑一，十连抽
	 * 
	 * @return
	 * @throws NotEnoughMoneyException
	 * @throws NoteException
	 */
	BuyHeroResult[] oneInTen10() throws NotEnoughMoneyException, NoteException;

	/**
	 * 百里挑一
	 * 
	 * @return
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	BuyHeroResult oneInHundred() throws NotEnoughYuanBaoException,
			NoteException;

	/**
	 * 百里挑一，十连抽
	 * 
	 * @return
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	BuyHeroResult[] oneInHundred10() throws NotEnoughYuanBaoException,
			NoteException;

	/**
	 * 购买限时武将
	 * @return
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	BuyHeroResult[] buyLimitHero(int type) throws NotEnoughYuanBaoException,
			NoteException;
}
