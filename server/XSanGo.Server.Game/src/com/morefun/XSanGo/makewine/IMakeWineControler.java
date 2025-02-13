package com.morefun.XSanGo.makewine;

import com.XSanGo.Protocol.AMD_MakeWineInfo_receiveShare;
import com.XSanGo.Protocol.AMD_MakeWineInfo_shareView;
import com.XSanGo.Protocol.AMD_MakeWineInfo_topUp;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 酿酒控制器
 * @author zhuzhi.yang
 *
 */
public interface IMakeWineControler extends IRedPointNotable {

	/**
	 * 酿酒界面
	 * @return
	 */
	String makeWineView() throws NoteException;
	
	/**
	 * 定时领取材料
	 * @throws NoteException
	 */
	String receiveMaterial() throws NoteException;
	
	/**
	 * 领取积分奖励
	 * @throws NoteException
	 */
	String receiveScoreAward() throws NoteException;
	
	/**
	 * 酿酒
	 * @param id合成目标,0:全部酿酒
	 * @param type: 0:酿酒一瓶, 1:全部酿酒,酿完该种类的酒 
	 * @throws NoteException
	 */
	String make(int targetId, int type) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NotEnoughException, NoteException;
	
	/**
	 * 分享界面
	 * onlyFriends：0:全部  1:只看好友 2:自己的分享
	 */
	void shareView(AMD_MakeWineInfo_shareView __cb, int condition, int startIndex) throws NoteException ;
			
	/**
	 * 分享
	 * id:目标
	 * count:多少组
	 */
	void share(int id, int count) throws NoteException ;
	
	/**
	 * 领取分享奖励
	 */
	void receiveShare(AMD_MakeWineInfo_receiveShare __cb, String id, int condition,
			int startIndex) throws NoteException;
	
	/**
	 * 置顶
	 */
	void topUp(AMD_MakeWineInfo_topUp __cb, String id) throws NoteException, NotEnoughYuanBaoException;
	
	/**
	 * 兑换界面
	 */
	String exchangeView() throws NoteException;

	/**
	 * 兑换
	 */
	void exchange(int id, int num) throws NoteException;
	
	/**
	 * 积分排名
	 */
	String scoreRank() throws NoteException;
	
	/**
	 * 积分排名奖励列表
	 */
	String scoreRankAward() throws NoteException;
	
	/**
	 * 查看酒的详情和奖励信息
	 */
	public String wineInfoView() throws NoteException;
	
	/**
	 * 检测数据是否需要重置
	 */
	void checkUpdate();
	
	/**
	 * 查看日志
	 * @param type:1:酿酒 2:领取 3:兑换
	 */
	String seeLog(int type) throws NoteException;
}
