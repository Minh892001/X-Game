// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TcItemUseEvent implements ITcItemUse{
   private IEventDispatcher dispatcher;
   public TcItemUseEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onItemUse(com.morefun.XSanGo.item.NormalItem item,int count,com.XSanGo.Protocol.ItemView[] items) {
      this.dispatcher.emit(ITcItemUse.class,new Object[]{item,count,items});
   }

}
