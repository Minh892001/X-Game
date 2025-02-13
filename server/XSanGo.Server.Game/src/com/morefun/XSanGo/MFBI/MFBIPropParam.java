package com.morefun.XSanGo.MFBI;

public class MFBIPropParam {
	private String propName; 	//道具名称
	private String propCateID;	//道具分类代码
	private String propCateName;//道具分类名称
	private String sysCode;		//功能系统代码， 如：商城、副本、 关卡等系统对应的代码
	private String sysName;		//功能系统名称， 如：商城、副本、 关卡等系统名称
	private String instanceID;	//道具实例 ID
	private int itemLifeTime;	//道具时效
	
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getPropCateID() {
		return propCateID;
	}
	public void setPropCateID(String propCateID) {
		this.propCateID = propCateID;
	}
	public String getPropCateName() {
		return propCateName;
	}
	public void setPropCateName(String propCateName) {
		this.propCateName = propCateName;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getInstanceID() {
		return instanceID;
	}
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}
	public int getItemLifeTime() {
		return itemLifeTime;
	}
	public void setItemLifeTime(int itemLifeTime) {
		this.itemLifeTime = itemLifeTime;
	}
	
	
}
