package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 开服活动
 */
public interface IOpenServerActiveControler extends IRedPointNotable {

	// 最强武将 N个武将进阶到+M
	int HERO_COLOR_UP = 1;
	// 宝石王老五 合成N颗M级宝石
	int STONE_NUM = 2;
	// 竞技场排名达到N
	int AREAN_NUM = 3;
	// 半价礼包
	int SALES = 4;
	// 战力达到
	int FIRST_FIGHT_POINT = 5;

	/**
	 * 获取开服活动列表
	 * 
	 * @return
	 */
	String getOpenServerActiveView();

	/**
	 * 领取开服活动奖励
	 * 
	 * @param active
	 * @param nodeId
	 */
	void acceptOpenServerActiveReward(int active, int nodeId) throws NoteException;

	/**
	 * 更新进度
	 * 
	 * @param type
	 * @param para
	 */
	void updateProgress(int type, String para);

	void update4Login();

	/**
	 * 购买半价道具
	 * @param day
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	String buySaleItem(int activeId,int day) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException;
}
