// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AttackCastleExchangeEvent implements IAttackCastleExchange{
   private IEventDispatcher dispatcher;
   public AttackCastleExchangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onExchange(com.XSanGo.Protocol.AttackCastleShopItemView item) {
      this.dispatcher.emit(IAttackCastleExchange.class,new Object[]{item});
   }

}
