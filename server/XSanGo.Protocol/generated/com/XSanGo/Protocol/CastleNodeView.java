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
// Generated from file `AttackCastle.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class CastleNodeView implements java.lang.Cloneable, java.io.Serializable
{
    public int id;

    public Property[] properties;

    public GrowableProperty[] growableProperties;

    public PvpOpponentFormationView opponent;

    public String movieId;

    public CastleNodeView()
    {
    }

    public CastleNodeView(int id, Property[] properties, GrowableProperty[] growableProperties, PvpOpponentFormationView opponent, String movieId)
    {
        this.id = id;
        this.properties = properties;
        this.growableProperties = growableProperties;
        this.opponent = opponent;
        this.movieId = movieId;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CastleNodeView _r = null;
        if(rhs instanceof CastleNodeView)
        {
            _r = (CastleNodeView)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                return false;
            }
            if(!java.util.Arrays.equals(properties, _r.properties))
            {
                return false;
            }
            if(!java.util.Arrays.equals(growableProperties, _r.growableProperties))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::CastleNodeView");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, properties);
        __h = IceInternal.HashUtil.hashAdd(__h, growableProperties);
        __h = IceInternal.HashUtil.hashAdd(__h, opponent);
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
        __os.writeInt(id);
        PropertySeqHelper.write(__os, properties);
        GrowablePropertySeqHelper.write(__os, growableProperties);
        opponent.__write(__os);
        __os.writeString(movieId);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readInt();
        properties = PropertySeqHelper.read(__is);
        growableProperties = GrowablePropertySeqHelper.read(__is);
        opponent = new PvpOpponentFormationView();
        opponent.__read(__is);
        movieId = __is.readString();
    }

    public static final long serialVersionUID = 624603192L;
}
