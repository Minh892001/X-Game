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

public class SeckillItemView implements java.lang.Cloneable, java.io.Serializable
{
    public int id;

    public String itemId;

    public int price;

    public int maxNum;

    public int remainNum;

    public int buyable;

    public String dateDesc;

    public SeckillItemView()
    {
    }

    public SeckillItemView(int id, String itemId, int price, int maxNum, int remainNum, int buyable, String dateDesc)
    {
        this.id = id;
        this.itemId = itemId;
        this.price = price;
        this.maxNum = maxNum;
        this.remainNum = remainNum;
        this.buyable = buyable;
        this.dateDesc = dateDesc;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        SeckillItemView _r = null;
        if(rhs instanceof SeckillItemView)
        {
            _r = (SeckillItemView)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                return false;
            }
            if(itemId != _r.itemId)
            {
                if(itemId == null || _r.itemId == null || !itemId.equals(_r.itemId))
                {
                    return false;
                }
            }
            if(price != _r.price)
            {
                return false;
            }
            if(maxNum != _r.maxNum)
            {
                return false;
            }
            if(remainNum != _r.remainNum)
            {
                return false;
            }
            if(buyable != _r.buyable)
            {
                return false;
            }
            if(dateDesc != _r.dateDesc)
            {
                if(dateDesc == null || _r.dateDesc == null || !dateDesc.equals(_r.dateDesc))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::SeckillItemView");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, itemId);
        __h = IceInternal.HashUtil.hashAdd(__h, price);
        __h = IceInternal.HashUtil.hashAdd(__h, maxNum);
        __h = IceInternal.HashUtil.hashAdd(__h, remainNum);
        __h = IceInternal.HashUtil.hashAdd(__h, buyable);
        __h = IceInternal.HashUtil.hashAdd(__h, dateDesc);
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
        __os.writeString(itemId);
        __os.writeInt(price);
        __os.writeInt(maxNum);
        __os.writeInt(remainNum);
        __os.writeInt(buyable);
        __os.writeString(dateDesc);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readInt();
        itemId = __is.readString();
        price = __is.readInt();
        maxNum = __is.readInt();
        remainNum = __is.readInt();
        buyable = __is.readInt();
        dateDesc = __is.readString();
    }

    public static final long serialVersionUID = 709575241L;
}
