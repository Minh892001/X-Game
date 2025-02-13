/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 商城物品可购买数量
 */
@Entity
@Table(name = "role_shop_item")
public class RoleShopItem implements Serializable {
	private static final long serialVersionUID = -4258307894491779282L;
	private String id;
	private Role role;
	private String itemId;
	private int buyTimes;// 已购买数量
	private int type;// 0-商城 1-礼包
	private boolean useOut;// 是否永久消失

	public RoleShopItem() {

	}

	public RoleShopItem(String id, Role role, String itemId, int buyTimes,
			int type, boolean useOut) {
		this.id = id;
		this.role = role;
		this.itemId = itemId;
		this.buyTimes = buyTimes;
		this.type = type;
		this.useOut = useOut;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "item_id", nullable = false)
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Column(name = "buy_times", nullable = false)
	public int getBuyTimes() {
		return buyTimes;
	}

	public void setBuyTimes(int buyTimes) {
		this.buyTimes = buyTimes;
	}

	@Column(name = "type", nullable = false, columnDefinition = "int default 0")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "use_out", nullable = false, columnDefinition = "bit default 0")
	public boolean isUseOut() {
		return useOut;
	}

	public void setUseOut(boolean useOut) {
		this.useOut = useOut;
	}

}
