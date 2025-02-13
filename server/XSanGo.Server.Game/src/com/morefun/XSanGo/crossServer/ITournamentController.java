package com.morefun.XSanGo.crossServer;

import com.XSanGo.Protocol.AMD_Tournament_beginFightWith;
import com.XSanGo.Protocol.AMD_Tournament_bet;
import com.XSanGo.Protocol.AMD_Tournament_enterPVPView;
import com.XSanGo.Protocol.AMD_Tournament_enterTournament;
import com.XSanGo.Protocol.AMD_Tournament_fightWith;
import com.XSanGo.Protocol.AMD_Tournament_getBetView;
import com.XSanGo.Protocol.AMD_Tournament_getFightMovieByRecordId;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutMovie;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutMovieList;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutView;
import com.XSanGo.Protocol.AMD_Tournament_getRankList;
import com.XSanGo.Protocol.AMD_Tournament_openTournamentView;
import com.XSanGo.Protocol.AMD_Tournament_refreshPVPView;
import com.XSanGo.Protocol.AMD_Tournament_setupFormation;
import com.XSanGo.Protocol.AMD_Tournament_signup;
import com.XSanGo.Protocol.FightRecordItemView;
import com.XSanGo.Protocol.MyFormationView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PreSignupView;
import com.XSanGo.Protocol.ShopBuyResultView;
import com.XSanGo.Protocol.TournamentShopView;
import com.XSanGo.Protocol.TournamentStatusView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 比武大会
 * 
 * @author guofeng.qin
 */
public interface ITournamentController extends IRedPointNotable {

	/**
	 * 打开报名界面
	 */
	void openSignup(final AMD_Tournament_enterTournament __cb);

	/**
	 * 预报名
	 */
	PreSignupView preSignup() throws NoteException;

	/**
	 * 报名
	 */
	void doSignup(final AMD_Tournament_signup __cb) throws NoteException;

	/**
	 * 获取对手匹配界面
	 */
	void getPVPInfoView(final AMD_Tournament_enterPVPView __cb) throws NoteException;

	/**
	 * 刷新对手匹配
	 */
	void refreshPVP(final AMD_Tournament_refreshPVPView __cb) throws NoteException;

	/**
	 * 保存布阵
	 */
	void setupFormation(final AMD_Tournament_setupFormation __cb) throws NoteException;

	/**
	 * 获取我当前布阵
	 */
	MyFormationView getMyCurrentFromation();

	/**
	 * 打开布阵
	 */
	MyFormationView openSetupFormation() throws NoteException;

	/**
	 * 购买刷新次数
	 */
	int buyRefreshCount() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 购买挑战次数
	 */
	int buyFightCount() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/** 获取排行榜 */
	void getRankList(final AMD_Tournament_getRankList __cb) throws NoteException;

	/** 获取战报列表 */
	FightRecordItemView[] getFightRecords() throws NoteException;

	/** 根据战斗记录ID获取战报 */
	void getFightMovieByRecordId(String recordId, final AMD_Tournament_getFightMovieByRecordId __cb);

	/** 开始战斗 */
	void beginFight(String opponentId, final AMD_Tournament_beginFightWith __cb);

	/** 结束战斗 */
	String endFight(String opponentId, int flag, int remainHeroCount, int power) throws NoteException;

	/** 后端生成战斗方式的战斗接口 */
	void fight(String opponentId, final AMD_Tournament_fightWith __cb);

	/** 获取淘汰赛对阵图 */
	void getKnockOutView(final AMD_Tournament_getKnockOutView __cb);

	/** 获取淘汰赛战报 */
	void getKnockOutMovie(int id, int index, final AMD_Tournament_getKnockOutMovie __cb);

	/** 获取押注界面 */
	void getBetView(final AMD_Tournament_getBetView __cb);

	/** 押注 */
	void bet(final AMD_Tournament_bet __cb, int stage, int id, String roleId, int num)
			throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/** 获取比武大会主页面 */
	void openTournamentView(AMD_Tournament_openTournamentView __cb) throws NoteException;

	/**
	 * 获取比武大会战斗列表，每个对阵会进行几局几胜制
	 */
	void getKnockOutMovieList(final AMD_Tournament_getKnockOutMovieList __cb, int id);

	/**
	 * 获取比武大会时间状态
	 */
	TournamentStatusView getTournamentStatus() throws NoteException;

	/**
	 * 设置冠军
	 */
	void setMaxRank(int rank);

	/**
	 * 设置冠军届数
	 */
	void setChampionNum(int num);

	/**
	 * 获取历史最高排名
	 */
	int getMaxRank();

	/**
	 * 冠军登录
	 */
	void championLogin();

	/**
	 * 获取当日胜利次数
	 * 
	 * @return
	 */
	int getWinNum();

	/** 获取商城 */
	TournamentShopView getShopView() throws NoteException;

	/**
	 * 购买商品
	 * 
	 * @param id
	 *            商品id
	 */
	ShopBuyResultView buyShopItem(String id, int num) throws NoteException;

	/**
	 * 增加至尊币
	 */
	void addCoin(int num);
	
	/**
	 * 增加至尊银币
	 * */
	void addYBCoin(int num);
}
