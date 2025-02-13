package com.morefun.XSanGo.sign;

import com.XSanGo.Protocol.AMD_Sign_inviteCode;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface ISignController extends IRedPointNotable {

	/**
	 * 签到
	 * 
	 * @param day
	 *            选项id
	 * @throws NotEnoughYuanBaoException
	 */
	void signIn(int day) throws Exception, NotEnoughYuanBaoException;

	/**
	 * 一键补签
	 */
	int autoResign() throws NoteException, NotEnoughYuanBaoException;

	/**
	 * 累计签到奖励
	 * 
	 * @param id
	 *            奖励对应的次数
	 */
	void collectGiftPack(int count);

	/**
	 * 抽奖
	 * 
	 * @return itemId,count
	 */
	String[] roulette() throws NotEnoughMoneyException;

	/**
	 * @return 已经签到的次数
	 */
	int getSignTimes(int day);

	boolean allreadyCollectGift(String id);

	byte getTotalSignTimes();

	int remainRouletteTimes();

	/**
	 * @return 是否可以一键补签
	 */
	boolean canAutoResign();

	/**
	 * @param day
	 *            当月的几号
	 * @return 当天的签到花费
	 */
	int signCost(int day);

	/**
	 * 客户端在抽奖动画播放完毕后会请求这个操作,避免动画未播放完毕而公告已经发送
	 */
	void broadcastLastRoulette();

	/**
	 * 输入邀请码
	 * */
	void inviteCode(String code, final AMD_Sign_inviteCode __cb);
}
