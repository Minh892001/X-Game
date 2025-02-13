// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuyFactionShopEvent implements IBuyFactionShop{
   private IEventDispatcher dispatcher;
   public BuyFactionShopEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyFactionShop(int id) {
      this.dispatcher.emit(IBuyFactionShop.class,new Object[]{id});
   }

}
