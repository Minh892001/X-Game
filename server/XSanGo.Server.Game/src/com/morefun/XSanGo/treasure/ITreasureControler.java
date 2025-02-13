package com.morefun.XSanGo.treasure;

import com.XSanGo.Protocol.AMD_Treasure_getAccidentFriend;
import com.XSanGo.Protocol.AMD_Treasure_getRescueFriend;
import com.XSanGo.Protocol.AMD_Treasure_rescue;
import com.XSanGo.Protocol.AMD_Treasure_sendRescueMsg;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.TreasureAccidentLog;
import com.XSanGo.Protocol.TreasureRescueFriend;
import com.XSanGo.Protocol.TreasureRescueLog;
import com.XSanGo.Protocol.TreasureView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 寻宝
 * 
 * @author lixiongming
 *
 */
public interface ITreasureControler extends IRedPointNotable {
	/**
	 * 获取寻宝view
	 * 
	 * @return
	 * @throws NoteException
	 */
	TreasureView getTreasureView() throws NoteException;

	/**
	 * 出发
	 * 
	 * @param id
	 * @param heroIds
	 *            ,分割
	 * @throws NoteException
	 */
	void depart(int id, String heroIds) throws NoteException;

	/**
	 * 召回
	 * 
	 * @param id
	 * @throws NoteException
	 */
	void recall(int id) throws NoteException;

	/**
	 * 收获
	 * 
	 * @param id
	 * @throws NoteException
	 */
	ItemView[] gain(int id) throws NoteException;

	/**
	 * 检测武将是否在寻宝队伍中
	 * 
	 * @param heroId
	 *            数据库ID
	 * @return
	 */
	boolean isInTreasureGroup(String heroId);

	/**
	 * 获取援救记录
	 * 
	 * @return
	 * @throws NoteException
	 */
	TreasureRescueLog[] getRescueLog() throws NoteException;

	/**
	 * 获取矿难记录
	 * 
	 * @return
	 * @throws NoteException
	 */
	TreasureAccidentLog[] getAccidentLog() throws NoteException;

	/**
	 * 加速
	 * 
	 * @param id
	 * @throws NoteException
	 */
	void speed(int id) throws NoteException;

	/**
	 * 援救好友矿难
	 * 
	 * @param friendId
	 * @throws NoteException
	 */
	void rescue(AMD_Treasure_rescue __cb, String friendId) throws NoteException;

	/**
	 * 好友援救自己矿难
	 * 
	 * @param friend
	 * @throws NoteException
	 */
	ItemView[] rescueSelf(TreasureRescueFriend friend) throws NoteException;

	/**
	 * 增加援救记录
	 * 
	 * @param rescueLog
	 */
	void addRescueLog(TreasureRescueLog rescueLog);

	/**
	 * 获取发生矿难的好友列表
	 * 
	 * @param __cb
	 * @throws NoteException
	 */
	void getAccidentFriend(AMD_Treasure_getAccidentFriend __cb) throws NoteException;

	/**
	 * 获取可援救自己的好友列表
	 * 
	 * @param __cb
	 * @throws NoteException
	 */
	void getRescueFriend(AMD_Treasure_getRescueFriend __cb) throws NoteException;

	/**
	 * 获取最新的矿难记录
	 * 
	 * @return
	 */
	TreasureAccidentLog getNewAccidentLog();

	/**
	 * 获取可援救次数
	 * 
	 * @return
	 */
	int getCanRescueCount();

	/**
	 * 向好友发送求援私信
	 * 
	 * @param __cb
	 * @param friendIds
	 * @throws NoteException
	 */
	void sendRescueMsg(AMD_Treasure_sendRescueMsg __cb, String friendIds) throws NoteException;

	/**
	 * 保存求援私信
	 * 
	 * @param msg
	 * @throws NoteException
	 */
	void saveRescueMsg(String msg) throws NoteException;
}
