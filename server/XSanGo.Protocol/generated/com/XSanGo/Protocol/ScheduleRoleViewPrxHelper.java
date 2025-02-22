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
 * 淘汰赛玩家信息
 **/
public final class ScheduleRoleViewPrxHelper extends Ice.ObjectPrxHelperBase implements ScheduleRoleViewPrx
{
    public static ScheduleRoleViewPrx checkedCast(Ice.ObjectPrx __obj)
    {
        ScheduleRoleViewPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof ScheduleRoleViewPrx)
            {
                __d = (ScheduleRoleViewPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    ScheduleRoleViewPrxHelper __h = new ScheduleRoleViewPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ScheduleRoleViewPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ScheduleRoleViewPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof ScheduleRoleViewPrx)
            {
                __d = (ScheduleRoleViewPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    ScheduleRoleViewPrxHelper __h = new ScheduleRoleViewPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ScheduleRoleViewPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ScheduleRoleViewPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    ScheduleRoleViewPrxHelper __h = new ScheduleRoleViewPrxHelper();
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

    public static ScheduleRoleViewPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ScheduleRoleViewPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    ScheduleRoleViewPrxHelper __h = new ScheduleRoleViewPrxHelper();
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

    public static ScheduleRoleViewPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        ScheduleRoleViewPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof ScheduleRoleViewPrx)
            {
                __d = (ScheduleRoleViewPrx)__obj;
            }
            else
            {
                ScheduleRoleViewPrxHelper __h = new ScheduleRoleViewPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ScheduleRoleViewPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ScheduleRoleViewPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ScheduleRoleViewPrxHelper __h = new ScheduleRoleViewPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::com::XSanGo::Protocol::ScheduleRoleView"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _ScheduleRoleViewDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _ScheduleRoleViewDelD();
    }

    public static void __write(IceInternal.BasicStream __os, ScheduleRoleViewPrx v)
    {
        __os.writeProxy(v);
    }

    public static ScheduleRoleViewPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ScheduleRoleViewPrxHelper result = new ScheduleRoleViewPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
