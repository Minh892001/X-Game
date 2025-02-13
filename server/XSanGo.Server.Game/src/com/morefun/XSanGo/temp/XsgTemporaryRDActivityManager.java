/**
 * 
 */
package com.morefun.XSanGo.temp;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LogManager;

/**
 * 临时运营活动管理类
 * 
 * @author sulingyun
 *
 */
public class XsgTemporaryRDActivityManager {
	private static XsgTemporaryRDActivityManager instance = new XsgTemporaryRDActivityManager();

	public static XsgTemporaryRDActivityManager getInstance() {
		return instance;
	}

	private List<TemporaryWrapperT> wrapperList;

	private XsgTemporaryRDActivityManager() {
		this.wrapperList = ExcelParser.parse(TemporaryWrapperT.class);
		CollectionUtil.removeWhere(this.wrapperList,
				new IPredicate<TemporaryWrapperT>() {

					@Override
					public boolean check(TemporaryWrapperT item) {
						return item.open != 1;
					}
				});
	}

	/**
	 * 创建运营临时活动加壳对象
	 * 
	 * @param rt
	 */
	public void createWrapperForRole(IRole rt) {
		try {
			for (TemporaryWrapperT template : this.wrapperList) {
				Class<?> meta = Class.forName(template.className);

				Constructor con = meta
						.getConstructor(IRole.class, String.class,Date.class,Date.class);
				con.setAccessible(true);
				con.newInstance(rt, template.params, template.beginTime,
						template.endTime);
			}
		} catch (Exception e) {
			LogManager.error(e);
		}
	}
}
