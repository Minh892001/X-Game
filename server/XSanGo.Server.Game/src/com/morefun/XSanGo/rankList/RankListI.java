/**
 * 
 */
package com.morefun.XSanGo.rankList;

import Ice.Current;

import com.XSanGo.Protocol.AMD_RankList_selFactionDetail;
import com.XSanGo.Protocol.AMD_RankList_selRoleDetail;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._RankListDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 查询各种排行榜 接口
 * 
 * @author 吕明涛
 * 
 */
public class RankListI extends _RankListDisp {

	private static final long serialVersionUID = 6751845654017369598L;

	private IRole roleRt;

	public RankListI(IRole role) {
		this.roleRt = role;
	}

	/**
	 * 查询 部队战力排名
	 */
	@Override
	public String selRankListCombat(Current __current) throws NoteException {
		String resStr = LuaSerializer.serialize(this.roleRt
				.getRankListControler().selRankListCombat());
		return resStr;
	}

	/**
	 * 查询 膜拜次数 排名
	 */
	@Override
	public String selrankListWorship(Current __current) throws NoteException {
		String resStr = LuaSerializer.serialize(this.roleRt
				.getRankListControler().selRankListWorship());

		return resStr;
	}

	/**
	 * 查询排行详情
	 */
	@Override
	public void selRoleDetail_async(AMD_RankList_selRoleDetail __cb,
			String roleId, Current __current) throws NoteException {
		this.roleRt.getRankListControler().selRoleDetail(__cb, roleId);
	}

	/**
	 * 公会排行榜
	 */
	@Override
	public String selRankListFaction(Current __current) throws NoteException {
		String resStr = LuaSerializer.serialize(this.roleRt
				.getRankListControler().selRankListFaction());

		return resStr;
	}

	/**
	 * 公会详情
	 */
	@Override
	public void selFactionDetail_async(AMD_RankList_selFactionDetail __cb,
			String id, Current __current) throws NoteException {
		this.roleRt.getRankListControler().selFactionDetail(__cb, id);
	}

	/**
	 * 查询 成就排名
	 */
	@Override
	public String selRankListAchieve(Current __current) throws NoteException {
		String resStr = LuaSerializer.serialize(this.roleRt
				.getRankListControler().selRankListAchieve());
		return resStr;
	}

}
