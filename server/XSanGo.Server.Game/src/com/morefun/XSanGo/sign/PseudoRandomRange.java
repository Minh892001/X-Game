package com.morefun.XSanGo.sign;

import java.util.List;

import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;

/**
 * 签到模块伪随机实现类
 */
public class PseudoRandomRange extends RandomRange<RandomRoulette> implements
		PseudoRandom<RandomRoulette> {
	PseudoRandomSignTcT tcT;
	RandomRoulette finalTcItem;

	public PseudoRandomRange(PseudoRandomSignTcT tcT,
			List<RandomRoulette> list, RandomRoulette finalItem) {
		super(list);
		this.tcT = tcT;
		this.finalTcItem = finalItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.morefun.XSanGo.sign.PseudoRandom#random(com.morefun.XSanGo.sign.
	 * SignController)
	 */
	public RandomRoulette random(SignController controller) {
		int pseudoRandomSeek = controller.getSignCountForTc();
		// 当>=这个数量时，如果没有抽中过指定道具，则必定抽中
		if (pseudoRandomSeek >= tcT.max ||
		// 从这个累积数量开始伪随机，每次伪随机概率是1/(Max-Min)
				(pseudoRandomSeek >= tcT.min && NumberUtil.random(tcT.max
						- tcT.min) < 1)) {
			controller.lastRouletteItem = finalTcItem;
			controller.resetSignCountForTc();
			return finalTcItem;
		} else {
			RandomRoulette item = random();
			if (item.neetBroadcast() || true) {
				controller.lastRouletteItem = item;
			}
			controller.increaseSignCountForTc();
			return item;
		}
	}
}
