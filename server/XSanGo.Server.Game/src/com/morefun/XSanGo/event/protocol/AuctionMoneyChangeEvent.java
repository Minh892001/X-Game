// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AuctionMoneyChangeEvent implements IAuctionMoneyChange{
   private IEventDispatcher dispatcher;
   public AuctionMoneyChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAuctionMoneyChange(long change) {
      this.dispatcher.emit(IAuctionMoneyChange.class,new Object[]{change});
   }

}
