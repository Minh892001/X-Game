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

public class CornucopiaView implements java.lang.Cloneable, java.io.Serializable
{
    public CornucopiaItem[] items;

    public Property[] superItems;

    public int superState;

    public int receiveDays;

    public int vipLevel;

    public int discount;

    public CornucopiaView()
    {
    }

    public CornucopiaView(CornucopiaItem[] items, Property[] superItems, int superState, int receiveDays, int vipLevel, int discount)
    {
        this.items = items;
        this.superItems = superItems;
        this.superState = superState;
        this.receiveDays = receiveDays;
        this.vipLevel = vipLevel;
        this.discount = discount;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CornucopiaView _r = null;
        if(rhs instanceof CornucopiaView)
        {
            _r = (CornucopiaView)rhs;
        }

        if(_r != null)
        {
            if(!java.util.Arrays.equals(items, _r.items))
            {
                return false;
            }
            if(!java.util.Arrays.equals(superItems, _r.superItems))
            {
                return false;
            }
            if(superState != _r.superState)
            {
                return false;
            }
            if(receiveDays != _r.receiveDays)
            {
                return false;
            }
            if(vipLevel != _r.vipLevel)
            {
                return false;
            }
            if(discount != _r.discount)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::CornucopiaView");
        __h = IceInternal.HashUtil.hashAdd(__h, items);
        __h = IceInternal.HashUtil.hashAdd(__h, superItems);
        __h = IceInternal.HashUtil.hashAdd(__h, superState);
        __h = IceInternal.HashUtil.hashAdd(__h, receiveDays);
        __h = IceInternal.HashUtil.hashAdd(__h, vipLevel);
        __h = IceInternal.HashUtil.hashAdd(__h, discount);
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
        CornucopiaItemSeqHelper.write(__os, items);
        PropertySeqHelper.write(__os, superItems);
        __os.writeInt(superState);
        __os.writeInt(receiveDays);
        __os.writeInt(vipLevel);
        __os.writeInt(discount);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        items = CornucopiaItemSeqHelper.read(__is);
        superItems = PropertySeqHelper.read(__is);
        superState = __is.readInt();
        receiveDays = __is.readInt();
        vipLevel = __is.readInt();
        discount = __is.readInt();
    }

    public static final long serialVersionUID = 1846969403L;
}
