// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuyFundEvent implements IBuyFund{
   private IEventDispatcher dispatcher;
   public BuyFundEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyFund(int price) {
      this.dispatcher.emit(IBuyFund.class,new Object[]{price});
   }

}
