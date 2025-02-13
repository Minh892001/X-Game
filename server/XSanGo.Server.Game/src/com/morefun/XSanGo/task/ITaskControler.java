/**
 * 
 */
package com.morefun.XSanGo.task;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SevenTargetView;
import com.XSanGo.Protocol.TaskView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 角色的任务 功能控制器
 * 
 * @author 吕明涛
 * 
 */
public interface ITaskControler extends IRedPointNotable {
	
	/**
	 * 显示任务列表
	 */
	TaskView selectTask() throws NoteException;
	
	/**
	 * 领取任务奖励
	 * @throws NotEnoughMoneyException 
	 */
	TaskView finishTask(int taskId, int type) throws NoteException, NotEnoughMoneyException;
	
	/**
	 * 初始化红包历史记录
	 */
	void initRoleRedPacket();
	
	/**
	 * 领取活跃点奖励
	 * @param id
	 * @throws NoteException
	 */
	void recActPointAward(int id) throws NoteException;
	
	/**
	 * 领取总星数量奖励
	 * 
	 * @param star
	 */
	void receiveStarAward(int star) throws NoteException;

	/**
	 * 领取日常单个星级奖励
	 * 
	 * @param index
	 */
	public String receiveTodayAward(int index) throws NoteException;

	/**
	 * 领取三星奖励
	 * 
	 * @param index
	 */
	public void receiveThreeStarAward() throws NoteException;

	boolean isCompletedSevenTask();
	/**
	 * 七日任务开始记录
	 */
	public void addSevenTask();
}
