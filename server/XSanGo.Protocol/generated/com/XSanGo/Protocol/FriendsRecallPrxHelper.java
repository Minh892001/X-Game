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
// Generated from file `FriendsRecall.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public final class FriendsRecallPrxHelper extends Ice.ObjectPrxHelperBase implements FriendsRecallPrx
{
    private static final String __activeInvitationCode_name = "activeInvitationCode";

    public String activeInvitationCode(String code)
        throws NoteException
    {
        return activeInvitationCode(code, null, false);
    }

    public String activeInvitationCode(String code, java.util.Map<String, String> __ctx)
        throws NoteException
    {
        return activeInvitationCode(code, __ctx, true);
    }

    private String activeInvitationCode(String code, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "activeInvitationCode", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("activeInvitationCode");
                    __delBase = __getDelegate(false);
                    _FriendsRecallDel __del = (_FriendsRecallDel)__delBase;
                    return __del.activeInvitationCode(code, __ctx, __observer);
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

    public Ice.AsyncResult begin_activeInvitationCode(String code)
    {
        return begin_activeInvitationCode(code, null, false, null);
    }

    public Ice.AsyncResult begin_activeInvitationCode(String code, java.util.Map<String, String> __ctx)
    {
        return begin_activeInvitationCode(code, __ctx, true, null);
    }

    public Ice.AsyncResult begin_activeInvitationCode(String code, Ice.Callback __cb)
    {
        return begin_activeInvitationCode(code, null, false, __cb);
    }

    public Ice.AsyncResult begin_activeInvitationCode(String code, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_activeInvitationCode(code, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_activeInvitationCode(String code, Callback_FriendsRecall_activeInvitationCode __cb)
    {
        return begin_activeInvitationCode(code, null, false, __cb);
    }

    public Ice.AsyncResult begin_activeInvitationCode(String code, java.util.Map<String, String> __ctx, Callback_FriendsRecall_activeInvitationCode __cb)
    {
        return begin_activeInvitationCode(code, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_activeInvitationCode(String code, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__activeInvitationCode_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __activeInvitationCode_name, __cb);
        try
        {
            __result.__prepare(__activeInvitationCode_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeString(code);
            __result.__endWriteParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_activeInvitationCode(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __activeInvitationCode_name);
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

    public boolean activeInvitationCode_async(AMI_FriendsRecall_activeInvitationCode __cb, String code)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__activeInvitationCode_name);
            __r = begin_activeInvitationCode(code, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __activeInvitationCode_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean activeInvitationCode_async(AMI_FriendsRecall_activeInvitationCode __cb, String code, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__activeInvitationCode_name);
            __r = begin_activeInvitationCode(code, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __activeInvitationCode_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __openInvitation_name = "openInvitation";

    public String openInvitation()
        throws NoteException
    {
        return openInvitation(null, false);
    }

    public String openInvitation(java.util.Map<String, String> __ctx)
        throws NoteException
    {
        return openInvitation(__ctx, true);
    }

    private String openInvitation(java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "openInvitation", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("openInvitation");
                    __delBase = __getDelegate(false);
                    _FriendsRecallDel __del = (_FriendsRecallDel)__delBase;
                    return __del.openInvitation(__ctx, __observer);
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

    public Ice.AsyncResult begin_openInvitation()
    {
        return begin_openInvitation(null, false, null);
    }

    public Ice.AsyncResult begin_openInvitation(java.util.Map<String, String> __ctx)
    {
        return begin_openInvitation(__ctx, true, null);
    }

    public Ice.AsyncResult begin_openInvitation(Ice.Callback __cb)
    {
        return begin_openInvitation(null, false, __cb);
    }

    public Ice.AsyncResult begin_openInvitation(java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_openInvitation(__ctx, true, __cb);
    }

    public Ice.AsyncResult begin_openInvitation(Callback_FriendsRecall_openInvitation __cb)
    {
        return begin_openInvitation(null, false, __cb);
    }

    public Ice.AsyncResult begin_openInvitation(java.util.Map<String, String> __ctx, Callback_FriendsRecall_openInvitation __cb)
    {
        return begin_openInvitation(__ctx, true, __cb);
    }

    private Ice.AsyncResult begin_openInvitation(java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__openInvitation_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __openInvitation_name, __cb);
        try
        {
            __result.__prepare(__openInvitation_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            __result.__writeEmptyParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_openInvitation(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __openInvitation_name);
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

    public boolean openInvitation_async(AMI_FriendsRecall_openInvitation __cb)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__openInvitation_name);
            __r = begin_openInvitation(null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __openInvitation_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean openInvitation_async(AMI_FriendsRecall_openInvitation __cb, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__openInvitation_name);
            __r = begin_openInvitation(__ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __openInvitation_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __openRecall_name = "openRecall";

    public String openRecall()
        throws NoteException
    {
        return openRecall(null, false);
    }

    public String openRecall(java.util.Map<String, String> __ctx)
        throws NoteException
    {
        return openRecall(__ctx, true);
    }

    private String openRecall(java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "openRecall", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("openRecall");
                    __delBase = __getDelegate(false);
                    _FriendsRecallDel __del = (_FriendsRecallDel)__delBase;
                    return __del.openRecall(__ctx, __observer);
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

    public Ice.AsyncResult begin_openRecall()
    {
        return begin_openRecall(null, false, null);
    }

    public Ice.AsyncResult begin_openRecall(java.util.Map<String, String> __ctx)
    {
        return begin_openRecall(__ctx, true, null);
    }

    public Ice.AsyncResult begin_openRecall(Ice.Callback __cb)
    {
        return begin_openRecall(null, false, __cb);
    }

    public Ice.AsyncResult begin_openRecall(java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_openRecall(__ctx, true, __cb);
    }

    public Ice.AsyncResult begin_openRecall(Callback_FriendsRecall_openRecall __cb)
    {
        return begin_openRecall(null, false, __cb);
    }

    public Ice.AsyncResult begin_openRecall(java.util.Map<String, String> __ctx, Callback_FriendsRecall_openRecall __cb)
    {
        return begin_openRecall(__ctx, true, __cb);
    }

    private Ice.AsyncResult begin_openRecall(java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__openRecall_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __openRecall_name, __cb);
        try
        {
            __result.__prepare(__openRecall_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            __result.__writeEmptyParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_openRecall(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __openRecall_name);
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

    public boolean openRecall_async(AMI_FriendsRecall_openRecall __cb)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__openRecall_name);
            __r = begin_openRecall(null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __openRecall_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean openRecall_async(AMI_FriendsRecall_openRecall __cb, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__openRecall_name);
            __r = begin_openRecall(__ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __openRecall_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __randomOfflineRole_name = "randomOfflineRole";

    public String randomOfflineRole(String currOfflineRoleId)
        throws NoteException
    {
        return randomOfflineRole(currOfflineRoleId, null, false);
    }

    public String randomOfflineRole(String currOfflineRoleId, java.util.Map<String, String> __ctx)
        throws NoteException
    {
        return randomOfflineRole(currOfflineRoleId, __ctx, true);
    }

    private String randomOfflineRole(String currOfflineRoleId, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "randomOfflineRole", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("randomOfflineRole");
                    __delBase = __getDelegate(false);
                    _FriendsRecallDel __del = (_FriendsRecallDel)__delBase;
                    return __del.randomOfflineRole(currOfflineRoleId, __ctx, __observer);
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

    public Ice.AsyncResult begin_randomOfflineRole(String currOfflineRoleId)
    {
        return begin_randomOfflineRole(currOfflineRoleId, null, false, null);
    }

    public Ice.AsyncResult begin_randomOfflineRole(String currOfflineRoleId, java.util.Map<String, String> __ctx)
    {
        return begin_randomOfflineRole(currOfflineRoleId, __ctx, true, null);
    }

    public Ice.AsyncResult begin_randomOfflineRole(String currOfflineRoleId, Ice.Callback __cb)
    {
        return begin_randomOfflineRole(currOfflineRoleId, null, false, __cb);
    }

    public Ice.AsyncResult begin_randomOfflineRole(String currOfflineRoleId, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_randomOfflineRole(currOfflineRoleId, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_randomOfflineRole(String currOfflineRoleId, Callback_FriendsRecall_randomOfflineRole __cb)
    {
        return begin_randomOfflineRole(currOfflineRoleId, null, false, __cb);
    }

    public Ice.AsyncResult begin_randomOfflineRole(String currOfflineRoleId, java.util.Map<String, String> __ctx, Callback_FriendsRecall_randomOfflineRole __cb)
    {
        return begin_randomOfflineRole(currOfflineRoleId, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_randomOfflineRole(String currOfflineRoleId, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__randomOfflineRole_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __randomOfflineRole_name, __cb);
        try
        {
            __result.__prepare(__randomOfflineRole_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeString(currOfflineRoleId);
            __result.__endWriteParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_randomOfflineRole(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __randomOfflineRole_name);
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

    public boolean randomOfflineRole_async(AMI_FriendsRecall_randomOfflineRole __cb, String currOfflineRoleId)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__randomOfflineRole_name);
            __r = begin_randomOfflineRole(currOfflineRoleId, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __randomOfflineRole_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean randomOfflineRole_async(AMI_FriendsRecall_randomOfflineRole __cb, String currOfflineRoleId, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__randomOfflineRole_name);
            __r = begin_randomOfflineRole(currOfflineRoleId, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __randomOfflineRole_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __receiveTaskReward_name = "receiveTaskReward";

    public String receiveTaskReward(int taskId)
        throws NotEnoughMoneyException,
               NoteException
    {
        return receiveTaskReward(taskId, null, false);
    }

    public String receiveTaskReward(int taskId, java.util.Map<String, String> __ctx)
        throws NotEnoughMoneyException,
               NoteException
    {
        return receiveTaskReward(taskId, __ctx, true);
    }

    private String receiveTaskReward(int taskId, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NotEnoughMoneyException,
               NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "receiveTaskReward", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("receiveTaskReward");
                    __delBase = __getDelegate(false);
                    _FriendsRecallDel __del = (_FriendsRecallDel)__delBase;
                    return __del.receiveTaskReward(taskId, __ctx, __observer);
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

    public Ice.AsyncResult begin_receiveTaskReward(int taskId)
    {
        return begin_receiveTaskReward(taskId, null, false, null);
    }

    public Ice.AsyncResult begin_receiveTaskReward(int taskId, java.util.Map<String, String> __ctx)
    {
        return begin_receiveTaskReward(taskId, __ctx, true, null);
    }

    public Ice.AsyncResult begin_receiveTaskReward(int taskId, Ice.Callback __cb)
    {
        return begin_receiveTaskReward(taskId, null, false, __cb);
    }

    public Ice.AsyncResult begin_receiveTaskReward(int taskId, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_receiveTaskReward(taskId, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_receiveTaskReward(int taskId, Callback_FriendsRecall_receiveTaskReward __cb)
    {
        return begin_receiveTaskReward(taskId, null, false, __cb);
    }

    public Ice.AsyncResult begin_receiveTaskReward(int taskId, java.util.Map<String, String> __ctx, Callback_FriendsRecall_receiveTaskReward __cb)
    {
        return begin_receiveTaskReward(taskId, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_receiveTaskReward(int taskId, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__receiveTaskReward_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __receiveTaskReward_name, __cb);
        try
        {
            __result.__prepare(__receiveTaskReward_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(taskId);
            __result.__endWriteParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_receiveTaskReward(Ice.AsyncResult __result)
        throws NotEnoughMoneyException,
               NoteException
    {
        Ice.AsyncResult.__check(__result, this, __receiveTaskReward_name);
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

    public boolean receiveTaskReward_async(AMI_FriendsRecall_receiveTaskReward __cb, int taskId)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveTaskReward_name);
            __r = begin_receiveTaskReward(taskId, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveTaskReward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean receiveTaskReward_async(AMI_FriendsRecall_receiveTaskReward __cb, int taskId, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__receiveTaskReward_name);
            __r = begin_receiveTaskReward(taskId, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __receiveTaskReward_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    private static final String __showFriendsRecallIcon_name = "showFriendsRecallIcon";

    public String showFriendsRecallIcon()
        throws NoteException
    {
        return showFriendsRecallIcon(null, false);
    }

    public String showFriendsRecallIcon(java.util.Map<String, String> __ctx)
        throws NoteException
    {
        return showFriendsRecallIcon(__ctx, true);
    }

    private String showFriendsRecallIcon(java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws NoteException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "showFriendsRecallIcon", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("showFriendsRecallIcon");
                    __delBase = __getDelegate(false);
                    _FriendsRecallDel __del = (_FriendsRecallDel)__delBase;
                    return __del.showFriendsRecallIcon(__ctx, __observer);
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

    public Ice.AsyncResult begin_showFriendsRecallIcon()
    {
        return begin_showFriendsRecallIcon(null, false, null);
    }

    public Ice.AsyncResult begin_showFriendsRecallIcon(java.util.Map<String, String> __ctx)
    {
        return begin_showFriendsRecallIcon(__ctx, true, null);
    }

    public Ice.AsyncResult begin_showFriendsRecallIcon(Ice.Callback __cb)
    {
        return begin_showFriendsRecallIcon(null, false, __cb);
    }

    public Ice.AsyncResult begin_showFriendsRecallIcon(java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_showFriendsRecallIcon(__ctx, true, __cb);
    }

    public Ice.AsyncResult begin_showFriendsRecallIcon(Callback_FriendsRecall_showFriendsRecallIcon __cb)
    {
        return begin_showFriendsRecallIcon(null, false, __cb);
    }

    public Ice.AsyncResult begin_showFriendsRecallIcon(java.util.Map<String, String> __ctx, Callback_FriendsRecall_showFriendsRecallIcon __cb)
    {
        return begin_showFriendsRecallIcon(__ctx, true, __cb);
    }

    private Ice.AsyncResult begin_showFriendsRecallIcon(java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__showFriendsRecallIcon_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __showFriendsRecallIcon_name, __cb);
        try
        {
            __result.__prepare(__showFriendsRecallIcon_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            __result.__writeEmptyParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_showFriendsRecallIcon(Ice.AsyncResult __result)
        throws NoteException
    {
        Ice.AsyncResult.__check(__result, this, __showFriendsRecallIcon_name);
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

    public boolean showFriendsRecallIcon_async(AMI_FriendsRecall_showFriendsRecallIcon __cb)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__showFriendsRecallIcon_name);
            __r = begin_showFriendsRecallIcon(null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __showFriendsRecallIcon_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean showFriendsRecallIcon_async(AMI_FriendsRecall_showFriendsRecallIcon __cb, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__showFriendsRecallIcon_name);
            __r = begin_showFriendsRecallIcon(__ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __showFriendsRecallIcon_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public static FriendsRecallPrx checkedCast(Ice.ObjectPrx __obj)
    {
        FriendsRecallPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof FriendsRecallPrx)
            {
                __d = (FriendsRecallPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    FriendsRecallPrxHelper __h = new FriendsRecallPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static FriendsRecallPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        FriendsRecallPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof FriendsRecallPrx)
            {
                __d = (FriendsRecallPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    FriendsRecallPrxHelper __h = new FriendsRecallPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static FriendsRecallPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        FriendsRecallPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    FriendsRecallPrxHelper __h = new FriendsRecallPrxHelper();
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

    public static FriendsRecallPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        FriendsRecallPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    FriendsRecallPrxHelper __h = new FriendsRecallPrxHelper();
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

    public static FriendsRecallPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        FriendsRecallPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof FriendsRecallPrx)
            {
                __d = (FriendsRecallPrx)__obj;
            }
            else
            {
                FriendsRecallPrxHelper __h = new FriendsRecallPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static FriendsRecallPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        FriendsRecallPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            FriendsRecallPrxHelper __h = new FriendsRecallPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::com::XSanGo::Protocol::FriendsRecall"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _FriendsRecallDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _FriendsRecallDelD();
    }

    public static void __write(IceInternal.BasicStream __os, FriendsRecallPrx v)
    {
        __os.writeProxy(v);
    }

    public static FriendsRecallPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            FriendsRecallPrxHelper result = new FriendsRecallPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
