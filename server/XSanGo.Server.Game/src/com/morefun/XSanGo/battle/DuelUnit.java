/**
 * 
 */
package com.morefun.XSanGo.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.AttackResult;
import com.XSanGo.Protocol.DuelBuffView;
import com.XSanGo.Protocol.DuelTemplateType;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 单挑单位
 * 
 * @author sulingyun
 *
 */
public class DuelUnit {

	public static final int MaxSp = 1000;
	private static final int Arrow_Percent = 20;
	private static final int Skill_SecKill_ID = 1;
	private static final int Skill_Arrow_ID = 2;
	public static final int Skill_Follow = 3;
	private static final int Skill_Follow_Arrow = 4;
	public static final int Skill_Escape = 5;
	/** 每次受击增加的怒气 */
	private static final int Sp_By_Damage = 200;
	/** 每次受击增加的怒气 */
	private static final int Sp_After_Attack = 200;

	/** 血量 */
	private int hp;
	private int maxHp;
	/** 怒气 */
	private int sp;

	/** 初始武力 */
	private int initPower;
	private int power;
	private int intel;
	/** 勇猛 */
	private int brave;
	/** 冷静 */
	private int calm;
	/** 闪避万份比 */
	private int dodge;

	/** 暴击率万份比 */
	private int critRate;

	/** 抗暴率万份比 */
	private int critResRate;

	/** 等级 */
	private int level;
	/** 战斗过程唯一标识符 */
	private int identity;
	/** 模板类型 */
	private DuelTemplateType templateType;
	/** 模板ID */
	private int templateId;
	/** 武将名字 */
	private String name;

	private boolean alreadyCheckEscape;
	private DuelSkillT skillT;
	/** BUFF集合，KEY为来源技能ID */
	private Map<Integer, AbsDuelBuff> buffMap = new HashMap<Integer, AbsDuelBuff>();

	/** 免伤万份比 */
	private int damageRes;
	/** 免伤BUFF加成 */
	private int damageResBuff;
	private int dodgeBuff;
	private int star;
	private int colorLevel;
	private int powerBuff = Const.Ten_Thousand;
	private int quality;
	private byte breakLevel;

