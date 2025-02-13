package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 公共世界BOSS
 * 
 * @author lixiongming
 * 
 */
@Entity
@Table(name = "world_boss")
public class WorldBoss implements Serializable {
	private static final long serialVersionUID = 3435039426361779796L;
	private String id;
	private int customsId;// 关卡ID
	private int bossId;
	private long bossSumBlood;// boss总血量
	private long bossRemainBlood;// boss剩余血量
	private String participatorIds = "";// 托管者ID,分割
	private String tailAwardJson = "[]";//TailAward[] 尾刀奖励者
	private String harmRankJson = "[]";// WorldBossRank[]
	private String countRankJson = "[]";// WorldBossRank[]
	private Date bossDeathDate;// BOSS死亡时间

	public WorldBoss() {

	}

	public WorldBoss(String id, int customsId, int bossId, int bossStatus,
			long bossSumBlood, long bossRemainBlood) {
		super();
		this.id = id;
		this.customsId = customsId;
		this.bossId = bossId;
		this.bossSumBlood = bossSumBlood;
		this.bossRemainBlood = bossRemainBlood;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "customs_id")
	public int getCustomsId() {
		return customsId;
	}

	public void setCustomsId(int customsId) {
		this.customsId = customsId;
	}

	@Column(name = "boss_id")
	public int getBossId() {
		return bossId;
	}

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	@Column(name = "boss_sum_blood")
	public long getBossSumBlood() {
		return bossSumBlood;
	}

	public void setBossSumBlood(long bossSumBlood) {
		this.bossSumBlood = bossSumBlood;
	}

	@Column(name = "boss_remain_blood")
	public long getBossRemainBlood() {
		return bossRemainBlood;
	}

	public void setBossRemainBlood(long bossRemainBlood) {
		this.bossRemainBlood = bossRemainBlood;
	}

	@Column(name = "participator_ids")
	public String getParticipatorIds() {
		return participatorIds;
	}

	public void setParticipatorIds(String participatorIds) {
		this.participatorIds = participatorIds;
	}

	@Column(name = "tail_award_json")
	public String getTailAwardJson() {
		return tailAwardJson;
	}

	public void setTailAwardJson(String tailAwardJson) {
		this.tailAwardJson = tailAwardJson;
	}

	@Column(name = "harm_rank_json")
	public String getHarmRankJson() {
		return harmRankJson;
	}

	public void setHarmRankJson(String harmRankJson) {
		this.harmRankJson = harmRankJson;
	}

	@Column(name = "count_rank_json")
	public String getCountRankJson() {
		return countRankJson;
	}

	public void setCountRankJson(String countRankJson) {
		this.countRankJson = countRankJson;
	}

	@Column(name = "boss_death_date")
	public Date getBossDeathDate() {
		return bossDeathDate;
	}

	public void setBossDeathDate(Date bossDeathDate) {
		this.bossDeathDate = bossDeathDate;
	}

}
