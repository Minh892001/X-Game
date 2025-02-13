// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GetSevenTaskEvent implements IGetSevenTask{
   private IEventDispatcher dispatcher;
   public GetSevenTaskEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGetSevenTask(int type,int id) {
      this.dispatcher.emit(IGetSevenTask.class,new Object[]{type,id});
   }

}
