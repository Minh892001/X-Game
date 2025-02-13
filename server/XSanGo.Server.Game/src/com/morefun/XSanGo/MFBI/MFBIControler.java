/**
 * 
 */
package com.morefun.XSanGo.MFBI;

import java.util.List;

import com.XSanGo.Protocol.BuyHeroResult;
import com.XSanGo.Protocol.CommodityView;
import com.XSanGo.Protocol.CustomChargeParams;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.event.protocol.IArenaMallExchange;
import com.morefun.XSanGo.event.protocol.IBuy10WineByJinbi;
import com.morefun.XSanGo.event.protocol.IBuy10WineByYuanbao;
import com.morefun.XSanGo.event.protocol.IBuySingleWineByJinbi;
import com.morefun.XSanGo.event.protocol.IBuySingleWineByYuanbao;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.IGuideComplete;
import com.morefun.XSanGo.event.protocol.IOffline;
import com.morefun.XSanGo.event.protocol.IRoleNameChange;
import com.morefun.XSanGo.event.protocol.IRoleReset;
import com.morefun.XSanGo.event.protocol.IShopping;
import com.morefun.XSanGo.event.protocol.ITraderItemBuy;
import com.morefun.XSanGo.event.protocol.IVipGiftBuy;
import com.morefun.XSanGo.event.protocol.IVipShopItemBuy;
import com.morefun.XSanGo.event.protocol.IYuanbaoChange;
import com.morefun.XSanGo.hero.market.HeroMarketManager;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.trader.TraderType;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.ChargeItemT;
import com.morefun.XSanGo.vip.XsgVipManager;
import com.morefun.bi.sdk.EventType;
import com.morefun.bi.sdk.RoleInfo;
import com.morefun.bi.sdk.UserInfo;

/**
 * 数据中心接口 功能
 * 
 * @author 吕明涛
 * 
 */
