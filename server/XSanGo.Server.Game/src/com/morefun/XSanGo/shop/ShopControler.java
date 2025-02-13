package com.morefun.XSanGo.shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ShopView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleShopItem;
import com.morefun.XSanGo.event.protocol.IShopping;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint(isTimer = true)
public class ShopControler implements IShopControler {

	private IRole roleRt;
	private Role roleDb;
	// 商品
	private List<ShopView> shopViews = new ArrayList<ShopView>();
	// 礼包
	private List<ShopView> levelShopViews = new ArrayList<ShopView>();
	// 宝石
	private List<ShopView> gemShopViews = new ArrayList<ShopView>();
	private IShopping iShoppingEvent;
	public static final int isGoods = 0;// 商品
	public static final int isGems = 2; // 宝石

	public ShopControler(IRole rt, Role db) {
		this.roleRt = rt;
		this.roleDb = db;
		this.iShoppingEvent = this.roleRt.getEventControler().registerEvent(IShopping.class);
	}

	private void initShop() {
		this.shopViews = XsgShopManage.getInstance().getShopView();
		this.levelShopViews = XsgShopManage.getInstance().getLevelShopView();
		this.gemShopViews = XsgShopManage.getInstance().getGemShopView();
		// 处理商城物品购买数量
		Iterator<ShopView> it = shopViews.iterator();
		while (it.hasNext()) {
			ShopView s = it.next();
			if (s.maxBuyTimes == -1) {// 购买次数无限
				continue;
			}
			for (RoleShopItem rs : this.roleDb.getShopItems()) {
				if (s.id.equals(rs.getItemId()) && rs.getType() == isGoods) {
					// 购买次数用完永久消失
					if (s.isUseOut && rs.getBuyTimes() >= s.maxBuyTimes) {
						it.remove();
					} else {
						s.buyTimes = rs.getBuyTimes();
					}
					break;
				}
			}
		}
		// 处理宝石可购买次数
		it = gemShopViews.iterator();
		while (it.hasNext()) {
			ShopView s = it.next();
			if (s.maxBuyTimes == -1) {// 购买次数无限
				continue;
			}
			for (RoleShopItem rs : this.roleDb.getShopItems()) {
				if (s.id.equals(rs.getItemId()) && rs.getType() == isGems) {
					// 购买次数用完永久消失
					if (s.isUseOut && rs.getBuyTimes() >= s.maxBuyTimes) {
						it.remove();
					} else {
						s.buyTimes = rs.getBuyTimes();
					}
					break;
				}
			}
		}
		// 处理礼包可购买次数
		it = levelShopViews.iterator();
		while (it.hasNext()) {
			ShopView s = it.next();
			if (s.maxBuyTimes == -1) {// 购买次数无限
				continue;
			}
			for (RoleShopItem rs : this.roleDb.getShopItems()) {
				if (s.id.equals(rs.getItemId()) && rs.getType() != isGoods) {
					// 购买次数用完永久消失
					if (s.isUseOut && rs.getBuyTimes() >= s.maxBuyTimes) {
						it.remove();
					} else {
						s.buyTimes = rs.getBuyTimes();
					}
					break;
				}
			}
		}
		sort();
	}

