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

public class BetView implements java.lang.Cloneable, java.io.Serializable
{
    public int id;

    public String betRoleId;

    public int winornot;

    public int result;

    public BetView()
    {
    }

    public BetView(int id, String betRoleId, int winornot, int result)
    {
        this.id = id;
        this.betRoleId = betRoleId;
        this.winornot = winornot;
        this.result = result;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        BetView _r = null;
        if(rhs instanceof BetView)
        {
            _r = (BetView)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                return false;
            }
            if(betRoleId != _r.betRoleId)
            {
                if(betRoleId == null || _r.betRoleId == null || !betRoleId.equals(_r.betRoleId))
                {
                    return false;
                }
            }
            if(winornot != _r.winornot)
            {
                return false;
            }
            if(result != _r.result)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::BetView");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, betRoleId);
        __h = IceInternal.HashUtil.hashAdd(__h, winornot);
        __h = IceInternal.HashUtil.hashAdd(__h, result);
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
        __os.writeInt(id);
        __os.writeString(betRoleId);
        __os.writeInt(winornot);
        __os.writeInt(result);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readInt();
        betRoleId = __is.readString();
        winornot = __is.readInt();
        result = __is.readInt();
    }

    public static final long serialVersionUID = 1727777112L;
}
