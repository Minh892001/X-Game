// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GetAchieveEvent implements IGetAchieve{
   private IEventDispatcher dispatcher;
   public GetAchieveEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGetAchieve(int achieveId) {
      this.dispatcher.emit(IGetAchieve.class,new Object[]{achieveId});
   }

}
