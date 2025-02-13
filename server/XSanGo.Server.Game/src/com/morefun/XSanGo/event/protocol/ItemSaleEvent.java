// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ItemSaleEvent implements IItemSale{
   private IEventDispatcher dispatcher;
   public ItemSaleEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onItemSaled(com.morefun.XSanGo.item.IItem item,int count,int money) {
      this.dispatcher.emit(IItemSale.class,new Object[]{item,count,money});
   }

}
