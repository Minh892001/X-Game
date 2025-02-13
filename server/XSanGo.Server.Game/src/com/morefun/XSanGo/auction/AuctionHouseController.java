package com.morefun.XSanGo.auction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AuctionBuyResView;
import com.XSanGo.Protocol.AuctionHouseItemView;
import com.XSanGo.Protocol.AuctionHouseView;
import com.XSanGo.Protocol.AuctionRoleView;
import com.XSanGo.Protocol.AuctionShopView;
import com.XSanGo.Protocol.AuctionStoreView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.TemplateNotFoundException;
import com.morefun.XSanGo.achieve.XsgAchieveManager.AchieveTemplate;
import com.morefun.XSanGo.auction.XsgAuctionHouseManager.ItemsAndTotal;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.AuctionHouseItem;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleAuctionHouse;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IAuctionHouseBuy;
import com.morefun.XSanGo.event.protocol.IAuctionHouseCancel;
import com.morefun.XSanGo.event.protocol.IAuctionHouseExchange;
import com.morefun.XSanGo.event.protocol.IAuctionHouseSell;
import com.morefun.XSanGo.event.protocol.IAuctionHouseSettle;
import com.morefun.XSanGo.event.protocol.IAuctionMoneyChange;
import com.morefun.XSanGo.event.protocol.IAuctionShopBuy;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.NormalItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.VipT;
import com.morefun.XSanGo.vip.XsgVipManager;

/**
 * 拍卖行controller
 *
 * @author qinguofeng
 * @date Mar 30, 2015
 */
public class AuctionHouseController implements IAuctionHouseController {
	private final static Log logger = LogFactory
			.getLog(AuctionHouseController.class);

	private IRole role;
	private Role roleDB;

	private RoleAuctionHouse myAuction;

	private IAuctionHouseSell auctionHouseSell;
	private IAuctionHouseBuy auctionHouseBuy;
	private IAuctionHouseCancel auctionHouseCancel;
	private IAuctionHouseSettle auctionHouseSettle;
	private IAuctionHouseExchange auctiohHouseExchange;
	private IAuctionMoneyChange moneyChangeEvent;
	private IAuctionShopBuy auctionShopBuyEvent;

	/**
	 * 当前拍卖行商品
	 */
	private List<AuctionShopView> auctionShops = new ArrayList<AuctionShopView>();

	public AuctionHouseController(IRole r, Role db) {
		this.role = r;
		this.roleDB = db;

		// 加载数据库信息
		myAuction = roleDB.getAuctionHouse();
		if (myAuction == null) {
			initRoleAuctionHouse();
		}
		if (!TextUtil.isBlank(myAuction.getShopItemJson())) {
			AuctionShopView[] asv = TextUtil.GSON.fromJson(
					myAuction.getShopItemJson(), AuctionShopView[].class);
			for (AuctionShopView s : asv) {
				auctionShops.add(s);
			}
		}
		IEventControler evtContrl = role.getEventControler();
		auctionHouseSell = evtContrl.registerEvent(IAuctionHouseSell.class);
		auctionHouseBuy = evtContrl.registerEvent(IAuctionHouseBuy.class);
		auctionHouseCancel = evtContrl.registerEvent(IAuctionHouseCancel.class);
		auctionHouseSettle = evtContrl.registerEvent(IAuctionHouseSettle.class);
		auctiohHouseExchange = evtContrl
				.registerEvent(IAuctionHouseExchange.class);
		this.moneyChangeEvent = evtContrl
				.registerEvent(IAuctionMoneyChange.class);
		auctionShopBuyEvent = evtContrl.registerEvent(IAuctionShopBuy.class);
	}

	private void initRoleAuctionHouse() {
		myAuction = new RoleAuctionHouse(roleDB, 0L, Calendar.getInstance()
				.getTime());
		setToRoleDB();
	}

	public void setToRoleDB() {
		roleDB.setAuctionHouse(myAuction);
	}

