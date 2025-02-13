package com.morefun.XSanGo.collect;

import com.XSanGo.Protocol.CollectHeroSoulResView;
import com.XSanGo.Protocol.CollectHeroSoulView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface ICollectHeroSoulController extends IRedPointNotable{

    /**
     * 请求召唤武将
     * */
    CollectHeroSoulView[] reqCollectHeroSoul() throws NoteException;

    /**
     * 获取召唤结果
     * 
     * @param cType 
     *          召唤类型
     *         consumType
     *          消耗类型
     * */
    CollectHeroSoulResView doCollectHeroSoul(int cType, int consumType) throws NoteException,
            NotEnoughMoneyException, NotEnoughYuanBaoException;

    /**
     * 刷新
     * 
     * @param cType
     *          召唤类型
     * */
    CollectHeroSoulView doRefresh(int cType) throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException;
    
    /**
	 * 重置红点
	 */
	void resetRedPoint();
	
	/**
	 * 限时活动设置是否首次打开，显示红点
	 * @param value
	 */
	public void setFirstOpen(boolean value);
}
