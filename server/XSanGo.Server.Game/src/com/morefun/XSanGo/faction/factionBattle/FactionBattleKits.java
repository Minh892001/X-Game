/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleKits
 * 功能描述：
 * 文件名：FactionBattleKits.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

/**
 * 公会战锦囊
 * 
 * @author zwy
 * @since 2016-1-9
 * @version 1.0
 */
public class FactionBattleKits {

	/** 编号 */
	private int id;

	/** 数量 */
	private int num;

	/** 到期时间 默认0 */
	private long time;

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the num.
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num The num to set.
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return Returns the time.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time The time to set.
	 */
	public void setTime(long time) {
		this.time = time;
	}
}
