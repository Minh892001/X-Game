package com.morefun.XSanGo.util;

import java.util.List;

public class RandomRange<T extends IRandomHitable> {
	private List<T> list;
	private int totalRank;

	public RandomRange(List<T> list) {
		this.totalRank = 0;
		this.list = list;
		for (T t : list) {
			this.totalRank += t.getRank();
		}
	}

	public List<T> getList() {
		return this.list;
	}

	public T random() {
		int random = NumberUtil.random(this.totalRank);
		int temp = 0;
		for (T t : this.list) {
			temp += t.getRank();
			if (random < temp) {
				return t;
			}
		}

		return null;
	}
}
