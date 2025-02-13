/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.XSanGo.Protocol.AMD_Copy_autoChallengeTa;
import com.XSanGo.Protocol.AMD_Copy_endChallenge;
import com.XSanGo.Protocol.AMD_Copy_getBigCopyView;
import com.XSanGo.Protocol.AMD_Copy_getSmallCopyViewWithWarmup;
import com.XSanGo.Protocol.BuyMilitaryOrderView;
import com.XSanGo.Protocol.ChallengeTaView;
import com.XSanGo.Protocol.ChapterRewardView;
import com.XSanGo.Protocol.CopyChallengeResultView;
import com.XSanGo.Protocol.CopyClearResultView;
import com.XSanGo.Protocol.CopyOccupy;
import com.XSanGo.Protocol.CopySummaryView;
import com.XSanGo.Protocol.EmployCaptureResult;
import com.XSanGo.Protocol.HuDongView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.db.game.RoleCopy;
import com.morefun.XSanGo.role.IRole;

/**
 * 副本控制器
 * 
 * @author sulingyun
 * 
 */
public interface ICopyControler {

	/**
	 * 获取副本概要信息
	 * 
	 * @return
	 */
	void getBigCopyView(final AMD_Copy_getBigCopyView cb);

	/**
	 * 以指定星级结束副本挑战
	 * 
	 * @return
	 * @throws NoteException
	 */
	boolean endChallenge(byte star) throws NoteException;

	/**
	 * 副本挑战失败
	 * */
	void failChallenge() throws NoteException;

	/**
	 * 结束副本挑战，此重载方法只应在网络接口处调用
	 * 
	 * @throws NoteException
	 */
	void endChallenge(AMD_Copy_endChallenge __cb) throws NoteException;

	/**
	 * 获取当天已挑战次数
	 * 
	 * @param copyId
	 * 
	 * @return
	 */
	short getCountToday(int copyId);

	/**
	 * 是否可以挑战指定副本，即该副本关卡之前的所有关卡都已通过
	 * 
	 * @param copyId
	 * @return
	 */
	boolean canChallenge(int copyId);

	/**
	 * 挑战副本，生成挑战结果，但不进行最终结算
	 * 
	 * @param formationId
	 * @param copyId
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughException
	 */
	CopyChallengeResultView beginChallenge(String formationId, int copyId, boolean needMovie) throws NoteException, NotEnoughException;

	/**
	 * 获取指定副本还能挑战的次数
	 * 
	 * @param copyId
	 * @return
	 */
	int getRemainTime(int copyId);

	/**
	 * 扫荡副本
	 * 
	 * @param copyTemplateId
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughException
	 */
	CopyClearResultView clearCopy(int copyTemplateId) throws NoteException, NotEnoughException;

	/**
	 * 查看章节奖励信息
	 * 
	 * @param chapterId
	 * @return
	 */
	ChapterRewardView getChapterRewardView(int chapterId);

	/**
	 * 领取章节奖励
	 * 
	 * @param chapterId
	 * @param level 奖励的星数等级1(十星),2(20星),3(满星)
	 * @throws NoteException
	 */
	void receiveChapterReward(int chapterId, int level) throws NoteException;

	/**
	 * 关卡是否过关
	 * 
	 * @param copyId
	 */
	int getCopyStar(int copyId);

	/**
	 * 购买关卡挑战次数
	 * 
	 * @param copyId
	 * @throws NotEnoughYuanBaoException
	 */
	void buyChallengeChance(int copyId) throws NotEnoughYuanBaoException, NoteException;

	/**
	 * 购买一章所有关卡挑战次数
	 * 
	 * @param copyId
	 * @throws NotEnoughYuanBaoException
	 */
	IntIntPair[] buyChapterChallengeChance(int copyTemplateId) throws NotEnoughYuanBaoException, NoteException;

	/**
	 * 获取今天已经重置关卡挑战次数
	 * 
	 * @param copyId
	 * 
	 * @return
	 */
	int getResetCountToday(int copyId);

