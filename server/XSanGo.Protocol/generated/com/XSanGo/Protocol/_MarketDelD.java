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
// Generated from file `Market.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public final class _MarketDelD extends Ice._ObjectDelD implements _MarketDel
{
    public String OneInHundred(java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NotEnoughYuanBaoException,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "OneInHundred", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    Market __servant = null;
                    if(__obj == null || __obj instanceof Market)
                    {
                        __servant = (Market)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.OneInHundred(__current);
                        return Ice.DispatchStatus.DispatchOK;
                    }
                    catch(Ice.UserException __ex)
                    {
                        setUserException(__ex);
                        return Ice.DispatchStatus.DispatchUserException;
                    }
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.getServant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
                return __result.value;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(NotEnoughYuanBaoException __ex)
        {
            throw __ex;
        }
        catch(NoteException __ex)
        {
            throw __ex;
        }
        catch(Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(java.lang.Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
        return __result.value;
    }

    public String OneInHundred10(java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NotEnoughYuanBaoException,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "OneInHundred10", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    Market __servant = null;
                    if(__obj == null || __obj instanceof Market)
                    {
                        __servant = (Market)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.OneInHundred10(__current);
                        return Ice.DispatchStatus.DispatchOK;
                    }
                    catch(Ice.UserException __ex)
                    {
                        setUserException(__ex);
                        return Ice.DispatchStatus.DispatchUserException;
                    }
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.getServant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
                return __result.value;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(NotEnoughYuanBaoException __ex)
        {
            throw __ex;
        }
        catch(NoteException __ex)
        {
            throw __ex;
        }
        catch(Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(java.lang.Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
        return __result.value;
    }

    public String OneInTen(java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NotEnoughMoneyException,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "OneInTen", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    Market __servant = null;
                    if(__obj == null || __obj instanceof Market)
                    {
                        __servant = (Market)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.OneInTen(__current);
                        return Ice.DispatchStatus.DispatchOK;
                    }
                    catch(Ice.UserException __ex)
                    {
                        setUserException(__ex);
                        return Ice.DispatchStatus.DispatchUserException;
                    }
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.getServant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
                return __result.value;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(NotEnoughMoneyException __ex)
        {
            throw __ex;
        }
        catch(NoteException __ex)
        {
            throw __ex;
        }
        catch(Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(java.lang.Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
        return __result.value;
    }

    public String OneInTen10(java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NotEnoughMoneyException,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "OneInTen10", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    Market __servant = null;
                    if(__obj == null || __obj instanceof Market)
                    {
                        __servant = (Market)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.OneInTen10(__current);
                        return Ice.DispatchStatus.DispatchOK;
                    }
                    catch(Ice.UserException __ex)
                    {
                        setUserException(__ex);
                        return Ice.DispatchStatus.DispatchUserException;
                    }
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.getServant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
                return __result.value;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(NotEnoughMoneyException __ex)
        {
            throw __ex;
        }
        catch(NoteException __ex)
        {
            throw __ex;
        }
        catch(Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(java.lang.Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
        return __result.value;
    }

    public String buyLimitHero(final int type, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NotEnoughYuanBaoException,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "buyLimitHero", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    Market __servant = null;
                    if(__obj == null || __obj instanceof Market)
                    {
                        __servant = (Market)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.buyLimitHero(type, __current);
                        return Ice.DispatchStatus.DispatchOK;
                    }
                    catch(Ice.UserException __ex)
                    {
                        setUserException(__ex);
                        return Ice.DispatchStatus.DispatchUserException;
                    }
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.getServant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
                return __result.value;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(NotEnoughYuanBaoException __ex)
        {
            throw __ex;
        }
        catch(NoteException __ex)
        {
            throw __ex;
        }
        catch(Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(java.lang.Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
        return __result.value;
    }

    public String getMarketView(java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "getMarketView", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    Market __servant = null;
                    if(__obj == null || __obj instanceof Market)
                    {
                        __servant = (Market)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    __result.value = __servant.getMarketView(__current);
                    return Ice.DispatchStatus.DispatchOK;
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.getServant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
                return __result.value;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(java.lang.Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
        return __result.value;
    }
}
