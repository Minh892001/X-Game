package com.morefun.XSanGo.ArenaRank;

import java.util.HashMap;
import java.util.Map;

import Ice.Current;

import com.XSanGo.Protocol.AMD_CrossArena_initRank;
import com.XSanGo.Protocol.ArenaReportView;
import com.XSanGo.Protocol.CrossArenaCallbackPrx;
import com.XSanGo.Protocol.CrossArenaCallbackPrxHelper;
import com.XSanGo.Protocol._CrossArenaDisp;
import com.morefun.XSanGo.robot.XsgRobotManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.LogManager;

public class CrossArenaI extends _CrossArenaDisp {

	private static final long serialVersionUID = -8440906149252747445L;

	private static CrossArenaI instance = new CrossArenaI();

	private CrossArenaI() {

	}

	public static CrossArenaI instance() {
		return instance;
	}

	@Override
	public boolean ping(long time, Current __current) {
		return CrossArenaManager.getInstance().isUsable();
	}

	@Override
	public void setCallback(CrossArenaCallbackPrx cb, Current __current) {
		Map<String, String> ctx = new HashMap<String, String>();
		ctx.put("_fwd", "t");
		CrossArenaCallbackPrx prx = CrossArenaCallbackPrxHelper.checkedCast(cb.ice_context(ctx));
		CrossArenaManager.getInstance().setCrossArenaCallback(prx);
	}

	@Override
	public void initRank_async(final AMD_CrossArena_initRank __cb, Current __current) {
		XsgRoleManager.getInstance().loadRoleAsync(XsgRobotManager.getInstance().ladderRobotIds, new Runnable() {

			@Override
			public void run() {
				for (String id : XsgRobotManager.getInstance().ladderRobotIds) {
					IRole r = XsgRoleManager.getInstance().findRoleById(id);
					if (r != null) {
						try {
							r.getCrossArenaControler().saveBattle(null);
						} catch (Exception e) {
							LogManager.error(e);
						}
					}
				}
				__cb.ice_response();
			}
		});
	}

	@Override
	public void addCrossArenaLog(final String roleId, final ArenaReportView report, Current __current) {
		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {

			@Override
			public void run() {
				IRole r = XsgRoleManager.getInstance().findRoleById(roleId);
				if (r != null) {
					r.getCrossArenaControler().addCrossArenaLog(report);
				}
			}
		}, null);
	}

}
