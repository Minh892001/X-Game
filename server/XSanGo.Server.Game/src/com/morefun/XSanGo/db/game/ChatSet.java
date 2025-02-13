/**
 * 
 */
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
 * 聊天 设置保存
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "role_chat_set")
public class ChatSet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8074968650182417999L;

	private String id;
	private int privateSet; // 私聊 屏蔽
	private String privateColor; // 私聊 颜色
	private String privateUserColor;// 私聊 自定义 颜色，JSON格式：{位置：颜色，位置：颜色……}
	private int worldSet; // 世界消息 屏蔽
	private String worldColor; // 世界消息 颜色
	private String worldUserColor; // 世界自定义 颜色，JSON格式：{位置：颜色，位置：颜色……}
	private int factionSet; // 公会 屏蔽
	private String factionColor; // 公会 颜色
	private String factionUserColor;// 公会 自定义 颜色，JSON格式：{位置：颜色，位置：颜色……}
	private int groupSet; // 盟友 屏蔽
	private String groupColor; // 盟友 自定义 颜色，JSON格式：{位置：颜色，位置：颜色……}
	private String groupUserColor; // 盟友 颜色
	private Role role;

	public ChatSet() {
	}

	public ChatSet(String id, int privateSet, String privateColor,
			String privateUserColor, int worldSet, String worldColor,
			String worldUserColor, int factionSet, String factionColor,
			String factionUserColor, int groupSet, String groupColor,
			String groupUserColor, Role role) {
		this.id = id;
		this.privateSet = privateSet;
		this.privateColor = privateColor;
		this.privateUserColor = privateUserColor;
		this.worldSet = worldSet;
		this.worldColor = worldColor;
		this.worldUserColor = worldUserColor;
		this.factionSet = factionSet;
		this.factionColor = factionColor;
		this.factionUserColor = factionUserColor;
		this.groupSet = groupSet;
		this.groupColor = groupColor;
		this.groupUserColor = groupUserColor;
		this.role = role;
	}

	// 没有自定义颜色 初始化
	public ChatSet(String id, int privateSet, String privateColor,
			int worldSet, String worldColor, int factionSet,
			String factionColor, int groupSet, String groupColor, Role role) {

		this(id, privateSet, privateColor, "", worldSet, worldColor, "",
				factionSet, factionColor, "", groupSet, groupColor, "", role);
	}

	// 自定义颜色 初始化
	public ChatSet(String id, String privateUserColor, String worldUserColor,
			String factionUserColor, String groupUserColor, Role role) {

		this(id, 0, "", privateUserColor, 0, "", worldUserColor, 0, "",
				factionUserColor, 0, "", groupUserColor, role);
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "private_set", nullable = false)
	public int getPrivateSet() {
		return privateSet;
	}

	public void setPrivateSet(int privateSet) {
		this.privateSet = privateSet;
	}

	@Column(name = "private_color", nullable = false)
	public String getPrivateColor() {
		return privateColor;
	}

	public void setPrivateColor(String privateColor) {
		this.privateColor = privateColor;
	}

	@Column(name = "world_set", nullable = false)
	public int getWorldSet() {
		return worldSet;
	}

	public void setWorldSet(int worldSet) {
		this.worldSet = worldSet;
	}

	@Column(name = "world_color", nullable = false)
	public String getWorldColor() {
		return worldColor;
	}

	public void setWorldColor(String worldColor) {
		this.worldColor = worldColor;
	}

	@Column(name = "faction_set", nullable = false)
	public int getFactionSet() {
		return factionSet;
	}

	public void setFactionSet(int factionSet) {
		this.factionSet = factionSet;
	}

	@Column(name = "faction_color", nullable = false)
	public String getFactionColor() {
		return factionColor;
	}

	public void setFactionColor(String factionColor) {
		this.factionColor = factionColor;
	}

	@Column(name = "group_set", nullable = false)
	public int getGroupSet() {
		return groupSet;
	}

	public void setGroupSet(int groupSet) {
		this.groupSet = groupSet;
	}

	@Column(name = "group_color", nullable = false)
	public String getGroupColor() {
		return groupColor;
	}

	public void setGroupColor(String groupColor) {
		this.groupColor = groupColor;
	}

	@Column(name = "private_user_color", nullable = false)
	public String getPrivateUserColor() {
		return privateUserColor;
	}

	public void setPrivateUserColor(String privateUserColor) {
		this.privateUserColor = privateUserColor;
	}

	@Column(name = "world_user_color", nullable = false)
	public String getWorldUserColor() {
		return worldUserColor;
	}

	public void setWorldUserColor(String worldUserColor) {
		this.worldUserColor = worldUserColor;
	}

	@Column(name = "faction_user_color", nullable = false)
	public String getFactionUserColor() {
		return factionUserColor;
	}

	public void setFactionUserColor(String factionUserColor) {
		this.factionUserColor = factionUserColor;
	}

	@Column(name = "group_user_color", nullable = false)
	public String getGroupUserColor() {
		return groupUserColor;
	}

	public void setGroupUserColor(String groupUserColor) {
		this.groupUserColor = groupUserColor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
