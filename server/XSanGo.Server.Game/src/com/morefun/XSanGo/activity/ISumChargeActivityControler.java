/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SummationActivityView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 累计充值活动控制接口
 * 
 * @author sulingyun
 *
 */
public interface ISumChargeActivityControler extends IRedPointNotable {

	/**
	 * 获取活动视图数据
	 * 
	 * @return
	 */
	SummationActivityView getView();

	/**
	 * 领取奖励
	 * 
	 * @param threshold
	 *            累计值
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	void receiveReward(int threshold) throws NotEnoughYuanBaoException,
			NoteException;
	
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
