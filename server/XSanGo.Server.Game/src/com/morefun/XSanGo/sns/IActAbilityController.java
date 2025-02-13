package com.morefun.XSanGo.sns;

import java.util.Collection;

import com.morefun.XSanGo.db.game.RoleVitReceiving;

/**
 * 行动力相关的几个操作
 */
public interface IActAbilityController {
	/**
	 * @return 可领取的行动力赠送者集合
	 */
	Collection<RoleVitReceiving> getUnCollectedVits();

	void saveReceivingFrom(String senderRoleId);

	void saveSendingTo(String receiverRoleId);

	void updateReceivingReceived(String roleId);
}
