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

public class LettoryShopInfoSub implements java.lang.Cloneable, java.io.Serializable
{
    public int id;

    public int status;

    public IntString item;

    public int coinType;

    public int price;

    public int price2;

    public int discount;

    public LettoryShopInfoSub()
    {
    }

    public LettoryShopInfoSub(int id, int status, IntString item, int coinType, int price, int price2, int discount)
    {
        this.id = id;
        this.status = status;
        this.item = item;
        this.coinType = coinType;
        this.price = price;
        this.price2 = price2;
        this.discount = discount;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        LettoryShopInfoSub _r = null;
        if(rhs instanceof LettoryShopInfoSub)
        {
            _r = (LettoryShopInfoSub)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                return false;
            }
            if(status != _r.status)
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
            if(coinType != _r.coinType)
            {
                return false;
            }
            if(price != _r.price)
            {
                return false;
            }
            if(price2 != _r.price2)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::LettoryShopInfoSub");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, status);
        __h = IceInternal.HashUtil.hashAdd(__h, item);
        __h = IceInternal.HashUtil.hashAdd(__h, coinType);
        __h = IceInternal.HashUtil.hashAdd(__h, price);
        __h = IceInternal.HashUtil.hashAdd(__h, price2);
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
        __os.writeInt(id);
        __os.writeInt(status);
        item.__write(__os);
        __os.writeInt(coinType);
        __os.writeInt(price);
        __os.writeInt(price2);
        __os.writeInt(discount);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readInt();
        status = __is.readInt();
        item = new IntString();
        item.__read(__is);
        coinType = __is.readInt();
        price = __is.readInt();
        price2 = __is.readInt();
        discount = __is.readInt();
    }

    public static final long serialVersionUID = -705845916L;
}
