package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ShareSub;
import com.XSanGo.Protocol.ShareView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 分享活动 controler
 * 
 * @author sunjie
 */
public interface IShareControler extends IRedPointNotable {

	ShareView sharePageView() throws NoteException;

	ShareSub share(int id) throws NoteException;

	void update4Login();
}
