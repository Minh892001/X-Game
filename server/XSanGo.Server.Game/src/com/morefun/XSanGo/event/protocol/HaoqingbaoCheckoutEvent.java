// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HaoqingbaoCheckoutEvent implements IHaoqingbaoCheckout{
   private IEventDispatcher dispatcher;
   public HaoqingbaoCheckoutEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCheckout(int num) {
      this.dispatcher.emit(IHaoqingbaoCheckout.class,new Object[]{num});
   }

}
