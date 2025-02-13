/**
 * 
 */
package com.morefun.XSanGo.drop;

/**
 * 掉落结果，包含所有奖励描述，如果金钱经验物品装备等，同时还可以设置一个回调函数，用于更新掉落统计数据
 * 
 * @author sulingyun
 * 
 */
public class DropResult {
	private int money;
	private IDropCallback callback;

	public IDropCallback getCallback() {
		return callback;
	}

	public void setCallback(IDropCallback callback) {
		this.callback = callback;
	}
}
