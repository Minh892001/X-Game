// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GetShareAwardEvent implements IGetShareAward{
   private IEventDispatcher dispatcher;
   public GetShareAwardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGetAward(int taskId) {
      this.dispatcher.emit(IGetShareAward.class,new Object[]{taskId});
   }

}
