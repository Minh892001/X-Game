package com.morefun.XSanGo.vip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AMD_Vip_createOrderForAppleAppStore;
import com.XSanGo.Protocol.AMD_Vip_getChannelOrderIdFromPayCenter;
import com.XSanGo.Protocol.Callback_CenterCallback_createOrderForAppleAppStore;
import com.XSanGo.Protocol.Callback_CenterCallback_getChannelOrderIdFromPayCenter;
import com.XSanGo.Protocol.CenterCallbackPrx;
import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.TopupItem;
import com.XSanGo.Protocol.TopupView;
import com.XSanGo.Protocol.VipTraderItem;
import com.XSanGo.Protocol.VipTraderView;
import com.XSanGo.Protocol._VipDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.center.XsgCenterManager;
import com.morefun.XSanGo.db.game.RoleVipGiftPacks;
import com.morefun.XSanGo.db.game.RoleVipTraderItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

import Ice.Current;
import Ice.LocalException;
import Ice.UserException;

public class VipI extends _VipDisp {
	private final static Log logger = LogFactory.getLog(VipI.class);

	private static final long serialVersionUID = 8900743033018318959L;
	private IRole roleRt;

	public VipI(IRole r) {
		this.roleRt = r;
	}

	@Override
	public String getVipTraderItems(Current __current) {
		Collection<RoleVipTraderItem> titems = roleRt.getVipController().getTraderItems();

		List<VipTraderItem> items = new ArrayList<VipTraderItem>();
		for (RoleVipTraderItem item : titems) {
			VipTraderT vipTraderItems = XsgVipManager.getInstance().vipTraderT.get(item.getItemTid());
			items.add(new VipTraderItem(vipTraderItems.id, vipTraderItems.itemId, item.getCount(), vipTraderItems.name,
					vipTraderItems.coinType, vipTraderItems.price * item.getCount(), vipTraderItems.buyVipLevel, item
							.getBoughtToday()));
		}

		return LuaSerializer.serialize(new VipTraderView(roleRt.getVipController().getTraderRefreshingTime(), items
				.toArray(new VipTraderItem[items.size()])));
	}

	@Override
	public void buyVipTraderItems(int itemId, Current __current) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		roleRt.getVipController().buyVipTraderItems(itemId);
	}

	@Override
	public String getGiftPackStatus(Current __current) {
		roleRt.getRoleOpenedMenu().setOpenVipGiftDate(new Date());
		IntIntPair[] pairs = new IntIntPair[XsgVipManager.getInstance().getVipSize()];
		for (int i = 0; i < pairs.length; i++) {
			pairs[i] = new IntIntPair(i, 0);
			for (RoleVipGiftPacks pack : roleRt.getVipController().getRoleGiftPacks()) {
				if (pack.getVipLevel() == i) {
					pairs[i] = new IntIntPair(i, 1);
				}
			}
		}
		return LuaSerializer.serialize(pairs);
	}

	@Override
	public String openTopupVIew(Current __current) {
		TopupView view = this.roleRt.getVipController().getChargeView();
		this.filterChargeView(view);

		return LuaSerializer.serialize(view);
	}

	/**
	 * 过滤充值界面信息，主要针对特殊渠道做针对性过滤
	 * 
	 * @param view
	 */
	private void filterChargeView(TopupView view) {
		if (this.roleRt.getAccountChannel() == 5017) {// 移动咪咕
			this.chinaMobileHandle(view);
		}
	}

	/**
	 * 移动咪咕渠道过滤处理，100块以上的不出现
	 * 
	 * @param view
	 */
	private void chinaMobileHandle(TopupView view) {
		List<TopupItem> list = new ArrayList<TopupItem>();
		for (TopupItem topupItem : view.topupItems) {
			list.add(topupItem);
		}
		CollectionUtil.removeWhere(list, new IPredicate<TopupItem>() {
			@Override
			public boolean check(TopupItem item) {
				return item.rmb > 100;
			}
		});

		view.topupItems = list.toArray(new TopupItem[0]);
	}

	@Override
	public void buyGiftPacks(int vipLevel, Current __current) throws NoteException, NotEnoughYuanBaoException {
		roleRt.getVipController().buyGiftPacks(vipLevel);
	}

	@Override
	public void checkChargeStatus(int chargeId, boolean chargeForFriend, Current __current) throws NoteException {
		this.roleRt.getVipController().checkChargeStatus(chargeId, chargeForFriend);
	}

	@Override
	public void getChannelOrderIdFromPayCenter_async(final AMD_Vip_getChannelOrderIdFromPayCenter __cb, int channel,
			int appId, int money, String mac, String params, Current __current) throws NoteException {
		// TODO S Ice.NoEndpointException
		CenterCallbackPrx prx = XsgCenterManager.instance().getCb();
		if (prx == null) {
			__cb.ice_exception(new NoteException(Messages.getString("VipI.0"))); //$NON-NLS-1$
			return;
		}

		prx.begin_getChannelOrderIdFromPayCenter(channel, appId, money, mac, this.roleRt.getAccount(),
				this.roleRt.getRoleId(), params, new Callback_CenterCallback_getChannelOrderIdFromPayCenter() {

					@Override
					public void exception(LocalException __ex) {
						__cb.ice_exception(__ex);
					}

					@Override
					public void response(String __ret) {
						__cb.ice_response(__ret);
					}

					@Override
					public void exception(UserException __ex) {
						__cb.ice_exception(__ex);
					}
				});
	}

	@Override
	public void createOrderForAppleAppStore_async(final AMD_Vip_createOrderForAppleAppStore __cb, int templateId,
			String appStoreOrderId, int channel, int appId, String itemId, String mac, String params, Current __current)
			throws NoteException {
		logger.info(TextUtil.format("Accept a IOS order,[{0},{1},{2}]", appStoreOrderId, appId, itemId));
		CenterCallbackPrx prx = XsgCenterManager.instance().getCb();
		if (prx == null) {
			__cb.ice_exception(new NoteException(Messages.getString("VipI.1"))); //$NON-NLS-1$
			return;
		}

		// 目前IOS正版不做给好友充值等功能，所以计算金额只要查出对应配置就行
		ChargeItemT chargeT = XsgVipManager.getInstance().getChargeItemT(templateId);
		int money = chargeT.rmb;
		// 由于苹果接入的限制，客户端无法生成自定义参数，只能在这里根据格式造一个
		CustomChargeParams paramObj = new CustomChargeParams(chargeT.id, mac, null);
		prx.begin_createOrderForAppleAppStore(appStoreOrderId, channel, appId, money, itemId, mac,
				this.roleRt.getAccount(), this.roleRt.getRoleId(), TextUtil.GSON.toJson(paramObj),
				new Callback_CenterCallback_createOrderForAppleAppStore() {

					@Override
					public void exception(LocalException __ex) {
						__cb.ice_exception(__ex);
					}

					@Override
					public void response() {
						__cb.ice_response();
					}

					@Override
					public void exception(UserException __ex) {
						__cb.ice_exception(__ex);
					}

				});
	}
}
