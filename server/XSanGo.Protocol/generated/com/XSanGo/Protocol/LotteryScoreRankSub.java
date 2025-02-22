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
// Generated from file `Activity.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class LotteryScoreRankSub implements java.lang.Cloneable, java.io.Serializable
{
    public int rank;

    public String roleId;

    public String roleName;

    public String factionName;

    public int vip;

    public int level;

    public String icon;

    public int score;

    public LotteryScoreRankSub()
    {
    }

    public LotteryScoreRankSub(int rank, String roleId, String roleName, String factionName, int vip, int level, String icon, int score)
    {
        this.rank = rank;
        this.roleId = roleId;
        this.roleName = roleName;
        this.factionName = factionName;
        this.vip = vip;
        this.level = level;
        this.icon = icon;
        this.score = score;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        LotteryScoreRankSub _r = null;
        if(rhs instanceof LotteryScoreRankSub)
        {
            _r = (LotteryScoreRankSub)rhs;
        }

        if(_r != null)
        {
            if(rank != _r.rank)
            {
                return false;
            }
            if(roleId != _r.roleId)
            {
                if(roleId == null || _r.roleId == null || !roleId.equals(_r.roleId))
                {
                    return false;
                }
            }
            if(roleName != _r.roleName)
            {
                if(roleName == null || _r.roleName == null || !roleName.equals(_r.roleName))
                {
                    return false;
                }
            }
            if(factionName != _r.factionName)
            {
                if(factionName == null || _r.factionName == null || !factionName.equals(_r.factionName))
                {
                    return false;
                }
            }
            if(vip != _r.vip)
            {
                return false;
            }
            if(level != _r.level)
            {
                return false;
            }
            if(icon != _r.icon)
            {
                if(icon == null || _r.icon == null || !icon.equals(_r.icon))
                {
                    return false;
                }
            }
            if(score != _r.score)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::LotteryScoreRankSub");
        __h = IceInternal.HashUtil.hashAdd(__h, rank);
        __h = IceInternal.HashUtil.hashAdd(__h, roleId);
        __h = IceInternal.HashUtil.hashAdd(__h, roleName);
        __h = IceInternal.HashUtil.hashAdd(__h, factionName);
        __h = IceInternal.HashUtil.hashAdd(__h, vip);
        __h = IceInternal.HashUtil.hashAdd(__h, level);
        __h = IceInternal.HashUtil.hashAdd(__h, icon);
        __h = IceInternal.HashUtil.hashAdd(__h, score);
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
        __os.writeInt(rank);
        __os.writeString(roleId);
        __os.writeString(roleName);
        __os.writeString(factionName);
        __os.writeInt(vip);
        __os.writeInt(level);
        __os.writeString(icon);
        __os.writeInt(score);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        rank = __is.readInt();
        roleId = __is.readString();
        roleName = __is.readString();
        factionName = __is.readString();
        vip = __is.readInt();
        level = __is.readInt();
        icon = __is.readString();
        score = __is.readInt();
    }

    public static final long serialVersionUID = 1071943038L;
}
