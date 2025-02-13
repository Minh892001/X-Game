/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.battle;

import com.morefun.XSanGo.formation.IFormation;

/**
 * 战斗接口
 * 
 * @author Su LingYun
 * 
 */
public interface IBattle {

	/**
	 * 执行一场战斗
	 * 
	 * @param left
	 *            战斗发起方
	 * @param right
	 *            战斗接受方
	 */
	boolean execute(IFormation left, IFormation right);
}
