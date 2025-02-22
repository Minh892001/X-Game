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
// Generated from file `Faction.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * 公会战排行榜
 **/
public class FactionBattleRankResultView implements java.lang.Cloneable, java.io.Serializable
{
    public FactionBattleRankAwardView[] rankAwardResults;

    public FactionBattleRankView[] rankResults;

    public int rank;

    public FactionBattleRankResultView()
    {
    }

    public FactionBattleRankResultView(FactionBattleRankAwardView[] rankAwardResults, FactionBattleRankView[] rankResults, int rank)
    {
        this.rankAwardResults = rankAwardResults;
        this.rankResults = rankResults;
        this.rank = rank;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FactionBattleRankResultView _r = null;
        if(rhs instanceof FactionBattleRankResultView)
        {
            _r = (FactionBattleRankResultView)rhs;
        }

        if(_r != null)
        {
            if(!java.util.Arrays.equals(rankAwardResults, _r.rankAwardResults))
            {
                return false;
            }
            if(!java.util.Arrays.equals(rankResults, _r.rankResults))
            {
                return false;
            }
            if(rank != _r.rank)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 5381;
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::FactionBattleRankResultView");
        __h = IceInternal.HashUtil.hashAdd(__h, rankAwardResults);
        __h = IceInternal.HashUtil.hashAdd(__h, rankResults);
        __h = IceInternal.HashUtil.hashAdd(__h, rank);
        return __h;
    }

    public java.lang.Object
    clone()
    {
        java.lang.Object o = null;
        try
        {
            o = super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return o;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        RankAwardViewsHelper.write(__os, rankAwardResults);
        RankViewsHelper.write(__os, rankResults);
        __os.writeInt(rank);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        rankAwardResults = RankAwardViewsHelper.read(__is);
        rankResults = RankViewsHelper.read(__is);
        rank = __is.readInt();
    }

    public static final long serialVersionUID = 140940736L;
}
