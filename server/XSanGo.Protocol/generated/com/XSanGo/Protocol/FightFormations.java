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
// Generated from file `Tournament.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class FightFormations implements java.lang.Cloneable, java.io.Serializable
{
    public PvpOpponentFormationView myFormation;

    public PvpOpponentFormationView opponentFormation;

    public FightMovieByteView[] movie;

    public int isWin;

    public int remainHero;

    public int winType;

    public FightFormations()
    {
    }

    public FightFormations(PvpOpponentFormationView myFormation, PvpOpponentFormationView opponentFormation, FightMovieByteView[] movie, int isWin, int remainHero, int winType)
    {
        this.myFormation = myFormation;
        this.opponentFormation = opponentFormation;
        this.movie = movie;
        this.isWin = isWin;
        this.remainHero = remainHero;
        this.winType = winType;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FightFormations _r = null;
        if(rhs instanceof FightFormations)
        {
            _r = (FightFormations)rhs;
        }

        if(_r != null)
        {
            if(myFormation != _r.myFormation)
            {
                if(myFormation == null || _r.myFormation == null || !myFormation.equals(_r.myFormation))
                {
                    return false;
                }
            }
            if(opponentFormation != _r.opponentFormation)
            {
                if(opponentFormation == null || _r.opponentFormation == null || !opponentFormation.equals(_r.opponentFormation))
                {
                    return false;
                }
            }
            if(!java.util.Arrays.equals(movie, _r.movie))
            {
                return false;
            }
            if(isWin != _r.isWin)
            {
                return false;
            }
            if(remainHero != _r.remainHero)
            {
                return false;
            }
            if(winType != _r.winType)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::FightFormations");
        __h = IceInternal.HashUtil.hashAdd(__h, myFormation);
        __h = IceInternal.HashUtil.hashAdd(__h, opponentFormation);
        __h = IceInternal.HashUtil.hashAdd(__h, movie);
        __h = IceInternal.HashUtil.hashAdd(__h, isWin);
        __h = IceInternal.HashUtil.hashAdd(__h, remainHero);
        __h = IceInternal.HashUtil.hashAdd(__h, winType);
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
        myFormation.__write(__os);
        opponentFormation.__write(__os);
        FightMovieByteViewSeqHelper.write(__os, movie);
        __os.writeInt(isWin);
        __os.writeInt(remainHero);
        __os.writeInt(winType);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        myFormation = new PvpOpponentFormationView();
        myFormation.__read(__is);
        opponentFormation = new PvpOpponentFormationView();
        opponentFormation.__read(__is);
        movie = FightMovieByteViewSeqHelper.read(__is);
        isWin = __is.readInt();
        remainHero = __is.readInt();
        winType = __is.readInt();
    }

    public static final long serialVersionUID = -889311409L;
}
