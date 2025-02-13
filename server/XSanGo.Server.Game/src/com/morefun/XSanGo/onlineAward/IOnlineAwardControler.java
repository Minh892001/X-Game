/**
 * 
 */
package com.morefun.XSanGo.onlineAward;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OnlineAwardView;

/**
 * 在线礼包  功能控制器
 * 
 * @author 吕明涛
 * 
 */
public interface IOnlineAwardControler {
	
	/**
	 * 查询在线时间 ，剩余时间，单位：秒
	 */
	OnlineAwardView selectOnlineTime() throws NoteException;
	
	/**
	 * 领取在线奖励，返回下一个在线奖励的剩余时间，单位：秒
	 */
	OnlineAwardView getAward() throws NoteException;
	
	/**
	 * 登录后调用，在线礼包时间 重置
	 */
	public void afterEnterGame();
	
}
