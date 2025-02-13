package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SummationActivityView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 大的日充值活动
 * @author lixiongming
 *
 */
public interface IBigDayChargeControler extends IRedPointNotable{
	
	/**
	 * 获取日充值活动信息
	 * @return
	 * @throws NoteException
	 */
	public SummationActivityView getDayChargeView() throws NoteException;

	/**
	 * 领取日充值活动奖励
	 * @param threshold
	 * @throws NoteException
	 */
	public void receiveDayCharge(int threshold) throws NoteException;
	
	/**
	 * 限时活动设置是否首次打开，显示红点
	 * @param value
	 */
	public void setFirstOpen(boolean value);
	
	/**
	 * 是否全部领取完
	 * @return
	 */
	public boolean isOver();
}
