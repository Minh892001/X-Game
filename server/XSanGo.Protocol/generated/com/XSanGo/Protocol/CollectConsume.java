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
// Generated from file `CollectHeroSoul.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class CollectConsume implements java.lang.Cloneable, java.io.Serializable
{
    public int type;

    public int num;

    public int count;

    public CollectConsume()
    {
    }

    public CollectConsume(int type, int num, int count)
    {
        this.type = type;
        this.num = num;
        this.count = count;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CollectConsume _r = null;
        if(rhs instanceof CollectConsume)
        {
            _r = (CollectConsume)rhs;
        }

        if(_r != null)
        {
            if(type != _r.type)
            {
                return false;
            }
            if(num != _r.num)
            {
                return false;
            }
            if(count != _r.count)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::CollectConsume");
        __h = IceInternal.HashUtil.hashAdd(__h, type);
        __h = IceInternal.HashUtil.hashAdd(__h, num);
        __h = IceInternal.HashUtil.hashAdd(__h, count);
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
        __os.writeInt(type);
        __os.writeInt(num);
        __os.writeInt(count);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        type = __is.readInt();
        num = __is.readInt();
        count = __is.readInt();
    }

    public static final long serialVersionUID = -555761912L;
}
