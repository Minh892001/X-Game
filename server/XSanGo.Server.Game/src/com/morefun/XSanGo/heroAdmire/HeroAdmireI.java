/**
 * 
 */
package com.morefun.XSanGo.heroAdmire;

import java.util.Date;

import Ice.Current;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._HeroAdmireDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 名将仰慕 接口
 * 
 * @author 吕明涛
 */
public class HeroAdmireI extends _HeroAdmireDisp {

	/** */
	private static final long serialVersionUID = -8610865747759872991L;

	private IRole roleRt;

	public HeroAdmireI(IRole role) {
		this.roleRt = role;
	}

	/**
	 * 显示名将仰慕界面
	 */
	@Override
	public String selectAdmireShow(Current __current) throws NoteException {
		roleRt.getRoleOpenedMenu().setOpenHeroAdmireDate(new Date());
		return LuaSerializer.serialize(roleRt.getHeroAdmireControler().selectAdmireShow());
	}

	/**
	 * 选择名将
	 */
	@Override
	public void chooseHero(int heroId, Current __current) throws NoteException {
		roleRt.getHeroAdmireControler().chooseHero(heroId);
	}

	/**
	 * 更换名将
	 */
	@Override
	public void exchangeHero(int heroId, Current __current) throws NoteException {
		roleRt.getHeroAdmireControler().exchangeHero(heroId);
	}

	/**
	 * 清空 名将
	 */
	@Override
	public void clearHero(Current __current) throws NoteException {
		roleRt.getHeroAdmireControler().clearHero();
	}

	/**
	 * 仰慕 名将
	 * 
	 * @return 仰慕之后，名将的仰慕值
	 */
	@Override
	public int presentHero(int id, Current __current) throws NoteException {
		return roleRt.getHeroAdmireControler().presentHero(id);
	}

	/**
	 * 召唤名将
	 */
	@Override
	public int summonHero(Current __current) throws NoteException {
		return roleRt.getHeroAdmireControler().summonHero();
	}

}
