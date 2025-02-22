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
// Generated from file `Ladder.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class LadderFightResult implements java.lang.Cloneable, java.io.Serializable
{
    public int fightStar;

    public int ladderChangerLevel;

    public int ladderChangerStar;

    public int ladderScore;

    public String movieId;

    public LadderFightResult()
    {
    }

    public LadderFightResult(int fightStar, int ladderChangerLevel, int ladderChangerStar, int ladderScore, String movieId)
    {
        this.fightStar = fightStar;
        this.ladderChangerLevel = ladderChangerLevel;
        this.ladderChangerStar = ladderChangerStar;
        this.ladderScore = ladderScore;
        this.movieId = movieId;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        LadderFightResult _r = null;
        if(rhs instanceof LadderFightResult)
        {
            _r = (LadderFightResult)rhs;
        }

        if(_r != null)
        {
            if(fightStar != _r.fightStar)
            {
                return false;
            }
            if(ladderChangerLevel != _r.ladderChangerLevel)
            {
                return false;
            }
            if(ladderChangerStar != _r.ladderChangerStar)
            {
                return false;
            }
            if(ladderScore != _r.ladderScore)
            {
                return false;
            }
            if(movieId != _r.movieId)
            {
                if(movieId == null || _r.movieId == null || !movieId.equals(_r.movieId))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::LadderFightResult");
        __h = IceInternal.HashUtil.hashAdd(__h, fightStar);
        __h = IceInternal.HashUtil.hashAdd(__h, ladderChangerLevel);
        __h = IceInternal.HashUtil.hashAdd(__h, ladderChangerStar);
        __h = IceInternal.HashUtil.hashAdd(__h, ladderScore);
        __h = IceInternal.HashUtil.hashAdd(__h, movieId);
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
        __os.writeInt(fightStar);
        __os.writeInt(ladderChangerLevel);
        __os.writeInt(ladderChangerStar);
        __os.writeInt(ladderScore);
        __os.writeString(movieId);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        fightStar = __is.readInt();
        ladderChangerLevel = __is.readInt();
        ladderChangerStar = __is.readInt();
        ladderScore = __is.readInt();
        movieId = __is.readString();
    }

    public static final long serialVersionUID = -1603436395L;
}
