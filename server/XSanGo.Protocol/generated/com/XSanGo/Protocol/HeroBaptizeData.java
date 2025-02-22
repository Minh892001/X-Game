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

public class HeroBaptizeData implements java.lang.Cloneable, java.io.Serializable
{
    public int lvl;

    public BaptizeProp[] props;

    public HeroBaptizeData()
    {
    }

    public HeroBaptizeData(int lvl, BaptizeProp[] props)
    {
        this.lvl = lvl;
        this.props = props;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        HeroBaptizeData _r = null;
        if(rhs instanceof HeroBaptizeData)
        {
            _r = (HeroBaptizeData)rhs;
        }

        if(_r != null)
        {
            if(lvl != _r.lvl)
            {
                return false;
            }
            if(!java.util.Arrays.equals(props, _r.props))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::HeroBaptizeData");
        __h = IceInternal.HashUtil.hashAdd(__h, lvl);
        __h = IceInternal.HashUtil.hashAdd(__h, props);
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
        __os.writeInt(lvl);
        BaptizePropSeqHelper.write(__os, props);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        lvl = __is.readInt();
        props = BaptizePropSeqHelper.read(__is);
    }

    public static final long serialVersionUID = -1318775111L;
}
