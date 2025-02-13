// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AuctionHouseSellEvent implements IAuctionHouseSell{
   private IEventDispatcher dispatcher;
   public AuctionHouseSellEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAuctionHouseSell(java.lang.String sellItemTemplateId,int num,long price,long fixedPrice) {
      this.dispatcher.emit(IAuctionHouseSell.class,new Object[]{sellItemTemplateId,num,price,fixedPrice});
   }

}
