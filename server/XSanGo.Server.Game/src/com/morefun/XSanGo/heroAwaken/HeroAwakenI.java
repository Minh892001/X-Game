/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: HeroAwakenI
 * 功能描述：
 * 文件名：HeroAwakenI.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import Ice.Current;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._HeroAwakenDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 武将觉醒协议处理
 * 
 * @author zwy
 * @since 2015-11-13
 * @version 1.0
 */
public class HeroAwakenI extends _HeroAwakenDisp {

	private static final long serialVersionUID = 4590056141089356619L;

	private IRole roleRt;

	public HeroAwakenI(IRole roleRt) {
		this.roleRt = roleRt;
	}

	@Override
	public int awakenHero(String heroId, Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		return roleRt.getHeroAwakenController().awakenHero(heroId);
	}

	@Override
	public String heroBaptizeShow(String heroId, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getHeroAwakenController().heroBaptizeShow(heroId));
	}

	@Override
	public String heroBaptize(String heroId, boolean isTenTimes, Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		return LuaSerializer.serialize(roleRt.getHeroAwakenController().heroBaptize(heroId, isTenTimes));
	}

	@Override
	public String baptizeUpgrade(String heroId, Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		return LuaSerializer.serialize(roleRt.getHeroAwakenController().baptizeUpgrade(heroId));
	}

	@Override
	public String baptizeReset(String heroId, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getHeroAwakenController().baptizeReset(heroId));
	}
}
