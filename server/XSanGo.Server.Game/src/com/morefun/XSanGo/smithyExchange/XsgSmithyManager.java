package com.morefun.XSanGo.smithyExchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.ArenaRank.ArenaMallRefreshT;
import com.morefun.XSanGo.ArenaRank.ArenaMallStoreT;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.XsgRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.NumberUtil;


public class XsgSmithyManager {
	private static XsgSmithyManager instance = new XsgSmithyManager();

	private static final Log log = LogFactory.getLog(XsgSmithyManager.class);
	
	// 竞技场紫装商城兑换
	private Map<Integer, SmithyMallStoreT> MallStoreMap1 = new HashMap<Integer, SmithyMallStoreT>();
	private Map<Integer, SmithyMallStoreT> MallStoreMap2 = new HashMap<Integer, SmithyMallStoreT>();
	private Map<Integer, SmithyMallStoreT> MallStoreMapAll = new HashMap<Integer, SmithyMallStoreT>();
	private Map<Integer, SmithyMallRefreshT> MallRefreshMap = new HashMap<Integer, SmithyMallRefreshT>();
	private List<SmithyNPCWordT> wordList = new ArrayList<SmithyNPCWordT>();
	
	// 竞技场蓝装商城兑换
	private Map<Integer, SmithyMallStoreT> blueMallStoreMap1 = new HashMap<Integer, SmithyMallStoreT>();
	private Map<Integer, SmithyMallStoreT> blueMallStoreMap2 = new HashMap<Integer, SmithyMallStoreT>();
	private Map<Integer, SmithyMallStoreT> blueMallStoreMapAll = new HashMap<Integer, SmithyMallStoreT>();
	private Map<Integer, SmithyMallRefreshT> blueMallRefreshMap = new HashMap<Integer, SmithyMallRefreshT>();
	private List<SmithyNPCWordT> blueWordList = new ArrayList<SmithyNPCWordT>();
	
	public static XsgSmithyManager getInstance() {
		return instance;
	}

	public XsgSmithyManager() {
		//加载铁匠铺紫装兑换脚本
		this.setMallRefreshMap(); // 商城刷新
		this.setMallStoreMap1(); // 商城固定道具
		this.setMallStoreMap2(); // 商城随机道具
		this.setWordList();

		MallStoreMapAll.putAll(this.getMallStoreMap1());
		MallStoreMapAll.putAll(this.getMallStoreMap2());
		
		//加载铁匠铺蓝装兑换脚本
		this.setBlueMallRefreshMap(); // 商城刷新
		this.setBlueMallStoreMap1(); // 商城固定道具
		this.setBlueMallStoreMap2(); // 商城随机道具
		this.setBlueWordList();

		blueMallStoreMapAll.putAll(this.getBlueMallStoreMap1());
		blueMallStoreMapAll.putAll(this.getBlueMallStoreMap2());
		
	}

	private void setBlueMallStoreMap2() {
		for (SmithyMallStoreT mallStore : ExcelParser.parse("蓝装随机刷新", //$NON-NLS-1$
				SmithyMallStoreT.class)) {
			if(!"".equals(mallStore.ItemID)){
				blueMallStoreMap2.put(mallStore.ID, mallStore);
			}
		}
	
	}

	private void setBlueMallStoreMap1() {
		for (SmithyMallStoreT mallStore : ExcelParser.parse("蓝装固定刷新", //$NON-NLS-1$
				SmithyMallStoreT.class)) {
			if(!"".equals(mallStore.ItemID)){
				blueMallStoreMap1.put(mallStore.ID, mallStore);
			}
		}
	}

	private void setBlueWordList() {
		this.blueWordList = ExcelParser.parse("蓝装NPC台词",SmithyNPCWordT.class);
		
	}

	private void setBlueMallRefreshMap() {
		for (SmithyMallRefreshT mallRefresh : ExcelParser
				.parse("蓝装刷新费用",SmithyMallRefreshT.class)) {
			blueMallRefreshMap.put(mallRefresh.Time, mallRefresh);
		}		
	}

	private void setWordList() {
		this.wordList = ExcelParser.parse("NPC台词",SmithyNPCWordT.class);
		
	}

	public Map<Integer, SmithyMallStoreT> getMallStoreMap1() {
		return MallStoreMap1;
	}
	
	public Map<Integer, SmithyMallStoreT> getMallStoreMap2() {
		return MallStoreMap2;
	}
	
	private void setMallStoreMap2() {
		for (SmithyMallStoreT mallStore : ExcelParser.parse("随机刷新", //$NON-NLS-1$
				SmithyMallStoreT.class)) {
			if(!"".equals(mallStore.ItemID)){
				MallStoreMap2.put(mallStore.ID, mallStore);
			}
		}
	}

	private void setMallStoreMap1() {
		for (SmithyMallStoreT mallStore : ExcelParser.parse("固定刷新", //$NON-NLS-1$
				SmithyMallStoreT.class)) {
			if(!"".equals(mallStore.ItemID)){
				MallStoreMap1.put(mallStore.ID, mallStore);
			}
		}
	}

	private void setMallRefreshMap() {
		for (SmithyMallRefreshT mallRefresh : ExcelParser
				.parse("刷新费用",SmithyMallRefreshT.class)) {
			MallRefreshMap.put(mallRefresh.Time, mallRefresh);
		}		
	}
	
	public Map<Integer, SmithyMallStoreT> getMallStoreMapAll() {
		return MallStoreMapAll;
	}
	
	public Map<Integer, SmithyMallRefreshT> getMallRefreshMap() {
		return MallRefreshMap;
	}

	public ISmithyExchangeController create(XsgRole xsgRole, Role db) {
		return new SmithyExchangeController(xsgRole,db);
	}
	
	public String generateRandomWord(){
		int random = NumberUtil.random(0, this.wordList.size());
		return this.wordList.get(random).word;
	}
	
	public Map<Integer, SmithyMallStoreT> getBlueMallStoreMapAll() {
		return blueMallStoreMapAll;
	}
	
	public Map<Integer, SmithyMallRefreshT> getBlueMallRefreshMap() {
		return blueMallRefreshMap;
	}
	
	public Map<Integer, SmithyMallStoreT> getBlueMallStoreMap1() {
		return blueMallStoreMap1;
	}
	
	public Map<Integer, SmithyMallStoreT> getBlueMallStoreMap2() {
		return blueMallStoreMap2;
	}
	
	public String generateBlueRandomWord(){
		int random = NumberUtil.random(0, this.blueWordList.size());
		return this.blueWordList.get(random).word;
	}
}
