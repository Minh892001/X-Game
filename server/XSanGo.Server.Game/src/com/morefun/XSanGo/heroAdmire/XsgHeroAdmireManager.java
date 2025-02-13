/**
 * 
 */
package com.morefun.XSanGo.heroAdmire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

/**
 * 名将仰慕 全局管理
 * 
 * @author 吕明涛
 * 
 */
public class XsgHeroAdmireManager {
	private static XsgHeroAdmireManager instance = new XsgHeroAdmireManager();

	/** 仰慕武将列表 */
	private Map<Integer, HeroAdmireT> heroMap = new HashMap<Integer, HeroAdmireT>();
	private Map<Integer, List<HeroAdmireT>> heroStarMap = new HashMap<Integer, List<HeroAdmireT>>();
	/** 好感度物品 */
	private Map<Integer, HeroAdmireItemT> itemMap = new HashMap<Integer, HeroAdmireItemT>();
	private Map<Integer, List<HeroAdmireItemT>> itemTypeMap = new HashMap<Integer, List<HeroAdmireItemT>>();
	/** 刷新配置 */
	private HeroAdmireInitT admireInitT;

	public static XsgHeroAdmireManager getInstance() {
		return instance;
	}

	/**
	 * 创建名将仰慕 的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public IHeroAdmireControler createBuyJInbiControler(IRole roleRt,
			Role roleDB) {
		return new HeroAdmireControler(roleRt, roleDB);
	}

	private XsgHeroAdmireManager() {
		loadHeroAdmireScript();
	}
	
	public void loadHeroAdmireScript(){
		admireInitT = ExcelParser.parse(HeroAdmireInitT.class).get(0);
		// 仰慕武将列表
		for (HeroAdmireT heroT : ExcelParser.parse(HeroAdmireT.class)) {
			heroMap.put(heroT.id, heroT);
			
			if(heroT.isShow == 1) {
				List<HeroAdmireT> heroList = heroStarMap.get(heroT.Star);
				if (heroList == null) {
					heroList = new ArrayList<HeroAdmireT>();
				}
				heroList.add(heroT);
				heroStarMap.put(heroT.Star, heroList);
			}
			
		}
		// 好感度物品
		for (HeroAdmireItemT itemT : ExcelParser.parse(HeroAdmireItemT.class)) {
			itemMap.put(itemT.id, itemT);
			
			if(itemT.isShow == 1) {
				List<HeroAdmireItemT> itemList = itemTypeMap.get(itemT.type);
				if (itemList == null) {
					itemList = new ArrayList<HeroAdmireItemT>();
				}
				itemList.add(itemT);
				itemTypeMap.put(itemT.type, itemList);
			}
		}
	}

	public HeroAdmireT getHeroMap(int id) {
		return heroMap.get(id);
	}

	public List<HeroAdmireT> getHeroStarMap(int star) {
		return heroStarMap.get(star);
	}

	public HeroAdmireItemT getItemMap(int id) {
		return itemMap.get(id);
	}

	public List<HeroAdmireItemT> getItemTypeMap(int id) {
		return itemTypeMap.get(id);
	}

	public HeroAdmireInitT getAdmireInitT() {
		return admireInitT;
	}
}
