/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleController
 * 功能描述：
 * 文件名：IFactionBattleController.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.XSanGo.Protocol.AMD_Faction_startBattle;
import com.XSanGo.Protocol.EnrollResult;
import com.XSanGo.Protocol.FactionBattleLogView;
import com.XSanGo.Protocol.FactionBattlePersonalRankResultView;
import com.XSanGo.Protocol.FactionBattleRankResultView;
import com.XSanGo.Protocol.FactionBattleShow;
import com.XSanGo.Protocol.FactionBattleStrongholdView;
import com.XSanGo.Protocol.FactionBattleView;
import com.XSanGo.Protocol.FactionCallBackPrx;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpMovieView;

/**
 * 公会战处理接口
 * 
 * @author zwy
 * @since 2016-1-5
 * @version 1.0
 */
public interface IFactionBattleController {

	/**
	 * 公会战检测，针对成员变更的检测，公会战期间，主动或者被动都不能离开公会
	 * 
	 * @throws NoteException
	 */
	void checkFactionBattle2LeaveFaction() throws NoteException;

	/**
	 * 是否红点
	 * 
	 * @return
	 */
	boolean isRedPoint();

	/**
	 * 公会相关回调接口
	 * 
	 * @return
	 */
	FactionCallBackPrx getFactionCallBack();

	/**
	 * 公会战报名界面
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionBattleShow openFactionBattle() throws NoteException;

	/**
	 * 报名公会战
	 * 
	 * @return
	 * @throws NoteException
	 */
	EnrollResult enrollFactionBattle() throws NoteException;

	/**
	 * 更换阵营
	 * 
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 */
	EnrollResult changeFactionBattleCamp() throws NoteException, NotEnoughYuanBaoException;

	/**
	 * 进入公会战场界面
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionBattleView enterFactionBattle() throws NoteException;

	/**
	 * 离开公会战场
	 * 
	 * @throws NoteException
	 */
	void leaveFactionBattle() throws NoteException;

	/**
	 * 公会战公会排行信息
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionBattleRankResultView lookFactionBattleRank() throws NoteException;

	/**
	 * 公会战个人排行榜
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionBattlePersonalRankResultView lookFactionBattlePersonalRank() throws NoteException;

	/**
	 * 行军
	 * 
	 * @param isUseKits
	 * @param strongholdId
	 * @return
	 * @throws NoteException
	 */
	FactionBattleStrongholdView marching(boolean isUseKits, int strongholdId) throws NoteException;

	/**
	 * 购买行军冷却时间
	 * 
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 */
	void buyMarchingCooling() throws NoteException, NotEnoughYuanBaoException;

	/**
	 * 挖宝
	 * 
	 * @return
	 * @throws NoteException
	 */
	IntString[] diggingTreasure() throws NoteException;

	/**
	 * 锦囊使用
	 * 
	 * @param kitsId
	 * @return
	 * @throws NoteException
	 */
	String useKits(int kitsId) throws NoteException;

	/**
	 * 开战
	 * 
	 * @param __cb
	 * @param type
	 * @throws NoteException
	 */
	void startBattle_async(AMD_Faction_startBattle __cb, byte type) throws NoteException;

	/**
	 * 战斗结果确认（复活）
	 * 
	 * @throws NoteException
	 */
	void resultConfirm() throws NoteException;

	/**
	 * 是否战中状态
	 * 
	 * @return
	 */
	boolean isBattleing();

	/**
	 * 设置战斗状态
	 * 
	 * @param isBattleing
	 */
	void setBattleing(boolean isBattleing);

	/**
	 * 设置公会战战报
	 * 
	 * @param movieView
	 */
	void setMovieView(PvpMovieView movieView);

	/**
	 * 查看当场战斗战报
	 * 
	 * @return
	 * @throws NoteException
	 */
	PvpMovieView lookFactionBattleMovieView() throws NoteException;

	/**
	 * 查看公会战日志
	 * 
	 * @param logType 0战斗日志 1 挖宝日志
	 * @return
	 * @throws NoteException
	 */
	FactionBattleLogView[] lookFactionBattleLog(byte logType) throws NoteException;
}
