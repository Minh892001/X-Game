/**
 * 
 */
package com.morefun.XSanGo.hero.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.BuyHeroResult;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.LimitHero;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 抽卡系统管理类
 * 
 * @author sulingyun
 * 
 */
public class HeroMarketManager {

	private static HeroMarketManager instance = new HeroMarketManager();

	public static HeroMarketManager getInstance() {
		return instance;
	}

	private List<HeroMarketCardT> oneInTenAllList;
	private List<HeroMarketCardT> oneInTenFirstList;
	private List<HeroMarketCardT> oneInTen10FixedList;

	private List<HeroMarketCardT> oneInHundredAllList;
	private List<HeroMarketCardT> oneInHundredFirstList;
	private List<HeroMarketCardT> oneInHundred10FixedList;

	/**
	 * 限时武将设置
	 */
	private LimitHeroConfT limitHeroConfT;
	/**
	 * 聚贤庄每天刷新
	 */
	private Map<Integer, LimitHeroT> dayHeroMap = new HashMap<Integer, LimitHeroT>();

	/**
	 * 聚贤庄每周武将
	 */
	private LimitHeroT weekHeroT;

	/**
	 * 限时武将物品列表
	 */
	private List<LimitHeroItemT> heroItemTs = new ArrayList<LimitHeroItemT>();
	/**
	 * 限时武将概率配置
	 */
	private Map<Integer, LimitHeroWeightT> heroWeightMap = new HashMap<Integer, LimitHeroWeightT>();

	/**
	 * 全局限时武将数据
	 */
	private LimitHero limitHero;

	public enum WeightType {
		WEEK_HERO, WEEK_SOUL, TODAY_HERO, TODAY_SOUL, ITEMS
	}

	private HeroMarketManager() {
		this.oneInTenAllList = ExcelParser.parse("金币抽卡", HeroMarketCardT.class);
		this.oneInTen10FixedList = new ArrayList<HeroMarketCardT>();
		this.oneInTenFirstList = new ArrayList<HeroMarketCardT>();
		for (HeroMarketCardT cardT : this.oneInTenAllList) {
			if (cardT.dropInFirst == 1) {
				this.oneInTenFirstList.add(cardT);
			}
			if (cardT.fixedDropInContinuous == 1) {
				this.oneInTen10FixedList.add(cardT);
			}
		}

		this.oneInHundredAllList = ExcelParser.parse("元宝抽卡", HeroMarketCardT.class);
		this.oneInHundred10FixedList = new ArrayList<HeroMarketCardT>();
		this.oneInHundredFirstList = new ArrayList<HeroMarketCardT>();
		for (HeroMarketCardT cardT : this.oneInHundredAllList) {
			if (cardT.dropInFirst == 1) {
				this.oneInHundredFirstList.add(cardT);
			}
			if (cardT.fixedDropInContinuous == 1) {
				this.oneInHundred10FixedList.add(cardT);
			}
		}
		loadLimitHeroScript();
	}

	/**
	 * 加载限时武将脚本
	 */
	public void loadLimitHeroScript() {
		dayHeroMap.clear();
		heroItemTs.clear();
		heroWeightMap.clear();

		limitHeroConfT = ExcelParser.parse(LimitHeroConfT.class).get(0);
		List<LimitHeroT> list = ExcelParser.parse("每天武将装备阵法", LimitHeroT.class);
		for (LimitHeroT h : list) {
			if (h.type == 0) {
				continue;
			}
			dayHeroMap.put(h.id, h);
		}
		// 周武将
		list = ExcelParser.parse("每周武将装备阵法", LimitHeroT.class);
		for (LimitHeroT h : list) {
			if (h.type == 0) {
				continue;
			}
			weekHeroT = h;
			break;
		}
		if (weekHeroT == null) {
			weekHeroT = list.get(0);
			LogManager.error(new Exception("limit hero week hero script error"));
		}

		heroItemTs = ExcelParser.parse(LimitHeroItemT.class);
		List<LimitHeroWeightT> weightTs = ExcelParser.parse(LimitHeroWeightT.class);
		for (LimitHeroWeightT hw : weightTs) {
			heroWeightMap.put(hw.index, hw);
		}
		SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		List<LimitHero> limitHeros = simpleDao.findAll(LimitHero.class);
		if (limitHeros.isEmpty()) {
			this.limitHero = new LimitHero(GlobalDataManager.getInstance().generatePrimaryKey(), 0, null, null, null);
			simpleDao.save(this.limitHero);
		} else {
			this.limitHero = limitHeros.get(0);
		}
	}

	public IHeroMarketControler createHeroMarketControler(IRole rt, Role db) {
		return new HeroMarketControler(rt, db);
	}

	/**
	 * 十里挑一
	 * 
	 * @param first
	 *            是否首抽
	 * @param charmFull
	 *            魅力值是否满
	 * @return
	 */
	public HeroMarketCardT oneInTen(boolean first, boolean charmFull) {
		return this.randomCardT(first || charmFull ? this.oneInTenFirstList : this.oneInTenAllList);
	}

	private HeroMarketCardT randomCardT(List<HeroMarketCardT> list) {
		int max = 0;
		for (HeroMarketCardT heroMarketCardT : list) {
			max += heroMarketCardT.rate;
		}

		int random = NumberUtil.random(max);
		int temp = 0;
		for (HeroMarketCardT heroMarketCardT : list) {
			temp += heroMarketCardT.rate;
			if (random < temp) {
				return heroMarketCardT;
			}
		}

		return null;
	}

