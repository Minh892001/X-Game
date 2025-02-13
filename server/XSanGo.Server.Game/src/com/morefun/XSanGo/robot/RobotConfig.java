package com.morefun.XSanGo.robot;

import java.util.HashMap;
import java.util.Map;

public class RobotConfig {
	public String name;
	public int sex;
	public int level;
	public String headImage;
	public Map<Integer, RobotHero> formationMap = new HashMap<Integer, RobotHero>(5);
	public int robotType;// 0-竞技场 1-群雄
	public int vipLevel;// 群雄才有
}
