package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SeckillActivityView;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 秒杀活动
 * @author lixiongming
 *
 */
public interface ISeckillControler extends IRedPointNotable{

	/**
	 * 获取秒杀活动信息
	 * @return
	 * @throws NoteException
	 */
	public SeckillActivityView getSeckillActivityView() throws NoteException;

	/**
	 *  抢购商品
	 * @param id
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	public void seckillItem(int id) throws NotEnoughYuanBaoException, NoteException;
	
	/**
	 * 限时活动设置是否首次打开，显示红点
	 * @param value
	 */
	public void setFirstOpen(boolean value);
}
