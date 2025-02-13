/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IHeroAwakenController
 * 功能描述：
 * 文件名：IHeroAwakenController.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import com.XSanGo.Protocol.HeroBaptizeData;
import com.XSanGo.Protocol.HeroBaptizeResetResult;
import com.XSanGo.Protocol.HeroBaptizeResult;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;

/**
 * 武将觉醒处理接口
 * 
 * @author weiyi.zhao
 * @since 2016-4-13
 * @version 1.0
 */
public interface IHeroAwakenController {

	/**
	 * 武将觉醒
	 * 
	 * @param heroId
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 */
	int awakenHero(String heroId) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 武将洗炼数据
	 * 
	 * @param heroId
	 * @return
	 * @throws NoteException
	 */
	HeroBaptizeData heroBaptizeShow(String heroId) throws NoteException;

	/**
	 * 武将洗炼
	 * 
	 * @param heroId
	 * @param isTenTimes
	 * @return
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	HeroBaptizeResult heroBaptize(String heroId, boolean isTenTimes) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException;

	/**
	 * 洗炼等级升级
	 * 
	 * @param heroId
	 * @return
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	HeroBaptizeData baptizeUpgrade(String heroId) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException;

	/**
	 * 洗炼重置
	 * 
	 * @param heroId
	 * @return
	 * @throws NoteException
	 */
	HeroBaptizeResetResult baptizeReset(String heroId) throws NoteException;
}
