// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GemSetEvent implements IGemSet{
   private IEventDispatcher dispatcher;
   public GemSetEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSetGem(java.lang.String templateId,int level) {
      this.dispatcher.emit(IGemSet.class,new Object[]{templateId,level});
   }

}
