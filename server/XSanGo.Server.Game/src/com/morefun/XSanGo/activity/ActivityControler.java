package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.XSanGo.Protocol.ActivityInfoView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.UpActivityInfoView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleUpGift;
import com.morefun.XSanGo.event.protocol.ILevelGift;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint
public class ActivityControler implements IActivityControler, IRoleLevelup {
	private IRole rt;
	private Role db;
	private ILevelGift levelGiftEvent;

	public ActivityControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;
		levelGiftEvent = this.rt.getEventControler().registerEvent(ILevelGift.class);
		
		rt.getEventControler().registerHandler(IRoleLevelup.class, this);
	}

	@Override
	public UpActivityInfoView[] getUpActivityList() {
		UpActivityInfoView[] upViews = XsgActivityManage.getInstance().getUpActivityInfoList();
		// 判断是否已领取
		for (UpActivityInfoView upv : upViews) {
			for (RoleUpGift up : this.db.getRoleUpGifts()) {
				if (up.getInfoId() == upv.id) {
					upv.draw = true;// 已领取
				}
			}
		}
		return upViews;
	}

	@Override
	public boolean getGift(int giftId) {
		UpGiftT upGiftT = XsgActivityManage.getInstance().getUpGiftTById(giftId);
		if (upGiftT == null) {
			return false;
		}
		if (this.db.getLevel() < upGiftT.level) {// 未达到等级
			return false;
		}
		for (RoleUpGift ra : this.db.getRoleUpGifts()) {
			if (ra.getInfoId() == giftId) {// 已经领取过了
				return false;
			}
		}
		RoleUpGift roleUp = new RoleUpGift(GlobalDataManager.getInstance().generatePrimaryKey(), this.db, giftId,
				new Date());
		this.db.getRoleUpGifts().add(roleUp);
		try {
			this.rt.winYuanbao(upGiftT.yuanbao, true);
		} catch (NotEnoughYuanBaoException e) {
			LogManager.error(e);
		}
		// if (this.rt.getVipLevel() < upGiftT.vip) {
		// this.rt.getVipController().setVipLevel(upGiftT.vip);
		// }
		// 增加VIP经验
		this.rt.getVipController().addExperience(upGiftT.vipExp);
		try {
			levelGiftEvent.onGetGift(upGiftT);
		} catch (Exception e) {
			LogManager.error(e);
		}
		return true;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		// 添加升级奖励红点提示
		int count = 0;
		for (UpGiftT up : XsgActivityManage.getInstance().getUpgiftList()) {
			if (this.db.getLevel() >= up.level) {
				count++;
			}
		}
		if (count > this.db.getRoleUpGifts().size()) {
			return new MajorUIRedPointNote(MajorMenu.Activity, false);
		}
		return null;
	}

	@Override
	public boolean isGetGift(int giftId) {
		for (RoleUpGift r : this.db.getRoleUpGifts()) {
			if (r.getInfoId() == giftId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ActivityInfoView[] getActivityList() {
		List<ActivityInfoView> listView = XsgActivityManage.getInstance().getActivityInfoList();

		// 关闭的活动放到最后
		List<ActivityInfoView> closeList = new ArrayList<ActivityInfoView>();
		// 领完消失的
		List<ActivityInfoView> fadeList = new ArrayList<ActivityInfoView>();
		for (ActivityInfoView av : listView) {
			if (isActivityOver(av.id) || av.type == XsgActivityManage.CLOSEED) {
				closeList.add(av);
			}
			if (isActivityFade(av.id)) {
				fadeList.add(av);
			}
		}
		listView.removeAll(closeList);
		listView.addAll(closeList);
		listView.removeAll(fadeList);

		// 渠道过滤
		Iterator<ActivityInfoView> it = listView.iterator();
		while (it.hasNext()) {
			ActivityT activityT = XsgActivityManage.getInstance().getActivityT(it.next().id);
			List<String> channels = TextUtil.stringToList(activityT.channelIds);
			if (!channels.isEmpty() && !channels.contains(String.valueOf(db.getRegChannel()))) {
				it.remove();
			}
		}

		return listView.toArray(new ActivityInfoView[0]);
	}

	/**
	 * 活动奖励是否置尾
	 * 
	 * @return
	 */
	public boolean isActivityOver(int id) {
		if (id == ActivityTemplate.RoleLevelUpReward.getValue()) {
			return this.db.getRoleUpGifts().size() >= XsgActivityManage.getInstance().getUpgiftList().size();
		} else if (id == ActivityTemplate.InviteFriend.getValue()) {
			return this.db.getInviteActivity().size() >= XsgActivityManage.getInstance().inviteActivitys.size();
		} else if (id == ActivityTemplate.VipGift.getValue()) {
			return this.db.getVip().getVipGiftPacks().size() >= 15;
		} else if (id == ActivityTemplate.ChongjiyouJiang.getValue()) {
			return rt.getLevelRewardControler().hasAcceptAll();
		} else if (id == ActivityTemplate.ZuiqingZhanli.getValue()) {
			return rt.getPowerRewardControler().hasAcceptAll();
		} else if (id == ActivityTemplate.Fund.getValue()) {
			return rt.getFundControler().hasAcceptAll();
		} else if (id == ActivityTemplate.DayCharge.getValue()) {
			return rt.getDayChargeControler().isOver();
		} else if (id == ActivityTemplate.DayConsume.getValue()) {
			return rt.getDayConsumeControler().isOver();
		} else if (id == ActivityTemplate.BigDayCharge.getValue()) {
			return rt.getBigDayChargeControler().isOver();
		} else if (id == ActivityTemplate.FirstJia.getValue()) {
			return rt.getFirstJiaControler().hasAcceptAll();
		} else if (id == ActivityTemplate.LeijiLogin.getValue()) {
			return rt.getDayLoginControler().hasAcceptAll();
		} else if (id == ActivityTemplate.ForverLeijiLogin.getValue()) {
			return rt.getDayforverLoginControler().hasAcceptAll();
		} else if (id == ActivityTemplate.SendJunLing.getValue()) {
			return rt.getSendJunLingControler().hasAcceptAll();
		} else if (id == ActivityTemplate.BigDayConsume.getValue()) {
			return rt.getBigDayConsumeControler().isOver();
		} else if (id == ActivityTemplate.SumCharge.getValue()) {
			return rt.getSumChargeActivityControler().isOver();
		} else if (id == ActivityTemplate.SumConsume.getValue()) {
			return rt.getSumConsumeActivityControler().isOver();
		} else if (id == ActivityTemplate.BigSumCharge.getValue()) {
			return rt.getBigSumChargeActivityControler().isOver();
		} else if (id == ActivityTemplate.BigSumConsume.getValue()) {
			return rt.getBigSumConsumeActivityControler().isOver();
		}
		return false;
	}

	/**
	 * 活动奖励是否消失
	 * 
	 * @return
	 */
	public boolean isActivityFade(int id) {
		if (id == ActivityTemplate.RoleLevelUpReward.getValue()) {
			return this.db.getRoleUpGifts().size() >= XsgActivityManage.getInstance().getUpgiftList().size();
		} else if (id == ActivityTemplate.ChongjiyouJiang.getValue()) {
			return rt.getLevelRewardControler().hasAcceptAll();
		} else if (id == ActivityTemplate.ZuiqingZhanli.getValue()) {
			return rt.getPowerRewardControler().hasAcceptAll();
		} else if (id == ActivityTemplate.Fund.getValue()) {
			return rt.getFundControler().hasAcceptAll();
//		} else if (id == ActivityTemplate.LeijiLogin.getValue()) {
//			return rt.getDayLoginControler().hasAcceptAll();
		} else if (id == ActivityTemplate.ForverLeijiLogin.getValue()) {
			return rt.getDayforverLoginControler().hasAcceptAll();
		}
		return false;
	}

	@Override
	public boolean isOpenApi(ActivityT at) {
		if (at.type == XsgActivityManage.CLOSEED) {// 活动关闭
			return false;
		}
		if (isActivityFade(at.id)) {// 可消失的活动
			return false;
		}
		List<String> channels = TextUtil.stringToList(at.channelIds);
		if (!channels.isEmpty() && !channels.contains(String.valueOf(db.getRegChannel()))) {// 渠道条件不符合
			return false;
		}
		if (at.type == XsgActivityManage.TIME_LIMIT || at.type == XsgActivityManage.OPEN_AFTER) {
			return DateUtil.isBetween(DateUtil.parseDate(at.startTime), DateUtil.parseDate(at.endTime));
		}
		return false;
	}

	@Override
	public void onRoleLevelup() {
		rt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}
}
