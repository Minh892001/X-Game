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

public class PreSignupView implements java.lang.Cloneable, java.io.Serializable
{
    public int canSignup;

    public int lvLimit;

    public int lastTime;

    public PreSignupView()
    {
    }

    public PreSignupView(int canSignup, int lvLimit, int lastTime)
    {
        this.canSignup = canSignup;
        this.lvLimit = lvLimit;
        this.lastTime = lastTime;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        PreSignupView _r = null;
        if(rhs instanceof PreSignupView)
        {
            _r = (PreSignupView)rhs;
        }

        if(_r != null)
        {
            if(canSignup != _r.canSignup)
            {
                return false;
            }
            if(lvLimit != _r.lvLimit)
            {
                return false;
            }
            if(lastTime != _r.lastTime)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::PreSignupView");
        __h = IceInternal.HashUtil.hashAdd(__h, canSignup);
        __h = IceInternal.HashUtil.hashAdd(__h, lvLimit);
        __h = IceInternal.HashUtil.hashAdd(__h, lastTime);
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
        __os.writeInt(canSignup);
        __os.writeInt(lvLimit);
        __os.writeInt(lastTime);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        canSignup = __is.readInt();
        lvLimit = __is.readInt();
        lastTime = __is.readInt();
    }

    public static final long serialVersionUID = -1674357503L;
}
