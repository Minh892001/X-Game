/**
 * 
 */
package com.morefun.XSanGo.role;

/**
 * 二次确认处理接口
 * 
 * @author sulingyun
 *
 */
public interface IConfirmHandler {
	/**
	 * 处理用户确认结果
	 * 
	 * @param role
	 * @param result
	 *            确认-true，取消-false
	 */
	void handle(IRole role, boolean result);
}
