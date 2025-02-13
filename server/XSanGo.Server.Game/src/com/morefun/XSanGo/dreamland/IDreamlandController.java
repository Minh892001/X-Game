/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandController
 * 功能描述：
 * 文件名：IDreamlandController.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import com.XSanGo.Protocol.DreamlandAwardView;
import com.XSanGo.Protocol.DreamlandRankView;
import com.XSanGo.Protocol.DreamlandSceneAwardResult;
import com.XSanGo.Protocol.DreamlandShopItemView;
import com.XSanGo.Protocol.DreamlandShow;
import com.XSanGo.Protocol.DreamlandSweepResult;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;

/**
 * 南华幻境处理接口
 * 
 * @author weiyi.zhao
 * @since 2016-4-19
 * @version 1.0
 */
public interface IDreamlandController {

	/**
	 * 增加南华令
	 * 
	 * @param value
	 */
	void addNanHuaLing(int value);

	/**
	 * 获得南华令
	 * 
	 * @return
	 */
	int getNanHuaLing();

	/**
	 * 南华幻境主界面
	 * 
	 * @param groupId 关卡集编号
	 * @return
	 * @throws NoteException
	 */
	DreamlandShow dreamlandPage(int groupId) throws NoteException;

	/**
	 * 切换关卡集
	 * 
	 * @param groupId 当前关卡图集编号
	 * @param isFront 是否向前
	 * @return
	 * @throws NoteException
	 */
	DreamlandShow dreamlandSwitchSceneGroup(int groupId, boolean isFront) throws NoteException;

	/**
	 * 开始战斗
	 * 
	 * @param sceneId
	 * @return
	 * @throws NoteException
	 */
	String beginDreamland(int sceneId) throws NoteException;

	/**
	 * 结束战斗
	 * 
	 * @param sceneId
	 * @param remainHero 剩余武将数
	 * @return
	 * @throws NoteException
	 */
	DreamlandSceneAwardResult endDreamland(int sceneId, byte remainHero) throws NoteException;

	/**
	 * 扫荡
	 * 
	 * @param sceneId
	 * @return
	 * @throws NoteException
	 */
	DreamlandSweepResult dreamlandSweep(int sceneId) throws NoteException;

	/**
	 * 领取关卡通关奖励
	 * 
	 * @param sceneId
	 * @return
	 * @throws NoteException
	 */
	// DreamlandSceneStarAwardResult drawSceneStarAward(int sceneId) throws
	// NoteException;

	/**
	 * 查看排行榜
	 * 
	 * @return
	 * @throws NoteException
	 */
	DreamlandRankView lookDreamlandRank() throws NoteException;

	/**
	 * 星数奖励界面
	 * 
	 * @return
	 * @throws NoteException
	 */
	DreamlandAwardView dreamlandAwardPage() throws NoteException;

	/**
	 * 领取星数奖励
	 * 
	 * @param star 星数
	 * @return
	 * @throws NoteException
	 */
	IntString[] drawStarAward(int star) throws NoteException;

	/**
	 * 兑换商店界面
	 * 
	 * @return
	 * @throws NoteException
	 */
	DreamlandShopItemView dreamlandShopPage() throws NoteException;

	/**
	 * 刷新兑换商店界面
	 * 
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 */
	DreamlandShopItemView dreamlandRefreshShop() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 商店物品兑换
	 * 
	 * @param id 兑换物品唯一编号
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 */
	IntString buyDreamlandShopItem(int id) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

	/**
	 * 购买每日挑战次数
	 * 
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughYuanBaoException
	 */
	int buyChallengeNum() throws NoteException, NotEnoughYuanBaoException;
}
