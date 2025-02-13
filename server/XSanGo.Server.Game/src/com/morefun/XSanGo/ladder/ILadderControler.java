/**
 * 
 */
package com.morefun.XSanGo.ladder;

import java.util.List;

import com.XSanGo.Protocol.AMD_Ladder_autoFight;
import com.XSanGo.Protocol.AMD_Ladder_beginFight;
import com.XSanGo.Protocol.AMD_Ladder_selectLadder;
import com.XSanGo.Protocol.AMD_Ladder_showRankList;
import com.XSanGo.Protocol.LadderFightResult;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.db.game.RoleLadder;
import com.morefun.XSanGo.db.game.RoleLadderReport;
import com.morefun.XSanGo.role.IRedPointNotable;
import com.morefun.XSanGo.role.IRole;

/**
 * 群雄争霸
 * 
 * @author 吕明涛
 * 
 */
public interface ILadderControler extends IRedPointNotable {

	/**
	 * 打开 群雄争霸界面
	 */
	public void selectLadder(AMD_Ladder_selectLadder __cb) throws NoteException;

	/**
	 * 显示排行榜
	 * 
	 * @param __cb
	 */
	public void showRankList(AMD_Ladder_showRankList __cb) throws NoteException;

	/**
	 * 保存防守队伍
	 */
	public void saveGuard(String guardId) throws NoteException;

	/**
	 * 购买挑战令
	 * 
	 * @param sneerId
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	public void buyChallenge() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 挑战 开始
	 * 
	 * @param formationId
	 * @param formationId
	 * @throws NoteException
	 */
	void beginFight(AMD_Ladder_beginFight __cb, String formationId) throws NoteException;

	/**
	 * 挑战
	 * 
	 * @param rivalId
	 * @throws NoteException
	 * @throws Exception
	 */
	public LadderFightResult endFight(String rivalId, int resFlag, byte remainHero) throws NoteException;

	/**
	 * 群雄争霸 数据库 数据
	 * 
	 * @return
	 */
	public RoleLadder getLadder();

	/**
	 * 群雄争霸 战报 数据库 数据
	 * 
	 * @return
	 */
	public List<RoleLadderReport> getLadderReportList();

	/**
	 * 添加 群雄争霸 战报 数据库 数据<br>
	 * 超过一定数量，删除时间最旧的数据
	 * 
	 * @return
	 */
	public void addReport(RoleLadderReport report);

	/**
	 * 根据等级，获得奖励
	 * 
	 * @param awardId
	 * @throws NoteException
	 */
	public void reward(int awardId) throws NoteException;

	/**
	 * 战斗结果处理<br>
	 * 等级和星级变化<br>
	 * 是否可以领取等级奖励
	 * 
	 * @param resFlag
	 * @return 等级变化 和 星级变化
	 */
	int[] fight(int resFlag);

	/*
	 * 保存战报
	 */
	void saveRport(int resFlag, int levelChange, int starChange, IRole rivalRole, String movieId);

	/**
	 * 赛季结束，清除群雄争霸的数据<br>
	 * 根据上赛季的等级，初始化新赛级的等级
	 */
	public RoleLadder clearLadder();

	/**
	 * 自动生成战报
	 * 
	 * @param __cb
	 * @throws NoteException
	 */
	void autoFight(AMD_Ladder_autoFight __cb);
}
