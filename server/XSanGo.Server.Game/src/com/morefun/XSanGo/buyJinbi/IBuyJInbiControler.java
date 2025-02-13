/**
 * 
 */
package com.morefun.XSanGo.buyJinbi;

import com.XSanGo.Protocol.BuyJinbiView;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;

/**
 * 点金手
 * 
 * @author 吕明涛
 * 
 */
public interface IBuyJInbiControler {
	
	/**
	 * 查询金币的购买次数
	 */
	String selectBuyNum() throws NoteException;
	
	/**
	 * 元宝购买金币
	 */
	BuyJinbiView buy() throws NoteException,NotEnoughYuanBaoException;
	
}
