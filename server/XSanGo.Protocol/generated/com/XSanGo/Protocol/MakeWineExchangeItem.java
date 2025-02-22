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
// Generated from file `MakeWine.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class MakeWineExchangeItem implements java.lang.Cloneable, java.io.Serializable
{
    public int id;

    public IntString item;

    public int needScore;

    public MakeWineExchangeItem()
    {
    }

    public MakeWineExchangeItem(int id, IntString item, int needScore)
    {
        this.id = id;
        this.item = item;
        this.needScore = needScore;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        MakeWineExchangeItem _r = null;
        if(rhs instanceof MakeWineExchangeItem)
        {
            _r = (MakeWineExchangeItem)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                return false;
            }
            if(item != _r.item)
            {
                if(item == null || _r.item == null || !item.equals(_r.item))
                {
                    return false;
                }
            }
            if(needScore != _r.needScore)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::MakeWineExchangeItem");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, item);
        __h = IceInternal.HashUtil.hashAdd(__h, needScore);
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
        item.__write(__os);
        __os.writeInt(needScore);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readInt();
        item = new IntString();
        item.__read(__is);
        needScore = __is.readInt();
    }

    public static final long serialVersionUID = 304641716L;
}
