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
// Generated from file `CrossServer.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * 跨服战报
 **/
public class CrossMovieView implements java.lang.Cloneable, java.io.Serializable
{
    public String winRoleId;

    public int selfHeroNum;

    public SceneDuelView[] soloMovie;

    public byte[] fightMovie;

    public int winType;

    public CrossMovieView()
    {
    }

    public CrossMovieView(String winRoleId, int selfHeroNum, SceneDuelView[] soloMovie, byte[] fightMovie, int winType)
    {
        this.winRoleId = winRoleId;
        this.selfHeroNum = selfHeroNum;
        this.soloMovie = soloMovie;
        this.fightMovie = fightMovie;
        this.winType = winType;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CrossMovieView _r = null;
        if(rhs instanceof CrossMovieView)
        {
            _r = (CrossMovieView)rhs;
        }

        if(_r != null)
        {
            if(winRoleId != _r.winRoleId)
            {
                if(winRoleId == null || _r.winRoleId == null || !winRoleId.equals(_r.winRoleId))
                {
                    return false;
                }
            }
            if(selfHeroNum != _r.selfHeroNum)
            {
                return false;
            }
            if(!java.util.Arrays.equals(soloMovie, _r.soloMovie))
            {
                return false;
            }
            if(!java.util.Arrays.equals(fightMovie, _r.fightMovie))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::CrossMovieView");
        __h = IceInternal.HashUtil.hashAdd(__h, winRoleId);
        __h = IceInternal.HashUtil.hashAdd(__h, selfHeroNum);
        __h = IceInternal.HashUtil.hashAdd(__h, soloMovie);
        __h = IceInternal.HashUtil.hashAdd(__h, fightMovie);
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
        __os.writeString(winRoleId);
        __os.writeInt(selfHeroNum);
        SceneDuelViewSeqHelper.write(__os, soloMovie);
        ByteSeqHelper.write(__os, fightMovie);
        __os.writeInt(winType);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        winRoleId = __is.readString();
        selfHeroNum = __is.readInt();
        soloMovie = SceneDuelViewSeqHelper.read(__is);
        fightMovie = ByteSeqHelper.read(__is);
        winType = __is.readInt();
    }

    public static final long serialVersionUID = -352292980L;
}