	@Override
	public AuctionHouseView getAuctionHouseItems(int startIndex, int count,
			int type, String key, int quality, int direction) {
		XsgAuctionHouseManager manager = XsgAuctionHouseManager.getInstance();
		// 获取符合条件的拍卖项目
		ItemsAndTotal<XsgAuctionHouseItem> itemsAndTotal = manager
				.getAuctionItems(startIndex, count, type, key, quality, direction);
		List<XsgAuctionHouseItem> items = itemsAndTotal.items;
		List<AuctionHouseItemView> viewList = new ArrayList<AuctionHouseItemView>();
		if (items != null && items.size() > 0) {
			for (XsgAuctionHouseItem item : items) {
				try {
					viewList.add(makeAuctionHouseItemView(item));
				} catch (TemplateNotFoundException e) {
					LogManager.error(e);
				}
			}
		}
		AuctionHouseView view = new AuctionHouseView(myAuction.getMoney(),
				viewList.toArray(new AuctionHouseItemView[0]),
				itemsAndTotal.totalCount);
		return view;
	}

	/** 合法性检查 */
	private IItem illegalCheck(XsgAuctionHouseManager manager,
			AuctionBaseConfigT config, VipT limit, String roleId, String id,
			int num, long price, long fixedPrice) throws NoteException {
		if (config.level > role.getLevel()) {
			throw new NoteException(Messages.getString("AuctionHouseController.0") + config.level + Messages.getString("AuctionHouseController.1")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (config.vipLevel > role.getVipLevel()) {
			throw new NoteException(Messages.getString("AuctionHouseController.2") + config.vipLevel + Messages.getString("AuctionHouseController.3")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		IItem item = role.getItemControler().getItem(id);
		if (item == null) {
			throw new NoteException(Messages.getString("AuctionHouseController.4")); //$NON-NLS-1$
		}
		// 装备配置表中配置了某件物品能否拍卖
		// if (ItemType.EquipItemType.equals(item.getTemplate().getItemType()))
		// {
		NormalItemT t = (NormalItemT) item.getTemplate();
		// isAuction不为1表示不能拍卖
		item.getTemplate().getId();
		if (t == null || t.canAuction != 1) {
			throw new NoteException(Messages.getString("AuctionHouseController.5")); //$NON-NLS-1$
		}
		// }
		if (num > item.getNum()) {
			throw new NoteException(Messages.getString("AuctionHouseController.6")); //$NON-NLS-1$
		}
		if (price > config.maxPrice) {
			throw new NoteException(Messages.getString("AuctionHouseController.7") + config.maxPrice); //$NON-NLS-1$
		}
		if (price < t.yuanbaoPrice) {
			throw new NoteException(Messages.getString("AuctionHouseController.8") + t.yuanbaoPrice + "!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (fixedPrice > config.maxPrice) {
			throw new NoteException(Messages.getString("AuctionHouseController.10")); //$NON-NLS-1$
		}
		// fixedPrice 为-1表示不设置一口价
		if (fixedPrice != -1
				&& price * (100 + config.fixedPrice) > fixedPrice * 100) {
			throw new NoteException(Messages.getString("AuctionHouseController.11")); //$NON-NLS-1$
		}
		if (manager.getRoleAuctionItemCount(roleId) >= limit.sellNum) {
			throw new NoteException(Messages.getString("AuctionHouseController.12") + role.getVipLevel() + Messages.getString("AuctionHouseController.13") //$NON-NLS-1$ //$NON-NLS-2$
					+ limit.sellNum + Messages.getString("AuctionHouseController.14")); //$NON-NLS-1$
		}
		// 检查物品的合法性, 正在使用中的物品不能拍卖
		// switch (item.getTemplate().getItemType()) {
		// case EquipItemType: // 装备
		// if (!TextUtil.isNullOrEmpty(((EquipItem) item).getRefereneHero())) {
		// throw new NoteException("正在使用中的物品不能拍卖");
		// }
		// break;
		// case FormationBuffItemType: // 阵法
		// if (!TextUtil.isNullOrEmpty(((FormationBuffItem) item)
		// .getRefereneFormationId())) {
		// throw new NoteException("正在使用中的物品不能拍卖");
		// }
		// break;
		// default:
		// break;
		// }
		return item;
	}

	@Override
	public void sell(String id, int num, long price, long fixedPrice)
			throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		logger.debug("begin sell ..."); //$NON-NLS-1$
		if (num <= 0 || price <= 0 || num > Integer.MAX_VALUE
				|| price > Long.MAX_VALUE) {
			throw new NoteException(Messages.getString("AuctionHouseController.16")); //$NON-NLS-1$
		}
		XsgAuctionHouseManager manager = XsgAuctionHouseManager.getInstance();
		AuctionBaseConfigT config = manager.getBaseConfigT();
		// 拍卖行拍卖总量限制检查
		if (manager.getTotalAuctionCount() > config.maxAuctionItemCount) {
			throw new NoteException(Messages.getString("AuctionHouseController.17")); //$NON-NLS-1$
		}
		String roleId = role.getRoleId();
		VipT limit = XsgVipManager.getInstance().findVipT(role.getVipLevel());
		// 检查拍卖参数条件合法性
		IItem item = illegalCheck(manager, config, limit, roleId, id, num,
				price, fixedPrice);
		// if (manager.getRoleAuctionItemCount(roleId) >= limit.sellNum) {
		// throw new NoteException("主公,Vip" + role.getVipLevel() + "最多只能同时拍卖"
		// + limit.sellNum + "件物品,提升vip等级获得更多拍卖次数");
		// }

		// 检查物品的合法性, 正在使用中的物品不能拍卖
		// switch (item.getTemplate().getItemType()) {
		// case EquipItemType: // 装备
		// if (!TextUtil.isNullOrEmpty(((EquipItem) item).getRefereneHero())) {
		// throw new NoteException("正在使用中的物品不能拍卖");
		// }
		// break;
		// case FormationBuffItemType: // 阵法
		// if (!TextUtil.isNullOrEmpty(((FormationBuffItem) item)
		// .getRefereneFormationId())) {
		// throw new NoteException("正在使用中的物品不能拍卖");
		// }
		// break;
		// default:
		// break;
		// }

		// 检查托管费
		if (config.trusteeFee > role.getJinbi()) {
			throw new NoteException(Messages.getString("AuctionHouseController.18")); //$NON-NLS-1$
		}
		// 扣除托管费
		role.reduceCurrency(new Money(CurrencyType.Jinbi, config.trusteeFee));

		// 拷贝RoleItem对象, 保持被拍卖物品的状态
		RoleItem roleItem = item.cloneData();
		// 设置拍卖的个数
		roleItem.setNum(num);

		// 从玩家身上减去对应的物品
		role.getItemControler().changeItemById(id, -num);

		// 计算总的拍卖时间
		long totalTime = config.auctionTime * 60 * 1000L; // 毫秒数
		// 创建拍卖对象
		XsgAuctionHouseItem auctionItem = XsgAuctionHouseItem.build()
				.setID(GlobalDataManager.getInstance().generatePrimaryKey())
				.setBasePrice(price).setBidNum(0).setFixedPrice(fixedPrice)
				.setNum(num).setSellerId(roleId).setItemJson(roleItem)
				.setStartTime(Calendar.getInstance().getTime())
				.setType(item.getTemplate().getItemType().value())
				.setRole(role)
				.setEndTime(System.currentTimeMillis() + totalTime);
		// 持久化玩家数据
		// 加入到拍卖行
		manager.addAuctionItem(auctionItem);
		logger.debug("sell success ..."); //$NON-NLS-1$

		// 增加日志
		XsgAuctionHosueLog.logSell(auctionItem.getId(), roleId, num,
				roleItem.getTemplateCode(), price, roleItem);

		auctionHouseSell.onAuctionHouseSell(item.getTemplate().getId(), num,
				price, fixedPrice);
	}

	/** 增加我更新竞价记录 */
	private void addOrUpdateAuctionRecord(XsgAuctionHouseItem item, long price,
			int count) {
		XsgAuctionHouseManager manager = XsgAuctionHouseManager.getInstance();
		XsgAuctionHouseRecord recordOld = manager.getAuctionRecord(
				role.getRoleId(), item.getId());
		// 旧的竞价记录, 之前对该项目竞过价, 更新记录
		if (recordOld != null) {
			recordOld.setCount(count);
			recordOld.setPrice(price);
			recordOld.setTime(Calendar.getInstance().getTime());
			item.saveToDBAsync();
			return;
		}
		// 第一次对该项目竞价
		String rid = GlobalDataManager.getInstance().generatePrimaryKey();
		XsgAuctionHouseRecord recordNew = new XsgAuctionHouseRecord(rid, item,
				role.getRoleId(), price, count, Calendar.getInstance()
						.getTime());
		// 增加记录
		manager.addAuctionRecord(item, recordNew);
	}

	/**
	 * 获取下次叫价的价格
	 *
	 * @param item
	 *            叫价的物品
	 * @param config
	 *            参数配置
	 * @param fixedPrice
	 *            是否是一口价
	 * */
	private long getNextPrice(XsgAuctionHouseItem item,
			AuctionBaseConfigT config, boolean fixedPrice) {
		if (fixedPrice) { // 如果是一口价, 直接返回一口价价格
			return item.getFixedPrice();
		} else { // 不是一口价, 根据上次竞价和增加的比例计算下次竞价金额
			long lastPrice = item.getCurrentPrice();
			if (lastPrice <= 0) { // 没有人竞过价, 直接返回基础竞价
				return item.getBasePrice();
			}
			long nextPrice = lastPrice
					+ (config.auctionAddition * item.getBasePrice() / 100);
			// 如果出价超过一口价, 按照一口价来减少拍卖币
			if (item.getFixedPrice() > 0
					&& nextPrice > item.getFixedPrice()) {
				nextPrice = item.getFixedPrice();
			}
			return nextPrice;
		}
	}

	@Override
	public AuctionBuyResView buy(String id, int type) throws NoteException {
		XsgAuctionHouseManager manager = XsgAuctionHouseManager.getInstance();
		XsgAuctionHouseItem auctionItem = manager.getAuctionItem(id);
		if (auctionItem == null) {
			throw new NoteException(Messages.getString("AuctionHouseController.20")); //$NON-NLS-1$
		}
		String roleId = role.getRoleId();
		if (auctionItem.getSellerId().equals(roleId)) {
			throw new NoteException(Messages.getString("AuctionHouseController.21")); //$NON-NLS-1$
		}
		// 是否为一口价
		boolean isFixedPrice = type == 2;
		if (isFixedPrice && auctionItem.getFixedPrice() < 0) {
			throw new NoteException(Messages.getString("AuctionHouseController.22")); //$NON-NLS-1$
		}
		// 已经是最高竞价, 仍然允许一口价购买
		String bidderId = auctionItem.getBidderId();
		if (!isFixedPrice && roleId.equals(bidderId)) {
			throw new NoteException(Messages.getString("AuctionHouseController.23")); //$NON-NLS-1$
		}
		VipT limit = XsgVipManager.getInstance().findVipT(role.getVipLevel());
		// 普通的竞价才判断能同时竞拍的数量
		if (!isFixedPrice
				&& manager.getRoleAuctionRecordsCount(roleId) >= limit.buyNum) {
			throw new NoteException(Messages.getString("AuctionHouseController.24") + role.getVipLevel() + Messages.getString("AuctionHouseController.25") //$NON-NLS-1$ //$NON-NLS-2$
					+ limit.buyNum + Messages.getString("AuctionHouseController.26")); //$NON-NLS-1$
		}
		AuctionBaseConfigT config = manager.getBaseConfigT();
		// 竞价应该付的拍卖币
		long nextPrice = getNextPrice(auctionItem, config, isFixedPrice);
		if (nextPrice <= 0 || nextPrice > Long.MAX_VALUE) {
			throw new NoteException(Messages.getString("AuctionHouseController.27")); //$NON-NLS-1$
		}
		if (nextPrice > getAuctionMoney()) {
			throw new NoteException(Messages.getString("AuctionHouseController.28")); //$NON-NLS-1$
		}
		// 如果出价超过一口价, 按照一口价来减少拍卖币
//		if (auctionItem.getFixedPrice() > 0
//				&& nextPrice > auctionItem.getFixedPrice()) {
//			nextPrice = auctionItem.getFixedPrice();
//		}
		// 减少拍卖币
		addAuctionMoney(-nextPrice);

		String auctionItemTemplateId = auctionItem.getTemplateId();
		int auctionItemNum = auctionItem.getNum();
		String auctionItemName = auctionItem.getName();
		String roleName = role.getName();

		// 如果有别人的叫价被超过, 则要发邮件通知他, 并退回拍卖币
		if (!TextUtil.isBlank(bidderId)) {
			// 退回拍卖币
			XsgAuctionHouseManager.mailBuyFailure(bidderId,
					auctionItem.getCurrentPrice(), auctionItemName, roleName,
					bidderId.equals(roleId));
			// 发送日志
			XsgAuctionHosueLog.logBuyFailure(id, bidderId, roleId, nextPrice,
					auctionItemNum, auctionItemTemplateId);
		}

		String auctionItemId = auctionItem.getId();
		String sellerId = auctionItem.getSellerId();

		// 标记物品是否被购买, 当一口价或当竞价超过一口价或者超过最大竞拍金额的时候直接购买
		boolean hasBuy = isFixedPrice;
		// 普通叫价的时候检查出价金额, 超过一口价或者最大价格直接一口价成交
		if (!isFixedPrice) {
			long fixedPrice = auctionItem.getFixedPrice();
			// 设置了一口价, 并且本次叫价已经超过一口价
			if (fixedPrice > 0 && nextPrice >= fixedPrice) {
				// 变为一口价交易
				type = 2;
				hasBuy = true;
			}
			// 出价超过最大价格, 直接一口价成交
			if (nextPrice >= config.maxPrice) {
				type = 2;
				hasBuy = true;
			}
		}

		// 更改竞拍物品状态
		switch (type) {
		case 1: // 普通叫价
			auctionItem.setBidRoleId(roleId);
			auctionItem.setBidder(role);
			auctionItem.setCurrentPrice(nextPrice);
			auctionItem.setBidNum(auctionItem.getBidNum() + 1);
			auctionItem.saveToDBAsync();
			addOrUpdateAuctionRecord(auctionItem, nextPrice,
					auctionItem.getBidNum());
			XsgAuctionHosueLog.logBuy(id, roleId, auctionItemNum,
					auctionItemTemplateId, nextPrice);
			break;
		case 2: // 一口价
			// 发送购买成功的物品邮件
			XsgAuctionHouseManager.mailFixedBuySuccess(roleId, auctionItemName,
					auctionItemTemplateId, auctionItemNum,
					auctionItem.getRoleItem());
			// 发送日志
			XsgAuctionHosueLog.logFixedBuySuccess(auctionItemId, roleId,
					sellerId, auctionItemTemplateId, auctionItemNum, nextPrice);

			// 扣税之后的收入
			long money = manager.getMoneyAfterRate(nextPrice);
			// 发送拍卖成功的收入邮件
			XsgAuctionHouseManager.mailSellSuccess(sellerId, nextPrice, money,
					auctionItemName, roleName);
			// 税收日志
			XsgAuctionHosueLog.logRevenue(id, sellerId, roleId, auctionItemNum,
					auctionItemTemplateId, nextPrice - money);
			// 发送日志
			XsgAuctionHosueLog.logSellSuccess(auctionItemId, sellerId, roleId,
					nextPrice, auctionItemTemplateId, auctionItemNum);

			XsgAuctionHouseManager.getInstance().stopAuction(auctionItem);
			//买了一次
			role.getAchieveControler().updateAchieveProgress(AchieveTemplate.AuctionSuccessNums.name(), 1 + "");
			final String sell = sellerId;
            //加再不在线玩家
			XsgRoleManager.getInstance().loadRoleByIdAsync(sell, new Runnable() {
				@Override
				public void run() {
					IRole temp = XsgRoleManager.getInstance().findRoleById(sell);
					if(null !=temp)
					{
						//卖的人 成功一次
						temp.getAchieveControler().updateAchieveProgress(AchieveTemplate.AuctionNums.name(), 1 + "");
					}
				}
			}, new Runnable() {
				@Override
				public void run() {// 失败什么都不做
				}
			});
			auctionHouseSettle.onAuctionHouseSettle(sellerId, roleId,
					auctionItemTemplateId, auctionItemNum, nextPrice, type, 1);
			break;
		default:
			break;
		}

		auctionHouseBuy.onAuctionHouseBuy(auctionItemTemplateId, type,
				nextPrice);
		// hasBuy 为1表示直接成交了, 为零表示没有成交可以继续竞价
		return new AuctionBuyResView(myAuction.getMoney(), hasBuy ? 1 : 0);
	}

	@Override
	public long getAuctionMoney() {
		return myAuction.getMoney();
	}

	/**
	 * 增加拍卖币
	 *
	 * @param price
	 *            增加数量, 为负数的话表示减少
	 * */
	public void addAuctionMoney(long price) throws NoteException {
		if (myAuction.getMoney() + price < 0) {
			throw new NoteException(Messages.getString("AuctionHouseController.29")); //$NON-NLS-1$
		}
		myAuction.setMoney(myAuction.getMoney() + price);
		// 通知
		this.role.getNotifyControler().onPropertyChange(
				Const.PropertyName.AUCTION_COIN, myAuction.getMoney());
		// 事件
		this.moneyChangeEvent.onAuctionMoneyChange(price);
	}

	@Override
	public long exchange(long money) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		AuctionBaseConfigT config = XsgAuctionHouseManager.getInstance()
				.getBaseConfigT();
		if (money < 0 || money > config.maxExchangeNum) {
			throw new NoteException(Messages.getString("AuctionHouseController.30")); //$NON-NLS-1$
		}
		int yuanbaoPrice = (int) (money / config.exchangeRate);
		if (yuanbaoPrice > role.getTotalYuanbao()) {
			throw new NotEnoughYuanBaoException();
		}
		// 减去元宝
		role.reduceCurrency(new Money(CurrencyType.Yuanbao, yuanbaoPrice));
		// 增加拍卖币
		addAuctionMoney(money);

		// 增加日志
		XsgAuctionHosueLog.logExchange(role.getRoleId(), yuanbaoPrice, money);

		auctiohHouseExchange.onAuctionHouseExchange(yuanbaoPrice, money);
		return myAuction.getMoney();
	}

	@Override
	public AuctionHouseView getMySellItems(int startIndex, int count, int type,
			String key, int quality, int direction) {
		XsgAuctionHouseManager manager = XsgAuctionHouseManager.getInstance();
		// 获取符合条件的项目
		ItemsAndTotal<XsgAuctionHouseItem> itemsAndTotal = manager
				.getRoleAuctionItems(role.getRoleId(), startIndex, count, type,
						key, quality, direction);
		List<XsgAuctionHouseItem> itemList = itemsAndTotal.items;
		List<AuctionHouseItemView> itemViewList = new ArrayList<AuctionHouseItemView>();
		if (itemList != null) {
			for (XsgAuctionHouseItem item : itemList) {
				try {
					itemViewList.add(makeAuctionHouseItemView(item));
				} catch (TemplateNotFoundException e) {
					LogManager.error(e);
				}
			}
		}
		return new AuctionHouseView(myAuction.getMoney(),
				itemViewList.toArray(new AuctionHouseItemView[0]),
				itemsAndTotal.totalCount);
	}

	@Override
	public AuctionHouseView getMyBidItems(int startIndex, int count, int type,
			String key, int quality, int direction) {
		// 获取我的竞拍记录
		ItemsAndTotal<XsgAuctionHouseRecord> itemsAndTotal = XsgAuctionHouseManager
				.getInstance().getAuctionRecords(role.getRoleId(), startIndex,
						count, type, key, quality, direction);
		List<XsgAuctionHouseRecord> records = itemsAndTotal.items;
		List<AuctionHouseItemView> viewList = new ArrayList<AuctionHouseItemView>();
		if (records != null) {
			// 获取竞拍过的项目的ID集合
			List<String> ids = new ArrayList<String>();
			for (XsgAuctionHouseRecord record : records) {
				ids.add(record.getAuctionId());
			}
			// 获取ID集合中的项目
			List<XsgAuctionHouseItem> items = XsgAuctionHouseManager
					.getInstance().getAuctionItems(ids);
			if (items != null) {
				for (XsgAuctionHouseItem item : items) {
					try {
						viewList.add(makeAuctionHouseItemView(item));
					} catch (TemplateNotFoundException e) {
						LogManager.error(e);
					}
				}
			}
		}
		return new AuctionHouseView(myAuction.getMoney(),
				viewList.toArray(new AuctionHouseItemView[0]),
				itemsAndTotal.totalCount);
	}

	@Override
	public void cancelAuction(String id) throws NoteException {
		XsgAuctionHouseManager manager = XsgAuctionHouseManager.getInstance();
		XsgAuctionHouseItem item = manager.getAuctionItem(id);
		if (item == null) {
			throw new NoteException(Messages.getString("AuctionHouseController.31")); //$NON-NLS-1$
		}
		if (!TextUtil.isBlank(item.getBidderId())) {
			throw new NoteException(Messages.getString("AuctionHouseController.32")); //$NON-NLS-1$
		}
		manager.stopAuction(item);
		// 发送下架邮件
		XsgAuctionHouseManager.mailCancelAuction(role.getRoleId(),
				item.getName(), item.getTemplateId(), item.getNum(),
				item.getRoleItem());
		// 发送日志
		XsgAuctionHosueLog.logCancelAuction(id, role.getRoleId(),
				item.getTemplateId(), item.getNum());

		auctionHouseCancel.onAuctionHouseCancel(item.getTemplateId(),
				item.getNum());
	}

	private AuctionHouseItemView makeAuctionHouseItemView(
			XsgAuctionHouseItem xsgItem) throws TemplateNotFoundException {
		IItem item = XsgItemManager.getInstance().createItem(
				xsgItem.getRoleItem());
		AuctionBaseConfigT config = XsgAuctionHouseManager.getInstance()
				.getBaseConfigT();
		AuctionHouseItem itemDB = xsgItem.getDBItem();
		long price = getNextPrice(xsgItem, config, false);
		AuctionHouseItemView view = new AuctionHouseItemView(itemDB.getId(),
				item.getView(), itemDB.getBasePrice(), price,
				itemDB.getFixedPrice(),
				xsgItem.getSellerRoleView() == null ? new AuctionRoleView[0] : new AuctionRoleView[]{xsgItem.getSellerRoleView()},
				xsgItem.getBidderRoleView() == null ? new AuctionRoleView[0] : new AuctionRoleView[]{xsgItem.getBidderRoleView()},
				(itemDB.getEndTime() - System.currentTimeMillis()) / 1000);
//		System.out.println(LuaSerializer.serialize(view));
		return view;
	}

	@Override
	public AuctionStoreView getAuctionShops() throws NoteException {
		if (isRefresh()) {
			doRefreshShopRewards();
			myAuction.setLastRefreshDate(new Date());
			myAuction.setRefreshShopTimes(0);
		}
		int refreshCount = this.roleDB.getAuctionHouse().getRefreshShopTimes();
		Map<Integer, Integer> refreshCoin = XsgAuctionHouseManager
				.getInstance().getRefreshConfigs();
		// 刷新需要的拍卖币，刷新次数用完为负
		int coin = refreshCoin.get(refreshCount + 1) == null ? -1 : refreshCoin
				.get(refreshCount + 1);
		AuctionStoreView view = new AuctionStoreView(refreshCount, coin,
				auctionShops.toArray(new AuctionShopView[0]));
		return view;
	}

	private boolean isRefresh() {
		if (this.roleDB.getAuctionHouse().getLastRefreshDate() == null
				|| auctionShops.isEmpty()) {
			return true;
		}
		AuctionBaseConfigT baseConfigT = XsgAuctionHouseManager.getInstance()
				.getBaseConfigT();
		if (DateUtil.isPass(
				DateUtil.parseDate("HH:mm", baseConfigT.refreshTime), //$NON-NLS-1$
				this.roleDB.getAuctionHouse().getLastRefreshDate())) {
			return true;
		}
		return false;
	}

	@Override
	public AuctionStoreView refreshAuctionShop() throws NoteException {
		int refreshCount = this.roleDB.getAuctionHouse().getRefreshShopTimes();
		Map<Integer, Integer> refreshCoin = XsgAuctionHouseManager
				.getInstance().getRefreshConfigs();
		// 刷新需要的拍卖币，刷新次数用完为负
		int coin = refreshCoin.get(refreshCount + 1) == null ? -1 : refreshCoin
				.get(refreshCount + 1);
		if (coin == -1) {
			throw new NoteException(Messages.getString("AuctionHouseController.34")); //$NON-NLS-1$
		}
		addAuctionMoney(-coin);
		doRefreshShopRewards();
		this.roleDB.getAuctionHouse().setRefreshShopTimes(
				this.roleDB.getAuctionHouse().getRefreshShopTimes() + 1);
		return getAuctionShops();
	}

	@Override
	public long buyAuctionShop(int id) throws NoteException {
		AuctionShopView shopView = null;
		for (AuctionShopView shop : auctionShops) {
			if (shop.id == id && !shop.isBuy) {
				shopView = shop;
				break;
			}
		}
		if (shopView == null) {
			throw new NoteException(Messages.getString("AuctionHouseController.35")); //$NON-NLS-1$
		}
		addAuctionMoney(-shopView.price);
		this.role.getRewardControler().acceptReward(shopView.itemId,
				shopView.num);
		shopView.isBuy = true;
		saveShop();
		auctionShopBuyEvent.onAuctionShopBuy(id);
		return myAuction.getMoney();
	}

	/**
	 * 刷新商城商品
	 * */
	private void doRefreshShopRewards() throws NoteException {
		AuctionBaseConfigT paramSetting = XsgAuctionHouseManager.getInstance()
				.getBaseConfigT();
		int fixedNum = paramSetting.fixedNum;
		int randomNum = paramSetting.randomNum;
		Map<Integer, AuctionShopRewardT> fixedShops = XsgAuctionHouseManager
				.getInstance().getFixedShops();
		Map<Integer, AuctionShopRewardT> radomShops = XsgAuctionHouseManager
				.getInstance().getRandomShops();
		// 合并概率
		List<RandomShopReward> randomRewardList = new ArrayList<RandomShopReward>();
		Map<Integer, RandomShopReward> randomRewardMap = new HashMap<Integer, RandomShopReward>();
		for (AuctionShopRewardT storeT : fixedShops.values()) {
			RandomShopReward r = new RandomShopReward(storeT.id, storeT.weight);
			randomRewardList.add(r);
			randomRewardMap.put(r.id, r);
		}
		auctionShops.clear();
		// 概率计算
		for (int i = 0; i < fixedNum; i++) {
			RandomRange<RandomShopReward> randomRewardGen = new RandomRange<RandomShopReward>(
					randomRewardList);
			RandomShopReward randomReward = randomRewardGen.random();
			AuctionShopRewardT t = fixedShops.get(randomReward.id);
			int num = NumberUtil.random(t.minNum, t.maxNum + 1);
			int price = num * t.price;
			AuctionShopView shopView = new AuctionShopView(t.id, t.itemId,
					t.name, num, price, false);
			auctionShops.add(shopView);
			// 随机出来过的概率设置为0, 使他不会再被随机出来
			randomRewardMap.get(randomReward.id).disappear();
		}

		// 获取随机商品
		randomRewardList = new ArrayList<RandomShopReward>();
		randomRewardMap = new HashMap<Integer, RandomShopReward>();
		for (AuctionShopRewardT storeT : radomShops.values()) {
			RandomShopReward r = new RandomShopReward(storeT.id, storeT.weight);
			randomRewardList.add(r);
			randomRewardMap.put(r.id, r);
		}
		// 概率计算
		for (int i = 0; i < randomNum; i++) {
			RandomRange<RandomShopReward> randomRewardGen = new RandomRange<RandomShopReward>(
					randomRewardList);
			RandomShopReward randomReward = randomRewardGen.random();
			AuctionShopRewardT t = radomShops.get(randomReward.id);
			int num = NumberUtil.random(t.minNum, t.maxNum + 1);
			int price = num * t.price;
			AuctionShopView shopView = new AuctionShopView(t.id, t.itemId,
					t.name, num, price, false);
			auctionShops.add(shopView);
			// 随机出来过的概率设置为0, 使他不会再被随机出来
			randomRewardMap.get(randomReward.id).disappear();
		}
		saveShop();
	}

	/**
	 * 保存商品到数据库
	 */
	private void saveShop() {
		// 序列化到数据库
		this.roleDB.getAuctionHouse().setShopItemJson(
				TextUtil.GSON.toJson(auctionShops
						.toArray(new AuctionShopView[0])));
	}

	private static class RandomShopReward implements IRandomHitable {
		public int id;
		public int rank;

		public RandomShopReward(int id, int rank) {
			this.id = id;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}

		/**
		 * 出现概率设置为0, 使他不会被随机出来
		 * */
		public void disappear() {
			this.rank = 0;
		}
	}
}
