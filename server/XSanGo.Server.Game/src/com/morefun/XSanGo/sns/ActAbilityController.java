package com.morefun.XSanGo.sns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleVitReceiving;
import com.morefun.XSanGo.db.game.RoleVitSending;
import com.morefun.XSanGo.event.protocol.IAddVitFromSns;
import com.morefun.XSanGo.event.protocol.ISendVitFromSns;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class ActAbilityController implements IActAbilityController {

	IRole roleRt;
	Role role;
	ISendVitFromSns sendEvent;
	IAddVitFromSns addEvent;

	int RECEIVE_LIMIT_MAX = 80;
	int SEND_LIMIT_MAX = 80;

	ActAbilityController(IRole roleRt, Role role) {
		this.roleRt = roleRt;
		this.role = role;
		sendEvent = roleRt.getEventControler().registerEvent(
				ISendVitFromSns.class);
		addEvent = roleRt.getEventControler().registerEvent(
				IAddVitFromSns.class);
	}

	public void updateReceivingReceived(String roleId) {
		for (RoleVitReceiving record : role.getVitReceiving()) {
			if (record.getSenderRoleId().equals(roleId)) {
				record.setReceiveDate(sdf.format(new Date()));
			}
		}
	}

	/**
	 * 没有领取的行动力保留7天:赠送的时间超过7天并且灭有被领取
	 */
	boolean beforeLastweek(RoleVitReceiving record) {
		if (record == null || TextUtil.isBlank(record.getReceiveDate())) {
			return false;
		}
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(record.getSendDate()));

			return DateUtil.addDays(cal, 7).before(Calendar.getInstance());
		} catch (ParseException e) {
			return false;
		}
	}

	@Override
	public Collection<RoleVitReceiving> getUnCollectedVits() {
		HashSet<RoleVitReceiving> senders = new HashSet<RoleVitReceiving>();
		for (RoleVitReceiving record : this.role.getVitReceiving()) {
			if ((record.getReceiveDate() == null || "".equals(record
					.getReceiveDate())) && !beforeLastweek(record)) {
				senders.add(record);
			}
		}
		return senders;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	void sendVit(final String friend) {
		if (CollectionUtil.exists(role.getVitSending(),
				new IPredicate<RoleVitSending>() {

					@Override
					public boolean check(RoleVitSending record) {
						return record.getTargetRoleId().equals(friend)
								&& sdf.format(new Date()).equals(
										record.getSendDate());
					}
				})) {
			return;
		}
		// 这里会不会因为ehcache的原因取到null,使用loadRole_async?
		IRole fr = XsgRoleManager.getInstance().findRoleById(friend);
		if (fr == null) {
			LogManager.warn(String.format("%s不存在的好友%s", roleRt.getName(),
					friend));
			return;
		}
		saveSendingTo(friend);
		XsgRoleManager.getInstance().findRoleById(friend).getSnsController()
				.getActAbilityController()
				.saveReceivingFrom(roleRt.getRoleId());
		refreshSentData(role.getVitSending());
	}

	/**
	 * 清理过期数据
	 */
	private void refreshSentData(Set<RoleVitSending> c) {
		Collection<RoleVitSending> receivings = CollectionUtil.removeWhere(c,
				new IPredicate<RoleVitSending>() {

					@Override
					public boolean check(RoleVitSending item) {
						return beforeLastweek(item);
					}

				});
		role.setVitSending(new HashSet<RoleVitSending>(receivings));
	}

	/**
	 * 没有领取的行动力保留7天:赠送的时间超过7天并且灭有被领取
	 */
	boolean beforeLastweek(RoleVitSending record) {
		if (record == null) {
			return false;
		}
		if (record.getSendDate() == null) {
			LogManager.warn(record.getId() + "赠送数据异常,发送时间为空");
		}
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(record.getSendDate()));

			return DateUtil.addDays(cal, 7).before(Calendar.getInstance());
		} catch (ParseException e) {
			return false;
		}
	}

	@Override
	public void saveReceivingFrom(String senderRoleId) {

		role.getVitReceiving().add(
				new RoleVitReceiving(GlobalDataManager.getInstance()
						.generatePrimaryKey(), role, senderRoleId, sdf
						.format(new Date()), null));
	}

	@Override
	public void saveSendingTo(String receiverRoleId) {

		role.getVitSending().add(
				new RoleVitSending(GlobalDataManager.getInstance()
						.generatePrimaryKey(), role, receiverRoleId, sdf
						.format(new Date())));

	}

}
