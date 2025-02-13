/**
 * 
 */
package com.morefun.XSanGo.mail;

import java.io.Serializable;

/**
 * 模板物品附件
 * 
 * @author sulingyun
 *
 */
public class TemplateAttachObject extends AbsMailAttachObject implements
		Serializable {
	private String templateId;
	private int num;

	public TemplateAttachObject(String templateId, int num) {
		this.templateId = templateId;
		this.num = num;
	}

	@Override
	public String getTemplateId() {
		return templateId;
	}

	@Override
	public int getNum() {
		return num;
	}
}
