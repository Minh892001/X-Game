package com.morefun.XSanGo.colorfulEgg;

import com.XSanGo.Protocol.ColorfullEggView;
import com.XSanGo.Protocol.NoteException;

public interface IColorfullEggController {
	/**获取彩蛋视图*/
	public ColorfullEggView getView() throws NoteException;
	/**砸蛋*/
	public ColorfullEggView brokenEgg(byte eggFlag) throws NoteException;
	/**领取奖励
	 * @param num 数量
	 * @param itemId 道具id 如果为-1则需要服务器生成
	 * */
	public void acceptReward(String itemId, int num) throws NoteException;
}
