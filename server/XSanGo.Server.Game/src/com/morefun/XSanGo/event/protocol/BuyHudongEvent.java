// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuyHudongEvent implements IBuyHudong{
   private IEventDispatcher dispatcher;
   public BuyHudongEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyHudong(int buyCount,int yuanbao) {
      this.dispatcher.emit(IBuyHudong.class,new Object[]{buyCount,yuanbao});
   }

}
