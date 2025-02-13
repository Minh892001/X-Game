/**
 * 
 */
package com.morefun.XSanGo.formation.datacollector;

import com.XSanGo.Protocol.PvpOpponentFormationView;

/**
 * 战力数据快照查询结果
 * 
 * @author sulingyun
 *
 */
public class BattlePowerSnapshotQueryResult {
	private String roleId;
	private int power;
	private PvpOpponentFormationView pvpView;

	public BattlePowerSnapshotQueryResult(String roleId, int power,
			PvpOpponentFormationView pvpView) {
		this.roleId = roleId;
		this.power = power;
		this.pvpView = pvpView;
	}

	public String getRoleId() {
		return roleId;
	}

	public int getPower() {
		return power;
	}

	public PvpOpponentFormationView getPvpView() {
		return pvpView;
	}
}
