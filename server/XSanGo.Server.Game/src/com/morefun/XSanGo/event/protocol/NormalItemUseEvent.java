// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class NormalItemUseEvent implements INormalItemUse{
   private IEventDispatcher dispatcher;
   public NormalItemUseEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onItemUse(com.morefun.XSanGo.item.NormalItem item,int count,int realCount) {
      this.dispatcher.emit(INormalItemUse.class,new Object[]{item,count,realCount});
   }

}
