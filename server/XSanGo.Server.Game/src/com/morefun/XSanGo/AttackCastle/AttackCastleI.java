package com.morefun.XSanGo.AttackCastle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Current;

import com.XSanGo.Protocol.AMD_AttackCastle_getCastleOpponentView;
import com.XSanGo.Protocol.AMD_AttackCastle_refresh;
import com.XSanGo.Protocol.CastleNodeView;
import com.XSanGo.Protocol.CastleOpponentView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._AttackCastleDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.AttackCastle.AttackCastleController.Callback;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 *
 * @author qinguofeng
 * @date Jan 27, 2015
 */
public class AttackCastleI extends _AttackCastleDisp {

	private static final long serialVersionUID = 8681034296910218595L;

	private final static Log logger = LogFactory.getLog(AttackCastleI.class);

	private IRole roleRt;

	public AttackCastleI(IRole role) {
		this.roleRt = role;
	}

	@Override
	public String requestAttackCastles(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAttackCastleController()
				.requestAttackCastles());
	}

	@Override
	public CastleNodeView beginAttackCastle(int castleNodeId, Current __current)
			throws NoteException {
		return roleRt.getAttackCastleController().beginAttackCastle(
				castleNodeId);
	}

	@Override
	public String acceptRewards(int castleNodeId, int startCount,
			Current __current) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		return LuaSerializer.serialize(roleRt.getAttackCastleController()
				.acceptRewards(castleNodeId, startCount));
	}

	@Override
	public void getCastleOpponentView_async(
			AMD_AttackCastle_getCastleOpponentView __cb, int castleNodeId,
			Current __current) throws NoteException {
		final AMD_AttackCastle_getCastleOpponentView _cb = __cb;
		roleRt.getAttackCastleController().getCastleOpponentView(castleNodeId,
				new Callback() {
					@Override
					public void cb(boolean success,
							CastleOpponentView castleView) {
						if (!success || castleView == null) {
							_cb.ice_exception(new NoteException(Messages.getString("AttackCastleI.0"))); //$NON-NLS-1$
							return;
						}
						_cb.ice_response(LuaSerializer.serialize(castleView));
					}
				});
	}

	@Override
	public String endAttackCastle(int castleNodeId, byte remainHero,
			Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAttackCastleController()
				.endAttackCastle(castleNodeId, remainHero));
	}

	@Override
	public String resetAttackCastles(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAttackCastleController()
				.resetAttackCastles());
	}

	@Override
	public String shopRewardList(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAttackCastleController()
				.getShopRewardList());
	}

	@Override
	public String refreshShopList(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAttackCastleController()
				.refreshShopRewardList());
	}

	@Override
	public String exchangeItem(int itemId, Current __current)
			throws NoteException {
		roleRt.getAttackCastleController().exchangeItem(itemId);
		return ""; //$NON-NLS-1$
	}

	@Override
	public void exitAttackCastle(int castleNodeId, Current __current)
			throws NoteException {
		roleRt.getAttackCastleController().exitAttackCastle(castleNodeId);
	}

	@Override
	public String clearLevel(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAttackCastleController().clear());
	}

	@Override
	public void refresh_async(final AMD_AttackCastle_refresh __cb, int castleNodeId,
			Current __current) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		roleRt.getAttackCastleController().refreshOpponent(castleNodeId, new Callback() {
			@Override
			public void cb(boolean success,
					CastleOpponentView castleView) {
				if (!success || castleView == null) {
					__cb.ice_exception(new NoteException(Messages.getString("AttackCastleI.0"))); //$NON-NLS-1$
					return;
				}
				__cb.ice_response(LuaSerializer.serialize(castleView));
			}
		});
	}

}
