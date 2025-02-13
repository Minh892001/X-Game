package com.morefun.XSanGo.goodsExchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.ExchangeItems;
import com.XSanGo.Protocol.ExchangeResult;
import com.XSanGo.Protocol.ExchangeView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleExchangeItem;
import com.morefun.XSanGo.event.protocol.IGoodsExchange;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint(isTimer = true)
public class ExchangeActivityControler implements IExchangeActivityControler {

	private IRole roleRt;
	private Role roleDB;
	private IGoodsExchange exchangeEvent;
	// 兑换活动显示红点
	private boolean firstOpen = false;

	public ExchangeActivityControler(IRole role, Role db) {
		this.roleRt = role;
		this.roleDB = db;
		this.firstOpen = true;

		this.exchangeEvent = roleRt.getEventControler().registerEvent(IGoodsExchange.class);
	}

	@Override
	public ExchangeView[] getExchangeViews(int itemType) {
		List<ExchangeItemT> items = XsgActivityManage.getInstance().getExchangeItems();
		List<ExchangeView> views = new ArrayList<ExchangeView>();

		if (items == null || items.size() <= 0) {
			return null;
		}

		if (itemType == 99) { // 如果类型是99，则返回默认兑换类型
			return this.getDefaultViews(items);
		}

		for (int i = 0; i < items.size(); i++) {
			ExchangeItemT item = items.get(i);
			if (item.itemType == itemType && itemType != 0) { // 兑换物类型为0的不开放兑换

				List<ExchangeItems> list = new ArrayList<ExchangeItems>();
				for (int j = 0; j < item.itemConfigs.length; j++) { // 兑换所需物品
					if (0 != (item.itemConfigs[j].value) && item.itemConfigs[j].code != null) {// 所需物品数量为零，则略过
						list.add(new ExchangeItems(item.itemConfigs[j].code, item.itemConfigs[j].value,
								item.itemConfigs[j].type));
					}
				}

				ExchangeItems[] itemArr = list.toArray(new ExchangeItems[0]);
				int leftCounts = this.getExchangeCountsLeft(item); // 剩余兑换次数
				// long endTime = item.endDate==null ? 0L :
				// item.endDate.getTime();
				long endTime = 0L;
				Date endDate = null;
				Date startDate = null;
				if (!TextUtil.isBlank(item.endDate)) {
					endDate = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", item.endDate);
					startDate = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", item.startDate);
					endTime = endDate.getTime();
				}
				ExchangeView view = new ExchangeView(item.itemNo, leftCounts, item.itemType, item.itemId, item.num,
						item.itemName, item.colorType, item.annoFlag, item.dealCountsLim, item.hideFlag, item.vipLevel,
						item.roleLevel, itemArr, item.limitTimeFlag, (endTime - new Date().getTime()) / 1000);

				if (item.limitTimeFlag == 0
						|| (item.limitTimeFlag == 1 && DateUtil.isBetween(new Date(), startDate, endDate))) {
					views.add(view);
				}
			}
		}

		return views.toArray(new ExchangeView[0]);
	}

	/**
	 * 获取默认兑换页面
	 * 
	 * @param items
	 * @return
	 */
	private ExchangeView[] getDefaultViews(List<ExchangeItemT> items) {

		List<ExchangeView> views = new ArrayList<ExchangeView>();

		for (int i = 0; i < items.size(); i++) {
			ExchangeItemT item = items.get(i);
			if (item.defaultFlag == 1) {

				List<ExchangeItems> list = new ArrayList<ExchangeItems>();
				for (int j = 0; j < item.itemConfigs.length; j++) {

					if (0 != (item.itemConfigs[j].value) && item.itemConfigs[j].code != null) {// 所需物品数量为零，则略过
						list.add(new ExchangeItems(item.itemConfigs[j].code, item.itemConfigs[j].value,
								item.itemConfigs[j].type));
					}
				}

				ExchangeItems[] itemArr = list.toArray(new ExchangeItems[0]);
				int leftCounts = this.getExchangeCountsLeft(item); // 剩余兑换次数

				long endTime = 0L;
				Date endDate = null;
				Date startDate = null;

				if (!TextUtil.isBlank(item.endDate)) {

					endDate = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", item.endDate);
					startDate = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", item.startDate);
					endTime = endDate.getTime();

				}

				ExchangeView view = new ExchangeView(item.itemNo, leftCounts, item.itemType, item.itemId, item.num,
						item.itemName, item.colorType, item.annoFlag, item.dealCountsLim, item.hideFlag, item.vipLevel,
						item.roleLevel, itemArr, item.limitTimeFlag, (endTime - new Date().getTime()) / 1000);

				if (item.limitTimeFlag == 0
						|| (item.limitTimeFlag == 1 && DateUtil.isBetween(new Date(), startDate, endDate))) {
					views.add(view);
				}

			}
		}
		return views.toArray(new ExchangeView[0]);
	}

