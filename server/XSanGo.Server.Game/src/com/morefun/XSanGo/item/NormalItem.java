/**
 * 
 */
package com.morefun.XSanGo.item;

import java.util.Calendar;

import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NormalItemExtendsView;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 普通物品类
 * 
 * @author sulingyun
 * 
 */
public class NormalItem extends AbsItem {
	
	/** 数据库扩展数据 */
	private NormalData data;

	public NormalItem(RoleItem db, AbsItemT template) {
		super(db, template);
		this.data = TextUtil.GSON.fromJson(db.getAttachData(), NormalData.class);
		if(data == null || data.expirationTime == 0) {// 初始化或者未设置限时
			setExpirationTime();
		}
	}

	@Override
	public ItemView getView() {
		byte limitType = 0;
		String limitTimeStr = "";
		if (getTemplate() instanceof NormalItemT) {
			NormalItemT nt = getTemplate(NormalItemT.class);
			limitType = nt.limitType;
			long limitTime = limitType == 0 ? 0 : (limitType == 1 ? data.expirationTime : DateUtil.parseDate(nt.limitDate).getTime());
			if(limitTime > 0) {
				limitTimeStr = DateUtil.toString(limitTime, "yyyy.MM.dd HH:mm");
			}
		} 
		return new ItemView(getId(), this.getTemplate().getItemType(), this
				.getTemplate().getId(), getNum(), data== null ? "": LuaSerializer.serialize(new NormalItemExtendsView(limitType, limitTimeStr)));
	}
	
	/**
	 * 刷新数据 
	 */
	private void flushData() {
		this.itemDb.setAttachData(TextUtil.GSON.toJson(data));
	}
	
	/**
	 * 设置过期时间
	 * 
	 * @param expirationTime 
	 */
	private void setExpirationTime(long expirationTime) {
		if (data == null) {
			data = new NormalData();
		}
		data.expirationTime = expirationTime;
		flushData();
	}
	
	/**
	 * 设置限时事件
	 *  
	 */
	private void setExpirationTime() {
		if (getTemplate() instanceof NormalItemT) {
			NormalItemT nt = getTemplate(NormalItemT.class);
			if (nt.limitType == 1) {// 只需对时长限时类型的道具进行处理
				Calendar curDate = DateUtil.getFirstSecondOfToday();// 当天第一秒
				long expirationTime = DateUtil.addDays(curDate, nt.limitTime + 1).getTimeInMillis();
				setExpirationTime(expirationTime);
			}
		}
	}
	
	/**
	 * 返回过期时间
	 * 
	 * @return 
	 */
	public long getExpirationTime() {
		if (getTemplate() instanceof NormalItemT) {
			NormalItemT nt = getTemplate(NormalItemT.class);
			if (nt.limitType == 1) {
				return data.expirationTime;
			} else if (nt.limitType == 2) {
				return DateUtil.parseDate(nt.limitDate).getTime();
			}
		}
		return 0;
	}
}

/**
 * 数据库数据
 * 
 * @author zwy
 * @since 2015-12-4 
 * @version 1.0
 */
class NormalData {
	
	/** 到期时间 */
	public long expirationTime;
}
