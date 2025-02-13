// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ItemChipCompoundEvent implements IItemChipCompound{
   private IEventDispatcher dispatcher;
   public ItemChipCompoundEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCompound(java.lang.String itemChipId) {
      this.dispatcher.emit(IItemChipCompound.class,new Object[]{itemChipId});
   }

}
