// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandSweepEvent implements IDreamlandSweep{
   private IEventDispatcher dispatcher;
   public DreamlandSweepEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSweepDreamland(int sceneId,int challengeNum,java.lang.String items) {
      this.dispatcher.emit(IDreamlandSweep.class,new Object[]{sceneId,challengeNum,items});
   }

}
