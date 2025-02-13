/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SummationActivityView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 累计消费活动控制逻辑接口
 * 
 * @author sulingyun
 *
 */
public interface ISumConsumeActivityControler extends IRedPointNotable {

	/**
	 * 获取活动描述等视图数据
	 * 
	 * @return
	 */
	SummationActivityView getView();

	/**
	 * 领取奖励
	 * 
	 * @param threshold
	 *            阈值
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 */
	void receiveReward(int threshold) throws NoteException,
			NotEnoughYuanBaoException;
	
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
