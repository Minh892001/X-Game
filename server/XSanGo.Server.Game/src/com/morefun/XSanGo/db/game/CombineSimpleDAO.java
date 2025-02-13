/**
 * 
 */
package com.morefun.XSanGo.db.game;

import org.springframework.context.ApplicationContext;

/**
 * @author linyun.su
 *
 */
public class CombineSimpleDAO extends SimpleDAO {
	public static CombineSimpleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (CombineSimpleDAO) ctx.getBean("CombineSimpleDAO");
	}
}
