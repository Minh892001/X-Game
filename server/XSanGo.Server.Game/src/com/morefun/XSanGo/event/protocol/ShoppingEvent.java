// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ShoppingEvent implements IShopping{
   private IEventDispatcher dispatcher;
   public ShoppingEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onShopping(java.lang.String shopId,java.lang.String templateId,int num,int price,int type) {
      this.dispatcher.emit(IShopping.class,new Object[]{shopId,templateId,num,price,type});
   }

}
