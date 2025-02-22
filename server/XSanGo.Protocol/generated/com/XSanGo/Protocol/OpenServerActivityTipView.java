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

public class OpenServerActivityTipView implements java.lang.Cloneable, java.io.Serializable
{
    public int isOpen;

    public OpenServerActivityTipView()
    {
    }

    public OpenServerActivityTipView(int isOpen)
    {
        this.isOpen = isOpen;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        OpenServerActivityTipView _r = null;
        if(rhs instanceof OpenServerActivityTipView)
        {
            _r = (OpenServerActivityTipView)rhs;
        }

        if(_r != null)
        {
            if(isOpen != _r.isOpen)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::OpenServerActivityTipView");
        __h = IceInternal.HashUtil.hashAdd(__h, isOpen);
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
        __os.writeInt(isOpen);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        isOpen = __is.readInt();
    }

    public static final long serialVersionUID = 404455525L;
}
