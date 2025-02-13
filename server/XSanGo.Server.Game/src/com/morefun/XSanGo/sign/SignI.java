package com.morefun.XSanGo.sign;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Ice.Current;
import Ice.LocalException;
import Ice.UserException;

import com.XSanGo.Protocol.AMD_Sign_cdkey;
import com.XSanGo.Protocol.AMD_Sign_inviteCode;
import com.XSanGo.Protocol.AMD_Sign_openTheSignView;
import com.XSanGo.Protocol.AMD_Sign_reqLottery;
import com.XSanGo.Protocol.AMD_Sign_roulette;
import com.XSanGo.Protocol.AMI_CenterCallback_beginUseCDK;
import com.XSanGo.Protocol.CenterCallbackPrx;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.LotteryView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OpenSignView;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.SignAward;
import com.XSanGo.Protocol.SignItem;
import com.XSanGo.Protocol.TotalSignItem;
import com.XSanGo.Protocol._SignDisp;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.center.XsgCenterManager;
import com.morefun.XSanGo.event.protocol.ICdkUse;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class SignI extends _SignDisp {
	private static final long serialVersionUID = 2640574291738222681L;

	private IRole roleRt;

	protected ICdkUse cdkUseEvent;

	public SignI(IRole r) {
		this.roleRt = r;
		this.cdkUseEvent = this.roleRt.getEventControler().registerEvent(ICdkUse.class);
	}

	@Override
	public void openTheSignView_async(AMD_Sign_openTheSignView __cb, Current __current) {
		SignT[] ast = XsgSignManager.getInstance().allSignTItems();

		SignItem[] si = new SignItem[ast.length];
		for (int i = 0; i < si.length; i++) {
			TcResult tc = XsgRewardManager.getInstance().doTc(roleRt, ast[i].tc);
			List<SignAward> awards = new ArrayList<SignAward>();
			for (Entry<String, Integer> entry : tc) {
				String code = entry.getKey();
				int num = entry.getValue();

				awards.add(new SignAward(XsgItemManager.getInstance().findAbsItemT(code).getItemType().value(), code,
						num));
			}
			si[i] = new SignItem(ast[i].day, awards.toArray(new SignAward[awards.size()]), roleRt.getSignController()
					.getSignTimes(ast[i].day), ast[i].vipLimit, roleRt.getSignController().signCost(ast[i].day),
					ast[i].quality);
		}

		Map<String, TotalSignGiftPacksT> sp = XsgSignManager.getInstance().getSignGiftPacksT();
		Iterator<Map.Entry<String, TotalSignGiftPacksT>> sitor = sp.entrySet().iterator();

		TotalSignItem[] totalSignPack = new TotalSignItem[sp.size()];
		for (int i = 0; i < sp.size(); i++) {
			Map.Entry<String, TotalSignGiftPacksT> drop = sitor.next();
			String sumTc = drop.getKey();
			TcResult tc = XsgRewardManager.getInstance().doTc(roleRt, sumTc);
			List<SignAward> apv = new ArrayList<SignAward>();
			for (Entry<String, Integer> entry : tc) {
				String code = entry.getKey();
				int num = entry.getValue();

				apv.add(new SignAward(XsgItemManager.getInstance().findAbsItemT(code).getItemType().value(), code, num));
			}
			int status = 0;
			TotalSignGiftPacksT template = drop.getValue();
			if (roleRt.getSignController().allreadyCollectGift(sumTc)) {
				status = 1;
			} else if (roleRt.getSignController().getTotalSignTimes() >= template.timeLimit) {
				status = 2;
			} else {
				status = 3;
			}
			totalSignPack[i] = new TotalSignItem(template.timeLimit, status, apv.toArray(new SignAward[apv.size()]),
			// 没领过的才显示特效
					(status == 2 || status == 3) && template.showSpecialEffect == 1);
		}

		SignRouletteT t = XsgSignManager.getInstance().todaySignRouletteT();
		int remainTimes = roleRt.getSignController().remainRouletteTimes();
		String[] items = new String[] { t.item1, t.item2, t.item3, t.item4, t.item5, t.item6, t.item7, t.item8 };
		int[] itemNums = new int[] { t.num1, t.num2, t.num3, t.num4, t.num5, t.num6, t.num7, t.num8 };

		LotteryView lview = new LotteryView(t.gold_need, mapTypes(items), remainTimes, items, itemNums);

		byte totalSignTimes = roleRt.getSignController().getTotalSignTimes();
		OpenSignView view = new OpenSignView(si, totalSignPack, lview, (byte) (Calendar.getInstance().get(
				Calendar.MONTH) + 1), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), totalSignTimes, roleRt
				.getSignController().canAutoResign(), roleRt.getVipController().hasMonthCard());

		__cb.ice_response(LuaSerializer.serialize(view));
	}

	private int[] mapTypes(String[] items) {
		int[] types = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			types[i] = XsgItemManager.getInstance().findAbsItemT(items[i]).getItemType().value();
		}
		return types;
	}

	@Override
	public void signIn(String itemId, Current __current) throws NoteException, NotEnoughYuanBaoException {
		try {
			roleRt.getSignController().signIn(Integer.parseInt(itemId));
		} catch (NotEnoughYuanBaoException e) {
			throw e;
		} catch (Exception e) {
			throw new NoteException(Messages.getString("SignI.0") + e.getMessage(), e); //$NON-NLS-1$
		}
	}

	@Override
	public int autoResign(Current __current) throws NoteException, NotEnoughYuanBaoException {
		return roleRt.getSignController().autoResign();
	}

	@Override
	public void collectGiftPack(int count, Current __current) throws NoteException {
		roleRt.getSignController().collectGiftPack(count);
	}

	@Override
	public void reqLottery_async(AMD_Sign_reqLottery __cb, Current __current) throws NoteException {
		SignRouletteT t = XsgSignManager.getInstance().todaySignRouletteT();
		LotteryView view = new LotteryView(t.gold_need, new int[] {
				XsgItemManager.getInstance().findAbsItemT(t.item1).getItemType().value(),
				XsgItemManager.getInstance().findAbsItemT(t.item2).getItemType().value(),
				XsgItemManager.getInstance().findAbsItemT(t.item3).getItemType().value(),
				XsgItemManager.getInstance().findAbsItemT(t.item4).getItemType().value() }, roleRt.getSignController()
				.remainRouletteTimes(), new String[] { t.item1, t.item2, t.item3, t.item4 }, new int[] { t.num1,
				t.num2, t.num3, t.num4, t.num5, t.num6, t.num7, t.num8 });
		__cb.ice_response(LuaSerializer.serialize(view));
	}

	@Override
	public void roulette_async(AMD_Sign_roulette __cb, Current __current) throws NotEnoughMoneyException {
		String[] s = roleRt.getSignController().roulette();
		__cb.ice_response(LuaSerializer.serialize(s));
	}

	@Override
	public void cdkey_async(final AMD_Sign_cdkey __cb, final String key, Current __current) throws NoteException {
		// 检查是否使用过同批次CDK
		final String cdkeyCode = key.toUpperCase();

		final CenterCallbackPrx centerPrx = XsgCenterManager.instance().getCb();
		if (centerPrx == null) {
			__cb.ice_exception(new NoteException(Messages.getString("SignI.2"))); //$NON-NLS-1$
			return;
		}

		centerPrx.begin_beginUseCDK(this.roleRt.getAccount(), cdkeyCode, this.roleRt.getLevel(), "", //$NON-NLS-1$
				new AMI_CenterCallback_beginUseCDK() {
					@Override
					public void ice_response(final Property[] __ret) {
						LogicThread.execute(new Runnable() {

							@Override
							public void run() {
								roleUseCdk(__cb, cdkeyCode, centerPrx, __ret);
							}
						});
					}

					@Override
					public void ice_exception(LocalException ex) {
						__cb.ice_exception(ex);
					}

					@Override
					public void ice_exception(UserException ex) {
						__cb.ice_exception(ex);
					}
				});
	}

	/**
	 * 
	 * @param __cb
	 *            玩家回调对象
	 * @param cdkeyCode
	 *            兑换码
	 * @param centerPrx
	 *            中心服务器代理
	 * @param __ret
	 *            对应的奖励列表
	 */
	private void roleUseCdk(final AMD_Sign_cdkey __cb, final String cdkeyCode, final CenterCallbackPrx centerPrx,
			Property[] __ret) {
		String group = cdkeyCode.substring(0, 4);
		if (roleRt.containsCDKGroup(group)) {
			__cb.ice_exception(new NoteException(Messages.getString("SignI.1"))); //$NON-NLS-1$
			return;
		}
		List<ItemView> list = new ArrayList<ItemView>();
		// 给予物品道具，并记录兑换日志
		for (Property p : __ret) {
			list.add(XsgRewardManager.getInstance().generateItemView(p.code, p.value));
		}

		ItemView[] items = list.toArray(new ItemView[0]);
		roleRt.getRewardControler().acceptReward(items);
		roleRt.addCDKRecord(group, cdkeyCode);
		// 通知中心服务器更新CDK状态
		centerPrx.begin_endUseCDK(cdkeyCode);
		__cb.ice_response(LuaSerializer.serialize(items));

		// 事件发送
		cdkUseEvent.onCdkUsed(cdkeyCode, items);
	}

	@Override
	public void broadcastLastLottery(Current __current) {
		roleRt.getSignController().broadcastLastRoulette();
	}

	@Override
	public void inviteCode_async(AMD_Sign_inviteCode __cb, String code, Current __current) throws NoteException {
		roleRt.getSignController().inviteCode(code, __cb);
	}
}
