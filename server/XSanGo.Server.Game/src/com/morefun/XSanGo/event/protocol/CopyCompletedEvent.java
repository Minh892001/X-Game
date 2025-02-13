// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CopyCompletedEvent implements ICopyCompleted{
   private IEventDispatcher dispatcher;
   public CopyCompletedEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCopyCompleted(com.morefun.XSanGo.copy.SmallCopyT templete,int star,boolean firstPass,int fightPower,int junling) {
      this.dispatcher.emit(ICopyCompleted.class,new Object[]{templete,star,firstPass,fightPower,junling});
   }

}
