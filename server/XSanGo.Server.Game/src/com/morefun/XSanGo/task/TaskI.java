/**
 * 
 */
package com.morefun.XSanGo.task;

import Ice.Current;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SevenTargetView;
import com.XSanGo.Protocol.TaskView;
import com.XSanGo.Protocol._taskDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 碎片的 合成、掠夺、复仇抢回碎片 接口
 * 
 * @author 吕明涛
 * 
 */
public class TaskI extends _taskDisp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7660418146518454141L;

	private IRole roleRt;

	public TaskI(IRole role) {
		this.roleRt = role;
	}

	/**
	 * 显示任务列表
	 */
	@Override
	public String selectTask(Current __current) throws NoteException {
		TaskView taskView = this.roleRt.getTaskControler()
				.selectTask();

		return LuaSerializer.serialize(taskView);
	}

	/**
	 * 领取任务奖励
	 * 
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public String finishTask(int taskId, int type, Current __current)
			throws NoteException, NotEnoughMoneyException {
		TaskView taskView = this.roleRt.getTaskControler()
				.finishTask(taskId, type);

		return LuaSerializer.serialize(taskView);
	}

	@Override
	public void receiveActAward(int awardId, Current __current)
			throws NoteException {
		    this.roleRt.getTaskControler().recActPointAward(awardId);
	}

	@Override
	public void receiveStarAward(int star, Current __current)
			throws NoteException {
		this.roleRt.getTaskControler().receiveStarAward(star);
	}

	@Override
	public String receiveTodayAward(int index, Current __current)
			throws NoteException {
		return this.roleRt.getTaskControler().receiveTodayAward(index);
	}

	@Override
	public void receiveThreeStarAward(Current __current) throws NoteException {
		this.roleRt.getTaskControler().receiveThreeStarAward();
	}
}
