/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleResult
 * 功能描述：
 * 文件名：IFactionBattleResult.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会战战斗结果事件
 * 
 * @author weiyi.zhao
 * @since 2016-2-3
 * @version 1.0
 */
@signalslot
public interface IFactionBattleResult {

	/**
	 * 战斗事件
	 * 
	 * @param strongholdId 开战据点
	 * @param targetRole 对手
	 * @param isRobot 是否机器人
	 * @param resultCode 网络回调代码
	 * @param isWin 是否胜利
	 * @param badge 徽章
	 * @param cd 挖宝CD
	 * @param kitsId 锦囊
	 * @param evenkill 连杀数
	 * @param isOccupy 是否占领
	 * @param resultStrongholdId 战斗结果角色所在据点
	 */
	void onResult(int strongholdId, String targetRole, boolean isRobot, int resultCode, boolean isWin, int badge, int cd, int kitsId, int evenkill, boolean isOccupy, int resultStrongholdId);
}
