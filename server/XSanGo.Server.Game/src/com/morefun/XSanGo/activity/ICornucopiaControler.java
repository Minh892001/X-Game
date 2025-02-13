package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.CornucopiaView;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;
/**
 * 聚宝盆
 * 
 * @author lixiongming
 *
 */
public interface ICornucopiaControler extends IRedPointNotable {

	/**
	 * 获取聚宝盆view
	 * 
	 * @return
	 * @throws NoteException
	 */
	CornucopiaView getCornucopiaView() throws NoteException;

	/**
	 * 一键购买聚宝盆物品
	 * 
	 * @throws NoteException
	 */
	void buyAllCornucopia() throws NoteException, NotEnoughYuanBaoException;

	/**
	 * 购买聚宝盆物品
	 * 
	 * @param id
	 * @throws NoteException
	 */
	void buyCornucopia(int id) throws NoteException, NotEnoughYuanBaoException;

	/**
	 * 领取超值物品
	 * 
	 * @throws NoteException
	 */
	void getSupperCornucopia() throws NoteException;

	/**
	 * 领取聚宝盆物品
	 * 
	 * @param id
	 * @throws NoteException
	 */
	void getCornucopia(int id) throws NoteException;
	
	/**
	 * 一键领取聚宝盆物品
	 * 
	 * @param id
	 * @throws NoteException
	 */
	void getAllCornucopia() throws NoteException;
	
	/**
	 * 首次打开，显示红点
	 * @param value
	 */
	void setFirstOpen(boolean value);

}
