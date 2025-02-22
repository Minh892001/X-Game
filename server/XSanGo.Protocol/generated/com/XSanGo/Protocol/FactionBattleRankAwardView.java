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
 * 公会战排行奖励
 **/
public class FactionBattleRankAwardView implements java.lang.Cloneable, java.io.Serializable
{
    public String rank;

    public IntString[] items;

    public FactionBattleRankAwardView()
    {
    }

    public FactionBattleRankAwardView(String rank, IntString[] items)
    {
        this.rank = rank;
        this.items = items;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FactionBattleRankAwardView _r = null;
        if(rhs instanceof FactionBattleRankAwardView)
        {
            _r = (FactionBattleRankAwardView)rhs;
        }

        if(_r != null)
        {
            if(rank != _r.rank)
            {
                if(rank == null || _r.rank == null || !rank.equals(_r.rank))
                {
                    return false;
                }
            }
            if(!java.util.Arrays.equals(items, _r.items))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::FactionBattleRankAwardView");
        __h = IceInternal.HashUtil.hashAdd(__h, rank);
        __h = IceInternal.HashUtil.hashAdd(__h, items);
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
        __os.writeString(rank);
        IntStringSeqHelper.write(__os, items);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        rank = __is.readString();
        items = IntStringSeqHelper.read(__is);
    }

    public static final long serialVersionUID = 38195496L;
}
