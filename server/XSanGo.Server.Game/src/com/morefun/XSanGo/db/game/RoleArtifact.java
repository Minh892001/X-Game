package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 神器
 * 
 * @author xiongming.li
 *
 */
@Entity
@Table(name = "role_artifact")
public class RoleArtifact implements Serializable {
	private static final long serialVersionUID = 2268238741898488599L;
	private String id;
	private Role role;
	private int artifactId;// 神器ID
	private int level;
	private String useHeroId; // 使用的武将

	public RoleArtifact() {

	}

	public RoleArtifact(String id, Role role, int artifactId, int level) {
		this.id = id;
		this.role = role;
		this.artifactId = artifactId;
		this.level = level;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return this.id;
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

	@Column(name = "artifact_id")
	public int getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(int artifactId) {
		this.artifactId = artifactId;
	}

	@Column(name = "level")
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "use_hero_id")
	public String getUseHeroId() {
		return useHeroId;
	}

	public void setUseHeroId(String useHeroId) {
		this.useHeroId = useHeroId;
	}

}
