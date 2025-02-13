/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.stat;


/**
 * @author lusongjie
 * 
 */
public class DragonStatManager {

	private static DragonStatManager instance;
	private IStat statService = new StatService();

	public static DragonStatManager instance() {
		if (instance == null) {
			instance = new DragonStatManager();
		}
		return instance;
	}

	public static void setInstance(DragonStatManager instance) {
		DragonStatManager.instance = instance;
	}

	public IStat getStat() {
		return statService;
	}
	
	public void  registerCallback(IStatCallback cb){
		
	}

}
