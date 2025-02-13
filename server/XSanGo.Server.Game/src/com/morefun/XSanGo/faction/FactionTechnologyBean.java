package com.morefun.XSanGo.faction;

public class FactionTechnologyBean {
	public int id;
	public int level;// 等级
	public int exp;// 经验
	public long studyTime;// 研究时间
	public boolean isRecommend;// 是否推荐
	
	public FactionTechnologyBean(){
		
	}
	
	public FactionTechnologyBean(int id, int level, int exp, long studyTime, boolean isRecommend) {
		this.id = id;
		this.level = level;
		this.exp = exp;
		this.studyTime = studyTime;
		this.isRecommend = isRecommend;
	}
}
