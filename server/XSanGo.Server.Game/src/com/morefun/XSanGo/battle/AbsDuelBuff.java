/**
 * 
 */
package com.morefun.XSanGo.battle;

/**
 * 单挑BUFF抽象类
 * 
 * @author sulingyun
 *
 */
public abstract class AbsDuelBuff {

	private int expireTime;
	private int targetId;
	private DuelSkillT duelSkillT;

	public AbsDuelBuff(DuelSkillT duelSkillT, int targetId, int expireTime) {
		this.duelSkillT = duelSkillT;
		this.targetId = targetId;
		this.expireTime = expireTime;
	}

	/**
	 * BUFF到期活失效时执行
	 */
	public abstract void expire();

	/**
	 * 递减剩余回合数并检查是否到期
	 * 
	 * @return 到期则返回true，否则为false
	 */
	public final boolean decreaseAndCheckExpire() {
		this.expireTime--;
		boolean expire = this.expireTime == 0;
		if (expire) {
			this.expire();
		}

		return expire;
	}

	public int getTargetId() {
		return this.targetId;
	}

	public DuelSkillT getSkillT() {
		return this.duelSkillT;
	}

}
