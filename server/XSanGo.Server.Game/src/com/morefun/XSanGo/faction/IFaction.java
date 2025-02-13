/**
 * 
 */
package com.morefun.XSanGo.faction;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.XSanGo.Protocol.FactionAllotLog;
import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.db.game.FactionBattle;
import com.morefun.XSanGo.db.game.FactionBattleMember;
import com.morefun.XSanGo.db.game.FactionCopy;
import com.morefun.XSanGo.db.game.FactionHistory;
import com.morefun.XSanGo.db.game.FactionMember;
import com.morefun.XSanGo.role.IRole;

/**
 * 公会对象接口
 * 
 * @author sulingyun
 * 
 */
public interface IFaction {

	/**
	 * 获取公会编号
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 获取公会图标
	 * 
	 * @return
	 */
	String getIcon();

	/**
	 * 获取公会名
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 获取公会等级
	 * 
	 * @return
	 */
	int getLevel();

	/**
	 * 获取公会QQ群号
	 * 
	 * @return
	 */
	String getQQ();

	/**
	 * 获取公会人数
	 * 
	 * @return
	 */
	int getMemberSize();

	/**
	 * 获取当前经验
	 * 
	 * @return
	 */
	int getExp();

	/**
	 * 获取公告信息
	 * 
	 * @return
	 */
	String getAnnounce();

	/**
	 * 加入类型
	 * 
	 * @return
	 */
	int getJoinType();

	/**
	 * 批准入会申请
	 * 
	 * @param candidate
	 */
	void approveReq(IRole candidate);

	/**
	 * 异步保存数据
	 */
	void saveAsyn();

	/**
	 * 指定的玩家是否有批准入会申请的权限
	 * 
	 * @param roleId
	 * @return
	 */
	boolean hasApproveNewMemberDuty(String roleId);

	/**
	 * 添加公会操作记录
	 * 
	 * @param remark
	 */
	void addHistory(IRole targetRole, String remark, IRole sponsor);

	/**
	 * 获取指定角色的成员记录
	 * 
	 * @param roleId
	 * @return
	 */
	FactionMember getMemberByRoleId(String roleId);

	/**
	 * 移除公会成员
	 * 
	 * @param member
	 */
	void removeMember(FactionMember member, IRole role);

	/**
	 * 会长离开后执行逻辑
	 */
	void bossLeft();

	/**
	 * 获取所有会员
	 * 
	 * @return
	 */
	Set<FactionMember> getAllMember();

	/**
	 * 更新公告
	 * 
	 * @param announcement
	 */
	void setAnnouncement(String announcement);

	/**
	 * 设置会长ID
	 * 
	 * @param roleId
	 */
	void setBossId(String roleId);

	/**
	 * 保存配置
	 * 
	 * @param icon
	 * @param qq
	 * @param joinType
	 */
	void saveConfig(String icon, String qq, String notice, int joinType, int joinLevel, int joinVip, String manifesto,
			int deleteDay);

	List<FactionHistory> getAllHistory();

	/**
	 * 获取给玩家看的ID
	 * 
	 * @return
	 */
	String getSubId();

	/**
	 * 获取会长ID
	 * 
	 * @return
	 */
	String getBossId();

	/**
	 * 增加公会经验
	 * 
	 * @param num
	 * @param roleId
	 *            捐赠者ID
	 */
	void addExp(int num, String roleId);

	FactionCopy getFactionCopy();

	void setFactionCopy(FactionCopy factionCopy);

	Date getLastOpenCopyTime();

	int getOpenCopyNum();

	void setLastOpenCopyTime(Date date);

	void setOpenCopyNum(int num);

	/**
	 * 获取公会创建时间
	 * 
	 * @return
	 */
	Date getCreateTime();

	String getShopJson();

	void setShopJson(String json);

	Date getShopRefreshDate();

	/**
	 * 获取公会历史荣誉
	 * 
	 * @return
	 */
	int getFactionHonor();

	/**
	 * 设置公会历史荣誉
	 * 
	 * @param honor
	 */
	void setFactionHonor(int honor);

	// /**
	// * 刷新商品
	// *
	// * @return
	// */
	// FactionShop[] refreshShop();

	/**
	 * 获取公会战报名人数
	 * 
	 * @return
	 */
	int getApplyPeople();

	/**
	 * 获取改名时间
	 * 
	 * @return
	 */
	Date getRenameDate();

	/**
	 * 设置改名时间
	 * 
	 * @param date
	 */
	void setRenameDate(Date date);

	/**
	 * 设置公会名
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * 获取入会等级
	 * 
	 * @return
	 */
	int getJoinLevel();

