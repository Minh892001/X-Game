package com.morefun.XSanGo.activity;

import java.util.Calendar;
import java.util.Date;

import com.XSanGo.Protocol.CustomChargeParams;
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
import com.morefun.XSanGo.db.game.RoleBigActivityForSumCharge;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.ISumChargeActivity;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;

@RedPoint
public class BigSumChargeControler implements IBigSumChargeActivityControler, ICharge {
	private Role db;
	private IRole rt;
	private ISumChargeActivity event;
	// 限时活动登录都显示红点
	private boolean firstOpen = false;

	public BigSumChargeControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;
		this.event = this.rt.getEventControler().registerEvent(ISumChargeActivity.class);
		firstOpen = true;
		
		rt.getEventControler().registerHandler(ICharge.class, this);
	}

	@Override
	public SummationActivityView getView() {
		SummationActivityView view = XsgActivityManage.getInstance().cloneBigSumChargeView();
		for (SummationReward reward : view.configs) {
			// 是否已领取
			reward.received = this.isAlreadyReceived(reward.threshold);
		}

		// 计算累计值
		ActivityT at = getChargeActivityTemplate();
		Date begin = DateUtil.parseDate(at.startTime);
		Date end = DateUtil.parseDate(at.endTime);
		view.total = this.rt.getVipController().getChargeYuanbao(begin, end);

		return view;
	}

	private boolean isAlreadyReceived(int threshold) {
		RoleBigActivityForSumCharge data = this.db.getBigActivityForSumCharge().get(threshold);
		ActivityT at = getChargeActivityTemplate();
		return data != null
				&& DateUtil.isBetween(data.getLastReceiveTime(), DateUtil.parseDate(at.startTime),
						DateUtil.parseDate(at.endTime));
	}

	/**
	 * 获取累计充值活动模板
	 * 
	 * @return
	 */
	private ActivityT getChargeActivityTemplate() {
		return XsgActivityManage.getInstance().getActivityT(ActivityTemplate.BigSumCharge);
	}

	@Override
	public void receiveReward(int threshold) throws NotEnoughYuanBaoException, NoteException {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.BigSumCharge)) {
			throw new NoteException(Messages.getString("SumChargeControler.0")); //$NON-NLS-1$
		}
		if (this.getView().total < threshold) {
			throw new NotEnoughYuanBaoException();
		}
		if (this.isAlreadyReceived(threshold)) {
			throw new NoteException(Messages.getString("SumChargeControler.1")); //$NON-NLS-1$
		}

		SummationActivityComponentT sat = XsgActivityManage.getInstance().getBigSumChargeTemplate(threshold);
		if (sat == null) {
			throw new NoteException(Messages.getString("SumChargeControler.2") + threshold); //$NON-NLS-1$
		}

		// 结算奖励
		this.rt.getRewardControler().acceptReward(XsgRewardManager.getInstance().doTc(this.rt, sat.tc));
		// 记录数据
		RoleBigActivityForSumCharge data = this.db.getBigActivityForSumCharge().get(threshold);
		Date now = Calendar.getInstance().getTime();
		if (data == null) {
			data = new RoleBigActivityForSumCharge(GlobalDataManager.getInstance().generatePrimaryKey(), db, threshold,
					now);
			this.db.getBigActivityForSumCharge().put(threshold, data);
		} else {
			data.setLastReceiveTime(now);
		}

		this.event.onReceiveSumReward(threshold);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.BigSumCharge)) {
			return null;
		}

		if (firstOpen && !isOver()) {
			firstOpen = false;
			return new MajorUIRedPointNote(MajorMenu.BigSumChargeMenu, true);
		}

		SummationActivityView view = this.getView();

		for (SummationReward item : view.configs) {
			if (view.total >= item.threshold && !this.isAlreadyReceived(item.threshold)) {
				return new MajorUIRedPointNote(MajorMenu.BigSumChargeMenu, false);
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

	@Override
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		rt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}
}
