package com.morefun.XSanGo.trader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.CommodityView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.DuelReportView;
import com.XSanGo.Protocol.DuelResult;
import com.XSanGo.Protocol.HeroCallResult;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.TraderView;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.battle.DuelBattle;
import com.morefun.XSanGo.battle.DuelReport;
import com.morefun.XSanGo.battle.DuelUnit;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleTrader;
import com.morefun.XSanGo.event.protocol.IAcceptHeroGift;
import com.morefun.XSanGo.event.protocol.IChallengeTraderHero;
import com.morefun.XSanGo.event.protocol.ITraderCall;
import com.morefun.XSanGo.event.protocol.ITraderItemBuy;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class TraderControler implements ITraderControler {

	private static int fixTraderGoodsCount = XsgGameParamManager.getInstance()
			.getTraderFixedGoodsCount();
	private static int randomTraderGoodsCount = XsgGameParamManager
			.getInstance().getTraderRandomGoodsCount();
	// private static int fixHeroTraderGoodsCount = XsgGameParamManager
	// .getInstance().getHeroTraderFixedGoodsCount();
	// private static int randomHeroTraderGoodsCount = XsgGameParamManager
	// .getInstance().getHeroTraderRandomGoodsCount();
	private IRole roleRt;
	private Role roleDb;
	private CommodityView[] itemArray;
	private HeroCallResult heroCallResult;
	private ITraderCall callEvent;
	private ITraderItemBuy itemBuyEvent;
	private IChallengeTraderHero challengeHeroEvent;
	private IAcceptHeroGift heroGiftEvent;

	public TraderControler(IRole rt, Role db) {
		this.roleRt = rt;
		this.roleDb = db;

		this.itemArray = TextUtil.GSON.fromJson(this.getOrCreateRoleTrader()
				.getCallResult(), CommodityView[].class);
		if (this.itemArray == null) {
			this.itemArray = new CommodityView[0];
		}

		this.heroCallResult = TextUtil.GSON.fromJson(this
				.getOrCreateRoleTrader().getHeroCallResult(),
				HeroCallResult.class);
		this.callEvent = this.roleRt.getEventControler().registerEvent(
				ITraderCall.class);
		this.itemBuyEvent = this.roleRt.getEventControler().registerEvent(
				ITraderItemBuy.class);
		this.challengeHeroEvent = this.roleRt.getEventControler()
				.registerEvent(IChallengeTraderHero.class);
		this.heroGiftEvent = this.roleRt.getEventControler().registerEvent(
				IAcceptHeroGift.class);
	}

	@Override
	public TraderView getTraderView() {
		int remainSecond = this.calculateTraderRemainSecond();
		int heroRemainSec = this.calculateHeroRemainSecond();

		return new TraderView(XsgGameParamManager.getInstance()
				.getJinbiTraderCallPrice(), XsgGameParamManager.getInstance()
				.getYuanbaoTraderCallPrice(), remainSecond, this.itemArray,

		XsgGameParamManager.getInstance().getJinbiHeroCallPrice(),
				XsgGameParamManager.getInstance().getYuanbaoHeroCallPrice(),
				heroRemainSec, 
				this.heroCallResult == null ? new HeroCallResult[0] : new HeroCallResult[]{this.heroCallResult});
	}

	/**
	 * 计算名将倒计时，如已到期或从未召唤过，则返回0
	 * 
	 * @return
	 */
	private int calculateHeroRemainSecond() {
		int remainSecond = 0;
		RoleTrader rt = this.getOrCreateRoleTrader();
		if (rt.getLastHeroCallTime() != null) {
			remainSecond = (int) ((rt.getLastHeroCallTime().getTime()
					+ XsgTraderManager.getInstance().getCallIntervalInMillis(
							this.roleRt.getVipLevel()) - System
					.currentTimeMillis()) / 1000);
			remainSecond = Math.max(remainSecond, 0);
		}

		return remainSecond;
	}

	/**
	 * 计算神秘商人倒计时，如已到期或从未召唤过，则返回0
	 * 
	 * @return
	 */
	private int calculateTraderRemainSecond() {
		int remainSecond = 0;
		RoleTrader rt = this.getOrCreateRoleTrader();
		if (rt.getLastCallTime() != null) {
			remainSecond = (int) ((rt.getLastCallTime().getTime()
					+ XsgTraderManager.getInstance().getCallIntervalInMillis(
							this.roleRt.getVipLevel()) - System
					.currentTimeMillis()) / 1000);
			remainSecond = Math.max(remainSecond, 0);
		}

		return remainSecond;
	}

	private RoleTrader getOrCreateRoleTrader() {
		RoleTrader rt = this.roleDb.getTrader();
		if (rt == null) {
			rt = new RoleTrader(this.roleDb);
			this.roleDb.setTrader(rt);
		}

		return rt;
	}

	@Override
	public TraderView callJinbiTrader() throws NotEnoughMoneyException,
			NoteException {
		if (this.calculateTraderRemainSecond() != 0) {
			throw new NoteException(Messages.getString("TraderControler.0")); //$NON-NLS-1$
		}

		// 扣除金币
		this.roleRt.winJinbi(-XsgGameParamManager.getInstance()
				.getJinbiTraderCallPrice());

		// 生成物品
		List<CommodityView> list = this.generateRandomCommodityList(
				TraderType.Trader, CurrencyType.Jinbi);

		this.itemArray = list.toArray(new CommodityView[0]);

		// 更新数据
		RoleTrader rt = this.getOrCreateRoleTrader();
		rt.setLastCallTime(Calendar.getInstance().getTime());
		rt.setJinbiCount(rt.getJinbiCount() + 1);

		this.flushCommodityData();
		this.callEvent.onTraderCalled(TraderType.Trader, CurrencyType.Jinbi);
		return this.getTraderView();
	}

	/**
	 * 刷新货品数据到数据库对象
	 */
	public void flushCommodityData() {
		this.getOrCreateRoleTrader().setCallResult(
				TextUtil.GSON.toJson(this.itemArray));
		this.getOrCreateRoleTrader().setHeroCallResult(
				TextUtil.GSON.toJson(this.heroCallResult));
	}

	@Override
	public void buyItem(String id) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		if (this.calculateTraderRemainSecond() == 0) {
			throw new NoteException(Messages.getString("TraderControler.1")); //$NON-NLS-1$
		}

		CommodityView item = this.findCommodity(this.itemArray, id);
		if (item == null) {
			throw new NoteException(Messages.getString("TraderControler.2")); //$NON-NLS-1$
		}
		if (item.alreadyBuy) {
			throw new NoteException(Messages.getString("TraderControler.3")); //$NON-NLS-1$
		}

		this.roleRt.reduceCurrency(new Money(item.price.type, item.price.num
				* item.num));
		this.roleRt.getItemControler().changeItemByTemplateCode(
				item.templateId, item.num);
		item.alreadyBuy = true;
		this.flushCommodityData();
		this.itemBuyEvent.onItemBuy(item, TraderType.Trader);
	}

	private CommodityView findCommodity(CommodityView[] array, String id) {
		for (CommodityView commodity : array) {
			if (commodity.id.equals(id)) {
				return commodity;
			}
		}

		return null;
	}

	@Override
	public TraderView callYuanbaoTrader() throws NotEnoughYuanBaoException,
			NoteException {
		if (this.calculateTraderRemainSecond() != 0) {
			throw new NoteException(Messages.getString("TraderControler.4")); //$NON-NLS-1$
		}

		try {
			this.roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao,
					XsgGameParamManager.getInstance()
							.getYuanbaoTraderCallPrice()));
		} catch (NotEnoughMoneyException e) {
			LogManager.error(e);
		}

		List<CommodityView> list = this.generateRandomCommodityList(
				TraderType.Trader, CurrencyType.Yuanbao);
		this.itemArray = list.toArray(new CommodityView[0]);
		RoleTrader rt = this.getOrCreateRoleTrader();
		rt.setLastCallTime(Calendar.getInstance().getTime());
		rt.setYuanbaoCount(rt.getYuanbaoCount() + 1);

		this.flushCommodityData();
		this.callEvent.onTraderCalled(TraderType.Trader, CurrencyType.Yuanbao);
		return this.getTraderView();

	}

	@Override
	public TraderView callJinbiHero() throws NotEnoughMoneyException,
			NoteException {
		if (this.calculateHeroRemainSecond() != 0) {
			throw new NoteException(Messages.getString("TraderControler.5")); //$NON-NLS-1$
		}

		this.roleRt.winJinbi(-XsgGameParamManager.getInstance()
				.getJinbiHeroCallPrice());

		TraderHeroT tht = XsgTraderManager.getInstance().randomHeroByJinbi();
		this.heroCallResult = new HeroCallResult(CurrencyType.Jinbi,
				tht.heroId, tht.title, tht.color, tht.star, false,
				XsgTraderManager.getInstance().getMaxHeroChallengeTime(),
				false, new CommodityView[0], false);
		List<CommodityView> list = this.generateRandomCommodityList(
				TraderType.Hero, CurrencyType.Jinbi);

		this.heroCallResult.heroCommodities = list
				.toArray(this.heroCallResult.heroCommodities);
		RoleTrader rt = this.getOrCreateRoleTrader();
		rt.setLastHeroCallTime(Calendar.getInstance().getTime());
		rt.setHeroJinbiCount(rt.getJinbiCount() + 1);

		this.flushCommodityData();

		this.callEvent.onTraderCalled(TraderType.Hero, CurrencyType.Jinbi);
		return this.getTraderView();
	}

	@Override
	public DuelReportView beginChallengeHero(String heroId)
			throws NoteException {
		if (this.calculateHeroRemainSecond() == 0) {
			throw new NoteException(Messages.getString("TraderControler.6")); //$NON-NLS-1$
		}
		if (this.heroCallResult == null) {
			throw new NoteException(Messages.getString("TraderControler.7")); //$NON-NLS-1$
		}
		if (this.heroCallResult.remainChance < 1) {
			throw new NoteException(Messages.getString("TraderControler.8")); //$NON-NLS-1$
		}

		if (this.heroCallResult.challengeSuccess) {
			throw new NoteException(Messages.getString("TraderControler.9")); //$NON-NLS-1$
		}

		IHero selected = this.roleRt.getHeroControler().getHero(heroId);
		if (selected == null) {
			throw new NoteException(Messages.getString("TraderControler.10")); //$NON-NLS-1$
		}
		if (!selected.getTemplate().canDuel()) {
			throw new NoteException(Messages.getString("TraderControler.11")); //$NON-NLS-1$
		}

		// 取玩家武将中战力最强的数据，同时附上召唤名将的技能
		HeroView[] heroArray = this.roleRt.getHeroControler().getHeroViewList();
		Arrays.sort(heroArray, new Comparator<HeroView>() {
			@Override
			public int compare(HeroView o1, HeroView o2) {
				return o1.compositeCombat - o2.compositeCombat;
			}
		});
		IHero npc = this.roleRt.getHeroControler().getHero(
				heroArray[heroArray.length - 1].id);
		DuelUnit unit = npc.createDuelUnit();
		HeroT ht = XsgHeroManager.getInstance().getHeroT(
				this.heroCallResult.heroId);
		unit.setName(XsgHeroManager.getInstance().orgnizeHeroName(
				unit.getQuality(), npc.getBreakLevel(), ht.name));
		unit.setTemplateId(ht.id);
		unit.setColorLevel(this.heroCallResult.colorLevel);
		unit.setStar(this.heroCallResult.star);
		unit.setSkillT(XsgHeroManager.getInstance()
				.findDuelSkillT(ht.duelSkill));
		
		DuelUnit selectedUnit = selected.createDuelUnit();
		if (selected.isAwaken()) {
			selectedUnit.setTemplateId(selected.getTemplate().awaken3DID);
		}
		DuelBattle battle = new DuelBattle(selectedUnit, unit);
		DuelReport dr = battle.fuckEachOther();
		DuelReportView view = dr.generateView();

		// 内测版本战斗过程改由客户端计算
		// this.heroCallResult.challengeSuccess = view.result ==
		// DuelResult.DuelResultWin;
		this.heroCallResult.remainChance--;
		this.flushCommodityData();

		return view;
	}

	@Override
	public void buyHeroItem(String id) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		if (this.calculateHeroRemainSecond() == 0) {
			throw new NoteException(Messages.getString("TraderControler.12")); //$NON-NLS-1$
		}

		CommodityView item = this.findCommodity(
				this.heroCallResult.heroCommodities, id);
		if (item == null || item.alreadyBuy) {
			throw new NoteException(Messages.getString("TraderControler.13")); //$NON-NLS-1$
		}

		this.roleRt.reduceCurrency(new Money(item.price.type, item.price.num
				* item.num));
		this.roleRt.getItemControler().changeItemByTemplateCode(
				item.templateId, item.num);
		item.alreadyBuy = true;
		this.flushCommodityData();

		this.itemBuyEvent.onItemBuy(item, TraderType.Hero);
	}

	@Override
	public TraderView callYuanbaoHero() throws NotEnoughYuanBaoException,
			NoteException {
		if (this.calculateHeroRemainSecond() != 0) {
			throw new NoteException(Messages.getString("TraderControler.14")); //$NON-NLS-1$
		}

		try {
			this.roleRt
					.reduceCurrency(new Money(CurrencyType.Yuanbao,
							XsgGameParamManager.getInstance()
									.getYuanbaoHeroCallPrice()));
		} catch (NotEnoughMoneyException e) {
			LogManager.error(e);
		}

		TraderHeroT tht = XsgTraderManager.getInstance().randomHeroByYuanbao();
		this.heroCallResult = new HeroCallResult(CurrencyType.Yuanbao,
				tht.heroId, tht.title, tht.color, tht.star, false,
				XsgTraderManager.getInstance().getMaxHeroChallengeTime(),
				false, new CommodityView[0], false);
		List<CommodityView> list = this.generateRandomCommodityList(
				TraderType.Hero, CurrencyType.Yuanbao);

		this.heroCallResult.heroCommodities = list
				.toArray(this.heroCallResult.heroCommodities);
		RoleTrader rt = this.getOrCreateRoleTrader();
		rt.setLastHeroCallTime(Calendar.getInstance().getTime());
		rt.setHeroYuanbaoCount(rt.getHeroYuanbaoCount() + 1);

		this.flushCommodityData();

		this.callEvent.onTraderCalled(TraderType.Hero, CurrencyType.Yuanbao);
		return this.getTraderView();

	}

	private List<CommodityView> generateRandomCommodityList(
			TraderType traderType, CurrencyType currencyType) {
		List<CommodityView> list = new ArrayList<CommodityView>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		// 先来固定的
		for (int i = 0; i < fixTraderGoodsCount; i++) {
			list.add(XsgTraderManager.getInstance().fixedCommodity(traderType,
					currencyType));
		}
		// 再来随机的
		int check = 0;
		while (list.size() < fixTraderGoodsCount + randomTraderGoodsCount) {
			check++;
			if (check > 100) {
				break;
			}
			CommodityView commodity = XsgTraderManager.getInstance()
					.randomCommodity(traderType, currencyType);
			if (map.containsKey(commodity.group)) {
				continue;
			}

			if (commodity.group != 0) {
				map.put(commodity.group, 1);
			}
			list.add(commodity);
		}

		return list;
	}

	@Override
	public ItemView[] acceptConsolation() {
		if (this.heroCallResult == null) {
			throw new IllegalStateException(
					Messages.getString("TraderControler.15")); //$NON-NLS-1$
		}
		if (!this.heroCallResult.hasGift) {
			throw new IllegalStateException(
					Messages.getString("TraderControler.16")); //$NON-NLS-1$
		}

		// 获取对应名将
		TraderHeroT tht = XsgTraderManager.getInstance().findTraderHeroT(
				this.heroCallResult.callType, this.heroCallResult.heroId);
		// 执行TC
		TcResult tc = XsgRewardManager.getInstance().doTc(
				this.roleRt,
				this.heroCallResult.challengeSuccess ? tht.successTc
						: tht.failTc);
		this.roleRt.getRewardControler().acceptReward(tc);
		this.heroCallResult.hasGift = false;
		this.flushCommodityData();

		ItemView[] items = XsgRewardManager.getInstance().generateItemView(tc);
		this.heroGiftEvent.onReceiveHeroGift(items);
		return items;
	}

	@Override
	public void endChallenge(DuelResult result) throws NoteException {
		if (this.heroCallResult.challengeEnd) {// 严禁重复挑战，有效结算只能有一次
			throw new NoteException(Messages.getString("TraderControler.17")); //$NON-NLS-1$
		}

		this.heroCallResult.challengeSuccess = result == DuelResult.DuelResultWin;
		if (this.heroCallResult.challengeSuccess
				|| this.heroCallResult.remainChance == 0) {
			this.heroCallResult.hasGift = true;
			this.heroCallResult.challengeEnd = true;
			this.flushCommodityData();
		}

		this.challengeHeroEvent.onChallengeHeroTrader(this.heroCallResult);
	}
}
