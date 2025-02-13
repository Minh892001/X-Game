// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandRefreshShopEvent implements IDreamlandRefreshShop{
   private IEventDispatcher dispatcher;
   public DreamlandRefreshShopEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRefreshDreamlandShop(boolean isFree,int refreshNum,java.lang.String items,java.lang.String newItems) {
      this.dispatcher.emit(IDreamlandRefreshShop.class,new Object[]{isFree,refreshNum,items,newItems});
   }

}
