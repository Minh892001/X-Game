package com.morefun.XSanGo.goodsExchange;

import com.XSanGo.Protocol.ExchangeResult;
import com.XSanGo.Protocol.ExchangeView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface IExchangeActivityControler  extends IRedPointNotable {

	/**
	 * 获取兑换列表
	 * @param itemType 兑换物品类型
	 * @return
	 */
	ExchangeView[] getExchangeViews(int itemType);
	
	/**
	 * 物品兑换 兑换成功返回1，失败返回0
	 * @param exchangeNo 兑换编号
	 * @param itemType 兑换物类型
	 */
	ExchangeResult doExchange(String exchangeNo, int itemType)throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException;
	
	/**
	 * 限时活动设置是否首次打开，显示红点
	 * @param value
	 */
	public void setFirstOpen(boolean value);

}
