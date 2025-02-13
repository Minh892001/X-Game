package com.morefun.XSanGo.treasure;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Treasure_getAccidentFriend;
import com.XSanGo.Protocol.AMD_Treasure_getRescueFriend;
import com.XSanGo.Protocol.AMD_Treasure_rescue;
import com.XSanGo.Protocol.AMD_Treasure_sendRescueMsg;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._TreasureDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class TreasureI extends _TreasureDisp {

	private static final long serialVersionUID = 835900731938959886L;

	private IRole iRole;

	public TreasureI(IRole iRole) {
		this.iRole = iRole;
	}

	@Override
	public String getTreasureView(Current __current) throws NoteException {
		iRole.setAccidentRedPoint(false);// 点开后取消红点
		return LuaSerializer.serialize(iRole.getTreasureControler().getTreasureView());
	}

	@Override
	public String depart(int id, String heroIds, Current __current) throws NoteException {
		iRole.getTreasureControler().depart(id, heroIds);
		return getTreasureView(__current);
	}

	@Override
	public void recall(int id, Current __current) throws NoteException {
		iRole.getTreasureControler().recall(id);
	}

	@Override
	public String gain(int id, Current __current) throws NoteException {
		return LuaSerializer.serialize(iRole.getTreasureControler().gain(id));
	}

	@Override
	public String getRescueLog(Current __current) throws NoteException {
		return LuaSerializer.serialize(iRole.getTreasureControler().getRescueLog());
	}

	@Override
	public String getAccidentLog(Current __current) throws NoteException {
		return LuaSerializer.serialize(iRole.getTreasureControler().getAccidentLog());
	}

	@Override
	public String speed(int id, Current __current) throws NoteException {
		iRole.getTreasureControler().speed(id);
		return getTreasureView(__current);
	}

	@Override
	public void rescue_async(AMD_Treasure_rescue __cb, String friendId, Current __current) throws NoteException {
		iRole.getTreasureControler().rescue(__cb, friendId);
	}

	@Override
	public void getAccidentFriend_async(AMD_Treasure_getAccidentFriend __cb, Current __current) throws NoteException {
		iRole.getTreasureControler().getAccidentFriend(__cb);
	}

	@Override
	public void getRescueFriend_async(AMD_Treasure_getRescueFriend __cb, Current __current) throws NoteException {
		iRole.getTreasureControler().getRescueFriend(__cb);
	}

	@Override
	public void sendRescueMsg_async(AMD_Treasure_sendRescueMsg __cb, String friendIds, Current __current)
			throws NoteException {
		iRole.getTreasureControler().sendRescueMsg(__cb, friendIds);
	}

	@Override
	public void saveRescueMsg(String msg, Current __current) throws NoteException {
		iRole.getTreasureControler().saveRescueMsg(msg);
	}

}
