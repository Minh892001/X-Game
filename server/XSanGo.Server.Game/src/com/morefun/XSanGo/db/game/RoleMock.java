package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "role_mock")
public class RoleMock implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;
	private String id;
	private Role role;
	private String templateId;
	private int totalNum;
	private int sucessNum;

	public RoleMock() {

	}

	public RoleMock(String id, Role role, String templateId) {
		this.id = id;
		this.role = role;
		this.templateId = templateId;
		// this.totalNum = 0;
		this.sucessNum = 0;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "template_id", nullable = false)
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	@Column(name = "sucess_num", nullable = false)
	public int getSucessNum() {
		return sucessNum;
	}

	public void setSucessNum(int sucessNum) {
		this.sucessNum = sucessNum;
	}

	@Column(name = "total_num", nullable = false)
	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
}
