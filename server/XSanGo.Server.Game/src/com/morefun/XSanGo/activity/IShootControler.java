package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.MarksmanScoreRankView;
import com.XSanGo.Protocol.MarksmanScoreRewardView;
import com.XSanGo.Protocol.MarksmanView;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 百步穿杨 controler
 * 
 * @author zhouming
 */
public interface IShootControler extends IRedPointNotable {

	/**
	 * 打开百步穿杨界面
	 * 
	 * @return 界面数据包装类
	 * @throws NoteException
	 */
	MarksmanView openMarksmanView(int systemType) throws NoteException;

	/**
	 * 装备射击
	 * 
	 * @param shootType
	 *            射击类型
	 * @param systemType 1:普通 2:超级
	 * @return 界面数据包装类
	 * @throws NotEnoughException
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	MarksmanView shootReward(int shootType, int systemType) throws NotEnoughException,
			NotEnoughMoneyException, NotEnoughYuanBaoException,
			NotEnoughException, NoteException;

	/**
	 * 积分装备领取
	 * 
	 * @param recvType
	 *            领取类型
	 * @return 下一个能领取的积分数据包装类
	 * @throws NoteException
	 */
	String getScoreReward(int score) throws NoteException;

	/**
	 * 打开百步穿杨积分排名列表界面
	 * 
	 * @return
	 * @throws NoteException
	 */
	MarksmanScoreRankView openMarksmanScoreRankView() throws NoteException;

	/**
	 * 打开百步穿杨积分排名奖励界面
	 * 
	 * @return
	 * @throws NoteException
	 */
	MarksmanScoreRewardView openMarksmanScoreRewardView() throws NoteException;
	
	/**
	 * 是否显示我的中奖记录
	 */
	void showMyRecord(boolean show) throws NoteException ;

	/**
	 * 累计获得的奖励
	 */
	String historyAward() throws NoteException;
}
