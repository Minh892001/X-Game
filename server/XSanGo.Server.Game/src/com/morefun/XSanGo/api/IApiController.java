package com.morefun.XSanGo.api;

import java.util.List;

import com.XSanGo.Protocol.ApiActView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * API处理器接口
 * 
* @author zhangwei02.zhang
 * @since 2015年11月11日
 * @version 1.0
 */
public interface IApiController extends IRedPointNotable {

	/**
	 * 打开活动界面
	 * 
	 * @return
	 * @throws NoteException
	 */
	public List<ApiActView> openApiAct() throws NoteException;

	/**
	 * 领奖
	 * 
	 * @param actId
	 * @param targetCount
	 * @throws NoteException
	 */
	public void receiveApiReward(int actId, int targetCount) throws NoteException;
}
