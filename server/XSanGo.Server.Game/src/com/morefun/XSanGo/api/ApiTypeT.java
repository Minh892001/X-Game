package com.morefun.XSanGo.api;

import java.util.Map;
import java.util.TreeMap;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * api类型表
 * 
 * @author zhangwei02.zhang
 * @since 2015年11月11日
 * @version 1.0
 */
@ExcelTable(fileName = "script/活动和礼包/活动列表.xls", sheetName = "活动API配置", beginRow = 2)
public class ApiTypeT {

	/** api编号 */
	@ExcelColumn(index = 0)
	public int apiId;

	/** 功能编号 */
	@ExcelColumn(index = 3)
	public int funcId;

	/** 参数1 */
	@ExcelColumn(index = 4)
	public String param1;

	/** 参数2 */
	@ExcelColumn(index = 5)
	public String param2;

	/** 是否跳转, 1:有跳转; 2:无跳转 */
	@ExcelColumn(index = 6)
	public int isGo;

	/** 跳转标识 */
	@ExcelColumn(index = 7)
	public String goTo;

	/** 是否计算历史数据 */
	@ExcelColumn(index = 8)
	public byte isCalculationHistory;

	/** 功能接口 */
	private String funcInterface;

	/** 奖励和活动条件 */
	private Map<Integer, ApiRewardT> rewardsMap = new TreeMap<Integer, ApiRewardT>();

	/** 最大条件的奖励脚本对象 */
	private ApiRewardT maxApiRewardT;

	public String getFuncInterface() {
		return funcInterface;
	}

	public void setFuncInterface(String funcInterface) {
		this.funcInterface = funcInterface;
	}

	public Map<Integer, ApiRewardT> getRewardsMap() {
		return rewardsMap;
	}

	public void addRewards(ApiRewardT rt) {
		rewardsMap.put(rt.rewardID, rt);
	}

	/**
	 * @return Returns the maxApiRewardT.
	 */
	public ApiRewardT getMaxApiRewardT() {
		return maxApiRewardT;
	}

	/**
	 * @param maxApiRewardT The maxApiRewardT to set.
	 */
	public void setMaxApiRewardT(ApiRewardT maxApiRewardT) {
		this.maxApiRewardT = maxApiRewardT;
	}
}
