/**
 * 
 */
package com.morefun.XSanGo.common;

import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.equip.EquipStarT;
import com.morefun.XSanGo.equip.XsgEquipManager;

/**
 * 游戏公式辅助类
 * 
 * @author sulingyun
 * 
 */
public class FormulaUtil {

	/**
	 * 计算装备被吞噬时候提供的星级经验
	 * 
	 * @param quatityColor
	 * @param star
	 * @return
	 */
	public static int caculateEquipProvideStarExp(EquipItem equipItem) {
		int star = equipItem.getStar();
		QualityColor quatityColor = equipItem.getQuatityColor();
		EquipStarT template = XsgEquipManager.getInstance().findStarT(star);
		if (template == null) {
			return 0;
		}
		int exp = equipItem.getStarExp();
		// 按所有升星经验/3
		while (star > 0) {
			template = XsgEquipManager.getInstance().findStarT(star);
			exp += template.conditions[quatityColor.ordinal()].exp;
			star--;
		}
		return exp / 3;
	}

	/**
	 * 计算武将暴击率
	 * 
	 * @param crit
	 * @param level
	 * @return
	 */
	public static double calculateCritRate(int crit, int level) {
		if (crit <= 80) { // 若 crit<=80
							// BaseCritChance=10000×(crit×0.06)÷(crit×0.06＋70)
			return Const.Ten_Thousand * crit * 0.06 / (crit * 0.06 + 70);
		} else if (crit > 80 && crit <= 1300) {// 若 80<crit<=1300
												// BaseCritChance=10000×((crit－80)×0.04)÷((crit－80)×0.04＋330)＋641
			return Const.Ten_Thousand * ((crit - 80) * 0.04)
					/ ((crit - 80) * 0.04 + 330) + 641;

		} else if (crit > 1300 && crit <= 2200) {// 若 1300<crit<=2200
													// BaseCritChance=10000×((crit－1300)×0.06)÷((crit－1300)×0.06＋280)＋1929
			return Const.Ten_Thousand * ((crit - 1300) * 0.06)
					/ ((crit - 1300) * 0.06 + 280) + 1929;

		} else if (crit > 2200 && crit <= 2400) {// 若 2200<crit<=2400
													// BaseCritChance=10000×((crit－2200)×0.06)÷((crit－2200)×0.06＋70)＋3545
			return Const.Ten_Thousand * ((crit - 2200) * 0.06)
					/ ((crit - 2200) * 0.06 + 70) + 3545;

		} else {// 若 crit>2400
			// BaseCritChance=10000×((crit－2400)×0.04)÷((crit－2400)×0.04＋350)＋5008
			return Const.Ten_Thousand * ((crit - 2400) * 0.04)
					/ ((crit - 2400) * 0.04 + 350) + 5008;
		}
	}

	/**
	 * 计算武将抗暴率
	 * 
	 * @param decrit
	 * @param level
	 * @return
	 */
	public static double calculateDecritRate(int decrit, int level) {
		if (decrit <= 80) {// 若 rescrit<=80
							// BaseResCritChance=10000×(rescrit×0.06)÷(rescrit×0.06＋180)
			return Const.Ten_Thousand * (decrit * 0.06) / (decrit * 0.06 + 180);
		} else if (decrit > 80 && decrit <= 1300) {// 若 80<rescrit<=1300
													// BaseResCritChance=10000×((rescrit－80)×0.04)÷((rescrit－80)×0.04＋860)＋259
			return Const.Ten_Thousand * ((decrit - 80) * 0.04)
					/ ((decrit - 80) * 0.04 + 860) + 259;
		} else if (decrit > 1300 && decrit <= 2200) {// 若 1300<rescrit<=2200
														// BaseResCritChance=10000×((rescrit－1300)×0.06)÷((rescrit－1300)×0.06＋590)＋795
			return Const.Ten_Thousand * ((decrit - 1300) * 0.06)
					/ ((decrit - 1300) * 0.06 + 590) + 795;
		} else if (decrit > 2200 && decrit <= 2400) {// 若 2200<rescrit<=2400
														// BaseResCritChance=10000×((rescrit－2200)×0.06)÷((rescrit－2200)×0.06＋210)＋1633
			return Const.Ten_Thousand * ((decrit - 2200) * 0.06)
					/ ((decrit - 2200) * 0.06 + 210) + 1633;
		} else {// 若 rescrit>2400
				// BaseResCritChance=10000×((rescrit－2400)×0.04)÷((rescrit－2400)×0.04＋500)＋2173
			return Const.Ten_Thousand * ((decrit - 2400) * 0.04)
					/ ((decrit - 2400) * 0.04 + 500) + 2173;
		}
	}

