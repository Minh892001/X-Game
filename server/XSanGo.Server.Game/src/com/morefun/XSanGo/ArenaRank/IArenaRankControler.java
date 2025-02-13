/**
 * 
 */
package com.morefun.XSanGo.ArenaRank;

import com.XSanGo.Protocol.AMD_ArenaRank_beginChallenge;
import com.XSanGo.Protocol.AMD_ArenaRank_beginRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_challenge;
import com.XSanGo.Protocol.AMD_ArenaRank_endChallenge;
import com.XSanGo.Protocol.AMD_ArenaRank_endRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_getFightMovie;
import com.XSanGo.Protocol.AMD_ArenaRank_revenge;
import com.XSanGo.Protocol.AMD_ArenaRank_robFightReport;
import com.XSanGo.Protocol.AMD_ArenaRank_selHundredRank;
import com.XSanGo.Protocol.AMD_ArenaRank_selectRank;
import com.XSanGo.Protocol.AMD_ArenaRank_selectRivalRank;
import com.XSanGo.Protocol.ArenaMallSel;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.NoFactionException;
import com.XSanGo.Protocol.NoGroupException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RivalRank;
import com.morefun.XSanGo.db.game.ArenaRank;
import com.morefun.XSanGo.db.game.ArenaRankFight;
import com.morefun.XSanGo.db.game.RoleArenaRank;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.role.IRedPointNotable;
import com.morefun.XSanGo.role.IRole;

/**
 * 竞技场 功能控制器
 * 
 * @author 吕明涛
 * 
 */
public interface IArenaRankControler extends IRedPointNotable {

	/**
	 * 竞技场排名查询
	 */
	void selectRank(final AMD_ArenaRank_selectRank __cb) throws NoteException;

	/**
	 * 刷新对手排行榜
	 */
	void selectRivalRank(final AMD_ArenaRank_selectRivalRank __cb) throws NoteException;

	/**
	 * 挑战 开始
	 * 
	 * @param formationId
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	void beginFight(AMD_ArenaRank_beginChallenge __cb, String targetId, String formationId) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 挑战
	 * 
	 * @param rivalId
	 * @throws NoteException
	 * @throws Exception
	 */
	public void fight(final AMD_ArenaRank_endChallenge __cb, final String rivalId, final int resFlag,
			final byte remainHero) throws NoteException;

	/**
	 * 保存防守队伍
	 * 
	 * @param guardId
	 * @throws NoteException
	 */
	public void saveGuard(String guardId) throws NoteException;

	/**
	 * 设置嘲讽模式
	 * 
	 * @param sneerId
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	public void setSneer(int sneerId, String sneerStr) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException;

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
	 * 查询竞技场的 用户数据
	 * 
	 * @return
	 */
	public RoleArenaRank getRoleArenaRank();

	/**
	 * 查询竞技场的 全局数据
	 * 
	 * @return
	 */
	public ArenaRank getArenaRank();

	/**
	 * 保存 角色 竞技场 数据
	 * 
	 * @param arenaRank
	 */
	public void setArenaRank(ArenaRank arenaRank, IRole role);

	/**
	 * 排名榜显示,显示1到100名
	 */
	void selHundredRank(final AMD_ArenaRank_selHundredRank __cb) throws NoteException;

	/**
	 * 排行兑换列表 和 刷新列表
	 * 
	 * @return
	 */
	public ArenaMallSel selMallList();

	/**
	 * 排行 刷新列表
	 * 
	 * @return
	 * @throws NoteException
	 */
	public ArenaMallSel refMallList() throws NoteException;

	/**
	 * 兑换, 目前数量是全部兑换，预留参数
	 * 
	 * @throws NoteException
	 */
	public ArenaMallSel exchangeItem(int storId) throws NoteException;

	/**
	 * 添加 竞技场排行 的战报信息
	 * 
	 * @param roleId
	 * @param fightflag
	 * @param fightId
	 */
	public ArenaRankFight addArenaRankFightInfo(String roleId, String movieId, int fightState, int rankCurrent,
			int rankChange, int sneerId, String reward, String fightId, int type);

