package com.morefun.XSanGo.crossServer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.db.game.RoleTournamentBet;
import com.morefun.XSanGo.db.game.TournamentBetDao;
import com.morefun.XSanGo.script.ExcelParser;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.expression.Criteria;

/**
 * 比武大会，押注管理类
 * 
 * @author guofeng.qin
 */
public class XsgTournamentBetManager {
	private static final Logger log = LoggerFactory.getLogger(XsgTournamentBetManager.class);

	private static final String CacheTournamentBetKey = "tournamentBet";
	private static final String CacheTournamentBetRoleIdAttr = "roleId";
	private static final String CacheTournamentBetFightId = "fightId";

	private static XsgTournamentBetManager instance = new XsgTournamentBetManager();
	private TournamentBetT tournamentBetT = null;

	private Ehcache betCache;
	private TournamentBetDao dao;

	private XsgTournamentBetManager() {
//		loadTournamentBetScript();
		betCache = XsgCacheManager.getInstance().getCache(CacheTournamentBetKey);
		if (betCache == null) {
			log.error("比武大会，押注Cache初始化错误");
			return;
		}
		dao = TournamentBetDao.getFromApplicationContext(ServerLancher.getAc());
		initFromDb();
	}

	public void loadTournamentBetScript() {
		List<TournamentBetT> list = ExcelParser.parse(TournamentBetT.class);
		if (list != null && list.size() > 0) {
			tournamentBetT = list.get(0);
		}
	}

	public TournamentBetT getBetT() {
		return tournamentBetT;
	}

	/**
	 * 清理过期数据
	 * */
	public void clearOldData(int stage) {
		List<String> list = betCache.getKeysWithExpiryCheck();
		if (list != null) {
			for (String id : list) {
				RoleTournamentBet bet = (RoleTournamentBet) betCache.get(id).getObjectValue();
				if (bet.getNum() != stage) {
					delTournamentBet(bet);
				}
			}
		}
	}

	private void initFromDb() {
		List<RoleTournamentBet> list = dao.getAllTourmentBet();
		if (list != null && list.size() > 0) {
			for (RoleTournamentBet bet : list) {
				betCache.put(new Element(bet.getId(), bet));
			}
		}
	}

	public List<RoleTournamentBet> getTournamentBetByRoleId(String roleId) {
		Attribute<String> roleIdAttr = betCache.getSearchAttribute(CacheTournamentBetRoleIdAttr);
		Criteria criteria = roleIdAttr.eq(roleId);
		Query query = betCache.createQuery().includeValues().addCriteria(criteria);
		List<net.sf.ehcache.search.Result> resList = query.execute().all();
		List<RoleTournamentBet> returnList = null;
		if (resList != null && resList.size() > 0) {
			returnList = XsgCacheManager.parseCacheValue(resList, RoleTournamentBet.class);
		}
		return returnList;
	}

	public List<RoleTournamentBet> getTournamentBetByFightId(int fightId) {
		Attribute<Integer> fightIdAttr = betCache.getSearchAttribute(CacheTournamentBetFightId);
		Criteria criteria = fightIdAttr.eq(fightId);
		Query query = betCache.createQuery().includeValues().addCriteria(criteria);
		List<net.sf.ehcache.search.Result> resList = query.execute().all();
		List<RoleTournamentBet> returnList = null;
		if (resList != null && resList.size() > 0) {
			returnList = XsgCacheManager.parseCacheValue(resList, RoleTournamentBet.class);
		}
		return returnList;
	}

	public void addTournamentBet(final RoleTournamentBet bet) {
		betCache.put(new Element(bet.getId(), bet));
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				dao.save(bet);
			}
		});
	}

	public void updateTournamentBet(final RoleTournamentBet... bets) {
		if (bets != null) {
			for (RoleTournamentBet bet : bets) {
				betCache.put(new Element(bet.getId(), bet));
			}
			DBThreads.execute(new Runnable() {
				@Override
				public void run() {
					for (RoleTournamentBet bet : bets) {
						dao.save(bet);
					}
				}
			});
		}
	}

	public void delTournamentBet(final RoleTournamentBet bet) {
		betCache.remove(bet.getId());
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				dao.delete(bet);
			}
		});
	}

	public static XsgTournamentBetManager getInstance() {
		return instance;
	}
}