	/**
	 * 释放俘虏
	 * 
	 */
	void releaseCaptured();

	/**
	 * 斩首俘虏
	 * 
	 */
	int killCaptured();

	/**
	 * 录用俘虏
	 * 
	 * @return
	 * @throws NotEnoughMoneyException
	 */
	EmployCaptureResult employCaptured() throws NotEnoughMoneyException;

	/**
	 * 获取副本挑战次数数据
	 * 
	 * @param idArray
	 * @return
	 */
	CopySummaryView[] getCopyChallengeInfo(int[] idArray);

	/**
	 * 获取各难度的当前进度
	 * 
	 * @return
	 */
	IntString[] getProgresses();

	/**
	 * 计算星级
	 * 
	 * @param remainHero
	 * @return
	 * @throws NoteException
	 */
	int calculateStar(byte remainHero, byte killNum, float minTime, float maxTime) throws NoteException;

	/**
	 * 副本总星数
	 * 
	 * @return
	 */
	int getTotalStar();

	/**
	 * 获取互动次数信息
	 * 
	 * @param copyId
	 * @return
	 */
	HuDongView getHuDongView(int copyId);

	RoleCopy getRoleCopy(int copyId);

	void buyHudong(int copyId) throws NoteException, NotEnoughYuanBaoException;

	int worshipTa(int copyId) throws NoteException;

	void endChallengeTa(int resFlag) throws NoteException;

	/**
	 * 重置一些数据
	 * 
	 * @param copyId
	 */
	void resetHudongCount(int copyId);

	ChallengeTaView getChallengeTaView(IRole iRole, int copyId);

	/**
	 * 获取军令购买相关信息
	 * 
	 * @return
	 */
	BuyMilitaryOrderView getBuyMilitaryOrderView();

	/**
	 * 购买军令
	 * 
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 */
	void buyMilitaryOrder() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 打开副本界面时获取副本数据，带热身赛逻辑
	 * 
	 * 
	 * @param __cb
	 * @param copyId
	 * @throws NoteException
	 */
	void getSmallCopyViewWithWarmup_async(AMD_Copy_getSmallCopyViewWithWarmup __cb, int copyId) throws NoteException;

	/**
	 * 开始挑战副本前的玩家间热身赛
	 * 
	 * @return
	 * @throws NoteException
	 */
	String beginWarmup() throws NoteException;

	/**
	 * 结束热身
	 * 
	 * @param remainHero
	 * @throws NoteException
	 */
	byte endWarmup(byte remainHero) throws NoteException;

	/**
	 * 
	 * 取消热身
	 * 
	 * @param first
	 * @param __current
	 * @return
	 * @throws NoteException
	 */
	String cancelWarmup(boolean first) throws NoteException;

	/**
	 * 获取关卡可膜拜次数
	 * @param copyId
	 * @return
	 * @throws NoteException
	 */
	int getWorshipCount(int copyId) throws NoteException;

	/**
	 * 获取挑战TA次数
	 * @return
	 * @throws NoteException
	 */
	int getChallengeTaCount();
	
	/**
	 * 征收
	 * @param copyId
	 * @throws NoteException
	 */
	int levyCopy(int copyId) throws NoteException;

	/**
	 * 放弃占领
	 * @param copyId
	 * @throws NoteException
	 */
	void giveCopy(int copyId) throws NoteException;
	
	/**
	 * 自己占领关卡列表
	 * @return
	 * @throws NoteException
	 */
	CopyOccupy[] myOccupyList() throws NoteException;

	/**
	 * 调整指定难度副本到指定关卡
	 * 
	 * @param diff
	 * @param copyId
	 */
	void setCopyProgress(int diff, int copyId);
	
	/**
	 * 自动战报挑战TA
	 * @param __cb
	 * @param copyId
	 */
	void autoChallengeTa(AMD_Copy_autoChallengeTa __cb, int copyId);
}