	public DuelUnit(DuelTemplateType type, int templateId, int color, int star,
			int quality, byte breakLevel, String name, int level, int maxHp, int brave,
			int calm, int power, int intel, int dodge, int critRate,
			int critResRate, int damageRes, DuelSkillT skillT) {
		this.templateType = type;
		this.templateId = templateId;
		this.colorLevel = color;
		this.star = star;
		this.quality = quality;
		this.breakLevel = breakLevel;
		this.name = name;
		this.level = level;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.dodge = dodge;
		this.critRate = critRate;
		this.critResRate = critResRate;
		this.brave = brave;
		this.calm = calm;
		this.initPower = power;
		this.power = power;
		this.intel = intel;
		this.damageRes = damageRes;
		this.skillT = skillT;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getSp() {
		return sp;
	}

	public void setSp(int sp) {
		sp = Math.min(sp, MaxSp);

		this.sp = sp;

	}

	public int getPower() {
		return power * this.powerBuff / Const.Ten_Thousand;
	}

	public int getIntel() {
		return intel;
	}

	public void setIntel(int intel) {
		this.intel = intel;
	}

	public int getBrave() {
		return brave;
	}

	public void setBrave(int brave) {
		this.brave = brave;
	}

	public int getCalm() {
		return calm;
	}

	public void setCalm(int calm) {
		this.calm = calm;
	}

	public boolean isDead() {
		return this.getHp() < 1;
	}

	public ActionReport attack(DuelUnit target) {
		DuelBuffView buff = null;
		int skillId = 0, spChange1 = 0, spChange2 = 0;
		int damage = this.getPower();
		// float damageRes = (target.getDef() * 1f)
		// / (target.getDef() + 50 * target.getLevel())
		// + target.getDamageResBuff();
		damage = (int) (damage - (damage * target.getDamageRes() / Const.Ten_Thousand));

		AttackResult attackResult = AttackResult.Hit;
		boolean crit = false;
		if (this.isSpFull()) {// 释放大招
			spChange1 = -MaxSp;
			if (this.skillT != null) {
				skillId = this.skillT.id;
				int buffRound = this.skillT.time / 1000;
				final int effectValue = this.skillT.effectValue;
				if (buffRound == 0) {
					damage += (damage * effectValue / Const.Ten_Thousand);
				} else {
					damage = 0;// BUFF不计算伤害
					final DuelUnit buffTarget = (this.skillT.target
							.equals("self")) ? this : target;

					if (buffTarget.buffMap.containsKey(skillId)) {// buff不叠加
						buffTarget.buffMap.get(skillId).expire();
					}
					buff = new DuelBuffView(buffTarget.getIdentity(),
							this.skillT.desc, this.skillT.effect);

					if (this.skillT.effectProperty.equals("武力%")) {
						buffTarget.powerBuff += effectValue;

						buffTarget.buffMap.put(skillId, new AbsDuelBuff(
								this.skillT, buffTarget.getIdentity(),
								buffRound) {
							@Override
							public void expire() {
								buffTarget.powerBuff -= effectValue;
							}
						});
					} else if (this.skillT.effectProperty.equals("免伤%")) {
						buffTarget.damageResBuff += effectValue;

						buffTarget.buffMap.put(skillId, new AbsDuelBuff(
								this.skillT, buffTarget.getIdentity(),
								buffRound) {
							@Override
							public void expire() {
								buffTarget.damageResBuff -= effectValue;
							}
						});
					} else if (this.skillT.effectProperty.equals("闪避%")) {
						buffTarget.dodgeBuff += effectValue;

						buffTarget.buffMap.put(skillId, new AbsDuelBuff(
								this.skillT, buffTarget.getIdentity(),
								buffRound) {
							@Override
							public void expire() {
								buffTarget.dodgeBuff -= effectValue;
							}
						});
					}
				}
			}

			if (buff == null && DuelUtil.calculateBlock(this, target, 1)) {
				attackResult = AttackResult.Block;
			}
		} else {
			spChange1 = Sp_After_Attack;
			if (DuelUtil.calculateDodge(this, target)) {
				attackResult = AttackResult.Dodge;
				damage = 0;
			} else if (DuelUtil.calculateBlock(this, target, 0.8f)) {
				attackResult = AttackResult.Block;
			}
		}
		// 格挡伤害减半
		if (attackResult == AttackResult.Block) {
			damage /= 2;
		}
		// 只有在对自己释放增益BUFF情况下对方怒气才不变
		spChange2 = (buff != null && buff.targetId == this.identity) ? 0
				: Sp_By_Damage;

		if (attackResult != AttackResult.Dodge
				&& DuelUtil.calculateCrit(this, target)) {
			crit = true;
			damage *= 1.5;
			spChange2 *= 1.5;
			if (spChange1 > 0) {
				spChange1 *= 1.5;
			}
		}

		target.reduceHp(damage);
		this.setSp(this.getSp() + spChange1);
		target.setSp(target.getSp() + spChange2);

		ActionReport action = new ActionReport(this.getIdentity(),
				target.getIdentity(), skillId, attackResult, damage, crit,
				this.getSp(), target.getSp(), this.getPower(),
				target.getPower());
		if (buff != null) {
			action.setBuff(buff);
		}

		return action;
	}

	private int getDamageRes() {
		int result = this.damageRes + this.damageResBuff;
		if (result < 0) {
			result = 0;
		}

		return result;
	}

	private void reduceHp(int damage) {
		this.hp -= damage;
		this.hp = Math.max(0, this.hp);
	}

	private boolean isSpFull() {
		return this.getSp() >= MaxSp;
	}

	public int getDodge() {
		int result = dodge + this.dodgeBuff;
		if (result < 0) {
			result = 0;
		}

		return result;
	}

	public int getCritRate() {
		return critRate;
	}

	public int getCritResRate() {
		return critResRate;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * 获得初始减伤
	 * 
	 * @return
	 */
	public int getBaseDamageRes() {
		return this.damageRes;
	}

	/**
	 * 释放冷箭
	 * 
	 * @param target
	 * @return
	 */
	public ActionReport tryArrow(DuelUnit target) {
		// 武力低的一方B武将才有可能释放【冷箭】
		// 【冷箭】释放概率= (A武力 - B武力)/(A武力+B武力)
		int rate = (target.getPower() - this.getPower()) * Const.Ten_Thousand
				/ (this.getPower() + target.getPower());
		if (NumberUtil.isHit(rate, Const.Ten_Thousand)) {
			int damage = (this.getPower() + this.getIntel()) * Arrow_Percent
					/ 100;
			target.reduceHp(damage);
			target.setSp(target.getSp() + Sp_By_Damage);
			return new ActionReport(this.getIdentity(), target.getIdentity(),
					Skill_Arrow_ID, AttackResult.Hit, damage, false,
					this.getSp(), target.getSp(), this.getPower(),
					target.getPower());
		}

		return null;
	}

	public ActionReport trySeckill(DuelUnit target) {
		// 武力高的一方A武将才有可能释放【一击必杀】
		// 【一击必杀】释放概率= (A武力 - B武力)/(A武力+B武力)
		int rate = (this.getPower() - target.getPower()) * Const.Ten_Thousand
				/ (this.getPower() + target.getPower());
		if (NumberUtil.isHit(rate, Const.Ten_Thousand)) {
			int damage = target.getHp();
			target.reduceHp(damage);
			return new ActionReport(this.getIdentity(), target.getIdentity(),
					Skill_SecKill_ID, AttackResult.Hit, damage, false, 0, 0,
					this.getPower(), target.getPower());
		}
		return null;
	}

	public int getIdentity() {
		return this.identity;
	}

	public int getTemplateId() {
		return this.templateId;
	}

	public String getName() {
		return this.name;
	}

	public void setIdentity(int i) {
		this.identity = i;
	}

	public ActionReport createEscapeAction(DuelUnit target) {
		return new ActionReport(this.getIdentity(), 0, Skill_Escape,
				AttackResult.Hit, 0, false, this.getSp(), target.getSp(),
				this.getPower(), target.getPower());
	}

	public ActionReport handleEscape(DuelUnit target) {
		int rate = this.getCalm() * Const.Ten_Thousand
				/ (this.getCalm() + target.getCalm());
		if (!NumberUtil.isHit(rate, Const.Ten_Thousand)) {
			return null;
		}

		int damage = target.getHp();
		target.reduceHp(damage);
		if (this.getCalm() > target.getCalm()) {// 百步穿杨
			return new ActionReport(this.getIdentity(), target.getIdentity(),
					Skill_Follow_Arrow, AttackResult.Hit, damage, false,
					this.getSp(), target.getSp(), this.getPower(),
					target.getPower());
		} else {// 追击
			return new ActionReport(this.getIdentity(), target.getIdentity(),
					Skill_Follow, AttackResult.Hit, damage, false,
					this.getSp(), target.getSp(), this.getPower(),
					target.getPower());
		}

	}

	public boolean alreadyCheckEscape() {
		return this.alreadyCheckEscape;
	}

	public void setEscapeCheck(boolean b) {
		this.alreadyCheckEscape = b;
	}

	public List<AbsDuelBuff> checkBuffExpire() {
		List<AbsDuelBuff> list = new ArrayList<AbsDuelBuff>();
		for (AbsDuelBuff buff : this.buffMap.values()) {
			if (buff.decreaseAndCheckExpire()) {
				list.add(buff);
			}
		}
		// 到期没移除，后果十分严重
		for (AbsDuelBuff buff : list) {
			this.buffMap.remove(buff.getSkillT().id);
		}

		return list;
	}

	public DuelTemplateType getTemplateType() {
		return templateType;
	}

	public void setSkillT(DuelSkillT skillT) {
		this.skillT = skillT;
	}

	public DuelSkillT getSkillT() {
		return this.skillT;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getStar() {
		return this.star;
	}

	public int getColorLevel() {
		return this.colorLevel;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	public void setColorLevel(int color) {
		this.colorLevel = color;
	}

	public int getInitPower() {
		return initPower;
	}

	public int getQuality() {
		return this.quality;
	}
	
	public byte getBreakLevel() {
		return this.breakLevel;
	}
}
