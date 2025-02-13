/**
 * 
 */
package com.morefun.XSanGo.onlineAward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

/**
 * 在线礼包 全局管理
 * 
 * @author 吕明涛
 * 
 */
public class XsgOnlineAwardManager {
	private static XsgOnlineAwardManager instance = new XsgOnlineAwardManager();

	// 根据等级差，掠夺概率 模板数据
	private TreeMap<Integer, List<OnlineAwardT>> onlineAwardLevelMap;
	private Map<Integer, OnlineAwardT> onlineAwardMap;

	public static XsgOnlineAwardManager getInstance() {
		return instance;
	}

	public XsgOnlineAwardManager() {
		//初始化
		this.onlineAwardLevelMap = new TreeMap<Integer, List<OnlineAwardT>>();
		this.onlineAwardMap = new HashMap<Integer, OnlineAwardT>();
		
		loadOnlineRewardScript();
	}
	
	/** 加载在线礼包配置 */
	public void loadOnlineRewardScript() {
		this.onlineAwardLevelMap.clear();
		this.onlineAwardMap.clear();
		
		//在线时长礼包 模板数据 
		//根据等级区分
		List<OnlineAwardT> onlineAwardList = ExcelParser.parse(OnlineAwardT.class);
		for(OnlineAwardT award : onlineAwardList) {
			List<OnlineAwardT> ReqLvList = onlineAwardLevelMap.get(award.ReqLvl);
			if(ReqLvList == null) {
				ReqLvList = new ArrayList<OnlineAwardT>();
			}

			ReqLvList.add(award);
			onlineAwardLevelMap.put(award.ReqLvl, ReqLvList);

			onlineAwardMap.put(award.GiftId, award);
		}
	}

	/**
	 * 在线礼包 控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public IOnlineAwardControler create(IRole roleRt, Role roleDB) {
		return new OnlineAwardControler(roleRt, roleDB);
	}

	public TreeMap<Integer, List<OnlineAwardT>> getOnlineAwardLevelMap() {
		return onlineAwardLevelMap;
	}

	public OnlineAwardT getOnlineAward(int giftId) {
		return onlineAwardMap.get(giftId);
	}
}
