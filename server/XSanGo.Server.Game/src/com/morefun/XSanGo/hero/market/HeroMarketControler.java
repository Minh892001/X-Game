package com.morefun.XSanGo.hero.market;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.XSanGo.Protocol.BuyHeroResult;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.LimitView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.MarketView;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.XSanGo.Protocol.StoreView;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.LimitHero;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleLimitHero;
import com.morefun.XSanGo.db.game.RoleMarket;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.event.protocol.IBuy10WineByJinbi;
import com.morefun.XSanGo.event.protocol.IBuy10WineByYuanbao;
import com.morefun.XSanGo.event.protocol.IBuyLimitHero;
import com.morefun.XSanGo.event.protocol.IBuySingleWineByJinbi;
import com.morefun.XSanGo.event.protocol.IBuySingleWineByYuanbao;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.hero.market.HeroMarketManager.WeightType;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.Collections;

@RedPoint(isTimer = true)
public class HeroMarketControler implements IHeroMarketControler {

	/** 首次金币单抽武将上阵引导 ，上阵后完成引导，避免下野后再次抽到直接上阵 */
	private static final int GuideOneInTenHeroToFormation = 21;
	private IRole roleRt;
	private Role roleDb;
	private IBuySingleWineByJinbi singleJinbiEvent;
	private IBuySingleWineByYuanbao singleYuanbaoEvent;
	private IBuy10WineByJinbi tenJinbiEvent;
	private IBuy10WineByYuanbao tenYuanbaoEvent;
	private IBuyLimitHero buyLimitHeroEvent;

	public HeroMarketControler(IRole rt, Role db) {
		this.roleRt = rt;
		this.roleDb = db;

		this.singleJinbiEvent = this.roleRt.getEventControler().registerEvent(IBuySingleWineByJinbi.class);
		this.singleYuanbaoEvent = this.roleRt.getEventControler().registerEvent(IBuySingleWineByYuanbao.class);
		this.tenJinbiEvent = this.roleRt.getEventControler().registerEvent(IBuy10WineByJinbi.class);
		this.tenYuanbaoEvent = this.roleRt.getEventControler().registerEvent(IBuy10WineByYuanbao.class);
		this.buyLimitHeroEvent = this.roleRt.getEventControler().registerEvent(IBuyLimitHero.class);
		// 初始化限时武将数据
		RoleLimitHero roleLimitHero = db.getRoleLimitHero();
		if (roleLimitHero == null) {
			roleLimitHero = new RoleLimitHero(GlobalDataManager.getInstance().generatePrimaryKey(), db, 0, 0, 0, null);
			db.setRoleLimitHero(roleLimitHero);
		}
	}

	@Override
	public MarketView getView() {
		byte totalTime = 5;
		int interval1 = 10 * 60, interval2 = 23 * 60 * 60;
		Money singlePrice1 = new Money(CurrencyType.Jinbi, XsgGameParamManager.getInstance().getMarketOnInTenPrice());
		Money price10Time1 = new Money(CurrencyType.Jinbi, XsgGameParamManager.getInstance().getMarketOnInTen10Price());
		Money singlePrice2 = new Money(CurrencyType.Yuanbao, XsgGameParamManager.getInstance()
				.getMarketOnInHundredPrice());
		Money price10Time2 = new Money(CurrencyType.Yuanbao, XsgGameParamManager.getInstance()
				.getMarketOnInHundred10Price());

		// 十里挑一先算剩余次数再算CD，百里挑一正好相反
		long now = System.currentTimeMillis();
		RoleMarket rm1 = this.getOrCreateRoleMarket(StoreType.OneInTen);
		byte remainTime = (byte) (totalTime - rm1.getFreeNum());
		int cd = 0;
		if (remainTime > 0) {// 还有免费次数才计算CD
			cd = (int) (rm1.getLastFreeTime() == null ? 0
					: (rm1.getLastFreeTime().getTime() + interval1 * 1000 - now) / 1000);
			if (cd < 0) {
				cd = 0;
			}
		}
		StoreView sv1 = new StoreView(cd, remainTime, totalTime, singlePrice1, price10Time1, rm1.isFirst());

		RoleMarket rm2 = this.getOrCreateRoleMarket(StoreType.OneInHundred);
		cd = (int) (rm2.getLastFreeTime() == null ? 0
				: (rm2.getLastFreeTime().getTime() + interval2 * 1000 - now) / 1000);
		if (cd < 0) {
			cd = 0;
		}
		remainTime = (byte) (cd == 0 ? 1 : 0);

		StoreView sv2 = new StoreView(cd, remainTime, remainTime, singlePrice2, price10Time2, rm2.isFirst());

		return new MarketView(sv1, sv2, getLimitView(), this.roleDb.getMarketCharm(), XsgGameParamManager.getInstance()
				.getMaxMarketCharm(), XsgGameParamManager.getInstance().getMarketWineCode());
	}