	/**
	 * 获取每天兑换记录的剩余次数
	 * 
	 * @param item
	 * @return
	 */
	private int getExchangeCountsLeft(ExchangeItemT item) {

		List<RoleExchangeItem> exchangeItems = this.roleDB.getExchangeItems();

		if (exchangeItems == null || exchangeItems.size() <= 0) {// 之前从未兑换过任何内容
			return item.dealCountsLim;
		}

		RoleExchangeItem roleExchangeItem = this.getItemFromExchangedItemByNo(item.itemNo, exchangeItems);

		// 兑换过的记录中不含本次兑换
		if (roleExchangeItem == null) {
			return item.dealCountsLim;
		}

		// 判断当前时间与上一次兑换时间是否同一天
		if (!DateUtil.isSameDay(new Date(), roleExchangeItem.getLastExchangeTime())) {
			return item.dealCountsLim;
		}

		// 兑换过的记录中包含本次兑换
		return item.dealCountsLim - roleExchangeItem.getExchangeCounts();
	}

	@Override
	public ExchangeResult doExchange(String exchangeNo, int itemType)
			throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException {
		// 1、扣除消耗
		// 2、添加收获

		if (itemType == 0) {
			return new ExchangeResult(0, 0, null);
		}
		ExchangeView view = this.getExchangeViewByNo(exchangeNo, itemType);

		if (view == null) {
			return new ExchangeResult(0, 0, null);
		}

		// 每天兑换次数判断是否已满
		if (this.checkExchangeNum(view, exchangeNo)) {
			return new ExchangeResult(0, 0, null);
		}

		// 主公等级
		if (this.roleDB.getLevel() < view.roleLevel) {
			return new ExchangeResult(0, 0, null);
		}

		// VIP等级
		if (roleRt.getVipLevel() < view.vipLevel) {
			return new ExchangeResult(0, 0, null);
		}

		// 兑换物品数量判断
		if (view.num <= 0) {
			return new ExchangeResult(0, 0, null);
		}

		if (!this.isItemNumEnough(view)) {
			return new ExchangeResult(0, 0, null);
		}

		// this.roleRt.getItemControler().getItemCountInPackage(view.itemId);

		ExchangeResult result = null;

		try {
			result = this.itemUpdate(view);// 金币物品扣除，失败返回0
			this.updateExchangeCounts(view, exchangeNo, result); // 更新兑换次数
		} catch (NoteException e) {
			return new ExchangeResult(0, 0, null);
		}

		// 添加兑换物品
		IItem roleItem = this.roleRt.getRewardControler().acceptReward(view.itemId, view.num);

		result.flag = 1;

		// 是否公告
		if (view.annoFlag == 1) {
			List<ChatAdT> adTList = XsgChatManager.getInstance()
					.getAdContentMap(XsgChatManager.AdContentType.ExchangeAD);
			if (adTList != null && adTList.size() > 0) {

				String itemID = null;

				IItem rItem = roleItem;// roleItems.get(0); //获取兑换物品
				if (view.itemId.equals(rItem.getTemplate().getId())) {
					itemID = rItem.getId();
				}
				// 公告内容：修炼丹本身是紫色的，但是如果很多个，就显示橙色
				if ("xdan".equals(view.itemId) && view.num > 1) {
					view.colorType = 4;
				}

				int type = XsgItemManager.getInstance().findAbsItemT(view.itemId).getItemType().value();
				XsgChatManager chat = XsgChatManager.getInstance();
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("~role_id~", this.roleRt.getRoleId());
				replaceMap.put("~role_name~", this.roleRt.getName());
				replaceMap.put("~role_vip~", this.roleRt.getVipLevel() + "");
				replaceMap.put("~custom_item~", TextUtil.format("{0}|{1}|{2}|{3}|{4}", type, view.itemId, view.itemName,
						view.colorType, itemID));
				XsgChatManager.getInstance().sendAnnouncementItem(rItem, this.roleRt.getChatControler()
						.parseAdConent(chat.replaceRoleContent(adT.content, this.roleRt), replaceMap));
			}
		}

		exchangeEvent.onExchangeItem(view.itemId, exchangeNo, itemType, view.num);

		return result;
	}

