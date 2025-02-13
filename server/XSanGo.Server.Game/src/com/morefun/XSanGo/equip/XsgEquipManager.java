/**
 * 
 */
package com.morefun.XSanGo.equip;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

/**
 * 装备全局管理类
 * 
 * @author sulingyun
 * 
 */
public class XsgEquipManager {
	private static XsgEquipManager instance = new XsgEquipManager();

	public static XsgEquipManager getInstance() {
		return instance;
	}

	private Map<Integer, EquipStarT> starTMap;
	private Map<Integer, SuitT> suitMap;
	private Map<Integer, EquipLevelFormulaT> equipLevelFormulaTMap;
	private Map<QualityColor, EquipRebuildT> rebuildMap;
	private Map<Integer, EquipSmeltT> smeltMap;

	/**
	 * key：id
	 */
	private Map<Integer, ArtifactT> artifactMap;
	/**
	 * key：id，value：key-等级
	 */
	private Map<Integer, Map<Integer, ArtifactLevelT>> artifactLevelMap;

	private XsgEquipManager() {
		// 强化等级配置
		this.equipLevelFormulaTMap = new HashMap<Integer, EquipLevelFormulaT>();
		for (EquipLevelFormulaT elft : ExcelParser.parse(EquipLevelFormulaT.class)) {
			this.equipLevelFormulaTMap.put(elft.level, elft);
		}

		// 星级配置
		List<EquipStarT> list = ExcelParser.parse(EquipStarT.class);
		this.starTMap = new HashMap<Integer, EquipStarT>();
		for (EquipStarT template : list) {
			this.starTMap.put(template.star, template);
		}

		// 套装配置
		this.suitMap = new HashMap<Integer, SuitT>();
		List<SuitT> suitList = ExcelParser.parse(SuitT.class);
		for (SuitT suitT : suitList) {
			this.suitMap.put(suitT.id, suitT);
		}

		this.rebuildMap = new HashMap<QualityColor, EquipRebuildT>();
		for (EquipRebuildT rebuildT : ExcelParser.parse(EquipRebuildT.class)) {
			this.rebuildMap.put(rebuildT.getColor(), rebuildT);
		}

		// 熔炼配置
		this.smeltMap = new HashMap<Integer, EquipSmeltT>();
		List<EquipSmeltT> smeltList = ExcelParser.parse(EquipSmeltT.class);
		for (EquipSmeltT smelt : smeltList) {
			this.smeltMap.put(smelt.type, smelt);
		}

		artifactMap = new HashMap<>();
		List<ArtifactT> ats = ExcelParser.parse(ArtifactT.class);
		for (ArtifactT a : ats) {
			artifactMap.put(a.id, a);
		}
		artifactLevelMap = new HashMap<>();
		List<ArtifactLevelT> lts = ExcelParser.parse(ArtifactLevelT.class);
		for (ArtifactLevelT l : lts) {
			Map<Integer, ArtifactLevelT> lMap = artifactLevelMap.get(l.id);
			if (lMap == null) {
				lMap = new HashMap<Integer, ArtifactLevelT>();
				artifactLevelMap.put(l.id, lMap);
			}
			lMap.put(l.level, l);
		}
	}

	/**
	 * 查找升星配置
	 * 
	 * @param star
	 * @return
	 */
	public EquipStarT findStarT(int star) {
		return this.starTMap.get(star);
	}

	/**
	 * 查找套装配置
	 * 
	 * @param suitId
	 * @return
	 */
	public SuitT findSuitT(int suitId) {
		return this.suitMap.get(suitId);
	}

	/**
	 * 计算装备强化所需金币
	 * 
	 * @param quatityColor
	 * @param level
	 * @return
	 */
	public int caculateEquipLevelupMoney(QualityColor color, int level) {
		EquipLevelFormulaT elft = this.equipLevelFormulaTMap.get(level + 1);
		if (elft == null) {
			throw new IllegalArgumentException(level + "");
		}

		return elft.getJinbi(color);
	}

	public EquipRebuildT findRebuildT(QualityColor color) {
		return this.rebuildMap.get(color);
	}

	/**
	 * 查找对应成色的装备熔炼配置
	 * */
	public EquipSmeltT findSmeltT(int type) {
		return this.smeltMap.get(type);
	}

	public ArtifactT getArtifactT(int id) {
		return this.artifactMap.get(id);
	}
	
	public Collection<ArtifactT> getAllArtifactT() {
		return this.artifactMap.values();
	}

	public ArtifactLevelT getArtifactLevelT(int id, int level) {
		return this.artifactLevelMap.get(id).get(level);
	}
	
	public IArtifactControler createArtifactControler(IRole iRole,Role role){
		return new ArtifactControler(iRole, role);
	}
}
