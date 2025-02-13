// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class VipShopItemBuyEvent implements IVipShopItemBuy{
   private IEventDispatcher dispatcher;
   public VipShopItemBuyEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyVipShopItem(java.lang.String code,int count) {
      this.dispatcher.emit(IVipShopItemBuy.class,new Object[]{code,count});
   }

}