	/**
	 * 获取限时武将View
	 * 
	 * @return
	 */
	private LimitView getLimitView() {
		refreshLimitHero();
		LimitHeroConfT conf = HeroMarketManager.getInstance().getLimitHeroConfT();
		LimitHero limitHero = HeroMarketManager.getInstance().getLimitHero();
		LimitHeroT weekHeroT = HeroMarketManager.getInstance().getWeekHeroT();
		List<String> todayScriptIds = TextUtil.stringToList(limitHero.getTodayScriptIds());

		// 检测是否重复或者关闭
		for (String id : todayScriptIds) {
			LimitHeroT limitHeroT = HeroMarketManager.getInstance().getLimitHeroT(Integer.parseInt(id));
			if (limitHeroT == null || limitHeroT.heroId.equals(weekHeroT.heroId)) {
				limitHero.setTodayRefreshDate(null);
				refreshLimitHero();
				break;
			}
		}

		todayScriptIds = TextUtil.stringToList(limitHero.getTodayScriptIds());
		List<IntString> todayHeroIds = new ArrayList<IntString>();
		for (String id : todayScriptIds) {
			LimitHeroT limitHeroT = HeroMarketManager.getInstance().getLimitHeroT(Integer.parseInt(id));
			if (limitHeroT != null) {
				todayHeroIds.add(new IntString(limitHeroT.type, limitHeroT.heroId));
			}
		}
		int freeNum = getFreeNum();
		int vipLevel = freeNum == 0 ? conf.vipLevel : 0;
		int itemVip = freeNum == 0 ? conf.itemVip : 0;
		int price = conf.price;
		if (freeNum > 0 && conf.isFree == 1) {
			price = 0;
		} else {
			freeNum = 0;
		}
		LimitView limitView = new LimitView(new IntString[] { new IntString(weekHeroT.type, weekHeroT.heroId) },
				todayHeroIds.toArray(new IntString[0]), price, vipLevel, freeNum, conf.itemId, conf.itemNum, itemVip);
		return limitView;
	}

	/**
	 * 刷新限时武将
	 */
	private void refreshLimitHero() {
		LimitHeroConfT conf = HeroMarketManager.getInstance().getLimitHeroConfT();
		LimitHero limitHero = HeroMarketManager.getInstance().getLimitHero();
		int count = 0;// 控制循环次数避免死循环
		// 用来控制刷新的物品和上次的不同
		List<String> existHeroId = new ArrayList<String>();
		LimitHeroT limitHeroT = HeroMarketManager.getInstance().getWeekHeroT();
		if (limitHeroT != null) {
			existHeroId.add(limitHeroT.heroId);
		}
		// 今日武将去重
		if (limitHero.getTodayScriptIds() != null) {
			List<String> todayList = TextUtil.stringToList(limitHero.getTodayScriptIds());
			for (String id : todayList) {
				limitHeroT = HeroMarketManager.getInstance().getLimitHeroT(Integer.parseInt(id));
				if (limitHeroT != null) {
					existHeroId.add(limitHeroT.heroId);
				}
			}
		}

		// 刷新今日武将
		if (limitHero.getTodayRefreshDate() == null
				|| DateUtil.isPass(conf.dayTime, "HH:mm:ss", limitHero.getTodayRefreshDate())) {
			List<String> newTodays = new ArrayList<String>();
			count = 0;
			while (newTodays.size() < 3 && count < 10000) {
				count++;
				limitHeroT = HeroMarketManager.getInstance().randomLimitHeroT();
				if (existHeroId.contains(limitHeroT.heroId)) {
					continue;
				}
				newTodays.add(String.valueOf(limitHeroT.id));
				existHeroId.add(limitHeroT.heroId);
			}
			if (newTodays.size() == 3) {
				limitHero.setTodayScriptIds(TextUtil.join(newTodays, ","));
			}
			limitHero.setTodayRefreshDate(new Date());
			updateLimitHero(limitHero);
		}
		RoleLimitHero roleLimitHero = roleDb.getRoleLimitHero();
		// 刷新物品购买次数
		if (roleLimitHero.getLastBuyDate() != null && !DateUtil.isSameDay(roleLimitHero.getLastBuyDate(), new Date())) {
			roleLimitHero.setItemBuyCount(0);
		}
	}

