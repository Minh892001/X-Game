/**
 * 
 */
package com.morefun.XSanGo.role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.event.protocol.IAttendantChange;
import com.morefun.XSanGo.event.protocol.IAttendantReset;
import com.morefun.XSanGo.event.protocol.IBuffAdvance;
import com.morefun.XSanGo.event.protocol.ICombatPowerChange;
import com.morefun.XSanGo.event.protocol.IEquipRebuild;
import com.morefun.XSanGo.event.protocol.IEquipStarUp;
import com.morefun.XSanGo.event.protocol.IEquipStrengthen;
import com.morefun.XSanGo.event.protocol.IFormationBuffChange;
import com.morefun.XSanGo.event.protocol.IFormationBuffLevelUp;
import com.morefun.XSanGo.event.protocol.IFormationClear;
import com.morefun.XSanGo.event.protocol.IFormationPosChange;
import com.morefun.XSanGo.event.protocol.IHeroArtifactChange;
import com.morefun.XSanGo.event.protocol.IHeroAwaken;
import com.morefun.XSanGo.event.protocol.IHeroBaptize;
import com.morefun.XSanGo.event.protocol.IHeroBaptizeReset;
import com.morefun.XSanGo.event.protocol.IHeroBaptizeUpgrade;
import com.morefun.XSanGo.event.protocol.IHeroBreakUp;
import com.morefun.XSanGo.event.protocol.IHeroEquipChange;
import com.morefun.XSanGo.event.protocol.IHeroLevelUp;
import com.morefun.XSanGo.event.protocol.IHeroPractice;
import com.morefun.XSanGo.event.protocol.IHeroQualityUp;
import com.morefun.XSanGo.event.protocol.IHeroRelationChange;
import com.morefun.XSanGo.event.protocol.IHeroSkillUp;
import com.morefun.XSanGo.event.protocol.IHeroStarUp;
import com.morefun.XSanGo.event.protocol.IPartnerClear;
import com.morefun.XSanGo.event.protocol.IPartnerPosChange;
import com.morefun.XSanGo.event.protocol.IPartnerStateChange;
import com.morefun.XSanGo.event.protocol.IResetPractice;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.FormationBuffItem;
import com.morefun.XSanGo.partner.IPartner;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 战力维护对象
 * 
 * @author linyun.su
 * 
 */
