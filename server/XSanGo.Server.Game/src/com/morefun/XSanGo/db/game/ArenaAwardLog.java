/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 竞技场排行 发送奖励日志
 * 
 * @author 吕明涛
 * 
 */
@Entity
@Table(name = "arena_award_log")
public class ArenaAwardLog implements Serializable {
	
	private static final long serialVersionUID = 5412864647385745330L;
	
	private Date awardDate;	//发送时间
	private int awardType;//奖励的类型 1：竞技场，2：群雄争霸，3：世界BOSS排行榜奖励，4：领取世界BOSS尾刀奖励 5:记录百步穿杨积分排名发奖的名单
	private String awardStr;//奖励的字符串
	
	public ArenaAwardLog() {
	}

	public ArenaAwardLog(Date awardDate, int awardType, String awardStr) {
		this.awardDate = awardDate;
		this.awardType = awardType;
		this.awardStr = awardStr;
	}
	
	@Id
	@Column(name = "award_date", nullable = false)
	public Date getAwardDate() {
		return awardDate;
	}

	public void setAwardDate(Date awardDate) {
		this.awardDate = awardDate;
	}
	
	@Column(name = "award_type", columnDefinition = "INT default 1")
	public int getAwardType() {
		return awardType;
	}

	public void setAwardType(int awardType) {
		this.awardType = awardType;
	}

	@Column(name = "award_str", length=5000)
	public String getAwardStr() {
		return awardStr;
	}

	public void setAwardStr(String awardStr) {
		this.awardStr = awardStr;
	}	

}