	@Override
	public ShopView[] getShopView(int type) {
		refreshShop();
		initShop();
		List<ShopView> returnViews = new ArrayList<ShopView>();
		if (type == isGoods) {
			for (ShopView s : shopViews) {
				if (!this.isGoodsShow(s)) {
					continue;
				}
				ShopT shopT = XsgShopManage.getInstance().getShopTById(s.id);
				// 处理倒计时
				if (shopT.isShowCountDown == 1) {
					Date startTime = DateUtil.parseDate(s.startTime);
					// 开售时间还没到
					if (System.currentTimeMillis() < startTime.getTime()) {
						s.remainSecond = 0;
					} else {
						Date endTime = DateUtil.parseDate(s.endTime);
						s.remainSecond = (int) ((endTime.getTime() - System.currentTimeMillis()) / 1000);
					}
				}
				// 处理是否显示折扣价格
				s.isDiscount = isDiscountPrice(s);
				returnViews.add(s);
			}
		} else if (type == isGems) {
			for (ShopView s : gemShopViews) {
				if (!isGoodsShow(s)) {
					continue;
				}
				ShopT shopT = XsgShopManage.getInstance().getGemShopTById(s.id);
				// 处理倒计时
				if (shopT.isShowCountDown == 1) {
					Date startTime = DateUtil.parseDate(s.startTime);
					// 开售时间还没到
					if (System.currentTimeMillis() < startTime.getTime()) {
						s.remainSecond = 0;
					} else {
						Date endTime = DateUtil.parseDate(s.endTime);
						s.remainSecond = (int) ((endTime.getTime() - System.currentTimeMillis()) / 1000);
					}
				}
				// 处理是否显示折扣价格
				s.isDiscount = isDiscountPrice(s);
				returnViews.add(s);
			}
		} else {
			for (ShopView s : levelShopViews) {
				if (!this.isGoodsShow(s)) {
					continue;
				}
				LevelShopT shopT = XsgShopManage.getInstance().getLevelShopTById(s.id);
				// 处理倒计时
				if (shopT.isShowCountDown == 1) {
					if (TextUtil.isNotBlank(s.endTime)) {
						Date endTime = DateUtil.parseDate(s.endTime);
						s.remainSecond = (int) ((endTime.getTime() - System.currentTimeMillis()) / 1000);
					}
					if (TextUtil.isNotBlank(shopT.fadeTime)) {
						Date endTime = DateUtil.parseDate(shopT.fadeTime);
						s.fadeSecond = (int) ((endTime.getTime() - System.currentTimeMillis()) / 1000);
					}
				}
				// 处理是否显示折扣价格
				s.isDiscount = isDiscountPrice(s);
				returnViews.add(s);
			}
		}

		return returnViews.toArray(new ShopView[0]);
	}

	/**
	 * 是否显示商品
	 * 
	 * @param s
	 * @return
	 */
	private boolean isGoodsShow(ShopView s) {
		// 20150312不在折扣期不显示
		return !this.isDiscountGoods(s) || this.isDiscountPrice(s);
	}

	/**
	 * 是否折扣商品
	 * 
	 * @param shopView
	 * @return
	 */
	private boolean isDiscountGoods(ShopView shopView) {
		return shopView.startTime.length() > 0 && shopView.endTime.length() > 0;
	}

	@Override
	public void refreshShop() {
		// 验证是否需要刷新商品数量
		if (this.roleDb.getLastBuyItemDate() == null
				|| !DateUtil.isSameDay(new Date(), this.roleDb.getLastBuyItemDate())) {
			Iterator<RoleShopItem> its = this.roleDb.getShopItems().iterator();
			while (its.hasNext()) {
				RoleShopItem item = its.next();
				if (!item.isUseOut()) {// 非永久消失
					its.remove();
				}
			}
			for (ShopView s : this.shopViews) {
				s.buyTimes = 0;
			}
			for (ShopView s : this.gemShopViews) {
				s.buyTimes = 0;
			}
			for (ShopView s : this.levelShopViews) {
				s.buyTimes = 0;
			}
			this.roleDb.setLastBuyItemDate(new Date());
		}
	}

