package com.morefun.XSanGo.AttackCastle;

/**
 * 关卡
 * 
 * @author qinguofeng
 * @date Jan 28, 2015
 */
public class CastleNodeEntity {
	private int nodeId;
	// 星级
	private int star;
	// 是否领取过奖励
	private int hasAcceptReward;

	public CastleNodeEntity(int nodeId, int star){
		this.nodeId = nodeId;
		this.star = star;
		hasAcceptReward = 0;
	}

	public int getId(){
		return nodeId;
	}

	public int getStar(){
		return star;
	}

	/** 设置玩家领奖标记 */
	public void acceptReward(){
		this.hasAcceptReward = 1;
	}

	public boolean hasAcceptReward(){
		return hasAcceptReward == 1;
	}
}
