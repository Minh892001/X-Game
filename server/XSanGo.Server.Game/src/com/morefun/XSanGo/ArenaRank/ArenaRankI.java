/**
 * 
 */
package com.morefun.XSanGo.ArenaRank;

import com.XSanGo.Protocol.AMD_ArenaRank_beginChallenge;
import com.XSanGo.Protocol.AMD_ArenaRank_beginRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_challenge;
import com.XSanGo.Protocol.AMD_ArenaRank_crossFight;
import com.XSanGo.Protocol.AMD_ArenaRank_crossRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_endChallenge;
import com.XSanGo.Protocol.AMD_ArenaRank_endRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_enterCrossArena;
import com.XSanGo.Protocol.AMD_ArenaRank_getCrossMovie;
import com.XSanGo.Protocol.AMD_ArenaRank_getCrossRank;
import com.XSanGo.Protocol.AMD_ArenaRank_getCrossReport;
import com.XSanGo.Protocol.AMD_ArenaRank_getFightMovie;
import com.XSanGo.Protocol.AMD_ArenaRank_refreshCrossRival;
import com.XSanGo.Protocol.AMD_ArenaRank_revenge;
import com.XSanGo.Protocol.AMD_ArenaRank_robFightReport;
import com.XSanGo.Protocol.AMD_ArenaRank_saveBattle;
import com.XSanGo.Protocol.AMD_ArenaRank_selHundredRank;
import com.XSanGo.Protocol.AMD_ArenaRank_selectRank;
import com.XSanGo.Protocol.AMD_ArenaRank_selectRivalRank;
import com.XSanGo.Protocol.Callback_CrossArenaCallback_getCrossMovie;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.NoFactionException;
import com.XSanGo.Protocol.NoGroupException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._ArenaRankDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

import Ice.Current;
import Ice.LocalException;
import Ice.UserException;

/**
 * 竞技场 接口
 * 
 * @author 吕明涛
 * 
 */
public class ArenaRankI extends _ArenaRankDisp {
	/**
	 * 序列化版本
	 */
	private static final long serialVersionUID = -8512497905937260469L;

	private IRole roleRt;

	public ArenaRankI(IRole roleRt) {
		this.roleRt = roleRt;
	}

	/**
	 * 竞技场排名查询
	 */
	@Override
	public void selectRank_async(AMD_ArenaRank_selectRank __cb, Current __current) throws NoteException {
		this.roleRt.getArenaRankControler().selectRank(__cb);
	}

	/**
	 * 刷新对手排行榜
	 */
	@Override
	public void selectRivalRank_async(AMD_ArenaRank_selectRivalRank __cb, Current __current) throws NoteException {
		this.roleRt.getArenaRankControler().selectRivalRank(__cb);
	}

	/**
	 * 保存防守队伍
	 */
	@Override
	public void saveGuard(String guardId, Current __current) throws NoteException {
		this.roleRt.getArenaRankControler().saveGuard(guardId);
	}

