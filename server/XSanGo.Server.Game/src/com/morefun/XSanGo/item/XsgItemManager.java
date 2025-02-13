/**
 * 
 */
package com.morefun.XSanGo.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.XSanGo.Protocol.ItemType;
import com.morefun.XSanGo.TemplateNotFoundException;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 物品装备管理类
 * 
 * @author sulingyun
 * 
 */
public class XsgItemManager {

	private static XsgItemManager instance = new XsgItemManager();

	public static XsgItemManager getInstance() {
		return instance;
	}

	private Map<String, AbsItemT> templateMap;
	/** 物品最大叠加数量 */
	private int maxOverlayCount = 1000000000;
	private Map<String, ChestKeyT> keyChestMap;
	/** 道具镶嵌模版 */
	private Map<Integer, EquipGemT> equipGemMap = new HashMap<Integer, EquipGemT>();
	/** 复合宝箱产出 */
	private Map<String, CompositeChestT> compositeChestMap = new HashMap<String, CompositeChestT>();
	/** 道具品质颜色对照表 */
	private Map<Integer, ItemColorT> ColorT = new HashMap<Integer, ItemColorT>();

	/**
	 * 阵法进阶配置
	 */
	private List<FormationBuffAdvancedT> advancedTs = new ArrayList<FormationBuffAdvancedT>();

	private XsgItemManager() {
		this.keyChestMap = CollectionUtil.toMap(ExcelParser.parse(ChestKeyT.class), "chestItem");

		this.templateMap = new HashMap<String, AbsItemT>();
		List<NormalItemT> list = ExcelParser.parse(NormalItemT.class);
		for (NormalItemT t : list) {
			// 脚本加载验证
			if (t.limitType > 0) {
				if (t.limitType == 1) {// 时长类限时
					if (t.limitTime < 1) {
						LogManager.warn(" limittime Less than 1, itemid:" + t.id);
						continue;
					}
					if (t.canOverlay()) {
						LogManager.warn(" item can not overlap, itemid:" + t.id);
						continue;
					}
				}
				if (t.limitType == 2) {
					if (!DateUtil.checkTimeFormat(t.limitDate)) {
						LogManager.warn("limitDate format error, itemId:" + t.id);
						continue;
					}
				}
			}
			this.templateMap.put(t.id, t);
		}

		List<FormationBuffItemT> formationList = ExcelParser.parse(FormationBuffItemT.class);
		for (FormationBuffItemT t : formationList) {
			this.templateMap.put(t.id, t);

			// // 阵法碎片
			// for (int i = 1; i <= t.pieceCount; i++) {
			// NormalItemT subT = this.createPieceTemplate(t, i);
			// this.templateMap.put(subT.id, subT);
			// }
		}

		List<EquipItemT> equipList = ExcelParser.parse(EquipItemT.class);
		for (EquipItemT t : equipList) {
			this.templateMap.put(t.id, t);
			// // 装备碎片
			// for (int i = 1; i <= t.pieceCount; i++) {
			// NormalItemT subT = createPieceTemplate(t, i);
			// this.templateMap.put(subT.id, subT);
			// }
		}

		loadGemScript();
		loadEquipGemScript();
//		loadAwakenItemScript();
		loadCompositeChestScript();

		advancedTs = ExcelParser.parse(FormationBuffAdvancedT.class);
		
		// 加载道具品质颜色对照
		for(ItemColorT ct : ExcelParser.parse(ItemColorT.class)) {
			this.ColorT.put(ct.quality, ct);
		}
	}

	/** 加载 装备脚本.xls->装备镶嵌 */
	public void loadEquipGemScript() {
		equipGemMap.clear();
		List<EquipGemT> equipGemList = ExcelParser.parse(EquipGemT.class);
		for (EquipGemT t : equipGemList) {
			equipGemMap.put(t.qColor, t);
		}
	}

	/** 加载 道具脚本.xls->宝石 */
	public void loadGemScript() {
		List<GemT> gemList = ExcelParser.parse(GemT.class);
		for (GemT t : gemList) {
			templateMap.put(t.getId(), t);
		}
	}

//	/**
//	 * 加载觉醒道具
//	 * 
//	 */
//	private void loadAwakenItemScript() {
//		List<AwakenItemT> itemList = ExcelParser.parse(AwakenItemT.class);
//		for (AwakenItemT t : itemList) {
//			templateMap.put(t.getId(), t);
//		}
//	}

	/**
	 * 加载复合宝箱产出配置
	 * 
	 */
	private void loadCompositeChestScript() {
		List<CompositeChestT> itemList = ExcelParser.parse(CompositeChestT.class);
		for (CompositeChestT t : itemList) {
			if (TextUtil.isNotBlank(t.itemId) && TextUtil.isNotBlank(t.awardItems)) {
				for (String items : StringUtils.split(t.awardItems, ",")) {
					String[] item = StringUtils.split(items, ":");
					PropertyT pt = new PropertyT();
					pt.code = item[0];
					pt.value = Integer.parseInt(item[1]);
					t.items = (PropertyT[]) ArrayUtils.add(t.items, pt);
				}
				t.awardItems = null;
				compositeChestMap.put(t.itemId, t);
			}
		}
	}

