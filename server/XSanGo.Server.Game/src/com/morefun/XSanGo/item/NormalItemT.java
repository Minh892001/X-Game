/**
 * 
 */
package com.morefun.XSanGo.item;

import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 道具模板类
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/道具脚本.xls", beginRow = 2, sheetName = "道具数据")
public class NormalItemT extends AbsItemT {
	// private static Pattern PiecePatten = Pattern
	// .compile("^[A-Za-z0-9]+-{1}[0-9]{1}$");

	@ExcelColumn(index = 1)
	public int itemTypeID;

	@ExcelColumn(index = 2)
	public String id;

	@ExcelColumn(index = 3)
	public String name;

	@ExcelColumn(index = 4)
	public int color;

	@ExcelColumn(index = 5)
	public int canUse;

	@ExcelColumn(index = 6)
	public String useCode;

	@ExcelColumn(index = 7)
	public String useValue;

	@ExcelColumn(index = 8)
	public int overlay;

	@ExcelColumn(index = 9)
	public int canSale;

	@ExcelColumn(index = 10)
	public int jinbiPrice;

	@ExcelColumn(index = 11)
	public float yuanbaoPrice;

	@ExcelColumn(index = 17)
	public int canAuction;

	@ExcelColumn(index = 18)
	public int useLevel;

	@ExcelColumn(index = 19)
	public int useVipLevel;

	/** 限时类型 0不限时 1时长限时 2日期限时 */
	@ExcelColumn(index = 20)
	public byte limitType;

	/** 限时时长 天数 */
	@ExcelColumn(index = 21)
	public int limitTime;

	/** 限时日期 时间格式 yyyy-MM-dd HH:mm:ss */
	@ExcelColumn(index = 22)
	public String limitDate;

	public int getItemTypeID() {
		return itemTypeID;
	}

	public void setItemTypeID(int itemTypeID) {
		this.itemTypeID = itemTypeID;
	}

	@Override
	public boolean canOverlay() {
		return this.overlay == 1;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.DefaultItemType;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean canSale() {
		return this.canSale == 1;
	}

	@Override
	public int getSaleMoney() {
		return this.jinbiPrice;
	}

	@Override
	public int getPieceCount() {
		return 0;
	}

	@Override
	public float getYuanbaoPrice() {
		return this.yuanbaoPrice;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public boolean canUse() {
		return this.canUse == 1;
	}

	@Override
	public QualityColor getColor() {
		return QualityColor.valueOf(color);
	}

	/**
	 * 是否需要额外的材料
	 * */
	@Override
	public int needExtra() {
		return 0;
	}

	@Override
	public Property[] extraItemAndNum() {
		return null;
	}

	/**
	 * 是否限时道具
	 * 
	 * @return
	 */
	public boolean isLimit() {
		return limitType > 0;
	}
}
