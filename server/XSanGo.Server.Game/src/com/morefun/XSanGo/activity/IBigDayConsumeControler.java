package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SummationActivityView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 大的日消费活动
 * 
 * @author lixiongming
 *
 */
public interface IBigDayConsumeControler extends IRedPointNotable {

	/**
	 * 获取日消费活动信息
	 * @return
	 * @throws NoteException
	 */
	public SummationActivityView getDayConsumeView() throws NoteException;

	/**
	 * 领取日消费活动奖励
	 * @param threshold
	 * @throws NoteException
	 */
	public void receiveDayConsume(int threshold) throws NoteException;

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
