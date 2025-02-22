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
// Generated from file `Task.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public final class taskPrxHelper extends Ice.ObjectPrxHelperBase implements taskPrx
{
    private static final String __finishTask_name = "finishTask";

    public String finishTask(int taskId, int type)
        throws NotEnoughMoneyException,
               NoteException
    {
        return finishTask(taskId, type, null, false);
    }

    public String finishTask(int taskId, int type, java.util.Map<String, String> __ctx)
        throws NotEnoughMoneyException,
               NoteException
    {
        return finishTask(taskId, type, __ctx, true);
    }

    private String finishTask(int taskId, int type, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NotEnoughMoneyException,
               NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "finishTask", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("finishTask");
                    __delBase = __getDelegate(false);
                    _taskDel __del = (_taskDel)__delBase;
                    return __del.finishTask(taskId, type, __ctx, __observer);
                }
                catch(IceInternal.LocalExceptionWrapper __ex)
                {
                    __handleExceptionWrapper(__delBase, __ex, __observer);
                }
                catch(Ice.LocalException __ex)
                {
                    __cnt = __handleException(__delBase, __ex, null, __cnt, __observer);
                }
            }
        }
        finally
        {
            if(__observer != null)
            {
                __observer.detach();
            }
        }
    }

    public Ice.AsyncResult begin_finishTask(int taskId, int type)
    {
        return begin_finishTask(taskId, type, null, false, null);
    }

    public Ice.AsyncResult begin_finishTask(int taskId, int type, java.util.Map<String, String> __ctx)
    {
        return begin_finishTask(taskId, type, __ctx, true, null);
    }

    public Ice.AsyncResult begin_finishTask(int taskId, int type, Ice.Callback __cb)
    {
        return begin_finishTask(taskId, type, null, false, __cb);
    }

    public Ice.AsyncResult begin_finishTask(int taskId, int type, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_finishTask(taskId, type, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_finishTask(int taskId, int type, Callback_task_finishTask __cb)
    {
        return begin_finishTask(taskId, type, null, false, __cb);
    }

    public Ice.AsyncResult begin_finishTask(int taskId, int type, java.util.Map<String, String> __ctx, Callback_task_finishTask __cb)
    {
        return begin_finishTask(taskId, type, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_finishTask(int taskId, int type, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__finishTask_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __finishTask_name, __cb);
        try
        {
            __result.__prepare(__finishTask_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(taskId);
            __os.writeInt(type);
            __result.__endWriteParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_finishTask(Ice.AsyncResult __result)
        throws NotEnoughMoneyException,
               NoteException
    {
        Ice.AsyncResult.__check(__result, this, __finishTask_name);
        boolean __ok = __result.__wait();
        try
        {
            if(!__ok)
            {
                try
                {
                    __result.__throwUserException();
                }
                catch(NotEnoughMoneyException __ex)
                {
                    throw __ex;
                }
                catch(NoteException __ex)
                {
                    throw __ex;
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            IceInternal.BasicStream __is = __result.__startReadParams();
            String __ret;
            __ret = __is.readString();
            __result.__endReadParams();
            return __ret;
        }
        catch(Ice.LocalException ex)
        {
            Ice.Instrumentation.InvocationObserver __obsv = __result.__getObserver();
            if(__obsv != null)
            {
                __obsv.failed(ex.ice_name());
            }
            throw ex;
        }
    }

    public boolean finishTask_async(AMI_task_finishTask __cb, int taskId, int type)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__finishTask_name);
            __r = begin_finishTask(taskId, type, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __finishTask_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean finishTask_async(AMI_task_finishTask __cb, int taskId, int type, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__finishTask_name);
            __r = begin_finishTask(taskId, type, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __finishTask_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __receiveActAward_name = "receiveActAward";

    public void receiveActAward(int awardId)
        throws NoteException
    {
        receiveActAward(awardId, null, false);
    }

    public void receiveActAward(int awardId, java.util.Map<String, String> __ctx)
        throws NoteException
    {
        receiveActAward(awardId, __ctx, true);
    }

    private void receiveActAward(int awardId, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "receiveActAward", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("receiveActAward");
                    __delBase = __getDelegate(false);
                    _taskDel __del = (_taskDel)__delBase;
                    __del.receiveActAward(awardId, __ctx, __observer);
                    return;
                }
                catch(IceInternal.LocalExceptionWrapper __ex)
                {
                    __handleExceptionWrapper(__delBase, __ex, __observer);
                }
                catch(Ice.LocalException __ex)
                {
                    __cnt = __handleException(__delBase, __ex, null, __cnt, __observer);
                }
            }
        }
        finally
        {
            if(__observer != null)
            {
                __observer.detach();
            }
        }
    }

    public Ice.AsyncResult begin_receiveActAward(int awardId)
    {
        return begin_receiveActAward(awardId, null, false, null);
    }

    public Ice.AsyncResult begin_receiveActAward(int awardId, java.util.Map<String, String> __ctx)
    {
        return begin_receiveActAward(awardId, __ctx, true, null);
    }

    public Ice.AsyncResult begin_receiveActAward(int awardId, Ice.Callback __cb)
    {
        return begin_receiveActAward(awardId, null, false, __cb);
    }

    public Ice.AsyncResult begin_receiveActAward(int awardId, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_receiveActAward(awardId, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_receiveActAward(int awardId, Callback_task_receiveActAward __cb)
    {
        return begin_receiveActAward(awardId, null, false, __cb);
    }

    public Ice.AsyncResult begin_receiveActAward(int awardId, java.util.Map<String, String> __ctx, Callback_task_receiveActAward __cb)
    {
        return begin_receiveActAward(awardId, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_receiveActAward(int awardId, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__receiveActAward_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __receiveActAward_name, __cb);
        try
        {
            __result.__prepare(__receiveActAward_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(awardId);
            __result.__endWriteParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public void end_receiveActAward(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __receiveActAward_name);
        boolean __ok = __result.__wait();
        try
        {
            if(!__ok)
            {
                try
                {
                    __result.__throwUserException();
                }
                catch(NoteException __ex)
                {
                    throw __ex;
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            __result.__readEmptyParams();
        }
        catch(Ice.LocalException ex)
        {
            Ice.Instrumentation.InvocationObserver __obsv = __result.__getObserver();
            if(__obsv != null)
            {
                __obsv.failed(ex.ice_name());
            }
            throw ex;
        }
    }

    public boolean receiveActAward_async(AMI_task_receiveActAward __cb, int awardId)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveActAward_name);
            __r = begin_receiveActAward(awardId, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveActAward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean receiveActAward_async(AMI_task_receiveActAward __cb, int awardId, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveActAward_name);
            __r = begin_receiveActAward(awardId, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveActAward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __receiveStarAward_name = "receiveStarAward";

    public void receiveStarAward(int star)
        throws NoteException
    {
        receiveStarAward(star, null, false);
    }

    public void receiveStarAward(int star, java.util.Map<String, String> __ctx)
        throws NoteException
    {
        receiveStarAward(star, __ctx, true);
    }

    private void receiveStarAward(int star, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "receiveStarAward", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("receiveStarAward");
                    __delBase = __getDelegate(false);
                    _taskDel __del = (_taskDel)__delBase;
                    __del.receiveStarAward(star, __ctx, __observer);
                    return;
                }
                catch(IceInternal.LocalExceptionWrapper __ex)
                {
                    __handleExceptionWrapper(__delBase, __ex, __observer);
                }
                catch(Ice.LocalException __ex)
                {
                    __cnt = __handleException(__delBase, __ex, null, __cnt, __observer);
                }
            }
        }
        finally
        {
            if(__observer != null)
            {
                __observer.detach();
            }
        }
    }

    public Ice.AsyncResult begin_receiveStarAward(int star)
    {
        return begin_receiveStarAward(star, null, false, null);
    }

    public Ice.AsyncResult begin_receiveStarAward(int star, java.util.Map<String, String> __ctx)
    {
        return begin_receiveStarAward(star, __ctx, true, null);
    }

    public Ice.AsyncResult begin_receiveStarAward(int star, Ice.Callback __cb)
    {
        return begin_receiveStarAward(star, null, false, __cb);
    }

    public Ice.AsyncResult begin_receiveStarAward(int star, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_receiveStarAward(star, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_receiveStarAward(int star, Callback_task_receiveStarAward __cb)
    {
        return begin_receiveStarAward(star, null, false, __cb);
    }

    public Ice.AsyncResult begin_receiveStarAward(int star, java.util.Map<String, String> __ctx, Callback_task_receiveStarAward __cb)
    {
        return begin_receiveStarAward(star, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_receiveStarAward(int star, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__receiveStarAward_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __receiveStarAward_name, __cb);
        try
        {
            __result.__prepare(__receiveStarAward_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(star);
            __result.__endWriteParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public void end_receiveStarAward(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __receiveStarAward_name);
        boolean __ok = __result.__wait();
        try
        {
            if(!__ok)
            {
                try
                {
                    __result.__throwUserException();
                }
                catch(NoteException __ex)
                {
                    throw __ex;
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            __result.__readEmptyParams();
        }
        catch(Ice.LocalException ex)
        {
            Ice.Instrumentation.InvocationObserver __obsv = __result.__getObserver();
            if(__obsv != null)
            {
                __obsv.failed(ex.ice_name());
            }
            throw ex;
        }
    }

    public boolean receiveStarAward_async(AMI_task_receiveStarAward __cb, int star)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveStarAward_name);
            __r = begin_receiveStarAward(star, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveStarAward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean receiveStarAward_async(AMI_task_receiveStarAward __cb, int star, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveStarAward_name);
            __r = begin_receiveStarAward(star, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveStarAward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __receiveThreeStarAward_name = "receiveThreeStarAward";

    public void receiveThreeStarAward()
        throws NoteException
    {
        receiveThreeStarAward(null, false);
    }

    public void receiveThreeStarAward(java.util.Map<String, String> __ctx)
        throws NoteException
    {
        receiveThreeStarAward(__ctx, true);
    }

    private void receiveThreeStarAward(java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "receiveThreeStarAward", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("receiveThreeStarAward");
                    __delBase = __getDelegate(false);
                    _taskDel __del = (_taskDel)__delBase;
                    __del.receiveThreeStarAward(__ctx, __observer);
                    return;
                }
                catch(IceInternal.LocalExceptionWrapper __ex)
                {
                    __handleExceptionWrapper(__delBase, __ex, __observer);
                }
                catch(Ice.LocalException __ex)
                {
                    __cnt = __handleException(__delBase, __ex, null, __cnt, __observer);
                }
            }
        }
        finally
        {
            if(__observer != null)
            {
                __observer.detach();
            }
        }
    }

    public Ice.AsyncResult begin_receiveThreeStarAward()
    {
        return begin_receiveThreeStarAward(null, false, null);
    }

    public Ice.AsyncResult begin_receiveThreeStarAward(java.util.Map<String, String> __ctx)
    {
        return begin_receiveThreeStarAward(__ctx, true, null);
    }

    public Ice.AsyncResult begin_receiveThreeStarAward(Ice.Callback __cb)
    {
        return begin_receiveThreeStarAward(null, false, __cb);
    }

    public Ice.AsyncResult begin_receiveThreeStarAward(java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_receiveThreeStarAward(__ctx, true, __cb);
    }

    public Ice.AsyncResult begin_receiveThreeStarAward(Callback_task_receiveThreeStarAward __cb)
    {
        return begin_receiveThreeStarAward(null, false, __cb);
    }

    public Ice.AsyncResult begin_receiveThreeStarAward(java.util.Map<String, String> __ctx, Callback_task_receiveThreeStarAward __cb)
    {
        return begin_receiveThreeStarAward(__ctx, true, __cb);
    }

    private Ice.AsyncResult begin_receiveThreeStarAward(java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__receiveThreeStarAward_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __receiveThreeStarAward_name, __cb);
        try
        {
            __result.__prepare(__receiveThreeStarAward_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            __result.__writeEmptyParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public void end_receiveThreeStarAward(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __receiveThreeStarAward_name);
        boolean __ok = __result.__wait();
        try
        {
            if(!__ok)
            {
                try
                {
                    __result.__throwUserException();
                }
                catch(NoteException __ex)
                {
                    throw __ex;
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            __result.__readEmptyParams();
        }
        catch(Ice.LocalException ex)
        {
            Ice.Instrumentation.InvocationObserver __obsv = __result.__getObserver();
            if(__obsv != null)
            {
                __obsv.failed(ex.ice_name());
            }
            throw ex;
        }
    }

    public boolean receiveThreeStarAward_async(AMI_task_receiveThreeStarAward __cb)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveThreeStarAward_name);
            __r = begin_receiveThreeStarAward(null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveThreeStarAward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean receiveThreeStarAward_async(AMI_task_receiveThreeStarAward __cb, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveThreeStarAward_name);
            __r = begin_receiveThreeStarAward(__ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveThreeStarAward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __receiveTodayAward_name = "receiveTodayAward";

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     **/
    public String receiveTodayAward(int index)
        throws NoteException
    {
        return receiveTodayAward(index, null, false);
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __ctx The Context map to send with the invocation.
     **/
    public String receiveTodayAward(int index, java.util.Map<String, String> __ctx)
        throws NoteException
    {
        return receiveTodayAward(index, __ctx, true);
    }

    private String receiveTodayAward(int index, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "receiveTodayAward", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("receiveTodayAward");
                    __delBase = __getDelegate(false);
                    _taskDel __del = (_taskDel)__delBase;
                    return __del.receiveTodayAward(index, __ctx, __observer);
                }
                catch(IceInternal.LocalExceptionWrapper __ex)
                {
                    __handleExceptionWrapper(__delBase, __ex, __observer);
                }
                catch(Ice.LocalException __ex)
                {
                    __cnt = __handleException(__delBase, __ex, null, __cnt, __observer);
                }
            }
        }
        finally
        {
            if(__observer != null)
            {
                __observer.detach();
            }
        }
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __cb The callback object for the operation.
     **/
    public Ice.AsyncResult begin_receiveTodayAward(int index)
    {
        return begin_receiveTodayAward(index, null, false, null);
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public Ice.AsyncResult begin_receiveTodayAward(int index, java.util.Map<String, String> __ctx)
    {
        return begin_receiveTodayAward(index, __ctx, true, null);
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __cb The callback object for the operation.
     **/
    public Ice.AsyncResult begin_receiveTodayAward(int index, Ice.Callback __cb)
    {
        return begin_receiveTodayAward(index, null, false, __cb);
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public Ice.AsyncResult begin_receiveTodayAward(int index, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_receiveTodayAward(index, __ctx, true, __cb);
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __cb The callback object for the operation.
     **/
    public Ice.AsyncResult begin_receiveTodayAward(int index, Callback_task_receiveTodayAward __cb)
    {
        return begin_receiveTodayAward(index, null, false, __cb);
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public Ice.AsyncResult begin_receiveTodayAward(int index, java.util.Map<String, String> __ctx, Callback_task_receiveTodayAward __cb)
    {
        return begin_receiveTodayAward(index, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_receiveTodayAward(int index, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__receiveTodayAward_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __receiveTodayAward_name, __cb);
        try
        {
            __result.__prepare(__receiveTodayAward_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(index);
            __result.__endWriteParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    /**
     * ice_response indicates that
     * the operation completed successfully.
     **/
    public String end_receiveTodayAward(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __receiveTodayAward_name);
        boolean __ok = __result.__wait();
        try
        {
            if(!__ok)
            {
                try
                {
                    __result.__throwUserException();
                }
                catch(NoteException __ex)
                {
                    throw __ex;
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            IceInternal.BasicStream __is = __result.__startReadParams();
            String __ret;
            __ret = __is.readString();
            __result.__endReadParams();
            return __ret;
        }
        catch(Ice.LocalException ex)
        {
            Ice.Instrumentation.InvocationObserver __obsv = __result.__getObserver();
            if(__obsv != null)
            {
                __obsv.failed(ex.ice_name());
            }
            throw ex;
        }
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __cb The callback object for the operation.
     **/
    public boolean receiveTodayAward_async(AMI_task_receiveTodayAward __cb, int index)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveTodayAward_name);
            __r = begin_receiveTodayAward(index, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveTodayAward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    /**
     * 七日目标-领取每日目标奖励, 返回 SevenTargetView
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean receiveTodayAward_async(AMI_task_receiveTodayAward __cb, int index, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveTodayAward_name);
            __r = begin_receiveTodayAward(index, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveTodayAward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __selectTask_name = "selectTask";

    public String selectTask()
        throws NoteException
    {
        return selectTask(null, false);
    }

    public String selectTask(java.util.Map<String, String> __ctx)
        throws NoteException
    {
        return selectTask(__ctx, true);
    }

    private String selectTask(java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "selectTask", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("selectTask");
                    __delBase = __getDelegate(false);
                    _taskDel __del = (_taskDel)__delBase;
                    return __del.selectTask(__ctx, __observer);
                }
                catch(IceInternal.LocalExceptionWrapper __ex)
                {
                    __handleExceptionWrapper(__delBase, __ex, __observer);
                }
                catch(Ice.LocalException __ex)
                {
                    __cnt = __handleException(__delBase, __ex, null, __cnt, __observer);
                }
            }
        }
        finally
        {
            if(__observer != null)
            {
                __observer.detach();
            }
        }
    }

    public Ice.AsyncResult begin_selectTask()
    {
        return begin_selectTask(null, false, null);
    }

    public Ice.AsyncResult begin_selectTask(java.util.Map<String, String> __ctx)
    {
        return begin_selectTask(__ctx, true, null);
    }

    public Ice.AsyncResult begin_selectTask(Ice.Callback __cb)
    {
        return begin_selectTask(null, false, __cb);
    }

    public Ice.AsyncResult begin_selectTask(java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_selectTask(__ctx, true, __cb);
    }

    public Ice.AsyncResult begin_selectTask(Callback_task_selectTask __cb)
    {
        return begin_selectTask(null, false, __cb);
    }

    public Ice.AsyncResult begin_selectTask(java.util.Map<String, String> __ctx, Callback_task_selectTask __cb)
    {
        return begin_selectTask(__ctx, true, __cb);
    }

    private Ice.AsyncResult begin_selectTask(java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__selectTask_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __selectTask_name, __cb);
        try
        {
            __result.__prepare(__selectTask_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            __result.__writeEmptyParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_selectTask(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __selectTask_name);
        boolean __ok = __result.__wait();
        try
        {
            if(!__ok)
            {
                try
                {
                    __result.__throwUserException();
                }
                catch(NoteException __ex)
                {
                    throw __ex;
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            IceInternal.BasicStream __is = __result.__startReadParams();
            String __ret;
            __ret = __is.readString();
            __result.__endReadParams();
            return __ret;
        }
        catch(Ice.LocalException ex)
        {
            Ice.Instrumentation.InvocationObserver __obsv = __result.__getObserver();
            if(__obsv != null)
            {
                __obsv.failed(ex.ice_name());
            }
            throw ex;
        }
    }

    public boolean selectTask_async(AMI_task_selectTask __cb)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__selectTask_name);
            __r = begin_selectTask(null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __selectTask_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean selectTask_async(AMI_task_selectTask __cb, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__selectTask_name);
            __r = begin_selectTask(__ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __selectTask_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public static taskPrx checkedCast(Ice.ObjectPrx __obj)
    {
        taskPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof taskPrx)
            {
                __d = (taskPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    taskPrxHelper __h = new taskPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static taskPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        taskPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof taskPrx)
            {
                __d = (taskPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    taskPrxHelper __h = new taskPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static taskPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        taskPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    taskPrxHelper __h = new taskPrxHelper();
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

    public static taskPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        taskPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    taskPrxHelper __h = new taskPrxHelper();
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

    public static taskPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        taskPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof taskPrx)
            {
                __d = (taskPrx)__obj;
            }
            else
            {
                taskPrxHelper __h = new taskPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static taskPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        taskPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            taskPrxHelper __h = new taskPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::com::XSanGo::Protocol::task"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _taskDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _taskDelD();
    }

    public static void __write(IceInternal.BasicStream __os, taskPrx v)
    {
        __os.writeProxy(v);
    }

    public static taskPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            taskPrxHelper result = new taskPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
