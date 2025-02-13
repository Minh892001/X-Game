/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.mail.AbsMailAttachObject;

/**
 * 邮件得到奖励
 * 
 * @author lvmingtao
 */

@signalslot
public interface IMailReward {
	/**
	 * @param mialTiltle
	 *            邮件标题
	 * @param attaches
	 *            附件内容
	 */
	void onReward(String mialTiltle, AbsMailAttachObject[] attaches);
}
