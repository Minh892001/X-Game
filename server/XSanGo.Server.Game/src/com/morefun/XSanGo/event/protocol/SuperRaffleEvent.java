// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SuperRaffleEvent implements ISuperRaffle{
   private IEventDispatcher dispatcher;
   public SuperRaffleEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptRaffleReward(int oldVal,java.lang.String itemCode,int num,int newVal) {
      this.dispatcher.emit(ISuperRaffle.class,new Object[]{oldVal,itemCode,num,newVal});
   }

}
