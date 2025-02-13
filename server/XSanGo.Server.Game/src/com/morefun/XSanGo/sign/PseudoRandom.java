package com.morefun.XSanGo.sign;

public interface PseudoRandom<T> {
	/**
	 * @param controller
	 *            执行伪随机算法命中或要发系统公告。并且需要操作相关的计数器，这个计数器放在了Role里面。
	 *            所以这里需要引用一下SignController
	 * @return 匹配到的RandomRoulette
	 */
	T random(SignController controller);
}