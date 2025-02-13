// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AuctionHouseBuyEvent implements IAuctionHouseBuy{
   private IEventDispatcher dispatcher;
   public AuctionHouseBuyEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAuctionHouseBuy(java.lang.String buyItemTemplateId,int type,long price) {
      this.dispatcher.emit(IAuctionHouseBuy.class,new Object[]{buyItemTemplateId,type,price});
   }

}
