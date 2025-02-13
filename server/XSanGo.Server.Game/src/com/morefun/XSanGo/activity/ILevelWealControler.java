package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.LevelWealView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 等级福利
 * 
 * @author qinguofeng
 */
public interface ILevelWealControler extends IRedPointNotable {
	/**
	 * 获取等级福利详情
	 * */
	LevelWealView levelWealInfo();

	/**
	 * 领取等级福利
	 * */
	int getLevelWeal() throws NoteException;
	
	/**
	 * 领取通过关卡等级福利
	 * */
	int getCopyLevelWeal();
}