class MFBIControler implements IMFBIControler, IOffline, IYuanbaoChange, ICharge, IVipGiftBuy, IVipShopItemBuy,
		IArenaMallExchange, ITraderItemBuy, IShopping, IBuySingleWineByJinbi, IBuySingleWineByYuanbao,
		IBuy10WineByJinbi, IBuy10WineByYuanbao, IGuideComplete, IRoleReset, IRoleNameChange {

	// private static final Log log =
	// LogFactory.getLog(BuyJInbiControler.class);

	public IRole roleRt;
	public Role roleDb;

	/** 变化的类型，增加和减少 */
	enum ChangeType {
		add, sub
	}

	public MFBIControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		// 角色，下线事件
		this.roleRt.getEventControler().registerHandler(IOffline.class, this);
		// 角色，元宝变化事件
		this.roleRt.getEventControler().registerHandler(IYuanbaoChange.class, this);
		// 角色，充值事件
		this.roleRt.getEventControler().registerHandler(ICharge.class, this);

		this.roleRt.getEventControler().registerHandler(IVipGiftBuy.class, this);
		this.roleRt.getEventControler().registerHandler(IVipShopItemBuy.class, this);
		this.roleRt.getEventControler().registerHandler(IArenaMallExchange.class, this);
		this.roleRt.getEventControler().registerHandler(IBuySingleWineByJinbi.class, this);
		this.roleRt.getEventControler().registerHandler(ITraderItemBuy.class, this);
		this.roleRt.getEventControler().registerHandler(IShopping.class, this);
		this.roleRt.getEventControler().registerHandler(IBuySingleWineByYuanbao.class, this);
		this.roleRt.getEventControler().registerHandler(IBuy10WineByJinbi.class, this);
		this.roleRt.getEventControler().registerHandler(IBuy10WineByYuanbao.class, this);
		this.roleRt.getEventControler().registerHandler(IGuideComplete.class, this);
		this.roleRt.getEventControler().registerHandler(IRoleReset.class, this);
		this.roleRt.getEventControler().registerHandler(IRoleNameChange.class, this);
	}

	/**
	 * 添加用户和角色信息，并发送消息
	 * 
	 * @param msg
	 */
	private void addAndSend(BIMessage msg) {

		UserInfo userInfo = new UserInfo(XsgMFBIManager.getInstance().isTest, roleRt.getAccount(),
				roleDb.getFirstInTime(), 0, "", "", "", "", roleRt.getRemoteIp(), roleRt.getDeviceId(), "", "", "",
				String.valueOf(roleRt.getAccountChannel()), "");

		RoleInfo roleInfo = new RoleInfo(ServerLancher.getServerId(), roleRt.getRoleId(), roleDb.getCreateTime()
				.getTime(), roleRt.getLevel(), roleRt.getVipLevel(), roleRt.getVipController().getSumCharge(),
				roleRt.getTotalYuanbao(), 0, roleRt.getJinbi(), 0, "", "", "", "");

		roleInfo.reserve4 = TextUtil.GSON.toJson(new RoleParam(roleRt.getName(), roleRt.getSex()));

		msg.setUserInfo(userInfo);
		msg.setRoleInfo(roleInfo);

		XsgMFBIManager.getInstance().sendMsg(msg);
	}

	/**
	 * 登录发送 数据中心 信息
	 */
	@Override
	public void sendRoleLogin() {
		BIMessage msg = new BIMessage();
		msg.setType(EventType.RoleEnter);
		this.addAndSend(msg);
	}

	/**
	 * 登出
	 */
	@Override
	public void onRoleOffline(long onlineInterval) {
		BIMessage msg = new BIMessage();
		msg.setType(EventType.RoleQuit);
		msg.setParams(onlineInterval / 1000);
		this.addAndSend(msg);
	}

	/**
	 * 支付中心
	 */
	@Override
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		BIMessage msg = new BIMessage();
		msg.setType(EventType.Pay);

		int chargeTemplateId = params.item;
		ChargeItemT chargeItem = XsgVipManager.getInstance().topupItemT.get(chargeTemplateId);
		// 订单编号,支付货币类型,支付金额,充值数额,充值免费赠送数额,支付套餐,套餐数量,支付方式,支付通道
		msg.setParams(orderId, currency, chargeItem.rmb * 100, chargeItem.yuanbao, returnYuanbao, chargeTemplateId, 1,
				"mock".equals(params.mac) ? "mock" : "", "");

		this.addAndSend(msg);
	}

	@Override
	public void onYuanbaoChange(int oldBind, int oldUnbind, int newBind, int newUnbind, int changeValue) {
		// 绑定元宝变化
		BIMessage msg;

		ChangeType type;
		if (newBind != oldBind) {
			// 判断增加还是减少
			if (newBind - oldBind > 0) {
				type = ChangeType.add;
			} else {
				type = ChangeType.sub;
			}
			msg = new BIMessage();
			msg.setType(EventType.CurrencyChange);
			// 变更类型, 一级代币变更数额, 二级代币变更数额, 变更来源, 流水号
			msg.setParams(type.name(), 0, newBind - oldBind, "", "");

			this.addAndSend(msg);
		}

		// 非绑定元宝变化
		if (newUnbind != oldUnbind) {
			msg = new BIMessage();
			msg.setType(EventType.CurrencyChange);
			if (newUnbind - oldUnbind > 0) {
				type = ChangeType.add;
			} else {
				type = ChangeType.sub;
			}
			// 变更类型, 一级代币变更数额, 二级代币变更数额, 变更来源, 流水号
			msg.setParams(type.name(), newUnbind - oldUnbind, 0, "", "");
			this.addAndSend(msg);
		}

		// 代币存量
		msg = new BIMessage();
		msg.setType(EventType.CurrencyRemain);
		msg.setParams(DateUtil.toString(System.currentTimeMillis()), this.roleRt.getVipController().getUnBindYuanbao(),
				this.roleRt.getVipController().getBindYuanbao());
		this.addAndSend(msg);
	}

	/*************************************************
	 * 道具变化 暂时不用考虑
	 *************************************************/

	// 暂时不用，以后需要在给BI数据
	// private String setPropParam(String propId) {
	// MFBIPropParam param = new MFBIPropParam();
	// AbsItemT absItemT = XsgItemManager.getInstance().findAbsItemT(propId);
	//
	// param.setPropName(absItemT.getName());
	// param.setPropCateID(propId);
	// // param.setPropCateName(propCateName);
	//
	// return TextUtil.GSON.toJson(param);
	// }

	private void onItemConsume(ChangeType type, String itemId, int moneyType, int num, int price, int virtualMoney1,
			int virtualMoney2, int virtualMoney3) {
		AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(itemId);
		BIMessage msg = new BIMessage();
		msg.setType(EventType.PropChange);
		msg.setParams(type.name(), itemId, moneyType, price, num, virtualMoney1, virtualMoney2, virtualMoney3,
				TextUtil.format("'{\"propName\":\"'{0}\"}", itemT == null ? "" : itemT.getName()));
		this.addAndSend(msg);
	}

	public static void main(String[] args) {
		System.out.println(TextUtil.format("'{\"propName\":\"'{0}\"}", "test"));
	}

	/**
	 * 抽卡抽到 道具触发的事件
	 * 
	 * @param itemCode
	 */
	private void onItemChange(BuyHeroResult result, int moneyType) {
		if (!HeroMarketManager.getInstance().isRewardHero(result)) {
			String itemId = result.soulTemplateId;
			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(itemId);
			if (itemT != null) {
				this.onItemConsume(ChangeType.add, itemId, moneyType, result.totalNum, itemT.getSaleMoney(), 0, 0, 0);
			}
		}
	}

	@Override
	public void onBuyWineByJinbi(BuyHeroResult result, boolean free) {
		this.onItemChange(result, 1);
	}

	@Override
	public void onBuy10WineByYuanbao(List<BuyHeroResult> list) {
		for (BuyHeroResult result : list) {
			this.onItemChange(result, 0);
		}
	}

	@Override
	public void onBuy10WineByJinbi(List<BuyHeroResult> list) {
		for (BuyHeroResult result : list) {
			this.onItemChange(result, 1);
		}
	}

	@Override
	public void onBuyWineByYuanbao(BuyHeroResult result, boolean free) {
		this.onItemChange(result, 0);
	}

	@Override
	public void onExchange(String itemId, int cost) {
		// AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(itemId);
		// if (itemT != null) {
		// this.onItemConsume(itemId, itemId, itemT.getName(), itemT.getName(),
		// 1,
		// itemT.getSaleMoney(), 0, 0, cost);
		// }
	}

	@Override
	public void onBuyVipShopItem(String code, int count) {
		// VipTraderT vipTraderT =
		// XsgVipManager.getInstance().vipTraderT.get(code);
		// AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(code);
		// if (itemT != null && vipTraderT != null) {
		// this.onItemConsume(code, code, itemT.getName(), itemT.getName(),
		// count,
		// itemT.getSaleMoney(), ItemSaleType.SaleMall, vipTraderT.price, 0, 0);
		// }
	}

	@Override
	public void onVipGiftBuy(int vipLevel, String reward, int num) {
		// AbsItemT item = XsgItemManager.getInstance().findAbsItemT(reward);
		// if (item != null) {
		// this.onItemConsume(reward, reward, item.getName(), item.getName(),
		// num,
		// item.getSaleMoney(), ItemSaleType.SaleMall, 0, 0, 0);
		// }
	}

	@Override
	public void onItemBuy(CommodityView item, TraderType traderType) {
		// AbsItemT itemT =
		// XsgItemManager.getInstance().findAbsItemT(item.templateId);
		// if (itemT != null) {
		// this.onItemConsume(item.templateId, item.templateId, itemT.getName(),
		// itemT.getName(),
		// item.num, itemT.getSaleMoney(), ItemSaleType.SaleMall,
		// item.price.num, 0, 0);
		// }
	}

	@Override
	public void onShopping(String shopId, String templateId, int num, int price, int type) throws Exception {
		// AbsItemT itemT =
		// XsgItemManager.getInstance().findAbsItemT(templateId);
		// if (itemT != null) {
		// VipTraderT vipTraderT =
		// XsgVipManager.getInstance().vipTraderT.get(shopId);
		// this.onItemConsume(shopId, shopId, itemT.getName(), itemT.getName(),
		// num, price,
		// ItemSaleType.SaleMall, vipTraderT.price, 0, 0);
		// }
	}

	@Override
	public void onGuideCompleted(int guideId) {
		BIMessage msg = new BIMessage();
		msg.setType(EventType.Guide);
		msg.setParams(guideId);
		this.addAndSend(msg);
	}

	@Override
	public void onReset() {
		BIMessage msg = new BIMessage();
		msg.setType(EventType.RoleDel);
		this.addAndSend(msg);
	}

	@Override
	public void onRoleNameChange(String old, String name) {
		BIMessage msg = new BIMessage();
		msg.setType(EventType.Act);
		msg.setParams("1","1",null,old);
		this.addAndSend(msg);		
	}

}

class RoleParam {
	String roleName;
	int roleGender;

	public RoleParam(String roleName, int roleGender) {
		super();
		this.roleName = roleName;
		this.roleGender = roleGender;
	}
}