public class CombatPowerControler implements IFormationPosChange, IFormationClear, IFormationBuffLevelUp,
		IFormationBuffChange, IHeroQualityUp, IHeroStarUp, IHeroSkillUp, IAttendantChange, IHeroEquipChange,
		IEquipStarUp, IEquipRebuild, IEquipStrengthen, IHeroPractice, IResetPractice, IHeroRelationChange,
		IHeroBreakUp, IPartnerClear, IPartnerPosChange, IPartnerStateChange, IAttendantReset, IHeroLevelUp,
		IBuffAdvance, IHeroAwaken, IHeroBaptizeUpgrade, IHeroBaptize, IHeroBaptizeReset, IHeroArtifactChange {

	private IRole rt;
	private Role db;
	private ICombatPowerChange powerChangeEvent;

	public CombatPowerControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;
		this.powerChangeEvent = this.rt.getEventControler().registerEvent(ICombatPowerChange.class);

		rt.getEventControler().registerHandler(IFormationPosChange.class, this);
		rt.getEventControler().registerHandler(IFormationClear.class, this);
		rt.getEventControler().registerHandler(IFormationBuffLevelUp.class, this);
		rt.getEventControler().registerHandler(IFormationBuffChange.class, this);
		rt.getEventControler().registerHandler(IHeroQualityUp.class, this);
		rt.getEventControler().registerHandler(IHeroStarUp.class, this);
		rt.getEventControler().registerHandler(IHeroSkillUp.class, this);
		rt.getEventControler().registerHandler(IAttendantChange.class, this);
		rt.getEventControler().registerHandler(IHeroEquipChange.class, this);
		rt.getEventControler().registerHandler(IEquipStarUp.class, this);
		rt.getEventControler().registerHandler(IEquipRebuild.class, this);
		rt.getEventControler().registerHandler(IEquipStrengthen.class, this);
		rt.getEventControler().registerHandler(IHeroPractice.class, this);
		rt.getEventControler().registerHandler(IResetPractice.class, this);
		rt.getEventControler().registerHandler(IHeroRelationChange.class, this);
		rt.getEventControler().registerHandler(IHeroBreakUp.class, this);
		rt.getEventControler().registerHandler(IPartnerClear.class, this);
		rt.getEventControler().registerHandler(IPartnerPosChange.class, this);
		rt.getEventControler().registerHandler(IPartnerStateChange.class, this);
		rt.getEventControler().registerHandler(IAttendantReset.class, this);
		rt.getEventControler().registerHandler(IHeroLevelUp.class, this);
		rt.getEventControler().registerHandler(IBuffAdvance.class, this);
		rt.getEventControler().registerHandler(IHeroAwaken.class, this);
		rt.getEventControler().registerHandler(IHeroBaptize.class, this);
		rt.getEventControler().registerHandler(IHeroBaptizeUpgrade.class, this);
		rt.getEventControler().registerHandler(IHeroBaptizeReset.class, this);
		rt.getEventControler().registerHandler(IHeroArtifactChange.class, this);
	}

	private void equioEvent(EquipItem equip) {
		if (!TextUtil.isBlank(equip.getRefereneHero())) {
			IHero referenceHero = this.rt.getHeroControler().getHero(equip.getRefereneHero());
			this.heroEvent(referenceHero.getTemplateId());
		}
	}

	private void heroEvent(int TemplateId) {
		this.formationEvent();
	}

	// 计算多个部队的最高战力情况
	private void formationEvent() {
		Map<String, IFormation> formationMap = this.rt.getFormationControler().getFormation();
		List<Integer> powerList = new ArrayList<Integer>(formationMap.size());
		for (IFormation formation : formationMap.values()) {
			powerList.add(formation.calculateBattlePower());
		}

		int maxPower = (Integer) Collections.max(powerList);

		int old = this.rt.getCachePower();
		this.rt.setCombatPower(maxPower);
		if (old != maxPower) {
			this.powerChangeEvent.onCombatPowerChange(old, maxPower);
		}
	}

	@Override
	public void onAttendantReset(IHero hero, byte pos, int attendantItemid) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onPartnerStateChange() {
		this.formationEvent();
	}

	@Override
	public void onHeroPositionChange(IPartner partner, int pos, IHero hero) {
		this.formationEvent();
	}

	@Override
	public void onPartnerClear(IPartner partner) {
		this.formationEvent();
	}

	@Override
	public void onHeroBreakUp(IHero hero, int beforeBreakLevel) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onRelationChange(IHero hero, int orignalRelationId, int oldLevel, int level) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onResetPractice(IHero hero, int index, String oldName, int oldColor, int oldLevel, int oldExp,
			String newName, int newColor) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onHeroPractice(IHero hero, int index, String name, int coloe, int oldLevel, int oldExp, int addExp,
			int newLevel, int newExp, int sumGx) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onEquipStrengthen(int auto, EquipItem equip, int beforeLevel, int afterLevel) {
		this.equioEvent(equip);
	}

	@Override
	public void onEquipRebuild(EquipItem equip) {
		this.equioEvent(equip);
	}

	@Override
	public void onEquipStarUp(EquipItem equip, int uplevel, List<EquipItem> deleteList, int money, int addExp,
			Map<String, Integer> consumeStars) {
		this.equioEvent(equip);
	}

	@Override
	public void onHeroEquipChange(IHero hero, EquipItem equip) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onAttendantChange(IHero hero, byte pos, IHero attendant) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onHeroSkillUp(IHero hero, String name, int oldLevel, int newLevel) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onHeroStarUp(IHero hero, int beforeStar) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onHeroQualityUp(IHero hero, int beforeQualityLevel) {
		this.heroEvent(hero.getTemplateId());
	}

	@Override
	public void onFormationBuffChange(IFormation formation, FormationBuffItem book) {
		this.formationEvent();
	}

	@Override
	public void onFormationBuffLevelUp(FormationBuffItem buff, int money, int expDiff, int beforeLevel, int beforeExp,
			int afterLevel, int afterExp) {
		this.formationEvent();
	}

	@Override
	public void onFormationClear(IFormation formation) {
		this.formationEvent();
	}

	@Override
	public void onFormationPositionChange(IFormation formation, int pos, IHero hero) {
		this.formationEvent();
	}

	@Override
	public void onHeroLevelUp(int tempId, int lvl) {
		this.heroEvent(tempId);
	}

	@Override
	public void onBuffAdvanceChange(int type, int currentType, String useBuffIds) {
		if (type == 0) {// 变化战力让客户端提示保存
			int old = this.rt.getCachePower();
			this.powerChangeEvent.onCombatPowerChange(old, old - 10000);
		}
		this.formationEvent();
	}

	@Override
	public void onHeroAwaken(int templeId, int star, boolean isAwaken) {
		this.formationEvent();
	}

	@Override
	public void onBaptizeUpgrade(String heroId, int lvl) {
		this.formationEvent();
	}

	@Override
	public void onHeroBaptize(String heroId, String props, int times) {
		this.formationEvent();
	}

	@Override
	public void onReset(String heroId, int baptizeLvl, String baptizeProps, String items) {
		this.formationEvent();
	}

	@Override
	public void onHeroArtifactChange(String oldHero, String newHero, int artifactId, int artifactLevel) {
		if (TextUtil.isNotBlank(oldHero) || TextUtil.isNotBlank(newHero)) {
			this.formationEvent();
		}
	}
}