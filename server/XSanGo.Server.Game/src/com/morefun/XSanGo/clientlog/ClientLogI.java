package com.morefun.XSanGo.clientlog;

import Ice.Current;

import com.XSanGo.Protocol._ClientLogDisp;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.role.IRole;

/**
 * @author qinguofeng
 */
public class ClientLogI extends _ClientLogDisp {

	private static final long serialVersionUID = 1L;

	private IRole roleRt;

	public ClientLogI(IRole roleRt) {
		this.roleRt = roleRt;
	}

	@Override
	public void report(String type, int lvlId, String params, Current __current) {
		XsgFightMovieManager.getInstance().saveClientLog(roleRt.getAccount(), roleRt.getRoleId(),
				type, lvlId, params);
	}

}
