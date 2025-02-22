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
// Generated from file `ArenaRank.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class FightResult implements java.lang.Cloneable, java.io.Serializable
{
    public int maxNum;

    public int historyRank;

    public int maxRank;

    public int firstWinNum;

    public int firsChangeRank;

    public int sneerNum;

    public int sneerhangeRank;

    public int fightStar;

    public String reportMovieId;

    public FightResult()
    {
    }

    public FightResult(int maxNum, int historyRank, int maxRank, int firstWinNum, int firsChangeRank, int sneerNum, int sneerhangeRank, int fightStar, String reportMovieId)
    {
        this.maxNum = maxNum;
        this.historyRank = historyRank;
        this.maxRank = maxRank;
        this.firstWinNum = firstWinNum;
        this.firsChangeRank = firsChangeRank;
        this.sneerNum = sneerNum;
        this.sneerhangeRank = sneerhangeRank;
        this.fightStar = fightStar;
        this.reportMovieId = reportMovieId;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FightResult _r = null;
        if(rhs instanceof FightResult)
        {
            _r = (FightResult)rhs;
        }

        if(_r != null)
        {
            if(maxNum != _r.maxNum)
            {
                return false;
            }
            if(historyRank != _r.historyRank)
            {
                return false;
            }
            if(maxRank != _r.maxRank)
            {
                return false;
            }
            if(firstWinNum != _r.firstWinNum)
            {
                return false;
            }
            if(firsChangeRank != _r.firsChangeRank)
            {
                return false;
            }
            if(sneerNum != _r.sneerNum)
            {
                return false;
            }
            if(sneerhangeRank != _r.sneerhangeRank)
            {
                return false;
            }
            if(fightStar != _r.fightStar)
            {
                return false;
            }
            if(reportMovieId != _r.reportMovieId)
            {
                if(reportMovieId == null || _r.reportMovieId == null || !reportMovieId.equals(_r.reportMovieId))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::FightResult");
        __h = IceInternal.HashUtil.hashAdd(__h, maxNum);
        __h = IceInternal.HashUtil.hashAdd(__h, historyRank);
        __h = IceInternal.HashUtil.hashAdd(__h, maxRank);
        __h = IceInternal.HashUtil.hashAdd(__h, firstWinNum);
        __h = IceInternal.HashUtil.hashAdd(__h, firsChangeRank);
        __h = IceInternal.HashUtil.hashAdd(__h, sneerNum);
        __h = IceInternal.HashUtil.hashAdd(__h, sneerhangeRank);
        __h = IceInternal.HashUtil.hashAdd(__h, fightStar);
        __h = IceInternal.HashUtil.hashAdd(__h, reportMovieId);
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
        __os.writeInt(maxNum);
        __os.writeInt(historyRank);
        __os.writeInt(maxRank);
        __os.writeInt(firstWinNum);
        __os.writeInt(firsChangeRank);
        __os.writeInt(sneerNum);
        __os.writeInt(sneerhangeRank);
        __os.writeInt(fightStar);
        __os.writeString(reportMovieId);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        maxNum = __is.readInt();
        historyRank = __is.readInt();
        maxRank = __is.readInt();
        firstWinNum = __is.readInt();
        firsChangeRank = __is.readInt();
        sneerNum = __is.readInt();
        sneerhangeRank = __is.readInt();
        fightStar = __is.readInt();
        reportMovieId = __is.readString();
    }

    public static final long serialVersionUID = -774131230L;
}
