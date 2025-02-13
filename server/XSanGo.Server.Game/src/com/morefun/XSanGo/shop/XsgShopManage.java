package com.morefun.XSanGo.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ShopView;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

public class XsgShopManage {
	private static XsgShopManage instance = new XsgShopManage();

	private List<ShopT> shopTs = new ArrayList<ShopT>();

	private List<LevelShopT> levelShopTs = new ArrayList<LevelShopT>();

	private List<ShopT> gemShopTs = new ArrayList<ShopT>();

	public static XsgShopManage getInstance() {
		return instance;
	}

	private XsgShopManage() {
		loadShopScript();
	}

	public void loadShopScript() {
		shopTs = ExcelParser.parse(ShopT.class);
		levelShopTs = ExcelParser.parse(LevelShopT.class);
		gemShopTs = ExcelParser.parse("宝石", ShopT.class);
	}

	/**
	 * 获取商城商品列表 未处理可购买数
	 * 
	 * @return
	 */
	public List<ShopView> getShopView() {
		List<ShopView> shopViews = new ArrayList<ShopView>();
		for (ShopT s : shopTs) {
			if (s.isShow == 0) {// 不显示
				continue;
			}
			ItemType itemType = XsgItemManager.getInstance().findAbsItemT(s.templateId).getItemType();
			ShopView shopView = new ShopView(s.id, itemType, s.templateId, s.num, s.name, s.remark, s.price,
					s.discountPrice, s.startTime, s.endTime, 0, s.buyTimes == 0 ? -1 : s.buyTimes, s.buyVipLevel,
					s.buyLevel, s.tag, s.tips, s.icon, 0, false, s.isUseOut == 1, s.discountIcon, 0);
			shopViews.add(shopView);
		}
		return shopViews;
	}

	/**
	 * 获取商城商品礼包列表 未处理可购买数
	 * 
	 * @return
	 */
	public List<ShopView> getGemShopView() {
		List<ShopView> shopViews = new ArrayList<ShopView>();
		for (ShopT s : gemShopTs) {
			if (s.isShow == 0)
				continue; // 不显示
			ItemType itemType = XsgItemManager.getInstance().findAbsItemT(s.templateId).getItemType();
			ShopView sv = new ShopView(s.id, itemType, s.templateId, s.num, s.name, s.remark, s.price, s.discountPrice,
					s.startTime, s.endTime, 0, s.buyTimes == 0 ? -1 : s.buyTimes, s.buyVipLevel, s.buyLevel, s.tag,
					s.tips, s.icon, 0, false, s.isUseOut == 1, s.discountIcon, 0);
			shopViews.add(sv);
		}
		return shopViews;
	}

	/**
	 * 获取商城商品礼包列表 未处理可购买数
	 * 
	 * @return
	 */
	public List<ShopView> getLevelShopView() {
		List<ShopView> shopViews = new ArrayList<ShopView>();
		for (LevelShopT s : levelShopTs) {
			if (s.isShow == 0) {// 不显示
				continue;
			}
			Date now = new Date();
			// 没到显示时间
			if (TextUtil.isNotBlank(s.showTime) && now.before(DateUtil.parseDate(s.showTime))) {
				continue;
			}
			// 过了消失时间
			if (TextUtil.isNotBlank(s.fadeTime) && now.after(DateUtil.parseDate(s.fadeTime))) {
				continue;
			}
			ItemType itemType = XsgItemManager.getInstance().findAbsItemT(s.templateId).getItemType();
			ShopView shopView = new ShopView(s.id, itemType, s.templateId, s.num, s.name, s.remark, s.price,
					s.discountPrice, s.startTime, s.endTime, 0, s.buyTimes == 0 ? -1 : s.buyTimes, s.buyVipLevel,
					s.buyLevel, s.tag, s.tips, s.icon, 0, false, s.isUseOut == 1, s.discountIcon, 0);
			shopViews.add(shopView);
		}
		return shopViews;
	}

	/**
	 * 获取单个商城商品
	 * 
	 * @param id
	 * @return
	 */
	public ShopT getShopTById(String id) {
		for (ShopT s : shopTs) {
			if (s.id.equals(id)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * 获取单个宝石商品
	 * 
	 * @param id
	 * @return
	 */
	public ShopT getGemShopTById(String id) {
		for (ShopT s : gemShopTs) {
			if (s.id.equals(id)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * 获取单个礼包商品
	 * 
	 * @param id
	 * @return
	 */
	public LevelShopT getLevelShopTById(String id) {
		for (LevelShopT s : levelShopTs) {
			if (s.id.equals(id)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * 判断不同类型的商品购买完是否消失
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	public boolean isUseOut(String id, int type) {
		if (type == 0) {
			for (ShopT s : shopTs) {
				if (s.id.equals(id)) {
					return s.isUseOut == 1;
				}
			}
		} else if (type == ShopControler.isGems) {
			for (ShopT s : gemShopTs) {
				if (s.id.equals(id)) {
					return s.isUseOut == 1;
				}
			}
		} else {
			for (LevelShopT s : levelShopTs) {
				if (s.id.equals(id)) {
					return s.isUseOut == 1;
				}
			}
		}
		return false;
	}

	public IShopControler createShopControler(IRole iRole, Role role) {
		return new ShopControler(iRole, role);
	}
}
