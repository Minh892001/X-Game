/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author sulingyun
 *
 */
@Entity
@Table(name = "role_fourth_test")
public class RoleFourthTest implements Serializable {
	
	private static final long serialVersionUID = 5503486180036430683L;
	private String roleId;
	private Role role;
	private boolean chargeReturn;

	public RoleFourthTest() {
	}

	public RoleFourthTest(Role role) {
		this.role = role;
		this.chargeReturn = false;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "charge_return", nullable = false)
	public boolean isChargeReturn() {
		return chargeReturn;
	}

	public void setChargeReturn(boolean chargeReturn) {
		this.chargeReturn = chargeReturn;
	}
}
