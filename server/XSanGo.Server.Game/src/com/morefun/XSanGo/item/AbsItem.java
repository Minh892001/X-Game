/**
 * 
 */
package com.morefun.XSanGo.item;

import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 物品抽象基类
 * 
 * @author sulingyun
 * 
 */
public abstract class AbsItem implements IItem {
	protected RoleItem itemDb;
	private AbsItemT template;

	public AbsItem(RoleItem db, AbsItemT template) {
		this.itemDb = db;
		this.template = template;
	}

	@Override
	public final String getId() {
		return this.itemDb.getId();
	}

	@Override
	public final AbsItemT getTemplate() {
		return this.template;
	}

	@Override
	public final <T> T getTemplate(Class<T> type) {
		return type.cast(this.template);
	}

	@Override
	public final void setNum(int i) {
		if (i < 0) {
			throw new IllegalArgumentException();
		}
		if (!this.template.canOverlay() && i > 1) {
			throw new IllegalStateException();
		}

		this.itemDb.setNum(i);
	}

	@Override
	public int getNum() {
		return this.itemDb.getNum();
	}

	@Override
	public abstract ItemView getView();

	@Override
	public final RoleItem cloneData() {
		return XsgItemManager.getInstance().cloneRoleItem(this.itemDb);
	}

	@Override
	public String toString() {
		return TextUtil.format("[name={0},num={1},id={2}]",
				this.template.getName(), this.getNum(), this.getId());
	}
}
