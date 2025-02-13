package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 公会副本信息
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "faction_copy")
public class FactionCopy implements Serializable {
	private static final long serialVersionUID = -8499784069163731553L;
	private String id;
	private Faction faction;
	private int copyId;
	private String roleId;// 正在挑战的玩家
	private long challengeTime;// 开始挑战时间
	private int stageNum;// 关卡序列
	private String monsterJson;// 怪物信息 json格式
	private Date openDate = new Date();// 开启时间
	private int harmBlood;// 掉血量
	private int additionType;// 加成类型

	public FactionCopy() {
		
	}

	public FactionCopy(String id, Faction faction, int copyId, String roleId,
			int stageNum, String monsterJson) {
		super();
		this.id = id;
		this.faction = faction;
		this.copyId = copyId;
		this.roleId = roleId;
		this.stageNum = stageNum;
		this.monsterJson = monsterJson;
	}

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faction_id", nullable = false)
	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	@Column(name = "copy_id", nullable = false)
	public int getCopyId() {
		return copyId;
	}

	public void setCopyId(int copyId) {
		this.copyId = copyId;
	}

	@Column(name = "role_id")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "stage_num", nullable = false)
	public int getStageNum() {
		return stageNum;
	}

	public void setStageNum(int stageNum) {
		this.stageNum = stageNum;
	}

	@Column(name = "monster_json", nullable = false)
	public String getMonsterJson() {
		return monsterJson;
	}

	public void setMonsterJson(String monsterJson) {
		this.monsterJson = monsterJson;
	}

	@Column(name = "open_date", nullable = false)
	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	@Column(name = "challenge_time", nullable = false)
	public long getChallengeTime() {
		return challengeTime;
	}

	public void setChallengeTime(long challengeTime) {
		this.challengeTime = challengeTime;
	}

	@Column(name = "harm_blood", nullable = false)
	public int getHarmBlood() {
		return harmBlood;
	}

	public void setHarmBlood(int harmBlood) {
		this.harmBlood = harmBlood;
	}

	@Column(name = "addition_type")
	public int getAdditionType() {
		return additionType;
	}

	public void setAdditionType(int additionType) {
		this.additionType = additionType;
	}

}
