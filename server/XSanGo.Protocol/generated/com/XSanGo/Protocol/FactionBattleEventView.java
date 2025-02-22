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
 * 公会战随机事件
 **/
public class FactionBattleEventView implements java.lang.Cloneable, java.io.Serializable
{
    public int strongholdId;

    public String eventIcon;

    public FactionBattleEventView()
    {
    }

    public FactionBattleEventView(int strongholdId, String eventIcon)
    {
        this.strongholdId = strongholdId;
        this.eventIcon = eventIcon;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FactionBattleEventView _r = null;
        if(rhs instanceof FactionBattleEventView)
        {
            _r = (FactionBattleEventView)rhs;
        }

        if(_r != null)
        {
            if(strongholdId != _r.strongholdId)
            {
                return false;
            }
            if(eventIcon != _r.eventIcon)
            {
                if(eventIcon == null || _r.eventIcon == null || !eventIcon.equals(_r.eventIcon))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::FactionBattleEventView");
        __h = IceInternal.HashUtil.hashAdd(__h, strongholdId);
        __h = IceInternal.HashUtil.hashAdd(__h, eventIcon);
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
        __os.writeInt(strongholdId);
        __os.writeString(eventIcon);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        strongholdId = __is.readInt();
        eventIcon = __is.readString();
    }

    public static final long serialVersionUID = -2025063659L;
}
