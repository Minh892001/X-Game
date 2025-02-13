// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AuctionHouseSettleEvent implements IAuctionHouseSettle{
   private IEventDispatcher dispatcher;
   public AuctionHouseSettleEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAuctionHouseSettle(java.lang.String sellerId,java.lang.String bidderId,java.lang.String templateId,int num,long price,int type,int success) {
      this.dispatcher.emit(IAuctionHouseSettle.class,new Object[]{sellerId,bidderId,templateId,num,price,type,success});
   }

}
