/**
 * 
 */
package com.morefun.XSanGo.item;

import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 阵法书模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/道具相关/道具脚本.xls", beginRow = 2, sheetName = "阵法脚本")
public class FormationBuffItemT extends AbsItemT {
	@ExcelColumn(index = 1)
	public String id;

	@ExcelColumn(index = 2)
	public String name;

	@ExcelColumn(index = 4)
	public int color;

	@ExcelColumn(index = 6)
	public int pieceCount;

	@ExcelColumn(index = 9)
	public int yuanbaoPrice;

	@ExcelComponet(index = 10, columnCount = 2, size = 4)
	public PropertyT[] properties;

	@Override
	public boolean canOverlay() {
		return false;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.FormationBuffItemType;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean canSale() {
		return false;
	}

	@Override
	public int getSaleMoney() {
		return 0;
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
}
