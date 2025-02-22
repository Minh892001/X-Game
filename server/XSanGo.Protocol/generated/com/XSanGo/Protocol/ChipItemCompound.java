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
// Generated from file `ItemChip.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class ChipItemCompound implements java.lang.Cloneable, java.io.Serializable
{
    public String chipItemId;

    public int num;

    public ChipItemCompound()
    {
    }

    public ChipItemCompound(String chipItemId, int num)
    {
        this.chipItemId = chipItemId;
        this.num = num;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        ChipItemCompound _r = null;
        if(rhs instanceof ChipItemCompound)
        {
            _r = (ChipItemCompound)rhs;
        }

        if(_r != null)
        {
            if(chipItemId != _r.chipItemId)
            {
                if(chipItemId == null || _r.chipItemId == null || !chipItemId.equals(_r.chipItemId))
                {
                    return false;
                }
            }
            if(num != _r.num)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::ChipItemCompound");
        __h = IceInternal.HashUtil.hashAdd(__h, chipItemId);
        __h = IceInternal.HashUtil.hashAdd(__h, num);
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
        __os.writeString(chipItemId);
        __os.writeInt(num);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        chipItemId = __is.readString();
        num = __is.readInt();
    }

    public static final long serialVersionUID = -2057017942L;
}
