/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.stat;

/**
 * @author lusongjie
 * 
 */
public interface IStat {

	/**
	 * 服务器在线量通知
	 * 
	 * @param serverId
	 * @param count
	 */
	void online(int serverId, int count);

	/**
	 * 角色升级事件
	 * 
	 * @param serverId
	 * @param roleId
	 * @param roleName
	 * @param newLevel
	 * @param onlineDuration
	 *            暂时不用
	 */
	void levelup(int serverId, String roleId, String roleName, int newLevel,
			int onlineDuration);

	/**
	 * 角色登出事件
	 * 
	 * @param serverId
	 * @param account
	 * @param roleId
	 * @param roleName
	 * @param onlineDuration
	 */
	void logout(int serverId, String account, String roleId, String roleName,
			int onlineDuration);

	/**
	 * @param cb 回调接口
	 * @param serverId
	 * @param account
	 * @param roleId
	 * @param roleName
	 * @param yuanbao
	 */
	int beginYuanbaoTransfer(int serverId,String account, String roleId, String roleName, int yuanbao);

	/**
	 * @param id
	 * @param isSuc
	 */
	void endYuanbaoTransfer(int id, boolean isSuc);

}
