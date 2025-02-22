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
// Generated from file `ArenaRank.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * 跨服竞技场回调
 **/
public abstract class _CrossArenaCallbackDisp extends Ice.ObjectImpl implements CrossArenaCallback
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::com::XSanGo::Protocol::CrossArenaCallback"
    };

    public boolean ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[] ice_ids()
    {
        return __ids;
    }

    public String[] ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String ice_id()
    {
        return __ids[1];
    }

    public String ice_id(Ice.Current __current)
    {
        return __ids[1];
    }

    public static String ice_staticId()
    {
        return __ids[1];
    }

    /**
     * 结束战斗，返回当前排名和排名变化
     **/
    public final IntIntPair endFight(String sourceRoleId, boolean isWin, String rivalRoleId, String movieId, FightMovieView movieView)
    {
        return endFight(sourceRoleId, isWin, rivalRoleId, movieId, movieView, null);
    }

    /**
     * 获取跨服竞技场排行榜
     **/
    public final RivalRank[] getArenaRank(int size)
    {
        return getArenaRank(size, null);
    }

    /**
     * 获取双方竞技场PVPView
     **/
    public final CrossArenaPvpView[] getCrossArenaPvpView(String leftRoleId, String rightRoleId)
    {
        return getCrossArenaPvpView(leftRoleId, rightRoleId, null);
    }

    /**
     * 获取跨服竞技场战报
     * @param __cb The callback object for the operation.
     **/
    public final void getCrossMovie_async(AMD_CrossArenaCallback_getCrossMovie __cb, String id)
        throws NoteException
    {
        getCrossMovie_async(__cb, id, null);
    }

    /**
     * 获取玩家保存的阵容信息
     **/
    public final PvpOpponentFormationView getRolePvpView(String roleId)
    {
        return getRolePvpView(roleId, null);
    }

    /**
     * 获取玩家跨服竞技场数据
     **/
    public final RivalRank getRoleRivalRank(String roleId)
    {
        return getRoleRivalRank(roleId, null);
    }

    /**
     * 刷新对手
     **/
    public final RivalRank[] refreshRival(String roleId)
    {
        return refreshRival(roleId, null);
    }

    /**
     * 设置个性签名
     **/
    public final void setSignature(String roleId, String signature)
    {
        setSignature(roleId, signature, null);
    }

    /**
     * 更新跨服竞技场数据
     **/
    public final RivalRank updateArena(RivalRank rank, PvpOpponentFormationView pvpView)
    {
        return updateArena(rank, pvpView, null);
    }

    public static Ice.DispatchStatus ___updateArena(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        RivalRank rank;
        PvpOpponentFormationView pvpView;
        rank = new RivalRank();
        rank.__read(__is);
        pvpView = new PvpOpponentFormationView();
        pvpView.__read(__is);
        __inS.endReadParams();
        RivalRank __ret = __obj.updateArena(rank, pvpView, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __ret.__write(__os);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getRoleRivalRank(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String roleId;
        roleId = __is.readString();
        __inS.endReadParams();
        RivalRank __ret = __obj.getRoleRivalRank(roleId, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __ret.__write(__os);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getRolePvpView(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String roleId;
        roleId = __is.readString();
        __inS.endReadParams();
        PvpOpponentFormationView __ret = __obj.getRolePvpView(roleId, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __ret.__write(__os);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getArenaRank(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int size;
        size = __is.readInt();
        __inS.endReadParams();
        RivalRank[] __ret = __obj.getArenaRank(size, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        RivalRankSeqHelper.write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___refreshRival(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String roleId;
        roleId = __is.readString();
        __inS.endReadParams();
        RivalRank[] __ret = __obj.refreshRival(roleId, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        RivalRankSeqHelper.write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getCrossArenaPvpView(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String leftRoleId;
        String rightRoleId;
        leftRoleId = __is.readString();
        rightRoleId = __is.readString();
        __inS.endReadParams();
        CrossArenaPvpView[] __ret = __obj.getCrossArenaPvpView(leftRoleId, rightRoleId, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        CrossArenaPvpViewSeqHelper.write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___endFight(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String sourceRoleId;
        boolean isWin;
        String rivalRoleId;
        String movieId;
        FightMovieView movieView;
        sourceRoleId = __is.readString();
        isWin = __is.readBool();
        rivalRoleId = __is.readString();
        movieId = __is.readString();
        movieView = new FightMovieView();
        movieView.__read(__is);
        __inS.endReadParams();
        IntIntPair __ret = __obj.endFight(sourceRoleId, isWin, rivalRoleId, movieId, movieView, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __ret.__write(__os);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setSignature(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String roleId;
        String signature;
        roleId = __is.readString();
        signature = __is.readString();
        __inS.endReadParams();
        __obj.setSignature(roleId, signature, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getCrossMovie(CrossArenaCallback __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String id;
        id = __is.readString();
        __inS.endReadParams();
        AMD_CrossArenaCallback_getCrossMovie __cb = new _AMD_CrossArenaCallback_getCrossMovie(__inS);
        try
        {
            __obj.getCrossMovie_async(__cb, id, __current);
        }
        catch(java.lang.Exception ex)
        {
            __cb.ice_exception(ex);
        }
        return Ice.DispatchStatus.DispatchAsync;
    }

    private final static String[] __all =
    {
        "endFight",
        "getArenaRank",
        "getCrossArenaPvpView",
        "getCrossMovie",
        "getRolePvpView",
        "getRoleRivalRank",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "refreshRival",
        "setSignature",
        "updateArena"
    };

    public Ice.DispatchStatus __dispatch(IceInternal.Incoming in, Ice.Current __current)
    {
        int pos = java.util.Arrays.binarySearch(__all, __current.operation);
        if(pos < 0)
        {
            throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return ___endFight(this, in, __current);
            }
            case 1:
            {
                return ___getArenaRank(this, in, __current);
            }
            case 2:
            {
                return ___getCrossArenaPvpView(this, in, __current);
            }
            case 3:
            {
                return ___getCrossMovie(this, in, __current);
            }
            case 4:
            {
                return ___getRolePvpView(this, in, __current);
            }
            case 5:
            {
                return ___getRoleRivalRank(this, in, __current);
            }
            case 6:
            {
                return ___ice_id(this, in, __current);
            }
            case 7:
            {
                return ___ice_ids(this, in, __current);
            }
            case 8:
            {
                return ___ice_isA(this, in, __current);
            }
            case 9:
            {
                return ___ice_ping(this, in, __current);
            }
            case 10:
            {
                return ___refreshRival(this, in, __current);
            }
            case 11:
            {
                return ___setSignature(this, in, __current);
            }
            case 12:
            {
                return ___updateArena(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    protected void __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice(ice_staticId(), -1, true);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        __is.endReadSlice();
    }

    public static final long serialVersionUID = 0L;
}