	/** 获取某个品质的道具的镶嵌模版 */
	public EquipGemT getEquipGemWithQuality(int q) {
		return equipGemMap.get(q);
	}

	/**
	 * 创建碎片模板数据
	 * 
	 * @param template
	 * @param i
	 * @return
	 */
	// private NormalItemT createPieceTemplate(AbsItemT template, int i) {
	// NormalItemT subT = new NormalItemT();
	// subT.id = TextUtil.format("{0}-{1}", template.getId(), i);
	// subT.name = TextUtil.format("{0}碎片之{1}", template.getId(),
	// TextUtil.getHanzi(i));
	// subT.overlay = 1;
	// if (template.getPieceCount() > 0) {
	// subT.yuanbaoPrice = (template.getYuanbaoPrice()
	// + template.getPieceCount() - 1)
	// / template.getPieceCount();
	// }
	// return subT;
	// }

	public IItemControler createControler(IRole xsgRole, Role db) {
		return new ItemControler(xsgRole, db);
	}

	public AbsItemT findAbsItemT(String templateCode) {
		AbsItemT result = this.templateMap.get(templateCode);
		if (result == null) {
			LogManager.warn(TextUtil.format("Item template [{0}] is not found.", templateCode));
		}

		return result;
	}

	public IItem createItem(RoleItem db) throws TemplateNotFoundException {
		AbsItemT template = this.findAbsItemT(db.getTemplateCode());
		if (template == null) {
			throw new TemplateNotFoundException(db.getTemplateCode());
		}
		if (template instanceof GemT) {
			return new NormalItem(db, template);
		}
		if (template instanceof NormalItemT) {
			return new NormalItem(db, template);
		}
		if (template instanceof FormationBuffItemT) {
			return new FormationBuffItem(db, (FormationBuffItemT) template);
		}
		if (template instanceof EquipItemT) {
			return new EquipItem(db, template);
		}
		//if (template instanceof AwakenItemT) {
		//	return new NormalItem(db, template);
		//}
		return null;
	}

	/**
	 * 指定模板编号物品是否为装备
	 * 
	 * @param code
	 * @return
	 */
	public boolean isEquipTemplate(String code) {
		return this.findAbsItemT(code) instanceof EquipItemT;
	}

	public float getYuanbaoPrice(String templateId) {
		AbsItemT template = this.templateMap.get(templateId);

		return template == null ? 0 : template.getYuanbaoPrice();
	}

	public int getMaxOverlayCount() {
		return this.maxOverlayCount;
	}

	public String[] getItemConfig() {
		List<String> list = new ArrayList<String>();
		for (String key : this.templateMap.keySet()) {
			list.add(key);
			list.add(this.findAbsItemT(key).getName());
		}

		return list.toArray(new String[0]);
	}

	public ItemType getItemType(String code) {
		AbsItemT absT = this.findAbsItemT(code);
		if (absT == null) {
			LogManager.warn(TextUtil.format("Item template [{0}] is not exists.", code));
		}

		return absT == null ? null : absT.getItemType();
	}

	/**
	 * 是否将魂
	 * 
	 * @param templateId
	 * @return
	 */
	public boolean isHeroSoulTemplate(String templateId) {
		AbsItemT template = this.findAbsItemT(templateId);
		if (template instanceof NormalItemT) {
			if ("hero".equals(((NormalItemT) template).useCode)) {
				return true;
			}
		}

		return false;
	}

	public String getKeyForChestItem(String id) {
		ChestKeyT config = this.keyChestMap.get(id);
		return config == null ? null : config.keyItem;
	}

	/**
	 * 复制物品的数据实体，返回值中的角色引用为NULL
	 * 
	 * @param itemDb
	 * @return
	 */
	public RoleItem cloneRoleItem(RoleItem itemDb) {
		RoleItem result = new RoleItem(itemDb.getId(), null, itemDb.getTemplateCode(), itemDb.getNum());
		result.setAttachData(itemDb.getAttachData());
		return result;
	}

	public Map<String, CompositeChestT> getCompositeChestMap() {
		return compositeChestMap;
	}

	/**
	 * 获取所有物品模板数据
	 * 
	 * @return
	 */
	public Collection<AbsItemT> getAllTemplate() {
		return this.templateMap.values();
	}

	/**
	 * 获取合成物品对应碎片的ID
	 * 
	 * @param compoundId
	 * @return
	 */
	public String getPieceTemplateId(String compoundId) {
		return compoundId + "-x";
	}

	/**
	 * 根据等级和类型获取进阶配置
	 * 
	 * @param level
	 * @param type
	 * @return
	 */
	public FormationBuffAdvancedT getByLevelAndType(int level, int type) {
		for (FormationBuffAdvancedT t : this.advancedTs) {
			if (t.level == level && t.type == type) {
				return t;
			}
		}
		return null;
	}

	/**
	 * @return Returns the colorT.
	 */
	public Map<Integer, ItemColorT> getColorT() {
		return ColorT;
	}
}