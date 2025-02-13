// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ItemChipStrutEvent implements IItemChipStrut{
   private IEventDispatcher dispatcher;
   public ItemChipStrutEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onStrut(java.lang.String itemId) {
      this.dispatcher.emit(IItemChipStrut.class,new Object[]{itemId});
   }

}
