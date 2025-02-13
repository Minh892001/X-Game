/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.logicserver;

/**
 * 供逻辑服务器调用
 * 
 * @author sulingyun
 * 
 */
public interface ILogicServerControler {
	/**
	 * 服务器有新角色创建时调用
	 * 
	 * @param account
	 * @param roleId
	 * @param roleName
	 */
	void createRole(String account, String roleId, String roleName);

	/**
	 * 角色升级时调用
	 * 
	 * @param account
	 * @param roleId
	 * @param roleName
	 * @param newLevel
	 */
	void newMaxLevel(String account, String roleId, String roleName,
			int newLevel);

	void bindToRoleResult(int id, boolean result);
}
