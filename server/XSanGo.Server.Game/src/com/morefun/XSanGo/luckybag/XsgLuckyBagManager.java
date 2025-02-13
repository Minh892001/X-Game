package com.morefun.XSanGo.luckybag;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

public class XsgLuckyBagManager {
	private static XsgLuckyBagManager instance = new XsgLuckyBagManager();

	/**
	 * 今日累充
	 */
	private List<LuckyBagItemT> dayItems = new ArrayList<LuckyBagItemT>();

	/**
	 * 本月累充
	 */
	private List<LuckyBagItemT> monthItems = new ArrayList<LuckyBagItemT>();

	public static XsgLuckyBagManager getInstance() {
		return instance;
	}

	private XsgLuckyBagManager() {
		loadLuckyBagScript();
	}
	
	/**
	 * 加载福袋脚本
	 */
	public void loadLuckyBagScript(){
		dayItems = ExcelParser.parse("今日累充", LuckyBagItemT.class);
		for (LuckyBagItemT lt : dayItems) {
			lt.items.add(new Property(lt.item1, lt.num1));
			lt.items.add(new Property(lt.item2, lt.num2));
			lt.items.add(new Property(lt.item3, lt.num3));
			lt.items.add(new Property(lt.item4, lt.num4));
			lt.items.add(new Property(lt.item5, lt.num5));
		}
		monthItems = ExcelParser.parse("本月累充", LuckyBagItemT.class);
		for (LuckyBagItemT lt : monthItems) {
			lt.items.add(new Property(lt.item1, lt.num1));
			lt.items.add(new Property(lt.item2, lt.num2));
			lt.items.add(new Property(lt.item3, lt.num3));
			lt.items.add(new Property(lt.item4, lt.num4));
			lt.items.add(new Property(lt.item5, lt.num5));
		}
	}

	/**
	 * 创建福袋管理模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public ILuckyBagControler createBagControler(IRole roleRt, Role roleDB) {
		return new LuckyBagControler(roleRt, roleDB);
	}

	public List<LuckyBagItemT> getDayBagItems() {
		return this.dayItems;
	}

	public List<LuckyBagItemT> getMonthBagItems() {
		return this.monthItems;
	}
}
