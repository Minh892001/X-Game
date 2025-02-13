package com.morefun.XSanGo.ArenaRank;

/**
 * 跨服竞技场战斗记录
 * 
 * @author xiongming.li
 *
 */
public class CrossArenaLog {
	public String movieId; // 战报录像ID
	public String name;// 以下是对方的一些信息
	public short level;
	public String icon;
	public int vip;
	public int sex;
	public int serverId;
	public int combat;
	public int rankCurrent; // 战斗之后的当前排名
	public int rankChange; // 排名变化, 负数：下降名次
	public int flag; // 输赢标记 0：输，1：赢
	public String rivalId; // 对手的ID
	public long fightTime; // 发生战报的时间
	public int type; // 战报类型 0：主动发起，1：被动接受挑战, 2: 主动复仇, 3: 被复仇
	public String signature;

	public CrossArenaLog() {

	}

	public CrossArenaLog(String movieId, String name, short level, String icon, int vip, int sex, int serverId,
			int combat, int rankCurrent, int rankChange, int flag, String rivalId, long fightTime, int type,
			String signature) {
		this.movieId = movieId;
		this.name = name;
		this.level = level;
		this.icon = icon;
		this.vip = vip;
		this.sex = sex;
		this.serverId = serverId;
		this.combat = combat;
		this.rankCurrent = rankCurrent;
		this.rankChange = rankChange;
		this.flag = flag;
		this.rivalId = rivalId;
		this.fightTime = fightTime;
		this.type = type;
		this.signature = signature;
	}

}
