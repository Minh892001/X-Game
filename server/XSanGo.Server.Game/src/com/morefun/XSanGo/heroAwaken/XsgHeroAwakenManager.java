/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: XsgHeroAwakenManager
 * 功能描述：
 * 文件名：XsgHeroAwakenManager.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.reflect.TypeToken;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleHeroAwaken;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.heroAwaken.HeroAwakenT;
import com.morefun.XSanGo.heroAwaken.HeroBaptizeConfigT;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 武将觉醒数据管理
 * 
 * @author weiyi.zhao
 * @since 2016-4-13
 * @version 1.0
 */
public class XsgHeroAwakenManager {

	/** 单例 */
	private static XsgHeroAwakenManager instance = new XsgHeroAwakenManager();

	/** 觉醒数据 key:heroid */
	private Map<Integer, HeroAwakenT> heroAwakens = new HashMap<Integer, HeroAwakenT>();

	/** 洗炼参数 key:baptizeLvl */
	private Map<Integer, HeroBaptizeConfigT> heroBaptizeConfigs = new HashMap<Integer, HeroBaptizeConfigT>();

	/** 洗炼属性 key:propType */
	private Map<String, HeroBaptizeT> heroBaptizes = new LinkedHashMap<String, HeroBaptizeT>();

	/** 洗炼附加属性 key:baptizeLvl */
	private Map<Integer, HeroBaptizeAddPropT> heroBaptizeAddProps = new TreeMap<Integer, HeroBaptizeAddPropT>();

	/** 最大洗炼等级 */
	private int maxBaptizeLvl;

	/**
	 * 构造函数
	 */
	private XsgHeroAwakenManager() {
		loadHeroAwakenScript();
	}

	/**
	 * 返回单例实例
	 * 
	 * @return
	 */
	public static XsgHeroAwakenManager getInstance() {
		return instance;
	}

	/**
	 * 加载武将觉醒配置
	 */
	private void loadHeroAwakenScript() {
		List<HeroAwakenT> list = ExcelParser.parse(HeroAwakenT.class);
		for (HeroAwakenT t : list) {
			this.heroAwakens.put(t.heroId, t);
		}

		List<HeroBaptizeConfigT> listHbc = ExcelParser.parse(HeroBaptizeConfigT.class);
		for (HeroBaptizeConfigT t : listHbc) {
			this.heroBaptizeConfigs.put(t.baptizeLvl, t);
			if (t.baptizeLvl > maxBaptizeLvl) {
				maxBaptizeLvl = t.baptizeLvl;
			}
		}

		List<HeroBaptizeT> listHB = ExcelParser.parse(HeroBaptizeT.class);
		for (HeroBaptizeT t : listHB) {
			heroBaptizes.put(t.propType, t);
		}

		List<HeroBaptizeAddPropT> listHBAP = ExcelParser.parse(HeroBaptizeAddPropT.class);
		for (HeroBaptizeAddPropT t : listHBAP) {
			if (t.baptizeLvl < 1) {
				continue;
			}
			heroBaptizeAddProps.put(t.baptizeLvl, t);
		}
	}

	/**
	 * 创建武将觉醒控制器
	 * 
	 * @param r
	 * @param db
	 * @return
	 */
	public HeroAwakenController createHeroAwakenController(IRole r, Role db) {
		return new HeroAwakenController(r, db);
	}

	/**
	 * 获得武将觉醒数据
	 * 
	 * @param hero_id
	 * @return
	 */
	public HeroAwakenT getHeroAwakens(int hero_id) {
		return heroAwakens.get(hero_id);
	}

	/**
	 * 获得洗练等级的参数
	 * 
	 * @param baptizeLvl
	 * @return
	 */
	public HeroBaptizeConfigT getHeroBaptizeConfigs(int baptizeLvl) {
		return heroBaptizeConfigs.get(baptizeLvl);
	}

	/**
	 * @return Returns the heroBaptizes.
	 */
	public Map<String, HeroBaptizeT> getHeroBaptizes() {
		return heroBaptizes;
	}

	/**
	 * 获得指定属性的参数
	 * 
	 * @param propType
	 * @return
	 */
	public HeroBaptizeT getHeroBaptizeT(String propType) {
		return heroBaptizes.get(propType);
	}

	/**
	 * @return Returns the heroBaptizeAddProps.
	 */
	/**
	 * 获得指定洗炼等级属性
	 * 
	 * @param baptizeLvl
	 * @return
	 */
	public HeroBaptizeAddPropT getHeroBaptizeAddProps(int baptizeLvl) {
		return heroBaptizeAddProps.get(baptizeLvl);
	}

	/**
	 * @return Returns the heroBaptizeAddProps.
	 */
	public Map<Integer, HeroBaptizeAddPropT> getHeroBaptizeAddProps() {
		return heroBaptizeAddProps;
	}

	/**
	 * @return Returns the maxBaptizeLvl.
	 */
	public int getMaxBaptizeLvl() {
		return maxBaptizeLvl;
	}

	/**
	 * 解析gson格式为对象
	 * 
	 * @param awakenMap
	 * @return
	 */
	public AwakenMap parseAwaken(String awakenMap) {
		return TextUtil.GSON.fromJson(awakenMap, new TypeToken<AwakenMap>() {
		}.getType());
	}

	/**
	 * 计算当前洗炼等级的属性上限
	 * 
	 * @param baptizeLvl 洗炼等级
	 * @param propType 属性类型编号
	 * @return
	 */
	public int calculationPropMaxValue(int baptizeLvl, String propType) {
		HeroBaptizeT hbt = getHeroBaptizeT(propType);
		if (hbt == null) {
			return 0;
		}
		int maxValue = hbt.initMax;
		for (int i = 1; i <= baptizeLvl; i++) {
			maxValue = maxValue * (10000 + hbt.propUpPerLv) / 10000;
		}
		return maxValue;
	}

	/**
	 * 是否洗炼等级满级并且对应洗练等级的属性也满
	 * 
	 * @param hero
	 * @return
	 */
	public boolean isBaptizePropFull(IHero hero) {
		RoleHeroAwaken rha = hero.getRoleHeroAwaken();
		if (rha == null) {
			return false;
		}
		if (rha.getLvl() < XsgHeroAwakenManager.getInstance().getMaxBaptizeLvl()) {
			return false;
		}
		AwakenMap propMaps = parseAwaken(rha.getBaptizeProps());
		for (String propType : heroBaptizes.keySet()) {
			int maxValue = calculationPropMaxValue(rha.getLvl(), propType);
			int curValue = propMaps.get(propType);
			if (maxValue > curValue) {
				return false;
			}
		}
		return true;
	}
}
