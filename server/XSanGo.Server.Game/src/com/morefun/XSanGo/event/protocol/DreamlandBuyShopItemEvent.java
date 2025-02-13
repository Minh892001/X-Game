// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandBuyShopItemEvent implements IDreamlandBuyShopItem{
   private IEventDispatcher dispatcher;
   public DreamlandBuyShopItemEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyShopItem(int id,java.lang.String itemCode,int itemNum) {
      this.dispatcher.emit(IDreamlandBuyShopItem.class,new Object[]{id,itemCode,itemNum});
   }

}
