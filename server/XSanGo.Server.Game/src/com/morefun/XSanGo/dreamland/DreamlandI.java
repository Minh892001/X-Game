/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandI
 * 功能描述：
 * 文件名：DreamlandI.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import Ice.Current;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._DreamlandDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 南华幻境协议入口
 * 
 * @author weiyi.zhao
 * @since 2016-4-21
 * @version 1.0
 */
public class DreamlandI extends _DreamlandDisp {
	private static final long serialVersionUID = -1457152598734679580L;

	/** 角色接口 */
	private IRole roleRt;

	/**
	 * 构造函数
	 * 
	 * @param roleRt
	 */
	public DreamlandI(IRole roleRt) {
		this.roleRt = roleRt;
	}

	@Override
	public String dreamlandPage(int groupId, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().dreamlandPage(groupId));
	}

	@Override
	public String dreamlandSwitchSceneGroup(int groupId, boolean isFront, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().dreamlandSwitchSceneGroup(groupId, isFront));
	}

	@Override
	public String beginDreamland(int sceneId, Current __current) throws NoteException {
		return roleRt.getDreamlandController().beginDreamland(sceneId);
	}

	@Override
	public String endDreamland(int sceneId, byte remainHero, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().endDreamland(sceneId, remainHero));
	}

	@Override
	public String dreamlandSweep(int sceneId, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().dreamlandSweep(sceneId));
	}

	// @Override
	// public String drawSceneStarAward(int sceneId, Current __current) throws
	// NoteException {
	// return
	// LuaSerializer.serialize(roleRt.getDreamlandController().drawSceneStarAward(sceneId));
	// }

	@Override
	public String lookDreamlandRank(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().lookDreamlandRank());
	}

	@Override
	public String dreamlandAwardPage(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().dreamlandAwardPage());
	}

	@Override
	public String drawStarAward(int star, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().drawStarAward(star));
	}

	@Override
	public String dreamlandShopPage(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().dreamlandShopPage());
	}

	@Override
	public String dreamlandRefreshShop(Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().dreamlandRefreshShop());
	}

	@Override
	public String buyDreamlandShopItem(int id, Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		return LuaSerializer.serialize(roleRt.getDreamlandController().buyDreamlandShopItem(id));
	}

	@Override
	public int buyChallengeNum(Current __current) throws NotEnoughYuanBaoException, NoteException {
		return roleRt.getDreamlandController().buyChallengeNum();
	}
}
