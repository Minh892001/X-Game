package com.morefun.XSanGo.haoqingbao;

import java.util.List;

import com.XSanGo.Protocol.AMD_Haoqingbao_getRedPacketDetail;
import com.XSanGo.Protocol.AMD_Haoqingbao_rankList;
import com.XSanGo.Protocol.HaoqingbaoView;
import com.XSanGo.Protocol.MyRedPacketView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PreRecvRedPacketResultView;
import com.XSanGo.Protocol.RankView;
import com.XSanGo.Protocol.RecvRedPacketResultView;
import com.morefun.XSanGo.db.game.RoleHaoqingbaoItem;
import com.morefun.XSanGo.db.game.RoleHaoqingbaoRedPacketRecord;

/**
 * 豪情宝controller
 * 
 * @author guofeng.qin
 */
public interface IHaoqingbaoController {
	/** 打开豪情宝界面 */
	HaoqingbaoView openHaoqingbaoView();

	/**
	 * 发送红包
	 * 
	 * @param type
	 *            :类型，1，工会；2，好友；3，全服,
	 * @param minLevel
	 *            :最低等级, minVipLevel:最低vip等级,
	 * @param range
	 *            :派发范围(0/1/2):(在线成员/会长和长老/所以成员),(所以成员/在线好友),(所以好友),
	 * @param minFriendPoint
	 *            :最低友情点数,
	 * @param totalYuanbaoNum
	 *            :红包总金额,
	 * @param packetNum
	 *            :红包数量,
	 * @param msg
	 *            :留言
	 * */
	void sendRedPacket(int type, int minLevel, int minVipLevel, int range,
			int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg)
			throws NoteException;

	/**
	 * 抢红包
	 * 
	 * @param packetId
	 *            红包ID
	 * */
	RecvRedPacketResultView recvRedPacket(String packetId) throws NoteException;

	/**
	 * 获取豪情宝中元宝数量
	 * */
	int getTotalYuanbaoNum();

	/**
	 * 更改豪情宝余额
	 * 
	 * @param num
	 *            变动的数量，负数表示扣除
	 * */
	// int addYuanbaoNum(int num);

	/**
	 * 豪情宝提出到正常账户
	 * */
	void checkout(int num) throws NoteException;

	/**
	 * 我的红包记录
	 * */
	MyRedPacketView myRedPacket() throws NoteException;

	/**
	 * 红包详情
	 * */
	void getRedPacketDetail(String itemId,
			final AMD_Haoqingbao_getRedPacketDetail __cb) throws NoteException;

	/**
	 * 排行榜
	 * */
	RankView getRankView(int type, final AMD_Haoqingbao_rankList __cb)
			throws NoteException;

	/** 获取玩家抢红包的记录 */
	List<RoleHaoqingbaoRedPacketRecord> getRedPacketRecords();

	/** 更新运气王记录 */
	void updateLuckyStar(String itemId, int luckyStar);

	/** 发送退回红包余额的消息 */
	void sendGiveBackMsg(RoleHaoqingbaoItem item, int lastNum);

	/** 更新充值状态 */
	void updateChargeStatus();

	/** 是否应该充值到豪情宝账户 */
	boolean shouldAddToHaoqingbao();

	/** 充值到豪情宝 */
	void acceptCharge(int yuanbaoNum, String desc);
	
	/** 红包退回事件触发 */
	void onGiveBack(String id, int num);

	/**
	 * 预先请求豪情宝界面
	 * 
	 * @return 0,正常可以抢;1,已抢完;2,已经抢过了
	 * */
	PreRecvRedPacketResultView preRecvRedPacket(String packetId) throws NoteException;
	
	/** 触发运气王事件 */
	void imluckyStar();
	
	/** 索要红包 */
	void claimRedPacket(String roleId, String redPacketId) throws NoteException;
}
