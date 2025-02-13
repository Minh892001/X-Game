package com.morefun.XSanGo.faction;

/**
 * 公会仓库物品
 * 
 * @author xiongming.li
 *
 */
public class WarehouseItemBean {
	public int id;
	public String itemId;
	public long itemNum;
	public String queue;

	public WarehouseItemBean() {

	}

	public WarehouseItemBean(int id, String itemId, long itemNum, String queue) {
		this.itemId = itemId;
		this.itemNum = itemNum;
		this.id = id;
		this.queue = queue;
	}

}
