package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.BuyHeroResult;
import com.XSanGo.Protocol.FortuneWheelResultView;
import com.XSanGo.Protocol.FortuneWheelView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * @author qinguofeng
 */
public interface IFortuneWheelControler extends IRedPointNotable {

	/**
	 * 获取幸运大转盘界面
	 * */
	FortuneWheelView getFortuneWheelView() throws NoteException;

	/**
	 * 进行大转盘抽奖
	 * */
	FortuneWheelResultView doFortuneWheel() throws NoteException;
	
	/**
	 * 10次大转盘抽奖
	 * */
	BuyHeroResult[] doRortuneWheelFor10() throws NoteException;
	
	/** 设置首次打开 */
	void setFirstOpen(boolean v);

	/**
	 * 增加大转盘抽奖次数
	 * @param count
	 */
	void addLastCount(int count);
}
