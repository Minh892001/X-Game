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
 * 超级充值领取记录
 * @author xiaojun.zhang
 *
 */
@Entity
@Table(name = "role_super_charge")
public class RoleSuperCharge implements Serializable {

	private static final long serialVersionUID = -4470590762231187036L;
	private String roleId;;
	private Role role;
	/**今日累充表单*/
	private String scriptId;
	/**累计充值金额，会不断循环更新*/
	private int chargeAmount;
	/**抽奖次数*/
	private int raffleNum;
	
	public RoleSuperCharge(){}

	public RoleSuperCharge(String roleId, Role role, String scriptId, int chargeAmount, int raffleNum) {
		super();
		this.roleId = roleId;
		this.role = role;
		this.scriptId = scriptId;
		this.chargeAmount = chargeAmount;
		this.raffleNum = raffleNum;
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
	
	@Column(name = "script_Id", nullable = true)
	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}
	@Column(name = "charge_amount", nullable = false)
	public int getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(int chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	@Column(name = "raffle_num", nullable = false)
	public int getRaffleNum() {
		return raffleNum;
	}

	public void setRaffleNum(int raffleNum) {
		this.raffleNum = raffleNum;
	}
	
}
