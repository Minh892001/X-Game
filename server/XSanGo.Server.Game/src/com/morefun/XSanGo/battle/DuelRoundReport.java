/**
 * 
 */
package com.morefun.XSanGo.battle;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.IntString;

/**
 * 单挑单回合战报
 * 
 * @author sulingyun
 *
 */
public class DuelRoundReport {
	private List<ActionReport> actionList = new ArrayList<ActionReport>();
	private List<IntString> expireBuffList = new ArrayList<IntString>();

	public List<ActionReport> getActionList() {
		return actionList;
	}

	public void appendAction(ActionReport act) {
		this.actionList.add(act);
	}

	public void appendExpireBuff(List<AbsDuelBuff> buffList) {
		for (AbsDuelBuff buff : buffList) {
			this.expireBuffList.add(new IntString(buff.getTargetId(), buff
					.getSkillT().effect));
		}
	}

	public IntString[] getExpireBuffList() {
		return this.expireBuffList.toArray(new IntString[0]);
	}
}