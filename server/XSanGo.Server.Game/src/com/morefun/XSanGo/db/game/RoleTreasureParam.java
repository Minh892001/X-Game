/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 寻宝参数
 * 
 * @author xiongming.li
 *
 */
@Entity
@Table(name = "role_treasure_param")
public class RoleTreasureParam implements Serializable {
	private static final long serialVersionUID = -6238688141005781538L;
	private String id;
	private Role role;
	private int gainNum;// 已收获次数
	private int rescueNum;// 已援救次数
	private int accidentNum;// 已发生矿难次数
	private Date refreshDate;
	private Date checkAccidentDate;// 检测矿难时间
	private String rescueLogs = "[]";// 援救记录TreasureRescueLog[]的json
	private String accidentLogs = "[]";// 矿难记录TreasureAccidentLog[]的json
	private boolean accidentRedPoint;// 是否有矿难红点
	private String rescueMsg = "";// 求援私信

	public RoleTreasureParam() {

	}

	public RoleTreasureParam(String id, Role role) {
		this.id = id;
		this.role = role;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "gain_num", nullable = false)
	public int getGainNum() {
		return gainNum;
	}

	public void setGainNum(int gainNum) {
		this.gainNum = gainNum;
	}

	@Column(name = "rescue_num", nullable = false)
	public int getRescueNum() {
		return rescueNum;
	}

	public void setRescueNum(int rescueNum) {
		this.rescueNum = rescueNum;
	}

	@Column(name = "accident_num", nullable = false)
	public int getAccidentNum() {
		return accidentNum;
	}

	public void setAccidentNum(int accidentNum) {
		this.accidentNum = accidentNum;
	}

	@Column(name = "refresh_date")
	public Date getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
	}

	@Column(name = "rescue_logs", columnDefinition = "text")
	public String getRescueLogs() {
		return rescueLogs;
	}

	public void setRescueLogs(String rescueLogs) {
		this.rescueLogs = rescueLogs;
	}

	@Column(name = "accident_logs", columnDefinition = "text")
	public String getAccidentLogs() {
		return accidentLogs;
	}

	public void setAccidentLogs(String accidentLogs) {
		this.accidentLogs = accidentLogs;
	}

	@Column(name = "check_accident_date")
	public Date getCheckAccidentDate() {
		return checkAccidentDate;
	}

	public void setCheckAccidentDate(Date checkAccidentDate) {
		this.checkAccidentDate = checkAccidentDate;
	}

	@Column(name = "accident_red_point")
	public boolean isAccidentRedPoint() {
		return accidentRedPoint;
	}

	public void setAccidentRedPoint(boolean accidentRedPoint) {
		this.accidentRedPoint = accidentRedPoint;
	}

	@Column(name = "rescue_msg")
	public String getRescueMsg() {
		return rescueMsg == null ? "" : rescueMsg;
	}

	public void setRescueMsg(String rescueMsg) {
		this.rescueMsg = rescueMsg;
	}

}
