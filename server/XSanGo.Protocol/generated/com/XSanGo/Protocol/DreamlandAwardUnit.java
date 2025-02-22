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
// Generated from file `Dreamland.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * 星数奖励界面单元数据
 **/
public class DreamlandAwardUnit implements java.lang.Cloneable, java.io.Serializable
{
    public int starNum;

    public boolean isDraw;

    public DreamlandAwardUnit()
    {
    }

    public DreamlandAwardUnit(int starNum, boolean isDraw)
    {
        this.starNum = starNum;
        this.isDraw = isDraw;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        DreamlandAwardUnit _r = null;
        if(rhs instanceof DreamlandAwardUnit)
        {
            _r = (DreamlandAwardUnit)rhs;
        }

        if(_r != null)
        {
            if(starNum != _r.starNum)
            {
                return false;
            }
            if(isDraw != _r.isDraw)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::DreamlandAwardUnit");
        __h = IceInternal.HashUtil.hashAdd(__h, starNum);
        __h = IceInternal.HashUtil.hashAdd(__h, isDraw);
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
        __os.writeInt(starNum);
        __os.writeBool(isDraw);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        starNum = __is.readInt();
        isDraw = __is.readBool();
    }

    public static final long serialVersionUID = 1579488247L;
}
