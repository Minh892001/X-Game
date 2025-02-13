package com.morefun.XSanGo.makewine;

import Ice.Current;

import com.XSanGo.Protocol.AMD_MakeWineInfo_receiveShare;
import com.XSanGo.Protocol.AMD_MakeWineInfo_shareView;
import com.XSanGo.Protocol.AMD_MakeWineInfo_topUp;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._MakeWineInfoDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.role.IRole;

/**
 * 酿酒
 * @author zhuzhi.yang
 *
 */
public class MakeWineI extends _MakeWineInfoDisp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IRole roleRt;
	
	public MakeWineI(IRole roleRt) {
		this.roleRt = roleRt;
	}
	
	/**
	 * 酿酒界面
	 */
	@Override
	public String makeWineView(Current __current) throws NoteException {
//		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		return this.roleRt.getMakeWineControler().makeWineView();
	}

	/**
	 * 定时领取材料
	 * @throws NoteException
	 */
	@Override
	public String receiveMaterial(Current __current) throws NoteException {
		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		return this.roleRt.getMakeWineControler().receiveMaterial();
	}

	/**
	 * 领取积分奖励
	 * @throws NoteException
	 */
	@Override
	public String receiveScoreAward(Current __current) throws NoteException {
		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		return this.roleRt.getMakeWineControler().receiveScoreAward();
	}

	/**
	 * 酿酒
	 * @param id合成目标,0:全部酿酒
	 * @param type: 0:酿酒一瓶, 1:全部酿酒,酿完该种类的酒 
	 * @throws NoteException
	 */
	@Override
	public String make(int targetId, int type, Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NotEnoughException, NoteException {
		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		return this.roleRt.getMakeWineControler().make(targetId, type);
	}
	
	/**
	 * 分享界面
	 * onlyFriends：0:全部  1:只看好友 2:自己的分享
	 */
	@Override
	public void shareView_async(AMD_MakeWineInfo_shareView __cb, int condition,
			int startIndex, Current __current)
			throws NoteException {
//		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		this.roleRt.getMakeWineControler().shareView(__cb, condition, startIndex);
	}

	/**
	 * 分享
	 * id:目标
	 * count:多少组
	 */
	@Override
	public void share(int id, int count, Current __current)
			throws NoteException {
		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		this.roleRt.getMakeWineControler().share(id, count);
	}
	
	/**
	 * 领取分享奖励
	 */
	@Override
	public void receiveShare_async(AMD_MakeWineInfo_receiveShare __cb, String id, int condition,
			int startIndex, Current __current) throws NoteException {
		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		this.roleRt.getMakeWineControler().receiveShare(__cb, id, condition, startIndex);
	}
	
	/**
	 * 置顶
	 */
	@Override
	public void topUp_async(AMD_MakeWineInfo_topUp __cb, String id, Current __current) throws NoteException, NotEnoughYuanBaoException {
		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		this.roleRt.getMakeWineControler().topUp(__cb, id);
	}

	/**
	 * 兑换界面
	 */
	@Override
	public String exchangeView(Current __current) throws NoteException {
//		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		return this.roleRt.getMakeWineControler().exchangeView();
	}

	/**
	 * 兑换
	 */
	@Override
	public void exchange(int id, int num, Current __current) throws NoteException {
		// 活动结束依旧可以兑换【2016-04-29,郭超凡说是他和阮斌商量的结果】
		// 检测入口是否消失
//		XsgMakeWineManager.getInstance().checkLevel(this.roleRt.getLevel());
		
		int openLevel = XsgMakeWineManager.getInstance().makeWineConfig.openLevel;
		if(this.roleRt.getLevel() < openLevel) {
			throw new NoteException(Messages.getString(Messages.getString("LadderControler.16") + openLevel + Messages.getString("LadderControler.17")));
		}
		if(XsgMakeWineManager.getInstance().showIcon(this.roleRt.getLevel())) {
			this.roleRt.getMakeWineControler().exchange(id, num);
		}
	}

	/**
	 * 积分排名
	 */
	@Override
	public String scoreRank(Current __current) throws NoteException {
//		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		return this.roleRt.getMakeWineControler().scoreRank();
	}

	/**
	 * 积分排名奖励列表
	 */
	@Override
	public String scoreRankAward(Current __current) throws NoteException {
//		XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		return this.roleRt.getMakeWineControler().scoreRankAward();
	}

	/**
	 * 查看酒的详情和奖励信息
	 */
	@Override
	public String wineInfoView(Current __current) throws NoteException {
		
		return this.roleRt.getMakeWineControler().wineInfoView();
	}

	/**
	 * 查看日志
	 * @param type:1:酿酒 2:领取 3:兑换
	 */
	@Override
	public String seeLog(int type, Current __current) throws NoteException {
		return this.roleRt.getMakeWineControler().seeLog(type);
	}

}
