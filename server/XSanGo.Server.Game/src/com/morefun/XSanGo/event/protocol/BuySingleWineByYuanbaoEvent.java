// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuySingleWineByYuanbaoEvent implements IBuySingleWineByYuanbao{
   private IEventDispatcher dispatcher;
   public BuySingleWineByYuanbaoEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyWineByYuanbao(com.XSanGo.Protocol.BuyHeroResult result,boolean isFree) {
      this.dispatcher.emit(IBuySingleWineByYuanbao.class,new Object[]{result,isFree});
   }

}
