// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuySingleWineByJinbiEvent implements IBuySingleWineByJinbi{
   private IEventDispatcher dispatcher;
   public BuySingleWineByJinbiEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyWineByJinbi(com.XSanGo.Protocol.BuyHeroResult result,boolean isFree) {
      this.dispatcher.emit(IBuySingleWineByJinbi.class,new Object[]{result,isFree});
   }

}
