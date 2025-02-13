package com.morefun.XSanGo.shop;

import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ShopView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 商城控制器
 */
public interface IShopControler extends IRedPointNotable {
	/**
	 * 获取商城商品列表
	 * 
	 * @return
	 */
	public ShopView[] getShopView(int type) throws NoteException;

	/**
	 * 购买商品
	 * 
	 * @param id
	 */
	public void buyItem(int num, String id, int type)
			throws NotEnoughYuanBaoException, NoteException;

	/**
	 * 刷新商城
	 */
	void refreshShop();
}
