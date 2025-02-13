package com.morefun.XSanGo.friendsRecall;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 老友召回配置
 * 
 * @author zhangwei02.zhang
 * @since 2015年10月28日
 * @version 1.0
 */
@ExcelTable(fileName = "script/活动和礼包/老友召回.xls", beginRow = 2, sheetName = "参数配置")
public class FriendsRecallCfgT {

	/** 开启条件,开服后X天, 基于上午6点来计算 */
	@ExcelColumn(index = 0)
	public int requireDays;

	/** 活动开启，1:显示; 0:隐藏 */
	@ExcelColumn(index = 1)
	public int opened;

	/** 离线时间，单位：天，按照实际时间来算 */
	@ExcelColumn(index = 2)
	public int offlineDays;

	/** 玩家等级，回归角色需要达到的等级 */
	@ExcelColumn(index = 3)
	public int roleLevel;

	/** 邀请奖励，道具id,数量 */
	@ExcelColumn(index = 4)
	public String invitationRewards;

	/** 回归奖励，道具id,数量 */
	@ExcelColumn(index = 5)
	public String recallRewards;
	
	/** 最大可召回人数 */
	@ExcelColumn(index = 6)
	public int recallLimit;

	private List<Property> recallRewardList = null;

	private List<Property> invitationRewardList = null;

	public List<Property> getRecallReward() {
		if (recallRewardList != null) {
			return recallRewardList;
		}
		recallRewardList = new ArrayList<Property>();
		for (String pair : recallRewards.split(";")) {
			if (TextUtil.isBlank(pair)) {
				continue;
			}
			String[] tmp = pair.split(",");
			if (tmp == null || tmp.length != 2) {
				continue;
			}
			recallRewardList
					.add(new Property(tmp[0], Integer.parseInt(tmp[1])));
		}
		return recallRewardList;
	}

	public List<Property> getInvitationReward() {
		if (invitationRewardList != null) {
			return invitationRewardList;
		}
		invitationRewardList = new ArrayList<Property>();
		for (String pair : invitationRewards.split(";")) {
			if (TextUtil.isBlank(pair)) {
				continue;
			}
			String[] tmp = pair.split(",");
			if (tmp == null || tmp.length != 2) {
				continue;
			}
			invitationRewardList.add(new Property(tmp[0], Integer
					.parseInt(tmp[1])));
		}
		return invitationRewardList;
	}
}
