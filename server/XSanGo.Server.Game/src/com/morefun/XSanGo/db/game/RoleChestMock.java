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
@Table(name = "role_chest_mock")
public class RoleChestMock implements Serializable {

	private static final long serialVersionUID = -8665863637875961743L;
	private String id;
	private Role role;
	private String templateId;
	private int num;

	public RoleChestMock() {

	}

	public RoleChestMock(String id, Role role, String templateId, int num) {
		this.id = id;
		this.role = role;
		this.templateId = templateId;
		this.num = num;
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

	@Column(name = "num", nullable = false)
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
