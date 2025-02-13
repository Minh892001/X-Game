/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sulingyun
 *
 */
@Entity
@Table(name = "battle_power_snapshot")
public class BattlePowerSnapshot implements Serializable {

	private static final long serialVersionUID = 382062918190437873L;
	private String roleId;
	private int roleLevel;
	private String roleName;
	private int power;
	private String data;

	public BattlePowerSnapshot() {
	}

	public BattlePowerSnapshot(String roleId, int roleLevel, String roleName,
			int power, String data) {
		this.roleId = roleId;
		this.roleLevel = roleLevel;
		this.roleName = roleName;
		this.power = power;
		this.data = data;
	}

	@Id
	@Column(name = "role_id", nullable = false, length = 64)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "role_level", nullable = false)
	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	@Column(name = "role_name", nullable = false)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "power", nullable = false)
	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	@Column(name = "data", nullable = false, columnDefinition = "text not null")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
