package com.morefun.XSanGo.activity;

import Ice.Current;

import com.XSanGo.Protocol.ActivityInfoView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.UpActivityInfoView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 *	活动控制器 
 */
public interface IActivityControler extends IRedPointNotable {
	/**
	 * 获取升级礼包列表信息
	 * @return
	 */
	UpActivityInfoView[] getUpActivityList();
	/**
	 * 领取升级礼包
	 * @param giftId
	 * @return
	 */
	boolean getGift(int giftId);
	
	/**
	 * 是否已领取升级礼包
	 * @param giftId 礼包ID
	 * @return
	 */
	boolean isGetGift(int giftId);
	
	ActivityInfoView[] getActivityList();
	
	/**
	 * 是否开启API活动
	 * 
	 * @param at
	 * @return 
	 */
	boolean isOpenApi(ActivityT at);
}
