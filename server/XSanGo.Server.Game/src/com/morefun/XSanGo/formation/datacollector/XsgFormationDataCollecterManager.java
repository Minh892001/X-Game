/**
 * 
 */
package com.morefun.XSanGo.formation.datacollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.GrowableProperty;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.db.game.BattlePowerSnapshot;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.expression.Criteria;

/**
 * @author sulingyun
 * 
 */
public final class XsgFormationDataCollecterManager {
	private final static Log logger = LogFactory.getLog(XsgFormationDataCollecterManager.class);
	
	private static String PowerIndexName = "power";
	private static String LevelIndexName = "level";
	private static XsgFormationDataCollecterManager instance = new XsgFormationDataCollecterManager();

	public static XsgFormationDataCollecterManager getInstance() {
		return instance;
	}

	private Ehcache cache;

	private XsgFormationDataCollecterManager() {
		this.cache = XsgCacheManager.getInstance().getCache("formationCollecter");
		List<BattlePowerSnapshot> list = SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).findAll(
				BattlePowerSnapshot.class);
		for (BattlePowerSnapshot snapshot : list) {
			this.cache.put(new Element(snapshot.getRoleId(), snapshot));
		}
	}

	/**
	 * 战力采集
	 * 
	 * @param role
	 */
	public void onCollectEventEmit(IRole role) {
		Element element = this.cache.get(role.getRoleId());
		if (element != null && ((BattlePowerSnapshot) element.getObjectValue()).getPower() >= role.getCachePower()) {
			return;
		}

		try {
			PvpOpponentFormationView newData = role.getFormationControler().getPvpOpponentFormationView(
					role.getFormationControler().getDefaultFormation().getId());
			this.addOrUpdate(role, newData);
		} catch (IOException e) {
			LogManager.error(e);
		}
	}

	private void addOrUpdate(IRole role, PvpOpponentFormationView pofv) throws IOException {
		for (HeroView hv : pofv.heros) {
			// 属性值合并计算，否则反序列化时会有数据丢失
			for (int i = 0; i < hv.properties.length; i++) {
				GrowableProperty gp = (GrowableProperty) hv.properties[i];
				gp.value += gp.grow;
				gp.grow = 0;// 这行对逻辑无影响，纯粹为了节省空间
			}

			// 没必要数据置空，减少数据量
			hv.exp = 0;
			hv.levelupExp = 0;
			hv.hpGrow = 0;
			hv.powerGrow = 0;
			hv.intelGrow = 0;
			hv.qualityAddPercents = 0;
			hv.equips = null;
			hv.activeRelations = null;
			hv.attendants = null;
			hv.levelupRelations = null;
		}
		final BattlePowerSnapshot bps = new BattlePowerSnapshot(role.getRoleId(), role.getLevel(), role.getName(),
				pofv.view.battlePower, TextUtil.gzip(TextUtil.GSON.toJson(pofv)));
		this.cache.put(new Element(role.getRoleId(), bps));
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).attachDirty(bps);
			}
		});
	}

	public IFormationDataCollecter createFormationCollecter(IRole rt, Role db) {
		return new FormationDataCollecter(rt, db);
	}

	/**
	 * 查询战力快照数据
	 * 
	 * @param startPower
	 *            最小战力
	 * @param endPower
	 *            最大战力
	 * @param startLevel
	 *            最低等级
	 * @param endLevel
	 *            最大等级
	 * @return
	 */
	public List<BattlePowerSnapshotQueryResult> queryFormationView(int startPower, int endPower, int startLevel,
			int endLevel) {
		Query query = this.cache.createQuery().includeValues();
		Criteria criteria = this.cache.getSearchAttribute(PowerIndexName).between(startPower, endPower)
				.and(this.cache.getSearchAttribute(LevelIndexName).between(startLevel, endLevel));
		List<BattlePowerSnapshot> snapshotList = XsgCacheManager.parseCacheValue(query.addCriteria(criteria).execute()
				.all(), BattlePowerSnapshot.class);
		List<BattlePowerSnapshotQueryResult> result = new ArrayList<BattlePowerSnapshotQueryResult>();
		for (BattlePowerSnapshot snapshot : snapshotList) {
			try {
				BattlePowerSnapshotQueryResult  snapshotResult = this.transferSnapshot(snapshot);
				if (snapshotResult != null) {
					result.add(snapshotResult);
				}
			} catch (IOException e) {
				LogManager.error(e);
			}
		}

		return result;
	}

	/**
	 * 查询指定玩家的战力快照数据
	 * 
	 * @param roleId
	 * @return
	 */
	public BattlePowerSnapshotQueryResult queryFormationViewById(String roleId) {
		Element element = this.cache.get(roleId);
		try {
			return element == null ? null : this.transferSnapshot((BattlePowerSnapshot) element.getObjectValue());
		} catch (IOException e) {
			LogManager.error(e);
			return null;
		}
	}

	/**
	 * 将玩家战力快照转换成查询结果对象
	 * 
	 * @param snapshot
	 * @return
	 * @throws IOException
	 */
	private BattlePowerSnapshotQueryResult transferSnapshot(BattlePowerSnapshot snapshot) throws IOException {
		try {
			return new BattlePowerSnapshotQueryResult(snapshot.getRoleId(), snapshot.getPower(), TextUtil.GSON.fromJson(
				TextUtil.ungzip(snapshot.getData()), PvpOpponentFormationView.class));
		} catch (Exception e) {
			logger.error("transferSnapshot ERROR>>>>>>>>" + snapshot.getRoleId() + ":" + snapshot.getRoleName());
			return null;
		}
	}
}