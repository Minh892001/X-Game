// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class LadderBuyEvent implements ILadderBuy{
   private IEventDispatcher dispatcher;
   public LadderBuyEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuy(int id,int sumNum) {
      this.dispatcher.emit(ILadderBuy.class,new Object[]{id,sumNum});
   }

}
