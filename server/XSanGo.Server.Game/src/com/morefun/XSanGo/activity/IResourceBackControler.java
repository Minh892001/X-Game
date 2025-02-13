package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ResourceBackView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * @author guofeng.qin
 */
public interface IResourceBackControler extends IRedPointNotable {
	ResourceBackView getResourceBackView() throws NoteException;

	void accept(String date, int type, int slot)
			throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	void acceptOneKey(String date) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	void addLoginStatus();
}
