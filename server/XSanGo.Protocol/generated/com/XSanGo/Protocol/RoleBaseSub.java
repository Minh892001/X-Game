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

public class RoleBaseSub implements java.lang.Cloneable, java.io.Serializable
{
    public int score;

    public int rank;

    public long leftTime;

    public int freeNum;

    public int curGrid;

    public int coinType;

    public int price;

    public int autoNeedNum;

    public int throwNum;

    public int autoNum;

    public int tips;

    public RoleBaseSub()
    {
    }

    public RoleBaseSub(int score, int rank, long leftTime, int freeNum, int curGrid, int coinType, int price, int autoNeedNum, int throwNum, int autoNum, int tips)
    {
        this.score = score;
        this.rank = rank;
        this.leftTime = leftTime;
        this.freeNum = freeNum;
        this.curGrid = curGrid;
        this.coinType = coinType;
        this.price = price;
        this.autoNeedNum = autoNeedNum;
        this.throwNum = throwNum;
        this.autoNum = autoNum;
        this.tips = tips;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        RoleBaseSub _r = null;
        if(rhs instanceof RoleBaseSub)
        {
            _r = (RoleBaseSub)rhs;
        }

        if(_r != null)
        {
            if(score != _r.score)
            {
                return false;
            }
            if(rank != _r.rank)
            {
                return false;
            }
            if(leftTime != _r.leftTime)
            {
                return false;
            }
            if(freeNum != _r.freeNum)
            {
                return false;
            }
            if(curGrid != _r.curGrid)
            {
                return false;
            }
            if(coinType != _r.coinType)
            {
                return false;
            }
            if(price != _r.price)
            {
                return false;
            }
            if(autoNeedNum != _r.autoNeedNum)
            {
                return false;
            }
            if(throwNum != _r.throwNum)
            {
                return false;
            }
            if(autoNum != _r.autoNum)
            {
                return false;
            }
            if(tips != _r.tips)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::RoleBaseSub");
        __h = IceInternal.HashUtil.hashAdd(__h, score);
        __h = IceInternal.HashUtil.hashAdd(__h, rank);
        __h = IceInternal.HashUtil.hashAdd(__h, leftTime);
        __h = IceInternal.HashUtil.hashAdd(__h, freeNum);
        __h = IceInternal.HashUtil.hashAdd(__h, curGrid);
        __h = IceInternal.HashUtil.hashAdd(__h, coinType);
        __h = IceInternal.HashUtil.hashAdd(__h, price);
        __h = IceInternal.HashUtil.hashAdd(__h, autoNeedNum);
        __h = IceInternal.HashUtil.hashAdd(__h, throwNum);
        __h = IceInternal.HashUtil.hashAdd(__h, autoNum);
        __h = IceInternal.HashUtil.hashAdd(__h, tips);
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
        __os.writeInt(score);
        __os.writeInt(rank);
        __os.writeLong(leftTime);
        __os.writeInt(freeNum);
        __os.writeInt(curGrid);
        __os.writeInt(coinType);
        __os.writeInt(price);
        __os.writeInt(autoNeedNum);
        __os.writeInt(throwNum);
        __os.writeInt(autoNum);
        __os.writeInt(tips);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        score = __is.readInt();
        rank = __is.readInt();
        leftTime = __is.readLong();
        freeNum = __is.readInt();
        curGrid = __is.readInt();
        coinType = __is.readInt();
        price = __is.readInt();
        autoNeedNum = __is.readInt();
        throwNum = __is.readInt();
        autoNum = __is.readInt();
        tips = __is.readInt();
    }

    public static final long serialVersionUID = -174754506L;
}
