package com.morefun.XSanGo.smithyExchange;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Preview;
import com.XSanGo.Protocol.SmithyMallSel;

/**
 * 铁匠铺兑换控制器接口
 * @author xiaojun.zhang
 *
 */
public interface ISmithyExchangeController {

	/**
	 * 兑换列表 和 刷新列表
	 * 
	 * @return
	 */
	public SmithyMallSel selMallList();
	
	/**
	 * 刷新列表
	 * 
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException 
	 */
	public SmithyMallSel refMallList() throws NoteException, NotEnoughYuanBaoException;

	/**
	 * 兑换, 目前数量是全部兑换，预留参数
	 * 
	 * @throws NoteException
	 */
	public SmithyMallSel exchangeItem(int storId) throws NoteException;
	
	/**
	 * 蓝装兑换列表
	 * @return
	 */
	public SmithyMallSel selBlueMallList();
	
	
	/**
	 * 蓝装刷新列表
	 * 
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException 
	 */
	public SmithyMallSel refBlueMallList() throws NoteException, NotEnoughMoneyException;

	/**
	 * 蓝装兑换, 目前数量是全部兑换，预留参数
	 * 
	 * @throws NoteException
	 */
	public SmithyMallSel exchangeBlueItem(int storId) throws NoteException;
	
	/**
	 * 获取紫装兑换预览图鉴
	 * 
	 * @throws NoteException
	 */
	public Preview[] getPreview() throws NoteException;
	
}
