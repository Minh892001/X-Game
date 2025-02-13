/**
 * 
 */
package com.morefun.XSanGo.MFBI;

import com.morefun.XSanGo.event.protocol.ICharge;

/**
 * 数据中心接口
 * 
 * @author 吕明涛
 * 
 */
public interface IMFBIControler extends ICharge {

	/**
	 * 登录发送 数据中心 信息
	 */
	void sendRoleLogin();

}