	/**
	 * 随机获取一个限时武将
	 * 
	 * @return
	 */
	public LimitHeroT randomLimitHeroT() {
		List<LimitHeroT> list = new ArrayList<LimitHeroT>();
		int max = 0;
		for (LimitHeroT ht : this.dayHeroMap.values()) {
			list.add(ht);
			max += ht.weight;
		}

		int random = NumberUtil.random(max);
		int temp = 0;
		for (LimitHeroT ht : list) {
			temp += ht.weight;
			if (random < temp) {
				return ht;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			HeroMarketManager.getInstance().oneInHundred10(false);
		}

		System.out.println(System.currentTimeMillis() - begin);
	}

	/**
	 * 十里挑一，十连抽
	 * 
	 * @param charmFull
	 *            是否魅力爆表
	 * 
	 * @return
	 */
	public HeroMarketCardT[] oneInTen10(boolean charmFull) {
		List<HeroMarketCardT> list = new ArrayList<HeroMarketCardT>();
		list.add(this.randomCardT(this.oneInTen10FixedList));
		if (charmFull) {
			list.add(this.randomCardT(this.oneInTenFirstList));
		}
		for (int i = 0; i < (charmFull ? 8 : 9); i++) {
			HeroMarketCardT cardT = this.randomCardT(this.oneInTenAllList);
			while (cardT.distinctInContinuous == 1 && list.contains(cardT)) {
				cardT = this.randomCardT(this.oneInTenAllList);
			}

			list.add(cardT);
		}

		return list.toArray(new HeroMarketCardT[0]);
	}

	public HeroMarketCardT oneInHundred(boolean first, boolean charmFull) {
		return this.randomCardT(first || charmFull ? this.oneInHundredFirstList : this.oneInHundredAllList);
	}

	public HeroMarketCardT[] oneInHundred10(boolean charmFull) {
		List<HeroMarketCardT> list = new ArrayList<HeroMarketCardT>();
		list.add(this.randomCardT(this.oneInHundred10FixedList));
		if (charmFull) {
			list.add(this.randomCardT(this.oneInHundredFirstList));
		}
		for (int i = 0; i < (charmFull ? 8 : 9); i++) {
			HeroMarketCardT cardT = this.randomCardT(this.oneInHundredAllList);
			while (cardT.distinctInContinuous == 1 && list.contains(cardT)) {
				cardT = this.randomCardT(this.oneInHundredAllList);
			}

			list.add(cardT);
		}

		return list.toArray(new HeroMarketCardT[0]);

	}

	/**
	 * 是否得到了武将
	 * 
	 * @param result
	 * @return
	 */
	public boolean isRewardHero(BuyHeroResult result) {
		return result.heroTemplateId > 0 && result.num == 1 && TextUtil.isBlank(result.soulTemplateId);
	}

	/**
	 * 获取限时武将设置模版
	 * 
	 * @return
	 */
	public LimitHeroConfT getLimitHeroConfT() {
		return this.limitHeroConfT;
	}

	/**
	 * 获取全局限时武将信息
	 */
	public LimitHero getLimitHero() {
		return this.limitHero;
	}

	/**
	 * 获取周武将配置
	 * 
	 * @return
	 */
	public LimitHeroT getWeekHeroT() {
		return this.weekHeroT;
	}

	/**
	 * 随机获取对应位置出卡类型
	 * 
	 * @param index
	 * @return
	 */
	public WeightType getRandomType(int index) {
		LimitHeroWeightT weightT = this.heroWeightMap.get(index);
		int max = weightT.itemWeight + weightT.todayHeroWeight + weightT.todaySoulWeight + weightT.weekHeroWeight
				+ weightT.weekSoulWeight;

		int random = NumberUtil.random(max);
		if (random < weightT.weekHeroWeight) {
			return WeightType.WEEK_HERO;
		} else if (random < weightT.weekHeroWeight + weightT.weekSoulWeight) {
			return WeightType.WEEK_SOUL;
		} else if (random < weightT.weekHeroWeight + weightT.weekSoulWeight + weightT.todayHeroWeight) {
			return WeightType.TODAY_HERO;
		} else if (random < weightT.weekHeroWeight + weightT.weekSoulWeight + weightT.todayHeroWeight
				+ weightT.todaySoulWeight) {
			return WeightType.TODAY_SOUL;
		} else {
			return WeightType.ITEMS;
		}
	}

	/**
	 * 随机获取固定位置的物品
	 * 
	 * @param index
	 * @return
	 */
	public LimitHeroItemT getLimitHeroItemTByIndex(int index) {
		List<LimitHeroItemT> itemTs = new ArrayList<LimitHeroItemT>();

		int max = 0;
		for (LimitHeroItemT l : this.heroItemTs) {
			if (l.index == index) {
				itemTs.add(l);
				max += l.weight;
			}
		}

		int random = NumberUtil.random(max);
		int temp = 0;
		for (LimitHeroItemT l : itemTs) {
			temp += l.weight;
			if (random < temp) {
				return l;
			}
		}
		return null;
	}

	/**
	 * 获取展示限时武将模版
	 * 
	 * @param parseInt
	 * @return
	 */
	public LimitHeroT getLimitHeroT(int scriptId) {
		return this.dayHeroMap.get(scriptId);
	}
}
