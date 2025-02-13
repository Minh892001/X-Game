// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuyJinbiEvent implements IBuyJinbi{
   private IEventDispatcher dispatcher;
   public BuyJinbiEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onbuy(int num,int crit,int jinbi) {
      this.dispatcher.emit(IBuyJinbi.class,new Object[]{num,crit,jinbi});
   }

}
