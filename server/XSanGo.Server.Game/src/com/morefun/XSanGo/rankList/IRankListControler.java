/**
 * 
 */
package com.morefun.XSanGo.rankList;

import com.XSanGo.Protocol.AMD_RankList_selFactionDetail;
import com.XSanGo.Protocol.AMD_RankList_selRoleDetail;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RankListShow;

/**
 * 各种排行榜
 * 
 * @author 吕明涛
 * 
 */
public interface IRankListControler {
		
	/**
	 * 查询 部队战力排名
	 */
	RankListShow selRankListCombat() throws NoteException;
	
	/**
	 * 查询 成就排名
	 */
	RankListShow selRankListAchieve() throws NoteException;
	
	/**
	 * 查询 膜拜次数 排名
	 */
	RankListShow selRankListWorship() throws NoteException;
	
	/**
	 * 查询排行详情
	 * @param ownRank
	 * @param ownValue
	 */
	public void selRoleDetail(AMD_RankList_selRoleDetail __cb, String roleId);
	
	/**
	 * 查询公会排行
	 */
	public RankListShow selRankListFaction() throws NoteException;
	
	/**
	 * 查询公会详情
	 */
	public void selFactionDetail(AMD_RankList_selFactionDetail __cb, String factionId)
			throws NoteException;
	
	void factionChannge(String factionId);
}
