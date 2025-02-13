package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.sns.SNSType;
import com.morefun.XSanGo.sns.SnsController.RelationChangeEventActionType;

/**
 * 好友模块社交关系变更事件
 *
 */
@signalslot
public interface ISnsRelationChange {
	
	void relationChanged(String target, SNSType relationType,
			RelationChangeEventActionType actionType);
}
