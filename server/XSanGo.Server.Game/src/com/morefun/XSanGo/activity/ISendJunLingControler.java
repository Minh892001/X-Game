package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SendJunLingView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * @author qinguofeng
 */
public interface ISendJunLingControler extends IRedPointNotable {

	/**
	 * 请求送军令页面
	 * */
	SendJunLingView getSendJunLingView() throws NoteException;

	/**
	 * 领取奖励
	 * */
	int acceptJunLing(int id) throws NoteException, NotEnoughYuanBaoException;

	/**
	 * 奖励是否已经领完了
	 * */
	boolean hasAcceptAll();
}
