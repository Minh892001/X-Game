package com.morefun.XSanGo.formation.datacollector;

import com.morefun.XSanGo.copy.SmallCopyT;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.event.protocol.IArenaFight;
import com.morefun.XSanGo.event.protocol.ICopyCompleted;
import com.morefun.XSanGo.role.IRole;

public class FormationDataCollecter implements IFormationDataCollecter {
	private IRole rt;
	private Role db;

	public FormationDataCollecter(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;

		this.rt.getEventControler().registerHandler(ICopyCompleted.class, this);
		this.rt.getEventControler().registerHandler(IArenaFight.class, this);
	}

	@Override
	public void onCopyCompleted(SmallCopyT templete, int star, boolean firstPass, int fightPower, int junling) {
		XsgFormationDataCollecterManager.getInstance().onCollectEventEmit(
				this.rt);
	}

	@Override
	public void onArenaFight(int resFlag, int roleRank, int rivalRank,
			int sneerId, String reward) {
		XsgFormationDataCollecterManager.getInstance().onCollectEventEmit(
				this.rt);
	}

}
