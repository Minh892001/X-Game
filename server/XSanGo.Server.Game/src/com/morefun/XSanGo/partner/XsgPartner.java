package com.morefun.XSanGo.partner;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.XSanGo.Protocol.HeroState;
import com.morefun.XSanGo.common.BattlePropertyMap;
import com.morefun.XSanGo.db.game.RolePartner;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class XsgPartner implements IPartner {
	
	private IRole roleRt;
	private RolePartner db;
	/**位置-武将详细信息*/
	private Map<Integer, IHero> heroMap;
	
	/**位置-伙伴属性详细信息*/
	private Map<Integer, PartnerPropT> propMap;
	/**位置-特殊武将模版信息*/
	private Map<Integer, PartnerHeroT> specialHeroMap;
	
	public XsgPartner() {}
	
	public XsgPartner(IRole roleRt, RolePartner db) {
		this.roleRt = roleRt;
		this.db = db;
		
		PartnerConfig config = TextUtil.GSON.fromJson(this.db.getConfigs(),
				PartnerConfig.class);
		
		this.heroMap = new HashMap<Integer, IHero>();
		if (config.heroPosition != null &&config.heroPosition.size()>0) {
			for (int pos : config.heroPosition.keySet()) {
				this.heroMap.put(
						pos,
						this.roleRt.getHeroControler().getHero(
								config.heroPosition.get(pos)));
			}
		}
		
		this.propMap = new HashMap<Integer, PartnerPropT>();
		if(config.positionProp != null && config.positionProp.size()>0){
			for (int pos : config.positionProp.keySet()) {
				int id = config.positionProp.get(pos);
				PartnerPropT prop = XsgPartnerManager.getInstance().findPropById(id);
				propMap.put(pos, prop);
			}
		}
		
		this.specialHeroMap = new HashMap<Integer, PartnerHeroT>();
		if (config.specialHeroPosition != null && config.specialHeroPosition.size()>0) {
			for (int pos : config.specialHeroPosition.keySet()) {
				int id = Integer.valueOf(config.specialHeroPosition.get(pos));
				PartnerHeroT heroT =  XsgPartnerManager.getInstance().findHeroById(id);
				this.specialHeroMap.put(pos,heroT);
			}
		}
		
	}



	@Override
	public IHero getHeroByPos(int pos) {
		return heroMap.get(pos);
	}

	@Override
	public boolean containsHero(int id) {
		for (IHero hero : this.heroMap.values()) {
			if (hero.getTemplateId() == id) {
				return true;
			}
		}

		return false;
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


	/**
	 * 更新内存数据到数据库
	 */
	private void flushData() {
		PartnerConfig config = new PartnerConfig();

		config.heroPosition = new HashMap<Integer, String>();
		
		for (int key : this.heroMap.keySet()) {
			config.heroPosition.put(key, this.heroMap.get(key).getId());
		}
		
		config.positionProp = new HashMap<Integer, Integer>();
		for (int  key : this.propMap.keySet()) {
			config.positionProp.put(key, this.propMap.get(key).id);
		}
		
		config.specialHeroPosition = new HashMap<Integer, String>();
		for (Map.Entry<Integer, PartnerHeroT> entry : this.specialHeroMap.entrySet()) {
			config.specialHeroPosition.put(entry.getKey(), entry.getValue().id+"");
		}
		this.db.setConfigs(TextUtil.GSON.toJson(config));;
		
	}

	@Override
	public BattlePropertyMap getBattlePropertyMap() {

		Map<Integer, IHero> heroMap = this.getHeroMap();
		Map<Integer, PartnerPropT> propMap = this.getPropMap();
		Map<Integer, PartnerHeroT> specialHeroMap = this.getSpecialHeroMap();

		BattlePropertyMap map = new BattlePropertyMap();

		for (Entry<Integer, IHero> entry : heroMap.entrySet()) {

			int pos = entry.getKey();
			IHero hero = entry.getValue();
			if(hero.getState()!=HeroState.PartnerShip){
				LogManager.warn("partner heroId:"+hero.getId()+", roleId:"+roleRt.getRoleId());
				continue;
			}
			// 当阵位武将是专属武将的时候，加成伙伴属性
			if (hero != null && propMap.get(pos) != null
					&& specialHeroMap.get(pos) != null) {
				// 该位置上的武将是专属武将时候
				if (hero.getTemplateId() == specialHeroMap.get(pos).id) {
					// 获取伙伴位置的属性加成
					PartnerPropT partnerPropT = propMap.get(pos);
					String code = XsgHeroManager.getInstance()
							.translatePropertyCode(partnerPropT.propName);
					// int value = props.getValue(code);
					int value = hero.getBattlePower();
					// 加成属性名，加成属性值
					map.combine(code, value * partnerPropT.propPercent
							/ 100);
				}
			}
		}
	
		return map;
	}
	
	@Override
	public PartnerPropT getPropByPos(int pos) {
		return propMap.get(pos);
	}

	@Override
	public int getHeroCount() {
		return heroMap.size();
	}
	@Override
	public Map<Integer, IHero> getHeroMap() {
		return heroMap;
	}
	@Override
	public Map<Integer, PartnerPropT> getPropMap() {
		return propMap;
	}
	@Override
	public Map<Integer, PartnerHeroT> getSpecialHeroMap() {
		return specialHeroMap;
	}

	@Override
	public String getId() {
		return db.getId();
	}

	@Override
	public boolean isOpened(int pos) {

		return propMap.containsKey(pos);
	}

}