	/**
	 * 设置嘲讽模式
	 * 
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public void setSneer(int sneerId, String sneerStr, Current __current) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		this.roleRt.getArenaRankControler().setSneer(sneerId, sneerStr);
	}

	/**
	 * 购买挑战令
	 */
	@Override
	public void buyChallenge(Current __current) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		this.roleRt.getArenaRankControler().buyChallenge();
	}

	/**
	 * 排名榜显示,显示1到100名
	 */
	@Override
	public void selHundredRank_async(AMD_ArenaRank_selHundredRank __cb, Current __current) throws NoteException {
		this.roleRt.getArenaRankControler().selHundredRank(__cb);
	}

	/**
	 * 排行兑换列表
	 */
	@Override
	public String selMallList(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getArenaRankControler().selMallList());
	}

	/**
	 * 刷新列表
	 */
	@Override
	public String refMallList(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getArenaRankControler().refMallList());
	}

	/**
	 * 兑换, 目前数量是全部兑换，预留参数
	 */
	@Override
	public String exchangeItem(int itemId, Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getArenaRankControler().exchangeItem(itemId));
	}

	/**
	 * 排名赛 战报列表
	 */
	@Override
	public void robFightReport_async(AMD_ArenaRank_robFightReport __cb, Current __current) throws NoteException {
		this.roleRt.getArenaRankControler().rankFightReport(__cb);
	}

	/**
	 * 获取对手数据 , targetId : 对手ID formationId : 阵容ID
	 * 
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public void beginChallenge_async(AMD_ArenaRank_beginChallenge __cb, String targetId, String formationId,
			Current __current) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		this.roleRt.getArenaRankControler().beginFight(__cb, targetId, formationId);
	}

	/**
	 * 战斗结束，结果通知，
	 * 
	 * @param targetId
	 *            : 对手ID
	 * @param resFlag
	 *            : 战斗结果 ，0:失败，1：胜利
	 * @return 每日首胜 和 历史最大排名 的 奖励
	 * @throws Exception
	 */
	@Override
	public void endChallenge_async(AMD_ArenaRank_endChallenge __cb, String targetId, int resFlag, byte remainHero,
			Ice.Current __current) throws NoteException {
		this.roleRt.getArenaRankControler().fight(__cb, targetId, resFlag, remainHero);
	}

	/**
	 * 炫耀 reportId:战报ID，channel：聊天频道，content：炫耀的字符
	 * 
	 * @throws NoFactionException
	 * @throws NoGroupException
	 */
	@Override
	public void strutReport(String reportId, int channelType, String targetId, String content, Current __current)
			throws NoteException, NoGroupException, NoFactionException {
		this.roleRt.getArenaRankControler().strutReport(reportId, channelType, targetId, content);
	}

	/**
	 * 复仇 获取对手数据
	 */
	@Override
	public void beginRevenge_async(AMD_ArenaRank_beginRevenge __cb, String targetId, String formationId,
			Current __current) throws NoteException {
		IArenaRankControler rankControler = this.roleRt.getArenaRankControler();
		rankControler.beginRevenge(targetId, __cb, formationId);
	}

	/**
	 * 复仇 战斗结束，结果通知
	 */
	@Override
	public void endRevenge_async(AMD_ArenaRank_endRevenge __cb, String targetId, int resFlag, byte remainHero,
			Ice.Current __current) throws NoteException {
		this.roleRt.getArenaRankControler().endRevenge(targetId, resFlag, remainHero, __cb);
	}

	/**
	 * 清除挑战CD时间
	 */
	@Override
	public void clearCD(Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		this.roleRt.getArenaRankControler().clearCD();
	}

	@Override
	public void uploadFightMovie(String id, FightMovieView movie, Current __current) throws NoteException {
		roleRt.getArenaRankControler().saveFightMovie(id, movie);
	}

	@Override
	public void getFightMovie_async(AMD_ArenaRank_getFightMovie __cb, String id, Current __current)
			throws NoteException {
		roleRt.getArenaRankControler().getFightMovie(id, __cb);
	}

	@Override
	public void challenge_async(AMD_ArenaRank_challenge __cb, String targetId, String formationId, Current __current)
			throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		roleRt.getArenaRankControler().autoFight(__cb, targetId, formationId);
	}

	@Override
	public void revenge_async(AMD_ArenaRank_revenge __cb, String targetId, String formationId, Current __current)
			throws NoteException {
		roleRt.getArenaRankControler().autoRevenge(targetId, __cb, formationId);
	}

	@Override
	public void enterCrossArena_async(AMD_ArenaRank_enterCrossArena __cb, Current __current) throws NoteException {
		roleRt.getCrossArenaControler().enterCrossArena(__cb);
	}

	@Override
	public void refreshCrossRival_async(AMD_ArenaRank_refreshCrossRival __cb, Current __current) throws NoteException {
		roleRt.getCrossArenaControler().refreshCrossRival(__cb);
	}

	@Override
	public void getCrossRank_async(AMD_ArenaRank_getCrossRank __cb, Current __current) throws NoteException {
		roleRt.getCrossArenaControler().getCrossRank(__cb);
	}

	@Override
	public void setSignature(String signature, Current __current) throws NoteException {
		roleRt.getCrossArenaControler().setSignature(signature);
	}

	@Override
	public void saveBattle_async(AMD_ArenaRank_saveBattle __cb, Current __current) throws NoteException {
		roleRt.getCrossArenaControler().saveBattle(__cb);
	}

	@Override
	public void buyCrossChallenge(Current __current) throws NoteException {
		roleRt.getCrossArenaControler().buyCrossChallenge();
	}

	@Override
	public void getCrossReport_async(AMD_ArenaRank_getCrossReport __cb, Current __current) throws NoteException {
		roleRt.getCrossArenaControler().getCrossReport(__cb);
	}

	@Override
	public void crossFight_async(AMD_ArenaRank_crossFight __cb, String rivalId, Current __current) throws NoteException {
		roleRt.getCrossArenaControler().crossFight(__cb, rivalId);
	}

	@Override
	public void crossRevenge_async(AMD_ArenaRank_crossRevenge __cb, String rivalId, Current __current)
			throws NoteException {
		roleRt.getCrossArenaControler().crossRevenge(__cb, rivalId);
	}

	@Override
	public void getCrossMovie_async(final AMD_ArenaRank_getCrossMovie __cb, String id, Current __current)
			throws NoteException {
		if (!CrossArenaManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
			return;
		}
		CrossArenaManager.getInstance().getCrossArenaCbPrx()
				.begin_getCrossMovie(id, new Callback_CrossArenaCallback_getCrossMovie() {

					@Override
					public void exception(LocalException __ex) {
						__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.movieOut")));
					}

					@Override
					public void response(FightMovieView __ret) {
						__cb.ice_response(new FightMovieView[] { __ret });
					}

					@Override
					public void exception(UserException __ex) {
						__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
					}
				});
	}

	@Override
	public void clearCrossCD(Current __current) throws NoteException {
		roleRt.getCrossArenaControler().clearCrossCD();
	}

}
