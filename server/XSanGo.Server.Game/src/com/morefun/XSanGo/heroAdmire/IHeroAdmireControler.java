/**
 * 
 */
package com.morefun.XSanGo.heroAdmire;

import com.XSanGo.Protocol.AdmireView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 名将仰慕 
 * @author 吕明涛
 * 
 */
public interface IHeroAdmireControler extends IRedPointNotable {
	
	/**
	 * 显示名将仰慕界面
	 */
	AdmireView selectAdmireShow() throws NoteException;
	
	/**
	 * 选择名将
	 */
	void chooseHero(int heroId) throws NoteException;
	
	/**
	 * 更换名将
	 */
	void exchangeHero(int heroId) throws NoteException;
	
	/**
	 * 清空名将仰慕
	 * @throws NoteException
	 */
	void clearHero() throws NoteException;
	
	/**
	 * 仰慕 名将
	 * @return 仰慕之后，名将的仰慕值 
	 */
	int presentHero(int id) throws NoteException;
	
	/**
	 * 召唤名将
	 */
	int summonHero() throws NoteException;
	
	/**
	 * 下线时重置红点为未通知状态
	 */
	void resetRedPoint();
}
