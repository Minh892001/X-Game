/**
 * 
 */
package com.morefun.XSanGo.item;

import com.XSanGo.Protocol.FormationBuffExtendsView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 阵法书对象
 * 
 * @author sulingyun
 * 
 */
public class FormationBuffItem extends AbsItem {
	private BuffItemData data;

	public FormationBuffItem(RoleItem db, FormationBuffItemT template) {
		super(db, template);
		this.data = TextUtil.GSON.fromJson(db.getAttachData(), BuffItemData.class);
		if (this.data == null) {
			this.data = new BuffItemData();
		}
	}

	public byte getLevel() {
		return this.data.level;
	}

	public int getExp() {
		return this.data.exp;
	}

	private void flushData() {
		this.itemDb.setAttachData(TextUtil.GSON.toJson(data));
	}

	public int getNum() {
		return this.itemDb.getNum();
	}

	// @Override
	// public FormationBuffView getView() {
	// return new FormationBuffView(this.getId(), this.getTemplate()
	// .getItemType(), this.getTemplate().getId(), this.getNum(),
	// this.getLevel(), this.getExp(), 1);
	// }
	/**
	 * 阵法属性封装在ItemView里面
	 */
	@Override
	public ItemView getView() {
		return new ItemView(this.getId(), this.getTemplate().getItemType(), this.getTemplate().getId(), this.getNum(),
				LuaSerializer.serialize(new FormationBuffExtendsView(this.getLevel(), this.getExp(), 1)));
	}

	/**
	 * 获取当前在哪个阵型中使用
	 * 
	 * @return
	 */
	public String getRefereneFormationId() {
		return this.data.refereneFormationId;
	}

	/**
	 * 设置阵型引用为空
	 */
	public void setRefereneFormationIndex2Empty() {
		this.setRefereneFormationIndex("");
	}

	public void setRefereneFormationIndex(String formationIndex) {
		this.data.refereneFormationId = formationIndex;
		this.flushData();
	}

	public QualityColor getColor() {
		return QualityColor.valueOf(this.getTemplate(FormationBuffItemT.class).color);
	}

	/**
	 * 获取指定位置的加成效果
	 * 
	 * @param position
	 * @return
	 */
	public Property getPropertyByPos(int position) {
		int index = position / 3;
		PropertyT pt = this.getTemplate(FormationBuffItemT.class).properties[index];
		int add = (this.getLevel() - 1) * (this.getColor().ordinal() + 1) * 100;
		return new Property(pt.code, pt.value + add);
	}

	/**
	 * 设置等级和经验
	 * 
	 * @param level
	 * @param exp
	 */
	public void setLevelAndExp(byte level, int exp) {
		this.data.level = level;
		this.data.exp = exp;
		this.flushData();
	}
}

class BuffItemData {
	public byte level = 1;
	public int exp;
	public String refereneFormationId;
}
