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
// Generated from file `Tournament.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _TournamentOperationsNC
{
    void enterTournament_async(AMD_Tournament_enterTournament __cb)
        throws NoteException;

    void openTournamentView_async(AMD_Tournament_openTournamentView __cb)
        throws NoteException;

    String preSignup()
        throws NoteException;

    void signup_async(AMD_Tournament_signup __cb)
        throws NoteException;

    int buyRefreshCount()
        throws NotEnoughMoneyException,
               NotEnoughYuanBaoException,
               NoteException;

    int buyFightCount()
        throws NotEnoughMoneyException,
               NotEnoughYuanBaoException,
               NoteException;

    void enterPVPView_async(AMD_Tournament_enterPVPView __cb)
        throws NoteException;

    void refreshPVPView_async(AMD_Tournament_refreshPVPView __cb)
        throws NoteException;

    void beginFightWith_async(AMD_Tournament_beginFightWith __cb, String opponentId)
        throws NoteException;

    String endFightWith(String opponentId, int flag, int remainHeroCount, int power)
        throws NoteException;

    void fightWith_async(AMD_Tournament_fightWith __cb, String opponentId)
        throws NoteException;

    String openSetupFormation()
        throws NoteException;

    void setupFormation_async(AMD_Tournament_setupFormation __cb)
        throws NoteException;

    String getFightRecords()
        throws NoteException;

    void getRankList_async(AMD_Tournament_getRankList __cb)
        throws NoteException;

    void getFightMovieByRecordId_async(AMD_Tournament_getFightMovieByRecordId __cb, String recordId)
        throws NoteException;

    void getKnockOutView_async(AMD_Tournament_getKnockOutView __cb);

    void getKnockOutMovieList_async(AMD_Tournament_getKnockOutMovieList __cb, int id)
        throws NoteException;

    void getKnockOutMovie_async(AMD_Tournament_getKnockOutMovie __cb, int id, int index)
        throws NoteException;

    void getBetView_async(AMD_Tournament_getBetView __cb)
        throws NoteException;

    void bet_async(AMD_Tournament_bet __cb, int stage, int id, String roleId, int num)
        throws NotEnoughMoneyException,
               NotEnoughYuanBaoException,
               NoteException;

    String getTournamentStatus()
        throws NoteException;

    /**
     * 获取自己积分和当日胜利次数 return IntIntPair
     * @param __cb The callback object for the operation.
     **/
    void getScoreAndWinNum_async(AMD_Tournament_getScoreAndWinNum __cb)
        throws NoteException;

    String getTournamentShopView()
        throws NoteException;

    String buyShopItem(String id, int num)
        throws NoteException;
}
