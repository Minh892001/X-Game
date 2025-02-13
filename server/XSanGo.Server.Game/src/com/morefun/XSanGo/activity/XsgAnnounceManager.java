/**
 * 
 */
package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

/**
 * 活动公告管理
 * 
 * @author sulingyun
 * 
 */
public class XsgAnnounceManager {
	private static XsgAnnounceManager instance = new XsgAnnounceManager();

	public static XsgAnnounceManager getInstance() {
		return instance;
	}

	private Map<Integer, AnnounceT> announceMap;

	private XsgAnnounceManager() {
		loadAnnounceScript();
	}

	/**
	 * 加载公告脚本
	 */
	public void loadAnnounceScript() {
		List<AnnounceT> list = ExcelParser.parse(AnnounceT.class);
		this.announceMap = new HashMap<Integer, AnnounceT>();
		for (AnnounceT announceT : list) {
			if (!announceT.isOpen()) {
				continue;
			}

			this.announceMap.put(announceT.id, announceT);
		}
	}

	public List<AnnounceT> getAllAnnounceT() {
		long now = System.currentTimeMillis();
		List<AnnounceT> list = new ArrayList<AnnounceT>();
		for (AnnounceT announceT : this.announceMap.values()) {
			if (now < announceT.endTime.getTime() && now > announceT.beginTime.getTime()) {
				list.add(announceT);
			}
		}
		return list;
	}

	public IAnnounceControler createAnnounceControler(IRole rt, Role db) {
		return new AnnounceControler(rt, db);
	}
}
