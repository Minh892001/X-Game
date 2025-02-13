// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TraderItemBuyEvent implements ITraderItemBuy{
   private IEventDispatcher dispatcher;
   public TraderItemBuyEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onItemBuy(com.XSanGo.Protocol.CommodityView item,com.morefun.XSanGo.trader.TraderType traderType) {
      this.dispatcher.emit(ITraderItemBuy.class,new Object[]{item,traderType});
   }

}
