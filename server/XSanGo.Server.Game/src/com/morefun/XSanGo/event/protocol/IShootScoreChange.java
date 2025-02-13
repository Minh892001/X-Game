/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 百步穿杨射击积分变更事件
 * 
 * @author zhouming
 * 
 */
@signalslot
public interface IShootScoreChange {

	/**
	 * 积分变更事件
	 * 
	 * @param shootType
	 *            射击类型
	 * @param isFree
	 *            是否免费
	 * @param beforeScore
	 *            变更前积分
	 * @param score
	 *            增加的积分
	 * @param afterScore
	 *            变更后积分
	 */
	void onShootScoreChange(int shootType, boolean isFree, int beforeScore,
			int score, int afterScore, int totalScore);

}
