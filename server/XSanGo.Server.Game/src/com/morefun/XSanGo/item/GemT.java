package com.morefun.XSanGo.item;

import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 宝石模版
 * 
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/道具相关/道具脚本.xls", beginRow = 2, sheetName = "宝石")
public class GemT extends AbsItemT {
	/** 宝石类型 */
	@ExcelColumn(index = 1)
	public int type;
	/** 模版ID */
	@ExcelColumn(index = 2)
	public String templateId;
	/** 名称 */
	@ExcelColumn(index = 3)
	public String name;
	/** 宝石品质 */
	@ExcelColumn(index = 4)
	public int color;
	/** 是否可使用 */
	@ExcelColumn(index = 5)
	public int isUse;
	/** 属性名称 */
	@ExcelColumn(index = 6)
	public String property;
	/** 属性值 */
	@ExcelColumn(index = 7)
	public int propValue;
	/** 是否可以叠加 */
	@ExcelColumn(index = 8)
	public int isGroup;
	/** 是否可出售 */
	@ExcelColumn(index = 9)
	public int isSale;
	/** 出售价格 */
	@ExcelColumn(index = 10)
	public int price;
	/** 元宝价格 */
	@ExcelColumn(index = 11)
	public int rmb;
	/** 是否可拍卖 */
	@ExcelColumn(index = 17)
	public int isAuction;
	/** 使用等级 */
	@ExcelColumn(index = 18)
	public int useLevel;

	@Override
	public String getId() {
		return templateId;
	}

	@Override
	public boolean canOverlay() {
		return isGroup == 1;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.DefaultItemType;
	}

	@Override
	public boolean canSale() {
		return isSale == 1;
	}

	@Override
	public int getSaleMoney() {
		return price;
	}

	@Override
	public int getPieceCount() {
		return 0;
	}

	@Override
	public float getYuanbaoPrice() {
		return rmb;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public QualityColor getColor() {
		return QualityColor.valueOf(color);
	}

	@Override
	public int needExtra() {
		return 0;
	}

	@Override
	public Property[] extraItemAndNum() {
		return null;
	}
}
