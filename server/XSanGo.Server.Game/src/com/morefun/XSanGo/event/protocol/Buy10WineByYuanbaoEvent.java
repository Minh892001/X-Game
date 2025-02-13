// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class Buy10WineByYuanbaoEvent implements IBuy10WineByYuanbao{
   private IEventDispatcher dispatcher;
   public Buy10WineByYuanbaoEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuy10WineByYuanbao(java.util.List<com.XSanGo.Protocol.BuyHeroResult> list) {
      this.dispatcher.emit(IBuy10WineByYuanbao.class,new Object[]{list});
   }

}
