/**
 * 
 */
package com.morefun.XSanGo.db.game;

import org.springframework.context.ApplicationContext;

/**
 * @author linyun.su
 * 
 */
public class CombineFactionDAO extends FactionDAO {
	public static CombineFactionDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (CombineFactionDAO) ctx.getBean("CombineFactionDAO");
	}
}
