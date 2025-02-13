package com.morefun.XSanGo.activity;

import java.util.Calendar;
import java.util.Date;

import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SummationActivityView;
import com.XSanGo.Protocol.SummationReward;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleBigActivityForSumConsume;
import com.morefun.XSanGo.event.protocol.ISumConsumeActivity;
import com.morefun.XSanGo.event.protocol.IYuanbaoChange;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;

@RedPoint
public class BigSumConsumeControler implements IBigSumConsumeActivityControler, IYuanbaoChange {

	private IRole rt;
	private Role db;
	private ISumConsumeActivity event;
	// 限时活动登录都显示红点
	private boolean firstOpen = false;

	public BigSumConsumeControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;

		this.event = this.rt.getEventControler().registerEvent(ISumConsumeActivity.class);
		this.rt.getEventControler().registerHandler(IYuanbaoChange.class, this);
		firstOpen = true;
	}

	@Override
	public SummationActivityView getView() {
		// 检查是否需要重置计数
		Date lastTime = this.db.getLastConsumeTime();
		ActivityT at = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.BigSumConsume);
		Date begin = DateUtil.parseDate(at.startTime);
		Date end = DateUtil.parseDate(at.endTime);
		if (lastTime == null || !DateUtil.isBetween(lastTime, begin, end)) {
			this.db.setBigConsumeYuanbao(0);
		}
		SummationActivityView view = XsgActivityManage.getInstance().cloneBigSumConsumeView();
		for (SummationReward reward : view.configs) {
			// 是否已领取
			reward.received = this.isAlreadyReceived(reward.threshold);
		}

		// 计算累计值
		view.total = this.db.getBigConsumeYuanbao();

		return view;

	}

	@Override
	public void receiveReward(int threshold) throws NoteException, NotEnoughYuanBaoException {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.BigSumConsume)) {
			throw new NoteException(Messages.getString("SumConsumeControler.0")); //$NON-NLS-1$
		}
		if (this.getView().total < threshold) {
			throw new NotEnoughYuanBaoException();
		}
		if (this.isAlreadyReceived(threshold)) {
			throw new NoteException(Messages.getString("SumConsumeControler.1")); //$NON-NLS-1$
		}

		SummationActivityComponentT sat = XsgActivityManage.getInstance().getBigSumConsumeTemplate(threshold);
		if (sat == null) {
			throw new NoteException(Messages.getString("SumConsumeControler.2") + threshold); //$NON-NLS-1$
		}

		// 结算奖励
		this.rt.getRewardControler().acceptReward(XsgRewardManager.getInstance().doTc(this.rt, sat.tc));
		// 记录数据
		RoleBigActivityForSumConsume data = this.db.getBigActivityForSumConsume().get(threshold);
		Date now = Calendar.getInstance().getTime();
		if (data == null) {
			data = new RoleBigActivityForSumConsume(GlobalDataManager.getInstance().generatePrimaryKey(), db,
					threshold, now);
			this.db.getBigActivityForSumConsume().put(threshold, data);
		} else {
			data.setLastReceiveTime(now);
		}

		this.event.onReceiveSumReward(threshold);
	}

	private boolean isAlreadyReceived(int threshold) {
		RoleBigActivityForSumConsume data = this.db.getBigActivityForSumConsume().get(threshold);
		ActivityT at = getConsumeActivityTemplate();
		return data != null
				&& DateUtil.isBetween(data.getLastReceiveTime(), DateUtil.parseDate(at.startTime),
						DateUtil.parseDate(at.endTime));
	}

	/**
	 * 获取累计消费活动模板
	 * 
	 * @return
	 */
	private ActivityT getConsumeActivityTemplate() {
		return XsgActivityManage.getInstance().getActivityT(ActivityTemplate.BigSumConsume);
	}

	@Override
	public void onYuanbaoChange(int oldBind, int oldUnbind, int newBind, int newUnbind, int changeValue) {
		int change = newBind + newUnbind - oldBind - oldUnbind;
		if (change >= 0// 非消费
				// 不在活动时间内
				|| !XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.BigSumConsume)) {
			return;
		}

		// 检查是否需要重置计数
		Date lastTime = this.db.getLastConsumeTime();
		ActivityT at = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.BigSumConsume);
		Date begin = DateUtil.parseDate(at.startTime);
		Date end = DateUtil.parseDate(at.endTime);
		if (lastTime == null || !DateUtil.isBetween(lastTime, begin, end)) {
			this.db.setBigConsumeYuanbao(0);
		}

		// 减去负数来实现增加的目的
		this.db.setBigConsumeYuanbao(this.db.getBigConsumeYuanbao() - change);
		// 刷新记录时间
		this.db.setLastConsumeTime(Calendar.getInstance().getTime());
		
		rt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.BigSumConsume)) {
			return null;
		}

		if (firstOpen && !isOver()) {
			firstOpen = false;
			return new MajorUIRedPointNote(MajorMenu.BigSumConsumeMenu, true);
		}

		SummationActivityView view = this.getView();

		for (SummationReward item : view.configs) {
			if (view.total >= item.threshold && !this.isAlreadyReceived(item.threshold)) {
				return new MajorUIRedPointNote(MajorMenu.BigSumConsumeMenu, false);
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
		SummationActivityView view = getView();
		for (SummationReward s : view.configs) {
			if (!s.received) {
				return false;
			}
		}
		return true;
	}
}
