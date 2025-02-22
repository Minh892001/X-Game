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

public class FactionMemberView implements java.lang.Cloneable, java.io.Serializable
{
    public String id;

    public String roleId;

    public String name;

    public String headImage;

    public int level;

    public int duty;

    public int vipLevel;

    public int contribution;

    public int pastMinute;

    public int honor;

    public int canAllotItemNum;

    public String demandItemId;

    public FactionMemberView()
    {
    }

    public FactionMemberView(String id, String roleId, String name, String headImage, int level, int duty, int vipLevel, int contribution, int pastMinute, int honor, int canAllotItemNum, String demandItemId)
    {
        this.id = id;
        this.roleId = roleId;
        this.name = name;
        this.headImage = headImage;
        this.level = level;
        this.duty = duty;
        this.vipLevel = vipLevel;
        this.contribution = contribution;
        this.pastMinute = pastMinute;
        this.honor = honor;
        this.canAllotItemNum = canAllotItemNum;
        this.demandItemId = demandItemId;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FactionMemberView _r = null;
        if(rhs instanceof FactionMemberView)
        {
            _r = (FactionMemberView)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                if(id == null || _r.id == null || !id.equals(_r.id))
                {
                    return false;
                }
            }
            if(roleId != _r.roleId)
            {
                if(roleId == null || _r.roleId == null || !roleId.equals(_r.roleId))
                {
                    return false;
                }
            }
            if(name != _r.name)
            {
                if(name == null || _r.name == null || !name.equals(_r.name))
                {
                    return false;
                }
            }
            if(headImage != _r.headImage)
            {
                if(headImage == null || _r.headImage == null || !headImage.equals(_r.headImage))
                {
                    return false;
                }
            }
            if(level != _r.level)
            {
                return false;
            }
            if(duty != _r.duty)
            {
                return false;
            }
            if(vipLevel != _r.vipLevel)
            {
                return false;
            }
            if(contribution != _r.contribution)
            {
                return false;
            }
            if(pastMinute != _r.pastMinute)
            {
                return false;
            }
            if(honor != _r.honor)
            {
                return false;
            }
            if(canAllotItemNum != _r.canAllotItemNum)
            {
                return false;
            }
            if(demandItemId != _r.demandItemId)
            {
                if(demandItemId == null || _r.demandItemId == null || !demandItemId.equals(_r.demandItemId))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 5381;
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::FactionMemberView");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, roleId);
        __h = IceInternal.HashUtil.hashAdd(__h, name);
        __h = IceInternal.HashUtil.hashAdd(__h, headImage);
        __h = IceInternal.HashUtil.hashAdd(__h, level);
        __h = IceInternal.HashUtil.hashAdd(__h, duty);
        __h = IceInternal.HashUtil.hashAdd(__h, vipLevel);
        __h = IceInternal.HashUtil.hashAdd(__h, contribution);
        __h = IceInternal.HashUtil.hashAdd(__h, pastMinute);
        __h = IceInternal.HashUtil.hashAdd(__h, honor);
        __h = IceInternal.HashUtil.hashAdd(__h, canAllotItemNum);
        __h = IceInternal.HashUtil.hashAdd(__h, demandItemId);
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
        __os.writeString(id);
        __os.writeString(roleId);
        __os.writeString(name);
        __os.writeString(headImage);
        __os.writeInt(level);
        __os.writeInt(duty);
        __os.writeInt(vipLevel);
        __os.writeInt(contribution);
        __os.writeInt(pastMinute);
        __os.writeInt(honor);
        __os.writeInt(canAllotItemNum);
        __os.writeString(demandItemId);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readString();
        roleId = __is.readString();
        name = __is.readString();
        headImage = __is.readString();
        level = __is.readInt();
        duty = __is.readInt();
        vipLevel = __is.readInt();
        contribution = __is.readInt();
        pastMinute = __is.readInt();
        honor = __is.readInt();
        canAllotItemNum = __is.readInt();
        demandItemId = __is.readString();
    }

    public static final long serialVersionUID = 1147430810L;
}