	@Override
	public void buyItem(int num, String id, int type) throws NotEnoughYuanBaoException, NoteException {
		refreshShop();
		initShop();
		ShopView shopView = null;
		if (type == isGoods) {
			for (ShopView s : shopViews) {
				if (s.id.equals(id)) {
					shopView = s;
					break;
				}
			}
		} else if (type == isGems) {
			for (ShopView s : gemShopViews) {
				if (s.id.equals(id)) {
					shopView = s;
					break;
				}
			}
		} else {
			for (ShopView s : levelShopViews) {
				if (s.id.equals(id)) {
					shopView = s;
					break;
				}
			}
		}
		if (num > 10000) {
			NoteException e = new NoteException(Messages.getString("ShopControler.0"));
			LogManager.error(e);
			throw e;
		}
		if (shopView == null) {
			throw new NoteException(Messages.getString("ShopControler.1"));
		}
		if (this.roleDb.getLevel() < shopView.buyLevel) {
			throw new NoteException(Messages.getString("ShopControler.2"));
		}
		if (this.roleDb.getVip().getVipLevel() < shopView.buyVipLevel) {
			throw new NoteException(Messages.getString("ShopControler.3"));
		}
		if (shopView.buyTimes + num > shopView.maxBuyTimes && shopView.maxBuyTimes != -1) {
			throw new NoteException(Messages.getString("ShopControler.4"));
		}
		int price = 0;
		int deduct = 0;// 扣除元宝数
		// 限时折扣
		if (isDiscountPrice(shopView)) {
			deduct = -shopView.discountPrice * num;
			price = shopView.discountPrice;
		} else {
			deduct = -shopView.price * num;
			price = shopView.price;
		}
		// 不能出现增加元宝的情况
		if (deduct > 0) {
			throw new NoteException(Messages.getString("ShopControler.5"));
		}
		this.roleRt.winYuanbao(deduct, true);
		this.roleRt.getRewardControler().acceptReward(shopView.templateId, shopView.num * num);
		// 保存已购买次数
		if (shopView.maxBuyTimes != -1) {// 不能无限购买
			shopView.buyTimes += num;
			RoleShopItem shopItem = null;
			for (RoleShopItem rs : this.roleDb.getShopItems()) {
				if (shopView.id.equals(rs.getItemId()) && rs.getType() == type) {
					shopItem = rs;
				}
			}
			if (shopItem == null) {
				shopItem = new RoleShopItem(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb,
						shopView.id, shopView.buyTimes, type, XsgShopManage.getInstance().isUseOut(shopView.id, type));
				this.roleDb.getShopItems().add(shopItem);
			} else {
				shopItem.setBuyTimes(shopView.buyTimes);
			}
			// 购买次数用完
			if (shopView.buyTimes >= shopView.maxBuyTimes) {
				// 是否消失
				if (XsgShopManage.getInstance().isUseOut(shopView.id, type)) {
					if (type == isGoods) {
						shopViews.remove(shopView);
					} else if (type == isGems) {
						gemShopViews.remove(shopView);
					} else {
						levelShopViews.remove(shopView);
					}
				}
				sort();
			}
		}
		try {
			iShoppingEvent.onShopping(id, shopView.templateId, num, price, type);
		} catch (Exception e) {
			LogManager.error(e);
		}
	}

	/**
	 * 是否使用折扣价格
	 * 
	 * @param shopView
	 * @return
	 */
	private boolean isDiscountPrice(ShopView shopView) {
		if (this.isDiscountGoods(shopView)) {
			Date now = new Date();
			Date startTime = DateUtil.parseDate(shopView.startTime);
			Date endTime = DateUtil.parseDate(shopView.endTime);
			if (DateUtil.isBetween(now, startTime, endTime)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 处理商品排序
	 * 
	 * @param type
	 */
	private void sort() {
		// 先以ID排序 因为有可能出现购买完第二天刷新后排序错乱的问题
		Collections.sort(shopViews, new Comparator<ShopView>() {

			@Override
			public int compare(ShopView o1, ShopView o2) {
				return Integer.parseInt(o1.id) - Integer.parseInt(o2.id);
			}
		});
		Collections.sort(gemShopViews, new Comparator<ShopView>() {

			@Override
			public int compare(ShopView o1, ShopView o2) {
				return Integer.parseInt(o1.id) - Integer.parseInt(o2.id);
			}
		});
		Collections.sort(levelShopViews, new Comparator<ShopView>() {

			@Override
			public int compare(ShopView o1, ShopView o2) {
				return Integer.parseInt(o1.id) - Integer.parseInt(o2.id);
			}
		});
		List<ShopView> temp = new ArrayList<ShopView>();
		Iterator<ShopView> it = shopViews.iterator();
		while (it.hasNext()) {
			ShopView shopView = it.next();
			// 购买完的商品放到最后
			if (shopView.maxBuyTimes - shopView.buyTimes == 0) {
				it.remove();
				temp.add(shopView);
			}
		}
		shopViews.addAll(temp);
		temp = new ArrayList<ShopView>();
		it = gemShopViews.iterator();
		while (it.hasNext()) {
			ShopView shopView = it.next();
			// 购买完的商品放到最后
			if (shopView.maxBuyTimes - shopView.buyTimes == 0) {
				it.remove();
				temp.add(shopView);
			}
		}
		gemShopViews.addAll(temp);
		temp = new ArrayList<ShopView>();
		it = levelShopViews.iterator();
		while (it.hasNext()) {
			ShopView shopView = it.next();
			// 购买完的商品放到最后
			if (shopView.maxBuyTimes - shopView.buyTimes == 0) {
				it.remove();
				temp.add(shopView);
			}
		}
		levelShopViews.addAll(temp);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		for (ShopView s : levelShopViews) {
			if (s.price == 0 && s.maxBuyTimes - s.buyTimes > 0 && this.roleRt.getLevel() >= s.buyLevel
					&& this.roleRt.getVipLevel() >= s.buyVipLevel) {
				return new MajorUIRedPointNote(MajorMenu.ShopMenu, false);
			}
		}
		return null;
	}

}
