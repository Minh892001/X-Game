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
// Generated from file `Copy.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _CopyOperationsNC
{
    void getBigCopyView_async(AMD_Copy_getBigCopyView __cb);

    void getSmallCopyView_async(AMD_Copy_getSmallCopyView __cb, int copyId);

    void getSmallCopyViewWithWarmup_async(AMD_Copy_getSmallCopyViewWithWarmup __cb, int copyId)
        throws NoteException;

    String beginWarmup()
        throws NoteException;

    byte endWarmup(byte remainHero)
        throws NoteException;

    CopyChallengeResultView beginChallenge(String formationId, int copyId)
        throws NotEnoughException,
               NoteException;

    int calculateStar(byte remainHero, byte killNum, float minTime, float maxTime)
        throws NoteException;

    void endChallenge_async(AMD_Copy_endChallenge __cb)
        throws NoteException;

    void failChallenge()
        throws NoteException;

    int getYuanbaoPrice(ItemView[] items)
        throws NoteException;

    void buySuccess(ItemView[] items)
        throws NotEnoughYuanBaoException,
               NoteException;

    void buyChallengeChance(int copyId)
        throws NotEnoughYuanBaoException,
               NoteException;

    IntIntPair[] buyChapterChallengeChance(int chapterId)
        throws NotEnoughYuanBaoException,
               NoteException;

    String clear(int copyTemplateId, int count)
        throws NotEnoughException,
               NoteException;

    ChapterRewardView getChapterRewardView(int chapterId);

    void receiveChapterReward(int chapterId, int level)
        throws NoteException;

    void releaseCaptured();

    int killCaptured();

    String employCaptured()
        throws NotEnoughMoneyException;

    String getCopyChallengeInfo(String idStr);

    void hallOfFameList_async(AMD_Copy_hallOfFameList __cb);

    /**
     * 打开互动界面 返回HuDongView的lua格式
     **/
    String getHuDongView(int copyId)
        throws NoteException;

    /**
     * 挑战TA
     * @param __cb The callback object for the operation.
     **/
    void beginChallengeTa_async(AMD_Copy_beginChallengeTa __cb, int copyId)
        throws NoteException;

    /**
     * 自动挑战TA
     * @param __cb The callback object for the operation.
     **/
    void autoChallengeTa_async(AMD_Copy_autoChallengeTa __cb, int copyId)
        throws NoteException;

    /**
     * 挑战TA结束 resFlag:0-失败，1-胜利 返回：resFlag
     **/
    ChallengeTaResult endChallengeTa(int resFlag)
        throws NoteException;

    /**
     * 膜拜TA
     **/
    int worshipTa(int copyId)
        throws NoteException;

    /**
     * 购买互动次数
     **/
    void buyHuDong(int copyId)
        throws NotEnoughYuanBaoException,
               NoteException;

    IntIntPair getMyOccupy()
        throws NoteException;

    BuyMilitaryOrderView getBuyMilitaryOrderView()
        throws NoteException;

    void buyMilitaryOrder()
        throws NotEnoughYuanBaoException,
               NoteException;

    String cancelWarmup(boolean first)
        throws NoteException;

    /**
     * 征收
     **/
    int levyCopy(int copyId)
        throws NoteException;

    /**
     * 我的占领列表 返回CopyOccupySeq的lua
     **/
    String myOccupyList()
        throws NoteException;

    /**
     * 放弃占领
     **/
    void giveCopy(int copyId)
        throws NoteException;
}
