package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.morefun.XSanGo.db.game.RoleDayConsume;
import com.morefun.XSanGo.event.protocol.IGainDayConsume;
import com.morefun.XSanGo.event.protocol.IYuanbaoChange;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;

@RedPoint
public class DayConsumeControler implements IDayConsumeControler, IYuanbaoChange {
	private IRole rt;
	private Role db;
	private IGainDayConsume gainDayConsumeEvent;
	// 限时活动登录都显示红点
	private boolean firstOpen = false;

	public DayConsumeControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;
		this.rt.getEventControler().registerHandler(IYuanbaoChange.class, this);
		this.gainDayConsumeEvent = this.rt.getEventControler().registerEvent(IGainDayConsume.class);
		firstOpen = true;
	}

	@Override
	public SummationActivityView getDayConsumeView() throws NoteException {
		// 检查是否需要重置计数
		Date lastTime = this.db.getLastConsumeTime();
		ActivityT at = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.DayConsume);
		DayChargeConsumeT consumeT = getCurrentActivity(at);
		if (consumeT == null) {
			throw new NoteException(Messages.getString("DayConsumeControler.0")); //$NON-NLS-1$
		}
		if (lastTime == null || !DateUtil.isBetween(lastTime, consumeT.startDate, consumeT.endDate)) {
			this.db.setDayConsumeYuanbao(0);
		}
		SummationActivityView view = new SummationActivityView();
		view.total = db.getDayConsumeYuanbao();
		view.remainSeconds = (int) (DateUtil.compareTime(consumeT.endDate, new Date()) / 1000);
		List<SummationReward> rewards = new ArrayList<SummationReward>();
		for (DayChargeConsumeItemT i : consumeT.items) {
			rewards.add(new SummationReward(i.money, isAlreadyReceived(i.money), new ItemView[] { new ItemView("", //$NON-NLS-1$
					ItemType.DefaultItemType, i.itemId, 1, "") }));
		}
		view.configs = rewards.toArray(new SummationReward[0]);
		return view;
	}

	@Override
	public void receiveDayConsume(int threshold) throws NoteException {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.DayConsume)) {
			throw new NoteException(Messages.getString("DayConsumeControler.2")); //$NON-NLS-1$
		}
		if (this.getDayConsumeView().total < threshold) {
			throw new NoteException(Messages.getString("DayConsumeControler.3")); //$NON-NLS-1$
		}
		if (isAlreadyReceived(threshold)) {
			throw new NoteException(Messages.getString("DayConsumeControler.4")); //$NON-NLS-1$
		}
		ActivityT at = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.DayConsume);
		DayChargeConsumeT dayT = getCurrentActivity(at);
		DayChargeConsumeItemT itemT = null;
		for (DayChargeConsumeItemT i : dayT.items) {
			if (i.money == threshold) {
				itemT = i;
			}
		}
		if (itemT == null) {
			throw new NoteException(Messages.getString("DayConsumeControler.5")); //$NON-NLS-1$
		}
		// 结算奖励
		this.rt.getRewardControler().acceptReward(itemT.itemId, 1);
		// 记录数据
		RoleDayConsume data = this.db.getDayConsume().get(threshold);
		Date now = Calendar.getInstance().getTime();
		if (data == null) {
			data = new RoleDayConsume(GlobalDataManager.getInstance().generatePrimaryKey(), db, threshold, now);
			this.db.getDayConsume().put(threshold, data);
		} else {
			data.setCreateDate(now);
		}
		gainDayConsumeEvent.onGainDayConsume(itemT.money, itemT.itemId);
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
			DayChargeConsumeT t = XsgActivityManage.getInstance().dayConsumes.get(Integer.parseInt(id));
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
		RoleDayConsume data = this.db.getDayConsume().get(threshold);
		ActivityT at = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.DayConsume);
		DayChargeConsumeT consumeT = getCurrentActivity(at);
		if (consumeT == null) {
			return false;
		}
		return data != null && DateUtil.isBetween(data.getCreateDate(), consumeT.startDate, consumeT.endDate);
	}

	@Override
	public void onYuanbaoChange(int oldBind, int oldUnbind, int newBind, int newUnbind, int changeValue) {
		int change = newBind + newUnbind - oldBind - oldUnbind;
		if (change >= 0// 非消费
				// 不在活动时间内
				|| !XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.DayConsume)) {
			return;
		}
		// 检查是否需要重置计数
		Date lastTime = this.db.getLastConsumeTime();
		if (lastTime == null || !DateUtil.isSameDay(new Date(), lastTime)) {
			this.db.setDayConsumeYuanbao(0);
		}
		// 减去负数来实现增加的目的
		this.db.setDayConsumeYuanbao(this.db.getDayConsumeYuanbao() - change);
		// 刷新记录时间
		this.db.setLastConsumeTime(Calendar.getInstance().getTime());
		
		rt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.DayConsume)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if (rt.getChatControler().getChatCb() == null) {
			return null;
		}

		if (firstOpen && !isOver()) {
			firstOpen = false;
			return new MajorUIRedPointNote(MajorMenu.DayConsumeMenu, true);
		}

		SummationActivityView view = null;
		try {
			view = getDayConsumeView();
		} catch (NoteException e) {
			return null;
		}
		for (SummationReward sr : view.configs) {
			if (view.total >= sr.threshold && !isAlreadyReceived(sr.threshold)) {
				return new MajorUIRedPointNote(MajorMenu.DayConsumeMenu, false);
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
			SummationActivityView view = getDayConsumeView();
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

}
