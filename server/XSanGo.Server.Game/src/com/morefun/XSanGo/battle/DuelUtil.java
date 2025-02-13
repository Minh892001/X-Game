/**
 * 
 */
package com.morefun.XSanGo.battle;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.DuelSkillTemplateView;
import com.XSanGo.Protocol.DuelUnitView;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 单挑辅助类
 * 
 * @author sulingyun
 *
 */
public class DuelUtil {

	/**
	 * 是否会被格挡
	 * 
	 * @param attack
	 *            攻击方
	 * @param def
	 *            防守方
	 * @return
	 */
	public static boolean calculateBlock(DuelUnit attack, DuelUnit def,
			float revise) {
		/*
		 * 格挡率与冷静相关，如A武将冷静为L1，B武将冷静为L2，公式如下： A武将格挡率 = L1/((L1+L2)*2) * X B武将格挡率
		 * = L2/((L1+L2)*2) * X 修正参数X：如果是普通攻击，X=0.8，如果是必杀技，X=1
		 */
		if (def.getCalm() == 0) {
			return false;
		}

		float blockRate = def.getCalm()
				/ ((def.getCalm() + attack.getCalm()) * 2 * revise);
		blockRate = Math.min(blockRate, 0.25f);

		return NumberUtil.isHit((int) (blockRate * Const.Ten_Thousand),
				Const.Ten_Thousand);
	}

	/**
	 * 是否能够闪避
	 * 
	 * @param attack
	 * @param def
	 * @return
	 */
	public static boolean calculateDodge(DuelUnit attack, DuelUnit def) {
		return NumberUtil.isHit(def.getDodge(), Const.Ten_Thousand);
	}

	/**
	 * 是否暴击
	 * 
	 * @param attack
	 * @param def
	 * @return
	 */
	public static boolean calculateCrit(DuelUnit attack, DuelUnit def) {
		// double critRate = FormulaUtil.calculateCritRate(attack.getCrit(),
		// attack.getLevel());
		int critRate = attack.getCritRate() - def.getCritResRate();
		critRate = Math.max(0, critRate);
		return NumberUtil.isHit((int) (critRate), Const.Ten_Thousand);
	}

	/**
	 * 创建单挑战斗单位的前端视图对象
	 * 
	 * @param unit
	 * @return
	 */
	public static DuelUnitView createDuelUnitView(DuelUnit unit) {
		DuelSkillT template = unit.getSkillT();
		List<DuelSkillTemplateView> skillList = new ArrayList<DuelSkillTemplateView>();
		if (template != null) {
			skillList.add(createDuelSkillTemplateView(template));
		}
		return new DuelUnitView(unit.getIdentity(), unit.getTemplateType(),
				unit.getTemplateId(), unit.getName(), unit.getMaxHp(),
				unit.getMaxHp(), 0, DuelUnit.MaxSp, unit.getHp(),
				unit.getInitPower(), unit.getStar(), unit.getColorLevel(),
				unit.getQuality(), unit.getBreakLevel(), unit.getLevel(), unit.getBrave(),
				unit.getCalm(), unit.getIntel(), unit.getDodge(),
				unit.getCritRate(), unit.getCritResRate(),
				unit.getBaseDamageRes(),
				skillList.toArray(new DuelSkillTemplateView[0]));
	}

	/**
	 * 生成单挑技能模板视图数据
	 * 
	 * @param template
	 * @return
	 */
	public static DuelSkillTemplateView createDuelSkillTemplateView(
			DuelSkillT template) {
		return new DuelSkillTemplateView(template.id, template.target,
				template.effectProperty, template.effectValue, template.time,
				template.desc, template.effect);
	}

}
