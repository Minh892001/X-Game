package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SummationActivityView;
import com.XSanGo.Protocol.SummationReward;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleBigDayCharge;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.IGainDayCharge;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;

@RedPoint
public class BigDayChargeControler implements IBigDayChargeControler, ICharge {
	private IRole rt;
	private Role db;
	private IGainDayCharge gainDayChargeEvent;
	// 限时活动登录都显示红点
	private boolean firstOpen = false;

	public BigDayChargeControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;
		this.gainDayChargeEvent = rt.getEventControler().registerEvent(IGainDayCharge.class);
		firstOpen = true;
		
		rt.getEventControler().registerHandler(ICharge.class, this);
	}

	@Override
	public SummationActivityView getDayChargeView() throws NoteException {
		SummationActivityView view = new SummationActivityView();
		ActivityT at = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.BigDayCharge);
		DayChargeConsumeT chargeT = getCurrentActivity(at);
		if (chargeT == null) {
			throw new NoteException(Messages.getString("DayChargeControler.0")); //$NON-NLS-1$
		}
		view.total = this.rt.getVipController().getChargeYuanbao(chargeT.startDate, chargeT.endDate);
		view.remainSeconds = (int) (DateUtil.compareTime(chargeT.endDate, new Date()) / 1000);
		List<SummationReward> rewards = new ArrayList<SummationReward>();
		for (DayChargeConsumeItemT i : chargeT.items) {
			rewards.add(new SummationReward(i.money, isAlreadyReceived(i.money), new ItemView[] { new ItemView("", //$NON-NLS-1$
					ItemType.DefaultItemType, i.itemId, 1, "") }));
		}
		view.configs = rewards.toArray(new SummationReward[0]);
		return view;
	}

	@Override
	public void receiveDayCharge(int threshold) throws NoteException {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.BigDayCharge)) {
			throw new NoteException(Messages.getString("DayChargeControler.2")); //$NON-NLS-1$
		}
		if (this.getDayChargeView().total < threshold) {
			throw new NoteException(Messages.getString("DayChargeControler.3")); //$NON-NLS-1$
		}
		if (isAlreadyReceived(threshold)) {
			throw new NoteException(Messages.getString("DayChargeControler.4")); //$NON-NLS-1$
		}
		ActivityT at = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.BigDayCharge);
		DayChargeConsumeT dayT = getCurrentActivity(at);
		DayChargeConsumeItemT itemT = null;
		for (DayChargeConsumeItemT i : dayT.items) {
			if (i.money == threshold) {
				itemT = i;
			}
		}
		if (itemT == null) {
			throw new NoteException(Messages.getString("DayChargeControler.5")); //$NON-NLS-1$
		}
		// 结算奖励
		this.rt.getRewardControler().acceptReward(itemT.itemId, 1);
		// 记录数据
		RoleBigDayCharge data = this.db.getBigDayCharges().get(threshold);
		Date now = Calendar.getInstance().getTime();
		if (data == null) {
			data = new RoleBigDayCharge(GlobalDataManager.getInstance().generatePrimaryKey(), db, threshold, now);
			this.db.getBigDayCharges().put(threshold, data);
		} else {
			data.setCreateDate(now);
		}
		gainDayChargeEvent.onGainDayCharge(itemT.money, itemT.itemId);
	}

	/**
	 * 获取当天的活动
	 * 
	 * @param at
	 * @return
	 */
	private DayChargeConsumeT getCurrentActivity(ActivityT at) {
		String[] ids = at.subActivity.split(","); //$NON-NLS-1$
		for (String id : ids) {
			DayChargeConsumeT t = XsgActivityManage.getInstance().bigDayCharges.get(Integer.parseInt(id));
			if (t != null && DateUtil.isSameDay(new Date(), t.startDate)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 是否领取过奖励
	 * 
	 * @param threshold
	 * @return
	 */
	private boolean isAlreadyReceived(int threshold) {
		RoleBigDayCharge data = this.db.getBigDayCharges().get(threshold);
		ActivityT at = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.BigDayCharge);
		DayChargeConsumeT chargeT = getCurrentActivity(at);
		if (chargeT == null) {
			return false;
		}
		return data != null && DateUtil.isBetween(data.getCreateDate(), chargeT.startDate, chargeT.endDate);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.BigDayCharge)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if (rt.getChatControler().getChatCb() == null) {
			return null;
		}

		if (firstOpen && !isOver()) {
			firstOpen = false;
			return new MajorUIRedPointNote(MajorMenu.BigDayChargeMenu, true);
		}

		SummationActivityView view = null;
		try {
			view = getDayChargeView();
		} catch (NoteException e) {
			return null;
		}
		for (SummationReward sr : view.configs) {
			if (view.total >= sr.threshold && !isAlreadyReceived(sr.threshold)) {
				return new MajorUIRedPointNote(MajorMenu.BigDayChargeMenu, false);
			}
		}
		return null;
	}

	@Override
	public void setFirstOpen(boolean value) {
		firstOpen = value;
	}

	@Override
	public boolean isOver() {
		try {
			SummationActivityView view = getDayChargeView();
			for (SummationReward s : view.configs) {
				if (!s.received) {
					return false;
				}
			}
		} catch (NoteException e) {
			return true;
		}
		return true;
	}

	@Override
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		rt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}
}
