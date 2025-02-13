package com.morefun.XSanGo.sign;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.XSanGo.Protocol.AMD_Sign_inviteCode;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.InviteCode;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleInviteLog;
import com.morefun.XSanGo.db.game.RoleSign;
import com.morefun.XSanGo.db.game.RoleTotalSignGift;
import com.morefun.XSanGo.event.protocol.IAutoResign;
import com.morefun.XSanGo.event.protocol.IRoulette;
import com.morefun.XSanGo.event.protocol.ISign;
import com.morefun.XSanGo.event.protocol.ISignGift;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint(isTimer = true)
public class SignController implements ISignController {

	SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$

	IRole roleRt;
	Role role;
	ISign signEvent;
	IAutoResign autoResignEvent;
	IRoulette rouletteEvent;
	ISignGift giftEvent;

	public SignController(IRole roleRt, Role role) {
		this.roleRt = roleRt;
		this.role = role;
		signEvent = roleRt.getEventControler().registerEvent(ISign.class);
		autoResignEvent = roleRt.getEventControler().registerEvent(IAutoResign.class);
		rouletteEvent = roleRt.getEventControler().registerEvent(IRoulette.class);
		giftEvent = roleRt.getEventControler().registerEvent(ISignGift.class);

		clearDataBeforeTwoMonths();
	}

