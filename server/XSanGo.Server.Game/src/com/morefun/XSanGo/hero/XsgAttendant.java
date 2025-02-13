/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.XSanGo.Protocol.AttendantView;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.common.Const;

/**
 * 随从对象
 * 
 * @author sulingyun
 * 
 */
public class XsgAttendant {

	/** 模板数据 */
	private AttendantT template;
	/** 该位置的武将 */
	private IHero hero;
	private boolean special;

	/**重置随从消耗物品id*/
	private IntString[] costItems;

	public XsgAttendant(AttendantT attendantT) {
		this.template = attendantT;
	}

	public IHero getHero() {
		return hero;
	}

	public void setHero(IHero hero) {
		this.hero = hero;
		this.special = hero != null
				&& this.template.specialHeroId == hero.getTemplateId();
	}

	public AttendantView generateView() {
		this.special = hero != null
				&& this.template.specialHeroId == hero.getTemplateId();
		if (hero == null) {
			AttendantView attendantView = new AttendantView();
			attendantView.costItems = costItems;
			attendantView.specialAttendantId = template.specialHeroId;
			attendantView.special=false;
			return attendantView;
		}
		return new AttendantView(hero.getId(), this.calculateValue(), special,costItems,template.specialHeroId);
	}

	public Property getProperty() {
		return new Property(template.propertyCode, this.calculateValue());
	}

	private int calculateValue() {
		if (this.hero == null) {
			return 0;
		}

		return hero.getBattlePower()
				* (special ? this.template.specialValue
						: this.template.defaultValue) / Const.Ten_Thousand;
	}
	
	public AttendantT getTemplate() {
		return template;
	}

	public void setTemplate(AttendantT template) {
		this.template = template;
	}
	
	public void setCostItems(IntString[] costItems) {
		this.costItems = costItems;
	}
	
	public void setSpecial(boolean special) {
		this.special = special;
	}
	
}