	/**
	 * 战报列表
	 * 
	 * @param __cb
	 */
	public void rankFightReport(AMD_ArenaRank_robFightReport __cb);

	/**
	 * 查询挑战的对手是否存在
	 * 
	 * @param rivalId
	 * @return
	 * @throws NoteException
	 */
	boolean isRivalExist(String rivalId) throws NoteException;

	/**
	 * 查询防守阵容ID
	 * 
	 * @return
	 */
	String findFormationId();

	/**
	 * 炫耀
	 * 
	 * @param reportId
	 *            战报ID
	 * @param channel
	 *            聊天频道
	 * @param targetId
	 *            私聊对象的ID
	 * @param content
	 *            炫耀的字符
	 * @throws NoFactionException
	 * @throws NoGroupException
	 */
	void strutReport(String reportId, int channelType, String targetId, String content) throws NoteException,
			NoGroupException, NoFactionException;

	/**
	 * 查询当前角色的挑战令
	 * 
	 * @return
	 */
	int getChallenge();

	/**
	 * 复仇
	 * 
	 * @param rivalId
	 * @throws NoteException
	 */
	public void endRevenge(final String rivalId, final int resFlag, final byte remainHero,
			final AMD_ArenaRank_endRevenge __cb) throws NoteException;

	/**
	 * 炫耀的结果参数
	 * 
	 * @return 炫耀ID，炫耀的花费，炫耀的奖励
	 */
	public int[] getSneerResult();

	/**
	 * 改变竞技币
	 * 
	 * @param num
	 *            负数表示减少
	 */
	public void mondifyChallengeMoney(int num);

	/**
	 * 清除CD时间
	 * 
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	void clearCD() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 挑战排行榜CD剩余时间
	 * 
	 * @param arenaRank
	 * @return
	 */
	int fightRemainTime(ArenaRank arenaRank);

	/**
	 * 初始化 竞技场数据 新生成的机器人使用
	 * 
	 * @param rank
	 * @param isRobot
	 * @return
	 */
	public ArenaRank initArenaRank(int rank, boolean isRobot);

	/**
	 * 初始化 竞技场数据 真实玩家使用
	 * 
	 * @param rank
	 * @return
	 */
	public ArenaRank initArenaRank(int rank);

	/**
	 * 复仇 获取对手数据
	 */
	void beginRevenge(String rivalId, AMD_ArenaRank_beginRevenge __cb, String formationId) throws NoteException;

	/**
	 * 头像不存在，随机设置一个
	 * 
	 * @param rivalRole
	 * @return
	 */
	public String rankIcon(IRole rivalRole);

	/**
	 * 计算胜率 公式
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	float calcScale(int num1, int num2);

	void clearData();

	/**
	 * 设置排行榜的数据
	 * 
	 * @param rivalRank
	 */
	void selectRivalArena(RivalRank rivalRank);

	/**
	 * 竞技场 部队
	 * 
	 * @return
	 */
	IFormation getFormation();

	/** 保存战报录像 */
	void saveFightMovie(final String fightMovieId, final FightMovieView view);

	/** 获取战报录像 */
	void getFightMovie(final String fightMovieId, final AMD_ArenaRank_getFightMovie _callback);

	/**
	 * 竞技场被打
	 * 
	 * @param opponentId
	 *            对手ID
	 * @param from
	 *            战前排名
	 * @param to
	 *            战后排名
	 * @param type
	 *            类型，0普通战斗；1复仇
	 * @param flag
	 *            胜负
	 * */
	void onFighted(String opponentId, int from, int to, int type, int flag);

	/**
	 * 自动生成战报
	 * 
	 * @param formationId
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	void autoFight(AMD_ArenaRank_challenge __cb, String targetId, String formationId) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 复仇 生成战报
	 */
	void autoRevenge(String rivalId, AMD_ArenaRank_revenge __cb, String formationId) throws NoteException;

}
