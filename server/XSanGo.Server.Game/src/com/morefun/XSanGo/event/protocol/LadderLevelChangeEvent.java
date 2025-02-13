// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class LadderLevelChangeEvent implements ILadderLevelChange{
   private IEventDispatcher dispatcher;
   public LadderLevelChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onLevelChange(int level) {
      this.dispatcher.emit(ILadderLevelChange.class,new Object[]{level});
   }

}
