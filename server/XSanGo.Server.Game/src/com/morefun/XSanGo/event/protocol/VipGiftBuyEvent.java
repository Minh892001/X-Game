// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class VipGiftBuyEvent implements IVipGiftBuy{
   private IEventDispatcher dispatcher;
   public VipGiftBuyEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onVipGiftBuy(int vipLevel,java.lang.String reward,int num) {
      this.dispatcher.emit(IVipGiftBuy.class,new Object[]{vipLevel,reward,num});
   }

}
