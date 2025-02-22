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
// Generated from file `Copy.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class WarmupView implements java.lang.Cloneable, java.io.Serializable
{
    public int winCount;

    public int loseCount;

    public String openDialog;

    public ItemView[] items;

    public WarmupOpponentView opponent;

    public WarmupView()
    {
    }

    public WarmupView(int winCount, int loseCount, String openDialog, ItemView[] items, WarmupOpponentView opponent)
    {
        this.winCount = winCount;
        this.loseCount = loseCount;
        this.openDialog = openDialog;
        this.items = items;
        this.opponent = opponent;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        WarmupView _r = null;
        if(rhs instanceof WarmupView)
        {
            _r = (WarmupView)rhs;
        }

        if(_r != null)
        {
            if(winCount != _r.winCount)
            {
                return false;
            }
            if(loseCount != _r.loseCount)
            {
                return false;
            }
            if(openDialog != _r.openDialog)
            {
                if(openDialog == null || _r.openDialog == null || !openDialog.equals(_r.openDialog))
                {
                    return false;
                }
            }
            if(!java.util.Arrays.equals(items, _r.items))
            {
                return false;
            }
            if(opponent != _r.opponent)
            {
                if(opponent == null || _r.opponent == null || !opponent.equals(_r.opponent))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::WarmupView");
        __h = IceInternal.HashUtil.hashAdd(__h, winCount);
        __h = IceInternal.HashUtil.hashAdd(__h, loseCount);
        __h = IceInternal.HashUtil.hashAdd(__h, openDialog);
        __h = IceInternal.HashUtil.hashAdd(__h, items);
        __h = IceInternal.HashUtil.hashAdd(__h, opponent);
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
        __os.writeInt(winCount);
        __os.writeInt(loseCount);
        __os.writeString(openDialog);
        ItemViewSeqHelper.write(__os, items);
        opponent.__write(__os);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        winCount = __is.readInt();
        loseCount = __is.readInt();
        openDialog = __is.readString();
        items = ItemViewSeqHelper.read(__is);
        opponent = new WarmupOpponentView();
        opponent.__read(__is);
    }

    public static final long serialVersionUID = -603259304L;
}
