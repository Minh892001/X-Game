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
// Generated from file `WorldBoss.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _WorldBossOperations
{
    /**
     * 返回worldBossView的lua
     * @param __current The Current object for the invocation.
     **/
    String getWorldBossView(Ice.Current __current)
        throws NoteException;

    /**
     * 获取伤害排名，返回WorldBossRankView的lua
     * @param __cb The callback object for the operation.
     * @param __current The Current object for the invocation.
     **/
    void getHarmRank_async(AMD_WorldBoss_getHarmRank __cb, Ice.Current __current)
        throws NoteException;

    /**
     * 获取伤害上榜排名，返回WorldBossRankView的lua
     * @param __cb The callback object for the operation.
     * @param __current The Current object for the invocation.
     **/
    void getCountRank_async(AMD_WorldBoss_getCountRank __cb, Ice.Current __current)
        throws NoteException;

    /**
     * 购买鼓舞
     * @param __current The Current object for the invocation.
     **/
    void buyInspire(Ice.Current __current)
        throws NoteException;

    /**
     * 清除CD
     * @param __current The Current object for the invocation.
     **/
    void clearCd(Ice.Current __current)
        throws NoteException;

    /**
     * 开始挑战，返回WorldBossChallengeView的lua
     * @param __current The Current object for the invocation.
     **/
    String beginChallenge(Ice.Current __current)
        throws NoteException;

    /**
     * 结束挑战传入真实伤害，返回false表示挑战无效
     * @param __current The Current object for the invocation.
     **/
    boolean endChallenge(int harm, int heroNum, Ice.Current __current)
        throws NoteException;

    /**
     * 领取尾刀奖励
     * @param __current The Current object for the invocation.
     **/
    void getTailAward(int hp, Ice.Current __current)
        throws NoteException;

    /**
     * 托管
     * @param __current The Current object for the invocation.
     **/
    void trust(Ice.Current __current)
        throws NoteException;

    /**
     * 取消托管
     * @param __current The Current object for the invocation.
     **/
    void cancelTrust(Ice.Current __current)
        throws NoteException;
}