	public static void main(String[] args) {
		double test = calculateDefPoro(1000, 10);
		System.out.println(test);
	}

	/**
	 * 物理伤害减免计算公式
	 * 
	 * @param phyDef
	 *            护甲值
	 * @param level
	 * @return
	 */
	public static int calculateDefPoro(int phyDef, int level) {
		if (phyDef <= 140) {// 若 armor<=140
							// armor_def=10000×(armor×0.04)÷(armor×0.04＋100)
			return (int) (Const.Ten_Thousand * (phyDef * 0.04) / (phyDef * 0.04 + 100));
		} else if (phyDef > 140 && phyDef <= 2800) {// 若 140<armor<=2800
													// armor_def=10000×((armor－140)×0.04)÷((armor－140)×0.04＋850)＋530
			return (int) (Const.Ten_Thousand * ((phyDef - 140) * 0.04)
					/ ((phyDef - 140) * 0.04 + 850) + 530);
		} else if (phyDef > 2800 && phyDef <= 3200) {// 若 2800<armor<=3200
														// armor_def=10000×((armor－2800)×0.04)÷((armor－2800)×0.04＋195)＋1642
			return (int) (Const.Ten_Thousand * ((phyDef - 2800) * 0.04)
					/ ((phyDef - 2800) * 0.04 + 195) + 1642);
		} else if (phyDef > 3200 && phyDef <= 4300) {// 若 3200<armor<=4300
														// armor_def=10000×((armor－3200)×0.04)÷((armor－3200)×0.04＋950)＋2400
			return (int) (Const.Ten_Thousand * ((phyDef - 3200) * 0.04)
					/ ((phyDef - 3200) * 0.04 + 950) + 2400);
		} else if (phyDef > 4300 && phyDef <= 4800) {// 若 4300<armor<=4800
														// armor_def=10000×((armor－4300)×0.04)÷((armor－4300)×0.04＋180)＋2842
			return (int) (Const.Ten_Thousand * ((phyDef - 4300) * 0.04)
					/ ((phyDef - 4300) * 0.04 + 180) + 2842);
		} else if (phyDef > 4800 && phyDef <= 6200) {// 若 4800<armor<=6200
														// armor_def=10000×((armor－4800)×0.04)÷((armor－4800)×0.04＋1150)＋3842
			return (int) (Const.Ten_Thousand * ((phyDef - 4800) * 0.04)
					/ ((phyDef - 4800) * 0.04 + 1150) + 3842);
		} else if (phyDef > 6200 && phyDef <= 6600) {// 若 6200<armor<=6600
														// armor_def=10000×((armor－6200)×0.04)÷((armor－6200)×0.04＋180)＋4306
			return (int) (Const.Ten_Thousand * ((phyDef - 6200) * 0.04)
					/ ((phyDef - 6200) * 0.04 + 180) + 4306);
		} else {// 若 armor>6600
				// armor_def=10000×((armor－6600)×0.04)÷((armor－6600)×0.04＋1150)＋5122
			return (int) (Const.Ten_Thousand * ((phyDef - 6600) * 0.04)
					/ ((phyDef - 6600) * 0.04 + 1150) + 5122);
		}
	}

	/**
	 * 魔法伤害减免计算公式
	 * 
	 * @param magicDef
	 *            魔法防御
	 * @param level
	 * @return
	 */
	public static int calculateMagicDefPoro(int magicDef, int level) {
		// 目前算法跟护甲计算伤害减免完全一样
		return calculateDefPoro(magicDef, level);
	}
}
