// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.1
//
// <auto-generated>
//
// Generated from file `AttackCastle.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _AttackCastleOperations
{
    String requestAttackCastles(Ice.Current __current)
        throws NoteException;

    String resetAttackCastles(Ice.Current __current)
        throws NoteException;

    void getCastleOpponentView_async(AMD_AttackCastle_getCastleOpponentView __cb, int castleNodeId, Ice.Current __current)
        throws NoteException;

    CastleNodeView beginAttackCastle(int castleNodeId, Ice.Current __current)
        throws NoteException;

    void exitAttackCastle(int castleNodeId, Ice.Current __current)
        throws NoteException;

    String endAttackCastle(int castleNodeId, byte remainHero, Ice.Current __current)
        throws NoteException;

    String acceptRewards(int castleNodeId, int startCount, Ice.Current __current)
        throws NotEnoughMoneyException,
               NotEnoughYuanBaoException,
               NoteException;

    String shopRewardList(Ice.Current __current)
        throws NoteException;

    String refreshShopList(Ice.Current __current)
        throws NoteException;

    String exchangeItem(int itemId, Ice.Current __current)
        throws NoteException;

    String clearLevel(Ice.Current __current)
        throws NoteException;

    void refresh_async(AMD_AttackCastle_refresh __cb, int castleNodeId, Ice.Current __current)
        throws NotEnoughMoneyException,
               NotEnoughYuanBaoException,
               NoteException;
}
