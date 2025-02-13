package com.morefun.XSanGo.equip;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.EquipExtendsView;
import com.XSanGo.Protocol.EquipPosition;
import com.XSanGo.Protocol.GrowableProperty;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.google.gson.Gson;
import com.morefun.XSanGo.common.BattlePropertyMap;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.hero.BattlePropertyHolder;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.AbsItem;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.EquipGemT;
import com.morefun.XSanGo.item.EquipItemT;
import com.morefun.XSanGo.item.GemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 装备物品类
 * 
 * @author sulingyun
 * 
 */
public class EquipItem extends AbsItem {
	private EquipData data;

	public EquipItem(RoleItem db, AbsItemT template) {
		super(db, template);
		this.data = TextUtil.GSON.fromJson(this.itemDb.getAttachData(), EquipData.class);
		if (this.data == null) {
			this.data = new EquipData();
			this.data.star = 1; // 暂时默认都1星
			// 装备初始成长调整为【初次获取=下限+（上限-下限）*0.7】
			EquipItemT equipTemplate = this.getTemplate(EquipItemT.class);
			float min = Float.parseFloat(equipTemplate.grows[this.getStar() - 1].value);
			float max = Float.parseFloat(equipTemplate.grows[this.getStar()].value);
			int initRate = 7;

			this.setGrow1(min + (max - min) * initRate / 10);
			if (equipTemplate.hasSecondMajorProperty()) {
				min = Float.parseFloat(equipTemplate.grows[this.getStar() + 5].value);
				max = Float.parseFloat(equipTemplate.grows[this.getStar() + 6].value);
				this.setGrow2(min + (max - min) * initRate / 10);
			}
		}
		// 处理第二主属性成长，存档升级
		EquipItemT ei = this.getTemplate(EquipItemT.class);
		if (ei.hasSecondMajorProperty() && this.data.grow2 <= 0) {
			float min = Float.parseFloat(ei.grows[this.getStar() + 5].value);
			float max = Float.parseFloat(ei.grows[this.getStar() + 6].value);
			float temp = (NumberUtil.random(Math.round(min * 100), Math.round(max * 100) + 1) * 1.0F / 100);
			this.setGrow2(temp);
		}
		List<IntString> gemList = this.data.gemList;
		// init gems
		if (gemList == null || gemList.size() <= 0) {
			EquipGemT egt = XsgItemManager.getInstance().getEquipGemWithQuality(template.getColor().ordinal());
			if (gemList == null) {
				gemList = new ArrayList<IntString>();
			}
			for (int i = 0; i < egt.holeNum; i++) {
				gemList.add(new IntString(i, ""));
			}
			this.data.gemList = gemList;
			flushData();
		}
	}

	public String getRefereneHero() {
		return this.data.heroId;
	}

	public void setRefereneHero(IHero refereneHero) {
		this.data.heroId = refereneHero == null ? null : refereneHero.getId();
		this.flushData();
	}

	private void flushData() {
		this.itemDb.setAttachData(TextUtil.GSON.toJson(this.data));
	}

	public EquipPosition getEquipPos() {
		return this.getTemplate(EquipItemT.class).getEquipPos();
	}

	@Override
	public ItemView getView() {
		return new ItemView(getId(), this.getTemplate().getItemType(), this.itemDb.getTemplateCode(),
				this.itemDb.getNum(), LuaSerializer.serialize(new EquipExtendsView(this.getTemplate(EquipItemT.class)
						.getType(), this.data.level, this.data.star, this.data.starExp, this.caculateBattlePower(),
						this.getGrowableProperty(), this.getProperties(), this.data.gemList.toArray(new IntString[0]))));
	}

	public ItemView getView4GM() {
		Gson gson = new Gson();
		return new ItemView(getId(), this.getTemplate().getItemType(), this.itemDb.getTemplateCode(),
				this.itemDb.getNum(), gson.toJson(new EquipExtendsView(this.getTemplate(EquipItemT.class).getType(),
						this.data.level, this.data.star, this.data.starExp, 0, this.getGrowableProperty(), null,
						getGemNameList(this.data.gemList))));
	}

	private IntString[] getGemNameList(List<IntString> pairs) {
		List<IntString> list = new ArrayList<IntString>();
		IntString name = null;
		for (IntString is : pairs) {
			if (!TextUtil.isBlank(is.strValue)) {
				GemT gemT = (GemT) XsgItemManager.getInstance().findAbsItemT(is.strValue);
				name = new IntString();
				name.strValue = gemT.getName();
				list.add(name);
			}
		}
		return list.toArray(new IntString[0]);
	}

	/**
	 * 主属性
	 * 
	 * @return
	 */
	private GrowableProperty[] getGrowableProperty() {
		// 主属性集合
		List<GrowableProperty> list_gp = new ArrayList<GrowableProperty>();
		EquipItemT template = this.getTemplate(EquipItemT.class);

		// 主属性1
		GrowableProperty gp = new GrowableProperty();
		gp.code = XsgHeroManager.getInstance().translatePropertyCode(template.majorProperties[0].code);
		gp.grow = this.getGrow();
		gp.value = (int) (template.majorProperties[0].value + (this.getLevel() - 1) * gp.grow);
		list_gp.add(gp);
		// 主属性2
		if (template.hasSecondMajorProperty()) {
			gp = new GrowableProperty();
			gp.code = XsgHeroManager.getInstance().translatePropertyCode(template.majorProperties[1].code);
			gp.grow =  this.getGrow2();
			gp.value = (int) (template.majorProperties[1].value + (this.getLevel() - 1) * gp.grow);
			list_gp.add(gp);
		}
		return list_gp.toArray(new GrowableProperty[0]);
	}

