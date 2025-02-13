/**
 * 
 */
package com.morefun.XSanGo.trader;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.XSanGo.Protocol.CommodityView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.ItemType;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;

/**
 * 神秘商人全局管理类
 * 
 * @author sulingyun
 * 
 */
public class XsgTraderManager {
	private static XsgTraderManager instance = new XsgTraderManager();

	public static XsgTraderManager getInstance() {
		return instance;
	}

	/** 金币商人物品范围 */
	private RandomRange<TraderCommodityT> jinbiTraderRange;
	/** 金币固定物品范围 */
	private Collection<TraderCommodityT> jinbiTraderFixed;
	/** 元宝商人物品范围 */
	private RandomRange<TraderCommodityT> yuanbaoTraderRange;
	/** 元宝固定物品范围 */
	private Collection<TraderCommodityT> yuanbaoTraderFixed;
	/** 金币名将物品范围 */
	private RandomRange<TraderCommodityT> jinbiHeroTraderRange;
	/** 金币名将固定物品范围 */
	private Collection<TraderCommodityT> jinbiHeroTraderFixed;
	/** 元宝名将物品范围 */
	private RandomRange<TraderCommodityT> yuanbaoHeroTraderRange;
	/** 元宝名将固定物品范围 */
	private Collection<TraderCommodityT> yuanbaoHeroTraderFixed;
	/** 金币名将范围 */
	private RandomRange<TraderHeroT> jinbiHeroRange;
	/** 元宝名将范围 */
	private RandomRange<TraderHeroT> yuanbaoHeroRange;

	/** 名将最多挑战次数 */
	private int maxHeroChallengeTime = 3;

	private Map<Integer, Integer> cd = new HashMap<Integer, Integer>();

	private XsgTraderManager() {
		IPredicate<TraderCommodityT> fixPredicate = new IPredicate<TraderCommodityT>() {
			@Override
			public boolean check(TraderCommodityT item) {
				return item.rank < 0;
			}
		};
		// 金币商人
		List<TraderCommodityT> list = ExcelParser.parse("金币商人物品",
				TraderCommodityT.class);
		this.jinbiTraderFixed = CollectionUtil.where(list, fixPredicate);
		list.removeAll(this.jinbiTraderFixed);
		this.jinbiTraderRange = new RandomRange<TraderCommodityT>(list);

		// 元宝商人
		list = ExcelParser.parse("元宝商人物品", TraderCommodityT.class);
		this.yuanbaoTraderFixed = CollectionUtil.where(list, fixPredicate);
		list.removeAll(this.yuanbaoTraderFixed);
		this.yuanbaoTraderRange = new RandomRange<TraderCommodityT>(list);

		// 金币名将
		list = ExcelParser.parse("金币探访物品", TraderCommodityT.class);
		this.jinbiHeroTraderFixed = CollectionUtil.where(list, fixPredicate);
		list.removeAll(this.jinbiHeroTraderFixed);
		this.jinbiHeroTraderRange = new RandomRange<TraderCommodityT>(list);

		this.jinbiHeroRange = new RandomRange<TraderHeroT>(ExcelParser.parse(
				"金币名将列表", TraderHeroT.class));

		// 元宝名将
		list = ExcelParser.parse("元宝探访物品", TraderCommodityT.class);
		this.yuanbaoHeroTraderFixed = CollectionUtil.where(list, fixPredicate);
		list.removeAll(this.yuanbaoHeroTraderFixed);
		this.yuanbaoHeroTraderRange = new RandomRange<TraderCommodityT>(list);

		this.yuanbaoHeroRange = new RandomRange<TraderHeroT>(ExcelParser.parse(
				"元宝名将列表", TraderHeroT.class));
		List<TraderCDT> cdts = ExcelParser.parse(TraderCDT.class);
		for (TraderCDT t : cdts) {
			cd.put(t.vipLevel, t.minute);
		}
	}

	public ITraderControler createTraderControler(IRole rt, Role db) {
		return new TraderControler(rt, db);
	}

	public long getCallIntervalInMillis(int vipLevel) {
		Integer minute = this.cd.get(vipLevel);
		if (minute == null) {
			return 3 * 60 * 1000;
		}
		return minute * 60 * 1000;
	}

	private CommodityView generateCommodity(TraderCommodityT tct) {
		ItemType type = XsgItemManager.getInstance()
				.findAbsItemT(tct.itemTemplate).getItemType();
		return new CommodityView(UUID.randomUUID().toString(), type,
				tct.itemTemplate, NumberUtil.random(tct.minCount,
						tct.maxCount + 1), tct.getPrice(), false, tct.group);
	}

	/**
	 * 随机生成商人物品
	 * 
	 * @param traderType
	 *            商人种类，普通商人或者名将
	 * @param currencyType
	 *            货币类型
	 * @return
	 */
	public CommodityView randomCommodity(TraderType traderType,
			CurrencyType currencyType) {
		RandomRange<TraderCommodityT> range = null;
		if (traderType == TraderType.Hero) {
			if (currencyType == CurrencyType.Jinbi) {
				range = this.jinbiHeroTraderRange;
			} else {
				range = this.yuanbaoHeroTraderRange;
			}
		} else {
			if (currencyType == CurrencyType.Jinbi) {
				range = this.jinbiTraderRange;
			} else {
				range = this.yuanbaoTraderRange;
			}
		}

		return this.generateCommodity(range.random());

	}

	public int getMaxHeroChallengeTime() {
		return this.maxHeroChallengeTime;
	}

	public TraderHeroT randomHeroByJinbi() {
		return this.jinbiHeroRange.random();
	}

	public TraderHeroT randomHeroByYuanbao() {
		return this.yuanbaoHeroRange.random();
	}

	/**
	 * 根据召唤类型和武将ID匹配名将模板
	 * 
	 * @param callType
	 * @param heroId
	 * @return
	 */
	public TraderHeroT findTraderHeroT(CurrencyType callType, final int heroId) {
		List<TraderHeroT> heroList = null;
		switch (callType) {
		case Jinbi:
			heroList = this.jinbiHeroRange.getList();
			break;
		case Yuanbao:
			heroList = this.yuanbaoHeroRange.getList();
			break;
		default:
			throw new IllegalArgumentException(callType.toString());
		}

		return CollectionUtil.first(heroList, new IPredicate<TraderHeroT>() {

			@Override
			public boolean check(TraderHeroT item) {
				return item.heroId == heroId;
			}
		});
	}

	public CommodityView fixedCommodity(TraderType traderType,
			CurrencyType currencyType) {
		Collection<TraderCommodityT> range;
		if (traderType == TraderType.Hero) {
			if (currencyType == CurrencyType.Jinbi) {
				range = this.jinbiHeroTraderFixed;
			} else {
				range = this.yuanbaoHeroTraderFixed;
			}
		} else {
			if (currencyType == CurrencyType.Jinbi) {
				range = this.jinbiTraderFixed;
			} else {
				range = this.yuanbaoTraderFixed;
			}
		}

		return range.size() == 0 ? this.randomCommodity(traderType,
				currencyType) : this.generateCommodity(range
				.toArray(new TraderCommodityT[0])[NumberUtil.random(range
				.size())]);
	}
}