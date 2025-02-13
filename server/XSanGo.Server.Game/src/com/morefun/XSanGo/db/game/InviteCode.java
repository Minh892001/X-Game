package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 邀请码表
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "invite_code")
public class InviteCode implements Serializable {
	private static final long serialVersionUID = 157904734410905391L;
	private String id;
	private String code;
	private String useRoleId;

	public InviteCode() {
	}

	public InviteCode(String id, String code, String useRoleId) {
		super();
		this.id = id;
		this.code = code;
		this.useRoleId = useRoleId;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "code", nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "use_role_id")
	public String getUseRoleId() {
		return useRoleId;
	}

	public void setUseRoleId(String useRoleId) {
		this.useRoleId = useRoleId;
	}

}
