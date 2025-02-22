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
// Generated from file `HeroAwaken.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class HeroBaptizeResult implements java.lang.Cloneable, java.io.Serializable
{
    public int times;

    public HeroBaptizeData result;

    public HeroBaptizeResult()
    {
    }

    public HeroBaptizeResult(int times, HeroBaptizeData result)
    {
        this.times = times;
        this.result = result;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        HeroBaptizeResult _r = null;
        if(rhs instanceof HeroBaptizeResult)
        {
            _r = (HeroBaptizeResult)rhs;
        }

        if(_r != null)
        {
            if(times != _r.times)
            {
                return false;
            }
            if(result != _r.result)
            {
                if(result == null || _r.result == null || !result.equals(_r.result))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::HeroBaptizeResult");
        __h = IceInternal.HashUtil.hashAdd(__h, times);
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
        __os.writeInt(times);
        result.__write(__os);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        times = __is.readInt();
        result = new HeroBaptizeData();
        result.__read(__is);
    }

    public static final long serialVersionUID = 1928920119L;
}