	/**
	 * 更新全局限时武将
	 * 
	 * @param limitHero
	 */
	private void updateLimitHero(final LimitHero limitHero) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				simpleDao.update(limitHero);
			}
		});
	}

	private RoleMarket getOrCreateRoleMarket(StoreType type) {
		int typeValue = type.ordinal();
		if (!this.roleDb.getRoleMarkets().containsKey(typeValue)) {
			this.roleDb.getRoleMarkets().put(typeValue,
					new RoleMarket(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb, typeValue));
		}

		RoleMarket rm = this.roleDb.getRoleMarkets().get(typeValue);
		if (type == StoreType.OneInTen && rm.getLastFreeTime() != null) {
			if (!DateUtil.isSameDay(rm.getLastFreeTime().getTime(), System.currentTimeMillis())) {
				rm.setFreeNum(0);
			}
		}

		return rm;
	}

	@Override
	public BuyHeroResult oneInTen() throws NotEnoughMoneyException, NoteException {
		this.roleRt.getItemControler().checkPackageFull();

		MarketView mv = this.getView();
		boolean free = mv.oneInTenStore.remainTime > 0 && mv.oneInTenStore.cd == 0;

		boolean first = false; // 是否首抽
		// 20150722按策划要求，金币单抽和十连都改为只增不减魅力值
		boolean charmFull = false;// this.isCharmFull();
		BuyHeroResult result = null;

		RoleMarket rm = this.getOrCreateRoleMarket(StoreType.OneInTen);
		if (free) {
			rm.setFreeNum(rm.getFreeNum() + 1);
			rm.setLastFreeTime(Calendar.getInstance().getTime());

			int firstFreeOnInTenHero = XsgRoleManager.getInstance().getInitHeroT().firstFreeOnInTenHero;
			if (rm.getFreeNum() == 1 && this.roleRt.getHeroControler().getHero(firstFreeOnInTenHero) == null
					&& !this.roleRt.isCompleteGuide(GuideOneInTenHeroToFormation)) {// 新手首次免费抽
				result = this.handleNovice(firstFreeOnInTenHero,
						XsgRoleManager.getInstance().getInitHeroT().firstFreeOnInTenHeroPos);
				this.roleRt.completeGuide(GuideOneInTenHeroToFormation);
			}
		} else {// 花钱抽
			try {
				this.roleRt.reduceCurrency(mv.oneInTenStore.singlePrice);
			} catch (NotEnoughYuanBaoException e) {
				// 这里跟元宝没关系
				LogManager.error(e);
			}

			first = !rm.isFirst();
			if (first) {
				rm.setFirst(true);
			}
		}
		if (charmFull) {
			this.resetMarketCharm();
		} else {
			this.addCharm(XsgGameParamManager.getInstance().getMarketCharmByJinbi());
		}

		if (result == null) {
			HeroMarketCardT cardT = HeroMarketManager.getInstance().oneInTen(first, charmFull);
			result = this.rewardCardT(cardT);
		}

		// 赠送美酒
		this.rewardWine(XsgGameParamManager.getInstance().getMarketWinForJinbi());
		this.singleJinbiEvent.onBuyWineByJinbi(result, free);

		return result;
	}

	/**
	 * 赠送美酒
	 * 
	 * @param num
	 *            美酒数量
	 */
	private void rewardWine(int num) {
		String wineCode = XsgGameParamManager.getInstance().getMarketWineCode();
		this.roleRt.getRewardControler().acceptReward(wineCode, num);
	}

	/**
	 * 新手处理
	 * 
	 * @param heroId
	 *            抽出武将ID
	 * @param pos
	 *            默认阵位
	 * @return
	 */
	private BuyHeroResult handleNovice(int heroId, byte pos) {
		HeroMarketCardT hmc = new HeroMarketCardT();
		hmc.templateId = heroId + "";
		BuyHeroResult bhr = this.rewardCardT(hmc, false);
		try {
			this.roleRt.getFormationControler().setFormationPosition(
					this.roleRt.getFormationControler().getDefaultFormation().getId(), pos,
					this.roleRt.getHeroControler().getHero(heroId), true);
		} catch (NoteException e) {
			LogManager.error(e);
		}

		return bhr;
	}

	private void resetMarketCharm() {
		this.roleDb.setMarketCharm(0);
	}

	/**
	 * 增加魅力
	 * 
	 * @param charm
	 * @return 是否爆表
	 */
	private boolean addCharm(int charm) {
		int value = this.roleDb.getMarketCharm() + charm;
		value = Math.min(value, XsgGameParamManager.getInstance().getMaxMarketCharm());
		this.roleDb.setMarketCharm(value);

		return this.isCharmFull();
	}

	private boolean isCharmFull() {
		return this.roleDb.getMarketCharm() >= XsgGameParamManager.getInstance().getMaxMarketCharm();
	}

	/**
	 * 根据抽卡结果结算奖励，并根据抽卡结果决定是否发送公告
	 * 
	 * @param cardT
	 * @return
	 */
	private BuyHeroResult rewardCardT(HeroMarketCardT cardT) {
		return this.rewardCardT(cardT, true);
	}

	/**
	 * 根据抽卡结果结算奖励
	 * 
	 * @param cardT
	 * @param announce
	 *            是否检查发送跑马灯公告
	 * @return
	 */
	private BuyHeroResult rewardCardT(HeroMarketCardT cardT, boolean announce) {
		int heroTemplateId = 0, num = 1;
		String soulTemplateId = "";
		int needSoulCount = 0;
		int heroId = NumberUtil.parseInt(cardT.templateId);
		if (heroId != 0) {// 恭喜你，抽到武将了
			heroTemplateId = heroId;

			IHero hero = this.roleRt.getHeroControler().getHero(heroId);
			if (hero != null) {// 已有武将，杯具了
				soulTemplateId = XsgHeroManager.getInstance().getSoulTemplateId(heroId);
				num = XsgHeroManager.getInstance().caculateSoulCountForCardTransform(hero.getTemplate().color);

				// 这时候需要的魂魄数量为升星数量
				needSoulCount = hero.getStar() >= Const.MaxStar ? 0 : XsgHeroManager.getInstance().findHeroStarT(
						hero.getColor().ordinal(), hero.getStar() + 1).soulCount;
			}
		} else {
			soulTemplateId = cardT.templateId;
			num = NumberUtil.random(cardT.minCount, cardT.maxCount + 1);

			if (XsgItemManager.getInstance().isHeroSoulTemplate(cardT.templateId)) {// 原来这里只有将魂，现在可能有其他道具

				// 这时候需要的魂魄数量为召唤数量
				needSoulCount = XsgHeroManager.getInstance().caculateSoulCountForSummonFromSoulTemplate(soulTemplateId);
			}
		}

		BuyHeroResult result = new BuyHeroResult(heroTemplateId, soulTemplateId, num, 0, needSoulCount,
				this.roleDb.getMarketCharm());
		if (HeroMarketManager.getInstance().isRewardHero(result)) {
			// IHero hero =
			this.roleRt.getHeroControler().addHero(XsgHeroManager.getInstance().getHeroT(result.heroTemplateId),
					HeroSource.Market);
		} else {
			result.totalNum = num + this.roleRt.getItemControler().getItemCountInPackage(soulTemplateId);

			this.roleRt.getItemControler().changeItemByTemplateCode(result.soulTemplateId, result.num);
		}

		if (announce) {
			this.checkAnnounce(result);
		}

		return result;
	}

	/**
	 * 根据抽卡结果结算奖励
	 * 
	 * @param cardT
	 * @param announce
	 *            是否检查发送跑马灯公告
	 * @return
	 */
	private BuyHeroResult rewardCardT(LimitHeroT cardT, boolean announce) {
		int heroTemplateId = 0, num = 1;
		String soulTemplateId = "";
		int needSoulCount = 0;
		int heroId = NumberUtil.parseInt(cardT.heroId);
		if (heroId != 0) {// 恭喜你，抽到武将了
			heroTemplateId = heroId;

			IHero hero = this.roleRt.getHeroControler().getHero(heroId);
			if (hero != null) {// 已有武将，杯具了
				soulTemplateId = XsgHeroManager.getInstance().getSoulTemplateId(heroId);
				num = XsgHeroManager.getInstance().caculateSoulCountForCardTransform(hero.getTemplate().color);

				// 这时候需要的魂魄数量为升星数量
				needSoulCount = hero.getStar() >= Const.MaxStar ? 0 : XsgHeroManager.getInstance().findHeroStarT(
						hero.getColor().ordinal(), hero.getStar() + 1).soulCount;
			}
		} else {// 获得装备
			soulTemplateId = cardT.heroId;
			num = 1;
		}
		BuyHeroResult result = new BuyHeroResult(heroTemplateId, soulTemplateId, num, 0, needSoulCount,
				this.roleDb.getMarketCharm());
		if (HeroMarketManager.getInstance().isRewardHero(result)) {
			// IHero hero =
			this.roleRt.getHeroControler().addHero(XsgHeroManager.getInstance().getHeroT(result.heroTemplateId),
					HeroSource.Market);
		} else {
			result.totalNum = num + this.roleRt.getItemControler().getItemCountInPackage(soulTemplateId);

			this.roleRt.getItemControler().changeItemByTemplateCode(result.soulTemplateId, result.num);
		}

		if (announce) {
			this.limitHeroAnnounce(result);
		}

		return result;
	}

	/**
	 * 根据抽卡结果决定是否发送全服公告
	 */
	private void checkAnnounce(BuyHeroResult result) {
		// 得到紫色武将 或 装备才发
		if (HeroMarketManager.getInstance().isRewardHero(result)) {
			HeroT heroT = XsgHeroManager.getInstance().getHeroT(result.heroTemplateId);
			if (heroT.color > QualityColor.Blue.ordinal()) {
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
						XsgChatManager.AdContentType.VioletHeroFromMarket);
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				XsgChatManager.getInstance().sendAnnouncement(
						this.roleRt.getChatControler().parseAdContentHeroT(heroT, adT.content));

			}
		} else {
			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(result.soulTemplateId);
			if (itemT != null && itemT.getColor().ordinal() > QualityColor.Blue.ordinal()
					&& itemT.getItemType() == ItemType.EquipItemType) {
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
						XsgChatManager.AdContentType.equipPurple);
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				XsgChatManager.getInstance().sendAnnouncement(
						this.roleRt.getChatControler().parseAdContentItem(itemT, adT.content));
			}
		}
	}

	/**
	 * 限时武将全服公告
	 */
	private void limitHeroAnnounce(BuyHeroResult result) {
		// 抽到重复的武将也要发公告
		if (result.heroTemplateId > 0) {
			HeroT heroT = XsgHeroManager.getInstance().getHeroT(result.heroTemplateId);
			List<ChatAdT> adTList = XsgChatManager.getInstance()
					.getAdContentMap(XsgChatManager.AdContentType.limitHero);
			ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
			XsgChatManager.getInstance().sendAnnouncement(
					this.roleRt.getChatControler().parseAdContentHeroT(heroT, adT.content));
		} else {// 装备和阵法
			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(result.soulTemplateId);
			if (itemT != null) {
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
						XsgChatManager.AdContentType.limitEquip);
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				XsgChatManager.getInstance().sendAnnouncement(
						this.roleRt.getChatControler().parseAdContentItem(itemT, adT.content));
			}
		}
	}

	@Override
	public BuyHeroResult[] oneInTen10() throws NotEnoughMoneyException, NoteException {
		this.roleRt.getItemControler().checkPackageFull();

		MarketView mv = this.getView();

		try {
			this.roleRt.reduceCurrency(mv.oneInTenStore.price10Time);
		} catch (NotEnoughYuanBaoException e) {
			// 这里跟元宝没关系
			LogManager.error(e);
		}

		// 20150722按策划要求，金币单抽和十连都改为只增不减魅力值
		boolean charmFull = false;// this.isCharmFull();
		HeroMarketCardT[] cardTArray = HeroMarketManager.getInstance().oneInTen10(charmFull);
		List<BuyHeroResult> list = new ArrayList<BuyHeroResult>();
		for (HeroMarketCardT cardT : cardTArray) {
			list.add(this.rewardCardT(cardT));
		}
		// 赠送美酒
		this.rewardWine(XsgGameParamManager.getInstance().getMarketWinForJinbi10());

		if (charmFull) {
			this.resetMarketCharm();
		} else {
			this.addCharm(10 * XsgGameParamManager.getInstance().getMarketCharmByJinbi());
		}

		this.tenJinbiEvent.onBuy10WineByJinbi(list);

		return list.toArray(new BuyHeroResult[0]);
	}

	@Override
	public BuyHeroResult oneInHundred() throws NotEnoughYuanBaoException, NoteException {
		this.roleRt.getItemControler().checkPackageFull();

		MarketView mv = this.getView();
		boolean free = mv.oneInHundredStore.remainTime > 0;

		boolean first = false; // 是否首抽
		boolean charmFull = this.isCharmFull(); // 是否魅力爆表
		BuyHeroResult result = null;

		RoleMarket rm = this.getOrCreateRoleMarket(StoreType.OneInHundred);
		if (free) {
			rm.setFreeNum(rm.getFreeNum() + 1);
			rm.setLastFreeTime(Calendar.getInstance().getTime());

			if (rm.getFreeNum() == 1) {// 新手首次免费抽
				result = this.handleNovice(XsgRoleManager.getInstance().getInitHeroT().firstFreeOnInHundredHero,
						XsgRoleManager.getInstance().getInitHeroT().firstFreeOnInHundredHeroPos);
			}
		} else {// 花钱抽
			try {
				this.roleRt.reduceCurrency(mv.oneInHundredStore.singlePrice);
			} catch (NotEnoughMoneyException e) {
				// 这里跟金币没关系
				LogManager.error(e);
			}

			first = !rm.isFirst();
			if (first) {
				rm.setFirst(true);
			}
		}
		if (charmFull) {
			this.resetMarketCharm();
		} else {
			this.addCharm(XsgGameParamManager.getInstance().getMarketCharmByYuanbao());
		}

		if (result == null) {
			HeroMarketCardT cardT = HeroMarketManager.getInstance().oneInHundred(first, charmFull);
			result = this.rewardCardT(cardT);
		}

		// 赠送美酒
		this.rewardWine(XsgGameParamManager.getInstance().getMarketWinForYuanbao());
		this.singleYuanbaoEvent.onBuyWineByYuanbao(result, free);

		return result;
	}

	@Override
	public BuyHeroResult[] oneInHundred10() throws NotEnoughYuanBaoException, NoteException {
		this.roleRt.getItemControler().checkPackageFull();

		MarketView mv = this.getView();

		try {
			this.roleRt.reduceCurrency(mv.oneInHundredStore.price10Time);
		} catch (NotEnoughMoneyException e) {
			// 这里跟元宝没关系
			LogManager.error(e);
		}

		boolean charmFull = this.isCharmFull(); // 是否魅力爆表
		HeroMarketCardT[] cardTArray = HeroMarketManager.getInstance().oneInHundred10(charmFull);
		List<BuyHeroResult> list = new ArrayList<BuyHeroResult>();
		for (HeroMarketCardT cardT : cardTArray) {
			list.add(this.rewardCardT(cardT));
		}
		// 赠送美酒
		this.rewardWine(XsgGameParamManager.getInstance().getMarketWinForYuanbao10());

		if (charmFull) {
			this.resetMarketCharm();
		} else {
			this.addCharm(10 * XsgGameParamManager.getInstance().getMarketCharmByYuanbao());
		}

		this.tenYuanbaoEvent.onBuy10WineByYuanbao(list);

		return list.toArray(new BuyHeroResult[0]);

	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		MarketView mv = this.getView();
		boolean note = (mv.oneInTenStore.remainTime > 0 && mv.oneInTenStore.cd == 0)
				|| mv.oneInHundredStore.remainTime > 0;

		return note ? new MajorUIRedPointNote(MajorMenu.HeroCardMarketMenu, false) : null;
	}

	@Override
	public BuyHeroResult[] buyLimitHero(int type) throws NotEnoughYuanBaoException, NoteException {
		this.roleRt.getItemControler().checkPackageFull();
		LimitHeroConfT conf = HeroMarketManager.getInstance().getLimitHeroConfT();
		int freeNum = getFreeNum();
		int needVip = freeNum == 0 ? conf.vipLevel : 0;
		if (type == 1) {// 物品购买
			needVip = freeNum == 0 ? conf.itemVip : 0;
		}
		if (roleRt.getVipLevel() < needVip) {
			throw new NoteException(TextUtil.format(Messages.getString("HeroMarketControler.needVip"), needVip));
		}
		refreshLimitHero();
		if (freeNum <= 0 || conf.isFree == 0) {// 没有免费次数或者不免费
			if (type == 0) {// 元宝购买
				try {
					this.roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, conf.price));
				} catch (NotEnoughMoneyException e) {
					LogManager.error(e);
				}
			} else {
				if (roleRt.getItemControler().getItemCountInPackage(conf.itemId) < conf.itemNum) {
					throw new NoteException(Messages.getString("HeroMarketControler.itemLack"));
				}
				if (conf.itemMaxBuy != -1 && roleDb.getRoleLimitHero().getItemBuyCount() >= conf.itemMaxBuy) {
					throw new NoteException(Messages.getString("HeroMarketControler.itemBuyOverrun"));
				}
				roleRt.getItemControler().changeItemByTemplateCode(conf.itemId, -conf.itemNum);
			}
		}
		// 赠送美酒
		this.rewardWine(XsgGameParamManager.getInstance().getBuyLimitHeroWine());
		this.addCharm(XsgGameParamManager.getInstance().getBuyLimitHeroCharm());
		// 运营活动
		if (DateUtil.isBetween(conf.startDate, conf.endDate)) {
			MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
					.get(MailTemplate.LimitHeroActivity.value());

			String[] itemArr = conf.items.split(";");
			Property[] pros = new Property[itemArr.length];
			for (int i = 0; i < pros.length; i++) {
				String[] idNum = itemArr[i].split(",");
				pros[i] = new Property(idNum[0], Integer.parseInt(idNum[1]));
			}

			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName, roleRt
							.getRoleId(), mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance()
							.serializeMailAttach(pros), Calendar.getInstance().getTime()));
		}
		RoleLimitHero roleLimitHero = roleDb.getRoleLimitHero();
		roleLimitHero.setBuyCount(roleLimitHero.getBuyCount() + 1);
		roleLimitHero.setBuyYuanbao(roleLimitHero.getBuyYuanbao() + conf.price);

		roleLimitHero.setLastBuyDate(new Date());
		roleLimitHero.setTodayBuyCount(roleLimitHero.getTodayBuyCount() + 1);
		if (type == 1) {// 物品购买
			roleLimitHero.setItemBuyCount(roleLimitHero.getItemBuyCount() + 1);
		}

		List<BuyHeroResult> results = getLimitHeroResult();
		buyLimitHeroEvent.onBuyLimitHero(results);
		return results.toArray(new BuyHeroResult[0]);
	}

	/**
	 * 获取限时武将购买结果
	 * 
	 * @return
	 */
	private List<BuyHeroResult> getLimitHeroResult() {
		LimitHero limitHero = HeroMarketManager.getInstance().getLimitHero();
		List<BuyHeroResult> result = new ArrayList<BuyHeroResult>();

		LimitHeroT weekHeroT = HeroMarketManager.getInstance().getWeekHeroT();
		// 伪随机结果
		WeightType randomType = random(weekHeroT);
		// 随机一个今日武将
		List<String> todayIds = TextUtil.stringToList(limitHero.getTodayScriptIds());
		int id = Integer.parseInt(todayIds.get(NumberUtil.random(todayIds.size())));
		LimitHeroT todayT = HeroMarketManager.getInstance().getLimitHeroT(id);

		for (int i = 1; i <= 10; i++) {
			WeightType type = HeroMarketManager.getInstance().getRandomType(i);
			// 固定第二个位置出伪随机的整卡
			if (i == 2 && randomType != null) {
				type = randomType;
			}
			BuyHeroResult re = null;
			if (type == WeightType.WEEK_HERO) {
				re = rewardCardT(weekHeroT, true);
				clearBuyLimitHeroLog();
			} else if (type == WeightType.TODAY_HERO) {
				re = rewardCardT(todayT, true);
				clearBuyLimitHeroLog();
			} else if (type == WeightType.WEEK_SOUL || type == WeightType.TODAY_SOUL) {// 如果是将魂或碎片
				LimitHeroT cardT = null;
				if (type == WeightType.WEEK_SOUL) {
					cardT = weekHeroT;
				} else {
					cardT = todayT;
				}
				int needSoulCount = 0;
				String soulTemplateId = cardT.soulId;
				if (XsgItemManager.getInstance().isHeroSoulTemplate(cardT.soulId)) {

					// 这时候需要的魂魄数量为召唤数量
					needSoulCount = XsgHeroManager.getInstance().caculateSoulCountForSummonFromSoulTemplate(
							cardT.soulId);
				}
				int num = NumberUtil.random(cardT.minSoul, cardT.maxSoul + 1);
				re = new BuyHeroResult(0, soulTemplateId, num, 0, needSoulCount, this.roleDb.getMarketCharm());
				this.roleRt.getItemControler().changeItemByTemplateCode(re.soulTemplateId, re.num);
			} else if (type == WeightType.ITEMS) {// 特殊物品
				LimitHeroItemT itemT = HeroMarketManager.getInstance().getLimitHeroItemTByIndex(i);
				int num = NumberUtil.random(itemT.minNum, itemT.maxNum + 1);
				re = new BuyHeroResult(0, itemT.itemId, num, 0, 0, this.roleDb.getMarketCharm());
				this.roleRt.getItemControler().changeItemByTemplateCode(re.soulTemplateId, re.num);
			}
			result.add(re);
		}
		Collections.shuffle(result);
		return result;
	}

	/**
	 * 伪随机
	 * 
	 * @param limitHeroT
	 * @return
	 */
	public WeightType random(LimitHeroT limitHeroT) {
		LimitHeroConfT conf = HeroMarketManager.getInstance().getLimitHeroConfT();
		if (limitHeroT.type == 1) {// 武将
			int buyCount = roleDb.getRoleLimitHero().getBuyCount();
			if (buyCount >= conf.heroMaxCount ||
			// 从这个累积数量开始伪随机，每次伪随机概率是1/(Max-Min)
					(buyCount >= conf.heroMinCount && NumberUtil.random(conf.heroMaxCount - conf.heroMinCount) < 1)) {
				// 从周武将里面出整卡
				if (NumberUtil.random(conf.weekWeight + conf.todayWeight) < conf.weekWeight) {
					return WeightType.WEEK_HERO;
				} else {
					return WeightType.TODAY_HERO;
				}
			} else {
				return null;
			}
		} else {// 装备
			int sumYuanbao = roleDb.getRoleLimitHero().getBuyYuanbao();
			if (sumYuanbao >= conf.equipMaxYuanbao ||
			// 从这个累积数量开始伪随机，每次伪随机概率是Min/Max
					(sumYuanbao >= conf.equipMinYuanbao && NumberUtil.random(conf.equipMaxYuanbao) < conf.equipMinYuanbao)) {
				// 从周装备里面出整装
				if (NumberUtil.random(conf.weekWeight + conf.todayWeight) < conf.weekWeight) {
					return WeightType.WEEK_HERO;
				} else {
					return WeightType.TODAY_HERO;
				}
			} else {
				return null;
			}
		}
	}

	/**
	 * 清除限时武将伪随机数据
	 */
	private void clearBuyLimitHeroLog() {
		roleDb.getRoleLimitHero().setBuyCount(0);
		roleDb.getRoleLimitHero().setBuyYuanbao(0);
	}

	/**
	 * 获取限时武将免费和无vip限制次数
	 * 
	 * @return
	 */
	private int getFreeNum() {
		LimitHeroConfT confT = HeroMarketManager.getInstance().getLimitHeroConfT();
		RoleLimitHero roleLimitHero = roleDb.getRoleLimitHero();
		if (roleLimitHero.getLastBuyDate() == null || !DateUtil.isSameDay(new Date(), roleLimitHero.getLastBuyDate())) {
			roleLimitHero.setTodayBuyCount(0);
		}
		if (DateUtil.isBetween(confT.vipStartDate, confT.vipEndDate)
				&& roleLimitHero.getTodayBuyCount() < confT.notVipNum) {
			return confT.notVipNum - roleLimitHero.getTodayBuyCount();
		}
		return 0;
	}
}