	/**
	 * 附属性
	 * 
	 * @return
	 */
	private Property[] getProperties() {
		// 副属性集合
		List<Property> list = new ArrayList<Property>();
		EquipItemT template = this.getTemplate(EquipItemT.class);

		for (com.morefun.XSanGo.item.PropertyT property : template.attachProperties) {
			if (property.isEffective()) {
				list.add(new Property(XsgHeroManager.getInstance().translatePropertyCode(property.code), property.value));
			}
		}

		return list.toArray(new Property[0]);
	}

	/**
	 * 计算装备战力
	 * 
	 * @return
	 */
	public int caculateBattlePower() {
		return XsgHeroManager.getInstance()
				.caculateBattlePower(this.getBattlePropertyMap(), BattlePropertyHolder.Equip);
	}

	/**
	 * 装备强化逻辑
	 * 
	 * @param levelup
	 * @param money
	 */
	public void levelup(int levelup, int money) {
		this.data.level += levelup;
		this.data.levelupConsume += money;
		this.flushData();
	}

	public int getStarExp() {
		return this.data.starExp;
	}

	public void setStarExp(int exp) {
		this.data.starExp = exp;
		this.flushData();
	}

	/**
	 * 获取当前获得的总经验
	 * */
	public long getAllStarExp() {
		int star = getStar();
		long total = getStarExp();
		if (star > 1) {
			for (int i = 2; i <= star; i++) {
				EquipStarT starT = XsgEquipManager.getInstance().findStarT(i);
				EquipStarUpConditionT condition = starT.conditions[getQuatityColor().ordinal()];
				total += condition.exp;
			}
		}
		return total;
	}

	public final float getGrow() {
		return this.data.grow1;
	}

	public void setGrow1(float f) {
		this.data.grow1 = f;
		this.flushData();
	}

	public QualityColor getQuatityColor() {
		return this.getTemplate(EquipItemT.class).getColor();
	}

	public final byte getStar() {
		return this.data.star;
	}

	public int getLevel() {
		return this.data.level;
	}

	public int getTotalLevelupConsume() {
		return this.data.levelupConsume;
	}

	public void setStar(byte star) {
		this.data.star = star;
		this.flushData();
	}
	
	public void setLevel(int level) {
		this.data.level = level;
		this.flushData();
	}

	/**
	 * 重铸
	 */
	public final void rebuild() {
		EquipItemT template = this.getTemplate(EquipItemT.class);
		float min = Float.parseFloat(template.grows[this.getStar() - 1].value);
		float max = Float.parseFloat(template.grows[this.getStar()].value);
		float temp;

		if (!(this.getGrow() == max)) {
			temp = (NumberUtil.random(Math.round(min * 100), Math.round(max * 100) + 1) * 1.0F / 100);
			this.setGrow1(temp);
		}

		if (template.hasSecondMajorProperty()) {
			min = Float.parseFloat(template.grows[this.getStar() + 5].value);
			max = Float.parseFloat(template.grows[this.getStar() + 6].value);
			if (!(this.getGrow2() == max)) {
				temp = (NumberUtil.random(Math.round(min * 100), Math.round(max * 100) + 1) * 1.0F / 100);
				this.setGrow2(temp);
			}
		}
	}

	public float getGrow2() {
		return this.data.grow2;
	}

	private void setGrow2(float f) {
		this.data.grow2 = f;
		this.flushData();
	}

	/**
	 * 获取属性表
	 * 
	 * @return
	 */
	public BattlePropertyMap getBattlePropertyMap() {
		Property[] properties = this.getProperties();
		GrowableProperty[] growableProperty = this.getGrowableProperty();
		BattlePropertyMap map = new BattlePropertyMap();
		for (GrowableProperty p : growableProperty) {
			map.combine(p.code, p.value);
		}
		for (Property p : properties) {
			map.combine(p.code, p.value);
		}
		// 宝石属性,战斗时叠加宝石效果
		for (IntString is : data.gemList) {
			if (!TextUtil.isBlank(is.strValue)) {
				GemT gemT = (GemT) XsgItemManager.getInstance().findAbsItemT(is.strValue);
				if (gemT != null) {
					map.combine(gemT.property, gemT.propValue);
				}
			}
		}
		return map;
	}

	public List<IntString> getGemPairs() {
		return data.gemList;
	}

	public IntString getGemPairWithPosition(int pos) {
		for (IntString is : this.data.gemList) {
			if (is.intValue == pos) {
				return is;
			}
		}
		return null;
	}

	public boolean addGemWithPosition(int pos) {
		IntString is = getGemPairWithPosition(pos);

		if (is != null)
			return false;

		is = new IntString(pos, "");
		this.data.gemList.add(is);
		flushData();
		return true;
	}

	public boolean setGemWithPosition(int pos, String gemId) {
		IntString is = getGemPairWithPosition(pos);

		if (is == null)
			return false;

		is.strValue = gemId;
		flushData();
		return true;
	}

	@Override
	public String toString() {
		return TextUtil.format("[name={0},num={1},id={2}],star={3},exp={4}", this.getTemplate().getName(),
				this.getNum(), this.getId(), this.getStar(), this.getStarExp());
	}
}

class EquipData {
	public int level = 1;

	public byte star;

	public int starExp;

	public float grow1;

	public float grow2;

	public String heroId;

	public int levelupConsume;

	public List<IntString> gemList;
}
