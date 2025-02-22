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

public class LotteryScoreRankView implements java.lang.Cloneable, java.io.Serializable
{
    public LotteryScoreRankSub[] lotteryRankList;

    public int myRank;

    public int myScore;

    public long sendAwardLastTime;

    public LotteryScoreRankView()
    {
    }

    public LotteryScoreRankView(LotteryScoreRankSub[] lotteryRankList, int myRank, int myScore, long sendAwardLastTime)
    {
        this.lotteryRankList = lotteryRankList;
        this.myRank = myRank;
        this.myScore = myScore;
        this.sendAwardLastTime = sendAwardLastTime;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        LotteryScoreRankView _r = null;
        if(rhs instanceof LotteryScoreRankView)
        {
            _r = (LotteryScoreRankView)rhs;
        }

        if(_r != null)
        {
            if(!java.util.Arrays.equals(lotteryRankList, _r.lotteryRankList))
            {
                return false;
            }
            if(myRank != _r.myRank)
            {
                return false;
            }
            if(myScore != _r.myScore)
            {
                return false;
            }
            if(sendAwardLastTime != _r.sendAwardLastTime)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::LotteryScoreRankView");
        __h = IceInternal.HashUtil.hashAdd(__h, lotteryRankList);
        __h = IceInternal.HashUtil.hashAdd(__h, myRank);
        __h = IceInternal.HashUtil.hashAdd(__h, myScore);
        __h = IceInternal.HashUtil.hashAdd(__h, sendAwardLastTime);
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
        LotteryScoreRankSubSeqHelper.write(__os, lotteryRankList);
        __os.writeInt(myRank);
        __os.writeInt(myScore);
        __os.writeLong(sendAwardLastTime);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        lotteryRankList = LotteryScoreRankSubSeqHelper.read(__is);
        myRank = __is.readInt();
        myScore = __is.readInt();
        sendAwardLastTime = __is.readLong();
    }

    public static final long serialVersionUID = -601750005L;
}