	/**
	 * 清除2个月以前的数据
	 */
	private void clearDataBeforeTwoMonths() {
		// 签到数据
		for (String k : new ArrayList<String>(role.getSign().keySet())) {
			try {
				Calendar c = Calendar.getInstance();
				c.setTime(yyyyMMdd.parse(role.getSign().get(k).getDate()));
				if (DateUtil.addDays(c, 60).before(Calendar.getInstance())) {
					role.getSign().remove(k);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 累计领奖的数据
		for (RoleTotalSignGift gift : new ArrayList<RoleTotalSignGift>(role.getTotalSignGift())) {
			if (Calendar.getInstance().get(Calendar.MONTH) - gift.getMonth() > 2) {
				role.getTotalSignGift().remove(role.getTotalSignGift());
			}
		}
	}

	/**
	 * @param t
	 * @return 可签到的次数，有vip限制是2次，否则就只有1次
	 */
	int signTimesLimit(SignT t) {
		if (t == null) {
			return 1;
		}
		return t.vipLimit != 0 ? 2 : 1;
	}

	/**
	 * @param day
	 *            指定日期(当月的几号)
	 * @return 当天的签到记录
	 */
	private RoleSign signOfDay(int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, day);
		return signOfDay(yyyyMMdd.format(c.getTime()));
	}

	/**
	 * @param day
	 *            指定日期 yyyyMMdd格式
	 * @return 当天的签到记录
	 */
	private RoleSign signOfDay(String yyyyMMddDateStr) {
		for (RoleSign sign : role.getSign().values()) {
			if (sign.getDate().equals(yyyyMMddDateStr)) {
				return sign;
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.morefun.XSanGo.sign.ISignController#signCost(int) 补签半价
	 */
	@Override
	public int signCost(int day) {
		SignT signT = XsgSignManager.getInstance().getSignT(day);
		ItemView[] items = XsgRewardManager.getInstance().doTcToItem(this.roleRt, signT.tc);

		int i = 0;

		if (signT.resignCost > 0) {
			i = signT.resignCost;
		} else {
			for (ItemView v : items) {
				float cost = XsgItemManager.getInstance().getYuanbaoPrice(v.templateId);
				cost *= v.num;
				if (day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
					cost /= 2;
				} else {
					cost = 0;
				}
				i += (int) cost;
			}
		}
		return i;
	}

	@Override
	public void signIn(int day) throws Exception {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, day);
		String date = yyyyMMdd.format(c.getTime());
		RoleSign sign = signOfDay(day);
		if (sign == null) {
			sign = new RoleSign(GlobalDataManager.getInstance().generatePrimaryKey(), role, date, 0, 0);
			role.getSign().put(sign.getId(), sign);
		}
		SignT t = XsgSignManager.getInstance().getSignT(day);

		if (!canSignIn(day)) {
			throw new Exception(Messages.getString("SignController.1")); //$NON-NLS-1$
		}
		// 补签
		if (day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
			// 只有月卡用户才能补签
			if (!roleRt.getVipController().hasMonthCard()) {
				throw new Exception(Messages.getString("SignController.2")); //$NON-NLS-1$
			}
			roleRt.winYuanbao(-signCost(day), true);
			sign.setResignFlag(1);
		}
		int remaingSignTimes = remainingTimes(day, t);
		for (int j = 0; j < remaingSignTimes; j++) {
			int i = sign.getSignTimes() + 1;
			sign.setSignTimes(i);
			TcResult tc = XsgRewardManager.getInstance().doTc(this.roleRt, t.tc);
			this.roleRt.getRewardControler().acceptReward(tc);
		}
		signEvent.sign(day);
	}

	/**
	 * @param day
	 * @return 当天是否可签到
	 */
	public boolean canSignIn(int day) {
		RoleSign sign = signOfDay(day);
		if (sign == null) {
			return true;
		}
		SignT signT = XsgSignManager.getInstance().getSignT(day);
		// 当天的签到，vip等级满足，未签满
		if (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && roleRt.getVipLevel() >= signT.vipLimit
				&& sign.getSignTimes() < signTimesLimit(signT)) {
			return true;
		}
		// 当天vip等级不满足且没签到过
		if (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && roleRt.getVipLevel() < signT.vipLimit
				&& sign.getSignTimes() == 0) {
			return true;
		}
		// 补签只能前1次
		if (day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && sign.getSignTimes() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canAutoResign() {
		for (int i = 1; i < Calendar.getInstance().get(Calendar.DAY_OF_MONTH); i++) {
			if (canSignIn(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param day
	 *            查询的日期
	 * @param t
	 *            策划的配置
	 * @return 当天签到可在vip升级后再判断是否再追加机会补足双倍奖励<br/>
	 *         补签只能根据操作时候的vip等级(判断是否双倍)补一次,vip升级后不会再追加机会
	 */
	private int remainingTimes(int day, SignT t) {
		int remaingSignTimes = 0;
		if (roleRt.getVipLevel() < t.vipLimit) {
			if (getSignTimes(day) == 0) {
				remaingSignTimes = 1;
			} else {
				remaingSignTimes = 0;
			}
		} else {
			if (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
				remaingSignTimes = signTimesLimit(t) - getSignTimes(day);
			} else {
				if (getSignTimes(day) == 0) {
					remaingSignTimes = signTimesLimit(t);
				} else {
					remaingSignTimes = 0;
				}
			}
		}
		return remaingSignTimes;
	}

	@Override
	public int autoResign() throws NoteException, NotEnoughYuanBaoException {
		final int NORMAL = 0; // 正常返回
		final int NEED_MONTHCARD = 1; // 需要购买月卡
		// 只有月卡用户才能一键补签
		if (!roleRt.getVipController().hasMonthCard()) {
			return NEED_MONTHCARD;
		}
		int cost = 0;
		for (int day = 1; day < Calendar.getInstance().get(Calendar.DAY_OF_MONTH); day++) {
			if (canSignIn(day)) {
				cost += signCost(day);
			}
		}
		if (roleRt.getTotalYuanbao() < cost) {
			throw new NotEnoughYuanBaoException();
		}
		List<Integer> resignedDay = new ArrayList<Integer>();
		for (int day = 1; day < Calendar.getInstance().get(Calendar.DAY_OF_MONTH); day++) {
			if (!canSignIn(day)) {
				continue;
			}
			RoleSign sign = signOfDay(day);
			if (sign == null) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_MONTH, day);
				String date = yyyyMMdd.format(c.getTime());
				sign = new RoleSign(GlobalDataManager.getInstance().generatePrimaryKey(), role, date, 0, 0);
				role.getSign().put(sign.getId(), sign);
			}
			SignT t = XsgSignManager.getInstance().getSignT(day);
			int remaingSignTimes = remainingTimes(day, t);
			for (int j = 0; j < remaingSignTimes; j++) {
				sign.setSignTimes(sign.getSignTimes() + 1);
				TcResult tc = XsgRewardManager.getInstance().doTc(this.roleRt, t.tc);
				this.roleRt.getRewardControler().acceptReward(tc);
			}
			if (day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
				roleRt.winYuanbao(-signCost(day), true);
				sign.setResignFlag(1);
			}
			resignedDay.add(day);
		}
		autoResignEvent.resign(resignedDay.toArray(new Integer[resignedDay.size()]), cost);
		clearDataBeforeTwoMonths();

		return NORMAL;
	}

	/**
	 * @param timesId
	 *            奖励对应的次数, 使用包装类型是为了方便下面的equals判断，因为equals里面的值可能为空
	 * @return 当月是否已经领过奖励
	 */
	private boolean alreadyPerformed(Integer timesId) {
		for (RoleTotalSignGift totalSignGift : role.getTotalSignGift()) {
			if (totalSignGift.getMonth() == (Calendar.getInstance().get(Calendar.MONTH) + 1) // 数据库中以自然月份存储
					&& timesId.equals(XsgSignManager.getInstance().getSignGiftPacksT().get(totalSignGift.getGiftId()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param times
	 *            奖励对应的次数
	 * @return 奖励的tcid
	 */
	private String getTcidByTime(int times) {
		TotalSignGiftPacksT item = XsgSignManager.getInstance().getSignGiftPacksTBySignCount(times);

		return item != null ? item.gift : null;
	}

	@Override
	public void collectGiftPack(int count) {
		if (XsgSignManager.getInstance().getSignGiftPacksTBySignCount(count) == null) {
			return;
		}
		if (alreadyPerformed(count)) {
			return;
		}

		String tcid = getTcidByTime(count);
		role.getTotalSignGift().add(
				new RoleTotalSignGift(GlobalDataManager.getInstance().generatePrimaryKey(), role, tcid, Calendar
						.getInstance().get(Calendar.MONTH) + 1)); // 数据库中以自然月份存储

		TcResult tc = XsgRewardManager.getInstance().doTc(this.roleRt, tcid);
		this.roleRt.getRewardControler().acceptReward(tc);
		giftEvent.onGift(tcid, count, XsgRewardManager.getInstance().generateItemView(tc));
	}

	/**
	 * 返回物品ID和数量
	 */
	@Override
	public String[] roulette() throws NotEnoughMoneyException {
		if (remainRouletteTimes() <= 0) {
			return null;
		}
		RoleSign sign = signOfDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		if (sign == null) {
			sign = new RoleSign(GlobalDataManager.getInstance().generatePrimaryKey(), role,
					yyyyMMdd.format(new Date()), 0, 0);
			role.getSign().put(sign.getId(), sign);
		}
		RandomRoulette rouletteItem = rouletteByTc();

		IItem item = roleRt.getRewardControler().acceptReward(rouletteItem.getTemplateId(), rouletteItem.getCount());
		rouletteItem.setItem(item);// 设置item实例对象到RandomRoulette中
		roleRt.winJinbi(-rouletteItem.getCost());

		sign.setRouletteTimes(sign.getRouletteTimes() + 1);
		rouletteEvent.onRoulette(sign.getRouletteTimes(), rouletteItem);
		return new String[] { rouletteItem.getTemplateId(), String.valueOf(rouletteItem.count) };
	}

	int getSignCountForTc() {
		return role.getSignCountForTc();
	}

	/**
	 * 重置伪随机的计数器
	 */
	void resetSignCountForTc() {
		role.setSignCountForTc(0);
	}

	/**
	 * 伪随机的计数器+1
	 */
	void increaseSignCountForTc() {
		int i = role.getSignCountForTc() + 1;
		role.setSignCountForTc(i);
	}

	/**
	 * @return 伪随机算法掉落道具
	 */
	RandomRoulette rouletteByTc() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		PseudoRandomRange pseduoRandom = XsgSignManager.getInstance().ranges.get(day);
		RandomRoulette item = pseduoRandom.random(this);
		return item;
	}

	void broadcastRoulette(RandomRoulette item) {
		String tid = item.templateId;
		// 判断抽到的奖品类型，金币/经验/元宝/竞技币 不广播
		IItem broadcastItem = item.getItem();
		if (item.broadcast && broadcastItem != null && !Const.PropertyName.EXP.equals(tid)
				&& !Const.PropertyName.MONEY.equals(tid) && !Const.PropertyName.RMBY.equals(tid)
				&& !Const.PropertyName.ORDER.equals(tid)) {
			XsgChatManager chatManager = XsgChatManager.getInstance();
			// 2 为公告配置（聊天公告走马灯.xls-走马灯）中名将召唤的类型id
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.drawCard);
			if (adTList != null && adTList.size() > 0) {
				ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
					chatManager.sendAnnouncement(this.roleRt.getChatControler().parseAdContentItem(
							broadcastItem.getTemplate(), chatAdT.content));
				}
			}
		}
	}

	public int remainRouletteTimes() {
		String date = yyyyMMdd.format(new Date());
		RoleSign sign = signOfDay(date);
		if (sign == null) {
			return XsgSignManager.getInstance().todaySignRouletteT().lottery_times;
		}
		return XsgSignManager.getInstance().todaySignRouletteT().lottery_times - sign.getRouletteTimes();
	}

	@Override
	public int getSignTimes(int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, day);

		RoleSign sign = signOfDay(day);
		if (sign != null) {
			return sign.getSignTimes();
		}
		return 0;
	}

	@Override
	public byte getTotalSignTimes() {
		int times = 0;
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM"); //$NON-NLS-1$
		String str = yyyyMM.format(new Date());
		for (RoleSign sign : role.getSign().values()) {
			if (str.equals(sign.getDate().substring(0, sign.getDate().length() - 2)) && sign.getSignTimes() != 0) {
				times++;
			}
		}
		return (byte) times;
	}

	@Override
	public boolean allreadyCollectGift(String id) {
		if (id == null) {
			return false;
		}
		for (RoleTotalSignGift record : role.getTotalSignGift()) {
			if (record.getMonth() == (Calendar.getInstance().get(Calendar.MONTH) + 1) // 数据库中以自然月份存储
					&& id.equals(record.getGiftId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		return canSignIn(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) ? new MajorUIRedPointNote(
				MajorMenu.SignMenu, true) : null;
	}

	RandomRoulette lastRouletteItem;

	@Override
	public void broadcastLastRoulette() {
		if (lastRouletteItem != null) {
			broadcastRoulette(lastRouletteItem);
			lastRouletteItem = null;
		}
	}

	@Override
	public void inviteCode(String inviteCode, final AMD_Sign_inviteCode __cb) {
		if (!TextUtil.isBlank(inviteCode)) {// 验证邀请码是否正确
			final InviteCode invite = XsgActivityManage.getInstance().getInviteCodeByCode(inviteCode.toUpperCase());
			if (invite == null) {
				__cb.ice_exception(new NoteException(Messages.getString("RoleI.21"))); //$NON-NLS-1$
				return;
			}
			if (role.getInvitedNum() > 0) {
				__cb.ice_exception(new NoteException(Messages.getString("RoleI.InvitedAlready")));
				return;
			}
			XsgRoleManager.getInstance().loadRoleByIdAsync(invite.getUseRoleId(), new Runnable() {
				@Override
				public void run() {
					// 邀请者
					IRole inviteRole = XsgRoleManager.getInstance().findRoleById(invite.getUseRoleId());
					// 不能自己邀请自己
					if (inviteRole != null && inviteRole.getRoleId().equals(roleRt.getRoleId())) {
						__cb.ice_exception(new NoteException(Messages.getString("RoleI.CannotInviteSelf")));
						return;
					}
					// 添加邀请记录
					inviteRole.addInviteLog(new RoleInviteLog(GlobalDataManager.getInstance().generatePrimaryKey(),
							null, roleRt.getDeviceId(), new Date(), roleRt.getRoleId()));
					// 发送邮件
					Mail mail = new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", //$NON-NLS-1$
							Messages.getString("RoleI.23"), //$NON-NLS-1$
							inviteRole.getRoleId(), Messages.getString("RoleI.24"), //$NON-NLS-1$
							TextUtil.format(Messages.getString("RoleI.25"), //$NON-NLS-1$
									roleRt.getName()), "", Calendar //$NON-NLS-1$
									.getInstance().getTime());
					XsgMailManager.getInstance().sendMail(mail);
					// 被邀请者发送好友请求给邀请人
					try {
						roleRt.getSnsController().applyForFriend(inviteRole.getRoleId());
					} catch (NoteException e) {
						e.printStackTrace();
					}
					// 被邀请者得到邀请奖励
					TcResult tcResult = XsgRewardManager.getInstance().doTc(roleRt,
							XsgActivityManage.getInstance().inviteConf.tc);
					roleRt.getRewardControler().acceptReward(tcResult);
					// 设置被邀请次数
					role.setInvitedNum(role.getInvitedNum() + 1); 
					inviteRole.saveAsyn();
					__cb.ice_response(Messages.getString("RoleI.InviteInputSuccess"));
				}

			}, new Runnable() {

				@Override
				public void run() {
					__cb.ice_exception(new NoteException(Messages.getString("RoleI.27"))); //$NON-NLS-1$
					return;
				}
			});
		} else {
			__cb.ice_exception(new NoteException(Messages.getString("RoleI.21")));
		}
	}

}
