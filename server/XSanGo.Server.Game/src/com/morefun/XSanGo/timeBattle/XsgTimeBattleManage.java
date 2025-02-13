package com.morefun.XSanGo.timeBattle;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.BattleTimesView;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

public class XsgTimeBattleManage {
	private static XsgTimeBattleManage instance = new XsgTimeBattleManage();

	private List<TimeBattleT> timeBattleTs = new ArrayList<TimeBattleT>();
	private List<BattleSceneT> battleSceneTs = new ArrayList<BattleSceneT>();
	public TimeBattleConfigT configT = null;

	public static XsgTimeBattleManage getInstance() {
		return instance;
	}

	private XsgTimeBattleManage() {
		timeBattleTs = ExcelParser.parse(TimeBattleT.class);
		battleSceneTs = ExcelParser.parse(BattleSceneT.class);
		for (BattleSceneT b : battleSceneTs) {
			getTimeBattleTById(b.passId).addSceneT(b);
		}
		configT = ExcelParser.parse(TimeBattleConfigT.class).get(0);
	}

	public BattleTimesView[] getChallengeTimes() {
		List<BattleTimesView> times = new ArrayList<BattleTimesView>();
		for (TimeBattleT t : timeBattleTs) {
			times.add(new BattleTimesView(t.id, t.times, 0, 0));
		}
		return times.toArray(new BattleTimesView[0]);
	}

	public ITimeBattleControler createTimeBattleControler(IRole iRole, Role role) {
		return new TimeBattleControler(iRole, role);
	}

	public TimeBattleT getTimeBattleTById(int id) {
		for (TimeBattleT t : timeBattleTs) {
			if (t.id == id) {
				return t;
			}
		}
		return null;
	}
}
