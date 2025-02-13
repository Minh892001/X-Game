package com.morefun.XSanGo.haoqingbao;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Haoqingbao_checkout;
import com.XSanGo.Protocol.AMD_Haoqingbao_getRedPacketDetail;
import com.XSanGo.Protocol.AMD_Haoqingbao_myRedPacket;
import com.XSanGo.Protocol.AMD_Haoqingbao_openHaoqingbao;
import com.XSanGo.Protocol.AMD_Haoqingbao_rankList;
import com.XSanGo.Protocol.AMD_Haoqingbao_sendRedPacket;
import com.XSanGo.Protocol.HaoqingbaoView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._HaoqingbaoDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * @author guofeng.qin
 */
public class HaoqingbaoI extends _HaoqingbaoDisp {
	private static final long serialVersionUID = -6688590799856173071L;

	private IRole roleRt;

	public HaoqingbaoI(IRole roleRt) {
		this.roleRt = roleRt;
	}

	@Override
	public void openHaoqingbao_async(AMD_Haoqingbao_openHaoqingbao __cb, Current __current) throws NoteException {
		HaoqingbaoView view = roleRt.getHaoqingbaoController().openHaoqingbaoView();
		if (view != null) {
			__cb.ice_response(LuaSerializer.serialize(view));
		} else {
			__cb.ice_exception(new NoteException("此功能暂未开启"));
		}
	}

	@Override
	public String recvRedPacket(String packetId, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getHaoqingbaoController().recvRedPacket(packetId));
	}

	@Override
	public void getRedPacketDetail_async(AMD_Haoqingbao_getRedPacketDetail __cb, String packetId, Current __current)
			throws NoteException {
		roleRt.getHaoqingbaoController().getRedPacketDetail(packetId, __cb);
	}

	@Override
	public void myRedPacket_async(AMD_Haoqingbao_myRedPacket __cb, Current __current) throws NoteException {
		__cb.ice_response(LuaSerializer.serialize(roleRt.getHaoqingbaoController().myRedPacket()));
	}

	@Override
	public void sendRedPacket_async(AMD_Haoqingbao_sendRedPacket __cb, int type, int minLevel, int minVipLevel,
			int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, Current __current)
			throws NoteException {
		roleRt.getHaoqingbaoController().sendRedPacket(type, minLevel, minVipLevel, range, minFriendPoint,
				totalYuanbaoNum, packetNum, msg);
		__cb.ice_response(LuaSerializer.serialize(roleRt.getHaoqingbaoController().openHaoqingbaoView()));
	}

	@Override
	public void checkout_async(AMD_Haoqingbao_checkout __cb, int num, Current __current) throws NoteException {
		roleRt.getHaoqingbaoController().checkout(num);
		__cb.ice_response(LuaSerializer.serialize(roleRt.getHaoqingbaoController().openHaoqingbaoView()));
	}

	@Override
	public void rankList_async(AMD_Haoqingbao_rankList __cb, int type, Current __current) throws NoteException {
		roleRt.getHaoqingbaoController().getRankView(type, __cb);
	}

	@Override
	public void charge(Current __current) throws NoteException {
		roleRt.getHaoqingbaoController().updateChargeStatus();
	}

	@Override
	public String preRecvRedPacket(String packetId, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getHaoqingbaoController().preRecvRedPacket(packetId));
	}

	@Override
	public void claimRedPacket(String roleId, String packetId, Current __current) throws NoteException {
		roleRt.getHaoqingbaoController().claimRedPacket(roleId, packetId);
	}
}
