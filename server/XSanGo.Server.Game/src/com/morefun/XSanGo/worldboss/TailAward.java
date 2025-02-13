package com.morefun.XSanGo.worldboss;

/**
 * 尾刀奖励
 * 
 * @author xiongming.li
 *
 */
public class TailAward {
	public int hp;
	public String roleId;
	public boolean isReceive;

	public TailAward(int hp, String roleId, boolean isReceive) {
		super();
		this.hp = hp;
		this.roleId = roleId;
		this.isReceive = isReceive;
	}
}
