/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.formation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.DuelUnitView;
import com.XSanGo.Protocol.FormationPosView;
import com.XSanGo.Protocol.FormationSummaryView;
import com.XSanGo.Protocol.FormationView;
import com.XSanGo.Protocol.IntIntPair;
import com.morefun.XSanGo.battle.DuelUtil;
import com.morefun.XSanGo.db.game.RoleFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.FormationBuffAdvancedT;
import com.morefun.XSanGo.item.FormationBuffItem;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author Su LingYun
 * 
 */
public class XsgFormation implements IFormation {
	private IRole roleRt;

	private RoleFormation db;

	private FormationBuffItem buff;

	private Map<Integer, IHero> heroMap;

	public XsgFormation(IRole role, RoleFormation db) {
		this.roleRt = role;
		this.db = db;

		FormationConfig config = TextUtil.GSON.fromJson(this.db.getConfig(), FormationConfig.class);
		// 阵法书
		if (!TextUtil.isBlank(config.buffBookId)) {
			this.buff = (FormationBuffItem) this.roleRt.getItemControler().getItem(config.buffBookId);
		}
		// 阵位
		this.heroMap = new HashMap<Integer, IHero>();
		if (config.positionMap != null) {
			for (int pos : config.positionMap.keySet()) {
				this.heroMap.put(pos, this.roleRt.getHeroControler().getHero(config.positionMap.get(pos)));
			}
		}
	}

	@Override
	public IHero getHeroByPos(int i) {
		return this.heroMap.get(i);
	}

	@Override
	public boolean containsHero(int i) {
		for (IHero hero : this.heroMap.values()) {
			if (hero.getTemplateId() == i) {
				return true;
			}
		}

		return false;
	}

	@Override
	public FormationBuffItem getBuff() {
		return this.buff;
	}

	@Override
	public void setBuff(FormationBuffItem book) {
		this.buff = book;
		this.flushData();
	}

	private void flushData() {
		FormationConfig config = new FormationConfig();
		if (this.buff != null) {
			config.buffBookId = this.buff.getId();
		}

		config.positionMap = new HashMap<Integer, String>();
		for (int key : this.heroMap.keySet()) {
			config.positionMap.put(key, this.heroMap.get(key).getId());
		}

		this.db.setConfig(TextUtil.GSON.toJson(config));
	}

	@Override
	public void setHeroPosition(int position, IHero hero) {
		if (hero == null) {
			this.heroMap.remove(position);
		} else {
			this.heroMap.put(position, hero);
		}
		this.flushData();
	}

	private FormationPosView[] getPositionView() {
		List<FormationPosView> list = new ArrayList<FormationPosView>();
		for (int index : this.heroMap.keySet()) {
			IHero hero = this.heroMap.get(index);
			if (hero == null) {
				throw new IllegalStateException(TextUtil.format("Hero not found.FormationId={0},index={1}",
						this.getId(), index));
			}
			list.add(new FormationPosView((byte) index, hero.getId()));
		}

		return list.toArray(new FormationPosView[0]);
	}

	@Override
	public byte getIndex() {
		return this.db.getIndex();
	}

	@Override
	public Iterable<IHero> getHeros() {
		return this.heroMap.values();
	}

	@Override
	public String getId() {
		return this.db.getId();
	}

	@Override
	public FormationView getView() {
		FormationView view = new FormationView();

		view.id = this.getId();
		view.index = this.getIndex();

		if (this.getBuff() != null) {
			view.buffItemId = this.getBuff().getId();
			view.buffAdvanced = TextUtil.GSON.fromJson(getAdvanceds(), IntIntPair[].class);
			view.buffAdvancedType = getAdvancedType();
		}

		view.postions = this.getPositionView();
		view.skills = this.getSkillView();
		view.battlePower = this.calculateBattlePower();

		return view;
	}

	private IntIntPair[] getSkillView() {
		return new IntIntPair[0];
	}

	@Override
	public int getHeroCountExcludeSupport() {
		int count = 0;
		for (int key : this.heroMap.keySet()) {
			if (XsgFormationManager.getInstance().isBattlePosition((byte) key)) {
				count++;
			}
		}

		return count;
	}

