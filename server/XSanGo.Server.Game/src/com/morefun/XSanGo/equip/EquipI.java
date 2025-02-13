package com.morefun.XSanGo.equip;

import Ice.Current;

import com.XSanGo.Protocol.EquipLevelEntity;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._EquipDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

public class EquipI extends _EquipDisp {
	private static final long serialVersionUID = 84815338708802882L;
	private IRole roleRt;

	public EquipI(IRole role) {
		this.roleRt = role;
	}

	/**
	 * 
	 */
	@Override
	public ItemView levelup(String id, Current __current) throws NoteException, NotEnoughMoneyException {
		this.roleRt.getItemControler().levelupEquip(id);
		ItemView view = this.getEquip(id).getView();
		return view;
	}

	@Override
	public ItemView levelupAuto(String id, Current __current) throws NoteException, NotEnoughMoneyException {
		this.roleRt.getItemControler().levelupEquipAuto(id);
		ItemView view = this.getEquip(id).getView();
		return view;
	}

	@Override
	public EquipLevelEntity[] levelupAll(String id, Current __current) throws NoteException, NotEnoughMoneyException {
		try {
			this.roleRt.getNotifyControler().setAutoNotify(false);
			EquipLevelEntity[] rs = this.roleRt.getItemControler().levelupEquipAll(id);
			return rs;
		} finally {
			this.roleRt.getNotifyControler().setAutoNotify(true);
		}
	}

	@Override
	public String starUp(String id, String idArray, String items, Current __current) throws NoteException,
			NotEnoughMoneyException {
		this.roleRt.getItemControler().starUpEquip(id, idArray, items);
		ItemView v = this.getEquip(id).getView();

		return TextUtil.GSON.toJson(v);
	}

	private EquipItem getEquip(String id) {
		return this.roleRt.getItemControler().getEquipItem(id);
	}

	@Override
	public ItemView rebuild(String id, Current __current) throws NoteException {
		this.roleRt.getItemControler().rebuildEquip(id);

		return this.getEquip(id).getView();
	}

	@Override
	public ItemView[] smelt(String idArrayStr, Current __current) throws NotEnoughMoneyException, NoteException {
		return this.roleRt.getItemControler().smeltEquip(idArrayStr);
	}

	@Override
	public String hole(String equipId, int position, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getItemControler().equipHole(equipId, position));
	}

	@Override
	public void setGem(String equipId, int position, String gemId, Current __current) throws NoteException {
		roleRt.getItemControler().setGem(equipId, position, gemId);
	}

	@Override
	public void removeGem(String equipId, int position, Current __current) throws NoteException {
		roleRt.getItemControler().removeGem(equipId, position);
	}

	@Override
	public String getAllArtifact(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getArtifactControler().getAllArtifact());
	}

	@Override
	public void upgradeArtifact(String dbId, Current __current) throws NoteException {
		roleRt.getArtifactControler().upgradeArtifact(dbId);
	}

	@Override
	public void useArtifact(String dbId, String heroId, Current __current) throws NoteException {
		roleRt.getArtifactControler().useArtifact(dbId, heroId);
	}

}
