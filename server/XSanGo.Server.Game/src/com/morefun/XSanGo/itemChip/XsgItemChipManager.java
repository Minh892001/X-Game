/**
 * 
 */
package com.morefun.XSanGo.itemChip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

/**
 * 碎片的 合成、掠夺、复仇抢回碎片 全局管理
 * 
 * @author 吕明涛
 * 
 */
public class XsgItemChipManager {
	private static XsgItemChipManager instance = new XsgItemChipManager();

	// 炫耀模板
	private List<String> strutStrList;
	// 宝石合成模版数据
	private Map<String, CompoundGemT> gemMap = new HashMap<String, CompoundGemT>();
	/** 合成公式 */
	private Map<String, List<Property>> compoundMap = new HashMap<String, List<Property>>();
	/** 合成材料与成品对照表 */
	private Map<String, List<String>> materialMap = new HashMap<String, List<String>>();

	public static XsgItemChipManager getInstance() {
		return instance;
	}

	public XsgItemChipManager() {
		this.strutStrList = new ArrayList<String>();
		// 炫耀模板 模板数据
		List<ItemStrutT> ItemStrutList = ExcelParser.parse(ItemStrutT.class);
		for (ItemStrutT strutStr : ItemStrutList) {
			strutStrList.add(strutStr.strutStr);
		}

		loadCompoundGemScript();
	}

	private void loadCompoundGemScript() {
		List<CompoundGemT> list = ExcelParser.parse(CompoundGemT.class);
		for (CompoundGemT t : list) {
			gemMap.put(t.id, t);
		}
	}

	public CompoundGemT getCompoundGemt(String id) {
		return gemMap.get(id);
	}

	public List<CompoundGemT> getAllCompoundGemT() {
		return new ArrayList<CompoundGemT>(gemMap.values());
	}

	/**
	 * 创建碎片的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public IItemChipControler createChatControler(IRole roleRt, Role roleDB) {
		return new ItemChipControler(roleRt, roleDB);
	}

	public List<String> getStrutStrList() {
		return strutStrList;
	}

	/**
	 * 分析合成材料，建立相关索引数据
	 */
	public void analyzeCompoundMaterial() {
		this.compoundMap.clear();
		this.materialMap.clear();

		Collection<AbsItemT> templateCollection = XsgItemManager.getInstance().getAllTemplate();
		for (AbsItemT t : templateCollection) {
			String compoundId = t.getId();// 合成物品ID

			if (t.getPieceCount() > 0) {// 通过碎片数量即可知是否为可合成物品的部分
				this.createOrAddCompoundConfig(compoundId, XsgItemManager.getInstance().getPieceTemplateId(compoundId),
						t.getPieceCount());
				this.createOrAddMaterialConfig(XsgItemManager.getInstance().getPieceTemplateId(compoundId), compoundId);

				if (t.getItemType() == ItemType.EquipItemType) {
					Property[] extraMaterial = t.extraItemAndNum();
					if (extraMaterial != null) {
						for (Property p : extraMaterial) {
							this.createOrAddCompoundConfig(compoundId, p.code, p.value);
							this.createOrAddMaterialConfig(p.code, compoundId);
						}
					}
				}
			}
		}

		// 宝石部分
		for (CompoundGemT gem : this.gemMap.values()) {
			this.createOrAddCompoundConfig(gem.id, gem.needItem, gem.needNum);
			this.createOrAddMaterialConfig(gem.needItem, gem.id);
		}
	}

	/**
	 * 创建或添加合成原料与成品的索引配置
	 * 
	 * @param materialId
	 * @param compoundId
	 */
	private void createOrAddMaterialConfig(String materialId, String compoundId) {
		List<String> list = this.materialMap.get(materialId);
		if (list == null) {
			list = new ArrayList<String>();
			this.materialMap.put(materialId, list);
		}

		list.add(compoundId);
	}

	/**
	 * 创建或添加合成公式配置
	 * 
	 * @param compoundId
	 * @param materialId
	 * @param value
	 */
	private void createOrAddCompoundConfig(String compoundId, String materialId, int value) {
		List<Property> list = this.compoundMap.get(compoundId);
		if (list == null) {
			list = new ArrayList<Property>();
			this.compoundMap.put(compoundId, list);
		}

		list.add(new Property(materialId, value));
	}

	/**
	 * 获取所有合成公式
	 * 
	 * @return
	 */
	public Map<String, List<Property>> getAllCompoundConfig() {
		return this.compoundMap;
	}

	/**
	 * 获取原材料对应的成品列表
	 * 
	 * @param material
	 * @return
	 */
	public List<String> getCompoundByMaterial(String material) {
		return this.materialMap.get(material);
	}
}