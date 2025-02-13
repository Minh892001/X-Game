package com.morefun.XSanGo.auction;

import Ice.Current;

import com.XSanGo.Protocol.AuctionBuyResView;
import com.XSanGo.Protocol.AuctionHouseView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._AuctionHouseDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 拍卖行
 * 
 * @author qinguofeng
 * @date Mar 30, 2015
 */
public class AuctionHouseI extends _AuctionHouseDisp {

	private static final long serialVersionUID = 1L;

	private IRole roleRt;

	public AuctionHouseI(IRole role) {
		this.roleRt = role;
	}

	@Override
	public void sell(String id, int num, long price, long fixedPrice,
			Current __current) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		roleRt.getAuctionHouseController().sell(id, num, price, fixedPrice);
	}

	@Override
	public AuctionBuyResView buy(String id, int type, Current __current)
			throws NoteException {
		return roleRt.getAuctionHouseController().buy(id, type);
	}

	@Override
	public long exchange(long price, Current __current) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		return roleRt.getAuctionHouseController().exchange(price);
	}

	@Override
	public String getAuctionHouseItems(int startIndex, int count,
			int type, String key, int quality, int direction, Ice.Current __current)
			throws NoteException {
		return LuaSerializer.serialize(roleRt.getAuctionHouseController().getAuctionHouseItems(
				startIndex, count, type, key, quality, direction));
	}

	@Override
	public String getMyBidItems(int startIndex, int count, int type,
			String key, int quality, int direction, Ice.Current __current)
			throws NoteException {
		return LuaSerializer.serialize(roleRt.getAuctionHouseController().getMyBidItems(startIndex,
				count, type, key, quality, direction));
	}

	@Override
	public String getMySellItems(int startIndex, int count, int type,
			String key, int quality, int direction, Ice.Current __current)
			throws NoteException {
		return LuaSerializer.serialize(roleRt.getAuctionHouseController().getMySellItems(startIndex,
				count, type, key, quality, direction));
	}

	@Override
	public void cancelAuction(String id, Current __current)
			throws NoteException {
		roleRt.getAuctionHouseController().cancelAuction(id);
	}

	@Override
	public String getAuctionShops(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAuctionHouseController()
				.getAuctionShops());
	}

	@Override
	public String refreshAuctionShop(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAuctionHouseController()
				.refreshAuctionShop());
	}

	@Override
	public long buyAuctionShop(int id, Current __current)
			throws NoteException {
		return roleRt.getAuctionHouseController().buyAuctionShop(id);
	}

}
