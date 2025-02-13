package com.morefun.XSanGo.achieve;

import com.XSanGo.Protocol.AchievePageView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 *	成就控制器 
 */
public interface IAchieveControler extends IRedPointNotable {
	
	/**
	 * 打开成就界面
	 * @param functionId
	 *            系统ID
	 * @return 界面数据包装类
	 * @throws NoteException
	 */
	AchievePageView achievePageView(int functionId) throws NoteException;

	/**
	 * 领取成就奖励
	 * 
	 * @param id
	 *            成就ID
	 * @return 界面数据包装类
	 * @throws NoteException
	 */
	AchievePageView achieveReward(int id) throws NoteException;

	void updateAchieveLogin();

	void updateAchieveProgress(String type, String para);
	
	/**
	 * 成就进度奖励界面
	 * @return
	 * @throws NoteException
	 */
	String getAchieveProgressView() throws NoteException;
	
	/**
	 * 成就进度领奖
	 * @return
	 * @throws NoteException
	 */
	String recProgressAward(int progress) throws NoteException;
}
