package com.morefun.XSanGo.battle;

import com.XSanGo.Protocol.DuelTemplateType;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.util.TextUtil;

public class OneVsOneTest {

	public static void main(String[] args) {
		DuelUnit first = new DuelUnit(DuelTemplateType.DuelTemplateTypeHero,
				4201, 4, 3, 8, (byte)0, "赵云", 10, 2913, 60, 50, 361, 217, 500, 0, 0,
				120, XsgHeroManager.getInstance().findDuelSkillT(26));

		DuelUnit second = new DuelUnit(DuelTemplateType.DuelTemplateTypeHero,
				4401, 5, 2, 9, (byte)0, "董卓", 10, 2913, 60, 50, 361, 217, 500, 0, 0,
				120, XsgHeroManager.getInstance().findDuelSkillT(26));

		DuelBattle battle = new DuelBattle(first, second);
		DuelReport report = battle.fuckEachOther();
		System.out.println(TextUtil.GSON.toJson(report.generateView()));
		// System.out.println(report.toString());
	}
}

interface TestInterface {
	void test();
}