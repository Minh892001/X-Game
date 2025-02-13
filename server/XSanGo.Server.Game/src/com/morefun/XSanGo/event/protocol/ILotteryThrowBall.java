package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 大富温摇骰子事件
 * @author sunjie
 *
 */
@signalslot
public interface ILotteryThrowBall {
	/**
	 * 
	 * @param type 类型
	 * @param frontGridId 之前的格子位
	 * @param curGridId 当前格子位
	 * @param throwPoint 掷出的点数
	 * @param frontScore 之前的积分
	 * @param frontSpecialScore 之前的任性值
	 * @param addScore 增加的积分
	 * @param addSpecialScore 增加的任性值
	 * @param isCycle 是否巡回
	 * @param cycleNum 当前巡回圈数
	 * @param throwNum 投掷总次数
	 * @param autoNum  遥控筛子剩余数量
	 */
	public void onThrowBall(int type,int frontGridId,int curGridId,int throwPoint,int frontScore,int frontSpecialScore,int addScore,int addSpecialScore,int isCycle,int cycleNum,int throwNum,int autoNum);
}