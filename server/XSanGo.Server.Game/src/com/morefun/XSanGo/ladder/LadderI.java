/**
 * 
 */
package com.morefun.XSanGo.ladder;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Ladder_autoFight;
import com.XSanGo.Protocol.AMD_Ladder_beginFight;
import com.XSanGo.Protocol.AMD_Ladder_selectLadder;
import com.XSanGo.Protocol.AMD_Ladder_showRankList;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._LadderDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.fightmovie.FightLifeNumT;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 群雄争霸 接口
 * 
 * @author 吕明涛
 */
public class LadderI extends _LadderDisp {

	/** */
	private static final long serialVersionUID = 1737323012248201853L;

	private IRole roleRt;

	public LadderI(IRole dragonRole) {
		this.roleRt = dragonRole;
	}

	/**
	 * 打开 群雄争霸界面
	 */
	@Override
	public void selectLadder_async(AMD_Ladder_selectLadder __cb,
			Current __current) throws NoteException {
		this.roleRt.getLadderControler().selectLadder(__cb);
	}

	/**
	 * 保存防守队伍
	 */
	@Override
	public void saveGurard(String guardId, Current __current)
			throws NoteException {
		this.roleRt.getLadderControler().saveGuard(guardId);
	}

	/**
	 * 挑战 获取对手数据
	 */
	@Override
	public void beginFight_async(AMD_Ladder_beginFight __cb,
			String formationId, Current __current) throws NoteException {
		FightLifeNumT numT = XsgFightMovieManager.getInstance().getFightLifeT(
				XsgFightMovieManager.Type.Ladder.ordinal());
		if (numT.newBattle == 1) { // 采用新的战斗机制，不应该请求这个接口
			__cb.ice_exception(new NoteException(Messages.getString("ResourceBackControler.invalidParam")));
			return;
		}
		this.roleRt.getLadderControler().beginFight(__cb, formationId);
	}

	/**
	 * 挑战 战斗结束
	 */
	@Override
	public String endFight(String rivalId, int resFlag, byte remainHero,
			Current __current) throws NoteException {
		String resStr = LuaSerializer.serialize(this.roleRt
				.getLadderControler().endFight(rivalId, resFlag, remainHero));

		return resStr;
	}

	/**
	 * 购买挑战令
	 * 
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public void buyChallenge(Current __current) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		this.roleRt.getLadderControler().buyChallenge();
	}

	/**
	 * 根据等级，获得奖励
	 */
	@Override
	public void reward(int awardId, Current __current) throws NoteException {
		this.roleRt.getLadderControler().reward(awardId);
	}

	@Override
	public void showRankList_async(AMD_Ladder_showRankList __cb,
			Current __current) throws NoteException {
		this.roleRt.getLadderControler().showRankList(__cb);
	}

	@Override
	public void autoFight_async(AMD_Ladder_autoFight __cb, Current __current) throws NoteException {
		roleRt.getLadderControler().autoFight(__cb);
	}

}
