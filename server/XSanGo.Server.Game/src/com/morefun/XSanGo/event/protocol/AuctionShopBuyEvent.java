// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AuctionShopBuyEvent implements IAuctionShopBuy{
   private IEventDispatcher dispatcher;
   public AuctionShopBuyEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAuctionShopBuy(int shopId) {
      this.dispatcher.emit(IAuctionShopBuy.class,new Object[]{shopId});
   }

}
