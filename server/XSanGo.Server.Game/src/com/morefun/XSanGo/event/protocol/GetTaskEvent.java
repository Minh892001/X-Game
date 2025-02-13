// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GetTaskEvent implements IGetTask{
   private IEventDispatcher dispatcher;
   public GetTaskEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGetTask(int taskId,int actBefore,int actAfter,int change) {
      this.dispatcher.emit(IGetTask.class,new Object[]{taskId,actBefore,actAfter,change});
   }

}
