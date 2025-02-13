/**
 * 
 */
package com.morefun.XSanGo.onlineAward;

import Ice.Current;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._OnlineAwardDisp;
import com.morefun.XSanGo.monitor.XsgMonitorManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 在线礼包 接口
 * 
 * @author 吕明涛
 * 
 */
public class OnlineAwardI extends _OnlineAwardDisp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5484386379706116358L;
	
	private IRole roleRt;

	public OnlineAwardI(IRole dragonRole) {
		this.roleRt = dragonRole;
	}


	@Override
	public String selectOnlineTime(Current __current) throws NoteException {
		String resStr = LuaSerializer.serialize(this.roleRt.getOnlineAwardControler().selectOnlineTime());
		
		return resStr;
	}


	@Override
	public String getAward(Current __current) throws NoteException {		
		String resStr = LuaSerializer.serialize(this.roleRt.getOnlineAwardControler().getAward());
		
		return resStr;
	}

}
