/**
 * 
 */
package com.morefun.XSanGo.battle;

import com.XSanGo.Protocol.AttackResult;
import com.XSanGo.Protocol.DuelBuffView;

/**
 * 战斗动作
 * 
 * @author sulingyun
 *
 */
public class ActionReport {

	private int skillId;
	private AttackResult attackResult;
	private int damage;
	private boolean crit;
	private int sp1;
	private int sp2;
	private int executorId;
	private int targetId;
	private int power1;
	private int power2;
	private DuelBuffView buff;

	public ActionReport(int executorId, int targetId, int skillId,
			AttackResult attackResult, int damage, boolean crit, int sp1,
			int sp2, int power1, int power2) {
		this.executorId = executorId;
		this.targetId = targetId;
		this.skillId = skillId;
		this.attackResult = attackResult;
		this.damage = damage;
		this.crit = crit;
		this.sp1 = sp1;
		this.sp2 = sp2;
		this.power1 = power1;
		this.power2 = power2;
	}

	public int getSkillId() {
		return skillId;
	}

	public AttackResult getAttackResult() {
		return attackResult;
	}

	public int getDamage() {
		return damage;
	}

	public boolean isCrit() {
		return crit;
	}

	public int getSp1() {
		return sp1;
	}

	public int getSp2() {
		return sp2;
	}

	public int getExecutorId() {
		return this.executorId;
	}

	public int getTargetId() {
		return this.targetId;
	}

	public int getPower1() {
		return this.power1;
	}

	public int getPower2() {
		return this.power2;
	}

	public DuelBuffView getBuff() {
		return buff;
	}

	public void setBuff(DuelBuffView buff) {
		this.buff = buff;
	}
}
