/**
 * 
 */
package com.morefun.XSanGo.itemChip;

import Ice.Current;

import com.XSanGo.Protocol.HeroResetView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._ItemChipDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 碎片的 合成、掠夺、复仇抢回碎片 接口
 * 
 * @author 吕明涛
 * 
 */
public class ItemChipI extends _ItemChipDisp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7660418146518454141L;

	private IRole roleRt;

	public ItemChipI(IRole dragonRole) {
		this.roleRt = dragonRole;
	}

	/**
	 * 碎片合成
	 */
	@Override
	public String compoundChip(String itemId, Current __current) throws NoteException {
		return compoundChipWithExtraId(itemId, null, __current);
	}

	@Override
	public String compoundChipWithExtraId(String itemId, String extraId, Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getItemChipControler().compoundChip(roleRt, itemId, extraId));
	}

	/**
	 * 炫耀合成后装备
	 */
	@Override
	public void strutItem(String itemId, Current __current) throws NoteException {
		this.roleRt.getItemChipControler().strutItem(itemId);
	}

	@Override
	public String heroReset(String heroId, int isPay, Current __current) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		return LuaSerializer.serialize(roleRt.getItemChipControler().heroReset(heroId, isPay));
	}

	@Override
	public String requestHeroReset(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getItemChipControler().requestHeroReset().toArray(new HeroResetView[0]));
	}

	@Override
	public void heroInherit(String inheritHeroId, String baseHeroId, Current __current) throws NoteException {
		roleRt.getItemChipControler().heroInherit(inheritHeroId, baseHeroId);
	}

	@Override
	public String requestHeroInherit(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getItemChipControler().requestHeroInherit());
	}

	@Override
	public String compoundGem(String itemId, int num, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getItemChipControler().compoundGem(itemId, num));
	}
}
