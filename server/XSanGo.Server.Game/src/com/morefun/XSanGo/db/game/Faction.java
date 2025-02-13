/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

/**
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "faction")
@Where(clause = "state != 1")
public class Faction implements Serializable {
	private static final long serialVersionUID = 4694888174243389475L;
	/** 解散状态 */
	public static final short STATE_DISBAND = 1;

	// Fields
	private String id;
	private String name;
	private String icon;
	private int level;
	private int exp;
	private String bossId;
	private String creatorId;
	private Date createTime;
	private short state;// 0-正常 1-解散
	private String qqNum;
	private String announcement;// 公告
	private int joinType;// 申请加入类型
	private int joinLevel = 15;// 入会主公等级
	private Date lastOpenCopyTime;
	private int openCopyNum;
	private Set<FactionMember> members = new HashSet<FactionMember>();
	private List<FactionHistory> histories = new ArrayList<FactionHistory>();
	private Set<FactionCopy> copys = new HashSet<FactionCopy>();
	private String shopJson;// 商城商品FactionShopView[]
	private Date shopRefreshDate;// 商品刷新时间
	private int honor;// 历史荣誉
	private Date renameDate;// 改名时间
	private int sendMailTimes;// 发送邮件次数
	private Date sendMailDate;// 发送邮件时间
	private String mailLogs;// FactionMailLog[]的json
	private int joinVip;
	private String manifesto;// 公会宣言
	private String warehouseData;// 仓库物品数据WarehouseItemBean[]的json
	private String oviStoreData;// 商铺物品数据OviStoreItem[]的json
	private int score;// 公会积分

	/** 公会战参战公会数据 */
	private FactionBattle factionBattle;

	/** 公会战参数成员数据 */
	private Map<String, FactionBattleMember> factionBattleMember = new HashMap<String, FactionBattleMember>();
	private int studyNum;// 研究次数
	private String technologyData = "[]";// 公会科技数据FactionTechnologyBean[]的json
	private String allotLog = "[]";// 仓库分配日志，FactionAllotLog[]的lua
	private int recommendNum;
	private Date recommendRefreshDate;
	private String purchaseLogs = "[]";// 栈房购置日志PurchaseLog[]的json
	private int deleteDay;
	private int recruitNum;
	private Date refreshRecruitDate;

	// Constructors

	/** default constructor */
	public Faction() {
	}

	/**
	 * 新建帮派时用的构造函数
	 * 
	 * @param name
	 * @param money
	 * @param creatorId
	 * @param createTime
	 */
	public Faction(String id, String name, String icon, String creatorId, Date createTime) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.creatorId = creatorId;
		this.bossId = creatorId;
		this.createTime = createTime;

		this.level = 1;// 默认为1级
		this.exp = 0;
		this.announcement = "";
		this.qqNum = "";
		this.joinType = 1;
	}

	// Property accessors

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "name", unique = true, nullable = false, length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "boss_id", nullable = false)
	public String getBossId() {
		return bossId;
	}

	public void setBossId(String bossId) {
		this.bossId = bossId;
	}

	@Column(name = "creator_id", nullable = false)
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "state", nullable = false)
	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	@Column(name = "exp", nullable = false)
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	@Column(name = "announcement", nullable = false)
	public String getAnnouncement() {
		return announcement;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "faction")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<FactionMember> getMembers() {
		return members;
	}

	public void setMembers(Set<FactionMember> members) {
		this.members = members;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "faction")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause = "create_time desc")
	public List<FactionHistory> getHistories() {
		return histories;
	}

	public void setHistories(List<FactionHistory> histories) {
		this.histories = histories;
	}

	@Column(name = "icon", nullable = false)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "qq", nullable = false, columnDefinition = "varchar(16) default ''")
	public String getQqNum() {
		return qqNum;
	}

	public void setQqNum(String qqNum) {
		this.qqNum = qqNum;
	}

	@Column(name = "join_type", nullable = false, columnDefinition = "int default 1")
	public int getJoinType() {
		return joinType;
	}

	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "faction")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public Set<FactionCopy> getCopys() {
		return copys;
	}

	public void setCopys(Set<FactionCopy> copys) {
		this.copys = copys;
	}

	@Column(name = "last_open_copy_time")
	public Date getLastOpenCopyTime() {
		return lastOpenCopyTime;
	}

	public void setLastOpenCopyTime(Date lastOpenCopyTime) {
		this.lastOpenCopyTime = lastOpenCopyTime;
	}

	@Column(name = "open_copy_num", nullable = false)
	public int getOpenCopyNum() {
		return openCopyNum;
	}

	public void setOpenCopyNum(int openCopyNum) {
		this.openCopyNum = openCopyNum;
	}

	@Column(name = "shop_json")
	public String getShopJson() {
		return shopJson;
	}

	public void setShopJson(String shopJson) {
		this.shopJson = shopJson;
	}

	@Column(name = "shop_refresh_date")
	public Date getShopRefreshDate() {
		return shopRefreshDate;
	}

	public void setShopRefreshDate(Date shopRefreshDate) {
		this.shopRefreshDate = shopRefreshDate;
	}

	@Column(name = "honor")
	public int getHonor() {
		return honor;
	}

	public void setHonor(int honor) {
		this.honor = honor;
	}

	@Column(name = "rename_date")
	public Date getRenameDate() {
		return renameDate;
	}

	public void setRenameDate(Date renameDate) {
		this.renameDate = renameDate;
	}

	@Column(name = "join_level")
	public int getJoinLevel() {
		return joinLevel;
	}

	public void setJoinLevel(int joinLevel) {
		this.joinLevel = joinLevel;
	}

	@Column(name = "send_mail_times")
	public int getSendMailTimes() {
		return sendMailTimes;
	}

	public void setSendMailTimes(int sendMailTimes) {
		this.sendMailTimes = sendMailTimes;
	}

	@Column(name = "send_mail_date")
	public Date getSendMailDate() {
		return sendMailDate;
	}

	public void setSendMailDate(Date sendMailDate) {
		this.sendMailDate = sendMailDate;
	}

	@Column(name = "mail_logs")
	public String getMailLogs() {
		return mailLogs;
	}

	public void setMailLogs(String mailLogs) {
		this.mailLogs = mailLogs;
	}

	@Column(name = "join_vip")
	public int getJoinVip() {
		return joinVip;
	}

	public void setJoinVip(int joinVip) {
		this.joinVip = joinVip;
	}

	@Column(name = "manifesto")
	public String getManifesto() {
		return manifesto;
	}

	public void setManifesto(String manifesto) {
		this.manifesto = manifesto;
	}

	@Column(name = "warehouse_data", columnDefinition = "text")
	public String getWarehouseData() {
		return warehouseData;
	}

	public void setWarehouseData(String warehouseData) {
		this.warehouseData = warehouseData;
	}

	@Column(name = "ovi_store_data", columnDefinition = "text")
	public String getOviStoreData() {
		return oviStoreData;
	}

	public void setOviStoreData(String oviStoreData) {
		this.oviStoreData = oviStoreData;
	}

	@Column(name = "score", nullable = false)
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return Returns the factionBattle.
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "faction")
	public FactionBattle getFactionBattle() {
		return factionBattle;
	}

	/**
	 * @param factionBattle
	 *            The factionBattle to set.
	 */
	public void setFactionBattle(FactionBattle factionBattle) {
		this.factionBattle = factionBattle;
	}

	/**
	 * @return Returns the factionBattleMember.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "faction")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "roleId")
	@Fetch(FetchMode.SELECT)
	public Map<String, FactionBattleMember> getFactionBattleMember() {
		return factionBattleMember;
	}

	/**
	 * @param factionBattleMember
	 *            The factionBattleMember to set.
	 */
	public void setFactionBattleMember(Map<String, FactionBattleMember> factionBattleMember) {
		this.factionBattleMember = factionBattleMember;
	}

	@Column(name = "study_num", nullable = false)
	public int getStudyNum() {
		return studyNum;
	}

	public void setStudyNum(int studyNum) {
		this.studyNum = studyNum;
	}

	@Column(name = "technology_data", columnDefinition = "text")
	public String getTechnologyData() {
		return technologyData;
	}

	public void setTechnologyData(String technologyData) {
		this.technologyData = technologyData;
	}

	@Column(name = "allot_log", columnDefinition = "text")
	public String getAllotLog() {
		return allotLog;
	}

	public void setAllotLog(String allotLog) {
		this.allotLog = allotLog;
	}

	@Column(name = "recommend_num", nullable = false)
	public int getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(int recommendNum) {
		this.recommendNum = recommendNum;
	}

	@Column(name = "recommend_refresh_date")
	public Date getRecommendRefreshDate() {
		return recommendRefreshDate;
	}

	public void setRecommendRefreshDate(Date recommendRefreshDate) {
		this.recommendRefreshDate = recommendRefreshDate;
	}

	@Column(name = "purchase_logs")
	public String getPurchaseLogs() {
		return purchaseLogs;
	}

	public void setPurchaseLogs(String purchaseLogs) {
		this.purchaseLogs = purchaseLogs;
	}

	@Column(name = "delete_day")
	public int getDeleteDay() {
		return deleteDay;
	}

	public void setDeleteDay(int deleteDay) {
		this.deleteDay = deleteDay;
	}

	@Column(name = "recruit_num")
	public int getRecruitNum() {
		return recruitNum;
	}

	public void setRecruitNum(int recruitNum) {
		this.recruitNum = recruitNum;
	}

	@Column(name = "refresh_recruit_date")
	public Date getRefreshRecruitDate() {
		return refreshRecruitDate;
	}

	public void setRefreshRecruitDate(Date refreshRecruitDate) {
		this.refreshRecruitDate = refreshRecruitDate;
	}

}