	/**
	 * 判断是否有足够的物品兑换
	 * 
	 * @param view
	 * @return
	 */
	private boolean isItemNumEnough(ExchangeView view) {

		ExchangeItems[] items = view.itemConfigs;

		for (ExchangeItems exchangeItems : items) {
			String id = exchangeItems.id;
			if (id.equals(Const.PropertyName.EXP)) {
				if (exchangeItems.num > this.roleRt.getPrestige()) {
					return false;
				}
			} else if (id.equals(Const.PropertyName.MONEY)) {
				if (exchangeItems.num > this.roleRt.getJinbi()) {
					return false;
				}
			} else if (id.equals(Const.PropertyName.RMBY)) {
				if (exchangeItems.num > this.roleRt.getTotalYuanbao()) {
					return false;
				}
			} else if (id.equals(Const.PropertyName.ORDER)) {
				if (exchangeItems.num > this.roleRt.getArenaRankControler().getChallenge()) {
					return false;
				}
			} else if (id.equals(Const.PropertyName.AUCTION_COIN)) {
				if (exchangeItems.num > this.roleRt.getAuctionHouseController().getAuctionMoney()) {
					return false;
				}
			} else {
				int num = this.roleRt.getItemControler().getItemCountInPackage(exchangeItems.id);
				if (num < exchangeItems.num) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 扣除兑换所需物品
	 * 
	 * @param view
	 *            本次兑换信息
	 * @return
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	private ExchangeResult itemUpdate(ExchangeView view)
			throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		ExchangeItems[] items = view.itemConfigs;
		ExchangeItems[] leftItems = new ExchangeItems[items.length];
		ExchangeResult result = new ExchangeResult(0, 0, leftItems);

		for (int i = 0; i < items.length; i++) {
			String id = items[i].id;
			int num = -items[i].num;
			leftItems[i] = new ExchangeItems();
			leftItems[i].id = id;
			leftItems[i].type = items[i].type;

			if (id.equals(Const.PropertyName.EXP)) {
				this.roleRt.winPrestige(num);
				leftItems[i].num = this.roleRt.getPrestige();
			} else if (id.equals(Const.PropertyName.MONEY)) {
				try {
					this.roleRt.winJinbi(num);
					leftItems[i].num = (int) this.roleRt.getJinbi();
				} catch (NotEnoughMoneyException e) {
					LogManager.error(e);
					throw e;
				}
			} else if (id.equals(Const.PropertyName.RMBY)) {
				try {
					this.roleRt.winYuanbao(num, true);
					leftItems[i].num = this.roleRt.getTotalYuanbao();
				} catch (NotEnoughYuanBaoException e) {
					LogManager.error(e);
					throw e;
				}
			} else if (id.equals(Const.PropertyName.ORDER)) {
				this.roleRt.winChallegeMoney(num);
				leftItems[i].num = this.roleDB.getArenaRank().getChallengeMoney();
			} else if (id.equals(Const.PropertyName.AUCTION_COIN)) {
				try {
					this.roleRt.addAuctionCoin(num);
					leftItems[i].num = (int) this.roleDB.getAuctionHouse().getMoney();
				} catch (NoteException e) {
					LogManager.error(e);
					throw e;
				}
			} else {// 物品奖励
				List<IItem> list = this.roleRt.getItemControler().changeItemByTemplateCode(id, num);
				leftItems[i].num = list.get(0).getNum();
			}

		}

		return result;
	}

	/**
	 * 该方法判断兑换次数是否已满
	 * 
	 * @param view
	 *            当前兑换信息
	 * @param exchangeNo
	 *            当前兑换编号
	 * @return
	 */
	private boolean checkExchangeNum(ExchangeView view, String exchangeNo) {

		if (view.dealCountsLim != 0) {
			List<RoleExchangeItem> exchangeItems = this.roleDB.getExchangeItems();
			if (exchangeItems == null || exchangeItems.size() == 0) {// 首次兑换时候
				return false;
			} else {// 非首次兑换

				RoleExchangeItem roleExchangeItem = this.getItemFromExchangedItemByNo(view.itemNo, exchangeItems);

				if (roleExchangeItem != null) {// 本次兑换有历史记录
					// 首先判断当前时间与上次兑换的时间是否在同一天之内
					if (DateUtil.isSameDay(new Date(), roleExchangeItem.getLastExchangeTime())) {// 同一天有兑换
						// 本次兑换已经达到最大兑换次数，则兑换失败
						if (roleExchangeItem.getExchangeCounts() >= view.dealCountsLim) {
							return true;
						} else {// 兑换次数+1,并更新兑换时间
							return false;
						}
					} else {// 若不在同一天兑换，则更新本次已兑换次数及时间
						return false;
					}
				} else {// 本次兑换没有有历史记录
					return false;
				}

			}
		}

		return false;
	}

	/**
	 * 本次兑换次数是否达到最大兑换次数
	 * 
	 * @param view
	 *            本次兑换信息
	 * @param exchangeNo
	 *            本次兑换编号
	 * @param result
	 * @throws NoteException
	 */
	private void updateExchangeCounts(ExchangeView view, String exchangeNo, ExchangeResult result)
			throws NoteException {

		if (view.dealCountsLim != 0) {
			List<RoleExchangeItem> exchangeItems = this.roleDB.getExchangeItems();

			if (exchangeItems == null || exchangeItems.size() == 0) {// 首次兑换时候
				RoleExchangeItem ret = new RoleExchangeItem(GlobalDataManager.getInstance().generatePrimaryKey(),
						this.roleDB, exchangeNo, 1, view.hideFlag == 1, new Date());
				exchangeItems = new ArrayList<RoleExchangeItem>();

				result.leftExchageNum = view.dealCountsLim - 1;
				exchangeItems.add(ret);
			} else {// 非首次兑换

				RoleExchangeItem roleExchangeItem = this.getItemFromExchangedItemByNo(view.itemNo, exchangeItems);

				if (roleExchangeItem != null) {// 本次兑换有历史记录
					// 首先判断当前时间与上次兑换的时间是否在同一天之内
					if (DateUtil.isSameDay(new Date(), roleExchangeItem.getLastExchangeTime())) {// 同一天有兑换
						// 本次兑换已经达到最大兑换次数，则兑换失败
						if (roleExchangeItem.getExchangeCounts() >= view.dealCountsLim) {
							NoteException e = new NoteException(Messages.getString("ShopControler.4"));
							LogManager.error(e);
							throw e;
						} else {// 兑换次数+1,并更新兑换时间
							roleExchangeItem.setLastExchangeTime(new Date());
							roleExchangeItem.setExchangeCounts(roleExchangeItem.getExchangeCounts() + 1);
							result.leftExchageNum = view.dealCountsLim - roleExchangeItem.getExchangeCounts();
						}
					} else {// 若不在同一天兑换，则更新本次已兑换次数及时间
						roleExchangeItem.setLastExchangeTime(new Date());
						roleExchangeItem.setExchangeCounts(1);
						result.leftExchageNum = view.dealCountsLim - roleExchangeItem.getExchangeCounts();
					}
				} else {// 本次兑换没有有历史记录
					RoleExchangeItem ret = new RoleExchangeItem(GlobalDataManager.getInstance().generatePrimaryKey(),
							this.roleDB, exchangeNo, 1, view.hideFlag == 1, new Date());
					result.leftExchageNum = view.dealCountsLim - 1;
					exchangeItems.add(ret);
				}

			}
			this.roleDB.setExchangeItems(exchangeItems);
		}
	}

	/**
	 * 根据兑换编号，获取兑换过的记录信息
	 * 
	 * @param itemNo
	 * @param exchangeItems
	 * @return
	 */
	private RoleExchangeItem getItemFromExchangedItemByNo(String itemNo, List<RoleExchangeItem> exchangeItems) {

		if (itemNo == null || exchangeItems == null || exchangeItems.size() <= 0) {
			return null;
		}

		for (RoleExchangeItem roleExchangeItem : exchangeItems) {

			if (itemNo.equals(roleExchangeItem.getExchangNo())) {
				return roleExchangeItem;
			}

		}

		return null;
	}

	/**
	 * 跟据兑换编号以及类型获取兑换信息
	 * 
	 * @param exchangeNo
	 *            兑换编号
	 * @return
	 */
	private ExchangeView getExchangeViewByNo(String exchangeNo, int itemType) {

		if ("".equals(exchangeNo.trim()) || exchangeNo.trim() == null) {
			return null;
		}
		ExchangeView[] views = this.getExchangeViews(itemType);

		if (views == null || views.length <= 0) {
			return null;
		}

		for (int i = 0; i < views.length; i++) {
			if (views[i] != null && exchangeNo.equals(views[i].itemNo)) {
				return views[i];
			}
		}

		return null;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.ItemExchange)) {
			return null;
		}

		// 聊天回调接口未注册则不发送
		if (roleRt.getChatControler().getChatCb() == null) {
			return null;
		}

		if (firstOpen) {
			firstOpen = false;
			return new MajorUIRedPointNote(MajorMenu.ExchangItemMenu, true);
		}

		return null;
	}

	@Override
	public void setFirstOpen(boolean value) {
		this.firstOpen = value;
	}

}
