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
// Generated from file `ItemChip.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public final class _ItemChipDelD extends Ice._ObjectDelD implements _ItemChipDel
{
    public String compoundChip(final String itemId, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "compoundChip", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    ItemChip __servant = null;
                    if(__obj == null || __obj instanceof ItemChip)
                    {
                        __servant = (ItemChip)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.compoundChip(itemId, __current);
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

    public String compoundChipWithExtraId(final String itemId, final String extraId, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "compoundChipWithExtraId", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    ItemChip __servant = null;
                    if(__obj == null || __obj instanceof ItemChip)
                    {
                        __servant = (ItemChip)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.compoundChipWithExtraId(itemId, extraId, __current);
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

    public String compoundGem(final String itemId, final int num, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "compoundGem", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    ItemChip __servant = null;
                    if(__obj == null || __obj instanceof ItemChip)
                    {
                        __servant = (ItemChip)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.compoundGem(itemId, num, __current);
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

    public void heroInherit(final String inheritHeroId, final String baseHeroId, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "heroInherit", Ice.OperationMode.Normal, __ctx);
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    ItemChip __servant = null;
                    if(__obj == null || __obj instanceof ItemChip)
                    {
                        __servant = (ItemChip)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __servant.heroInherit(inheritHeroId, baseHeroId, __current);
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
            }
            finally
            {
                __direct.destroy();
            }
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
    }

    public String heroReset(final String heroId, final int isPay, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NotEnoughMoneyException,
               NotEnoughYuanBaoException,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "heroReset", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    ItemChip __servant = null;
                    if(__obj == null || __obj instanceof ItemChip)
                    {
                        __servant = (ItemChip)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.heroReset(heroId, isPay, __current);
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

    public String requestHeroInherit(java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "requestHeroInherit", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    ItemChip __servant = null;
                    if(__obj == null || __obj instanceof ItemChip)
                    {
                        __servant = (ItemChip)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.requestHeroInherit(__current);
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

    public String requestHeroReset(java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "requestHeroReset", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    ItemChip __servant = null;
                    if(__obj == null || __obj instanceof ItemChip)
                    {
                        __servant = (ItemChip)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.requestHeroReset(__current);
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

    public void strutItem(final String itemId, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __observer)
        throws IceInternal.LocalExceptionWrapper,
               NoteException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "strutItem", Ice.OperationMode.Normal, __ctx);
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    ItemChip __servant = null;
                    if(__obj == null || __obj instanceof ItemChip)
                    {
                        __servant = (ItemChip)__obj;
                    }
                    else
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __servant.strutItem(itemId, __current);
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
            }
            finally
            {
                __direct.destroy();
            }
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
    }
}
