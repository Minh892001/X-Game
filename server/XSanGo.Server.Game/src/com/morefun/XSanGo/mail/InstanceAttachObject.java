/**
 * 
 */
package com.morefun.XSanGo.mail;

import java.io.Serializable;

import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 实例物品附件
 * 
 * @author sulingyun
 *
 */
public class InstanceAttachObject extends AbsMailAttachObject implements
		Serializable {
	/** */
	private static final long serialVersionUID = 5444818401836465088L;

	private RoleItem instanceData;

	public InstanceAttachObject(RoleItem data) {
		this.instanceData = data;
	}

	@Override
	public String getTemplateId() {
		return this.instanceData.getTemplateCode();
	}

	@Override
	public int getNum() {
		return this.instanceData.getNum();
	}

	public RoleItem getInstanceData() {
		return instanceData;
	}

	@Override
	public String toString() {
		return TextUtil.format("{0}|{1}|{2}", this.instanceData.getId(),
				this.instanceData.getTemplateCode(),
				this.instanceData.getAttachData());
	}
}
