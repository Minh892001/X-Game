/**
 * 
 */
package com.morefun.XSanGo.item;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.EquipPosition;
import com.XSanGo.Protocol.EquipType;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.script.CellWrap;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 装备物品模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/装备脚本.xls", beginRow = 2, sheetName = "装备模板")
public class EquipItemT extends AbsItemT {
	@ExcelColumn(index = 1)
	public int type;

	@ExcelColumn(index = 2)
	public String id;

	/** 套装ID */
	@ExcelColumn(index = 3)
	public int suitId;

	@ExcelColumn(index = 4)
	public String name;

	@ExcelColumn(index = 6)
	public int quatityColor;

	@ExcelColumn(index = 8)
	public int pieceCount;

	@ExcelColumn(index = 10)
	public int price;

	@ExcelColumn(index = 11)
	public int yuanbaoPrice;

	@ExcelComponet(index = 12, columnCount = 2, size = 2)
	public PropertyT[] majorProperties;

	@ExcelComponet(index = 16, columnCount = 2, size = 2)
	public PropertyT[] attachProperties;

	@ExcelComponet(index = 20, columnCount = 1, size = 12)
	// public GrowRange[] grows;
	public CellWrap[] grows;

	@ExcelColumn(index = 32)
	public String desc;

	@ExcelColumn(index = 36)
	public int isAuction;

	/**
	 * 是否需要额外的材料
	 * */
	@ExcelColumn(index = 38)
	public int needExtra;

	/**
	 * 额外材料1
	 * */
	@ExcelColumn(index = 39)
	public String extraItemAndNum1;

	/**
	 * 额外材料2
	 * */
	@ExcelColumn(index = 40)
	public String extraItemAndNum2;

	private Property[] extraItems;

	public EquipType getType() {
		return EquipType.valueOf(type - 1);
	}

	public EquipPosition getEquipPos() {
		int temp = type == 7 ? 2 : 1;

		return EquipPosition.valueOf(type - temp);
	}

	@Override
	public boolean canOverlay() {
		return false;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.EquipItemType;
	}

	@Override
	public boolean canSale() {
		return true;
	}

	@Override
	public int getSaleMoney() {
		return this.price;
	}

	/**
	 * 是否有两个主属性
	 * 
	 * @return
	 */
	public boolean hasSecondMajorProperty() {
		return this.majorProperties[1].isEffective();
	}

	@Override
	public QualityColor getColor() {
		return QualityColor.valueOf(this.quatityColor);
	}

	@Override
	public int getPieceCount() {
		return this.pieceCount;
	}

	@Override
	public float getYuanbaoPrice() {
		return this.yuanbaoPrice;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 是否需要额外的材料
	 * */
	@Override
	public int needExtra() {
		return needExtra;
	}

	/**
	 * 额外材料1的数量
	 * */
	@Override
	public Property[] extraItemAndNum() {
		if (this.extraItems == null) {
			List<Property> list = new ArrayList<Property>();
			if (TextUtil.isNotBlank(extraItemAndNum1)) {
				String[] array1 = extraItemAndNum1.split("\\*");
				list.add(new Property(array1[0], NumberUtil.parseInt(array1[1])));
			}
			if (TextUtil.isNotBlank(extraItemAndNum2)) {
				String[] array2 = extraItemAndNum2.split("\\*");
				list.add(new Property(array2[0], NumberUtil.parseInt(array2[1])));
			}
			extraItems = list.toArray(new Property[0]);
		}

		return this.extraItems;
	}
}
