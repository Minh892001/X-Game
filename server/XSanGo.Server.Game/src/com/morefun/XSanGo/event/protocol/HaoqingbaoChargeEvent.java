// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HaoqingbaoChargeEvent implements IHaoqingbaoCharge{
   private IEventDispatcher dispatcher;
   public HaoqingbaoChargeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCharge(int num) {
      this.dispatcher.emit(IHaoqingbaoCharge.class,new Object[]{num});
   }

}