	@Override
	public FormationSummaryView[] getSummaryView() {
		List<FormationSummaryView> list = new ArrayList<FormationSummaryView>();
		for (int index : this.heroMap.keySet()) {
			if (!XsgFormationManager.getInstance().isBattlePosition((byte) index)) {
				continue;
			}
			IHero hero = this.heroMap.get(index);
			list.add(hero.getSummaryView(index));
		}

		return list.toArray(new FormationSummaryView[0]);
	}

	/**
	 * 援军摘要信息视图，一般在给其他玩家展示时调用
	 * 
	 * @return
	 */
	@Override
	public FormationSummaryView[] getSupportSummaryView() {
		List<FormationSummaryView> list = new ArrayList<FormationSummaryView>();
		for (int index : this.heroMap.keySet()) {
			if (!XsgFormationManager.getInstance().isBattlePosition((byte) index)) {
				IHero hero = this.heroMap.get(index);
				list.add(hero.getSummaryView(index));
			}
		}

		return list.toArray(new FormationSummaryView[0]);
	}

	@Override
	public IHero randomDuelHero() {
		List<RandomDuel> list = new ArrayList<RandomDuel>();

		for (int pos : this.heroMap.keySet()) {
			if (!XsgFormationManager.getInstance().isDuelPosition((byte) pos)) {
				continue;
			}
			IHero hero = this.heroMap.get(pos);
			if (hero.getTemplate().canDuel()) {
				list.add(new RandomDuel(hero));
			}
		}

		if (list.size() == 0) {
			return null;
		}

		RandomRange<RandomDuel> range = new RandomRange<RandomDuel>(list);
		return range.random().getHero();

	}

	@Override
	public int calculateBattlePower() {
		int total = 0;
		for (int key : this.heroMap.keySet()) {
			// if (XsgFormationManager.getInstance().isBattlePosition((byte)
			// key)) {
			total += this.heroMap.get(key).getBattlePower();
			// }
		}
		// 增加阵法进阶战力
		IntIntPair[] ip = TextUtil.GSON.fromJson(getAdvanceds(), IntIntPair[].class);
		int type = getAdvancedType();
		int level = 0;
		for (IntIntPair i : ip) {
			if (i.first == type) {
				level = i.second;
				break;
			}
		}
		FormationBuffAdvancedT baT = XsgItemManager.getInstance().getByLevelAndType(level, type);
		if (baT != null) {
			total += baT.addPower;
		}
		return total;
	}

	@Override
	public byte getHeroCountIncludeSupport() {
		return (byte) this.heroMap.size();
	}

	@Override
	public String toString() {
		return TextUtil.format("[idx={0},data={1}]", this.db.getIndex(), this.db.getConfig());
	}

	@Override
	public DuelUnitView[] generateDuelCandidateData() {
		List<DuelUnitView> list = new ArrayList<DuelUnitView>();
		for (int pos : this.heroMap.keySet()) {
			if (!XsgFormationManager.getInstance().isDuelPosition((byte) pos)) {
				continue;
			}
			IHero hero = this.heroMap.get(pos);
			list.add(DuelUtil.createDuelUnitView(hero.createDuelUnit()));
		}

		return list.toArray(new DuelUnitView[0]);
	}

	@Override
	public int getAdvancedType() {
		return db.getAdvancedType();
	}

	@Override
	public String getAdvanceds() {
		String advs = db.getAdvanceds();
		if (TextUtil.isBlank(advs)) {
			return "[]";
		}
		return advs;
	}

	@Override
	public void setAdvancedType(int type) {
		db.setAdvancedType(type);
	}

	@Override
	public void setAdvanceds(String advanceds) {
		db.setAdvanceds(advanceds);
	}
}

class RandomDuel implements IRandomHitable {
	private IHero hero;

	public RandomDuel(IHero hero) {
		this.hero = hero;
	}

	public IHero getHero() {
		return this.hero;
	}

	@Override
	public int getRank() {
		// for (Property p : this.hero.getHeroView().properties) {
		// if (p.code.equals(Const.PropertyName.Hero.Brave)) {
		// return p.value;
		// }
		// }
		return this.hero.getTemplate().brave;
	}

}

class FormationConfig {
	public String buffBookId;

	public Map<Integer, String> positionMap;
}
