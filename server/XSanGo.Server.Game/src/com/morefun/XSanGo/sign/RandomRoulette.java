package com.morefun.XSanGo.sign;

import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.util.IRandomHitable;

public class RandomRoulette implements IRandomHitable {
	int rank;
	String templateId;
	int count;
	int cost;
	/**
	 * 抽中是否需要系统公告
	 */
	boolean broadcast;
	IItem item; // 抽中的奖品对应的实例对象

	RandomRoulette(int rank, String templateId, int count, int cost,
			boolean broadcast) {
		this.rank = rank;
		this.templateId = templateId;
		this.count = count;
		this.cost = cost;
		this.broadcast = broadcast;
	}

	@Override
	public int getRank() {
		return rank;
	}

	public String getTemplateId() {
		return templateId;
	}

	public int getCount() {
		return count;
	}

	public int getCost() {
		return cost;
	}

	public IItem getItem(){
		return item;
	}

	public void setItem(IItem i) {
		this.item = i;
	}

	@Override
	public String toString() {
		return templateId + "*" + count + " (cost:" + cost + ") 概率为" + rank;
	}

	public boolean neetBroadcast() {
		return broadcast;
	}

}