package com.morefun.XSanGo.haoqingbao;

import com.morefun.XSanGo.util.DelayedTask;

/**
 * @author guofeng.qin
 */
public class RedPacketDelayTask extends DelayedTask {

	private String itemId;

	public RedPacketDelayTask(String itemId, long delayed) {
		super(delayed);
		this.itemId = itemId;
	}

	@Override
	public void run() {
		XsgHaoqingbaoManager manager = XsgHaoqingbaoManager.getInstance();
		// 结束一个任务
		manager.finishRedPacketItem(itemId);
	}

	public String getItemId() {
		return itemId;
	}
}
