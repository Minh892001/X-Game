/**
 * 
 */
package com.morefun.XSanGo.onlineAward;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OnlineAwardView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleOnline;
import com.morefun.XSanGo.event.protocol.IOffline;
import com.morefun.XSanGo.event.protocol.IOnlineAward;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 在线礼包
 * 
 * @author 吕明涛
 * 
 */
class OnlineAwardControler implements IOnlineAwardControler, IOffline {

	// private static final Log log =
	// LogFactory.getLog(OnlineAwardControler.class);

	private IRole roleRt;
	private Role roleDb;

	private IOnlineAward eventOnlineAward; // 在线礼包 事件

	public OnlineAwardControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		// 注册角色，下线事件
		roleRt.getEventControler().registerHandler(IOffline.class, this);

		this.eventOnlineAward = this.roleRt.getEventControler().registerEvent(
				IOnlineAward.class);
	}

	// 根据等级,取得在线礼包数值 模板数值
	private List<OnlineAwardT> selectLevelOnlineAwardT() {
		TreeMap<Integer, List<OnlineAwardT>> OnlineAwardTMap = XsgOnlineAwardManager
				.getInstance().getOnlineAwardLevelMap();
		List<OnlineAwardT> onlineAwardTList = null;
		for (int level : OnlineAwardTMap.keySet()) {
			if (this.roleRt.getLevel() >= level) {
				onlineAwardTList = OnlineAwardTMap.get(level);
			} else {
				break;
			}
		}

		return onlineAwardTList;
	}

	// 返回在线礼包显示
	private OnlineAwardView resView(int id, long resReqTime, String ItemViewArr) {
		return new OnlineAwardView(id, resReqTime, TextUtil.GSON.fromJson(
				ItemViewArr, ItemView[].class));
	}

	// 返回礼包中TC的道具数据
	private ItemView[] getAwardTc(String giftTc) {
		// 附件TC中的随机数据,获得奖励物品
		return XsgRewardManager.getInstance().doTcToItem(this.roleRt, giftTc);
	}

	// 计算领取礼包剩余时间
	private long remainTime(long starDate, int ReqTime) {
		long remainTime = (starDate / 1000 + ReqTime)
				- System.currentTimeMillis() / 1000;
		if (remainTime < 0)
			remainTime = 0;

		return remainTime;
	}

	// 计算领取礼包 到第二天凌晨，剩余时间
	private long remainNextDateTime() {
		long remainTime = DateUtil.addDays(DateUtil.getFirstSecondOfToday(), 1)
				.getTimeInMillis() - System.currentTimeMillis();

		return remainTime / 1000;
	}

	/**
	 * 查询在线时间
	 */
	@Override
	public OnlineAwardView selectOnlineTime() throws NoteException {
		// 剩余时间，单位：秒
		long resReqTime = 0;

		// 根据等级,取得在线礼包数值
		List<OnlineAwardT> onlineAwardTList = this.selectLevelOnlineAwardT();

		RoleOnline onlineAward = this.roleDb.getOnlineAward();
		if (onlineAward != null) {
			// 同一天重置一下开始时间
			// 不是同一天，重置在线礼包
			if (DateUtil.isSameDay(new Date(), onlineAward.getStarDate())) {
				// 计算领取在线礼包剩余时间
				if (onlineAward.getRewardNum() <= this
						.selectLevelOnlineAwardT().size()) {
					resReqTime = this.remainTime(onlineAward.getStarDate()
							.getTime(), onlineAward.getReqTime());
				} else {
					onlineAward.setOnlineGiftId(0);
					onlineAward.setReqTime((int) this.remainNextDateTime());
					onlineAward.setItemView(null);

					resReqTime = onlineAward.getReqTime();
				}

				// 当天在线礼包 有未领取的
			} else {
				// 重置从第一条数据开始
				OnlineAwardT awardT = onlineAwardTList.get(0);

				onlineAward.setOnlineGiftId(awardT.GiftId);
				onlineAward.setStarDate(new Date());
				onlineAward.setRewardNum(1);
				onlineAward.setReqTime(awardT.ReqTime);
				onlineAward.setItemView(TextUtil.GSON.toJson(this
						.getAwardTc(awardT.GiftTC)));

				resReqTime = onlineAward.getReqTime();
			}

		} else {
			OnlineAwardT awardT = onlineAwardTList.get(0);
			onlineAward = new RoleOnline(GlobalDataManager.getInstance()
					.generatePrimaryKey(), this.roleDb, awardT.GiftId,
					new Date(), awardT.ReqTime, 0, TextUtil.GSON.toJson(this
							.getAwardTc(awardT.GiftTC)));
			resReqTime = onlineAward.getReqTime();
		}

		this.roleDb.setOnlineAward(onlineAward);

		return this.resView(onlineAward.getOnlineGiftId(), resReqTime,
				onlineAward.getItemView());
	}

	/**
	 * 领取在线奖励
	 */
	@Override
	public OnlineAwardView getAward() throws NoteException {

		RoleOnline onlineAward = this.roleDb.getOnlineAward();
		if (onlineAward != null) {
			OnlineAwardT awardT = XsgOnlineAwardManager.getInstance()
					.getOnlineAward(onlineAward.getOnlineGiftId());

			if (awardT != null) {
				// 计算领取时间是否已到
				if (this.remainTime(onlineAward.getStarDate().getTime(),
						onlineAward.getReqTime()) == 0) {
					//
					int giftId = onlineAward.getOnlineGiftId();

					// 获得奖励物品
					ItemView[] rewardItemView = TextUtil.GSON.fromJson(
							onlineAward.getItemView(), ItemView[].class);
					this.roleRt.getRewardControler().acceptReward(
							rewardItemView);

					// 开始下一个在线礼包计时,当天的在线礼包全部领取后，返回空数据
					List<OnlineAwardT> levelOnlineAwardList = this
							.selectLevelOnlineAwardT();
					if (onlineAward.getRewardNum() < levelOnlineAwardList
							.size()) {

						OnlineAwardT nextAward = levelOnlineAwardList
								.get(onlineAward.getRewardNum());

						onlineAward.setOnlineGiftId(nextAward.GiftId);
						onlineAward.setStarDate(new Date());
						onlineAward.setReqTime(nextAward.ReqTime);
						onlineAward.setItemView(TextUtil.GSON.toJson(this
								.getAwardTc(nextAward.GiftTC)));

					} else {
						onlineAward.setOnlineGiftId(0);
						onlineAward.setReqTime((int) this.remainNextDateTime());
						onlineAward.setItemView(null);
					}

					// 领取次数 增加，判断当天是否已经全部领取
					onlineAward.setRewardNum(onlineAward.getRewardNum() + 1);
					this.roleDb.setOnlineAward(onlineAward);

					// 获得在线礼包事件
					try {
						eventOnlineAward.onOnlineAward(giftId, rewardItemView);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					throw new NoteException(Messages.getString("OnlineAwardControler.0")); //$NON-NLS-1$
				}

			} else {
				throw new NoteException(Messages.getString("OnlineAwardControler.1")); //$NON-NLS-1$
			}
		} else {
			throw new NoteException(Messages.getString("OnlineAwardControler.2")); //$NON-NLS-1$
		}

		return this.resView(onlineAward.getOnlineGiftId(),
				onlineAward.getReqTime(), onlineAward.getItemView());
	}

	/**
	 * 登录后调用，在线礼包时间 重置
	 */
	@Override
	public void afterEnterGame() {
		RoleOnline onlineAward = this.roleDb.getOnlineAward();
		if (onlineAward != null) {
			OnlineAwardT awardT = XsgOnlineAwardManager.getInstance()
					.getOnlineAward(onlineAward.getOnlineGiftId());
			if (awardT != null) {
				// 当天在线礼包 有未领取的
				if (DateUtil.isSameDay(new Date(), onlineAward.getStarDate())
						&& onlineAward.getRewardNum() <= this
								.selectLevelOnlineAwardT().size()) {
					onlineAward.setStarDate(new Date());
					this.roleDb.setOnlineAward(onlineAward);
				}
			}
		}
	}

	/**
	 * 下线记录在线时长
	 * 
	 * @throws Exception
	 */
	@Override
	public void onRoleOffline(long onlineInterval) {
		RoleOnline onlineAward = this.roleDb.getOnlineAward();
		if (onlineAward != null) {
			OnlineAwardT awardT = XsgOnlineAwardManager.getInstance()
					.getOnlineAward(onlineAward.getOnlineGiftId());
			if (awardT != null) {
				// 当天在线礼包 有未领取的
				if (onlineAward.getRewardNum() <= this
						.selectLevelOnlineAwardT().size()) {
					long remainTime = this.remainTime(onlineAward.getStarDate()
							.getTime(), onlineAward.getReqTime());
					onlineAward.setReqTime((int) remainTime);
					this.roleDb.setOnlineAward(onlineAward);
				}
			}
		}
	}
}
