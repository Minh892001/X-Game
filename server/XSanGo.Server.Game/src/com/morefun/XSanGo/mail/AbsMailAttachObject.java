/**
 * 
 */
package com.morefun.XSanGo.mail;

import java.io.Serializable;

/**
 * 邮件附件描述类
 * 
 * @author sulingyun
 *
 */
public abstract class AbsMailAttachObject implements Serializable {

	/** */
	private static final long serialVersionUID = -99621860387791442L;

	/**
	 * 获取附件物品的模板ID
	 * 
	 * @return
	 */
	public abstract String getTemplateId();

	/**
	 * 获取物品数量
	 * 
	 * @return
	 */
	public abstract int getNum();

	@Override
	public String toString() {
		return this.getNum() + "*" + this.getTemplateId();
	}
}
