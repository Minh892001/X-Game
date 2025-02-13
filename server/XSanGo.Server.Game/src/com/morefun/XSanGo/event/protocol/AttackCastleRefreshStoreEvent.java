// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AttackCastleRefreshStoreEvent implements IAttackCastleRefreshStore{
   private IEventDispatcher dispatcher;
   public AttackCastleRefreshStoreEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRefreshStore(int count,int price) {
      this.dispatcher.emit(IAttackCastleRefreshStore.class,new Object[]{count,price});
   }

}
