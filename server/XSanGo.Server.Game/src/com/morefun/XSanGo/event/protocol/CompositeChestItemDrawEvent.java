// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CompositeChestItemDrawEvent implements ICompositeChestItemDraw{
   private IEventDispatcher dispatcher;
   public CompositeChestItemDrawEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void doDrawCompositeChestItem(int index,java.lang.String itemId,java.lang.String itemCode,java.lang.String gainCode,int gainNum) {
      this.dispatcher.emit(ICompositeChestItemDraw.class,new Object[]{index,itemId,itemCode,gainCode,gainNum});
   }

}
