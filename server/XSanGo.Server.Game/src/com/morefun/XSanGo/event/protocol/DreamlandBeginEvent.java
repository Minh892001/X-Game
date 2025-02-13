// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandBeginEvent implements IDreamlandBegin{
   private IEventDispatcher dispatcher;
   public DreamlandBeginEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBeginDreamland(int sceneId,java.lang.String fightMovieIdContext) {
      this.dispatcher.emit(IDreamlandBegin.class,new Object[]{sceneId,fightMovieIdContext});
   }

}
