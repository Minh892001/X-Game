/**
 * 
 */
package com.morefun.XSanGo.copy;

import java.util.Date;
import java.util.Map;

import com.XSanGo.Protocol.CaptureView;
import com.morefun.XSanGo.reward.TcResult;

/**
 * 副本挑战结果
 * 
 * @author sulingyun
 * 
 */
public class CopyChallengeResult {
	/** 副本模板 */
	public int templateId;
	/** 星级评定 */
	public byte star;
	/** 副本掉落 */
	public TcResult rewards;
	/** 武将经验 */
	public Map<String, Integer> heroExpMap;
	/** 俘虏数据 */
	public CaptureView captureView;
	/** 初始武将数量 */
	public byte orignalHeroCount;
	/** 战斗是否结束 */
	public boolean copyEnd;
	/** 是否购买过战利品 */
	public boolean buyItem;
	/** 副本开始时间 */
	public Date beginTime;
	/** 副本结束时间 */
	public Date endTime;
}
