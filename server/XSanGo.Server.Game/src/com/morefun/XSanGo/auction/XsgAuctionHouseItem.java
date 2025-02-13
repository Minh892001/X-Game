package com.morefun.XSanGo.auction;

import java.util.Date;

import com.XSanGo.Protocol.AuctionRoleView;
import com.morefun.XSanGo.AsynSaver;
import com.morefun.XSanGo.IAsynSavable;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.AuctionHouseItem;
import com.morefun.XSanGo.db.game.AuctionHouseRecord;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.item.NormalItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 拍卖项目
 * 
 * @author qinguofeng
 * @date Mar 31, 2015
 */
public class XsgAuctionHouseItem implements IAsynSavable {

	private AuctionHouseItem itemDB;

	// private IItem item;
	private AuctionRoleView sellerRoleView;
	private AuctionRoleView bidderRoleView;
	private RoleItem roleItem;

	private AsynSaver saver;

	private XsgAuctionHouseItem() {
		saver = new AsynSaver(this);
	}

	/** 会有数据库操作 */
	public XsgAuctionHouseItem(AuctionHouseItem db) {
		this();
		this.itemDB = db;
		roleItem = TextUtil.GSON.fromJson(db.getItemJsonStr(), RoleItem.class);
		// 加载
		RoleDAO roleDao = RoleDAO.getFromApplicationContext(ServerLancher
				.getAc());
		if (!TextUtil.isBlank(db.getSellerId())) {
			Role seller = roleDao.findById(db.getSellerId());
			sellerRoleView = new AuctionRoleView(seller.getId(),
					seller.getName(), seller.getVip().getVipLevel());
		}
		if (!TextUtil.isBlank(db.getBidRoleId())) {
			Role bidder = roleDao.findById(db.getBidRoleId());
			bidderRoleView = new AuctionRoleView(bidder.getId(),
					bidder.getName(), bidder.getVip().getVipLevel());
		}
	}

	public String getId() {
		return itemDB.getId();
	}

	/*
	 * public AuctionHouseItemView getView() { IItem item =
	 * XsgItemManager.getInstance().createItem(roleItem); long price =
	 * itemDB.getCurrentPrice(); // 没人出价则当前价格为基础价格 if (price <= 0) { price =
	 * itemDB.getBasePrice(); } AuctionHouseItemView view = new
	 * AuctionHouseItemView(itemDB.getId(), item.getView(),
	 * itemDB.getBasePrice(), price, itemDB.getFixedPrice(), sellerRoleView,
	 * bidderRoleView, (itemDB.getEndTime() - System.currentTimeMillis()) /
	 * 1000); return view; }
	 */

	public static XsgAuctionHouseItem build() {
		return new XsgAuctionHouseItem(new AuctionHouseItem());
	}

	public XsgAuctionHouseItem setID(String id) {
		this.itemDB.setId(id);
		return this;
	}

	public XsgAuctionHouseItem setSellerId(String sellerId) {
		this.itemDB.setSellerId(sellerId);
		return this;
	}

	public XsgAuctionHouseItem setRole(IRole role) {
		this.sellerRoleView = new AuctionRoleView(role.getRoleId(),
				role.getName(), role.getVipLevel());
		return this;
	}

	public XsgAuctionHouseItem setBasePrice(long price) {
		this.itemDB.setBasePrice(price);
		return this;
	}

	public XsgAuctionHouseItem setCurrentPrice(long price) {
		this.itemDB.setCurrentPrice(price);
		return this;
	}

	public XsgAuctionHouseItem setFixedPrice(long price) {
		this.itemDB.setFixedPrice(price);
		return this;
	}

	public XsgAuctionHouseItem setBidNum(int num) {
		this.itemDB.setBidNum(num);
		return this;
	}

	public XsgAuctionHouseItem setBidRoleId(String id) {
		this.itemDB.setBidRoleId(id);
		return this;
	}

	public XsgAuctionHouseItem setBidder(IRole role) {
		this.bidderRoleView = new AuctionRoleView(role.getRoleId(),
				role.getName(), role.getVipLevel());
		return this;
	}

	public XsgAuctionHouseItem setStartTime(Date time) {
		this.itemDB.setStartTime(time);
		return this;
	}

	public XsgAuctionHouseItem setEndTime(long time) {
		this.itemDB.setEndTime(time);
		return this;
	}

	public XsgAuctionHouseItem setType(int type) {
		this.itemDB.setType(type);
		return this;
	}

	public XsgAuctionHouseItem setItemJson(RoleItem roleItem) {
		this.roleItem = roleItem;
		this.itemDB.setItemJsonStr(TextUtil.GSON.toJson(roleItem));
		return this;
	}

	public XsgAuctionHouseItem setNum(int num) {
		this.itemDB.setNum(num);
		return this;
	}

	public String getName() {
		return XsgItemManager.getInstance()
				.findAbsItemT(roleItem.getTemplateCode()).getName();
	}

	public RoleItem getRoleItem() {
		return roleItem;
	}

	public long getCurrentPrice() {
		return itemDB.getCurrentPrice();
	}

	public long getBasePrice() {
		return itemDB.getBasePrice();
	}

	public int getBidNum() {
		return itemDB.getBidNum();
	}

	public long getFixedPrice() {
		return itemDB.getFixedPrice();
	}

	public String getSellerId() {
		return itemDB.getSellerId();
	}

	public String getBidderId() {
		return itemDB.getBidRoleId();
	}

	public String getBidderName() {
		return bidderRoleView.name;
	}

	public int getType() {
		return itemDB.getType();
	}

	public int getSubType() {
		int itemType = getType();
		switch (itemType) {
		case 0:
			NormalItemT itemT = (NormalItemT) XsgItemManager.getInstance()
					.findAbsItemT(roleItem.getTemplateCode());
			return itemT.itemTypeID;
		case 1: // 阵法
			return 9;
		case 2: // 装备
			return 8;
		default:
			break;
		}
		return 0;
	}

	public int getQuality() {
		return XsgItemManager.getInstance()
				.findAbsItemT(roleItem.getTemplateCode()).getColor().value();
	}

	public int getNum() {
		return itemDB.getNum();
	}

	public long getEndTime() {
		return itemDB.getEndTime();
	}

	public AuctionHouseItem getDBItem() {
		return itemDB;
	}

	public String getTemplateId() {
		return roleItem.getTemplateCode();
	}

	public AuctionRoleView getSellerRoleView() {
		return sellerRoleView;
	}

	public AuctionRoleView getBidderRoleView() {
		return bidderRoleView;
	}

	@Override
	public byte[] cloneData() {
		return TextUtil.objectToBytes(this.itemDB);
	}

	@Override
	public void save(byte[] data) {
		XsgAuctionHouseManager.getInstance().saveAuctionHouseItem(
				(AuctionHouseItem) TextUtil.bytesToObject(data));
	}

	/** 持久化到数据库, DBThread 中执行 */
	public void saveToDBAsync() {
		saver.saveAsyn();
	}

	public void addRecord(AuctionHouseRecord record) {
		itemDB.getRecords().add(record);
	}

	public void setPauseTime(Date time) {
		itemDB.setPauseTime(time);
	}
}
