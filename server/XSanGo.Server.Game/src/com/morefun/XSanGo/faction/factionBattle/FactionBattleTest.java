/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleTest
 * 功能描述：
 * 文件名：FactionBattleTest.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.XSanGo.Protocol.FactionCallBackPrx;
import com.XSanGo.Protocol.StrongHoldState;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 功能描述
 * 
 * @author weiyi.zhao
 * @since 2016-3-9
 * @version 1.0
 */
public class FactionBattleTest {

	public static void test() {
		// test
		final List<Integer> list = new ArrayList<Integer>();
		for (int id : XsgFactionBattleManager.getInstance().getBattleScenes().keySet()) {
			if (!XsgFactionBattleManager.getInstance().isInitCamp(id)) {
				list.add(id);
			}
		}
		long interval = 500;
		LogicThread.scheduleTask(new DelayedTask(interval, interval) {
			@Override
			public void run() {
				sendStrongholdStateNotify(ArrayUtils.toPrimitive(list.toArray(new Integer[0])));
				System.out.println(DateUtil.format(new Date()) + "<<<<<<<<<<<<<<success<<<<<<<<<<<<<<<<<<<<<<<<");
			}
		});
	}

	/**
	 * 发送据点状态通知包，通知所有参战的成员
	 * 
	 * @param strongholdIds
	 */
	private static void sendStrongholdStateNotify(int... strongholdIds) {
		if (strongholdIds.length == 0) {
			return;
		}
		List<String> notifyRoleList = XsgFactionBattleManager.getInstance().getJoinRoleList();
		for (String roleId : notifyRoleList) {
			IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
			if (role != null) {
				FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
				if (callBack != null) {
					StrongHoldState[] views = createStrongHoldStateView(role, strongholdIds);
					callBack.begin_strongholdStateNotify(LuaSerializer.serialize(views));
				}
			}
		}
	}

	/**
	 * 据点状态视图数据
	 * 
	 * @param role
	 * @param strongholdIds
	 * @return
	 */
	private static StrongHoldState[] createStrongHoldStateView(IRole role, int... strongholdIds) {
		StrongHoldState[] views = null;
		for (int strongholdId : strongholdIds) {
			StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
			StrongHoldState shState = new StrongHoldState();
			shState.strongholdId = strongholdId;

			shState.state = (byte) sh.occupyState();

			views = (StrongHoldState[]) ArrayUtils.add(views, shState);
		}
		return views;
	}

}
