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

public final class CrossServerGMPrxHelper extends Ice.ObjectPrxHelperBase implements CrossServerGMPrx
{
    public static CrossServerGMPrx checkedCast(Ice.ObjectPrx __obj)
    {
        CrossServerGMPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof CrossServerGMPrx)
            {
                __d = (CrossServerGMPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    CrossServerGMPrxHelper __h = new CrossServerGMPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static CrossServerGMPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        CrossServerGMPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof CrossServerGMPrx)
            {
                __d = (CrossServerGMPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    CrossServerGMPrxHelper __h = new CrossServerGMPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static CrossServerGMPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        CrossServerGMPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    CrossServerGMPrxHelper __h = new CrossServerGMPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static CrossServerGMPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        CrossServerGMPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    CrossServerGMPrxHelper __h = new CrossServerGMPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static CrossServerGMPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        CrossServerGMPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof CrossServerGMPrx)
            {
                __d = (CrossServerGMPrx)__obj;
            }
            else
            {
                CrossServerGMPrxHelper __h = new CrossServerGMPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static CrossServerGMPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        CrossServerGMPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            CrossServerGMPrxHelper __h = new CrossServerGMPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::com::XSanGo::Protocol::CrossServerGM"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _CrossServerGMDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _CrossServerGMDelD();
    }

    public static void __write(IceInternal.BasicStream __os, CrossServerGMPrx v)
    {
        __os.writeProxy(v);
    }

    public static CrossServerGMPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            CrossServerGMPrxHelper result = new CrossServerGMPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
