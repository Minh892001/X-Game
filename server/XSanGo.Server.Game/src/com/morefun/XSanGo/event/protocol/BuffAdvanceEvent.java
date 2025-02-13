// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuffAdvanceEvent implements IBuffAdvance{
   private IEventDispatcher dispatcher;
   public BuffAdvanceEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuffAdvanceChange(int type,int currentType,java.lang.String useBuffIds) {
      this.dispatcher.emit(IBuffAdvance.class,new Object[]{type,currentType,useBuffIds});
   }

}
