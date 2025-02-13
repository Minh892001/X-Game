package com.morefun.XSanGo.faction;

/**
 * 公会战胜利记录，控制不可重复挑战
 * 
 * @author lixiongming
 *
 */
public class GvgLog {
	/** 胜利者 */
	public String winRoleId;
	/** 失败者 */
	public String failRoleId;
	
	public GvgLog(String winRoleId, String failRoleId) {
		this.winRoleId = winRoleId;
		this.failRoleId = failRoleId;
	}

}
