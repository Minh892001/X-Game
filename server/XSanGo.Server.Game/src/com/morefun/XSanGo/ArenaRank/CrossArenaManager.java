package com.morefun.XSanGo.ArenaRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.CrossArenaCallbackPrx;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 跨服竞技场
 * 
 * @author xiongming.li
 *
 */
public class CrossArenaManager {
	private static CrossArenaManager instance = new CrossArenaManager();

	private CrossArenaCallbackPrx crossArenaPrx;

	private CrossArenaConfT crossArenaConfT;

	private Map<Integer, CrossArenaBuyT> crossArenaBuyMap = new HashMap<Integer, CrossArenaBuyT>();

	private List<CrossArenaMatchT> crossMathTs = new ArrayList<CrossArenaMatchT>();

	private List<CrossArenaFirstWinT> arenaFirstWinTs = new ArrayList<CrossArenaFirstWinT>();

	public CrossArenaManager() {
		crossArenaConfT = ExcelParser.parse(CrossArenaConfT.class).get(0);
		for (CrossArenaBuyT c : ExcelParser.parse(CrossArenaBuyT.class)) {
			crossArenaBuyMap.put(c.count, c);
		}
		crossMathTs = ExcelParser.parse(CrossArenaMatchT.class);
		arenaFirstWinTs = ExcelParser.parse(CrossArenaFirstWinT.class);
	}

	public static CrossArenaManager getInstance() {
		return instance;
	}

	public ICrossArenaControler create(IRole roleRt, Role roleDB) {
		return new CrossArenaControler(roleRt, roleDB);
	}

	public void setCrossArenaCallback(CrossArenaCallbackPrx cb) {
		LogManager.warn("setup cross arena server callback ...");
		crossArenaPrx = cb;
	}

	public CrossArenaCallbackPrx getCrossArenaCbPrx() {
		return crossArenaPrx;
	}

	public boolean isUsable() {
		return crossArenaPrx != null;
	}

	public CrossArenaConfT getCrossArenaConfT() {
		return this.crossArenaConfT;
	}

	public CrossArenaBuyT getCrossArenaBuyT(int count) {
		return this.crossArenaBuyMap.get(count);
	}

	/**
	 * 获取排行首胜奖励
	 * 
	 * @param rank
	 * @return
	 */
	public int getFirstWinItem(int rank) {
		for (CrossArenaFirstWinT w : this.arenaFirstWinTs) {
			if (rank >= w.beginRank && rank <= w.endRank) {
				return NumberUtil.parseInt(w.item.split(":")[1]);
			}
		}
		return 0;
	}

	public List<CrossArenaMatchT> getCrossArenaMatch() {
		return crossMathTs;
	}
}
