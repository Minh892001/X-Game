package com.morefun.XSanGo.luckybag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.LuckyBagItem;
import com.XSanGo.Protocol.LuckyBagView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleLuckyBag;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.ILuckyBag;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;

@RedPoint(isTimer = true)
public class LuckyBagControler implements ILuckyBagControler, ICharge {

	private IRole iRole;
	private Role role;
	private ILuckyBag luckyBagEvent;

	public LuckyBagControler(IRole iRole, Role role) {
		this.iRole = iRole;
		this.role = role;
		luckyBagEvent = iRole.getEventControler().registerEvent(ILuckyBag.class);
		iRole.getEventControler().registerHandler(ICharge.class, this);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		try {
			LuckyBagView view = getLuckBagView();
			for (LuckyBagItem i : view.dayBag) {
				if (!i.received && view.totalCharge >= i.value) {
					return new MajorUIRedPointNote(MajorMenu.LuckyBagMenu, false);
				}
			}
			for (LuckyBagItem i : view.monthBag) {
				if (!i.received && view.chargeDay >= i.value) {
					return new MajorUIRedPointNote(MajorMenu.LuckyBagMenu, false);
				}
			}
		} catch (NoteException e) {
			return null;
		}
		return null;
	}

	@Override
	public LuckyBagView getLuckBagView() throws NoteException {
		List<LuckyBagItem> dayItems = new ArrayList<LuckyBagItem>();
		for (LuckyBagItemT lt : XsgLuckyBagManager.getInstance().getDayBagItems()) {
			dayItems.add(new LuckyBagItem(lt.id, lt.value, lt.items.toArray(new Property[0]), isAlreadyReceived(lt.id,
					0)));
		}
		List<LuckyBagItem> monthItems = new ArrayList<LuckyBagItem>();
		for (LuckyBagItemT lt : XsgLuckyBagManager.getInstance().getMonthBagItems()) {
			monthItems.add(new LuckyBagItem(lt.id, lt.value, lt.items.toArray(new Property[0]), isAlreadyReceived(
					lt.id, 1)));
		}
		int totalCharge = iRole.getVipController().getChargeYuanbao(DateUtil.getFirstSecondOfToday().getTime(),
				new Date());
		int sumChargeDays = iRole.getVipController().getMonthChargeDays(new Date());
		int[] redNotes = new int[2];
		redNotes[0] = iRole.getSuperChargeControlle().getRedPointNote()==null ? 0 : 1;
		redNotes[1] = iRole.getSuperRaffleControlle().getRedPointNote()==null ? 0 : 1;
		LuckyBagView view = new LuckyBagView(totalCharge, dayItems.toArray(new LuckyBagItem[0]), sumChargeDays,
		    monthItems.toArray(new LuckyBagItem[0]), redNotes);
		return view;
	}

	@Override
	public void receiveDayBag(int id) throws NoteException {
		LuckyBagItemT bagItemT = null;
		for (LuckyBagItemT lt : XsgLuckyBagManager.getInstance().getDayBagItems()) {
			if (lt.id == id) {
				bagItemT = lt;
				break;
			}
		}
		if (bagItemT == null) {
			throw new NoteException(Messages.getString("DayConsumeControler.5"));
		}
		if (this.getLuckBagView().totalCharge < bagItemT.value) {
			throw new NoteException(Messages.getString("DayChargeControler.3"));
		}
		if (isAlreadyReceived(id, 0)) {
			throw new NoteException(Messages.getString("DayChargeControler.4"));
		}
		// 结算奖励
		for (Property p : bagItemT.items) {
			this.iRole.getRewardControler().acceptReward(p.code, p.value);
		}
		// 记录数据
		RoleLuckyBag data = null;
		for (RoleLuckyBag l : role.getRoleLuckyBags()) {
			if (l.getScriptId() == id && l.getType() == 0) {
				data = l;
				break;
			}
		}
		Date now = Calendar.getInstance().getTime();
		if (data == null) {
			data = new RoleLuckyBag(GlobalDataManager.getInstance().generatePrimaryKey(), role, id, now, 0);
			this.role.getRoleLuckyBags().add(data);
		} else {
			data.setLastReceiveTime(now);
		}
		luckyBagEvent.onReceiveLuckyBag(0, id);
	}

	@Override
	public void receiveMonthBag(int id) throws NoteException {
		LuckyBagItemT bagItemT = null;
		for (LuckyBagItemT lt : XsgLuckyBagManager.getInstance().getMonthBagItems()) {
			if (lt.id == id) {
				bagItemT = lt;
				break;
			}
		}
		if (bagItemT == null) {
			throw new NoteException(Messages.getString("DayConsumeControler.5"));
		}
		if (this.getLuckBagView().chargeDay < bagItemT.value) {
			throw new NoteException(Messages.getString("DayChargeControler.3"));
		}
		if (isAlreadyReceived(id, 1)) {
			throw new NoteException(Messages.getString("DayChargeControler.4"));
		}
		// 结算奖励
		for (Property p : bagItemT.items) {
			this.iRole.getRewardControler().acceptReward(p.code, p.value);
		}
		// 记录数据
		RoleLuckyBag data = null;
		for (RoleLuckyBag l : role.getRoleLuckyBags()) {
			if (l.getScriptId() == id && l.getType() == 1) {
				data = l;
				break;
			}
		}
		Date now = Calendar.getInstance().getTime();
		if (data == null) {
			data = new RoleLuckyBag(GlobalDataManager.getInstance().generatePrimaryKey(), role, id, now, 1);
			this.role.getRoleLuckyBags().add(data);
		} else {
			data.setLastReceiveTime(now);
		}
		luckyBagEvent.onReceiveLuckyBag(1, id);
	}

	private boolean isAlreadyReceived(int scriptId, int type) {
		RoleLuckyBag data = null;
		for (RoleLuckyBag l : role.getRoleLuckyBags()) {
			if (l.getScriptId() == scriptId && l.getType() == type) {
				data = l;
				break;
			}
		}
		if (type == 0) {// 当日
			return data != null && DateUtil.isSameDay(data.getLastReceiveTime(), new Date());
		} else {// 当月
			return data != null && DateUtil.isSameMonth(data.getLastReceiveTime(), new Date());
		}
	}

	@Override
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		MajorUIRedPointNote redPoint = getRedPointNote();
		if (redPoint != null) {
			iRole.getNotifyControler().onMajorUIRedPointChange(redPoint);
		}
	}
}
