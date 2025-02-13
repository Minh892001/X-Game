// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class Buy10WineByJinbiEvent implements IBuy10WineByJinbi{
   private IEventDispatcher dispatcher;
   public Buy10WineByJinbiEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuy10WineByJinbi(java.util.List<com.XSanGo.Protocol.BuyHeroResult> list) {
      this.dispatcher.emit(IBuy10WineByJinbi.class,new Object[]{list});
   }

}