	/**
	 * 获取发送邮件次数
	 * 
	 * @return
	 */
	int getSendMailTimes();

	/**
	 * 更新发送邮件次数
	 * 
	 * @param times
	 */
	void setSendMailTimes(int times);

	/**
	 * 获取发送邮件时间
	 * 
	 * @return
	 */
	Date getSendMailDate();

	/**
	 * 更新发送邮件时间
	 * 
	 * @param date
	 */
	void setSendMailDate(Date date);

	/**
	 * 获取公会邮件发送记录
	 * 
	 * @return
	 */
	String getMailLogs();

	/**
	 * 更新公会邮件发送记录
	 * 
	 * @param mailLogs
	 */
	void setMailLogs(String mailLogs);

	/**
	 * 获取公会宣言
	 * 
	 * @return
	 */
	String getManifesto();

	/**
	 * 更新公会宣言
	 * 
	 * @param manifesto
	 */
	void setManifesto(String manifesto);

	/**
	 * 获取入会vip等级
	 * 
	 * @return
	 */
	int getJoinVip();

	/**
	 * 更新入会vip等级
	 * 
	 * @param joinVip
	 */
	void setJoinVip(int joinVip);

	/**
	 * 获取公会仓库数据
	 * 
	 * @return
	 */
	String getWarehouseData();

	/**
	 * 更新公会仓库数据
	 * 
	 * @param warehouseData
	 */
	void setWarehouseData(String warehouseData);

	/**
	 * 获取公会商铺数据
	 * 
	 * @return
	 */
	String getOviStoreData();

	/**
	 * 更新公会商铺数据
	 * 
	 * @param oviStoreData
	 */
	void setOviStoreData(String oviStoreData);

	/**
	 * 获取公会积分
	 * 
	 * @return
	 */
	int getScore();

	/**
	 * 更新公会积分
	 * 
	 * @param score
	 */
	void setScore(int score);

	int getStudyNum();

	void setStudyNum(int studyNum);

	String getTechnologyData();

	void setTechnologyData(String technologyData);

	/**
	 * 公会仓库物品放入（能放多少放多少）
	 * 
	 * @param itemViews
	 *            物品结果集
	 */
	void putWarehouseItem(ItemView... itemViews);

	/**
	 * 公会仓库物品放入（能放多少放多少）
	 * 
	 * @param itemTemplateId
	 *            物品模版编号
	 * @param count
	 *            数量
	 */
	void putWarehouseItem(String itemTemplateId, int count);

	/**
	 * 获取公会战参数数据
	 * 
	 * @return
	 */
	FactionBattle getFactionBattle();

	/**
	 * 设置公会战参战数据
	 * 
	 * @param factionBattle
	 */
	void setFactionBattle(FactionBattle factionBattle);

	/**
	 * 获取公会战参战成员的数据
	 * 
	 * @param roleId
	 * @return
	 */
	FactionBattleMember getFactionBattleMember(String roleId);

	/**
	 * 获取公会战参战成员数据
	 * 
	 * @return
	 */
	Map<String, FactionBattleMember> getAllFactionBattleMember();

	/**
	 * 增加公会战参战成员数据
	 * 
	 * @param fbm
	 */
	void addFactionBattleMember(FactionBattleMember fbm);

	/**
	 * 重置公会战数据
	 */
	void resetFactionBattle();

	/**
	 * 获取公会仓库等级
	 * 
	 * @return
	 */
	int getWarehouseLevel();

	/**
	 * 获取公会栈房等级
	 * 
	 * @return
	 */
	int getStorehouseLevel();

	/**
	 * 阵营科技升级后的处理
	 */
	void onTechnologyLevelUp();

	String getAllotLog();

	void setAllotLog(String allotLogs);

	int getRecommendNum();

	void setRecommendNum(int recommendNum);

	Date getRecommendRefreshDate();

	void setRecommendRefreshDate(Date recommendRefreshDate);

	String getPurchaseLogs();

	void setPurchaseLogs(String purchaseLogs);

	/**
	 * 初始化公会仓库物品
	 */
	void initWarehouseItem();

	/**
	 * 增加分配日志
	 * 
	 * @param log
	 */
	void addAllotLog(FactionAllotLog log);

	/**
	 * 移除玩家仓库物品申请队列
	 * 
	 * @param roleId
	 */
	void removeItemQueue(String roleId);

	int getDeleteDay();

	int getRecruitNum();

	void setRecruitNum(int recruitNum);

	Date getRefreshRecruitDate();

	void setRefreshRecruitDate(Date refreshRecruitDate);
	
	void setJoinType(int joinType);
}
